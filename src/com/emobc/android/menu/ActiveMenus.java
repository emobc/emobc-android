/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ActiveMenus.java
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

/**
 * Class that stores the menus that are activated from the application
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class ActiveMenus {
	private String topMenu;
	private String bottomMenu;
	private String contextMenu;
	private String sideMenu;
	
	public ActiveMenus(){
		this.topMenu = null;
		this.bottomMenu = null;
		this.contextMenu = null;
		this.sideMenu = null;
	}
	
	public String getTopMenu() {
		return topMenu;
	}
	public void setTopMenu(String topMenu) {
		this.topMenu = topMenu;
	}
	public String getBottomMenu() {
		return bottomMenu;
	}
	public void setBottomMenu(String bottomMenu) {
		this.bottomMenu = bottomMenu;
	}
	public String getContextMenu() {
		return contextMenu;
	}
	public void setContextMenu(String contextMenu) {
		this.contextMenu = contextMenu;
	}
	public String getSideMenu() {
		return sideMenu;
	}
	public void setSideMenu(String sideMenu) {
		this.sideMenu = sideMenu;
	}
	
}
