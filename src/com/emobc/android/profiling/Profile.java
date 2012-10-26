/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * Profile.java
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
package com.emobc.android.profiling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.emobc.android.levels.AppLevelDataItem;
import com.emobc.android.levels.impl.FormDataItem;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Profile Data Item
 * @author Jorge E. Villaverde
 * @see AppLevelDataItem
 * @version 0.1
 * @since 0.1
 */
public class Profile extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7780560160626829149L;
	private static final String PROFILE_PREFS_NAME = "_EMOBC_PROFILE_";
	private static final String PROFILE_FILLED = "_EMOBC_PROFILE_FILLED_";

	private List<FormDataItem> fields = new ArrayList<FormDataItem>();
	
	public List<FormDataItem> getFields() {
		return fields;
	}
	public void setFields(List<FormDataItem> fields) {
		this.fields = fields;
	}
	
	public static boolean isFilled(Context context) {
		final SharedPreferences settings = getProfileData(context);
		return  settings.getBoolean(Profile.PROFILE_FILLED, false);
	}
	public static SharedPreferences getProfileData(Context context) {
		return context.getSharedPreferences(Profile.PROFILE_PREFS_NAME, 0);
	}

	public static void setFilled(Context context, boolean filled) {
		final SharedPreferences settings = getProfileData(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(Profile.PROFILE_FILLED, true);
		editor.commit();
	}
	public static List<NameValuePair> createNamedParameters(Context context) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		SharedPreferences settings = getProfileData(context);
		Map<String, ?> map = settings.getAll();
		for(String key : map.keySet()){
			final String value = map.get(key).toString();			
			if(value != null && value.length() > 0){
		        nameValuePairs.add(new BasicNameValuePair(key, value));
			}			
		}
		return nameValuePairs;
	}
}
