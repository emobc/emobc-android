/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CrudFormActivity.java
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
package com.emobc.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emobc.android.data.engine.DataEngine;
import com.emobc.android.data.metadata.Field;
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class CrudFormActivity extends Activity {
	private Table table;
	private Map<String,View> controlsMap;
	private Model model;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.crud_form);
    	
    	table = (Table)getIntent().getExtras().get("table");
    	model = (Model)getIntent().getExtras().get("model");;
    	
    	if(table != null){
    		TextView header = (TextView)findViewById(R.id.crud_header);
    		header.setText(table.getName());
    		
    		insertFields();
    	}
    }
	private void insertFields() {
		List<Field> fields = table.getFields();
		controlsMap = new HashMap<String, View>();
		
		LinearLayout formLayout = (LinearLayout)findViewById(R.id.crud_form_layout);
		
		for(Field field : fields){
			if(!Table.DEFAULT_ID_FILE_NAME.equals(field.getName()))
				insertField(field, formLayout);
		}
		
		Button submit = new Button(this);
		submit.setText(R.string.form_submit_buttom);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSubmit();
			}
		});
		formLayout.addView(submit);
		
	}
	
	protected void processSubmit() {
		DataEngine engine = new DataEngine(this, model);
		
		List<Object> data = new ArrayList<Object>();
		// add de id
		data.add("");
		
		for(Field field : table.getFields()){
			View view = controlsMap.get(field.getName());
			if(view != null){
				String value = ((EditText)view).getText().toString();
				data.add(value);
			}
		}
		
		engine.createEntity(table, data.toArray());
		
    	Intent createEntityIntend = new Intent();
    	createEntityIntend = new Intent(this, CrudReadActivity.class);
    	createEntityIntend.putExtra("table", "table-test.xml");
		startActivity(createEntityIntend);
		finish();
	}
	
	private void insertField(Field field, LinearLayout formLayout) {
		View control = null;
		
		switch(field.getType()){
		case TEXT:
			control = insertTextField(field, formLayout);
			break;
		case NUMBER:
			control = insertNumberField(field, formLayout);
			break;
		case DATE:
			control = insertDateField(field, formLayout);
			break;
		default:
			break;
		}
		
		if(control != null){
			controlsMap.put(field.getName(), control);
			formLayout.addView(control);
		}
	}
	
	private View insertDateField(Field field, LinearLayout formLayout) {
		EditText txt = createEditText(field);
		txt.setInputType(InputType.TYPE_CLASS_DATETIME);
		return txt;
	}
	
	private View insertNumberField(Field field, LinearLayout formLayout) {
		EditText txt = createEditText(field);
		txt.setInputType(InputType.TYPE_CLASS_NUMBER);
		return txt;
	}
	
	private View insertTextField(Field field, LinearLayout formLayout) {
		EditText txt = createEditText(field);
		txt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		return txt;
	}
	
	private EditText createEditText(Field field){
		EditText txt = new EditText(this);
		txt.setTag(field.getName());
		txt.setHint(field.getName());
		
		txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (!hasFocus) {
		        	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        }
		    }
		});
		
		return txt;
	}
}
