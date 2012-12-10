/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* StyleParser.java
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
package com.emobc.android.themes.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.ActivityType;
import com.emobc.android.parse.AbstractParser;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;
import com.emobc.android.themes.LevelTypeStyle;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class StyleParser extends AbstractParser<Map<ActivityType, LevelTypeStyle>> {
	private static final String _APPLICATION_TAG_ = "application";
	private static final String _STYLES_TAG_ = "styles";
	private static final String _TYPEID_TAG_ = "typeId";
	private static final String _TYPE_TAG_ = "type";
	private static final String _BACKGROUND_FILE_NAME_TAG_ = "backgroundFileName";
	private static final String _COMPONENTS_TAG_ = "components";

	public StyleParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected Map<ActivityType, LevelTypeStyle> generateObjectFromParseData(Map<String, Object> data) {
		Map<ActivityType, LevelTypeStyle> ret = new HashMap<ActivityType, LevelTypeStyle>();

		@SuppressWarnings("unchecked")
		List<LevelTypeStyle> list = ((List<LevelTypeStyle>)data.get(_STYLES_TAG_));
		
		if(list != null && !list.isEmpty()){
			for(LevelTypeStyle currLevelTypeStyle : list){
				ret.put(currLevelTypeStyle.getLevelType(), currLevelTypeStyle);				
			}
		}
		return ret;
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private LevelTypeStyle currItem;
					private List<LevelTypeStyle> currList;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_STYLES_TAG_)){
							currList = new ArrayList<LevelTypeStyle>();
							ret.put(currentField, currList);
						}else if(currentField.equals(_TYPE_TAG_)){
							currItem = new LevelTypeStyle();
							currList.add(currItem);
						}else if(currentField.equals(_TYPEID_TAG_)){
							try {
								currItem.setLevelType(ActivityType.valueOf(text));
							} catch (IllegalArgumentException e) {
							}
						}else if(currentField.equals(_BACKGROUND_FILE_NAME_TAG_)){
							currItem.setBackground(text);
						}else if(currentField.equals(_COMPONENTS_TAG_)){
							currItem.setComponents(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
					}
					
					@Override
					public void handleBeginTag(String currentField) {
					}
				}
		, _APPLICATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}

}
