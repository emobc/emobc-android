/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* EntryPoint.java
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
package com.emobc.android;

/**
 * Class responsible for saving the data "entryPoint" in app.xml file
 * It consists of a <data> levelId </ data> and <data> Dataid </ data> unique.
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @sine 1.0
 */
public class EntryPoint {
	private String levelId;
	private String dataId;
	
	public EntryPoint(){
		levelId=null;
		dataId=null;
	}
	
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
}
