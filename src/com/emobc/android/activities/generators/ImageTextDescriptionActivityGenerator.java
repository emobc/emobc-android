/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageTextDescriptionActivityGenerator.java
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

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.ImageTextDescriptionLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "IMAGE_TEXT_DESCRIPTION". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageTextDescriptionActivityGenerator extends LevelActivityGenerator {
	
	public ImageTextDescriptionActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id > 0)
				return id;
		}
		return R.layout.image_text_descr;
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final ImageTextDescriptionLevelDataItem item = (ImageTextDescriptionLevelDataItem)data.findByNextLevel(nextLevel);
		
		if(item == null){
			showAlerDialog(activity, "No se ha encontrado el NextLevel:\n" + nextLevel.toString());
		}else{
			//Typeface ubuntu = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/Ubuntu-Bold.ttf");
			//Typeface arial = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/Arial.ttf");
			
			initializeHeader(activity, item);
			
			//Create Banner
			CreateMenus c = (CreateMenus)activity;
			c.createBanner();
			
			Display display = activity.getWindowManager().getDefaultDisplay();
			int height = display.getHeight();

			if(Utils.hasLength(item.getImageFile())){
				Drawable drawable;
				try {
					drawable = ImagesUtils.getDrawable(activity, item.getImageFile());
					ImageView descrImaga = (ImageView)activity.findViewById(R.id.descr_image);
					descrImaga.setImageDrawable(drawable);
				} catch (InvalidFileException e) {
					Log.e("AppCoverData", e.getLocalizedMessage());
			    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
				}			
			}
			if(Utils.hasLength(item.getText())){
				TextView descrText = (TextView)activity.findViewById(R.id.basic_text);
	            descrText.setText(item.getText(), BufferType.EDITABLE); 
				//descrText.setTypeface(arial);
	            if(height > 533){
					ScrollView sv = (ScrollView)activity.findViewById(R.id.text_scroll);
					LayoutParams params = sv.getLayoutParams();
					params.height += 35;
					sv.setLayoutParams(params);
				}			
			}
			
	        LinearLayout leerMasBtn =(LinearLayout) activity.findViewById(R.id.descr_mas);
	        leerMasBtn.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View view) {
					showNextLevel(activity, item.getNextLevel());
		        }
	        });
	        leerMasBtn.setBackgroundResource(R.drawable.mas_selector);		
		}		
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.IMAGE_TEXT_DESCRIPTION_ACTIVITY;
	}
}
