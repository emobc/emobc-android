/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* RequiredFieldException.java
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
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.VideoLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.Utils;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "VIDEO". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class VideoActivityGenerator extends LevelActivityGenerator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4156983446642925701L;

	public VideoActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final VideoLevelDataItem item = (VideoLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		if(Utils.hasLength(item.getVideoPath())){
			if(Utils.isUrl(item.getVideoPath())){
				activity.startActivity(new Intent(
						Intent.ACTION_VIEW, 
						Uri.parse(item.getVideoPath())));
				activity.finish();
			}else{
				Log.d("URL",item.getVideoPath());
				VideoView video = (VideoView)activity.findViewById(R.id.video);
				video.setVideoURI(Uri.parse(item.getVideoPath()));
				MediaController mc = new MediaController(activity);
				video.setMediaController(mc);
				video.requestFocus();
				video.start();
				mc.show();
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
		return R.layout.video_only;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.VIDEO_ACTIVITY;
	}
}
