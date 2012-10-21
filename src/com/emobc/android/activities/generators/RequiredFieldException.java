/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* RequiredFieldException.java
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

import com.emobc.android.levels.impl.FormDataItem;

/**
 * Thrown by the FormActivityGenerator when a required field is empty.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class RequiredFieldException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final FormDataItem dataItem;
	
	public RequiredFieldException(FormDataItem dataItem) {
		super();
		this.dataItem = dataItem;
	}

	public FormDataItem getDataItem() {
		return dataItem;
	}

}
