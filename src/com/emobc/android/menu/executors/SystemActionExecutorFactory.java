/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* SystemActionFactory.java
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
package com.emobc.android.menu.executors;

import android.app.Activity;
import android.util.Log;

import com.emobc.android.menu.SystemAction;
import com.emobc.android.menu.SystemActionExecutor;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public abstract class SystemActionExecutorFactory {

	public static SystemActionExecutor createSystemActionExecutor(Activity context, SystemAction systemAction) {
		switch (systemAction) {
		case GO_HOME:
			return new HomeSystemActionExecutor(context);
		case GO_BACK:
			return new BackSystemActionExecutor(context);
		case TTS:
			return new TextToSpeachSystemActionExecutor(context);
		case SHARE:
			return new ShareSystemActionExecutor(context);
		case COPY:
			return new CopySystemActionExecutor(context);
		case EMAIL:
			return new EmailSystemActionExecutor(context);
		default:
			break;
		}
		Log.e("eMobc", "Invalid System Action Executor for SystemAction: " + systemAction.toString());
		return null;		
	}
}
