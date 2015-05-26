package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class StoreTimelineRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/StoreTimeline";

	private AQuery aq = null;
	private String Listener;

	public StoreTimelineRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 获取店铺时间线
	 * 
	 * @param typeid
	 *            : 店铺类型ID 1外卖 2超市 3医疗 4服务*
	 * @param count
	 *            : Count
	 * @param page
	 *            : Page
	 */
	public void request(Long typeid, Long count, Long page) {
		Parameters params = new Parameters();

		if (typeid != null)
			params.add("typeid", typeid);
		if (count != null)
			params.add("count", count);
		if (page != null)
			params.add("page", page);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}