/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CoverParser.java
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
package com.emobc.android.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.AppButton;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.CoverActivityGenerator;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class CoverParser extends AbstractParser<CoverActivityGenerator>{
	private static final String _BG_FILE_NAME_ = "backgroundFileName";
	private static final String _TITLE_FILE_NAME_ = "titleFileName";
	private static final String _FACEBOOK_TAG_ = "facebook";
	private static final String _FACEBOOK_IMAGE_TAG_ = "facebookImage";
	private static final String _TWITTER_TAG_ = "twitter";
	private static final String _TWITTER_IMAGE_TAG_ = "twitterImage";
	private static final String _WWW_TAG_ = "www";
	private static final String _BUTTONS_TAG_ = "buttons";
	private static final String _BUTTON_TAG_ = "button";	
	private static final String _BUTTON_TITLE_TAG_ = "buttonTitle";	
	private static final String _BUTTON_FILE_NAME_TAG_ = "buttonFileName";	
	private static final String _NEXT_LEVEL_TAG_ = "nextLevel";
	private static final String _LEVEL_NUMBER_TAG_ = "levelNumber";
	private static final String _NL_LEVEL_ID_TAG_ = "nextLevelLevelId";
	private static final String _NL_DATA_ID_TAG_ = "nextLevelDataId";
	private static final String _DATA_NUMBER_TAG_ = "dataNumber";
	private static final String _LEVEL_TAG_ = "level";	

	public CoverParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected CoverActivityGenerator generateObjectFromParseData(Map<String, Object> data) {
		CoverActivityGenerator ret = new CoverActivityGenerator();

		ret.setBackgroundFileName((String)data.get(_BG_FILE_NAME_));
		ret.setTitleFileName((String)data.get(_TITLE_FILE_NAME_));
		@SuppressWarnings("unchecked")
		List<AppButton> buttons = (List<AppButton>)data.get(_BUTTONS_TAG_);
		ret.setFacebookUrl((String)data.get(_FACEBOOK_TAG_));
		ret.setFacebookImage((String)data.get(_FACEBOOK_IMAGE_TAG_));
		ret.setTwitterUrl((String)data.get(_TWITTER_TAG_));
		ret.setTwitterImage((String)data.get(_TWITTER_IMAGE_TAG_));
		ret.setWwwUrl((String)data.get(_WWW_TAG_));
				
		if(buttons != null){
			ret.setButtons(Collections.unmodifiableList(buttons));
		}		
		return ret;
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
				new NwXmlStandarParserTextHandler() {
					private List<AppButton> buttons = null;
					private AppButton currentButton = null;
					private NextLevel nextLevel = null;
					@Override
					public void handleText(String currentField, String text) {
							
						if(currentField.equals(_BUTTONS_TAG_)){
							buttons = new ArrayList<AppButton>();
							ret.put(_BUTTONS_TAG_, buttons);
						}else if(currentField.equals(_BUTTON_TAG_)){
							currentButton = new AppButton();
							buttons.add(currentButton);
						}else if(currentField.equals(_BUTTON_TITLE_TAG_)){
							currentButton.setTitle(text);
						}else if(currentField.equals(_BUTTON_FILE_NAME_TAG_)){
							currentButton.setFileName(text);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currentButton.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
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
		, _LEVEL_TAG_);
		
		parser.startParsing();
		
		return ret;
	}

}
