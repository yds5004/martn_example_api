package net.ncue.spring;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XMLManager {
	public void makeXML(HttpServletResponse response, String xml) {
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
	public void makeText(HttpServletResponse response, String xml) {
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
