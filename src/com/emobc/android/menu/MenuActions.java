/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MenuActions.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains a list of actions (buttons) unique for 
 * a particular menu.
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class MenuActions {
	private List<MenuActionDataItem> list;
	
	public MenuActions(){
		list = new ArrayList<MenuActionDataItem>();
	}

	public List<MenuActionDataItem> getList() {
		return list;
	}

	public void setList(List<MenuActionDataItem> list) {
		this.list = list;
	}

}
