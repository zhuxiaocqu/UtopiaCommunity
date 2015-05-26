package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class UserCheckUniquenessRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/UserCheckUniqueness";

	private AQuery aq = null;
	private String Listener;

	public UserCheckUniquenessRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 测试用户账户名重复性
	 * 
	 * @param account
	 *            : 用户账户名*
	 */
	public void request(String account) {
		Parameters params = new Parameters();

		if (account != null)
			params.add("account", account);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}