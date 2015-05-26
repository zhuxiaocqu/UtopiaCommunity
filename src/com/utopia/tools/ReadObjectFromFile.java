package com.utopia.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.utopia.structs.MailMessage;

import android.content.Context;
/**
 * 
 * @author zhuxiao
 *
 */
public class ReadObjectFromFile {
	private Context context;

	public ReadObjectFromFile(Context context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> readList(String fileName)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		FileInputStream fileStream = context.openFileInput(fileName);
		ObjectInputStream readObject = new ObjectInputStream(fileStream);
		list = (List<HashMap<String, Object>>) readObject.readObject();
		readObject.close();
		fileStream.close();
		return list;
	}

	public HashMap<Long, Integer> readHash(String fileName)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		HashMap<Long, Integer> hash = new HashMap<Long, Integer>();

		FileInputStream fileStream = context.openFileInput(fileName);
		ObjectInputStream readObject = new ObjectInputStream(fileStream);
		hash = (HashMap<Long, Integer>) readObject.readObject();
		readObject.close();
		fileStream.close();
		return hash;
	}

	public Date readDateObj(String fileName) throws StreamCorruptedException,
			IOException, ClassNotFoundException {
		Date date = null;
		FileInputStream fileStream = context.openFileInput(fileName);
		ObjectInputStream readObject = new ObjectInputStream(fileStream);
		date = (Date) readObject.readObject();
		readObject.close();
		fileStream.close();
		return date;
	}

	public List<MailMessage> readMessageObj(String fileName)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		List<MailMessage> message = null;
		FileInputStream fileStream = context.openFileInput(fileName);
		ObjectInputStream readObject = new ObjectInputStream(fileStream);
		message = (List<MailMessage>) readObject.readObject();
		readObject.close();
		fileStream.close();
		return message;
	}

}
