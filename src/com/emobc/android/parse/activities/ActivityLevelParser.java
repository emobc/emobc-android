/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ActivityLevelParser.java
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
package com.emobc.android.parse.activities;

import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.levels.AppLevelData;
import com.emobc.android.parse.AbstractParser;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public abstract class ActivityLevelParser extends AbstractParser<AppLevelData> {
	protected static final String _DATA_TAG_ = "data";
	protected static final String _DATA_ID_TAG_ = "dataId";
	protected static final String _LEVEL_DATA_TAG_ = "levelData";
	protected static final String _HEADER_IMAGE_FILE_TAG_ = "headerImageFile";
	protected static final String _HEADER_TEXT_TAG_ = "headerText";
	protected static final String _GEO_REF_TAG_ = "geoRef";
	
	// Next Level Tags
	protected static final String _NEXT_LEVEL_TAG_ = "nextLevel";
	protected static final String _NL_LEVEL_ID_TAG_ = "nextLevelLevelId";
	protected static final String _NL_DATA_ID_TAG_ = "nextLevelDataId";

	protected static final String _LEVEL_NUMBER_TAG_ = "levelNumber";
	protected static final String _DATA_NUMBER_TAG_ = "dataNumber";
	protected static final String _LEVEL_ID_TAG_ = "levelId";
	

	public ActivityLevelParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected AppLevelData generateObjectFromParseData(Map<String, Object> data) {
		return (AppLevelData)data.get(_LEVEL_DATA_TAG_);
	}
}
