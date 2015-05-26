package com.utopia.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ilive.structs.News;
/**
 * 
 * @author zhuxiao
 *
 */
public class SortNewsByCreateTime {
	public static List<News> sort(List<News> oldNews) throws ParseException {
		// 按升序进行排列，减小时间复杂度
		// System.out.println("in sort fun before--------");
		int flag = 0;
		for (int i = 0; i < oldNews.size() - 1; i++) {
			flag = 0;
			for (int j = 0; j < oldNews.size() - i - 1; j++) {
				if (compareTime(oldNews.get(j).getCreated_at(),
						oldNews.get(j + 1).getCreated_at())) {
					News tempNews = oldNews.get(j);
					oldNews.set(j, oldNews.get(j + 1));
					oldNews.set(j + 1, tempNews);
					flag = 1;
				}
			}
			if (flag == 0)
				break;
			// return oldNews;
		}
		// for(int i=0;i<oldNews.size();i++)
		// System.out.println(oldNews.get(i).getCreated_at());
		// System.out.println("in sort fun after--------");
		return oldNews;
	}

	public static boolean compareTime(String strTime1, String strTime2)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date d1 = df.parse(strTime1);
		Date d2 = df.parse(strTime2);
		return d1.getTime() > d2.getTime();
	}
}
