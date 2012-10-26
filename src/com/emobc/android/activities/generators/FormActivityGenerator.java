/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* FormActivityGenerator.java
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.FormActivity;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.FormDataItem;
import com.emobc.android.levels.impl.FormLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.RetreiveFileContentTask;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "FORM". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class FormActivityGenerator extends LevelActivityGenerator {
	
	private Map<String,View> controlsMap;
	private FormLevelDataItem item;
	
	public FormActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		this.item = (FormLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		controlsMap = new HashMap<String, View>();
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner(); 
		
		LinearLayout formLayout = (LinearLayout)activity.findViewById(R.id.formLayout);
		for(FormDataItem dataItem : item.getList()){
			TextView label = new TextView(activity);
			label.setText(dataItem.getFieldLabel());
			formLayout.addView(label);
			insertField(activity, dataItem, formLayout);
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
	 * Check the contents of a web address to add new data to existing 
	 * app.xml file. Then, it goes to the next level.
	 * @param activity
	 */
	protected void processSubmit(Activity activity) {
		try {
			List<NameValuePair> parameters = createParameters();			
			try {
				URL url = new URL(item.getActionUrl());

				RetreiveFileContentTask task = new RetreiveFileContentTask(parameters, true); 
				task.execute(url);
				try {
					String text = task.get();
		        	
		        	ApplicationData.mergeAppDataFromString(activity, text);
		        	
		        	showNextLevel(activity, item.getNextLevel());
				} catch (InterruptedException e) {
			    	Log.e("FormActivityGenerator: InterruptedException: ", e.getMessage());
				} catch (ExecutionException e) {
			    	Log.e("FormActivityGenerator: ExecutionException: ", e.getMessage());
				}
			} catch (MalformedURLException e1) {
			}		    
		} catch (final RequiredFieldException e) {
			final AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
			dlg.setTitle(R.string.form_level_title);
			dlg.setMessage(R.string.form_level_alert_required_fields);
			dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					View view = controlsMap.get(e.getDataItem().getFieldName());
					view.requestFocus();
				}
			});
			dlg.show();
		}
	}

	/**
	 * Create the basic parameters to establish a connection
	 * @return
	 * @throws RequiredFieldException
	 */
	private List<NameValuePair> createParameters() throws RequiredFieldException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(item.getList().size());
		
		for(FormDataItem dataItem : item.getList()){
			View view = controlsMap.get(dataItem.getFieldName());
			final String paramValue = getControlText(view, dataItem);
			
			if(paramValue != null && paramValue.length() > 0){
		        nameValuePairs.add(new BasicNameValuePair(dataItem.getFieldName(), paramValue));
			}else{
				if(dataItem.isRequired())
					throw new RequiredFieldException(dataItem);
			}			
		}
		return nameValuePairs;
	}

	/**
	 * Sets the text characteristics depending on the particular type
	 * @param view
	 * @param dataItem
	 * @return String
	 */
	private String getControlText(View view, FormDataItem dataItem) {
		String ret = "";
		switch (dataItem.getType()) {
		case INPUT_TEXT:
		case INPUT_NUMBER:
		case INPUT_EMAIL:
		case INPUT_PHONE:
		case INPUT_PASSWORD:
			ret  = ((EditText)view).getText().toString();
			break;
		case INPUT_CHECK:
			ret  = ((CheckBox)view).isChecked() ? "true":"false";
			break;
		case INPUT_PICKER:
			ret = ((Spinner)view).getSelectedItem().toString();
			break; 
		default:
			break;
		}
		return ret;
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

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.form;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.FORM_ACTIVITY;
	}
}
