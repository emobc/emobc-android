/**
* Copyright 2013 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ArLevelDataItem.java
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
import java.util.Collection;
import java.util.List;

import com.emobc.android.levels.AppLevelDataItem;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ArLevelDataItem extends AppLevelDataItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5549711692101967087L;
	
	private final String itemDescription;
	private List<Target> targetList = new ArrayList<Target>();
	
	public ArLevelDataItem(String id, String headerImageFile,
			String headerText, String geoReferencia, 
			String itemDescription) {
		super(id, headerImageFile, headerText, geoReferencia);
		this.itemDescription = itemDescription;
	}

	public String getItemDescription() {
		return itemDescription;
	}
	
	public void addTarget(Target target){
		if(!targetList.contains(target))
			targetList.add(target);
	}
	
	public void addAllTarget(Collection<Target> allTargets){
		targetList.addAll(allTargets);
	}
	
	public String toString(){
		return "[AR " + itemDescription + "]";
	}
	
}
