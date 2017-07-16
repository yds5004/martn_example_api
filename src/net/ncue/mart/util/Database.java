package net.ncue.mart.util;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;

public class Database{
	private boolean isPrintLog = false;
    private Connection conn = null; 
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private ResultSetMetaData meta = null;
    private CallableStatement cstmt = null;
    private Context env = null;
    private DataSource source = null;
    private String dbName = "";
    private Boolean utf8_check = false;

    public Database() {
    }

    public Database(String host, int port, String database, String ID, String password, String bending) throws SQLException {
    	this.connect(host, port, database, ID, password, bending);
    }
    
    public Database(String host, int port, String database, String ID, String password, boolean utf8_check) throws SQLException {
    	this.utf8_check = utf8_check;
    	this.connect(host, port, database, ID, password);
    }
    
    public void connect (String dir, String tns, String id, String password) {
    	if (dir.equals("")||tns.equals("")) return;

    	System.setProperty("oracle.net.tns_admin", dir);
		if (this.conn!=null) this.close();
		try {
	    	Class.forName("oracle.jdbc.driver.OracleDriver");
    		this.conn = DriverManager.getConnection( "jdbc:oracle:thin:@" + tns, id, password );
	     } catch( SQLException e) {
	    	 e.printStackTrace();
	     } catch(Exception e) {
	    	 e.printStackTrace();
	     }
    }
    public void connect(String host, int port, String database, String ID, String password) throws SQLException {
    	this.connect(host, port, database, ID, password, "mysql");
    }
	public void connect(String host, int port, String database, String ID, String password, String bending) throws SQLException {
		bending = bending.trim().toLowerCase();
		if (bending.equals("oracle")) {
			if (host.equals("")&&database.equals("")&&ID.equals("")&&password.equals("")) return;
			if (this.conn!=null) this.close();
			try {
				this.dbName = database;
		    	Class.forName("oracle.jdbc.driver.OracleDriver");
		    	if (this.utf8_check) {
		    		this.conn = DriverManager.getConnection( "jdbc:oracle:thin:@" + host + ":" + port +"/" + database, ID, password );
		    	} else {
		    		this.conn = DriverManager.getConnection( "jdbc:oracle:thin:@" + host + ":" + port +"/" + database, ID, password );
		    	}
		     } catch( SQLException e) {
		    	 e.printStackTrace();
		     } catch(Exception e) {
		    	 e.printStackTrace();
		     }
		} else {
			if (host.equals("")&&database.equals("")&&ID.equals("")&&password.equals("")) return;
			if (this.conn!=null) this.close();
			try {
				this.dbName = database;
		    	Class.forName("com.mysql.jdbc.Driver");
		    	if (this.utf8_check) {
		    		this.conn = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port +"/" + database + "?useUnicode=true&characterSetResults=UTF-8&characterEncoding=utf-8", ID, password );
		    	} else {
		    		this.conn = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port +"/" + database, ID, password );
		    	}
		     } catch( SQLException e) {
		    	 e.printStackTrace();
		     } catch(Exception e) {
		    	 e.printStackTrace();
		     }
		}
	}


    public void close(){
        try{
            if(cstmt!=null) cstmt.close();
            if(ps!=null)	ps.close();
            if(conn!=null)	conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /************************
     ****** Debugging
     ************************/
    public String addParam(String query, Vector input){
        String tmpStr;
        Object o;
        int index=-1, count=0;
        while(true) {
            index = query.indexOf("?", index+1);
            if(index==-1) break;
            else count++;
        }
        if(count == input.size()) {
            index=-1;
            count=0;
            while(true) {
                index = query.indexOf("?", index+1);
                if(index==-1) break;
                else {
                    tmpStr 	= query.substring(0, index);
                    o = input.elementAt(count);
                    if(o instanceof String) tmpStr += "'" + o.toString() + "'";
                    else tmpStr += o.toString();
                    tmpStr += query.substring(index+1);
                    query 	= tmpStr;
                    count++;
                }
            }
        }

        return query;
    }

    public void print(String query, Vector input) {
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(addParam(query, input));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }


    public String[][] getRSet(String query){
        return getRSet(query, new Vector());
    }
    public String[][] getRSet(String query, Vector param){
        String[][] result = null;
        Vector row = new Vector();
        String[] column;
        int i, pSize, mSize, rows;
        String type;
        try{
            ps = conn.prepareStatement(query);
            Object o;
            pSize=param.size();
            for(i=0; i<pSize; i++) {
                o = param.elementAt(i);
                if(o instanceof String) ps.setString(i+1, o.toString());
                else ps.setInt(i+1, Integer.valueOf(o.toString()).intValue());
            }
            rs = ps.executeQuery();
            meta = rs.getMetaData();
            mSize = meta.getColumnCount();
            int count=0;
            while(rs.next()) {
                count++;
                column = new String[mSize];
                for(i=0; i<mSize; i++) {
                    type = meta.getColumnTypeName(i+1);
                    type = type.toUpperCase();
                    if(type.equals("VARCHAR2") || type.equals("VARCHAR") || type.equals("CHAR")||type.equals("VARBINARY")||type.equals("BLOB")) {
                        column[i] = rs.getString(i+1);
                        if (column[i]==null || column[i].equals("") || column[i].equals("null")) column[i] = "";
                        if (this.utf8_check) {
	                        byte[] bytes = rs.getBytes(i+1);
	                        if (this.utf8_check) {
	                        	column[i] = new String(rs.getBytes(i+1), "UTF-8");
	                        } else {
		                        if (bytes != null) {
		                            column[i] = new String(rs.getBytes(i+1), "euc-kr");
		                        } else {
		                            column[i] = null;
		                        }
	                        }
                        }
                    }
                    else if(type.equals("NUMBER"))
                        column[i] = ""+rs.getInt(i+1);
                    else if((type.indexOf("LONG")>=0)||(type.indexOf("INT")>=0))
                        column[i] = ""+rs.getLong(i+1);
                    else if(type.equals("DOUBLE")||(type.indexOf("DECIMAL")>=0))
                        column[i] = ""+rs.getDouble(i+1);
                    else if(type.equals("FLOAT")) 
                        column[i] = ""+rs.getFloat(i+1);
                    else if(type.equals("TIMESTAMP"))
                    	column[i] = ""+rs.getTimestamp(i+1);
                    else if(type.equals("DATETIME"))
                    	column[i] = ""+rs.getDate(i+1)+" "+rs.getTime(i+1);
                    else if(type.equals("DATE"))
                    	column[i] = ""+rs.getDate(i+1);
                    else
                        column[i] = "EMPTY";
                }
                row.add(column);
            }
            rows = row.size();
            if(rows>0) {
                result = new String[rows][];
                for(i=0; i<rows; i++) result[i] = (String[]) row.elementAt(i);
            }
            ps.close();
        }catch(Exception e){
            e.printStackTrace(System.out);
            // e.printStackTrace();
        }
        return result;
    }


    /************************
     ****** Insert
     ************************/
    public int insert(String query, Vector param) {
        return insert(query, param, false);
    }
    public int insert(String query, Vector param, boolean isPrint){
        int retval =0;
        int pSize;
        try{
            if(isPrint) print(query, param);
            ps = this.conn.prepareStatement(query);
            Object o;
            pSize=param.size();
            for(int i=0;i<pSize;i++){
                o = param.elementAt(i);
                if(o instanceof String) ps.setString(i+1,o.toString());
                else if(o instanceof Float) ps.setFloat(i+1, Float.valueOf(o.toString()).floatValue());
                else if(o instanceof Double) ps.setDouble(i+1, Double.valueOf(o.toString()).doubleValue());
                else ps.setInt(i+1, Integer.valueOf(o.toString()).intValue());
            }
            retval = ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
        return retval;
    }


    /************************
     ****** Update
     ************************/
    public int update(String query, Vector param) {
        return update(query, param, -1, false);
    }
    public int update(String query, Vector param, int index) {
        return update(query, param, index, false);
    }
    public int update(String query, Vector param, boolean isPrint) {
        return update(query, param, -1, isPrint);
    }
    public int update(String query, Vector param, int index, boolean isPrint) {
        int result=0, pSize;
        try{
            ps = this.conn.prepareStatement(query);
            Object o;
            pSize=param.size();

            if(index==-1){
                if(isPrint) print(query, param);
                for(int i=0; i<pSize; i++){
                    o = param.elementAt(i);
                    if(o instanceof String) ps.setString(i+1,o.toString());
                    else if(o instanceof Float) ps.setFloat(i+1, Float.valueOf(o.toString()).floatValue());
                    else if(o instanceof Double) ps.setDouble(i+1, Double.valueOf(o.toString()).doubleValue());
                    else ps.setInt(i+1, Integer.valueOf(o.toString()).intValue());
                }
                result = ps.executeUpdate();
            }else{
            	this.conn.setAutoCommit(false);
                int count=0;
                int[] tmpInt;
                String[] subParam = (String[]) param.elementAt(index);
                for(int i=0; i<subParam.length; i++) {
                    count++;
                    for(int j=0; j<pSize; j++){
                        if(j==index) {
                            o = (Object) subParam[i];
                        }
                        else
                            o = param.elementAt(j);
                        
                        if(o instanceof String) ps.setString(i+1,o.toString());
                        else if(o instanceof Float) ps.setFloat(i+1, Float.valueOf(o.toString()).floatValue());
                        else if(o instanceof Double) ps.setDouble(i+1, Double.valueOf(o.toString()).doubleValue());
                        else ps.setInt(i+1, Integer.valueOf(o.toString()).intValue());
                    }
                    ps.addBatch();

                    if(count%100==0) {
                        tmpInt=ps.executeBatch();
                        for(int k=0; k<tmpInt.length; k++) {
                            result += tmpInt[k];
                        }
                    }
                }

                if(count%100!=0) {
                    tmpInt=ps.executeBatch();
                    for(int k=0; k<tmpInt.length; k++) {
                        result += tmpInt[k];
                    }
                }

                this.conn.commit();
                this.conn.setAutoCommit(true);

            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
        return result;
    }


    public int update(String query, String[][] subParam) {
        int result=0, pSize;
        try{
            ps = conn.prepareStatement(query);

            conn.setAutoCommit(false);

            Object o;
            int count=0;
            int[] tmpInt;
            for(int i=0; i<subParam.length; i++) {
                count++;

                for(int j=0; j<subParam[i].length; j++) {
                    o = (Object) subParam[i][j];
                    if(o instanceof String) ps.setString(j+1,Encode.toH(o.toString()));
                    else if(o instanceof Float) ps.setFloat(j+1, Float.valueOf(o.toString()).floatValue());
                    else if(o instanceof Double) ps.setDouble(j+1, Double.valueOf(o.toString()).doubleValue());
                    else ps.setInt(j+1, Integer.valueOf(o.toString()).intValue());
                    
                }

                ps.addBatch();
                if(count%100==0) {
                    tmpInt=ps.executeBatch();
                    for(int k=0; k<tmpInt.length; k++) result += tmpInt[k];
                }
            }

            if(count%100!=0) {
                tmpInt=ps.executeBatch();
                for(int k=0; k<tmpInt.length; k++) result += tmpInt[k];
            }

            conn.commit();
            conn.setAutoCommit(true);
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
        return result;
    }

    /************************
     ****** Delete
     ************************/
    public int delete(String query, Vector param) {
        return delete(query, param, false);
    }
    public int delete(String query, Vector param, boolean isPrint){
        int retval =0;
        int pSize;
        try{
            if(isPrint) print(query, param);
            ps = this.conn.prepareStatement(query);
            Object o;
            pSize=param.size();
            for(int i=0;i<pSize;i++){
                o = param.elementAt(i);
                if(o instanceof String) ps.setString(i+1,o.toString());
                else if(o instanceof Float) ps.setFloat(i+1, Float.valueOf(o.toString()).floatValue());
                else if(o instanceof Double) ps.setDouble(i+1, Double.valueOf(o.toString()).doubleValue());
                else ps.setInt(i+1, Integer.valueOf(o.toString()).intValue());
            }
            retval = ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
        return retval;
    }
    
    /************************
     ****** Procedure Call
     ************************/
    public void callProc(String query, Vector setParam, boolean isPrint){
        int retval =0;
        try{
            if(isPrint) print(query, setParam);
            cstmt = conn.prepareCall("{"+query+"}");
            Object o;
            for(int i=0; i<setParam.size(); i++){
                o = setParam.elementAt(i);
                if(o instanceof String) cstmt.setString(i+1,o.toString());
                else if(o instanceof Float) cstmt.setFloat(i+1, Float.valueOf(o.toString()).floatValue());
                else if(o instanceof Double) cstmt.setDouble(i+1, Double.valueOf(o.toString()).doubleValue());
                else cstmt.setInt(i+1, Integer.valueOf(o.toString()).intValue());
            }

            cstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
