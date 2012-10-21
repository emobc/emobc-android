/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* FormFieldType.java
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

import com.emobc.android.activities.FormActivity;
import com.emobc.android.activities.generators.FormActivityGenerator;


/**
 * Enumerates field types to the Form Activity
 * @author Jorge E. Villaverde
 * @see FormActivity
 * @see FormActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public enum FormFieldType {
	INPUT_TEXT,
	INPUT_NUMBER,
	INPUT_EMAIL,
	INPUT_PHONE,
	INPUT_CHECK,
	INPUT_PICKER,
	INPUT_PASSWORD;	

	public static FormFieldType fromText(final String text){
		if("INPUT_TEXT".equals(text))
			return INPUT_TEXT;
		if("INPUT_NUMBER".equals(text))
			return INPUT_NUMBER;
		if("INPUT_EMAIL".equals(text))
			return INPUT_EMAIL;
		if("INPUT_PHONE".equals(text))
			return INPUT_PHONE;
		if("INPUT_CHECK".equals(text))
			return INPUT_CHECK;
		if("INPUT_PICKER".equals(text))
			return INPUT_PICKER;
		if("INPUT_PASSWORD".equals(text))
			return INPUT_PASSWORD;
		return null;
	}
}
