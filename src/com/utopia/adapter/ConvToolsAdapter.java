package com.utopia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.ilive.structs.Store;
import com.utopia.activity.ConvToolsActivity;
import com.utopia.activity.R;
import com.utopia.tools.DialPopWindowDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConvToolsAdapter extends BaseAdapter implements
		OnItemClickListener {
	private LayoutInflater layoutInflater;
	private Context context;
	String str_inflater = Context.LAYOUT_INFLATER_SERVICE;
	private List<Store> listInner = null;

	PopupWindow dialPopupWindow; // 拨打电话的对话框

	private String buttonLookText;

	AQuery aq;

	public ConvToolsAdapter(Context context) {
		this.context = context;
	};

	public ConvToolsAdapter(Context context, List<Store> list,
			String buttonLookText) {
		this.context = context;
		this.listInner = list;
		this.buttonLookText = buttonLookText;
		layoutInflater = (LayoutInflater) context
				.getSystemService(str_inflater);
	};

	@Override
	public int getCount() {
		return listInner.size();
	}

	@Override
	public Object getItem(int position) {
		return listInner.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	class ViewHoldler {
		public ImageView img;
		public TextView textView_name;
		public TextView textView_phoneNumber;
		public TextView textView_shopHours;
		public TextView textView_Introduction;
		public Button button_dial;
		public Button button_look;
	}

	// 为每一项中控件赋值，每一项显示的数据由它决定
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 得到模板布局文件对象，并为其中的控件赋值
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.convtools_listview_item, parent, false);
			aq = new AQuery(convertView);
			inflateView(aq, position);
			convertView.setTag(aq);
		} else {
			// aq=new AQuery(convertView);
			aq = (AQuery) convertView.getTag();
			inflateView(aq, position);
		}

		// //为查看菜单按钮绑定事件，以对话框风格的Activity显示菜单
		// lookFoodMenu look=new lookFoodMenu(DataObjectPosition);
		// holder.button_look.setOnClickListener(look);
		return convertView;
	}

	// 填充list_item中的view组件
	public void inflateView(AQuery aq, int position) {
		aq.id(R.id.TextView_ConvToolsActivity_ListView_item_Name).text(
				listInner.get(position).getName());
		aq.id(R.id.TextView_ConvToolsActivity_ListView_item_phoneNumber).text(
				listInner.get(position).getPhone_number());
		aq.id(R.id.TextView_ConvToolsActivity_ListView_item_shopHours).text(
				listInner.get(position).getCreated_at());
		aq.id(R.id.TextView_ConvToolsActivity_ListView_item_introduction).text(
				listInner.get(position).getDescription());

		// 为image绑定事件 ,以对话框形式查看店铺所有信息
		showDialogListener showDialog = new showDialogListener(position);
		if (listInner.get(position).getPortrait_pic() == null
				|| listInner.get(position).getPortrait_pic().equals("")) {
			aq.id(R.id.ImageView_ConvToolsActivity_ListView_item_img)
					.image(R.drawable.ic_launcher).clicked(showDialog);
		} else {
			aq.id(R.id.ImageView_ConvToolsActivity_ListView_item_img)
					.progress(
							R.id.ProcessBar_ConvToolsActivity_ListView_item_loadingimg)
					.image(listInner.get(position).getPortrait_pic(), true,
							true).clicked(showDialog);
			// System.out.println("------>加载  : "+listInner.get(position).getName()+"  的图片");
		}
		// aq.id(R.id.ProcessBar_ConvToolsActivity_ListView_item_loadingimg).invisible();
		// 为拨号按钮绑定事件，跳转到拨号界面
		Dial dialListener = new Dial(position, aq.id(
				R.id.Button_ConvToolsActivity_ListView_item_dial).getContext());
		aq.id(R.id.Button_ConvToolsActivity_ListView_item_dial).text("拨打电话")
				.clicked(dialListener);

		// 为推荐服务类按钮设置文本
		aq.id(R.id.Button_ConvToolsActivity_Takeout_look).text(buttonLookText)
				.clicked(this, "readMoreClick");

	}

	// 查看详细按钮绑定事件
	public void readMoreClick() {
		Toast.makeText(context, "对不起，此功能暂未实现", Toast.LENGTH_LONG).show();
	}

	class Dial implements OnClickListener {
		private int position;
		Context context;

		Dial(int position, Context context) {
			this.position = position;
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			DialPopWindowDialog dialDialog = new DialPopWindowDialog(context,
					dialPopupWindow, listInner.get(position).getName(),
					listInner.get(position).getPhone_number());
			dialDialog.selectPopFun();
		}
	}

	class showDialogListener implements OnClickListener {
		private int position;

		showDialogListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 定义一个AlertDialog.BUilder对象
			final Builder builder = new AlertDialog.Builder(v.getContext());
			final View view = v;
			builder.setTitle(listInner.get(position).getName());
			builder.setMessage(listInner.get(position).getDescription());
			// 给对话框设置拨号按钮
			builder.setPositiveButton("拨号",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Uri uri = Uri
									.parse("tel:"
											+ listInner.get(position)
													.getPhone_number());
							Intent intent = new Intent(Intent.ACTION_DIAL, uri);
							view.getContext().startActivity(intent);
						}
					});
			// 给对话框设置关闭对话框按钮
			builder.setNegativeButton("关闭",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							System.out.println("--->cancel the dialog!");
						}
					});
			builder.create().show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

}
