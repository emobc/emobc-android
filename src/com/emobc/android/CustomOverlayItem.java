/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CustomOverlayItem.java
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
package com.emobc.android;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class CustomOverlayItem extends OverlayItem {
//	private MapDataItem mDataItem;
	private NextLevel nextLevel;

	public CustomOverlayItem(GeoPoint arg0, String arg1, String arg2) {
		super(arg0, arg1, arg2);
	}
	
	public void setNextLevel (NextLevel nextLevel){
		this.nextLevel = nextLevel; 
		
	}

	public NextLevel getNextLevel() {
		return nextLevel;
	}

}
