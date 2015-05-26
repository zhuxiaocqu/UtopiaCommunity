package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class LetterTimelineRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/LetterTimeline";

	private AQuery aq = null;
	private String Listener;

	public LetterTimelineRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 根据用户账号获取站内信列表
	 * 
	 * @param account
	 *            : 用户账号*
	 * @param password
	 *            : 用户密码*
	 * @param page
	 *            : Page
	 * @param count
	 *            : Count
	 */
	public void request(String account, String password, Long page, Long count) {
		Parameters params = new Parameters();

		if (account != null)
			params.add("account", account);
		if (password != null)
			params.add("password", password);
		if (page != null)
			params.add("page", page);
		if (count != null)
			params.add("count", count);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}