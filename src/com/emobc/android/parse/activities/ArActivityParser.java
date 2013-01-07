/**
* Copyright 2013 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ArActivityParser.java
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

import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.ArLevelDataItem;
import com.emobc.android.levels.impl.DefaultAppLevelData;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ArActivityParser extends ActivityLevelParser{
	private static final String _ITEM_DESCRIPTION_TAG_ = "itemDescripcion";
	
	public ArActivityParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ArLevelDataItem currItem;
					private String dataId;
					private String headerImage;
					private String headerText;
					private String geoRef;
					private String itemDescription;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_ID_TAG_)){
							dataId = text;							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							headerImage = text;
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							headerText = text;
						}else if(currentField.equals(_GEO_REF_TAG_)){
							geoRef = text;
						}else if(_ITEM_DESCRIPTION_TAG_.equals(currentField)){
							itemDescription = text;
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}else if(currentField.equals(_DATA_TAG_)){
							currItem = new ArLevelDataItem(
									dataId, 
									headerImage,  
									headerText,
									geoRef,
									itemDescription);
							appLevelData.addItem(currItem);
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
						if(currentField.equals(_DATA_TAG_)){
							dataId = null; 
							headerImage = null;  
							headerText = null;
							geoRef = null;
							itemDescription = null; 
						}
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}
}
