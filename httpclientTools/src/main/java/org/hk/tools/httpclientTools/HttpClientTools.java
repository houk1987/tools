package org.hk.tools.httpclientTools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class HttpClientTools {

	// httpClient
	private CloseableHttpClient client;
	
	private RequestConfig requestConfig;
	
	
	{
		
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
		// Increase max total connection to 200  
		cm.setMaxTotal(200);  
		// Increase default max connection per route to 20  
		cm.setDefaultMaxPerRoute(200);  
		client = HttpClientBuilder.create().setConnectionManager(cm).build();
		requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000)
                .setConnectTimeout(6000000).setRedirectsEnabled(false)
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
			if (statuscode != HttpStatus.SC_OK) {
				request.abort();
				return null;
			}
			HttpEntity entity = cResponse.getEntity();
			Thread.sleep(1000);
			return readHtmlContentFromEntity(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
	
	 /**
     * 从response返回的实体中读取页面代码
     *
     * @param httpEntity Http实体
     * @return 页面代码
     * @throws ParseException
     * @throws IOException
     */

    private String readHtmlContentFromEntity(final HttpEntity httpEntity) throws ParseException, IOException {

        String html = "";

        Header header = httpEntity.getContentEncoding();

        if (httpEntity.getContentLength() < 2147483647L) {             //EntityUtils无法处理ContentLength超过2147483647L的Entity
            if (header != null && "gzip".equals(header.getValue())) {
                html = EntityUtils.toString(new GzipDecompressingEntity(httpEntity),"UTF-8");
            } else {
                html = EntityUtils.toString(httpEntity,"UTF-8");
                EntityUtils.consume(httpEntity); 
            }

        } else {
            InputStream in = httpEntity.getContent();
            if (header != null && "gzip".equals(header.getValue())) {
                html = unZip(in, ContentType.getOrDefault(httpEntity).getCharset().toString());
            } else {
                html = readInStreamToString(in, ContentType.getOrDefault(httpEntity).getCharset().toString());
            }
            if (in != null) {
                in.close();
            }
        }
        return html;

    }

    /**
     * 解压服务器返回的gzip流
     *
     * @param in      抓取返回的InputStream流
     * @param charSet 页面内容编码
     * @return 页面内容的String格式
     * @throws IOException
     */

    private String unZip(InputStream in, String charSet) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GZIPInputStream gis = null;

        try {

            gis = new GZIPInputStream(in);

            byte[] _byte = new byte[1024];

            int len = 0;

            while ((len = gis.read(_byte)) != -1) {

                baos.write(_byte, 0, len);

            }

            String unzipString = new String(baos.toByteArray(), charSet);

            return unzipString;

        } finally {

            if (gis != null) {

                gis.close();

            }

            if (baos != null) {

                baos.close();

            }

        }

    }


    /**
     * 读取InputStream流
     *
     * @param in InputStream流
     * @return 从流中读取的String
     * @throws IOException
     */

    private String readInStreamToString(InputStream in, String charSet) throws IOException {

        StringBuilder str = new StringBuilder();

        String line;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charSet));

        while ((line = bufferedReader.readLine()) != null) {

            str.append(line);

            str.append("\n");

        }

        if (bufferedReader != null) {

            bufferedReader.close();

        }

        return str.toString();
    }
}
