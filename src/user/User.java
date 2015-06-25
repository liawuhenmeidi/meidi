package user;

import group.Group;
import group.GroupService;
import utill.StringUtill;

public class User {

	private int id;
	private String username;
	private String nickusername;
	private String userpassword;

	private String entryTime;
	private String branch; // 所属部门
	private String branchName;
	private String positions; // 职位
	private int usertype; // 所属的组 组的编号 // 系统默认2 为主管组
	private String products;
	private String phone;
	private String charge; // 主管
	private int statues; // 是否授权 0 没授权， 1 已授权
	private int chargeid;

	private String location;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getChargeid() {
		return chargeid;
	}

	public void setChargeid(int chargeid) {
		this.chargeid = chargeid;
	}

	public String getCharge() {
		return charge;
	}

	public String getChargeName() {
		if (StringUtill.isNull(charge)) {
			return "";
		} else {
			// System.out.println(UserService.getMapId().get(Integer.valueOf(charge)));
			return UserService.getMapId().get(Integer.valueOf(charge))
					.getUsername();
		}

	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public int getStatues() {
		return statues;
	}

	public void setStatues(int statues) {
		this.statues = statues;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEntryTime() {
		if(StringUtill.isNull(entryTime)){
			entryTime = ""; 
		}
		return entryTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickusername() {
		return nickusername;
	}

	public void setNickusername(String nickusername) {
		this.nickusername = nickusername;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
        
	public String getProductIDS(){
		String str = "";      
		//System.out.println(this.getId()); 
		//System.out.println(this.getUsertype());  
		Group g = GroupService.getidMap().get(this.getUsertype());
		if(null != g){
			String prodectsp = GroupService.getidMap().get(this.getUsertype()).getProducts();  
			//System.out.println(prodectsp);  
			String pp = prodectsp.replace("_", ",");  
			str = "( "+pp+" ) "; 
		}
		//System.out.println(GroupService.getidMap().get(this.getUsertype()));
		
		return str ; 
	}
}
