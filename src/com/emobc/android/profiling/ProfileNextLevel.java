/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * ProfileNextLevel.java
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

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;

/**
 * Application Level NextLevel of Profile Activity.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ProfileNextLevel extends NextLevel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * SharedSettings eMobc Profile Preferences Name
	 */
	public static final String PROFILE_PREFS_NAME = "EMOBC_PROFILE";

	/**
	 * Name of Settings that marks if the user has filled in the profile.
	 */
	public static final String PROFILE_FILLED = "EP_FILLED";

	public static final String PROFILE_NL_DATA_ID = "profile";
	
	private static NextLevel instance;

	private ProfileNextLevel(String levelId, String dataId) {
		super(levelId, dataId);
	}

	public static NextLevel getInstance(){
		if(instance == null){
			instance = new ProfileNextLevel(ApplicationData.EMOBC_LEVEL_ID, PROFILE_NL_DATA_ID);
		}
		return instance;
	}	
}
