package com.ilive.request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class UserProfileUploadRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/UserProfileUpload";

	private AQuery aq = null;
	private String Listener;

	public UserProfileUploadRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 上传更新业主用户账户头像图片
	 * 
	 * @param account
	 *            : 用户账户名*
	 * @param password
	 *            : 用户密码*
	 * @param upfile
	 *            : 头像文件*
	 */
	public void request(String account, String password, File upfile) {
		Map<String, Object> params = new HashMap<String, Object>();

		if (account != null)
			params.put("account", account);
		if (password != null)
			params.put("password", password);
		if (upfile != null)
			params.put("upfile", upfile);

		aq.ajax(SERVER_URL_PRIX, params, JSONObject.class, aq.getContext(),
				Listener);
	}
}