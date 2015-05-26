package com.utopia.mainpage;

import com.androidquery.AQuery;
import com.utopia.activity.ConvToolsActivity;
import com.utopia.activity.R;

import android.content.Intent;
import android.view.View;

public class ShowConvTools {
	private View view;
	private AQuery aq;

	public ShowConvTools(View view) {
		this.view = view;
	}

	public void showConvTools() {
		aq = new AQuery(view);
		// 四个控件点击后均跳转到ConvToolsActivity
		aq.id(R.id.ImageView_MainAcitvity_Takeout).clicked(this,
				"takeoutClicked");
		aq.id(R.id.ImageView_MainAcitvity_Market)
				.clicked(this, "marketClicked");
		aq.id(R.id.ImageView_MainAcitvity_Medical).clicked(this,
				"medicalClicked");
		aq.id(R.id.ImageView_MainAcitvity_Service).clicked(this,
				"serviceClicked");
	}

	// 为四个控件的绑定事件函数
	public void toolsClicked(int toolIndex) {
		Intent intent = new Intent();
		intent.putExtra("toolKind", toolIndex); // 向ConvToolsActivity传递参数，识别显示的页面是外卖，还是超市等
		intent.setClass(view.getContext(), ConvToolsActivity.class);
		(view.getContext()).startActivity(intent);
	}

	public void takeoutClicked() {
		toolsClicked(0);
	}

	public void marketClicked() {
		toolsClicked(1);
	}

	public void medicalClicked() {
		toolsClicked(2);
	}

	public void serviceClicked() {
		toolsClicked(3);
	}
}
