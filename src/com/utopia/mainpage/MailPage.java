package com.utopia.mainpage;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ilive.request.LettersUploadRequest;
import com.ilive.response.LetterUploadResponse;
import com.utopia.activity.MainActivity;
import com.utopia.activity.R;
import com.utopia.adapter.MailViewAdapter;
import com.utopia.structs.MailMessage;
import com.utopia.tools.ReadObjectFromFile;
import com.utopia.tools.WriteObjectToFile;

public class MailPage {

	private AQuery aqMail;
	private Context context;
	private String Package="com.broadlink.switchproduct";
	private String Activity="com.broadlink.switchproduct.activity.LoadingActivity";

	public MailPage(View viewGroup) {
		initData(viewGroup);
		aqMail.id(R.id.imageview_mail_smart).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isSmartHomeExsit()){
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					ComponentName comp = new ComponentName(
							Package,
							Activity);
					intent.setComponent(comp);
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "没有检测到智能插座APP", 2000).show();
				}
			}
		});
	}
	
	public boolean isSmartHomeExsit(){
		boolean start=true;
		try {
			PackageManager pm = context.getPackageManager();
			pm.getPackageInfo(Package, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
//			Toast.makeText(context, "不存在包", 1000).show();
			start=false;
			e.printStackTrace();
		}

//		try {
//			Class.forName(".activity.LoadingActivity");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			Toast.makeText(context, "不存在类", 1000).show();
//			start=false;
//			//return;
//		}
		return start;
	}

	public void initData(View viewGroup) {
		context = viewGroup.getContext();
		aqMail = new AQuery(viewGroup);
	}
}
