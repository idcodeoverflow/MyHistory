package com.david.myhistory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends Fragment {

	private TextView textView1;
	private ImageView imageView1;
	private final int PICKER = 1;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
         
        view.setBackgroundColor(Color.BLACK);
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        textView1 = (TextView) view.findViewById(R.id.textView1);
		
      //set long click listener for each gallery thumbnail item
        imageView1.setOnLongClickListener(new OnLongClickListener() {
            //handle long clicks
        	@Override
        	public boolean onLongClick(View v) {
                //take user to choose an image
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			    //check if we are returning from picture selection
			    if (requestCode == PICKER) {
			            //import the image
			             
			    }
			}
			//superclass method
			super.onActivityResult(requestCode, resultCode, data);
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
			View rootView = inflater.inflate(R.layout.fragment_welcome,
					container, false);
			return rootView;
		}
	}
}
