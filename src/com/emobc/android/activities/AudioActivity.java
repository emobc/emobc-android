/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AudioActivity.java
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AudioActivityGenerator;
import com.emobc.android.menu.CreateMenus;

/**
 * Class that defines an activity of type <tt>AUDIO_ACTIVITY</tt>, and 
 * initialize all screen menu and the screen rotations.
 * @author Jorge E. Villaverde
 * @see AudioActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class AudioActivity extends CreateMenus {
	private AudioActivityGenerator generator;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
        boolean isEntryPoint = false;
        rotateScreen(this);
        
        ApplicationData applicationData = SplashActivity.getApplicationData();
		if(applicationData != null){
			Intent intent = getIntent();  
			isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			generator = (AudioActivityGenerator) applicationData.getFromNextLevel(this, nextLevel);
			generator.initializeActivity(this);
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}
		createMenus(this, isEntryPoint);
    }
    
    @Override
    public void onBackPressed() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Se cancelará la reproducción")
		       .setCancelable(false)
		       .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		       })
		       .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    return;
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	generator.finishAudio();
    	Log.i("Audio","onDestroy()");
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	Log.i("Audio","onStop()");
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	generator.pauseAudio();
    	Log.i("Audio","onPause()");
    }
}
