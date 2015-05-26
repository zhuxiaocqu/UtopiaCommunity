package com.utopia.activity;

import java.io.Serializable;

import com.ilive.structs.User;
import com.utopia.tools.ReadUserOutXml;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private Handler handler;
	PopupWindow popupWindowLogo;
	PopupWindow popupWindowWelcome;
	View logoPop;
	View welcomePop;

	String userInfo = "userInfo"; // 存储user的xml文件名

	// View view
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		MyApplication.getInstance().addActivity(this);

		logoPop = getLayoutInflater().inflate(R.layout.popwindow_logo, null,
				false);
		popupWindowLogo = new PopupWindow(logoPop,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popupWindowLogo.setFocusable(true);
		popupWindowLogo.setOutsideTouchable(false);
		// 设置动画效果
		popupWindowLogo.setAnimationStyle(R.style.AnimationFade);
		// ------------------------------
		welcomePop = getLayoutInflater().inflate(R.layout.popwindow_welcome,
				null, false);
		popupWindowWelcome = new PopupWindow(welcomePop,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popupWindowWelcome.setFocusable(true);
		popupWindowWelcome.setOutsideTouchable(false);
		// 设置动画效果
		popupWindowWelcome.setAnimationStyle(R.style.AnimationFade);

		new Thread(new PauseRun()).start();

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					popupWindowLogo.showAtLocation(logoPop,
							Gravity.CENTER_HORIZONTAL, 10, 60);
					break;
				case 1:
					popupWindowWelcome.showAtLocation(welcomePop,
							Gravity.CENTER_HORIZONTAL, 10, 100);
					break;
				case 2:
					if (null != popupWindowLogo && popupWindowLogo.isShowing())
						popupWindowLogo.dismiss();
					if (null != popupWindowWelcome
							&& popupWindowWelcome.isShowing())
						popupWindowWelcome.dismiss();
					break;
				case 3:
					initUserInfo();
				default:
					break;
				}
			}

		};
	}

	public void initUserInfo() {
		// 尝试读取xml中的user对象，如果有直接Intent到MainActivity,否则到登录页面
		SharedPreferences preferences = getSharedPreferences(userInfo,
				MODE_PRIVATE);
		String productUser = preferences.getString("user", null);
		if (productUser != null) {
			User user = ReadUserOutXml.readUser(productUser);
			Intent intent = new Intent();
			intent.setClass(SplashActivity.this, MainActivity.class);
			// 使用Bundle传递user对象
			// 创建一个Bundle对象
			if (user != null) {
				Bundle userObjcet = new Bundle();
				userObjcet.putSerializable("user", (Serializable) user);
				intent.putExtras(userObjcet);
			}
			if (null != popupWindowLogo && popupWindowLogo.isShowing())
				popupWindowLogo.dismiss();
			if (null != popupWindowWelcome
					&& popupWindowWelcome.isShowing())
				popupWindowWelcome.dismiss();
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(SplashActivity.this, LogActivity.class);
			startActivity(intent);
			finish();
		}
	}

	class PauseRun implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				handler.sendEmptyMessage(0);
				Thread.sleep(1000);
				handler.sendEmptyMessage(1);
//				Thread.sleep(1000);
//				handler.sendEmptyMessage(2);
				Thread.sleep(1000);
				handler.sendEmptyMessage(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
