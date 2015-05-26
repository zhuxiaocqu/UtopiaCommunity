package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class StoreShowRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER + "/api/StoreShow";

	private AQuery aq = null;
	private String Listener;

	public StoreShowRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 根据店铺的唯一id获取单条店铺信息
	 * 
	 * @param _id
	 *            : 店铺的唯一id*
	 */
	public void request(Long _id) {
		Parameters params = new Parameters();

		if (_id != null)
			params.add("_id", _id);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}