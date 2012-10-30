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
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.emobc.android.data.engine.DataEngine;
import com.emobc.android.data.metadata.Entity;
import com.emobc.android.data.metadata.Model;
import com.emobc.android.data.metadata.Table;
import com.emobc.android.data.parse.TableParser;
import com.emobc.android.parse.ParseUtils;

/**
 * @author Jorge E. Villaverde
 * @since 0.1
 * @version 0.1
 */
public class CrudReadActivity extends Activity {
	private TableParser parser;
	private DataEngine engine;
	private Model model;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.crud_read);

    	String tableName = getIntent().getExtras().getString("table");
    	
    	parser = new TableParser(ParseUtils.createXpp(
    			this, 
    			Locale.getDefault(), 
    			tableName, 
    			false));
    	
    	
    	Table table = parser.parse();

    	model = new Model("Test Model");
		model.addTable(table);
    	
    	engine = new DataEngine(this, model);

		Object[] data={"1", "Leo Messi", "01/10/1980", 180};
		engine.createEntity(table, data);
    	
    	List<Entity> entities = engine.readAllEntities(table); 
    	
    	ListView lv = (ListView)findViewById(R.id.crud_list);
		lv.setAdapter(new EntityListAdapter(this, R.layout.list_item, entities));
		lv.setTextFilterEnabled(true);
		
        unregisterForContextMenu(lv);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crud_menu, menu);
        return true;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crud_context_menu, menu);
    }    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
    	case R.id.crud_create:
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.crud_open:
                openEntity(info.id);
                return true;
            case R.id.crud_delete:
                deleteEntity(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }    
    
	private void deleteEntity(long id) {
		// TODO Auto-generated method stub
		
	}

	private void openEntity(long id) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Class intended for the creation and initialization of listView
	 */
    private class EntityListAdapter extends BaseAdapter {
    	private List<Entity> items;
    	private Activity activity;
    	private LayoutInflater inflater=null;
    	
        public class ViewHolder{
            @SuppressWarnings("unused")
			public Button button;
            public ImageView image;
        }
        
        public EntityListAdapter(Activity context, int textViewResourceId, List<Entity> objects) {
    		this.items = objects;
    		this.activity = context;
    		inflater = LayoutInflater.from(context);
		}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View vi=convertView;
            ViewHolder holder;
            final Entity item = items.get(position);
            if(convertView==null){
                vi = inflater.inflate(R.layout.list_item, null);
                holder=new ViewHolder();
               // button.setText(item.getText().toUpperCase());            
            }else{
                holder=(ViewHolder)vi.getTag();
            }
            
            View.OnClickListener listener = new View.OnClickListener() {
		        public void onClick(View view) {
//		        	showNextLevel(activity, item.getNextLevel());
		        	Toast.makeText(activity, item.getId(), Toast.LENGTH_SHORT).show();
		        }
            };

            Button button = (Button)vi.findViewById(R.id.selection_list);
            button.setText(item.getFieldValue("nombre").toString());
            button.setOnClickListener(listener);
            holder.button=button;
            holder.image=(ImageView)vi.findViewById(R.id.list_img);
            vi.setTag(holder);
                        
            return vi;
    	 }

    	@SuppressWarnings("unused")
		private LinearLayout createView(ViewGroup parent) {
    		 LinearLayout item = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
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
    }
}
