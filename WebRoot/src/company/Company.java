package company;

public class Company {
  private String name ;
  private String phone ;
  private String locate;  // 地区 
  private String location; // 详细地址
 
private String username; 
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
