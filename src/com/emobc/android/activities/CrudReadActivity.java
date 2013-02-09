/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* CrudReadActivity.java
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

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.data.engine.DataEngine;
import com.emobc.android.data.metadata.Entity;
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;
import com.emobc.android.data.parse.ModelParser;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.parse.ParseUtils;

/**
 * Defines an activity of type CRUDREAD_ACTIVITY.
 * 
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class CrudReadActivity extends EMobcActivity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8093189867443396173L;
	public static final String CRUD_TABLE = "table";
	private DataEngine engine;
	private Model model;
	private Table table;
	private EntityListAdapter adpter;
	
	private static final int CRUD_CREATE_ENTITY = 1;
	private static final int CRUD_UPDATE_ENTITY = 2;
	
	public static final String CRUD_ENTITY = "_crud_entity_";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.crud_read);

		Intent intent = getIntent();  
		NextLevel nextLevel = (NextLevel)intent.getSerializableExtra(ApplicationData.NEXT_LEVEL_TAG);

		ApplicationData applicationData = getApplicationData();
		
		AppLevel level = applicationData.getNextAppLevel(nextLevel, this);
		
		ModelParser parser = new ModelParser(ParseUtils.createXpp(
    			this, 
    			Locale.getDefault(), 
    			level.getFileName(), 
    			false)); 
		
		this.model = parser.parse();
    	this.table = model.getTable(nextLevel.getDataId());
    	this.engine = new DataEngine(this, model);    	
   	
		TextView header = (TextView)findViewById(R.id.crud_header);
		header.setText(table.getName());
		
    	List<Entity> entities = engine.readAllEntities(table); 
    	
    	ListView lv = (ListView)findViewById(R.id.crud_list);
    	this.adpter = new EntityListAdapter(this, R.layout.list_item, entities);
		lv.setAdapter(adpter);
		lv.setTextFilterEnabled(true);
		
		registerForContextMenu(lv);
    }
    
    // Application Menu
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crud_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.crud_create:
                createEntity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    // Context Menu
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        
        menu.setHeaderTitle(((TextView)info.targetView).getText());
        
        inflater.inflate(R.menu.crud_context_menu, menu);
    }    
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        
        Entity entity = adpter.getEntityByPosition(info.position);
        
        switch (item.getItemId()) {
            case R.id.crud_open:
                openEntity(entity);
                return true;
            case R.id.crud_delete:
                deleteEntity(entity);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }   
    
    
    private void createEntity() {
    	Intent createEntityIntend = new Intent();
    	createEntityIntend = new Intent(this, CrudFormActivity.class);
    	createEntityIntend.putExtra(CRUD_TABLE, table);
		startActivityForResult(createEntityIntend, CRUD_CREATE_ENTITY);
    }
    
	private void deleteEntity(final Entity entity) {
		if(entity != null){
			
			String strFormat = getResources().getString(R.string.crud_delete_confirm_msg);
			String strMessage = String.format(strFormat, entity.getFieldValue("nombre")); 
			
			new AlertDialog.Builder(this).setTitle(R.string.crud_delete_confirm)
	        .setMessage(strMessage)
	        .setPositiveButton(android.R.string.ok, 
	        		new DialogInterface.OnClickListener() {
	        			@Override
	        			public void onClick(DialogInterface dialogInterface, int i) {
	        				adpter.removeEntity(entity);
	        				engine.deleteEntity(entity);
	        			}
	        })
	        .setNeutralButton(android.R.string.cancel, null) 
	        .create()
	        .show();		
		}
	}

	private void openEntity(final Entity entity) {
    	Intent createEntityIntend = new Intent();
    	createEntityIntend = new Intent(this, CrudFormActivity.class);
    	createEntityIntend.putExtra(CRUD_TABLE, table);
    	createEntityIntend.putExtra(CRUD_ENTITY, entity);
    	
		startActivityForResult(createEntityIntend, CRUD_UPDATE_ENTITY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			if(requestCode == CRUD_CREATE_ENTITY){
				Entity entity = (Entity)data.getSerializableExtra(CRUD_ENTITY);
				if(engine.createEntity(entity)){
					adpter.addEntity(entity);					
				}
			}else if(requestCode == CRUD_UPDATE_ENTITY){
				Entity entity = (Entity)data.getSerializableExtra(CRUD_ENTITY);
				if(engine.updateEntity(entity)){
					adpter.updateEntity(entity);
				}
			}
		}
	}	

	/**
	 * Class intended for the creation and initialization of listView
	 */
    private class EntityListAdapter extends BaseAdapter {
    	private List<Entity> items;
    	private Activity activity;
    	        
        public EntityListAdapter(Activity context, int textViewResourceId, List<Entity> objects) {
    		this.items = objects;
    		this.activity = context;
		}
    	
		public Entity getEntityByPosition(int position) {
			return items.get(position);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
    		TextView view = (convertView != null) ? (TextView) convertView : createView(parent);
    		final Entity item = items.get(position);
    		
    		view.setText(item.getFieldValue("nombre").toString());   		
    		
	    	return view;
    	}

    	private TextView createView(ViewGroup parent) {
    		TextView item = (TextView)activity.getLayoutInflater().inflate(
    				R.layout.crud_list_item, parent, false);
    		return item;
    	}
    	
		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void removeEntity(Entity entity){
			items.remove(entity);
			notifyDataSetChanged();
		}
		
    	public void addEntity(Entity entity) {
			items.add(entity);
			notifyDataSetChanged();
		}
    	
    	public void updateEntity(Entity entity) {
    		int location = items.indexOf(entity);
    		if(location != -1){
    			items.set(location, entity);
    			notifyDataSetChanged();    			
    		}
		}
    }
}
