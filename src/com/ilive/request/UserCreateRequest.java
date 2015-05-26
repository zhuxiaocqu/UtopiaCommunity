package com.ilive.request;

import org.json.JSONObject;

import com.androidquery.AQuery;

import com.ilive.iLiveRequest;
import com.ilive.utils.HttpManager;
import com.ilive.utils.Parameters;

public class UserCreateRequest implements iLiveRequest {
	private static final String SERVER_URL_PRIX = API_SERVER
			+ "/api/UserCreate";

	private AQuery aq = null;
	private String Listener;

	public UserCreateRequest(AQuery aq, String listener) {
		this.aq = aq;
		this.Listener = listener;
	}

	/**
	 * 创建用户
	 * 
	 * @param account
	 *            : 用户账户（唯一）*
	 * @param name
	 *            : 用户名
	 * @param password
	 *            : 用户密码*
	 * @param groupid
	 *            : 分组（分区）编号*
	 * @param sex
	 *            : 用户性别，性别代号：0 男 1女 2其他
	 * @param description
	 *            : 个人描述
	 * @param Is_vip
	 *            : 是否为实名用户
	 * @param phone_number
	 *            : 手机号码
	 * @param imei
	 *            : 手机机器码
	 * @param address
	 *            : 用户地址（单元-楼层-门牌号）
	 */
	public void request(String account, String name, String password,
			Long groupid, Long sex, String description, Long Is_vip,
			String phone_number, String imei, String address) {
		Parameters params = new Parameters();

		if (account != null)
			params.add("account", account);
		if (name != null)
			params.add("name", name);
		if (password != null)
			params.add("password", password);
		if (groupid != null)
			params.add("groupid", groupid);
		if (sex != null)
			params.add("sex", sex);
		if (description != null)
			params.add("description", description);
		if (Is_vip != null)
			params.add("Is_vip", Is_vip);
		if (phone_number != null)
			params.add("phone_number", phone_number);
		if (imei != null)
			params.add("imei", imei);
		if (address != null)
			params.add("address", address);

		aq.ajax(HttpManager.getUrl(SERVER_URL_PRIX, params), JSONObject.class,
				aq.getContext(), Listener);
	}
}