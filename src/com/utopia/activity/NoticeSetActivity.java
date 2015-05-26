package com.utopia.activity;

import android.app.Activity;
import com.androidquery.AQuery;
import com.utopia.structs.Flags;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class NoticeSetActivity extends Activity {
	private AQuery aqNotice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noticeset);
		MyApplication.getInstance().addActivity(this);
		aqNotice = new AQuery(this);
		clickBackEvent();
	}

	public void clickBackEvent() {
		aqNotice.id(R.id.ImageView_setting_noticeset_back).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent jumpBack = new Intent(NoticeSetActivity.this,
								MainActivity.class);
						jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 4);
						jumpBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
						startActivity(jumpBack);
						finish();
					}
				});
	}
}
