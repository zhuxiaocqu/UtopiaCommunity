package com.utopia.activity;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ilive.request.UserCheckLoginRequest;
import com.ilive.response.UserCheckLoginResponse;
import com.ilive.structs.User;
import com.utopia.tools.CustomProgressDialog;
import com.utopia.tools.EditTextBackTxtHelper;
import com.utopia.tools.WriteUserIntoXml;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class LogActivity extends Activity {
	private EditText editText_account; // 用户名输入框
	private EditText editText_pwd; // 密码输入框
	private ImageView button_eye; // 单击查看密码
	private TextView forgotpassword; // 忘记密码提示
	private String defaultAccountStr; // 用户民框默认提示语句
	private String defaultPwdStr; // 密码框默认提示语句
	private long logResponseCode = 0;
	private boolean logResult = false;

	private PopupWindow popupWindow;
	String userInfo = "userInfo"; // 存储user的xml文件名
	private CustomProgressDialog progressDialog = null; // 登录等待进度条
	AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_log);
		MyApplication.getInstance().addActivity(this);
		aq = new AQuery(this);

		Resources res = getResources();
		defaultAccountStr = res
				.getString(R.string.log_accountEditText_defaultStr);
		defaultPwdStr = res.getString(R.string.log_pwdEditText_defaultStr);
		// 为注册TextView绑定跳转到注册Activity事件
		aq.id(R.id.TextView_LogActivity_toRegister).clicked(this,
				"registerCilcked");

		// 为登录account的EditText绑定事件，当没有输入内容时，内容为“请输入账号”
		editText_account = (EditText) findViewById(R.id.EditText_LogActivity_account);
		editText_account.setSelection(0);
		EditTextBackTxtHelper editText_account_helper = new EditTextBackTxtHelper(
				editText_account, defaultAccountStr, InputType.TYPE_CLASS_TEXT,
				10);
		editText_account.addTextChangedListener(editText_account_helper);
		editText_account.setOnFocusChangeListener(editText_account_helper);

		// 为登录Pwd的EditText绑定事件，当没有输入内容时，内容为“请输入密码”
		editText_pwd = (EditText) findViewById(R.id.EditText_LogActivity_pwd);
		editText_pwd
				.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		EditTextBackTxtHelper editText_pwd_helper = new EditTextBackTxtHelper(
				editText_pwd, defaultPwdStr, InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, 6);
		editText_pwd.setOnFocusChangeListener(editText_pwd_helper);
		editText_pwd.addTextChangedListener(editText_pwd_helper);

		// 显示密码按钮，当该控件获取焦点时，密码显示出来，失去焦点时，密码变成原点
		button_eye = (ImageView) findViewById(R.id.ImageView_LogActivity_eye);
		button_eye.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (!editText_pwd.getText().toString()
							.equals(defaultPwdStr))
						editText_pwd
								.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!editText_pwd.getText().toString()
							.equals(defaultPwdStr))
						editText_pwd.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				return false;
			}
		});

		// 为登录按钮绑定事件
		aq.id(R.id.Button_LogActivity_login).clicked(this, "logButtonClicked");

		// 为退出按钮绑定事件
		aq.id(R.id.ImageView_LogActivity_quit).clicked(this,
				"quitTextViewClicked");

		// 为忘记密码绑定事件
		forgotpassword = (TextView) findViewById(R.id.TextView_LogActivity_forgotpwd);
		forgotpassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		forgotpassword.getPaint().setAntiAlias(true);
		forgotpassword.setClickable(true);
		forgotpassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPopFun(LogActivity.this, "确定拨打管理处电话\n10086 找回密码？", 1);
			}
		});

	}

	@Override
	protected void onDestroy() {
		stopProgressDialog();
		super.onDestroy();
	}

	// 显示登录进度条
	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在登录...");
		}
		progressDialog.show();
	}

	// 关闭登录进度条
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void registerCilcked() {
		Intent intent = new Intent();
		intent.setClass(LogActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	// 登录按钮监听函数----------------------->预留函数，根据返回code判断登录情况
	public void logButtonClicked() throws JSONException {

		String username = editText_account.getText().toString();
		String pwd = editText_pwd.getText().toString();

		// 离线登录
		// logToMainActivity(null);

		// 在线登录
		UserCheckLoginRequest logRequest = new UserCheckLoginRequest(aq,
				"logCallBack");
		logRequest.request(username, pwd);
		// 使用进度条，定时5秒为登录最长时间，否则登录超时，提示登录超时，检查网络。
		// 进度条开始显示。。
		startProgressDialog();

	}

	// 退出按钮监听函数
	public void quitTextViewClicked() {
		selectPopFun(LogActivity.this, "确定退出助手？", 0);
	}

	// 0 为退出 ，1为找回密码
	public void selectPopFun(Context context, String Info, int type) {
		final View jumpDialog = LayoutInflater.from(context).inflate(
				R.layout.dialogstyle, null, false);
		jumpDialog.getBackground().setAlpha(190);
		BitmapDrawable bd = (BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.conv_tel_dig);
		int height = bd.getBitmap().getHeight();
		int width = bd.getBitmap().getWidth();

		popupWindow = new PopupWindow(jumpDialog, width, height, true);
		AQuery aq = new AQuery(jumpDialog);
		aq.id(R.id.textview).text(Info);

		popupWindow.setAnimationStyle(R.style.AnimationFade);
		popupWindow.showAtLocation(jumpDialog, Gravity.CENTER, 0, 0);
		clickevent(jumpDialog, type);
	}

	public void clickevent(View jumpDialog, final int type) {
		AQuery aqDialog = new AQuery(jumpDialog);

		aqDialog.id(R.id.but1).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				if (type == 0) {
					MyApplication.getInstance().exit();
				} else if (type == 1) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + "10086"));
					startActivity(intent);
				}
			}
		});
		aqDialog.id(R.id.but2).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	// 登录的回调函数
	public void logCallBack(String url, JSONObject json, AjaxStatus status)
			throws JSONException {
		System.out.println("jump into log callback!!");
		if (json != null) {
			UserCheckLoginResponse logResponse = new UserCheckLoginResponse(
					json);
			logResult = logResponse.getResult();
			logResponseCode = logResponse.getCode();
			User user = logResponse.getUser();
			if (logResult) {
				System.out.println("登录成功！");

				// 将User写入xml
				WriteUserIntoXml.writeUser(LogActivity.this, user, userInfo);
				logToMainActivity(logResponse.getUser());
			} else {
				// 登录失败
				logFailed();
			}
		} else {
			logResponseCode = status.getCode();
			// 页面访问错误
			accessError(status.getCode());
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
	}

	public void logToMainActivity(User user) {
		Intent intent = new Intent();
		intent.setClass(LogActivity.this, MainActivity.class);
		// 使用Bundle传递user对象
		// 创建一个Bundle对象
		if (user != null) {
			Bundle userObjcet = new Bundle();
			userObjcet.putSerializable("user", (Serializable) user);
			intent.putExtras(userObjcet);
			// 进度条结束
		}
		// progressDialog.cancel();
		startActivity(intent);
		// stopProgressDialog();

	}

	public void logFailed() {
		stopProgressDialog();
		new AlertDialog.Builder(this).setTitle("登录失败").setMessage("用户名或密码错误!")
				.setPositiveButton("是", null).show();
	}

	public void accessError(int statusCode) {
		stopProgressDialog();
		new AlertDialog.Builder(this).setTitle("网络错误")
				.setMessage("页面访问" + statusCode + "错误!")
				.setPositiveButton("是", null).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

}
