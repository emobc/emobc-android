/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MapsActivity.java
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.levels.AppDataItemText;
import com.emobc.android.levels.AppLevelDataItem;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;
import com.google.android.maps.MapActivity;
import com.google.tts.TextToSpeechBeta;
import com.google.tts.TextToSpeechBeta.OnInitListener;


/** 
 * Class that defines an activity of type MAP_ACTIVITY. In its 
 * method onCreate(), call its MapActivityGenerator generator class. 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @see MapActivityGenerator
 * @version 0.1
 * @since 0.1
 */
public class MapsActivity extends MapActivity {
	private TextToSpeechBeta myTts;
	
	@SuppressWarnings("unused")
	private OnInitListener ttsInitListener = new OnInitListener() {
		@Override
		public void onInit(int arg0, int arg1) {
		}
      };

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
        ApplicationData applicationData = SplashActivity.getApplicationData();
		if(applicationData != null){
			Intent intent = getIntent();
			NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			ActivityGenerator generator = applicationData.getFromNextLevel(this, nextLevel);
			generator.initializeActivity(this);
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}

	}
	
	protected void showHome() {
		Intent launchHome = new Intent(this, CoverActivity.class);
		startActivity(launchHome);
	}

	protected void showShare() {
		ApplicationData applicationData = SplashActivity.getApplicationData();
		if (applicationData != null) {
			Intent intent = getIntent();
			NextLevel nextLevel = (NextLevel) intent
					.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
			AppLevelDataItem item = applicationData.getDataItem(this,
					nextLevel);
			AppDataItemText textItem;
			try {
				textItem = (AppDataItemText) item;

				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				// sharingIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				// new String[] { "contacto@neurowork.net" });
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						textItem.getItemText());
				startActivity(Intent.createChooser(sharingIntent,
						"Compartir contenido"));

			} catch (ClassCastException e) {
			}
		}
	}

	protected void showEdit() {
		TextView tv = (TextView) findViewById(R.id.basic_text);
		String texto = (String) tv.getText();
		tv.setText(texto, BufferType.EDITABLE);
	}

	protected void showMap() {

		ApplicationData applicationData;
		try {
			applicationData = ApplicationData.readApplicationData(this);
			if (applicationData != null) {
				Intent intent = getIntent();
				NextLevel nextLevel = (NextLevel) intent
						.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
				AppLevelDataItem item = applicationData.getDataItem(this,
						nextLevel);

				if (Utils.hasLength(item.getGeoReferencia())) {
					String urlString = "http://maps.google.com/maps?q="
							+ item.getGeoReferencia() + "&near=Madrid,Espa�a";
					Intent browserIntent = new Intent(
							"android.intent.action.VIEW", Uri.parse(urlString));
					startActivity(browserIntent);
				}
			}
		} catch (InvalidFileException e) {
		}
	}

	protected void showMapCity() {
		// Intent launchHome = new Intent(this, MapsActivity.class);
		// startActivity(launchHome);
		setContentView(R.layout.image_zoom);
		Drawable drawable;
		try {
			drawable = ImagesUtils.getDrawable(this, "images/plano.jpg");
			ImageView planoImage = (ImageView) this
					.findViewById(R.id.planoImage);
			planoImage.setImageDrawable(drawable);

			TextView head = (TextView) this.findViewById(R.id.header);
			head.setText("Plano Turístico");
		} catch (InvalidFileException e) {
			Log.e("AppCoverData", e.getLocalizedMessage());
		}

	}

	protected void showSearch() {

		setContentView(R.layout.search_screen);
	}

	protected void textToSeach() {
		ApplicationData applicationData;
		try {
			applicationData = ApplicationData.readApplicationData(this);
			if (applicationData != null) {
				Intent intent = getIntent();
				NextLevel nextLevel = (NextLevel) intent
						.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);
				AppLevelDataItem item = applicationData.getDataItem(this,
						nextLevel);
				AppDataItemText textItem;
				try {
					textItem = (AppDataItemText) item;
					myTts.speak(textItem.getItemText(), 0, null);

					new AlertDialog.Builder(this)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setMessage(R.string.reading)
							.setPositiveButton(R.string.reading_stop,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											myTts.stop();
											finish();
										}

									})
							.setNegativeButton(R.string.reading_continue, null)
							.show();

				} catch (ClassCastException e) {
				}
			}
		} catch (InvalidFileException e) {
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_map_near:
	            showNearItems();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void showNearItems() {
		
		
	}	
}