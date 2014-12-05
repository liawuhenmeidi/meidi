package saledealsend;

import java.util.List;

import group.Group;
import user.UserManager;

public class Saledealsend {
   private int id ;
   private int dealsendid ;
   
   private String message ;
   private String name ;
   private int givestatues ;
   private int receivestatues ;
   private String submittime;
   private String orderids;
   private List<SaledealsendMessage> list ;
   
public String getOrderids() {
	return orderids;
}
public void setOrderids(String orderids) {
	this.orderids = orderids;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getDealsendid() {
	return dealsendid;
}
public void setDealsendid(int dealsendid) {
	this.dealsendid = dealsendid;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getGivestatues() {
	return givestatues;
}
public void setGivestatues(int givestatues) {
	this.givestatues = givestatues;
}
public int getReceivestatues() {
	return receivestatues;
}
public void setReceivestatues(int receivestatues) {
	this.receivestatues = receivestatues;
}
public String getSubmittime() {
	return submittime;
}
public void setSubmittime(String submittime) {
	this.submittime = submittime;
}
public List<SaledealsendMessage> getList() {
	return list;
}
public void setList(List<SaledealsendMessage> list) {
	this.list = list;
}
   
   
}
