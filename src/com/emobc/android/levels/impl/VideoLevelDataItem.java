/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* VideoLevelDataItem.java
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
package com.emobc.android.levels.impl;

import com.emobc.android.levels.AppLevelDataItem;

/**
 * Item that contains data specific to a level of the activityType "VIDEO"
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class VideoLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3320267440402470040L;
	private String videoPath;

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
}
