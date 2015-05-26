package com.utopia.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.utopia.mainpage.*;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.ilive.response.LetterUploadResponse;
import com.ilive.response.NewsTimelineResponse;
import com.ilive.structs.News;
import com.ilive.structs.User;
import com.utopia.activity.R;
import com.utopia.adapter.MailViewAdapter;
import com.utopia.adapter.MyPageAdapter;
import com.utopia.structs.MailMessage;
import com.utopia.structs.NewsKind;
import com.utopia.tools.CovertNewsToListData;
import com.utopia.tools.ImageUtil;
import com.utopia.tools.SortNewsByCreateTime;
import com.utopia.tools.WriteObjectToFile;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ViewPager viewPager;
	private AQuery aqAdmin;
	private LayoutInflater layoutInflater;
	private NewsPage news;
	private ShowConvTools serve;
	private Manager manager;
	private SetPage set;
	private MailPage mail;
	View viewNews;
	View viewAdmin;
	View viewServe;
	View viewMail;
	View viewSettings;
	private final int NUM_SUB_PAGE_NEWS = 0;
	private final int NUM_SUB_PAGE_MANGER = 1;
	private final int NUM_SUB_PAGE_SERVE = 2;
	private final int NUM_SUB_PAGE_MAIL = 3;
	private final int NUM_SUB_PAGE_SETTINGS = 4;
	private final String CHOOSE_PAGE_FLAG = "whichpage";

	private String newsFile = "tempnews.bin";
	private long exitTime = 0;

	public static User userInfo;

	private int choosePage = 0;

	

	public static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		MyApplication.getInstance().addMainActivity(this);
		System.out.println("mainActivity onCreate");
		aqAdmin = new AQuery(this);

		iniData();
		// 初始化viewpager中的各个子布局
		findViewsInViewPager();
		// 初始化viewpager
		try {
			news = new NewsPage(viewNews);
			serve = new ShowConvTools(viewServe);
			serve.showConvTools();
			manager = new Manager(viewAdmin);
			mail = new MailPage(viewMail);

			set = new SetPage(viewSettings);
			// 初始化viewpager
			iniViewPager();
			// 将站内信完全暴露在MainActivity中
//			setMailEvent();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 对导航textview设置监听
		setRadioButtonOnClickListener();
	}

	

	public void LettersUploadCallBack(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		if (json != null) {
			// aq.id(R.id.textView1).text(json.toString());
			System.out.println(json.toString());
			LetterUploadResponse luresponse = new LetterUploadResponse(json);
			System.out.println(luresponse.getCode());
			System.out.println(luresponse.getLetters());
			if (luresponse.getCode().intValue() == 200)
				Toast.makeText(context, "已投递到服务器", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "Error:" + status.getCode(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void iniData() {
		Intent getData = getIntent();
		getJumpFlag(getData);
		getUserInfo(getData);
	}

	public void getJumpFlag(Intent intent) {
		choosePage = intent.getIntExtra(CHOOSE_PAGE_FLAG, 0);
	}

	public void getUserInfo(Intent getUserIntent) {
		userInfo = (User) getUserIntent.getSerializableExtra("user");
		// System.out.println(userInfo.getAccount());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) { // System.currentTimeMillis()无论何时调用，肯定大于2000
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				MyApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------newsCallbacks
	// 新闻回调函数集中--------------------------------

	public static List<HashMap<String, Object>> newData = new ArrayList<HashMap<String, Object>>();

	public void getRefreshDataFromJson(JSONObject json, int CountPrefer) {
		try {
			NewsTimelineResponse Newses = new NewsTimelineResponse(json);
			// 将按id与时间的关系新闻排序
			List<News> Newslist = SortNewsByCreateTime.sort(Newses.getNews());
			int sumNews = Newslist.size();
			System.out.println("allcallback加载news总数-》" + sumNews);
			int startPos = sumNews - 1;
			int endPos = sumNews - CountPrefer;
			if (sumNews >= CountPrefer) {
				// 往hashmap中刷新数据---------20条
				for (int i = startPos; i >= endPos; i--) {
					CovertNewsToListData getNewsData = new CovertNewsToListData(
							Newslist.get(i));
					newData.add(getNewsData.covert());
				}

			} else {
				// 往hashmap中刷新数据---------如果没有20条 则视NewsList的大小而定
				for (int i = startPos; i >= 0; i--) {
					CovertNewsToListData getNewsData = new CovertNewsToListData(
							Newslist.get(i));
					newData.add(getNewsData.covert());
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void allCallback(String url, JSONObject json, AjaxStatus status)
			throws JSONException {
		if (json != null) {
			// System.out.println(json.toString());
			getRefreshDataFromJson(json, NewsKind.SUM_NEWS_ONETIME_ALL);
			news.notifyAdapterAll(newData, NewsPage.DRAG_INDEX);
		} else {
			Toast.makeText(this, "Error:" + status.getCode(), Toast.LENGTH_LONG)
					.show();
			// Toast.makeText(this, "加载失败", Toast.LENGTH_LONG).show();
			news.notifyConnectAllFail(NewsPage.DRAG_INDEX);
		}
	}

	public void cityTopicCallback(String url, JSONObject json, AjaxStatus status)
			throws JSONException, IOException, ParseException {
		if (json != null) {
			// 将json数据装载到newsdata中
			getRefreshDataFromJson(json, NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_HOTC);
			news.notifyAdapter(newData, NewsPage.DRAG_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_HOTC);
			news.notifyConnectFail(NewsPage.DRAG_INDEX);
		}
	}

	public void comunNoticeCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException, IOException,
			ParseException {
		if (json != null) {
			getRefreshDataFromJson(json, NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_COMUNOTICE);
			news.notifyAdapter(newData, NewsPage.DRAG_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_COMUNOTICE);
			news.notifyConnectFail(NewsPage.DRAG_INDEX);
		}
	}

	public void chargeTipCallback(String url, JSONObject json, AjaxStatus status)
			throws JSONException, IOException, ParseException {
		if (json != null) {
			getRefreshDataFromJson(json, NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_CHARGETIP);
			news.notifyAdapter(newData, NewsPage.DRAG_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_CHARGETIP);
			news.notifyConnectFail(NewsPage.DRAG_INDEX);
		}

	}

	public void mediServeCallback(String url, JSONObject json, AjaxStatus status)
			throws JSONException, IOException, ParseException {
		if (json != null) {
			getRefreshDataFromJson(json, NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_MEDISERVE);
			news.notifyAdapter(newData, NewsPage.DRAG_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_MEDISERVE);
			news.notifyConnectFail(NewsPage.DRAG_INDEX);
		}
	}

	// ----------------------------load more--------------------

	public void getLoadMoreDataFromJson(JSONObject json, int typeid,
			int CountPrefer) throws JSONException, IOException, ParseException {
		// 表示新闻列表中对应的typeid的新闻的条数
		// System.out.println("----------------4->  click onloadmore");
		int sumExsitType = 0;
		if (CountPrefer == NewsKind.SUM_NEWS_ONETIME_EACH) {
			// 从1开始循环，是因为hashmap中的第一项是广告图片，为此调试了好久 ，切记，切记！
			for (int i = 1; i < NewsPage.data.size(); i++) {
				// System.out.println("in new foreach");
				int t = (Integer) NewsPage.data.get(i).get("typeid");
				// System.out.println("typeid->  " + (t == typeid));
				if (t == typeid)
					sumExsitType++;
			}
		} else if (CountPrefer == NewsKind.SUM_NEWS_ONETIME_ALL) {
			sumExsitType = NewsPage.data.size() - 1;
		}

		// System.out.println("sumExsitType->  " + sumExsitType);
		NewsTimelineResponse Newses = new NewsTimelineResponse(json);
		List<News> Newslist = SortNewsByCreateTime.sort(Newses.getNews());
		System.out.println("newslist is:  " + Newslist.size());
		if ((Newslist.size() - sumExsitType) >= CountPrefer) {
			// 往hashmap中刷新数据---------20条
			// System.out
			// .println("(Newslist.size() - sumExsitType) >= NewsPage.SUM_NEWS_ONETIME_EACH");
			int startPosOfRestNews = (Newslist.size() - sumExsitType - 1);
			// System.out.println("startPosOfRestNews->   " +
			// startPosOfRestNews);
			int endPosOfWantedNews = (Newslist.size() - sumExsitType - CountPrefer);
			// System.out.println("endPosOfWantedNews->   " +
			// endPosOfWantedNews);
			// System.out.println("----------添加数据中-----------");
			for (int i = startPosOfRestNews; i >= endPosOfWantedNews; i--) {
				CovertNewsToListData getNewsData = new CovertNewsToListData(
						Newslist.get(i));
				// System.out.println("adding1");
				newData.add(getNewsData.covert());
			}

		} else {
			// 往hashmap中刷新数据---------如果没有20条 则视NewsList的大小而定
			int startPosOfRestNews = (Newslist.size() - sumExsitType - 1);
			for (int i = startPosOfRestNews; i >= 0; i--) {
				CovertNewsToListData getNewsData = new CovertNewsToListData(
						Newslist.get(i));
				// System.out.println("typeid" + typeid + "  adding2");
				newData.add(getNewsData.covert());
			}
		}
	}

	public void allMoreCallback(String url, JSONObject json, AjaxStatus status)
			throws JSONException, IOException, ParseException {
		if (json != null) {
			// System.out.println(json.toString());
			getLoadMoreDataFromJson(json, NewsKind.TYPE_ID_ALL,
					NewsKind.SUM_NEWS_ONETIME_ALL);
			news.notifyAdapterAll(newData, NewsPage.LOADMORE_INDEX);

		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.notifyConnectAllFail(NewsPage.LOADMORE_INDEX);
		}
	}

	public void cityTopicMoreCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException, IOException,
			ParseException {
		if (json != null) {
			// System.out.println("3->  click onloadmore");
			getLoadMoreDataFromJson(json, NewsKind.TYPE_ID_HOTC,
					NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_HOTC);
			news.notifyAdapter(newData, NewsPage.LOADMORE_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_HOTC);
			news.notifyConnectFail(NewsPage.LOADMORE_INDEX);
		}
	}

	public void comunNoticeMoreCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException, IOException,
			ParseException {
		if (json != null) {
			getLoadMoreDataFromJson(json, NewsKind.TYPE_ID_COMUNOTICE,
					NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_COMUNOTICE);
			news.notifyAdapter(newData, NewsPage.LOADMORE_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_COMUNOTICE);
			news.notifyConnectFail(NewsPage.LOADMORE_INDEX);
		}
	}

	public void chargeTipMoreCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException, IOException,
			ParseException {
		if (json != null) {
			getLoadMoreDataFromJson(json, NewsKind.TYPE_ID_CHARGETIP,
					NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_CHARGETIP);
			news.notifyAdapter(newData, NewsPage.LOADMORE_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_CHARGETIP);
			news.notifyConnectFail(NewsPage.LOADMORE_INDEX);
		}
	}

	public void mediServeMoreCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException, IOException,
			ParseException {
		if (json != null) {
			getLoadMoreDataFromJson(json, NewsKind.TYPE_ID_MEDISERVE,
					NewsKind.SUM_NEWS_ONETIME_EACH);
			news.changeRefreshFlag(NewsKind.TYPE_ID_MEDISERVE);
			news.notifyAdapter(newData, NewsPage.LOADMORE_INDEX);
		} else {
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();
			news.changeRefreshFlag(NewsKind.TYPE_ID_MEDISERVE);
			news.notifyConnectFail(NewsPage.LOADMORE_INDEX);
		}
	}

	// protected void onDestroy() {
	// super.onDestroy();
	// if (isTaskRoot()) {
	// // clean the file cache with advance option
	// long triggerSize = 3000000; // starts cleaning when cache size is
	// // larger than 3M
	// long targetSize = 2000000; // remove the least recently used files
	// // until cache size is less than 2M
	// AQUtility.cleanCacheAsync(this, triggerSize, targetSize);
	// }
	// }

	public void clearAQCache() {
		AQUtility.cleanCacheAsync(this);
	}

	protected void onDestroy() {

		super.onDestroy();

		// clean the file cache when root activity exit
		// the resulting total cache size will be less than 3M
		if (isTaskRoot()) {
			clearAQCache();
		}
	}

	// 初始化viewpager中的各个子布局 -------------------添加其他版块activity---------
	public void findViewsInViewPager() {
		layoutInflater = getLayoutInflater();
		viewNews = layoutInflater.inflate(R.layout.sub_viewpager_news, null);
		viewAdmin = layoutInflater
				.inflate(R.layout.sub_viewpager_manager, null);
		viewServe = layoutInflater.inflate(R.layout.sub_viewpager_convtools,
				null);
		viewMail = layoutInflater.inflate(R.layout.sub_viewpager_mail, null);
		viewSettings = layoutInflater.inflate(R.layout.sub_viewpager_settings,
				null);
	}

	public void iniViewPager() throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		viewPager = (ViewPager) findViewById(R.id.viewpager_main);
		List<View> views = new ArrayList<View>();
		views.add(viewNews);
		views.add(viewAdmin);
		views.add(viewServe);
		views.add(viewMail);
		views.add(viewSettings);
		viewPager.setAdapter(new MyPageAdapter(views));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 初始化viewpager初始页面号
		if (choosePage == NUM_SUB_PAGE_NEWS) {
			news.updateListView();
		}
		viewPager.setCurrentItem(choosePage);

		// aqAdmin.id(R.id.radiobutton_main_news).checked(true);
	}

	public void selectNavigation(int pos) {
		switch (pos) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		default:
			break;
		}
	}

	// 待修改----------
	public void turnToNews(View viewGroup) throws StreamCorruptedException,
			IOException, ClassNotFoundException {
		// news.initListView();
	}

	// 管理处预留调用-----------------
	public void turnToManagerPage(View viewGroup) {
		// Manager manager = new Manager(viewGroup);
	}

	// 便民服务预留调用-----------------
	public void turnToServe(View viewGroup) {
		// serve.showConvTools();
	}

	// 站内信预留调用-----------------
	public void turnToMail(View viewGroup) {
	}

	// 设置页面预留调用-----------------
	public void turnToSettings(View viewGroup) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void setRadioButtonOnClickListener() {
		aqAdmin.id(R.id.radiobutton_main_news).clicked(
				new MyOnClickListener(NUM_SUB_PAGE_NEWS));
		aqAdmin.id(R.id.radiobutton_main_manager).clicked(
				new MyOnClickListener(NUM_SUB_PAGE_MANGER));
		aqAdmin.id(R.id.radiobutton_main_serve).clicked(
				new MyOnClickListener(NUM_SUB_PAGE_SERVE));
		aqAdmin.id(R.id.radiobutton_main_mail).clicked(
				new MyOnClickListener(NUM_SUB_PAGE_MAIL));
		aqAdmin.id(R.id.radiobutton_main_settings).clicked(
				new MyOnClickListener(NUM_SUB_PAGE_SETTINGS));

	}

	class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			// Animation animation = null;
			// System.out.println("arg0->：" + arg0);
			switch (arg0) {
			case NUM_SUB_PAGE_NEWS:
				aqAdmin.id(R.id.radiobutton_main_news).checked(true);
				news.updateListView();
				break;
			case NUM_SUB_PAGE_MANGER:
				turnToManagerPage(viewAdmin);
				aqAdmin.id(R.id.radiobutton_main_manager).checked(true);
				break;
			case NUM_SUB_PAGE_SERVE:
				turnToServe(viewServe);
				aqAdmin.id(R.id.radiobutton_main_serve).checked(true);
				break;
			case NUM_SUB_PAGE_MAIL:
				turnToMail(viewMail);
				aqAdmin.id(R.id.radiobutton_main_mail).checked(true);
				break;
			case NUM_SUB_PAGE_SETTINGS:
				turnToSettings(viewSettings);
				aqAdmin.id(R.id.radiobutton_main_settings).checked(true);
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	
}
