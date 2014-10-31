package inventory;

import java.util.List;
 
public class Inventory {  
	
  private int id ;    
  private List<InventoryMessage> inventory;  // 所包含的产品入库信息
  private String intime ;   // 入库申请发起时间
  private int uid  ;   // 入库申请发起用户id  
    
  private int chekid  ;   // 入库申请待审核用户id 
  private int outbranchid ;  // 出库仓库id   
  private int inbranchid ;  // 入库仓库id  
  private int outstatues ;   // 出库放确认状态   
  private int instatues ;    // 入库方确认状态    
  private String remark ;     
  private int intype ;  //   1 表示调货单   2 预约调货单  3 票面调货单
  
  public int getIntype() {
	return intype;
}
public void setIntype(int intype) {
	this.intype = intype;
}
public int getOutstatues() {
	return outstatues;
}
public void setOutstatues(int outstatues) {
	this.outstatues = outstatues;
}
public int getInstatues() {
	return instatues;
}
public void setInstatues(int instatues) {
	this.instatues = instatues;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public int getOutbranchid() {
	return outbranchid;
}
public void setOutbranchid(int outbranchid) {
	this.outbranchid = outbranchid;
}
public int getInbranchid() {
	return inbranchid;
}
public void setInbranchid(int inbranchid) {
	this.inbranchid = inbranchid;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
 
public List<InventoryMessage> getInventory() {
	return inventory;
} 
public void setInventory(List<InventoryMessage> inventory) {
	this.inventory = inventory;
}

public String getIntime() {
	return intime;
}
public void setIntime(String intime) {
	this.intime = intime;
}
public int getUid() {
	return uid;
}
public void setUid(int uid) {
	this.uid = uid;
}
public int getChekid() {
	return chekid;
}
public void setChekid(int chekid) {
	this.chekid = chekid;
}
	
	
}
