package com.utopia.tools;

import java.util.Comparator;

/*
 * 将ConvToolsDataObject对象列表按count字段从大到小排序
 * */
public class sortByCount implements Comparator<ConvToolsDataObject> {

	@Override
	public int compare(ConvToolsDataObject lhs, ConvToolsDataObject rhs) {
		// TODO Auto-generated method stub
		if (lhs.getCount() < rhs.getCount())
			return 1;
		else if (lhs.getCount() > rhs.getCount())
			return -1;
		else
			return 0;
	}

}
