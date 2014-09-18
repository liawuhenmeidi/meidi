package inventory;

public class InventoryBranch {  
  private int id ;     
  private int inventoryid;  // 所包含的产品类别id
  private int branchid ;
      
  private String type ;   
   
  private int realcount ;  
  
  private int papercount;
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

      
 
}
