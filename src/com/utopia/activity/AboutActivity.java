package com.utopia.activity;

import com.androidquery.AQuery;
import com.utopia.structs.Flags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutActivity extends Activity {

	private AQuery aqAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		MyApplication.getInstance().addActivity(this);
		aqAbout = new AQuery(this);
		clickBackEvent();
	}

	public void clickBackEvent() {
		aqAbout.id(R.id.button_setting_about_back).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent jumpBack = new Intent(AboutActivity.this,
								MainActivity.class);
						jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 4);
						jumpBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
						startActivity(jumpBack);
						finish();
					}
				});
	}

}
