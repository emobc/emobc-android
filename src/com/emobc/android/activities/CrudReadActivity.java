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

import com.emobc.android.data.engine.DataEngine;
import com.emobc.android.data.metadata.Entity;
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;
import com.emobc.android.data.parse.TableParser;
import com.emobc.android.parse.ParseUtils;

/**
 * Defines an activity of type CRUDREAD_ACTIVITY.
 * 
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class CrudReadActivity extends Activity {
	private DataEngine engine;
	private Model model;
	private Table table;
	private EntityListAdapter adpter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.crud_read);

    	String tableName = getIntent().getExtras().getString("table");
    	
    	TableParser parser = new TableParser(ParseUtils.createXpp(
    			this, 
    			Locale.getDefault(), 
    			tableName, 
    			false));
    	
    	this.table = parser.parse();

    	model = new Model("Test Model");
		model.addTable(table);
    	
    	engine = new DataEngine(this, model);    	
   	
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
    	createEntityIntend.putExtra("table", table);
    	createEntityIntend.putExtra("model", model);
		
		startActivity(createEntityIntend);
		finish();
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
    }
}
