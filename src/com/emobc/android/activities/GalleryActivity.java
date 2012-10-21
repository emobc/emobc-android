/*
 * Copyright © 2011-2012 Neurowork S.L.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emobc.android.activities;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.emobc.android.ApplicationData;
import com.emobc.android.SearchResult;

/** 
* Class that defines an activity of type GALLERY_ACTIVITY, and 
* initialize all screen menu and the screen rotations. In its 
* method onCreate(), call its GalleryActivityGenerator generator class. 
* @author Jonatan Alcocer Luna
*/
public class GalleryActivity extends CoverButtonsActivity {
	protected static final Context Activity = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.image_galery);
		
		 boolean isEntryPoint = false;
	     rotateScreen(this);
	        
	     Intent intent = getIntent();
		 isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
		
		Gallery g = (Gallery) findViewById(R.id.galery);
		final ImageView i = (ImageView) findViewById(R.id.imageGalery);
		final AssetManager am = getAssets();

		ApplicationData applicationData = SplashActivity.getApplicationData();

		final List<SearchResult> listaImagenes = applicationData
				.findAllLevelsImages(this);

		g.setAdapter(new ImageAdapter(this, listaImagenes));

		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {

				BufferedInputStream buf;
				try {
					buf = new BufferedInputStream(
							am.open(listaImagenes.get(position).getText()));
					i.setImageBitmap(BitmapFactory.decodeStream(buf));
					  
					i.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							
							showNextLevel(GalleryActivity.this, listaImagenes.get(position).getNextLevel());
				        }
					});
				} catch (IOException e) {
					Log.e("eMobc", e.getMessage());
				}
			}
		});
		//createToolBar(isEntryPoint);
		createMenus(this, isEntryPoint);
	}
	
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private List<SearchResult> images;

		public ImageAdapter(Context c, List<SearchResult> images) {
			mContext = c;
			this.images = images;
		}

		public int getCount() {
			return images.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			AssetManager assetManager = getAssets();

			BufferedInputStream buf;

			try {
				buf = new BufferedInputStream(assetManager.open(images.get(
						position).getText()));
				i.setImageBitmap(BitmapFactory.decodeStream(buf));
				i.setLayoutParams(new Gallery.LayoutParams(100, 100));
				i.setScaleType(ImageView.ScaleType.FIT_XY);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return i;
		}

	}
}