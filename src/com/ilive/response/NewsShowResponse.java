package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.News;

public class NewsShowResponse implements iLiveResponse {
	private News news = null;
	private Long code = null;

	public NewsShowResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("news"))
				news = new News(json.getJSONObject("news"));

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public News getNews() {
		return news;
	}

	public Long getCode() {
		return code;
	}

}