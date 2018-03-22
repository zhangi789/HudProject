package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class SDCardScanner {

    public static File getStorgePath(Context context) {

        File root;
        switch (checkExternalMedia()) {
            case 2: /* Use external */
                root = Environment.getExternalStorageDirectory();
                break;
            default: /* Use internal */
                root = context.getFilesDir();
                break;
        }
        return root;
    }


    public static int checkExternalMedia() {

        int mExternalStorageAvailable = 0;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = 2;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = 1;
        } else {
            // Can't read or write
            mExternalStorageAvailable = 0;
        }

        // For TEST on internal storage
        //mExternalStorageAvailable = 0;

        return mExternalStorageAvailable;
    }

    public static boolean isNeedPerssion() {
        boolean needPerrsion = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            needPerrsion = true;
        } else {
            needPerrsion = false;
        }
        return needPerrsion;
    }
}
