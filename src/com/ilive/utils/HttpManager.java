package com.ilive.utils;

import java.net.URLEncoder;

import android.util.Log;

public class HttpManager {
	public static String getUrl(String url, Parameters params) {
		url = url + "?" + encodeUrl(params);
		return url;
	}

	public static String encodeUrl(Parameters params) {
		if (params == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int loc = 0; loc < params.size(); loc++) {
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			String _key = params.getKey(loc);
			String _value = params.getValue(_key);
			if (_value == null) {
				Log.i("encodeUrl", "key:" + _key + " 's value is null");
			} else {
				sb.append(URLEncoder.encode(params.getKey(loc)) + "="
						+ URLEncoder.encode(params.getValue(loc)));
			}

		}
		return sb.toString();
	}
}
