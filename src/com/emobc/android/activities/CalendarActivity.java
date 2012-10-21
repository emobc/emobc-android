/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CalendarActivity.java
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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.activities.generators.CalendarActivityGenerator;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.views.CalendarView;
import com.emobc.android.views.Cell;

/**
 * class that defines an activity of type CALENDAR_ACTIVITY, and 
 * initialize all screen menu and the screen rotations. In its 
 * method onCreate(), call its CalendarActivityGenerator generator class. 
 * @author Manuel Mora Cuesta
 * @author Jonatan Alcocer Luna
 * @see CalendarActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class CalendarActivity extends CreateMenus implements CalendarView.OnCellTouchListener {
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	CalendarView mView = null;
	TextView mHit;
	Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isEntryPoint=false;
        rotateScreen(this);
        
        ApplicationData applicationData = SplashActivity.getApplicationData();
		if(applicationData != null){
			Intent intent = getIntent();
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

    }

	@Override
	public void onTouch(Cell cell) {
		mView = (CalendarView)findViewById(R.id.calendarView);
		int year  = mView.getYear();
		int month = mView.getMonth();
		int day   = cell.getDayOfMonth();
		
		// FIX issue 6: make some correction on month and year
		if(cell instanceof CalendarView.GrayCell) {
			// oops, not pick current month...				
			if (day < 15) {
				// pick one beginning day? then a next month day
				if(month==11)
				{
					month = 0;
					year++;
				} else {
					month++;
				}
				
			} else {
				// otherwise, previous month
				if(month==0) {
					month = 11;
					year--;
				} else {
					month--;
				}
			}
		}
		//cell.setColourCell(Color.GREEN,cell.);
		month++; //Need to increase the number of month
		String dateKey;
		if (day<10){
			dateKey = "0"+day;
			
		}else{
			dateKey = ""+day;
		}
		if (month <10){
			dateKey = dateKey+"/0"+month;
		}else{
			dateKey = dateKey+"/"+month;
		}
		dateKey = dateKey+"/"+year;
		
		//Update the textView
		TextView tv = (TextView) findViewById(R.id.textViewDayEvents);
		tv.setText(getResources().getString(R.string.events_day)+dateKey);
		try {
			ApplicationData applicationData = ApplicationData.readApplicationData(this);
			if(applicationData != null){
				Intent intent = getIntent();
				NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
				CalendarActivityGenerator generator = (CalendarActivityGenerator) applicationData.getFromNextLevel(this, nextLevel);
				generator.updateList(this, dateKey);
				
			}
		} catch (InvalidFileException e) {
			Log.e("CalendartActivity", e.getLocalizedMessage());
	    	Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
        
	
	}

    
}
