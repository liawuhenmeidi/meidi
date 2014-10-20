package orderPrint;
    
public class OrderPrintln { 
  public static int comit = 0;    // 提交请求
  public static int comiting = 1;   
  public static int comited = 2;  //  同意请求   
  public static int uncomited = 4;    // 拒绝请求
  public static int comiteded = 3;   // 订单被修改，状态已过期  
    
  public static int huanhuo = 10;     // 换货
  public static int unmodify = 8 ;      // 导购修改申请       
  public static int modify = 0 ;      // 导购修改申请 
  public static int returns = 1 ;     //    导购退货 
  public static int release = 2 ;      // 安装公司释放     
  
  
  public static int salerelease = 3 ;  // 未送货释放                
  public static int salereleasesonghuo = 4 ;  //  已送货未安装释放  
  public static int salereleaseanzhuang = 5 ;  // 安装人员释放    
  public static int salereleasereturn = 8 ;  // 退货人员释放    
  
  
  public static int releasedispatch = 6 ;  // 文员向安装公司提交退货申请     
  public static int releasemodfy = 7 ;  // 文员向安装公司提交修改申请     
  
  private int pGroupId; 
   
  private int id; 
   
  private int type ;    //  0 表示申请修改    1 表示退货      2 配单元释放订单   3 送货员释放订单
                           
  private String message; 
  private int orderid ;
  private int statues ;
  
  public int getType() {
		return type;
	}
	  
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	public int getpGroupId() {
		return pGroupId;
	} 
	
	public void setpGroupId(int pGroupId) {
		this.pGroupId = pGroupId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getStatues() {
		return statues;
	}
	public void setStatues(int statues) {
		this.statues = statues;
	}
	
}
