package net.ncue.mart.model;

public class Sale {
	private String id = "";
	private String category = "";
	private String mcategory = "";
	private String product = "";
	private String productIdx = "";
	private String head = "";
	private String idx1 = "";
	private String count = "";
	private String morecount = "";
	private String isadd = "";
	private String price = "";
	private String conditionalprice = "";
	private String originalprice = "";
	private String description = "";
	private String image = "";
	private String sDate = "";
	private String eDate = "";
	private String display = "";
	private String mid = "";
	private String create_date = "";
	
	public Sale (String[] arr) {
		this.id = arr[0];
		this.mid = arr[1];
		this.mcategory = arr[2];
		this.category = arr[3];
		this.product = arr[4];
		this.head = arr[5];
		this.count = arr[6];
		this.morecount = arr[7];
		this.isadd = arr[8];
		this.price = arr[9];
		this.conditionalprice = arr[10];
		this.originalprice = arr[11];
		this.description = arr[12];
		this.image = arr[13];
		this.sDate = arr[14];
		this.eDate = arr[15];
		this.display = arr[16];
	}
	
	public Sale (String mcategory, String product, String count, String price, String conditionalprice, String originalprice, String sDate, String eDate) {
		this.mcategory = mcategory;
		this.product = product;
		this.count = count;
		this.price = price;
		this.conditionalprice = conditionalprice;
		this.originalprice = originalprice;
		this.sDate = sDate;
		this.eDate = eDate;
	}
	public Sale (String mcategory, String category, String product, String idx1, String count, String morecount, String isadd, String price, String conditionalprice, String originalprice, String description, String sDate, String eDate) {
		this.mcategory = mcategory;
		this.category = category;
		this.product = product;
		this.idx1 = idx1;
		this.count = count;
		this.morecount = morecount;
		this.isadd = isadd;
		this.price = price;
		this.conditionalprice = conditionalprice;
		this.originalprice = originalprice;
		this.description = description;
		this.sDate = sDate;
		this.eDate = eDate;
	}
	public Sale (String mcategory, String category, String product, String productIdx, String head, String idx1, String count, String morecount, String isadd, String price, String conditionalprice, String originalprice, String description, String sDate, String eDate) {
		this.mcategory = mcategory;
		this.category = category;
		this.product = product;
		this.productIdx = productIdx;
		this.head = head;
		this.idx1 = idx1;
		this.count = count;
		this.morecount = morecount;
		this.isadd = isadd;
		this.price = price;
		this.conditionalprice = conditionalprice;
		this.originalprice = originalprice;
		this.description = description;
		this.sDate = sDate;
		this.eDate = eDate;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mcategory: ").append(mcategory).append("\n");
		sb.append("category: ").append(category).append("\n");
		sb.append("product: ").append(product).append("\n");
		sb.append("productIdx: ").append(productIdx).append("\n");
		sb.append("head: ").append(head).append("\n");
		sb.append("idx1").append(idx1).append("\n");
		sb.append("count: ").append(count).append("\n");
		sb.append("morecount: ").append(morecount).append("\n");
		sb.append("isadd: ").append(isadd).append("\n");
		sb.append("price").append(price).append("\n");
		sb.append("conditionalprice: ").append(conditionalprice).append("\n");
		sb.append("originalprice: ").append(originalprice).append("\n");
		sb.append("description: ").append(description).append("\n");
		sb.append("image: ").append(image).append("\n");
		sb.append("sDate: ").append(sDate).append("\n");
		sb.append("eDate: ").append(eDate).append("\n");
		return sb.toString();
	}
	
	public String getID() {
		return this.id;
	}
	public void setID(String id) {
		this.id = id.trim();
	}
	public String getMcategory() {
		return this.mcategory;
	}
	public void setMcategory(String mcategory) {
		this.mcategory = mcategory.trim();
	}
	
	public String getCategory() {
		return this.category;
	}
	public void setCategory(String category) {
		this.category = category.trim();
	}

	public String getProduct() {
		return this.product;
	}
	public void setProduct(String product) {
		this.product = product.trim();
	}
	
	public String getProductIdx() {
		return this.productIdx;
	}	
	public void getProductIdx(String productIdx) {
		this.productIdx = productIdx.trim();
	}

	public String getHead() {
		return this.head;
	}	
	public void setHead(String head) {
		this.head = head.trim();
	}

	public String getIdx1() {
		return this.idx1;
	}
	public void setIdx1(String idx1) {
		this.idx1 = idx1.trim();
	}
	
	public String getCount() {
		return this.count;
	}
	public void setCount(String count) {
		this.count = count.trim();
	}
	
	public String getMorecount() {
		if (this.morecount.trim().equals("")) return "0";
		return this.morecount;
	}
	public void setMorecount(String morecount) {
		this.morecount = morecount.trim();
	}

	public String getIsadd() {
		if (this.isadd.trim().equals("")) return "0";
		return this.isadd;
	}
	public void setIsadd(String isadd) {
		this.isadd = isadd.trim();
	}	
	
	
	public String getPrice() {
		if (this.price.trim().equals("")) return "0";
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price.trim();
		if (this.price.trim().equals("")) this.price = "0";
	}
	
	public String getConditionalPrice() {
		if (this.conditionalprice.trim().equals("")) return "0";
		return this.conditionalprice;
	}
	public void setConditionalPrice(String conditionalprice) {
		this.conditionalprice = conditionalprice.trim();
		if (this.conditionalprice.trim().equals("")) this.conditionalprice = "0";
	}
	
	public String getOriginalprice() {
		if (this.originalprice.trim().equals("")) return "0";
		return this.originalprice;
	}	
	public void setOriginalprice(String originalprice) {
		this.originalprice = originalprice.trim();
		if (this.originalprice.trim().equals("")) this.originalprice = "0";
	}
	
	public String getdescription() {
		return this.description;
	}
	public void setdescription(String description) {
		this.description = description.trim();
	}
	
	public String getImage() {
		return this.image;
	}
	public void setImage(String image) {
		this.image = image.trim();
	}	
	
	public String getSDate() {
		return this.sDate;
	}	
	public void setSDate(String sDate) {
		this.sDate = sDate.trim();
	}
	
	public String getEDate() {
		return this.eDate;
	}	
	public void setEDate(String eDate) {
		this.eDate = eDate.trim();
	}
	
	public String getDisplay() {
		return this.display;
	}	
	public void setDisplay(String display) {
		this.display = display.trim();
	}
	
	public String getMid() {
		return this.mid;
	}	
	public void setMid(String mid) {
		this.mid = mid.trim();
	}
	
	public String getCreateDate() {
		return this.create_date;
	}	
	public void setCreateDate(String create_date) {
		this.create_date = create_date.trim();
	}

}
	