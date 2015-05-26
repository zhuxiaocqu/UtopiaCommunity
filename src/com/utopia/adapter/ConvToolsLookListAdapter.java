package com.utopia.adapter;

import java.util.ArrayList;

import com.utopia.tools.ConvToolsDataObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ConvToolsLookListAdapter extends BaseAdapter implements
		OnItemClickListener {

	private LayoutInflater layoutInflater;
	private Context context;
	String str_inflater = Context.LAYOUT_INFLATER_SERVICE;
	private ArrayList<ConvToolsDataObject> listInner = null;

	public ConvToolsLookListAdapter(Context context) {
		this.context = context;
	};

	public ConvToolsLookListAdapter(Context context,
			ArrayList<ConvToolsDataObject> list) {
		this.context = context;
		this.listInner = list;
		layoutInflater = (LayoutInflater) context
				.getSystemService(str_inflater);
	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
