package group;

public class Group {
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
   public static int inventory = 12;    // 提交调货单权限 
   public static int inventoryquery = 13;    // 确认调货单权限  
   public static int tuihuo = 14;  
    //   从 15开始 
   private int id ;  
   private String name;
   private String detail; 
   private int pid ;
   
   private int ptype; 
   
   public int getPtype() {
	return ptype;
}
public void setPtype(int ptype) {
	this.ptype = ptype;
}
public int getPid() {
	return pid;
}
public void setPid(int pid) {
	this.pid = pid;
}
//  0 为未启用   1 为启用
   private int statues ;  
   //  0  为超级管理员    1 提交报装单         2 送货单   3 管理用户      4 添加产品类别    5 配单    6 抢单
   private String permissions;
   
   private String products;
   
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
