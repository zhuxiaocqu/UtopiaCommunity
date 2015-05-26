package com.utopia.tools;

import java.util.HashMap;
import java.util.Map;

import com.ilive.structs.News;
import com.utopia.activity.R;
import com.utopia.structs.HashMapData;
/**
 * 
 * @author zhuxiao
 * 协助完成JSon完成新闻数据的获取
 *
 */
public class CovertNewsToListData {
	private News news;

	public CovertNewsToListData(News news) {
		this.news = news;
	}

	// 此处有一个问题，author有一个表可以查询吗？？？？
	public String getAuthorNameById(long id) {
//		return String.valueOf(id);
		return "author";
	}

	public HashMap<String, Object> covert() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(HashMapData.TYPE_ID, selectTypeId(news.getTypeid()));
		map.put(HashMapData.TITLE, news.getTitle());
		map.put(HashMapData.AUTHOR, getAuthorNameById(news.getAuthorid()));
		map.put(HashMapData.AUTHORNAME, news.getauthorname());
		map.put(HashMapData.CREATTIME, news.getCreated_at());
		// 获取图片的地址
		map.put(HashMapData.PICSTR, news.getOriginal_pic());
		map.put(HashMapData.TEXT, news.getText());
		map.put(HashMapData.U_ID, news.get_id());
		return map;
	}

	public int selectTypeId(long origicId) {
		if (origicId == 1) {
			return 1;
		} else if (origicId == 2) {
			return 2;
		} else if (origicId == 3) {
			return 3;
		} else if (origicId == 4) {
			return 4;
		} else
			return 0;
	}
}
