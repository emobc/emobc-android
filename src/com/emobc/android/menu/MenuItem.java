/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MenuItem.java
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

import android.app.Activity;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public abstract class MenuItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2773697802000273424L;

	private final String title;
	private final String imageFileName;

	
	public MenuItem(String title, String imageFileName) {
		super();
		this.title = title;
		this.imageFileName = imageFileName;
	}

	public String toString(){
		return "[menu = " + title + "]";
	}

	public String getTitle() {
		return title;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public abstract void executeMenuItem(Activity context);
	
	public abstract boolean isEnable(Activity context);
}
