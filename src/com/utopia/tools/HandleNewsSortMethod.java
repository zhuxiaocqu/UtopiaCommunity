package com.utopia.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.utopia.structs.Flags;

/**
 * 
 * @author zhuxiao
 * 
 */
public class HandleNewsSortMethod {
	/*
	 * 1.将设置页面新闻排序选项写入sharepreference文件
	 * 文件名为：Flags.SET_PARAM_NEWS_SORT_FLAG="userpreference"
	 * 2.从sharepreference文件中读出用户设置 默认为按时间排序
	 */
	private Context context;
	private SharedPreferences sortKind;
	private Editor editor;
	private static int sortMethod;

	public HandleNewsSortMethod(Context context) {
		this.context = context;
		sortKind = context.getSharedPreferences(Flags.NEWS_SORT_SHARE_NAME,
				Context.MODE_PRIVATE);
		editor = sortKind.edit();
	}

	public void saveSortKind(int kind) {
		editor.putInt(Flags.NEWS_SORT_SHARE_FLAG, kind);
		editor.commit();
		System.out.println("待写入的排序序号为->"+kind);
		getSortKind();
	}

	public void getSortKind() {
		sortMethod = sortKind.getInt(Flags.NEWS_SORT_SHARE_FLAG, 1);
		System.out.println("当前排序序号：->"+sortMethod);
	}

	// 不通过直接应用静态变量，而是通过内部接口访问静态变量 以减小类间耦合性
	public int getSortMethod() {
		getSortKind();
		return sortMethod;
	}
}
