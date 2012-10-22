/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ButtonsLevelDataItem.java
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

import com.emobc.android.AppButton;
import com.emobc.android.levels.AppLevelDataItem;


/**
 * Item that contains data specific to a level of the activityType "BUTTONS"
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ButtonsLevelDataItem extends AppLevelDataItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6628336619871975019L;

	public static final int DEFAULT_COLUMNS_NUMBER = 2;
	
	private List<AppButton> buttonList = new ArrayList<AppButton>();
	private int columns = DEFAULT_COLUMNS_NUMBER;
	
	public List<AppButton> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<AppButton> buttonList) {
		this.buttonList = buttonList;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}	
}
