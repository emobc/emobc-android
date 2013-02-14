/**
 * Copyright 2012 Neurowork Consulting S.L.
 *
 * This file is part of eMobc.
 *
 * ImagesUtils.java
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
package com.emobc.android.utils;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.emobc.android.activities.EMobcApplication;


/**
 * Utility to access all application images
 * @author Jorge E. Villaverde
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class ImagesUtils {

	private static final String DEFAULT_DRAWABLE = "drawable";

	private static final String TAG = "ImagesUtils";
	
	private static final String DEFAULT_IMAGE_PATH_IMAGES = "images" + File.separator;
	private static final String DEFAULT_IMAGE_PATH_DRAWABLE = DEFAULT_DRAWABLE + File.separator;
	
	private static final String DENSITY_LOW_IMAGE_PATH = "ldpi";
	private static final String DENSITY_MEDIUM_IMAGE_PATH = "mdpi";
	private static final String DENSITY_HIGH_IMAGE_PATH = "hdpi";
	private static final String DENSITY_XHIGH_IMAGE_PATH = "xhdpi";

	private static final String IMAGE_SEPARATOR = "-";

	private static final Object LANDSCAPE_PATH = "land";
	
	
	/**
	 * Class that implements a "counter bytes skipped" method
	 */
	public static class FlushedInputStream extends FilterInputStream {
	    public FlushedInputStream(InputStream inputStream) {
	    super(inputStream);
	    }

	    @Override
	    public long skip(long n) throws IOException {
	        long totalBytesSkipped = 0L;
	        while (totalBytesSkipped < n) {
	            long bytesSkipped = in.skip(n - totalBytesSkipped);
	            if (bytesSkipped == 0L) {
	                  int byteValue = read();
	                  if (byteValue < 0) {
	                      break;  // we reached EOF
	                  } else {
	                      bytesSkipped = 1; // we read one byte
	                  }
	           }
	           totalBytesSkipped += bytesSkipped;
	        }
	        return totalBytesSkipped;
	    }
	}
	
	/**
	 * Returns an image from an URL path
	 * @param url
	 * @return
	 * @throws InvalidFileException
	 */
	private static Drawable getDrawableFromUrl(String url) throws InvalidFileException {
		InputStream is;
		Log.i(TAG, "Loading Url: " + url);
		try {
			is = (InputStream) fetch(url);
			Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
			return new BitmapDrawable(bitmap);
		} catch (MalformedURLException e) {
			throw new InvalidParameterException("Invalid URL: " + url);
		} catch (IOException e) {
			throw new InvalidFileException(e.getLocalizedMessage());
		}
	}
	
	/**
	 * Returns a Drawable object from an image name in drawable/images folder.
	 * @param context
	 * @param imageName
	 * @return
	 * @throws InvalidFileException
	 */
	public static Drawable getDrawable(Activity activity, String imageName) throws InvalidFileException{
		try {
			if(imageName == null || imageName.isEmpty())
				return null;
			
			Drawable ret = null;
			EMobcApplication app = (EMobcApplication)activity.getApplication();
			
			String imageToFetchFromCache = null;
			if(Utils.isUrl(imageName)){
				imageToFetchFromCache = imageName;
			}else{
				imageToFetchFromCache = buildImagePathNameFromModifiers(imageName, activity);
			}
			
			if (app.getApplicationData().getCache().get(imageToFetchFromCache)!=null){
				Log.i(TAG, "Loading from cache: " + imageToFetchFromCache);
				return app.getApplicationData().getCache().get(imageToFetchFromCache);
			}else{
				String imageToCache = imageName; 
				if(Utils.isUrl(imageName)){
					ret = getDrawableFromUrl(imageName);
				}else{
		
					if(imageName.startsWith(DEFAULT_IMAGE_PATH_DRAWABLE)){
						ret = getDrawableFromName(activity, imageName);
					}else{
						ret = getDrawableFromName(activity, DEFAULT_IMAGE_PATH_DRAWABLE + imageName);
					}
					
					if(ret == null){
						String imagePathName = getImagesPathName(activity);
						String rawImageName = null;
						StringBuilder imageNameBuilder = new StringBuilder();
						
						if(imageName.startsWith(DEFAULT_IMAGE_PATH_IMAGES)){
							rawImageName = imageName.substring(DEFAULT_IMAGE_PATH_IMAGES.length());
						}else{
							rawImageName = imageName;
						}
						imageNameBuilder.append(imagePathName);
						imageNameBuilder.append(rawImageName);
						try {
							ret = getDrawableFromAssetName(activity, imageNameBuilder.toString());
							imageToCache = imageNameBuilder.toString();
						} catch (InvalidFileException e) {
							ret = getDrawableFromAssetName(activity, DEFAULT_IMAGE_PATH_IMAGES + rawImageName);
							imageToCache = DEFAULT_IMAGE_PATH_IMAGES + rawImageName;
						}
					}
						
				}
				app.getApplicationData().getCache().put(imageToCache, ret);
				return app.getApplicationData().getCache().get(imageToCache);
			}
		} catch (Exception e) {
			Log.d(TAG, "Drawable exception");
		}
		return null;
	}
	
	
	private static String buildImagePathNameFromModifiers(String imageName, Activity activity) {
		String imagePathName = getImagesPathName(activity);
		String rawImageName = null;
		StringBuilder imageNameBuilder = new StringBuilder();
		
		if(imageName.startsWith(DEFAULT_IMAGE_PATH_IMAGES)){
			rawImageName = imageName.substring(DEFAULT_IMAGE_PATH_IMAGES.length());
		}else{
			rawImageName = imageName;
		}
		imageNameBuilder.append(imagePathName);
		imageNameBuilder.append(rawImageName);
		return imageNameBuilder.toString();				
	}

	/**
	 * Return the directory where to find the application images based on the Display Metrics of the Context.
	 * <p>
	 * By default, images should be placed under <code>/assets/images/</code> directory.
	 * </p><p>
	 * According to the density detected, this are the images directories:
	 * <ul>
	 * 	<li><strong>Low Density</strong>: <code>/assets/images/ldpi/</code></li>
	 * 	<li><strong>Medium Density</strong>: <code>/assets/images/mdpi/</code></li>
	 * 	<li><strong>High Density</strong>: <code>/assets/images/hdpi/</code></li>
	 * 	<li><strong>Extra High Density</strong>: <code>/assets/images/xhdpi/</code></li>
	 * </ul>
	 * </p>
	 * Added landscape and portrait orientation.
	 * @param context
	 * @return Path to the application images directory.
	 */
	private static String getImagesPathName(Context context) {
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = display.getRotation();		
		
		StringBuilder builder = new StringBuilder();
		
		switch (context.getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			builder.append(DEFAULT_IMAGE_PATH_IMAGES);
			builder.append(DENSITY_LOW_IMAGE_PATH);
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			builder.append(DEFAULT_IMAGE_PATH_IMAGES);
			builder.append(DENSITY_MEDIUM_IMAGE_PATH);
			break;
		case DisplayMetrics.DENSITY_HIGH:
			builder.append(DEFAULT_IMAGE_PATH_IMAGES);
			builder.append(DENSITY_HIGH_IMAGE_PATH);
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			builder.append(DEFAULT_IMAGE_PATH_IMAGES);
			builder.append(DENSITY_XHIGH_IMAGE_PATH);
			break;
		default:
			builder.append(DEFAULT_DRAWABLE);
			break;		    
		}		
		
		switch (rotation) {
		case Surface.ROTATION_0:
		case Surface.ROTATION_180:
			// Portrait
			break;
		case Surface.ROTATION_90:
		case Surface.ROTATION_270:
			// Landscape
			builder.append(IMAGE_SEPARATOR);
			builder.append(LANDSCAPE_PATH);
			break;
		default:
			break;
		}
		
		builder.append(File.separator);
		
		return builder.toString();
	}

	/**
	 * Returns a Drawable object from assets/images path.
	 * @param context
	 * @param imageName
	 * @return
	 * @throws InvalidFileException
	 */
	private static Drawable getDrawableFromAssetName(Context context, String imageName) throws InvalidFileException {
		if(context == null)
			throw new IllegalArgumentException("Invalid context = null");
		if(imageName == null)
			throw new IllegalArgumentException("Invalid imageName = null");
		
    	Drawable drawable = null;    
    	
    	try {
    		Log.i(TAG, "Loading Image: " + imageName);
    		InputStream is = context.getAssets().open(imageName);
    		drawable = Drawable.createFromStream(is, "src name");
    	} catch (OutOfMemoryError e) {
    		throw new InvalidFileException("Image " +imageName+ " is to big");
    	} catch (IOException e) {
    		throw new InvalidFileException(e.getLocalizedMessage());
    	}
    	//drawable = new BitmapDrawable(decodeSampledBitmapFromResource(context, imageName, 100, 100));
    	
    	return drawable;
	}
	public static Bitmap decodeSampledBitmapFromResource(Context context, String imageName,
	        int reqWidth, int reqHeight) {
			
		try {
			InputStream is;
			is = context.getAssets().open(imageName);
			
		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(is, null, options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeStream(is, null, options);
		} catch (IOException e) {
			Log.e(TAG, "IOException: " + e.getMessage());
		}
	return null;
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
	}
	
	/**
	 * Returns a Drawable object from drawable path.
	 * @param context 
	 * @param imageName
	 * @return
	 * @throws InvalidFileException
	 */
	private static Drawable getDrawableFromName(Context context, String imageName) throws InvalidFileException {
		if(imageName == null)
			throw new IllegalArgumentException("Invalid imageName = null");
		
		Drawable drawable = null;    
    	Log.i(TAG, "Loading Image: " + imageName);
    	if (imageName.contains(".")){
    		imageName = imageName.split("\\.")[0];

    	}
    	try{
    		int id = context.getResources().getIdentifier(imageName, DEFAULT_DRAWABLE, context.getPackageName());
        	drawable = context.getResources().getDrawable(id);
    	} catch (NotFoundException e){
    		Log.w(TAG, "Error loading Image: drawable/" + imageName);
    	}
    	
    	/*try {
    		drawable = Drawable.createFromPath(imageName);
    	} catch (OutOfMemoryError e) {
    		throw new InvalidFileException("Image " +imageName+ " is to big");
    	}*/
    	return drawable;
		
	}
	
	/**
	 * Returns a content from an URL.
	 * @param address
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}		
}
