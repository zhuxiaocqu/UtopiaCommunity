package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.News;

public class NewsTimelineResponse implements iLiveResponse {
	private List<News> news = null;
	private Long code = null;

	public NewsTimelineResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("news"))
				if (json.getJSONArray("news").toString().length() > 2) {
					int size = json.getJSONArray("news").length();
					news = new ArrayList<News>(size);
					for (int i = 0; i < size; i++) {
						news.add(new News(json.getJSONArray("news")
								.getJSONObject(i)));
					}
				}

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public List<News> getNews() {
		return news;
	}

	public Long getCode() {
		return code;
	}

}