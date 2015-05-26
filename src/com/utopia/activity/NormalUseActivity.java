package com.utopia.activity;

import com.androidquery.AQuery;
import com.utopia.structs.Flags;
import com.utopia.structs.NewsKind;
import com.utopia.tools.HandleNewsSortMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class NormalUseActivity extends Activity {
	private AQuery aqNormal;
	private Context context;
	private HandleNewsSortMethod handleNewsSortMethod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normaluse);
		MyApplication.getInstance().addActivity(this);
		aqNormal = new AQuery(this);
		context = NormalUseActivity.this;
		// 初始化数据
		iniData();
		// 定义返回事件
		clickBackEvent();
		// 定义新闻排序事件
		clickSortMethodEvent();
	}

	public void iniData() {
		handleNewsSortMethod = new HandleNewsSortMethod(context);
		if (handleNewsSortMethod.getSortMethod() == Flags.NEWS_SORT_BY_KIND) {
			aqNormal.id(R.id.radiobutton_sort_type).checked(true);
		} else if (handleNewsSortMethod.getSortMethod() == Flags.NEWS_SORT_BY_TIME) {
			aqNormal.id(R.id.radiobutton_sort_time).checked(true);
		}
	}

	public void clickSortMethodEvent() {
		aqNormal.id(R.id.radiobutton_sort_time).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleNewsSortMethod.saveSortKind(Flags.NEWS_SORT_BY_TIME);
				Toast.makeText(context, "刷新新闻后生效", 2000).show();
			}
		});
		aqNormal.id(R.id.radiobutton_sort_type).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 handleNewsSortMethod.saveSortKind(Flags.NEWS_SORT_BY_KIND);
				 Toast.makeText(context, "刷新新闻后生效", 2000).show();
			}
		});
	}

	public void clickBackEvent() {
		aqNormal.id(R.id.ImageView_setting_normal_back).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent jumpBack = new Intent(NormalUseActivity.this,
								MainActivity.class);
						jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 4);
						jumpBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
						startActivity(jumpBack);
						finish();
					}
				});
	}
}
