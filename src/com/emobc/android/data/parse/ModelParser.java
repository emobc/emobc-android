/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ModelParser.java
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
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;
import com.emobc.android.parse.EMobcParser;
import com.emobc.android.parse.NwXmlStandarParser;
import com.emobc.android.parse.NwXmlStandarParserTextHandler;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ModelParser implements EMobcParser<Model> {
	private static final String _TABLES_TAG_ = "tables";
	private static final String _MODEL_NAME_TAG_ = "modelName";
	private static final String _TABLE_TAG = "table";
	private static final String _TABLE_NAME_TAG = "name";
	@SuppressWarnings("unused")
	private static final String _TABLE_FIELDS_TAG = "fields";
	private static final String _TABLE_FIELD_TAG = "field";
	private static final String _TABLE_FIELD_NAME_TAG = "fieldName";
	private static final String _TABLE_FIELD_TYPE_TAG = "fieldType";
	private static final String _TABLE_FIELD_KEY_TAG = "fieldKey";

	private XmlPullParser xpp;

	public ModelParser(XmlPullParser xpp) {
		super();
		this.xpp = xpp;
	}

	@Override
	public Model parse() {
		if(xpp == null)
			return null;
		return fromModelData(parseModelFile());		
	}

	@SuppressWarnings("unchecked")
	private Model fromModelData(Map<String, Object> data) {
		Model model = null;

		if(data != null && !data.isEmpty()){
			String modelName = (String)data.get(_MODEL_NAME_TAG_);
			if(modelName == null || modelName.isEmpty())
				modelName = "Err Model Name Not Found";
			model = new Model(modelName);
			List<Table> tables  = (List<Table>)data.get(_TABLES_TAG_);
			if(tables != null && !tables.isEmpty()){
				for(Table table : tables){
					model.addTable(table);
				}
			}
		}
		
		
		return model;
	}

	private Map<String, Object> parseModelFile() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
			new NwXmlStandarParserTextHandler() {
			private Table table;
			private List<Table> tables = new ArrayList<Table>();
			private FieldType fieldType;
			private String fieldName;
			private boolean key = false;
			
			@Override
			public void handleText(String currentField, String text) {
				if(_TABLE_NAME_TAG.equals(currentField)){
					table = new Table(text);					
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
					table.addField(field);
				}else if(_TABLE_TAG.equals(currentField)){
					tables.add(table);
				}else if(_TABLES_TAG_.equals(currentField)){
					ret.put(_TABLES_TAG_, tables);
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
