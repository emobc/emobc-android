/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* Model.java
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5215024398259728798L;
	private List<Table> tables = new ArrayList<Table>();
	private Map<String, Table> tableMap = new WeakHashMap<String, Table>();
	
	private final String name;

	public Model(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Table getTable(String tableName){
		return tableMap.get(tableName);
	}
	
	public Collection<? extends Table> getTables(){
		return Collections.unmodifiableCollection(tables);
	}
	
	public void addTable(Table table){
		if(table == null)
			throw new IllegalArgumentException("Table is null");
		if(!tables.contains(table)){
			tables.add(table);
			tableMap.put(table.getName(), table);
		}
	}
}
