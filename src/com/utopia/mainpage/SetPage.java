package com.utopia.mainpage;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.utopia.activity.AboutActivity;
import com.utopia.activity.LogActivity;
import com.utopia.activity.MainActivity;
import com.utopia.activity.MyAccountActivity;
import com.utopia.activity.MyApplication;
import com.utopia.activity.NormalUseActivity;
import com.utopia.activity.NoticeSetActivity;
import com.utopia.activity.R;
import com.utopia.tools.ClearLocalCache;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SetPage {
	private AQuery aqAdmain;
	private Context context;
	private PopupWindow popupWindow;

	private final int POP_CLEAR = 0;
	private final int POP_EXIT = 2;

	public SetPage(View viewGroup) {
		context = viewGroup.getContext();
		aqAdmain = new AQuery(viewGroup);
		setViewEvent();

	}

	public void jump(Context context, Class cl) {
		Intent intent = new Intent(context, cl);
		context.startActivity(intent);
	}

	public void setViewEvent() {
		aqAdmain.id(R.id.relative_settings_firstlay).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						jump(context, MyAccountActivity.class);
					}
				});

		aqAdmain.id(R.id.relative_settings_secondlay_normal).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						jump(context, NormalUseActivity.class);
					}
				});
		aqAdmain.id(R.id.relative_settings_secondlay_notice).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						jump(context, NoticeSetActivity.class);
					}
				});
		aqAdmain.id(R.id.relative_settings_secondlay_clear).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						selectPopFun(context, POP_CLEAR);
					}
				});

		aqAdmain.id(R.id.relative_settings_thirdlay).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						jump(context, AboutActivity.class);
					}
				});

		aqAdmain.id(R.id.button_settings_forthlay).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						selectPopFun(context, POP_EXIT);
					}
				});

	}

	public void selectPopFun(Context context, int popFlag) {
		View jumpDialog = null;
		int height = 0;
		int width = 0;
		BitmapDrawable bd;
		AQuery aq;
		if (popFlag == POP_EXIT) {
			jumpDialog = LayoutInflater.from(context).inflate(
					R.layout.dialogstyle, null, false);
			jumpDialog.getBackground().setAlpha(190);
			bd = (BitmapDrawable) context.getResources().getDrawable(
					R.drawable.conv_tel_dig);
			height = bd.getBitmap().getHeight();
			width = bd.getBitmap().getWidth();
			aq = new AQuery(jumpDialog);
			aq.id(R.id.textview).text("确定注销登录？");
		} else if (popFlag == POP_CLEAR) {
			jumpDialog = LayoutInflater.from(context).inflate(
					R.layout.dialogstyle, null, false);
			jumpDialog.getBackground().setAlpha(190);
			bd = (BitmapDrawable) context.getResources().getDrawable(
					R.drawable.conv_tel_dig);
			height = bd.getBitmap().getHeight();
			width = bd.getBitmap().getWidth();
			aq = new AQuery(jumpDialog);
			aq.id(R.id.textview).text("您的本地文件将会全部缓存！");
		}
		popupWindow = new PopupWindow(jumpDialog, width, height, true);
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		popupWindow.showAtLocation(jumpDialog, Gravity.CENTER, 0, 0);
		clickevent(jumpDialog, popFlag);
	}

	public void clickevent(View jumpDialog, final int popFlag) {
		AQuery aqDialog = new AQuery(jumpDialog);
		aqDialog.id(R.id.but1).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(context, "press confirm", 2000).show();
				if (popFlag == POP_EXIT) {
					Intent toLogin = new Intent(context, LogActivity.class);
					toLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// MyApplication.getInstance().clearMainActivity();
					context.startActivity(toLogin);
					ClearLocalCache clearTool = new ClearLocalCache(context);
					clearTool.clear();
					// 将MainActivity清除
					MyApplication.getInstance().clearMainActivity();
				}
				// 清除栈中保存的所有Activity，退出程序

				if (popFlag == POP_CLEAR) {
					Toast.makeText(context, "清理缓存中...", 2000).show();
					AQUtility.cleanCacheAsync(MainActivity.context);
					ClearLocalCache clearTool = new ClearLocalCache(context);
					if (clearTool.clear())
						Toast.makeText(context, "清理完毕", 2000).show();
					else
						Toast.makeText(context, "清理失败", 2000).show();
				}
				// MainActivity.this.finish();
				popupWindow.dismiss();
			}
		});
		aqDialog.id(R.id.but2).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(context, "press cancel", 2000).show();
				popupWindow.dismiss();
			}
		});
	}
}
