/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CustomItemizedOverlay.java
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
package com.emobc.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.emobc.android.levels.AppLevel;
import com.emobc.android.utils.InvalidFileException;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {
   
   private ArrayList<CustomOverlayItem> mapOverlays = new ArrayList<CustomOverlayItem>();
   
   private Context context;
   
   public CustomItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
   }
   
   public CustomItemizedOverlay(Drawable defaultMarker, Context context) {
        this(defaultMarker);
        this.context = context;
   }

   @Override
   protected OverlayItem createItem(int i) {
      return mapOverlays.get(i);
   }

   @Override
   public int size() {
      return mapOverlays.size();
   }
   
   @Override
   protected boolean onTap(int index) {
      final CustomOverlayItem item = mapOverlays.get(index);
      AlertDialog.Builder dialog = new AlertDialog.Builder(context);
      dialog.setTitle(item.getTitle());
      dialog.setMessage(item.getSnippet());
      final Context nContext = context;
      dialog.setPositiveButton("Más información", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
        	 showNextLevel(nContext, item.getNextLevel());
         }
     });
     dialog.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
              dialog.cancel();
         }
     });
      dialog.show();
      return true;
   }
   
   public void addOverlay(CustomOverlayItem overlay) {
      mapOverlays.add(overlay);
      this.populate();
   }
   
   /**
	 * Start a new activity in the levelId leaning and dataId of NextLevel. 
	 * Also initializes parameters NextLevel and entrypoint 
	 * @param context
	 * @param nextLevel
	 */
	protected void showNextLevel(Context context, NextLevel nextLevel) {
		 if(nextLevel != null){
			ApplicationData appData;
			try {
				appData = ApplicationData.readApplicationData(context);
				AppLevel level = appData.getNextAppLevel(nextLevel, context);
				if(level != null){
					Class<? extends Activity> clazz = level.getAcivityClass();
					
					Intent launchActivity = new Intent(context, clazz);				
					launchActivity.putExtra(ApplicationData.NEXT_LEVEL_TAG, nextLevel);
					launchActivity.putExtra(ApplicationData.IS_ENTRY_POINT_TAG, false);
																	
					context.startActivity(launchActivity);
				}else{
					CharSequence text = "Invalid Next Level: " + nextLevel.toString();
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();					
				}
			} catch (InvalidFileException e) {
			
			}  
		} 
	}

}