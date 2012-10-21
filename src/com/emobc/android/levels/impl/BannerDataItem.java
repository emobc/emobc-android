/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* BannerDataItem.java
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

/**
 * Class for save information about the kind of banner inside the application
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class BannerDataItem {
	//Constants
	//Type Constants
	public static final String ADMOB = "admob";
	
	//Position Constant
	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	private static final String TOP_S = "top";
	private static final String BOTTOM_S = "bottom";
	
	//Attributes
	private String type;
	private int position;
	private String id;
	
	/**
	 * Return the type of the banner
	 * @return 
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set the banner type.
	 * @param type 
	 */
	public void setType(String type) {
		//Default
		this.type = ADMOB;
		//for valid values. Now just ADMOB
		if (type.equals(ADMOB)){
			this.type=type;
		}
	}
	
	/**
	 * Return the position of the banner
	 * @return Can be constants TOP or BOTTOM
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Set the banner position
	 * @param position String with the position. Valid values: "bottom" or "top". <b>
	 * If the string is invalid, position takes "top" value by default.
	 * 
	 */
	public void setPosition(String position) {
		this.position = TOP;
		if (position.equals(TOP_S)){
			this.position = TOP;
		}else if(position.equals(BOTTOM_S)){
			this.position = BOTTOM;
		}
	}
	
	/**
	 * Return the banner id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set the banner id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
