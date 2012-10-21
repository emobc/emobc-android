/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * NwXmlStandarParser.java
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


import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/**
 * Main class used to parse a file, where are implemented its methods
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 **/
public class NwXmlStandarParser {
	private XmlPullParser xpp;
	private NwXmlStandarParserTextHandler handler;
	private String topLevelName;
	
	public NwXmlStandarParser(XmlPullParser xpp, 
			NwXmlStandarParserTextHandler handler,
			String topLevelName) {
		super();
		
		if(xpp == null) 
			throw new IllegalArgumentException("Invalid xpp = null");
		if(handler == null) 
			throw new IllegalArgumentException("Invalid handler = null");
		if(topLevelName == null) 
			throw new IllegalArgumentException("Invalid topLevelName = null");
		
		this.xpp = xpp;
		this.handler = handler;
		this.topLevelName = topLevelName; 
	}

	public void startParsing(){
		try {
			xpp.next();
			int eventType = xpp.getEventType();
			
			String currentField = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					if(topLevelName.equals(xpp.getName())){
						currentField = null;
					}else{ 
						currentField = xpp.getName();
						handler.handleBeginTag(currentField);						
					}				
				} else if (eventType == XmlPullParser.END_TAG) {
					handler.handleEndTag(xpp.getName());
					currentField = null;
				} else if (eventType == XmlPullParser.TEXT) {
					if(currentField != null){
						String text = xpp.getText();
						handler.handleText(currentField, text);
					}
				}
				eventType = xpp.next();
			}
			
		} catch (XmlPullParserException e) {
			Log.e("ApplicationData", "XmlPullParserException: "  + e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e("ApplicationData", "IOException: " + e.getLocalizedMessage());
		}
	}

}
