package inventory;

import enums.ProductModel;
import product.Product;
import utill.StringUtill;

public class InventoryAll {
	public int categoryid;
   
   public String cateoryName ;
   private String type ;
   private String typeid ;
   private Boolean isquery ;   // 是否已盘点
   private String time ;
   private int papercount; 
   private int realcount;   
   
   private int typestatues;
   private String typestatuesName ;
    
   public int getTypestatues() {
	return typestatues;
} 
   
public void setTypestatues(int typestatues) {
	this.typestatues = typestatues;
}     
    
public String getTypestatuesName() {  
	if(StringUtill.isNull(typestatuesName)){
		//System.out.println(typestatues); 
		if(ProductModel.Model.标准采购订单.getValue() == typestatues){     
			typestatuesName = ProductModel.Model.标准采购订单.toString();
		}else if(ProductModel.Model.特价机采购订单.getValue()== typestatues){
			typestatuesName = ProductModel.Model.特价机采购订单.toString();
		}else if(ProductModel.Model.样机采购订单.getValue()==typestatues){ 
			typestatuesName = ProductModel.Model.样机采购订单.toString();
		}    
	}
	
	//System.out.println(goodtypeStatues);   
	return typestatuesName; 
}
public void setTypestatuesName(String typestatuesName) {
	this.typestatuesName = typestatuesName;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
private int branchid ; 
   private Product product;
// 小电苏宁订单号
	private String orderNUmSN;
	// 订单过期时间
	private String activetime ;
	
   
   public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public Boolean getIsquery() {
	return isquery;
}
public void setIsquery(Boolean isquery) {
	this.isquery = isquery;
}
public String getTypeid() {
	return typeid;
}
public void setTypeid(String typeid) {
	this.typeid = typeid;
}

   public int getBranchid() {
	return branchid;
}
public void setBranchid(int branchid) {
	this.branchid = branchid;
}

   public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
   public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}  
public String getCateoryName() {
	return cateoryName;
}
public void setCateoryName(String cateoryName) {
	this.cateoryName = cateoryName;
}
public int getPapercount() {
	return papercount;
}
public void setPapercount(int papercount) {
	this.papercount = papercount;
}
public int getRealcount() {
	return realcount;
}
public void setRealcount(int realcount) {
	this.realcount = realcount;
}
public String getOrderNUmSN() { 
	return orderNUmSN;
}
public void setOrderNUmSN(String orderNUmSN) {
	this.orderNUmSN = orderNUmSN;
}
public String getActivetime() {
	return activetime; 
}
public void setActivetime(String activetime) {
	this.activetime = activetime;
}


}
