/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* Entity.java
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

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class Entity implements Serializable {
	private static final long serialVersionUID = 8994319763813195117L;
	private final Table table;
	private final Object[] data;
	
	public Entity(Table table, Object[] data) {
		super();
		if(table == null)
			throw new IllegalArgumentException("Table is null");
		if(data == null)
			throw new IllegalArgumentException("Data is null");
		if(table.getFields().size() != data.length)
			throw new IllegalArgumentException("Table fields counts diferent from data size!");
		this.table = table;
		this.data = data;
	}
	
	public Object getFieldValue(String fieldName){
		int index = table.getFieldIndex(fieldName);
		if(index == Table.INVALID_INDEX_VALUE)
			throw new IllegalArgumentException(String.format("Field %s not found in Table %s", fieldName, table.getName()));
		return data[index];
	}
	
	public void setFieldValue(String fieldName, Object value){
		int index = table.getFieldIndex(fieldName);
		if(index == Table.INVALID_INDEX_VALUE)
			throw new IllegalArgumentException(String.format("Field %s not found in Table %s", fieldName, table.getName()));
		data[index] = value;		
	}

	public Table getTable() {
		return table;
	}

	public Object[] getData() {
		return data;
	}

	public String getId() {
		return String.valueOf(getFieldValue(Table.DEFAULT_ID_FIELD_NAME));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		return other.getId().equals(getId());
	}
 }
