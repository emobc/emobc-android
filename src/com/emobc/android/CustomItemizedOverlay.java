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
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.emobc.android.activities.generators.AbstractActivtyGenerator;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {
   
   private ArrayList<CustomOverlayItem> mapOverlays = new ArrayList<CustomOverlayItem>();
   
   private Activity activity;
   
   public CustomItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
   }
   
   public CustomItemizedOverlay(Drawable defaultMarker, Activity activity) {
        this(defaultMarker);
        this.activity = activity;
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
      AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
      dialog.setTitle(item.getTitle());
      dialog.setMessage(item.getSnippet());
      dialog.setPositiveButton("Más información", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
        	 AbstractActivtyGenerator.showNextLevel(activity, item.getNextLevel());
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
}