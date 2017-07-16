package net.ncue.home.util;

import javax.servlet.http.HttpServletRequest;

import net.ncue.spring.Box;

public class Parameter {
	String dir = "";
	int page = 0;
	String query = "";

	public void setAttribute(HttpServletRequest request) {
		request.setAttribute("dir", this.getDir());
		request.setAttribute("page", this.getPage());
		request.setAttribute("query", this.getQuery());
	}
	
	public void parse(HttpServletRequest request) {
		this.setDir(Box.get(request, "dir"));
		this.setPage(Box.get(request, "page"));
		this.setQuery(Box.get(request, "query"));
	}
	
	public void setDir(String dir) {
		if ((dir==null) || (dir.equals(""))) this.dir = "latest_news";
		else this.dir = dir;
	}
	public String getDir() {
		return this.dir;
	}
		
	public void setPage(String page) {
		if ((page==null) || (page.equals(""))) this.page = 1;
		else this.page = Integer.parseInt(page);
	}
	public int getPage() {
		return this.page;
	}
	
	public void setQuery(String query) {
		if ((query==null) || (query.equals(""))) this.query = "";
		else this.query = query;
	}
	public String getQuery() {
		return this.query;
	}
}
