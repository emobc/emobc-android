/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CalendarActivityGenerator.java
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
package com.emobc.android.activities.generators;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.CalendarLevelDataItem;
import com.emobc.android.levels.impl.EventDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.CalendarHelper;
import com.emobc.android.views.CalendarView;
import com.emobc.android.views.CalendarView.OnCellTouchListener;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "CALENDAR". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Manuel Mora Cuesta
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class CalendarActivityGenerator extends LevelActivityGenerator {

	public CalendarActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(Activity activity, AppLevelData data) {
		final CalendarLevelDataItem item = (CalendarLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();	
		
		
		final CalendarView cv = (CalendarView)activity.findViewById(R.id.calendarView);
		final TextView tv = (TextView) activity.findViewById(R.id.textViewMonth);
		final CalendarHelper h = new CalendarHelper(activity);
		tv.setText(h.getMonthInString(cv.getMonth()+1));
		//Lista de eventos del calendario
		if (!item.getEvents().isEmpty()){
			cv.setEvents(item.getEvents());	
		}
		cv.setOnCellTouchListener((OnCellTouchListener) activity);
		
		Button prev = (Button) activity.findViewById(R.id.prevMonthButton);
		Button next = (Button) activity.findViewById(R.id.nextMonthButton);
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cv.previousMonth();	
				tv.setText(h.getMonthInString(cv.getMonth()+1));
			}
		});
		next.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				cv.nextMonth();
				tv.setText(h.getMonthInString(cv.getMonth()+1));
			}
		});
		//Getting today
		int day = cv.getDate().get(Calendar.DAY_OF_MONTH);
		int month = cv.getDate().get(Calendar.MONTH)+1;
		int year = cv.getDate().get(Calendar.YEAR);
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
		TextView tve = (TextView) activity.findViewById(R.id.textViewDayEvents);
		tve.setText(activity.getResources().getString(R.string.events_day)+dateKey);
		updateList(activity, dateKey);


	}
	
	/**
	 * Update the event list in a specific date
	 * @param activity
	 * @param date String in the dd/mm/yy format
	 */
	public void updateList(Activity activity, String date){
		AppLevelData data = appLevel.getData(activity);
		CalendarLevelDataItem item = (CalendarLevelDataItem)data.findByNextLevel(nextLevel);
		HashMap<String,TreeSet<EventDataItem>> TotalEvents = item.getEvents();
		TreeSet<EventDataItem> eventsInDate = TotalEvents.get(date);
		ArrayList<EventDataItem> eventsList = new ArrayList<EventDataItem>();
		//Maybe null
		if (eventsInDate!=null){
			eventsList.addAll(eventsInDate);
		}
		
		//Putting the list in the ListView
		ListView lv = (ListView)activity.findViewById(R.id.listViewCalendarEvents);
		lv.setAdapter(new CalListAdapter(activity, R.layout.list_item, eventsList));
		lv.setTextFilterEnabled(true);
	}

	@Override
	protected int getContentViewResourceId(Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.calendar_layout;
	}
	
	
	/**
	 * Adapter for Event list
	 * @author Manuel Mora Cuesta
	 *
	 */
	public class CalListAdapter extends ArrayAdapter<EventDataItem> {
    	private List<EventDataItem> items;
    	private Activity activity;
    	
    	public CalListAdapter(Activity context, int textViewResourceId, List<EventDataItem> objects) {
    		super(context, textViewResourceId, objects);
    		this.items = objects;
    		this.activity = context;
		}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		RelativeLayout view = (convertView != null) ? (RelativeLayout) convertView : createView(parent);
    		final EventDataItem item = items.get(position);
            
    		View.OnClickListener listener = new View.OnClickListener() {
		        public void onClick(View view) {
		        	showNextLevel(activity, item.getNextLevel());		        	
		        }
            };

            Button button = (Button)view.findViewById(R.id.selection_list);
            button.setText(item.getTime()+"    "+item.getTitle());
            button.setOnClickListener(listener);
            initializeListFormat(activity, ActivityType.CALENDAR_ACTIVITY, button);
            
	    	return view;
    	 }

    	 private RelativeLayout createView(ViewGroup parent) {
    		 RelativeLayout item = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
    		 return item;
    	 }
    }

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.CALENDAR_ACTIVITY;
	}

}
