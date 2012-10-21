/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ListActivityGenerator.java
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.emobc.android.levels.impl.ListItemDataItem;
import com.emobc.android.levels.impl.ListLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImageLoader;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "LIST". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ListActivityGenerator extends LevelActivityGenerator {

	public ListActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final ListLevelDataItem item = (ListLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		if(item.getList() != null && !item.getList().isEmpty()){
			ListView lv = (ListView)activity.findViewById(R.id.list);
			List<ListItemDataItem> objects = item.getList();
			if(item.isOrder()){
				Collections.sort(objects, new Comparator<ListItemDataItem>() {
					@Override
					public int compare(ListItemDataItem object1, ListItemDataItem object2) {
						return object1.getText().compareTo(object2.getText());
					}
				});
			}
			lv.setAdapter(new NwListAdapter(activity, R.layout.list_item, objects));
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
		return R.layout.list_only;
	}
	
	/**
	 * Class intended for the creation and initialization of listView
	 */
    private class NwListAdapter extends BaseAdapter {
    	private List<ListItemDataItem> items;
    	private Activity activity;
    	private LayoutInflater inflater=null;
        public ImageLoader imageLoader;
    	
        public class ViewHolder{
            @SuppressWarnings("unused")
			public Button button;
            public ImageView image;
        }
        
        public NwListAdapter(Activity context, int textViewResourceId, List<ListItemDataItem> objects) {
    		this.items = objects;
    		this.activity = context;
    		inflater = LayoutInflater.from(context);
    		imageLoader=new ImageLoader(activity.getApplicationContext());
		}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View vi=convertView;
            ViewHolder holder;
            final ListItemDataItem item = items.get(position);
            if(convertView==null){
                vi = inflater.inflate(R.layout.list_item, null);
                holder=new ViewHolder();
               // button.setText(item.getText().toUpperCase());            
            }
            else
                holder=(ViewHolder)vi.getTag();
            
            
            View.OnClickListener listener = new View.OnClickListener() {
		        public void onClick(View view) {
		        	showNextLevel(activity, item.getNextLevel());		        	
		        }
            };

            Button button = (Button)vi.findViewById(R.id.selection_list);
            button.setText(item.getText());
            button.setOnClickListener(listener);
            initializeListFormat(activity, ActivityType.LIST_ACTIVITY, button);
            holder.button=button;
            holder.image=(ImageView)vi.findViewById(R.id.list_img);
            vi.setTag(holder);
            
            
            if (ImagesUtils.isUrl(item.getImageFile())){
            	holder.image.setTag(item.getImageFile());
            	imageLoader.DisplayImage(item.getImageFile(), activity, holder.image);
            }else{
            	try {
					holder.image.setImageDrawable(ImagesUtils.getDrawable(activity, item.getImageFile()));
				} catch (InvalidFileException e) {
					e.printStackTrace();
					Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
            }
            
            return vi;
    		/*LinearLayout view = (convertView != null) ? (LinearLayout) convertView : createView(parent);
    		final ListItemDataItem item = items.get(position);
            
    		View.OnClickListener listener = new View.OnClickListener() {
		        public void onClick(View view) {
		        	showNextLevel(activity, item.getNextLevel());		        	
		        }
            };
    		
            Button button = (Button)view.findViewById(R.id.selection_list);
            button.setText(item.getText());
            button.setOnClickListener(listener);
            SplashActivity.getApplicationData().initializeListFormat( activity, "LIST_ACTIVITY", button);
            
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(activity, item.getImageFile());
				if(drawable != null){
					ImageView listItemImage = (ImageView)view.findViewById(R.id.list_img);
					if(listItemImage != null)
						listItemImage.setImageDrawable(drawable);
				}
			} catch (InvalidFileException e) {
				Log.e("AppCoverData", e.getLocalizedMessage());
		    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}          
            
	    	return view;*/
    	 }

    	@SuppressWarnings("unused")
		private LinearLayout createView(ViewGroup parent) {
    		 LinearLayout item = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
    		 return item;
    	 }

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
    }

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.LIST_ACTIVITY;
	}
}
