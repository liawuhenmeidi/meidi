package inventory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import enums.ProductModel;

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
	  
	private String typestatuesName ;
	     
	private int snNum ;  // 苏宁库存  
	
	private int snModelnum ;  // 苏宁样机 
	  
	private int snBad;  // 苏宁坏机
	private String goodnum ;  
	private String goodname;// 苏宁名称
	private int isOverStatues;  //    0  已完成    1  未完成 
	
	private int incommonnum ;
	private int inmodelnum ;   
	
	  private Map<Integer,String> map = new  HashMap<Integer,String>();
	    
	  public Map<Integer, String> getMap() {
		return map;
	}

	public void setMap(Map<Integer, String> map) {
		this.map = map;
	}
	 
	public int getIncommonnum() {
		return incommonnum;
	} 
	public void setIncommonnum(int incommonnum) {
		this.incommonnum = incommonnum;
	}
	public int getInmodelnum() {
		return inmodelnum;
	}
	public void setInmodelnum(int inmodelnum) {
		this.inmodelnum = inmodelnum;
	}
	public int getSnBad() {
		return snBad;
	}
	public void setSnBad(int snBad) {
		this.snBad = snBad;
	}
	public String getGoodnum() {
		return goodnum;
	}
	public void setGoodnum(String goodnum) {
		this.goodnum = goodnum;
	}
	public String getGoodname() {
		return goodname;
	}
	public void setGoodname(String goodname) {
		this.goodname = goodname;
	}
	public int getSnNum() {
		return snNum;
	}
	public void setSnNum(int snNum) {
		this.snNum = snNum;
	}
	public int getSnModelnum() {
		return snModelnum;
	}
	public void setSnModelnum(int snModelnum) {
		this.snModelnum = snModelnum;
	}
	public String getTypestatuesName() { 
		//System.out.println(typestatues); 
		if(ProductModel.Model.标准采购订单.getValue() == typeStatues){     
			typestatuesName = ProductModel.Model.标准采购订单.toString();
		}else if(ProductModel.Model.特价机采购订单.getValue()== typeStatues){
			typestatuesName = ProductModel.Model.特价机采购订单.toString();
		}else if(ProductModel.Model.样机采购订单.getValue()==typeStatues){ 
			typestatuesName = ProductModel.Model.样机采购订单.toString();
		}     
		//System.out.println(goodtypeStatues);   
		return typestatuesName; 
	}
	
	public String getTypestatuesName(int statues) { 
		//System.out.println(typestatues);  
		if(ProductModel.Model.标准采购订单.getValue() == statues){     
			typestatuesName = ProductModel.Model.标准采购订单.toString();
		}else if(ProductModel.Model.特价机采购订单.getValue()== statues){
			typestatuesName = ProductModel.Model.特价机采购订单.toString();
		}else if(ProductModel.Model.样机采购订单.getValue()==statues){ 
			typestatuesName = ProductModel.Model.样机采购订单.toString();
		}     
		//System.out.println(goodtypeStatues);   
		return typestatuesName; 
	}
	
	
	public void setTypestatuesName(String typestatuesName) {
		this.typestatuesName = typestatuesName;
	}
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
		if(!StringUtill.isNull(this.getTypeid())){  
			//System.out.println(this.getTypeid());  
			//System.out.println(StringUtill.GetJson(ProductService.getIDmap().get(Integer.valueOf(this.getTypeid()))));  
			product = ProductService.getIDmap().get(Integer.valueOf(this.getTypeid()));
			return product; 
		} 
		 
		return new Product(); 
	}

	public int getIsOverStatues() {
		return isOverStatues;
	}
	public void setIsOverStatues(int isOverStatues) {
		this.isOverStatues = isOverStatues;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

}
