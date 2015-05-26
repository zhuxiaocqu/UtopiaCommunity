package com.utopia.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 
 * @author zhuxiao
 *
 */
public class NetHelper {
	// 是否联网网络
	public static boolean IsHaveInternet(final Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = manger.getActiveNetworkInfo();
			return (info != null && info.isConnected() && info.isAvailable());
		} catch (Exception e) {
			return false;
		}
	}
}
