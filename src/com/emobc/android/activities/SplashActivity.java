/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CoverActivity.java
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

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AbstractActivtyGenerator;
import com.emobc.android.parse.ParseUtils;
import com.emobc.android.profiling.Profile;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/** 
 * First screen in the main Application. 
 * onCreate() loads the splash content view. 
 * Loads the Cover Activity
 *
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class SplashActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 5000;
	private static ApplicationData instance = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle); 
		Log.i("SplashActivity", "OnCreate Splash");
		//overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		
		setContentView(R.layout.splash_screen);		
		
		
		try {
			instance = ApplicationData.readApplicationData(this);
			instance.setLevelStyleTypeMap(ParseUtils.parseStylesData(this, instance.getStylesFileName()));
			instance.setFormatStyleMap(ParseUtils.parseFormatData(this, instance.getFormatsFileName()));
			instance.setProfile(ParseUtils.parseProfileData(this, instance.getProfileFileName()));
		} catch (InvalidFileException e) {
			Log.e("SplashActivity", e.toString());
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = null;
				if(instance.getProfile() == null || Profile.isFilled(SplashActivity.this)){
					mainIntent = new Intent(SplashActivity.this, CoverActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					ImageView splashImage = (ImageView) SplashActivity.this.findViewById(R.id.splashImage);
					Drawable splashDrawable;
					try {
						splashDrawable = ImagesUtils.getDrawable(SplashActivity.this, "images/splash.png");
						splashImage.setImageDrawable(splashDrawable);
					} catch (InvalidFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					SplashActivity.this.finish();
				}else{
					AbstractActivtyGenerator.showNextLevel(SplashActivity.this, NextLevel.PROFILE_NEXT_LEVEL);
				}
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

    public static ApplicationData getApplicationData() {
        return instance;
    }
    
	
}