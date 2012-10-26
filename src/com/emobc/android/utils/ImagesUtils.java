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

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.emobc.android.activities.SplashActivity;


/**
 * Utility to access all application images
 * @author Jorge E. Villaverde
 * @author Jonatan Alcocer Luna
 * @version 0.1
 * @since 0.1
 */
public class ImagesUtils {

	private static final String DEFAULT_IMAGE_PATH_IMAGES = "images" + File.separator;
	private static final String DEFAULT_IMAGE_PATH_DRAWABLE = "drawable" + File.separator;
	
	private static final String DENSITY_LOW_IMAGE_PATH = "ldpi";
	private static final String DENSITY_MEDIUM_IMAGE_PATH = "mdpi";
	private static final String DENSITY_HIGH_IMAGE_PATH = "hdpi";
	private static final String DENSITY_XHIGH_IMAGE_PATH = "xhdpi";
	
	
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
		Log.i("ImagesUtils", "Loading Url: " + url);
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
	 * Returns true if the imageFile name starts with <code>http://</code> or <code>https://</code> 
	 * @param imageFile
	 * @return
	 */
	public static boolean isUrl(String imageFile){
		if(imageFile.startsWith("http://")||imageFile.startsWith("https://")){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Returns a Drawable object from an image name in drawable/images folder.
	 * @param context
	 * @param imageName
	 * @return
	 * @throws InvalidFileException
	 */
	public static Drawable getDrawable(Context context, String imageName) throws InvalidFileException{
		if(imageName == null || imageName.isEmpty())
			return null;
		Drawable ret = null;
		if (SplashActivity.getApplicationData().getCache().get(imageName)!=null){
			Log.i("ImagesUtils", "Loading from cache: " + imageName);
			return SplashActivity.getApplicationData().getCache().get(imageName);
		}else{
			if(imageName.startsWith("http://") || imageName.startsWith("https://")){
				ret = getDrawableFromUrl(imageName);
			}else{
	
				if(imageName.startsWith(DEFAULT_IMAGE_PATH_DRAWABLE)){
					ret = getDrawableFromName(context, imageName);
				}else{
					ret = getDrawableFromName(context, DEFAULT_IMAGE_PATH_DRAWABLE + imageName);
				}
				
				if(ret == null){
					String imagePathName = getImagesPathName(context);
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
						ret = getDrawableFromAssetName(context, imageNameBuilder.toString());
					} catch (InvalidFileException e) {
						ret = getDrawableFromAssetName(context, DEFAULT_IMAGE_PATH_IMAGES + rawImageName);
					}
				}
					
			}
			SplashActivity.getApplicationData().getCache().put(imageName, ret);
			return SplashActivity.getApplicationData().getCache().get(imageName);
		}		
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
	 * @param context
	 * @return Path to the application images directory.
	 */
	private static String getImagesPathName(Context context) {
		switch (context.getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			return DEFAULT_IMAGE_PATH_IMAGES + DENSITY_LOW_IMAGE_PATH + File.separator;
		case DisplayMetrics.DENSITY_MEDIUM:
			return DEFAULT_IMAGE_PATH_IMAGES + DENSITY_MEDIUM_IMAGE_PATH + File.separator;
		case DisplayMetrics.DENSITY_HIGH:
			return DEFAULT_IMAGE_PATH_IMAGES + DENSITY_HIGH_IMAGE_PATH + File.separator;
		case DisplayMetrics.DENSITY_XHIGH:
			return DEFAULT_IMAGE_PATH_IMAGES + DENSITY_XHIGH_IMAGE_PATH + File.separator;
		default:
			break;		    
		}		
		return DEFAULT_IMAGE_PATH_DRAWABLE;
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
    		Log.i("ImagesUtils", "Loading Image: " + imageName);
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
			Log.e("ImagesUtils", "IOException: " + e.getMessage());
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
    	Log.i("ImagesUtils", "Loading Image: " + imageName);
    	if (imageName.contains(".")){
    		imageName = imageName.split("\\.")[0];

    	}
    	try{
    		int id = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        	drawable = context.getResources().getDrawable(id);
    	} catch (NotFoundException e){
    		Log.e("ImagesUtils", "Error loading Image: drawable/" + imageName);
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
