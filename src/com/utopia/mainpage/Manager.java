package com.utopia.mainpage;

import com.utopia.activity.ManagerCommuInfoActivity;
import com.utopia.activity.ManagerCompActivity;
import com.utopia.activity.R;
import com.utopia.tools.DialPopWindowDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class Manager implements OnClickListener {
	private ImageView mana_info_view = null;
	private ImageView mana_call_view = null;
	private ImageView mana_comp_view = null;
	private ImageView mana_bill_view = null;
	private View viewPage;

	PopupWindow dialPopupWindow; // 拨打电话的对话框
	String manager_num = "10086"; // 静态设置的管理处的电话号码

	public Manager(View viewPage) {
		this.viewPage = viewPage;
		mana_info_view = (ImageView) viewPage.findViewById(R.id.mana_info_view);
		mana_call_view = (ImageView) viewPage.findViewById(R.id.mana_call_view);
		mana_bill_view = (ImageView) viewPage.findViewById(R.id.mana_bill_view);
		mana_comp_view = (ImageView) viewPage.findViewById(R.id.mana_comp_view);
		mana_info_view.setOnClickListener(this);
		mana_call_view.setOnClickListener(this);
		mana_comp_view.setOnClickListener(this);
		mana_bill_view.setOnClickListener(this);
	}

	public void turnToInfo() {
		Intent intent = new Intent();
		intent.setClass(viewPage.getContext(), ManagerCommuInfoActivity.class);
		viewPage.getContext().startActivity(intent);
	}

	public void turnToCall() {
		DialPopWindowDialog dialDialog = new DialPopWindowDialog(
				viewPage.getContext(), dialPopupWindow, "管理处", manager_num);
		dialDialog.selectPopFun();
	}

	public void turnToComp() {

		Intent intent = new Intent();
		intent.setClass(viewPage.getContext(), ManagerCompActivity.class);
		viewPage.getContext().startActivity(intent);
	}

	public void turnToBill() {
		mana_bill_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// intent.setClass(manager.this, manager_bill.class);
				// startActivity(intent);
				Toast.makeText(viewPage.getContext(), "对不起，此功能暂未实现",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mana_info_view:
			turnToInfo();
			break;
		case R.id.mana_call_view:
			turnToCall();
			break;
		case R.id.mana_comp_view:
			turnToComp();
			break;
		case R.id.mana_bill_view:
			turnToBill();
			break;
		}
	}
}
