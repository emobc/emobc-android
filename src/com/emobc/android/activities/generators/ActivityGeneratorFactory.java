/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* ActivityGeneratorFactory.java
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
import android.util.Log;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.utils.InvalidFileException;

/**
 * Abstract Factory of Activities Generators
 * 
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public abstract class ActivityGeneratorFactory {
	
	/**
	 * Returns a specific ActivityGenerator a screen depending on the requested activity
	 * @param activity
	 * @param nextLevel
	 * @return ActivityGenerator
	 */
	public static ActivityGenerator createActivityGenerator(Activity activity, NextLevel nextLevel) throws InvalidFileException{
		ApplicationData applicationData = AbstractActivtyGenerator.getApplicationData(activity);
		AppLevel appLevel = applicationData.getNextAppLevel(nextLevel, activity);
		switch (appLevel.getActivityType()) {
		case IMAGE_TEXT_DESCRIPTION_ACTIVITY:
			return new ImageTextDescriptionActivityGenerator(appLevel, nextLevel);
		case IMAGE_LIST_ACTIVITY:
			return new ImageListActivityGenerator(appLevel, nextLevel);
		case LIST_ACTIVITY:
			return new ListActivityGenerator(appLevel, nextLevel);
		case VIDEO_ACTIVITY:
			return new VideoActivityGenerator(appLevel, nextLevel);
		case IMAGE_ZOOM_ACTIVITY:
			return new ImageZoomActivityGenerator(appLevel, nextLevel);
		case IMAGE_GALLERY_ACTIVITY:
			return new ImageGalleryActivityGenerator(appLevel, nextLevel);
		case WEB_ACTIVITY:
			return new WebActivityGenerator(appLevel, nextLevel);
		case QR_ACTIVITY:
			return new QrActivityGenerator(appLevel, nextLevel);
		case FORM_ACTIVITY:
			return new FormActivityGenerator(appLevel, nextLevel);
		case MAP_ACTIVITY:
			return new MapActivityGenerator(appLevel, nextLevel);
		case PDF_ACTIVITY:
			return new PdfActivityGenerator(appLevel, nextLevel);
		case CALENDAR_ACTIVITY:
			return new CalendarActivityGenerator(appLevel, nextLevel);
		case QUIZ_ACTIVITY:
			return new QuizActivityGenerator(appLevel, nextLevel);
		case AUDIO_ACTIVITY:
			return new AudioActivityGenerator(appLevel, nextLevel);
		case BUTTONS_ACTIVITY:
			return new ButtonsActivityGenerator(appLevel, nextLevel);
		case CANVAS_ACTIVITY:
			return new CanvasActivityGenerator(appLevel, nextLevel);
		case PROFILE_ACTIVITY:
			return new ProfileActivityGenerator();
		case SALE_ACTIVITY:
			return null;
		default:
			break;
		}
		Log.e("eMobc", "No existe el activity generator para el tipo: " + appLevel.getActivityType().toString());
		return null;
	}
}
