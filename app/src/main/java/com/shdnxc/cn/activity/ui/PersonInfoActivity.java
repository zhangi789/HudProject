package com.shdnxc.cn.activity.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BitmapUtils;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.FileUtils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/4/1.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {
    private ImageView mBack;
    //  标题
    private TextView tv_title;
    private RoundImageView mCricle;
    String iphoneName;
    //  手机号
    private TextView mIphone;
    private TextView tv_sex_data;
    //备注名称 F   性别  地址
    private RelativeLayout mReBeiMark, mReSex, mReCity, mReQianName;
    //  备注名称  性别   地址
    private TextView mRemark, mSex, mDdress;
    private String mImageAddress;
    //  控制Pop显示的
    private RelativeLayout mRePop;
    private TextView mPopName;
    private EditText mPopContent, mPopContent2;
    private TextView btn_cancle;
    private TextView btn_sure, btn_sure2;
    private RelativeLayout re_show_sex;
    private TextView tv_cancle, tv_sure;
    private RelativeLayout re_man, re_serect, re_nv;

    private TextView tv_nan, tv_nv, tv_serect;

    String mSexMan, mSexNv, mSexSerect;


    private TextView btn_cancle_sex, btn_sure_sex;
    String sexDate;

    private RelativeLayout re_shop;

    private static final int PERMISSIONS_REQUEST_PHOTO = 0x01;
    private static final int PERMISSIONS_REQUEST_FILE = 0x02;

    private static final int REQUEST_CODE_TAKING_PHOTO = 0x03;
    private static final int REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL = 0x04;
    private static final int REQUEST_CODE_CUT_PHOTO = 0x05;
    private String mPath;
    private String[] mItems = {"拍照上传", "选择图片"};
    private boolean isTakePhoto = false;
    private boolean isGetPic = false;
    private static final int TARGET_HEAD_SIZE = 150;
    private static final String IMAGE_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/yulin/photo";
    private static final String IMAGE_SAVE_PATH = IMAGE_SAVE_DIR + "/demo.jpg";
    private Uri mUri;
    //拍照   图片
    private TextView mTakePhote, mSelectPhote;
    private TextView mPhoteCancle, mPhotoSure;
    private String token;


    private String getCheckData;

    private RelativeLayout re_select_photo;

    //用户头像
    private String mUserPicUrl = "";
    //用户昵称

    private String mUserNiCheng = "";
    //用户性别

    private String mUserSex = "";
    //所在城市
    private String mUserCityInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_sett);
        token = SharedUtils.getString(PersonInfoActivity.this, ConstantUils.CU_LOGIN, "0");
        //2017 427  添加的
        mUserPicUrl = SharedUtils.getString(PersonInfoActivity.this, ConstantUils.USER_HEAD_PIC, "0");
        mUserNiCheng = SharedUtils.getString(PersonInfoActivity.this, ConstantUils.USER_REALNAME, "YES");

        mUserCityInfo = SharedUtils.getString(PersonInfoActivity.this, ConstantUils.USER_ADDRESS_CITY, "YES");
        Log.i("JKS", token + " token");
        iphoneName = SharedUtils.getString(PersonInfoActivity.this, ConstantUils.PERSON_PHONE, "0");
        initviews();
        if (savedInstanceState != null) mPath = savedInstanceState.getString("Path");
        File file = new File(IMAGE_SAVE_DIR);
        if (!file.exists()) file.mkdirs();
        tv_title.setText("资料设置");
        if (!iphoneName.equals("0")) {
            mIphone.setText(iphoneName);
        }
        if (mUserPicUrl.length() > 10) {
            Picasso.with(PersonInfoActivity.this).load(mUserPicUrl).placeholder(R.drawable.default_portrait).into(mCricle);
        }
        if (!mUserNiCheng.equals("YES")) {
            mRemark.setText(mUserNiCheng);
        } else {
            mRemark.setText("添加备注");
        }

        if (!mUserCityInfo.equals("YES")) {
            mDdress.setText(mUserCityInfo);
        } else {
            mDdress.setText("所在城市");
        }
        initLenster();
    }

    @Override
    protected void onResume() {
        mUserSex = SharedUtils.getString(PersonInfoActivity.this, ConstantUils.USER_SEX, "3");
        Log.i("JKS", mUserSex + " mUserSex");
        if (!mUserSex.equals("3")) {
            if (mUserSex.equals("1")) {
                tv_sex_data.setText("男");
            } else if (mUserSex.equals("0")) {
                tv_sex_data.setText("保密");
            } else if (mUserSex.equals("2")) {
                tv_sex_data.setText("女");
            }
        }


        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Path", mPath);
    }

    private void initLenster() {
        re_shop.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mReBeiMark.setOnClickListener(this);
        mReSex.setOnClickListener(this);
        mReCity.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_sure2.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        mReQianName.setOnClickListener(this);
        re_serect.setOnClickListener(this);
        re_nv.setOnClickListener(this);
        re_man.setOnClickListener(this);
        btn_sure_sex.setOnClickListener(this);
        btn_cancle_sex.setOnClickListener(this);
        mTakePhote.setOnClickListener(this);
        mSelectPhote.setOnClickListener(this);
        mPhoteCancle.setOnClickListener(this);
        mPhotoSure.setOnClickListener(this);
    }

    private void initviews() {
        re_select_photo = (RelativeLayout) findViewById(R.id.re_select_photo);
        mPhotoSure = (TextView) findViewById(R.id.sure_tv);
        mPhoteCancle = (TextView) findViewById(R.id.cancle_tv);
        mSelectPhote = (TextView) findViewById(R.id.select_phote);
        mTakePhote = (TextView) findViewById(R.id.take_photo);
        re_shop = (RelativeLayout) findViewById(R.id.re_shop);
        btn_cancle_sex = (TextView) findViewById(R.id.btn_cancle_sex);
        btn_sure_sex = (TextView) findViewById(R.id.btn_sure_sex);
        tv_sex_data = (TextView) findViewById(R.id.tv_sex_data);
        tv_nan = (TextView) findViewById(R.id.tv_man);
        tv_nv = (TextView) findViewById(R.id.tv_nv);
        tv_serect = (TextView) findViewById(R.id.tv_serect);
        re_serect = (RelativeLayout) findViewById(R.id.re_serect);
        re_nv = (RelativeLayout) findViewById(R.id.re_nv);
        re_man = (RelativeLayout) findViewById(R.id.re_man);

        re_show_sex = (RelativeLayout) findViewById(R.id.re_show_sex);
        mPopContent2 = (EditText) findViewById(R.id.tv_pop_content2);
        mReQianName = (RelativeLayout) findViewById(R.id.re_person_name);
        btn_sure = (TextView) findViewById(R.id.btn_name);
        btn_sure2 = (TextView) findViewById(R.id.btn_name2);
        btn_cancle = (TextView) findViewById(R.id.btn_cancle);
        mPopContent = (EditText) findViewById(R.id.tv_pop_content);
        mPopName = (TextView) findViewById(R.id.tv_pop_name);
        mRePop = (RelativeLayout) findViewById(R.id.re_pop_show);
        mCricle = (RoundImageView) findViewById(R.id.mcricle);
        mSex = (TextView) findViewById(R.id.tv_sex);
        mDdress = (TextView) findViewById(R.id.tv_address_data);
        mReCity = (RelativeLayout) findViewById(R.id.re_address);
        mReSex = (RelativeLayout) findViewById(R.id.re_sex);
        mReBeiMark = (RelativeLayout) findViewById(R.id.re_car_name);
        mRemark = (TextView) findViewById(R.id.tv_phote_data);
        mIphone = (TextView) findViewById(R.id.tv_ipnoe_name);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.take_photo:
                getCheckData = mItems[0];
                break;
            case R.id.select_phote:
                getCheckData = mItems[1];
                break;

            case R.id.cancle_tv:
                re_select_photo.setVisibility(View.INVISIBLE);
                break;
            case R.id.sure_tv:
                if (getCheckData.equals(mItems[0])) {
                    operTakePhoto();
                } else {
                    operChoosePic();
                }
                re_select_photo.setVisibility(View.INVISIBLE);
                break;
            case R.id.re_shop:
                re_select_photo.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_sure_sex:

                if (sexDate != null) {
                    if (sexDate.equals("男")) {
                        OkGo.post(WeiYunURL.UPDATE_USER_INFO).isMultipart(true)
                                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                .params("token", token)
                                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                                .params("gender", "1")
                                // 可以添加文件上传
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        try {
                                            JSONObject json = new JSONObject(s);
                                            String status = json.getString("status");
                                            if (status.equals("200")) {
                                                SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.USER_SEX, "1");
                                                Log.i("JKS", sexDate + " 提交成功");
                                                tv_sex_data.setText(sexDate);
                                            } else {
                                                Toast.makeText(PersonInfoActivity.this, "错误码" + status, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else if (sexDate.equals("女")) {
                        OkGo.post(WeiYunURL.UPDATE_USER_INFO).isMultipart(true)
                                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                .params("token", token)
                                .params("gender", "2")
                                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                                // 可以添加文件上传
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {

                                        try {
                                            JSONObject json = new JSONObject(s);
                                            String status = json.getString("status");
                                            if (status.equals("200")) {
                                                SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.USER_SEX, "2");
                                                tv_sex_data.setText(sexDate);
                                                Log.i("JKS", sexDate + " 提交成功");
                                            } else {
                                                Toast.makeText(PersonInfoActivity.this, "错误码" + status, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else if (sexDate.equals("保密")) {
                        OkGo.post(WeiYunURL.UPDATE_USER_INFO).isMultipart(true)
                                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                .params("token", token)
                                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                                .params("gender", "0")
                                // 可以添加文件上传
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        try {
                                            JSONObject json = new JSONObject(s);
                                            String status = json.getString("status");
                                            if (status.equals("200")) {
                                                SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.USER_SEX, "0");
                                                tv_sex_data.setText("保密");
                                                Log.i("JKS", sexDate + " 提交成功");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                }
                re_show_sex.setVisibility(View.INVISIBLE);


                break;
            case R.id.btn_cancle_sex:
                re_show_sex.setVisibility(View.INVISIBLE);
                break;
            //保密
            case R.id.re_serect:
                re_nv.setBackgroundColor(Color.rgb(255, 255, 255));
                re_man.setBackgroundColor(Color.rgb(255, 255, 255));
                re_serect.setBackgroundColor(Color.rgb(175, 187, 205));
                mSexSerect = tv_serect.getText().toString();
                sexDate = mSexSerect;
                break;
            //女
            case R.id.re_nv:
                re_serect.setBackgroundColor(Color.rgb(255, 255, 255));
                re_man.setBackgroundColor(Color.rgb(255, 255, 255));
                re_nv.setBackgroundColor(Color.rgb(175, 187, 205));
                mSexNv = tv_nv.getText().toString();
                sexDate = mSexNv;
                break;
            //男
            case R.id.re_man:
                re_serect.setBackgroundColor(Color.rgb(255, 255, 255));
                re_nv.setBackgroundColor(Color.rgb(255, 255, 255));
                re_man.setBackgroundColor(Color.rgb(175, 187, 205));

                mSexMan = tv_nan.getText().toString();
                sexDate = mSexMan;
                break;
            case R.id.iv_find_back:
                // startActivity(new Intent(PersonInfoActivity.this, MainActivity.class));
                PersonInfoActivity.this.finish();
                break;
            //备注名称
            case R.id.re_car_name:
                btn_sure.setVisibility(View.VISIBLE);
                btn_sure2.setVisibility(View.INVISIBLE);
                mPopContent.setVisibility(View.VISIBLE);
                mPopContent2.setVisibility(View.INVISIBLE);
                mPopName.setText("昵称");
                mRePop.setVisibility(View.VISIBLE);
                break;
            //性别
            case R.id.re_sex:
                re_show_sex.setVisibility(View.VISIBLE);
                break;
            //  地址
            case R.id.re_address:

                Log.i("JKS", "地址");
                startActivity(new Intent(PersonInfoActivity.this, SelectAddActivity.class));
                PersonInfoActivity.this.finish();
                break;
            case R.id.re_person_name:
                btn_sure.setVisibility(View.INVISIBLE);
                btn_sure2.setVisibility(View.VISIBLE);
                mPopContent.setVisibility(View.INVISIBLE);
                mPopContent2.setVisibility(View.VISIBLE);
                mPopName.setText("个性签名");
                mRePop.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_name:
                String niName = mPopContent.getText().toString();


                Log.i("JKS", niName);
                if (niName.length() >= 1) {
                  /*  SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.PERSON_NI_NAME, niName);
                    mRemark.setText(niName);*/
                    OkGo.post(WeiYunURL.UPDATE_USER_INFO).isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("token", token)
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                            .params("realname", niName)
                            // 可以添加文件上传
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject json = new JSONObject(s);
                                        String status = json.getString("status");
                                        if (status.equals("200")) {
                                            Log.i("JKS", niName + " 提交成功");
                                            SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.USER_REALNAME, niName);
                                            mRemark.setText(niName);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                } else {
                    Toast.makeText(PersonInfoActivity.this, "不能为空哦", Toast.LENGTH_SHORT).show();
                }
                mRePop.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_name2:
                String charName = mPopContent2.getText().toString();
                if (charName.length() >= 1) {
                    if (charName.length() > 20) {
                        charName = charName.substring(0, 20);
                    } else {
                        charName = charName;
                    }
                    SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.PERSON_ChaCTER_NAME, charName);
                } else {
                    Toast.makeText(PersonInfoActivity.this, "不能为空哦", Toast.LENGTH_SHORT).show();
                }
                mRePop.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_cancle:
                mRePop.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 拍照操作
     */
    private void operTakePhoto() {
        isTakePhoto = true;
        isGetPic = false;
        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PersonInfoActivity.this, Manifest.permission.CAMERA))
                showPhotoPerDialog();
            else
                ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO);
        } else takePhoto();
    }

    /**
     * 选择图片操作
     */
    private void operChoosePic() {
        isTakePhoto = false;
        isGetPic = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showFilePerDialog();
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
        } else getPictureFromLocal();
    }

    /**
     * 拍照权限提示
     */
    private void showPhotoPerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要获取访问您的相机权限，以确保您可以正常拍照。")
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO)).create().show();
    }

    /**
     * 文件权限提示
     */
    private void showFilePerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要获取存储文件权限，以确保可以正常保存拍摄或选取的图片。")
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSIONS_REQUEST_FILE)).create().show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        String mUUID = UUID.randomUUID().toString();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mPath = FileUtils.getStorageDirectory() + mUUID;
        File file = new File(mPath + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CODE_TAKING_PHOTO);
    }

    /**
     * 从本地选择图片
     */
    private void getPictureFromLocal() {
        Intent innerIntent =
                new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL);
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            //   intent.putExtra("return-data", true);
            mUri = Uri.parse("file:///" + IMAGE_SAVE_PATH);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理拍照结果
     */
    private void dealTakePhotoWithoutZoom() {
        String finalPath = mPath + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(finalPath);
        if (bitmap != null) {
            boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
            if (b) {
                mCricle.setImageBitmap(BitmapFactory.decodeFile(IMAGE_SAVE_PATH));
            }
        }

    }

    /**
     * 处理拍照并剪裁
     * <p>
     * 逻辑先服务器上传在处裁剪逻辑
     */
    private void dealTakePhotoThenZoom() {
    /*    Bundle data = new Bundle();
        data.putInt(HandlerUtil.Take_PHOTE_DATA,12);
        HandlerUtil.handleMsg(mHandler, HandlerUtil.Take_PHOTE, data);*/
        File file = new File(mPath + ".jpg");
        startPhotoZoom(Uri.fromFile(file), TARGET_HEAD_SIZE);
    }

    /**
     * 处理选择图片结果
     */
    private void dealChoosePhotoWithoutZoom(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            Bitmap bitmap = BitmapUtils.uriToBitmap(this, uri);
            if (bitmap != null) {
                mCricle.setImageBitmap(bitmap);
                boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);

            }
        }
    }

    /**
     * 处理选择图片并剪裁
     */
    private void dealChoosePhotoThenZoom(Intent data) {

        Uri uri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
            }
            startPhotoZoom(Uri.fromFile(new File(IMAGE_SAVE_PATH)), TARGET_HEAD_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理剪裁图片的结果
     */
    private void dealZoomPhoto() {
        try {
            if (mUri != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
                if (bitmap != null) {
                    //mImg.setImageBitmap(bitmap);
                    boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
                    Log.i("ASP", b + " " + IMAGE_SAVE_PATH);
                    if (b) {
                        OkGo.post(WeiYunURL.MAC_IMG_UPLOAD)//
                                .tag(this)//
                                .isMultipart(true)
                                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                .params("token", token)
                                .params("files", new File(IMAGE_SAVE_PATH))
                                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                                // 可以添加文件上传
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {

                                        try {
                                            JSONObject json = new JSONObject(s);
                                            String status = json.getString("status");
                                            if (status.equals("200")) {
                                                JSONObject data = json.getJSONObject("data");
                                                String avatar = data.getString("avatar");
                                                Log.i("NMN", avatar + " avatar");
                                                SharedUtils.saveString(PersonInfoActivity.this, ConstantUils.USER_HEAD_PIC, avatar);
                                                Picasso.with(PersonInfoActivity.this).load(avatar).placeholder(R.drawable.default_portrait).into(mCricle);
                                            } else {
                                                Toast.makeText(PersonInfoActivity.this, "拍照上传失败", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                });
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_PHOTO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isTakePhoto) takePhoto();
                    if (isGetPic) getPictureFromLocal();
                }
            }
            case PERMISSIONS_REQUEST_FILE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    dealZoomPhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKING_PHOTO: // 拍照的结果


                    dealTakePhotoThenZoom();
                    break;
                case REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL://选择图片的结果
                    dealChoosePhotoThenZoom(data);
                    break;
                case REQUEST_CODE_CUT_PHOTO: // 剪裁图片的结果
                    dealZoomPhoto();
                    break;
            }
        }
    }


}
