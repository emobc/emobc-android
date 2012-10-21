/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ProfileActivityGenerator.java
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
package com.emobc.android.activities.generators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.emobc.android.activities.CoverActivity;
import com.emobc.android.activities.FormActivity;
import com.emobc.android.activities.R;
import com.emobc.android.activities.SplashActivity;
import com.emobc.android.levels.impl.FormDataItem;
import com.emobc.android.profiling.Profile;

/**
 * Initialize the Profile Activity.
 * @author Jorge E. Villaverde
 * @see FormActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class ProfileActivityGenerator extends AbstractActivtyGenerator {
	private Map<String,View> controlsMap;

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		return R.layout.form;
	}

	@Override
	protected void intializeSubActivity(final Activity activity) {
		Profile profile = SplashActivity.getApplicationData().getProfile();
		initializeHeader(activity, profile);

		controlsMap = new HashMap<String, View>();
				
		LinearLayout formLayout = (LinearLayout)activity.findViewById(R.id.formLayout);
		if(profile != null){
			for(FormDataItem dataItem : profile.getFields()){
				TextView label = new TextView(activity);
				label.setText(dataItem.getFieldLabel());
				formLayout.addView(label);
				insertField(activity, dataItem, formLayout);
			}
		}
		
		Button submit = new Button(activity);
		submit.setText(R.string.form_submit_buttom);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSubmit(activity);
			}
		});
		formLayout.addView(submit);
		((FormActivity) activity).setControlsMap(controlsMap);
	}

	/**
	 * Inserts a dataItem to View depending on the particular type
	 * @param activity
	 * @param dataItem
	 * @param formLayout
	 */
	private void insertField(Activity activity, FormDataItem dataItem, LinearLayout formLayout) {
		View control = null;
		
		switch (dataItem.getType()) {
		case INPUT_TEXT:
			control = insertTextField(activity, dataItem);
			break;
		case INPUT_NUMBER:
			control = insertNumberField(activity, dataItem);
			break;
		case INPUT_EMAIL:
			control = insertEmailField(activity, dataItem);
			break;
		case INPUT_PHONE:
			control = insertPhoneField(activity, dataItem);
			break;
		case INPUT_PASSWORD:
			control = insertPasswordField(activity, dataItem);
			break;
		case INPUT_CHECK:
			control = insertCheckField(activity, dataItem);
			break;
		case INPUT_PICKER:
			control = insertPickerField(activity, dataItem);
			break;
		default:
			break;
		}
		
		formLayout.addView(control);
		controlsMap.put(dataItem.getFieldName(), control);

	}

	private View insertPickerField(Activity activity, FormDataItem dataItem) {
	    Spinner spinner = new Spinner(activity);
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, 
	    		android.R.layout.simple_spinner_item, 
	    		dataItem.getParameters());
	    spinner.setAdapter(adapter);
	    return spinner;		
	}

	private View insertCheckField(Activity activity, FormDataItem dataItem) {
		CheckBox checkBox = new CheckBox(activity);
		checkBox.setTag(dataItem.getFieldName());
		return checkBox;		
	}

	private EditText insertPasswordField(Activity activity, FormDataItem dataItem) {
		EditText txt = insertTextField(activity, dataItem);
		txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		return txt;
	}
	private EditText insertPhoneField(Activity activity, FormDataItem dataItem) {
		EditText txt = insertTextField(activity, dataItem);
		txt.setInputType(InputType.TYPE_CLASS_PHONE);
		return txt;
	}

	private EditText insertEmailField(Activity activity, FormDataItem dataItem) {
		EditText txt = insertTextField(activity, dataItem);
		txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		return txt;
	}

	private EditText insertNumberField(Activity activity, FormDataItem dataItem) {
		EditText txt = insertTextField(activity, dataItem);
		txt.setInputType(InputType.TYPE_CLASS_NUMBER);
		return txt;
	}

	private EditText insertTextField(final Activity activity, FormDataItem dataItem) {
		EditText txt = new EditText(activity);
		txt.setTag(dataItem.getFieldName());
		txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (!hasFocus) {
		        	InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        }
		    }
		});
		return txt;
	}

	/**
	 * Check the contents of a web address to add new data to existing 
	 * app.xml file. Then, it goes to the next level.
	 * @param activity
	 */
	protected void processSubmit(Activity activity) {
		final SharedPreferences settings = Profile.getProfileData(activity);
		saveAllInstances(settings);
		Profile.setFilled(activity, true);
		Intent mainIntent = new Intent(activity, CoverActivity.class);
		activity.startActivity(mainIntent);
	}
	
	private void saveAllInstances(SharedPreferences settings) {
		Iterator<String> i = controlsMap.keySet().iterator();
		while (i.hasNext()){
			String key = i.next();
			View v = controlsMap.get(key);
			SharedPreferences.Editor editor = settings.edit();
			if (v instanceof Spinner){
            	editor.putInt(key,((Spinner) v).getSelectedItemPosition());
            }else if (v instanceof CheckBox){
            	editor.putBoolean(key,((CheckBox) v).isChecked());
            }else if (v instanceof EditText){
            	editor.putString(key, ((EditText)v).getText().toString());
            }
			editor.commit();
		}
		
	}
	
}
