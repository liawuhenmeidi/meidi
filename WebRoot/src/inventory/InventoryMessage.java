package inventory;

public class InventoryMessage {
  private int id ; 
  private int categoryId; 
  private String  productId ; 
    
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
