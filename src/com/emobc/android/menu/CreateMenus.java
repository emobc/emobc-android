/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CreateMenus.java
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
package com.emobc.android.menu;


import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.R;
import com.emobc.android.activities.SplashActivity;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.impl.BannerDataItem;
import com.emobc.android.menu.builders.HorizontalMenuBuilder;
import com.emobc.android.menu.builders.MenuBuilder;
import com.emobc.android.menu.parse.MenuParser;
import com.emobc.android.parse.ParseUtils;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.Utils;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


/**
 * Class that contains the logic for create all menus of a screen depending on
 * the content of app.xml file
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class CreateMenus extends Activity implements AnimationListener {
	
	private static final String ROTATION_LANDSCAPE = "landscape";
	private static final String ROTATION_PORTRAIT = "portrait";
	private static final String ROTATION_BOTH = "both";
	private String contextMenuXmlFileName;
	private com.emobc.android.menu.Menu contextMenu;
	
	private boolean isEntryPoint;
	
	private LinearLayout sideMenuLayout;
	private RelativeLayout appLayout;
	private boolean menuOut = false;
    private AnimParams animParams = new AnimParams();
 
	/**
	 * Class for intercept call phone.
	 */
	class TeleListener extends PhoneStateListener    {
    	public void onCallStateChanged(int state, String incomingNumber){
    		super.onCallStateChanged(state, incomingNumber);
    		switch (state)  {
    		case TelephonyManager.CALL_STATE_IDLE:
    			break;
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			break;
    		case TelephonyManager.CALL_STATE_RINGING:
    			break;
    		default:
    			break;
    		}
    	}
    }
	
	/**
	 * Class for start a screen animation when a view is pressed
	 */
	class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
        	startAnimationMenu();
        }
    }
	
	/**
	 * Class that contains animation parameters
	 */
	static class AnimParams {
        int left, right, top, bottom;

        void init(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }
		
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	//Unbind drawables from parent layout
    	if (this.findViewById(R.id.parent)!=null){
    		unbindDrawables(this.findViewById(R.id.parent));
    	}
    	
    }
    /**
     * Unbinds all drawables in a given view (and its child tree).
     * 
     * @param findViewById     Root view of the tree to unbind
     */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            try
            {
                ((ViewGroup) view).removeAllViews();
            }
            catch(UnsupportedOperationException ignore)
            {
                //if can't remove all view (e.g. adapter view) - no problem 
            }
        }
    }

    /**
     * Creates each menu if is defined in app.xml file (if the field is empty
     * in app.xml file, simply ignore the creation.)
     * @param activity
     * @param isEntryPoint
     */
	protected void createMenus(){		
		ApplicationData applicationData = SplashActivity.getApplicationData();
		
		int menuHeight = 0;
		
		//TOP MENU
		LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
		final String topMenuXml = applicationData.getTopMenu();
		
		if(topLayout != null && Utils.hasLength(topMenuXml))
			menuHeight += buildMenu(topLayout, topMenuXml, new HorizontalMenuBuilder());
		
		//BOTTOM MENU
		LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
		final String bottomMenu = applicationData.getBottomMenu();
		
		if(bottomLayout != null && Utils.hasLength(bottomMenu))
			menuHeight += buildMenu(bottomLayout, bottomMenu, new HorizontalMenuBuilder());
		
		//CONTEXT MENU
		final String contextMenu = applicationData.getContextMenu();
		if(Utils.hasLength(contextMenu)){
			this.contextMenuXmlFileName = contextMenu;
		}

		if(menuHeight > 0){	
			Display display = getWindowManager().getDefaultDisplay();
			int height = display.getHeight() - menuHeight;		
			
			RelativeLayout contentLayout = (RelativeLayout)findViewById(R.id.contentLayout);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 
					height);
			contentLayout.setLayoutParams(lp);
		}
		//SIDE MENU
//		final String sideMenu = applicationData.getSideMenu();
//		if(Utils.hasLength(sideMenu)){
//			initializeSideMenuList(sideMenu);
//		}
	}
	
	protected int buildMenu(LinearLayout layout, String menuXmlFileName, MenuBuilder menuBuilder) {
		// If no layout, noting to do.
		if(layout == null)
			return 0;
		// If no xml, hide layout
		if(!Utils.hasLength(menuXmlFileName)){
			layout.setVisibility(View.GONE);
		}
		// If no menu build, nothing to do
		if(menuBuilder == null)
			return 0;
				
		// Create Menu Parser
		MenuParser menuParser = new MenuParser(ParseUtils.createXpp(
    			this, 
    			Locale.getDefault(), 
    			menuXmlFileName, 
    			false));
		// Parse Context Menu File
		com.emobc.android.menu.Menu menu  = menuParser.parse();
		
		if(menu != null){
			// Invoke the builder	
			return menuBuilder.buildMenu(this, menu, layout);
		}else{
			layout.setVisibility(View.GONE);
			return 0;
		}
	}
	
	//  -- Context_Menu methods
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 if(Utils.hasLength(contextMenuXmlFileName)){
			 createContextMenu(menu);
		     return true;
		 }
		 Log.i("CreateMenus", "No existe menu contextual");
		 return false;			 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int selection = item.getItemId();
		
		if(selection >= 0 && selection < this.contextMenu.getItems().size()){
			com.emobc.android.menu.MenuItem itemSelected = this.contextMenu.getItems().get(selection);
		
			if(itemSelected != null){
				itemSelected.executeMenuItem(this);
				return true;				
			}
		}		
		return false;
						
	}
	
	/**
	 * Special method for creating the context menu. 
	 * Note: The context menu is a predefined menu of Android, so 
	 * that part of initializing the components to implement an 
	 * action is necessary to override the system selection methods.
	 * @param contextMenu
	 */
	private void createContextMenu(Menu contextMenu) {
		// Create Menu Parser
		MenuParser menuParser = new MenuParser(ParseUtils.createXpp(
    			this, 
    			Locale.getDefault(), 
    			contextMenuXmlFileName, 
    			false));
		// Parse Context Menu File
		this.contextMenu = menuParser.parse();
		
		if(this.contextMenu != null){
			// Add Menu Items to Android Context Menu
			List<com.emobc.android.menu.MenuItem> items = this.contextMenu.getItems();
			for(int i=0; i < items.size(); i++){
				com.emobc.android.menu.MenuItem item = items.get(i);
				
				MenuItem menuItem = contextMenu.add(Menu.NONE, i, Menu.NONE, item.getTitle());
				
				if(Utils.hasLength(item.getImageFileName())){
					Drawable icon;
					try {
						icon = ImagesUtils.getDrawable(this, item.getImageFileName());
						menuItem.setIcon(icon);
					} catch (InvalidFileException e) {
						Log.e("CreateMenus", e.getLocalizedMessage());
					}						
				}
			}				
		}
	}
	
	//  -- Side_Menu methods
	
	/**
	 * Starts an animation to make room for SIDEMENU
	 */
	private void startAnimationMenu(){
		CreateMenus me = CreateMenus.this;
        TextView menuHeader = (TextView) findViewById(R.id.MenuHeader);
        menuHeader.setText("SideMenu options");
        Animation anim;

        int w = appLayout.getMeasuredWidth();
        int h = appLayout.getMeasuredHeight();
        
        int left = (int) (appLayout.getMeasuredWidth() * 0.8);

        if (!menuOut) {//OPEN SIDEMENU
            anim = new TranslateAnimation(0, left, 0, 0);
            sideMenuLayout.setVisibility(View.VISIBLE);
            animParams.init(left, 0, left + w, h);
        } else {//CLOSE SIDEMENU
            anim = new TranslateAnimation(0, -left, 0, 0);
            animParams.init(0, 0, w, h);
        }

        anim.setDuration(500);
        anim.setAnimationListener(me);

        // Only use fillEnabled and fillAfter if we don't call layout ourselves.
        // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
        // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
        // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
        // work.
        // anim.setFillEnabled(true);
        // anim.setFillAfter(true);

        appLayout.startAnimation(anim);
	}

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.v("CreateMenus", "onAnimationEnd");
        menuOut = !menuOut;
        if (!menuOut) {
            sideMenuLayout.setVisibility(View.INVISIBLE);
        }
        appLayout.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    	Log.v("CreateMenus", "onAnimationRepeat");
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.v("CreateMenus", "onAnimationStart");
    }
    				
	/**
	 * Start a new activity in the levelId leaning and dataId of NextLevel. 
	 * Also initializes parameters NextLevel and entrypoint 
	 * @param context
	 * @param nextLevel
	 */
	protected void showNextLevel(Context context, NextLevel nextLevel) {
		if(nextLevel != null && nextLevel.isDefined()){
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
				Log.d("CreateMenus", "CreateMenus");
			}  
		} 
	}
	
	//  -- onCreate
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyMgr.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);        
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if (!menuOut) {
    		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                if(this.isEntryPoint!=true){
                	return super.onKeyDown(keyCode, event);
                }else{
                	moveTaskToBack(true);
                    return true;
                }
            }    
		}else{
			startAnimationMenu();
		}
    	return false;
    }
    
    /**
     * Set activity rotation depending to field form app.xml file
     * Rotation types: landscape(horizontal), portrait (vertical), 
     * both (horizontal|vertical) its determined by the sensor
     * @param activity
     */
    public static void rotateScreen(Activity activity){
		ApplicationData applicationData = SplashActivity.getApplicationData();
		String rotation = applicationData.getRotation();
		rotateScreen(activity, rotation);
    }
	
    /**
     * Set activity rotation depending to field form app.xml file
     * Rotation types: landscape(horizontal), portrait (vertical), 
     * both (horizontal|vertical) its determined by the sensor
     * @param activity
     */
	private static void rotateScreen(Activity activity, String rotation){
		if(!Utils.hasLength(rotation))
			return;
		
		if(ROTATION_BOTH.equals(rotation)){
    		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    	}else if(ROTATION_PORTRAIT.equals(rotation)){
    		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	}else if(ROTATION_LANDSCAPE.equals(rotation)){
    		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	
    	}else{
    		Log.e("CreateMenus", "Rotation undefined: '"+rotation+"'");
    		rotateScreen(activity, ROTATION_BOTH);//Default
    	}
	}
    
    /**
	 * Show a banner in the activity with the information of his application Data item
	 * If there is no information about the banner, it will not show
	 */
	public void createBanner(){
		try {
			ApplicationData applicationData = SplashActivity.getApplicationData();
			BannerDataItem banner = applicationData.getBanner();
			if (banner!=null){
				LinearLayout bannerLayout;
				
				//Banner position
				switch (banner.getPosition()){
				case BannerDataItem.TOP :
					bannerLayout = (LinearLayout) findViewById(R.id.bannerTopLayout);
					break; 
				case BannerDataItem.BOTTOM :
					bannerLayout = (LinearLayout) findViewById(R.id.bannerBottomLayout);
					break;
				default:
					bannerLayout = (LinearLayout) findViewById(R.id.bannerTopLayout);
				}
				
				//Banner data
				String id = banner.getId();
				
				AdView adView = new AdView(this, AdSize.SMART_BANNER, id);
				bannerLayout.addView(adView);
				
				AdRequest request = new AdRequest();
				
				//Just for testing
				request.addTestDevice(AdRequest.TEST_EMULATOR);
				adView.loadAd(request);
				
			}
		} catch (Exception e) { 
			Log.d("CreateBanner", "Error"); 
		}
	}

	public boolean isEntryPoint() {
		return isEntryPoint;
	}

	public void setEntryPoint(boolean isEntryPoint) {
		this.isEntryPoint = isEntryPoint;
	}
}

