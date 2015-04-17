package ordersgoods;

import branch.Branch;
import branch.BranchService;
import user.User;
import user.UserService;
import utill.StringUtill;

public class OrderMessage { 

	public static int unexamine = 0; // 未审核
	public static int examine = 1; // 已审核
	public static int billing = 2;// 已开单 
	public static int all = 3;// 已开单  
	
	private int id;
	private String oid; // 订单号
	private int submitid; // 提交订单人
	private String submittime; // 提交订单时间
	private int opstatues; // 订单状态 1 业务已审核 2 文员已出单
	private String remark;
	private int branchid;
	private String branchname;
	private Branch branch;
	private User user; 
   
    
	public User getUser() {
		if (submitid != 0) {
			user = UserService.getMapId().get(submitid);
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOid() {
		if (StringUtill.isNull(oid)) {
			oid = "尚未生成订单";
		}
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getSubmitid() {
		return submitid;
	}

	public void setSubmitid(int submitid) {
		this.submitid = submitid;
	}

	public String getSubmittime() {
		return submittime;
	}

	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}

	public String getRemark() {
		if(StringUtill.isNull(remark)){
			remark = ""; 
		}
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getOpstatues() {
		return opstatues;
	}

	public void setOpstatues(int opstatues) {
		this.opstatues = opstatues;
	}

	public int getBranchid() {
		return branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		if(branchid != 0 ){  
			System.out.println("branchid"+branchid);
			branchname = BranchService.getMap().get(branchid).getLocateName();
		}  
		return branchname; 
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public Branch getBranch() {
		if(branchid != 0 ){  
			branch = BranchService.getMap().get(branchid);
		} 
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}
 
	
}
