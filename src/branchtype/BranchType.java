package branchtype;

public class BranchType {
   private int id ;
   // 大类名称
   private String name ;
   // 导购是否可见
   private int statues; 
   // 1 表示系统默认，不可修改
   private int isSystem ;
   
   
public int getIsSystem() {
	return isSystem;
}
public void setIsSystem(int isSystem) {
	this.isSystem = isSystem;
}
public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
}
