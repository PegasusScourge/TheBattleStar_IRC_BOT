import org.jibble.pircbot.*;
import java.util.ArrayList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileDescriptor;

public class LogBot{
	
	private File logFile;
	private File oldFile;
	
	private FileOutputStream os;
	private PrintStream logStream;
	
	private PrintStream oldSysOut;
	
	private boolean prefixMessages = false;
	
	LogBot(String prefix){
		oldFile = new File(prefix + ".bak");
		logFile = new File(prefix + ".log");
		
		System.out.println("New log file: " + logFile.getAbsolutePath());
		System.out.println("Bak log file: " + oldFile.getAbsolutePath());
		
		//Rename the old log file to the oldFile
		if(!logFile.renameTo(oldFile)){
			//Rename was complete
			System.out.println("Failed to rename the old log file!");
		}
		
		//Open the log file for writing and write the lead-in
		try{
			logFile.createNewFile();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(openLogStreams()){
			logStream.println("TheBattleStar: Pirc IRC bot");
			oldSysOut.println("TheBattleStar: Test message");
		}else{
			System.err.println("Failed to open output streams!");
		}
		
		flushLogStreams();
		
		System.setOut(logStream);
	}
	
	LogBot(String prefix, boolean shouldPrefixMsgs){
		this(prefix);
		this.prefixMessages = shouldPrefixMsgs;
	}
	
	private boolean openLogStreams(){
		try{
			os = new FileOutputStream(logFile);
			logStream = new PrintStream(os, false); //New ps, no autoflush
			oldSysOut = new PrintStream(new FileOutputStream(FileDescriptor.out));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void flushLogStreams(){
		try{
			if(logStream != null){
				logStream.flush();
			}
			if(oldSysOut != null){
				oldSysOut.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String getMsgPrefix(){
		return "[TheBattleStar]";
	}
	
	public final void log(String txt){
		String msg;
		if(prefixMessages){
			msg = getMsgPrefix() + txt;
		}else{
			msg = txt;
		}
		
		logStream.println(msg);
		oldSysOut.println(msg);
		
		flushLogStreams();
	}
	
	public final void log(String txt, String prefix){
		String msg;
		if(prefixMessages){
			msg = prefix + txt;
		}else{
			msg = txt;
		}
		
		logStream.println(msg);
		oldSysOut.println(msg);
		
		flushLogStreams();
	}
	
	public final void dispose(){
		flushLogStreams();
		oldSysOut.close();
		logStream.close();
	}
}