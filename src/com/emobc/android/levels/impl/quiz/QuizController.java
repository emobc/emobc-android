/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuizController.java
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
package com.emobc.android.levels.impl.quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import android.util.Log;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7877510063743276015L;

	public static final String QUIZ_CONTROLLER_TAG = "_QUIZ_CONTROLLER_TAG_";
	
	/*--------- Attributes ----------*/
	//Quiz
	private List<QuizLevelDataItem> data;
	private int quizIndex; //Index of quiz
	private QuizLevelDataItem actualQuiz;
	//Questions
	private List<String> ids;//ids of quiz questions
	private int questionIndex; //Index of question
	private QuestionDataItem currQuestion;
	private String idNextQuestion;
	private List<QuizAnswerDataItem> currAnswers;
	private int currAnswer; //Current answer selected
	//Results
	private QuizResults results = new QuizResults(0);

	/*------- Constructor --------*/
	public QuizController(){
		data = new ArrayList<QuizLevelDataItem>();
	}
	/*--------- Methods ----------*/
	public void addQuiz(QuizLevelDataItem quiz){
		data.add(quiz);
	}
	/**
	 * Starts the quiz. Must be called before start your quiz. Then
	 * you can call other methods like getQuestion for get the actual question.
	 * 
	 * @param time Time for make the quiz, in seconds
	 */
	public void start(int time){
		this.quizIndex=0;
		initQuiz(data.get(quizIndex));
		if (actualQuiz.isAdventureMode()){
			this.results = new QuizResults(0);
		}
		
	}
	
	/**
	 * Initialize the Quiz Controller to start the quiz
	 * @param quiz The quiz to be started by the Quiz Controller
	 */
	private void initQuiz(QuizLevelDataItem quiz){
		this.actualQuiz = quiz;
		this.questionIndex = 0;
		if(actualQuiz.isAdventureMode()){
			idNextQuestion = actualQuiz.getFirst();
			this.currQuestion = actualQuiz.getQuestion(this.idNextQuestion);
		}else{	
			ids = actualQuiz.getQuestionIds();
			Collections.shuffle(ids);//Shuffle questions
			this.currQuestion = actualQuiz.getQuestion(ids.get(questionIndex));
		}
		if(currQuestion != null){
			this.currAnswers = currQuestion.getAnswers();
			Collections.shuffle(currAnswers);
		}else{
			Log.w("QuizController", "No se ha encontrado la pregunta para el Quiz");
		}
	}
	
	/**
	 * Return if there is more questions available. If not, you should call finish() method
	 * for finish the quiz and confirm the last answer.
	 * @return True if there are more questions. False if the actual question is the last one
	 */
	public boolean hasNext(){
		if (hasNextQuestionActualQuiz()){
			return true;
		}else{
			return hasNextQuiz();
		}
	}
	
	/**
	 * Return if there are more question in the actual quiz
	 * @return
	 */
	private boolean hasNextQuestionActualQuiz(){
		if (actualQuiz.isAdventureMode()){
			return idNextQuestion!=null;
		}else{
			return this.questionIndex<actualQuiz.size()-1;
		}	
	}
	
	/**
	 * Return if there are more quiz after the current quiz
	 * @return
	 */
	private boolean hasNextQuiz(){
		return this.quizIndex<data.size()-1;
	}
	
	/**
	 * Skip to the next quiz and initialize the controller for start it.
	 */
	private void nextQuiz(){
		this.quizIndex++;
		initQuiz(data.get(quizIndex));
	}
	
	/**
	 * Skips to the next quiz question and confirms your last answer set with
	 * setAnswer() method.  
	 * You should be call this method after checking with hasNext method.
	 * If the quiz is an adventure, you must use setAnswer before call this 
	 * method.
	 */
	public void next(){
		//Saves answer results
		QuizAnswerDataItem answer = currQuestion.getAnswers().get(currAnswer);
		if (answer.isCorrect()){
			this.results.setAnswer(currQuestion.getWeight(), true);
		}else{
			this.results.setAnswer(currQuestion.getWeight(), false);
		}
		
		//Skips to the next quiz question
		if (hasNextQuestionActualQuiz()){
			nextQuestionActualQuiz();
		}else{
			nextQuiz();
		}
	}
	
	/**
	 * Skips to the next quiz question in current quiz depending on the
	 * quiz mode: quiz or adventure and save the answer results. 
	 */
	private void nextQuestionActualQuiz(){	
		if (actualQuiz.isAdventureMode()){
			this.currQuestion = actualQuiz.getQuestion(this.idNextQuestion);
		}else{
			this.questionIndex++;
			this.currQuestion = actualQuiz.getQuestion(ids.get(questionIndex));
		}
		this.currAnswers = currQuestion.getAnswers();
		Collections.shuffle(currAnswers);
	}
	
	/**
	 * Return the actual question
	 * @return
	 */
	public String getQuestion(){
		return currQuestion.getText();	
	}
	
	/**
	 * Return the actual question image
	 * @return The path or url of the image.
	 */
	public String getImage(){
		return currQuestion.getImage();
	}
	
	/**
	 * Return a list of String with answers of actual question
	 * @return
	 */
	public List<String> getAnswers(){
		Iterator<QuizAnswerDataItem> i = currAnswers.iterator();
		List<String> toReturn = new ArrayList<String>();
		while (i.hasNext()){
			QuizAnswerDataItem actual = (QuizAnswerDataItem) i.next();
			toReturn.add(actual.getAnsText());
		}
		return toReturn;
	}
	
	/**
	 * Set the answer for the actual question with his index. You must call this method
	 * before call next() if your quiz is an adventure. For confirm your answer you 
	 * have to call next() method later.
	 * 
	 * @param index Answer index from 0 to getAnswers().size()-1;
	 */
	public void setAnswer(int index){
		currAnswer = index;
		QuizAnswerDataItem answer = currQuestion.getAnswers().get(currAnswer);
		this.idNextQuestion = answer.getNext(); 
	}
	
	
	/**
	 * Return the actual quiz results. You can call this method anytime.
	 * 
	 * @return
	 */
	public QuizResults getResult(){
		return this.results;
	}
	
	/**
	 * Call this method for finish the quiz. It's needed to confirm the 
	 * last answer  which you set with setAnswer method.
	 */
	public void finish(){
		//Saves answer results
		QuizAnswerDataItem answer = currQuestion.getAnswers().get(currAnswer);
		if (answer.isCorrect()){
			this.results.setAnswer(currQuestion.getWeight(), true);
		}else{
			this.results.setAnswer(currQuestion.getWeight(), false);
		}
	}
}
