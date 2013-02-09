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

import java.io.Serializable;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AbstractActivtyGenerator;
import com.emobc.android.profiling.Profile;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * First screen in the main Application. onCreate() loads the splash content
 * view. Loads the Cover Activity
 * 
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class SplashActivity extends EMobcActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3066139946109986911L;

	protected static final String TAG = "SplashActivity";

	private final int SPLASH_DISPLAY_LENGHT = 5000;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.i(TAG, "OnCreate Splash");

		setContentView(R.layout.splash_screen);

		ViewGroup splashVg = (ViewGroup) findViewById(R.id.splash_background);
		Drawable splashDrawable;
		try {
			splashDrawable = ImagesUtils.getDrawable(this, "images/splash.png");
			if (splashDrawable != null)
				splashVg.setBackgroundDrawable(splashDrawable);
		} catch (InvalidFileException e) {
			Log.e(TAG, e.getMessage());
		}

        EMobcApplication app = (EMobcApplication)getApplication();
		final ApplicationData applicationData = app.getApplicationData();
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = null;
				if (applicationData.getProfile() == null
						|| Profile.isFilled(SplashActivity.this)) {
					mainIntent = new Intent(SplashActivity.this,
							CoverActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				} else {
					AbstractActivtyGenerator.showNextLevel(SplashActivity.this,
							NextLevel.PROFILE_NEXT_LEVEL);
				}
			}

		}, SPLASH_DISPLAY_LENGHT);

	}
}