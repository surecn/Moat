package com.asyncaction.http;

import java.util.ArrayDeque;
import java.util.HashMap;


/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public class HttpForm {
	public enum HttpMethod {
		GET, POST, PUT
	}
	private String mUrl;
	private HttpMethod mMethod;
	private int mTimeout = -1;
	private HashMap<String, String> mTextParameters;
	private ArrayDeque<HttpFileBody> mFileParameters;
	private HashMap<String, String> mHeaderParameters;

	public HttpForm(String url) {
		mUrl = url;
		mMethod = HttpMethod.GET;
		mTextParameters = new HashMap<String, String>();
		mFileParameters = new ArrayDeque<HttpFileBody>();
		mHeaderParameters = new HashMap<String, String>();
	}

	/**
	 * 设置请求的超时时间
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		mTimeout = timeout;
	}

	/**
	 * 获取改请求的超时时间
	 * @return
	 */
	public int getTimeout() {
		return mTimeout;
	}

	/**
	 * 获取http请求的方法
	 * @return
	 */
	public HttpMethod getMethod() {
		return mMethod;
	}

	/**
	 * 设置请求的方法
	 * @param method
	 */
	public void setMethod(HttpMethod method) {
		this.mMethod = method;
	}

	/**
	 * 获取请求的完整url
	 * @param url
	 */
	public String getUrl() {
		return mUrl;
	}

	/**
	 * 设置请求的完整url
	 * @param url
	 */
	public void setUrl(String url) {
		this.mUrl = url;
	}

	/**
	 * 添加一个参数
	 * @param name
	 * @param value
	 */
	public void addTextParameter(String name, String value) {
		mTextParameters.put(name, value);
	}

	/**
	 * 设置上传的header
	 * @param name
	 * @param value
	 */
	public void addHeaderParameter(String name, String value) {
		mHeaderParameters.put(name, value);
	}

	/**
	 * 添加一个上传的文件
	 * @param httpFileBody
	 */
	public void addFileBody(HttpFileBody httpFileBody) {
		mFileParameters.add(httpFileBody);
	}

	/**
	 * 获取http请求的参数
	 * @return
	 */
	public HashMap<String, String> getTextParameters() {
		return mTextParameters;
	}

	/**
	 * 获取http请求的上传的文件
	 * @return
	 */
	public ArrayDeque<HttpFileBody> getFileParameters() {
		return mFileParameters;
	}

	/**
	 * 获取http请求的header
	 * @return
	 */
	public HashMap<String, String> getHeaderParameters() {
		return mHeaderParameters;
	}

	/**
	 * 清楚所有数据
	 */
	public void clear() {
		if (mTextParameters != null) {
			mTextParameters.clear();
			mTextParameters = null;
		}
		if (mFileParameters != null) {
			mFileParameters.clear();
			mFileParameters = null;
		}
		if (mHeaderParameters != null) {
			mHeaderParameters.clear();
			mHeaderParameters = null;
		}
		mUrl = null;
		mTimeout = -1;
	}
	
}
