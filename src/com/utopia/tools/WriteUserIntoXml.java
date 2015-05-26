package com.utopia.tools;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ilive.structs.User;

public class WriteUserIntoXml {
	public static void writeUser(Context context, User user, String fileName) {
		SharedPreferences preferences = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		// 创建字节输入流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			// 将对象写入字节流
			oos.writeObject(user);
			// 将字节流编码成base64的字符窜
			String user_Base64 = new String(Base64.encodeBase64(baos
					.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString("user", user_Base64);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
