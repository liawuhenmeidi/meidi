package inventory;

public class InventoryAll {
	public int categoryid;
   
   public String cateoryName ;
   private String type ;
   private String typeid ;
   private Boolean isquery ;   // 是否已盘点
   private String time ;
   
   public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public Boolean getIsquery() {
	return isquery;
}
public void setIsquery(Boolean isquery) {
	this.isquery = isquery;
}
public String getTypeid() {
	return typeid;
}
public void setTypeid(String typeid) {
	this.typeid = typeid;
}
private int branchid ;
   public int getBranchid() {
	return branchid;
}
public void setBranchid(int branchid) {
	this.branchid = branchid;
}
public int papercount;
   public int realcount;
   
   public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
   public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}  
public String getCateoryName() {
	return cateoryName;
}
public void setCateoryName(String cateoryName) {
	this.cateoryName = cateoryName;
}
public int getPapercount() {
	return papercount;
}
public void setPapercount(int papercount) {
	this.papercount = papercount;
}
public int getRealcount() {
	return realcount;
}
public void setRealcount(int realcount) {
	this.realcount = realcount;
}
}
