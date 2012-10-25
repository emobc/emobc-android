/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* TableParser.java
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
package com.emobc.android.data.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.emobc.android.data.metadata.Field;
import com.emobc.android.data.metadata.FieldType;
import com.emobc.android.data.metadata.Table;
import com.emobc.android.parse.EMobcParser;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class TableParser implements EMobcParser<Table>{
	private static final String _TABLES_TAG_ = "tables";
//	private static final String _TABLE_TAG = "table";
	private static final String _TABLE_NAME_TAG = "name";
	private static final String _TABLE_FIELDS_TAG = "fields";
	private static final String _TABLE_FIELD_TAG = "field";
	private static final String _TABLE_FIELD_NAME_TAG = "fieldName";
	private static final String _TABLE_FIELD_TYPE_TAG = "fieldType";
	private static final String _TABLE_FIELD_KEY_TAG = "fieldKey";
	

	private XmlPullParser xpp;

	public TableParser(XmlPullParser xpp) {
		super();
		this.xpp = xpp;
	}

	public Table parse(){
		if(xpp == null)
			return null;
		return fromTableData(parseTableFile());		
	}
	
	@SuppressWarnings("unchecked")
	private Table fromTableData(Map<String, Object> data) {
		Table ret = null;
		if(data != null && !data.isEmpty()){
			String tableName = (String)data.get(_TABLE_NAME_TAG);
			List<Field> fields = (List<Field>)data.get(_TABLE_FIELDS_TAG);
			ret = new Table(tableName);
			if(fields != null && !fields.isEmpty()){
				for(Field field : fields){
					ret.addField(field);
				}
			}
		}
		
		return ret;
	}
	
	private Map<String, Object> parseTableFile() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
			new NwXmlStandarParserTextHandler() {
			private List<Field> fieldList;
			private FieldType fieldType;
			private String fieldName;
			private boolean key = false;
			
			@Override
			public void handleText(String currentField, String text) {
				if(_TABLE_FIELDS_TAG.equals(currentField)){
					fieldList = new ArrayList<Field>();					
				}else if(_TABLE_FIELD_NAME_TAG.equals(currentField)){
					fieldName = text;
				}else if(_TABLE_FIELD_TYPE_TAG.equals(currentField)){
					fieldType = FieldType.parseText(text);
				}else if(_TABLE_FIELD_KEY_TAG.equals(currentField)){
					key = Boolean.parseBoolean(text);
				}else{
					ret.put(currentField, text);
				}
				
			}
					
			@Override
			public void handleEndTag(String currentField) {
				if(_TABLE_FIELD_TAG.equals(currentField)){
					Field field = new Field(fieldName, fieldType, key);
					fieldList.add(field);
				}else if(_TABLE_FIELDS_TAG.equals(currentField)){
					ret.put(_TABLE_FIELDS_TAG, fieldList);
				}

			}
			
			@Override
			public void handleBeginTag(String currentField) {						
				if(_TABLE_FIELD_TAG.equals(currentField)){
					key = false;
					fieldName = null;
					fieldType = null;
				}
			}
		}
		, _TABLES_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
}
