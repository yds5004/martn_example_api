package net.ncue.mart.dao;

/**
 * @author: DosangYoon
 * @version: 0.1
 * @date    01/01/2010
 * @brief:
 */
 
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.htmlparser.jericho.Source;
import net.ncue.mart.model.Msg;
import net.ncue.mart.model.Sale;
import net.ncue.mart.util.Database;
import net.ncue.mart.util.Excel;
import net.ncue.mart.util.KMAKwd;
import net.ncue.mart.util.MartParameter;
import net.ncue.conf.Configure;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

public class MartDao {
	Database database = null;
	Excel excel = new Excel();
	KMAKwd kMAKwd = new KMAKwd();

	public JSONObject getMartsByRoundSearch(MartParameter parameter) throws IOException {
		try{
			String url = Configure.MART_URL+"q="+URLEncoder.encode(parameter.getQ(), "UTF-8");
			url += "&wt=json&indent=true&start="+parameter.getStart()+"&rows="+parameter.getRow()+"&";
			url += "fq={!geofilt}&pt="+parameter.getLat()+","+parameter.getLng()+"&sfield=latlon&d="+parameter.getD()+"&sort="+URLEncoder.encode(parameter.getSort(), "UTF-8");
			url += "&facet=true&facet.field="+parameter.getFacet();

			Source source = new Source(new URL(url));
			return JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public JSONObject getMartsByBBoxSearch(MartParameter parameter) throws IOException {
		try{
			String url = Configure.MART_URL+"q="+URLEncoder.encode(parameter.getQ(), "UTF-8");
			url += "&wt=json&indent=true&start="+parameter.getStart()+"&rows="+parameter.getRow()+"&";
			url += "fq=latlon:["+parameter.getMinLat()+","+parameter.getMinLng()+"%20TO%20"+parameter.getMaxLat()+","+parameter.getMaxLng()+"]";
			url += "&facet=true&facet.field="+parameter.getFacet();
			Source source = new Source(new URL(url));
			return JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public JSONObject getGroupCountByRegion(MartParameter parameter) throws IOException {
		try{
			String url = Configure.MART_URL+"q="+URLEncoder.encode(parameter.getQ(), "UTF-8");
			url += "&wt=json&indent=true&start="+parameter.getStart()+"&rows="+parameter.getRow();
			url += "&facet=true&facet.field="+parameter.getFacet();
			if (!parameter.getFacetQuery().equals("")) {
				url += "&facet.query="+parameter.getFacetQuery();
			}

			Source source = new Source(new URL(url));
			return JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}

	
    
    public void connect() throws Exception {
    	this.disconnect();
    	if (this.database == null) {
    		this.database = new Database(Configure.DBHOST, Configure.DBPORT, "ncue", Configure.DBID, Configure.DBPASSWORD, false);
    	}
    }
    public void disconnect() {
    	if (this.database != null) {
    		this.database.close();
    		this.database=null;
    	}
    }
    public void connectToMartDB() throws Exception {
    	this.disconnect();    	
    	if (this.database == null) {
    		this.database = new Database(Configure.DBHOST, Configure.DBPORT, "mart", Configure.DBID, Configure.DBPASSWORD, false);
    	}
    }
    public void connectToXE() throws Exception {
    	this.disconnect();
    	if (this.database == null) {
    		this.database = new Database(Configure.DBHOST, Configure.DBPORT, "martn", Configure.DBID, Configure.DBPASSWORD, false);
    	}
    }
    
    
    public JSONObject toMsgString(String msg) {
    	return JSONObject.fromObject(JSONSerializer.toJSON("{\"msg\":\""+msg+"\"}"));
    }
    public JSONObject toMsgString(String msg, MartParameter parameter) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{");
    	sb.append("\"msg\":\"").append(msg).append("\"");
    	sb.append(",\"email\":\"").append(parameter.getEmail()).append("\"");
    	sb.append(",\"name\":\"").append(parameter.getName()).append("\"");
    	sb.append(",\"password\":\"").append(parameter.getPassword()).append("\"");    	
    	sb.append(",\"addr1\":\"").append(parameter.getAddr1()).append("\"");
    	sb.append(",\"addr2\":\"").append(parameter.getAddr2()).append("\"");
    	sb.append(",\"addr3\":\"").append(parameter.getAddr3()).append("\"");
    	sb.append(",\"addr4\":\"").append(parameter.getAddr4()).append("\"");
    	sb.append(",\"addr5\":\"").append(parameter.getAddr5()).append("\"");
    	sb.append(",\"x\":\"").append(parameter.getX()).append("\"");
    	sb.append(",\"y\":\"").append(parameter.getY()).append("\"");
    	sb.append(",\"mid\":\"").append(parameter.getMid()).append("\"");
    	sb.append(",\"keepLogin\":\"").append(parameter.getKeepLogin()).append("\"");
    	sb.append("}");
    	return JSONObject.fromObject(JSONSerializer.toJSON(sb.toString()));
    }
    public JSONObject signup(HttpServletRequest request, MartParameter parameter) throws IOException {
		try{
			// name이 없는 경우
			if (parameter.getName().equals("")) return this.toMsgString(Msg.NOT_EXIST_NAME.toString());
			// email이 없는 경우
			if (parameter.getEmail().equals("")) return this.toMsgString(Msg.NOT_EXIST_EMAIL.toString());
			// password가 없는 경우
			if (parameter.getPassword().equals("")) return this.toMsgString(Msg.NOT_EXIST_PASSWORD.toString());
			
						
			this.connect();
			String sql = "select count(*) count from user where email='" +parameter.getEmail()+ "'";	
			String[][] arr = this.database.getRSet(sql);
			this.disconnect();
			if (arr!=null && arr.length==1 && Integer.parseInt(arr[0][0])>=1) {
				return this.toMsgString(Msg.SAME_EMAIL.toString());
			}

			this.connect();
			sql = "insert into user(email, name, password, x, y) values(?,?,?,?,?)";
			Vector<String> param = new Vector<String>();
			param.add(parameter.getEmail());
			param.add(parameter.getName());
			param.add(parameter.getPassword());
			param.add(parameter.getX());
			param.add(parameter.getY());
			this.database.insert(sql, param, false);
			this.database.insert("commit", new Vector<String>());
			this.disconnect();
			HttpSession session = request.getSession();
			session.setAttribute("email", parameter.getEmail());
			session.setAttribute("name", parameter.getName());
			session.setAttribute("password", parameter.getPassword());
			session.setAttribute("addr1", parameter.getAddr1());
			session.setAttribute("addr2", parameter.getAddr2());
			session.setAttribute("addr3", parameter.getAddr3());
			session.setAttribute("addr4", parameter.getAddr4());
			session.setAttribute("addr5", parameter.getAddr5());
			session.setAttribute("x", parameter.getX());
			session.setAttribute("y", parameter.getY());
			session.setAttribute("mid", parameter.getMid());
   
			return this.toMsgString(Msg.SUCCESS.toString(), parameter);
	       
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public JSONObject login(HttpServletRequest request, MartParameter parameter) throws IOException {
		try{
			// email이 없는 경우
			if (parameter.getEmail().equals("")) return this.toMsgString(Msg.NOT_EXIST_EMAIL.toString());
			// password가 없는 경우
			if (parameter.getPassword().equals("")) return this.toMsgString(Msg.NOT_EXIST_PASSWORD.toString());
			this.connect();
			String sql = "select email, name, password, addr1, addr2, addr3, addr4, addr5, x, y, mid from user where email='" +parameter.getEmail()+ "' and password='"+parameter.getPassword()+"'";
			String[][] arr = this.database.getRSet(sql);
			this.disconnect();

			if (arr!=null && arr.length>=1) {
				HttpSession session = request.getSession();
			   session.setAttribute("email", arr[0][0]);
			   session.setAttribute("name", arr[0][1]);
			   session.setAttribute("password", arr[0][2]);
			   session.setAttribute("addr1", arr[0][3]);
			   session.setAttribute("addr2", arr[0][4]);
			   session.setAttribute("addr3", arr[0][5]);
			   session.setAttribute("addr4", arr[0][6]);
			   session.setAttribute("addr5", arr[0][7]);
			   session.setAttribute("x", arr[0][8]);
			   session.setAttribute("y", arr[0][9]);
			   if (arr[0][10]==null) {
				   session.setAttribute("mid", "");
			   } else {
				   session.setAttribute("mid", arr[0][10]);
			   }
			   session.setAttribute("keepLogin", parameter.getKeepLogin());
			   
			   parameter.setEmail(arr[0][0]);
			   parameter.setName(arr[0][1]);
			   parameter.setPassword(arr[0][2]);
			   parameter.setAddr1(arr[0][3]);
			   parameter.setAddr2(arr[0][4]);
			   parameter.setAddr3(arr[0][5]);
			   parameter.setAddr4(arr[0][6]);
			   parameter.setAddr5(arr[0][7]);
			   parameter.setX(arr[0][8]);
			   parameter.setY(arr[0][9]);
			   if (arr[0][10]==null) {
				   parameter.setMid("");
			   } else {
				   parameter.setMid(arr[0][10]);
			   }
				return this.toMsgString(Msg.SUCCESS.toString(), parameter);
			} else {
				this.connect();
				sql = "select count(*) count from user where email='" +parameter.getEmail()+ "'";
				arr = this.database.getRSet(sql);
				this.disconnect();
				if (arr!=null && Integer.parseInt(arr[0][0])>0) {
					return this.toMsgString(Msg.WRONG_PASSWORD.toString());
				} else {
					return this.toMsgString(Msg.NOT_EXIST_EMAIL.toString());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	public JSONObject logout(HttpServletRequest request, MartParameter parameter) throws IOException {
		try{
			HttpSession session = request.getSession();
			session.setAttribute("email", "");
			session.setAttribute("name", "");
			session.setAttribute("password", "");
			session.setAttribute("addr1", "");
			session.setAttribute("addr2", "");
			session.setAttribute("addr3", "");
			session.setAttribute("addr4", "");
			session.setAttribute("addr5", "");
			session.setAttribute("x", "");
			session.setAttribute("y", "");
			session.setAttribute("mid", "");
			session.invalidate();
			return this.toMsgString(Msg.LOGOUT.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	public JSONObject changePassword(HttpServletRequest request, MartParameter parameter) throws IOException {
		try{
			HttpSession session = request.getSession();
			if (session == null || session.getAttribute("email")==null) {
				this.toMsgString(Msg.FAIL.toString());
			}
			
			this.connect();
			String sql = "select password from user where email='" +session.getAttribute("email").toString()+ "'";
			String[][] arr = this.database.getRSet(sql);
			if (arr==null || arr.length<1) {
				this.disconnect();
				return this.toMsgString(Msg.FAIL.toString());
			} else {
				if (!arr[0][0].trim().equals(parameter.getPassword())) {
					this.disconnect();
					return this.toMsgString(Msg.WRONG_PASSWORD.toString());
				}
			}
			sql = "update user set password = '"+parameter.getNewPassword()+"', name = '"+parameter.getName()+"' where email='" +session.getAttribute("email").toString()+ "'";

			this.database.update(sql, new Vector<String>());
			this.database.update("commit", new Vector<String>());
			this.disconnect();

		   session.setAttribute("email", session.getAttribute("email").toString());
		   session.setAttribute("password", parameter.getPassword());
		   return this.toMsgString(Msg.SUCCESS_CHANGE_PASSWORD.toString());		   
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	
	public JSONObject checkEmail(HttpServletRequest request, MartParameter parameter) throws Exception {
		if (!parameter.getEmail().equals("")) {
			this.connect();
			String sql = "select email, name, password, addr1, addr2, addr3, addr4, addr5, x, y, mid from user where email='" +parameter.getEmail()+ "'";
			String[][] arr = this.database.getRSet(sql);					
			this.disconnect();

			if (arr!=null && arr.length>=1) {
				parameter.setKeepLogin("1");
				HttpSession session = request.getSession();
				session.setAttribute("email", arr[0][0]);
				session.setAttribute("name", arr[0][1]);
				session.setAttribute("password", arr[0][2]);
				session.setAttribute("addr1", arr[0][3]);
				session.setAttribute("addr2", arr[0][4]);
				session.setAttribute("addr3", arr[0][5]);
				session.setAttribute("addr4", arr[0][6]);
				session.setAttribute("addr5", arr[0][7]);
				session.setAttribute("x", arr[0][8]);
				session.setAttribute("y", arr[0][9]);
				if (arr[0][10]==null) {
					   session.setAttribute("mid", "");
				   } else {
					   session.setAttribute("mid", arr[0][10]);
				   }
				session.setAttribute("keepLogin", parameter.getKeepLogin());
				parameter.setEmail(arr[0][0]);
			   	parameter.setName(arr[0][1]);
			   	parameter.setPassword(arr[0][2]);
			   	parameter.setAddr1(arr[0][3]);
			   	parameter.setAddr2(arr[0][4]);
			   	parameter.setAddr3(arr[0][5]);
			   	parameter.setAddr4(arr[0][6]);
			   	parameter.setAddr5(arr[0][7]);
			   	parameter.setX(arr[0][8]);
			   	parameter.setY(arr[0][9]);
			   	if (arr[0][10]==null) {
			   		parameter.setMid("");
				} else {
					parameter.setMid(arr[0][10]);
				}
				return this.toMsgString(Msg.LOGIN.toString(), parameter);
			}
		}
		return this.toMsgString(Msg.NOT_EXIST_EMAIL.toString(), parameter);
	}
	
	public JSONObject checkStatus(HttpServletRequest request, MartParameter parameter) throws IOException {
		try{
			HttpSession session = request.getSession();
			if (session != null) {
				if (session.getAttribute("addr1")!=null) {
					parameter.setAddr1(session.getAttribute("addr1").toString());
				}
				if (session.getAttribute("addr2")!=null) {
					parameter.setAddr2(session.getAttribute("addr2").toString());
				}
				if (session.getAttribute("addr3")!=null) {
					parameter.setAddr3(session.getAttribute("addr3").toString());
				}
				if (session.getAttribute("addr4")!=null) {
					parameter.setAddr4(session.getAttribute("addr4").toString());
				}
				if (session.getAttribute("addr5")!=null) {
					parameter.setAddr5(session.getAttribute("addr5").toString());
				}
				if (session.getAttribute("name")!=null) {
					parameter.setName(session.getAttribute("name").toString());
				}
				if (session.getAttribute("password")!=null) {
					parameter.setPassword(session.getAttribute("password").toString());
				}
				if (session.getAttribute("x")!=null) {
					parameter.setX(session.getAttribute("x").toString());
				}
				if (session.getAttribute("y")!=null) {
					parameter.setY(session.getAttribute("y").toString());
				}
				if (session.getAttribute("mid")!=null) {
					parameter.setMid(session.getAttribute("mid").toString());
				} else {
					this.connect();
					String sql = "select mid from user where email='" +parameter.getEmail()+ "'";
					String[][] arr = this.database.getRSet(sql);
					this.disconnect();
					if (arr!=null && arr.length>=1) {
						parameter.setMid(arr[0][0]);	
					}
				}
				if (session.getAttribute("keepLogin")!=null) {
					parameter.setKeepLogin(session.getAttribute("keepLogin").toString());
				}
				if (session.getAttribute("email")!=null) {
					parameter.setEmail(session.getAttribute("email").toString());
					return this.toMsgString(Msg.LOGIN.toString(), parameter);
				}
				if (!parameter.getEmail().equals("")) {
					return this.checkEmail(request, parameter);
				}
			} else {
				return this.login(request, parameter);
			}
			return this.toMsgString(Msg.LOGOUT.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	
	
	
	
	
	
	
	
	
	public JSONObject searchProduct(MartParameter parameter) throws IOException {
		try{
			// http://ncue.net:8999/solr/price/select?q=*%3A*&wt=json&indent=true&start=0&rows=20&fq={!geofilt}&pt=37.32168096048367,127.08729028701782&sfield=latlon&d=2&sort=geodist%28%29+asc&facet=true&facet.field=addr1
			String url = Configure.PRICE_URL+"q="+URLEncoder.encode(parameter.getQ(), "UTF-8");
			url += "&wt=json&indent=true&start="+parameter.getStart()+"&rows="+parameter.getRow()+"&";
			url += "fq={!geofilt}&pt="+parameter.getLat()+","+parameter.getLng()+"&sfield=latlon&d="+parameter.getD()+"&sort="+URLEncoder.encode(parameter.getSort(), "UTF-8");

			Source source = new Source(new URL(url));
			return JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	
	public JSONObject searchSaleProduct(MartParameter parameter) throws IOException {
		try{
			String url = Configure.SALE_URL+"q="+URLEncoder.encode(parameter.getQ(), "UTF-8");
			url += "&wt=json&indent=true&start="+parameter.getStart()+"&rows="+parameter.getRow()+"&";
			url += "fq={!geofilt}&pt="+parameter.getLat()+","+parameter.getLng()+"&sfield=latlon&d="+parameter.getD()+"&sort="+URLEncoder.encode(parameter.getSort(), "UTF-8");

			Source source = new Source(new URL(url));
			return JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	
	
	
	public void insertFileInfo(String mid, String fileName) throws Exception {
		this.connectToMartDB();
		String sql = "insert into file(mid, filename) values(?,?)";
		Vector<String> param = new Vector<String>();
		param.add(mid);
		param.add(fileName);		
		this.database.insert(sql, param, false);
		this.database.insert("commit", new Vector<String>());		
		this.disconnect();
	}
	
	public void insertSalePrice(String mid, String fullFileName) throws Exception {
		List<Sale> saleList = new ArrayList<Sale>();
		if (fullFileName.indexOf(".")>0) {
			String[] arr = fullFileName.split("\\.");
			if (arr[1].trim().equals("xlsx")) {
				saleList = excel.processXlsx(fullFileName);
			} else {
				saleList = excel.processXls(fullFileName);
			}
		}
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.connectToMartDB();
		for (int i=0; i<saleList.size(); i++) {
			Sale sale = saleList.get(i);
			String sql = "select count(*) count from sale where mid="+mid+" and product='"+sale.getProduct()+"' and sday='"+sale.getSDate() + "' and eday='"+sale.getEDate() + "'";
			
			String[][] arr = this.database.getRSet(sql);
			if (arr!=null && arr.length>0 && Integer.parseInt(arr[0][0])>0) {
				// update
				sql = "update sale set mcategory=?, category=?, productidx=?, head=?, idx1=?, count=?, morecount=?, isadd=?, price=?, conditionalprice=?, originalprice=?, description=?, sday=?, eday=?, create_date=? where mid=? and product=?";
				Vector<String> param = new Vector<String>();
				param.add(sale.getMcategory());
				param.add(sale.getCategory());
				param.add(sale.getProductIdx());
				param.add(sale.getHead());
				param.add(sale.getIdx1());
				param.add(sale.getCount());
				param.add(sale.getMorecount());
				param.add(sale.getIsadd());
				param.add(sale.getPrice());
				param.add(sale.getConditionalPrice());
				param.add(sale.getOriginalprice());
				param.add(sale.getdescription());
				param.add(sale.getSDate());
				param.add(sale.getEDate());
				param.add(format.format(now));
				param.add(mid);
				param.add(sale.getProduct());
				this.database.update(sql, param, false);
			} else {
				// insert
				sql = "insert into sale(mid, mcategory, category, product, productidx, head, idx1, count, morecount, isadd, price, conditionalprice, originalprice, description, sday, eday) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Vector<String> param = new Vector<String>();
				param.add(mid);
				param.add(sale.getMcategory());
				param.add(sale.getCategory());
				param.add(sale.getProduct());
				param.add(sale.getProductIdx());
				param.add(sale.getHead());
				param.add(sale.getIdx1());
				param.add(sale.getCount());
				param.add(sale.getMorecount());
				param.add(sale.getIsadd());
				param.add(sale.getPrice());
				param.add(sale.getConditionalPrice());
				param.add(sale.getOriginalprice());
				param.add(sale.getdescription());
				param.add(sale.getSDate());
				param.add(sale.getEDate());
				this.database.insert(sql, param, false);
			}
		}
		this.database.insert("commit", new Vector<String>());
		this.disconnect();
	}
	
	class ModifiedDate implements Comparator<File>{
		public int compare(File f1, File f2) {
			if(f1.lastModified() < f2.lastModified()) return 1;
	        if(f1.lastModified() == f2.lastModified()) return 0;
	        return -1;
		}
	}
	public JSONArray uploadFileInfo(String mid) throws Exception {
		MartParameter parameter = new MartParameter();
		parameter.setMid(mid);
		return this.uploadFileInfo(parameter);
	}
	public JSONArray uploadFileInfo(MartParameter parameter) throws Exception {
		JSONArray jSONArray = new JSONArray();
		String dir = Configure.MART_SALE_FIlE_DIR+parameter.getMid();
		File dirFile=new File(dir);
		File[] fileList=dirFile.listFiles();
		Arrays.sort(fileList,new ModifiedDate());
		JSONObject jSONObject = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(File file : fileList) {
			if (file.isFile()) {				
				Date datetime=new Date(file.lastModified());
				jSONObject.put("url", "http://mfile.ncue.net/"+parameter.getMid()+"/"+file.getName());
				jSONObject.put("filename", file.getName());
				jSONObject.put("datetime", format.format(datetime));
				jSONArray.add(jSONObject);
				if (jSONArray.size()>=20) break;
			}
		}
		return jSONArray;
	}
	
	
	
	
	
	
	
	public JSONObject setViewCount(MartParameter parameter) throws Exception {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int viewcount = 1;

		this.connectToMartDB();
		String sql = "select cnt from viewcount where mid="+parameter.getMid()+" and pid="+parameter.getPid()+" and day='"+format.format(now)+"'";

		String[][] arr = this.database.getRSet(sql);
		if (arr==null || arr.length<1) {
			// insert
			sql = "insert into viewcount(mid, pid, day, cnt) values("+parameter.getMid()+","+parameter.getPid()+",'"+format.format(now)+"', 1)";

			this.database.insert(sql, new Vector<String>());
		} else {
			// update
			viewcount += Integer.parseInt(arr[0][0]);
			sql = "update viewcount set cnt="+Integer.toString(viewcount)+" where mid="+parameter.getMid()+" and pid="+parameter.getPid()+" and day='"+format.format(now)+"'";
			this.database.update(sql, new Vector<String>());
		}
		this.database.insert("commit", new Vector<String>());
		
		sql = "select cnt from viewcount where mid="+parameter.getMid() + " and pid="+parameter.getPid();
		arr = this.database.getRSet(sql);		
		viewcount += Integer.parseInt(arr[0][0]);
		this.disconnect();

		return this.toMsgString(Integer.toString(viewcount));
	}
	public JSONObject setPbClickCount(MartParameter parameter) throws Exception {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int viewcount = 1;

		this.connectToMartDB();
		String sql = "select cnt from pbclickcount where mid="+parameter.getMid()+" and pid="+parameter.getPid()+" and day='"+format.format(now)+"'";

		String[][] arr = this.database.getRSet(sql);
		if (arr==null || arr.length<1) {
			// insert
			sql = "insert into pbclickcount(mid, pid, day, cnt) values("+parameter.getMid()+","+parameter.getPid()+",'"+format.format(now)+"', 1)";

			this.database.insert(sql, new Vector<String>());
		} else {
			// update
			viewcount += Integer.parseInt(arr[0][0]);
			sql = "update pbclickcount set cnt="+Integer.toString(viewcount)+" where mid="+parameter.getMid()+" and pid="+parameter.getPid()+" and day='"+format.format(now)+"'";
			this.database.update(sql, new Vector<String>());
		}
		this.database.insert("commit", new Vector<String>());
		
		sql = "select cnt from pbclickcount where mid="+parameter.getMid() + " and pid="+parameter.getPid();
		arr = this.database.getRSet(sql);		
		viewcount += Integer.parseInt(arr[0][0]);
		this.disconnect();

		return this.toMsgString(Integer.toString(viewcount));
	}
	
	public JSONArray getProductDescription(MartParameter parameter) throws IOException {
		try{
			JSONArray array = new JSONArray();
			String idx1 = parameter.getIdx1();
			if (idx1.trim().equals("")) return array; 

			StringBuffer sb = new StringBuffer();
			sb.append("(mid:").append(parameter.getMid()).append(")");
			sb.append("AND -(product:").append(parameter.getName()).append(")");

			
			StringBuffer indexQuery = new StringBuffer();
			String[] arr = idx1.split(" ");
			for (int i=0; i<arr.length; i++) {
				if (i==0) {
					if (i == arr.length-1) {
						indexQuery.append(" AND (").append("idx1:").append(arr[i]).append(")");
					} else {
						indexQuery.append(" AND (").append("idx1:").append(arr[i]);	
					}
				} else if (i == arr.length-1) {
					indexQuery.append(" OR ").append("idx1:").append(arr[i]).append(")");
				} else {
					indexQuery.append(" OR ").append("idx1:").append(arr[i]);
				}
			}
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");			
			indexQuery.append(" AND (sday:[* TO ").append(format.format(now)).append("T99:99:99Z] AND eday:[").append(format.format(now)).append("T00:00:00Z TO *])");
			sb.append(indexQuery.toString());
			

			String url = Configure.SALE_URL+"q="+URLEncoder.encode(sb.toString(), "UTF-8");
			url += "&wt=json&indent=true&start=0&rows=5&";
			url += "&sort="+URLEncoder.encode(parameter.getSort(), "UTF-8");
			Source source1 = new Source(new URL(url));
			array.add(JSONObject.fromObject(JSONSerializer.toJSON(source1.toString())));
			
			sb.setLength(0);
			sb.append("-(mid:").append(parameter.getMid()).append(")");
			sb.append(indexQuery.toString());
			url = Configure.SALE_URL+"q="+URLEncoder.encode(sb.toString(), "UTF-8");
			url += "&wt=json&indent=true&start=0&rows=10&";
			url += "&sort="+URLEncoder.encode(parameter.getSort(), "UTF-8");
			Source source2 = new Source(new URL(url));
			array.add(JSONObject.fromObject(JSONSerializer.toJSON(source2.toString())));
			
			
			StringBuffer onlineQuery = new StringBuffer();
			for (int i=0; i<arr.length; i++) {
				if (i==0) {
					if (i == arr.length-1) {
						onlineQuery.append(" (").append("idx1:").append(arr[i]).append(")");
					} else {
						onlineQuery.append(" (").append("idx1:").append(arr[i]);	
					}
				} else if (i == arr.length-1) {
					onlineQuery.append(" OR ").append("idx1:").append(arr[i]).append(")");
				} else {
					onlineQuery.append(" OR ").append("idx1:").append(arr[i]);
				}
			}
			url = Configure.ONLINEMALL_URL+"q="+URLEncoder.encode(onlineQuery.toString(), "UTF-8");
			url += "&wt=json&indent=true&start=0&rows=4&";
			Source source3 = new Source(new URL(url));
			array.add(JSONObject.fromObject(JSONSerializer.toJSON(source3.toString())));		
			
			return array;
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONArray();
	}

	public String getMyMid(MartParameter parameter) throws Exception {
		this.connectToXE();
		String sql = "select extra_vars from xe_member where email_address='"+parameter.getEmail()+"'";
		String[][] arr = this.database.getRSet(sql);
		this.disconnect();

		if (arr!=null && arr.length>0 && arr[0]!=null && arr[0][0]!=null && !arr[0][0].equals("")) {
			String str = arr[0][0];
			int pos = str.indexOf("martid");
			if (pos>=0) {
				str = str.substring(pos).replaceAll("}", "").replaceAll("\"", "");
				String[] temps = str.split(":");
				if (temps.length>=2) {
					String mid = temps[2];
					temps = mid.split(";");
					mid = temps[0];
					return mid;
				}
			} else {
			}
		}
		return "0";
	}
	public JSONArray getMID(MartParameter parameter) throws IOException {
		try{
			this.connectToXE();
			String sql = "select extra_vars from xe_member where email_address='"+parameter.getEmail()+"'";
			String[][] arr = this.database.getRSet(sql);
			this.disconnect();

			if (arr!=null && arr.length>0 && arr[0]!=null && arr[0][0]!=null && !arr[0][0].equals("")) {
				String str = arr[0][0];
				int pos = str.indexOf("martid");
				if (pos>=0) {
					str = str.substring(pos).replaceAll("}", "").replaceAll("\"", "");
					String[] temps = str.split(":");
					if (temps.length>=2) {
						String mid = temps[2];
						temps = mid.split(";");
						mid = temps[0];
						return this.uploadFileInfo(mid);
					}
				} else {
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	
	
	
	
	public JSONArray getSaleData(String mid, String sort) throws Exception {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JSONArray jSONArray = new JSONArray();
		
		this.connectToMartDB();
		String sql = "select id, mid, mcategory, category, product, head, count, morecount, isadd, saletype, price, conditionalprice, originalprice, description, image, sday, eday, display, create_date from sale where mid="+mid;
		sql += " and (sday<='"+sdf.format(now)+"' and eday>='"+sdf.format(now)+"')";
		if (sort.equals("a_product"))  sql += " order by product asc"; else if (sort.equals("d_product")) sql += " order by product desc";
		if (sort.equals("a_sale"))  sql += " order by price asc"; else if (sort.equals("d_sale")) sql += " order by price desc";
		if (sort.equals("a_price"))  sql += " order by description asc"; else if (sort.equals("d_price")) sql += " order by description desc";
		if (sort.equals("a_count"))  sql += " order by count asc"; else if (sort.equals("d_count")) sql += " order by count desc";
		if (sort.equals("a_morecount"))  sql += " order by morecount asc"; else if (sort.equals("d_morecount")) sql += " order by morecount desc";
		if (sort.equals("a_issadd"))  sql += " order by isadd asc"; else if (sort.equals("d_issadd")) sql += " order by isadd desc";
		if (sort.equals("a_sdate"))  sql += " order by sday asc"; else if (sort.equals("d_sdate")) sql += " order by sday desc";
		if (sort.equals("a_edate"))  sql += " order by eday asc"; else if (sort.equals("d_edate")) sql += " order by eday desc";
		if (sort.equals("a_display"))  sql += " order by display asc"; else if (sort.equals("d_display")) sql += " order by display desc";
		if (sort.equals("a_create_date"))  sql += " order by create_date asc"; else if (sort.equals("d_create_date")) sql += " order by create_date desc";
		
		String[][] arr = this.database.getRSet(sql);
		this.disconnect();
		if (arr!=null && arr.length>0) {
			for (int i=0; i<arr.length; i++) {
				Sale sale = new Sale(arr[i]);
				jSONArray.add(sale);
			}
		}

		return jSONArray;
	}
	// mid와 pid를 이용하여 상품 정보를 가져
	public JSONArray getSaleProductData(String email, String mid, String pid) throws Exception {
		JSONArray jSONArray = new JSONArray();
		if (email.trim().equals("") || pid.trim().equals("")) return jSONArray;
		mid = this.getMid(email);
		
		this.connectToMartDB();
		String sql = "select id, mid, mcategory, category, product, head, count, morecount, isadd, saletype, price, conditionalprice, originalprice, description, image, sday, eday, display, create_date from sale where mid="+mid+" and id="+pid;

		String[][] arr = this.database.getRSet(sql);
		this.disconnect();
		if (arr!=null && arr.length>0) {
			for (int i=0; i<arr.length; i++) {
				Sale sale = new Sale(arr[i]);
				jSONArray.add(sale);
			}
		}

		return jSONArray;
	}
	// mid와 pid를 이용하여 상품 정보를 가져
	public JSONArray getSaleProductData(String mid, String pid) throws Exception {
		JSONArray jSONArray = new JSONArray();
		if (mid.trim().equals("") || pid.trim().equals("")) return jSONArray;
		
		this.connectToMartDB();
		String sql = "select id, mid, mcategory, category, product, head, count, morecount, isadd, saletype, price, conditionalprice, originalprice, description, image, sday, eday, display, create_date from sale where mid="+mid+" and id="+pid;

		String[][] arr = this.database.getRSet(sql);
		this.disconnect();
		if (arr!=null && arr.length>0) {
			for (int i=0; i<arr.length; i++) {
				Sale sale = new Sale(arr[i]);
				jSONArray.add(sale);
			}
		}

		return jSONArray;
	}
	public JSONArray getSaleData(MartParameter parameter) throws IOException {
		try{
			this.connectToXE();
			String sql = "select extra_vars from xe_member where email_address='"+parameter.getEmail()+"'";
			String[][] arr = this.database.getRSet(sql);
			this.disconnect();

			if (arr!=null && arr.length>0 && arr[0]!=null && arr[0][0]!=null && !arr[0][0].equals("")) {
				String str = arr[0][0];
				int pos = str.indexOf("martid");
				if (pos>=0) {
					str = str.substring(pos).replaceAll("}", "").replaceAll("\"", "");
					String[] temps = str.split(":");
					if (temps.length>=2) {
						String mid = temps[2];
						temps = mid.split(";");
						mid = temps[0];
						return this.getSaleData(mid, parameter.getSort());
					}
				} else {
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONArray();
	}
	// email로 mid 알아내기
	public String getMid(String email) throws IOException {
		try{
			this.connectToXE();
			String sql = "select extra_vars from xe_member where email_address='"+email+"'";
			String[][] arr = this.database.getRSet(sql);
			this.disconnect();

			if (arr!=null && arr.length>0 && arr[0]!=null && arr[0][0]!=null && !arr[0][0].equals("")) {
				String str = arr[0][0];
				int pos = str.indexOf("martid");
				if (pos>=0) {
					str = str.substring(pos).replaceAll("}", "").replaceAll("\"", "");
					String[] temps = str.split(":");
					if (temps.length>=2) {
						String mid = temps[2];
						temps = mid.split(";");
						mid = temps[0];
						return mid;
					}
				} else {
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public JSONArray changeDisplayStatus(MartParameter parameter) throws IOException {
		try{
			String sql = "update sale set display="+parameter.getDisplay()+" where mid="+parameter.getMid()+" and id="+parameter.getNo();
			
			this.connectToMartDB();
			this.database.update(sql, new Vector<String>(), false);
			this.database.insert("commit", new Vector<String>());
			this.disconnect();

			return this.getSaleData(parameter.getMid(), parameter.getSort());			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONArray();
	}
	

	public JSONArray registProduct(MartParameter parameter) throws Exception {
		String mid = parameter.getMid();
		String q = parameter.getQ();
		String[] productList = q.split("===");
		
		this.connectToMartDB();
		for (int i=0; i<productList.length; i++) {
			String[] product = productList[i].split("=");
			if (product.length!=7) continue;
			
			String name=product[0];
			String count=product[1];
			String morecount=product[2];
			String price=product[3];
			String originalprice=product[4];
			String sday=product[5];
			String eday=product[6];

			String sql = "insert into sale(mid, category, product, idx1, count, morecount, price, originalprice, sday, eday) values("+mid+",'공산','"+name+"','"+kMAKwd.analyzeProduct(name)+"',"+count+","+morecount+","+price+","+originalprice+",'"+sday+"','"+eday+"')";
			this.database.insert(sql, new Vector<String>());
		}
		this.database.insert("commit", new Vector<String>());
		this.disconnect();
		
		JSONArray jsonArray = this.getSaleData(mid, "d_create_date");
		return jsonArray;
	}
	
	public JSONArray modifyItem(MartParameter parameter) throws Exception {
		String mid = parameter.getMid();
		String id = parameter.getNo();
		
		this.connectToMartDB();
		
		String product=parameter.getProduct();
		String price=parameter.getPrice();
		String oprice=parameter.getOPrice();
		String sdate=parameter.getSDate();
		String edate=parameter.getEDate();
		String desc=parameter.getDesc();

		String sql = "update sale set product='"+product+"', price="+price+", originalprice="+oprice+", sday='"+sdate+"', eday='"+edate+"',description='"+desc+"' where mid="+mid+" and id="+id;
		this.database.insert(sql, new Vector<String>(), false);
		this.database.insert("commit", new Vector<String>());
		this.disconnect();
		
		String sort = parameter.getSort();
		JSONArray jsonArray = this.getSaleData(mid, sort);
		return jsonArray;
	}
	
	// 마트의 위치를 확인하기 위한 함수
	public JSONObject getPosition(MartParameter parameter) throws Exception {
		try{
			this.connectToXE();
			String sql = "select extra_vars from xe_member where email_address='"+parameter.getEmail()+"'";
			String[][] arr = this.database.getRSet(sql);
			this.disconnect();

			if (arr!=null && arr.length>0 && arr[0]!=null && arr[0][0]!=null && !arr[0][0].equals("")) {
				String str = arr[0][0];
				int pos = str.indexOf("martid");
				if (pos>=0) {
					str = str.substring(pos).replaceAll("}", "").replaceAll("\"", "");
					String[] temps = str.split(":");
					if (temps.length>=2) {
						String mid = temps[2];
						temps = mid.split(";");
						mid = temps[0];
						
						
						this.connectToMartDB();
						sql = "select name, telephone, addr1, addr2, addr3, addr4, addr5, x, y, martdesc, create_date from mart where id="+mid;
						arr = this.database.getRSet(sql);
						this.disconnect();
						
						if (arr.length==1) {
							StringBuffer sb = new StringBuffer();
							sb.append("{");
					    	sb.append("\"name\":\"").append(arr[0][0]).append("\"");
					    	sb.append(",\"telephone\":\"").append(arr[0][1]).append("\"");
					    	sb.append(",\"addr1\":\"").append(arr[0][2]).append("\"");    	
					    	sb.append(",\"addr2\":\"").append(arr[0][3]).append("\"");
					    	sb.append(",\"addr3\":\"").append(arr[0][4]).append("\"");
					    	sb.append(",\"addr4\":\"").append(arr[0][5]).append("\"");
					    	sb.append(",\"addr5\":\"").append(arr[0][6]).append("\"");
					    	sb.append(",\"x\":\"").append(arr[0][7]).append("\"");
					    	sb.append(",\"y\":\"").append(arr[0][8]).append("\"");
					    	sb.append(",\"mid\":\"").append(mid).append("\"");
					    	sb.append(",\"martdesc\":\"").append(arr[0][9]).append("\"");
					    	sb.append(",\"create_date\":\"").append(arr[0][10]).append("\"");
					    	sb.append("}");
					    	
							return JSONObject.fromObject(JSONSerializer.toJSON(sb.toString()));							
						} else {
							return new JSONObject();
						}
					}
				} else {
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	///////// cart (장바구니) 에 대한 처리 ///////////
	public JSONObject getCartedCount(MartParameter parameter) throws Exception {
		this.connectToMartDB();
		String sql  = " select count(*) from cart where email = '"+parameter.getEmail()+"'";
		String[][] arr = this.database.getRSet(sql);
		this.disconnect();
		
		if (arr==null || arr.length<1) return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"-1\", \"msg\":\"로그인을 하셔야 장바구니를 이용할 수 있습니다.\"}"));
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"cartedCount\":\""+arr[0][0]+"\", \"msg\":\"정상적으로 조회되었습니다.\"}"));	
	}
	
	public JSONObject insertCart(MartParameter parameter) throws Exception {
		this.connectToMartDB();
		String email = parameter.getEmail();
		
		// 이미 카트에 등록되어 있는지 확인한다.
		String sql  = " select selectcount from cart where email = '"+email+"' and pid="+parameter.getPid();
		String[][] arr = this.database.getRSet(sql);
		if (arr!=null && arr.length>0) {
			sql = "update cart set selectcount = "+Integer.toString(Integer.parseInt(arr[0][0])+1)+" where email='"+email+"' and pid="+parameter.getPid();
			this.database.update(sql, new Vector<String>());
			this.database.update("commit", new Vector<String>());
		} else {
			// 없을 시 추가한다.
			sql = "select mid, category, product, count, morecount, isadd, price, originalprice, description, image, sday, eday from sale where id = "+parameter.getPid();
			String[][] arrProduct = this.database.getRSet(sql);
			if (arrProduct.length==0) return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"등록된 상품이 없습니다.\"}"));
			String pid = parameter.getPid();
			String mid = arrProduct[0][0];
			String category = arrProduct[0][1];
			String product = arrProduct[0][2];
			String count = arrProduct[0][3];
			String morecount = arrProduct[0][4];
			String isadd = arrProduct[0][5];
			String price = arrProduct[0][6];
			String originalprice = arrProduct[0][7];
			String description = arrProduct[0][8];
			String image = arrProduct[0][9];
			String sday = arrProduct[0][10];
			String eday = arrProduct[0][11];
			String selectcount = parameter.getSelectCount();
			
			sql = "select name, telephone, mobile, addr1, addr2, addr3, addr4, addr5, x, y, image, martdesc from mart where id = "+mid;
			String[][] arrMart = this.database.getRSet(sql);
			if (arrMart.length==0) return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"등록된 상품이 없습니다.\"}"));
			String name = arrMart[0][0];
			String telephone = arrMart[0][1];
			String mobile = arrMart[0][2];
			String addr1 = arrMart[0][3];
			String addr2 = arrMart[0][4];
			String addr3 = arrMart[0][5];
			String addr4 = arrMart[0][6];
			String addr5 = arrMart[0][7];
			String x = arrMart[0][8];
			String y = arrMart[0][9];
			String mimage = arrMart[0][10];
			String martdesc = arrMart[0][11];
			
			sql  = " insert into cart(email, mid, name, telephone, mobile, addr1, addr2, addr3, addr4, addr5, x, y, mimage, martdesc, pid, category, product, count, morecount, isadd, price, originalprice, description, image, sday, eday, selectcount) ";
			sql += " values('"+email+"',"+mid+",'"+name+"','"+telephone+"','"+mobile+"','"+addr1+"','"+addr2+"','"+addr3+"','"+addr4+"','"+addr5+"','"+x+"','"+y+"','"+mimage+"','"+martdesc+"','"+pid+"','"+category+"','"+product+"','"+count+"','"+morecount+"','"+isadd+"','"+price+"','"+originalprice+"','"+description+"','"+image+"','"+sday+"','"+eday+"',"+selectcount+")";
			this.database.insert(sql, new Vector<String>(), false);
			this.database.insert("commit", new Vector<String>());
		}
		
		sql  = " select count(*) from cart where email = '"+email+"'";
		String[][] arrCount = this.database.getRSet(sql);
		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"cartedCount\":\""+arrCount[0][0]+"\", \"msg\":\"정상적으로 카트에 담겼습니다.\"}"));	
	}
	
	public JSONObject selectCart(MartParameter parameter) throws Exception {
		return this.selectCart(parameter, "ALL");
	}
	public JSONObject selectCart(MartParameter parameter, String type) throws Exception {
		this.connectToMartDB();
		String sql  = "";
		String[][] arr = null;
		if (type.equals("ALL")) {
			sql  = " select id, mid, name, telephone, mobile, addr1, addr2, addr3, addr4, addr5, x, y, mimage, martdesc, pid, category, product, count, morecount, isadd, price, originalprice, description, image, sday, eday, selectcount, create_date ";
			sql += " from cart where email = '"+parameter.getEmail()+"' order by create_date desc";
			arr = this.database.getRSet(sql);
			if (arr == null || arr.length==0) {
				this.disconnect();
				return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"등록된 상품이 없습니다.\"}"));
			}
		} else if (type.equals("ISCONFIRM")){
			sql  = " select id, mid, name, telephone, mobile, addr1, addr2, addr3, addr4, addr5, x, y, mimage, martdesc, pid, category, product, count, morecount, isadd, price, originalprice, description, image, sday, eday, selectcount, create_date ";
			sql += " from cart where email = '"+parameter.getEmail()+"' and isconfirm=1 order by create_date desc";
			arr = this.database.getRSet(sql);
			if (arr == null || arr.length==0) {
				this.disconnect();
				return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"주문 상품이 없습니다.\"}"));
			}
		}
		this.disconnect();

		int totalAmount = 0;
		if (arr.length>0) {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"resultCode\":\"1\", \"cartedCount\":\""+Integer.toString(arr.length)+"\",\"cart\":[");
			for (int i=0; i<arr.length; i++) {
				sb.append("{");
		    	sb.append("\"id\":\"").append(arr[i][0]).append("\"");
		    	sb.append(",\"mid\":\"").append(arr[i][1]).append("\"");
		    	sb.append(",\"name\":\"").append(arr[i][2]).append("\"");
		    	sb.append(",\"telephone\":\"").append(arr[i][3]).append("\"");
		    	sb.append(",\"mobile\":\"").append(arr[i][4]).append("\"");
		    	sb.append(",\"addr1\":\"").append(arr[i][5]).append("\"");    	
		    	sb.append(",\"addr2\":\"").append(arr[i][6]).append("\"");
		    	sb.append(",\"addr3\":\"").append(arr[i][7]).append("\"");
		    	sb.append(",\"addr4\":\"").append(arr[i][8]).append("\"");
		    	sb.append(",\"addr5\":\"").append(arr[i][9]).append("\"");
		    	sb.append(",\"x\":\"").append(arr[i][10]).append("\"");
		    	sb.append(",\"y\":\"").append(arr[i][11]).append("\"");
		    	sb.append(",\"mimage\":\"").append(arr[i][12]).append("\"");
		    	sb.append(",\"martdesc\":\"").append(arr[i][13]).append("\"");
		    	sb.append(",\"pid\":\"").append(arr[i][14]).append("\"");
		    	sb.append(",\"category\":\"").append(arr[i][15]).append("\"");
		    	sb.append(",\"product\":\"").append(arr[i][16]).append("\"");
		    	sb.append(",\"count\":\"").append(arr[i][17]).append("\"");
		    	sb.append(",\"morecount\":\"").append(arr[i][18]).append("\"");
		    	sb.append(",\"isadd\":\"").append(arr[i][19]).append("\"");
		    	sb.append(",\"price\":\"").append(arr[i][20]).append("\"");
		    	sb.append(",\"originalprice\":\"").append(arr[i][21]).append("\"");
		    	sb.append(",\"description\":\"").append(arr[i][22]).append("\"");
		    	sb.append(",\"image\":\"").append(arr[i][23]).append("\"");
		    	sb.append(",\"sday\":\"").append(arr[i][24]).append("\"");
		    	sb.append(",\"eday\":\"").append(arr[i][25]).append("\"");
		    	sb.append(",\"selectcount\":\"").append(arr[i][26]).append("\"");
		    	sb.append(",\"create_date\":\"").append(arr[i][27]).append("\"");
		    	sb.append("}");
		    	if (i<arr.length-1) {
		    		sb.append(",");
		    	}
		    	totalAmount += (Integer.parseInt(arr[i][20])*Integer.parseInt(arr[i][26]));
			}
	    	sb.append("],");
	    	sb.append("\"totalAmount\":").append(Integer.toString(totalAmount));
	    	sb.append("}");
			return JSONObject.fromObject(JSONSerializer.toJSON(sb.toString()));							
		} else {
			return new JSONObject();
		}
	}
	
	public JSONObject removeCart(MartParameter parameter) throws Exception {
		this.connectToMartDB();
		String email = parameter.getEmail();		
		String sql  = " delete from cart where email = '"+email+"' and ";
		String[] arr = parameter.getPid().split(";");
		sql += "(";
		for (int i=0; i<arr.length; i++) {
			sql += " pid="+arr[i];
			if (i<arr.length-1) {
				sql += " OR ";
			}
		}
		sql += ")";
		this.database.delete(sql, new Vector<String>());
		this.database.delete("commit", new Vector<String>());
		this.disconnect();
		
		return this.selectCart(parameter, "ALL");
	}

	public JSONObject updateSelectCount(MartParameter parameter) throws Exception {
		this.connectToMartDB();
		String email = parameter.getEmail();
		String sql  = " update cart set selectcount = "+parameter.getSelectCount()+" where email = '"+email+"' and pid="+parameter.getPid();
		this.database.update(sql, new Vector<String>());
		this.database.update("commit", new Vector<String>());
		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"msg\":\"정상적으로 변경되었습니다.\"}"));
	}

	public JSONObject orderCart(MartParameter parameter) throws Exception {
		this.connectToMartDB();
		String email = parameter.getEmail();		
		String sql  = " update cart set isconfirm = 1 where email = '"+email+"' and ";
		String[] arr = parameter.getPid().split(";");
		sql += "(";
		for (int i=0; i<arr.length; i++) {
			sql += " pid="+arr[i];
			if (i<arr.length-1) {
				sql += " OR ";
			}
		}
		sql += ")";
		this.database.update(sql, new Vector<String>());
		this.database.update("commit", new Vector<String>());
		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"msg\":\"정상적으로 변경되었습니다.\"}"));
	}
	
	public JSONObject cancelCart(MartParameter parameter) throws Exception {
		this.connectToMartDB();
		String email = parameter.getEmail();		
		String sql  = " update cart set isconfirm = 0 where email = '"+email+"' and ";
		String[] arr = parameter.getPid().split(";");
		sql += "(";
		for (int i=0; i<arr.length; i++) {
			sql += " pid="+arr[i];
			if (i<arr.length-1) {
				sql += " OR ";
			}
		}
		sql += ")";
		this.database.update(sql, new Vector<String>());
		this.database.update("commit", new Vector<String>());
		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"msg\":\"정상적으로 변경되었습니다.\"}"));
	}
	
	public JSONObject getConfirmCart(MartParameter parameter) throws Exception {
		return this.selectCart(parameter, "ISCONFIRM");
	}
	
	
	public JSONObject setInitInfo(MartParameter parameter) throws Exception {
		String uuid = parameter.getData1();
		String model = parameter.getData2();		
		String platform = parameter.getData3();
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		this.connectToMartDB();
		String sql  = " select cnt from logs where uuid=\""+uuid+"\" and model=\""+model+"\" and platform=\""+platform+"\" and date=\""+format.format(now)+"\"";
		String[][] arr = this.database.getRSet(sql);
		if (arr == null || arr.length==0 || Integer.parseInt(arr[0][0])==0) {
			sql  = " insert into logs(uuid, model, platform, date, cnt) values('"+uuid+"','"+model+"','"+platform+"','"+format.format(now)+"', 1)";
			this.database.insert(sql, new Vector<String>(), false);
			this.database.insert("commit", new Vector<String>());
		} else {
			int count = Integer.parseInt(arr[0][0]);
			sql  = " update logs set cnt = "+Integer.toString(count+1) +" where uuid='"+uuid+"' and model='"+model+"' and platform='"+platform+"' and date='"+format.format(now)+"'";
			this.database.update(sql, new Vector<String>(), false);
			this.database.update("commit", new Vector<String>());
		}
		this.disconnect();

		String email = parameter.getEmail();
		if (!email.equals("")) {
			this.connectToXE();		
			sql  = " select count(*) from xe_member where uuid='"+uuid+"' and model='"+model+"' and platform='"+platform+"' and date='"+format.format(now)+"'";
			if (arr != null && Integer.parseInt(arr[0][0])>0) {
				sql  = " update xe_member set uuid='"+uuid+"', model='"+model+"', platform='"+platform+"' where email_address='"+email+"'";
				this.database.update(sql, new Vector<String>(), false);
				this.database.update("commit", new Vector<String>());
			}
			this.disconnect();
		}
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"msg\":\"정상적으로 변경되었습니다.\"}"));
	}
	
	// mid와 pid를 이용해서 상품 정보를 가져온다.	
	public JSONArray getProductInfo(MartParameter parameter) throws Exception {
		String email = parameter.getEmail();
		String mid = parameter.getMid();	
		String pid = parameter.getPid();
		
		return this.getSaleProductData(email, mid, pid);
	}
	
	// 상품을 수정한다.
	public JSONObject modifyProduct(MartParameter parameter) throws Exception {
		String email = parameter.getEmail();
		String mid = parameter.getMid();	
		String pid = parameter.getPid();
		
		if (email.trim().equals("") || pid.trim().equals("")) return new JSONObject();
		mid = this.getMid(email);
		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
		String sql = "update sale set product='"+parameter.getProduct()+"', display="+parameter.getDisplay()+", count="+parameter.getCount();
		sql += ", morecount="+parameter.getMoreCount()+", price="+parameter.getPrice()+", conditionalprice="+parameter.getConditionalPrice();
		sql += ", originalprice="+parameter.getOriginalPrice()+", description='"+parameter.getDesc()+"', sday='"+parameter.getSDate();
		sql +="', eday='"+parameter.getEDate()+"', update_date='"+format.format(now)+"' where mid="+mid+" and id="+pid;

		this.connectToMartDB();
		this.database.update(sql, new Vector<String>(), false);
		this.database.insert("commit", new Vector<String>());
		this.disconnect();
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"msg\":\"정상적으로 변경되었습니다.\"}"));
	}	

	
	// 앱 접속시 gcm 및 user agreemenet를 체크한다.
	public JSONObject checkGCM(MartParameter parameter) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String gcm = parameter.getGCM();
		String mid = parameter.getMid();
		String ua = parameter.getUserAgreement();	
		String la = parameter.getLocationAgreement();
		String manager = parameter.getManager();
		String msgID="", msg="", id="0";

		if (gcm.trim().equals("")) {
			msgID = "0";
			msg = "gcm코드가 비었습니다."; 
		}
		
		this.connectToMartDB();
		String sql = "select id, mid, ua, la, manager from user where gcm='"+gcm+"'";
		String[][] arr = this.database.getRSet(sql);
		if (arr!=null && arr.length>0) {
			boolean wasUpdate = false;
			if (!ua.trim().equals("")) {
				sql = "update user set ua="+ua+" where gcm='"+gcm+"'";
				this.database.update(sql, new Vector<String>(), false);
				msgID = "1"; msg += " 이용약관동의";
			} 
			if (!la.trim().equals("")) {
				sql = "update user set la="+la+ " where gcm='"+gcm+"'";
				this.database.update(sql, new Vector<String>(), false);
				msgID = "1"; msg += " 사용자정보이용동의";
			}
			if (!manager.trim().equals("")) {
				sql = "update user set manager="+manager+ " where gcm='"+gcm+"'";
				this.database.update(sql, new Vector<String>(), false);
				msgID = "1"; msg += " 점장처리";
			}
			
			if (wasUpdate) {
				this.database.insert("commit", new Vector<String>());
			}
		} else {
			if (!ua.trim().equals("") && !la.trim().equals("")) {
				sql = "insert into user (gcm, mid, ua, la) values ('"+gcm+"', "+mid+", "+ua+", "+la+")";
				this.database.insert(sql, new Vector<String>(), false);
				msgID = "1"; msg += " 이용약관동의 사용자정보이용동의";
				if (!manager.trim().equals("")) {
					sql = "update user set manager="+manager+ " where gcm='"+gcm+"'";
					this.database.update(sql, new Vector<String>(), false);
					msgID = "1"; msg += " 점창처리";
				}
				this.database.insert("commit", new Vector<String>());				
			} else {
				sql = "insert into user (gcm, mid) values ('"+gcm+"', "+mid+")";
				this.database.insert(sql, new Vector<String>(), false);
				msgID = "1"; msg += " GCM등록";
				if (!manager.trim().equals("")) {
					sql = "update user set manager="+manager+ " where gcm='"+gcm+"'";
					this.database.update(sql, new Vector<String>(), false);
					msgID = "1"; msg += " 점창처리";
				}
				this.database.insert("commit", new Vector<String>());
			}
		}
		sql = "select id, mid, ua, la, manager from user where gcm='"+gcm+"'";
		arr = this.database.getRSet(sql);
		if (arr!=null && arr.length>0) {
			id = arr[0][0];
			mid = arr[0][1];
			ua = arr[0][2];
			la = arr[0][3];
			manager = arr[0][4];
		}
		this.disconnect();
		
		jsonObject = JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\""+msgID+"\", \"msg\":\""+mid+"\", \"msg\":\""+mid+"\", \"id\":\""+id+"\", \"ua\":\""+ua+"\", \"la\":\""+la+"\", \"manager\":\""+manager+"\"}"));
		return jsonObject;
	}


	// 오늘의 세일 상품 list를 가지고 온다.
	public JSONObject registerTodaySale(MartParameter parameter) throws Exception {
		String sdate = parameter.getSDate();
		String mid = "10488";
		String content = parameter.getTodaySale();
		String msg = "";
		
		if (sdate.trim().equals("") || mid.trim().equals("")) {
			return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"오류입니다.\"}"));
		}
		
		Date now = new Date();
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String today = format2.format(now);
		String currenttime = format3.format(now);
		
		this.connectToXE();
		//String sql = "select max(document_srl) from xe_documents where module_srl=867 and substring(regdate, 1, 8) ='"+today+"'";
		String sql = "select max(document_srl) from xe_documents where module_srl=867";
		String[][] arr = this.database.getRSet(sql);
		int maxid = Integer.parseInt(arr[0][0]) + 1;
		
		sql = "insert into xe_documents (document_srl, module_srl, category_srl, lang_code, title, title_color, content, user_id, user_name, nick_name, member_srl, email_address, extra_vars, regdate, last_update, ipaddress, list_order, update_order) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Vector vector = new Vector();
		vector.add(maxid);
		vector.add(867);
		vector.add(0);
		vector.add("ko");
		vector.add(today+" 세일 알림");
		vector.add("N");
		vector.add(content);
		vector.add("martn");
		vector.add("admin");
		vector.add("용인수지홈마트");
		vector.add(4);
		vector.add("dsyoon@ncue.net");
		vector.add("N;");
		vector.add(currenttime);
		vector.add(currenttime);
		vector.add("112.153.174.166");
		vector.add(maxid);
		vector.add(maxid);

		this.database.insert(sql, vector, false);
		this.database.insert("commit", new Vector<String>());
	
		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"id\":\""+Integer.toString(maxid)+"\"}"));
	}
}