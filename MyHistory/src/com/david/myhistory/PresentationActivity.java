package com.david.myhistory;

import java.io.IOException;

import com.david.filecontrol.GalleryFileControl;
import com.david.myhistory.GalleryActivity.ImageAdapter;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Build;

public class PresentationActivity extends Fragment {

	AnimationDrawable frameAnimation;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_presentation, container, false);
		
		GalleryFileControl galleryFile = new GalleryFileControl(getActivity());
		String paths[] = galleryFile.readFile();
		
		

		view.setBackgroundColor(Color.BLACK);

		// Load the ImageView that will host the animation and
		// set its background to our AnimationDrawable XML resource.
		ImageView img = (ImageView) view.findViewById(R.id.imageView2);
		img.setImageBitmap(null);

		
		img.setBackgroundResource(R.animator.spin_animation);

		// Get the background, which has been compiled to an AnimationDrawable object.
		frameAnimation = (AnimationDrawable) img.getBackground();
		
		for(int i = 0; i < paths.length; i++){
			//declare the bitmap
	    	Bitmap pic = null;
	    	
	    	ExifInterface exif = null;
			try {
				exif = new ExifInterface(paths[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        	int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        	
	    	
        	//set the width and height we want to use as maximum display
    		int targetWidth = 600;
    		int targetHeight = 400;
    		
    		//create bitmap options to calculate and use sample size
    		BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
    		
    		//first decode image dimensions only - not the image bitmap itself
    		bmpOptions.inJustDecodeBounds = true;
    		
    		pic = BitmapFactory.decodeFile(paths[i], bmpOptions);
    		
    		//image width and height before sampling
    		int currHeight = bmpOptions.outHeight;
    		int currWidth = bmpOptions.outWidth;
    		
    		//variable to store new sample size
    		int sampleSize = 1;
    		
    		//calculate the sample size if the existing size is larger than target size
    		if (currHeight>targetHeight || currWidth>targetWidth) {
    		    //use either width or height
    		    if (currWidth>currHeight)
    		        sampleSize = Math.round((float)currHeight/(float)targetHeight);
    		    else
    		        sampleSize = Math.round((float)currWidth/(float)targetWidth);
    		}
    		
    		//use the new sample size
    		bmpOptions.inSampleSize = sampleSize;
    		
    		//now decode the bitmap using sample options
    		bmpOptions.inJustDecodeBounds = false;
    		
    		//get the file as a bitmap
    		pic = BitmapFactory.decodeFile(paths[i], bmpOptions);
    		pic = this.rotateBitmap(pic, orientation);
    		Drawable bDraw = new BitmapDrawable(getResources(), pic);
    		
    		frameAnimation.addFrame(bDraw, 5000);
    		
		}

		// Start the animation (looped playback by default).
		frameAnimation.start();

		return view;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			frameAnimation.start();
			return true;
		}
		return getActivity().onTouchEvent(event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

	    try{
	        Matrix matrix = new Matrix();
	        switch (orientation) {
	            case ExifInterface.ORIENTATION_NORMAL:
	                return bitmap;
	            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
	                matrix.setScale(-1, 1);
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_180:
	                matrix.setRotate(180);
	                break;
	            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
	                matrix.setRotate(180);
	                matrix.postScale(-1, 1);
	                break;
	            case ExifInterface.ORIENTATION_TRANSPOSE:
	                matrix.setRotate(90);
	                matrix.postScale(-1, 1);
	                break;
	           case ExifInterface.ORIENTATION_ROTATE_90:
	               matrix.setRotate(90);
	               break;
	           case ExifInterface.ORIENTATION_TRANSVERSE:
	               matrix.setRotate(-90);
	               matrix.postScale(-1, 1);
	               break;
	           case ExifInterface.ORIENTATION_ROTATE_270:
	               matrix.setRotate(-90);
	               break;
	           default:
	               return bitmap;
	        }
	        try {
	            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	            bitmap.recycle();
	            return bmRotated;
	        }
	        catch (OutOfMemoryError e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_presentation,
					container, false);
			return rootView;
		}
	}
}
