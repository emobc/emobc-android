/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageLevelDataItem.java
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

import java.util.List;

import com.emobc.android.NextLevel;
import com.emobc.android.SearchResult;
import com.emobc.android.SimpleSearchResult;
import com.emobc.android.levels.AppLevelDataItem;


/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -845851879439529555L;
	private String imageFile;

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
	@Override
	public List<SearchResult> getAllImages(final String levelId) {
		List<SearchResult> ret = super.getAllImages(levelId);
		NextLevel nextLevel = new NextLevel(levelId, getId());
		ret.add(new SimpleSearchResult(imageFile, imageFile, nextLevel));
		return ret;
	}
}