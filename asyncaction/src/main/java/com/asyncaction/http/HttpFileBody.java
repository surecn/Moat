package com.asyncaction.http;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public class HttpFileBody {

	public final static String PREFIX_ASSETS = "file:///android_asset/";

	public final static String PREFIX_RES = "file:///android_res/";

	private String mName;
	private String mFilePath;
	private String mContentType;
	private Context mContext;
	
	public HttpFileBody(Context context, String name, String filePath, String contentType) {
		mContext = context;
		mName = name;
		mFilePath = filePath;
		mContentType = contentType;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}

	public String getContentType() {
		return mContentType;
	}

	public void setContentType(String contentType) {
		this.mContentType = contentType;
	}

	/*package*/void writeToStream(OutputStream os) throws Exception {
		if (mFilePath == null) {
			return;
		}
		if (mContext == null) {
			throw new NullPointerException();
		}

		InputStream is = getFildeInputStream();

		if (is == null) {
			throw new FileNotFoundException();
		}

		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		try {
			byte[] b = new byte[1024];
			int n;
			while ((n = bis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
		} finally {
			bis.close();
			bos.close();
		}

	}

	/*package*/InputStream getFildeInputStream() {
		InputStream is = null;
		String mTempFilePath = mFilePath.toLowerCase();
		if (mTempFilePath.startsWith(PREFIX_ASSETS)) {
			//读取assets文件
			mTempFilePath = mFilePath.substring(PREFIX_ASSETS.length());
			try {
				is = mContext.getAssets().open(mTempFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (mTempFilePath.startsWith(PREFIX_RES)) {
			//TODO res文件读取
		} else {
			try {
				is = new FileInputStream(new File(mFilePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return is;
	}
}
