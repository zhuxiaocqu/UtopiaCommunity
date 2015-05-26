package com.utopia.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import com.androidquery.AQuery;
import com.ilive.structs.Store;
import com.utopia.activity.R;

public class DialPopWindowDialog {
	private Context context = null;
	private PopupWindow popupWindow = null;
	private String name;
	private String phoneNumber;

	public DialPopWindowDialog(Context context, PopupWindow popupWindow,
			String name, String phoneNumber) {
		this.context = context;
		this.popupWindow = popupWindow;
		if (name == null)
			this.name = "";
		else
			this.name = name;

		if (phoneNumber == null)
			this.phoneNumber = "";
		else
			this.phoneNumber = phoneNumber;
	}

	public void selectPopFun() {
		final View jumpDialog = LayoutInflater.from(context).inflate(
				R.layout.dialstoredialogstyle, null, false);
		AQuery aqDialog = new AQuery(jumpDialog);

		BitmapDrawable bd = (BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.conv_tel_dig);
		int height = bd.getBitmap().getHeight();
		int width = bd.getBitmap().getWidth();
		jumpDialog.getBackground().setAlpha(190);
		popupWindow = new PopupWindow(jumpDialog, width, height, true);
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		popupWindow.showAtLocation(jumpDialog, Gravity.CENTER, 0, 0);

		aqDialog.id(R.id.Textview_ConvToolsAdapter_title).text(
				"是否拨号 \n" + name + "\n" + phoneNumber + "  ?");
		clickevent(aqDialog);
	}

	public void clickevent(AQuery aqDialog) {
		aqDialog.id(R.id.Button_ConvToolsAdapter_ok).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phoneNumber));

						context.startActivity(intent);
						popupWindow.dismiss();
					}
				});
		aqDialog.id(R.id.Button_ConvToolsAdapter_cancel).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
					}
				});
	}
}
