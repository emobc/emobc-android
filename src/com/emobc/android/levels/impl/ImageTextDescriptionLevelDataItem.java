/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageTextDescriptionLevelDataItem.java
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
import com.emobc.android.levels.AppDataItemText;
import com.emobc.android.levels.AppLevelDataItem;


/**
 * Item that contains data specific to a level of the activityType "IMAGE_TEXT_DESCRIPTION"
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageTextDescriptionLevelDataItem extends AppLevelDataItem implements AppDataItemText{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4465741016627387925L;
	private String text;
	private String imageFile;
	private String barText;
	
	public String getBarText() {
		return barText;
	}
	public void setBarText(String barText) {
		this.barText = barText;
	}
	private NextLevel nextLevel;
	
	public NextLevel getNextLevel() {
		return nextLevel;
	}
	public void setNextLevel(NextLevel nextLevel) {
		this.nextLevel = nextLevel;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
	@Override
	public String getItemText() {
		return getText();
	}
	
	@Override
	public List<SearchResult> getAllImages(final String levelId) {
		List<SearchResult> ret = super.getAllImages(levelId);
		NextLevel nextLevel = new NextLevel(levelId, getId());
		ret.add(new SimpleSearchResult(imageFile, imageFile, nextLevel));
		return ret;
	}
	@Override
	public List<SearchResult> findWidthText(String text, String levelId) {
		List<SearchResult> ret = super.findWidthText(text, levelId);
		if(text != null && text.toLowerCase().contains(this.text.toLowerCase())){
			NextLevel nextLevel = new NextLevel(levelId, getId());
			ret.add(new SimpleSearchResult(imageFile, this.text, nextLevel));
		}		
		return ret;
	}
}