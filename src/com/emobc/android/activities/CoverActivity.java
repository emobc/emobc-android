/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CoverActivity.java
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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.generators.AbstractActivtyGenerator;
import com.emobc.android.activities.generators.ActivityGenerator;
import com.emobc.android.levels.impl.ServerPushDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.CommonUtilities;
import com.emobc.android.utils.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;

/** 
 * Defines an activity of type COVER_ACTIVITY, and 
 * initialize all screen menu and the screen rotations. In its 
 * method onCreate(), call its ImageListActivityGenerator generator class if 
 * is not entry point. If not, call method show Next Level, and start another new activity.
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class CoverActivity extends CreateMenus {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6350857578407577615L;
	private AsyncTask<Void, Void, Void> mRegisterTask;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CoverActivity", "OnCreate Cover");
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        final ApplicationData applicationData = getApplicationData();
        
		if(applicationData != null){
			
			/*ENTRY POINT
			 * If the field <home> is empty, load the CoverActivity
			 * Else, load an Activity with the NextLevel
			 */
			setTitle(applicationData.getTitle());
						
			//GCM
			final ServerPushDataItem serverPushData = applicationData.getServerPush();
			if (serverPushData != null){
				String pushNotificationType = serverPushData.getType();
				if (pushNotificationType == ServerPushDataItem.GCM){
					// Make sure the device has the proper dependencies.
			        GCMRegistrar.checkDevice(this);
			        // Make sure the manifest was properly set - comment out this line
			        // while developing the app, then uncomment it when it's ready.
			        GCMRegistrar.checkManifest(this);
			        final String regId = GCMRegistrar.getRegistrationId(this);
			        if (regId.equals("")) {
			            // Automatically registers application on startup.
			        	
			            GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
			        } else {
			            // Device is already registered on GCM, check server.
			            if (GCMRegistrar.isRegisteredOnServer(this)) {
			                // Skips registration
			                Log.v("CoverActivity", GCMRegistrar.getRegistrationId(this));
			            } else {
			                // Try to register again, but not in the UI thread.
			                // It's also necessary to cancel the thread onDestroy(),
			                // hence the use of AsyncTask instead of a raw thread.
			                final Context context = this;
			                mRegisterTask = new AsyncTask<Void, Void, Void>() {
		
			                    @Override
			                    protected Void doInBackground(Void... params) {
			                        String appId = applicationData.getServerPush().getAppName(); 
									boolean registered = ServerUtilities.register(context, regId, appId, serverPushData.getServerUrl());
			                        // At this point all attempts to register with the app
			                        // server failed, so we need to unregister the device
			                        // from GCM - the app will try to register again when
			                        // it is restarted. Note that GCM will send an
			                        // unregistered callback upon completion, but
			                        // GCMIntentService.onUnregistered() will ignore it.
			                        if (!registered) {
			                            GCMRegistrar.unregister(context);
			                        }
			                        return null;
			                    }
		
			                    @Override
			                    protected void onPostExecute(Void result) {
			                        mRegisterTask = null;
			                    }
		
			                };
			                mRegisterTask.execute(null, null, null);
			            }
			        }
				}
			}
			
			NextLevel entryPoint = applicationData.getEntryPoint();
			setCurrentNextLevel(NextLevel.COVER_NEXT_LEVEL);
			
	        //Show next level
			if(entryPoint != null && entryPoint.isDefined()){
		        AbstractActivtyGenerator.showNextLevel(this, entryPoint, true);
			}else{
				ActivityGenerator generator = applicationData.getAppCoverData(this);
				generator.initializeActivity(this);
			}
			
		}else{
			Intent i = new Intent (this, SplashActivity.class);
			startActivity(i);
			finish();
		}

    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}