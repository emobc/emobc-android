/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageGalleryLevelDataItem.java
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

import com.emobc.android.NextLevel;
import com.emobc.android.SearchResult;
import com.emobc.android.SimpleSearchResult;
import com.emobc.android.levels.AppLevelDataItem;


/**
 * Item that contains data specific to a level of the activityType "IMAGE_GALLERY"
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageGalleryLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5660240605896211302L;
	private List<ImageDataItem> list = new ArrayList<ImageDataItem>();
	
	public List<ImageDataItem> getList() {
		return list;
	}
	public void setList(List<ImageDataItem> list) {
		this.list = list;
	}
	
	@Override
	public List<SearchResult> getAllImages(final String levelId) {
		List<SearchResult> ret = super.getAllImages(levelId);
		NextLevel nextLevel = new NextLevel(levelId, getId());
		for(ImageDataItem item : list)
			ret.add(new SimpleSearchResult(item.toString(), item.getImageFile(), nextLevel));
		return ret;
	}
}
