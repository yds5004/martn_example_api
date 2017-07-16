package net.ncue.fileupload.control;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ncue.fileupload.model.FileItem;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class FileUploadController extends SimpleFormController {

	 @Override
	 protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		 FileItem fileItem = (FileItem) command;
		 MultipartFile picFile = fileItem.getPicFile();
		 String picFileName = picFile.getOriginalFilename();

		 System.out.println("fileName:"+picFileName);

		 String savePath = "c:/down/tmp/";
		 picFile.transferTo(new File(savePath+picFileName));
		 
		 ModelAndView modelAndView = new ModelAndView();
		 modelAndView.setViewName(getSuccessView());
		 modelAndView.addAllObjects(errors.getModel());
		 return modelAndView;
	 }
}