/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* FormatParser.java
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.parse.AbstractParser;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;
import com.emobc.android.themes.FormatStyle;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class FormatParser extends AbstractParser<Map<String, FormatStyle>> {
	private static final String _APPLICATION_TAG_ = "application";
	private static final String _FORMATS_TAG_ = "formats";
	private static final String _FORMAT_TAG_ = "format";
	private static final String _NAME_TAG_ = "name";
	private static final String _TEXTCOLOR_TAG_ = "textColor";
	private static final String _TEXTSIZE_TAG_ = "textSize";
	private static final String _TEXTSTYLE_TAG_ = "textStyle";
	private static final String _TYPEFACE_TAG_ = "typeFace";
	private static final String _CACHE_COLOR_HINT_TAG_ = "cacheColorHint";
	private static final String _BACKGROUND_COLOR_TAG_ = "backgroundColor";
	private static final String _BG_SELECTION_FILE_NAME_TAG_ = "backgroundSelectionFileName";

	public FormatParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected Map<String, FormatStyle> generateObjectFromParseData(Map<String, Object> data) {
		Map<String, FormatStyle> ret = new HashMap<String, FormatStyle>();
		@SuppressWarnings("unchecked")
		Iterator<FormatStyle> list = ((List<FormatStyle>)data.get(_FORMATS_TAG_)).iterator();
				
		while(list.hasNext()){
			FormatStyle currFormat = list.next();
			ret.put(currFormat.getName(), currFormat);
		}
		
		return ret;
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private FormatStyle currItem;
					private List<FormatStyle> currList;
					private String name;
					private String textColor;
					private String textSize;
					private String textStyle;
					private String typeFace;;
					private String cacheColorHint;;
					private String backgroundColor;
					private String backgroundSelectionFileName;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_FORMATS_TAG_)){
							currList = new ArrayList<FormatStyle>();
							ret.put(currentField, currList);
						}else if(currentField.equals(_NAME_TAG_)){
							name = text;
						}else if(currentField.equals(_TEXTCOLOR_TAG_)){
							textColor = text;
						}else if(currentField.equals(_TEXTSIZE_TAG_)){
							textSize = text;
						}else if(currentField.equals(_TEXTSTYLE_TAG_)){
							textStyle = text;
						}else if(currentField.equals(_TYPEFACE_TAG_)){
							typeFace = text;
						}else if(currentField.equals(_CACHE_COLOR_HINT_TAG_)){
							cacheColorHint = text;
						}else if(currentField.equals(_BACKGROUND_COLOR_TAG_)){
							backgroundColor = text;
						}else if(currentField.equals(_BG_SELECTION_FILE_NAME_TAG_)){
							backgroundSelectionFileName = text;
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_FORMAT_TAG_)){
							currItem = new FormatStyle(name, 
									textColor, 
									textSize, 
									textStyle, 
									typeFace, 
									cacheColorHint, 
									backgroundColor, 
									backgroundSelectionFileName);
							currList.add(currItem);
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {
						if(currentField.equals(_FORMAT_TAG_)){
							name = null;
							textColor = null;
							textSize = null;
							textStyle = null;
							typeFace = null; 
							cacheColorHint = null; 
							backgroundColor = null;
							backgroundSelectionFileName = null;							
						}
					}
				}
		, _APPLICATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
}
