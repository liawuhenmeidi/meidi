package company;

public class Company {
  private String name ;
  private String phone ;
  private String locate;  // 地区 
  private String location; // 详细地址
  private int usercount ;
  
  private String username;  
  // 苏宁供应商编码  
  public static String supply = "10056027"; // 供应商编码
  public static String supplyGM = "10003699"; //国美 
  
public int getUsercount() {
	return usercount;
}
public void setUsercount(int usercount) {
	this.usercount = usercount;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getLocate() {
	return locate;
}
public void setLocate(String locate) {
	this.locate = locate;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
	
}
