package order;

import gift.Gift;
import gift.GiftService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import category.Category;

import category.CategoryService;

import orderproduct.OrderProduct;

import orderproduct.OrderProductService;
import product.ProductService;
import user.User;
import user.UserService;
import utill.StringUtill;
 
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
 public static int returns = 21 ;   // 送货员退货
 public static int dispatch = 22 ;
 public static int installonly = 23 ;
 public static int chargeall = 24 ; 
 public static int orderquery = 25 ;
 public static int huanhuo = 26 ;
 public static int tuihuo = 27 ;
 public static int repareorderDispatching = 28 ;
 public static int aftersalerepare = 29;   // 售后待处理上报美的
 public static int aftersale = 30;    // 网点上报售后待处理
 public static int aftersalesearch = 31;  // 查询 
  
 public static int aftersalesecond = 32;   // 网点配工
 public static int aftersaledeal = 33;   // 售后文员配工 
 public static int aftersalephone = 34;   // 电话回访 
 public static int aftersalecharge = 35;    // 网点上报售后待处理
 public static int aftersaledealupload= 36;     // 保养单上报
 public static int aftersaledealcharge= 37;     // 保养单结款 
 
 public static int orderpeisong= 1;  
 public static int orderinstall = 2; 
 public static int orderreturn = 3;   
 public static int ordersong = 4;   // 只安装门店提货
 public static int orderinsta = 5;   // 只安装顾客已提
  
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
                                    // 20   换货
  private int deliverytype ;      //   1 表示是一个人既送货又安装   2  表示送货安装不是一个人
  
  private int sendId;          // 送货员  
   
  private String sendtime ;
  
  private String username;    //  用户名
  
  private String phone1;   // 电话1
   
  private String phone2;    // 电话2
  
  private String remark;
  
  private int branch;
  
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
  
  private String oderStatus ;//  初始送货状态      20 表示换货      30 表示退货
  
  private String chargeDealsendtime ;
  
  private String chargeSendtime ;
  
  private String chargeInstalltime ;
  
  private Ostatues ostatues ; 
  
  private String statuesCharge;
  
  private String branchname;
  
  public String getbranchName(int branchid){
		return OrderService.getBranchName(branchid);
	} 
	
  public String getBranchname() {
	  if(StringUtill.isNull(branchname)){
		  return OrderService.getBranchName(branch);
	  }else { 
		  return branchname;
	  }
	 
}


public void setBranchname(String branchname) {
	this.branchname = branchname;
}


public String getStatuesCharge() {
	return statuesCharge;
}


public void setStatuesCharge(String statuesCharge) {
	this.statuesCharge = statuesCharge;
}


public Ostatues getOstatues(){
	 if(null == ostatues){
		 ostatues = new Ostatues(this);
	 } 
	 return ostatues;
  }
  
  
  public String getChargeDealsendtime() {
	  if(StringUtill.isNull(chargeDealsendtime)){
		  return "";
	  }
	return chargeDealsendtime;
}

public void setChargeDealsendtime(String chargeDealsendtime) {
	this.chargeDealsendtime = chargeDealsendtime;
}

public String getChargeSendtime() {
	if(StringUtill.isNull(chargeSendtime)){
		return "";
	}
	return chargeSendtime;
}

public void setChargeSendtime(String chargeSendtime) {
	this.chargeSendtime = chargeSendtime;
}

public String getChargeInstalltime() {
	if(StringUtill.isNull(chargeInstalltime)){
		return "";
	}
	return chargeInstalltime;
}

public void setChargeInstalltime(String chargeInstalltime) {
	this.chargeInstalltime = chargeInstalltime;
}

public String getOderStatus() {
	return oderStatus;
}

public void setOderStatus(String oderStatus) {
	this.oderStatus = oderStatus;
}

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
	if(StringUtill.isNull(dealSendTime)){
		return "";
	}
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
	if(StringUtill.isNull(returntime)){
		return "";
	}
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
  
public  String getdealsendName(){ 
	String str = "";
	if(this.getDealsendId() != 0){
		User user = UserService.getMapId().get(this.getDealsendId());
		if(null != user ){
			str = user.getUsername();
		}
	}
	return str ;
}
  
public  String getdealsendphone(){ 
	String str = "";
	if(this.getDealsendId() != 0){
		User user = UserService.getMapId().get(this.getDealsendId());
		if(null != user ){
			str = user.getPhone();
		}
	}
	return str ;
}

public  String getsendName(){ 
	String str = "";
	if(this.getSendId() != 0){
		User user = UserService.getMapId().get(this.getSendId());
		if(null != user ){
			str = user.getUsername();
		}
	} 
	return str ;
}

public  String getinstallName(){ 
	String str = "";
	if(this.getInstallid() != 0){
		User user = UserService.getMapId().get(this.getInstallid() );
		if(null != user ){
			str = user.getUsername();
		}
	}
	return str ;
}

public  String getreturnName(){ 
	String str = "";
	if(this.getReturnid() != 0){
		User user = UserService.getMapId().get(this.getReturnid());
		if(null != user ){
			str = user.getUsername();
		}
	}
	return str ;
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
	if(StringUtill.isNull(sendtime)){
		return "";
	}
	return sendtime;
}

public void setSendtime(String sendtime) {
	this.sendtime = sendtime;
}

public String getInstalltime() {
	if(StringUtill.isNull(installtime)){
		return "";
	}
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
  
public int getBranch() {
	return branch;
}

public void setBranch(int branch) {
	this.branch = branch; 
}
private List<OrderProduct> Orderproduct = null; 
  
  public List<OrderProduct> getOrderproduct() {
	  if(null == Orderproduct){
		  Orderproduct = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	  }
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
  
public String getPhone() {
	return phone1+"</p>"+phone2; 
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
	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	HashMap<Integer,Category> categorymap = CategoryService.getmap();
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
	Map<Integer,List<Gift>> gMap = GiftService.getmap();
	
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
		Map<Integer,List<Gift>> gMap = GiftService.getmap();
		
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
		Map<Integer,List<Gift>> gMap = GiftService.getmap();
		
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
			    		 category += decollator;
			    	 } 
			    		
			     } 
			}
		return category;  
	}
   
public String getCategory(int statues,String decollator){   
	String category = "";  

	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	HashMap<Integer,Category> categorymap = CategoryService.getmap();
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

public String getCategoryid(int statues,String decollator){   
	String category = "";  

	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){      
			//String tempType = categorymap.get(Integer.valueOf(lists.get(g).getCategoryId())).getName();
			if(lists.get(g).getStatues() == statues){
			   category += decollator + lists.get(g).getCategoryId();
			} 
		} 
	}
	return category; 
}

// 是否是顶码
public String getSendType(){
	String sendType = "";
    try{
    	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
    	if(null != lists){	
    		for(int g = 0 ;g<lists.size();g++){
    				if(lists.get(g).getStatues() == 1){ 
    					sendType = "";
    					String tempType = lists.get(g).getSaleType();
    					//System.out.println(tempType);
    					tempType = ProductService.getIDmap().get(Integer.valueOf(tempType)).getType();
    					sendType += ((tempType == null || tempType.equals("null"))?"":tempType) +"|" ;
    					return sendType;
    				}else{
    					String tempType = lists.get(g).getSendType(); 
    					tempType = ProductService.getIDmap().get(Integer.valueOf(tempType)).getType();
    					sendType += ((tempType == null || tempType.equals("null"))?"":tempType) +"|"  ;
    				}
    				   
    		}   
    	} 
    }catch(Exception e){
    	return "";
    }
	
	 return sendType;
}
	

public String getSendType(int statues,String decollator){ 
	String sendType = ""; 

	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){   
				if(statues == 1){ 
					String tempType = lists.get(g).getSaleType(); 
					tempType = ProductService.getIDmap().get(Integer.valueOf(tempType)).getType();
					sendType += decollator + ((tempType == null || tempType.equals("null"))?"":tempType);
				}else { 
					String tempType = lists.get(g).getSendType();  
					//System.out.println(lists.get(g).getId()+tempType); 
					tempType = ProductService.getIDmap().get(Integer.valueOf(tempType)).getType();
					sendType += decollator + ((tempType == null || tempType.equals("null"))?"":tempType);
				   }
				}
			   
			}
       }
	return sendType;
}

public String getSendTypejson(int statues){ 
	String sendType = "["; 

	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){   
				int opid = lists.get(g).getId();
				if(statues == 1){ 
					String tempType = lists.get(g).getSaleType();
					
					String realtempType = ProductService.getIDmap().get(Integer.valueOf(tempType)).getType();
					sendType += "{\"id\":\""+opid+"\",\"name\":\""+realtempType+"\"},";
				}else {  
					String tempType = lists.get(g).getSendType(); 
					String realtempType = ProductService.getIDmap().get(Integer.valueOf(tempType)).getType();
					sendType += "{\"id\":\""+opid+"\",\"name\":\""+realtempType+"\"},";
				   } 
				}
			   
			}
       }
	
	sendType = sendType.substring(0, sendType.length()-1)+"]";
	return sendType;
}

public String getSendTypeID(int statues,String decollator){ 
	String sendType = ""; 

	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){   
				if(statues == 1){ 
					String tempType = lists.get(g).getSaleType(); 
					sendType += decollator + tempType;
				}else {
					String tempType = lists.get(g).getSendType(); 
					sendType += decollator + tempType;
				   }
				}
			   
			}
       }
	return sendType;
}

public String getSize(int statues,String decollator){ 
	String sizes = ""; 

	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	if(lists != null){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){   
				if(statues == 1){ 
					String tempType = lists.get(g).getSaleType(); 
					double size = ProductService.getIDmap().get(Integer.valueOf(tempType)).getSize();
					sizes += decollator + size;
				}else {
					String tempType = lists.get(g).getSendType(); 
					double size = ProductService.getIDmap().get(Integer.valueOf(tempType)).getSize();
					sizes += decollator + size;
				   }
				}
			   
			}
       }
	return sizes; 
}

public String getSendCount(){
	String sendCount = "";
	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	 if(null != lists){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == 1){ 
				sendCount = "" ;
				String tempCount = String.valueOf(lists.get(g).getCount());   
				sendCount += ((tempCount == null || tempCount.equals("null"))?"":tempCount)+"|";
				return sendCount;
			}else{
				String tempCount = String.valueOf(lists.get(g).getCount()); 
				sendCount += ((tempCount == null || tempCount.equals("null"))?"":tempCount) +"|";
			}
		} 
	 }
	return sendCount;
}
 

public String getSendCount(int statues,String decollator){
	String sendCount = "";
	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	 if(null != lists){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){
			   sendCount += decollator + lists.get(g).getCount();
			}
		} 
	 }
	return sendCount;
}
  
public String getSendprice(int statues,String decollator){
	String sendCount = "";
	List<OrderProduct> lists = OrderProductService.getStaticOrderStatuesM().get(this.getId());
	 if(null != lists){
		for(int g = 0 ;g<lists.size();g++){
			if(lists.get(g).getStatues() == statues){
			   sendCount += decollator + lists.get(g).getPrice();
			}
		} 
	 }
	return sendCount;
}


public void clear() {
	this.branch = -1;
	this.id = -1;
	this.pos = "";
	this.saleTime = "";
}


//以下是对比页面优化专用
private String sendTypeForCompare = null;
private String sendCountForCompare = null;
private String shopNameForCompare = null;


public String getSendTypeForCompareWidthOutCharactar() {
	if(sendTypeForCompare == null){
		sendTypeForCompare = getSendType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "");;
	}
	return sendTypeForCompare;
}

public String getSendCountForCompare() {
	if(sendCountForCompare == null){
		sendCountForCompare = getSendCount();
	}
	return sendCountForCompare;
}

public String getShopNameForCompare() {
	if(shopNameForCompare == null){
		shopNameForCompare = getbranchName(getBranch());
	}
	return shopNameForCompare;
}

//uploadOrder 向Order转换时候使用
private boolean fromUploadOrder = false;
private String shopname_upload = "";
private int salenum_upload;
private String saleType_upload = "";

public boolean isFromUploadOrder() {
	return fromUploadOrder;
}

public void setFromUploadOrder(boolean fromUploadOrder) {
	this.fromUploadOrder = fromUploadOrder;
}

public String getShopname_upload() {
	return shopname_upload;
}

public void setShopname_upload(String shopname_upload) {
	this.shopname_upload = shopname_upload;
}

public int getSalenum_upload() {
	return salenum_upload;
}

public void setSalenum_upload(int salenum) {
	this.salenum_upload = salenum;
}

public String getSaleType_upload() {
	return saleType_upload;
}

public void setSaleType_upload(String saleType) {
	this.saleType_upload = saleType;
}


public  String getprint(){
	//打印状态     0  未打印   1 打印
	String str = "";
	if(0 == this.getPrintSatues()){
	   str = "未打印";
     }else if(1 == this.getPrintSatues()){
	 str = "已打印";
     }
	return str ;
	
}

public  String getprintp(){
	//打印状态     0  未打印   1 打印
	String str = "";
	if(0 == this.getPrintSatuesP()){
	   str = "未打印";
     }else if(1 == this.getPrintSatuesP()){
	 str = "已打印";
     }
	return str ;
	
}

public boolean isDiangma(){
	   boolean flag = false ;
	   String str = this.getSendType(1, "");
	   if(!StringUtill.isNull(str)){
		   flag = true ;
	   }
	   return flag ;
}

public boolean isreturn(){
	boolean flag = false ;
	if(this.getDeliveryStatues() == 3 || this.getDeliveryStatues() == 4 || this.getDeliveryStatues() == 5 || this.getDeliveryStatues() == 11 || this.getDeliveryStatues() == 12 || this.getDeliveryStatues() == 13){
		flag = true ;
	}
	return flag ;
	
}





}
