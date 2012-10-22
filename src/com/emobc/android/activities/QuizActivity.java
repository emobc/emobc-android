/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuizActivity.java
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.levels.impl.quiz.QuizController;
import com.emobc.android.levels.impl.quiz.QuizLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.InvalidFileException;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 */
public class QuizActivity extends CreateMenus {
	private QuizController quizController;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        try {
        	boolean isEntryPoint = false;
        	ApplicationData applicationData = ApplicationData.readApplicationData(this);
			if(applicationData != null){
				Intent intent = getIntent();
				isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
				isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
				NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
				ActivityGenerator generator = applicationData.getFromNextLevel(this, nextLevel);
				generator.initializeActivity(this);
				
			}else{
				Intent i = new Intent (this, SplashActivity.class);
				startActivity(i);
				finish();
			}
	        createMenus(this, isEntryPoint);
			
		} catch (InvalidFileException e) {
			Log.e("QuizActivity", e.getLocalizedMessage());
	    	Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
        
        Button start = (Button) findViewById(R.id.startQuizButton);
        
        start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent launchActivity = new Intent (getApplicationContext(), QuizQuestionsActivity.class);
				launchActivity.putExtra(QuizController.QUIZ_CONTROLLER_TAG, quizController);	
				startActivity(launchActivity);
			}
		});
    }
    
    public void setQuiz(QuizLevelDataItem quiz){
    	this.quizController = new QuizController();
    	quizController.addQuiz(quiz);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure the request was successful
        if (resultCode == RESULT_OK) {
        	Intent launchActivity = new Intent (this,QuizResultsActivity.class);
        	launchActivity.putExtra(QuizController.QUIZ_CONTROLLER_TAG, quizController);
        	startActivity(launchActivity);
         }
    }
}
