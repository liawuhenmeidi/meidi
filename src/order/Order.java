package order;

import gift.Gift;
import gift.GiftManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import category.Category;
import category.CategoryManager;

import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import user.User;
 
public class Order {
  
 public static int query = 1;  
 public static int unquery = 0 ;
	    
 public static int orderPrint = 1; 
 public static int orderDispatching  = 2; 	
 public static int serach = 3 ;	  
 public static int charge = 4 ;	   
 public static int come = 5 ;  
 public static int go = 6 ;	   
 public static int over = 7;  
 public static int dingma = 8;   
 public static int deliveryStatuesTuihuo = 9 ; 
 public static int callback = 10 ;   
 public static int porderDispatching = 11 ;
 public static int pserach = 12 ;
 public static int porderPrint = 13 ;
 public static int pcharge = 14 ; 
 public static int pinstall = 15 ; 
 public static int pinstallprintln = 16 ;
 public static int pchargepaisong = 17 ; 
 public static int neworder = 18 ;
 public static int motify = 19 ;
 public static int release = 20 ; 
 public static int returns = 21 ; 
 public static int dispatch = 22 ;
 public static int installonly = 23 ;
 public static int chargeall = 24 ; 

 public static int orderpeisong= 1;
 public static int orderinstall = 2; 
 public static int orderreturn = 3;   
 public static int ordersong = 4; 
 public static int orderinsta = 4;
  
  private int id ;
  
  private int productCategory; // 产品类别
  
  private String productType;    // 产品型号
  
  private String pos ;          //  pos 单号

  private String userMessage;   //个人信息
  private String locate;        // 地址
  private String locateDetail;  // 详细地址
  
  private String odate ;    //  预约日期
   
  private String otime;     //预约时间
  
  private int saleID ;     // 提交表单人员
  
  private String sailId;    // 销售单号
  
  private String check ;    // 效验码
  
  private String saleTime;   // 销售日期
  
  private int printSatues;   //打印状态     0  未打印   1 打印

  private String mail ;    // 邮箱 
   
  private int shipmentStatues;   //出货状态     0 未出货     1 出货    
    
  private int deliveryStatues;    //送货状态  0 表示未送货   1 表示已送货   2 送货成功（送货安装） 
                                   // 3  表示退货  (无安装和派送)  4  表示退货（送回） 5 表示退货  （安装）
                                   //   7 安装同意释放了     // 8 已自提      9  只安装(门店提货)  10 只安装(顾客已提)
     
  private int deliverytype ;      //   1 表示是一个人既送货又安装   2  表示送货安装不是一个人
  
  private int sendId;          // 送货员  
   
  private String sendtime ;
  
  private String username;    //  用户名
  
  private String phone1;   // 电话1
   
  private String phone2;    // 电话2
  
  private String remark;
  
  private String branch;
  
  private int dealsendId ;
  
  private int statues1 ; 
  private int statues2 ;
  private int statues3 ;
  private int statues4 ;  //是否给安装公司结款 
  private int printSatuesP;
  
  private int statuesPaigongQuery;  // 派工是否收到代理公司款项
  
  private int statuesPaigong;     // 派工是否给送货员结账 
    
  private int statuesinstall;     // 派工是否给送货员结账     0  表示没有结款   1  表示已结款      2 表示已忽略
  
  private int statuesSonghuo;     //  送货员是否收到货款
   
  private int statuesDingma; 
  
  private int installid ;
  
  private int posremark;
  
 private String submitTime;
  
  private String printlnid;
  
  private int statuescallback;
   
  private int dayremark ;   // 每天的编号 

  private int dayID;  // 标记是周几
    
  private int phoneRemark ;  // 1 表示有相同的电话
  
  private int reckedremark;
  
  private int sailidrecked; 
  
  private String installtime ;
  
  private int returnid ;
   
  private int returnprintstatues; 
  
  private int returnwenyuan ; 
   
  private String returntime ; 
    
  private int printdingma ;   //  0 顶码未打印      1 顶码打印  
  
  private String dealSendTime;
  
  private int wenyuancallback;
  
  private String imagerUrl ; 
  

  
  public String getImagerUrl() {
	return imagerUrl;
}

public void setImagerUrl(String imagerUrl) {
	this.imagerUrl = imagerUrl;
}

public int getWenyuancallback() {
	return wenyuancallback;
}

public void setWenyuancallback(int wenyuancallback) {
	this.wenyuancallback = wenyuancallback;
}

public String getDealSendTime() {
	return dealSendTime;
}

public void setDealSendTime(String dealSendTime) {
	this.dealSendTime = dealSendTime;
}

public int getPrintdingma() {
	return printdingma;
}

public void setPrintdingma(int printdingma) {
	this.printdingma = printdingma;
}
private int returnstatuse;   //  0 未回来    1 回来   2  安装公司同意
  
  public int getReturnwenyuan() {
	return returnwenyuan;
}

public void setReturnwenyuan(int returnwenyuan) {
	this.returnwenyuan = returnwenyuan;
}

public int getReturnprintstatues() {
	return returnprintstatues;
}

public void setReturnprintstatues(int returnprintstatues) {
	this.returnprintstatues = returnprintstatues;
}

public int getReturnid() {
	return returnid;
}

public void setReturnid(int returnid) {
	this.returnid = returnid;
}

public String getReturntime() {
	return returntime;
}

public void setReturntime(String returntime) {
	this.returntime = returntime;
}

public int getReturnstatuse() {
	return returnstatuse;
}

public void setReturnstatuse(int returnstatuse) {
	this.returnstatuse = returnstatuse;
}
  
  
  public int getStatuesinstall() {
		return statuesinstall;
	}

	public void setStatuesinstall(int statuesinstall) {
		this.statuesinstall = statuesinstall;
	}
	
  public int getPosremark() {
	return posremark;
}

public void setPosremark(int posremark) {
	this.posremark = posremark;
}

public int getReckedremark() {
	return reckedremark;
}

public void setReckedremark(int reckedremark) {
	this.reckedremark = reckedremark;
}

public int getSailidrecked() {
	return sailidrecked;
}

public void setSailidrecked(int sailidrecked) {
	this.sailidrecked = sailidrecked;
}

    
  public int getDeliverytype() {
	return deliverytype;
}

public void setDeliverytype(int deliverytype) {
	this.deliverytype = deliverytype;
}

public String getSendtime() {
	return sendtime;
}

public void setSendtime(String sendtime) {
	this.sendtime = sendtime;
}

public String getInstalltime() {
	return installtime;
}

public void setInstalltime(String installtime) {
	this.installtime = installtime;
}

  
public int getPhoneRemark() {
	return phoneRemark;
}

public void setPhoneRemark(int phoneRemark) {
	this.phoneRemark = phoneRemark;
}

public int getDayID() {
	return dayID;
} 
 
public void setDayID(int dayID) {
	this.dayID = dayID;
}

public int getDayremark() {
	return dayremark;
}

public void setDayremark(int dayremark) {
	this.dayremark = dayremark;
}

public int getStatuescallback() {
	return statuescallback;
} 

public void setStatuescallback(int statuescallback) {
	this.statuescallback = statuescallback;
}

public String getPrintlnid() {
	return printlnid;
}
 
public void setPrintlnid(String printlnid) {
	this.printlnid = printlnid;
}

public String getSubmitTime() {
	return submitTime;
}

public void setSubmitTime(String submitTime) {
	this.submitTime = submitTime;
}

public int getInstallid() {
	return installid;
}

public void setInstallid(int installid) {
	this.installid = installid;
}

public int getStatuesDingma() {
	return statuesDingma;
}

public void setStatuesDingma(int statuesDingma) {
	this.statuesDingma = statuesDingma;
}

public int getStatuesPaigongQuery() {
	return statuesPaigongQuery;
   }

   public void setStatuesPaigongQuery(int statuesPaigongQuery) {
	  this.statuesPaigongQuery = statuesPaigongQuery;
    } 
 
  public int getStatuesPaigong() {
	  return statuesPaigong;
  }

public void setStatuesPaigong(int statuesPaigong) {
	this.statuesPaigong = statuesPaigong;
}

public int getStatuesSonghuo() {
	return statuesSonghuo;
}

public void setStatuesSonghuo(int statuesSonghuo) {
	this.statuesSonghuo = statuesSonghuo;
}

public int getPrintSatuesP() {
	return printSatuesP;
} 

public void setPrintSatuesP(int printSatuesP) {
	this.printSatuesP = printSatuesP;
}

public int getStatues1() {
	return statues1;
}

public void setStatues1(int statues1) {
	this.statues1 = statues1;
}

public int getStatues2() {
	return statues2;
}

public void setStatues2(int statues2) {
	this.statues2 = statues2;
}

public int getStatues3() {
	return statues3;
}

public void setStatues3(int statues3) {
	this.statues3 = statues3;
}

public int getStatues4() {
	return statues4;
}

public void setStatues4(int statues4) {
	this.statues4 = statues4;
}

  
  
  public int getDealsendId() {
	return dealsendId;
} 

public void setDealsendId(int dealsendId) {
	this.dealsendId = dealsendId;
}

public String getCategoryID() {
	return categoryID;
}

public void setCategoryID(String categoryID) {
	this.categoryID = categoryID;
}
private String categoryID; // 对应的产品类别
  
  public String getBranch() {
	return branch;
}

public void setBranch(String branch) {
	this.branch = branch;
}
private List<OrderProduct> Orderproduct = new ArrayList<OrderProduct>();
  
  public List<OrderProduct> getOrderproduct() {
	return Orderproduct;
}

public void setOrderproduct(List<OrderProduct> orderproduct) {
	Orderproduct = orderproduct;
}

public List<Gift> getOrdergift() {
	return ordergift;
}

public void setOrdergift(List<Gift> ordergift) {
	this.ordergift = ordergift;
}

private List<Gift> ordergift = new ArrayList<Gift>();

  public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public String getPhone1() {
	return phone1;
}

public void setPhone1(String phone1) {
	this.phone1 = phone1;
}

public String getPhone2() {
	return phone2;
}

public void setPhone2(String phone2) {
	this.phone2 = phone2;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getPos() {
	return pos;
  }
  
  public void setPos(String pos) {
	this.pos = pos;
 }
  public String getCheck() {
	return check;
}
public void setCheck(String check) {
	this.check = check;
}
public String getSailId() {
	return sailId;
}
public void setSailId(String sailId) {
	this.sailId = sailId;
}

  public String getOdate() {
	return odate;
}
public void setOdate(String odate) {
	this.odate = odate;
}
public String getOtime() {
	return otime;
}
public void setOtime(String otime) {
	this.otime = otime;
}
public int getShipmentStatues() {
	return shipmentStatues;
}
public void setShipmentStatues(int shipmentStatues) {
	this.shipmentStatues = shipmentStatues;
}
public int getDeliveryStatues() {
	return deliveryStatues;
}
public void setDeliveryStatues(int deliveryStatues) {
	this.deliveryStatues = deliveryStatues;
}
public int getSendId() {
	return sendId;
}
public void setSendId(int sendId) {
	this.sendId = sendId;
}
	public int getPrintSatues() {
		return printSatues;
	}
	public void setPrintSatues(int printSatues) {
		this.printSatues = printSatues;
	}
  
  public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	public String getLocate() {
		return locate;
	}
	public void setLocate(String locate) {
		this.locate = locate;
	}
  public String getLocateDetail() {
	return locateDetail;
}
public void setLocateDetail(String locateDetail) {
	this.locateDetail = locateDetail;
}
public String getMail() {
	return mail;
}
public void setMail(String mail) {
	this.mail = mail;
}

  public int getProductCategory() {
	return productCategory;
}
public void setProductCategory(int productCategory) {
	this.productCategory = productCategory;
}
public String getProductType() {
	return productType;
}
public void setProductType(String productType) {
	this.productType = productType;
}//送货人
  public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int  getSaleID() {
	return saleID;
}
public void setSaleID(int saleID) {
	this.saleID = saleID;
}
public String getSaleTime() {
	return saleTime;
}
public void setSaleTime(String saleTime) {
	this.saleTime = saleTime;
}
public String getDate() {
	return odate;
}
public void setDate(String date) {
	this.odate = date;
}
public String getTime() {
	return otime;
}
public void setTime(String time) {
	this.otime = time;
}
 
 
public String getCategory(){   
	String category = ""; 
	List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(this.getId());
	HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
	 if(null != lists){
		for(int g = 0 ;g<lists.size();g++){   
			String tempType = categorymap.get(Integer.valueOf(lists.get(g).getCategoryId())).getName();
			category += (tempType == null || tempType.equals("null"))?"":tempType;
		}
	 } 
	return category;  
}
// 赠品
 public String getGifttype(String decollator){    
	String category = "";   
	Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(new User());
	 List<Gift> lists = gMap.get(this.getId());  
	 if(null != lists){
		     for(int g = 0 ;g<lists.size();g++){
		    	 Gift op = lists.get(g);
		    	 if(null !=op ){
		    		 category += decollator + op.getName();
		    	 }
		     } 
		}
	return category;  
}

 public String getGifcount(String decollator){   
		String category = "";   
		Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(new User());
		 List<Gift> lists = gMap.get(this.getId()); 
		 if(null != lists){ 
			     for(int g = 0 ;g<lists.size();g++){
			    	 Gift op = lists.get(g);
			    	 if(null !=op ){
			    		 category += decollator +  op.getCount();
			     }
			}
		 } 
		return category;  
	}
  
 public String getGifStatues(String decollator){   
		String category = "";   
		Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(new User());
		 List<Gift> lists = gMap.get(this.getId()); 
		 if(null != lists){ 
			     for(int g = 0 ;g<lists.size();g++){
			    	 Gift op = lists.get(g);
			    	 if(null !=op ){
			    		 if(0==op.getStatues()){
			    			 category = "需配送";
				         }else if(1 == op.getStatues()) {
				        	 category = "已自提";
				         }else if(9 == op.getStatues()) { 
				        	 category = "只安装(门店提货)";
				         }else if(10 == op.getStatues()) {
				        	 category = "只安装(顾客已提)";
				         } 
			    		 category += decollator +  op.getCount();
			    	 } 
			    		
			     } 
			}
		return category;  
	}
 
public String getCategory(int statues,String decollator){   
	String category = "";  
	List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(this.getId());
	HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){     
			String tempType = categorymap.get(Integer.valueOf(lists.get(g).getCategoryId())).getName();
			if(lists.get(g).getStatues() == statues){
			   category += decollator + ((tempType == null || tempType.equals("null"))?"":tempType);
			} 
		} 
	}
	return category; 
}
// 是否是顶码
public String getSendType(){
	String sendType = "";
	List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(this.getId());
	 if(null != lists){	
		for(int g = 0 ;g<lists.size();g++){
				String tempType = lists.get(g).getSendType();
					sendType += (tempType == null || tempType.equals("null"))?"":tempType;
			}
	 }
	return sendType;
}

public String getSendType(int statues,String decollator){ 
	String sendType = ""; 
	List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(this.getId());
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){   
				if(statues == 1){          
					String tempType = lists.get(g).getSaleType();   
					sendType += decollator + ((tempType == null || tempType.equals("null"))?"":tempType);
				}else {
					String tempType = lists.get(g).getSendType(); 
					sendType += decollator + ((tempType == null || tempType.equals("null"))?"":tempType);
				   }
				}
			   
			}
       }
	return sendType;
}

public int getSendCount(){
	int sendCount = 0;
	if(!OrderProductManager.getStaticOrderStatuesM().containsKey(this.getId())){
		OrderProductManager.resetOrPMap();
	}
	List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(this.getId());
	 if(null != lists){
		for(int g = 0 ;g<lists.size();g++){
			  sendCount += lists.get(g).getCount();
		}
	 }
	return sendCount;
}
 
public String getSendCount(int statues,String decollator){
	String sendCount = "";
	if(!OrderProductManager.getStaticOrderStatuesM().containsKey(this.getId())){
		OrderProductManager.resetOrPMap();
	}
	List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(this.getId());
	 if(null != lists){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){
			   sendCount += decollator + lists.get(g).getCount();
			}
		} 
	 }
	return sendCount;
}
  
}
