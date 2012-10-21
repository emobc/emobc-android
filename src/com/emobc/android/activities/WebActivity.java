/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AudioActivity.java
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
package com.emobc.android.activities;

import java.lang.reflect.InvocationTargetException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AudioActivityGenerator;
import com.emobc.android.activities.generators.WebActivityGenerator;
import com.emobc.android.menu.CreateMenus;

/**
 * Class that defines an activity of type <tt>WEB_ACTIVITY</tt>, and 
 * initialize all screen menu and the screen rotations.
 * @author Jorge E. Villaverde
 * @see AudioActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class WebActivity extends CreateMenus {
	private WebActivityGenerator generator;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
        boolean isEntryPoint = false;
        rotateScreen(this);
        
        ApplicationData applicationData = SplashActivity.getApplicationData();
		if(applicationData != null){
			Intent intent = getIntent();  
			isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			generator = (WebActivityGenerator) applicationData.getFromNextLevel(this, nextLevel);
			generator.initializeActivity(this);
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}
		createMenus(this, isEntryPoint);
    }
        
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	if(generator == null || generator.getWebview() == null) 
    		return;
    	Log.i("WebView","onDestroy()");
    	try {
			Class.forName("android.webkit.WebView").getMethod("onDestroy", (Class[]) null).invoke(generator.getWebview(), (Object[]) null);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (ClassNotFoundException e) {
		}
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	if(generator == null || generator.getWebview() == null) 
    		return;
    	Log.i("WebView","onStop()");
    	try {
			Class.forName("android.webkit.WebView").getMethod("onStop", (Class[]) null).invoke(generator.getWebview(), (Object[]) null);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (ClassNotFoundException e) {
		}
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	if(generator == null || generator.getWebview() == null) 
    		return;
    	
    	Log.i("WebView","onPause()");
    	try {
			Class.forName("android.webkit.WebView").getMethod("onPause", (Class[]) null).invoke(generator.getWebview(), (Object[]) null);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (ClassNotFoundException e) {
		}
    }

	@Override
	protected void onResume() {
		super.onResume();
    	if(generator == null || generator.getWebview() == null) 
    		return;
    	Log.i("WebView","onResume()");
    	try {
			Class.forName("android.webkit.WebView").getMethod("onResume", (Class[]) null).invoke(generator.getWebview(), (Object[]) null);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (ClassNotFoundException e) {
		}
	}
}