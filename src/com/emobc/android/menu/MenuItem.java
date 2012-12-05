/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MenuItem.java
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
package com.emobc.android.menu;

import java.io.Serializable;

import com.emobc.android.NextLevel;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class MenuItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2773697802000273424L;

	private final String title;
	private final String imageFileName;
	private final NextLevel nextLevel;
	
	public MenuItem(String title, String imageFileName, NextLevel nextLevel) {
		super();
		if(nextLevel == null || !nextLevel.isDefined())
			throw new IllegalArgumentException("Invalid NextLevel: " + String.valueOf(nextLevel));
		
		this.title = title;
		this.imageFileName = imageFileName;
		this.nextLevel = nextLevel;
	}

	public String toString(){
		return "[menu = " + title + "]";
	}

	public String getTitle() {
		return title;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public NextLevel getNextLevel() {
		return nextLevel;
	}
}
