package net.ncue.mart.util;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Encode{
	public final static String UTF8 = "UTF-8";
	public final static String EUCKR = "EUC-KR";
	public final static String MS949 = "MS949";
	public final static String KSC5601 = "KSC5601";	
	
	public static String LocalString(String val)
	{
		if (val == null)
			return null;
		else {
			byte[] b;
			try {
				b = val.getBytes("8859_1");
				CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
				try {
					CharBuffer r = decoder.decode(ByteBuffer.wrap(b));
					return r.toString();
				} catch (CharacterCodingException e) {
					return new String(b, "EUC-KR");
				}
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		} return null;
	}
	 
	public static String iconv(String str, String from, String to) {
        if(str == null) return null;
        String result  =  null;
        byte[] rawBytes  =  null;
        try {
            rawBytes  =  str.getBytes(from);
            result  =  new String(rawBytes , to);
        } catch(java.io.UnsupportedEncodingException e ) {
        }
        return result;
    }
	public static String toH(String str) {
		return toHAN(str);
	}
	public static String toHAN(String str) {
        if(str == null) return null;
        String result  =  null;
        byte[] rawBytes  =  null;
        try {
            rawBytes  =  str.getBytes("8859_1");
            result  =  new String(rawBytes , "KSC5601");
        } catch(java.io.UnsupportedEncodingException e ) {
        }
        return result;
    }
	public static String toUTF8(String str) {
        if(str == null) return null;
        String result  =  null;
        byte[] rawBytes  =  null;
        try {
            rawBytes  =  str.getBytes("8859_1");
            result  =  new String(rawBytes , "UTF8");
        } catch(java.io.UnsupportedEncodingException e ) {
        }
        return result;

    }

    public static String toA(String str) {
		return toASCII(str);
	}
    public static String toASCII(String str) {
        if(str == null) return null;

        String result = null;
        byte[] rawBytes = null;
        try {
            rawBytes = str.getBytes("KSC5601");
            result = new String(rawBytes , "8859_1");
        } catch(java.io.UnsupportedEncodingException e ) {
        }
        return result;
    }

	//Encoder
	public static String toE(String str){
		return toEncode(str);
	}
	public static String toE(String str, String enc){
		return toEncode(str, enc);
	}
	public static String toEncode(String str){
		return toEncode(str, null);
	}
	public static String toEncode(String str, String enc){
		String result = null;
		String encoding = "EUC-KR";
		if(enc!=null) encoding = enc;
		try{
			result = URLEncoder.encode(str, encoding);
		}catch(Exception e){
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	//Decoder
	public static String toD(String str){
		return toDecode(str);
	}
	public static String toD(String str, String dec){
        return toDecode(str, dec);
    }
	public static String toDecode(String str){
		return toDecode(str, null);
	}
	public static String toDecode(String str, String dec){
		String result = null;
		String decoding = "EUC-KR";
		if(dec!=null) decoding = dec;
		try{
			result = URLDecoder.decode(str, decoding);
		}catch(Exception e){
			e.printStackTrace();
			result = null;
        }
		return result; 
	}


	public static String getZeroStr(String str){
		if(str==null || str.equals(" "))
			str = "";
		return toH(str);
	}
	public static String getZeroStr(String str,String str2){
		if(str==null || str.equals(" "))
			str = str2;
		return toH(str);
	}
	public static String ntoe(String str){
		if(str==null || str.equals(" "))
			str = "";
		return str;
	}

	public static String ntoe(String str,String str2){
		if(str==null || str.equals(" "))
			str = str2;
		return str;
	}

	public static String[] getZeroStrValues(String[] str){
		if(str==null)
			return new String[0];
		return str;
	}
	
	public static String getNTag(String str){
		if(str!=null)
		{
			str = str.replaceAll("&","&amp;");
			str = str.replaceAll("\"","&quot;");
			str = str.replaceAll("<","&lt;");
			str = str.replaceAll(">","&gt;");
		}
		return str;
	}
    public static String getChecked(String op1, String op2) {
        String str = new String("");
        String st1 = new String("");
        String st2 = new String("");

        if ((op1.trim().length() <= 0) || (op2.trim().length() <= 0))
        {
            str = "";
            return str;
        }
        st1 = op1.trim();
        st2 = op2.trim();

        if (st1.equals(st2) )
            str = str+ " checked ";
        return str;
    }


    public static String getChecked_checkbox(String strop1, String strop2) {
        int intval;
        String str = new String("");
        String st1 = new String("");
        String st2 = new String("");
        if ((strop1.trim().length() <= 0) || (strop2.trim().length() <= 0))
        {
            str = "";
            return str;
        }
        st1 = strop1.trim();
        st2 = strop2.trim();
        intval = st1.indexOf(st2);
        if (intval >= 0)
            str = " checked ";
        else
            str = "";
        return str;
    }
    public static String getChecked_select(String strop1, String strop2) {
        int intval;
        String str = new String("");
        String st1 = new String("");
        String st2 = new String("");
        if ((strop1.trim().length() <= 0) || (strop2.trim().length() <= 0))
        {
            str = "";
            return str;
        }
        st1 = strop1.trim();
        st2 = strop2.trim();
        intval = st1.indexOf(st2);
        if (intval >= 0)
            str = " selected ";
        else
            str = "";
        return str;
    }
    public static int getIntNumber(String str) throws StringIndexOutOfBoundsException  {

        int i, iddd;
        int  intln = 0;
        char ch;
        String qq = new String("");

        if ((str == "") || (str == null)) return ( 0) ;
        if (str.substring(0,1).equals("-"))
        {
            i = 1;
            iddd = -1;
        }
        else
        {
            i = 0;
            iddd = 1;
        }
        try
        {
            while(i < str.length())
            {
                ch = str.charAt(i);
                if ((!Character.isDigit(ch)) && (ch != ','))
                {
                    intln = 0;
                    return intln;
                }
                if (ch != ',') qq = qq + ch;
                i++ ;
            }

            if (!qq.equals("") || qq != null)
                intln = Integer.parseInt(qq) * iddd;
            else
                intln = 0;
            return intln;
        }
        catch(Exception e)
        {
            intln = 0;
            return intln;
        }
    }

    public static double getDoubleNumber(String str) throws StringIndexOutOfBoundsException  {

        int i;
        double iddd;
        double intln = 0;
        char ch;
        String qq = new String("");

        if ((str == "") || (str == null)) return ( 0) ;
        if (str.substring(0,1).equals("-"))
        {
            i = 1;
            iddd = -1;
        }
        else
        {
            i = 0;
            iddd = 1;
        }
        try
        {
            while(i < str.length())
            {
                ch = str.charAt(i);
                if ((!Character.isDigit(ch)) && (ch != ',') && (ch != '.'))
                {
                    intln = 0;
                    return intln;
                }
                if (ch != ',') qq = qq + ch;
                i++ ;
            }

            if (!qq.equals("") || qq != null)
                intln = Double.parseDouble(qq) * iddd;
            else
                intln = 0;
            return intln;
        }
        catch(Exception e)
        {
            intln = 0;
            return intln;
        }
    }

    public  static String getReplace(String strln, String from_str, String to_str) throws java.lang.ArrayIndexOutOfBoundsException  {

        int i;
        String str = new String("");
        String str1 = new String("");

        if ((strln == null) || (strln.equals("")))
        {
             str1 = "";
             return str1;
        }
        i = 0;
        try {
            while( i < strln.length())
            {
                str = strln.substring(i,i+1);
                if (!str.equals(from_str) )
                {
                    str1 += str;
                }
                else
                {
                    str1 += to_str;
                }
                i++ ;
            };
            return str1;
        }
        catch(Exception e)
        {
            str1 = "";
            return str1;
        }
    }

    public static boolean checkSPC(String str){
	char ch;
	boolean check = true;
	int i = 0;
        try
        {
		for(int ii=0;ii<str.length();ii++){
                        ch = str.charAt(ii);
			if(!checkAlpha(ch)){
				if(!checkDigit(ch)){
					if(!checkHangul(ch)){
						check=false;
					}
				}
			}
                }
	}catch(Exception e){
		check = false;
	}
	return check;
    }

    public static boolean checkDIGIT(String str){
        char ch;
        boolean check = true;
        int i = 0;
        try
        {
            for(int ii=0;ii<str.length();ii++){
                ch = str.charAt(ii);
                if(!checkDigit(ch)){
                        check=false;
                }
            }
        }catch(Exception e){
                check = false;
        }
        return check;
    }

    public static boolean checkSENG(String str){
        char ch;
        boolean check = true;
        int i = 0;
        try
        {
            for(int ii=0;ii<str.length();ii++){
                ch = str.charAt(ii);
                if(!checkSmallAlpha(ch)){
                        check=false;
                }
            }
        }catch(Exception e){
                check = false;
        }
        return check;
    }


    public static boolean checkSmallAlpha(char ch){
		boolean check = true;
		if ( ch >= 'a' && ch <= 'z' ){
			check = true;
		}else{
			check = false;
		}
		return check;
    }

    public static boolean checkAlpha(char ch){
		boolean check = true;
		if ( ( ch >= 'A' && ch <= 'Z' ) || ( ch >= 'a' && ch <= 'z' ) ){
			check = true;
		}else{
			check = false;
		}
		return check;
    }
    public static boolean checkDigit(char ch){
		boolean check = true;
		if ( ch >= '0' && ch <= '9' ){
			check = true;
		}else{
			check = false;
		}
		return check;
    }
    public static boolean checkHangul(char ch){
		boolean check = true;
		if ( ch >= '\uAC00' && ch <= '\uD7A3' ){
			check = true;
		}else{
			check = false;
		}
		return check;
	}
}
