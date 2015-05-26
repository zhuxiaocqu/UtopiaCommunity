package com.utopia.activity;

import java.io.Serializable;

import com.androidquery.AQuery;
import com.ilive.structs.User;
import com.utopia.structs.Flags;
import com.utopia.tools.ReadUserOutXml;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MyAccountActivity extends Activity {
	private AQuery aqAccount;
	private String userInfoStr = "userInfo";
	private User userInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaccount);
		MyApplication.getInstance().addActivity(this);
		aqAccount = new AQuery(this);
		findUserInfo();
		showUserInfo();
		clickBackEvent();
	}

	public void findUserInfo() {
		SharedPreferences preferences = getSharedPreferences(userInfoStr,
				MODE_PRIVATE);
		String productUser = preferences.getString("user", null);
		if (productUser != null) {
			User user = ReadUserOutXml.readUser(productUser);
			if (user != null) {
				userInfo = user;
			}

		} else {

		}
	}

	public void clickBackEvent() {
		aqAccount.id(R.id.ImageView_setting_account_back).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent jumpBack = new Intent(MyAccountActivity.this,
								MainActivity.class);
						jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 4);
						jumpBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
						startActivity(jumpBack);
						finish();
					}
				});
	}

	public void showUserInfo() {
		if (userInfo != null) {
			aqAccount.id(R.id.imageview_setting_account_accountvalue).text(
					userInfo.getName());
			aqAccount.id(R.id.imageview_setting_account_doornumvalue).text(
					userInfo.getAccount());
			if (userInfo.getSex() == 0)
				aqAccount.id(R.id.imageview_setting_account_sexvalue).text("男");
			else
				aqAccount.id(R.id.imageview_setting_account_sexvalue).text("女");
			aqAccount.id(R.id.imageview_setting_account_phonevalue).text(
					userInfo.getPhone_number());
			aqAccount.id(R.id.imageview_setting_account_imelvalue).text(
					userInfo.getImei());
			aqAccount.id(R.id.imageview_setting_account_addressvalue).text(
					userInfo.getAddress());
		} else {
			Toast.makeText(MyAccountActivity.this, "您还未完善您的信息", 2000).show();
		}

	}
}
