/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuestionDataItem.java
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
import java.util.List;


/**
 * Class for save information about quiz questions
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuestionDataItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 533093280057664189L;
	//Attributes
	private String id;
	//Question
	private String text;
	//Weight
	private int weight;
	//Answers
	private List<QuizAnswerDataItem> answers;
	//Image
	private String image;
	
	
	public QuestionDataItem(){
		this.answers = new ArrayList<QuizAnswerDataItem>();
		this.weight = 1;
	}
	
	
	public String getId() {
		return id;
	}

	/**
	 * Method for parseUtils
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}


	public void setText(String text){
		this.text= text;
	}
	
	public String getText() {
		return text;
	}

	 
	public int getWeight() {
		return weight;
	}
	
	/**
	 * Method for parseUtils. Cast the String to int. Set the answer weight. 1 by default. 
	 * @param weight
	 */
	public void setWeight(String weight) {
		this.weight = Integer.valueOf(weight);
	}
	/**
	 * Return an the answer list
	 * @return 
	 */
	public List<QuizAnswerDataItem> getAnswers() {
		return this.answers;
	}
	/**
	 * Method for parseUtils. Set a new answer for the question
	 * @param answer
	 */
	public void setAnswer(QuizAnswerDataItem answer) {
		this.answers.add((QuizAnswerDataItem) answer);
	}
	
	/**
	 * Return the image path of the question
	 * @return The path or url of the image.
	 */
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	

}
