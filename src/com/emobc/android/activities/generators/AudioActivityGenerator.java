/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AudioActivityGenerator.java
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

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.AudioLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;

/**
 * This type of Activity reproduces an audio file from an URL.
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class AudioActivityGenerator extends LevelActivityGenerator {
	
	private SeekBar audioBar; 
	private ImageButton playPause;
	private ImageButton prev;
	private ImageButton next;
	private MediaPlayer mp;
	private Handler handler;
	private AudioLevelDataItem item;
	private boolean firstTime;
	private Runnable notification;
	private Activity activity;
	
	public AudioActivityGenerator(AppLevel appLevel, NextLevel nextLevel){
		super(appLevel, nextLevel);
		firstTime = true;
	}
	@Override
	protected void loadAppLevelData(Activity activity, AppLevelData data) {
		item = (AudioLevelDataItem)data.findByNextLevel(nextLevel);
		audioBar = (SeekBar) activity.findViewById(R.id.seekBarAudio);
		playPause = (ImageButton) activity.findViewById(R.id.playPauseButton);
		prev = (ImageButton) activity.findViewById(R.id.prevButton);
		next = (ImageButton) activity.findViewById(R.id.nextButton);
		mp = new MediaPlayer();
		handler = new Handler();
		this.activity = activity;
				
		initializeHeader(activity, item);
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner();
		
		//Audio Image
		ImageView audioImage = (ImageView) activity.findViewById(R.id.imageAudio);
		if (item.getImage()!=null){
			try {
				audioImage.setImageDrawable(ImagesUtils.getDrawable(activity, item.getImage()));
			} catch (InvalidFileException e1) {
				e1.printStackTrace();
			}
		}
		
		//Description
		//TextView description = (TextView) activity.findViewById(R.id.description);
		
		//Lyrics
		if (item.getLyrics()!=null){
			TextView lyrics = (TextView) activity.findViewById(R.id.lyrics);
			lyrics.setText(item.getLyrics());
		}
		
		
		//Media Player
		mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				audioBar.setSecondaryProgress(percent);
				
			}
		});
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				playPause.setImageResource(R.drawable.ic_media_play);
			}
		});
		mp.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				play();
			}
		});
		
		//Button Play/Pause
		playPause.setImageResource(R.drawable.ic_media_play);
		playPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playPause();				
			}

			
		});
		
		//Button next
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//Button prev
		prev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mp.seekTo(0);
				audioBar.setProgress(0);
			}
		});
		
		//Audio Bar
		audioBar.setMax(99);
		audioBar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(v.getId()==R.id.seekBarAudio){
					SeekBar sb = (SeekBar)v;
					int playPositionInMillisecconds = (mp.getDuration() / 100) * sb.getProgress();
					mp.seekTo(playPositionInMillisecconds);
				}
				return false;
			}
		});
	    
	}
	
	/**
	 * Updates primary bar progress
	 */
	private void primarySeekBarProgressUpdater() {
		audioBar.setProgress((int)(((float)mp.getCurrentPosition()/mp.getDuration())*100)); // This math construction give a percentage of "was playing"/"song length"
		if (mp.isPlaying()) {
			notification = new Runnable() {
				public void run() {
					if (mp.isPlaying()){
						primarySeekBarProgressUpdater();
					}
				}
			};
			handler.postDelayed(notification,1000);
		}
		
	}
	
	/**
	 * Play music
	 */
	private void play(){
		mp.start();
		playPause.setImageResource(R.drawable.ic_media_pause);
		primarySeekBarProgressUpdater();
	}
	/**
	 * Pause music
	 */
	private void pause(){
		mp.pause();
		playPause.setImageResource(R.drawable.ic_media_play);
		handler.removeCallbacks(notification);
	}
	
	/**
	 * Method for play/pause button. If is playing, pause. Else, play.
	 */
	private void playPause(){
		if (firstTime){
			try {
				if (item.isLocal()){
					//the audio is in assets/audio folder
					AssetFileDescriptor afd = activity.getAssets().openFd("audio/"+item.getAudioUrl());
					mp.setDataSource(afd.getFileDescriptor());
				}else{
					mp.setDataSource(item.getAudioUrl());
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.prepareAsync();
			firstTime = false;
		}else{
			if(mp.isPlaying()){
				pause();
			}else{
				play();
			}
		}

		
	}
	@Override
	protected int getContentViewResourceId(Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.audio_layout;
	}
	
	/**
	 * Clear threads and mediaPlayer
	 */
	public void finishAudio() {
		mp.release();
		handler.removeCallbacks(notification);
	}
	
	public void pauseAudio() {
		if (!firstTime){
			if (mp.isPlaying()){
				pause();
			}
		}
	}
	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.AUDIO_ACTIVITY;
	}
}
