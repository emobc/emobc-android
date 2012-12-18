/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* SaleLevelDataItem.java
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

import java.math.BigDecimal;

import com.emobc.android.levels.AppLevelDataItem;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class SaleLevelDataItem extends AppLevelDataItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7276865635070371196L;
	
	private final String itemDescription;
	private final String itemImage;
	private final BigDecimal itemPrice;
	private final BigDecimal itemTax;
	private final BigDecimal itemShipping;

	public SaleLevelDataItem(String id, String headerImageFile,
			String headerText, String geoReferencia, 
			String itemDescription, String itemImage,
			BigDecimal itemPrice, BigDecimal itemTax, BigDecimal itemShipping) {
		super(id, headerImageFile, headerText, geoReferencia);
		this.itemDescription = itemDescription;
		this.itemImage = itemImage;
		this.itemPrice = itemPrice;
		this.itemTax = itemTax;
		this.itemShipping = itemShipping;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public String getItemImage() {
		return itemImage;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public String toString(){
		return "[Sale " + itemDescription + "]";
	}

	public BigDecimal getItemTax() {
		return itemTax;
	}

	public BigDecimal getItemShipping() {
		return itemShipping;
	}
}
