package net.ncue.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class OutFormatManager {
	
	public static void makeXML(HttpServletResponse response, String xml) {
		try {
			byte[] bytes = xml.getBytes("UTF-8");
			response.setContentType ("text/xml;charset=UTF-8");
			response.setContentLength (bytes.length);
			OutputStream out = response.getOutputStream();
			out.write( bytes );
			out.flush ();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void makeText(HttpServletResponse response, String xml) {
		try {
			byte[] bytes = xml.getBytes("UTF-8");
			response.setContentType ("text/html;charset=UTF-8");
			response.setContentLength (bytes.length);
			OutputStream out = response.getOutputStream();
			out.write( bytes );
			out.flush ();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
