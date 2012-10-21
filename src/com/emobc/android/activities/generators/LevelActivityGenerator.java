/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* LevelActivityGenerator.java
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

import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.activities.SplashActivity;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.themes.FormatStyle;
import com.emobc.android.themes.LevelTypeStyle;
import com.emobc.android.utils.ImagesUtils;

/**
 * Abstract class that implements all the methods necessary for the proper 
 * start of the screens, focusing primarily on the current "level".
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public abstract class LevelActivityGenerator extends AbstractActivtyGenerator {
	protected AppLevel appLevel;
	protected NextLevel nextLevel;

	public LevelActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super();
		this.appLevel = appLevel;
		this.nextLevel = nextLevel;
	}
	
	public AppLevel getAppLevel() {
		return appLevel;
	}
	public void setAppLevel(AppLevel appLevel) {
		this.appLevel = appLevel;
	}

	@Override
	protected void intializeSubActivity(Activity activity) {
		AppLevelData data = appLevel.getData(activity);
		LevelTypeStyle format = appLevel.getLevelTypeStyle();
		if(data == null){
			showInvalidDataAlerDialog(activity);
		}else{
			// Initialize Application Level
			if(format == null || format.isCleanFormat()){				
				initializeScreen(activity, getActivityGeneratorType());
			}else{
				initializeScreenWithFormat(activity, format);
			}			
			loadAppLevelData(activity, data);
		}
	}
	

	protected void showInvalidDataAlerDialog(final Activity activity) {
		final AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
		dlg.setTitle(R.string.err_level_data_title);
		dlg.setMessage(R.string.err_level_data_alert);
		dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.onBackPressed();
			}
		});
		
		dlg.show();
	}		

	/**
	 * Initializes the background, and all components formats for an activity type
	 * @param activity
	 * @param activityType
	 * @param params
	 */
	protected void initializeScreen(Activity activity, ActivityType activityType){
		ApplicationData applicationData = SplashActivity.getApplicationData();		
		LevelTypeStyle format = applicationData.getLevelStyle(activityType);
		if(format != null && !format.isCleanFormat()){				
			initializeScreenWithFormat(activity, format);
		}			
	}
	
		
	/**
	 * Initialize the specific background for a level
	 * @param activity
	 * @param activityType
	 */
	public void initializeBackground(Activity activity, LevelTypeStyle levelTypeStyle){
		if(activity == null || levelTypeStyle == null)
			return;
		
		String backgroundFileName = levelTypeStyle.getBackground();
		//String backgroundName = backgroundFileName.split("\\.")[0];
		RelativeLayout backgroundLayout = (RelativeLayout)activity.findViewById(R.id.backgroundLayout);
		
		try {
			//int imageResource = activity.getResources().getIdentifier(backgroundName, "drawable", activity.getPackageName());
		    //Drawable backgroundDrawable = activity.getResources().getDrawable(imageResource);
			//backgroundLayout.setBackgroundDrawable(backgroundDrawable);
			backgroundLayout.setBackgroundDrawable(ImagesUtils.getDrawable(activity, backgroundFileName));
		} catch (Exception e) {
			Log.e("ApplicationData","ImageFile not found");
		}
	}
	
	
	/**
	 * Initializes the background, and all components formats for an specific level
	 * @param activity
	 * @param levelTypeStyle
	 */
	protected void initializeScreenWithFormat(Activity activity, LevelTypeStyle levelTypeStyle){					
		initializeBackground(activity, levelTypeStyle);		
		initializeWidgetFormat(activity, levelTypeStyle);
	}
	
	
	/**
	 * Initialize the widget's components (textColor, textSize, textStyle, typeFace) 
	 * with the XML content, depending on a specific screen type.
	 * @param activity
	 * @param typeScreen
	 * @param currWidget
	 */
	public void initializeWidgetFormat(Activity activity, ActivityType activityType){
		ApplicationData applicationData = SplashActivity.getApplicationData();		
		LevelTypeStyle levelTypeStyle = applicationData.getLevelStyle(activityType);
		initializeWidgetFormat(activity, levelTypeStyle);
	}
	
	/**
	 * Initialize the widget's components (textColor, textSize, textStyle, typeFace)  
	 * with the XML content, depending on a specific screen type.
	 * @param activity
	 * @param typeScreen
	 * @param currWidget
	 */
	public void initializeWidgetFormat(Activity activity, LevelTypeStyle levelStyle){
		Map<String, FormatStyle> formatStyleMap = SplashActivity.getApplicationData().getFormatStyleMap();
				
		Iterator<String> iterator = levelStyle.getListComponents().iterator();
		
		while(iterator.hasNext()){
			String currWidget = iterator.next();
			FormatStyle currFormat = getCurrentFormatWidget(levelStyle, currWidget);
			
			if(currFormat == null)
				currFormat = formatStyleMap.get("default");
			
			try{
				int resID = activity.getResources().getIdentifier(currWidget, "id", activity.getPackageName());
				
				TextView v = (TextView) activity.findViewById(resID);
				
				v.setTextColor(Color.parseColor(currFormat.getTextColor())); 
				
				
				String complexSize = currFormat.getTextSize();
				int sep = getSeparation(0, complexSize);		
				String unitStr = complexSize.substring(sep);
				int size = Integer.parseInt(complexSize.substring(0, sep));
				int unit;
				if(unitStr.equals("sp")){
					unit = TypedValue.COMPLEX_UNIT_SP;
				}else if(unitStr.equals("dip")){
					unit = TypedValue.COMPLEX_UNIT_DIP;
				}else if(unitStr.equals("px")){
					unit = TypedValue.COMPLEX_UNIT_PX;
				}else//default
					unit = TypedValue.COMPLEX_UNIT_SP;
				
				v.setTextSize(unit, size);
				
				
				String complexStyle = currFormat.getTextStyle();
				int style;
				if(complexStyle.equals("normal")){
					style = Typeface.NORMAL;
				}else if(complexStyle.equals("bold")){
					style = Typeface.BOLD;
				}else if(complexStyle.equals("italic")){
					style = Typeface.ITALIC;
				}else//default
					style = Typeface.NORMAL;
				
				String type = currFormat.getTypeFace();
				Typeface tf;
				//Try create a typeface with the type and the style.
				//If it can't, it uses the specific font in assets/fonts
				try{
					Typeface tfAux = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/"+type+".ttf");
					tf = Typeface.create(tfAux, style);	
					v.setTypeface(tf);
				}catch(Exception e){
					Log.e("AplicationData","Impossible to apply any typeface");
				}	
				
			}catch(Exception e){
				Log.e("ApplicationData","Impossible to apply the format");
			}//END Try-catch
			
		}//END While
	}
	
	/**
	 * Returns the format to associated with activity type and a widget  
	 * @param activityType
	 * @param currWidget
	 * @return
	 */
	private FormatStyle getCurrentFormatWidget(LevelTypeStyle levelTypeStyle, String currWidget){		
		Map<String, FormatStyle> formatStyleMap = SplashActivity.getApplicationData().getFormatStyleMap();
		Map<String,String> mapFormatComponents = levelTypeStyle.getMapFormatComponents();
		String formatName = mapFormatComponents.get(currWidget);
		return formatStyleMap.get(formatName);

	}

	/**
	 * Initialize the list's components (textColor, textSize, textStyle, typeFace, cacheColorHint)  
	 * with the XML content.
	 * @param activity
	 * @param v
	 * @param fs
	 */
	private void initializeSelectionFormat(Activity activity, Button v, FormatStyle fs){
		try{
			ListView list = (ListView) activity.findViewById(R.id.list);
			list.setCacheColorHint(Color.parseColor(fs.getCacheColorHint()));
			list.setBackgroundColor(Color.parseColor(fs.getBackgroundColor()));
			v.setTextColor(Color.parseColor(fs.getTextColor()));
			
			String complexSize = fs.getTextSize();
			int sep = getSeparation(0, complexSize);		
			String unitStr = complexSize.substring(sep);
			int size = Integer.parseInt(complexSize.substring(0, sep));
			int unit;
			if(unitStr.equals("sp")){
				unit = TypedValue.COMPLEX_UNIT_SP;
			}else if(unitStr.equals("dip")){
				unit = TypedValue.COMPLEX_UNIT_DIP;
			}else if(unitStr.equals("px")){
				unit = TypedValue.COMPLEX_UNIT_PX;
			}else//default
				unit = TypedValue.COMPLEX_UNIT_SP;
			
			v.setTextSize(unit, size);
			
			String complexStyle = fs.getTextStyle();
			int style;
			if(complexStyle.equals("normal")){
				style = Typeface.NORMAL;
			}else if(complexStyle.equals("bold")){
				style = Typeface.BOLD;
			}else if(complexStyle.equals("italic")){
				style = Typeface.ITALIC;
			}else//default
				style = Typeface.NORMAL;
			
			String type = fs.getTypeFace();
			Typeface tf;
				
			//Try create a typeface with the type and the style.
			//If it can't, it uses the specific font in assets/fonts
			
			try{
				Typeface tfAux = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/"+type+".ttf");
				tf = Typeface.create(tfAux, style);	
				v.setTypeface(tf);
			}catch(Exception e2){
				Log.e("ApplicationData","Impossible to apply any typeface");
			}
			
		}catch(Exception e){
			Log.e("ApplicationData","Impossible to apply the format");
		}
	}

	/**
	 * Initialize the list's format and background, with the XML content, 
	 * depending on a specific screen type.
	 * @param activity
	 * @param activityType
	 * @param selection
	 */
	public void initializeListFormat(Activity activity, ActivityType activityType, Button selection){
		try {
			Map<ActivityType, LevelTypeStyle> levelStyleTypeMap = SplashActivity.getApplicationData().getLevelStyleTypeMap();
			Map<String, FormatStyle> formatStyleMap = SplashActivity.getApplicationData().getFormatStyleMap();
			
			LevelTypeStyle levelTypeStyle = levelStyleTypeMap.get(activityType);		
			String listFormat = levelTypeStyle.getSelectionList();
			FormatStyle fs = formatStyleMap.get(listFormat);	

			String backgroundSelectionFileName = fs.getBackgroundSelectionFileName();
			String backgroundSelectionName = backgroundSelectionFileName.split("\\.")[0];
		
			int imageResource = activity.getResources().getIdentifier(backgroundSelectionName, "drawable", activity.getPackageName());
		    Drawable backgroundSelectionDrawable = activity.getResources().getDrawable(imageResource);
			selection.setBackgroundDrawable(backgroundSelectionDrawable);
		
			initializeSelectionFormat(activity, selection, fs);
		} catch (Exception e) {
			Log.e("ApplicationData","ImageFile not found");
		}
	}
	
	/**
	 * Recursive method witch returns the position between number and character
	 * @param i
	 * @param complexSize
	 * @return
	 */
	private static int getSeparation(int i, String complexSize){
		if(isNumeric(complexSize.charAt(i))){
			i++;
			i = getSeparation(i, complexSize);
		}
		return i;
	}
	
	/**
	 * Return true if character is a number, and false if it isn't
	 * @param cadena
	 * @return
	 */
	private static boolean isNumeric(char character){
		try {
			Integer.parseInt(String.valueOf(character));
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	
	/**
	 * Primary method of ActivityGenerator. It initializes all components 
	 * of the screen, depending on the type of activity to call him.
	 * @param activity
	 * @param data
	 */
	protected abstract void loadAppLevelData(final Activity activity, final AppLevelData data);

	/**
	 * 
	 * @return Type of Activity that the generator implements
	 */
	protected abstract ActivityType getActivityGeneratorType();
}
