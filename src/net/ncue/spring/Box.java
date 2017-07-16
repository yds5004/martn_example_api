package net.ncue.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import net.ncue.spring.OpenUtil;

public class Box extends ServletRequestUtils {

	/**
	 * 
	 * @param request
	 * @param strings
	 * @return
	 */
	public static String get (HttpServletRequest request, String... strings) {
		String value = null;
		
		try {
			if (strings.length == 1) {
				//request.setCharacterEncoding("UTF-8");
				value = request.getParameter(strings[0]);
			} else {
				//request.setCharacterEncoding("UTF-8");
				value = OpenUtil.parseParam (getStringParameter (request, strings[0], strings[1]));
			}
		} catch (Exception e) {
			return "";
		}

		return value;
	}

	/**
	 * @param request
	 * @param string
	 * @return
	 */
	public static String[] getValues (HttpServletRequest request, String string) {
		String[] value = null;
		try {
			request.setCharacterEncoding("UTF-8");
			value = getStringParameters (request, string);
		} catch (Exception e) {
			return new String[] { "" };
		}

		return value;
	}

	/**
	 * 
	 * @param request
	 * @param strings
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public static String getString (HttpServletRequest request, String... strings) throws ServletRequestBindingException {
		return get (request, strings);
	}

	/**
	 * 
	 * @param request
	 * @param strings
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public static int getInt (HttpServletRequest request, String... strings) throws ServletRequestBindingException {
		try {
			String val = removeComma (get (request, strings));

			if (val == null || val.equals ("")) {
				return 0;
			} else {
				return Integer.parseInt (val);
			}
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private static String removeComma (String s) throws Exception {
		if (s.indexOf (",") != -1) {
			StringBuffer buf = new StringBuffer ();
			for (int i = 0; i < s.length (); i++) {
				char c = s.charAt (i);
				if (c != ',')
					buf.append (c);
			}
			return buf.toString ();
		}
		return s;
	}
}
