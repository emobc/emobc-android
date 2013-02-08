/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * SystemActionMenuItem.java
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
package com.emobc.android.menu;

import android.app.Activity;

import com.emobc.android.menu.executors.SystemActionExecutorFactory;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class SystemActionMenuItem extends MenuItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 18003868344623958L;
	private final SystemAction systemAction;

	/**
	 * @param title
	 * @param imageFileName
	 * @param nextLevel
	 * @param systemAction
	 */
	public SystemActionMenuItem(String title, String imageFileName, SystemAction systemAction) {
		super(title, imageFileName);

		if (systemAction == null)
			throw new IllegalArgumentException("Invalid SystemAction: "
					+ String.valueOf(systemAction));

		this.systemAction = systemAction;
	}

	public SystemAction getSystemAction() {
		return systemAction;
	}

	@Override
	public void executeMenuItem(Activity context) {
		SystemActionExecutor executor = SystemActionExecutorFactory.createSystemActionExecutor(context, systemAction);
		if(executor != null){
			executor.executeSystemAction();
		}
	}

	@Override
	public boolean isEnable(Activity context) {
		return true;
	}

}
