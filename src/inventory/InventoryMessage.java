package inventory;

import java.util.HashMap;
import java.util.Map;

import utill.StringUtill;


public class InventoryMessage {
  private int id ; 
  private int categoryId; 
  private String categoryName;
  private String  productId ; 
  
  private String  productname;
  private int anlycount;
  private int count ;   	  
  private int inventoryId; // 订单信息所属单据号
   
  private String realString ;
  
  private String paperString ;
  
  
  
public String getRealString() {
	if(StringUtill.isNull(realString)){
		return "";
	}
	return realString;
}
public void setRealString(String realString) {
	this.realString = realString;
}
public String getPaperString() {
	if(StringUtill.isNull(paperString)){
		return "";
	}
	return paperString;
}
public void setPaperString(String paperString) {
	this.paperString = paperString;
}
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
public int getAnlycount() {
	return anlycount;
}
public void setAnlycount(int anlycount) {
	this.anlycount = anlycount;
}
public String getProductname() {
	return productname;
}
public void setProductname(String productname) {
	this.productname = productname;
}

  public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getCategoryId() {
	return categoryId;
}
public void setCategoryId(int categoryId) {
	this.categoryId = categoryId;
}

public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public int getInventoryId() {
	return inventoryId;
}
public void setInventoryId(int inventoryId) {
	this.inventoryId = inventoryId;
}
	  
public static Map<String,String> getDeliveryStatuesMap(){
	Map<String,String> map = new HashMap<String,String>();
	map.put(0+"", "需配送安装");
	map.put(1+"", "已送货");
	map.put(8+"", "已自提 ");
	map.put(9+"", "只安装(门店提货)");
	map.put(10+"", "只安装(顾客已提) ");
	map.put(-1+"", "调拨单"); 
	map.put(20+"", "换货单");
	map.put(21+"", "网上数据");  
	//map.put(20+"", "换货单");
	return map ;
	
	
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
