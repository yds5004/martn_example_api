package net.ncue.spring;

public final class DateFormat {

	/**
	 * Don't let anyone instantiate this class
	 */
	private DateFormat () {
	}

	/**
	 * check date string validation with the default format "yyyyMMdd".
	 * 
	 * @param s date string you want to check with default format "yyyyMMdd".
	 * @return date java.util.Date
	 */
	public static java.util.Date check (String s) throws java.text.ParseException {
		return check (s, "yyyyMMdd");
	}

	/**
	 * check date string validation with an user defined format.
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return date java.util.Date
	 */
	public static java.util.Date check (String s, String format) throws java.text.ParseException {
		if (s == null)
			throw new java.text.ParseException ("date string to check is null", 0);
		if (format == null)
			throw new java.text.ParseException ("format string to check date is null", 0);

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = null;
		try {
			date = formatter.parse (s);
		} catch (java.text.ParseException e) {
			/*
			 * throw new java.text.ParseException( e.getMessage() + " with
			 * format \"" + format + "\"", e.getErrorOffset() );
			 */
			throw new java.text.ParseException (" wrong date:\"" + s + "\" with format \"" + format + "\"", 0);
		}

		if (!formatter.format (date).equals (s))
			throw new java.text.ParseException ("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
		return date;
	}

	public static boolean isValid (String s) throws Exception {
		return DateFormat.isValid (s, "yyyyMMdd");
	}

	public static boolean isValid (String s, String format) {
		/*
		 * if ( s == null ) throw new NullPointerException("date string to check
		 * is null"); if ( format == null ) throw new
		 * NullPointerException("format string to check date is null");
		 */
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = null;
		try {
			date = formatter.parse (s);
		} catch (java.text.ParseException e) {
			return false;
		}

		if (!formatter.format (date).equals (s))
			return false;

		return true;
	}

	/**
	 * @return formatted string representation of current day with "yyyy-MM-dd".
	 */
	public static String getDateString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}

	/**
	 * 
	 * For example, String time = DateFormat.getFormatString("yyyy-MM-dd
	 * HH:mm:ss");
	 * 
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static int getDay () {
		return getNumberByPattern ("dd");
	}

	/**
	 * 
	 * For example, String time = DateFormat.getFormatString("yyyy-MM-dd
	 * HH:mm:ss");
	 * 
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static int getYear () {
		return getNumberByPattern ("yyyy");
	}

	/**
	 * 
	 * For example, String time = DateFormat.getFormatString("yyyy-MM-dd
	 * HH:mm:ss");
	 * 
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static int getMonth () {
		return getNumberByPattern ("MM");
	}

	/**
	 * 
	 * For example, String time = DateFormat.getFormatString("yyyy-MM-dd
	 * HH:mm:ss");
	 * 
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static int getNumberByPattern (String pattern) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (pattern, java.util.Locale.KOREA);
		String dateString = formatter.format (new java.util.Date ());
		return Integer.parseInt (dateString);
	}

	/**
	 * 
	 * For example, String time = DateFormat.getFormatString("yyyy-MM-dd
	 * HH:mm:ss");
	 * 
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static String getFormatString (String pattern) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (pattern, java.util.Locale.KOREA);
		String dateString = formatter.format (new java.util.Date ());
		return dateString;
	}

	/**
	 * @return formatted string representation of current day with "yyyyMMdd".
	 */
	public static String getShortDateString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}

	/**
	 * @return formatted string representation of current time with "HHmmss".
	 */
	public static String getShortTimeString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("HHmmss", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}

	/**
	 * @return formatted string representation of current time with
	 *         "yyyy-MM-dd-HH:mm:ss".
	 */
	public static String getTimeStampString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss:SSS", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}

	/**
	 * @return formatted string representation of current time with "HH:mm:ss".
	 */
	public static String getTimeString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("HH:mm:ss", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}

	/**
	 * return days between two date strings with default defined
	 * format.(yyyyMMdd)
	 * 
	 * @param s date string you want to check.
	 */
	public static String nameDate (String sdate) throws java.text.ParseException {
		String s = null;
		int i = whichDay (sdate);

		if (i == java.util.Calendar.MONDAY)
			s = "占쏙옙";
		if (i == java.util.Calendar.TUESDAY)
			s = "화";
		if (i == java.util.Calendar.WEDNESDAY)
			s = "占쏙옙";
		if (i == java.util.Calendar.THURSDAY)
			s = "占쏙옙";
		if (i == java.util.Calendar.FRIDAY)
			s = "占쏙옙";
		if (i == java.util.Calendar.SATURDAY)
			s = "占쏙옙";
		if (i == java.util.Calendar.SUNDAY)
			s = "占쏙옙";

		return s;
	}

	public static int whichDay (String s) throws java.text.ParseException {
		return whichDay (s, "yyyyMMdd");
	}

	/**
	 * return days between two date strings with user defined format.
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 */
	public static int whichDay (String s, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = check (s, format);

		java.util.Calendar calendar = formatter.getCalendar ();
		calendar.setTime (date);
		return calendar.get (java.util.Calendar.DAY_OF_WEEK);
	}

	// modified by WDH 2001.02.23 START
	/**
	 * return days between two date strings with default defined
	 * format.(yyyyMMdd)
	 * 
	 * @param s date string you want to check.
	 * @return int
	 */
	public static int weeksOfMonth (String s) throws java.text.ParseException {
		return weeksOfMonth (s, "yyyyMMdd");
	}

	/**
	 * return days between two date strings with default defined
	 * format.("yyyyMMdd")
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int java.text.ParseException 
	 */
	public static int weeksOfMonth (String s, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);

		java.util.Date date = check (lastDayOfMonth (s), format);

		java.util.Calendar calendar = formatter.getCalendar ();
		calendar.setTime (date);
		return calendar.get (java.util.Calendar.WEEK_OF_MONTH);
	}

	public static int numOfWeek (String s) throws java.text.ParseException {
		return numOfWeek (s, "yyyyMMdd");
	}

	public static int numOfWeek (String s, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);

		java.util.Date date = check (s, format);

		java.util.Calendar calendar = formatter.getCalendar ();
		calendar.setTime (date);
		return calendar.get (java.util.Calendar.WEEK_OF_MONTH);
	}

	/**
	 * return days between two date strings with default defined
	 * format.("yyyyMMdd")
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @param week
	 * @return int
	 */
	public static String sundayOfWeek (String s) throws java.text.ParseException {
		return sundayOfWeek (s, "yyyyMMdd");
	}

	/**
	 * return days between two date strings with default defined
	 * format.("yyyyMMdd")
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @param week
	 */
	public static String sundayOfWeek (String s, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);

		java.util.Date date = check (s, format);

		String temp = formatter.format (date);

		if (DateFormat.whichDay (temp) != 0) {
			date.setTime (date.getTime () - (long) (DateFormat.whichDay (temp) - 1) * 1000 * 60 * 60 * 24);
		}
		return formatter.format (date);
	}

	// modified by WDH 2001.02.23 END

	/**
	 * return days between two date strings with default defined
	 * format.("yyyyMMdd")
	 * 
	 * @param String from date string
	 * @param String to date string
	 */
	public static int daysBetween (String from, String to) throws java.text.ParseException {
		return daysBetween (from, to, "yyyyMMdd");
	}

	/**
	 * return days between two date strings with user defined format.
	 * 
	 * @param String from date string
	 * @param String to date string
	 */
	public static int daysBetween (String from, String to, String format) throws java.text.ParseException {
		java.util.Date d1 = check (from, format);
		java.util.Date d2 = check (to, format);

		long duration = d2.getTime () - d1.getTime ();

		return (int) (duration / (1000 * 60 * 60 * 24));
		// seconds in 1 day
	}

	/**
	 * return times between two date strings with user defined format.
	 * 
	 * @param String from date string
	 * @param String to date string
	 */
	public static int timesBetween (String from, String to, String format) throws java.text.ParseException {
		java.util.Date d1 = check (from, format);
		java.util.Date d2 = check (to, format);

		long duration = d2.getTime () - d1.getTime ();

		return (int) (duration / (1000 * 60));
	}

	/**
	 * return age between two date strings with default defined
	 * format.("yyyyMMdd")
	 * 
	 * @param String from date string
	 * @param String to date string
	 */
	public static int ageBetween (String from, String to) throws java.text.ParseException {
		return ageBetween (from, to, "yyyyMMdd");
	}

	/**
	 * return age between two date strings with user defined format.
	 * 
	 * @param String from date string
	 * @param String to date string
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 */
	public static int ageBetween (String from, String to, String format) throws java.text.ParseException {
		return (int) (daysBetween (from, to, format) / 365);
	}

	/**
	 * return add day to date strings
	 * 
	 * @param String date string
	 */
	public static String addDays (String s, int day) throws java.text.ParseException {
		return addDays (s, day, "yyyyMMdd");
	}

	/**
	 * return add day to date strings with user defined format.
	 * 
	 * @param String date string
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 */
	public static String addDays (String s, int day, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = check (s, format);

		date.setTime (date.getTime () + ((long) day * 1000 * 60 * 60 * 24));
		return formatter.format (date);
	}

	/**
	 * return add month to date strings
	 * 
	 * @param String date string
	 */
	public static String addMonths (String s, int month) throws Exception {
		return addMonths (s, month, "yyyyMMdd");
	}

	/**
	 * return add month to date strings with user defined format.
	 * 
	 * @param String date string
	 * @param format string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 */
	public static String addMonths (String s, int addMonth, String format) throws Exception {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = check (s, format);

		java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat ("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat ("MM", java.util.Locale.KOREA);
		java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat ("dd", java.util.Locale.KOREA);
		int year = Integer.parseInt (yearFormat.format (date));
		int month = Integer.parseInt (monthFormat.format (date));
		int day = Integer.parseInt (dayFormat.format (date));

		month += addMonth;
		if (addMonth > 0) {
			while (month > 12) {
				month -= 12;
				year += 1;
			}
		} else {
			while (month <= 0) {
				month += 12;
				year -= 1;
			}
		}
		java.text.DecimalFormat fourDf = new java.text.DecimalFormat ("0000");
		java.text.DecimalFormat twoDf = new java.text.DecimalFormat ("00");
		String tempDate = String.valueOf (fourDf.format (year)) + String.valueOf (twoDf.format (month))
				+ String.valueOf (twoDf.format (day));
		java.util.Date targetDate = null;

		try {
			targetDate = check (tempDate, "yyyyMMdd");
		} catch (java.text.ParseException pe) {
			day = lastDay (year, month);
			tempDate = String.valueOf (fourDf.format (year)) + String.valueOf (twoDf.format (month))
					+ String.valueOf (twoDf.format (day));
			targetDate = check (tempDate, "yyyyMMdd");
		}

		return formatter.format (targetDate);
	}

	public static String addYears (String s, int year) throws java.text.ParseException {
		return addYears (s, year, "yyyyMMdd");
	}

	public static String addYears (String s, int year, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = check (s, format);
		date.setTime (date.getTime () + ((long) year * 1000 * 60 * 60 * 24 * (365 + 1)));
		return formatter.format (date);
	}

	public static int monthsBetween (String from, String to) throws java.text.ParseException {
		return monthsBetween (from, to, "yyyyMMdd");
	}

	public static int monthsBetween (String from, String to, String format) throws java.text.ParseException {
		java.util.Date fromDate = check (from, format);
		java.util.Date toDate = check (to, format);

		// if two date are same, return 0.
		if (fromDate.compareTo (toDate) == 0)
			return 0;

		java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat ("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat ("MM", java.util.Locale.KOREA);
		java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat ("dd", java.util.Locale.KOREA);

		int fromYear = Integer.parseInt (yearFormat.format (fromDate));
		int toYear = Integer.parseInt (yearFormat.format (toDate));
		int fromMonth = Integer.parseInt (monthFormat.format (fromDate));
		int toMonth = Integer.parseInt (monthFormat.format (toDate));
		int fromDay = Integer.parseInt (dayFormat.format (fromDate));
		int toDay = Integer.parseInt (dayFormat.format (toDate));

		int result = 0;
		result += ((toYear - fromYear) * 12);
		result += (toMonth - fromMonth);

		if (((toDay - fromDay) > 0))
			result += toDate.compareTo (fromDate);

		return result;
	}

	public static String lastDayOfMonth (String src) throws java.text.ParseException {
		return lastDayOfMonth (src, "yyyyMMdd");
	}

	public static String lastDayOfMonth (String src, String format) throws java.text.ParseException {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = check (src, format);

		java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat ("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat ("MM", java.util.Locale.KOREA);

		int year = Integer.parseInt (yearFormat.format (date));
		int month = Integer.parseInt (monthFormat.format (date));
		int day = lastDay (year, month);

		java.text.DecimalFormat fourDf = new java.text.DecimalFormat ("0000");
		java.text.DecimalFormat twoDf = new java.text.DecimalFormat ("00");
		String tempDate = String.valueOf (fourDf.format (year)) + String.valueOf (twoDf.format (month))
				+ String.valueOf (twoDf.format (day));
		date = check (tempDate, format);

		return formatter.format (date);
	}

	private static int lastDay (int year, int month) throws java.text.ParseException {
		int day = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			if ((year % 4) == 0) {
				if ((year % 100) == 0 && (year % 400) != 0) {
					day = 28;
				} else {
					day = 29;
				}
			} else {
				day = 28;
			}
			break;
		default:
			day = 30;
		}
		return day;
	}
}
