package com.utopia.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.androidquery.AQuery;
import com.ilive.structs.Store;
import com.utopia.activity.R;
import com.utopia.adapter.ConvToolsAdapter;

import android.graphics.drawable.Drawable;
import android.view.View;

public class ShowMarketList {
	private View view;
	private List<Store> marketList;
	private String buttonLookText;
	AQuery aq;

	public ShowMarketList(View view, List<Store> marketList,
			String buttonLookText) {
		this.view = view;
		this.marketList = marketList;
		this.buttonLookText = buttonLookText;
	}

	public void showMarket() {
		aq = new AQuery(view);

		// 将list中元素按count从大到小排序
		// Collections.sort(marketDataObjectList, new sortByCount());
		if (marketList == null || marketList.isEmpty()) {
			aq.id(R.id.Layout_ConvtoolsActivity_market).background(
					R.drawable.noinfo);
		} else {
			ConvToolsAdapter marketAdapter = new ConvToolsAdapter(
					view.getContext(), marketList, buttonLookText);
			aq.id(R.id.ListView_ConvtoolsActivity_market)
					.adapter(marketAdapter).itemClicked(marketAdapter);
		}
	}
}
