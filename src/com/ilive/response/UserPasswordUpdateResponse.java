package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.User;

public class UserPasswordUpdateResponse implements iLiveResponse {
	private Boolean result = null;
	private User user = null;
	private Long code = null;

	public UserPasswordUpdateResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("result"))
				result = json.getBoolean("result");

			if (!json.isNull("user"))
				user = new User(json.getJSONObject("user"));

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public Boolean getResult() {
		return result;
	}

	public User getUser() {
		return user;
	}

	public Long getCode() {
		return code;
	}

}