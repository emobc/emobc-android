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

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.emobc.android.menu.CreateMenus;

/**
 * @author Jorge E. Villaverde
 *
 */
public class CoverButtonsActivity extends CreateMenus {
	protected static final Context Activity = null;
      
    protected void createCoverButtons(){
    	ImageView gallery = (ImageView) findViewById(R.id.btnGallery);
    	if(gallery != null)
			gallery.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
						showGallery();
					
				}
			});
		
		ImageView planos = (ImageView) findViewById(R.id.btnPlanos);
		if(planos != null)
			planos.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					showMap();
				}
			});
		
		ImageView videos = (ImageView) findViewById(R.id.btnVideos);
		if(videos != null)
			videos.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					showVideos();
				}
			});
		
		ImageView search = (ImageView) findViewById(R.id.btnSearch);
		if(search != null)
			search.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					showSearch();
				}
			});
    }

    protected void showGallery() {		
    	Intent galery = new Intent(this, GalleryActivity.class);				
		this.startActivity(galery);
	}	

    // Show map
    protected void showMap() {
    	// Embed MapView
    	Intent maps = new Intent(this, MapsActivity.class);				
		this.startActivity(maps);
		
    	/* Old maps
		try {
				String urlString = "http://maps.google.com/maps?q=" + "Madrid" + "&near=Madrid,España";
					Intent browserIntent = new Intent("android.intent.action.VIEW", 
							Uri.parse(urlString ));
					startActivity(browserIntent);
		} catch (Exception e) {
			// Test
		}*/
    }
    
    protected void showVideos() {
    	Intent video = new Intent(this, VideoActivity.class);				
		this.startActivity(video);
    }
    
    protected void showSearch() {
     	Intent search = new Intent(this, SearchActivity.class);				
		this.startActivity(search);
    }
    
}	