package com.utopia.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.ilive.request.NewsShowRequest;
import com.ilive.response.NewsShowResponse;
import com.ilive.structs.News;
import com.utopia.mainpage.NewsPage;
import com.utopia.structs.Flags;
import com.utopia.structs.HashMapData;
import com.utopia.tools.CustomProgressDialog;
import com.utopia.tools.HandlerNewsReadFlag;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class NewsShowActivity extends Activity {

	private AQuery aqNewsShow;
	private Long uid;
	public HandlerNewsReadFlag handlerNewsReadFlag;// 处理新闻已读标志对象
	private Context context;
	private CustomProgressDialog progressDialog = null; // 登录等待进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_show);
		
		// startProgressDialog();

		MyApplication.getInstance().addActivity(this);
		iniData();
		addViewEvent();
		setReadFlag();
	}

	public void setReadFlag() {
		// 设置该新闻条已读
		if (HandlerNewsReadFlag.hash == null)
			HandlerNewsReadFlag.hash = new HashMap<Long, Integer>();
		HandlerNewsReadFlag.hash.put(uid, new Integer(1));

		// 将hashmap回写到文件中
		handlerNewsReadFlag.saveReadFlagHashMapToFile();

//		handlerNewsReadFlag.getReadFlagHashMapFromFile();
		if(NewsPage.adapterListView!=null)
			NewsPage.adapterListView.notifyDataSetChanged();
//		System.out.println("hash size->" + HandlerNewsReadFlag.hash.size());
	}

	// 显示登录进度条
	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在加载...");
		}
		progressDialog.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// Intent jumpBack = new Intent(NewsShowActivity.this,
		// MainActivity.class);
		// jumpBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		// startActivity(jumpBack);
		// finish();
		Intent jumpBack = new Intent(NewsShowActivity.this, MainActivity.class);
		jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 0);
		jumpBack.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 注意本行的FLAG设置
		startActivity(jumpBack);
		finish();
	}

	// 关闭登录进度条
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void iniData() {
		context=NewsShowActivity.this;
		aqNewsShow = new AQuery(this);
		handlerNewsReadFlag=new HandlerNewsReadFlag(context);
		Intent getData = getIntent();
		// newsCont = (HashMap<String, Object>) getData
		// .getSerializableExtra("dataMap");
		uid = (Long) getData.getSerializableExtra("uid");
		// System.out.println(uid);
		requestNewsData(uid);
	}

	// 请求新闻数据
	public void requestNewsData(Long uid) {
		prepareViewForRequestData();
		NewsShowRequest newsRequest = new NewsShowRequest(aqNewsShow,
				"newsRequestCallback");
		newsRequest.request(uid);
	}

	public void newsRequestCallback(String url, JSONObject json,
			AjaxStatus status) throws JSONException {
		if (json != null) {
			// System.out.println(json.toString());
			NewsShowResponse nsresponse = new NewsShowResponse(json);
			News sigleNews = nsresponse.getNews();
			// System.out.println(sigleNews.getTitle());
			showNewsCont(sigleNews);
		} else {
			aqNewsShow.id(R.id.relative_newshow_default_progressbar).gone();
			aqNewsShow.id(R.id.relative_newsshow_connect_fail).visible();
			Toast.makeText(NewsShowActivity.this, "加载失败,请检查网络连接", 2000).show();
			// Toast.makeText(this, "Error:" + status.getCode(),
			// Toast.LENGTH_LONG)
			// .show();

		}
	}

	protected void onDestroy() {

		super.onDestroy();

		// clean the file cache when root activity exit
		// the resulting total cache size will be less than 3M
		if (isTaskRoot()) {
			AQUtility.cleanCacheAsync(this);
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

	public void addViewEvent() {

		aqNewsShow.id(R.id.button_newsshow_title_back).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent jumpBack = new Intent(NewsShowActivity.this,
								MainActivity.class);
						jumpBack.putExtra(Flags.CHOOSE_PAGE_FLAG, 0);
						jumpBack.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 注意本行的FLAG设置
						startActivity(jumpBack);

						finish();
					}
				});
		aqNewsShow.id(R.id.relative_newsshow_connect_fail).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("click image newsshow");
						requestNewsData(uid);
					}
				});
	}

	public void prepareViewForRequestData() {
		aqNewsShow.id(R.id.relative_newsshow_connect_fail).invisible();
		aqNewsShow.id(R.id.relative_newshow_default_progressbar).visible();
		aqNewsShow.id(R.id.relative_newsshow_scrolldown).invisible();
	}

	public void showNewsCont(News news) {
		// stopProgressDialog();
		aqNewsShow.id(R.id.relative_newshow_default_progressbar).gone();
		aqNewsShow.id(R.id.relative_newsshow_scrolldown).visible();

		if (news.getOriginal_pic() == null || news.getOriginal_pic().equals("")) {
			aqNewsShow.id(R.id.relative_newsshow_thirdlay_image).gone();
		} else {

			// aqNewsShow
			// .id(R.id.imageview_newsshow_pic)
			// .progress(R.id.progressbar_newsshow_pic)
			// .image((String) newsCont.get(PICSTR), true, true, 0, 0,
			// null, 0, 1.5f / 1.0f);
			aqNewsShow.id(R.id.relative_newsshow_thirdlay_image).visible();
			aqNewsShow
					.id(R.id.imageview_newsshow_pic)
					.progress(R.id.progressbar_newsshow_pic)
					.image((String) news.getOriginal_pic(), true, true, 443,
							R.drawable.image_newsshow_default,
							new BitmapAjaxCallback() {

								@Override
								public void callback(String url, ImageView iv,
										Bitmap bm, AjaxStatus status) {
									iv.setScaleType(ScaleType.FIT_CENTER);
									iv.setImageBitmap(bm);

								}
							});
		}

		aqNewsShow.id(R.id.textview_newsshow_newstitle).text(news.getTitle());
		// aqNewsShow.id(R.id.textview_newsshow_newsauthor).text((String)newsCont.get("author"));
		aqNewsShow.id(R.id.textview_newsshow_newstime).text(
				news.getCreated_at());
		aqNewsShow.id(R.id.textview_newsshow_cont).text(news.getText());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hot_city, menu);
		return true;
	}

}
