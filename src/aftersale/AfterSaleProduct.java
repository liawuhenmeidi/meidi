package aftersale;

import category.CategoryService;
import product.ProductService;

public class AfterSaleProduct {
	public static int fault = 1 ;   // 维修 
	public static int maintain = 2;  // 保养
	
	public static int pending = 0;  //待处理
	public static int success = 1;  //处理完  
	public static int Failure = 2;  //失败 
	 
   private int id ;
   private int asid ;   // 售后单id
   private int type ;  //  1 维修单   2  保养单
   private String cause ;  // 故障原因
   private int cid ;
   private String cname ; 
   private int tid ;
   private String tname ; 
   private double prince ; 
   private int dealid;
   private int dealsendid;
   private String dealtime ; 
   private int result;  //  
   private int statues ;  // 处理结果          1   安装网点驳回      2 维修人员驳回
   private String detail ;   // 处理备注  
   private String nexttime;   // 下次处理时间
   private String thistime;   // 此次维护处理时间    
   
public String getThistime() {
	return thistime;
}
public void setThistime(String thistime) {
	this.thistime = thistime;
}
public String getNexttime() {
	return nexttime;
}
public void setNexttime(String nexttime) {
	this.nexttime = nexttime;
}
public int getAsid() {
	return asid; 
}
public void setAsid(int asid) {
	this.asid = asid;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public String getCause() {
	return cause;
}
public void setCause(String cause) {
	this.cause = cause;
}
public int getCid() {
	return cid;
}
public void setCid(int cid) {
	this.cid = cid;
}
public String getCname() {
	if(cid != 0 ){
		cname = CategoryService.getmap().get(cid).getName();
	}
	return cname;
}
public void setCname(String cname) {

	this.cname = cname;
	
	
}
public int getTid() {
	return tid;
}
public void setTid(int tid) {
	this.tid = tid;
}
public String getTname() {
	if(tid != 0 ){
		tname = ProductService.getIDmap().get(tid).getType();
	}
	return tname; 
}
public void setTname(String tname) {
	this.tname = tname;
}
public double getPrince() {
	return prince;
}
public void setPrince(double prince) {
	this.prince = prince;
}
public int getDealid() {
	return dealid;
}
public void setDealid(int dealid) {
	this.dealid = dealid;
}

public int getDealsendid() {
	return dealsendid;
}
public void setDealsendid(int dealsendid) {
	this.dealsendid = dealsendid;
}
public String getDealtime() {
	return dealtime;
}
public void setDealtime(String dealtime) {
	this.dealtime = dealtime;
}
public int getResult() {
	return result;
}
public void setResult(int result) {
	this.result = result;
}
public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}
public String getDetail() {
	return detail;
}
public void setDetail(String detail) {
	this.detail = detail;
}
   
   
   
}
