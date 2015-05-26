package com.utopia.activity;

import org.json.JSONException;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ilive.request.UserCheckUniquenessRequest;
import com.ilive.request.UserCreateRequest;
import com.ilive.response.UserCheckUniquenessResponse;
import com.ilive.response.UserCreateResponse;
import com.ilive.structs.User;
import com.utopia.tools.EditTextBackTxtHelper;
import com.utopia.tools.WriteUserIntoXml;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity {

	AQuery aq;
	/**
	 * 创建用户
	 * 
	 * @param account
	 *            : 用户账号*
	 * @param name
	 *            : 用户名*
	 * @param password
	 *            : 用户密码*
	 * @param groupid
	 *            : 用户所在小区编号*
	 * @param sex
	 *            : 用户性别，性别代号：0 男 1女 2其他
	 * @param description
	 *            : 用户个人描述
	 * @param phone_number
	 *            : 用户电话号码
	 * @param imei
	 *            : 手机串号
	 * @param address
	 *            : 用户地址（单元-楼层-门牌号）
	 * @param upfine
	 *            : 附图地址
	 */
	// 变量
	private String account = null; // 用户账户（唯一）
	private String name = null; // 用户名
	private String password = null; // 用户密码
	private Long sex = null; // 性别代号：0 男 1女 2其他
	private String description = null; // 个人描述
	private Long is_vip = null; // 是否为实名用户
	private String portrait_pic = null; // 头像
	private Long groupid = null; // 分组（分区）编号
	private String phone_number = null; // 手机号码
	private String imei = null; // 手机机器码
	private String address = null; // 地址（单元-楼层-门牌号）
	private String created_at = null; // 创建时间
	private String upfile = null;

	// UI提示字符串
	private String defaultAccountStr;
	private String defaultPwdStr;
	private String defaultAckPwdStr;
	private String defaultGroupIdStr;
	private String defaultNameStr;
	private String defaultVipStr;
	private String defaultPhoneNumStr;
	private String defaultAddressStr;
	private String strMale;
	private String strFemale;
	private String strSecret;
	private String strSex = null;
	// 定义单个测试用户名是否存在的现场对象
	JustTestUserHave testHavaThread = null;
	// 定义一个缓存字符串，避免每次焦点切换都会测试
	String buffUserNameStr = "";
	// 控件，不用AQuery因为在AQuery中没有找到setOnFocusChangeListener（）的事件监听器
	EditText editText_RegisterAccount;
	EditText editText_RegisterPwd;
	EditText editText_RegisterAckPwd;
	EditText editText_RegisterGroupId;
	EditText editText_RegisterName;
	RadioGroup radioGroup_RegisterSex;
	EditText editText_RegisterISVIP;
	EditText editText_RegisterPhoneNumber;
	EditText editText_RegisterAddress;

	String RegisterAccount = "";
	String RegisterPwd = "";

	boolean hasAccount = false;
	String userInfo = "userInfo"; // 存储user的xml文件名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		MyApplication.getInstance().addActivity(this);
		aq = new AQuery(this);
		// -------------------------输入账号密码注册----------------
		// 从String.xml文件中得到UserName EditText和Pwd EditText 中默认显示的提示字符串
		Resources res = getResources();
		defaultAccountStr = res
				.getString(R.string.register_accountEditText_defaultStr);
		defaultPwdStr = res.getString(R.string.register_pwdEditText_defaultStr);
		defaultAckPwdStr = res
				.getString(R.string.register_ackpwdEditText_defaultStr);
		defaultGroupIdStr = res
				.getString(R.string.register_groupidEditText_defaultStr);
		defaultNameStr = res
				.getString(R.string.register_nameEditText_defaultStr);
		defaultVipStr = res
				.getString(R.string.register_isVipEditText_defaultStr);
		defaultPhoneNumStr = res
				.getString(R.string.register_phoneNumberEditText_defaultStr);
		defaultAddressStr = res
				.getString(R.string.register_addressEditText_defaultStr);

		strMale = res.getString(R.string.male);
		strFemale = res.getString(R.string.female);
		strSecret = res.getString(R.string.secret);

		// ----------------------------* account EditText---------,* 表示必填内容，不能为空
		// 为登录account的EditText绑定事件，当没有输入内容时，内容为“请输入账号”,当失去焦点时进行一次验证是否用户名被使用
		editText_RegisterAccount = (EditText) findViewById(R.id.EditText_RegisterActivity_account);
		editText_RegisterAccount.setSelection(0);
		EditTextBackTxtHelper editText_account_helper = new EditTextBackTxtHelper(
				editText_RegisterAccount, defaultAccountStr,
				InputType.TYPE_CLASS_TEXT, 10);
		editText_RegisterAccount
				.addTextChangedListener(editText_account_helper);
		editText_RegisterAccount.setOnFocusChangeListener(new FocusChange(
				editText_RegisterAccount, defaultAccountStr, 1));

		// ----------------------------* pwd EditText---------
		// 为登录Pwd的EditText绑定事件，当没有输入内容时，内容为“请输入密码”
		editText_RegisterPwd = (EditText) findViewById(R.id.EditText_RegisterActivity_pwd);
		EditTextBackTxtHelper editText_pwd_helper = new EditTextBackTxtHelper(
				editText_RegisterPwd, defaultPwdStr, InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, 6);
		editText_RegisterPwd.addTextChangedListener(editText_pwd_helper);
		editText_RegisterPwd.setOnFocusChangeListener(editText_pwd_helper);

		// ----------------------------* ackpwd EditText---------
		// 为登录Pwd的EditText绑定事件，当没有输入内容时，内容为“请输入密码”
		editText_RegisterAckPwd = (EditText) findViewById(R.id.EditText_RegisterActivity_ackpwd);
		EditTextBackTxtHelper editText_ackpwd_helper = new EditTextBackTxtHelper(
				editText_RegisterAckPwd, defaultAckPwdStr,
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, 6);
		editText_RegisterAckPwd.addTextChangedListener(editText_ackpwd_helper);
		editText_RegisterAckPwd
				.setOnFocusChangeListener(editText_ackpwd_helper);

		// ----------------------------* group_id EditText---------
		editText_RegisterGroupId = (EditText) findViewById(R.id.EditText_RegisterActivity_groupid);
		EditTextBackTxtHelper editText_groupid_helper = new EditTextBackTxtHelper(
				editText_RegisterGroupId, defaultGroupIdStr,
				InputType.TYPE_CLASS_TEXT, 6);
		editText_RegisterGroupId
				.addTextChangedListener(editText_groupid_helper);
		editText_RegisterGroupId
				.setOnFocusChangeListener(editText_groupid_helper);

		// ----------------------------name EditText---------
		editText_RegisterName = (EditText) findViewById(R.id.EditText_RegisterActivity_name);
		EditTextBackTxtHelper editText_name_helper = new EditTextBackTxtHelper(
				editText_RegisterName, defaultNameStr,
				InputType.TYPE_CLASS_TEXT, 10);
		editText_RegisterName.addTextChangedListener(editText_name_helper);
		editText_RegisterName.setOnFocusChangeListener(editText_name_helper);

		// ----------------------------sex RadioButton---------
		radioGroup_RegisterSex = (RadioGroup) findViewById(R.id.RadioGroup_RegisterActivity_sex);
		radioGroup_RegisterSex
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@SuppressLint("UseValueOf")
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int arg1) {
						int radioButtonId = radioGroup
								.getCheckedRadioButtonId();
						RadioButton rb = (RadioButton) RegisterActivity.this
								.findViewById(radioButtonId);
						strSex = rb.getText().toString();
						if (strSex.equals(strMale)) {
							sex = new Long(0);
						} else if (strSex.equals(strFemale)) {
							sex = new Long(1);
						} else {
							sex = new Long(2);
						}
					}
				});

		// ----------------------------phone number EditText---------
		editText_RegisterPhoneNumber = (EditText) findViewById(R.id.EditText_RegisterActivity_phoneNumber);
		EditTextBackTxtHelper editText_phonenumber_helper = new EditTextBackTxtHelper(
				editText_RegisterPhoneNumber, defaultPhoneNumStr,
				InputType.TYPE_CLASS_TEXT, 11);
		editText_RegisterPhoneNumber
				.addTextChangedListener(editText_phonenumber_helper);
		editText_RegisterPhoneNumber
				.setOnFocusChangeListener(editText_phonenumber_helper);

		// ----------------------------address EditText---------
		editText_RegisterAddress = (EditText) findViewById(R.id.EditText_RegisterActivity_address);
		EditTextBackTxtHelper editText_address_helper = new EditTextBackTxtHelper(
				editText_RegisterAddress, defaultAddressStr,
				InputType.TYPE_CLASS_TEXT, 30);
		editText_RegisterAddress
				.addTextChangedListener(editText_address_helper);
		editText_RegisterAddress
				.setOnFocusChangeListener(editText_address_helper);

		// ----------------------------register Button---------
		// -------------------------提交注册信息----------------
		aq.id(R.id.Button_RegisterActivity_register).clicked(this,
				"RegisterCilcked");

		// 返回到登录页面
		aq.id(R.id.ImageView_RegisterActivity_backToLog).clicked(this,
				"backToLog");
		aq.id(R.id.TextView_RegisterActivity_toLog).clicked(this, "backToLog");
	}

	// 设置一个EditText的事件监听器，控制默认字符串的显示， type =1 表示开启线程，用于用户账户注册， type =0 表示不做处理
	class FocusChange implements View.OnFocusChangeListener {
		private String defaultStr;
		private EditText editText;
		private int type = 0;

		public FocusChange(EditText editText, String defaultStr, int type) {
			this.defaultStr = defaultStr;
			this.editText = editText;
			this.type = type;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			String textStr = editText.getText().toString();
			if (hasFocus) {
				if (textStr.equals(defaultStr))
					editText.setSelection(0);
				else
					editText.setSelection(editText.getText().length());
			} else {
				if (textStr.equals("")) {
					editText.setTextColor(R.color.edittext_backtxt_color);
					editText.setText(defaultStr);

				} else {
					if (type == 1) {
						if (!buffUserNameStr.equals(textStr)) {
							initThreadObject();
							testHavaThread.start();
							buffUserNameStr = textStr;
						}
					}
				}
			}
		}
	}

	// 回到登录页面的按钮响应函数
	public void backToLog() {
		Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, LogActivity.class);
		startActivity(intent);
	}

	// 初始化用户名测试线程对象
	public void initThreadObject() {
		testHavaThread = new JustTestUserHave();
	}

	// 创建成员内部类，使用多线程单个测试用户填写的注册用户名是否被占用
	class JustTestUserHave extends Thread {
		public void run() {
			UserCheckUniquenessRequest usernameCheckRequest = new UserCheckUniquenessRequest(
					aq, "usernameCheckCallback");
			usernameCheckRequest.request(editText_RegisterAccount.getText()
					.toString());

			System.out.println("注册账号为："
					+ editText_RegisterAccount.getText().toString());
		}
	}

	// 函数Register用于注册Button按下后的事件响应
	public void RegisterCilcked(View button) throws JSONException {
		System.out.println("register button clicked!");
		boolean canRegister = false;
		// 如果线程alive就一直循环
		while (true) {
			if (testHavaThread == null || !testHavaThread.isAlive()) {
				// 提交所有注册信息
				// 获得所有已填写的注册的信息,前三个为必填
				if (judgeStrOfSubmit(editText_RegisterAccount,
						defaultAccountStr, "账号"))
					break;
				account = editText_RegisterAccount.getText().toString(); // *账号
				System.out.println("tag3");
				if (judgeStrOfSubmit(editText_RegisterPwd, defaultPwdStr, "密码"))
					break;
				if (judgeStrOfSubmit(editText_RegisterAckPwd, defaultAckPwdStr,
						"确认密码"))
					break;
				if (!editText_RegisterPwd.getText().toString()
						.equals(editText_RegisterAckPwd.getText().toString())) {
					Toast.makeText(this, "确认密码错误，请重新输入", Toast.LENGTH_LONG)
							.show();
					editText_RegisterAckPwd.hasFocus();
					editText_RegisterAckPwd.setText("");
					break;
				}
				password = editText_RegisterPwd.getText().toString(); // *密码
				groupid = new Long(1); // *分组（分区）编号 目前设置为默认值
				name = finalStrOfSubmit(editText_RegisterName, defaultNameStr); // 昵称
				phone_number = finalStrOfSubmit(editText_RegisterPhoneNumber,
						defaultPhoneNumStr);// 电话号码

				TelephonyManager telephonyManager = (TelephonyManager) this
						.getSystemService(Context.TELEPHONY_SERVICE);
				imei = telephonyManager.getDeviceId();// 手机机器码
				address = finalStrOfSubmit(editText_RegisterAddress,
						defaultAddressStr); // 地址
				canRegister = true;
				break;
			}
		}
		if (canRegister) {
			UserCreateRequest userCreatResquest = new UserCreateRequest(aq,
					"userCreateCallBack");
			userCreatResquest.request(account, name, password, groupid, sex,
					description, is_vip, phone_number, imei, address);
		}
	}

	// 对前三个必填EditText进行非空处理
	public boolean judgeStrOfSubmit(EditText editText, String defaultStr,
			String infoStr) {
		String str = null;
		boolean isNull = true;
		str = editText.getText().toString();
		if (str.equals(defaultStr) || str.equals("")) {
			Toast.makeText(this, infoStr + "不能为空", Toast.LENGTH_LONG).show();
			editText.hasFocus();
			editText.setText("");
		} else {
			isNull = false;
		}
		return isNull;
	}

	// 获取EditText中最终要提交的字符串，排除默认字符串
	public String finalStrOfSubmit(EditText editText, String defaultStr) {
		String str = null;
		str = editText.getText().toString();
		if (str.equals(defaultStr))
			str = "";
		return str;
	}

	public void userCreateCallBack(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		System.out.println("jump into userCreate callback!!");
		if (json != null) {
			UserCreateResponse userCreateResponse = new UserCreateResponse(json);
			long responseCode = userCreateResponse.getCode();
			User user = userCreateResponse.getUser();
			if (responseCode == 200) {
				// 注册成功
				registerSuccessed();
				// 将User写入xml
				WriteUserIntoXml.writeUser(RegisterActivity.this, user,
						userInfo);
			} else {
				// 注册失败
				registerFailed();
			}
		} else {
			accessError(status.getCode());
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
	}

	public void registerSuccessed() {
		new AlertDialog.Builder(this).setTitle("注册成功")
				.setMessage("恭喜您，新用户注册成功！\n账号：" + account + "\n密码：" + password)
				.setPositiveButton("是", null).show();
	}

	public void registerFailed() {
		new AlertDialog.Builder(this).setTitle("注册失败")
				.setMessage("对不起注册失败。是否重试？").setPositiveButton("是", null)
				.show();
	}

	public void accessError(int statusCode) {
		new AlertDialog.Builder(this).setTitle("网络错误")
				.setMessage("页面访问" + statusCode + "错误!")
				.setPositiveButton("是", null).show();
	}

	// 回调函数
	public void usernameCheckCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		System.out.println("jump into username check callback!!");
		boolean accountHavaResult = false;
		if (json != null) {
			System.out.println("usernameCheckCallback json:------>"
					+ json.toString());
			UserCheckUniquenessResponse usernameCheckResponse = new UserCheckUniquenessResponse(
					json);
			accountHavaResult = usernameCheckResponse.getResult();
			long responseCode = usernameCheckResponse.getCode();
			// 已存在 accountHavaResult 为false,code =302,不存在为true code=200,
			if (!accountHavaResult) {
				Toast.makeText(RegisterActivity.this, "用户名已存在，不能使用该用户名",
						Toast.LENGTH_LONG).show();
				editText_RegisterAccount.setText("");
			} else {
				Toast.makeText(RegisterActivity.this, "用户名不存在，可以使用",
						Toast.LENGTH_LONG).show();
			}
		} else {
			accessError(status.getCode());
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
