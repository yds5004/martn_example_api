package net.ncue.util;

import java.security.MessageDigest;
import java.security.GeneralSecurityException;

public class PasswordManager {
	public static String encode(String inpara) {
		byte[] bpara = new byte[inpara.length()];
		byte[] rethash;
		int i;
		for (i=0; i < inpara.length(); i++)
			bpara[i] = (byte)(inpara.charAt(i) & 0xff );
		try {
			MessageDigest sha1er = MessageDigest.getInstance("SHA1");
			rethash = sha1er.digest(bpara);
			rethash = sha1er.digest(rethash);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		StringBuffer r = new StringBuffer(41);
		r.append("*");
		for (i=0; i < rethash.length; i++) {
			String x = Integer.toHexString(rethash[i] & 0xff).toUpperCase();
			if (x.length()<2)
				r.append("0");
			r.append(x);
		}
		return r.toString();
	}
}