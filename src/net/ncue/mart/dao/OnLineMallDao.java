package net.ncue.mart.dao;

/**
 * @author: DosangYoon
 * @version: 0.1
 * @date    01/01/2017
 * @brief:
 */
 
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import net.htmlparser.jericho.Source;
import net.ncue.mart.util.MartParameter;
import net.ncue.conf.Configure;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class OnLineMallDao {
	public JSONObject getOnLineMall(MartParameter parameter) throws IOException {
		try{
			String url = Configure.ONLINEMALL_URL+"q="+URLEncoder.encode(parameter.getQ(), "UTF-8");
			url += "&wt=json&indent=true&start="+parameter.getStart()+"&rows="+parameter.getRow();
			if (!parameter.getSort().equals("")) url += "&sort="+URLEncoder.encode(parameter.getSort(), "UTF-8");


			Source source = new Source(new URL(url));
			return JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
}
 