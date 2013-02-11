/**
* Copyright 2013 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ListActivityParser.java
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
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.NextLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.DefaultAppLevelData;
import com.emobc.android.levels.impl.ListItemDataItem;
import com.emobc.android.levels.impl.ListLevelDataItem;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;

/**
 * Generate a table of the elements of XmlPullParser useful for the List Activity
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ListActivityParser extends ActivityLevelParser {

	private static final String _LIST_TAG_ = "list";
	private static final String _LIST_ITEM_TAG_ = "listItem";
	private static final String _ORDER_TAG_ = "order";
	
	private static final String _IMAGE_FILE_TAG_ = "imageFile";
	private static final String _TEXT_TAG_ = "text";
	private static final String _DESCRIPTION_TAG = "description";
	
	public ListActivityParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ListLevelDataItem currItem;
					private List<ListItemDataItem> list;
					private ListItemDataItem currListItem;
					private NextLevel nextLevel;
					private String imageFile;
					private String text;
					private String description;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ListLevelDataItem();
							appLevelData.addItem(currItem);
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_ORDER_TAG_)){
							currItem.setOrder(Boolean.parseBoolean(text));
						}else if(currentField.equals(_LIST_TAG_)){ 
							list = currItem.getList();
							list.clear();
						}else if(currentField.equals(_TEXT_TAG_)){
							this.text = text;
						}else if(_DESCRIPTION_TAG.equals(currentField)){
							this.description = text;
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							this.imageFile = text;	
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else if(currentField.equals(_GEO_REF_TAG_)){
							currItem.setGeoReferencia(text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}else if(currentField.equals(_LIST_ITEM_TAG_)){
							currListItem = new ListItemDataItem(nextLevel, imageFile, text, description);
							list.add(currListItem);
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
						if(currentField.equals(_LIST_ITEM_TAG_)){
							nextLevel = null;
							imageFile = null; 
							text = null;
							description = null;
						}
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;	
	}
}