package com.utopia.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.androidquery.AQuery;
import com.ilive.structs.Store;
import com.utopia.activity.R;
import com.utopia.adapter.ConvToolsAdapter;

import android.view.View;

public class ShowTakeoutList {
	private View view;
	private List<Store> takeoutList;
	private String buttonLookText;
	AQuery aq;

	public ShowTakeoutList(View view, List<Store> takeoutList,
			String buttonLookText) {
		this.view = view;
		this.takeoutList = takeoutList;
		this.buttonLookText = buttonLookText;
	}

	public void showTakeout() {
		aq = new AQuery(view);
		// 将list中元素按count从大到小排序
		// Collections.sort(takeoutDataObjectList, new sortByCount());
		if (takeoutList == null || takeoutList.isEmpty()) {
			aq.id(R.id.Layout_ConvtoolsActivity_takeout).background(
					R.drawable.noinfo);
		} else {
			ConvToolsAdapter takeoutAdapter = new ConvToolsAdapter(
					view.getContext(), takeoutList, buttonLookText);
			aq.id(R.id.ListView_ConvtoolsActivity_takeout)
					.adapter(takeoutAdapter).itemClicked(takeoutAdapter);
		}
	}
}
