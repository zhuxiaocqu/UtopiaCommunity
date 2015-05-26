package com.utopia.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShowConvToolsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_conv_tools_list);
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_conv_tools_list, menu);
		return true;
	}

}
