package category;

public class Category {
  public static int sale = 0;   
  public static int unsale = 1;
  
  private int id;
  private String name;
  private int pid ;  // null 为根目录  // 父类别
  private String time;
  private int statues ;   // 1 已断货           0 正常销售
  private int ptype ;      // 0 销售商品    1 维修配件
  
	public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}
	public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
	public int getPid() {
	return pid;
}
public void setPid(int pid) {
	this.pid = pid;
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
	public int getPtype() {
		return ptype;
	} 
	public void setPtype(int ptype) {
		this.ptype = ptype;
	}
	
}
