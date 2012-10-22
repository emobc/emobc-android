/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* Results.java
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

/**
 * Class for save and show quiz results.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizResults implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2562040949530110518L;
	//private boolean penalty;
	private double penaltyPor;
	private int corrects;
	private int incorrects;
	private float score;
	private int totalWeight;
	
	/**
	 * Constructor for default penalty Results objects
	 */
	public QuizResults(){
		//this.penalty=false;
		this.corrects=0;
		this.incorrects=0;
		this.totalWeight = 0;
		this.score=0;
	}
	/**
	 * Constructor
	 * @param penalty Penalty percentage. From 0 to 1. Default value = 0.25
	 */
	public QuizResults(double penalty){
		//this.penalty = true;
		if (penalty<0||penalty>1){
			this.penaltyPor = 0.25;
		}else{
			this.penaltyPor = penalty;
		}
		this.corrects=0;
		this.incorrects=0;
		this.totalWeight = 0;
	}
	
	/**
	 * Set the result of a quiz answer
	 * @param weight Weight of the quiz answer
	 * @param correct If the answer was correct or incorrect
	 */
	public void setAnswer(int weight, boolean correct){
		if (correct){
			this.corrects++;
			score = score + weight;
		}else{
			this.incorrects++;
			score = (float) (score - (this.penaltyPor*weight));
		}
		totalWeight = totalWeight + weight;
	}
	
	/**
	 * Return how many questions the Results class have.
	 * @return Total number of questions
	 */
	public int getTotalQuestions() {
		return corrects+incorrects;
	}
	
	/**
	 * Set the total weight of your quiz for calculate the correct results.
	 * You should call this method before call getResults() for receiving a correct result.
	 * 
	 * 
	 * @param totalQuestions
	 */
	public void setTotalWeight(int totalWeight) {
		if (totalWeight>this.totalWeight){
			this.totalWeight = totalWeight;
		}
		
	}
	
	/**
	 * Return results of your quiz. You should call this method after finish your quiz.
	 * If you call getResults() without setting the total question number, you will receive
	 * the average supposing the total questions number is the total questions answered.
	 * 
	 * @return A four components vector with results. The first component is the final score 
	 * based on the correct or incorrect questions and their weight and penalties.
	 * The second component is the total Weight. The third component is the number of 
	 * correct answers. And the fourth component is is the number of incorrect answers
	 * 
	 */
	public float[] getResults(){
		float[] toReturn = new float[4];
		
		toReturn[0] = this.score;
		toReturn[1] = this.totalWeight;
		toReturn[2] = (float) corrects;
		toReturn[3] = (float) incorrects;
		
		return toReturn;
	}
	
	/**
	 * Return the total weight of all quiz questions.
	 * @return
	 */
	public int getTotalWeight() {
		return totalWeight;
	}
	
	/**
	 * Return the final score based on the correct or incorrect questions 
	 * and their weight and penalties.
	 * 
	 * @return
	 */
	public float getScore() {
		return score;
	}
	
	
}
