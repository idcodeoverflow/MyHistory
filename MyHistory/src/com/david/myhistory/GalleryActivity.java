package com.david.myhistory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.david.filecontrol.GalleryFileControl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class GalleryActivity extends Fragment {

	final Activity activity = this.getActivity();
	//variable for selection intent
	private final int PICKER = 1;
	private int currentPic = 0;
	
	private ImageAdapter imgAdapt;
	private Gallery gallery;
	
	//the images to display
	private Integer[] imageIds = {
			R.drawable.mihistoria/*,
			R.drawable.keira2,
			R.drawable.keira3,
			R.drawable.keira4,
			R.drawable.keira5*/
	};

	private List<Object> imageIDs = new ArrayList<Object>();
	
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		
		
		for(int i = 0; i < imageIds.length; i++){
			imageIDs.add(imageIds[i]);
		}
		
		GalleryFileControl galleryFile = new GalleryFileControl(getActivity());
		
		String paths[] = galleryFile.readFile();


		final View view = inflater.inflate(R.layout.fragment_gallery, container, false);

		view.setBackgroundColor(Color.BLACK);

		// Note that Gallery view is deprecated in Android 4.1---
		gallery = (Gallery) view.findViewById(R.id.gallery1);
		gallery.setAdapter(new ImageAdapter(getActivity()));
		for(int i = 0; i < paths.length; i++){
			//declare the bitmap
			ExifInterface exif = null;
			try {
				exif = new ExifInterface(paths[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        	int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        	
	    	Bitmap pic = null;
	    	
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
    		
    		imgAdapt = new ImageAdapter(getActivity());
    		//pass bitmap to ImageAdapter to add to array
    		imgAdapt.addPic(pic);
    		//redraw the gallery thumbnails to reflect the new addition
    		gallery.setAdapter(imgAdapt);
    		
		}
		
		if(imageIDs.size() > 0){
			int currentSelection = 0;

			ImageView imageView = (ImageView) view.findViewById(R.id.image1);
			try{
				imageView.setImageResource((Integer)imageIDs.get(currentSelection));
			} catch(Exception ex) {
				imageView.setImageBitmap((Bitmap)imageIDs.get(currentSelection));
			}
		}
		
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				// display the images selected
				ImageView imageView = (ImageView) view.findViewById(R.id.image1);
				try{
					imageView.setImageResource((Integer)imageIDs.get(position));
				} catch(Exception ex) {
					imageView.setImageBitmap((Bitmap)imageIDs.get(position));
				}
			}
		});

		
		//set long click listener for each gallery thumbnail item
        gallery.setOnItemLongClickListener(new OnItemLongClickListener() {
            //handle long clicks
        	public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
        		//update the currently selected position so that we assign the imported bitmap to correct item
        		currentPic = position;
        		 
        		//take the user to their chosen image selection app (gallery or file manager)
        		Intent pickIntent = new Intent();
        		pickIntent.setType("image/*");
        		pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        		//we will handle the returned data in onActivityResult
        		startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);
        		 
        		return true;
            }
        	
        	public boolean onTouchEvent(MotionEvent event) {
	        	// TODO Auto-generated method stub
	        	return getActivity().onTouchEvent(event);
        	}

        });
		
		return view;
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		
		if (resultCode == getActivity().RESULT_OK) {
			    //check if we are returning from picture selection
			    if (requestCode == PICKER) {
			            //import the image
			    	//the returned picture URI
			    	Uri pickedUri = data.getData();
			    	//declare the bitmap
			    	Bitmap pic = null;
			    	 
			    	//declare the path string
			    	String imgPath = "";
			    	//retrieve the string using media data
			    	String[] medData = { MediaStore.Images.Media.DATA };
			    	
			    	//query the data
			    	Cursor picCursor = getActivity().managedQuery(pickedUri, medData, null, null, null);
			    	if(picCursor!=null) {
			    	    //get the path string
			    	    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			    	    picCursor.moveToFirst();
			    	    imgPath = picCursor.getString(index);
			    	} else {
			    	    imgPath = pickedUri.getPath();
			    	}

			    	//if we have a new URI attempt to decode the image bitmap
			    	if(pickedUri!=null) {
			    		
			    		ExifInterface exif = null;
						try {
							exif = new ExifInterface(imgPath);
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
			    		
			    		pic = BitmapFactory.decodeFile(imgPath, bmpOptions);
			    		
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
			    		pic = BitmapFactory.decodeFile(imgPath, bmpOptions);
			    		
			    		pic = this.rotateBitmap(pic, orientation);
			    		
			    		imgAdapt = new ImageAdapter(getActivity());
			    		//pass bitmap to ImageAdapter to add to array
			    		imgAdapt.addPic(pic);
			    		//redraw the gallery thumbnails to reflect the new addition
			    		gallery.setAdapter(imgAdapt);
			    		
			    		
			    		try{
				    		String FILENAME = "galleryfile";
	
				    		FileOutputStream fos = getActivity().openFileOutput(FILENAME, getActivity().MODE_APPEND);
				    		imgPath += ';';
				    		fos.write(imgPath.getBytes());
				    		fos.close();
			    		} catch (Exception ex) {
			    			ex.printStackTrace();
			    		}
			    	}
			             
			    }
			}
			//superclass method
			super.onActivityResult(requestCode, resultCode, data);
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		public ImageAdapter(Context c)
		{
			context = c;
			// sets a grey background; wraps around the images
			TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
			itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
			a.recycle();
		}
		// returns the number of images
		public int getCount() {
			return imageIDs.size();
		}
		// returns the ID of an item
		public Object getItem(int position) {
			return position;
		}
		// returns the ID of an item
		public long getItemId(int position) {
			return position;
		}
		//helper method to add a bitmap to the gallery when the user chooses one
		public void addPic(Bitmap newPic)
		{
		    //set at currently selected index
		    imageIDs.add(newPic);
		}
		// returns an ImageView view
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			try{
				imageView.setImageResource((Integer)imageIDs.get(position));
			} catch(Exception ex) {
				imageView.setImageBitmap((Bitmap)imageIDs.get(position));
			}
			imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
			imageView.setBackgroundResource(itemBackground);
			return imageView;
		}
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_gallery,
					container, false);
			return rootView;
		}
	}
}
