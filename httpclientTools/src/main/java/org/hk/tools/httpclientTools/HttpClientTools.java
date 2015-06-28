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
	
	private RequestConfig requestConfig;
	
	private int time;
	
	{
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
		// Increase max total connection to 200  
		cm.setMaxTotal(200);  
		// Increase default max connection per route to 20  
		cm.setDefaultMaxPerRoute(20);  
		client = HttpClientBuilder.create().setConnectionManager(cm).build();
		time = 3000;
		requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(time).setConnectTimeout(time)
				.build();
		
	}

	public String executeGet(String url) {
		CloseableHttpResponse cResponse = null;
		HttpGet request = null;
		try {
			request = new HttpGet(url);
			request.setConfig(this.requestConfig);
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
				e.printStackTrace();
			}
		}
		return "";
	}
}
