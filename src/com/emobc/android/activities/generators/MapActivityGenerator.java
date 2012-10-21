/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* MapActivityGenerator.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.emobc.android.ActivityType;
import com.emobc.android.CustomItemizedOverlay;
import com.emobc.android.CustomOverlayItem;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.MapDataItem;
import com.emobc.android.levels.impl.MapDataItemDistance;
import com.emobc.android.levels.impl.MapLevelDataItem;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "MAP". 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class MapActivityGenerator extends LevelActivityGenerator {
	private static final double CENTER_LATITUD = 40.4170;
	private static final double CENTER_LONGITUDE = -3.7030;

	private List<MapDataItemDistance> nearItems;

	private MapLevelDataItem dataItem;

	public MapActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		this.dataItem = (MapLevelDataItem) data.findByNextLevel(nextLevel);

		//rotateScreen(activity);
		initializeHeader(activity, dataItem);
		
		/*
		//NO FUNCIONA
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		*/
		
		if(dataItem != null){
			if (dataItem.isShowAllPositions()) {
				//TODO 
			}else {
				loadMapItems(activity);
			}				
		}
	}

	private void loadMapItems(Activity activity) {
		if (isOnline(activity)) {
			GeoPoint point;
			CustomOverlayItem overlayitem;
			CustomItemizedOverlay itemizedOverlay;
			Double latitudeE6, longitudeE6;
			MapController mapController;
			try {
				MapView mapView = (MapView) activity.findViewById(R.id.mapview);

				mapView.setBuiltInZoomControls(true);

				List<Overlay> mapOverlays = mapView.getOverlays();
				
				GeoPoint center = new GeoPoint((int) (40.4170 * 1E6), (int) (-3.7030 * 1E6));
				Location locationCenter = new Location("Center Point");
				locationCenter.setLatitude(CENTER_LATITUD);
				locationCenter.setLongitude(CENTER_LONGITUDE);
				
				nearItems = new ArrayList<MapDataItemDistance>();

				for (MapDataItem item : dataItem.getItems()) {
					Drawable drawable = ImagesUtils.getDrawable(activity, item.getIcon());
					itemizedOverlay = new CustomItemizedOverlay(drawable, activity);

					latitudeE6 = item.getLat();
					longitudeE6 = item.getLon();

					point = new GeoPoint((int) (latitudeE6 * 1E6), (int) (longitudeE6 * 1E6));
					overlayitem = new CustomOverlayItem(point, item.getTitle(), item.getAddress());
					overlayitem.setNextLevel(item.getNextLevel());
					itemizedOverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedOverlay);
					
					Location itemLocation = new Location(item.getTitle());
					itemLocation.setLatitude(item.getLat());
					itemLocation.setLongitude(item.getLon());
					float distance = locationCenter.distanceTo(itemLocation);
					nearItems.add(new MapDataItemDistance(item, distance));
				}

				
				Collections.sort(nearItems, new Comparator<MapDataItemDistance>(){
					@Override
					public int compare(MapDataItemDistance lhs, MapDataItemDistance rhs) {
						if(lhs.getDistance() == rhs.getDistance())
							return 0;
						else if (lhs.getDistance() > rhs.getDistance())
							return 1;
						return -1; 
					}					
				});
				
				mapController = mapView.getController();

				mapController.animateTo(center);
				mapController.setZoom(18);

				mapView.setSatellite(false);
			} catch (InvalidFileException e) {
				Log.e("MapActivityGenerator", e.getLocalizedMessage());
		    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
		}		
	}

	private boolean isOnline(Activity activity) {
		ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if (appLevel.getXib() != null && appLevel.getXib().length() > 0) {
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if (id > 0)
				return id;
		}
		return R.layout.map_screen;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.MAP_ACTIVITY;
	}
	
}
