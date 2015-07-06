package logistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.Branch;
import branch.BranchService;

import servlet.OrderServlet;
import user.User;
import user.UserService;

public class LogisticsMessage {
	protected static Log logger = LogFactory.getLog(LogisticsMessage.class);
   private int id ;
   private int carid;   // 车号
   private int uid ;   //   司机
   private int bid ;   // 目的地
   private int price ;  // 价位
   private int statues ; 
   private String submittime;
   private String sendtime ;  // 送货时间
   
    
   public String getSendtime() {
	return sendtime;
}

public void setSendtime(String sendtime) {
	this.sendtime = sendtime;
}

public User getUser(){
	   User u = null;
	   
	   if(uid != 0){
		   u = UserService.getMapId().get(uid);
	   } 
	   return u ;
   } 
      
   public  Cars getCars(){ 
	   Cars ca = null;    
	  // logger.info("CarsService.getmap()"+CarsService.getmap());
	   //logger.info(carid);  
	   if(carid != 0 ){ 
		   ca = CarsService.getmap().get(carid);
	   } 
	   return ca;
   }
    
   public Branch getBranch(){
	   Branch b = null ;
	   if(bid != 0 ){
		   b = BranchService.getMap().get(bid);
	   }
	   return b ;
   }
public String getSubmittime() {
	return submittime;
}
public void setSubmittime(String submittime) {
	this.submittime = submittime;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getCarid() {
	return carid;
}
public void setCarid(int carid) {
	this.carid = carid;
}
public int getUid() {
	return uid;
}
public void setUid(int uid) {
	this.uid = uid;
}
public int getBid() {
	return bid;
}
public void setBid(int bid) {
	this.bid = bid;
}
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}
public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}    
   
   
   
   
}
