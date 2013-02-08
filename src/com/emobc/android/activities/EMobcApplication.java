/**
* Copyright 2013 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* EMobcApplication.java
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

import com.emobc.android.ApplicationData;
import com.emobc.android.parse.ParseUtils;
import com.emobc.android.utils.InvalidFileException;

import android.app.Application;
import android.util.Log;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class EMobcApplication extends Application {
	
	private static final String TAG = "EMobcApplication";
	
	private ApplicationData instance = null;

	@Override
	public void onCreate() {
		super.onCreate();

		try {
			instance = ApplicationData.readApplicationData(this);
			instance.setProfile(ParseUtils.parseProfileData(this, instance.getProfileFileName()));
			instance.initStylesAndFormats(this);
		} catch (InvalidFileException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public ApplicationData getApplicationData() {
		return instance;
	}
}
