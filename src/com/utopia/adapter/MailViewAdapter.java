package com.utopia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.utopia.activity.R;
import com.utopia.structs.MailMessage;
import com.utopia.tools.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MailViewAdapter extends BaseAdapter {

	private Context context;
	private List<MailMessage> messageData = new ArrayList<MailMessage>();
	private LayoutInflater layoutInflater;
	private final int TYPE_ONE = 0;
	private final int TYPE_TWO = 1;
	Bitmap me_bmp_round;

	public MailViewAdapter(Context context, List<MailMessage> messageCont) {
		this.context = context;
		messageData = messageCont;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		Bitmap me_bmp = BitmapFactory.decodeResource(context.getResources(),
//				R.drawable.me);
//		me_bmp_round=ImageUtil.getRoundedCornerBitmap(me_bmp, 5);
//		if(me_bmp!=null||!me_bmp.isRecycled()){
//			me_bmp.recycle();
//			System.gc();
//		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (messageData == null)
			return 0;
		else
			return messageData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return messageData.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolderReceive {
		AQuery aqReceive;
	}

	class ViewHolderSend {
		AQuery aqSend;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderSend viewHolderSend = null;
		ViewHolderReceive viewHolderReceive = null;
		int kind = messageData.get(position).getKind();
//		if (convertView == null) {
			switch (kind) {
			case TYPE_ONE:
				convertView = layoutInflater.inflate(R.layout.messagesend,
						parent, false);
				viewHolderSend = new ViewHolderSend();
				viewHolderSend.aqSend = new AQuery(convertView);
				convertView.setTag(viewHolderSend);
				break;
			case TYPE_TWO:
				convertView = layoutInflater.inflate(R.layout.messagereceive,
						parent, false);
				viewHolderReceive = new ViewHolderReceive();
				viewHolderReceive.aqReceive = new AQuery(convertView);
				convertView.setTag(viewHolderReceive);
				break;
			default:
				break;
			}
//		} 
//		else {
//			switch (kind) {
//			case TYPE_ONE:
//				viewHolderSend = (ViewHolderSend) convertView.getTag();
//
//				break;
//			case TYPE_TWO:
//				viewHolderReceive = (ViewHolderReceive) convertView.getTag();
//
//				break;
//			default:
//				break;
//			}
//		}

		switch (kind) {
		case TYPE_ONE:
			viewHolderSend.aqSend.id(R.id.textview_talk_send).text(
					messageData.get(position).getContent());
//			viewHolderSend.aqSend.id(R.id.imageview_talk_send).image(me_bmp_round);
			break;
		case TYPE_TWO:
			viewHolderReceive.aqReceive.id(R.id.textview_talk_receive).text(
					messageData.get(position).getContent());
			break;
		default:
			break;
		}
		return convertView;
	}

}
