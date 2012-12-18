/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* PdfActivityGenerator.java
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
package com.emobc.android.activities.generators;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.PdfLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.Utils;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "PDF". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class PdfActivityGenerator extends LevelActivityGenerator {
	//private static final String PDF_WEBVIEW_TPL = "http://docs.google.com/gview?embedded=true&url=%s";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1787100866474268843L;

	/**
	 *  Class that prevents opening the Browser 
	 */
    private class InsideWebViewClient extends WebViewClient {
    	Context activity;
    	ProgressDialog dialog;
    	boolean loading;
    	public InsideWebViewClient(Context activity){
    		super();
    		this.activity=activity;
    		loading=false;
    	}
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("docs.google.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon){
    		super.onPageStarted(view, url, favicon);
    		if (!loading){
	    		dialog = new ProgressDialog(activity);
	    		dialog.setMessage(activity.getResources().getString(R.string.cargando_pdf));
	    		dialog.show();
	    		loading = true;
    		}
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE  
        }
    	@Override
        public void onPageFinished(WebView view, String url) {
	        if (loading){
	        	dialog.dismiss();
		        dialog = null;
	        }	

        }

 
    }

	public PdfActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final PdfLevelDataItem item = (PdfLevelDataItem)data.findByNextLevel(nextLevel);
		if(item == null)
			return;
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		if(Utils.hasLength(item.getPdfUrl())){
			WebView webview = (WebView) activity.findViewById(R.id.pdfviewer);
	        webview.getSettings().setJavaScriptEnabled(true);
	        webview.getSettings().setPluginsEnabled(true);
	        webview.setWebViewClient(new InsideWebViewClient(activity));
	        
	        webview.loadUrl("http://docs.google.com/gview?embedded=true&url="+item.getPdfUrl());

			
			//activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPdfUrl())));
			
			
			/*
			File outputDir = activity.getCacheDir(); 
			try {
				File outputFile = File.createTempFile("eMobc", ".tmp", outputDir);
				if(downloadPdfToFile(outputFile, item.getPdfUrl())){
					File file = new File(outputFile.getAbsolutePath());

					if (file.exists()) {		 					
	                    Uri path = Uri.fromFile(outputFile);
	                    Intent intent = new Intent(Intent.ACTION_VIEW);
	                    intent.setDataAndType(path, "application/pdf");
	                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	
	                    try {
	                    	activity.startActivity(intent);
	                    }catch (ActivityNotFoundException e) {
	                        Toast.makeText(activity, 
	                            "No Application Available to View PDF", 
	                            Toast.LENGTH_SHORT).show();
	                    }
					}
				}				
			} catch (IOException e1) {
				Log.e("PdfActivityGenerator", "Error: " + e1.getMessage());
			}*/			
			/*
			
			String pdfFileUrl = String.format(PDF_WEBVIEW_TPL, item.getPdfUrl());
			
			try {
				URL url = new URL(pdfFileUrl);
				URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
				url = uri.toURL();

				
				WebView webView = (WebView)activity.findViewById(R.id.pdfviewer);
				webView.getSettings().setJavaScriptEnabled(true); 
				webView.loadUrl(url.toString());
				
				Uri uriPath = Uri.parse(item.getPdfUrl());
				
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uriPath, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                	activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, 
                        "No Application Available to View PDF", 
                        Toast.LENGTH_SHORT).show();
                }
			} catch (MalformedURLException e) {
				Log.e("PdfActivityGenerator", "MalformedURLException: " + e.getMessage());
			} catch (URISyntaxException e) {
				Log.e("PdfActivityGenerator", "URISyntaxException: " + e.getMessage());
			}
			*/
		} 		
	}

	/**
	 * Download a pdf file to web.
	 * @param file
	 * @param urlString
	 * @return
	 */
	protected boolean downloadPdfToFile(File file, String urlString){
        try {
            URL url = new URL(urlString); 

            long startTime = System.currentTimeMillis();
            Log.d("ImageManager", "download begining");
            Log.d("ImageManager", "download url:" + url);
            Log.d("ImageManager", "downloaded file name:" + file.getAbsolutePath());
            
            // Open a connection to that URL.
            URLConnection ucon = url.openConnection();

            // Define InputStreams to read from the URLConnection.
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

             // Read bytes to the Buffer until there is nothing more to read(-1).
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
            	baf.append((byte) current);
            }

            // Convert the Bytes read to a String.
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("ImageManager", "download ready in"
                            + ((System.currentTimeMillis() - startTime) / 1000)
                            + " sec");
            
            return true;
        } catch (IOException e) {
        	Log.d("ImageManager", "Error: " + e);
        }
		return false;
	}
	
	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.pdf_activity;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.PDF_ACTIVITY;
	}
}
