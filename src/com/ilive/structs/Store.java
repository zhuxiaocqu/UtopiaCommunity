package com.ilive.structs;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Store implements Serializable {
	private static final long serialVersionUID = 7911523062563984178L;
	private Long _id = null; // 店铺唯一ID
	private String Name = null; // 店铺名称
	private String Description = null; // 店铺描述
	private String Phone_number = null; // 店铺电话
	private String Portrait_pic = null; // 店铺头像图片
	private Long Authorid = null; // 创建该店铺数据的管理员ID(对应authors表_id)
	private String Created_at = null; // 店铺数据创建时间
	private Long Is_vip = null; // 是否为VIP店铺
	private Long Typeid = null; // 店铺类型ID 1外卖 2超市 3医疗 4服务

	public void set_id(Long _id) {
		this._id = _id;
	}

	public Long get_id() {
		return _id;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getName() {
		return Name;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public String getDescription() {
		return Description;
	}

	public void setPhone_number(String Phone_number) {
		this.Phone_number = Phone_number;
	}

	public String getPhone_number() {
		return Phone_number;
	}

	public void setPortrait_pic(String Portrait_pic) {
		this.Portrait_pic = Portrait_pic;
	}

	public String getPortrait_pic() {
		return Portrait_pic;
	}

	public void setAuthorid(Long Authorid) {
		this.Authorid = Authorid;
	}

	public Long getAuthorid() {
		return Authorid;
	}

	public void setCreated_at(String Created_at) {
		this.Created_at = Created_at;
	}

	public String getCreated_at() {
		return Created_at;
	}

	public void setIs_vip(Long Is_vip) {
		this.Is_vip = Is_vip;
	}

	public Long getIs_vip() {
		return Is_vip;
	}

	public void setTypeid(Long Typeid) {
		this.Typeid = Typeid;
	}

	public Long getTypeid() {
		return Typeid;
	}

	public Store(JSONObject json) throws JSONException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws JSONException {
		if (json != null) {
			if (!json.isNull("_id"))
				_id = json.getLong("_id");

			if (!json.isNull("name"))
				Name = json.getString("name");

			if (!json.isNull("description"))
				Description = json.getString("description");

			if (!json.isNull("phone_number"))
				Phone_number = json.getString("phone_number");

			if (!json.isNull("portrait_pic"))
				Portrait_pic = json.getString("portrait_pic");

			if (!json.isNull("authorid"))
				Authorid = json.getLong("authorid");

			if (!json.isNull("created_at"))
				Created_at = json.getString("created_at");

			if (!json.isNull("is_vip"))
				Is_vip = json.getLong("is_vip");

			if (!json.isNull("typeid"))
				Typeid = json.getLong("typeid");

		}
	}
}
