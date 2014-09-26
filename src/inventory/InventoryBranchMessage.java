package inventory;

public class InventoryBranchMessage { 
	
  private int id ;     
  private int inventoryid;  // 所包含的产品入库信息
  private int branchid ;
  private String time ; 
  

  private String type ;   
    
  private int count ;  
  
  private int operatortype ;  //  0 表示出货  1  表示入库   2 文员派单  4 安装公司释放
        // 11 安装公司派送货员   6  送货员释放             7    退货员拉回    8 文员同意退货
   
  private int realcount ;  //  实际库存  
  
  private int papercount ;  //  虚拟库存
  
 

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

public String getTime() {
	return time;
}
 
public void setTime(String time) {
	this.time = time;
} 
   
	public int getOperatortype() {
	return operatortype;
}

public void setOperatortype(int operatortype) {
	this.operatortype = operatortype;
}

	public int getId() {
		return id;
	}
	
public void setId(int id) {
	this.id = id;
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
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
      
 
}
