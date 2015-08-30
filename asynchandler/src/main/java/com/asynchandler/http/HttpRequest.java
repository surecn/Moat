package com.asynchandler.http;

import com.asynchandler.exception.HttpResponseException;
import com.asynchandler.rest.IHttpResult;
import com.asynchandler.utils.FileUtils;
import com.asynchandler.utils.StringUtils;
import com.asynchandler.utils.log;

import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public class HttpRequest {
	
	private final static String BOUNDARY = "--------httppost123";
	private static int TIMEOUT = 30000;

	/**
	 * 设置超时时间
	 * @param timeout
	 */
	public static void setDefaultTimeout(int timeout) {
		TIMEOUT = timeout;
	}

	/**
	 * 设置是否重定向
	 * @param followRedirects
	 */
	public static void setDefaultFollowRedirects(boolean followRedirects) {
		HttpURLConnection.setFollowRedirects(followRedirects);
	}

	/**
	 * 进行http请求
	 * @param httpForm
	 * @return
	 * @throws Exception
	 */
	public static IHttpResult request(HttpForm httpForm) throws Exception {
		HttpURLConnection urlConnection = null;
		switch(httpForm.getMethod()) {
		case GET:
			urlConnection = openGetRequest(httpForm);
			break;
		case POST:
			urlConnection = openPostRequest(httpForm);
			break;
		case PUT:
			urlConnection = openPutRequest(httpForm);
			break;
		}

		int code = urlConnection.getResponseCode();
		if (code != 200) {
			throw new HttpResponseException(code);
		}
		return new HttpResult(urlConnection);
	}

	private static void setTimeout(HttpURLConnection conn, int timeout) {
		if (timeout > 0) {
			conn.setConnectTimeout(timeout / 2);
			conn.setReadTimeout(timeout / 2);
		} else {
			conn.setConnectTimeout(TIMEOUT / 2);
			conn.setReadTimeout(TIMEOUT / 2);
		}
	}

	private static void setHeaders(HttpURLConnection conn, HashMap<String, String> headers) {
		if (headers != null && headers.size() > 0) {
			for (HashMap.Entry<String, String> entry : headers.entrySet()) {
				conn.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
	}
	
    //connection初始化
	private static HttpURLConnection initConnection(URL url, HttpForm httpForm) throws Exception {
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		setTimeout(urlConnection, httpForm.getTimeout());
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		HashMap<String, String> headers = httpForm.getHeaderParameters();
		setHeaders(urlConnection, headers);
		return urlConnection;
	}
	
	private static HttpURLConnection openGetRequest(HttpForm httpForm) throws Exception {
		String urlString = httpForm.getUrl() + "?" + StringUtils.hashMapToUrlString(httpForm.getTextParameters());
		log.i("HTTP REQUEST GET:" + urlString);
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = initConnection(url, httpForm);
		urlConnection.setRequestMethod("GET");
		return urlConnection;
	}
	
	private static HttpURLConnection openPostRequest(HttpForm httpForm) throws Exception {
		log.i("HTTP REQUEST POST:" + httpForm.getUrl());
		URL url = new URL(httpForm.getUrl());
		HttpURLConnection urlConnection = initConnection(url, httpForm);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
		try {
			dos.writeBytes(StringUtils.hashMapToUrlString(httpForm.getTextParameters()));
		} finally {
			dos.flush();
			dos.close();
		}
		return urlConnection;
	}
	
	private static HttpURLConnection openPutRequest(HttpForm httpForm) throws Exception {
		log.i("HTTP REQUEST PUT:" + httpForm.getUrl());
		URL url = new URL(httpForm.getUrl());
		HttpURLConnection urlConnection = initConnection(url, httpForm);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
		try {
			writeTextParamters(dos, httpForm.getTextParameters());
			writeFileParamters(dos, httpForm.getFileParameters());
			writeParamtersEnd(dos);
		} finally {
			dos.flush();
			dos.close();
		}
		return urlConnection;
	}
	
    //普通字符串数据
	private static void writeTextParamters(DataOutputStream ds, HashMap<String, String> textParamters) throws Exception {
		Set<String> keySet = textParamters.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = textParamters.get(name);
			ds.writeBytes("--" + BOUNDARY + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"\r\n");
			ds.writeBytes("\r\n");
			ds.writeBytes(encode(value) + "\r\n");
		}
	}
    //文件数据
	private static void writeFileParamters(DataOutputStream ds, ArrayDeque<HttpFileBody> fileParamters) throws Exception {
		for (HttpFileBody fileBody : fileParamters) {
			String name = fileBody.getName();
			File file = new File(fileBody.getFilePath());
			ds.writeBytes("--" + BOUNDARY + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"; filename=\"" + encode(file.getName()) + "\"\r\n");
			ds.writeBytes("Content-Type: " + fileBody.getContentType() + "\r\n");
			ds.writeBytes("\r\n");
			fileBody.writeToStream(ds);
			ds.writeBytes("\r\n");
		}
	}

	//添加结尾数据
	private static void writeParamtersEnd(DataOutputStream ds) throws Exception {
		ds.writeBytes("--" + BOUNDARY + "--" + "\r\n");
		ds.writeBytes("\r\n");
	}
	
	// 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
    private static String encode(String value) throws Exception{
    	return URLEncoder.encode(value, "UTF-8");
    }

}

