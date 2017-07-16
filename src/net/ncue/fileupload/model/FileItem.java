package net.ncue.fileupload.model;

import org.springframework.web.multipart.MultipartFile;

public class FileItem {
	private MultipartFile picFile;

	public MultipartFile getPicFile() {
		return picFile;
	}

	public void setPicFile(MultipartFile picFile) { 
		this.picFile = picFile;
	}
}