/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* EventDataItem.java
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
package com.emobc.android.levels.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.emobc.android.NextLevel;

import android.util.Log;


/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class EventDataItem implements Comparable<EventDataItem> {
	private String eventDate;
	private GregorianCalendar cal;
	private String title;
	private String description;
	private NextLevel nextLevel;
	private int hour;
	private int min;
	
	public NextLevel getNextLevel() {
		return nextLevel;
	}
	public void setNextLevel(NextLevel nextLevel) {
		this.nextLevel = nextLevel;
	}
	
	//Date
	/**
	 * Return a string of the date: dd/mm/yy
	 * @return
	 */
	public String getEventDate() {
		return eventDate;
	}

	public int getDayOfMonth(){	
		return this.cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);	
	}
	
	public int getMonth(){	
		return this.cal.get(Calendar.MONTH);	
	}
	
	public int getYear(){	
		return this.cal.get(Calendar.YEAR);	
	}
	
	/**
	 * This method configures the date of the event
	 * @param eventDate It's needed the format: dd/mm/yy
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
		
		//parse the date string
		try{
			StringTokenizer t = new StringTokenizer(eventDate,"/");
			int day = Integer.parseInt(t.nextToken());
			int month = Integer.parseInt(t.nextToken());
			int year = Integer.parseInt(t.nextToken());
			this.cal = new GregorianCalendar(year,month,day);
		}catch (Exception e){
			Log.e("EventDataItem", "Error parsing the event date");
			Log.e("EventDataItem", e.getLocalizedMessage());
		}
		
				
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * This method configures the date of the event
	 * @param eventTime It's needed the format: dd/mm/yy
	 */
	public void setTime(String eventTime) {
		//parse the date string
		try{
			StringTokenizer t = new StringTokenizer(eventTime,":");
			setHour(Integer.parseInt(t.nextToken()));
			setMin(Integer.parseInt(t.nextToken()));
		}catch (Exception e){
			Log.e("EventDataItem", "Error parsing the event date");
			Log.e("EventDataItem", e.getLocalizedMessage());
		}

	}
	/**
	 * Return a string of the time
	 * @return time format: 23:59
	 */
	public String getTime(){
		String hour;
		if (this.hour<10){
			hour = "0"+String.valueOf(this.hour);
		}else{
			hour = String.valueOf(this.hour);
		}
		String min;
		if (this.min<10){
			min = "0"+String.valueOf(this.min);
		}else{
			min = String.valueOf(this.min);
		}
		return hour+":"+min;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	@Override
	public int compareTo(EventDataItem another) {
		Integer h = Integer.valueOf(hour);
		int toReturn = 0;
		toReturn = h.compareTo(another.getHour());
		if (toReturn==0){
			Integer m = Integer.valueOf(min);
			toReturn = m.compareTo(another.getHour());
		}
		
		return toReturn;
	}
	
	
}
