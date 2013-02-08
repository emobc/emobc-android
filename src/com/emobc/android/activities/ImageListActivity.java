/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageListActivity.java
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
import android.os.Bundle;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.activities.generators.ImageListActivityGenerator;
import com.emobc.android.activities.generators.ListActivityGenerator;
import com.emobc.android.levels.impl.ListItemDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.menu.SystemAction;

/** 
 * Defines an activity of type IMAGE_LIST_ACTIVITY, and 
 * initialize all screen menu and the screen rotations. In its 
 * method onCreate(), call its ImageListActivityGenerator generator class. 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ImageListActivity extends CreateMenus 
	implements ContentAwareActivity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8411848964108255329L;
	private ActivityGenerator generator;
	/** 
	 * Called when the activity is first created. 
	 **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
        Boolean isEntryPoint = null;
        rotateScreen(this);
        
        ApplicationData applicationData = getApplicationData();
        
		if(applicationData != null){
			Intent intent = getIntent();  
			isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			setCurrentNextLevel(nextLevel);
			this.generator = applicationData.getFromNextLevel(this, nextLevel);
			this.generator.initializeActivity(this);
			setEntryPoint(isEntryPoint == null ? false : isEntryPoint.booleanValue());
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
		
		StringBuilder builder = new StringBuilder();
		String sep = "";
		
		if (generator instanceof ListActivityGenerator) {
			ListActivityGenerator lag = (ListActivityGenerator) generator;
			for(ListItemDataItem item : lag.getItem().getList()){
				builder.append(sep);
				builder.append(item.getText());
				sep = "\n";
			}			
		}else if (generator instanceof ImageListActivityGenerator){
			ImageListActivityGenerator ilag = (ImageListActivityGenerator) generator;
			for(ListItemDataItem item : ilag.getItem().getList()){
				builder.append(sep);
				builder.append(item.getText());
				sep = "\n";
			}			
		}
		
		return builder.toString();
	}

}
