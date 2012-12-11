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
import com.emobc.android.themes.ActivityTypeStyle;
import com.emobc.android.themes.LevelStyle;
import com.emobc.android.utils.Utils;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class StyleParser extends AbstractParser<StyleResult> {
	private static final String _APPLICATION_TAG_ = "application";
	private static final String _STYLES_TAG_ = "styles";
	private static final String _TYPEID_TAG_ = "typeId";
	private static final String _LEVEL_ID_TAG_ = "levelId";
	private static final String _TYPE_TAG_ = "type";
	private static final String _BACKGROUND_FILE_NAME_TAG_ = "backgroundFileName";
	private static final String _COMPONENTS_TAG_ = "components";

	private static final String _AT_STYLE = "_ACTIVITY_TYPE_STYLE_";
	private static final String _LEVEL_STYLE = "_LEVEL_STYLE_";
	
	public StyleParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected StyleResult generateObjectFromParseData(Map<String, Object> data) {
		Map<ActivityType, ActivityTypeStyle> atStyle = new HashMap<ActivityType, ActivityTypeStyle>();
		Map<String, LevelStyle> lStyle = new HashMap<String, LevelStyle>();
		
		@SuppressWarnings("unchecked")
		List<ActivityTypeStyle> atList = ((List<ActivityTypeStyle>)data.get(_AT_STYLE));
		
		if(atList != null && !atList.isEmpty()){
			for(ActivityTypeStyle currLevelTypeStyle : atList){
				atStyle.put(currLevelTypeStyle.getActivityType(), currLevelTypeStyle);				
			}
		}
		
		@SuppressWarnings("unchecked")
		List<LevelStyle> levelList = ((List<LevelStyle>)data.get(_LEVEL_STYLE));
		if(levelList != null && !atList.isEmpty()){
			for(LevelStyle levelStyle : levelList){
				lStyle.put(levelStyle.getLevelId(), levelStyle);
			}
		}
		
		return new StyleResult(atStyle, lStyle);
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private List<ActivityTypeStyle> atList;
					private List<LevelStyle> leveList;
					private ActivityType activityType;
					private String levelId;
					private String background;
					private String components;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_LEVEL_ID_TAG_)){
							levelId = text;
						}else if(currentField.equals(_TYPEID_TAG_)){
							try {
								activityType = ActivityType.valueOf(text);
							} catch (IllegalArgumentException e) {
							}
						}else if(currentField.equals(_BACKGROUND_FILE_NAME_TAG_)){
							background = text;
						}else if(currentField.equals(_COMPONENTS_TAG_)){
							components = text;
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_TYPE_TAG_)){
							if(activityType != null){
								atList.add(new ActivityTypeStyle(background, components, activityType));
							}else if(Utils.hasLength(levelId)){
								leveList.add(new LevelStyle(background, components, levelId));
							}							
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {
						if(_STYLES_TAG_.equals(currentField)){
							atList = new ArrayList<ActivityTypeStyle>();
							leveList = new ArrayList<LevelStyle>();
							ret.put(_AT_STYLE, atList);
							ret.put(_LEVEL_STYLE, leveList);
						}else if(_TYPE_TAG_.equals(currentField)){
							levelId = null;
							activityType = null;
							background = null;
							components = null;
						}
					}
				}
		, _APPLICATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}

}
