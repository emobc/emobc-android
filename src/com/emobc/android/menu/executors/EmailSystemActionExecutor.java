/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* EmailSystemActionExecutor.java
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

import com.emobc.android.menu.SystemAction;

import android.app.Activity;
import android.content.Intent;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class EmailSystemActionExecutor extends ContentAwareSystemActionExecutor {

	/**
	 * @param context
	 */
	public EmailSystemActionExecutor(Activity context) {
		super(context, SystemAction.EMAIL);
	}

	@Override
	protected void executeContentAwareSystemAction(String activityContent) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
		intent.putExtra(Intent.EXTRA_TEXT, activityContent);

		context.startActivity(Intent.createChooser(intent, "Send Email"));		
	}

}
