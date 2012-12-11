/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* StyleResult.java
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
package com.emobc.android.themes.parse;

import java.util.Map;

import com.emobc.android.ActivityType;
import com.emobc.android.themes.ActivityTypeStyle;
import com.emobc.android.themes.LevelStyle;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public final class StyleResult {
	private final Map<ActivityType, ActivityTypeStyle> activityTypeStyleMap;
	private final Map<String, LevelStyle> levelStyleMap;

	public StyleResult(Map<ActivityType, ActivityTypeStyle> activityTypeStyleMap,
			Map<String, LevelStyle> levelStyleMap) {
		super();
		this.activityTypeStyleMap = activityTypeStyleMap;
		this.levelStyleMap = levelStyleMap;
	}
	
	public Map<ActivityType, ActivityTypeStyle> getActivityTypeStyleMap() {
		return activityTypeStyleMap;
	}
	
	public Map<String, LevelStyle> getLevelStyleMap() {
		return levelStyleMap;
	}
}
