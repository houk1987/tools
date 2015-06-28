package org.hk.tools.httpclientTools;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientTools {

	// httpClient
	private CloseableHttpClient client;

	private HttpClientTools init() {
		client = HttpClientBuilder.create().build();
		initHttpClient();
		return this;
	}

	private HttpClient initHttpClient() {
		return client;
	}

	private String executeGet(String url) {

		CloseableHttpResponse cResponse;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			httpGet.setConfig(buildRequestConfig()); 
			cResponse = client.execute(httpGet);
			int statuscode = cResponse.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				HttpEntity entity = cResponse.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				if(httpGet!=null){
					httpGet.releaseConnection();
				}
		}

		return "";
	}

	private RequestConfig buildRequestConfig() {
		int time = 200;
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(time).setConnectTimeout(time)
				.setSocketTimeout(time).build();
		return requestConfig;
	}

	public static void main(String[] args) {
		HttpClientTools clientTools = new HttpClientTools();
		clientTools.init();
		String html = clientTools
				.executeGet("http://bbs.tianya.cn/list-333-1.shtml");
		System.out.print(html);
	}

}
