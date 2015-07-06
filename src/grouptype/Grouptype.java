package grouptype;
         
public class Grouptype { 
   private int id ;
   private String name ;
   private int statues ;   //  标记是系统默认还是管理员添加    1 系统默认   0  管理员添加
   private int type ;  // 标记组类别 
   
	public int getType() {
	return type; 
}

public void setType(int type) {
	this.type = type;
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
 