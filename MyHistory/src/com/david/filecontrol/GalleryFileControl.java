package com.david.filecontrol;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

public class GalleryFileControl {

	Context context;
	String FILENAME;
	
	public GalleryFileControl(Context c){
		context = c;
		FILENAME = "galleryfile";
	}
	
	
	public void writeFile(String string){
		string += ';';
		try{
    		FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_APPEND);
    		fos.write(string.getBytes());
    		fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String[] readFile(){
		String imagePath = "";
        try{
        	
    		FileInputStream fis = context.openFileInput(FILENAME);
    		int readInt = fis.read();
    		
    		while(readInt != -1){
    			imagePath += (char) readInt;
    			readInt = fis.read();
    		}
    		
    		fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return imagePath.split(";");
	}
	
}
