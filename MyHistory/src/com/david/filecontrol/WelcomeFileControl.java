package com.david.filecontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import android.content.Context;
import android.widget.Toast;

public class WelcomeFileControl {

	private static final int READ_BLOCK_SIZE = 100;
	private String filename = "welcomeFile.txt";
	private FileOutputStream fileout;
	private OutputStreamWriter outputStream;
	private FileInputStream fileIn;
	private InputStreamReader InputRead;
	private File file;
	private String readString;
	private String writeString;
	private Context context;

	public WelcomeFileControl(Context c){

		try {
			readString = "";
			context = c;
			file = new File(c.getFilesDir(), filename);
			outputStream = new OutputStreamWriter(fileout);
			if(file.exists()){
				fileIn = context.openFileInput(filename);
				InputRead = new InputStreamReader(fileIn);
				fileout = context.openFileOutput(filename, context.MODE_PRIVATE);
			} else {
				file.createNewFile();
				fileIn = context.openFileInput(filename);
				InputRead = new InputStreamReader(fileIn);
				fileout = context.openFileOutput(filename, context.MODE_PRIVATE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeFile(){

		try{
			outputStream.write(writeString);
			outputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String readFile(){
		
		String s = "";
		
		//reading text from file
		try {
			
			char[] inputBuffer = new char[READ_BLOCK_SIZE];
			int charRead;

			while ((charRead = InputRead.read(inputBuffer)) > 0) {
				// char to string conversion
				String readstring = String.copyValueOf(inputBuffer,0,charRead);
				s += readstring; 
			}
			
			Toast.makeText(context, s,Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
		}

		readString = s;
		return s;
	}

	public void openStreams(){

		try{
			if(file.exists()){
				fileIn = context.openFileInput(filename);
				InputRead = new InputStreamReader(fileIn);
				fileout = context.openFileOutput(filename, context.MODE_PRIVATE);
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void closeStreams(){

		try{
			if(file.exists()){
				InputRead.close();
				outputStream.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getReadString() {
		return readString;
	}

	public void setReadString(String readString) {
		this.readString = readString;
	}

	public String getWriteString() {
		return writeString;
	}

	public void setWriteString(String writeString) {
		this.writeString = writeString;
	}

}
