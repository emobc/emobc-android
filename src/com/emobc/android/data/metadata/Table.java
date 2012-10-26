/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* Table.java
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
package com.emobc.android.data.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Jorge E. Villaverde
 * @sice 0.1
 * @version 0.1
 */
public class Table implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3962721272514772791L;
	
	private static final String DEFAULT_ID_FILE_NAME = "id";
	private static final FieldType DEFAULT_ID_FILE_TYPE = FieldType.NUMBER;

	public static final int INVALID_INDEX_VALUE = -1;
	
	private final String name;
	private List<Field> fields = new ArrayList<Field>();
	private Map<String, Field> fieldMap = new WeakHashMap<String, Field>();
	
	public Table(String name) {
		super();
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Invalida tabla name: " + name); 
		this.name = name;
		addField(new Field(DEFAULT_ID_FILE_NAME, DEFAULT_ID_FILE_TYPE, true));
	}

	public void addField(Field field){
		if(field == null)
			throw new IllegalArgumentException("Field is null");
		if(fields.contains(field))
			throw new InvalidFieldException("Table: " + name + " already contains field: " + field.getName());
		fields.add(field);
		fieldMap.put(field.getName(), field);
	}
	
	public List<Field> getFields() {
		return Collections.unmodifiableList(fields);
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return "[table = "+name+"]";
	}

	public int getFieldIndex(String fieldName) {
		if(fieldName == null || fieldName.isEmpty())
			throw new IllegalArgumentException("Invalid Field Name: " + fieldName);
		Field field = fieldMap.get(fieldName);
		return fields.indexOf(field);
	}
}