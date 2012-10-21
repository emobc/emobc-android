/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ButtonsActivityGenerator.java
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

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.emobc.android.ActivityType;
import com.emobc.android.AppButton;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.ButtonsLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * <p>
 * Screen generator, responsible for specific components to initialize the 
 * display type "BUTTONS". It also creates the menus, rotations, and the format for 
 * the components.
 * </p>
 * @author Jorge E. Villaverde
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class ButtonsActivityGenerator extends LevelActivityGenerator{
	
	public ButtonsActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		final ButtonsLevelDataItem item = (ButtonsLevelDataItem)data.findByNextLevel(nextLevel);
 		
		if(item == null){
			showAlerDialog(activity, "No se ha encontrado el NextLevel:\n" + nextLevel.toString());
		}else{
			initializeHeader(activity, item);
			
			//Create Banner
			CreateMenus c = (CreateMenus)activity;
			c.createBanner();
			
			//Initialize the screen buttons
			initializeAllButtons(activity, item.getButtonList(), item.getColumns());
		}
	}

	@Override
	protected int getContentViewResourceId(Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id > 0)
				return id;
		}
		return R.layout.buttons_layout;
	}

	/**
	 * Initializes and structure the matrix that contains all dynamic buttons
	 * @param activity
	 * @param listButtons
	 */
	private void initializeAllButtons(Activity activity, List<AppButton> listButtons, int columnsNumber){
		if(columnsNumber <= 0)
			columnsNumber = ButtonsLevelDataItem.DEFAULT_COLUMNS_NUMBER;
		
		TableLayout buttonsMatrix = (TableLayout)activity.findViewById(R.id.buttonsMatrix);
		if(buttonsMatrix == null)
			return;
		
		TableRow buttonsRow = null;
		
		for(int i=0; i < listButtons.size(); i++){
			AppButton currButton = listButtons.get(i);
			
			if(i % columnsNumber == 0){
				buttonsRow = new TableRow(activity);
				buttonsMatrix.addView(buttonsRow);
			}
			
			Button newButton = initializeButton(activity, currButton);
			if(newButton != null){
				buttonsRow.addView(newButton);
			}				
		}
	}
	
	/**
	 * Initializes the button functionally
	 * @param activity
	 * @param button
	 * @return
	 */
	private Button initializeButton(final Activity activity, final AppButton button){
		Button childButton = new Button(activity);
		Drawable imageButton;
		try {
			imageButton = ImagesUtils.getDrawable(activity, button.getFileName());
			if(imageButton != null){
				childButton.setBackgroundDrawable(imageButton);
			}else{
				childButton.setText(button.getTitle());
			}

			childButton.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					showNextLevel(activity, button.getNextLevel());
				}
			});
			
			return childButton;
		} catch (InvalidFileException e) {
			Log.e("ImageUtils", "Image Fail: " + e); 
			return null;
		}			
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.BUTTONS_ACTIVITY;
	}
}