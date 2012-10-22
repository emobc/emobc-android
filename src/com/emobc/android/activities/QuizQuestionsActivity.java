/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuizQuestionsActivity.java
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

import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.emobc.android.levels.impl.quiz.QuizController;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizQuestionsActivity extends CreateMenus {
	
	private QuizController quizController;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_questions_layout);
        
        createBanner();

        Intent intent = getIntent();
        this.quizController = (QuizController)intent.getSerializableExtra(QuizController.QUIZ_CONTROLLER_TAG);
        if(this.quizController != null){        
	        quizController.start(0);
	        
	        updateQuestion();
	        Button next = (Button) findViewById(R.id.nextQuizButton);
	        
	        next.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					checkAnswer();
				}
			});
        }
    }
    @Override
    public void onBackPressed() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.quiz_alert_exit))
		       .setCancelable(false)
		       .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                finish();
		           }
		       })
		       .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    return;
    }
    
    /**
     * Checks if there is an answer selected
     */
    protected void checkAnswer(){
    	RadioGroup rGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
    	int idCheck = rGroup.getCheckedRadioButtonId();
		if (idCheck!=-1){
			quizController.setAnswer(idCheck);
			rGroup.clearCheck();
			nextQuestion();
		}else{
			//No radio button checked
			showAlertDialog(getResources().getString(R.string.quiz_alert_no_answer));
			
		}
    }
    
    /**
     * Updates the activity with next questions
     */
    protected void nextQuestion(){
    	if (quizController.hasNext()){
    		quizController.next();
    		updateQuestion();
    	}else{
    		quizController.finish();
    		setResult(Activity.RESULT_OK);
    		Intent launchActivity = new Intent (this,QuizResultsActivity.class);
        	launchActivity.putExtra(QuizController.QUIZ_CONTROLLER_TAG, quizController);    		
        	startActivity(launchActivity);
    		finish();
    		this.finish();
    	}
    			
    }
    
    /**
     * Shows a dialog to inform that no answer selected
     */
    protected void showAlertDialog(String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                nextQuestion();
		           }
		       })
		       .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();	
	}
	
	/**
	 * Write the question information in the layout
	 */
	private void updateQuestion(){
		String currQuestion = quizController.getQuestion();
		
		//Image
		ImageView image = (ImageView) findViewById(R.id.questionImage);
		Drawable d;
		if (quizController.getImage()!=null){
			try {
				d = ImagesUtils.getDrawable(getApplicationContext(), quizController.getImage());
				image.setImageDrawable(d);
				image.setVisibility(ImageView.VISIBLE);
			} catch (InvalidFileException e) {
				e.printStackTrace();
			}
		}else{
			//Clear ImageView
			image.setImageDrawable(null);
		}
		
		
		//Write question
        TextView question = (TextView) findViewById(R.id.questionText);
        question.setText(currQuestion);
        
        //RadioGroup
        RadioGroup ansRadioGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        ansRadioGroup.removeAllViews();
        //Answers
        Iterator<String> i = quizController.getAnswers().iterator();
        int idButton = 0;
        while (i.hasNext()){
        	RadioButton r = new RadioButton(getApplicationContext());
        	String a = i.next();
        	r.setText(a);
        	r.setId(idButton);	
        	ansRadioGroup.addView(r);
        	idButton++;
        }
	}

    
}
