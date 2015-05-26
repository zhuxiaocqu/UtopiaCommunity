package com.utopia.tools;

import java.io.File;

import android.content.Context;
/**
 * 
 * @author zhuxiao
 *
 */
public class ClearLocalCache {
	private Context context;
	private File filePath;
	private File cachePath;
	private String sharePath;

	public ClearLocalCache(Context oContext) {
		context = oContext;
		filePath = context.getFilesDir();
		cachePath = context.getCacheDir();
		// System.out.println("filePath->" + filePath);
		// System.out.println("cachePath->" + cachePath);
		// context.get
	}

	private void clearFiles(File filePath) {
		System.out.println("------clearFiles------");
		File[] fileList = null;
		// File[] newFilePath=null;
		// int restFiles=1;
		if (filePath.isDirectory()) {
			fileList = filePath.listFiles();
		}
		if (fileList.length > 0)
			for (File file : fileList) {
				System.out.println(file);
				if (file.isDirectory())
					clearFiles(file);
				else if (file.isFile()) {
					file.delete();
					// restFiles = filePath.listFiles().length;
				}
			}
	}

	private void clearCaches(File cachePath) {
		System.out.println("------clearCaches------");
		File[] cacheList = null;
		// File[] newCacheList=null;

		if (cachePath.isDirectory()) {
			cacheList = cachePath.listFiles();
		}
		if (cacheList.length > 0)
			for (File file : cacheList) {
				System.out.println(file);
				if (file.isDirectory()) {
					clearCaches(file);
					System.out.println("++aquery dir++");
				} else if (file.isFile()) {
					file.delete();
					System.out.println("==aquery file==");
					// restFile = cachePath.listFiles().length;
				}
			}
	}

	private static int sum = 0;

	public int calSumOfFiles(File file) {
		// int sum=0;
		if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			for (File fileTemp : fileList) {
				if (fileTemp.isFile())
					sum++;
				else if (fileTemp.isDirectory())
					calSumOfFiles(fileTemp);
			}
			return sum;
		} else
			return 1;
	}

	public boolean clear() {
		clearFiles(filePath);
		clearCaches(cachePath);
		if (calSumOfFiles(filePath) == 0 && calSumOfFiles(cachePath) == 0)
			return true;
		else
			return false;
	}
}
