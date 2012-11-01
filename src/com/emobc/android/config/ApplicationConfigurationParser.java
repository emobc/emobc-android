/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* WebActivityGenerator.java
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
package com.emobc.android.config;

import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;

/**
 * Application Configuration Objects.
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 **/
public class ApplicationConfigurationParser {
	private XmlPullParser xpp;
	
	private static final String _CONFIGURATION_TAG_ = "config";
	
	private static final String _APP_DATA_URL_TAG = "applicationDataUrl";
	
	
	public ApplicationConfigurationParser(XmlPullParser xpp){
		super();
		this.xpp = xpp;
	}
	
	public ApplicationConfiguration parse(){
		if(xpp == null)
			return null;
		return fromConfigData(parseConfigurationFile());		
	}

	private ApplicationConfiguration fromConfigData(Map<String, Object> data) {
		ApplicationConfiguration ret = new ApplicationConfiguration();
		if(data != null && !data.isEmpty()){
			for(String key : data.keySet()){
				ret.setAttibute(key, data.get(key));
			}
		}
		return ret;
	}

	private Map<String, Object> parseConfigurationFile() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
			new NwXmlStandarParserTextHandler() {
					
			@Override
			public void handleText(String currentField, String text) {
				if(_APP_DATA_URL_TAG.equals(currentField)){
					ret.put(ApplicationConfiguration.REMOTE_APP_URL, text);
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
		, _CONFIGURATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
}