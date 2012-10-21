/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageZoomActivityGenerator.java
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.ImageLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "IMAGE_ZOOM". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageZoomActivityGenerator extends LevelActivityGenerator {

	public ImageZoomActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final ImageLevelDataItem item = (ImageLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		if(Utils.hasLength(item.getImageFile())){
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(activity, item.getImageFile());
				ImageView planoImage = (ImageView)activity.findViewById(R.id.planoImage);
				planoImage.setImageDrawable(drawable);
				TextView head = (TextView)activity.findViewById(R.id.header);
		        head.setText(item.getHeaderText());
				

				/* LinearLayout mainLayout = (LinearLayout)activity.findViewById(R.id.imageMap);	
				ZoomImageView imageView = new ZoomImageView(activity, drawable);
				
				MarginLayoutParams params = new MarginLayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				params.setMargins(15, 5, 14, 0);
				imageView.setLayoutParams(params);
				
				mainLayout.addView(imageView); */

			} catch (InvalidFileException e) {
				Log.e("AppCoverData", e.getLocalizedMessage());
		    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}			
		}		
	}

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.image_zoom;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.IMAGE_ZOOM_ACTIVITY;
	}
}
