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
package com.emobc.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class ZoomImageView extends View {
	private Drawable image;
	private int zoomControler=100;
	
	public ZoomImageView(Context context, Drawable drawable) {
		super(context);
		image = drawable;
		setFocusable(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//here u can control the width and height of the images........ this line is very important
		image.setBounds((getWidth()/2)-zoomControler, (getHeight()/2)-zoomControler, (getWidth()/2)+zoomControler, (getHeight()/2)+zoomControler);
	    image.draw(canvas);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_DPAD_UP)// zoom in
			zoomControler+=10;
		if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN) // zoom out
			zoomControler-=10;
		if(zoomControler<10)
			zoomControler=10;
	                     
		invalidate();
		return true;
	}
}