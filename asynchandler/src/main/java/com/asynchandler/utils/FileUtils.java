package com.asynchandler.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.R;
import android.content.Context;
import android.widget.TextView;

public class FileUtils {
	
	//把文件转换成字节数组
	public static byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}
	
	/**
	 * 获取文件夹大小
	 * @param directory
	 * @return
	 */
	public static int getDirectorySize(File directory) {
		if (directory.listFiles() == null)
			return 0;
		int size = 0;
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				size += getDirectorySize(file);
			} else {
				size += file.length();
			}
		}
		return size;
	}
	
	/**
	 * 拷贝文件
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copyFile(File src, File dst) throws IOException {
		FileInputStream srcFis = null;
		FileOutputStream dstFos = null;
		try {
			srcFis = new FileInputStream(src);
			if (!dst.exists()) {
				if (!dst.getParentFile().exists()) {
					dst.mkdirs();
				}
				dst.createNewFile();
			}
			dstFos = new FileOutputStream(dst);
			byte[] b = new byte[20000];
			int temp = 0;
			while ((temp = srcFis.read(b)) != -1)
				dstFos.write(b, 0, temp);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			if (srcFis != null) {
				srcFis.close();
			}
			srcFis = null;
			if (dstFos != null) {
				dstFos.close();
			}
			dstFos = null;
		}
	}

	/**
	 * 拷贝文件夹
	 * @param src
	 * @param dst
	 */
	public static void copyDir(File src, File dst) {
		if (!dst.exists()) {
			dst.mkdirs();
		}
		for (File tFile : src.listFiles()) {
			if (tFile.isDirectory())
				copyDir(tFile, new File(dst, tFile.getName()));
			else
				try {
					copyFile(tFile, new File(dst, tFile.getName()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		}
	}
	
	/**
	 * 删除文件夹
	 * @param file
	 */
	public static void removeDir(File file) {
		if (file.exists()) {
			for (File tFile : file.listFiles()) {
				if (tFile.isDirectory())
					removeDir(tFile);
				else {
					tFile.delete();
				}
			}
			file.delete();
		}
	}

	/**
	 * 读取raw文件
	 * 
	 * @param context
	 * @param tv
	 * @param rawId
	 *            传R.raw.my_raw
	 */
	private static void rawRead(Context context, TextView tv, int rawId) {
		String ret = "";
		try {
			InputStream is = context.getResources().openRawResource(rawId);
			int len = is.available();
			byte[] buffer = new byte[len];
			is.read(buffer);
			ret = EncodingUtils.getString(buffer, "utf-8");
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tv.setText(ret);
	}

	/**
	 * 读取assets文件
	 * @param context
	 * @param tv
	 * @param fileName  assets下文件，如"test/my_assets_test.txt"
	 */
	private static void assetsRead(Context context, TextView tv, String fileName) {
		String ret = "";

		try {
			InputStream is = context.getResources().getAssets().open(fileName);
			int len = is.available();
			byte[] buffer = new byte[len];

			is.read(buffer);
			ret = EncodingUtils.getString(buffer, "utf-8");

			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		tv.setText(ret);
	}
	
}
