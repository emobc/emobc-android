/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * SaleActivity.java
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emobc.android.ActivityType;
import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AbstractActivtyGenerator;
import com.emobc.android.activities.generators.LevelActivityGenerator;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.SaleLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.menu.SystemAction;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class SaleActivity extends CreateMenus implements ContentAwareActivity,
		OnClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1787095450181673703L;

	public static final int REQUEST_PAY = 0;

	private static final String TAG = "SaleActivity";

	public static final String SALE_ITEM = "sale-item";

	private SaleLevelDataItem item;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean isEntryPoint = false;
		rotateScreen(this);

		setContentView(R.layout.sale_layout);

		ApplicationData applicationData = getApplicationData();
		
		if (applicationData != null) {
			Intent intent = getIntent();
			isEntryPoint = (Boolean) intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
			NextLevel nextLevel = (NextLevel) intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			setCurrentNextLevel(nextLevel);
			
			AppLevel appLevel = applicationData.getNextAppLevel(nextLevel, this);
			AppLevelData data = appLevel.getData(this);
			data.findByNextLevel(nextLevel);

			LevelActivityGenerator.initializeScreen(this, ActivityType.SALE_ACTIVITY, nextLevel);

			loadAppLevelData(data);

			setEntryPoint(isEntryPoint);
			createMenus();
		} else {
			Intent i = new Intent(this, SplashActivity.class);
			startActivity(i);
			finish();
		}
	}

	protected void loadAppLevelData(AppLevelData data) {
		this.item = (SaleLevelDataItem) data.findByNextLevel(getCurrentNextLevel());
		AbstractActivtyGenerator.initializeHeader(this, item);

		// Item Image
		ImageView itemImage = (ImageView) findViewById(R.id.sale_img);
		if (Utils.hasLength(item.getItemImage())) {
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(this, item.getItemImage());
				itemImage.setImageDrawable(drawable);
			} catch (InvalidFileException e) {
				Log.e(TAG, e.getLocalizedMessage());
			}
		} else {
			itemImage.setVisibility(View.GONE);
		}

		// Item Description
		TextView itemText = (TextView) findViewById(R.id.sale_descr);
		if (Utils.hasLength(item.getItemDescription())) {
			itemText.setText(item.getItemDescription());
		} else {
			itemText.setVisibility(View.GONE);
		}

		// Item Price
		TextView itemPrice = (TextView) findViewById(R.id.sale_price);
		if (item.getItemPrice() != null) {
			itemPrice.setText(item.getItemPrice().toString());
		} else {
			itemPrice.setText("0.00");
		}

		InitPayPalLibraryAsyncTask task = new InitPayPalLibraryAsyncTask(this,
				this);
		task.execute();
	}

	@Override
	public String getActivityContent(SystemAction systemAction) {
		if (item == null)
			return null;

		StringBuilder builder = new StringBuilder();

		if (Utils.hasLength(item.getItemDescription())) {
			builder.append(item.getItemDescription());
		}
		if (item.getItemPrice() != null) {
			builder.append(getString(R.string.price));
			builder.append(item.getItemPrice().toString());
		}

		return builder.toString();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_PAY) {
			return;
		}

		/**
		 * If you choose not to implement the PayPalResultDelegate, then you
		 * will receive the transaction results here. Below is a section of code
		 * that is commented out. This is an example of how to get result
		 * information for the transaction. The resultCode will tell you how the
		 * transaction ended and other information can be pulled from the Intent
		 * using getStringExtra.
		 */
		switch (resultCode) {
		case Activity.RESULT_OK:
			String payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
			String status = data.getStringExtra(PayPalActivity.EXTRA_PAYMENT_STATUS);

			Intent result = new Intent(this, SaleResultActivity.class);
			result.putExtra(SALE_ITEM, item);
			result.putExtra(PayPalActivity.EXTRA_PAY_KEY, payKey);
			result.putExtra(PayPalActivity.EXTRA_PAYMENT_STATUS, status);
			startActivity(result);
			finish();
			break;
		case Activity.RESULT_CANCELED:
			finish();
			break;
		case PayPalActivity.RESULT_FAILURE:
			String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
			String errorMessage = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			StringBuilder message = new StringBuilder();
			message.append(getString(R.string.dialog_pay_error_id));
			message.append(errorID);
			message.append(getString(R.string.dialog_pay_error_message));
			message.append(errorMessage);
			
			builder.setTitle(R.string.dialog_pay_error)
				.setMessage(message.toString())
	        	.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        		public void onClick(DialogInterface dialog, int id) {
	        			SaleActivity.this.finish();
	        		}
	        	});		
			
			// Create the AlertDialog object and return it
	        AlertDialog dialog = builder.create();
			
		    dialog.show();
			
			break;
		}
	}

	public void onClick(View v) {
		PayPalPayment payment = getSimpleItemPayment();
		// Use checkout to create our Intent.
		Intent checkoutIntent = PayPal.getInstance().checkout(payment, this);
		// Use the android's startActivityForResult() and pass in our Intent.
		// This will start the library.
		startActivityForResult(checkoutIntent, REQUEST_PAY);
	}

	private PayPalPayment getSimpleItemPayment() {
		ApplicationData applicationData = getApplicationData();

		// Create a basic PayPalPayment.
		PayPalPayment payment = new PayPalPayment();
		// Sets the currency type for this payment.
		payment.setCurrencyType("EUR");
		// Sets the recipient for the payment. This can also be a phone number.
		payment.setRecipient(applicationData.getPayPalRecipient());
		// Sets the amount of the payment, not including tax and shipping
		// amounts.
		payment.setSubtotal(item.getItemPrice());
		// Sets the payment type. This can be PAYMENT_TYPE_GOODS,
		// PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE.
		payment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);

		// PayPalInvoiceData can contain tax and shipping amounts. It also
		// contains an ArrayList of PayPalInvoiceItem which can
		// be filled out. These are not required for any transaction.
		PayPalInvoiceData invoice = new PayPalInvoiceData();
		// Sets the tax amount.
		invoice.setTax(item.getItemTax());
		// Sets the shipping amount.
		invoice.setShipping(item.getItemShipping());

		// PayPalInvoiceItem has several parameters available to it. None of
		// these parameters is required.
		PayPalInvoiceItem item1 = new PayPalInvoiceItem();
		// Sets the name of the item.
		item1.setName(item.getItemDescription());
		// Sets the ID. This is any ID that you would like to have associated
		// with the item.
		// Use the same DataId
		item1.setID(item.getId());
		// Sets the total price which should be (quantity * unit price). The
		// total prices of all PayPalInvoiceItem should add up
		// to less than or equal the subtotal of the payment.
		item1.setTotalPrice(item.getItemPrice());
		// Sets the unit price.
		item1.setUnitPrice(item.getItemPrice());
		// Sets the quantity.
		item1.setQuantity(1);
		// Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively,
		// you can create an ArrayList<PayPalInvoiceItem>
		// and pass it to the PayPalInvoiceData function setInvoiceItems().
		invoice.getInvoiceItems().add(item1);

		// Sets the PayPalPayment invoice data.
		payment.setInvoiceData(invoice);
		// Sets the merchant name. This is the name of your Application or
		// Company.
		// payment.setMerchantName("The Gift Store");
		// Sets the description of the payment.
		// payment.setDescription("Quite a simple payment");
		// Sets the Custom ID. This is any ID that you would like to have
		// associated with the payment.
		// payment.setCustomID(applicationData.getPayPalCustomerId());
		// Sets the Instant Payment Notification url. This url will be hit by
		// the PayPal server upon completion of the payment.
		// payment.setIpnUrl("http://www.exampleapp.com/ipn");
		// Sets the memo. This memo will be part of the notification sent by
		// PayPal to the necessary parties.
		// payment.setMemo("Hi! I'm making a memo for a simple payment.");

		return payment;
	}

	/**
	 * AsyncTask to init a simple Paypal buttom
	 * 
	 * @author http://francho.org
	 * 
	 */
	public class InitPayPalLibraryAsyncTask extends
			AsyncTask<Void, Void, Boolean> {
		// The PayPal server to be used - can also be ENV_NONE and ENV_LIVE
		private static final int PAYPAL_SERVER = PayPal.ENV_SANDBOX;

		private Context context;
		private OnClickListener onClickListener;

		public InitPayPalLibraryAsyncTask(Activity context,
				OnClickListener onClickListener) {
			this.context = context;
			this.onClickListener = onClickListener;
		}

		/**
		 * The hard task (requires connect with the server) will be do in
		 * background
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			PayPal pp = PayPal.getInstance();
			// If the library is already initialized, then we don't need to
			// initialize it again.
			if (pp == null) {
				// This is the main initialization call that takes in your
				// Context, the Application ID, and the server you would like to
				// connect to.
				final String appId = getApplicationData().getPayPalApplicationId();

				pp = PayPal.initWithAppID(context, appId, PAYPAL_SERVER);

				// -- These are required settings.
				pp.setLanguage("es_ES"); // Sets the language for the library.
				// --

				// -- These are a few of the optional settings.
				// Sets the fees payer. If there are fees for the transaction,
				// this person will pay for them. Possible values are
				// FEEPAYER_SENDER,
				// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and
				// FEEPAYER_SECONDARYONLY.
				pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
				// Set to true if the transaction will require shipping.
				pp.setShippingEnabled(false);
				// Dynamic Amount Calculation allows you to set tax and shipping
				// amounts based on the user's shipping address. Shipping must
				// be
				// enabled for Dynamic Amount Calculation. This also requires
				// you to create a class that implements PaymentAdjuster and
				// Serializable.
				pp.setDynamicAmountCalculationEnabled(false);
				// --
			}

			return PayPal.getInstance().isLibraryInitialized();
		}

		/**
		 * When the library init is finished, setup the pay button
		 */
		@Override
		protected void onPostExecute(Boolean isLibraryInitOk) {
			if (isLibraryInitOk) {
				PayPal pp = PayPal.getInstance();

				CheckoutButton launchSimplePayment = pp.getCheckoutButton(
						context, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
				// You'll need to have an OnClickListener for the
				// CheckoutButton. For this application, MPL_Example implements
				// OnClickListener and we
				// have the onClick() method below.
				launchSimplePayment.setOnClickListener(onClickListener);
				// The CheckoutButton is an android LinearLayout so we can add
				// it to our display like any other View.

				ViewGroup paypalLayout = (ViewGroup) findViewById(R.id.sale_button_grp);
				if (paypalLayout != null) {
					paypalLayout.addView(launchSimplePayment);
				}
			} else {
				Toast.makeText(context,
						"Error al inicializar la libreria de paypal",
						Toast.LENGTH_LONG).show();
			}
		}

	}

}
