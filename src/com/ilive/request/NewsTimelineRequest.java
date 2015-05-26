package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class NewsTimelineRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/NewsTimeline";

	private AQuery aq = null;
	private String Listener;

	public NewsTimelineRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 获取新闻列表
	 * 
	 * @param groupid
	 *            : 分组（分区）编号*
	 * @param typeid
	 *            : 新闻类型编号：1 城市热点 2 社区提醒 3 激费通知 4 医疗文化
	 * @param count
	 *            : Count
	 * @param page
	 *            : Page
	 */
	public void request(Long groupid, Long typeid, Long count, Long page) {
		Parameters params = new Parameters();

		if (groupid != null)
			params.add("groupid", groupid);
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