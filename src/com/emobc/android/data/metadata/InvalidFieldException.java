/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* InvalidFieldException.java
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
package com.emobc.android.data.metadata;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class InvalidFieldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5531815190826493201L;

	public InvalidFieldException() {
		super();
	}

	public InvalidFieldException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InvalidFieldException(String detailMessage) {
		super(detailMessage);
	}

	public InvalidFieldException(Throwable throwable) {
		super(throwable);
	}
}
