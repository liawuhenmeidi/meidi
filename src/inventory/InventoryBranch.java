package inventory;

import java.util.Date;

import product.Product;
import product.ProductService;

import utill.StringUtill;
import utill.TimeUtill;

public class InventoryBranch {
	private int id;
	private int inventoryid; // 所包含的产品类别id
	private int branchid;
 
	private String type;
       
	private Product product  ; 
	 
	private int typeStatues; // 1常规 2 特价 3 样机 4 换货 5 赠品 
   
	private String typeid;

	private int realcount;

	private int papercount;

	private int isquery; // 1 为已盘点

	private String querymonth;
    
	// 小电苏宁订单号
	private String orderNUmSN;
	// 订单过期时间
	private String activetime ;
	
	public boolean isquery() {
		boolean flag = false;
		int month = TimeUtill.getMonth();
		String format = "yyyy-MM-dd";
		if (!StringUtill.isNull(querymonth)) {
			Date date = TimeUtill.StringToDate(querymonth, format);
			if (month == date.getMonth() && isquery == 1) {
				flag = true;
			}
		}

		return flag;
	}

	public int getIsquery() {
		return isquery;
	}

	public void setIsquery(int isquery) {
		this.isquery = isquery;
	}

	public String getQuerymonth() {
		return querymonth;
	}

	public void setQuerymonth(String querymonth) {
		this.querymonth = querymonth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public int getInventoryid() {
		return inventoryid;
	}

	public void setInventoryid(int inventoryid) {
		this.inventoryid = inventoryid;
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
    
	
	public int getRealcount() {
		return realcount;
	}

	public void setRealcount(int realcount) {
		this.realcount = realcount;
	}

	public int getPapercount() {
		return papercount;
	}

	public void setPapercount(int papercount) {
		this.papercount = papercount;
	}

	public int getTypeStatues() {
		return typeStatues;
	}
 
	public void setTypeStatues(int typeStatues) {
		this.typeStatues = typeStatues;
	} 

	public String getOrderNUmSN() {
		if(StringUtill.isNull(orderNUmSN)){
			orderNUmSN = "";
		} 
		return orderNUmSN;
	} 

	public void setOrderNUmSN(String orderNUmSN) {
		this.orderNUmSN = orderNUmSN;
	}

	public String getActivetime() {
		if(StringUtill.isNull(activetime)){
			activetime = "";
		} 
		return activetime;
	}  

	public void setActivetime(String activetime) {
		this.activetime = activetime;
	}
   
	public Product getProduct() {  
		//System.out.println(Integer.valueOf(this.getTypeid())); 
		product = ProductService.getIDmap().get(Integer.valueOf(this.getTypeid()));
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
