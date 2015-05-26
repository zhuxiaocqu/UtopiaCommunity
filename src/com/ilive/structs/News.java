package com.ilive.structs;

import org.json.JSONException;
import org.json.JSONObject;

public class News {
	private Long _id = null; // 新闻唯一ID
	private Long authorid = null; // 管理员（楼管）的唯一ID
	private String authorname=null;
	private String title = null; // 新闻标题
	private String text = null; // 新闻内容
	private String original_pic = null; // 图片地址
	private Long typeid = null; // 新闻类型编号
	private Long groupid = null; // 分组（分区）编号
	private String created_at = null; // 创建时间
	
	public void setAuthorname(String authorname){
		this.authorname=authorname;
	}
	public String getauthorname(){
		return authorname;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public Long get_id() {
		return _id;
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

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public Long getTypeid() {
		return typeid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public Long getGroupid() {
		return groupid;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public News(JSONObject json) throws JSONException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("_id"))
				_id = json.getLong("_id");

			if (!json.isNull("authorid"))
				authorid = json.getLong("authorid");

			if (!json.isNull("title"))
				title = json.getString("title");

			if (!json.isNull("text"))
				text = json.getString("text");

			if (!json.isNull("original_pic"))
				original_pic = json.getString("original_pic");

			if (!json.isNull("typeid"))
				typeid = json.getLong("typeid");

			if (!json.isNull("groupid"))
				groupid = json.getLong("groupid");

			if (!json.isNull("created_at"))
				created_at = json.getString("created_at");

		}
	}
}
