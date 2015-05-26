package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;

public class UserProfileUploadResponse implements iLiveResponse {
	private Boolean result = null;
	private Long code = null;

	public UserProfileUploadResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("result"))
				result = json.getBoolean("result");

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public Boolean getResult() {
		return result;
	}

	public Long getCode() {
		return code;
	}

}