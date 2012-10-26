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

import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.sql.SQLiteSqlCreator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class DataEngine extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 0;
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

	public Model getModel() {
		return model;
	}
	
}
