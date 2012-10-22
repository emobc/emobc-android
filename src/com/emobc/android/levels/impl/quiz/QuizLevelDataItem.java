/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* QuizLevelDataItem.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emobc.android.levels.AppLevelDataItem;

/**
 * Class for save information about Quiz. 
 * It helps for make a quiz in your application.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizLevelDataItem extends AppLevelDataItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1652883187842179071L;
	private Map<String, QuestionDataItem> questions = new HashMap<String, QuestionDataItem>();
	private String description;
	private boolean adventureMode = false;
	private int time;
	private String first;
	
	public boolean isAdventureMode() {
		return adventureMode;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getTime() {
		return time;
	}
	
	public QuestionDataItem getQuestion(String id){
		return questions.get(id);
	}
	
	/**
	 * Set the first question of the quiz. If you set a first question then the quiz actives
	 * adventure mode.
	 * @param id
	 */
	public void setFirst(String id){
		this.adventureMode = true;
		this.first = id;
	}
	
	/**
	 * Return the id of the first question of the quiz. Null if there isn't.
	 * 
	 * @return
	 */
	public String getFirst(){
		return first;
	}
	
	/**
	 * Return the number of question
	 * @return
	 */
	public int size(){
		return this.questions.size();
	}
	
	/**
	 * Return a list with the IDs of questions contained in the quiz
	 * @return
	 */
	public List<String> getQuestionIds(){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.addAll(questions.keySet());
		return toReturn;
	}
	/**
	 * Method for parseUtils. Cast the time to integer.
	 * @param time
	 */
	public void setTime(String time) {
		this.time = Integer.valueOf(time);
	}
	
	/**
	 * Method for parseUtils. Add new question. Question must have ID.
	 * @param questions
	 */
	public void addQuestion(QuestionDataItem question) {
		this.questions.put(question.getId(), question);
		
	}
}
