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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.QrActivityGenerator;
import com.emobc.android.levels.impl.DefaultAppLevelData;
import com.emobc.android.levels.impl.QrLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/** 
* Class that defines an activity of type QR_ACTIVITY, and 
* initialize all screen menu and the screen rotations. In its 
* method onCreate(), call its QrActivityGenerator generator class. 
* @author Jonatan Alcocer Luna
* @author Jorge E. Villaverde
*/
public class QrActivity extends CreateMenus {
	private QrLevelDataItem dataItem;
	
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
			
			QrActivityGenerator generator = (QrActivityGenerator)applicationData.getFromNextLevel(this, nextLevel);
			DefaultAppLevelData appLevelData = (DefaultAppLevelData) generator.getAppLevel().getData(this);
			dataItem = (QrLevelDataItem)appLevelData.findByNextLevel(nextLevel);
			
			generator.initializeActivity(this);
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}
      //createToolBar(isEntryPoint);
		createMenus(this, isEntryPoint);
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(dataItem == null)
			return;
		
		if(resultCode == Activity.RESULT_OK){
			IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if (scanResult != null) {
				String qrCode = scanResult.getContents();
				NextLevel nl = dataItem.getNextLevel(qrCode);
				if(nl != null){
					showNextLevel(this, nl);
				}
			}
		}
	}
}
