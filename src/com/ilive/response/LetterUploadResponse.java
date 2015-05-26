package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.Letter;

public class LetterUploadResponse implements iLiveResponse {
	private Letter letters = null;
	private Long code = null;

	public LetterUploadResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("letters"))
				letters = new Letter(json.getJSONObject("letters"));

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public Letter getLetters() {
		return letters;
	}

	public Long getCode() {
		return code;
	}

}