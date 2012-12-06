/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MenuParser.java
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
package com.emobc.android.menu.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.NextLevel;
import com.emobc.android.menu.Menu;
import com.emobc.android.menu.MenuItem;
import com.emobc.android.menu.NextLevelMenuItem;
import com.emobc.android.menu.SystemAction;
import com.emobc.android.menu.SystemActionMenuItem;
import com.emobc.android.parse.AbstractParser;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class MenuParser extends AbstractParser<Menu> {
	
	private static final String _MENU_TAG_ = "menu";
	private static final String _MENU_ITEM_TAG_ = "menu-item";
	private static final String _MENU_TITLE_TAG_ = "menu-title";
	private static final String _MENU_IMAGE_TAG_ = "menu-image";
	private static final String _SYSTEM_ACTION_TAG_ = "systemAction";
	
	private static final String _NEXT_LEVEL_TAG_ = "nextLevel";
	private static final String _NL_LEVEL_ID_TAG_ = "nextLevelLevelId";
	private static final String _NL_DATA_ID_TAG_ = "nextLevelDataId";
	
	public MenuParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected Menu generateObjectFromParseData(Map<String, Object> data) {
		Menu ret = null;
		if(data != null && !data.isEmpty()){
			ret = new Menu();
			
			@SuppressWarnings("unchecked")
			List<MenuItem> items = (List<MenuItem>)data.get(_MENU_TAG_);
			
			if(items != null && !items.isEmpty()){
				for(MenuItem item : items){
					ret.addMenuItem(item);
				}
			}
		}
		
		return ret;
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
			new NwXmlStandarParserTextHandler() {
			private List<MenuItem> items = new ArrayList<MenuItem>();
			private NextLevel nextLevel;
			private SystemAction systemAction;
			private String title;
			private String imageFileName;
			private String levelId;
			private String dataId;
			
			@Override
			public void handleText(String currentField, String text) {
				if(_MENU_TITLE_TAG_.equals(currentField)){
					title = text;					
				}else if(_MENU_IMAGE_TAG_.equals(currentField)){
					imageFileName = text;
				}else if(_SYSTEM_ACTION_TAG_.equals(currentField)){
					systemAction = SystemAction.parseText(text);
				}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
					levelId = text;
				}else if(currentField.equals(_NL_DATA_ID_TAG_)){
					dataId = text;
				}else{
					ret.put(currentField, text);
				}
				
			}
					
			@Override
			public void handleEndTag(String currentField) {
				if(_MENU_TAG_.equals(currentField)){
					ret.put(_MENU_TAG_, items);
				}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
					nextLevel = new NextLevel(levelId, dataId);
				}else if(_MENU_ITEM_TAG_.equals(currentField)){
					MenuItem menuItem = null;
					if(nextLevel != null)
						menuItem = new NextLevelMenuItem(title, imageFileName, nextLevel);
					else if(systemAction != null)
						menuItem = new SystemActionMenuItem(title, imageFileName, systemAction);
					if(menuItem != null)
						items.add(menuItem);
				}
			}
			
			@Override
			public void handleBeginTag(String currentField) {
				if(_MENU_ITEM_TAG_.equals(currentField)){
					nextLevel = null;
					systemAction = null;
					title = null;
					imageFileName = null;
				}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
					levelId = null;
					dataId = null;
				}
			}
		}
		, _MENU_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
}
