/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MapsActivity.java
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

import java.util.Iterator;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.FormActivityGenerator;
import com.emobc.android.activities.generators.ProfileActivityGenerator;
import com.emobc.android.menu.CreateMenus;

/**
 * Defines an activity of type FORM_ACTIVITY. In its 
 * method onCreate(), call its MapActivityGenerator generator. 
 * 
 * @author Jorge E. Villaverde
 * @see FormActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class FormActivity extends CreateMenus {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4153042867395757676L;
	public static final String PREFS_NAME = "FormPrefsFile";
	public static final int PICK_IMAGE_REQUEST = 1;
	private Map<String,View> controlsMap;
	private FormActivityGenerator generator;
	private ProfileActivityGenerator profileGenerator;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
	    boolean isEntryPoint = false;
	    Boolean isProfile = null;
	    rotateScreen(this);
	    generator = null;
	    
	    ApplicationData applicationData = getApplicationData();
	    
		if(applicationData != null){
			Intent intent = getIntent();  
			isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			setCurrentNextLevel(nextLevel);
			isProfile = NextLevel.PROFILE_NEXT_LEVEL.equals(nextLevel);
			if (Boolean.TRUE.equals(isProfile)) {
				this.profileGenerator = (ProfileActivityGenerator)applicationData.getFromNextLevel(this, nextLevel);
				profileGenerator.initializeActivity(this);
			} else {
				this.generator = (FormActivityGenerator)applicationData.getFromNextLevel(this, nextLevel);
				generator.initializeActivity(this);
				setEntryPoint(isEntryPoint);
				createMenus();
			}
			
			if (savedInstanceState!=null){
				restoreAllInstances(savedInstanceState);
			}
	       // Restore preferences
	       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	       restoreAllInstances(settings);
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}		
    }
    
    private void restoreAllInstances(SharedPreferences settings) {
    	Iterator<String> i = controlsMap.keySet().iterator();
		while (i.hasNext()){
			String key = i.next();
			View v = controlsMap.get(key);
			try{
				if (v instanceof Spinner){
					((Spinner) v).setSelection(settings.getInt(key,0));
	            }else if (v instanceof CheckBox){
	            	((CheckBox) v).setChecked(settings.getBoolean(key,false));
	            }else if (v instanceof EditText){
	            	((EditText)v).setText(settings.getString(key,""));
	            }
			}catch(ClassCastException e){
				Log.e("FormActivity", e.getMessage());
			}
			
		}
		
	}

	/**
     * Saving data
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      saveAllInstances(savedInstanceState);
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        saveAllInstances(settings);
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

	private void saveAllInstances(Bundle savedInstanceState) {
		Iterator<String> i = controlsMap.keySet().iterator();
		while (i.hasNext()){
			String key = i.next();
			View v = controlsMap.get(key);
			if (v instanceof Spinner){
            	savedInstanceState.putInt(key,((Spinner) v).getSelectedItemPosition());
            }else if (v instanceof CheckBox){
            	savedInstanceState.putBoolean(key,((CheckBox) v).isChecked());
            }else if (v instanceof EditText){
            	savedInstanceState.putString(key, ((EditText)v).getText().toString());
            }
			
		}
		
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  restoreAllInstances(savedInstanceState);
	}

	private void restoreAllInstances(Bundle savedInstanceState) {
		Iterator<String> i = controlsMap.keySet().iterator();
		while (i.hasNext()){
			String key = i.next();
			View v = controlsMap.get(key);
			if (v instanceof Spinner){
				((Spinner) v).setSelection(savedInstanceState.getInt(key));
            }else if (v instanceof CheckBox){
            	((CheckBox) v).setChecked(savedInstanceState.getBoolean(key));
            }else if (v instanceof EditText){
            	((EditText)v).setText(savedInstanceState.getString(key));
            }
			
		}
	}
	public void setControlsMap (Map<String,View> controlsMap){
		this.controlsMap = controlsMap;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE_REQUEST) {
			if (resultCode == RESULT_OK) {
				// A contact was picked.  Here we will just display it
				// to the user.
				String imageContent = data.getDataString();
				generator.setImageContent(imageContent);
			}
		}		
	}
}
