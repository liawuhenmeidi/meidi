package group;

import java.util.List;

public class Group {
   //  0  为超级管理员    1 提交报装单         2 送货单   3 管理用户      4 添加产品类别    5 配单    6 抢单    
	// w 提交   r  查看     q 确认 
   public static int Manger = 0;
   public static int sale = 1;
   public static int send = 2; 
      
   public static int ManagerUser = 3;
   public static int addprodoct = 4;
   public static int dealSend = 5; 
   public static int juese = 6; 
   public static int serchBranch = 7;  
   public static int sencondDealsend = 8; 
   public static int branch = 9;   
   public static int locate = 10; 
   public static int println = 11;  
   
   
   public static int inventoryquery = 12;    // 库存查询 
   public static int inventory = 13;    // 调货单权限    w
   public static int inventoryreserve = 28;    // 预约调货单权限    w
   
   
   public static int tuihuo = 14;       // 导购提交退货申请
   public static int queryOrder = 15;       // 查询手机端导购员报装单
   public static int huanhuo = 16;       // 提交换货单 
   public static int updateOrderDealsend = 17;       // 修改导购单据
   public static int querytuihuo = 18;      // 同意导购退货
   public static int queryrelease = 19;      // 同意安装网点释放
   public static int callback = 20;      // 确认已回访
   public static int come = 21 ;     //确认厂送票已回
   public static int go = 22 ;	   //确认厂送票已销
   public static int over = 23;    //确认厂送票已结款
   public static int sallOrder = 24;    //系统对比单:
   public static int Commission = 25;    //提成标准:
   public static int sall = 27;    //销售单:
   public static int salecharge = 29;   // 与卖场匹配
   public static int dealsendcharge = 30;   // 给安装网点结款
   public static int saledcharge = 31;   // 给导购员结款
   public static int dealsendchargerule = 32;   // 给安装网点结款标准
   public static int aftersalerepare= 33;    // 预约售后待处理
   public static int aftersale= 34;    // 预约售后处理
   public static int installOrderupload = 35;    // 上报厂家安装单 
   public static int maintainOrder = 36;    // 保养单  
   public static int faultOrder = 37;    // 维修单   
   public static int aftersaleCharge= 39;    // 售后结款    
   public static int change = 38;    // 型号转化与门店转化权限  
    //   从 15开始  
   private int id ;  
   private String name;
   private String detail; 
   
   private int ptype; 
   
// 0 为未启用   1 为启用
  private int statues ;  
  
  private String permissions;
  
  private String products;
  
  private List<Integer> pid ;
  
   public int getPtype() {
	return ptype;
}
public void setPtype(int ptype) {
	this.ptype = ptype;
}


    
public List<Integer> getPid() {
	if(null == pid){
		pid = GroupService.getmappid().get(this.id);
	}
	return pid;
}
public void setPid(List<Integer> pid) {
	this.pid = pid;
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
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDetail() {
	return detail;
}
public void setDetail(String detail) {
	this.detail = detail;
}
public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}
public String getPermissions() {
	return permissions;
}
public void setPermissions(String permissions) {
	this.permissions = permissions;
}     
   
}
