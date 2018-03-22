package com.shdnxc.cn.activity.servce;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.shdnxc.cn.activity.aidl.RemoteService;


/**
 * Created by Android on 2017/5/8.
 */

public class LocalService extends Service {
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
        this.bindService(new Intent(LocalService.this, WatchService.class),
                conn, Context.BIND_ABOVE_CLIENT);
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
        public void onServiceDisconnected(ComponentName name) {
            Log.i("TAG", "RemoteService被杀死了");
            Intent localService = new Intent(LocalService.this, WatchService.class);
            LocalService.this.startService(localService);
            LocalService.this.bindService(new Intent(LocalService.this, WatchService.class),
                    conn, Context.BIND_ABOVE_CLIENT);
//            Toast.makeText(LocalService.this, "RemoteService被杀死!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("TAG", "RemoteService链接成功!");
            try {
                if(mBilder != null)
                    mBilder.startOtherService();


            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


    private class MyBilder extends RemoteService.Stub{

        @Override
        public void startOtherService() throws RemoteException {
            Log.i("TAG", "绑定成功!");
            Intent localService = new Intent(LocalService.this, WatchService.class);
            LocalService.this.startService(localService);
            LocalService.this.bindService(new Intent(LocalService.this, WatchService.class),
                    conn, Context.BIND_ABOVE_CLIENT);
        }
    }
}
