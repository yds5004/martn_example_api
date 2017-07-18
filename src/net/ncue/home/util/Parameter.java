package net.ncue.home.util;

import javax.servlet.http.HttpServletRequest;

import net.ncue.spring.Box;

public class Parameter {
	String query = "";

	public void setAttribute(HttpServletRequest request) {
		request.setAttribute("query", this.getQuery());
	}
	
	public void parse(HttpServletRequest request) {
		this.setQuery(Box.get(request, "query"));
	}

	public void setQuery(String query) {
		if ((query==null) || (query.equals(""))) this.query = "";
		else this.query = query;
	}
	public String getQuery() {
		return this.query;
	}
}
