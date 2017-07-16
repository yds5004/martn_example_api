package net.ncue.spring;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class StaticFileViewController implements Controller {

	protected final Logger logger = Logger.getLogger (getClass ());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#
	 *      handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 * 
	 */
	public ModelAndView handleRequest (HttpServletRequest request, HttpServletResponse response) throws Exception {

		String contextPath = request.getContextPath ();

		String uri = null;
		String INCLUDE_URI = "javax.servlet.include.request_uri";
		if ((request.getAttribute (INCLUDE_URI)) == null) {
			uri = request.getRequestURI ();
		} else {
			uri = (String) request.getAttribute (INCLUDE_URI);
		}

		if (logger.isDebugEnabled ()) {
			logger.debug ("ContextPath : " + contextPath);
			logger.debug ("URI : " + uri);
		}

		int begin = 0;
		if ((contextPath == null) || (contextPath.equals (""))) {
			begin = 1;
		} else {
			begin = contextPath.length () + 1;
		}

		if (logger.isDebugEnabled ()) {
			logger.debug ("Begin : " + begin);
		}

		int end;
		if (uri.indexOf (";") != -1) {
			end = uri.indexOf (";");
		} else if (uri.indexOf ("?") != -1) {
			end = uri.indexOf ("?");
		} else {
			end = uri.length ();
		}

		String filename = uri.substring (begin, end);

		if (filename.indexOf (".") != -1) {
			filename = filename.substring (0, filename.lastIndexOf ("."));
		}

		for (Enumeration en = request.getParameterNames (); en.hasMoreElements ();) {
			String attribute = (String) en.nextElement ();
			Object attributeValue = request.getParameter (attribute);

			if (logger.isDebugEnabled ()) {
				logger.debug ("set Attribute in Request : " + attribute + "=" + attributeValue);
			}

			request.setAttribute (attribute, attributeValue);
		}

		if (logger.isDebugEnabled ()) {
			logger.debug ("filename : " + filename);
		}
		return new ModelAndView (filename);
	}
}
