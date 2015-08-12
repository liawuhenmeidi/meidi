package branchtype;

import enums.SaleModel;
import exportModel.ExportModel;
import utill.StringUtill;

public class BranchType {
	public static int sale = 1;
	public static int install = 2 ;
	
	private int id;           // 编号
	private String name;      // 名称
	// 导购是否可见
	private int statues;      // 
	// 1 表示系统默认，不可修改
	private int isSystem;      // 是否是系统类别，不可删除

	private int typestatues;  //  门店属性          1 是卖场 2 售后网点
	 
	private int saletype ; //   1 苏宁   2 国美     ExportModel.SuNing
	
	private String saletypeStr;   // 
	
	private int exportmodel ;  //  开订单导出模式

	public int getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(int isSystem) {
		this.isSystem = isSystem;
	}
 
	public int getStatues() {
		return statues;
	}
 
	public String getSaletypeStr() {
		return SaleModel.get(saletype );
	}

	public void setSaletypeStr(String saletypeStr) {
		this.saletypeStr = saletypeStr;
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

	public int getExportmodel() {
		return exportmodel;
	}

	public void setExportmodel(int exportmodel) {
		this.exportmodel = exportmodel;
	}

	public int getSaletype() {
		return saletype;
	}

	public void setSaletype(int saletype) {
		this.saletype = saletype;
	}

}
