package services.websiteparser;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import domain.SimpleItemInfo;

/**
 * 
* <p>Title: ParseDealSite</p>
* <p>Description:Interface for parsing URL </p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public interface DealSiteParser {
	
	public SimpleItemInfo parseURL(String url) throws IOException;
	/**
	 * 
	* <p>Title: getContent</p>
	* <p>Description: Default method to get content for an Url </p>
	* @param url
	* @return
	* @throws IOException
	 */
	default public String getContent(String url) throws IOException{
		String response = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		
		try {
			HttpGet httpGet = new HttpGet(url);
			response = httpClient.execute(httpGet,
					new ResponseHandler<String>() {

						public String handleResponse(HttpResponse response)
								throws ClientProtocolException, IOException {
							
							int status = response.getStatusLine()
									.getStatusCode();
							if (status >= 200 && status < 300)
								return EntityUtils.toString(response
										.getEntity());
							else
								throw new ClientProtocolException(
										"Unexpected Response Status : "
												+ status);
						}
					});
		
		} finally {
			httpClient.close();
		}
	
		return response;
	};

}
