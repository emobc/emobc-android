/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* VideoActivity.java
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

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.activities.generators.VideoActivityGenerator;
import com.emobc.android.menu.CreateMenus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/** 
 * Defines an activity of type VIDEO_ACTIVITY, and 
 * initialize all screen menu and the screen rotations. In its 
 * method onCreate(), call its VideoActivityGenerator generator class.
 *  
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @see VideoActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class VideoActivity extends CreateMenus {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4187653662439165263L;
	protected static final Context Activity = null;
    /** Called when the activity is ontefirst created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        boolean isEntryPoint=false;
        rotateScreen(this);
        
        ApplicationData applicationData = getApplicationData();
		if(applicationData != null){
			Intent intent = getIntent();
			isEntryPoint= (Boolean) intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			setCurrentNextLevel(NextLevel.SEARCH_NEXT_LEVEL);
			ActivityGenerator generator = applicationData.getFromNextLevel(this, nextLevel);
			generator.initializeActivity(this);

			setEntryPoint(isEntryPoint);
			createMenus();        
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}
		//createToolBar(isEntryPoint);
    }
}