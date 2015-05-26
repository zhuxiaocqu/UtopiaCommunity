package com.utopia.structs;

public class Flags {
	//从其他页面跳转到MainActivity页面时传递参数的标志内容
	public static final String CHOOSE_PAGE_FLAG = "whichpage";
	//保存新闻排序的文件的名称
	public static final String NEWS_SORT_SHARE_NAME="userpreference";
	//对保存新闻排序类型的文件进行读写时的标志位
	public static final String NEWS_SORT_SHARE_FLAG = "newssortflag";
	//通过类型将新闻排序
	public static final int NEWS_SORT_BY_KIND = 0;
	//通过时间将新闻排序
	public static final int NEWS_SORT_BY_TIME = 1;
}
