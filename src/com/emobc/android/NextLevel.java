/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * NextLevel.java
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

import java.io.Serializable;

/**
 * Represents a <tt>NextLevel</tt> in the application.
 * <p>A NextLevel is a <code>levelId</code>-<code>dataId</code> pair, 
 * for access to a particular screen in the application.
 * </p></p>
 * The NextLevel is used to relate <code>levelId</code> from app.xml file
 * with <code>dataId</code> from other xml file.
 * </p>
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class NextLevel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6849041165502606331L;

	public static final int NO_LEVEL = -1;
	public static final String EMOBC_COVER_DATA_ID = "cover";
	
	private int levelNumber = NO_LEVEL;
	private String levelId;
	private int dataNumber = NO_LEVEL;
	private String dataId;
	
	public static NextLevel COVER_NEXT_LEVEL = new NextLevel(ApplicationData.EMOBC_LEVEL_ID, EMOBC_COVER_DATA_ID);

	public NextLevel() {
		super();
	}

	public NextLevel(String levelId, String dataId) {
		super();
		this.levelId = levelId;
		this.dataId = dataId;
	}
	
	public NextLevel(int levelNumber, int dataNumber) {
		super();
		this.levelNumber = levelNumber;
		this.dataNumber = dataNumber;
	}
	

	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public int getDataNumber() {
		return dataNumber;
	}

	public void setDataNumber(int dataNumber) {
		this.dataNumber = dataNumber;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		if(levelId != null && levelId.length() > 0){
			builder.append("Level Id:");
			builder.append(levelId);
		}else{
			builder.append("Level Nro:");
			builder.append(levelNumber);
		}
		if(dataId != null && dataId.length() > 0){
			builder.append( " Data Id: ");
			builder.append(dataId);
		}else{
			builder.append( " Data Nro: ");
			builder.append(dataNumber);
		}	
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataId == null) ? 0 : dataId.hashCode());
		result = prime * result + dataNumber;
		result = prime * result + ((levelId == null) ? 0 : levelId.hashCode());
		result = prime * result + levelNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NextLevel other = (NextLevel) obj;
		if (dataId == null) {
			if (other.dataId != null)
				return false;
		} else if (!dataId.equals(other.dataId))
			return false;
		if (dataNumber != other.dataNumber)
			return false;
		if (levelId == null) {
			if (other.levelId != null)
				return false;
		} else if (!levelId.equals(other.levelId))
			return false;
		if (levelNumber != other.levelNumber)
			return false;
		return true;
	}
}
