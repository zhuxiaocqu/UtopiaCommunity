package com.ilive.structs;

import org.json.JSONException;
import org.json.JSONObject;

public class Letter {
	private Long _id = null; // 信息唯一ID
	private Long is_usersend = null; // 是否为用户发送信息
	private Long userid = null; // 用户ID(users表的唯一ID)
	private Long authorid = null; // 管理员ID(authors表的唯一ID)
	private String title = null; // 站内信标题
	private String text = null; // 站内信内容
	private String original_pic = null; // 站内信附图地址
	private String created_at = null; // 创建时间

	public void set_id(Long _id) {
		this._id = _id;
	}

	public Long get_id() {
		return _id;
	}

	public void setIs_usersend(Long is_usersend) {
		this.is_usersend = is_usersend;
	}

	public Long getIs_usersend() {
		return is_usersend;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setAuthorid(Long authorid) {
		this.authorid = authorid;
	}

	public Long getAuthorid() {
		return authorid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public Letter(JSONObject json) throws JSONException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("_id"))
				_id = json.getLong("_id");

			if (!json.isNull("is_usersend"))
				is_usersend = json.getLong("is_usersend");

			if (!json.isNull("userid"))
				userid = json.getLong("userid");

			if (!json.isNull("authorid"))
				authorid = json.getLong("authorid");

			if (!json.isNull("title"))
				title = json.getString("title");

			if (!json.isNull("text"))
				text = json.getString("text");

			if (!json.isNull("original_pic"))
				original_pic = json.getString("original_pic");

			if (!json.isNull("created_at"))
				created_at = json.getString("created_at");

		}
	}
}
