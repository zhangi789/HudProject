package com.shdnxc.cn.activity.Utils;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ble.api.DataUtil;
import com.ble.ble.BleCallBack;
import com.ble.ble.BleService;
import com.ble.ble.adaption.Error;
import com.ble.ble.adaption.OnResultListener;
import com.ble.ble.constants.BleUUIDS;
import com.ble.ble.oad.OADListener;
import com.ble.ble.oad.OADManager;
import com.ble.ble.oad.OADProxy;
import com.ble.ble.oad.OADType;
import com.ble.ble.util.GattUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by JiaJiefei on 2017/2/17.
 */
public class LeProxy {
    private static final String TAG = "LeProxy";

    //各蓝牙事件的广播action
    public static final String ACTION_CONNECT_TIMEOUT = ".LeProxy.ACTION_CONNECT_TIMEOUT";
    public static final String ACTION_CONNECT_ERROR = ".LeProxy.ACTION_CONNECT_ERROR";
    public static final String ACTION_GATT_CONNECTED = ".LeProxy.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = ".LeProxy.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = ".LeProxy.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE = ".LeProxy.ACTION_DATA_AVAILABLE";
    public static final String ACTION_REG_DATA_AVAILABLE = ".LeProxy.ACTION_REG_DATA_AVAILABLE";
    public static final String ACTION_RSSI_AVAILABLE = ".LeProxy.ACTION_RSSI_AVAILABLE";

    public static final String EXTRA_ADDRESS = ".LeProxy.EXTRA_ADDRESS";
    public static final String EXTRA_DATA = ".LeProxy.EXTRA_DATA";
    public static final String EXTRA_UUID = ".LeProxy.EXTRA_UUID";
    public static final String EXTRA_REG_DATA = ".LeProxy.EXTRA_REG_DATA";
    public static final String EXTRA_REG_FLAG = ".LeProxy.EXTRA_REG_FLAG";
    public static final String EXTRA_RSSI = ".LeProxy.EXTRA_RSSI";

    private static LeProxy mInstance;

    public static BleService mBleService;

    private LeProxy() {
    }

    public static LeProxy getInstance() {
        if (mInstance == null) {
            mInstance = new LeProxy();
        }
        return mInstance;
    }

    public void setBleService(IBinder binder) {
        mBleService = ((BleService.LocalBinder) binder).getService(mBleCallBack);
        mBleService.setMaxConnectedNumber(1);// 设置最大可连接从机数量，默认为4
        mBleService.setDecode(false);//设置是否解密接收的数据（仅限于默认的接收通道【0x1002】，依据模透传块数据是否加密而定）
        mBleService.setConnectTimeout(2000);//设置APP端的连接超时时间（单位ms）

        mBleService.initialize();// 必须调用初始化函数
    }

    public OADProxy getOADProxy(OADListener listener, OADType type) {
        if (mBleService != null) {
            return OADManager.getOADProxy(mBleService, listener, type);
        }
        return null;
    }

    public boolean connect(String address, boolean autoConnect) {
        if (mBleService != null) {
            return mBleService.connect(address, autoConnect);
        }
        return false;
    }

    //设置是否解密接收的数据（仅限于默认的接收通道【0x1002】）
    public void setDecode(boolean decode) {
        if (mBleService != null) {
            mBleService.setDecode(decode);
        }
    }

    public void disconnect(String address) {
        if (mBleService != null) {
            mBleService.setAutoConnect(address, false);
            mBleService.disconnect(address);
        }
    }

    public BluetoothGatt getBluetoothGatt(String address) {
        if (mBleService != null) {
            return mBleService.getBluetoothGatt(address);
        }
        return null;
    }

    //获取已连接的设备
    public List<BluetoothDevice> getConnectedDevices() {
        if (mBleService != null) {
            return mBleService.getConnectedDevices();
        }
        return new ArrayList<>();
    }

    public boolean send(String address, String data, boolean encode) {
        if (mBleService != null) {
            return mBleService.send(address, data, encode);
        }
        return false;
    }


    //向默认通道【0x1001】发送数据
    public boolean send(String address, byte[] data, boolean encode) {
        if (mBleService != null) {
            return mBleService.send(address, data, encode);
        }
        return false;
    }

    /**
     * 向指定通道发数据
     *
     * @param address  设备地址
     * @param serUuid  服务uuid
     * @param charUuid 特征uuid
     * @param data     发送的数据
     * @param encode   是否加密发送数据（依据模块来定）
     */
    public boolean send(String address, UUID serUuid, UUID charUuid, byte[] data, boolean encode) {
        if (mBleService != null) {
            BluetoothGatt gatt = mBleService.getBluetoothGatt(address);
            BluetoothGattCharacteristic c = GattUtil.getGattCharacteristic(gatt, serUuid, charUuid);
            return mBleService.send(gatt, c, data, encode);
        }
        return false;
    }

    /**
     * 检测设备是否已连接
     *
     * @param address 设备地址
     * @return true表示已连接
     */
    public boolean isConnected(String address) {
        if (mBleService != null && address != null) {
            return mBleService.getConnectionState(address) == BluetoothProfile.STATE_CONNECTED;
        }
        return false;
    }

    /**
     * 开启指定通道的notify
     *
     * @param address  设备地址
     * @param serUuid  服务uuid
     * @param charUuid 特征uuid
     */
    public boolean enableNotification(String address, UUID serUuid, UUID charUuid) {
        BluetoothGatt gatt = mBleService.getBluetoothGatt(address);
        BluetoothGattCharacteristic c = GattUtil.getGattCharacteristic(gatt, serUuid, charUuid);
        Log.e(TAG, "gatt=" + gatt + ", characteristic=" + c);
        return setCharacteristicNotification(gatt, c, true);
    }

    public boolean setCharacteristicNotification(BluetoothGatt gatt, BluetoothGattCharacteristic c, boolean enable) {
        if (mBleService != null) {
            return mBleService.setCharacteristicNotification(gatt, c, enable);
        }
        return false;
    }

    /**
     * 读取寄存器数据
     */
    public void readReg(String address, int regFlag) {
        if (mBleService != null) {
            mBleService.readReg(address, regFlag);
        }
    }

    /**
     * 更改寄存器数据
     */
    public void setReg(String address, int regFlag, int value) {
        if (mBleService != null) {
            mBleService.setReg(address, regFlag, value);
        }
    }

    public boolean readCharacteristic(String address, UUID serUuid, UUID charUuid) {
        if (mBleService != null) {
            BluetoothGatt gatt = mBleService.getBluetoothGatt(address);
            BluetoothGattCharacteristic c = GattUtil.getGattCharacteristic(gatt, serUuid, charUuid);
            return mBleService.read(gatt, c);
        }
        return false;
    }
    //这里集合了所有的蓝牙交互事件
    //注意事项：回调方法所在线程不能有阻塞操作，否则可能导致数据发送失败或者某些方法无法正常回调！！！
    private final BleCallBack mBleCallBack = new BleCallBack() {
        @Override
        public void onConnected(String address) {
            //!!!这里只代表手机与模组建立了物理连接，APP还不能与模组进行数据交互
            Log.i(TAG, "onConnected() - " + address);
            //启动获取rssi的定时器，如不需要获取信号，可以不启动该定时任务
            mBleService.startReadRssi(address, 1000);
            updateBroadcast(address, ACTION_GATT_CONNECTED);
        }

        @Override
        public void onConnectTimeout(String address) {
            Log.e(TAG, "onConnectTimeout() - " + address);
            updateBroadcast(address, ACTION_CONNECT_TIMEOUT);
        }

        @Override
        public void onConnectionError(String address, int error, int newState) {
            Log.e(TAG, "onConnectionError() - " + address + " error code: " + error + ", new state: " + newState);
            updateBroadcast(address, ACTION_CONNECT_ERROR);
        }

        @Override
        public void onDisconnected(String address) {
            Log.e(TAG, "onDisconnected() - " + address);
            updateBroadcast(address, ACTION_GATT_DISCONNECTED);
        }

        @Override
        public void onServicesDiscovered(String address) {
            //!!!检索服务成功，到这一步才可以与从机进行数据交互，有些手机可能需要延时几百毫秒才能数据交互
            Log.i(TAG, "onServicesDiscovered() - " + address);
            new Timer().schedule(new ServicesDiscoveredTask(address), 300, 100);
        }

        @Override
        public void onServicesUndiscovered(String address, int status) {
            //检索服务异常
            Log.e(TAG, "onServicesUndiscovered() - " + address + ", status = " + status);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicChanged(String address, BluetoothGattCharacteristic characteristic) {
            Log.i(TAG, "onCharacteristicChanged() - " + address + " uuid=" + characteristic.getUuid().toString()
                    + "\n len=" + characteristic.getValue().length
                    + " [" + DataUtil.byteArrayToHex(characteristic.getValue()) + ']');

            updateBroadcast(address, characteristic);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicRead(String address, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onCharacteristicRead() - " + address + " uuid=" + characteristic.getUuid().toString()
                        + "\n len=" + characteristic.getValue().length
                        + " [" + DataUtil.byteArrayToHex(characteristic.getValue()) + ']');
                updateBroadcast(address, characteristic);
            }
        }

        @Override
        public void onRegRead(String address, String regData, int regFlag, int status) {
            //获取到模组寄存器数据
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Intent intent = new Intent(ACTION_REG_DATA_AVAILABLE);
                intent.putExtra(EXTRA_ADDRESS, address);
                intent.putExtra(EXTRA_REG_FLAG, regFlag);
                intent.putExtra(EXTRA_REG_DATA, regData);
                LocalBroadcastManager.getInstance(mBleService).sendBroadcast(intent);
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicWrite(String address, BluetoothGattCharacteristic characteristic, int status) {
            //调试时可以在这里打印status来看数据有没有发送成功
            if (status == BluetoothGatt.GATT_SUCCESS) {
                String uuid = characteristic.getUuid().toString();
                //如果发送数据加密，可以先把characteristic.getValue()获取的数据解密一下再打印
                //byte[] decodedData = new EncodeUtil().decodeMessage(characteristic.getValue());
                Log.i(TAG, "onCharacteristicWrite() - " + address + ", " + uuid
                        + "\n len=" + characteristic.getValue().length
                        + " [" + DataUtil.byteArrayToHex(characteristic.getValue()) + ']');
            }
        }

        @Override
        public void onReadRemoteRssi(String address, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Intent intent = new Intent(ACTION_RSSI_AVAILABLE);
                intent.putExtra(EXTRA_ADDRESS, address);
                intent.putExtra(EXTRA_RSSI, rssi);
                LocalBroadcastManager.getInstance(mBleService).sendBroadcast(intent);
            }
        }
    };

    private void updateBroadcast(String address, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_ADDRESS, address);
        LocalBroadcastManager.getInstance(mBleService).sendBroadcast(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void updateBroadcast(String address, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(ACTION_DATA_AVAILABLE);
        intent.putExtra(EXTRA_ADDRESS, address);
        intent.putExtra(EXTRA_UUID, characteristic.getUuid().toString());
        intent.putExtra(EXTRA_DATA, characteristic.getValue());
        LocalBroadcastManager.getInstance(mBleService).sendBroadcast(intent);
    }

    //TODO 刚连上线做的一些准备工作
    private class ServicesDiscoveredTask extends TimerTask {
        String address;
        int i;

        ServicesDiscoveredTask(String address) {
            this.address = address;
        }

        void cancelTask() {
            //准备工作完成，向外发送广播
            updateBroadcast(address, ACTION_GATT_SERVICES_DISCOVERED);
            Log.w(TAG, "Cancel ServicesDiscoveredTask: " + cancel() + ", i=" + i);
        }

        @Override
        public void run() {
            switch (i) {
                case 0:
                    //打开模组默认的数据接收通道【0x1002】，这一步成功才能保证APP收到数据
                    boolean success = enableNotification(address, BleUUIDS.PRIMARY_SERVICE, BleUUIDS.CHARACTERS[1]);
                    Log.i(TAG, "Enable 0x1002 notification: " + success);
                    break;

//                case 1:
//                    //适配CC2541透传模块与部分手机的连接问题（就是连线后不走onServicesDiscovered()方法，一段时间后自动断开），
//                    //初次成功需要重启模块，2.6以下版本还要重启手机蓝牙或者断线时调用mBleService.refresh()，
//                    //不过mBleService.refresh()会清除手机缓存的uuid，影响再次连接的速度
//                    AdaptionUtil au = new AdaptionUtil(mBleService);
//                    au.setOnResultListener(mAdaptionResultListener);
//                    au.writeAdaptionConfigs(address);
//                    break;

                default:
                    cancelTask();
                    break;
            }
            i++;
        }
    }

    private OnResultListener mAdaptionResultListener = new OnResultListener() {
        @Override
        public void onSuccess(String address) {
            Log.e(TAG, "配置成功！" + address);
        }

        @Override
        public void onError(String address, Error error) {
            //Log.e(TAG, "error code[" + error.getErrorCode() + "] " + error.getMessage());
            switch (error.getErrorCode()) {
                case Error.DISCONNECTED:
                    Log.e(TAG, "没链接设备");
                    break;
                case Error.WRITE_TIMEOUT:
                    Log.e(TAG, "写入超时");
                    break;
                case Error.WRITE_FAILED:
                    Log.e(TAG, "写入失败");
                    break;
                case Error.NOT_NEED_TO_ADAPT:
                    Log.e(TAG, "无需适配");
                    break;
            }
        }
    };
}
