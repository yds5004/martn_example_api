package net.ncue.util;

public class WebStr {
	public final static String converToWebStr(String str) {
		return str.replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;");
	}
	public final static String converToDate(String str) {
		if (str.length()==4) return str;
		else if ((str.length()==6)) return str.substring(0, 4)+"."+str.substring(4, 6);
		else if ((str.length()==8)) return str.substring(0, 4)+"."+str.substring(4, 6)+"."+str.substring(6, 8);
		return "";
	}
}
