/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AbstractParser.java
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

import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public abstract class AbstractParser<T> implements EMobcParser<T> {

	protected XmlPullParser xpp;
	
	public AbstractParser(XmlPullParser xpp) {
		super();
		this.xpp = xpp;
	}
	
	public T parse(){
		if(xpp == null)
			return null;
		return generateObjectFromParseData(parseData());		
	}
	
	protected abstract T generateObjectFromParseData(Map<String, Object> data);
	
	protected abstract Map<String, Object> parseData();
}
