package net.ncue.home.control;

/**
 * @author: DosangYoon
 * @version: 0.1
 * @date    01/01/2010
 * @brief:
 */

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.web.servlet.ModelAndView;

import net.ncue.conf.Configure;
import net.ncue.home.util.Parameter;
import net.ncue.spring.NCueController;

/** Simple command-line based search demo. */
public class HomeController extends NCueController {
	Parameter parameter = new Parameter();

    // http://localhost:8080/home.ncue?cmd=info&query=test_input
	// http://localhost:8080/home.ncue?cmd=hello&query=test_input
	public ModelAndView info (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Configure.setAttribute(request);
		parameter.parse(request);
		parameter.setAttribute(request);

		request.setAttribute("greeting", "welcome");
		return new ModelAndView("home/background");
	}

	public ModelAndView hello (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Configure.setAttribute(request);
		parameter.parse(request);
		parameter.setAttribute(request);

		request.setAttribute("greeting", "hello");
		return new ModelAndView("home/background");
	}
}
