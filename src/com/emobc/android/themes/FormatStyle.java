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

/**
 * Component that save a unique component FORMAT.
 * 
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class FormatStyle {

	private final String name;
	private final String textColor;
	private final String textSize;
	private final String textStyle;
	private final String typeFace;;
	private final String cacheColorHint;;
	private final String backgroundColor;
	private final String backgroundSelectionFileName;

	public FormatStyle(String name, String textColor, String textSize,
			String textStyle, String typeFace, String cacheColorHint,
			String backgroundColor, String backgroundSelectionFileName) {
		super();
		this.name = name;
		this.textColor = textColor;
		this.textSize = textSize;
		this.textStyle = textStyle;
		this.typeFace = typeFace;
		this.cacheColorHint = cacheColorHint;
		this.backgroundColor = backgroundColor;
		this.backgroundSelectionFileName = backgroundSelectionFileName;
	}

	public String getName() {
		return name;
	}

	public String getTextColor() {
		return textColor;
	}

	public String getTextSize() {
		return textSize;
	}

	public String getTextStyle() {
		return textStyle;
	}

	public String getTypeFace() {
		return typeFace;
	}

	public String getCacheColorHint() {
		return cacheColorHint;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public String getBackgroundSelectionFileName() {
		return backgroundSelectionFileName;
	}

	public String toString(){
		return "[format = " + name + "]";
	}
}
