package net.ncue.spring;

import java.beans.PropertyEditorSupport;

import net.ncue.spring.OpenUtil;

public class NCuePropertyEditor extends PropertyEditorSupport {
	public void setAsText (String val) throws IllegalArgumentException {
		String strXss = OpenUtil.parseParam (val);
		setValue (strXss);
	}
}
