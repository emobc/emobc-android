/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ImageListActivityGenerator.java
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.ImageListLevelDataItem;
import com.emobc.android.levels.impl.ListItemDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "IMAGE_LIST". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageListActivityGenerator extends LevelActivityGenerator {
	
	public ImageListActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final ImageListLevelDataItem item = (ImageListLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		if(Utils.hasLength(item.getImageFile())){
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(activity, item.getImageFile());
				ImageView descrImaga = (ImageView)activity.findViewById(R.id.descr_image);
				descrImaga.setImageDrawable(drawable);
			} catch (InvalidFileException e) {
				Log.e("AppCoverData", e.getLocalizedMessage());
		    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}			
		}
		if(item.getList() != null && !item.getList().isEmpty()){
			List<ListItemDataItem> objects = item.getList();
			if(item.isOrder()){
				Collections.sort(objects, new Comparator<ListItemDataItem>() {
					@Override
					public int compare(ListItemDataItem object1, ListItemDataItem object2) {
						return object1.getText().compareTo(object2.getText());
					}
				});
			}
		
			
			ListView lv = (ListView)activity.findViewById(R.id.list);
			lv.setAdapter(new NwListAdapter(activity, R.layout.image_list_item, objects));
			lv.setTextFilterEnabled(true);
		}
	}


	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.image_list;
	}
	
	/**
	 * Class intended for the creation and initialization of listView
	 */
    private class NwListAdapter extends ArrayAdapter<ListItemDataItem> {
    	private List<ListItemDataItem> items;
    	private Activity activity;
    	
    	public NwListAdapter(Activity context, int textViewResourceId, List<ListItemDataItem> objects) {
    		super(context, textViewResourceId, objects);
    		this.items = objects;
    		this.activity = context;
		}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		LinearLayout view = (convertView != null) ? (LinearLayout) convertView : createView(parent);
    		final ListItemDataItem item = items.get(position);
            
    		View.OnClickListener listener = new View.OnClickListener() {
		        public void onClick(View view) {
		        	showNextLevel(activity, item.getNextLevel());		        	
		        }
            };

            Button button = (Button)view.findViewById(R.id.selection_list);
            button.setText(item.getText().toUpperCase());
            button.setOnClickListener(listener);
            initializeListFormat(activity, ActivityType.IMAGE_LIST_ACTIVITY, button);       
    		
	    	return view;
    	 }

    	 private LinearLayout createView(ViewGroup parent) {
    		 LinearLayout item = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.image_list_item, parent, false);
    		 return item;
    	 }
    }

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.IMAGE_LIST_ACTIVITY;
	}
	
}
