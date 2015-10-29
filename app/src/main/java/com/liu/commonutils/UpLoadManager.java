package com.liu.commonutils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.http.conn.ConnectTimeoutException;

/**
 * 文件上传类
 * 
 * @author liu
 * 
 *         2014-9-3
 */
public class UpLoadManager {
	// 上传的url
	private String mUrl;
	// 上传的path路径
	private String mFilePath;
	// 上传的其他属性（udid,uid）
	private Map<String, String> mFieldValues;
	private UpLoadCallBack mUpLoadCallBack;
	private String mName, mFileName;

	public void setUpLoadCallBack(UpLoadCallBack mUpLoadCallBack) {
		this.mUpLoadCallBack = mUpLoadCallBack;
	}

	public UpLoadManager(String url, String mFilePath,
			Map<String, String> mFieldValues, String name, String fileName) {
		this.mUrl = url;
		this.mFilePath = mFilePath;
		this.mFieldValues = mFieldValues;
		this.mName = name;
		this.mFileName = fileName;

	}

	public void upLoad() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		HttpURLConnection con = null;
		try {
			URL url = new URL(mUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(3000);
			con.setReadTimeout(5000);
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设定传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			if (mFieldValues != null && mFieldValues.size() > 0) {
				Set<String> mFields = mFieldValues.keySet();
				for (String name : mFields) {
					ds.writeBytes(twoHyphens + boundary + end);
					ds.writeBytes("Content-Disposition: form-data; " + "name="
							+ name + end + end);
					// ds.writeBytes(mFieldValues.get(name));
					ds.write(mFieldValues.get(name).getBytes());
					ds.writeBytes(end);

				}
			}
			if (mFilePath != null) {
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; " + "name="
						+ mName + ";filename=" + mFileName + end + end);
				/* 取得文件的FileInputStream */
				FileInputStream fStream = new FileInputStream(mFilePath);
				/* 设定每次写入1024bytes */
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				int length = -1;
				/* 从文件读取数据到缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将数据写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end + twoHyphens + boundary + twoHyphens + end);
				/* close streams */
				fStream.close();
			}
			ds.flush();
			ds.close();
			// 取得response内容
			InputStream is = con.getInputStream();
			if (is != null) {
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				if (mUpLoadCallBack != null) {
					mUpLoadCallBack.upLoadBack(b.toString().trim());
				}
			}
			/* 关闭DataOutputStream */
			is.close();
		} catch (ConnectTimeoutException e) {
			e.getMessage();
			if (mUpLoadCallBack != null) {
				mUpLoadCallBack.upLoadBack(null);
			}
		} catch (SocketTimeoutException e) {
			e.getMessage();
			if (mUpLoadCallBack != null) {
				mUpLoadCallBack.upLoadBack(null);
			}
		} catch (SocketException e) {
			e.getMessage();
			if (mUpLoadCallBack != null) {
				mUpLoadCallBack.upLoadBack(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (mUpLoadCallBack != null) {
				mUpLoadCallBack.upLoadBack(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mUpLoadCallBack != null) {
				mUpLoadCallBack.upLoadBack(null);
			}
		} finally {
			if (con != null) {
				con.disconnect();
			}

		}
	}

	public interface UpLoadCallBack {
		void upLoadBack(String result);
	}
}
