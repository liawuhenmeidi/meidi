package branch;

import branchtype.BranchType;
import branchtype.BranchTypeService;
import utill.StringUtill;

public class Branch {
	private String locateName;       // 地点
	private int id;                  // 编号
	private int pid;                 // 所属类别
	private BranchType branchtype ;   
	private String message; //           门店订单需要的号，是否需要pos号，等
	private int statues; //         是否作为总库（可以考虑不要）
	private String branchids;       //关联门店 （比如国美南楼导购可以看那些网点的库存）
	private int disable;          // 是否删除
	private String encoded;       // 编码
	private String nameSN;        // 苏宁国美别名
	private String Reservoir;     // 库位名称 
  
	public BranchType getBranchtype() {
		//System.out.println(BranchTypeService.getMap());
		//System.out.println(pid); 
		branchtype = BranchTypeService.getMap().get(pid);
		return branchtype;
	}

	public void setBranchtype(BranchType branchtype) {
		this.branchtype = branchtype;
	}

	public int getDisable() {
		return disable;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}

	public String getBranchids() {
		return branchids;
	}

	public void setBranchids(String branchids) {
		this.branchids = branchids;
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

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocateName() {
		return locateName;
	}

	public void setLocateName(String locateName) {
		this.locateName = locateName;
	}
 
	public String getEncoded() {
		if(StringUtill.isNull(encoded)){
			encoded = "";
		}
		return encoded;
	}   
 
	public String getEncoded(int statues) {
		String encodedd = "";  
		if(StringUtill.isNull(encoded)){
			encodedd = "";
		}else{    
			 if(2 == statues){ 
				 char c = encoded.charAt(0);
				 if(c == '7'){  
					 encodedd =  encoded.replaceFirst("7", "A");
					}else if(c == '8'){
						encodedd =  encoded.replaceFirst("8", "B");
					}else if(c == '9'){
						//System.out.print(c == '9'); 
						  
						encodedd =  encoded.replaceFirst("9", "C");
					} 
			 }else { 
				 encodedd = encoded;
			 } 
		}  
		return encodedd;
	}
	
	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	public String getNameSN() {
		if(StringUtill.isNull(nameSN)){
			nameSN = ""; 
		}
		return nameSN;
	}

	public void setNameSN(String nameSN) {
		this.nameSN = nameSN;
	}
  
	public String getReservoir() {
		if(StringUtill.isNull(Reservoir)){
			Reservoir = "";
		}
		return Reservoir;
	}

	public void setReservoir(String reservoir) {
		Reservoir = reservoir;
	}
 
	

}
