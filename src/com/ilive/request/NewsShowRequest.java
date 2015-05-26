package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class NewsShowRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER + "/api/NewsShow";

	private AQuery aq = null;
	private String Listener;

	public NewsShowRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 根据新闻的唯一id获取单条新闻
	 * 
	 * @param _id
	 *            : 每一条新闻的唯一id*
	 */
	public void request(Long _id) {
		Parameters params = new Parameters();

		if (_id != null)
			params.add("_id", _id);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}