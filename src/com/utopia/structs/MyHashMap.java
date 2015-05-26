package com.utopia.structs;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class MyHashMap extends HashMap<String, Object> implements Parcelable {

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

}
