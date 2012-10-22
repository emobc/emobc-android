/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MapLevelDataItem.java
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

import java.util.ArrayList;
import java.util.List;

import com.emobc.android.levels.AppLevelDataItem;


/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class MapLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5549692558498988163L;
	private boolean localizeMe;
	private boolean showAllPositions;
	private List<MapDataItem> items = new ArrayList<MapDataItem>();
	private String currentPositionIconFileName;
	
	public boolean isLocalizeMe() {
		return localizeMe;
	}
	public void setLocalizeMe(boolean localizeMe) {
		this.localizeMe = localizeMe;
	}
	public boolean isShowAllPositions() {
		return showAllPositions;
	}
	public void setShowAllPositions(boolean showAllPositions) {
		this.showAllPositions = showAllPositions;
	}
	public List<MapDataItem> getItems() {
		return items;
	}
	public void setItems(List<MapDataItem> items) {
		this.items = items;
	}
	public String getCurrentPositionIconFileName() {
		return currentPositionIconFileName;
	}
	public void setCurrentPositionIconFileName(String currentPositionIconFileName) {
		this.currentPositionIconFileName = currentPositionIconFileName;
	}
	public void addItem(MapDataItem mapItem) {
		this.items.add(mapItem);		
	}
}
