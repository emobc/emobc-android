/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * RetreiveFileContentTask.java
 * eMobc Android Framework
 *
 * eMobc is free software: you can redistribute it and/or modify
 * it under the terms of the Affero GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * eMobc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public License
 * along with eMobc. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.emobc.android.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Ansync Task to download a file in backgorund.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class RetreiveFileContentTask extends AsyncTask<URL, Void, String> {
	private List<NameValuePair> parameters;
	private boolean usePostMethod;
	
	public RetreiveFileContentTask(List<NameValuePair> parameters, boolean usePostMethod){
		super();
		this.parameters = parameters;
		this.usePostMethod = usePostMethod;
	}
	
	@Override
	protected String doInBackground(URL... params) {
		URL url = params[0];
		HttpClient httpclient = HttpUtils.getHttpClient(url.getProtocol().equalsIgnoreCase(HttpUtils.HTTPS_PROTOCOL));
		
		HttpUriRequest httpUriRequest = createUriRequest(url);
		
	    try {
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httpUriRequest);
	      
	        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        	String str = EntityUtils.toString(response.getEntity());
	        	return str;
	        }else{
	        	Log.w("RetreiveFileContentTask: ", "HttpStatus Code: " + String.valueOf(response.getStatusLine().getStatusCode()));
	        }
	    } catch (ClientProtocolException e) {
	    	Log.e("RetreiveFileContentTask: ClientProtocolException: ", e.getMessage());
//	    	Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
	    } catch (IOException e) {
	    	Log.e("RetreiveFileContentTask: IOException: ", e.getMessage());
//	    	Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();		    	
	    }
	    return "";
	}

	private HttpUriRequest createUriRequest(URL url) {
		if(usePostMethod){
			return createPostUriRequest(url);
		}
		return createGetUriRequest(url);	
	}

	private HttpUriRequest createPostUriRequest(URL url) {
		HttpPost httppost = new HttpPost(url.toString());
        // Add your data
        try {
			httppost.setEntity(new UrlEncodedFormEntity(parameters));
			return httppost;
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	private HttpUriRequest createGetUriRequest(URL url) {
		HttpGet httpGet = new HttpGet(url.toString());
		return httpGet;
	}
}
