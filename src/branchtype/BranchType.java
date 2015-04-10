package branchtype;

import utill.StringUtill;

public class BranchType {
	public static int sale = 1;
	public static int install = 2 ;
	 
	private int id;
	private String name;
	// 导购是否可见
	private int statues;
	// 1 表示系统默认，不可修改
	private int isSystem; 

	private int typestatues;  // 1 是卖场 2 售后网点

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
		if(StringUtill.isNull(name)){
			name = ""; 
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTypestatues() {
		return typestatues;
	}

	public void setTypestatues(int typestatues) {
		this.typestatues = typestatues;
	}

}
