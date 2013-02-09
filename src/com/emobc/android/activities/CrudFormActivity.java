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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emobc.android.data.metadata.Entity;
import com.emobc.android.data.metadata.Field;
import com.emobc.android.data.metadata.Table;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class CrudFormActivity extends EMobcActivity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4777666912927892342L;
	private Table table;
	private Map<String,View> controlsMap;
	private Entity entity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.crud_form);
    	
    	table = (Table)getIntent().getSerializableExtra(CrudReadActivity.CRUD_TABLE);
    	entity = (Entity)getIntent().getSerializableExtra(CrudReadActivity.CRUD_ENTITY);
    	
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
			if(!Table.DEFAULT_ID_FIELD_NAME.equals(field.getName()))
				insertField(field, formLayout);
		}
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		Button submit = new Button(this);
		submit.setText(android.R.string.ok);
		submit.setLayoutParams(new LayoutParams(
		        ViewGroup.LayoutParams.WRAP_CONTENT,
	            ViewGroup.LayoutParams.WRAP_CONTENT));
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSubmit();
			}
		});

		Button cancel = new Button(this);
		cancel.setText(android.R.string.cancel);
		cancel.setLayoutParams(new LayoutParams(
		        ViewGroup.LayoutParams.WRAP_CONTENT,
	            ViewGroup.LayoutParams.WRAP_CONTENT));
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processCancel();
			}
		});
		
		ll.addView(submit);
		ll.addView(cancel);

		formLayout.addView(ll);
		
	}
	
	protected void processCancel() {
		Intent intent = getIntent();
		intent.putExtra(CrudReadActivity.CRUD_ENTITY, entity);
		setResult(RESULT_CANCELED, intent);
		finish();				
	}
	
	protected void processSubmit() {
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
		
		if(this.entity == null){
			this.entity = new Entity(table, data.toArray());	
		}else{
			List<Field> fields = table.getFields();
			for(int i=0; i < fields.size(); i ++){
				Field field = fields.get(i);
				Object value = data.get(i);
				if(!Table.DEFAULT_ID_FIELD_NAME.equals(field.getName())){
					this.entity.setFieldValue(field.getName(), value);
				}
			}
		}
		
		Intent intent = getIntent();
		intent.putExtra(CrudReadActivity.CRUD_ENTITY, entity);
		setResult(RESULT_OK, intent);
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
		
		if(entity != null){
			txt.setText(entity.getFieldValue(field.getName()).toString());
		}
		
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
