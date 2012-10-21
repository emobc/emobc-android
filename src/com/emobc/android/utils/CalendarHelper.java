/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CalendarHelper.java
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
package com.emobc.android.utils;

import android.content.Context;

import com.emobc.android.activities.R;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class CalendarHelper {
	private Context context;
	
	/**
	 * Convert an integer in a String of the Month
	 * @param month 1-12
	 */
	public CalendarHelper (Context c){
		this.context=c;
	}
	
	public String getMonthInString(int month){
		switch (month){
		case 1:
			return (context.getResources().getString(R.string.month1));
		case 2:
			return (context.getResources().getString(R.string.month2));
		case 3:
			return (context.getResources().getString(R.string.month3));
		case 4:
			return (context.getResources().getString(R.string.month4));
		case 5:
			return (context.getResources().getString(R.string.month5));
		case 6:
			return (context.getResources().getString(R.string.month6));
		case 7:
			return (context.getResources().getString(R.string.month7));
		case 8:
			return (context.getResources().getString(R.string.month8));
		case 9:
			return (context.getResources().getString(R.string.month9));
		case 10:
			return (context.getResources().getString(R.string.month10));
		case 11:
			return (context.getResources().getString(R.string.month11));
		case 12:
			return (context.getResources().getString(R.string.month12));
		default:
			return "Error";
		}
		
	}
}
