package com.cn.controller.weChat.util;

import com.alibaba.fastjson.JSONObject;
import com.cn.controller.weChat.util.encrypt.CryptionUtil;
import com.cn.controller.weChat.util.encrypt.MD5;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;


public class HttpClientUtil {
//	private static Logger LOGGER = LoggerFactory.getActionLog(HttpClientUtil.class);
	private static final String APPLICATION_JSON = "application/json";

	/**
	 * post请求
	 * 
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	public static String httpPost(String url, String jsonObject) {
		CloseableHttpClient httpClient = null;
		String result = null;
		try {
			System.out.println("httpPost => ***---------------发送接口机url:------------------------------------" + url);
			System.out.println("httpPost => ***---------------json Content-Type:---------------" + APPLICATION_JSON);
			System.out.println("httpPost => ***---------------发送接口机json--------------------------" + jsonObject);
			// 设置超时时间
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(40000)
					.setConnectionRequestTimeout(40000).build();
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();
			httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			HttpPost httpPost = new HttpPost(url);
			// 设置hearder信息
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			//fixme:把请求参数加密进行处理  适配统一导航规范
			JSONObject data = JSONObject.parseObject(jsonObject);
			JSONObject reqData = data.getJSONObject("reqData");

			if (null != reqData) {
				//fixme：截取字符串
				String reqDataStr = jsonObject.substring(11,jsonObject.length()-1);
				System.out.println("加密前的报文---->>>>>>>>>>>>>>>>>>>>"+reqDataStr);
				MD5 md5 = new MD5();
				String newdata = md5.getMD5ofStr(reqDataStr.trim());
				System.out.println("MD5 加密的报文---->>>>>>>>>>>>>>>>>>>>"+newdata);
				httpPost.addHeader("reqData", CryptionUtil.encode(newdata));
				System.out.println("加密后的报文---->>>>>>>>>>>>>>>>>>>>"+CryptionUtil.encode(newdata));
				Header header= httpPost.getFirstHeader("reqData");
				System.out.println("添加的头部信息-----"+header.getName()+"-----"+header.getValue());
				System.out.println("添加的头部信息-----"+header.toString());

			}

			StringEntity se = new StringEntity(jsonObject.trim(), ContentType.APPLICATION_JSON);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(se);
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 判定返回状态是否成功
			int flag = response.getStatusLine().getStatusCode();
			if (flag == 200) {
				System.out.println("httpPost => ***---------------接口机连接成功------------***" + flag);
				// 获取返回值
				// 如果是下载文件,可以用response.getEntity().getContent()返回InputStream
				// 处理乱码
				// result=new String(result.getBytes("ISO-8859-1"),"GBK");//网页编码为GBK
				result = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
			} else {
				JSONObject params = new JSONObject();
				params.put("respStatus", flag);
				result = params.toJSONString();
				System.out.println("httpPost => ***-------------接口机连接失败------------当前请求的地址为："+url + "{flag}>>>" + flag);
			}
		} catch (ClientProtocolException e) {
//			LOGGER.error("httpPost => ***---------------接口机连接异常------------当前请求的地址为："+url+"---ClientProtocolException:", e);
		} catch (IOException e) {
//			LOGGER.error("httpPost => ***---------------接口机连接异常----------当前请求的地址为："+url+"--IOException", e);
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
//					LOGGER.error("httpPost => 关闭失败", e);
				}
			}

		}
		System.out.println("httpPost => ***---------------接口机返回值------------------------***" + result);
		return result;
	}

	/**
	 * put请求
	 * 
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	public static String httpPut(String url, String jsonObject) {
		CloseableHttpClient httpClient = null;
		String result = null;
		try {
			System.out.println("httpPut => ***---------------发送接口机url:------------------------------------" + url);
			System.out.println("httpPut => ***---------------json Content-Type:---------------" + APPLICATION_JSON);
			System.out.println("httpPut => ***---------------发送接口机json--------------------------" + jsonObject);
			// 设置超时时间
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(40000)
					.setConnectionRequestTimeout(40000).build();
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();
			httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			HttpPut httpPut = new HttpPut(url);
			// 设置hearder信息
			httpPut.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity se = new StringEntity(jsonObject, ContentType.APPLICATION_JSON);
			httpPut.setConfig(requestConfig);
			httpPut.setEntity(se);
			HttpResponse response;
			response = httpClient.execute(httpPut);
			// 判定返回状态是否成功
			int flag = response.getStatusLine().getStatusCode();
			if (flag == 200) {
				System.out.println("httpPut => ***---------------接口机连接成功------------***" + "{" + flag + "}");
				result = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
			} else {
				JSONObject params = new JSONObject();
				params.put("respStatus", flag);
				result = params.toJSONString();
				System.out.println("httpPut => ***---------------接口机连接失败------------***" + "{" + flag + "}");
			}
		} catch (ClientProtocolException e) {
//			LOGGER.error("httpPut => ***---------------接口机连接异常------------ClientProtocolException", e);
		} catch (IOException e) {
//			LOGGER.error("httpPut => ***---------------接口机连接异常------------IOException", e);
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
//					LOGGER.error("httpPut => 关闭失败", e);
				}
			}
		}
		System.out.println("httpPut => ***---------------接口机返回值------------------------***" + result);
		return result;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String httpGet(String url) {
		CloseableHttpClient httpClient = null;
		String result = null;
		try {
			System.out.println("httpGet => ***---------------发送接口机url:------------------------------------" + url);
//			LOGGER.info("httpPut => ***---------------json Content-Type:---------------" + APPLICATION_JSON);
			// 设置超时时间
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(40000)
					.setConnectionRequestTimeout(40000).build();

			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();
			httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			HttpGet httpGet = new HttpGet(url);
			// 设置hearder信息
//			httpGet.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			httpGet.setConfig(requestConfig);
			HttpResponse response;
			response = httpClient.execute(httpGet);
			// 判定返回状态是否成功
			int flag = response.getStatusLine().getStatusCode();
			if (flag == 200) {
				System.out.println("httpGet => ***---------------接口机连接成功------------***" + "{" + flag + "}");
				result = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
			} else {
				JSONObject params = new JSONObject();
				params.put("respStatus", flag);
				result = params.toJSONString();
				System.out.println("httpGet => ***---------------接口机连接失败------------***" + "{" + flag + "}");
			}
		} catch (ClientProtocolException e) {
//			LOGGER.error("httpGet => ***---------------接口机连接异常------------ClientProtocolException", e);
		} catch (IOException e) {
//			LOGGER.error("httpGet => ***---------------接口机连接异常------------IOException", e);
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
//					LOGGER.error("httpGet => 关闭失败", e);
				}
			}
		}
		System.out.println("httpGet => ***---------------接口机返回值------------------------***" + result);
		return result;
	}

	/**
	 * delete请求
	 * 
	 * @param url
	 * @return
	 */
	public static String httpDelete(String url) {
		CloseableHttpClient httpClient = null;
		String result = null;
		try {
			System.out.println("httpDelete => ***---------------发送接口机url:------------------------------------" + url);
			System.out.println("httpDelete => ***---------------json Content-Type:---------------" + APPLICATION_JSON);
			// 设置超时时间
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(40000)
					.setConnectionRequestTimeout(40000).build();

			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();
			httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			HttpDelete httpDelete = new HttpDelete(url);
			// 设置hearder信息
//			httpDelete.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			httpDelete.setConfig(requestConfig);
			HttpResponse response;
			response = httpClient.execute(httpDelete);
			// 判定返回状态是否成功
			int flag = response.getStatusLine().getStatusCode();
			if (flag == 200) {
				System.out.println("httpDelete => ***---------------接口机连接成功------------***" +  "{" + flag + "}");
				result = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
			} else {
				JSONObject params = new JSONObject();
				params.put("respStatus", flag);
				result = params.toJSONString();
				System.out.println("httpDelete => ***---------------接口机连接失败------------***" + "{" + flag + "}");
			}
		} catch (ClientProtocolException e) {
//			LOGGER.error("httpDelete => ***---------------接口机连接异常------------ClientProtocolException", e);
		} catch (IOException e) {
//			LOGGER.error("httpDelete => ***---------------接口机连接异常------------IOException", e);
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
//					LOGGER.error("httpDelete => 关闭失败", e);
				}
			}
		}
//		LOGGER.info("httpDelete => ***---------------接口机返回值------------------------***" + result);
		return result;
	}

//	public static  void main(String[] args){
//		String str = "{\"reqData\":{\"platformNo\":\"yunkefu\",\"platformCode\":\"10086\",\"pwd\":\"wx_160768471\",\"rtcPlatform\":\"\",\"imAppkey\":\"\",\"companyType\":\"1\",\"interfaceServiceNumber\":\"10086666\",\"companyId\":\"wx_160768471\",\"companyName\":\"wx_160768471\",\"maxUserCount\":\"1000\"}}";
//		String str1 = str.substring(11,str.length()-1);
//		System.out.println("----截取后的字符串"+str1);
//	}
}
