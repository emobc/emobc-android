/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* Menu.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8054165167038397622L;
	
	private final String levelId;
	
	private final List<MenuItem> items = new ArrayList<MenuItem>();

	public Menu(String levelId) {
		super();
		this.levelId = levelId;
	}
	
	public void addMenuItem(MenuItem item){
		this.items.add(item);
	}
	
	public void addMenuItems(List<MenuItem> items){
		this.items.addAll(items);
	}
	
	public List<MenuItem> getItems(){
		return Collections.unmodifiableList(items);
	}

	public String getLevelId() {
		return levelId;
	}
	
	public boolean isGeneral(){
		return levelId == null || levelId.isEmpty();
	}
	
	public String toString(){
		return isGeneral() ? "Menu = general" : "Menu = " + levelId;
	}
}
