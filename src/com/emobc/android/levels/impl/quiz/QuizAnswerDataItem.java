/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AnswerDataItem.java
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
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class QuizAnswerDataItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8813047025399307726L;
	private String AnsText;
	private String next;
	private boolean correct;
	
	
	public String getNext() {
		return next;
	}
	
	/**
	 * Method for ParseUtils. Converts from String to int
	 * @param next
	 */
	public void setNext(String next) {
		this.next = next;
	}
	public String getAnsText() {
		return AnsText;
	}
	public void setAnsText(String ansText) {
		AnsText = ansText;
	}
	public boolean isCorrect() {
		return correct;
	}
	
	/**
	 * Method for parseUtils. Cast to boolean
	 * @param correct
	 */
	public void setCorrect(String correct) {
		this.correct = Boolean.valueOf(correct);
	}
}
