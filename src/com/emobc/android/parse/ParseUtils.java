/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * ParseUtils.java
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
package com.emobc.android.parse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.emobc.android.ActivityType;
import com.emobc.android.AppButton;
import com.emobc.android.ApplicationData;
import com.emobc.android.EntryPoint;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.CoverActivityGenerator;
import com.emobc.android.config.ApplicationConfiguration;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.AudioLevelDataItem;
import com.emobc.android.levels.impl.BannerDataItem;
import com.emobc.android.levels.impl.ButtonsLevelDataItem;
import com.emobc.android.levels.impl.CalendarLevelDataItem;
import com.emobc.android.levels.impl.DefaultAppLevelData;
import com.emobc.android.levels.impl.EventDataItem;
import com.emobc.android.levels.impl.FormDataItem;
import com.emobc.android.levels.impl.FormFieldType;
import com.emobc.android.levels.impl.FormLevelDataItem;
import com.emobc.android.levels.impl.ImageDataItem;
import com.emobc.android.levels.impl.ImageGalleryLevelDataItem;
import com.emobc.android.levels.impl.ImageLevelDataItem;
import com.emobc.android.levels.impl.ImageListLevelDataItem;
import com.emobc.android.levels.impl.ImageTextDescriptionLevelDataItem;
import com.emobc.android.levels.impl.ListItemDataItem;
import com.emobc.android.levels.impl.ListLevelDataItem;
import com.emobc.android.levels.impl.MapDataItem;
import com.emobc.android.levels.impl.MapLevelDataItem;
import com.emobc.android.levels.impl.PdfLevelDataItem;
import com.emobc.android.levels.impl.QrLevelDataItem;
import com.emobc.android.levels.impl.ServerPushDataItem;
import com.emobc.android.levels.impl.VideoLevelDataItem;
import com.emobc.android.levels.impl.WebLevelDataItem;
import com.emobc.android.levels.impl.quiz.QuestionDataItem;
import com.emobc.android.levels.impl.quiz.QuizAnswerDataItem;
import com.emobc.android.levels.impl.quiz.QuizLevelDataItem;
import com.emobc.android.menu.ActiveMenus;
import com.emobc.android.menu.MenuActionDataItem;
import com.emobc.android.menu.MenuActions;
import com.emobc.android.profiling.Profile;
import com.emobc.android.themes.FormatStyle;
import com.emobc.android.themes.LevelTypeStyle;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.RetreiveFileContentTask;

/**
 * Utility xml parsing of all of the application.
 * @author Jorge E. Villaverde
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 **/
public class ParseUtils {
	private static final String _FIELD_PARAM_TAG_ = "fieldParam";
	private static final String _REQUIRED_TAG_ = "required";
	private static final String _FIELD_NAME_TAG_ = "fieldName";
	private static final String _FIELD_LABEL_TAG_ = "fieldLabel";
	private static final String _FIELD_TYPE_TAG_ = "fieldType";
	private static final String _ACTION_URL_TAG_ = "actionUrl";
	private static final String _FIELD_TAG_ = "field";
	private static final String _FORM_TAG_ = "form";
	private static final String _BUTTONS_TAG_ = "buttons";
	private static final String _BUTTON_TAG_ = "button";	
	private static final String _BUTTON_TITLE_TAG_ = "buttonTitle";	
	private static final String _BUTTON_FILE_NAME_TAG_ = "buttonFileName";	
	private static final String _APPLICATION_TAG_ = "application";
	private static final String _LEVELS_TAG_ = "levels";
	private static final String _TITLE_FIELD_ = "title";
	private static final String _COVER_FILE_NAME_ = "coverFileName";
	private static final String _STYLES_FILE_NAME_TAG_ = "stylesFileName";
	private static final String _FORMATS_FILE_NAME_TAG_ = "formatsFileName";
	private static final String _PROFILE_FILE_NAME_TAG_ = "profileFileName";
	
	private static final String _FACEBOOK_TAG_ = "facebook";
	private static final String _TWITTER_TAG_ = "twitter";
	private static final String _WWW_TAG_ = "www";
	
	private static final String _ENTRY_POINT_TAG_ = "entryPoint";
	private static final String _POINT_LEVEL_ID_TAG_ = "pointLevelId";
	private static final String _POINT_DATA_ID_TAG_ = "pointDataId";
	
	private static final String _ROTATION_TAG_ = "rotation";
	
	private static final String _MENU_TAG_ = "menu";
	private static final String _TOP_MENU_TAG_ = "topMenu";
	private static final String _BOTTOM_MENU_TAG_ = "bottomMenu";
	private static final String _CONTEXT_MENU_TAG_ = "contextMenu";
	private static final String _SIDE_MENU_TAG_ = "sideMenu";
	
	private static final String _MENU_ACTIONS_TAG_ = "menuActions";
	private static final String _ACTION_TAG_ = "action";
	private static final String _ACTION_TITLE_TAG_ = "actionTitle";
	private static final String _ACTION_IMAGE_NAME_TAG_ = "actionImageName";
	private static final String _SYSTEM_ACTION_TAG_ = "systemAction";
	private static final String _LEFT_MARGIN_TAG_ = "leftMargin";
	private static final String _WIDTH_BUTTON_TAG_ = "widthButton";
	private static final String _HEIGHT_BUTTON_TAG_ = "heightButton";
	
	private static final String _STYLES_TAG_ = "styles";
	private static final String _TYPEID_TAG_ = "typeId";
	private static final String _BACKGROUND_FILE_NAME_TAG_ = "backgroundFileName";
	private static final String _COMPONENTS_TAG_ = "components";
	
	private static final String _FORMATS_TAG_ = "formats";
	private static final String _FORMAT_TAG_ = "format";
	private static final String _NAME_TAG_ = "name";
	private static final String _TEXTCOLOR_TAG_ = "textColor";
	private static final String _TEXTSIZE_TAG_ = "textSize";
	private static final String _TEXTSTYLE_TAG_ = "textStyle";
	private static final String _TYPEFACE_TAG_ = "typeFace";
	private static final String _CACHE_COLOR_HINT_TAG_ = "cacheColorHint";
	private static final String _BACKGROUND_COLOR_TAG_ = "backgroundColor";
	private static final String _BG_SELECTION_FILE_NAME_TAG_ = "backgroundSelectionFileName";
	
	private static final String _LEVEL_TAG_ = "level";
	private static final String _LEVEL_TITLE_ = "levelTitle";
	private static final String _LEVEL_FILE_ = "levelFile";
	private static final String _LEVEL_USE_PROFILE_ = "levelUseProfile";
	private static final String _LEVEL_TYPE_ = "levelType";
	
	private static final String _BG_FILE_NAME_ = "backgroundFileName";
	private static final String _TITLE_FILE_NAME_ = "titleFileName";
	private static final String _NEXT_LEVEL_TAG_ = "nextLevel";
	private static final String _LEVEL_NUMBER_TAG_ = "levelNumber";
	private static final String _LEVEL_ID_TAG_ = "levelId";
	private static final String _GEO_REF_TAG_ = "geoRef";
	private static final String _COLUMNS_TAG_ = "columns";
		
	private static final String _LEVEL_DATA_TAG_ = "levelData";
	private static final String _DATA_NUMBER_TAG_ = "dataNumber";
	private static final String _DATA_ID_TAG_ = "dataId";
	
	private static final String _NL_LEVEL_ID_TAG_ = "nextLevelLevelId";
	private static final String _NL_DATA_ID_TAG_ = "nextLevelDataId";

	private static final String _DATA_TAG_ = "data";

	private static final String _HEADER_IMAGE_FILE_TAG_ = "headerImageFile";
	private static final String _HEADER_TEXT_TAG_ = "headerText";

	private static final String _ORDER_TAG_ = "order";
	
	private static final String _IMAGE_FILE_TAG_ = "imageFile";
	private static final String _TEXT_TAG_ = "text";
	private static final String _BAR_TEXT_TAG_ = "barText";
	
	private static final String _LIST_TAG_ = "list";
	private static final String _LIST_ITEM_TAG_ = "listItem";
	
	private static final String _VIDEO_PATH_TAG_ = "videoUrl";
	private static final String _GALERRY_TAG_ = "galery";
	private static final String _LOCAL_TAG_ = "local";
	private static final String _WEB_URL_TAG_ = "webUrl";
	private static final String _LEVEL_XIB_TAG_ = "levelXib";	
	
	/*------------- MAP_LEVEL CONSTANTS -----------------------------*/
	private static final String _POSITION_TAG_ = "position";
	private static final String _POSITION_TITLE_TAG_ = "positionTitle";
	private static final String _POSITION_ADDRESS_TAG_ = "positionAddress";
	private static final String _POSITION_LAT_TAG_ = "positionLat";
	private static final String _POSITION_LON_TAG_ = "positionLon";
	private static final String _POSITION_IMAGE_TAG_ = "imageFile";
	private static final String _POSITION_ICON_TAG_ = "iconFile";
	private static final String _POSITION_CURR_ICON_TAG_ = "currentPositionIconFile";
	private static final String _POSITION_LOCALIZE_ME_TAG_ = "localizeMe";
	private static final String _POSITION_SHOW_ALL_TAG_ = "showAll";
	
	/*------------- AUDIO_LEVEL CONSTANTS -----------------------------*/
	private static final String _AUDIO_URL_TAG_ = "audioUrl";
	private static final String _LYRICS_TAG_ = "lyrics";
	
	/*------------- PDF_LEVEL CONSTANTS -----------------------------*/
	private static final String _PDF_URL_TAG_ = "pdfUrl";
	
	/*------------- CALENDAR CONSTANTS -----------------------------*/
	private static final String _EVENTS = "events";
	private static final String _EVENT = "event";
	private static final String _EVENT_DATE = "eventDate";
	private static final String _TIME = "time";
	
	/*------------- BANNER CONSTANTS -----------------------------*/
	private static final String _BANNER_TAG_ = "banner";
	private static final String _TYPE_TAG_ = "type";
	private static final String _ID_TAG_= "id";
	
	/*------------- GCM CONSTANTS -----------------------------*/
	private static final String _PUSH_TAG_ = "push";
	private static final String _SERVER_TAG_ = "server";
	private static final String _SENDER_TAG_ = "sender";
	private static final String _APPNAME_TAG_ = "appname";
	
	/*------------- QUIZ CONSTANTS -----------------------------*/
	private static final String _TIME_TAG_ = "time";
	private static final String _FIRST_TAG_ = "first";
	private static final String _DESCRIPTION_TAG = "description";
	private static final String _QUESTIONS_TAG_ = "questions";
	private static final String _QUESTION_TAG_ = "question";
	private static final String _WEIGHT_TAG_ = "weight";
	private static final String _ANSWER_TEXT_TAG = "answerText";
	private static final String _ANSWER_TAG_ = "answer";
	private static final String _CORRECT_TAG_ = "correct";
	private static final String _NEXT_TAG_ = "next";
	
	/*-------------PROFILE CONSTANTS----------------------------*/
	private static final String _PROFILE_TAG_ = "profile";
	
	/*-------------QR CONSTANCTS--------------------------------*/
	private static final String _ID_QR_TAG_ = "idQr";
	private static final String _QR_TAG_ = "qr";
	
	// -- APPLICATION DATA --
	
	/**
	 * This method is used to make the ride app.xml 
	 * file, passed in the parameter xmlFileName. 
	 * @param context
	 * @param locale
	 * @param xmlFileName
	 * @return ApplicationData
	 */
	public static ApplicationData parseApplicationData(Context context, Locale locale, String xmlFileName) {
		XmlPullParser xpp = createXpp(context, locale, xmlFileName, false);
		if(xpp == null)
			return null;
		
		ApplicationConfiguration config = null;
		try {
			config = ApplicationConfiguration.readConfiguration(context);
		} catch (InvalidFileException e) {
		}
		return fromData(parseApplicationFile(xpp), config);
	}
	
	/**
	 * This method is used to make the ride app.xml 
	 * file, passed in the parameter xmlFileName. 
	 * @param context
	 * @param str
	 * @return
	 */
	public static ApplicationData parseApplicationData(Context context, String str) {
		XmlPullParser xpp = createXppFromString(context, str);
		if(xpp == null)
			return null;
		
		ApplicationConfiguration config = null;
		try {
			config = ApplicationConfiguration.readConfiguration(context);
		} catch (InvalidFileException e) {
		}
		return fromData(parseApplicationFile(xpp), config);
	}
	
	/**
	 * Creates a xpp from a string value
	 * @param context
	 * @param str
	 * @return
	 */
	private static XmlPullParser createXppFromString(Context context, String str) {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(str.getBytes("UTF-8"));

		} catch (UnsupportedEncodingException e) {
			Log.e("ApplicactionData", e.getLocalizedMessage());
	    	//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return createXpp(context, is);
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the app.xml
	 * @param xpp
	 * @return Map<String, Object>
	 */
	private static Map<String, Object> parseApplicationFile(XmlPullParser xpp){
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
				new NwXmlStandarParserTextHandler() {
					private EntryPoint entryP;
					private ActiveMenus activeMenus;
					private AppLevel currLevel = null;
					private LevelTypeStyle currLevTypeStyle;
					private List<AppLevel> levels = null;
					private BannerDataItem currBanner;
					private ServerPushDataItem currpushServer;
					
					@Override
					public void handleText(String currentField, String text) {
						
						if(currentField.equals(_ENTRY_POINT_TAG_)){
							entryP = new EntryPoint();
							ret.put(currentField, entryP);
						}else if(currentField.equals(_POINT_LEVEL_ID_TAG_)){
							entryP.setLevelId(text);
						}else if(currentField.equals(_POINT_DATA_ID_TAG_)){
							entryP.setDataId(text);
						}else 
						
						if(currentField.equals(_MENU_TAG_)){
							activeMenus = new ActiveMenus();
							ret.put(currentField, activeMenus);
						}else if(currentField.equals(_TOP_MENU_TAG_)){
							activeMenus.setTopMenu(text);
						}else if(currentField.equals(_BOTTOM_MENU_TAG_)){
							activeMenus.setBottomMenu(text);
						}else if(currentField.equals(_CONTEXT_MENU_TAG_)){
							activeMenus.setContextMenu(text);
						}else if(currentField.equals(_SIDE_MENU_TAG_)){
							activeMenus.setSideMenu(text);
						}else 		
								
						if(currentField.equals(_STYLES_FILE_NAME_TAG_)){
							ret.put(currentField, text);
						}else if(currentField.equals(_FORMATS_FILE_NAME_TAG_)){
							ret.put(currentField, text);
						}else if (_PROFILE_FILE_NAME_TAG_.equals(currentField)) {
							ret.put(currentField, text);
						}else if (currentField.equals("levelFormat")) {
							currLevTypeStyle = new LevelTypeStyle();
							currLevel.setLevelTypeStyle(currLevTypeStyle);
						}else if(currentField.equals(_BACKGROUND_FILE_NAME_TAG_)){
							currLevTypeStyle.setBackground(text);
						}else if(currentField.equals(_COMPONENTS_TAG_)){
							currLevTypeStyle.setComponents(text);
						}else 
							//---------------Banner-----------------//
						if(currentField.equals(_BANNER_TAG_)){
							currBanner = new BannerDataItem();
							currBanner.setType("gcm");
							ret.put(currentField, currBanner);
						}else if(currentField.equals(_POSITION_TAG_)){
							currBanner.setPosition(text);
						}else if(currentField.equals(_ID_TAG_)){
							currBanner.setId(text);
                    
							//---------------Push-----------------//
						}else if(currentField.equals(_PUSH_TAG_)){
							currpushServer = new ServerPushDataItem();
							ret.put(currentField, currpushServer);
							currpushServer.setType("gcm");
						}else if(currentField.equals(_TYPE_TAG_)){

						}else if (currentField.equals(_APPNAME_TAG_)){
							currpushServer.setAppName(text);
						}else if(currentField.equals(_SERVER_TAG_)){
							currpushServer.setServerUrl(text);
						}else if(currentField.equals(_SENDER_TAG_)){
							currpushServer.setSenderId(text);
							
						//---------------LEVELS-----------------//
						}else if(currentField.equals(_LEVELS_TAG_)){
							levels = new ArrayList<AppLevel>();
							ret.put(currentField, levels);
						}else if(currentField.equals(_LEVEL_TAG_)){
							currLevel = new AppLevel(levels.size());
							levels.add(currLevel);
						}else if(currentField.equals(_LEVEL_ID_TAG_)){
							currLevel.setId(text);
						}else if(currentField.equals(_LEVEL_TITLE_)){
							currLevel.setTitle(text);
						}else if(currentField.equals(_LEVEL_FILE_)){
							currLevel.setFileName(text);
						}else if(currentField.equals(_LEVEL_USE_PROFILE_)){
							currLevel.setUseProfile(Boolean.parseBoolean(text));
						}else if(currentField.equals(_LEVEL_TYPE_)){
							currLevel.setActivityType(ActivityType.valueOf(text));
						}else if(currentField.equals(_LEVEL_XIB_TAG_)){
							currLevel.setXib(text);
						}else{
							ret.put(currentField, text);
						}
						
					}
					
					@Override
					public void handleEndTag(String currentField) {						
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _APPLICATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Based on the mapping of XmlPullParser, ApplicationData returns the 
	 * values​​: TITLE_FIELD, COVER_FILE_NAME and LEVELS_TAG
	 * @param data
	 * @return ApplicationData
	 */
	@SuppressWarnings("unchecked")
	private static ApplicationData fromData(Map<String, Object> data, ApplicationConfiguration config) {
		ApplicationData ret = null;
		if(config != null){
			ret = new ApplicationData((String)config.getAttribute(ApplicationConfiguration.REMOTE_APP_URL));
		}else {
			ret = new ApplicationData();
		}
		ret.setEntryPoint((EntryPoint)data.get(_ENTRY_POINT_TAG_));
		
		ret.setMenu((ActiveMenus)data.get(_MENU_TAG_));

		//Banner
		ret.setBanner((BannerDataItem) data.get(_BANNER_TAG_));
		
		//Push
		ret.setServerPush((ServerPushDataItem) data.get(_PUSH_TAG_));

		ret.setTitle((String)data.get(_TITLE_FIELD_));
		ret.setCoverFileName((String)data.get(_COVER_FILE_NAME_));
		ret.setRotation((String)data.get(_ROTATION_TAG_));		
		ret.setStylesFileName((String)data.get(_STYLES_FILE_NAME_TAG_));
		ret.setFormatsFileName((String)data.get(_FORMATS_FILE_NAME_TAG_));
		ret.setProfileFileName((String)data.get(_PROFILE_FILE_NAME_TAG_));
		List<AppLevel> parsedLevels = (List<AppLevel>)data.get(_LEVELS_TAG_);
		
		if(parsedLevels != null){
			ret.addLevels(parsedLevels);
		}			
		
		Log.v("ParseUtils", "app.xml parsed");
		return ret;
	}
	
	
	// -- COVER ACTIVITY --
	
	/**
	 * This method is used to make the ride home file "coverFileName" 
	 * passed in the parameter xmlFileName.
	 * @param context
	 * @param locale
	 * @param xmlFileName
	 * @return CoverActivityGenerator
	 */
	public static CoverActivityGenerator parseAppCoverData(Context context, Locale locale, String xmlFileName) {
		XmlPullParser xpp = createXpp(context, locale, xmlFileName, false);
		if(xpp != null)
			return fromAppCoverData(parseAppCoverDataFile(xpp));
		return null;
	}
	
	/**
	 * Generates a new XmlPullParser through a String (InputStream is created 
	 * from the input data)-Generally, this method is called in 
	 * parseAppCoverData (), parseAplicationData (), and parseLevelData (). 
	 * This in turn makes use of createXpp () which supports InputStream.
	 * @param context
	 * @param locale
	 * @param xmlFileName
	 * @param usePostMethod 
	 * @return XmlPullParser
	 */
	public static XmlPullParser createXpp(Context context, Locale locale, String xmlFileName, boolean usePostMethod){
		if(xmlFileName == null || xmlFileName.isEmpty())
			return null;
		InputStream is = null;
		if(xmlFileName.startsWith("http://") || xmlFileName.startsWith("https://")){
			is = contentInputStreamFromUri(context, xmlFileName, usePostMethod);
		}else{
			is = contentInputStreamFromAssets(context, locale, xmlFileName); 
		}	
		if(is != null)
			return createXpp(context, is);
		return null;
	}

	/**
	 * @param context
	 * @param locale
	 * @param xmlFileName
	 * @param is
	 * @return
	 */
	protected static InputStream contentInputStreamFromAssets(Context context, Locale locale, String xmlFileName) {
		InputStream is = null;
		try {
			String localeXmlFileName = locale.getLanguage() + File.separator + xmlFileName;
			is = context.getAssets().open(localeXmlFileName);
		} catch (IOException e) {
			Log.e("ParseUtils", e.getLocalizedMessage());
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			try {
				is = context.getAssets().open(xmlFileName);
			} catch (IOException e1) {
				Log.e("ParseUtils", e1.getLocalizedMessage());
			}
		}
		return is;
	}

	/**
	 * @param xmlFileName
	 * @param usePostMethod 
	 * @return
	 */
	protected static InputStream contentInputStreamFromUri(Context context, String xmlFileName, boolean usePostMethod) {
		InputStream is = null;
		try {
			URL url = new URL(xmlFileName);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			url = uri.toURL();
			
			if(!Profile.isFilled(context)){
				//url = new URL(encodedUrl);
				is = (InputStream)url.getContent();
			}else{
				RetreiveFileContentTask task = new RetreiveFileContentTask(Profile.createNamedParameters(context), usePostMethod); 
				task.execute(url);
				String text = task.get();
				is = new ByteArrayInputStream(text.getBytes("UTF-8"));		
			}
		} catch (MalformedURLException e) {
			Log.e("ParseUtils", e.getLocalizedMessage());
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}catch (FileNotFoundException e) {	
			Log.e("ParseUtils", "No se puede encontrar el archivo: " + e.getLocalizedMessage());
			//Toast.makeText(context, "No se puede encontrar el archivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}catch (IOException e) {
			Log.e("ParseUtils", e.getLocalizedMessage());
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (URISyntaxException e) {
			Log.e("ParseUtils", "URISyntaxException: " + e.getLocalizedMessage());
			//Toast.makeText(context, "URISyntaxException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (InterruptedException e) {
			Log.e("ParseUtils", "InterruptedException: " + e.getLocalizedMessage());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * Generates a new XmlPullParser through an InputStream "is" 
	 * that serve to easily parse an xml file
	 * @param Context context
	 * @param InputStream is
	 * @return XmlPullParser
	 */
	public static XmlPullParser createXpp(Context context, InputStream is){
		if(is == null)
			throw new InvalidParameterException("InputStream = null");
			
		XmlPullParser xpp = null;
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			xpp =  factory.newPullParser();
			xpp.setInput(is, "UTF-8");
		}catch (XmlPullParserException e) {
			Log.e("ParseUtils", e.getLocalizedMessage());
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return xpp;		
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful to cover
	 * @param XmlPullParser xpp
	 * @return Map<String, Object>
	 */
	private static Map<String, Object> parseAppCoverDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
				new NwXmlStandarParserTextHandler() {
					private List<AppButton> buttons = null;
					private AppButton currentButton = null;
					private NextLevel nextLevel = null;
					@Override
					public void handleText(String currentField, String text) {
							
						if(currentField.equals(_BUTTONS_TAG_)){
							buttons = new ArrayList<AppButton>();
							ret.put(_BUTTONS_TAG_, buttons);
						}else if(currentField.equals(_BUTTON_TAG_)){
							currentButton = new AppButton();
							buttons.add(currentButton);
						}else if(currentField.equals(_BUTTON_TITLE_TAG_)){
							currentButton.setTitle(text);
						}else if(currentField.equals(_BUTTON_FILE_NAME_TAG_)){
							currentButton.setFileName(text);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currentButton.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {						
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Based on the mapping of XmlPullParser, CoverActivityGenerator returns 
	 * the values​​: BG_FILE_NAME, TITLE_FILE_NAME, BUTTONS_TAG, FACEBOOK_TAG, 
	 * TWITTER_TAG, WWW_TAG
	 * @param data
	 * @return CoverActivityGenerator
	 */
	@SuppressWarnings("unchecked")
	private static CoverActivityGenerator fromAppCoverData(Map<String, Object> data) {
		CoverActivityGenerator ret = new CoverActivityGenerator();

		ret.setBackgroundFileName((String)data.get(_BG_FILE_NAME_));
		ret.setTitleFileName((String)data.get(_TITLE_FILE_NAME_));
		List<AppButton> buttons = (List<AppButton>)data.get(_BUTTONS_TAG_);
		ret.setFacebookUrl((String)data.get(_FACEBOOK_TAG_));
		ret.setTwitterUrl((String)data.get(_TWITTER_TAG_));
		ret.setWwwUrl((String)data.get(_WWW_TAG_));
				
		if(buttons != null){
			ret.setButtons(Collections.unmodifiableList(buttons));
		}		
		return ret;
	}
	
	
	// -- MENU --
	
	/**
	 * This method is used to make the ride home file "top_menu|bottom_menu|
	 * context_menu|side_menu" 
	 * passed in the parameter xmlFileName.
	 * @param context
	 * @param xmlFileName
	 * @return
	 */
	public static MenuActions parseMenuData(Context context, String xmlFileName) throws InvalidFileException {
		XmlPullParser xpp = createXpp(context, Locale.getDefault(), xmlFileName, false);
		if(xpp == null)
			throw new InvalidFileException(String.format("File %s does not exits", xmlFileName));
		Map<String, Object> data = parseMenuDataFile(xpp);  
		return fromMenuData(data);
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the menu
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseMenuDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private MenuActionDataItem currItem;
					private NextLevel nextLevel;
					private List<MenuActionDataItem> currList;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_MENU_ACTIONS_TAG_)){
							currList = new ArrayList<MenuActionDataItem>();
							ret.put(_MENU_ACTIONS_TAG_, currList);
						}else if(currentField.equals(_ACTION_TAG_)){
							currItem = new MenuActionDataItem();
							currList.add(currItem);
						}else if(currentField.equals(_ACTION_TITLE_TAG_)){
							currItem.setTitle(text);
						}else if(currentField.equals(_ACTION_IMAGE_NAME_TAG_)){
							currItem.setImageName(text);
						}else if(currentField.equals(_SYSTEM_ACTION_TAG_)){
							currItem.setSystemAction(text);
						}else if(currentField.equals(_LEFT_MARGIN_TAG_)){
							currItem.setLeftMargin(Integer.parseInt(text));
						}else if(currentField.equals(_WIDTH_BUTTON_TAG_)){
							currItem.setWidthButton(Integer.parseInt(text));
						}else if(currentField.equals(_HEIGHT_BUTTON_TAG_)){
							currItem.setHeightButton(Integer.parseInt(text));
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currItem.setNextLevel(nextLevel);
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
					}
					
					@Override
					public void handleBeginTag(String currentField) {
					}
				}
		, _MENU_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Based on the mapping of XmlPullParser, returns the value MENU_ACTIONS
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static MenuActions fromMenuData(Map<String, Object> data) {
		MenuActions ret = new MenuActions();
				
		ret.setList((List<MenuActionDataItem>)data.get(_MENU_ACTIONS_TAG_));

		return ret;
	}
	
	// -- STYLES --
	
	/**
	 * This method is used to make the ride home file "styles.xml" 
	 * passed in the parameter xmlFileName.
	 * @param context
	 * @param xmlFileName
	 * @return
	 */
	public static Map<ActivityType, LevelTypeStyle> parseStylesData(Context context, String xmlFileName) {
		XmlPullParser xpp = createXpp(context, Locale.getDefault(), xmlFileName, false);
		if(xpp != null){

			Map<String, Object> data = parseStylesDataFile(xpp);  
			return fromStylesData(data);
		}
		return null;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the styles
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseStylesDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private LevelTypeStyle currItem;
					private List<LevelTypeStyle> currList;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_STYLES_TAG_)){
							currList = new ArrayList<LevelTypeStyle>();
							ret.put(currentField, currList);
						}else if(currentField.equals(_TYPE_TAG_)){
							currItem = new LevelTypeStyle();
							currList.add(currItem);
						}else if(currentField.equals(_TYPEID_TAG_)){
							currItem.setLevelType(ActivityType.formString(text));
						}else if(currentField.equals(_BACKGROUND_FILE_NAME_TAG_)){
							currItem.setBackground(text);
						}else if(currentField.equals(_COMPONENTS_TAG_)){
							currItem.setComponents(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
					}
					
					@Override
					public void handleBeginTag(String currentField) {
					}
				}
		, _APPLICATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}

	/**
	 * Based on the mapping of XmlPullParser, returns a map with names and LevelTypeStyle
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<ActivityType, LevelTypeStyle> fromStylesData(Map<String, Object> data) {
		Map<ActivityType, LevelTypeStyle> ret = new HashMap<ActivityType, LevelTypeStyle>();
		Iterator<LevelTypeStyle> list = ((List<LevelTypeStyle>)data.get(_STYLES_TAG_)).iterator();
				
		while(list.hasNext()){
			LevelTypeStyle currLevelTypeStyle = list.next();
			ret.put(currLevelTypeStyle.getLevelType(), currLevelTypeStyle);
		}
		
		return ret;
	}
	
	
	// -- FORMATS --
	
	/**
	 * This method is used to make the ride home file "formats.xml" 
	 * passed in the parameter xmlFileName.
	 * @param context
	 * @param xmlFileName
	 * @return
	 */
	public static Map<String, FormatStyle> parseFormatData(Context context, String xmlFileName) {
		XmlPullParser xpp = createXpp(context, Locale.getDefault(), xmlFileName, false);
		if(xpp != null){

			Map<String, Object> data = parseFormatDataFile(xpp);  
			return fromFormatData(data);
		}
		return null;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the formats
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseFormatDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private FormatStyle currItem;
					private List<FormatStyle> currList;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_FORMATS_TAG_)){
							currList = new ArrayList<FormatStyle>();
							ret.put(currentField, currList);
						}else if(currentField.equals(_FORMAT_TAG_)){
							currItem = new FormatStyle();
							currList.add(currItem);
						}else if(currentField.equals(_NAME_TAG_)){
							currItem.setName(text);
						}else if(currentField.equals(_TEXTCOLOR_TAG_)){
							currItem.setTextColor(text);
						}else if(currentField.equals(_TEXTSIZE_TAG_)){
							currItem.setTextSize(text);
						}else if(currentField.equals(_TEXTSTYLE_TAG_)){
							currItem.setTextStyle(text);
						}else if(currentField.equals(_TYPEFACE_TAG_)){
							currItem.setTypeFace(text);
						}else if(currentField.equals(_CACHE_COLOR_HINT_TAG_)){
							currItem.setCacheColorHint(text);
						}else if(currentField.equals(_BACKGROUND_COLOR_TAG_)){
							currItem.setBackgroundColor(text);
						}else if(currentField.equals(_BG_SELECTION_FILE_NAME_TAG_)){
							currItem.setBackgroundSelectionFileName(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
					}
					
					@Override
					public void handleBeginTag(String currentField) {
					}
				}
		, _APPLICATION_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Based on the mapping of XmlPullParser, returns a map with names and FormatStyle
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, FormatStyle> fromFormatData(Map<String, Object> data) {
		Map<String, FormatStyle> ret = new HashMap<String, FormatStyle>();
		Iterator<FormatStyle> list = ((List<FormatStyle>)data.get(_FORMATS_TAG_)).iterator();
				
		while(list.hasNext()){
			FormatStyle currFormat = list.next();
			ret.put(currFormat.getName(), currFormat);
		}
		
		return ret;
	}
	
	
	// -- OTHER ACTIVITIES --
		
	/**
	 * This method is used to make the ride home file "LEVEL.xml" 
	 * passed in the parameter xmlFileName.
	 * @param context
	 * @param locale
	 * @param xmlFileName
	 * @param activityType
	 * @return
	 */
	public static AppLevelData parseLevelData(Context context, Locale locale, String xmlFileName, ActivityType activityType) {
		return parseLevelData(context, locale, xmlFileName, activityType, false);
	}
	
	public static AppLevelData parseLevelData(Context context, Locale locale, String xmlFileName, ActivityType activityType, boolean usePostMethod) {
		XmlPullParser xpp = createXpp(context, locale, xmlFileName, usePostMethod);
		if(xpp != null){
			Map<String, Object> data = parseLevelDataFile(xpp, activityType); 
			return fromLevelData(data);
		}
		return null;		
	}
	
	/**
	 * Selects the parse corresponding to ActivityType
	 * @param xpp
	 * @param activityType
	 * @return
	 */
	private static Map<String, Object> parseLevelDataFile(XmlPullParser xpp, ActivityType activityType) {
		switch (activityType) {
		case IMAGE_TEXT_DESCRIPTION_ACTIVITY:
			return parseImageTextLevelDataFile(xpp);
		case IMAGE_LIST_ACTIVITY:
			return parseImageListLevelDataFile(xpp);
		case LIST_ACTIVITY:
			return parseListLevelDataFile(xpp);
		case VIDEO_ACTIVITY:
			return parseVideoLevelDataFile(xpp);
		case IMAGE_ZOOM_ACTIVITY:
			return parseImageZoomLevelDataFile(xpp);
		case IMAGE_GALLERY_ACTIVITY:
			return parseImageGaleryLevelDataFile(xpp);
		case WEB_ACTIVITY:
			return parseWebLevelDataFile(xpp);
		case QR_ACTIVITY:
			return parseQrLevelDataFile(xpp);
		case FORM_ACTIVITY:
			return parseFormLevelDataFile(xpp);
		case MAP_ACTIVITY:
			return parseMapLevelDataFile(xpp);
		case PDF_ACTIVITY:
			return parsePdfLevelDataFile(xpp);
		case CALENDAR_ACTIVITY:
			return parseCalendarLevelDataFile(xpp);
		case QUIZ_ACTIVITY:
			return parseQuizLevelDataFile(xpp);
		case AUDIO_ACTIVITY:
			return parseAudioLevelDataFile(xpp);
		case BUTTONS_ACTIVITY:
			return parseButtonsLevelDataFile(xpp);
		case CANVAS_ACTIVITY:
			return parseButtonsLevelDataFile(xpp);
		default:
			break;
		}
		Log.e("ParseUtils", "No se encuentre el parser para el tipo: " + activityType.toString());
		return null;
	}

	/**
	 * Generates a table of elements of XmlPullParser useful for the <tt>Profile Activity</tt>.
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseProfileDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp, 
				new NwXmlStandarParserTextHandler() {
			private Profile profile = new Profile();
			private List<FormDataItem> currList;
			private FormDataItem currFormItem;
			
			@Override
			public void handleText(String currentField, String text) {

				if(currentField.equals(_DATA_TAG_)){
					currList = new ArrayList<FormDataItem>();
					profile.setFields(currList);									
				}else if(currentField.equals(_FIELD_TAG_)){
					currFormItem = new FormDataItem();
					if(currList == null){
						currList = new ArrayList<FormDataItem>();
						profile.setFields(currList);			
					}
					currList.add(currFormItem);
				}else if(currentField.equals(_FIELD_TYPE_TAG_)){
					currFormItem.setType(FormFieldType.fromText(text));
				}else if(currentField.equals(_FIELD_LABEL_TAG_)){
					currFormItem.setFieldLabel(text);
				}else if(currentField.equals(_FIELD_NAME_TAG_)){
					currFormItem.setFieldName(text);
				}else if(currentField.equals(_REQUIRED_TAG_)){
					currFormItem.setRequired("true".equalsIgnoreCase(text));
				}else if(currentField.equals(_FIELD_PARAM_TAG_)){
					currFormItem.getParameters().add(text);
				}
			}

			@Override
			public void handleEndTag(String currentField) {
				if(currentField.equals(_PROFILE_TAG_)){
					ret.put(_PROFILE_TAG_, profile);
				}
			}

			@Override
			public void handleBeginTag(String currentField) {
			}
		
		}, _PROFILE_TAG_);
		
		parser.startParsing();
		
		return ret;
	}

	/**
	 * Generate a table of the elements of XmlPullParser useful for the Calendar
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseCalendarLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private CalendarLevelDataItem currItem;
					private HashMap<String,TreeSet<EventDataItem>> currEventsMap;
					private TreeSet<EventDataItem> currTree;
					private EventDataItem currEvent;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new CalendarLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);	
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_EVENTS)){
							currEventsMap = new HashMap<String,TreeSet<EventDataItem>>();
							currItem.setEvents(currEventsMap);
						}else if(currentField.equals(_EVENT)){
							currEvent = new EventDataItem();
						}else if(currentField.equals(_TITLE_FIELD_)){
							currEvent.setTitle(text);
						}else if(currentField.equals(_EVENT_DATE)){
							currEvent.setEventDate(text);
							//Now we can put the event in to the map (with key = date)
							currTree = currEventsMap.get(text);
							if (currTree==null){
								currTree = new TreeSet<EventDataItem>();
							}
							currEventsMap.put(text, currTree);
						}else if(currentField.equals(_TIME)){
							currEvent.setTime(text);
							currTree.add(currEvent);
						}else if(currentField.equals(_TEXT_TAG_)){
							currEvent.setDescription(text);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currEvent.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}

	/**
	 * Generate a table of the elements of XmlPullParser useful for the Buttons Screen
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseButtonsLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ButtonsLevelDataItem currItem;
					private List<AppButton> currList;
					private AppButton currButton;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ButtonsLevelDataItem();
							appLevelData.addItem(currItem);	
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_COLUMNS_TAG_)){
							int columns;
							try {
								columns = Integer.parseInt(text);
								currItem.setColumns(columns );
							} catch (NumberFormatException e) {
							}
						}else if(currentField.equals(_BUTTONS_TAG_)){
							currList = new ArrayList<AppButton>();
							currItem.setButtonList(currList);
						}else if(currentField.equals(_BUTTON_TAG_)){
							currButton = new AppButton();
							currList.add(currButton);
						}else if(currentField.equals(_BUTTON_TITLE_TAG_)){
							currButton.setTitle(text);
						}else if(currentField.equals(_BUTTON_FILE_NAME_TAG_)){
							currButton.setFileName(text);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currButton.setNextLevel(nextLevel);
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Gallery
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseImageGaleryLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ImageGalleryLevelDataItem currItem;
					private List<ImageDataItem> currList;
					private ImageDataItem currImage;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ImageGalleryLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_GALERRY_TAG_)){
							currList = new ArrayList<ImageDataItem>();
							currItem.setList(currList);
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							currImage = new ImageDataItem();
							currImage.setImageFile(text);
							currList.add(currImage);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currImage.setNextLevel(nextLevel);
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	 /**
     * Method for parse a Quiz information xml
     * @param xpp
     * @return
     */
    private static Map<String, Object> parseQuizLevelDataFile(XmlPullParser xpp) {
        final Map<String, Object> ret = new HashMap<String, Object>();
        
        NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
                new NwXmlStandarParserTextHandler() {
                    private AppLevelData appLevelData = new DefaultAppLevelData();
                    private QuizLevelDataItem currItem;
                    private QuizAnswerDataItem currAnswer;
                    private QuestionDataItem currQuestion;
                    
                    @Override
                    public void handleText(String currentField, String text) {
                        if(currentField.equals(_DATA_TAG_)){
                            currItem = new QuizLevelDataItem();
                            appLevelData.addItem(currItem);                            
                        }else if(currentField.equals(_DATA_ID_TAG_)){
                            currItem.setId(text);    
                        }else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
                            currItem.setHeaderImageFile(text);
                        }else if(currentField.equals(_HEADER_TEXT_TAG_)){
                            currItem.setHeaderText(text);
                        }else if(currentField.equals(_DESCRIPTION_TAG)){
                            currItem.setDescription(text);
                        }else if(currentField.equals(_TIME_TAG_)){
                            currItem.setTime(text);
                        }else if(currentField.equals(_FIRST_TAG_)){
                            currItem.setFirst(text);
                        }else if(currentField.equals(_QUESTIONS_TAG_)){
                            //currQuestionsList = new ArrayList<QuestionDataItem>();
                            //currItem.setQuestions(currQuestionsList);
                        }else if(currentField.equals(_QUESTION_TAG_)){
                            currQuestion = new QuestionDataItem();
                        }else if(currentField.equals(_ID_TAG_)){
                            currQuestion.setId(text);
                            //Now we can add the new question because it have an ID.
                            currItem.addQuestion(currQuestion);
                        }else if(currentField.equals(_TEXT_TAG_)){
                            currQuestion.setText(text);
                        }else if(currentField.equals(_IMAGE_FILE_TAG_)){
                            currQuestion.setImage(text);
                        }else if(currentField.equals(_WEIGHT_TAG_)){
                            currQuestion.setWeight(text);
                        }else if(currentField.equals(_ANSWER_TAG_)){
                            currAnswer = new QuizAnswerDataItem();
                            currQuestion.setAnswer(currAnswer);
                        }else if(currentField.equals(_ANSWER_TEXT_TAG)){
                            currAnswer.setAnsText(text);
                        }else if(currentField.equals(_CORRECT_TAG_)){
                            currAnswer.setCorrect(text);
                        }else if(currentField.equals(_NEXT_TAG_)){
                            currAnswer.setNext(text);
                        }else{
                            ret.put(currentField, text);
                        }
                    }
                    
                    @Override
                    public void handleEndTag(String currentField) {
                        if(currentField.equals(_LEVEL_DATA_TAG_)){
                            ret.put(_LEVEL_DATA_TAG_, appLevelData);
                            appLevelData.reIndex();
                        }
                    }
                    
                    @Override
                    public void handleBeginTag(String currentField) {                        
                    }
                }
        , _LEVEL_DATA_TAG_);
        
        parser.startParsing();
        
        return ret;
    }
	
	/**
	 * Audio Level Data File Parser
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseAudioLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private AudioLevelDataItem currItem;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new AudioLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							currItem.setImage(text);
						}else if(currentField.equals(_LYRICS_TAG_)){
							currItem.setLyrics(text);	
						}else if(currentField.equals(_LOCAL_TAG_)){
							currItem.setLocal(Boolean.parseBoolean(text));
						}else if(currentField.equals(_AUDIO_URL_TAG_)){
							currItem.setAudioUrl(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Pdf
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parsePdfLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private PdfLevelDataItem currItem;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new PdfLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_LOCAL_TAG_)){
							currItem.setLocal(Boolean.parseBoolean(text));
						}else if(currentField.equals(_PDF_URL_TAG_)){
							currItem.setPdfUrl(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Web
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseWebLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private WebLevelDataItem currItem;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new WebLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_LOCAL_TAG_)){
							currItem.setLocal(Boolean.parseBoolean(text));
						}else if(currentField.equals(_WEB_URL_TAG_)){
							currItem.setWebUrl(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Qr
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseQrLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private QrLevelDataItem currItem;
					private String qrCode;
					private NextLevel currentNextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new QrLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_ID_QR_TAG_)){
							qrCode = text;
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							currentNextLevel = new NextLevel();
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							currentNextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							currentNextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							currentNextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							currentNextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}else if(currentField.equals(_QR_TAG_)){
							if(currItem != null)
								currItem.addCodeNextLevel(qrCode, currentNextLevel);
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Form
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseFormLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private FormLevelDataItem currItem;
					private List<FormDataItem> currList;
					private FormDataItem currFormItem;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new FormLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_FORM_TAG_)){
							currList = new ArrayList<FormDataItem>();
							currItem.setList(currList);
						}else if(currentField.equals(_FIELD_TAG_)){
							currFormItem = new FormDataItem();
							currList.add(currFormItem);
						}else if(currentField.equals(_ACTION_URL_TAG_)){
							currItem.setActionUrl(text);
						}else if(currentField.equals(_FIELD_TYPE_TAG_)){
							FormFieldType type = FormFieldType.fromText(text);
							if(type != null)
								currFormItem.setType(type);
						}else if(currentField.equals(_FIELD_LABEL_TAG_)){
							currFormItem.setFieldLabel(text);
						}else if(currentField.equals(_FIELD_NAME_TAG_)){
							currFormItem.setFieldName(text);
						}else if(currentField.equals(_REQUIRED_TAG_)){
							currFormItem.setRequired("true".equalsIgnoreCase(text));
						}else if(currentField.equals(_FIELD_PARAM_TAG_)){
							currFormItem.getParameters().add(text);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currItem.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Map
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseMapLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();

		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private MapLevelDataItem currItem;
					private MapDataItem currMapItem;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new MapLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_POSITION_TAG_)){
							currMapItem = new MapDataItem();
							currItem.addItem(currMapItem);
						}else if(currentField.equals(_POSITION_TITLE_TAG_)){
							currMapItem.setTitle(text);
						}else if(currentField.equals(_POSITION_ADDRESS_TAG_)){
							currMapItem.setAddress(text);
						}else if(currentField.equals(_POSITION_LAT_TAG_)){
							currMapItem.setLat(safeDouble(text));
						}else if(currentField.equals(_POSITION_LON_TAG_)){
							currMapItem.setLon(safeDouble(text));
						}else if(currentField.equals(_POSITION_IMAGE_TAG_)){
							currMapItem.setImage(text);
						}else if(currentField.equals(_POSITION_ICON_TAG_)){
							currMapItem.setIcon(text);
						}else if(currentField.equals(_POSITION_CURR_ICON_TAG_)){
							currItem.setCurrentPositionIconFileName(text);
						}else if(currentField.equals(_POSITION_LOCALIZE_ME_TAG_)){
							currItem.setLocalizeMe("true".equals(text));
						}else if(currentField.equals(_POSITION_SHOW_ALL_TAG_)){	
							currItem.setShowAllPositions("true".equals(text));
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currMapItem.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;		
	}

	protected static double safeDouble(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	/**
	 * Generate a table of the elements of XmlPullParser useful for the ImageZoom
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseImageZoomLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ImageLevelDataItem currItem;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ImageLevelDataItem();
							appLevelData.addItem(currItem);							
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							currItem.setImageFile(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the ImageList
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseImageListLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ImageListLevelDataItem currItem;
					private List<ListItemDataItem> list;
					private ListItemDataItem currListItem;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ImageListLevelDataItem();
							appLevelData.addItem(currItem);
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							currItem.setImageFile(text);
						}else if(currentField.equals(_LIST_TAG_)){
							list = currItem.getList();
							list.clear();
						}else if(currentField.equals(_LIST_ITEM_TAG_)){
							currListItem = new ListItemDataItem();
							list.add(currListItem);
						}else if(currentField.equals(_TEXT_TAG_)){
							currListItem.setText(text);
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currListItem.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else if(currentField.equals(_GEO_REF_TAG_)){
							currItem.setGeoReferencia(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the Video
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseVideoLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private VideoLevelDataItem currItem;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new VideoLevelDataItem();
							appLevelData.addItem(currItem);
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_VIDEO_PATH_TAG_)){
							currItem.setVideoPath(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the List
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseListLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ListLevelDataItem currItem;
					private List<ListItemDataItem> list;
					private ListItemDataItem currListItem;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ListLevelDataItem();
							appLevelData.addItem(currItem);
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_ORDER_TAG_)){
							currItem.setOrder(Boolean.parseBoolean(text));
						}else if(currentField.equals(_LIST_TAG_)){ 
							list = currItem.getList();
							list.clear();
						}else if(currentField.equals(_LIST_ITEM_TAG_)){
							currListItem = new ListItemDataItem();
							list.add(currListItem);
						}else if(currentField.equals(_TEXT_TAG_)){
							currListItem.setText(text);
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							currListItem.setImageFile(text);	
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currListItem.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else if(currentField.equals(_GEO_REF_TAG_)){
							currItem.setGeoReferencia(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Generate a table of the elements of XmlPullParser useful for the ImageTextDescripction
	 * @param xpp
	 * @return
	 */
	private static Map<String, Object> parseImageTextLevelDataFile(XmlPullParser xpp) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		NwXmlStandarParser parser = new NwXmlStandarParser(xpp,
				new NwXmlStandarParserTextHandler() {
					private AppLevelData appLevelData = new DefaultAppLevelData();
					private ImageTextDescriptionLevelDataItem currItem;
					private NextLevel nextLevel;
					
					@Override
					public void handleText(String currentField, String text) {
						if(currentField.equals(_DATA_TAG_)){
							currItem = new ImageTextDescriptionLevelDataItem();
							appLevelData.addItem(currItem);
						}else if(currentField.equals(_DATA_ID_TAG_)){
							currItem.setId(text);							
						}else if(currentField.equals(_HEADER_IMAGE_FILE_TAG_)){
							currItem.setHeaderImageFile(text);
						}else if(currentField.equals(_HEADER_TEXT_TAG_)){
							currItem.setHeaderText(text);
						}else if(currentField.equals(_IMAGE_FILE_TAG_)){
							currItem.setImageFile(text);
						}else if(currentField.equals(_BAR_TEXT_TAG_)){
							currItem.setBarText(text);							
						}else if(currentField.equals(_TEXT_TAG_)){
							currItem.setText(text);							
						}else if(currentField.equals(_NEXT_LEVEL_TAG_)){
							nextLevel = new NextLevel();
							currItem.setNextLevel(nextLevel);
						}else if(currentField.equals(_LEVEL_NUMBER_TAG_)){
							nextLevel.setLevelNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_LEVEL_ID_TAG_)){
							nextLevel.setLevelId(text);
						}else if(currentField.equals(_DATA_NUMBER_TAG_)){
							nextLevel.setDataNumber(Integer.parseInt(text));
						}else if(currentField.equals(_NL_DATA_ID_TAG_)){
							nextLevel.setDataId(text);
						}else if(currentField.equals(_GEO_REF_TAG_)){
							currItem.setGeoReferencia(text);
						}else{
							ret.put(currentField, text);
						}
					}
					
					@Override
					public void handleEndTag(String currentField) {
						if(currentField.equals(_LEVEL_DATA_TAG_)){
							ret.put(_LEVEL_DATA_TAG_, appLevelData);
							appLevelData.reIndex();
						}
					}
					
					@Override
					public void handleBeginTag(String currentField) {						
					}
				}
		, _LEVEL_DATA_TAG_);
		
		parser.startParsing();
		
		return ret;
	}
	
	/**
	 * Based on the mapping of XmlPullParser, returns an ApplLevelData
	 * corresponding to a level
	 * @param data
	 * @return
	 */

	private static AppLevelData fromLevelData(Map<String, Object> data) {
		return (AppLevelData)data.get(_LEVEL_DATA_TAG_);
	}

	public static Profile parseProfileData(Context context, String xmlFileName) {
		XmlPullParser xpp = createXpp(context, Locale.getDefault(), xmlFileName, false);
		if(xpp != null){

			Map<String, Object> data = parseProfileDataFile(xpp);  
			return fromProfileData(data);
		}
		return null;
	}

	private static Profile fromProfileData(Map<String, Object> data) {
		Profile ret = null;
		ret = (Profile)data.get(_PROFILE_TAG_);
		return ret;
	}

}
