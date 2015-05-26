package com.utopia.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
public class WriteObjectToFile {
	private Context context;

	public WriteObjectToFile(Context context) {
		this.context = context;
	}

	public void writeList(String fileName, List<HashMap<String, Object>> list)
			throws IOException {
		FileOutputStream fileStream = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ObjectOutputStream writeObject = new ObjectOutputStream(fileStream);
		writeObject.writeObject(list);
		writeObject.flush();
		writeObject.close();
		fileStream.close();
	}

	public void writeHash(String fileName, HashMap<Long, Integer> hash)
			throws IOException {
		FileOutputStream fileStream = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ObjectOutputStream writeObject = new ObjectOutputStream(fileStream);
		writeObject.writeObject(hash);
		writeObject.flush();
		writeObject.close();
		fileStream.close();
	}

	public void writeDateObj(String fileName, Date date) throws IOException {
		FileOutputStream fileStream = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ObjectOutputStream writeObject = new ObjectOutputStream(fileStream);
		writeObject.writeObject(date);
		writeObject.flush();
		writeObject.close();
		fileStream.close();
	}

	public void writeMessageObj(String fileName, List<MailMessage> message)
			throws IOException {
		FileOutputStream fileStream = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ObjectOutputStream writeObject = new ObjectOutputStream(fileStream);
		writeObject.writeObject(message);
		writeObject.flush();
		writeObject.close();
		fileStream.close();
	}
}
