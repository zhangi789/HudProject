package com.shdnxc.cn.activity.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.adapter.DeviceAdapter;
import com.shdnxc.cn.activity.bean.DeviceBean;
import com.shdnxc.cn.activity.dbDao.DeviceDao;
import com.shdnxc.cn.activity.zxing.camera.CameraManager;
import com.shdnxc.cn.activity.zxing.decoding.CaptureActivityHandler;
import com.shdnxc.cn.activity.zxing.decoding.InactivityTimer;
import com.shdnxc.cn.activity.zxing.view.ViewfinderView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CameraActivity extends Activity implements Callback, OnClickListener, PermissionListener {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private RelativeLayout re_btn_res;
    private EditText mEtHudId;
    private Button mBtnSendHudInfo;

    private Button btn_auto_conn;

    private RelativeLayout re_camere_scan;

    private DeviceDao mDevicesDb;

    private ListView lv_devices;

    private Button btn_auto;

    private ArrayList<DeviceBean> mDeviceLists = new ArrayList<>();
    /**
     * 扫描权限
     */
    // 用户取消后重新提醒
    private static final int REQUEST_CODE_SETTING = 300;
    //请求权限
    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;
    //浏览权限
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private String snAddress;
    private String token;
    private boolean isSann;

    Message message = Message.obtain();

    private static Boolean isAppExit = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        CameraManager.init(getApplication());
        isSann = BaseUtils.isNeedPerssion();

        if (!AndPermission.hasPermission(this, Manifest.permission.CAMERA)) {
            AndPermission.with(this)
                    .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                    .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE)
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                    .rationale((requestCode, rationale) ->
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            AndPermission.rationaleDialog(CameraActivity.this, rationale).show()
                    )
                    .send();
        }

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        re_btn_res = (RelativeLayout) findViewById(R.id.re_btn_res);
        re_btn_res.setOnClickListener(this);
        mDevicesDb = new DeviceDao(this);
        mEtHudId = (EditText) findViewById(R.id.edit_hud_id);
        mBtnSendHudInfo = (Button) findViewById(R.id.btn_add_hud);
        btn_auto_conn = (Button) findViewById(R.id.btn_auto_conn);
        btn_auto_conn.setOnClickListener(this);
      /*  btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_auto.setOnClickListener(this);*/
        mBtnSendHudInfo.setOnClickListener(this);
        lv_devices = (ListView) findViewById(R.id.lv_device_info);
        re_camere_scan = (RelativeLayout) findViewById(R.id.re_camere_scan);
        ImageView mButtonBack = (ImageView) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new OnClickListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CameraActivity.this, LoginActivity.class));
                ActivityAnimation.explodeToSlide(CameraActivity.this, LoginActivity.class);
                CameraActivity.this.finish();
            }
        });

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        lv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mInfo = mEtHudId.getText().toString().trim();
                mInfo = mInfo.toUpperCase();
                if (mInfo != null && mInfo.length() == 8 && mInfo.startsWith("AA")) {
                    String mLoginInfo = mInfo + mDeviceLists.get(position).getmDeviceAddressInfo();
                    mEtHudId.setText(mLoginInfo);
                } else {
                    Toast.makeText(CameraActivity.this, "前八位橙色输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mDeviceLists.clear();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(CameraActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            addHUDiD(resultString);
        }


    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

          /*  case R.id.btn_auto:
                mDeviceLists.clear();
                re_camere_scan.setVisibility(View.INVISIBLE);
                btn_auto_conn.setVisibility(View.VISIBLE);
                btn_auto.setVisibility(View.INVISIBLE);
                break;*/
            case R.id.btn_auto_conn:
             /*   btn_auto.setVisibility(View.VISIBLE);*/
                btn_auto_conn.setVisibility(View.INVISIBLE);
                List<String> recordsList = mDevicesDb.getRecordsList();
                if (recordsList.size() != 0) {
                    for (int i = recordsList.size() - 1; i >= 0; i--) {
                        mDeviceLists.add(new DeviceBean(recordsList.get(i)));
                        DeviceAdapter adapter = new DeviceAdapter(CameraActivity.this, mDeviceLists);
                        lv_devices.setAdapter(adapter);
                        lv_devices.setSelection(0);
                        lv_devices.setDivider(null);
                    }
                }
                re_camere_scan.setVisibility(View.VISIBLE);


                break;
            case R.id.re_btn_res:
                CameraActivity.this.finish();
                break;
            case R.id.btn_add_hud:
                String etInfo = mEtHudId.getText().toString().trim();
                etInfo = etInfo.toUpperCase();
                if (!etInfo.equals("") && etInfo.startsWith("AA") && etInfo.length() == 20) {
                    addHUDiD(etInfo);
                } else {
                    Toast.makeText(CameraActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
//        Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_OTHER:
                Toast.makeText(this, "权限申请失败请重新请求", Toast.LENGTH_SHORT).show();

                break;
        }
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            //  AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

            //第二种：用自定义的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle("权限申请失败")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         * @param listener PermissionListener 对象。
         */
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 监听back键退出应用
     */
  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppExit();
        }
        return false;
    }

    public void AppExit() {
        if (!isAppExit) {
            isAppExit = true;
            mHander.sendEmptyMessageDelayed(111, 2000);
        } else {// 2s内再次按back时,isExit= true，执行以下操作，app退出

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

        }

    }
*/
    private void addHUDiD(String id) {
        token = SharedUtils.getString(CameraActivity.this, ConstantUils.CU_LOGIN, "0");
        snAddress = SharedUtils.getString(CameraActivity.this, ConstantUils.CU_SPLASH, "0");
        if (!snAddress.equals(id))
            OkGo.post(WeiYunURL.MAC_BIND)//
                    .tag(this)//
                    .isMultipart(true)
                    .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                    .params("sn", id)
                    .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                    .params("token", token)  // 这里可以上传参数
                    // 可以添加文件上传
                    .execute(new StringCallback() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("CameraActivity", "onSuccess: " + s);
                            if (s.contains("400")) {
                                Toast.makeText(CameraActivity.this, "绑定失败  重新扫描", Toast.LENGTH_LONG).show();
                            } else if (s.contains("200")) {
                                boolean hasRecord = mDevicesDb.isHasRecord(id);
                                Log.i("====", hasRecord + "-----------------------------------------------------------");
                                if (!hasRecord) {
                                    mDevicesDb.addRecords(id.substring(8, id.length()));
                                }
                                SharedUtils.saveString(CameraActivity.this, ConstantUils.CU_SPLASH, id);
                                startActivity(new Intent(CameraActivity.this, MainActivity.class));
                                btn_auto_conn.setVisibility(View.VISIBLE
                                );
//                              ActivityAnimation.explodeToSlide(CameraActivity.this, MainActivity.class);
                                CameraActivity.this.finish();
                            }
                        }
                    });

    }


}