package com.utopia.tools;

import com.utopia.activity.R;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

@SuppressLint("ResourceAsColor")
public class EditTextBackTxtHelper implements TextWatcher,
		OnFocusChangeListener {
	private EditText editText;
	private String defaultStr;
	private int inputType; // 但为0 是表示普通
	private int maxLength;

	private CharSequence temp;
	private int editStart;
	private int editEnd;

	public EditTextBackTxtHelper(EditText editText, String defaultStr,
			int inputType, int maxLength) {
		this.editText = editText;
		this.defaultStr = defaultStr;
		this.inputType = inputType;
		this.maxLength = maxLength;
	}

	@Override
	public void afterTextChanged(Editable s) {
		editStart = editText.getSelectionStart();
		editEnd = editText.getSelectionEnd();
		System.out.println("length:  " + temp.length() + "  max: " + maxLength);
		System.out.println("temp :" + temp);
		if (temp.length() < defaultStr.length()) {
			if (temp.length() > maxLength) {
				s.delete(editStart - 1, editEnd);
				editText.setText(s);
				editText.setSelection(editText.getText().length());
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		temp = s;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (count > 0 && !s.equals(defaultStr)) {
			String buffStrPre = "";
			if (start != 0) {
				buffStrPre = s.subSequence(0, start).toString();
			}
			String buffStrTail = s.subSequence(start + 1, s.length())
					.toString();
			String str = buffStrPre + buffStrTail;
			if (str.equals(defaultStr)) {
				editText.setTextColor(Color.BLACK);
				editText.setText(s.subSequence(start, start + count));
				editText.setInputType(inputType);
				editText.setSelection(editText.getText().length());
			}
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		String strOfText = editText.getText().toString();
		if (hasFocus) {
			if (strOfText.equals(defaultStr))
				editText.setSelection(0);
			else
				editText.setSelection(editText.getText().length());
		} else {
			if (strOfText.equals("")) {
				editText.setTextColor(R.color.edittext_backtxt_color);
				editText.setText(defaultStr);
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			editText.setInputType(inputType);
			if (strOfText.equals(defaultStr))
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

		}
	}
}
