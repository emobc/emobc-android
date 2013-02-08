/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* SaleResultActivity.java
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
package com.emobc.android.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emobc.android.activities.generators.AbstractActivtyGenerator;
import com.emobc.android.levels.impl.SaleLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.menu.SystemAction;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;
import com.paypal.android.MEP.PayPalActivity;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class SaleResultActivity 
	extends CreateMenus 
	implements ContentAwareActivity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6318388378158229574L;
	
	private static final String TAG = "SaleResultActivity";
	
	private SaleLevelDataItem item;
	private String payKey = null;
	private String status = null;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	rotateScreen(this);
         
    	setContentView(R.layout.sale_result_layout);

    	Intent intent = getIntent();  
    	this.item = (SaleLevelDataItem)intent.getSerializableExtra(SaleActivity.SALE_ITEM);
         
        this.payKey = intent.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
        this.status = intent.getStringExtra(PayPalActivity.EXTRA_PAYMENT_STATUS);

		AbstractActivtyGenerator.initializeHeader(this, item);

		// Item Image
		ImageView itemImage = (ImageView) findViewById(R.id.sale_img);
		if(Utils.hasLength(item.getItemImage())){
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(this, item.getItemImage());
				itemImage.setImageDrawable(drawable);
			} catch (InvalidFileException e) {
				Log.e(TAG, e.getLocalizedMessage());
			}			
		}else{
			itemImage.setVisibility(View.GONE);
		}
		
		// Item Description
		TextView itemText = (TextView)findViewById(R.id.sale_descr);
		if(Utils.hasLength(item.getItemDescription())){
			itemText.setText(item.getItemDescription());
		}else{
			itemText.setVisibility(View.GONE);
		}
		
		// Item Price
		TextView itemPrice = (TextView)findViewById(R.id.sale_price);
		if(item.getItemPrice() != null){
			itemPrice.setText(item.getItemPrice().toString());
		}else{
			itemPrice.setText("0.00");
		}
         
		// Estado Compra
		TextView payStatus = (TextView)findViewById(R.id.sale_status);
		if(Utils.hasLength(status)){
			payStatus.setText(status);
		}else{
			ViewGroup payStatusGroup = (ViewGroup)findViewById(R.id.sale_status_grp);
			payStatusGroup.setVisibility(View.GONE);
		}
		
		// Codigo de Pago
		TextView payKeyTxt = (TextView)findViewById(R.id.sale_pay_key);
		if(Utils.hasLength(payKey)){
			payKeyTxt.setText(payKey);
		}else{
			ViewGroup payKeyGroup = (ViewGroup)findViewById(R.id.sale_pay_key_grp);
			payKeyGroup.setVisibility(View.GONE);
		}
		
		setEntryPoint(false);
		createMenus();		
    }
    
	@Override
	public String getActivityContent(SystemAction systemAction) {
		if(item == null)
			return null;
		
		StringBuilder builder = new StringBuilder();
		
		if(Utils.hasLength(item.getItemDescription())){
			builder.append(item.getItemDescription());
			builder.append("\n");
		}
		if(item.getItemPrice() != null){
			builder.append(getString(R.string.price));
			builder.append(" ");
			builder.append(item.getItemPrice().toString());
			builder.append("\n");
		}
		if(Utils.hasLength(status)){
			builder.append(getString(R.string.sale_status));
			builder.append(" ");
			builder.append(status);			
			builder.append("\n");
		}
		if(Utils.hasLength(payKey)){
			builder.append(getString(R.string.sale_pay_key));
			builder.append(" ");
			builder.append(payKey);			
			builder.append("\n");
		}
		return builder.toString();		
	}

}
