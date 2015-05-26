package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class UserCheckLoginRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/UserCheckLogin";

	private AQuery aq = null;
	private String Listener;

	public UserCheckLoginRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 测试用户账号及密码，获取账号信息
	 * 
	 * @param account
	 *            : 用户账号*
	 * @param password
	 *            : 用户密码*
	 */
	public void request(String account, String password) {
		Parameters params = new Parameters();

		if (account != null)
			params.add("account", account);
		if (password != null)
			params.add("password", password);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}