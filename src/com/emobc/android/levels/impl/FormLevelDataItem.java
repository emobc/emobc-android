/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* FormLevelDataItem.java
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
import com.emobc.android.levels.AppLevelDataItem;


/**
 * Stores the DataItems of a <tt>FORM_ACTIVITY</tt>.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class FormLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2729772219137566162L;
	private List<FormDataItem> list = new ArrayList<FormDataItem>();
	private String actionUrl;
	private NextLevel nextLevel;
	
	public List<FormDataItem> getList() {
		return list;
	}
	public void setList(List<FormDataItem> list) {
		this.list = list;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public NextLevel getNextLevel() {
		return nextLevel;
	}
	public void setNextLevel(NextLevel nextLevel) {
		this.nextLevel = nextLevel;
	}
}
