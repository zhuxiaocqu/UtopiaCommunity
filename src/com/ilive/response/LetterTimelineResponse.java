package com.ilive.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ilive.iLiveResponse;
import com.ilive.structs.Letter;

public class LetterTimelineResponse implements iLiveResponse {
	private List<Letter> letters = null;
	private Long code = null;

	public LetterTimelineResponse(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("letters"))
				if (json.getJSONArray("letters").toString().length() > 2) {
					int size = json.getJSONArray("letters").length();
					letters = new ArrayList<Letter>(size);
					for (int i = 0; i < size; i++) {
						letters.add(new Letter(json.getJSONArray("letters")
								.getJSONObject(i)));
					}
				}

			if (!json.isNull("code"))
				code = json.getLong("code");

		}
	}

	public List<Letter> getLetters() {
		return letters;
	}

	public Long getCode() {
		return code;
	}

}