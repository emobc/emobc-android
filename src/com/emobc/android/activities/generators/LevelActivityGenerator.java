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

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.themes.ActivityTypeStyle;
import com.emobc.android.themes.FormatStyle;
import com.emobc.android.themes.LevelStyle;
import com.emobc.android.themes.Style;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;

/**
 * Abstract class that implements all the methods necessary for the proper 
 * start of the screens, focusing primarily on the current "level".
 * 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public abstract class LevelActivityGenerator extends AbstractActivtyGenerator {
	private static final String TAG = "LevelActivityGenerator";
	/**
	 * 
	 */
	private static final long serialVersionUID = 6029596069752642543L;
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
		if(data == null){
			showInvalidDataAlerDialog(activity);
		}else{
			initializeScreen(activity, getActivityGeneratorType());
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
		initializeScreen(activity, activityType, nextLevel);
	}
	
	public static void initializeScreen(Activity activity, ActivityType activityType, NextLevel nextLevel){
		ApplicationData applicationData = AbstractActivtyGenerator.getApplicationData(activity);		
		ActivityTypeStyle activityTypeStyle = applicationData.getActivityTypeStyle(activityType);
		LevelStyle levelStyle = null;
		if(nextLevel != null && nextLevel.isDefined())
			levelStyle = applicationData.getLevelStyle(nextLevel.getLevelId());
				
		// Fist try to apply ActivityType Style
		if(activityTypeStyle != null && !activityTypeStyle.isCleanFormat()){				
			initializeScreenWithFormat(activity, activityTypeStyle);
		}
		// Then try to apply Level Stype
		if(levelStyle != null && !levelStyle.isCleanFormat()){
			initializeScreenWithFormat(activity, levelStyle);
		}		
	}
		
	/**
	 * Initialize the specific background for a level
	 * @param activity
	 * @param activityType
	 */
	public static void initializeBackground(Activity activity, Style style){
		if(activity == null || style == null)
			return;
		
		String backgroundFileName = style.getBackground();
		
		if(Utils.hasLength(backgroundFileName)){									
			ViewGroup backgroundLayout = (ViewGroup)activity.findViewById(R.id.backgroundLayout);
			if(backgroundLayout == null)
				return;
			Drawable backgroundDrawable;
			try {
				backgroundDrawable = ImagesUtils.getDrawable(activity, backgroundFileName);
				backgroundLayout.setBackgroundDrawable(backgroundDrawable);
			} catch (InvalidFileException e) {
				Log.e(TAG, e.getLocalizedMessage());
			}
		}
	}
	
	
	/**
	 * Initializes the background, and all components formats for an specific level
	 * @param activity
	 * @param levelTypeStyle
	 */
	public static void initializeScreenWithFormat(Activity activity, Style style){					
		initializeBackground(activity, style);		
		initializeWidgetFormat(activity, style);
	}
	
	
	/**
	 * Initialize the widget's components (textColor, textSize, textStyle, typeFace)  
	 * with the XML content, depending on a specific screen type.
	 * @param activity
	 * @param typeScreen
	 * @param currWidget
	 */
	public static void initializeWidgetFormat(Activity activity, Style levelStyle){
		Map<String, FormatStyle> formatStyleMap = AbstractActivtyGenerator.getApplicationData(activity).getFormatStyleMap(activity);
		
		if(levelStyle == null)
			return;
		if(levelStyle.getListComponents() == null)
			return;
		
		for(String currWidget : levelStyle.getListComponents()){
			FormatStyle currFormat = getCurrentFormatWidget(levelStyle, currWidget, activity);
			
			if(currFormat == null)
				currFormat = formatStyleMap.get("default");
			
			try{
				int resID = activity.getResources().getIdentifier(currWidget, "id", activity.getPackageName());
				
				TextView v = (TextView) activity.findViewById(resID);
				
				if(v == null)
					continue;
				
				if(Utils.hasLength(currFormat.getTextColor()))
					v.setTextColor(Color.parseColor(currFormat.getTextColor())); 
				
				String complexSize = currFormat.getTextSize();
				
				if(Utils.hasLength(complexSize)){
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
				}
				
				String complexStyle = currFormat.getTextStyle();
				
				if(Utils.hasLength(complexStyle)){
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
					
					if(Utils.hasLength(type)){
						Typeface tf;
						//Try create a typeface with the type and the style.
						//If it can't, it uses the specific font in assets/fonts
						try{
							Typeface tfAux = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/"+type+".ttf");
							tf = Typeface.create(tfAux, style);	
							v.setTypeface(tf);
						}catch(Exception e){
							Log.e(TAG, "Impossible to apply any typeface: " + e.getMessage());
						}	
					}					
				}
			}catch(Exception e){
				Log.e(TAG,"Impossible to apply the format: " + e.getMessage());
			}//END Try-catch
			
		}//END While
	}
	
	/**
	 * Returns the format to associated with activity type and a widget  
	 * @param activityType
	 * @param currWidget
	 * @return
	 */
	private static FormatStyle getCurrentFormatWidget(Style style, String currWidget, Activity activity){		
		Map<String, FormatStyle> formatStyleMap = AbstractActivtyGenerator.getApplicationData(activity).getFormatStyleMap(activity);
		Map<String,String> mapFormatComponents = style.getMapFormatComponents();
		String formatName = mapFormatComponents.get(currWidget);
		return formatStyleMap.get(formatName);

	}

	/**
	 * Initialize the list's components (textColor, textSize, textStyle, typeFace, cacheColorHint)  
	 * with the XML content.
	 * @param activity
	 * @param textView
	 * @param fs
	 */
	private void initializeSelectionFormat(Activity activity, View view, FormatStyle fs){
		try{
			ListView list = (ListView) activity.findViewById(R.id.list);
			TextView textView = (TextView)view.findViewById(R.id.list_item_text);
			
			if(Utils.hasLength(fs.getCacheColorHint()))
				list.setCacheColorHint(Color.parseColor(fs.getCacheColorHint()));
			if(Utils.hasLength(fs.getBackgroundColor()))
				list.setBackgroundColor(Color.parseColor(fs.getBackgroundColor()));
			if(Utils.hasLength(fs.getTextColor()))
				textView.setTextColor(Color.parseColor(fs.getTextColor()));
			
			String complexSize = fs.getTextSize();
			if(Utils.hasLength(complexSize)){
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
				
				textView.setTextSize(unit, size);
			}
			
			String complexStyle = fs.getTextStyle();
			if(Utils.hasLength(complexStyle)){
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
				
				if(Utils.hasLength(type)){	
					Typeface tf;
					//Try create a typeface with the type and the style.
					//If it can't, it uses the specific font in assets/fonts
					
					try{
						Typeface tfAux = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/"+type+".ttf");
						tf = Typeface.create(tfAux, style);	
						textView.setTypeface(tf);
					}catch(Exception e){
						Log.e(TAG,"Impossible to apply any typeface: " + e.getMessage());
					}
				}			
			}			
		}catch(Exception e){
			Log.e(TAG,"Impossible to apply the format: " + e.getMessage());
		}
	}

	/**
	 * Initialize the list's format and background, with the XML content, 
	 * depending on a specific screen type.
	 * @param activity
	 * @param activityType
	 * @param textView
	 */
	public void initializeListFormat(Activity activity, ActivityType activityType, View view){
		Map<ActivityType, ActivityTypeStyle> activityTypeStyleTypeMap = 
				AbstractActivtyGenerator.getApplicationData(activity).getActivityTypeStyleTypeMap(activity);
		
		Map<String, LevelStyle> levelStyleTypeMap = 
				AbstractActivtyGenerator.getApplicationData(activity).getLevelStyleTypeMap(activity);
		
		Map<String, FormatStyle> formatStyleMap = 
				AbstractActivtyGenerator.getApplicationData(activity).getFormatStyleMap(activity);
		
		ActivityTypeStyle activityTypeStyle = activityTypeStyleTypeMap.get(activityType);
		LevelStyle levelStyle = levelStyleTypeMap.get(nextLevel.getLevelId());
		
		if(activityTypeStyle != null && !activityTypeStyle.isCleanFormat()){
			applyStyle(activity, activityTypeStyle, formatStyleMap, view);			
		}
		if(levelStyle != null && !levelStyle.isCleanFormat()){
			applyStyle(activity, levelStyle, formatStyleMap, view);
		}
	}
	
	private void applyStyle(Activity activity, Style style, Map<String, FormatStyle> formatStyleMap, View view) {
		String listFormat = style.getSelectionList();
		FormatStyle fs = formatStyleMap.get(listFormat);	
		
		if(fs != null){
			String backgroundSelectionFileName = fs.getBackgroundSelectionFileName();
			String backgroundSelectionName = backgroundSelectionFileName.split("\\.")[0];
			
			int imageResource = activity.getResources().getIdentifier(backgroundSelectionName, "drawable", activity.getPackageName());
			Drawable backgroundSelectionDrawable = activity.getResources().getDrawable(imageResource);
			view.setBackgroundDrawable(backgroundSelectionDrawable);
	
			initializeSelectionFormat(activity, view, fs);
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
