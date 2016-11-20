package com.mail.file;

import java.io.File;

public class FileObject {

	//源文件名字
	private String sourceName;
	//对应的文件
	private File file;
	
	public FileObject(String sourceName, File file) {
		this.sourceName = sourceName;
		this.file = file;
	}

	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return this.sourceName;
	}
	
	
}
