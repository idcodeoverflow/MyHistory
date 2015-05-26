package com.david.filecontrol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.Context;

public class GalleryFileControl {
	
	private String filename = "welcomeFile";
	private FileOutputStream outputStream;
	private Scanner scanner;
	private File file;
	private List<String> readStrings;
	private String writeString;
	private Context context;
	
	public GalleryFileControl(Context c){
		
		try {
			readStrings = new ArrayList<String>();
			context = c;
			file = new File(c.getFilesDir(), filename);
			if(file.exists()){
				scanner = new Scanner(new FileReader(file));
				outputStream = c.openFileOutput(filename, Context.MODE_PRIVATE);
			} else {
				file.createNewFile();
				scanner = new Scanner(new FileReader(file));
				outputStream = c.openFileOutput(filename, Context.MODE_PRIVATE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeFile(){
		
		try{
			outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream.write(writeString.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> readFile(){
		
		try{
			while(scanner.hasNext()){
				readStrings.add(scanner.nextLine());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return readStrings;
	}
	
	public void openStreams(){
		
		try{
			if(file.exists()){
				scanner = new Scanner(new FileReader(file));
				outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeStreams(){
		
		try{
			if(file.exists()){
				scanner.close();
				outputStream.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getReadString() {
		return readStrings;
	}

	public void setReadString(List<String> readStrings) {
		this.readStrings = readStrings;
	}

	public String getWriteString() {
		return writeString;
	}

	public void setWriteString(String writeString) {
		this.writeString = writeString;
	}
	
}
