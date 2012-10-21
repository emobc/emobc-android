/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* DefaultAppLevelData.java
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.SearchResult;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.AppLevelDataItem;


/**
 * Default Application Level Data.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class DefaultAppLevelData implements AppLevelData {
	private List<AppLevelDataItem> items = new ArrayList<AppLevelDataItem>();
	private Map<String, AppLevelDataItem> itemMap = new HashMap<String, AppLevelDataItem>();
	
	@Override
	public List<AppLevelDataItem> getItems() {
		return items;
	}

	@Override
	public AppLevelDataItem findById(String id) {
		if(id == null)
			return null;
		return itemMap.get(id);
	}

	@Override
	public void addItem(AppLevelDataItem item) {
		final String id = item.getId();
		if(item.getId() != null){
			itemMap.put(id, item);
		}
		items.add(item);
	}

	@Override
	public AppLevelDataItem findByNextLevel(NextLevel nextLevel) {
		String dataId = nextLevel.getDataId();
		if(dataId != null){
			return itemMap.get(dataId);
		}
		if(nextLevel.getDataNumber() > NextLevel.NO_LEVEL)
			return items.get(nextLevel.getDataNumber());
		return null;
	}

	@Override
	public void reIndex() {
		itemMap.clear();
		for(AppLevelDataItem item : items){
			final String id = item.getId();
			if(item.getId() != null){
				itemMap.put(id, item);
			}
		}
	}

	@Override
	public List<SearchResult> getAllImages(final String levelId) {
		List<SearchResult> ret = new ArrayList<SearchResult>();
		for(AppLevelDataItem item : items){
			ret.addAll(item.getAllImages(levelId));
		}
		return ret;
	}

	@Override
	public List<SearchResult> findWidthText(String text, final String levelId) {
		List<SearchResult> ret = new ArrayList<SearchResult>();
		for(AppLevelDataItem item : items){
			if(ret.size() < ApplicationData.SEARCH_LIMIT)
				ret.addAll(item.findWidthText(text, levelId));
		}
		return ret;
	}

	@Override
	public List<SearchResult> findAllGeoref(String levelId) {
		List<SearchResult> ret = new ArrayList<SearchResult>();
		for(AppLevelDataItem item : items){
			ret.addAll(item.findAllGeoref(levelId));
		}
		return ret;
	}
}
