package org.hk.tools.httpclientTools;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpClientTools {

	// httpClient
	private CloseableHttpClient client;

	{
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
		// Increase max total connection to 200  
		cm.setMaxTotal(200);  
		// Increase default max connection per route to 20  
		cm.setDefaultMaxPerRoute(20);  
		client = HttpClientBuilder.create().setConnectionManager(cm).build();
		
	}

	public String executeGet(String url) {
		
		CloseableHttpResponse cResponse = null;
		HttpGet request = null;
		try {
			request = new HttpGet(url);
			request.setConfig(buildRequestConfig());
			//request.addHeader(HttpHeaders.ACCEPT, "application/xml"); 
			cResponse = client.execute(request);
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
			if (request != null) {
				request.releaseConnection();
			}
			
			try {
				if(cResponse !=null)
				cResponse.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";
	}

	private RequestConfig buildRequestConfig() {
		int time = 3000;
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(time).setConnectTimeout(time)
				.setSocketTimeout(time).build();
		return requestConfig;
	}
//
//	public static void main(String[] args) {
//		HttpClientTools clientTools = new HttpClientTools();
//		clientTools.init();
//		String html = clientTools
//				.executeGet("http://bbs.tianya.cn/post-333-688409-1.shtml");
//		System.out.print(html);
//	}

}
