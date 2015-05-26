package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.Store;

public class StoreShowResponse implements iLiveResponse {
	private Store stores = null;
	private Long code = null;

	public StoreShowResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("stores"))
				stores = new Store(json.getJSONObject("stores"));

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public Store getStores() {
		return stores;
	}

	public Long getCode() {
		return code;
	}

}