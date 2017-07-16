package net.ncue.spring;

import javax.servlet.http.HttpServletRequest;

public class Parameter {
	String query;
	int count;
	String type;
	
	String result;
	String exp;
	
	public void setAttribute(HttpServletRequest request) {
		request.setAttribute("query", this.getQuery());
		request.setAttribute("count", this.count);
		request.setAttribute("type", this.type);	
	}
	
	public void parse(HttpServletRequest request) {
		this.setQuery(Box.get(request, "qry"));
		this.setCount(Box.get(request, "cnt"));
		this.setType(Box.get(request, "type"));
	}
	
	public void setQuery(String query) {
		if ((query==null) || (query.equals(""))) this.query = "";
		else this.query = query;
	}
	public String getQuery() {
		return this.query;
	}
	
	public void setCount(String count) {
		if ((count==null) || (count.equals(""))) this.count = -1;
		else this.count = Integer.parseInt(count);
	}
	public int getCount() {
		return this.count;
	}
	
	public void setType(String type) {
		if ((type==null) || (type.equals(""))) this.type = "";
		else this.type = type;
	}
	public String getType() {
		return this.type;
	}
	
	public void setExp(String exp) {
		this.exp = exp;
	}
	public void addExp(String exp) {
		this.exp += exp;
	}
	public String getExp() {
		return this.exp;
	}
}
