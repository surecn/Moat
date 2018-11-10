package com.asyncaction.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

public class StringUtils {
	
	public final static String UTF_8 = "utf-8";
	
	//将hashmap中得参数用url方式连接
	public static String hashMapToUrlString(HashMap<String, String> parameters) {
		StringBuffer urlbuff = new StringBuffer();
		if (parameters != null && parameters.size() > 0) {
			Set<String> keySet = parameters.keySet();
			for (String key : keySet) {
				try {
					urlbuff.append(key).append("=").append(URLEncoder.encode(parameters.get(key), UTF_8)).append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			urlbuff.deleteCharAt(urlbuff.length() - 1);
		}
		return urlbuff.toString();
	}
	
	//将字符中全角转换成半角
	public static String ToDBC(String input) {  
	   char[] c = input.toCharArray();  
	   for (int i = 0; i< c.length; i++) {
	       if (c[i] == 12288) {  
	         c[i] = (char) 32;  
	         continue;  
	       }
	       if (c[i]> 65280&& c[i]< 65375)  
	          c[i] = (char) (c[i] - 65248);  
	   }  
	   return new String(c);  
	}
}
