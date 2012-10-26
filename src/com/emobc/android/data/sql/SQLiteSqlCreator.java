/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* SQLiteSqlCreator.java
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
package com.emobc.android.data.sql;

import java.util.List;

import com.emobc.android.data.metadata.Field;
import com.emobc.android.data.metadata.FieldType;
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class SQLiteSqlCreator {
	
	private static final String CREATE_STATEMENT = "CREATE TABLE ";
	private static final String OPEN_PARENTESIS = "(";
	private static final String CLOSE_PARENTESIS = ")";
	private static final String END_STATEMENT = ";";
	private static final String FIELD_SEPARATOR = ",\n";
	private static final String FIELD_NAME_TYPE_SEPARATOR = " ";
	
	private static final String VARCHAR_FIELD_TYPE = "VARCHAR";
	private static final String DATETIME_FIELD_TYPE = "DATETIME";
	private static final String INTEGER_FIELD_TYPE = "INTEGER";
	private static final Object PRIMARY_KEY_STATEMENT = " PRIMARY KEY";
	private static final Object DROP_STATEMENT = "DROP TABLE IF EXISTS ";
	
	public String createTableSql(Table table){
		StringBuilder builder = new StringBuilder();
		
		builder.append(CREATE_STATEMENT);
		builder.append(table.getName());
		builder.append(OPEN_PARENTESIS);
		builder.append(createTableFieldsDefinition(table.getFields()));
		builder.append(CLOSE_PARENTESIS);
		builder.append(END_STATEMENT);
		
		return builder.toString();
	}

	private String createTableFieldsDefinition(List<Field> fields) {
		if(fields == null || fields.isEmpty())
			return "";
		StringBuilder builder = new StringBuilder();
		String sep = "";
		for(Field field : fields){
			builder.append(sep);
			builder.append(createTableFieldDefinition(field));
			sep = FIELD_SEPARATOR;
		}
		return builder.toString();
	}

	private String createTableFieldDefinition(Field field) {
		if(field == null)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(field.getName());
		builder.append(FIELD_NAME_TYPE_SEPARATOR);
		builder.append(mapSqliteFieldType(field.getType()));
		if(field.isKey()){
			builder.append(PRIMARY_KEY_STATEMENT);
		}
		return builder.toString();
	}

	private String mapSqliteFieldType(FieldType type) {
		switch (type) {
		case TEXT:
			return VARCHAR_FIELD_TYPE;
		case DATE:
			return DATETIME_FIELD_TYPE;
		case NUMBER:
			return INTEGER_FIELD_TYPE;
		default:
			return VARCHAR_FIELD_TYPE;
		}
		
	}

	public String createModelSql(Model model) {
		StringBuilder builder = new StringBuilder();
		
		String sep = "";
		for(Table table : model.getTables()){
			builder.append(sep);
			builder.append(createTableSql(table));
			sep = "\n";
		}
		
		return builder.toString();
	}

	public String dropModelSql(Model model) {
		StringBuilder builder = new StringBuilder();
		
		String sep = "";
		for(Table table : model.getTables()){
			builder.append(sep);
			builder.append(dropTableSql(table));
			sep = "\n";
		}
		
		return builder.toString();
	}

	public String dropTableSql(Table table) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(DROP_STATEMENT);
		builder.append(table.getName());
		builder.append(END_STATEMENT);
		
		return builder.toString();
	}
}
