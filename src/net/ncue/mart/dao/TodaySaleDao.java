package net.ncue.mart.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import net.ncue.conf.Configure;
import net.ncue.mart.util.Database;
import net.ncue.mart.util.MartParameter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import sun.net.www.protocol.http.HttpURLConnection;

public class TodaySaleDao {
	Database database = null;

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
    public void disconnect() {
    	if (this.database != null) {
    		this.database.close();
    		this.database=null;
    	}
    }
    
    public int insertTodaySale(String mid, String content) throws Exception {
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
		
		sql = "insert into xe_documents (document_srl, module_srl, category_srl, lang_code, title, title_color, content, user_id, user_name, nick_name, homepage, member_srl, email_address, extra_vars, regdate, last_update, ipaddress, list_order, update_order) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Vector vector = new Vector();
		vector.add(maxid);
		vector.add(867);
		vector.add(0);
		vector.add("ko");
		vector.add(today+" 세일 알림");
		vector.add("N");
		vector.add(content.replaceAll("\n", "<br />"));
		vector.add("martn");
		vector.add("admin");
		vector.add("용인수지홈마트");
		vector.add("");
		vector.add(4);
		vector.add("dsyoon@ncue.net");
		vector.add("N;");
		vector.add(currenttime);
		vector.add(currenttime);
		vector.add("112.153.174.166");
		vector.add(-1*maxid);
		vector.add(-1*maxid);

		this.database.insert(sql, vector, false);
		this.database.insert("commit", new Vector<String>());
	
		this.disconnect();
		return maxid;
    }
    
	public JSONObject push(MartParameter parameter) throws Exception {
		String email = parameter.getEmail();
		String content = URLDecoder.decode(parameter.getTodaySale(), "UTF-8");
		String mid = this.getMid(email);
		
		// DB에 내용을 저장함
		int docid = this.insertTodaySale(mid, content);
		
		// 사용자 gcm 코드를 얻어와서 메시지 발송
		this.connectToMartDB();
		String sql = "select id, gcm from user where mid="+mid+" and ua=1 and la=1";
		String[][] arr = this.database.getRSet(sql);
		
		for (int i=0; i<arr.length; i++) {
			JSONObject object = this.getData(arr[i][1], content, docid);
			this.pushMsg(object);
		}

		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"count\":\""+Integer.toString(arr.length)+"\", \"msg\":\"메시지가 성공적으로 발송되었습니다..\"}"));
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
	
	public JSONObject getData(String key, String content, int docid) {
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "마트");
        jsonObject.put("contents", content);
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("image_urls", jsonArray);
        jsonObject.put("link_url", "http://localhost/sale/"+Integer.toString(docid));

        JSONObject json = new JSONObject();
        json.put("key", key);
        json.put("obj", jsonObject);
        return json;
	}
	
	public void pushMsg(JSONObject object) throws IOException {
		URL pushURL = new URL("http://222.122.81.128:19051/send/");
        HttpURLConnection connection = (HttpURLConnection) pushURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
		connection.connect();
        
		OutputStreamWriter connectionOuput = new OutputStreamWriter(connection.getOutputStream());
		
        // 전송 메시지 확인
        System.out.println(object.toString());
        connectionOuput.write(object.toString());
        connectionOuput.flush();
        
    	// 전송 메시지 확인
        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while((line = connectionInput.readLine())!=null) {
        	 System.out.println(line);
        }
        connectionInput.close();
        connectionOuput.close();
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
				sql = "insert into user (mid, gcm, ua, la) values ("+mid+", '"+gcm+"', "+ua+", "+la+")";
				this.database.insert(sql, new Vector<String>(), false);
				msgID = "1"; msg += " 이용약관동의 사용자정보이용동의";
				if (!manager.trim().equals("")) {
					sql = "update user set manager="+manager+ " where gcm='"+gcm+"'";
					this.database.update(sql, new Vector<String>(), false);
					msgID = "1"; msg += " 점창처리";
				}
				this.database.insert("commit", new Vector<String>());				
			} else {
				sql = "insert into user (mid, gcm) values ("+mid+", '"+gcm+"')";
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
		
		jsonObject = JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\""+msgID+"\", \"msg\":\""+msg+"\", \"id\":\""+id+"\", \"ua\":\""+ua+"\", \"la\":\""+la+"\", \"manager\":\""+manager+"\"}"));
		return jsonObject;
	}
	// 사용자 list를 가져온다.
	public JSONObject getGCMList(MartParameter parameter) throws Exception {
		String sql = "select id, mid, gcm, ua, la, manager, create_date from user";
		this.connectToMartDB();
		String[][] arr = this.database.getRSet(sql);
		this.disconnect();
		if (arr!=null && arr.length>0) {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"resultCode\":\"1\", \"count\":\""+Integer.toString(arr.length)+"\",\"user\":[");
			for (int i=0; i<arr.length; i++) {
				sb.append("{");
		    	sb.append("\"id\":\"").append(arr[i][0]).append("\"");
		    	sb.append(",\"mid\":\"").append(arr[i][1]).append("\"");
		    	sb.append(",\"gcm\":\"").append(arr[i][2]).append("\"");
		    	sb.append(",\"ua\":\"").append(arr[i][3]).append("\"");
		    	sb.append(",\"la\":\"").append(arr[i][4]).append("\"");
		    	sb.append(",\"manager\":\"").append(arr[i][5]).append("\"");
		    	sb.append(",\"create_date\":\"").append(arr[i][6]).append("\"");
		    	sb.append("}");
		    	if (i<arr.length-1) {
		    		sb.append(",");
		    	}
			}
	    	sb.append("]");
	    	sb.append("}");
			return JSONObject.fromObject(JSONSerializer.toJSON(sb.toString()));							
		} else {
			return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"사용자가 없습니다..\"}"));
		}
	}
	// 오늘의 세일 상품 list를 가지고 온다.
	public JSONObject getTodaySale(MartParameter parameter) throws Exception {
		String sdate = parameter.getSDate();
		String mid = "10488";
		String id = "", content = "";
		if (sdate.trim().equals("") || mid.trim().equals("")) {
			return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"서버 오류\"}"));
		}
		
		String sql = "select content from xe_documents where module_srl=867 and substring(regdate,1,8)='"+sdate+"'";
		this.connectToXE();
		String[][] arr = this.database.getRSet(sql);
		this.disconnect();
		if (arr!=null && arr.length>0) {
			content = URLEncoder.encode(arr[0][0].replaceAll("<br />", "\n"), "UTF-8");
			return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"date\":\""+ sdate.substring(0, 4)+"-"+sdate.substring(4, 6)+"-"+sdate.substring(6, 8) +"\", \"content\":\""+content+"\"}"));						
		} else {
			return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"0\", \"msg\":\"오늘은 세일 상품은 중비 중입니다..\"}"));
		}
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
		
		String sql = "select id, mid, content from todaysale where mid="+mid+" and sdate='"+sdate+"'";

		this.connectToMartDB();
		String[][] arr = this.database.getRSet(sql);
		if (arr!=null && arr.length>0) {			
			sql = "update content set content='"+content+"' where id="+arr[0][0];
			this.database.update(sql, new Vector<String>(), false);
			msg = "업데이트되었습니다.";
		} else {
			sql = "insert todaysale (mid, content, sdate) values ("+mid+",'"+content+"','"+sdate+"')";
			this.database.insert(sql, new Vector<String>(), false);
			msg = "추가되었습니다.";
		}
		this.database.insert("commit", new Vector<String>(), false);
		this.disconnect();
		
		return JSONObject.fromObject(JSONSerializer.toJSON("{\"resultCode\":\"1\", \"msg\":\""+msg+"\"}"));
	}
}
