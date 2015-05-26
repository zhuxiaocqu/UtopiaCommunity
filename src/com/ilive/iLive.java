package com.ilive;

public class iLive {
	public static String URL_OAUTH2_ACCESS_AUTHORIZE = "";

	private static iLive miLiveInstance = null;

	public static String app_key = "";
	public static String redirecturl = "";

	public static final String KEY_TOKEN = "access_token";
	public static final String KEY_EXPIRES = "expires_in";
	public static final String KEY_REFRESHTOKEN = "refresh_token";
	public static boolean isWifi = false;

	public synchronized static iLive getInstance(String appKey,
			String redirectUrl) {
		if (miLiveInstance == null) {
			miLiveInstance = new iLive();
		}
		app_key = appKey;
		iLive.redirecturl = redirectUrl;
		return miLiveInstance;
	}

	public void setupConsumerConfig(String appKey, String redirectUrl) {
		app_key = appKey;
		redirecturl = redirectUrl;
	}
}
