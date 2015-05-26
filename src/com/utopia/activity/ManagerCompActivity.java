package com.utopia.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.androidquery.AQuery;
import com.ilive.request.LettersUploadRequest;
import com.utopia.activity.R;
import com.utopia.adapter.MailViewAdapter;
import com.utopia.structs.Flags;
import com.utopia.structs.MailMessage;
import com.utopia.tools.ImageUtil;
import com.utopia.tools.ReadObjectFromFile;
import com.utopia.tools.WriteObjectToFile;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerCompActivity extends Activity {

	EditText manager_comp_cont_view = null;
	Button manager_comp_sent_butt = null;
	ImageView manager_comp_conc_butt = null;
	String manager_comp_cont_str;
	private AQuery ManagerCompAq;
	private Context context;
	public final int KIND_SEND = 0;
	public final int KIND_RECEIVE = 1;
	public List<MailMessage> messageData;
	public MailViewAdapter mailAdapter;
	public WriteObjectToFile writeObject;
	public ReadObjectFromFile readObject;
	public String MessageFileName = "mailMessage.msg";
	public String messageFilePath;
	
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private String picname;
	public static String picPath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_comp_layout);
		MyApplication.getInstance().addActivity(this);
		ManagerCompAq = new AQuery(this);

		// 初始化数据 对象
		initData();
		// 初始化保存历史邮件工具
		intiIOTools();

		// System.out.println(context.getFilesDir());
		try {
			messageData = readObject.readMessageObj(MessageFileName);
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
		System.out.println("messageData 大小->" + messageData.size());

		// 初始化邮件列表
		initList();
		setSmartHomeEvent();
	}

	public void initData() {
		context = ManagerCompActivity.this;
		messageData = new ArrayList<MailMessage>();
		messageData.add(new MailMessage(KIND_RECEIVE, "住户你好"));
		messageData.add(new MailMessage(KIND_SEND, "为什么物业费又长了，能给出让我们信服的理由么？"));
	}

	public void initList() {
		mailAdapter = new MailViewAdapter(context, messageData);
		ManagerCompAq.id(R.id.manager_listview_talk_record).adapter(mailAdapter);
		if (messageData.size() >= 1)
			ManagerCompAq.id(R.id.manager_listview_talk_record).setSelection(
					messageData.size() - 1);

	}
	
	public void setSmartHomeEvent() {
		ManagerCompAq.id(R.id.manager_comp_sent_butt).clicked(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String messageStr = ManagerCompAq.id(R.id.manager_comp_cont_view)
						.getText().toString();
				if (messageStr.equals("")) {
					Toast.makeText(context, "消息不能为空", 2000)
							.show();
					return;
				}

				MailMessage message = new MailMessage(KIND_SEND,
						messageStr);
				messageData.add(message);
				// 方便秦娟调试 注释部分-------------------
				try {
					writeObject.writeMessageObj(MessageFileName,
							messageData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ManagerCompAq.id(R.id.manager_listview_talk_record).adapter(
						new MailViewAdapter(context,
								messageData));
				// mailAdapter.notifyDataSetChanged();
				// -------------------
				ManagerCompAq.id(R.id.manager_comp_cont_view).text("");
				if (messageData.size() >= 1)
					ManagerCompAq.id(R.id.manager_listview_talk_record).setSelection(
							messageData.size() - 1);
				upLoadLetters(message);

			}
		});

		ManagerCompAq.id(R.id.manager_capture_image).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPicByCamera();
			}
		});
		ManagerCompAq.id(R.id.manager_send_image).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPicFromAlbum();
			}
		});
		ManagerCompAq.id(R.id.manager_message_record_image).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "此功能暂未开通", 1000)
								.show();
					}
				});
		ManagerCompAq.id(R.id.manager_pls_talk).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "此功能暂未开通", 1000).show();
			}
		});
		ManagerCompAq.id(R.id.manager_imageview_sms_button_insert).clicked(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "此功能暂未开通", 1000)
								.show();
					}
				});

	}

	public void upLoadLetters(MailMessage message) {
		LettersUploadRequest lurequest = new LettersUploadRequest(
				ManagerCompAq, "LettersUploadCallBack");
		File upfile = null;
		System.out.println("MainActivity.picPath->" + picPath);
		if (picPath != null)
			upfile = new File(picPath);
		lurequest.request(MainActivity.userInfo.getAccount(),
				MainActivity.userInfo.getPassword(), "1", message.getContent(),
				message.getContent(), upfile);
	}

	public void intiIOTools() {
		messageFilePath = context.getFilesDir() + "/mailMessage.msg";
		File file = new File(messageFilePath);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			System.out.println("file already exsit");
		writeObject = new WriteObjectToFile(context);
		readObject = new ReadObjectFromFile(context);
	}
	
	public void getPicByCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}

	public void getPicFromAlbum() {
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType("image/*");
		startActivityForResult(getAlbum, PHOTO_PICKED_WITH_DATA);
	}

	// 照相和相册图片监听 腾讯微博授权调回监听
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Bitmap bitmap = null;
			switch (requestCode) {
			case CAMERA_WITH_DATA: {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.v("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

				if (bitmap.getWidth() > bitmap.getHeight())
					bitmap = ImageUtil.zoomBitmap(bitmap, 800, 480);
				else
					bitmap = ImageUtil.zoomBitmap(bitmap, 480, 800);
				String partenPath = "/mnt/sdcard/images/";
				FileOutputStream b = null;
				File file = new File(partenPath);
				if (!file.exists())
					file.mkdirs();// 创建文件夹
				SimpleDateFormat sFormat = new SimpleDateFormat(
						"yyyyMMdd_HHmmss");// 按时间命名
				picname = sFormat.format(new Date()).toString();
				picPath = partenPath + picname + ".jpg";
				try {
					b = new FileOutputStream(picPath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					// aqTest.id(R.id.img2).image(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case PHOTO_PICKED_WITH_DATA: {
				try {
					Uri originalUri = data.getData(); // 获得图片的uri
					bitmap = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(originalUri, proj, null, null,
							null); // 好像是android多媒体数据库的封装接口，具体的看Android文档
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); // 按我个人理解
																					// 这个是获得用户选择的图片的索引值
					cursor.moveToFirst(); // 将光标移至开头 ，这个很重要，不小心很容易引起越界
					picPath = cursor.getString(column_index); // 最后根据索引值获取图片路径
					picname = picPath.substring(picPath.lastIndexOf("/") + 1,
							picPath.lastIndexOf(".jpg"));
					// System.out.println(picPath + ":" + picname);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			}

			if (picPath != null) {
				System.out.println(picPath);
				// aqTest.id(R.id.img).image(new File(picPath), 200);
			}

		}
	}

}
