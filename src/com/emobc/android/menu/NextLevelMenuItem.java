/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* NextLevelMenuItem.java
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

import android.app.Activity;

import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AbstractActivtyGenerator;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class NextLevelMenuItem extends MenuItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2307492400247060866L;
	private final NextLevel nextLevel;

	/**
	 * @param title
	 * @param imageFileName
	 * @param nextLevel
	 * @param systemAction
	 */
	public NextLevelMenuItem(String title, String imageFileName, NextLevel nextLevel) {
		super(title, imageFileName);
		
		if(nextLevel == null || !nextLevel.isDefined()) 
			throw new IllegalArgumentException("Invalid NextLevel: " + String.valueOf(nextLevel));
		
		this.nextLevel = nextLevel;
	}
	
	public NextLevel getNextLevel() {
		return nextLevel;
	}

	@Override
	public void executeMenuItem(Activity context) {
		AbstractActivtyGenerator.showNextLevel(context, nextLevel);		
	}

	@Override
	public boolean isEnable(Activity context) {
		if (context instanceof CreateMenus) {
			CreateMenus cm = (CreateMenus) context;
			NextLevel nextLevel = cm.getCurrentNextLevel();
			if(nextLevel == null)
				return true;
			return !this.nextLevel.equals(nextLevel);			
		}
		return true;
	}
}
