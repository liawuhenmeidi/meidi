package httpClient.download;

import java.text.DecimalFormat;
 
public class Inventory {
  private String branchName;  // 库位名称 
  private String branchNum;  // 库位代码
  private String goodType ;  // 商品类别
  private String goodGroupName ;  // 商品组名称
  private String goodGroupNum ;  // 商品组代码
  private String goodpName ;  // 商品名称
  private String goodNum ;  // 商品代码
  private int ATP;  //atp 可卖数  
  private int num;  // 实货可用数
  private int saleNum;  
  private int inreduce; // 库存较少 
  private String serialNumber;   // 序列号
  private int inInbranch ;   // 入库数量
  private double dynamic ; 
  private int modelnum ;
  private int outnum ;    // 未入库数量
   
       
public int getModelnum() {
	return modelnum;
}

public void setModelnum(int modelnum) {
	this.modelnum = modelnum;
}

public double getDynamic() {
	DecimalFormat    df   = new DecimalFormat("######0.00");   
	    
	if(0 == num){  
		dynamic = saleNum ;  
	}else {      
		dynamic = (double)saleNum/(double)num ;
	}  
	 return Double.valueOf(df.format(dynamic));
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

}
