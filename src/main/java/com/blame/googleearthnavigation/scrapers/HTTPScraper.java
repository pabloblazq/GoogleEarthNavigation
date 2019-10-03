package com.blame.googleearthnavigation.scrapers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import groovy.json.JsonSlurper;

public abstract class HTTPScraper extends Thread {

	protected Logger logger;

	protected HttpClient httpClient;
	//protected List<String> cookies;

	protected String url;
	protected String method;
	protected String parameters;
	
	public HTTPScraper() {
		super();
		
		this.logger = LogManager.getLogger(this.getClass());
		
		// httpClient = HttpClientBuilder.create().build();
		// The above commented code gives warnings for: "Invalid cookie header: ... Invalid 'expires' attribute ..."
		// Using the below one solves these warnings
		httpClient = HttpClients.custom().setDefaultRequestConfig(
				RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	protected Object getJSONObject() throws IOException {
		String response = getHTTPResponseAsString();
		JsonSlurper js = new JsonSlurper();
		return js.parseText(response);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	protected synchronized String getHTTPResponseAsString() throws IOException {
		String responseBody = null;

		if(method == null || method.equals("GET")) {
			// connect and get the response as a string
			responseBody = httpGet(this.url);
		}
		else {
			// connect and get the response as a string
		    Map<String, String> paramsMap = new HashMap<String, String>();
			for(String keyValue : parameters.split("&")) {
				String[] keyValueSplitted = keyValue.split("=");
				paramsMap.put(keyValueSplitted[0], keyValueSplitted[1]);
			}

			responseBody = httpPost(this.url, paramsMap);
		}

		return responseBody;
	}

	/**
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	private String httpGet(String urlString) throws IOException {
		
		//HttpGet httpGet = new HttpGet(URLEncoder.encode(urlString, "UTF-8"));
		HttpGet httpGet = new HttpGet(urlString);
		addUserAgentHeader(httpGet);
		//addCookies(httpGet);
		HttpResponse response = httpClient.execute(httpGet);
		//keepCookies(response);
		
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}
	
	/**
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	private String httpPost(String urlString, Map<String, String> paramsStringMap) throws IOException {
//	    HttpPost httpPost = new HttpPost(URLEncoder.encode(urlString,"UTF-8"));
	    HttpPost httpPost = new HttpPost(urlString);
	 
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    for(Entry<String, String> entry : paramsStringMap.entrySet()) {
		    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	    }
	    httpPost.setEntity(new UrlEncodedFormEntity(params));

		addUserAgentHeader(httpPost);
	    
		//addCookies(httpPost);
	    HttpResponse response = httpClient.execute(httpPost);
		//keepCookies(response);
		
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}

	/**
	 * Adds the User-Agent header to simulate the request comes from a browser
	 * @param httpRequest
	 */
	private static void addUserAgentHeader(HttpRequestBase httpRequest) {
		httpRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
	}
}
