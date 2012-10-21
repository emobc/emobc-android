/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* WebActivityGenerator.java
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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.WebLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.Utils;
/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "WEB". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class WebActivityGenerator extends LevelActivityGenerator {

	private WebView webview;

	public WebActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final WebLevelDataItem item = (WebLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		if(Utils.hasLength(item.getWebUrl())){
			try {
				if (item.isLocal()){
					String fileName = item.getWebUrl();
					webview = (WebView)activity.findViewById(R.id.web_viewer);
					webview.loadUrl("file:///android_asset/html/"+fileName);
				}else{
					URL url = new URL(item.getWebUrl());
					URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
					url = uri.toURL();
					
					webview = (WebView)activity.findViewById(R.id.web_viewer);
					webview.loadUrl(url.toString());
				}
			} catch (MalformedURLException e) {
				Log.e("WebActivityGenerator", "MalformedURLException: " + e.getMessage());
			} catch (URISyntaxException e) {
				Log.e("WebActivityGenerator", "URISyntaxException: " + e.getMessage());
			}
		} 		
	}

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.web_activity;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.WEB_ACTIVITY;
	}

	public WebView getWebview() {
		return this.webview;
	}
}
