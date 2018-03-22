package com.shdnxc.cn.activity.servce;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.LeProxy;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.aidl.RemoteService;

import static com.shdnxc.cn.activity.MainActivity.mLeProxy;

/**
 * Created by Android on 2017/5/8.
 */

public class WatchService extends Service {
    private MyBilder mBilder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(mBilder == null)
            mBilder = new MyBilder();
        return mBilder;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        this.bindService(new Intent(WatchService.this,LocalService.class),conn, Context.BIND_ABOVE_CLIENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在终止后调用,我们在杀死服务的时候就会引起意外终止,就会调用onServiceDisconnected
         * 则我们就得里面启动被杀死的服务,然后进行绑定
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("TAG", "LocalService链接成功!");
            try {
                if(mBilder != null)
                    mBilder.startOtherService();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG", "run22222222:--------- ");
                    }
                }).start();

            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("TAG", "LocalService被杀死了");
            Intent remoteService = new Intent(WatchService.this, LocalService.class);
            WatchService.this.startService(remoteService);
            WatchService.this.bindService(new Intent(WatchService.this, LocalService.class),
                    conn, Context.BIND_ABOVE_CLIENT);
//            Toast.makeText(WatchService.this, "LocalService被杀死!", Toast.LENGTH_SHORT).show();

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i("TAG", "run22222222:--------- ");
//                }
//            }).start();

//            bindService(new Intent(WatchService.this, BleService.class), mConnection, BIND_ABOVE_CLIENT);
////            LocalBroadcastManager.getInstance(WatchService.this).registerReceiver(mLocalReceiver, mMikeFilter());
//            mConnHander.postDelayed(mConnRunable,1000);
        }
    };


    Handler mConnHander = new Handler();
    /**
     * 状态控制蓝牙再次进来连接的状态
     */
    Runnable mConnRunable = new Runnable() {
        @Override
        public void run() {
            try {
//                if (mBluetoothAdapter.isEnabled()) {
                        mLeProxy.connect(BaseUtils.getMacStr(SharedUtils.getString(WatchService.this, ConstantUils.CU_SPLASH, "0"))[2], false);
//                } else {
//                    Intent mConnIntent = new Intent();
//                    mConnIntent.setAction(ConstantUils.BLE_START_NO_START);
//                    sendBroadcast(mConnIntent);
                    Log.i("result", "服务服务蓝牙:----------------------------->> ");
                Toast.makeText(WatchService.this, "启动服务!", Toast.LENGTH_SHORT).show();
//                }
                mConnHander.postDelayed(this, 3000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };


    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w("TAG", "onServiceDisconnected()");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LeProxy.getInstance().setBleService(service);
        }
    };



    private class MyBilder extends RemoteService.Stub{


        @Override
        public void startOtherService() throws RemoteException {

        }
    }

}
