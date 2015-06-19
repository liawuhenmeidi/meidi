package httpClient.download;

import inventory.InventoryBranch;
import product.ProductService;
      
public class SNInventory extends InventoryBranch implements Comparable<SNInventory>{
  private String branchName;  // 库位名称  
  private String branchNum;  // 库位代码  
  private String oldbranchNum;  // 原库位
  private String goodType ;  // 商品类别
  private String goodGroupName ;  // 商品组名称
  private String goodGroupNum ;  // 商品组代码
  private String goodpName ;  // 商品名称
  private String goodNum ;  // 商品代码
  private int ATP;  //atp 可卖数  
  private int num;  // 实货可用数 
  private int Nomention ;  // 销售未提
  private int saleNum;    // 销售数量 
  private int inreduce; // 库存较少   
  private String serialNumber;   // 序列号
  private int inInbranch ;   // 入库数量
  private double dynamic ;   
  private int modelnum ;    // 样机数量 
  

private int outmodelnum ; // 未入库样机 
  private int outcommonnum ; // 未入库常规 
  private int outspecialnum ; // 未入库特价
  private int badnum ;    //坏机  
  private int outnum ;    // 未入库数量  
  private int bid ; 
  private int tid ;    
  private int cid ;   
  // 产品状态  ， 盘点时间 
  // 0  默认  1  已赋值  2 已对比    3 未对比
  private int flag = 0 ;  // 0 未提交  1  已提交
 private int flagupm = 0; 
 private int flagupin = 0;
 private int flagupout = 0;
 private int upmodel ;
 private int upin ;
 private int upout ;

public int getFlag() {
	return flag;
}

public void setFlag(int flag) {
	this.flag = flag;
}

public int getFlagupm() {
	return flagupm;
}

public void setFlagupm(int flagupm) {
	this.flagupm = flagupm;
}

public int getFlagupin() {
	return flagupin;
}

public void setFlagupin(int flagupin) {
	this.flagupin = flagupin;
}

public int getFlagupout() {
	return flagupout;
}

public void setFlagupout(int flagupout) {
	this.flagupout = flagupout;
}

public int getUpmodel() {
	return upmodel;
}

public void setUpmodel(int upmodel) {
	this.upmodel = upmodel;
}

public int getUpin() {
	return upin;
}

public void setUpin(int upin) {
	this.upin = upin;
}

public int getUpout() {
	return upout;
}

public void setUpout(int upout) {
	this.upout = upout;
}

public int getBadnum() {
	return badnum;
}

public void setBadnum(int badnum) {
	this.badnum = badnum;
}

public int getOutcommonnum() {
		return outcommonnum;
	}

	public void setOutcommonnum(int outcommonnum) {
		this.outcommonnum = outcommonnum;
	}

	public int getOutspecialnum() {
		return outspecialnum;
	}

	public void setOutspecialnum(int outspecialnum) {
		this.outspecialnum = outspecialnum;
	}
public int getOutmodelnum() {
	return outmodelnum;
}

public void setOutmodelnum(int outmodelnum) {
	this.outmodelnum = outmodelnum;
}

public int getNomention() {
	return Nomention;
}
  
public void setNomention(int nomention) {
	Nomention = nomention;
}

public String getOldbranchNum() { 
	return oldbranchNum;
}

public void setOldbranchNum(String oldbranchNum) {
	this.oldbranchNum = oldbranchNum;
}

public int getCid() {
	if(tid != 0 && tid != -1){
		cid = ProductService.getIDmap().get(tid).getCategoryID();
	} 
	return cid;
}
 
public void setCid(int cid) {
	this.cid = cid;
} 
  
public int getBid() {
	return bid;
}

public void setBid(int bid) {
	this.bid = bid;
}

public int getTid() {
	return tid;
}

public void setTid(int tid) {
	this.tid = tid;
}

public int getModelnum() {
	return modelnum;
}

public void setModelnum(int modelnum) {
	this.modelnum = modelnum;
}

public double getDynamic() {
	//DecimalFormat    df   = new DecimalFormat("######0.00");    
	if(0 == (num+saleNum+outnum+modelnum)){   
		dynamic = saleNum*100 ;     
	}else {             
	//System.out.println(num+saleNum+outnum+modelnum);
		//dynamic = ((double)saleNum*100/(double)(num+saleNum+outnum+modelnum)) ;
		dynamic = (saleNum*100/(num+saleNum+outnum+modelnum)) ;
	}      
	// return Double.valueOf(df.format(dynamic));
	return dynamic; 
}

public void setDynamic(double dynamic) {
	this.dynamic = dynamic;
}
public int getInInbranch() {
	return inInbranch;
}
public void setInInbranch(int inInbranch) {
	this.inInbranch = inInbranch;
}
public String getSerialNumber() {
	return serialNumber;
}
public void setSerialNumber(String serialNumber) {
	this.serialNumber = serialNumber;
}
public int getInreduce() {
	return inreduce;
}
public void setInreduce(int inreduce) {
	this.inreduce = inreduce;
}
public int getSaleNum() {
	return saleNum;
}
public void setSaleNum(int saleNum) {
	this.saleNum = saleNum;
}
public String getBranchName() {
	return branchName;
}
public void setBranchName(String branchName) {
	this.branchName = branchName;
}
public String getBranchNum() {
	return branchNum;
}
public void setBranchNum(String branchNum) {
	this.branchNum = branchNum;
}
public String getGoodType() {
	return goodType;
}
public void setGoodType(String goodType) {
	this.goodType = goodType;
}
public String getGoodGroupName() {
	return goodGroupName;
}
public void setGoodGroupName(String goodGroupName) {
	this.goodGroupName = goodGroupName;
}
public String getGoodGroupNum() {
	return goodGroupNum;
}
public void setGoodGroupNum(String goodGroupNum) {
	this.goodGroupNum = goodGroupNum;
}
public String getGoodpName() {
	return goodpName;
}
public void setGoodpName(String goodpName) {
	this.goodpName = goodpName;
}
public String getGoodNum() {
	return goodNum;
}
public void setGoodNum(String goodNum) {
	this.goodNum = goodNum;
}
public int getATP() {
	return ATP;
}
public void setATP(int aTP) {
	ATP = aTP;
}
public int getNum() {
	return num;
}
public void setNum(int num) {
	this.num = num;
}

public int getOutnum() {
	return outnum;
}
 
public void setOutnum(int outnum) {
	this.outnum = outnum;
}

@Override
public int compareTo(SNInventory o) {
     	    
        if(this.getSaleNum() > o.getSaleNum()){
     	   return 0 ; 
        }else if(this.getSaleNum() < o.getSaleNum()){
     	   return 1 ;  
        }else if(this.getSaleNum() == o.getSaleNum()){
     	   if(this.getNum() >= o.getNum()){
     		   return 0 ;  
     	   }else { 
     		   return 1 ;
     	   }
        }  
 
	return 0;
}


}
