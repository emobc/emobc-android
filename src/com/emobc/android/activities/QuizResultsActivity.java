/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuizResultsActivity.java
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
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emobc.android.levels.impl.quiz.QuizController;
import com.emobc.android.levels.impl.quiz.QuizResults;
import com.emobc.android.menu.CreateMenus;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizResultsActivity extends CreateMenus {
	private QuizController quizController;
	private QuizResults results;
	//timer for the progress bar
    Handler handler = new Handler();
    
    Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			updateScoreBar();
			
			//scoreText.setText(String.valueOf(results.getScore()*1000)+"/"+results.getTotalWeight());
			
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_results_layouts);
        
        createBanner();
        
        Intent intent = getIntent();
        this.quizController = (QuizController)intent.getSerializableExtra(QuizController.QUIZ_CONTROLLER_TAG);
        if(this.quizController != null){
	        this.results = this.quizController.getResult();
	        Button ok = (Button) findViewById(R.id.ok);
	        ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
					
				}
			});
	        Button share = (Button) findViewById(R.id.share);
	        share.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					share();
					
				}
			});
	        runnable.run();
        }        
    }
    
    private void updateScoreBar(){
    	ProgressBar score = (ProgressBar) findViewById(R.id.score);
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        score.setMax(results.getTotalWeight()*1000);
    	if(score.getProgress()<results.getScore()*1000){
			score.incrementProgressBy((int) (results.getScore()*1000/10));
			scoreText.setText(String.valueOf(score.getProgress())+"/"+results.getTotalWeight()*1000);
		}else{
			scoreText.setText(String.valueOf((int)results.getScore()*1000)+"/"+results.getTotalWeight()*1000);
		}
    	handler.postDelayed(runnable, 100);
    }
    
    private void share(){
    	Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "He conseguido una puntuación de "+results.getScore()*1000+"en " +
        		"el quiz. ¡Intenta superarlo!");

        startActivity(Intent.createChooser(intent, "Compartir"));

    }

    
}
