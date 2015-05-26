package com.utopia.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.androidquery.AQuery;
import com.ilive.structs.Store;
import com.utopia.activity.R;
import com.utopia.adapter.ConvToolsAdapter;

import android.view.View;

public class ShowMedicalList {
	private View view;
	private List<Store> medicalList;
	private String buttonLookText;
	AQuery aq;

	public ShowMedicalList(View view, List<Store> medicalList,
			String buttonLookText) {
		this.view = view;
		this.medicalList = medicalList;
		this.buttonLookText = buttonLookText;
	}

	public void showMedical() {
		aq = new AQuery(view);

		// 将list中元素按count从大到小排序
		// Collections.sort(medicalDataObjectList, new sortByCount());
		if (medicalList == null || medicalList.isEmpty()) {
			aq.id(R.id.Layout_ConvtoolsActivity_medical).background(
					R.drawable.noinfo);
		} else {
			ConvToolsAdapter medicalAdapter = new ConvToolsAdapter(
					view.getContext(), medicalList, buttonLookText);
			aq.id(R.id.ListView_ConvtoolsActivity_medical)
					.adapter(medicalAdapter).itemClicked(medicalAdapter);
		}
	}
}
