/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CoverActivityGenerator.java
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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.emobc.android.AppButton;
import com.emobc.android.activities.R;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;


/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "COVER". 
 * 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class CoverActivityGenerator extends AbstractActivtyGenerator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1094440374180322643L;
	private String backgroundFileName;
	private String titleFileName;
	private String facebookUrl;
	private String twitterUrl;
	private String facebookImage;
	private String twitterImage;
	private String wwwUrl;
	
	private List<AppButton> buttons;
	
	public CoverActivityGenerator() {
		super();
	}

	public String getBackgroundFileName() {
		return backgroundFileName;
	}

	public void setBackgroundFileName(String backgroundFileName) {
		this.backgroundFileName = backgroundFileName;
	}
	
	private void initializeButton(RelativeLayout mainLayout, final Activity activity, final AppButton button,int id){
		Button childButton = (Button)mainLayout.findViewById(id);
		Drawable imageButton;
		try {
			imageButton = ImagesUtils.getDrawable(activity, button.getFileName());
			if(imageButton != null){
				childButton.setBackgroundDrawable(imageButton);
			}else{
				childButton.setText(button.getTitle());
			}

			childButton.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					showNextLevel(activity, button.getNextLevel());
				}
			});
			
		} catch (InvalidFileException e) {
			Log.e("ImageUtils", "Image Fail: " + e);
		}			
	}

	@Override
	protected void intializeSubActivity(final Activity activity) {
		activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout mainLayout = (RelativeLayout)activity.findViewById(R.id.MainLayout);
		
		//rotateScreen(activity);
		
		if(Utils.hasLength(backgroundFileName)){
			Drawable backgroundDrawable;
			try {
				backgroundDrawable = ImagesUtils.getDrawable(activity, backgroundFileName);
				mainLayout.setBackgroundDrawable(backgroundDrawable);
			} catch (InvalidFileException e) {
				Log.e("AppCoverData", e.getLocalizedMessage());
		    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		
		ImageView titleImage = (ImageView) activity.findViewById(R.id.CoverTitle);
		if(Utils.hasLength(titleFileName)){
			Drawable titleDrawable;
			try {
				titleDrawable = ImagesUtils.getDrawable(activity, titleFileName);
				if (titleDrawable != null) 
					titleImage.setImageDrawable(titleDrawable);
				
			} catch (InvalidFileException e) {
				Log.e("AppCoverData", e.getLocalizedMessage());
		    	Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		} else {
			titleImage.setVisibility(View.GONE);
		}
		
		if(buttons != null && buttons.size() > 0){
			if(buttons.size() > 0){
				final AppButton button1 = buttons.get(0);
				if(button1 != null){					
					initializeButton(mainLayout, activity, button1, R.id.firstButton);
				}
			}
			
			if(buttons.size() > 1){
				final AppButton button2 = buttons.get(1);					
				if(button2 != null){
					initializeButton(mainLayout, activity, button2, R.id.secondButton);			
				}
			}
			
			if(buttons.size() > 2){
				final AppButton button3 = buttons.get(2);					
				if(button3 != null){
					initializeButton(mainLayout, activity, button3, R.id.thirdButton);			
				}
			
			}
			
			if(buttons.size() > 3){
				final AppButton button4 = buttons.get(3);
				if(button4 != null){
					if(button4 != null){
						initializeButton(mainLayout, activity, button4, R.id.fourthButton);			
					}			
				}
			}
			
			if(buttons.size() > 4){
				final AppButton button5 = buttons.get(4);
				if(button5 != null){
					if(button5 != null){
						initializeButton(mainLayout, activity, button5, R.id.fifthButton);			
					}			
				}
			}
			
			if(buttons.size() > 5){
				final AppButton button6 = buttons.get(5);
				if(button6 != null){
					if(button6 != null){
						initializeButton(mainLayout, activity, button6, R.id.sixthButton);			
					}			
				}
			}
		}		
		
		
		/*
		 * Add Web, Facebook & Twitter links
		 */
		ImageButton wwwButton = (ImageButton)mainLayout.findViewById(R.id.wwwButton);
		if(Utils.hasLength(wwwUrl)){
			if(wwwButton != null){
				wwwButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						launchUrl(activity, wwwUrl);
					}
				});
			}
		} else {
			if(wwwButton != null) 
				wwwButton.setVisibility(View.GONE);
		}
		
		ImageButton facebookButton = (ImageButton)mainLayout.findViewById(R.id.facebookButton);
		if(Utils.hasLength(facebookUrl)){
			if(facebookButton != null){
				facebookButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// If we place the user id of facebook, we can open the facebook app.
						if(facebookUrl.startsWith("http://www.facebook.com/"))
							launchUrl(activity, facebookUrl);
						else
							launchFacebookUrl(activity, facebookUrl);
					}
				});				
				if(Utils.hasLength(facebookImage)){
					Drawable facebookDrawable;
					try {
						facebookDrawable = ImagesUtils.getDrawable(activity, facebookImage);
						if (facebookDrawable != null) 
							facebookButton.setImageDrawable(facebookDrawable);
					} catch (InvalidFileException e) {
						Log.e("CoverActivityGenerator", e.getLocalizedMessage());
					}				
				}
			}
		} else {
			if(facebookButton != null)
				facebookButton.setVisibility(View.GONE);
		}
		
		ImageButton twitterButton = (ImageButton)mainLayout.findViewById(R.id.twitterButton);
		if(Utils.hasLength(twitterUrl)){			
			if(twitterButton != null){
				twitterButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						launchUrl(activity, twitterUrl);
					}
				});				
				if(Utils.hasLength(twitterImage)){
					Drawable twitterDrawable;
					try {
						twitterDrawable = ImagesUtils.getDrawable(activity, twitterImage);
						if (twitterDrawable != null) 
							twitterButton.setImageDrawable(twitterDrawable);
					} catch (InvalidFileException e) {
						Log.e("CoverActivityGenerator", e.getLocalizedMessage());
					}				
				}
			}
		} else {
			if(twitterButton != null) 
				twitterButton.setVisibility(View.GONE);
		}	
	}

	public void setTitleFileName(String titleFileName) {
		this.titleFileName = titleFileName;
	}

	public String getTitleFileName() {
		return titleFileName;
	}

	public void setButtons(List<AppButton> buttons) {
		this.buttons = buttons;
	}

	public List<AppButton> getButtons() {
		return buttons;
	}

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		return R.layout.main;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setWwwUrl(String wwwUrl) {
		this.wwwUrl = wwwUrl;
	}

	public String getWwwUrl() {
		return wwwUrl;
	}

	public String getFacebookImage() {
		return facebookImage;
	}

	public void setFacebookImage(String facebookImage) {
		this.facebookImage = facebookImage;
	}

	public String getTwitterImage() {
		return twitterImage;
	}

	public void setTwitterImage(String twitterImage) {
		this.twitterImage = twitterImage;
	}
}
