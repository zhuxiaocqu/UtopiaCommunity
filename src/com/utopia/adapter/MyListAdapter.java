package com.utopia.adapter;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.utopia.activity.R;
import com.utopia.structs.HashMapData;
import com.utopia.structs.NewsKind;
import com.utopia.tools.HandlerNewsReadFlag;
import com.utopia.tools.ReadObjectFromFile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {

	private final int TYPE_ONE = 0;
	private final int TYPE_TWO = 1;
	private int flag = 0;
	private Context context;
	private LayoutInflater layoutInflater;
	private List<HashMap<String, Object>> data;
	private AQuery aqList;

	public MyListAdapter(Context context, List<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MyListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_ONE;
		} else
			return TYPE_TWO;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	class ViewHolderAd {
		AQuery aqAd;
	}

	class ViewHolderContent {
		AQuery aqContent;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderContent holderCont = null;
		ViewHolderAd holderAd = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_ONE:
				convertView = layoutInflater.inflate(
						R.layout.listview_news_ad_picture, parent, false);
				holderAd = new ViewHolderAd();
				holderAd.aqAd = new AQuery(convertView);
				convertView.setTag(holderAd);
				break;
			case TYPE_TWO:
				convertView = layoutInflater.inflate(
						R.layout.listview_news_content, parent, false);
				holderCont = new ViewHolderContent();
				holderCont.aqContent = new AQuery(convertView);
				convertView.setTag(holderCont);
				break;
			}
		} else {
			switch (type) {
			case TYPE_ONE:
				holderAd = (ViewHolderAd) convertView.getTag();
				break;
			case TYPE_TWO:
				holderCont = (ViewHolderContent) convertView.getTag();
				break;
			}
		}
		// 填充listview中的内容
		switch (type) {
		// 填充广告
		case TYPE_ONE:
			// holderAd.adPicture.setImageResource((Integer)
			// data.get(TYPE_ONE).get(
			// "adpicture"));
			// 需要加载地址时修改-----------
			if (data != null && !data.isEmpty())
				holderAd.aqAd.id(R.id.imageview_listview_ad)
						.progress(R.id.progressbar_listview_ad)
						.image((Integer) data.get(position).get("adpicture"));
			// System.out.println(flag);
			break;
		// 填充新闻内容
		case TYPE_TWO:
			// holderCont.newsAuthor.setText((String)
			// data.get(position).get("author"));
			// holderCont.newsPicture.setImageResource((Integer)
			// data.get(position).get(
			// "newspicture"));
			// holderCont.newsTime.setText((String)
			// data.get(position).get("time"));
			// holderCont.newsTitle.setText((String)
			// data.get(position).get("title"));
			if (data != null && !data.isEmpty()) {
				// 设置每条”新闻“对应的图标
				switch ((Integer) data.get(position).get(HashMapData.TYPE_ID)) {
				case NewsKind.TYPE_ID_HOTC:
					holderCont.aqContent.id(R.id.imageview_news_picture).image(
							R.drawable.news_citytopic);
					break;
				case NewsKind.TYPE_ID_COMUNOTICE:
					holderCont.aqContent.id(R.id.imageview_news_picture).image(
							R.drawable.news_comtips);
					break;
				case NewsKind.TYPE_ID_CHARGETIP:
					holderCont.aqContent.id(R.id.imageview_news_picture).image(
							R.drawable.news_chargetip);
					break;
				case NewsKind.TYPE_ID_MEDISERVE:
					holderCont.aqContent.id(R.id.imageview_news_picture).image(
							R.drawable.news_medicalserve);
					break;
				default:
					break;

				}
				if (HandlerNewsReadFlag.hash != null) {
					if (!HandlerNewsReadFlag.hash.isEmpty()) {
						System.out.println("保存的hash对象为："
								+ HandlerNewsReadFlag.hash);
						System.out.println("保存的hash值为："
								+ HandlerNewsReadFlag.hash.get(data.get(
										position).get(HashMapData.U_ID)));
						Integer readFlagInt = HandlerNewsReadFlag.hash.get(data
								.get(position).get(HashMapData.U_ID));
						// 根据是否被读过设置新闻显示的样式
						if (readFlagInt == null) {
							newsNotBeenRead(holderCont, position);
						} else if (readFlagInt == HashMapData.BEEN_READ) {
							newsBeenRead(holderCont, position);
						}
					}
					else
						newsNotBeenRead(holderCont, position);
				} else {
					newsNotBeenRead(holderCont, position);
				}
			}

			// 用image的Url来设置image的贴图
			// holderCont.aqContent.id(R.id.imageview_news_picture).image(
			// data.get(position).get("newspicture"));
			// flag++;
			break;
		}
		// 让hashmap中的值后移一位

		// System.out.println(position);
		return convertView;
	}

	public void newsNotBeenRead(ViewHolderContent holderCont, int position) {
		/*
		 * 当新闻未被读过时，显示格式如下
		 */
		// 新闻的标题
		holderCont.aqContent.id(R.id.textview_news_content_title)
				.textColor(Color.BLACK)
				.text((String) data.get(position).get(HashMapData.TITLE));
		// 新闻的作者
		holderCont.aqContent.id(R.id.textview_news_content_auther)
				.textColor(Color.BLACK)
				.text((String) data.get(position).get(HashMapData.AUTHOR));
		// 新闻的创建时间
		holderCont.aqContent.id(R.id.textview_news_content_time)
				.textColor(Color.BLACK)
				.text((String) data.get(position).get(HashMapData.CREATTIME));
	}

	public void newsBeenRead(ViewHolderContent holderCont, int position) {
		/*
		 * 当新闻被读过时，显示格式如下
		 */
		// 新闻的标题
		holderCont.aqContent.id(R.id.textview_news_content_title)
				.textColor(Color.GRAY)
				.text((String) data.get(position).get(HashMapData.TITLE));
		// 新闻的作者
		holderCont.aqContent.id(R.id.textview_news_content_auther)
				.textColor(Color.GRAY)
				.text((String) data.get(position).get(HashMapData.AUTHOR));
		// 新闻的创建时间
		holderCont.aqContent.id(R.id.textview_news_content_time)
				.textColor(Color.GRAY)
				.text((String) data.get(position).get(HashMapData.CREATTIME));
	}

}
