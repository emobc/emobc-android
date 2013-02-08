/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ShowProfileActivity.java
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.levels.impl.FormDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.profiling.Profile;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ShowProfileActivity extends CreateMenus {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7453009669247569934L;
	private static final String TAG = "ShowProfileActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.form);
		Profile profile = getApplicationData().getProfile();
		initializeHeader(profile.getHeaderText(), profile.getHeaderImageFile());
		
		showProfileData(profile);		
		
		rotateScreen(this);
		
		setCurrentNextLevel(NextLevel.PROFILE_NEXT_LEVEL);
		
		setEntryPoint(false);
		createMenus();
	}
	
	private void showProfileData(Profile profile) {
		final SharedPreferences settings = Profile.getProfileData(this);

		LinearLayout formLayout = (LinearLayout)findViewById(R.id.formLayout);
		if(profile != null){
			for(FormDataItem dataItem : profile.getFields()){
				Object itemValue = null;
				
				switch (dataItem.getType()) {
				case INPUT_LABEL:
					break;
				case INPUT_TEXT:
				case INPUT_NUMBER:
				case INPUT_EMAIL:
				case INPUT_PHONE:
				case INPUT_PASSWORD:
				case INPUT_TEXTVIEW:
					itemValue = settings.getString(dataItem.getFieldName(), null);
					break;
				case INPUT_CHECK:
					itemValue = settings.getBoolean(dataItem.getFieldName(), Boolean.FALSE);
					break;
				case INPUT_PICKER:
					itemValue = settings.getInt(dataItem.getFieldName(), 0);
					break;
				case INPUT_IMAGE:
				default:
					break;
				}
				insertField(dataItem, formLayout, itemValue);
			}
		}
		Button submit = new Button(this);
		
		if(Utils.hasLength(profile.getEditImage())){
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(this, profile.getEditImage());
				submit.setBackgroundDrawable(drawable);
			} catch (InvalidFileException e) {
				Log.e(TAG, e.getLocalizedMessage());
			}									
		}else{
			submit.setText(R.string.edit_profile);
		}
		
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent (ShowProfileActivity.this, FormActivity.class);

				intent.putExtra(ApplicationData.NEXT_LEVEL_TAG, NextLevel.PROFILE_NEXT_LEVEL);
				intent.putExtra(ApplicationData.IS_ENTRY_POINT_TAG, false);

				startActivity(intent);
				
				finish();				
			}
		});
		formLayout.addView(submit);
		
	}

	protected void initializeHeader(String headerText, String headerImage){
		if(headerText == null){
			headerText = getString(R.string.view_profile);
		}
		
		TextView header = (TextView)findViewById(R.id.header);
		if(Utils.hasLength(headerImage)){
			try {
				header.setBackgroundDrawable(ImagesUtils.getDrawable(this, headerImage));
			} catch (InvalidFileException e) {
				Log.e(TAG, "Error loading Image "+ headerImage);
			}
		}		
		if(Utils.hasLength(headerText)){
			header.setText(headerText);
		}	
	}
		   
	private void insertField(FormDataItem dataItem, LinearLayout formLayout, Object itemValue) {
		View control = null;
		
		if(dataItem.getType() == null)
			return;
		
		switch (dataItem.getType()) {
		case INPUT_LABEL:
			break;
		case INPUT_TEXT:
		case INPUT_NUMBER:
		case INPUT_EMAIL:
		case INPUT_PHONE:
		case INPUT_TEXTVIEW:
			control = insertTextField(dataItem, String.valueOf(itemValue));
			break;
		case INPUT_PASSWORD:
			control = insertPasswordField(dataItem, String.valueOf(itemValue));
			break;
		case INPUT_CHECK:
			control = insertCheckField(dataItem, (Boolean)itemValue);
			break;
		case INPUT_PICKER:
			control = insertPickerField(dataItem, (Integer)itemValue);
		case INPUT_IMAGE:
			break;
		default:
			break;
		}

		if (control != null) {
			formLayout.addView(control);
		}		
	}
	
	private View insertPickerField(FormDataItem dataItem, Integer itemValue) {
	    Spinner spinner = new Spinner(this);
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
	    		android.R.layout.simple_spinner_item, 
	    		dataItem.getParameters());
	    spinner.setAdapter(adapter);
	    return spinner;		
	}

	private View insertCheckField(FormDataItem dataItem, Boolean itemValue) {
		return insertField(dataItem, itemValue == true ? "Si" : "No");
	}

	private View insertPasswordField(FormDataItem dataItem, Object itemValue) {
		return insertField(dataItem, "****");
	}

	private View insertTextField(final FormDataItem dataItem, final String itemValue) {
		return insertField(dataItem, itemValue);
	}
	
	private View insertField(final FormDataItem dataItem, final String itemValue) {
		LinearLayout ctrlLayout = new LinearLayout(this);
		ctrlLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				70);
		
		ctrlLayout.setLayoutParams(params);
		
		TextView txtViewLabel = new TextView(this);
		txtViewLabel.setText(dataItem.getFieldLabel());
		txtViewLabel.setTypeface(null, Typeface.BOLD);
		
		ctrlLayout.addView(txtViewLabel);
		
		TextView txtViewValue = new TextView(this);
		txtViewValue.setText(itemValue);
		ctrlLayout.addView(txtViewValue);
		
		return ctrlLayout;
	}
	
}
