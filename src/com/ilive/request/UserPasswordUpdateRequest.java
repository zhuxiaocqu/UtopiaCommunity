package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class UserPasswordUpdateRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/UserPasswordUpdate";

	private AQuery aq = null;
	private String Listener;

	public UserPasswordUpdateRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 更新业主用户账户密码
	 * 
	 * @param account
	 *            : 用户账户名*
	 * @param password
	 *            : 用户密码*
	 * @param password_new
	 *            : 用户新密码*
	 */
	public void request(String account, String password, String password_new) {
		Parameters params = new Parameters();

		if (account != null)
			params.add("account", account);
		if (password != null)
			params.add("password", password);
		if (password_new != null)
			params.add("password_new", password_new);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}