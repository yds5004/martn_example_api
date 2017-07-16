package net.ncue.mart.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.htmlparser.jericho.Source;
import net.ncue.conf.Configure;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class KMAKwd {
	public String analyzeProduct(String product) throws Exception {
		URL oURL = new URL(Configure.POS_TAGGER_URL+"&tag=N&qry="+URLEncoder.encode(product, "UTF-8"));
		HttpURLConnection connection = (HttpURLConnection) oURL.openConnection();
		connection.setConnectTimeout(35000);
		connection.setReadTimeout(5000);
		HttpURLConnection.setFollowRedirects(true);
		int responseCode = connection.getResponseCode();
		if (responseCode != 200) return null;

		Source source = new Source(oURL);
		JSONObject object = JSONObject.fromObject(JSONSerializer.toJSON(source.toString()));
		return object.getString("N");
	}
}
