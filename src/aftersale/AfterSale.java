package aftersale;

import user.User;
import user.UserService;

public class AfterSale {
   public static int typeupdate = 1 ;
   public static int typesale = 0 ;
   
   private String printid;
   private int oriedid;
   
   private int id ;
   private int tid; // 产品型号id
   private String tName; // 产品型号
   private int cid; // 产品类别id
   private String cName; // 产品类别名称
   private int pcount;
   
   private String uname;
   private String phone;
   private String andate;
   private String batchNumber;  //批号
   private String barcode;  // 条码
    
   private String location ; //
   private int branch ; 
   private int type;  // 单据类型      0 表示自己销售   1 网点上报
   private String saledate;
   private String branchName;
   private String detail ;
   private String submitTime;
   private int submitId;
   private User submituser;
   private String typeName ;
   
   public String getTypeName() {
	if(type == 0){
		typeName = "网点上报";
	}else if(type == 1){
		typeName = "系统直营";
	}
	return typeName;
}

   public void setTypeName(String typeName){
	   this.typeName = typeName; 
   }
public User getSubmituser() {
	submituser = UserService.getMapId().get(submitId);
	return submituser; 
}
public void setSubmituser(User submituser) {
	this.submituser = submituser;
}
public int getSubmitId() {
	return submitId;
}
public void setSubmitId(int submitId) {
	this.submitId = submitId;
}
public int getOriedid() {
		return oriedid;
	}
	public void setOriedid(int oriedid) {
		this.oriedid = oriedid;
	}
	
   public String getSubmitTime() {
	return submitTime;
}
public void setSubmitTime(String submitTime) {
	this.submitTime = submitTime;
}
public String getDetail() {
	return detail;
}
public void setDetail(String detail) {
	this.detail = detail;
}
public String getPrintid() {
		return printid;
	}
	public void setPrintid(String printid) {
		this.printid = printid;
	}
	
	public int getPcount() {
		return pcount;
	}
	public void setPcount(int pcount) {
		this.pcount = pcount;
	}
	
public String gettName() {
	return tName;
}
public void settName(String tName) {
	this.tName = tName;
}
public String getcName() {
	return cName;
} 
public void setcName(String cName) {
	this.cName = cName;
}
public int getBranch() {
	return branch;
}
public void setBranch(int branch) {
	this.branch = branch;
}
public String getBranchName() {
	return branchName;
}
public void setBranchName(String branchName) {
	this.branchName = branchName;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getTid() {
	return tid;
}
public void setTid(int tid) {
	this.tid = tid;
}
public int getCid() {
	return cid;
}
public void setCid(int cid) {
	this.cid = cid;
}
public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getAndate() {
	return andate;
}
public void setAndate(String andate) {
	this.andate = andate;
}
public String getBatchNumber() {
	return batchNumber;
}
public void setBatchNumber(String batchNumber) {
	this.batchNumber = batchNumber;
}

public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}

public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public String getSaledate() {
	return saledate;
}
public void setSaledate(String saledate) {
	this.saledate = saledate;
}
   
public String getBarcode() {
	return barcode;
}
public void setBarcode(String barcode) {
	this.barcode = barcode;
}
   
   
}
