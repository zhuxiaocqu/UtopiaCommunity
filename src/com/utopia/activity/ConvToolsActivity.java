package com.utopia.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ilive.request.StoreTimelineRequest;
import com.ilive.response.StoreTimelineResponse;
import com.ilive.structs.Store;
import com.utopia.structs.Flags;
import com.utopia.tools.CustomProgressDialog;
import com.utopia.tools.ShowMarketList;
import com.utopia.tools.ShowMedicalList;
import com.utopia.tools.ShowServiceList;
import com.utopia.tools.ShowTakeoutList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConvToolsActivity extends Activity {
	AQuery aq;
	// 每一页的名字
	private String Str_takeout = "外卖";
	private String buttonLookTakeoutText;
	private long takeoutRequestTypeId = 1;
	private String Str_market = "超市";
	private String buttonLookMarketText;
	private long marketRequestTypeId = 2;
	private String Str_medical = "医疗";
	private String buttonLookMedicalText;
	private long medicalRequestTypeId = 3;
	private String Str_service = "服务";
	private String buttonLookServiceText;
	private long serviceRequestTypeId = 4;

	// 定义四个全局List变量
	private List<Store> takeoutList = null;
	private List<Store> marketList = null;
	private List<Store> medicalList = null;
	private List<Store> serviceList = null;

	private ViewPager ViewPager_ConvTools;// 页卡内容
	private ImageView imageView_cousor;// cursor图片
	private TextView textView_takeout, textView_market, textView_medical,
			textView_service;
	private ArrayList<View> viewsConvTools;// Tab页面列表
	private ArrayList<String> titles;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View view_takeout, view_market, view_medical, view_service;// 各个页卡
	private int toolKind; // 从MainActivity得到的信息，先显示哪个页面

	private final String convtoolsBuffInfoFile = "convtoolsBuff"; // /SharedPrefereces的xml文件名，用于存储List<Store>
																	// 对象，关键词key="stores"
	private int flag;
	private CustomProgressDialog progressDialog = null; // 刷新等待进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conv_tools);
		MyApplication.getInstance().addActivity(this);
		aq = new AQuery(this);

		flag = 0;
		Intent intent = getIntent();
		toolKind = intent.getIntExtra("toolKind", 0);

		// 初始化查看按钮的文本内容，从String.xml中获取
		Resources res = getResources();
		buttonLookTakeoutText = res.getString(R.string.buttontext_takeout_look);
		buttonLookMarketText = res.getString(R.string.buttontext_market_look);
		buttonLookMedicalText = res.getString(R.string.buttontext_medical_look);
		buttonLookServiceText = res.getString(R.string.buttontext_service_look);
		// 为返回按钮绑定事件
		aq.id(R.id.ImageView_ConvToolsActivity_back).clicked(this,
				"backButtonClicked");
		// 为刷新按钮绑定事件
		aq.id(R.id.ImageView_ConvToolsActivity_update).clicked(this,
				"updateButtonClicked");

		InitImageView();
		InitTextView();
		try {
			InitViewPager();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		intentPage(toolKind);
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
			progressDialog.setMessage("正在加载...");
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

	public void backButtonClicked() {
		Intent intent = new Intent();
		intent.setClass(ConvToolsActivity.this, MainActivity.class);
		intent.putExtra(Flags.CHOOSE_PAGE_FLAG, 2);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void updateButtonClicked() throws InterruptedException {
		updateStoreInfo(aq);
	}

	// 点击后跳转到第几个工具页面
	public void intentPage(int toolIndex) {
		ViewPager_ConvTools.setCurrentItem(toolIndex);
	}

	// 重写PageAdapter
	class ConvToolsPagerAdapter extends PagerAdapter {

		private ArrayList<View> views;
		private ArrayList<String> titles;

		public ConvToolsPagerAdapter(ArrayList<View> views,
				ArrayList<String> titles) {
			this.views = views;
			this.titles = titles;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}
	}

	// 初始化ViewPager
	private void InitViewPager() throws InterruptedException {
		ViewPager_ConvTools = (ViewPager) findViewById(R.id.ViewPager_convTools);

		// 将要分页的View装入数组
		LayoutInflater layoutInfConvTools = LayoutInflater.from(this);
		view_takeout = layoutInfConvTools.inflate(R.layout.convtools_takeout,
				null);
		view_market = layoutInfConvTools.inflate(R.layout.convtools_market,
				null);
		view_medical = layoutInfConvTools.inflate(R.layout.convtools_medical,
				null);
		view_service = layoutInfConvTools.inflate(R.layout.convtools_service,
				null);

		SharedPreferences preferences = getSharedPreferences(
				convtoolsBuffInfoFile, MODE_PRIVATE);
		String productBase64 = preferences.getString("stores", null);
		if (productBase64 == null) {
			updateStoreInfo(aq);
		} else {
			List<Store> stores = readConvToolsInfo(convtoolsBuffInfoFile,
					productBase64);
			if (stores == null || stores.isEmpty()) {
				updateStoreInfo(aq);
			} else
				try {
					initStoreInfo(stores);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

		// 每个页面的Title数据
		viewsConvTools = new ArrayList<View>();
		viewsConvTools.add(view_takeout);
		viewsConvTools.add(view_market);
		viewsConvTools.add(view_medical);
		viewsConvTools.add(view_service);

		// 将每一页的名字填入ArrayList
		titles = new ArrayList<String>();
		titles.add(Str_takeout);
		titles.add(Str_market);
		titles.add(Str_medical);
		titles.add(Str_service);

		// 填充ViewPager的Adapter
		ViewPager_ConvTools.setAdapter(new ConvToolsPagerAdapter(
				viewsConvTools, titles));
		ViewPager_ConvTools.setCurrentItem(0);
		ViewPager_ConvTools
				.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public void initStoreInfo(List<Store> stores) throws InterruptedException {
		takeoutList = new ArrayList<Store>();
		marketList = new ArrayList<Store>();
		medicalList = new ArrayList<Store>();
		serviceList = new ArrayList<Store>();
		for (Store s : stores) {
			int typeid = s.getTypeid().intValue();
			switch (typeid) {
			case 1:
				takeoutList.add(s);
				break;
			case 2:
				marketList.add(s);
				break;
			case 3:
				medicalList.add(s);
				break;
			case 4:
				serviceList.add(s);
				break;
			}
		}
		ShowTakeoutList takeout = new ShowTakeoutList(view_takeout,
				takeoutList, buttonLookTakeoutText);
		takeout.showTakeout();

		ShowMarketList market = new ShowMarketList(view_market, marketList,
				buttonLookMarketText);
		market.showMarket();

		ShowMedicalList medical = new ShowMedicalList(view_medical,
				medicalList, buttonLookMedicalText);
		medical.showMedical();

		ShowServiceList service = new ShowServiceList(view_service,
				serviceList, buttonLookServiceText);
		service.showService();

	}

	public void showToast() {
		System.out.println("flag:" + flag);
		if (flag >= 4) {
			Toast.makeText(ConvToolsActivity.this, "更新完毕", Toast.LENGTH_LONG)
					.show();
		} else if (flag <= -4) {
			stopProgressDialog();
			Toast.makeText(ConvToolsActivity.this, "更新失败", Toast.LENGTH_LONG)
					.show();
		}
	}

	// 联网获取数据，更新店铺信息
	public void updateStoreInfo(AQuery aq) throws InterruptedException {
		// 启动刷新的进度条
		startProgressDialog();
		// 开启一个线程，当flag>=4时，就存入数据到本地文件中
		new Thread() {
			public void run() {
				System.out.println("开启线程-------");
				while (true) {
					if (flag >= 4) {
						stopProgressDialog();
						// Toast.makeText(ConvToolsActivity.this,"更新成功",Toast.LENGTH_LONG).show();
						try {
							writeConvToolsInfo(convtoolsBuffInfoFile,
									takeoutList, marketList, medicalList,
									serviceList);
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("---图片加载完成");
						break;
					} else if (flag <= -4) {
						stopProgressDialog();
						break;
					}

				}
				System.out.println("------线程结束");
			}
		}.start();
		// ------------------------------------------外卖页面
		StoreTimelineRequest takeoutRequest = new StoreTimelineRequest(aq,
				"takeoutTimeLineCallBack");
		takeoutRequest.request(takeoutRequestTypeId, new Long(20), new Long(1));

		// ------------------------------------------超市页面
		StoreTimelineRequest marketRequest = new StoreTimelineRequest(aq,
				"marketTimeLineCallBack");
		marketRequest.request(marketRequestTypeId, new Long(20), new Long(1));

		// ------------------------------------------医疗页面
		StoreTimelineRequest medicalRequest = new StoreTimelineRequest(aq,
				"medicalTimeLineCallBack");
		medicalRequest.request(medicalRequestTypeId, new Long(20), new Long(1));

		// ------------------------------------------服务页面
		StoreTimelineRequest serviceRequest = new StoreTimelineRequest(aq,
				"serviceTimeLineCallBack");
		serviceRequest.request(serviceRequestTypeId, new Long(20), new Long(1));

	}

	// 初始化头标
	private void InitTextView() {
		textView_takeout = (TextView) findViewById(R.id.TextView_ConvToolsActivity_takeout);
		textView_market = (TextView) findViewById(R.id.TextView_ConvToolsActivity_market);
		textView_medical = (TextView) findViewById(R.id.TextView_ConvToolsActivity_medical);
		textView_service = (TextView) findViewById(R.id.TextView_ConvToolsActivity_service);

		textView_takeout.setOnClickListener(new MyOnClickListener(0));
		textView_market.setOnClickListener(new MyOnClickListener(1));
		textView_medical.setOnClickListener(new MyOnClickListener(2));
		textView_service.setOnClickListener(new MyOnClickListener(3));
	}

	// 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	private void InitImageView() {
		imageView_cousor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 4 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView_cousor.setImageMatrix(matrix);// 设置动画初始位置
	}

	// 头标点击监听 3
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			ViewPager_ConvTools.setCurrentItem(index);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int desIndex) {

			Animation animation = new TranslateAnimation(one * currIndex, one
					* desIndex, 0, 0);
			currIndex = desIndex;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(200);
			imageView_cousor.startAnimation(animation);
		}
	}

	// 获取外卖列表的回调函数
	public void takeoutTimeLineCallBack(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		System.out.println("-----------jump into takeout callback-------");
		if (json != null) {
			// 使用model解析json
			System.out.println("takeout------>" + json.toString());
			StoreTimelineResponse storesTimeLine = new StoreTimelineResponse(
					json);
			long responseCode = storesTimeLine.getCode();
			System.out.println("回复码：" + responseCode);
			takeoutList = storesTimeLine.getStores();
			flag++;
			showToast();
		} else {
			flag--;
			showToast();
			System.out.println("takeoutTimeLineCallBack json is null！");
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
		ShowTakeoutList takeout = new ShowTakeoutList(view_takeout,
				takeoutList, buttonLookTakeoutText);
		takeout.showTakeout();
	}

	// 获取超市列表的回调函数
	public void marketTimeLineCallBack(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		System.out.println("-----------jump into market callback-------");
		if (json != null) {
			// 使用model解析json
			System.out.println("market------>" + json.toString());
			StoreTimelineResponse storesTimeLine = new StoreTimelineResponse(
					json);
			long responseCode = storesTimeLine.getCode();
			System.out.println("回复码：" + responseCode);
			marketList = storesTimeLine.getStores();
			flag++;
			showToast();
		} else {
			flag--;
			showToast();
			System.out.println("marketTimeLineCallBack json is null！");
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
		ShowMarketList market = new ShowMarketList(view_market, marketList,
				buttonLookMarketText);
		market.showMarket();
	}

	// 获取医疗列表的回调函数
	public void medicalTimeLineCallBack(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		System.out.println("-----------jump into medical callback-------");
		if (json != null) {
			// 使用model解析json
			System.out.println("medical------>" + json.toString());
			StoreTimelineResponse storesTimeLine = new StoreTimelineResponse(
					json);
			long responseCode = storesTimeLine.getCode();
			System.out.println("回复码：" + responseCode);
			medicalList = storesTimeLine.getStores();
			flag++;
			showToast();
		} else {
			flag--;
			showToast();
			System.out.println(" medicalTimeLineCallBack json is null！");
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
		ShowMedicalList medical = new ShowMedicalList(view_medical,
				medicalList, buttonLookMedicalText);
		medical.showMedical();
	}

	// 获取服务列表的回调函数
	public void serviceTimeLineCallBack(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		System.out.println("-----------jump into service callback-------");
		if (json != null) {
			// 使用model解析json
			System.out.println("service------>" + json.toString());
			StoreTimelineResponse storesTimeLine = new StoreTimelineResponse(
					json);
			long responseCode = storesTimeLine.getCode();
			System.out.println("回复码：" + responseCode);
			serviceList = storesTimeLine.getStores();
			flag++;
			showToast();
		} else {
			flag--;
			showToast();
			System.out.println(" serviceTimeLineCallBack json is null！");
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG).show();
		}
		ShowServiceList service = new ShowServiceList(view_service,
				serviceList, buttonLookServiceText);
		service.showService();
	}

	// 将store写入takeoutBuffInfoFiele="convtoolsBuff.txt" 中
	public void writeConvToolsInfo(String filename, List<Store> takeout,
			List<Store> market, List<Store> medical, List<Store> service)
			throws IOException {
		List<Store> stores = new ArrayList<Store>();
		stores.addAll(takeout);
		stores.addAll(market);
		stores.addAll(medical);
		stores.addAll(service);

		SharedPreferences preferences = getSharedPreferences(
				convtoolsBuffInfoFile, MODE_PRIVATE);
		// 创建字节输入流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			// 将对象写入字节流
			oos.writeObject(stores);
			// 将字节流编码成base64的字符窜
			String store_Base64 = new String(Base64.encodeBase64(baos
					.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString("stores", store_Base64);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 从takeoutBuffInfoFiele="convtoolsBuff.txt" 中读取信息
	@SuppressWarnings("unchecked")
	public List<Store> readConvToolsInfo(String filename, String productBase64) {
		System.out.println("进入读取缓存数据-----");

		List<Store> stores = new ArrayList<Store>();

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				stores = (ArrayList<Store>) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stores;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conv_tools, menu);
		return true;
	}

}
