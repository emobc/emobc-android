/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AbstractActivtyGenerator.java
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.activities.SplashActivity;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * Abstract class that inherit all other ActivityGenerators. Its more important methods 
 * are initializeActivity and showNextLevel.
 * @author Jorge E. Villaverde
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public abstract class AbstractActivtyGenerator implements ActivityGenerator {
    
	public AbstractActivtyGenerator() {
		super();
	}

	/**
	 * First screen is displayed corresponding to the activity contained in / res / layout /;
	 * After the values ​​are initialized and the actions of its components.
	 * @param activity
	 */
	@Override
	public void initializeActivity(final Activity activity) {
		activity.setContentView(getContentViewResourceId(activity));
		
		initializeGestures((CreateMenus)activity);
		
		intializeSubActivity(activity);
	}

	private void initializeGestures(CreateMenus activity) {
        GestureOverlayView gestureOverlayView = new GestureOverlayView(activity);
        
        View inflate = activity.getLayoutInflater().inflate(getContentViewResourceId(activity), null);
        
        gestureOverlayView.addView(inflate);
        gestureOverlayView.addOnGesturePerformedListener(activity);
        GestureLibrary gestureLib = GestureLibraries.fromRawResource(activity, R.raw.gestures);
        if (!gestureLib.load()) {
        	activity.finish();
        }
        activity.setGestureLib(gestureLib);
	}

	/**
	 * Abstract method that calls the method getContentViewResourceId() corresponding 
	 * to the activity being executed at that time.
	 * <code>Pick up the identifier for each activity type.</code>
	 * For example: If you want to get the layout CoverActivity, will go to class 
	 * CoverActivityGenerator, and his method is responsible for providing the XML 
	 * ID corresponding to CoverActivity.
	 * @param activity
	 * @return int
	 */
	protected abstract int getContentViewResourceId(final Activity activity);
	
	/**
	 * Abstract method that calls the method intializeSubActivity() corresponding 
	 * to the activity being executed at that time.
	 * <code>Load levelData and sets levelTypeStyle for a specified activity.</code>
	 * For example: If you want to get the layout CoverActivity, will go to class 
	 * CoverActivityGenerator, and his method is responsible for providing the XML 
	 * ID corresponding to CoverActivity.
	 * @param activity
	 */
	protected abstract void intializeSubActivity(final Activity activity);
		
	/**
	 * Start the navigator with the specified URL
	 * @param context
	 * @param urlString
	 */
	protected void lunchUrl(Context context, String urlString){
		Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(urlString ));
		context.startActivity(browserIntent);
	}
	
	/**
	 * Start a new activity in the levelId leaning and dataId of NextLevel. 
	 * Also initializes parameters NextLevel and entrypoint 
	 * @param context
	 * @param nextLevel
	 */
	public static void showNextLevel(Context context, NextLevel nextLevel) {
		 if(nextLevel != null){
			ApplicationData applicationData = SplashActivity.getApplicationData();
			AppLevel level = applicationData.getNextAppLevel(nextLevel, context);
			if(level != null){
				Class<? extends Activity> clazz = level.getAcivityClass();
				
				Intent launchActivity = new Intent(context, clazz);				
				launchActivity.putExtra(ApplicationData.NEXT_LEVEL_TAG, nextLevel);	
				launchActivity.putExtra(ApplicationData.IS_ENTRY_POINT_TAG, false);
				
				if(ApplicationData.EMOBC_LEVEL_ID.equals(level.getId())){
					launchActivity.putExtra(ApplicationData.IS_PROFILE_TAG, true);
				}
				
				context.startActivity(launchActivity);
			}else{
				CharSequence text = "Invalid Next Level: " + nextLevel.toString();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();					
			}  
		} 
	}
	
	protected void initializeHeader(Activity activity, AppLevelDataItem item){
		if(item == null)
			return;
		
		TextView header = (TextView)activity.findViewById(R.id.header);
		if(Utils.hasLength(item.getHeaderImageFile())){
			try {
				header.setBackgroundDrawable(ImagesUtils.getDrawable(activity, item.getHeaderImageFile()));
			} catch (InvalidFileException e) {
				Log.e("AbstractActivityGenerator", "Error loading Image "+item.getHeaderImageFile());
			}
		}		
		if(Utils.hasLength(item.getHeaderText())){
			header.setText(item.getHeaderText());
		}	
	}
	
	
	protected void showAlerDialog(final Activity activity, final String content) {
		final AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
		dlg.setTitle(R.string.err_level_data_title);
		dlg.setMessage(content);
		dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.onBackPressed();
			}
		});
		
		dlg.show();
		
	}
	
	/**
	 * Returns the id for an layout name.
	 * @param activity
	 * @param layoutName
	 * @return
	 */
	protected int getActivityLayoutIdFromString(Activity activity, String layoutName){
		int resID;
		try {
			resID = activity.getResources().getIdentifier(layoutName, "layout",	activity.getPackageName());
			return resID;
		} catch (Resources.NotFoundException e) {
		}
		return -1;
	}
	
}
