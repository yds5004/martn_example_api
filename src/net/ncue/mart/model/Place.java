package net.ncue.mart.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Place {
	private String url="";
	private String userid="";
	private String datetime="";
	private String title="";
	private String content="";
	private List<String> tag= new ArrayList<String>();
	private List<String> localtag = new ArrayList<String>();
	private List<String> image = new ArrayList<String>();
	private String place="";
	private String location="";
	private String source="";
	private String createdate="";
	
	public Place(JSONObject jsonObject) throws JSONException {
		parse (jsonObject);
	}
	
	public void parse(JSONObject jsonObject) throws JSONException {
		this.setUrl((String)jsonObject.getString("url"));
		this.setUserid((String)jsonObject.getString("userid"));
		this.setDatetime((String)jsonObject.getString("datetime"));
		this.setTitle((String)jsonObject.getString("title"));
		this.setContent((String)jsonObject.getString("content"));
		this.setTag((String)jsonObject.getString("tag"));
		this.setLocaltag((String)jsonObject.getString("localtag"));
		this.setImage((String)jsonObject.getString("image"));
		this.setPlace((String)jsonObject.getString("place"));
		this.setLocation((String)jsonObject.getString("location"));
		this.setSource((String)jsonObject.getString("source"));
		this.setCreatedate((String)jsonObject.getString("createdate"));
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(url).append("\t");
		sb.append(userid).append("\t");
		sb.append(datetime).append("\t");
		sb.append(title).append("\t");
		sb.append(content).append("\t");
		sb.append(tag.toString()).append("\t");
		sb.append(localtag.toString()).append("\t");
		sb.append(image.toString()).append("\t");
		sb.append(place).append("\t");
		sb.append(location).append("\t");
		sb.append(source).append("\t");
		sb.append(createdate).append("\n");
		return sb.toString();
	}
	
	public String getUrl() {
		return this.url;
	}
	public String getUserid() {
		return this.userid;
	}
	public String getDatetime() {
		return this.datetime;
	}
	public String getTitle() {
		return this.title;
	}
	public String getContent() {
		return this.content;
	}
	public List<String> getTag() {
		return this.tag;
	}
	public List<String> getLocaltag() {
		return this.localtag;
	}
	public List<String> getImage() {
		return this.image;
	}
	public String getPlace() {
		return this.place;
	}
	public String getLocation() {
		return this.location;
	}
	public String getSource() {
		return this.source;
	}
	public String getCreatedate() {
		return this.createdate;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public void setDatetime(String datetime) {
		if (!datetime.equals("")) {
			datetime = datetime.replaceAll("T", " ").replaceAll("Z", "");
			this.datetime = datetime;
		}
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setTag(String tag) {
		if (!tag.equals("")) {
			String[] arr = tag.split(",");
			for (int i=0; i<arr.length; i++)
				this.tag.add(arr[i]);
		}
	}
	public void setLocaltag(String localtag) {
		if (!localtag.equals("")) {
			String[] arr = localtag.split(",");
			for (int i=0; i<arr.length; i++)
				this.localtag.add(arr[i]);
		}
	}
	public void setImage(String image) {
		if (!image.equals("")) {
			String[] arr = image.split(",");
			for (int i=0; i<arr.length; i++)
				this.image.add(arr[i]);
		}		
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setCreatedate(String createdate) {
		if (!createdate.equals("")) {
			createdate = createdate.replaceAll("T", " ").replaceAll("Z", "");
			this.createdate = createdate;
		}
	}	
}
