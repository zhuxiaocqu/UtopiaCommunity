package com.ilive.request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class LettersUploadRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/LetterUpload";

	private AQuery aq = null;
	private String Listener;

	public LettersUploadRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * user上传私信
	 * 
	 * @param account
	 *            : 用户账户（唯一）*
	 * @param password
	 *            : 用户密码*
	 * @param authorid
	 *            : 管理员唯一id*
	 * @param title
	 *            : 私信标题
	 * @param text
	 *            : 私信内容
	 * @param upfile
	 *            : 私信附图地址
	 */
	public void request(String account, String password, String authorid,
			String title, String text, File upfile) {
		Map<String, Object> params = new HashMap<String, Object>();

		if (account != null)
			params.put("account", account);
		if (password != null)
			params.put("password", password);
		if (authorid != null)
			params.put("authorid", authorid);
		if (title != null)
			params.put("title", title);
		if (text != null)
			params.put("text", text);
		if (upfile != null)
			params.put("upfile", upfile);

		aq.ajax(SERVER_URL_PRIX, params, JSONObject.class, aq.getContext(),
				Listener);
	}
}