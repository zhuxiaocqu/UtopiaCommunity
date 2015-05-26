package com.utopia.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.androidquery.AQuery;
import com.ilive.structs.Store;
import com.utopia.activity.R;
import com.utopia.adapter.ConvToolsAdapter;

import android.view.View;

public class ShowServiceList {
	private View view;
	private List<Store> serviceList;
	private String buttonLookText;
	AQuery aq;

	public ShowServiceList(View view, List<Store> serviceList,
			String buttonLookText) {
		this.view = view;
		this.serviceList = serviceList;
		this.buttonLookText = buttonLookText;
	}

	public void showService() {
		aq = new AQuery(view);
		// 将list中元素按count从大到小排序
		// Collections.sort(serviceDataObjectList, new sortByCount());

		if (serviceList == null || serviceList.isEmpty()) {
			aq.id(R.id.Layout_ConvtoolsActivity_service).background(
					R.drawable.noinfo);
		} else {
			ConvToolsAdapter serviceAdapter = new ConvToolsAdapter(
					view.getContext(), serviceList, buttonLookText);
			aq.id(R.id.ListView_ConvtoolsActivity_service)
					.adapter(serviceAdapter).itemClicked(serviceAdapter);
		}

	}
}
