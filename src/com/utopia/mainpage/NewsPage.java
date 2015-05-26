package com.utopia.mainpage;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.androidquery.AQuery;
import com.ilive.request.NewsTimelineRequest;
import com.utopia.activity.MainActivity;
import com.utopia.activity.NewsShowActivity;
import com.utopia.activity.R;

import com.utopia.adapter.MyListAdapter;
import com.utopia.tools.HandleNewsSortMethod;
import com.utopia.tools.HandlerNewsReadFlag;
import com.utopia.tools.NetHelper;
import com.utopia.tools.ReadObjectFromFile;
import com.utopia.tools.WriteObjectToFile;
import com.utopia.structs.Flags;
import com.utopia.structs.HashMapData;
import com.utopia.structs.MyListView;
import com.utopia.structs.NewsKind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsPage implements MyListView.OnRefreshListener {
	private View viewGroup;
	private MyListView listViewNews;
	public static List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	public static BaseAdapter adapterListView;
	public final static int DRAG_INDEX = 1;// 下拉刷新标识
	public final static int LOADMORE_INDEX = 2;// 加载更多标识
	private String newsFile = "tempnews.bin";
	private String newsFilePath = "/data/data/com.utopia.activity/files/tempnews.bin";
	private String refreshTimeFile = "refreshTimeFile.info";
	private String refreshTimePath = "/data/data/com.utopia.activity/files/refreshTimeFile.info";
	private WriteObjectToFile writeObject;
	private ReadObjectFromFile readObject;

	private AQuery aqAdmin;
	private Context context;
	public static final int REFRESH_SEP = 30;//自动刷新时间间隔（分钟）

	private boolean TYPE_ID_HOTC_COMPLIETE = false;
	private boolean TYPE_ID_COMUNOTICE_COMPLIETE = false;
	private boolean TYPE_ID_CHARGETIP_COMPLIETE = false;
	private boolean TYPE_ID_MEDISERVE_COMPLIETE = false;

	private Long GROUP_ID = new Long(1);

	public HandlerNewsReadFlag handlerNewsReadFlag;// 处理新闻已读标志对象
	private HandleNewsSortMethod handleNewsSort;//处理新闻排序方式

	public NewsPage(View viewGroup) throws StreamCorruptedException,
			IOException, ClassNotFoundException {
		System.out.println("in newsPage");
		context = viewGroup.getContext();
		this.viewGroup = viewGroup;
		aqAdmin = new AQuery(viewGroup);

		// 处理新闻已读标志对象
		handlerNewsReadFlag = new HandlerNewsReadFlag(context);
		handlerNewsReadFlag.getReadFlagHashMapFromFile();//从文件中读出文件已读标志
		// 处理新闻排序对象
		handleNewsSort = new HandleNewsSortMethod(context);
		handleNewsSort.getSortKind();//从文件中读出新闻排序类型

		if (MainActivity.userInfo != null)
			GROUP_ID = MainActivity.userInfo.getGroupid();
		// 初始化读写文件工具
		writeObject = new WriteObjectToFile(context);
		readObject = new ReadObjectFromFile(context);

		initListView();
		// 设置屏幕点击事件--只有当没有网络时才会显示
		setNoServiceEvent();
		// if (NetHelper.IsHaveInternet(context)) {
		// System.out.println("网络已连接");
		// // 当初始登陆时让listview处于刷新状态
		if (isRefresh()) {
			listViewNews.onRefreshLoading();
			// 更新数据
			onRefresh();
		}
		// } else if (data.isEmpty()) {
		// System.out.println("网络已断开");
		// showNoService();
		// }
	}

	// public void refreshDataNews() {
	// new MyAsyncTask(context, DRAG_INDEX).execute();
	// }
	// 显示默认页面
	public void showDefault() {
		// System.out.println("fun->default");
		aqAdmin.id(R.id.relative_news_defeault).visible();
		aqAdmin.id(R.id.relative_news_no_service).invisible();
		aqAdmin.id(R.id.listview_news).invisible();
	}

	// 显示断网时页面
	public void showNoService() {
		// System.out.println("fun->noservice");
		aqAdmin.id(R.id.relative_news_defeault).invisible();
		aqAdmin.id(R.id.relative_news_no_service).visible();
		aqAdmin.id(R.id.listview_news).invisible();
	}

	// 显示联网时页面
	public void showList() {
		// System.out.println("fun->list");
		aqAdmin.id(R.id.relative_news_defeault).gone();
		aqAdmin.id(R.id.relative_news_no_service).gone();
		aqAdmin.id(R.id.listview_news).visible();
	}

	// 设置断网屏幕监听器
	public void setNoServiceEvent() {
		aqAdmin.id(R.id.relative_news_no_service).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDefault();
						if (NetHelper.IsHaveInternet(context))
							onRefresh();
						else
							Toast.makeText(context, "请检查网络连接", 2000).show();
					}
				});
	}

	// 判断刷新间隔
	public boolean isRefresh() throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		Date curTime = new Date();
		File file = new File(refreshTimePath);
		if (file.exists()) {
			System.out.println("refreshtime exsit");
			Date lastRefreshTime = readObject.readDateObj(refreshTimeFile);
			System.out.println("curTime->" + curTime);
			System.out.println("lastRefreshTime->" + lastRefreshTime);
			if (lastRefreshTime == null)
				return true;
			else {
				if (curTime.getYear() - lastRefreshTime.getYear() > 0)
					return true;
				else if (curTime.getMonth() - lastRefreshTime.getMonth() > 0)
					return true;
				else if (curTime.getDay() - lastRefreshTime.getDay() > 0)
					return true;
				else if (curTime.getHours() - lastRefreshTime.getHours() > 0)
					return true;
				else if (curTime.getMinutes() - lastRefreshTime.getMinutes() > REFRESH_SEP)
					return true;
				else
					return false;
			}
		} else {
			System.out.println("refreshtime not exsit");
			return true;
		}
	}

	// 初始化新闻列表数据
	public void initDataNews() throws StreamCorruptedException, IOException,
			ClassNotFoundException {

		File binFile = new File(newsFilePath);
		if (!binFile.exists()) {
			// 此处为测试代码-------------------读取上次浏览过的最新的新闻内容-------------------------
			System.out.println("binfile not exsit");
			// showNoService();
			showDefault();
			// 更新数据
			// onRefresh();
		} else {
			// 测试代码-------------------------第一次登录时---------------课根据需求显示不同效果---------
//			System.out.println("binfile exsit");

			readObject = new ReadObjectFromFile(context);
			data = readObject.readList(newsFile);

			// System.out.println("data 是空->" + data.isEmpty());
			// System.out.println("reading data");

			if (data.isEmpty()) {
				// System.out.println("data is empty");
				showNoService();
				// 更新数据
				// onRefresh();
			} else {
				// adapterListView.notifyDataSetChanged();
				showList();
			}

		}
	}

	// 初始化新闻列表listview控件
	public void initListView() throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		listViewNews = (MyListView) viewGroup.findViewById(R.id.listview_news);
		listViewNews.setCacheColorHint(0);
		initDataNews();
		// 设置listview的适配器
		adapterListView = new MyListAdapter(context, data);
		listViewNews.setAdapter(adapterListView);
		// listview设置监听器
		listViewNews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if (arg2 == 1) {
					// Toast.makeText(context, "点击广告图片", 2000).show();
				} else {
					// Toast.makeText(context, "点击了ID:" + arg2, 2000).show();
					// 点击后设置已读标志位为1，表示已读

					// 跳转到新闻展示页面
					Intent jumpToViewNews = new Intent(context,
							NewsShowActivity.class);
					jumpToViewNews.putExtra("uid", (Long) data.get(arg2 - 1)
							.get("id"));
					context.startActivity(jumpToViewNews);
				}
			}
		});
		// listview承载新闻更新操作
		listViewNews.setonRefreshListener(this);
	}

	public void changeRefreshFlag(int kind) {
		System.out.println("5->  click onloadmore");
		switch (kind) {
		case NewsKind.TYPE_ID_HOTC:
			TYPE_ID_HOTC_COMPLIETE = true;
			break;
		case NewsKind.TYPE_ID_COMUNOTICE:
			TYPE_ID_COMUNOTICE_COMPLIETE = true;
			break;
		case NewsKind.TYPE_ID_CHARGETIP:
			TYPE_ID_CHARGETIP_COMPLIETE = true;
			break;
		case NewsKind.TYPE_ID_MEDISERVE:
			TYPE_ID_MEDISERVE_COMPLIETE = true;
			break;
		}

	}

	public void changeRefreshFlagToFalse() {
		TYPE_ID_HOTC_COMPLIETE = false;
		TYPE_ID_COMUNOTICE_COMPLIETE = false;
		TYPE_ID_CHARGETIP_COMPLIETE = false;
		TYPE_ID_MEDISERVE_COMPLIETE = false;
	}

	public void notifyConnectFail(int flag) {
		if (TYPE_ID_HOTC_COMPLIETE && TYPE_ID_COMUNOTICE_COMPLIETE
				&& TYPE_ID_CHARGETIP_COMPLIETE && TYPE_ID_MEDISERVE_COMPLIETE) {
			System.out.println("加载失败后的data大小（Each）->"+data.size());
			if (data.size() > 0) {
				if (flag == DRAG_INDEX) {
					try {
						listViewNews.onRefreshComplete();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (flag == LOADMORE_INDEX)
					listViewNews.onLoadMoreComplete(false);
			} else
				showNoService();
			Toast.makeText(context, "加载失败,请检查网络连接", 2000).show();
			// 将refreshFlag置为false（相当于初始化）方便下次刷新判断
			changeRefreshFlagToFalse();
		}
	}

	public void notifyConnectAllFail(int flag) {
		System.out.println("加载失败后的data大小（All）->"+data.size());
		if (data.size() > 0) {
			if (flag == DRAG_INDEX) {
				try {
					listViewNews.onRefreshComplete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (flag == LOADMORE_INDEX)
				listViewNews.onLoadMoreComplete(false);
		} else
			showNoService();
		Toast.makeText(context, "加载失败,请检查网络连接", 2000).show();
	}

	public void updateListView() {
		adapterListView.notifyDataSetChanged();
		System.out.println("in newpage update listview");
	}

	public void prepareForRefresh(List<HashMap<String, Object>> newData) {
		if (data != null && data.size() > 0)
			data.removeAll(data);
		// 添加广告图片
		Map<String, Object> mapAd = new HashMap<String, Object>();
		mapAd.put("adpicture", R.drawable.imageview_listview_ad);
		data.add((HashMap<String, Object>) mapAd);
		// System.out.println("refresh newData size->" + newData.size());
		// 添加新闻数据
		for (int i = 0; i < newData.size(); i++) {
			data.add(newData.get(i));
		}
		adapterListView.notifyDataSetChanged();
		try {
			listViewNews.onRefreshComplete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteObjectToFile writeObject = new WriteObjectToFile(context);
		try {
			writeObject.writeList(newsFile, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showList();

		if (data.isEmpty()) {
			Toast.makeText(context, "加载失败,请检查网络连接", 2000).show();
			System.out.println("data is emptty");
			showNoService();
		} else if (data.size() == 1) {
			System.out.println("刷新后的data是：" + data.size());
			Toast.makeText(context, "加载失败,请检查网络连接", 2000).show();
			showNoService();
		}
		MainActivity.newData.clear();
		Toast.makeText(context, "更新完毕", 2000).show();
	}

	public void prepareDataForLoadMore(List<HashMap<String, Object>> newData) {
		if (newData == null || newData.isEmpty()) {
			Toast.makeText(context, "管理员不给力，没有可加载项", 2000).show();
		} else {
			// System.out.println("loadmore newData size->"
			// + newData.size());
			for (int i = 0; i < newData.size(); i++) {
				data.add(newData.get(i));
			}

			adapterListView.notifyDataSetChanged();
			MainActivity.newData.clear();
			Toast.makeText(context, "加载完毕", 2000).show();
		}

		listViewNews.onLoadMoreComplete(false);
	}

	public void notifyAdapter(List<HashMap<String, Object>> newData, int flag)
			throws IOException {

		if (flag == DRAG_INDEX) {
			System.out.println("notifying adapter");
			if (TYPE_ID_HOTC_COMPLIETE && TYPE_ID_COMUNOTICE_COMPLIETE
					&& TYPE_ID_CHARGETIP_COMPLIETE
					&& TYPE_ID_MEDISERVE_COMPLIETE) {
				// 将Refresh获取的新闻数据newsdata装载到ListView中
				prepareForRefresh(newData);
				// 将refreshFlag置为false（相当于初始化）方便下次刷新判断
				changeRefreshFlagToFalse();
			}
		} else if (flag == LOADMORE_INDEX) {
			System.out.println("notifying loadmore");
			if (TYPE_ID_HOTC_COMPLIETE && TYPE_ID_COMUNOTICE_COMPLIETE
					&& TYPE_ID_CHARGETIP_COMPLIETE
					&& TYPE_ID_MEDISERVE_COMPLIETE) {
				// 将LoadMore获取的新闻数据newsdata装载到ListView中
				prepareDataForLoadMore(newData);
				// 将refreshFlag置为false（相当于初始化）方便下次刷新判断
				changeRefreshFlagToFalse();
			}
		}
	}

	public void notifyAdapterAll(List<HashMap<String, Object>> newData, int flag) {
		switch (flag) {
		case DRAG_INDEX:
			// 将Refresh获取的新闻数据newsdata装载到ListView中
			prepareForRefresh(newData);
			break;
		case LOADMORE_INDEX:
			// 将LoadMore获取的新闻数据newsdata装载到ListView中
			prepareDataForLoadMore(newData);
			break;
		default:
			break;
		}
	}

	public void refreshNewsData(Long groupid, Long typeid, AQuery aq,
			String callback) {
		System.out.println("2->  click onloadmore");
		NewsTimelineRequest nTR = new NewsTimelineRequest(aq, callback);
		nTR.request(groupid, typeid, null, null);
	}

	// 根据用户设置判定新闻排序方式

	// listview上拉刷新
	@Override
	public void onRefresh() {
		if (NetHelper.IsHaveInternet(context)) {

			// new MyAsyncTask(context, DRAG_INDEX).execute();
			// -----------------接受刷新数据-------------------------
			System.out.println("------刷新数据中---------");
			if (handleNewsSort.getSortMethod() == Flags.NEWS_SORT_BY_KIND) {
				refreshNewsData(GROUP_ID, new Long(NewsKind.TYPE_ID_HOTC),
						aqAdmin, "cityTopicCallback");
				refreshNewsData(GROUP_ID,
						new Long(NewsKind.TYPE_ID_COMUNOTICE), aqAdmin,
						"comunNoticeCallback");
				refreshNewsData(GROUP_ID, new Long(NewsKind.TYPE_ID_CHARGETIP),
						aqAdmin, "chargeTipCallback");
				refreshNewsData(GROUP_ID, new Long(NewsKind.TYPE_ID_MEDISERVE),
						aqAdmin, "mediServeCallback");
				System.out.println("have exc zhuxiao");
			} else if (handleNewsSort.getSortMethod() == Flags.NEWS_SORT_BY_TIME) {
				refreshNewsData(GROUP_ID, null, aqAdmin, "allCallback");
			}

		} else if (data.isEmpty()) {
			showNoService();
			Toast.makeText(context, "您的网络未连接", 2000).show();
			System.out.println("onfresh no net");
		} else {
			try {
				showList();
				listViewNews.onRefreshComplete();
				Toast.makeText(context, "您的网络未连接", 2000).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// listview点击加载更多
	@SuppressLint("UseValueOf")
	@Override
	public void onLoadMore() {
		// new MyAsyncTask(context, LOADMORE_INDEX).execute();

		if (NetHelper.IsHaveInternet(context)) {
			// new MyAsyncTask(context, DRAG_INDEX).execute();
			// -----------------接受刷新数据-------------------------
			System.out.println("------加载更多数据中---------");
			if (handleNewsSort.getSortMethod() == Flags.NEWS_SORT_BY_KIND) {
				refreshNewsData(GROUP_ID, new Long(NewsKind.TYPE_ID_HOTC),
						aqAdmin, "cityTopicMoreCallback");
				refreshNewsData(GROUP_ID,
						new Long(NewsKind.TYPE_ID_COMUNOTICE), aqAdmin,
						"comunNoticeMoreCallback");
				refreshNewsData(GROUP_ID, new Long(NewsKind.TYPE_ID_CHARGETIP),
						aqAdmin, "chargeTipMoreCallback");
				refreshNewsData(GROUP_ID, new Long(NewsKind.TYPE_ID_MEDISERVE),
						aqAdmin, "mediServeMoreCallback");
			} else if (handleNewsSort.getSortMethod() == Flags.NEWS_SORT_BY_TIME) {
				refreshNewsData(GROUP_ID, null, aqAdmin, "allMoreCallback");
			}

		} else if (data.isEmpty()) {
			showNoService();
			Toast.makeText(context, "您的网络未连接", 2000).show();
			System.out.println("onfresh no net");
		} else {
			showList();
			listViewNews.onLoadMoreComplete(false);
			Toast.makeText(context, "您的网络未连接", 2000).show();
		}
	}
}
