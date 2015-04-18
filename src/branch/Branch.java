package branch;

import utill.StringUtill;

public class Branch {
	private String locateName;
	private int id;
	private int pid;
	private String message; // 门店订单需要的号，是否需要pos号，等
	private int statues; // 是否作为总库
	private String branchids;
	private int disable;
	private String encoded; 
	private String nameSN;  // 苏宁国美别名
	private String Reservoir; // 库位名称 

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
