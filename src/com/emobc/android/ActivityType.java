/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ActivityType.java
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

/**
 * Enumerates all existing activity types.
 * <p>
 * <ul>
 * 	<li><strong>COVER_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>BUTTONS_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>IMAGE_TEXT_DESCRIPTION_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>IMAGE_LIST_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>LIST_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>VIDEO_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>IMAGE_ZOOM_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>IMAGE_GALLERY_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>WEB_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>QR_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>FORM_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>MAP_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>PDF_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>SOUND_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>CALENDAR_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>QUIZ_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>AUDIO_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>CANVAS_ACTIVITY</strong>:
 *  </li>
 * 	<li><strong>PROFILE_ACTIVITY</strong>: 
 *  this activity is similar to <tt>FORM_ACTIVITY</tt>, but its purpose is to retrieve user profile data. 
 *  </li> 
 * </ul>
 * @author Jorge E. Villaverde
 * @author Manuel Mora Cuesta
 * @author Jonatan Alcocer Luna
 */
public enum ActivityType {
	COVER_ACTIVITY,
	BUTTONS_ACTIVITY,
	IMAGE_TEXT_DESCRIPTION_ACTIVITY,
	IMAGE_LIST_ACTIVITY,
	LIST_ACTIVITY,
	VIDEO_ACTIVITY,
	IMAGE_ZOOM_ACTIVITY,
	IMAGE_GALLERY_ACTIVITY,
	WEB_ACTIVITY,
	QR_ACTIVITY,
	FORM_ACTIVITY,
	MAP_ACTIVITY,
	PDF_ACTIVITY,
	SOUND_ACTIVITY,
	CALENDAR_ACTIVITY,
	QUIZ_ACTIVITY,
	AUDIO_ACTIVITY,
	CANVAS_ACTIVITY,
	PROFILE_ACTIVITY;

	public static ActivityType formString(String text) {
		if("COVER_ACTIVITY".equalsIgnoreCase(text)){
			return COVER_ACTIVITY;
		}else if("BUTTONS_ACTIVITY".equalsIgnoreCase(text)){
			return BUTTONS_ACTIVITY;
		}else if("IMAGE_TEXT_DESCRIPTION_ACTIVITY".equalsIgnoreCase(text)){
			return IMAGE_TEXT_DESCRIPTION_ACTIVITY;
		}else if("IMAGE_LIST_ACTIVITY".equalsIgnoreCase(text)){
			return IMAGE_LIST_ACTIVITY;
		}else if("LIST_ACTIVITY".equalsIgnoreCase(text)){
			return LIST_ACTIVITY;
		}else if("VIDEO_ACTIVITY".equalsIgnoreCase(text)){
			return VIDEO_ACTIVITY;
		}else if("IMAGE_ZOOM_ACTIVITY".equalsIgnoreCase(text)){
			return IMAGE_ZOOM_ACTIVITY;
		}else if("IMAGE_GALLERY_ACTIVITY".equalsIgnoreCase(text)){
			return IMAGE_GALLERY_ACTIVITY;
		}else if("WEB_ACTIVITY".equalsIgnoreCase(text)){
			return WEB_ACTIVITY;
		}else if("QR_ACTIVITY".equalsIgnoreCase(text)){
			return QR_ACTIVITY;
		}else if("FORM_ACTIVITY".equalsIgnoreCase(text)){
			return FORM_ACTIVITY;
		}else if("MAP_ACTIVITY".equalsIgnoreCase(text)){
			return MAP_ACTIVITY;
		}else if("PDF_ACTIVITY".equalsIgnoreCase(text)){
			return PDF_ACTIVITY;
		}else if("SOUND_ACTIVITY".equalsIgnoreCase(text)){
			return SOUND_ACTIVITY;
		}else if("CALENDAR_ACTIVITY".equalsIgnoreCase(text)){
			return CALENDAR_ACTIVITY;
		}else if("QUIZ_ACTIVITY".equalsIgnoreCase(text)){
			return QUIZ_ACTIVITY;
		}else if("AUDIO_ACTIVITY".equalsIgnoreCase(text)){
			return AUDIO_ACTIVITY;
		}else if("CANVAS_ACTIVITY".equalsIgnoreCase(text)){
			return CANVAS_ACTIVITY;
		}else if("PROFILE_ACTIVITY".equalsIgnoreCase(text)){
			return PROFILE_ACTIVITY;
		}		
		return null;
	}
}
