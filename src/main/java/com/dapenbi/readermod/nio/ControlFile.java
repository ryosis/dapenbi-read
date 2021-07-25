package com.dapenbi.readermod.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ControlFile {
	private String fullPathFilename;
	private FileChannel channel;
	private FileLock lock;
	private FileOutputStream fos;
	private File file;
	
	public ControlFile(String fullPathFilename) {
		// TODO Auto-generated constructor stub
		this.fullPathFilename = fullPathFilename;		
		file = new File(fullPathFilename);
	}
	
	public void setLockStream() throws IOException {
		fos = new FileOutputStream(fullPathFilename);		
        channel = fos.getChannel();
        lock = channel.lock();
//        lock = channel.tryLock();
	}
	
	public File getFile() {
		return file;
	}
	
	public void setLockFile() throws IOException {		
		channel = new RandomAccessFile(file, "rw").getChannel();
		//blocking
//        lock = channel.lock();
		//non blocking
        lock = channel.tryLock();
	}
	
	public boolean isFileLocked() throws IOException {			
        if(lock != null) {
        	return true;
        } else {
        	setUnlockFile();
        	return false;
        }
	}
	
	public FileOutputStream getStreamLock() {
		return fos;
	}
	
	public void setUnlockFile() throws IOException {
		if(fos != null) fos.close();
		if( lock != null ) lock.release();		
        channel.close();
	}
	
}
