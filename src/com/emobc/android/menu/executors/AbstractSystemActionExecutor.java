/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AbstractSystemActionExecutor.java
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

import com.emobc.android.menu.SystemAction;
import com.emobc.android.menu.SystemActionExecutor;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public abstract class AbstractSystemActionExecutor implements SystemActionExecutor {
	protected final Activity context;
	protected final SystemAction systemAction;
	
	public AbstractSystemActionExecutor(Activity context, SystemAction systemAction){
		super();
		this.context = context;
		this.systemAction = systemAction;
	}
	
	public SystemAction getSystemAction(){
		return systemAction;
	}
}
