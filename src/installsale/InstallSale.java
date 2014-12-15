package installsale;

import java.util.List;

public class InstallSale {
   private int id ;
   private int uid ;
   
   private int uname ;
   private int phone ;
   private int locate ;
   private int andate ;
    
   private List<InstallSaleMessage> list ;
    
  private String message ;
  private int type ;    // 1 表示安装网点   2 送货员   3 安装员  4 送货安装员
  
  
public int getType() {
	return type;
}

public void setType(int type) {
	this.type = type;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getUid() {
	return uid;
}

public void setUid(int uid) {
	this.uid = uid;
}

public int getUname() {
	return uname;
}

public void setUname(int uname) {
	this.uname = uname;
}

public int getPhone() {
	return phone;
}

public void setPhone(int phone) {
	this.phone = phone;
}

public int getLocate() {
	return locate;
}

public void setLocate(int locate) {
	this.locate = locate;
}

public int getAndate() {
	return andate;
}

public void setAndate(int andate) {
	this.andate = andate;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public List<InstallSaleMessage> getList() {
	return list;
}

public void setList(List<InstallSaleMessage> list) {
	this.list = list;
}
  
  
}
