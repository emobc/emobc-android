/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MenuActionDataItem.java
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

import com.emobc.android.NextLevel;

/**
 * Item that contains data specific to a particular menu 
 * (TopMenu, BottomMenu, ContextMenu, SideMenu)
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class MenuActionDataItem {
	
	private String title;
	private String imageName;
	private String systemAction;
	private NextLevel nextLevel;
	private int leftMargin;
	private int widthButton;
	private int heightButton;
	
	public MenuActionDataItem(){
		this.title = "";
		this.imageName = "";
		this.systemAction = "";
		this.nextLevel = null;
		this.leftMargin = 0;
		this.widthButton = 0;
		this.heightButton = 0;
	}
	
	public NextLevel getNextLevel() {
		return nextLevel;
	}
	public void setNextLevel(NextLevel nextLevel) {
		this.nextLevel = nextLevel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getSystemAction() {
		return systemAction;
	}
	public void setSystemAction(String systemAction) {
		this.systemAction = systemAction;
	}
	
	public int getLeftMargin() {
		return leftMargin;
	}
	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}
	public int getWidthButton() {
		return widthButton;
	}
	public void setWidthButton(int widthButton) {
		this.widthButton = widthButton;
	}
	public int getHeightButton() {
		return heightButton;
	}
	public void setHeightButton(int heightButton) {
		this.heightButton = heightButton;
	}
}
