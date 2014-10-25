package inventory;

import product.ProductService;

public class InventoryMessage {
  private int id ; 
  private int categoryId; 
  private String categoryName;
  private String  productId ; 
  
  private String  productname;
  private int anlycount;
  
  
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
private int count ;   	  
  private int inventoryId; // 订单信息所属单据号
   
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
