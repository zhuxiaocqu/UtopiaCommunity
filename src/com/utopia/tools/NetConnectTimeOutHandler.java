package com.utopia.tools;

import java.util.Date;
/**
 * 
 * @author zhuxiao
 *
 */
public class NetConnectTimeOutHandler {

	private long startTime;//起始联网时间点
	private long endTime;//终止联网
	private final long TIME_LIMIT=15000;
	private Date date;
	
	public NetConnectTimeOutHandler(){
		date=new Date();
	}
	public void setStartTime(){
		startTime=date.getTime();
	}
	public void getEndTime(){
		endTime=date.getTime();
	}
	public boolean timeoutPolicy(){
		if(endTime-startTime>TIME_LIMIT)
			//联网超时
			return true;
		else
			//联网未超时
			return false;
	}
}
