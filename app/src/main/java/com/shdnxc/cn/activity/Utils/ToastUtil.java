package com.shdnxc.cn.activity.Utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast = null;

	public static void showMsg(final Context context, final int resId) {
		if (context instanceof Activity) {
			((Activity) context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
					} else {
						mToast.setText(resId);
						mToast.setDuration(Toast.LENGTH_LONG);
					}
					mToast.show();
				}
			});
		}
	}

	public static void showMsg(final Context context, final int resId, final String msg) {
		if (context instanceof Activity) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String msg2 = msg + context.getString(resId);
					if (mToast == null) {
						mToast = Toast.makeText(context, msg2, Toast.LENGTH_LONG);
					} else {
						mToast.setText(msg2);
						mToast.setDuration(Toast.LENGTH_LONG);
					}
					mToast.show();
				}
			});
		}
	}
}
