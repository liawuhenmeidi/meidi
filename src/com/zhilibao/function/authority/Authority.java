package com.zhilibao.function.authority;

public class Authority {
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
	      
	   public static int ordergoods = 40;  //  订货单权限    w  可提交    q 审核       r  查看
	    //  c  生成苏宁订单      d  开单发货     e 修改实收数量    f 确认卖场入库数量
	       
	   public static int logistics = 41;   //  物流权限      p 物流配工     d（Delivery） 送货   r 查看物流记录  s(Settlement ) 结算  
	      
	   public static int TaxBasicMessage = 42 ;
	   public enum AuthorityValue{ 
	        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
	        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
	    	税务格式转化功能(TaxBasicMessage);
	           
	        private final int value;  

	        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	        AuthorityValue(int value) {
	            this.value = value;
	        } 
	        
	        public int getValue() {
	            return value;
	        }
	    }
	   
}
