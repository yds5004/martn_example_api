package net.ncue.conf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: DosangYoon
 * @version: 0.1
 * @date    01/01/2010
 * @brief:
 */

public class Configure {
	
	/* SERVER */
	public final static String HOME_URL = "http://localhost";
	public final static String DBHOST = "localhost:8080";
	public final static String POS_TAGGER_URL="";
	public final static String MART_URL = HOME_URL+":8999/solr/mart/select?";
	public final static String SALE_URL = HOME_URL+":8999/solr/sale/select?";
	public final static String PRICE_URL = HOME_URL+":8999/solr/price/select?";
	public final static String ONLINEMALL_URL = HOME_URL+":8999/solr/onlinemall/select?";
	public final static String MART_SALE_FIlE_DIR = "/home/dsyoon/public_html/martn_file/";

	// Database Info
	public final static String DBID = "";
	public final static String DBPASSWORD = "";
	public final static int DBPORT = 3306;

	
	public static void setAttribute(HttpServletRequest request) {
		request.setAttribute("HOME_URL", Configure.HOME_URL);
		
		HttpSession session = request.getSession();
		if (session!=null && session.getAttribute("email")!=null) {
			request.setAttribute("login", "1");
		}
	}
}

