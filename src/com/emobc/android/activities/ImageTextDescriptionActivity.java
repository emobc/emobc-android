/*
 * Copyright © 2011-2012 Neurowork S.L.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emobc.android.activities;

import android.content.Intent;
import android.os.Bundle;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.ImageTextDescriptionActivityGenerator;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.menu.SystemAction;

/** 
* Defines an activity of type IMAGE_TEXT_DESCRIPTION_ACTIVITY, and 
* initialize all screen menu and the screen rotations. In its 
* method onCreate(), call its ImageTextDescriptionActivityGenerator generator.
* 
* @author Jonatan Alcocer Luna
* @author Jorge E. Villaverde
*/
public class ImageTextDescriptionActivity extends CreateMenus 
	implements ContentAwareActivity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7730914120122780854L;
	/** Called when the activity is first created. */
	
	private ImageTextDescriptionActivityGenerator generator;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isEntryPoint=false;
        rotateScreen(this);
        
        ApplicationData applicationData = getApplicationData();
        
		if(applicationData != null){
			Intent intent = getIntent();
			isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			setCurrentNextLevel(nextLevel);
			this.generator = (ImageTextDescriptionActivityGenerator)applicationData.getFromNextLevel(this, nextLevel);			
			this.generator.initializeActivity(this);
			setEntryPoint(isEntryPoint);
			createMenus();
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}
    }

	@Override
	public String getActivityContent(SystemAction systemAction) {
		if(generator == null)
			return null;
		
		return generator.getItem().getText();		
	}    

}
