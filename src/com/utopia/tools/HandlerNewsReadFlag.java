package com.utopia.tools;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.HashMap;

import android.content.Context;

import com.utopia.structs.HashMapData;
/**
 * 
 * @author zhuxiao
 *
 */
public class HandlerNewsReadFlag {
	public static HashMap<Long, Integer> hash= new HashMap<Long, Integer>();
	private Context context;

	public HandlerNewsReadFlag(Context context) {
		this.context = context;
	}

	public void saveReadFlagHashMapToFile() {
		WriteObjectToFile writeFlag = new WriteObjectToFile(context);
		try {
			writeFlag.writeHash(HashMapData.FILE_NAME_READ_FLAG, hash);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getReadFlagHashMapFromFile() {
		ReadObjectFromFile readFlag = new ReadObjectFromFile(context);
		try {
			// 从文件中读取保存了每条新闻已读标志位的hashmap
			hash = readFlag.readHash(HashMapData.FILE_NAME_READ_FLAG);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
