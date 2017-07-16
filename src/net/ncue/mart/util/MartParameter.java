package net.ncue.mart.util;

import javax.servlet.http.HttpServletRequest;

import net.ncue.spring.Box;

public class MartParameter {
	String service = "";
	String q = "*:*";
	String lat = "";
	String lng = "";
	String minLat = "";
	String minLng = "";
	String maxLat = "";
	String maxLng = "";
	
	String d = "1";
	String sort = "geodist() asc";
	String start = "0";
	String row = "0";
	String facet = "";
	String facetQuery = "";
	
	String mid = "";
	String pid = "";
	String product = "";
	String price = "";
	String oprice = "";
	String sdate = "";
	String edate = "";
	String desc = "";
	String selectcount = "0";
	String count = "";
	String morecount = "";
	String conditionalprice = "";
	String originalprice = "";
	String sday = "";
	String eday = "";
	

	String idx1 = "";
	String email = "";
	String no = "";
	String display = "";
	String password = "";
	String newPassword = "";
	String keepLogin = "";
	String name = "";
	String addr1 = "";
	String addr2 = "";
	String addr3 = "";
	String addr4 = "";
	String addr5 = "";
	String x = "";
	String y = "";
	
	String data1="";
	String data2="";
	String data3="";
	String gcm="";
	String ua="";
	String la="";
	String manager="";
	String todaysale="";
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (!this.service.trim().equals("")) sb.append("service: ").append(this.service).append("\n");
		if (!this.q.trim().equals("")) sb.append("q: ").append(this.q).append("\n");
		if (!this.lat.trim().equals("")) sb.append("lat: ").append(this.lat).append("\n");
		if (!this.lng.trim().equals("")) sb.append("lng: ").append(this.lng).append("\n");
		if (!this.minLat.trim().equals("")) sb.append("minLat: ").append(this.minLat).append("\n");
		if (!this.minLng.trim().equals("")) sb.append("minLng: ").append(this.minLng).append("\n");
		if (!this.maxLat.trim().equals("")) sb.append("maxLat: ").append(this.maxLat).append("\n");
		if (!this.maxLng.trim().equals("")) sb.append("maxLng: ").append(this.maxLng).append("\n");
		if (!this.d.trim().equals("")) sb.append("d: ").append(this.d).append("\n");
		if (!this.sort.trim().equals("")) sb.append("sort: ").append(this.sort).append("\n");
		if (!this.start.trim().equals("")) sb.append("start: ").append(this.start).append("\n");
		if (!this.row.trim().equals("")) sb.append("row: ").append(this.row).append("\n");
		if (!this.facet.trim().equals("")) sb.append("facet: ").append(this.facet).append("\n");
		if (!this.facetQuery.trim().equals("")) sb.append("facetQuery: ").append(this.facetQuery).append("\n");
		
		if (!this.mid.trim().equals("")) sb.append("mid: ").append(this.mid).append("\n");
		if (!this.pid.trim().equals("")) sb.append("pid: ").append(this.pid).append("\n");		
		if (!this.product.trim().equals("")) sb.append("product: ").append(this.product).append("\n");
		if (!this.price.trim().equals("")) sb.append("price: ").append(this.price).append("\n");
		if (!this.oprice.trim().equals("")) sb.append("oprice: ").append(this.oprice).append("\n");
		if (!this.sdate.trim().equals("")) sb.append("sdate: ").append(this.sdate).append("\n");
		if (!this.edate.trim().equals("")) sb.append("edate: ").append(this.edate).append("\n");
		if (!this.desc.trim().equals("")) sb.append("desc: ").append(this.desc).append("\n");
		if (!this.selectcount.trim().equals("")) sb.append("selectcount: ").append(this.selectcount).append("\n");
		if (!this.count.trim().equals("")) sb.append("count: ").append(this.count).append("\n");
		if (!this.morecount.trim().equals("")) sb.append("morecount: ").append(this.morecount).append("\n");
		if (!this.conditionalprice.trim().equals("")) sb.append("conditionalprice: ").append(this.conditionalprice).append("\n");
		if (!this.originalprice.trim().equals("")) sb.append("originalprice: ").append(this.originalprice).append("\n");
		
		
		
		if (!this.idx1.trim().equals("")) sb.append("idx1: ").append(this.idx1).append("\n");
		if (!this.email.trim().equals("")) sb.append("email: ").append(this.email).append("\n");
		if (!this.no.trim().equals("")) sb.append("no: ").append(this.no).append("\n");
		if (!this.display.trim().equals("")) sb.append("display: ").append(this.display).append("\n");
		if (!this.password.trim().equals("")) sb.append("password: ").append(this.password).append("\n");
		if (!this.newPassword.trim().equals("")) sb.append("newPassword: ").append(this.newPassword).append("\n");
		if (!this.keepLogin.trim().equals("")) sb.append("keepLogin: ").append(this.keepLogin).append("\n");
		if (!this.name.trim().equals("")) sb.append("name: ").append(this.name).append("\n");
		if (!this.addr1.trim().equals("")) sb.append("addr1: ").append(this.addr1).append("\n");
		if (!this.addr2.trim().equals("")) sb.append("addr2: ").append(this.addr2).append("\n");
		if (!this.addr3.trim().equals("")) sb.append("addr3: ").append(this.addr3).append("\n");
		if (!this.addr4.trim().equals("")) sb.append("addr4: ").append(this.addr4).append("\n");
		if (!this.addr5.trim().equals("")) sb.append("addr5: ").append(this.addr5).append("\n");
		if (!this.x.trim().equals("")) sb.append("x: ").append(this.x).append("\n");
		if (!this.y.trim().equals("")) sb.append("y: ").append(this.y).append("\n");
		
		if (!this.data1.trim().equals("")) sb.append("data1: ").append(this.data1).append("\n");
		if (!this.data2.trim().equals("")) sb.append("data2: ").append(this.data2).append("\n");
		if (!this.data3.trim().equals("")) sb.append("data3: ").append(this.data3).append("\n");
		if (!this.gcm.trim().equals("")) sb.append("gcm: ").append(this.gcm).append("\n");
		if (!this.ua.trim().equals("")) sb.append("ua: ").append(this.ua).append("\n");
		if (!this.la.trim().equals("")) sb.append("la: ").append(this.la).append("\n");
		if (!this.manager.trim().equals("")) sb.append("manager: ").append(this.manager).append("\n");
		if (!this.todaysale.trim().equals("")) sb.append("todaysale: ").append(this.todaysale).append("\n");
		
		return sb.toString();
	}
	
	public void setAttribute(HttpServletRequest request) {
		request.setAttribute("service", this.getService());
		request.setAttribute("q", this.getQ());
		request.setAttribute("lat", this.getLat());
		request.setAttribute("lng", this.getLng());
		request.setAttribute("minLat", this.getMinLat());
		request.setAttribute("minLng", this.getMinLng());
		request.setAttribute("maxLat", this.getMaxLat());
		request.setAttribute("maxLng", this.getMaxLng());
		request.setAttribute("d", this.getD());
		request.setAttribute("sort", this.getSort());
		request.setAttribute("start", this.getStart());
		request.setAttribute("row", this.getRow());
		request.setAttribute("facet", this.getFacet());
		request.setAttribute("facetQuery", this.getFacetQuery());
		
		request.setAttribute("mid", this.getMid());
		request.setAttribute("pid", this.getPid());
		request.setAttribute("product", this.getProduct());
		request.setAttribute("price", this.getPrice());
		request.setAttribute("oprice", this.getOPrice());
		request.setAttribute("sdate", this.getSDate());
		request.setAttribute("edate", this.getEDate());
		request.setAttribute("desc", this.getDesc());
		request.setAttribute("selectcount", this.getSelectCount());
		request.setAttribute("count", this.getCount());
		request.setAttribute("morecount", this.getMoreCount());
		request.setAttribute("conditionalprice", this.getConditionalPrice());
		request.setAttribute("originalprice", this.getOriginalPrice());
		
		
		request.setAttribute("idx1", this.getIdx1());
		request.setAttribute("email", this.getEmail());
		request.setAttribute("no", this.getNo());
		request.setAttribute("display", this.getDisplay());
		request.setAttribute("name", this.getName());
		request.setAttribute("password", this.getPassword());
		request.setAttribute("newPassword", this.getNewPassword());
		request.setAttribute("addr1", this.getAddr1());
		request.setAttribute("addr2", this.getAddr2());
		request.setAttribute("addr3", this.getAddr3());
		request.setAttribute("addr4", this.getAddr4());
		request.setAttribute("addr5", this.getAddr5());
		request.setAttribute("x", this.getX());
		request.setAttribute("y", this.getY());
		
		request.setAttribute("data1", this.getData1());
		request.setAttribute("data2", this.getData2());
		request.setAttribute("data3", this.getData3());
		request.setAttribute("gcm", this.getGCM());
		request.setAttribute("ua", this.getUserAgreement());
		request.setAttribute("la", this.getLocationAgreement());
		request.setAttribute("manager", this.getManager());
		request.setAttribute("todaysale", this.getTodaySale());
	}
	
	public void parse(HttpServletRequest request) {
		this.setService(Box.get(request, "service"));
		this.setQ(Box.get(request, "q"));
		this.setLat(Box.get(request, "lat"));
		this.setLng(Box.get(request, "lng"));
		this.setMinLat(Box.get(request, "minLat"));
		this.setMinLng(Box.get(request, "minLng"));
		this.setMaxLat(Box.get(request, "maxLat"));
		this.setMaxLng(Box.get(request, "maxLng"));
		this.setD(Box.get(request, "d"));
		this.setSort(Box.get(request, "sort"));
		this.setStart(Box.get(request, "start"));
		this.setRow(Box.get(request, "row"));
		this.setFacet(Box.get(request, "facet"));
		this.setFacetQuery(Box.get(request, "facetQuery"));
		
		this.setMid(Box.get(request, "mid"));
		this.setPid(Box.get(request, "pid"));		
		this.setProduct(Box.get(request, "product"));
		this.setPrice(Box.get(request, "price"));
		this.setOPrice(Box.get(request, "oprice"));
		this.setSDate(Box.get(request, "sdate"));
		this.setEDate(Box.get(request, "edate"));
		this.setDesc(Box.get(request, "desc"));
		this.setSelectCount(Box.get(request, "selectcount"));
		this.setCount(Box.get(request, "count"));
		this.setMoreCount(Box.get(request, "morecount"));
		this.setConditionalPrice(Box.get(request, "conditionalprice"));
		this.setOriginalPrice(Box.get(request, "originalprice"));
				
		
		this.setIdx1(Box.get(request, "idx1"));
		this.setEmail(Box.get(request, "email"));
		this.setNo(Box.get(request, "no"));
		this.setDisplay(Box.get(request, "display"));
		this.setPassword(Box.get(request, "password"));
		this.setNewPassword(Box.get(request, "newPassword"));
		this.setKeepLogin(Box.get(request, "keepLogin"));
		this.setName(Box.get(request, "name"));
		this.setAddr1(Box.get(request, "addr1"));
		this.setAddr2(Box.get(request, "addr2"));
		this.setAddr3(Box.get(request, "addr3"));
		this.setAddr4(Box.get(request, "addr4"));
		this.setAddr5(Box.get(request, "addr5"));
		this.setX(Box.get(request, "x"));
		this.setY(Box.get(request, "y"));
		
		this.setData1(Box.get(request, "data1"));
		this.setData2(Box.get(request, "data2"));
		this.setData3(Box.get(request, "data3"));
		this.setGCM(Box.get(request, "gcm"));
		this.setUserAgreement(Box.get(request, "ua"));
		this.setLocationAgreement(Box.get(request, "la"));
		this.setManager(Box.get(request, "manager"));
		this.setTodaySale(Box.get(request, "todaysale"));
	}
	
	public void setService(String service) {
		if ((service==null) || (service.equals(""))) this.service = "";
		else this.service = service;
	}
	public void setQ(String q) {
		if ((q==null) || (q.equals(""))) this.q = "*:*";
		else this.q = q;
	}
	public void setLat(String lat) {
		if ((lat==null) || (lat.equals(""))) this.lat = "";
		else this.lat = lat;
	}
	public void setLng(String lng) {
		if ((lng==null) || (lng.equals(""))) this.lng = "";
		else this.lng = lng;
	}
	public void setMinLat(String minLat) {
		if ((minLat==null) || (minLat.equals(""))) this.minLat = "";
		else this.minLat = minLat;
	}
	public void setMinLng(String minLng) {
		if ((minLng==null) || (minLng.equals(""))) this.minLng = "";
		else this.minLng = minLng;
	}
	public void setMaxLat(String maxLat) {
		if ((maxLat==null) || (maxLat.equals(""))) this.maxLat = "";
		else this.maxLat = maxLat;
	}
	public void setMaxLng(String maxLng) {
		if ((maxLng==null) || (maxLng.equals(""))) this.maxLng = "";
		else this.maxLng = maxLng;
	}
	public void setD(String d) {
		if ((d==null) || (d.equals(""))) this.d = "";
		else this.d = d;
	}
	public void setSort(String sort) {
		if ((sort==null) || (sort.equals(""))) this.sort = "";
		else this.sort = sort;
	}
	public void setStart(String start) {
		if ((start==null) || (start.equals(""))) this.start = "0";
		else this.start = start;
	}	
	public void setRow(String row) {
		if ((row==null) || (row.equals(""))) this.row = "10";
		else this.row = row;
	}
	public void setFacet(String facet) {
		if ((facet==null) || (facet.equals(""))) this.facet = "addr1";
		else this.facet = facet;
	}
	public void setFacetQuery(String facetQuery) {
		if ((facetQuery==null) || (facetQuery.equals(""))) this.facetQuery = "";
		else this.facetQuery = facetQuery;
	}
	
	public void setMid(String mid) {
		if ((mid==null) || (mid.equals(""))) this.mid = "";
		else this.mid = mid;
	}
	public void setPid(String pid) {
		if ((pid==null) || (pid.equals(""))) this.pid = "";
		else this.pid = pid;
	}
	
	
	
	public void setIdx1(String idx1) {
		if ((idx1==null) || (idx1.equals(""))) this.idx1 = "";
		else this.idx1 = idx1;
	}
	public void setEmail(String email) {
		if ((email==null) || (email.equals(""))) this.email = "";
		else this.email = email;
	}
	public void setNo(String no) {
		if ((no==null) || (no.equals(""))) this.no = "";
		else this.no = no;
	}
	public void setDisplay(String display) {
		if ((display==null) || (display.equals(""))) this.display = "";
		else this.display = display;
	}
	public void setPassword(String password) {
		if ((password==null) || (password.equals(""))) this.password = "";
		else this.password = password;
	}
	public void setNewPassword(String newPassword) {
		if ((newPassword==null) || (newPassword.equals(""))) this.newPassword = "";
		else this.newPassword = newPassword;
	}
	public void setKeepLogin(String keepLogin) {
		if ((keepLogin==null) || (keepLogin.equals(""))) this.keepLogin = "";
		else this.keepLogin = keepLogin;
	}
	public void setName(String name) {
		if ((name==null) || (name.equals(""))) this.name = "";
		else this.name = name;
	}
	public void setAddr1(String addr1) {
		if ((addr1==null) || (addr1.equals(""))) this.addr1 = "";
		else this.addr1 = addr1;
	}
	public void setAddr2(String addr2) {
		if ((addr2==null) || (addr2.equals(""))) this.addr2 = "";
		else this.addr2 = addr2;
	}
	public void setAddr3(String addr3) {
		if ((addr3==null) || (addr3.equals(""))) this.addr3 = "";
		else this.addr3 = addr3;
	}
	public void setAddr4(String addr4) {
		if ((addr4==null) || (addr4.equals(""))) this.addr4 = "";
		else this.addr4 = addr4;
	}
	public void setAddr5(String addr5) {
		if ((addr5==null) || (addr5.equals(""))) this.addr5 = "";
		else this.addr5 = addr5;
	}
	public void setX(String x) {
		if ((x==null) || (x.equals(""))) this.x = "";
		else this.x = x;
	}
	public void setY(String y) {
		if ((y==null) || (y.equals(""))) this.y = "";
		else this.y = y;
	}
	
	public void setData1(String data1) {
		if ((data1==null) || (data1.equals(""))) this.data1 = "";
		else this.data1 = data1;
	}
	public void setData2(String data2) {
		if ((data2==null) || (data2.equals(""))) this.data2 = "";
		else this.data2 = data2;
	}
	public void setData3(String data3) {
		if ((data3==null) || (data3.equals(""))) this.data3 = "";
		else this.data3 = data3;
	}
	public void setGCM(String gcm) {
		if ((gcm==null) || (gcm.equals(""))) this.gcm = "";
		else this.gcm = gcm;
	}
	public void setUserAgreement(String ua) {
		if ((ua==null) || (ua.equals(""))) this.ua = "";
		else this.ua = ua;
	}
	public void setLocationAgreement(String la) {
		if ((la==null) || (la.equals(""))) this.la = "";
		else this.la = la;
	}
	public void setManager(String manager) {
		if ((manager==null) || (manager.equals(""))) this.manager = "";
		else this.manager = manager;
	}
	public void setTodaySale(String todaysale) {
		if ((todaysale==null) || (todaysale.equals(""))) this.todaysale = "";
		else this.todaysale = todaysale;
	}
	
	

	public String getService() {
		return this.service;
	}
	public String getQ() {
		return this.q;
	}
	public String getLat() {
		return this.lat;
	}
	public String getLng() {
		return this.lng;
	}
	public String getMinLat() {
		return this.minLat;
	}
	public String getMinLng() {
		return this.minLng;
	}
	public String getMaxLat() {
		return this.maxLat;
	}
	public String getMaxLng() {
		return this.maxLng;
	}
	public String getD() {
		return this.d;
	}
	public String getSort() {
		return this.sort;
	}
	public String getStart() {
		return this.start;
	}
	public String getRow() {
		return this.row;
	}
	public String getFacet() {
		return this.facet;
	}
	public String getFacetQuery() {
		return this.facetQuery;
	}
	
	public String getMid() { return this.mid; }
	public String getPid() { return this.pid; }
	public String getProduct() { return this.product; }
	public String getPrice() { return this.price; }
	public String getOPrice() { return this.oprice; }
	public String getSDate() { return this.sdate; }
	public String getEDate() { return this.edate; }
	public String getDesc() { return this.desc; }
	public String getSelectCount() { return this.selectcount; }
	public String getCount() { return this.count; }
	public String getMoreCount() { return this.morecount; }
	public String getConditionalPrice() { return this.conditionalprice; }
	public String getOriginalPrice() { return this.originalprice; }
	
	public void setProduct(String product) { if ((product==null) || (product.equals(""))) this.product = ""; else this.product = product; }
	public void setPrice(String price) { if ((price==null) || (price.equals(""))) this.price = ""; else this.price = price; }
	public void setOPrice(String oprice) { if ((oprice==null) || (oprice.equals(""))) this.oprice = ""; else this.oprice = oprice; }
	public void setSDate(String sdate) { if ((sdate==null) || (sdate.equals(""))) this.sdate = ""; else this.sdate = sdate; }
	public void setEDate(String edate) { if ((edate==null) || (edate.equals(""))) this.edate = ""; else this.edate = edate; }
	public void setDesc(String desc) { if ((desc==null) || (desc.equals(""))) this.desc = ""; else this.desc = desc; }
	public void setSelectCount(String selectcount) { if ((selectcount==null) || (selectcount.equals(""))) this.selectcount = ""; else this.selectcount = selectcount; }
	public void setCount(String count) { if ((count==null) || (count.equals(""))) this.count = ""; else this.count = count; }
	public void setMoreCount(String morecount) { if ((morecount==null) || (morecount.equals(""))) this.morecount = ""; else this.morecount = morecount; }
	public void setConditionalPrice(String conditionalprice) { if ((conditionalprice==null) || (conditionalprice.equals(""))) this.conditionalprice = ""; else this.conditionalprice = conditionalprice; }
	public void setOriginalPrice(String originalprice) { if ((originalprice==null) || (originalprice.equals(""))) this.originalprice = ""; else this.originalprice = originalprice; }
	

	
	public String getIdx1() {
		return this.idx1;
	}
	public String getEmail() {
		return this.email;
	}
	public String getNo() {
		return this.no;
	}
	public String getDisplay() {
		return this.display;
	}
	public String getPassword() {
		return this.password;
	}
	public String getNewPassword() {
		return this.newPassword;
	}
	public String getKeepLogin() {
		return this.keepLogin;
	}
	public String getName() {
		return this.name;
	}
	public String getAddr1() {
		return this.addr1;
	}
	public String getAddr2() {
		return this.addr2;
	}
	public String getAddr3() {
		return this.addr3;
	}
	public String getAddr4() {
		return this.addr4;
	}
	public String getAddr5() {
		return this.addr5;
	}
	public String getX() {
		return this.x;
	}
	public String getY() {
		return this.y;
	}
		
	public String getData1() {
		return this.data1;
	}
	public String getData2() {
		return this.data2;
	}
	public String getData3() {
		return this.data3;
	}
	public String getGCM() {
		return this.gcm;
	}
	public String getUserAgreement() {
		return this.ua;
	}
	public String getLocationAgreement() {
		return this.la;
	}
	public String getManager() {
		return this.manager;
	}
	public String getTodaySale() {
		return this.todaysale;
	}
}
