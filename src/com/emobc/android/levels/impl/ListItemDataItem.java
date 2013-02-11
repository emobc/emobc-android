/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ListItemDataItem.java
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

import com.emobc.android.NextLevel;

/**
 * Item for an list
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ListItemDataItem {
	private final NextLevel nextLevel;
	private final String imageFile;
	private final String text;
	private final String description;

	public ListItemDataItem(NextLevel nextLevel, String imageFile, String text,
			String description) {
		super();
		this.nextLevel = nextLevel;
		this.imageFile = imageFile;
		this.text = text;
		this.description = description;
	}
	
	public String getText() {
		return text;
	}

	public NextLevel getNextLevel() {
		return nextLevel;
	}

	public String getImageFile() {
		return imageFile;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "ListItemDataItem [text = " + text + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((imageFile == null) ? 0 : imageFile.hashCode());
		result = prime * result
				+ ((nextLevel == null) ? 0 : nextLevel.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListItemDataItem other = (ListItemDataItem) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (imageFile == null) {
			if (other.imageFile != null)
				return false;
		} else if (!imageFile.equals(other.imageFile))
			return false;
		if (nextLevel == null) {
			if (other.nextLevel != null)
				return false;
		} else if (!nextLevel.equals(other.nextLevel))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}