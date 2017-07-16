package net.ncue.mart.model;

import org.springframework.web.multipart.MultipartFile;

public class SaleFile {
    private String fileName;
    private String fileSize;
    private String fileType; 
    private byte[] bytes;
    
	private MultipartFile attachedfile = null;
	
	public MultipartFile getAttachedfile() {
		return this.attachedfile;
	}
	public void setAttachedfile(MultipartFile attachedfile) { 
		this.attachedfile = attachedfile;
	}
	
	
	public String getFilename() {
		return this.fileName;
	}
	public void setFilename(String fileName) { 
		this.fileName = fileName;
	}
	
	public String getFilesize() {
		return this.fileSize;
	}
	public void setFilesize(String fileSize) { 
		this.fileSize = fileSize;
	}
	
	public String getFiletype() {
		return this.fileType;
	}
	public void setFiletype(String fileType) { 
		this.fileType = fileType;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	public void setBytes(byte[] bytes) { 
		this.bytes = bytes;
	}
}