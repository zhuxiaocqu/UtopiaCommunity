package com.ilive.structs;

import org.json.JSONException;
import org.json.JSONObject;

public class Author {
	private String account = null; // 管理员账户（唯一
	private String name = null; // 管理员名称
	private String password = null; // 管理员密码
	private Long sex = null; // 性别代号：0 男 1女 2其他
	private String description = null; // 个人描述
	private String portrait_pic = null; // 头像地址
	private Long groupid = null; // 分组（分区）编号
	private String phone_number = null; // 手机号码
	private String address = null; // 地址
	private Long typeid = null; // 管理员类别：1超级管理员 2楼管（发布新闻） 3黄页维护者
	private String created_at = null; // 创建时间

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setSex(Long sex) {
		this.sex = sex;
	}

	public Long getSex() {
		return sex;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPortrait_pic(String portrait_pic) {
		this.portrait_pic = portrait_pic;
	}

	public String getPortrait_pic() {
		return portrait_pic;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public Long getGroupid() {
		return groupid;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public Long getTypeid() {
		return typeid;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public Author(JSONObject json) throws JSONException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("account"))
				account = json.getString("account");

			if (!json.isNull("name"))
				name = json.getString("name");

			if (!json.isNull("password"))
				password = json.getString("password");

			if (!json.isNull("sex"))
				sex = json.getLong("sex");

			if (!json.isNull("description"))
				description = json.getString("description");

			if (!json.isNull("portrait_pic"))
				portrait_pic = json.getString("portrait_pic");

			if (!json.isNull("groupid"))
				groupid = json.getLong("groupid");

			if (!json.isNull("phone_number"))
				phone_number = json.getString("phone_number");

			if (!json.isNull("address"))
				address = json.getString("address");

			if (!json.isNull("typeid"))
				typeid = json.getLong("typeid");

			if (!json.isNull("created_at"))
				created_at = json.getString("created_at");

		}
	}
}
