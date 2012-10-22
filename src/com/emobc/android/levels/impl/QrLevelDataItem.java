/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QrLevelDataItem.java
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

import java.util.HashMap;
import java.util.Map;

import com.emobc.android.NextLevel;
import com.emobc.android.levels.AppLevelDataItem;

/**
 * Item that contains data specific to a level of the activityType "QR"
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QrLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2438456538947693894L;
	private Map<String, NextLevel> idMap = new HashMap<String, NextLevel>();
	
	public void addCodeNextLevel(String qrCode, NextLevel nextLevel){
		idMap.put(qrCode, nextLevel);
	}
	
	public NextLevel getNextLevel(String qrCode){
		return idMap.get(qrCode);
	}
}
