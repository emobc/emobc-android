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
public class MenuParser extends AbstractParser<List<Menu>> {
	private static final String _MENUES_TAG = "level";
	private static final String _MENU_LEVEL_TAG = "menuLevel";
	private static final String _MENU_TAG_ = "menuActions";
	private static final String _MENU_ITEM_TAG_ = "action";
	private static final String _MENU_TITLE_TAG_ = "actionTitle";
	private static final String _MENU_IMAGE_TAG_ = "actionImageName";
	private static final String _SYSTEM_ACTION_TAG_ = "systemAction";
	
	private static final String _NEXT_LEVEL_TAG_ = "nextLevel";
	private static final String _NL_LEVEL_ID_TAG_ = "nextLevelLevelId";
	private static final String _NL_DATA_ID_TAG_ = "nextLevelDataId";
	
	public MenuParser(XmlPullParser xpp) {
		super(xpp);
	}

	@Override
	protected List<Menu> generateObjectFromParseData(Map<String, Object> data) {
		if(data != null && !data.isEmpty()){
			@SuppressWarnings("unchecked")
			List<Menu> ret = (List<Menu>)data.get(_MENUES_TAG);
			return ret;
		}
		return null;
	}

	@Override
	protected Map<String, Object> parseData() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
			new NwXmlStandarParserTextHandler() {
			private List<MenuItem> items = null;
			private NextLevel nextLevel;
			private SystemAction systemAction;
			private String menuLevelId;
			private String title;
			private String imageFileName;
			private String levelId;
			private String dataId;
			private List<Menu> menuList = new ArrayList<Menu>();
			
			@Override
			public void handleText(String currentField, String text) {
				if(_MENU_LEVEL_TAG.equals(currentField)){
					menuLevelId = text;
				}else if(_MENU_TITLE_TAG_.equals(currentField)){
					title = text;					
				}else if(_MENU_IMAGE_TAG_.equals(currentField)){
					imageFileName = text;
				}else if(_SYSTEM_ACTION_TAG_.equals(currentField)){
					systemAction = SystemAction.parseText(text);
				}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
					levelId = text;
				}else if(currentField.equals(_NL_DATA_ID_TAG_)){
					dataId = text;
				}
				
			}
					
			@Override
			public void handleEndTag(String currentField) {
				if(_MENUES_TAG.equals(currentField)){
					ret.put(_MENUES_TAG, menuList);
				}else if(_MENU_TAG_.equals(currentField)){
					Menu menu = new Menu(menuLevelId);
					if(items != null && !items.isEmpty()){
						for(MenuItem item : items){
							menu.addMenuItem(item);
						}
					}
					menuList.add(menu);
				}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
					nextLevel = new NextLevel(levelId, dataId);
				}else if(_MENU_ITEM_TAG_.equals(currentField)){
					MenuItem menuItem = null;
					if(nextLevel != null && nextLevel.isDefined())
						menuItem = new NextLevelMenuItem(title, imageFileName, nextLevel);
					else if(systemAction != null)
						menuItem = new SystemActionMenuItem(title, imageFileName, systemAction);
					if(menuItem != null)
						items.add(menuItem);
				}
			}
			
			@Override
			public void handleBeginTag(String currentField) {
				if(_MENU_TAG_.equals(currentField)){
					menuLevelId = null;
					items = new ArrayList<MenuItem>();
				}else if(_MENU_ITEM_TAG_.equals(currentField)){
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
		, _MENUES_TAG);
		
		parser.startParsing();
		
		return ret;
	}
}
