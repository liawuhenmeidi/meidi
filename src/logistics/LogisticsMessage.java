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
   private int price ;  // 运费（给司机结款） 
   private String startLocate; // 起始地点  
   private int advancePrice;  // 给（聚美）垫付  
   private int statues ;   // 0 未完成  1 已完成  2 提出结款请求   3 同意结款   4 已结款   -1 被删除
   private String submittime; // 提交日期
   private String sendtime ;  // 送货时间  
   private String locateMessage;   //路线>行车记录
   private String locates;  // 送货地点    
   private String remark ; 
   private String statuesName ;
   private String advancestatuesName ;
   private int advanceStatues; 
   private int pid ;    // 关联ID
   
   public int getPid() {
	return pid;
}

public void setPid(int pid) {
	this.pid = pid;
}

public String getStatuesName() {
	   if(0 == statues){
		   statuesName = "待送货";
	   }else if(1 == statues){ 
		   statuesName = "已送货";
	   }else if(2 == statues){
		   statuesName = "请求结款";
	   }else if(3 == statues){
		   statuesName = "同意结款";
	   }else if(4 == statues){
		   statuesName = "已结款";
	   }else if(-1 == statues){
		   statuesName = "取消";
	   }
	return statuesName;
}

public String getadvancestatuesName() {
	   if(0 == advanceStatues){
		   advancestatuesName = "未结款";
	   }else if(1 == advanceStatues){ 
		   advancestatuesName = "已结款";
	   }else{
		   
	   }
	return advancestatuesName;
}

public int getAdvanceStatues() {
	return advanceStatues;
}

public void setAdvanceStatues(int advanceStatues) {
	this.advanceStatues = advanceStatues;
}

public String getStartLocate() {
	return startLocate;
}

public void setStartLocate(String startLocate) {
	this.startLocate = startLocate;
}

public int getAdvancePrice() {
	return advancePrice;
}

public void setAdvancePrice(int advancePrice) {
	this.advancePrice = advancePrice;
}

public String getLocates() {
	return locates;
}

public void setLocates(String locates) {
	this.locates = locates;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public String getLocateMessage() {
	return locateMessage;
}

public void setLocateMessage(String locateMessage) {
	this.locateMessage = locateMessage;
}

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
