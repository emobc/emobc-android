/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* SaleActivity.java
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
import com.emobc.android.activities.generators.SaleActivityGenerator;
import com.emobc.android.menu.CreateMenus;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class SaleActivity extends CreateMenus {
	private SaleActivityGenerator generator;
	
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
			generator = (SaleActivityGenerator) applicationData.getFromNextLevel(this, nextLevel);
			generator.initializeActivity(this);
			setEntryPoint(isEntryPoint);
			createMenus(nextLevel.getLevelId());
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}
    }

}
