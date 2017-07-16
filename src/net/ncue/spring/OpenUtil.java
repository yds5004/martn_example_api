package net.ncue.spring;

/**
 * @author: DosangYoon
 * @version: 0.1
 * @date    01/01/2010
 * @brief:
 */
 
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;

import net.ncue.spring.DateFormat;
import net.ncue.spring.Paging;

public class OpenUtil {
	private static final String DEFAULT_DELIMETER = ".";
	private static final long NEW_MARKER = 2000000;
	private static Hashtable<String, String> oneHt = new Hashtable<String, String> ();
	protected static final Log logger = LogFactory.getLog (OpenUtil.class);
	
	private static MessageSourceAccessor msAccessor;

	/**
	 * 
	 * @param msAccessor
	 */
	public void setMessageSourceAccessor (MessageSourceAccessor ms) {
		msAccessor = ms;
	}

	/**
	 * 
	 * @return
	 */
	public MessageSourceAccessor getMsAccessor () {
		return msAccessor;
	}	

	/**
	 * 
	 * @param byteArray
	 * @return
	 */
	public static int toInt (byte byteArray[]) {
		return (((int) byteArray[0] & 0xff) << 24) + (((int) byteArray[1] & 0xff) << 16) + (((int) byteArray[2] & 0xff) << 8)
				+ ((int) byteArray[3] & 0xff);
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] toByte (int i) {
		byte byteArray[] = new byte[4];
		byteArray[0] = (byte) (i >>> 24);
		byteArray[1] = (byte) ((i & 0xff0000) >>> 16);
		byteArray[2] = (byte) ((i & 0xff00) >>> 8);
		byteArray[3] = (byte) (i & 0xff);
		return byteArray;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public static String parseParam (String content) {
		if (content == null)
			return "";

		String result = null;
		result = replaceHtmlTag (content);

		return result;
	}

	public static String replaceHtmlTag (String content) {
		if (content == null)
			return "";

		String tmp = content.replaceAll ("<", "&lt;");
		tmp = tmp.replaceAll (">", "&gt;");
		tmp = tmp.replaceAll ("\"", "&quot;");

		return tmp;
	}

	/**
	 * <PRE>
	 * 
	 * 데이터를 받아서 원하는 포맷으로 바꿔준다. 전제조건은 String 으로 받고, 해당 길이에 따라서 원하는 포맷으로 바꿔준다.
	 * 
	 * </PRE>
	 * 
	 * @param s 변환하고자 하는 문자열
	 * @param delimeter 문자열 변환하고자 할때의 구분자(/...)
	 * @return 변환된 문자열
	 */
	public static String formatDate (String... strings) {
		String value = "";
		String delimeter = null;

		if (strings[0] == null)
			return value;
		String s = strings[0];
		if (strings.length == 1) {
			delimeter = DEFAULT_DELIMETER;
		} else {
			delimeter = strings[1];
		}

		if (s.length () == 6) {
			value = s.substring (0, 2) + delimeter + s.substring (2, 4) + delimeter + s.substring (4);
		} else if (s.length () == 8) {
			value = s.substring (0, 4) + delimeter + s.substring (4, 6) + delimeter + s.substring (6);
		} else if (s.length () == 12) {
			value = s.substring (0, 4) + delimeter + s.substring (4, 6) + delimeter + s.substring (6, 8) + " "
					+ s.substring (8, 10) + ":" + s.substring (10, 12);
		} else if (s.length () >= 14) {
			value = s.substring (0, 4) + delimeter + s.substring (4, 6) + delimeter + s.substring (6, 8) + " "
					+ s.substring (8, 10) + ":" + s.substring (10, 12) + ":" + s.substring (12, 14);
		} else {
			value = s;
		}
		return value;
	}

	/**
	 * <PRE>
	 * 
	 * 원하는 길이만큼 자른 후 원하는 포맷으로 바꿔준다.
	 * 
	 * </PRE>
	 * 
	 * @param s 변환하고자 하는 문자열
	 * @param length 자르고자 하는 index
	 * @return 변환된 문자열
	 */
	public static String formatDate (String s) {
		return formatDate (s, DEFAULT_DELIMETER);
	}

	public static String formatDate (String s, int length) {
		if (s.length () < length)
			return "";
		return formatDate (s.substring (0, length));
	}

	/**
	 * 
	 * @param format
	 * @return
	 */
	public static String getFormatDate (String... strings) {
		String strFormat = null;
		if (strings.length == 0) {
			strFormat = "yyyyMMddHHmmss";
		} else {
			strFormat = strings[0];
		}

		return DateFormat.getFormatString (strFormat);
	}

	public static String isEventOn (String start_date, String end_date) {
		String tmp = "N";

		if (OpenUtil.getMinute (end_date) > 0 && OpenUtil.getMinute (start_date) < 0) {
			tmp = "Y";
		}

		return tmp;
	}

	public static String formatNum (int num) {
		java.text.DecimalFormat df = new java.text.DecimalFormat ("###,###,###");

		String tmp = df.format (num);

		return tmp;
	}

	/**
	 * 
	 * @param toDate
	 * @param format
	 * @return
	 */
	public static int getMinute (String toDate) {
		int minute = 0;
		String strFormat = "yyyyMMddHHmmss";

		try {
			minute = DateFormat.timesBetween (getFormatDate (strFormat), toDate, strFormat);
		} catch (Exception e) {
			System.out.println (e);
		}

		return minute;
	}

	/**
	 * 
	 * @param toDate
	 * @param format
	 * @return
	 */
	public static int daysBetween (String date) {
		int minute = 0;
		String strFormat = "yyyyMMdd";

		try {
			minute = DateFormat.daysBetween (getFormatDate (strFormat), date);
		} catch (Exception e) {
			System.out.println (e);
		}

		return minute;
	}

	public static int daysBetween (String date, int day) {
		if (day < 10) {
			return daysBetween (date + "0" + day);
		} else {
			return daysBetween (date + day);
		}
	}

	/**
	 * 
	 * @param toDate
	 * @param format
	 * @return
	 */
	public static String nowAddDays (int interval) {
		String temp = null;
		String strFormat = "yyyyMMddHHmmss";

		try {
			temp = "-" + getFormatDate (DateFormat.addDays (DateFormat.getFormatString (strFormat), interval, strFormat));
		} catch (Exception e) {
			return temp = "-99999999999999";
		}

		return temp;
	}

	/**
	 * 
	 * @param str
	 * @param before
	 * @param after
	 * @return
	 */
	public static String replace (String str, String before, String after) {

		int s = 0;
		int e = 0;

		StringBuffer dest = new StringBuffer ();

		while ((e = str.indexOf (before, s)) >= 0) {

			dest.append (str.substring (s, e));
			dest.append (after);
			s = e + before.length ();

		}

		dest.append (str.substring (s));
		return dest.toString ();
	}

	/**
	 * 
	 * @param str
	 * @param limit
	 * @return
	 */
	public static String cutString (String str, int limit) {
		return cutString (str, limit, false);
	}

	public static String cutString (String str, int limit, boolean appendix) {
		if (str == null)
			return "";
		else if (str.length () <= limit)
			return str;
		else {
			String tmp = str.substring (0, limit);
			if (appendix)
				tmp += "..";
			return tmp;
		}
	}

	public static String shortCutString (String str, int limit) {
		return shortCutString (str, limit, true);
	}

	public static String shortCutString (String str, int limit, boolean appendix) {
		try {
			if (str == null || limit < 4)
				return str;
			if (str.getBytes ().length < limit)
				return str;

			int len = str.length ();
			int cnt = 0, index = 0;

			while (index < len && cnt < limit) {
				if (str.charAt (index++) < 256) {
					cnt++;
				} else {
					cnt += 2;
				}
			}

			if (index < len && limit >= cnt)
				str = str.substring (0, index);
			else if (index < len && limit < cnt)
				str = str.substring (0, index - 1);

			if (appendix) {
				return str + "..";
			} else {
				return str;
			}
	
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String parseURL (HttpServletRequest request) throws Exception {
		return parseURL (request, true, null, null);
	}

	public static String parseURL (HttpServletRequest request, String except) throws Exception {
		return parseURL (request, true, except, null);
	}

	public static String parseURL (HttpServletRequest request, String except, String append) throws Exception {
		return parseURL (request, true, except, append);
	}

	public static String parseURL (HttpServletRequest request, boolean isEncode) throws Exception {
		return parseURL (request, isEncode, null, null);
	}

	public static String parseURL (HttpServletRequest request, boolean isEncode, String except) throws Exception {
		return parseURL (request, isEncode, except, null);
	}

	public static String parseURL (HttpServletRequest request, boolean isEncode, String except, String append) throws Exception {
		StringBuffer sb = new StringBuffer ();
		int pIdx = 0;

		try {
			String nurl = request.getRequestURI ();
			sb.append (nurl);

			Enumeration e = request.getParameterNames ();
			while (e.hasMoreElements ()) {
				String sKey = (String) e.nextElement ();
				String value = OpenUtil.parseParam (request.getParameterValues (sKey)[0]);

				if (except != null && except.indexOf (sKey) >= 0)
					continue;
				if (isEncode)
					value = encode (value);
				if (pIdx == 0) {
					sb.append ("?");
				} else {
					sb.append ("&");
				}

				sb.append (sKey);
				sb.append ("=");
				sb.append (value);

				pIdx++;
			}

			if (append != null)
				sb.append (append);
		} catch (Exception e) {
		}

		return Paging.makeLink (sb.toString ());
	}

	public static String cookieURL (HttpServletRequest request) throws Exception {
		return cookieURL (request, "");
	}

	public static String cookieURL (HttpServletRequest request, String name) throws Exception {
		StringBuffer sb = new StringBuffer ();
		int pIdx = 0;

		try {
			String nurl = request.getRequestURI ();
			sb.append (nurl);

			Enumeration e = request.getParameterNames ();
			while (e.hasMoreElements ()) {
				String sKey = (String) e.nextElement ();
				String value = request.getParameterValues (sKey)[0];

				// contains 로 지정된 문자열만 조합해 보내준다.
				if (!name.equals ("")) {
					if (!sKey.equals (name))
						continue;
				}

				if (pIdx == 0) {
					sb.append ("?");
				} else {
					sb.append ("&");
				}
				sb.append (sKey);
				sb.append ("=");
				sb.append (value);

				pIdx++;
			}
		} catch (Exception e) {
		}

		return sb.toString ();
	}

	public static String sourceURL (HttpServletRequest request) throws Exception {
		StringBuffer sb = new StringBuffer ();
		int pIdx = 0;

		try {
			sb.append (request.getRequestURL ());

			Enumeration e = request.getParameterNames ();
			while (e.hasMoreElements ()) {
				String sKey = (String) e.nextElement ();
				String value = request.getParameterValues (sKey)[0];

				if (pIdx == 0) {
					sb.append ("?");
				} else {
					sb.append ("&");
				}
				sb.append (sKey);
				sb.append ("=");
				sb.append (value);

				pIdx++;
			}
		} catch (Exception e) {
		}

		return sb.toString ();
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static String getErrorMessage (Exception e) {
		StringBuffer errorMessage = new StringBuffer ();
		errorMessage.append (e.toString () + "\n");

		StackTraceElement[] traces = e.getStackTrace ();
		int length = Math.min (5, traces.length);

		for (int i = 0; i < length; i++) {
			errorMessage.append (traces[i].toString () + "\n");
		}

		return errorMessage.toString ();
	}

	public static String BR2NL (String src) {
		if (src == null || src.equals (""))
			return "";
		else
			return src.replace ("<br />", "");
	}

	/**
	 * Convert new line character(\n) into '<br>'
	 * 
	 * @return the translated string.
	 * @param src String the string to be changed
	 */
	public static String NL2BR (String src) {
		return translatePostfix (src, "<br />");
	}

	/**
	 * Convert new line character(\n) into specified string(prefix).
	 * 
	 * @return the translated string.
	 * @param src String the source string
	 * @param prefix String the destination string
	 */
	public static String translatePrefix (String src, String prefix) {
		String result = "";
		java.util.StringTokenizer st = new java.util.StringTokenizer (src, "\n");

		while (st.hasMoreTokens ())
			result += prefix + st.nextToken ();
		return result;
	}

	/**
	 * Convert new line character(\n) into specified string(postfix).
	 * 
	 * @return the translated string.
	 * @param src String the source string
	 * @param postfix String the destination string
	 */
	public static String translatePostfix (String src, String postfix) {
		String result = "";
		java.util.StringTokenizer st = new java.util.StringTokenizer (src, "\n");

		while (st.hasMoreTokens ())
			result += st.nextToken () + postfix;
		return result;
	}

	/**
	 * 
	 * @param s
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String decode (String s, String enc) throws java.io.UnsupportedEncodingException {
		return java.net.URLDecoder.decode (s, enc);
	}

	public static String decode (String s) throws java.io.UnsupportedEncodingException {
		return decode (s, "UTF-8");
	}

	/**
	 * 
	 * @param s
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String encode (String s, String enc) throws java.io.UnsupportedEncodingException {
		return java.net.URLEncoder.encode (s, enc);
	}

	public static String encode (String s) throws java.io.UnsupportedEncodingException {
		return encode (s, "UTF-8");
	}

	public static String encodeC1 (String s) throws java.io.UnsupportedEncodingException {
		byte[] tmp = s.getBytes ("UTF-8");
		return new String (tmp, "euc-kr");
	}

	/**
	 * 
	 * @param o
	 */
	public static void fixNullAndTrim (Object o) {
		if (o == null)
			return;

		Class c = o.getClass ();
		if (c.isPrimitive ())
			return;

		Field[] fields = c.getFields ();
		for (int i = 0; i < fields.length; i++) {
			try {
				Object f = fields[i].get (o);
				Class fc = fields[i].getType ();
				if (fc.getName ().equals ("java.lang.String")) {
					if (f == null)
						fields[i].set (o, "");
					else {
						String item = OpenUtil.trim ((String) f);
						fields[i].set (o, item);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static String trim (String s) {
		int st = 0;
		char[] val = s.toCharArray ();
		int count = val.length;
		int len = count;

		while ((st < len) && (val[st] <= ' '))
			st++;
		while ((st < len) && (val[len - 1] <= ' '))
			len--;

		return ((st > 0) || (len < count)) ? s.substring (st, len) : s;
	}

	public static String removeTag (String s) {
		final int NORMAL_STATE = 0;
		final int TAG_STATE = 1;
		final int START_TAG_STATE = 2;
		final int END_TAG_STATE = 3;
		final int SINGLE_QUOT_STATE = 4;
		final int DOUBLE_QUOT_STATE = 5;
		int state = NORMAL_STATE;
		int oldState = NORMAL_STATE;
		char[] chars = s.toCharArray ();
		StringBuffer sb = new StringBuffer ();
		char a;
		for (int i = 0; i < chars.length; i++) {
			a = chars[i];
			switch (state) {
			case NORMAL_STATE:
				if (a == '<')
					state = TAG_STATE;
				else
					sb.append (a);
				break;
			case TAG_STATE:
				if (a == '>')
					state = NORMAL_STATE;
				else if (a == '\"') {
					oldState = state;
					state = DOUBLE_QUOT_STATE;
				} else if (a == '\'') {
					oldState = state;
					state = SINGLE_QUOT_STATE;
				} else if (a == '/')
					state = END_TAG_STATE;
				else if (a != ' ' && a != '\t' && a != '\n' && a != '\r' && a != '\f')
					state = START_TAG_STATE;
				break;
			case START_TAG_STATE:
			case END_TAG_STATE:
				if (a == '>')
					state = NORMAL_STATE;
				else if (a == '\"') {
					oldState = state;
					state = DOUBLE_QUOT_STATE;
				} else if (a == '\'') {
					oldState = state;
					state = SINGLE_QUOT_STATE;
				} else if (a == '\"')
					state = DOUBLE_QUOT_STATE;
				else if (a == '\'')
					state = SINGLE_QUOT_STATE;
				break;
			case DOUBLE_QUOT_STATE:
				if (a == '\"')
					state = oldState;
				break;
			case SINGLE_QUOT_STATE:
				if (a == '\'')
					state = oldState;
				break;
			}
		}
		return sb.toString ();
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String stripHtmlTag (String str) {
		String delStr = "P \\{MARGIN-TOP:2px; MARGIN-BOTTOM:2px\\}";
		String ohterDelStr = "P \\{MARGIN-TOP: 2px; MARGIN-BOTTOM: 2px\\}";
		
		str = str.replaceAll (delStr, "");
		str = str.replaceAll (ohterDelStr, "");
		str = removeTag (str);

		return str;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static int toInt (String str) {
		int i = 0;

		try {
			i = Integer.parseInt (str);
		} catch (Exception e) {
			return i;
		}

		return i;
	}

	public static boolean getEditable (String user_id, String poster_id) {
		if (poster_id == null)
			return false;

		if (!user_id.equals ("") && user_id.equals (poster_id)) {
			return true;
		}

		return false;
	}

	public static String getSortdate_String (long sortdate) {
		String sort_date = String.valueOf (sortdate);
		return sort_date.substring (5, 7) + "." + sort_date.substring (7, 9) + " " + sort_date.substring (9, 11) + ":"
				+ sort_date.substring (11, 13);
	}

	public static Long getSort_date () {
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
		return Long.parseLong ("-" + sdf.format (new Date ()));
	}

	public static Long getSort_day () {
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
		return Long.parseLong ("-" + sdf.format (new Date ()));
	}

	public static boolean isNew (long sortdate) {
		boolean b_check = true;
		Long now_date = getSort_date ();
		if (sortdate - now_date > NEW_MARKER)
			b_check = false;
		return b_check;
	}

	/**
	 * 전화번호 서식 수정
	 * 
	 * @param tel_no
	 * @return
	 */
	public static String getEditTelNo (String tel_no) {
		if (tel_no == null || tel_no.equals (""))
			return "";
		/*
		 * 숫자로만 이루어진 전화번호를 구별하기 쉽도록 '-'을 넣어준다.
		 */
		String editTelNo = "";
		try {
			int len = tel_no.length ();
			int startIdx = 0;

			if ("02".equals (tel_no.substring (0, 2))) {
				startIdx = 2;
			} else {
				startIdx = 3;
			}
			editTelNo = tel_no.substring (0, startIdx) + "-" + tel_no.substring (startIdx, len - 4) + "-"
					+ tel_no.substring (len - 4);
		} catch (Exception e) {
			return "";
		}

		return editTelNo;
	}

	public static String disEmail (String email) {
		String tmp = null;

		if (email == null)
			return "";
		int pos = email.indexOf ("@");
		if (pos <= 0)
			return email;

		tmp = email.substring (0, pos + 1) + "***";
		return tmp;
	}

	public static boolean isNumeric (String strValue) {
		boolean b_check = true;
		if (strValue == null)
			return false;
		if (strValue.startsWith ("-") || strValue.startsWith ("+")) {
			strValue = strValue.substring (1);
		}
		for (int i = 0; i < strValue.length (); i++) {
			if (!Character.isDigit (strValue.charAt (i))) {
				if (strValue.charAt (i) != ',' && strValue.charAt (i) != '.') {
					b_check = false;
					break;
				}
			}
		}
		return b_check;
	}

	/*
	 * 지도코드 식별
	 */
	public static boolean isGidoCode (String code) {
		boolean tmp = false;

		if (code.startsWith ("42") && code.length () == 9) {
			if (isNumeric (code)) {
				tmp = true;
			} else {
				tmp = false;
			}
		} else {
			tmp = false;
		}

		return tmp;
	}

	public static int getAddedDay (int addDay) {
		Calendar targetTime = Calendar.getInstance ();
		targetTime.add (Calendar.DAY_OF_YEAR, addDay);
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
		int day = 0;
		try {
			day = 0 - Integer.parseInt (sdf.format (targetTime.getTime ()));
		} catch (Exception e) {
		}
		return day;
	}

	/* 알파벳으로 시작하는지 체크한다 */
	public static boolean checkC2User (String tmp) {
		if (tmp == null || tmp.equals (""))
			return false;

		String REGEX = "[a-z]";
		tmp = tmp.substring (0, 1).toLowerCase ();

		return Pattern.matches (REGEX, tmp);
	}

	public static String min (String x1, String x2) {
		return compare (x1, x2) < 0 ? x1 : x2;
	}

	public static String max (String x1, String x2) {
		return compare (x1, x2) > 0 ? x1 : x2;
	}

	private static int compare (String x1, String x2) {
		double dx1 = 0.0;
		double dx2 = 0.0;
		try {
			dx1 = Double.parseDouble (x1);
			dx2 = Double.parseDouble (x2);
		} catch (Exception e) {
		}
		return Double.compare (dx1, dx2);
	}

	/**
	 * 파라미터의 값이 null 혹은 공백(트림한 값)인가를 체크
	 * 
	 * @param value 체크할 문자열
	 * @return null, 공백의 경우 true
	 */
	public static boolean isEmpty (String value) {
		if (value == null || value.trim ().equals ("")) {
			return true;
		}
		return false;
	}

	/**
	 * 실수 d의 소수점 num자리까지 자른후 리턴한다.
	 * 
	 * @param d
	 * @param num
	 * @return
	 */
	public static String cutMaximumFractionDigits (double d, int num) {
		NumberFormat nf = NumberFormat.getInstance ();
		nf.setMaximumFractionDigits (num);
		return nf.format (d);
	}

	/**
	 * 실수 d를 퍼센트(%붙음) 형태로 반환한다.
	 * 
	 * @param d
	 * @return
	 */
	public static String getPercentValue (double d) {
		NumberFormat nf = NumberFormat.getPercentInstance ();
		return nf.format (d);
	}

	/**
	 * 지도 첨부됐는지 여부 확인
	 * 
	 * @param s
	 * @return
	 */
	public static String checkMap (String s) {
		if (s == null)
			return "";
		
		try {
			int idx1 = 0;
			if ((idx1 = s.indexOf ("id=map_id")) >= 0 || (idx1 = s.indexOf ("id=\"map_id\"")) >= 0 ) {
				String tmp = s.substring (s.indexOf ("name=", idx1) + 5).replace("\"", "");
				String tmpStr, result = "";

				for (int i = 0; i < tmp.length (); i++) {
					tmpStr = tmp.substring (i, i + 1);
					if (Character.isDigit (tmp.charAt (i))) {
						result += tmpStr;
					} else {
						break;
					}
				}

				return result;
			}
		} catch (Exception e) {
			return "";			
		}
		
		return "";
	}

	public static String parseAddr (String s) {
		String result = null;
		String[] val = s.split (" ");
		if (val.length == 0)
			return "";
		else
			result = val[0];

		return result;
	}
	
	/**
	 * 계정 통합후 아이디 체크 함수
	 * @param req
	 * @param user_id
	 * @return
	 */	
	public static String getIDInfo (HttpServletRequest req, String user_id) {
		String url="http://211.234.241.25/ICache/icache.dll?tid=" + user_id;

		HttpClient httpclient = new HttpClient();
		GetMethod method = new GetMethod(url);
		int statusCode = 0;
		String value = "";
		
		try {
			//method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			//method.setRequestHeader("Cookie", "cookieinfouser=" + PSession.getSession(req, "cookieinfouser"));
			method.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=EUC-KR");
			method.setFollowRedirects(false);
			
			try {
				statusCode = httpclient.executeMethod(method);

				if (statusCode == HttpStatus.SC_OK) {
					value = method.getResponseBodyAsString().trim();
				}					
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} finally {
			method.releaseConnection();
		}
		return value;
	}
	
	public static String getFileType (String fileUrl) {
		if (fileUrl == null) return "NONE";
		if (fileUrl.startsWith("http://c2down") && !fileUrl.toLowerCase().endsWith("swf")) {
			return "C2";
		} else {
			if (fileUrl.indexOf ('.') > 0) {
				return fileUrl.substring (fileUrl.lastIndexOf ('.') + 1);
			} else {
				return "NONE";
			}
		}
	}
	
	public static Hashtable getRecRandom (List list, int limit) {
		Random random = new Random ();
		Hashtable<Integer, Object> ht = new Hashtable<Integer, Object> ();
		
		while (true) {
			if (ht.size() == limit) break;
			
			int num = random.nextInt (list.size());
			if (ht.containsKey(num)) {
				continue;
			} else {
				ht.put(num, list.get (num));	
			}
		}
		
		return ht;
	}
	
	public static void notifyDBCache (String user_id) {
		logger.debug ("notify DBCache: "+user_id);

		int serverNumber = 0;
		try{
			serverNumber = (int) (Long.parseLong(user_id) % 8);
		}
		catch(Exception e){
			if (logger.isErrorEnabled()){
				logger.error(user_id+"is not number.", e);
			}
		}
		
		HttpClient httpclient = new HttpClient ();
		String httpUrl = msAccessor.getMessage ("sk.map.cy.dbcache."+serverNumber);
		PostMethod method = new PostMethod (httpUrl);

		try {
			//method.getParams ().setCookiePolicy (CookiePolicy.IGNORE_COOKIES);
			//method.setRequestHeader ("Cookie", "cookieinfouser=" + PSession.getSession (request, "cookieinfouser"));
			//method.addRequestHeader ("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");

			/* 본문 */
			method.setParameter ("request", "DEL!\t@nateon!\t@"+user_id);

			method.setFollowRedirects (false);

			int statusCode = httpclient.executeMethod (method);

			if (logger.isDebugEnabled()){
				logger.debug("statusCode of notification DBCache: "+statusCode);
			}
		}
		catch(Exception e){
			if (logger.isErrorEnabled()){
				logger.error("fail to notify DBCache.", e);
			}
		}
		finally {
			method.releaseConnection ();
		}
	}
	
	public static String getStationName(String station){
		if (station == null || station.equals("")) return station;
		else if (station.equals("서울") || station.equals("대전") 
	           || station.equals("대구") || station.equals("동대구") 
		       || station.equals("녹동간이") || station.equals("부산") )
        	return station+"역";
        else if (!station.equals("서울역") && !station.equals("대전역") 
        	   && !station.equals("대구역") && !station.equals("동대구역")
        	   && !station.equals("녹동간이역") && !station.equals("부산역")
        	   && station.endsWith("역"))
        	return station.substring(0, station.length()-1);
	    else
	        return station;		
	}
}