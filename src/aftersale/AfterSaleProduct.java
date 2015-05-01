package aftersale;

import category.CategoryService;
import product.ProductService;
import user.UserService;
import utill.StringUtill;

public class AfterSaleProduct {
	public static int fault = 1 ;   // 维修 
	public static int maintain = 2;  // 保养
	public static int fm  = 0;  //  类型未知
	public static int install = 3;  // 安装上报
	        
	public static int pending = 0;  //待处理  
	public static int success = 1;  //处理完   
	public static int uploaded = 2; //已上报 
	public static int charged = 3; //已结款
	 
   private int id ;  
   private int asid ;   // 售后单id 
   private int type ;  //  1 维修单   2  保养单   3 安装单 
   private String cause ;  // 故障原因
   private int cid ; 
   private String cname ;  
   private int tid ;
   private String tname ;  
   private double prince ; 
   private int dealid;
   private String dealName; 
   private int dealsendid;
   private String dealsendName;
   private String dealtime ;   // 处理完成时间   
   private int result;  //    1 已处理  2 已上报    3  已结款
   private int statues ;  // 处理结果        0 待处理  1   安装网点驳回      2 维修人员驳回     3 文员驳回
   private String detail ;   // 处理备注  
   private String nexttime;   // 下次处理时间 
   private String thistime;   // 此次维护处理时间    
   private String resultStr;    
   private String typeStr ;
   private String dealresult ;
   private String price; 
   
   public String getPrice() {
	return price;
}

public void setPrice(String price) {
	this.price = price;
}

public String getDealresult() {
	return dealresult;
}

public void setDealresult(String dealresult) {
	this.dealresult = dealresult;
}

public String getTypeStr() { 
	   if(type == 0 ){   
		   typeStr = "维修保养单";
	   }else if(type == 1){  
		   typeStr = "维修";
	   }else if(type == 2){ 
		   typeStr = "保养";  
	   }else if(type == 3){
		   typeStr = "安装上报"; 
	   }
	return typeStr;
}

public void setTypeStr(String typeStr) {
	this.typeStr = typeStr;
}

public String getResultStr() {
	   String str = "";
	   if(result == 0 ){
		   str = "未处理";
	   }else if(result == 1){ 
		   str = "待上报"; 
	   }else if(result == 2){
		   str = "已上报"; 
	   }else if(result == 3){
		   str = "已结款";
	   }
	   return str ;
}

public void setResultStr(String resultStr) {
	this.resultStr = resultStr;
}
 
   
public String getDealsendName() {
	if(dealsendid != 0 ){ 
	
		  if(null != UserService.getMapId().get(dealsendid)){ 
			  dealsendName = UserService.getMapId().get(dealsendid).getUsername();
		  }else {
			  dealsendName = "网点已过期";  
		  }
		//System.out.println(UserService.getMapId());  
		
	}else {
		dealsendName = ""; 
	} 
	return dealsendName;
}


public void setDealsendName(String dealsendName) {
	this.dealsendName = dealsendName;
}


public String getDealName() {   
	if(dealid != 0 ){ 
		if(null != UserService.getMapId().get(dealid)){ 
			dealName = UserService.getMapId().get(dealid).getUsername();
		  }else { 
			  dealName= "网点已过期";   
		  }
		//System.out.println(UserService.getMapId());  
		
	}else {
		dealName = ""; 
	}
	return dealName;
}


public void setDealName(String dealName) {
	this.dealName = dealName;
}


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
	if(StringUtill.isNull(cause)){
		cause = ""; 
	} 
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
	}else { 
		cname = "";  
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
	}else {
		tname = ""; 
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
