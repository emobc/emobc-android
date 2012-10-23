/**
 * 
 */
package com.emobc.android.config;

import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;

import com.emobc.android.ApplicationData;
import com.emobc.android.parse.ParseUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * Application Configuration Object.
 * This class holds the configuration the global configuration of the eMobc Framework.
 * <p>This configuration is read from: <tt>/assets/config.xml</tt>.
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class ApplicationConfiguration {
	/**
	 * URL to set {@link ApplicationData} in remote mode.
	 */
	public static final String REMOTE_APP_URL = "_REMOTE_APP_URL_";
	
	/**
	 * Default Application Configuration File Name
	 */
	private static final String _CONFIG_FILE_NAME_ = "config.xml";
	
	/**
	 * Map that stores application attributes.
	 */
	private Map<String, Object> attributeMap = new WeakHashMap<String, Object>();
	
	
	public ApplicationConfiguration(){
		super();
	}
	
	/**
	 * Returns an attribute in the configuration from a key.
	 * @param key to the value of the attribute
	 * @return attribute value.
	 */
	public Object getAttribute(String key){
		if(key == null)
			return null;
		return attributeMap.get(key);
	}
	
	public void setAttibute(String key, Object value) {
		this.attributeMap.put(key, value);
	}
	
	//----------------------------- static methods-------------------------------
	/**
	 * Returns an ApplicationData with data obtained after parsing app.xml file
	 * @param context
	 * @return ApplicationData
	 */
	public static ApplicationConfiguration readConfiguration(Context context) throws InvalidFileException {
		return readConfiguration(context, Locale.getDefault());
	}
	
	/**
	 * Returns an ApplicationData with data obtained after parsing app.xml file
	 * @param context
	 * @param locale
	 * @return ApplicationData
	 */
	public static ApplicationConfiguration readConfiguration(Context context, Locale locale) throws InvalidFileException {
		XmlPullParser xpp = ParseUtils.createXpp(context, locale, _CONFIG_FILE_NAME_, false);
		return new ApplicationConfigurationParser(xpp).parse();
	}
}
