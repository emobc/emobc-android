/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ApplicationData.java
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
package com.emobc.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.activities.generators.ActivityGeneratorFactory;
import com.emobc.android.activities.generators.CoverActivityGenerator;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.AppLevelDataItem;
import com.emobc.android.levels.impl.BannerDataItem;
import com.emobc.android.levels.impl.ServerPushDataItem;
import com.emobc.android.menu.Menu;
import com.emobc.android.menu.parse.MenuParser;
import com.emobc.android.parse.CoverParser;
import com.emobc.android.parse.ParseUtils;
import com.emobc.android.profiling.Profile;
import com.emobc.android.themes.FormatStyle;
import com.emobc.android.themes.ActivityTypeStyle;
import com.emobc.android.themes.LevelStyle;
import com.emobc.android.themes.parse.FormatParser;
import com.emobc.android.themes.parse.StyleParser;
import com.emobc.android.themes.parse.StyleResult;
import com.emobc.android.utils.InvalidFileException;

/**
 * Singleton is used for read and manage the app.xml file
 * From this class you can access to application Levels.
 * <p>ApplicationData operates in local or remove model.</p>
 * <p><strong>Local mode</strong> seeks for 
 * Level Ids only in memory tables. If a Level Id is not found, a <code>null</code>
 * {@link AppLevel} is returned.</p>
 * <p><strong>Remote mode</strong> seeks for Level Ids in memory first and if it not present then 
 * seeks for the Level Id in the <i>Remote Application File</i>.
 * </p>
 * @author Jorge E. Villaverde
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @sine 1.0
 */
public class ApplicationData {
	private static final String TAG = "ApplicationData";
	
	private static final String _APP_DATA_FILE_NAME_ = "app.xml";
	
	public static final String NEXT_LEVEL_TAG = "_NEXT_LEVEL_";
	public static final String IS_ENTRY_POINT_TAG = "_IS_ENTRY_POINT_";

//	public static final String IS_SIDE_MENU_TAG = "_IS_SIDE_MENU_";
	
	// The ID of your application that you received from PayPal
	private static final String PAYPAL_APP_ID = "APP-80W284485P519543T";
	
	public static final int SEARCH_LIMIT = 20;

	public static final String EMOBC_LEVEL_ID = "emobc";

	
	private String title;
	private String coverFileName;
	private String stylesFileName;
	private String formatsFileName;
	private String profileFileName;
	private NextLevel entryPoint;
	
	// Menues
	private String topMenu;
	private String bottomMenu;
	private String contextMenu;
	private String sideMenu;
	
	private String rotation;
	private BannerDataItem banner;
	private ServerPushDataItem serverPush; 


	private List<AppLevel> levels = new ArrayList<AppLevel>();
	private Map<String, AppLevel> levelMap = new HashMap<String, AppLevel>();
	private StyleResult styleResult = null;
	private Map<String, FormatStyle> formatStyleMap = null;
	
	private String payPalRecipient;
	private String payPalApplicationId = PAYPAL_APP_ID;
	
	private static ApplicationData instance = null;

	/**
	 * 6 MiB Cache Size
	 */
	private static final int MAX_CACHE_SIZE = 6 * 1024 * 1024;
	
	private LruCache<String, Drawable> cache = null;

	private Profile profile;
	
	/**
	 * If <tt>true</tt>, {@link AppLevel} could be read from a remote
	 * location defined in {@link ApplicationData#removeApplicationFileUrl}.
	 */
	private final boolean remote;
	
	/**
	 * URL of the Remote Application File.
	 */
	private final String remoteApplicationFileUrl;

	
	/**
	 * Menu Level Map
	 * LevelId -> Menu
	 * if (levelId == null) -> General Menu 
	 */
	private Map<String, Menu> topMenuLevelMap = null;
	
	private Map<String, Menu> bottomMenuLevelMap = null;

	private Map<String, Menu> contextMenuLevelMap = null;
	
	private Map<String, Menu> sideMenuLevelMap = null;

	
	/**
	 * Default Constructor. 
	 * <p>Used for Local Application File</p>
	 */
	public ApplicationData (){
		this(null);
	}
	
	/**
	 * Remote Application File Constructor.
	 * Set the application Data in remote mode.
	 * @param remoteApplicationFileUrl
	 */
	public ApplicationData(String remoteApplicationFileUrl){
		super();
		this.remoteApplicationFileUrl = remoteApplicationFileUrl;
		this.remote = (remoteApplicationFileUrl != null && !remoteApplicationFileUrl.isEmpty());
		loadDefaultLevels();
	}
	
	/**
	 * Load default levels
	 */
	private void loadDefaultLevels() {
		if(levels == null)
			levels = new ArrayList<AppLevel>();
//		
//		AppLevel emobcLevel = new AppLevel(0);
//		emobcLevel.setId(EMOBC_LEVEL_ID);
//		emobcLevel.setActivityType(ActivityType.PROFILE_ACTIVITY);
//		emobcLevel.setTitle("Profile");	
//		
//		addLevel(emobcLevel, false);		
	}

	/**
	 * Returns an ApplicationData with data obtained after parsing app.xml file
	 * @param context
	 * @return ApplicationData
	 */
	public static ApplicationData readApplicationData(Context context) throws InvalidFileException {
		return readApplicationData(context, Locale.getDefault());
	}
	
	/**
	 * Returns an ApplicationData with data obtained after parsing app.xml file
	 * @param context
	 * @param locale
	 * @return ApplicationData
	 */
	public static ApplicationData readApplicationData(Context context, Locale locale) throws InvalidFileException {
		if(instance == null){
			instance = ParseUtils.parseApplicationData(context, locale, _APP_DATA_FILE_NAME_);
		}
		return instance;
	}
	
	/**
	 * Check the app.xml file again and add the new data
	 * @param context
	 * @param str
	 * @return
	 */
	public static ApplicationData mergeAppDataFromString(Context context, String str) {
		if(instance == null){
			throw new IllegalStateException("Application Data instance is null");
		}
		ApplicationData appData = ParseUtils.parseApplicationData(context, str);
		if(appData != null){
			instance.merge(appData);
		}
		return instance;
	}
	
	
	/**
	 * Add new levels to list
	 * @param appData
	 */
	public void merge(ApplicationData appData) {
		for(AppLevel level : appData.getLevels()){
			levels.add(level);
			if(level.getId() != null){
				levelMap.put(level.getId(), level);
			}
		}
	}

	/**
	 * Returns the AppLevelDataItem associated to nextLevel
	 * @param context
	 * @param nextLevel
	 * @return
	 */
	public AppLevelDataItem getDataItem(Context context, NextLevel nextLevel){
		AppLevel appLevel = getNextAppLevel(nextLevel, context);
		AppLevelData data = appLevel.getData(context);
		AppLevelDataItem item = data.findByNextLevel(nextLevel);				
		return item;
	}

	/**
	 * Returns an ActivityGenerator with data obtained after parsing cover file
	 * @param context
	 * @return ActivityGenerator
	 */
	public ActivityGenerator getAppCoverData(Context context) {
		return getAppCoverData(context, Locale.getDefault());
	}
	
	/**
	 * Returns an ActivityGenerator with data obtained after parsing cover file
	 * @param context
	 * @param locale
	 * @return CoverActivityGenerator
	 */
	public CoverActivityGenerator getAppCoverData(Context context, Locale locale) {
		CoverParser parser = new CoverParser(ParseUtils.createXpp(
				context, 
    			locale, 
    			coverFileName, 
    			false));
		
		return parser.parse();
	}	

	/**
	 * Returns the AppLevel associated with a nextLevel
	 * @param NextLevel nextLevel
	 * @return AppLevel
	 */
	public AppLevel getNextAppLevel(NextLevel nextLevel, Context context){
		if(!isRemote())
			return localNextLevel(nextLevel);
		return remoteNextLevel(nextLevel, context);
	}

	private AppLevel remoteNextLevel(NextLevel nextLevel, Context context) {
		AppLevel localAppLevel = localNextLevel(nextLevel);
		if(localAppLevel != null)
			return localAppLevel;
		ApplicationData appData = ParseUtils.parseApplicationData(context, remoteApplicationFileUrl);
		merge(appData);
		return localNextLevel(nextLevel);
	}

	private AppLevel localNextLevel(NextLevel nextLevel) {
		AppLevel defaultLevel = checkDefaultLevel(nextLevel);
		if(defaultLevel != null)
			return defaultLevel;
		
		String levelId = nextLevel.getLevelId();
		if(levelId != null){
			return levelMap.get(levelId);
		}
		if(nextLevel.getLevelNumber() > NextLevel.NO_LEVEL && nextLevel.getLevelNumber() < levels.size())
			return levels.get(nextLevel.getLevelNumber());
		return null;
	}

	
	private AppLevel checkDefaultLevel(NextLevel nextLevel) {
		if(NextLevel.COVER_NEXT_LEVEL.equals(nextLevel)){
			return AppLevel.COVER_APP_LEVEL;
		}			
		if(NextLevel.PROFILE_NEXT_LEVEL.equals(nextLevel)){
			return AppLevel.PROFILE_APP_LEVEL;
		}
		if(NextLevel.SEARCH_NEXT_LEVEL.equals(nextLevel)){
			return AppLevel.SEARCH_APP_LEVEL;
		}
		return null;
	}

	/**
	 * Returns an ActivityGenerator (if exists), of a specific type
	 * @param Context context
	 * @param NextLevel nextLevel
	 * @return ActivityGenerator
	 */
	public ActivityGenerator getFromNextLevel(Activity activity, NextLevel nextLevel) {
		try {
			return ActivityGeneratorFactory.createActivityGenerator(activity, nextLevel);
		} catch (InvalidFileException e) {
		}
		return null;
	}

	/**
	 * Returns all images associated to one level.
	 * @param context
	 * @return
	 */
	public List<SearchResult> findAllLevelsImages(Context context){
		return findAllLevelsImages(context, Locale.getDefault());
	}

	/**
	 * Returns a list with all levelImages
	 * @param context
	 * @param locale
	 * @return
	 */
	public List<SearchResult> findAllLevelsImages(Context context, Locale locale){
		List<SearchResult> ret = new ArrayList<SearchResult>();
		for(AppLevel level : levels){
			ret.addAll(level.getAllImages(context, locale));
		}	
		return ret;
	}

	/**
	 * Returns a list with all levelImages
	 * @param context
	 * @param text
	 * @return
	 */
	public List<SearchResult> findWidthText(Context context, final String text){
		return findWidthText(context, Locale.getDefault(), text);
	}
	
	/**
	 * Returns a list with all search results
	 * @param context
	 * @param locale
	 * @param text
	 * @return
	 */
	public List<SearchResult> findWidthText(Context context, Locale locale, final String text){
		List<SearchResult> ret = new ArrayList<SearchResult>();
		for(AppLevel level : levels){
			if(ret.size() < SEARCH_LIMIT)
				ret.addAll(level.findWidthText(context, locale, text));
		}	
		return ret;
	}

	/**
	 * Returns a list with all geo-referrals
	 * @param context
	 * @return
	 */
	public List<SearchResult> findAllGeoref(Context context){
		return findAllGeoref(context, Locale.getDefault());
	}

	/**
	 * Returns a list with all geo-referrals
	 * @param context
	 * @param locale
	 * @return
	 */
	public List<SearchResult> findAllGeoref(Context context, Locale locale){
		List<SearchResult> ret = new ArrayList<SearchResult>();
		for(AppLevel level : levels){
			ret.addAll(level.findAllGeoref(context, locale));
		}	
		return ret;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setCoverFileName(String coverFileName) {
		this.coverFileName = coverFileName;
	}

	public String getCoverFileName() {
		return coverFileName;
	}
	
	public String getStylesFileName() {
		return stylesFileName;
	}

	public void setStylesFileName(String stylesFileName) {
		this.stylesFileName = stylesFileName;
	}

	public String getFormatsFileName() {
		return formatsFileName;
	}

	public void setFormatsFileName(String formatsFileName) {
		this.formatsFileName = formatsFileName;
	}
	
	
	public NextLevel getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(NextLevel entryPoint) {
		this.entryPoint = entryPoint;
	}
	
	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	
	public void setLevels(List<AppLevel> levels) {
		this.levels = levels;
		levelMap.clear();
		loadDefaultLevels();
		for(AppLevel level : levels){
			if(level.getId() != null)
				levelMap.put(level.getId(), level);
		}
	}

	public List<AppLevel> getLevels() {
		return levels;
	}
	
	public Map<ActivityType, ActivityTypeStyle> getActivityTypeStyleTypeMap(Context context) {
		if(styleResult == null){
			// Create Parser
			StyleParser styleParser = new StyleParser(ParseUtils.createXpp(
					context, 
	    			Locale.getDefault(), 
	    			this.stylesFileName, 
	    			false));
			
			styleResult = styleParser.parse();			
		}
		return styleResult.getActivityTypeStyleMap();
	}

	public Map<String, LevelStyle> getLevelStyleTypeMap(Context context) {
		if(styleResult == null){
			// Create Parser
			StyleParser styleParser = new StyleParser(ParseUtils.createXpp(
					context, 
	    			Locale.getDefault(), 
	    			this.stylesFileName, 
	    			false));
			
			styleResult = styleParser.parse();			
		}
		return styleResult.getLevelStyleMap();
	}

	public Map<String, FormatStyle> getFormatStyleMap(Context context) {
		if(formatStyleMap == null){
			// Create Parser
			FormatParser formatParser = new FormatParser(ParseUtils.createXpp(
					context, 
	    			Locale.getDefault(), 
	    			this.formatsFileName, 
	    			false));
			
			this.formatStyleMap = formatParser.parse();
		}		
		return formatStyleMap;
	}
	
	public void initStylesAndFormats(Context context){
		if(styleResult == null){
			// Create Parser
			StyleParser styleParser = new StyleParser(ParseUtils.createXpp(
					context, 
	    			Locale.getDefault(), 
	    			this.stylesFileName, 
	    			false));
			
			styleResult = styleParser.parse();			
		}
		Log.i(TAG, "Styles Initialized");
		if(formatStyleMap == null){
			// Create Parser
			FormatParser formatParser = new FormatParser(ParseUtils.createXpp(
					context, 
	    			Locale.getDefault(), 
	    			this.formatsFileName, 
	    			false));
			
			this.formatStyleMap = formatParser.parse();
		}
		Log.i(TAG, "Formats Initialized");
	}
	
	public ServerPushDataItem getServerPush() {
		return serverPush;
	}

	public void setServerPush(ServerPushDataItem serverPush) {
		this.serverPush = serverPush;
	}
	
	public BannerDataItem getBanner() {
		return banner;
	}

	public void setBanner(BannerDataItem banner) {
		this.banner = banner;
	}

	public String getProfileFileName() {
		return profileFileName;
	}

	public void setProfileFileName(String profileFileName) {
		this.profileFileName = profileFileName;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public LruCache<String, Drawable> getCache() {
		if(this.cache == null)
			this.cache = new LruCache<String, Drawable>(MAX_CACHE_SIZE);
		return cache;
	}


	public void addLevels(List<AppLevel> addingLevles) {
		for(AppLevel level : addingLevles){
			addLevel(level);
		}		
	}


	public void addLevel(AppLevel level) {
		addLevel(level, true);
	}
	
	/**
	 * Adds a new Level if the levelId is valid or if 
	 * <code>checkDefaults</code>is false.
	 * @param level
	 * @param checkDefaults
	 */
	private void addLevel(AppLevel level, boolean checkDefaults) {
		if(!checkDefaults || validLevelId(level.getId())){
			levels.add(level);
			if(level.getId() != null)
				levelMap.put(level.getId(), level);
		}else{
			Log.w("ApplicationData: addLevel", "Invalid Level Id: " + level.getId());
		}
		
	}



	private boolean validLevelId(String levelId) {
		if(EMOBC_LEVEL_ID.equals(levelId))
			return false;
		return true;
	}

	public ActivityTypeStyle getActivityTypeStyle(ActivityType activityType) {
		if(styleResult != null)
			return styleResult.getActivityTypeStyleMap().get(activityType);
		return null;
	}

	public LevelStyle getLevelStyle(String levelId) {
		if(styleResult != null)
			return styleResult.getLevelStyleMap().get(levelId);
		return null;
	}

	public boolean isRemote() {
		return remote;
	}

	// ---------------- Menues --------------------------
	public String getRemoteApplicationFileUrl() {
		return remoteApplicationFileUrl;
	}

	public void setTopMenuFileName(String topMenu) {
		this.topMenu = topMenu;
	}

	public void setBottomMenuFileName(String bottomMenu) {
		this.bottomMenu = bottomMenu;
	}

	public void setContextMenuFileName(String contextMenu) {
		this.contextMenu = contextMenu;
	}

	public void setSideMenuFileName(String sideMenu) {
		this.sideMenu = sideMenu;
	}

	private Map<String, Menu> loadMenuLevelMap(String xmlMenuFileName, Context context) {
		Map<String, Menu> ret = new HashMap<String, Menu>();
		
		if(xmlMenuFileName != null && !xmlMenuFileName.isEmpty()){
		
			// Create Menu Parser
			MenuParser menuParser = new MenuParser(ParseUtils.createXpp(
					context, 
	    			Locale.getDefault(), 
	    			xmlMenuFileName, 
	    			false));
			
			List<Menu> menues = menuParser.parse();
			
			for(Menu menu : menues){
				ret.put(menu.getLevelId(), menu);
			}
		}
		return ret;
	}
	
	/**
	 * Alex: Los menus no tienen que agregar sobre el level. 
	 * Si tiene defecto usa el de defecto y si tiene para su level solo el de su level (no suma con defecto). 
	 * Ej en la portada solo se debe de ver en el top el back e info.
	 * @param levelId
	 * @param menuLevelMap
	 * @return
	 */
	private static Menu buildLevelMenuFromMap(NextLevel nextLevel, Map<String, Menu> menuLevelMap){
		String levelId = nextLevel.getLevelId();
		
		Menu generalMenu = menuLevelMap.get(null);
		Menu menu = menuLevelMap.get(levelId);
		
		if(menu == null)
			return generalMenu;
		return menu;
//		Menu retMenu = new Menu(menu.getLevelId());
//		if(generalMenu != null)
//			retMenu.addMenuItems(generalMenu.getItems());
//		
//		retMenu.addMenuItems(menu.getItems());
//		return retMenu;
	} 
	
	public Menu getTopMenu(NextLevel nextLevel, Context context){
		if(this.topMenuLevelMap == null)
			this.topMenuLevelMap = loadMenuLevelMap(this.topMenu, context);
		return buildLevelMenuFromMap(nextLevel, topMenuLevelMap);
	}
	
	public Menu getBottomMenu(NextLevel nextLevel, Context context){
		if(this.bottomMenuLevelMap == null)
			this.bottomMenuLevelMap = loadMenuLevelMap(this.bottomMenu, context);
		return buildLevelMenuFromMap(nextLevel, bottomMenuLevelMap);
	}
	
	public Menu getContextMenu(NextLevel nextLevel, Context context){
		if(this.contextMenuLevelMap == null)
			this.contextMenuLevelMap = loadMenuLevelMap(this.contextMenu, context);
		return buildLevelMenuFromMap(nextLevel, contextMenuLevelMap);
	}
	
	public Menu getSideMenu(NextLevel nextLevel, Context context){
		if(this.sideMenuLevelMap == null)
			this.sideMenuLevelMap = loadMenuLevelMap(this.sideMenu, context);
		return buildLevelMenuFromMap(nextLevel, sideMenuLevelMap);
	}
	
	
	// PayPal
	public String getPayPalRecipient() {
		return payPalRecipient;
	}

	public void setPayPalRecipient(String payPalRecipient) {
		this.payPalRecipient = payPalRecipient;
	}

	public String getPayPalApplicationId() {
		return payPalApplicationId;
	}
	
	public void setPayPalApplicationId(String payPalApplicationId) {
		this.payPalApplicationId = payPalApplicationId;
	}
}