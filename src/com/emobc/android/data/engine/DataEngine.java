/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* DataEngine.java
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
package com.emobc.android.data.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.emobc.android.data.metadata.Entity;
import com.emobc.android.data.metadata.Field;
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;
import com.emobc.android.data.sql.SQLiteSqlCreator;

/**
 * Simple SQLite Database Engine Wrapper.
 * This class uses the metadata objects such as {@link Table} and {@link Field}
 * to create a CRUD Facade.
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class DataEngine extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private final Model model;
	
	private SQLiteSqlCreator sqlCreator = new SQLiteSqlCreator();
	
	public DataEngine(Context context, Model model){
		super(context, model.getName(), null, DATABASE_VERSION);
		this.model = model;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String createSql = sqlCreator.createModelSql(model);
		db.execSQL(createSql);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String dropSql = sqlCreator.dropModelSql(model);
		db.execSQL(dropSql);
		onCreate(db);
	}

	/**
	 * @return Database Model Metadata.
	 */
	public Model getModel() {
		return model;
	}
	
	
	/**
	 * Creates a {@link Cursor} for {@link Table}.
	 * If the table does not exist in the model, an {@link IllegalArgumentException} is thrown.
	 * @param table
	 * @return
	 */
	public Cursor readTable(Table table){
		if(table == null)
			throw new IllegalArgumentException("Table is null");
		Table modelTable = model.getTable(table.getName());		
		if(modelTable == null)
			throw new IllegalArgumentException("Model has no table: " + table.getName());
		
		Cursor ret = getReadableDatabase().query(table.getName(),
				table.getColumns(),
				null, null, null, null, null);
		
		return ret;
	}
	
	/**
	 * Read all {@link Entity} from the database.
	 * @param table
	 * @return List of entities read form database.
	 */
	public List<Entity> readAllEntities(Table table) {
		Cursor cursor = readTable(table);
		if(cursor == null)
			return null;
		
		List<Entity> entities = new ArrayList<Entity>();
		if (cursor.moveToFirst()) {
			do {
				Entity entity = readEntityFromCursor(cursor, table);
				entities.add(entity);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return entities;
	}
	
	public Entity findEntityById(Table table, String id){
		checkTable(table);
		
		Cursor cursor = getReadableDatabase().query(table.getName(), 
				table.getColumns(), 
				Table.DEFAULT_WHERE,
				new String[]{id},
				null,
				null,
				null);
		
		Entity ret = null;
		
		if (cursor.moveToFirst()) {
			do {
				ret = readEntityFromCursor(cursor, table);
				if(ret != null){
					break;
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		return ret ;
	}
	
	public boolean createEntity(Entity entity) {
		return createEntity(entity.getTable(), entity.getData());
	}
	
	public boolean createEntity(Table table, Object[] data){		
		if(table == null)
			throw new IllegalArgumentException("Table is null");
		Table modelTable = model.getTable(table.getName());		
		if(modelTable == null)
			throw new IllegalArgumentException("Model has no table: " + table.getName());

		ContentValues values = new ContentValues();
		List<Field> fields = table.getFields();
		for(int i = 0; i < fields.size(); i++){
			Field field = fields.get(i);
			if(!Table.DEFAULT_ID_FIELD_NAME.equals(field.getName())){
				values.put(field.getName(), data[i].toString());
			}
		}
		Long id = getWritableDatabase().insert(table.getName(), null, values);
		return (id != -1);
	}
	
	
	public boolean updateEntity(Entity entity){
		checkEntity(entity);
		Table table = entity.getTable();
		Object[] data = entity.getData();
		
		String id = null;
		ContentValues values = new ContentValues();
		List<Field> fields = table.getFields();
		for(int i = 0; i < fields.size(); i++){
			Field field = fields.get(i);
			if(Table.DEFAULT_ID_FIELD_NAME.equals(field.getName())){
				id = data[i].toString();
			}else{
				values.put(field.getName(), data[i].toString());
			}
		}
		int ret = getWritableDatabase().update(
				table.getName(), 
				values, Table.DEFAULT_WHERE,
				new String[]{id});
		return (ret != -1);
	}
	
	public boolean deleteEntity(Entity entity){
		checkEntity(entity);
		Table table = entity.getTable();
		String id = entity.getId();
		
		int rowsAffected = getWritableDatabase().delete(
				table.getName(),
				Table.DEFAULT_WHERE,
				new String[]{id});
		
		return (rowsAffected > 0);
	}

	private void checkEntity(Entity entity) {
		if(entity == null)
			throw new IllegalArgumentException("Entity is null");		
		if(entity.getTable() == null)
			throw new IllegalArgumentException("Table is null");
		if(model.getTable(entity.getTable().getName()) == null)
			throw new IllegalArgumentException("Model has no table: " + entity.getTable().getName());
		if(entity.getData() == null)
			throw new IllegalArgumentException("Entity Data is null");		
	}
	
	private void checkTable(Table table){
		if(model.getTable(table.getName()) == null)
			throw new IllegalArgumentException("Model has no table: " + table.getName());		
	}
	
	private static Entity readEntityFromCursor(Cursor cursor, Table table){
		List<Field> fields = table.getFields();
		
		List<Object> data = new ArrayList<Object>(fields.size());
		for(int i=0;i < fields.size(); i++){
			Field field = fields.get(i);
			switch (field.getType()) {
			case TEXT:
				data.add(cursor.getString(i));	
				break;
			case NUMBER:
				data.add(cursor.getInt(i));
				break;
			case DATE:
				data.add(cursor.getString(i));
				break;
			default:
				break;
			}
		}
		Entity entity = new Entity(table, data.toArray());
		return entity;
	}
}
