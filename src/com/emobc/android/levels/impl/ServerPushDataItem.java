/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ServerPushDataItem.java
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
package com.emobc.android.levels.impl;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ServerPushDataItem {
	//Constants
	//Type Constants
	public static final String GCM = "gcm";
	
	//Attributes
	private String type;
	private String appName;
	private String senderId;
	private String serverUrl;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * Return push service type
	 * @return serverPushDataItem.GCM or null
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set the push service type
	 * @param type Must be a safe constant or will be GCM by default.
	 */
	public void setType(String type) {
		if (type.equals(GCM)){
			this.type = type;
		} else{
			this.type = GCM;
		}
	}
	
	/**
	 * Return the service sender ID
	 * @return
	 */
	public String getSenderId() {
		return senderId;
	}
	
	/**
	 * Set the service sender ID
	 * @param senderId
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	/**
	 * Return the push service server URL
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}
	
	/**
	 * Set the push service server URL
	 * @param serverUrl
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
}
