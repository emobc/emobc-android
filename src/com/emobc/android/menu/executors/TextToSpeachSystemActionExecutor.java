/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* TextToSpeachSystemActionExecutor.java
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
package com.emobc.android.menu.executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.emobc.android.activities.R;
import com.emobc.android.menu.SystemAction;
import com.google.tts.TextToSpeechBeta;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class TextToSpeachSystemActionExecutor extends ContentAwareSystemActionExecutor
	implements TextToSpeechBeta.OnInitListener{

	protected static final String LOG_TAG = "TextToSpeachSystemActionExecutor";
	
	/**
     * The TTS engine. Only one of this and mTtsExtended will be non-null at a time.
     */
    private TextToSpeechBeta mTts = null;
	
	/**
	 * @param context
	 */
	public TextToSpeachSystemActionExecutor(Activity context) {
		super(context, SystemAction.TTS);
	}

	
	@Override
	protected void executeContentAwareSystemAction(String activityContent) {
        
        mTts = new TextToSpeechBeta(context, this);
		
		int result = mTts.speak(activityContent, 0, null);					
		
		if(result == TextToSpeechBeta.SUCCESS){
	        new AlertDialog.Builder(context)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setMessage(R.string.reading)
	        .setPositiveButton(R.string.reading_stop, new DialogInterface.OnClickListener() {
	
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	mTts.stop();
					context.finish();
	            }
	
	        })
	        .setNegativeButton(R.string.reading_continue, null)
	        .show();
		}else{
			Toast toast = Toast.makeText(context, "TTS Error", Toast.LENGTH_SHORT);
			toast.show();					
		}
	}


    @Override
    public void onInit(int status, int version) {
        if (status != TextToSpeechBeta.SUCCESS) {
            Log.e(LOG_TAG, "TTS extended init failed.");
            return;
        }
    }
}
