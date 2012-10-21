/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* FormActivityGenerator.java
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
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.ImageDataItem;
import com.emobc.android.levels.impl.ImageGalleryLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "IMAGE_GALLERY". It also creates the menus, rotations, and the format for 
 * the components.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ImageGalleryActivityGenerator extends LevelActivityGenerator {
	
	public ImageGalleryActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final ImageGalleryLevelDataItem item = (ImageGalleryLevelDataItem)data.findByNextLevel(nextLevel);

		//rotateScreen(activity);		
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		if(item.getList() != null && !item.getList().isEmpty()){
		    Gallery g = (Gallery) activity.findViewById(R.id.galery);
		    List<Drawable> images = new ArrayList<Drawable>();
		    
		    for(ImageDataItem imgItem : item.getList()){
				try {
					Drawable drawble;
					drawble = ImagesUtils.getDrawable(activity, imgItem.getImageFile());
					images.add(drawble);
				} catch (InvalidFileException e) {
					Log.i("ImageGalleryActivity","Error loading Image "+imgItem.getImageFile());
				}
				
		    }
		    
		    g.setAdapter(new ImageAdapter(activity, images));

		    g.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            showNextLevel(activity, item.getList().get(position).getNextLevel());
		        }
		    });
			
		}		
	}

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.image_galery;
	}

	/**
	 * Class intended for positioning images
	 */
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private List<Drawable> images;
 
	    public ImageAdapter(Context c, List<Drawable> images) {
	        mContext = c;
	        this.images = images;
	    }

	    public int getCount() {
	        return images.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView i = new ImageView(mContext);

	        i.setImageDrawable(images.get(position));
//	        i.setImageResource(mImageIds[position]);
//	        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
	        i.setScaleType(ImageView.ScaleType.FIT_XY);

	        return i;
	    }
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.IMAGE_GALLERY_ACTIVITY;
	}	
}
