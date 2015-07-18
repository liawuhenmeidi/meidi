package com.zhilibao.model;

import user.User;
import user.UserService;

public class Cars {
   public int id ; 
   public int uid ;
   public String num ;
   public User getUser(){
	   User u = null; 
	   if(uid != 0 ){
		  u = UserService.getMapId().get(uid); 
	   }
	   return u ;
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
public String getNum() {
	return num;
} 
public void setNum(String num) {
	this.num = num;
} 
   
   
}
