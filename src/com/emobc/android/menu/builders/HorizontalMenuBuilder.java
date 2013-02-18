/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* TopMenuBuilder.java
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
package com.emobc.android.menu.builders;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emobc.android.activities.R;
import com.emobc.android.menu.Menu;
import com.emobc.android.menu.MenuItem;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class HorizontalMenuBuilder implements MenuBuilder {
	
	private static final int HORIZONTAL_MENU_MIN_HEIGTH = 50;
	private static final int HORIZONTAL_MENU_MIN_WIDTH = 133;
	private static final long MENU_ITEM_COLOR_FILTER_TIMEOUT = 500;

	@Override
	public int buildMenu(final Activity context, Menu menu, ViewGroup layout) {
		if(context == null)
			throw new IllegalArgumentException("Invalid Context: null");
		if(menu == null)
			throw new IllegalArgumentException("Invalid Menu: null");
		if(layout == null)
			throw new IllegalArgumentException("Invalid Layout: null");
				
		List<MenuItem> items = menu.getItems();
				
		Display display = context.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();		
		
		int menuItemHeight = (int) context.getResources().getDimension(R.dimen.menu_height);
		if(menuItemHeight <= 0)
			menuItemHeight = HORIZONTAL_MENU_MIN_HEIGTH;
		int menuItemMinWidth = (int) context.getResources().getDimension(R.dimen.menu_width);
		if(menuItemMinWidth <= 0)
			menuItemMinWidth = HORIZONTAL_MENU_MIN_WIDTH;
		
		int numberOfMenuItemPerBand = width / menuItemMinWidth;
		int menuItemWidth = width / numberOfMenuItemPerBand;
		
		int numberOfBands = (items.size() / numberOfMenuItemPerBand);
		if(items.size() >  numberOfMenuItemPerBand * numberOfBands)
			numberOfBands++;
		
		if(numberOfMenuItemPerBand > items.size())
			numberOfMenuItemPerBand = items.size();
		
		int itemNumber = 0;
		
		for(int i = 0; i < numberOfBands; i++){
			LinearLayout menuBandLinearLayout = new LinearLayout(context);
			menuBandLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, HORIZONTAL_MENU_MIN_HEIGTH);
			menuBandLinearLayout.setLayoutParams(llp);
			
			int bandMenuItemWidth = menuItemWidth;
			if(itemNumber + numberOfMenuItemPerBand >= items.size()){
				int bandItems = items.size() - itemNumber;
				bandMenuItemWidth = width / bandItems;
			}
			
			for(int j = 0; j < numberOfMenuItemPerBand; j++){
				if(itemNumber >= items.size())
					break;
				
				final MenuItem item = items.get(itemNumber);
				
				RelativeLayout itemView = (RelativeLayout)context.getLayoutInflater().inflate(R.layout.menu_item, menuBandLinearLayout, false);
				itemView.setId(itemNumber+1);
				
				TextView menuItemTextView = (TextView )itemView.findViewById(R.id.menu_item_text);
				final ImageView menuItemImageView = (ImageView) itemView.findViewById(R.id.menu_item_image);
				
				if(Utils.hasLength(item.getTitle())){
					menuItemTextView.setText(item.getTitle());
				}else{
					menuItemTextView.setVisibility(View.GONE);
				}
				
				if(Utils.hasLength(item.getImageFileName())){									
					Drawable icon;
					try {
						icon = ImagesUtils.getDrawable(context, item.getImageFileName());
						menuItemImageView.setImageDrawable(icon);
					} catch (InvalidFileException e) {
						Log.e("TopMenuBuilder", e.getLocalizedMessage());
					}
				}else{
					menuItemImageView.setVisibility(View.GONE);
				}
				
				itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						menuItemImageView.setColorFilter(0x77000000, Mode.SRC_ATOP);
						item.executeMenuItem(context);
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								menuItemImageView.setColorFilter(Color.TRANSPARENT, Mode.SRC_ATOP);
							}
						}, MENU_ITEM_COLOR_FILTER_TIMEOUT);
					}
				});
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						bandMenuItemWidth, 
						menuItemHeight);
				itemView.setLayoutParams(params);
				
				boolean enabled = item.isEnable(context);
				itemView.setEnabled(enabled);
				if(!enabled){
					menuItemImageView.setColorFilter(0x77000000, Mode.SRC_ATOP);	
				}
				
				menuBandLinearLayout.addView(itemView);
				
				itemNumber++;
			}			
			
//			RelativeLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, 
//					TOP_MENU_MIN_HEIGTH);
			layout.addView(menuBandLinearLayout);
		}
		
		int menuHeight = menuItemHeight * numberOfBands; 
		
		RelativeLayout.LayoutParams menuLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, 
				menuHeight);
		
		layout.setLayoutParams(menuLayoutParams);
		
		return menuHeight;
	}
}
