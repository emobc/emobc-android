/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * package-info.java
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
package com.emobc.android.themes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emobc.android.ActivityType;


/**
 * Component that save the background name, and , for a activiyty type, or level.
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class LevelTypeStyle {
	private ActivityType levelType;
	private String background;
	private Map<String, String> mapFormatComponents = new HashMap<String, String>();
	private List<String> listComponents = new ArrayList<String>();
	private String selectionList;
	
	public ActivityType getLevelType() {
		return levelType;
	}

	public void setLevelType(ActivityType levelType) {
		this.levelType = levelType;
	}
	
	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setComponents(String components) {
		
		String[] separatedComponents = components.split(";");
		String[] assignment;
		for(int i = 0; i < separatedComponents.length; i++){
			assignment = separatedComponents[i].split("=");
			String component = assignment[0];
			String format = assignment[1];
			mapFormatComponents.put(component, format);
			if(!component.equals("selection_list")){
				listComponents.add(component);
			}else{
				this.selectionList = format;
			}
		}
	}

	public Map<String, String> getMapFormatComponents() {
		return mapFormatComponents;
	}

	public List<String> getListComponents() {
		return listComponents;
	}

	public String getSelectionList() {
		return selectionList;
	}

	public boolean isCleanFormat(){
		if(background != null && !background.isEmpty())
			return false;
		if(selectionList != null && !selectionList.isEmpty())
			return false;	
		return true;
	}
	
}
