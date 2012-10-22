/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuizActivityGenerator.java
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
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.QuizActivity;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.quiz.QuizLevelDataItem;
import com.emobc.android.menu.CreateMenus;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "QUIZ". It also creates the menus, rotations, and the format for 
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizActivityGenerator extends LevelActivityGenerator {

	public QuizActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(Activity activity, AppLevelData data) {
		final QuizLevelDataItem  item = (QuizLevelDataItem) data.findByNextLevel(nextLevel);
		
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		((QuizActivity)activity).setQuiz(item);
		TextView description = (TextView) activity.findViewById(R.id.quizDescription);
		description.setText(item.getDescription());
	}

	@Override
	protected int getContentViewResourceId(Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.quiz_layout;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.QUIZ_ACTIVITY;
	}
}
