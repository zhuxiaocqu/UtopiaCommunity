package com.utopia.tools;

public class ConvToolsDataObject {
	private int count; // 查看次数
	private String name; // 单位名称
	private String phoneNumber; // 电话号码
	private String shopHours; // 营业时间
	private String introduction; // 单位简介

	public ConvToolsDataObject() {
	};

	public ConvToolsDataObject(int count, String name, String phoneNumber,
			String shopHours, String introduction) {
		this.count = count;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.shopHours = shopHours;
		this.introduction = introduction;
	};

	public int getCount() {
		return this.count;
	}

	public String getName() {
		return this.name;
	}

	public String getphoneNumber() {
		return this.phoneNumber;
	}

	public String getshopHours() {
		return this.shopHours;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	// 查看简介一次count加1
	public void addCountOnce() {
		count++;
	}

	// 拨号一次count加2
	public void addCountDoulbe() {
		count = count + 2;
		;
	}

}
