package com.utopia.activity;

import com.androidquery.AQuery;
import com.utopia.structs.Flags;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class ManagerCommuInfoActivity extends Activity {

	private AQuery aqInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_commu_info);
		MyApplication.getInstance().addActivity(this);
		aqInfo = new AQuery(this);
		clickBackEvent();
	}

	public void clickBackEvent() {
		aqInfo.id(R.id.ImageView_mana_conmu_info_back).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent jumpBack = new Intent(
								ManagerCommuInfoActivity.this,
								MainActivity.class);
						jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 1);
						jumpBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
						startActivity(jumpBack);
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager_commu_info, menu);
		return true;
	}

}
