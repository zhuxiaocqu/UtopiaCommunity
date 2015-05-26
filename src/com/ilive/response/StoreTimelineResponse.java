package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.Store;

public class StoreTimelineResponse implements iLiveResponse {
	private List<Store> stores = null;
	private Long code = null;

	public StoreTimelineResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("store"))
				if (json.getJSONArray("store").toString().length() > 2) {
					int size = json.getJSONArray("store").length();
					stores = new ArrayList<Store>(size);
					for (int i = 0; i < size; i++) {
						stores.add(new Store(json.getJSONArray("store")
								.getJSONObject(i)));
					}
				}

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public List<Store> getStores() {
		return stores;
	}

	public Long getCode() {
		return code;
	}

}