/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* SearchActivity.java
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

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.SearchResult;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.menu.CreateMenus;

/** 
 * Class that defines an activity of type SEARCH_ACTIVITY, and 
 * initialize all screen menu and the screen rotations. 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public class SearchActivity extends CreateMenus {
	protected static final Context Activity = null;
    private EditText textResult;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        
        setContentView(R.layout.search_screen);
        
        boolean isEntryPoint = false;
        rotateScreen(this);
        
        Intent intent = getIntent();
		isEntryPoint=(Boolean)intent.getSerializableExtra(ApplicationData.IS_ENTRY_POINT_TAG);
		   
        textResult = (EditText) findViewById (R.id.editText1);

        Button searchButton = (Button) findViewById (R.id.SearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				locateText(textResult.getText().toString());
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
}
        });
      //createToolBar(isEntryPoint);
        createMenus(this, isEntryPoint);
    }
    
    public void locateText (String searchText){
		ProgressDialog dialog = new ProgressDialog(this);
		try {
			dialog.setMessage("Buscando...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
			
			ApplicationData applicationData = SplashActivity.getApplicationData();
			List<SearchResult> lista = applicationData.findWidthText(this, searchText.toString());
			if(lista != null){
				Log.e("eMobc", "Tamaño: " + lista.size());
				ListView lv = (ListView) findViewById(R.id.searchList);	
				lv.setAdapter(new SearchListAdapter(this, R.layout.list_item, lista));
				lv.setTextFilterEnabled(true);
			}
			

		}finally{
			dialog.hide();
		}    
    }
   
    
    private class SearchListAdapter extends ArrayAdapter<SearchResult> {
    	private List<SearchResult> items;
    	private Activity activity;
    	
    	public SearchListAdapter(Activity context, int textViewResourceId, List<SearchResult> objects) {
    		super(context, textViewResourceId, objects);
    		this.items = objects;
    		this.activity = context;
		}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		LinearLayout view = (convertView != null) ? (LinearLayout) convertView : createView(parent);
    		final SearchResult item = items.get(position);
            
    		View.OnClickListener listener = new View.OnClickListener() {
		        public void onClick(View view) {
		        	showNextLevel(activity, item.getNextLevel());		        	
		        }
            };

            Button button = (Button)view.findViewById(R.id.searchList);
            button.setText(item.getText().toUpperCase());
            button.setBackgroundResource(R.drawable.list_selector);
            button.setOnClickListener(listener);
            
            // Set font
            Typeface ubuntu = Typeface.createFromAsset(activity.getApplicationContext().getAssets(), "fonts/Ubuntu-Medium.ttf");
    		button.setTypeface(ubuntu);
    		
	    	return view;
    	 }
    	
    	public void showNextLevel(Context context, NextLevel nextLevel) {
			 if(nextLevel != null){
				ApplicationData applicationData = SplashActivity.getApplicationData();
				AppLevel level = applicationData.getNextAppLevel(nextLevel, context);
				if(level != null){
					Class<? extends Activity> clazz = level.getAcivityClass();
					
					Intent launchActivity = new Intent(context, clazz);				
					launchActivity.putExtra(ApplicationData.NEXT_LEVEL_TAG, nextLevel);				
					context.startActivity(launchActivity);
				}else{
					CharSequence text = "Invalid Next Level: " + nextLevel.toString();
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();					
				}  
			} 
		}


    	 private LinearLayout createView(ViewGroup parent) {
    		 LinearLayout item = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
    		 return item;
    	 }
    }
}