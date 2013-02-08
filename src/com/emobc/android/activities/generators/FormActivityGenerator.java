/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* FormActivityGenerator.java
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.emobc.android.ActivityType;
import com.emobc.android.ApplicationData;
import com.emobc.android.NextLevel;
import com.emobc.android.activities.FormActivity;
import com.emobc.android.activities.R;
import com.emobc.android.levels.AppLevel;
import com.emobc.android.levels.AppLevelData;
import com.emobc.android.levels.impl.FormDataItem;
import com.emobc.android.levels.impl.FormLevelDataItem;
import com.emobc.android.menu.CreateMenus;
import com.emobc.android.utils.ImagesUtils;
import com.emobc.android.utils.ImagesUtils.FlushedInputStream;
import com.emobc.android.utils.InvalidFileException;
import com.emobc.android.utils.RetreiveFileContentTask;
import com.emobc.android.utils.Utils;

/**
 * Screen generator, responsible for specific components to initialize the 
 * display type "FORM". It also creates the menus, rotations, and the format for 
 * the components.
 * 
 * @author Jonatan Alcocer Luna
 * @author Jorge E. Villaverde
 * @author Alejandro S‡nchez Acosta
 * @version 0.1
 * @since 0.1
 */
public class FormActivityGenerator extends LevelActivityGenerator {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2121008326910267522L;
	private Map<String,View> controlsMap;
	private FormLevelDataItem item;
	private String imageContent;
	
	public FormActivityGenerator(AppLevel appLevel, NextLevel nextLevel) {
		super(appLevel, nextLevel);
	}

	@Override
	protected void loadAppLevelData(final Activity activity, final AppLevelData data) {
		this.item = (FormLevelDataItem)data.findByNextLevel(nextLevel);
		
		//rotateScreen(activity);
		initializeHeader(activity, item);
		
		controlsMap = new HashMap<String, View>();
		imageContent = null;
		
		//Create Banner
		CreateMenus c = (CreateMenus)activity;
		c.createBanner(); 
		
		LinearLayout formLayout = (LinearLayout)activity.findViewById(R.id.formLayout);
		for(FormDataItem dataItem : item.getList()){
			TextView label = new TextView(activity);
			label.setText(dataItem.getFieldLabel());
			formLayout.addView(label);
			insertField(activity, dataItem, formLayout);
		}
		
		Button submit = new Button(activity);
		LayoutParams buttonParams = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		submit.setLayoutParams(buttonParams);
		
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSubmit(activity);
			}
		});
		
		if(Utils.hasLength(item.getSubmitImage())){
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(activity, item.getSubmitImage());
				submit.setBackgroundDrawable(drawable);
			} catch (InvalidFileException e) {
				Log.e("FormActivityGenerator", e.getLocalizedMessage());
			}						
		}else{
			submit.setText(R.string.form_submit_buttom);			
		}
		
		formLayout.addView(submit);
		((FormActivity) activity).setControlsMap(controlsMap);
	}

	/**
	 * Check the contents of a web address to add new data to existing 
	 * app.xml file. Then, it goes to the next level.
	 * @param activity
	 */
	protected void processSubmit(Activity activity) {
		try {
			List<NameValuePair> parameters = createParameters(activity);			
			try {
				URL url = new URL(item.getActionUrl());

				RetreiveFileContentTask task = new RetreiveFileContentTask(parameters, true); 
				task.execute(url);
				try {
					String text = task.get();
		        	
		        	ApplicationData.mergeAppDataFromString(activity, text);
		        	
		        	showNextLevel(activity, item.getNextLevel());
				} catch (InterruptedException e) {
			    	Log.e("FormActivityGenerator: InterruptedException: ", e.getMessage());
				} catch (ExecutionException e) {
			    	Log.e("FormActivityGenerator: ExecutionException: ", e.getMessage());
				}
			} catch (MalformedURLException e1) {
			}		    
		} catch (final RequiredFieldException e) {
			final AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
			dlg.setTitle(R.string.form_level_title);
			dlg.setMessage(R.string.form_level_alert_required_fields);
			dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					View view = controlsMap.get(e.getDataItem().getFieldName());
					view.requestFocus();
				}
			});
			dlg.show();
		}
	}

	/**
	 * Create the basic parameters to establish a connection
	 * @param activity 
	 * @return
	 * @throws RequiredFieldException
	 */
	private List<NameValuePair> createParameters(Activity activity) throws RequiredFieldException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(item.getList().size());
		
		for(FormDataItem dataItem : item.getList()){
			View view = controlsMap.get(dataItem.getFieldName());
			final String paramValue = getControlText(view, dataItem, activity);
			
			if(paramValue != null && paramValue.length() > 0){
		        nameValuePairs.add(new BasicNameValuePair(dataItem.getFieldName(), paramValue));
			}else{
				if(dataItem.isRequired())
					throw new RequiredFieldException(dataItem);
			}			
		}
		return nameValuePairs;
	}

	/**
	 * Sets the text characteristics depending on the particular type
	 * @param view
	 * @param dataItem
	 * @param activity 
	 * @return String
	 */
	private String getControlText(View view, FormDataItem dataItem, Activity activity) {
		if(dataItem.getType() == null)
			return "";
		
		String ret = "";
		switch (dataItem.getType()) {
		case INPUT_LABEL:
		case INPUT_TEXT:
		case INPUT_NUMBER:
		case INPUT_EMAIL:
		case INPUT_PHONE:
		case INPUT_PASSWORD:
		case INPUT_TEXTVIEW:
			ret  = ((EditText)view).getText().toString();
			break;
		case INPUT_CHECK:
			ret  = ((CheckBox)view).isChecked() ? "true":"false";
			break;
		case INPUT_PICKER:
			ret = ((Spinner)view).getSelectedItem().toString();
			break; 
		case INPUT_IMAGE:
			ret = base64EncodedImage(activity);
			break;
		default:
			break;
		}
		return ret;
	}

	private static String getRealPathFromURI(Activity activity, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	private String base64EncodedImage(Activity activity) {
		if(imageContent == null || imageContent.isEmpty())
			return null;
		
		InputStream is;

		try {
			Uri uri = Uri.parse(imageContent);
			String fileName = getRealPathFromURI(activity, uri);
			
			is =  new FileInputStream(new File(fileName));
			Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		    byte[] b = baos.toByteArray();
		    String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
		    
		    return imageEncoded;
		} catch (IOException e) {
			Log.e("FormActivityGenerator: base64EncodedImage", e.getMessage());
		}
		return null;
	}

	/**
	 * Inserts a dataItem to View depending on the particular type
	 * @param activity
	 * @param dataItem
	 * @param formLayout
	 */
	private void insertField(Activity activity, FormDataItem dataItem, LinearLayout formLayout) {
		View control = null;
		
		if(dataItem.getType() == null)
			return;
		
		switch (dataItem.getType()) {
		case INPUT_LABEL:
			break;
		case INPUT_TEXT:
			control = insertTextField(activity, dataItem);
			break;
		case INPUT_NUMBER:
			control = insertNumberField(activity, dataItem);
			break;
		case INPUT_EMAIL:
			control = insertEmailField(activity, dataItem);
			break;
		case INPUT_PHONE:
			control = insertPhoneField(activity, dataItem);
			break;
		case INPUT_PASSWORD:
			control = insertPasswordField(activity, dataItem);
			break;
		case INPUT_CHECK:
			control = insertCheckField(activity, dataItem);
			break;
		case INPUT_PICKER:
			control = insertPickerField(activity, dataItem);
			break;
		case INPUT_TEXTVIEW:
			control = insertTextViewField(activity, dataItem);
			break;
		case INPUT_IMAGE:
			control = insertImageField(activity, dataItem);
			break;
		default:
			break;
		}
		
		if (control != null) {
			formLayout.addView(control);
			controlsMap.put(dataItem.getFieldName(), control);
		}
	}

	private View insertImageField(final Activity activity, FormDataItem dataItem) {
		Button imageButton = new Button(activity);
		imageButton.setText("Imagen");
		
		if(Utils.hasLength(item.getCameraImage())){
			Drawable drawable;
			try {
				drawable = ImagesUtils.getDrawable(activity, item.getCameraImage());
				imageButton.setBackgroundDrawable(drawable);
			} catch (InvalidFileException e) {
				Log.e("FormActivityGenerator", e.getLocalizedMessage());
			}
		}
		
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    // Camera.
			    final List<Intent> cameraIntents = new ArrayList<Intent>();
			    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			    final PackageManager packageManager = activity.getPackageManager();
			    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
			    for(ResolveInfo res : listCam) {
			        final String packageName = res.activityInfo.packageName;
			        final Intent intent = new Intent(captureIntent);
			        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			        intent.setPackage(packageName);
			        cameraIntents.add(intent);
			    }

			    // Filesystem.
			    final Intent galleryIntent = new Intent();
			    galleryIntent.setType("image/*");
			    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

			    // Chooser of filesystem options.
			    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

			    // Add the camera options.
			    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

				activity.startActivityForResult(chooserIntent, FormActivity.PICK_IMAGE_REQUEST);				
			}
		});
		
		return imageButton;
	}

	private View insertTextViewField(Context context, FormDataItem dataItem) {
		EditText txt = insertTextField(context, dataItem);
		txt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		txt.setSingleLine(false);
		return txt;
	}

	private View insertPickerField(Context context, FormDataItem dataItem) {
	    Spinner spinner = new Spinner(context);
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
	    		android.R.layout.simple_spinner_item, 
	    		dataItem.getParameters());
	    spinner.setAdapter(adapter);
	    return spinner;		
	}

	private View insertCheckField(Context context, FormDataItem dataItem) {
		CheckBox checkBox = new CheckBox(context);
		checkBox.setTag(dataItem.getFieldName());
		return checkBox;		
	}

	private EditText insertPasswordField(Context context, FormDataItem dataItem) {
		EditText txt = insertTextField(context, dataItem);
		txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		return txt;
	}
	
	private EditText insertPhoneField(Context context, FormDataItem dataItem) {
		EditText txt = insertTextField(context, dataItem);
		txt.setInputType(InputType.TYPE_CLASS_PHONE);
		return txt;
	}

	private EditText insertEmailField(Context context, FormDataItem dataItem) {
		EditText txt = insertTextField(context, dataItem);
		txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		return txt;
	}

	private EditText insertNumberField(Context context, FormDataItem dataItem) {
		EditText txt = insertTextField(context, dataItem);
		txt.setInputType(InputType.TYPE_CLASS_NUMBER);
		return txt;
	}

	private EditText insertTextField(final Context context, FormDataItem dataItem) {
		EditText txt = new EditText(context);
		txt.setTag(dataItem.getFieldName());
		txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (!hasFocus) {
		        	InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        }
		    }
		});
		return txt;
	}

	@Override
	protected int getContentViewResourceId(final Activity activity) {
		if(appLevel.getXib() != null && appLevel.getXib().length() > 0){
			int id = getActivityLayoutIdFromString(activity, appLevel.getXib());
			if(id >0)
				return id;
		}
		return R.layout.form;
	}

	@Override
	protected ActivityType getActivityGeneratorType() {
		return ActivityType.FORM_ACTIVITY;
	}

	public String getImageContent() {
		return imageContent;
	}

	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}
}
