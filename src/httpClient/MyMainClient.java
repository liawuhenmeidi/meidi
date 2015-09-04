package httpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import httpClient.download.InventoryBadGoodsDownLoad;
import httpClient.download.InventoryDownLoad;
import httpClient.download.InventoryModelDownLoad;
import httpClient.download.OrderDownLoad;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import utill.TimeUtill;

public class MyMainClient { 
	static public Map<String,String> map = new HashMap<String,String>(); 
	
	  
	protected static Log logger = LogFactory.getLog(MyMainClient.class);
	public static MyMainClient instance = new MyMainClient();
	private static BasicCookieStore cookieStore = new BasicCookieStore();
  
	/*
	 * private CloseableHttpClient httpclient = HttpClients.custom()
	 * .setDefaultCookieStore(cookieStore).build();
	 */
   
	/*
	 * public void setHttpclient(CloseableHttpClient httpclient) {
	 * this.httpclient = httpclient; }
	 */   
    // 圣荣   
	private static String cacheUsername = "haoyuely@163.com";
	private static String cachePassword = "sn8888sy"; 
	private static String verifyCode = "";
	private static String uuid = "";
       
	// 九阳  
	// private static String cacheUsername = "tjhdlshm@163.com";
	// private static String cachePassword = "lxn123456";
      
	//大港倾心 
	//private static String cacheUsername = "qxgs2014@163.com";
	//private static String cachePassword = "qingxin2014";

	private static String cacheLoginURL = "https://passport.suning.com/ids/login";

	public static MyMainClient getinstance() {
		return instance;
	}
  
	public static void main(String[] args) {

		MyMainClient mc = getinstance();
		// String branch = StringUtill.toUtf8String("天津宁河店");
		// branch = "天津宁河店";
		// branch = "";
		mc.run();   
		// List<String> s = InventorySN.getinventory("122707928","9367");
		// List<String> s = InventorySN.getinventoryModel("105057044",branch,);
		// List<String> s = InventorySN.getinventoryModel("105057044","");
		// logger.info(s);
	}    
                  
	public  synchronized Map<String,String>  run() {
		try {                             
		   startinventoryIN(this);
		 
			//round(this);      
			// sdi.selectDeliverInform(new
			// URI("http://scs.suning.com/sps/PurchaseOrderDelivery/orderDelivery.action?menuid=100000111&scspageexptime=1427005676454"),"","",this);
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 
		  return map; 
	}      
     
	public synchronized void initInventoryInAndOut(String time){
		
		 
		 
		 
		int max = TimeUtill.getMax(time);
		int ro = 6 ;
		if(max < 30){ 
			ro = 5 ; 
		} 
		 for(int i=1;i<ro;i++){ 
			 
			 String starttime =  time+"-"+getStartday(i);
			 
			   
			 String endtime = ""; 
			 if(i<5){
				 endtime = time+"-"+getendday(i);
			 }else { 
				 endtime =time + "-"+max ; 
			 }
			  
			    
			  
			//	inventoryOut.get(starttime, endtime);  
				                     
			
			//	inventoryModelOut.get(starttime, endtime);
				      
		      
			    inventoryIN.get(starttime, endtime);  
			 
			 
			 
			 
			 logger.info(starttime+"***"+endtime);
			 
		 }
		//System.out.print("initInventoryInAndOut:"+time); 
		
	}
	
	private static String getendday(int i ){
		int m = (6*(i)+i);
		if(m<10){ 
			return "0"+m;
		}
		return m+"" ;
	}
	
	 
	private static String getStartday(int i ){
		int m = (6*(i-1)+i);
		if(m<10){ 
			return "0"+m;
		}
		return m+"" ;
	}
	
	public  synchronized Map<String,String>  run(Map<String,String> maps) {
		try { 
			map.clear(); 
			verifyCode = maps.get("verifyCode"); 
		   startinventoryIN(this); 
		   
			//round(this);        
			// sdi.selectDeliverInform(new
			// URI("http://scs.suning.com/sps/PurchaseOrderDelivery/orderDelivery.action?menuid=100000111&scspageexptime=1427005676454"),"","",this);
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		  return map; 
	}     
	
	public  void refresh(){
		String starttime = TimeUtill.getdateString();
		String endtime = TimeUtill.getdateString();
                               
		endtime = TimeUtill.getdateString(); 
		starttime = TimeUtill.dataAdd(endtime, -8);       
		while (TimeUtill.compare(starttime,"2013-01-01"))  {                             
			int num = -10;                               
		      
		    inventoryOrder.getinventoryOut(starttime, endtime,this);
		    inventoryOrder.getinventoryOutModel(starttime, endtime, this); 
			 
			
			starttime = TimeUtill.dataAdd(starttime, num);
			endtime = TimeUtill.dataAdd(endtime, num);
			   
		} 
	}
	
	
	public static void round(MyMainClient mc) {
		String starttime = TimeUtill.getdateString();
		String endtime = TimeUtill.getdateString();
                                    
		//starttime = "2015-08-01";    
		//endtime = "2015-08-09";         
		while (TimeUtill.compare(starttime,"2013-07-30"))  {                             
			int num = -10;       
			//SaleDownLoad.saveDB(starttime, endtime);  
		//	count++;              
			///inventoryOut.get(starttime, endtime);  
			//inventoryModelOut.getinventoryOutModel(start, endtime, mc);
			//inventoryModelOut.get(starttime, endtime);
			     
	       // inventoryIN.getinventoryIN(start, endtime, mc);
		  // inventoryIN.get(starttime, endtime);  
			inventoryOrder.getinventoryOut(starttime, endtime, mc);
			inventoryOrder.getinventoryOutModel(starttime, endtime, mc);
			
			
			starttime = TimeUtill.dataAdd(starttime, num);
			endtime = TimeUtill.dataAdd(endtime, num);
			   
		} 
	}     
       
	//  库存下载   
	public void Inventory(){
		// 库存                          
		InventoryDownLoad.save();   
         
		InventoryModelDownLoad.save();
		// 坏品    
		InventoryBadGoodsDownLoad.save();
	} 
	
	
	public synchronized void startinventoryIN(MyMainClient mc) {
		try {     
			String starttime = TimeUtill.getdateString(); 
			//String start = TimeUtill.dataAdd(starttime, -6); 
			String endtime = TimeUtill.getdateString();  
			   
			Inventory(); 
			 
			// int count = 0 ;  
			//starttime = "2015-08-09"; 
			//endtime = "2015-08-09";       
                                   
			// 异常退货订单           
			//inventoryOrder.getinventoryOut(starttime, endtime, mc);
			//inventoryOrder.getinventoryOutModel(starttime, endtime, mc);
                    
			// 入库退货信息            
                    
			//inventoryOut.getinventoryOut(start, endtime, mc); 
			inventoryOut.get(starttime, endtime);  
			                     
			//inventoryModelOut.getinventoryOutModel(start, endtime, mc);
			inventoryModelOut.get(starttime, endtime);
			      
	       // inventoryIN.getinventoryIN(start, endtime, mc);
		    inventoryIN.get(starttime, endtime);  
			// 开始    
			
			
			// InventoryModelDownLoad.saveDB();
			
			 //InventoryChange.compare("2015-05-01","2015-05-03");
     
			// 更新过期订单(系统内)   
			//InventoryBranchManager.initOrderNumSN();
       
			logger.info("更新订单加订单号"); 
			// 更新订单信息    
			
			
			OrderDownLoad.saveDB(starttime, endtime);
			
			
           //           
			//ProductSN.save(mc); 
			// 销售数据                            
			//SaleDownLoad.saveDB(starttime, endtime);  
			// 样机退货数据未采集
		} catch (Exception e) {
			logger.info(e);
		} 
	}
    

	public static CloseableHttpClient getHttpclient() {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		return httpclient;
	}

	public static String getCacheUsername() {
		return cacheUsername;
	}

	public static String getCachePassword() {
		return cachePassword;
	}

	public static String getCacheLoginURL() {
		return cacheLoginURL;
	}

	public static BasicCookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(BasicCookieStore cookieStore) {
		MyMainClient.cookieStore = cookieStore;
	}

	public static String getVerifyCode() {
		return verifyCode;
	}

	public static void setVerifyCode(String verifyCode) {
		MyMainClient.verifyCode = verifyCode;
	}

	public static String getUuid() {
		return uuid;
	}

	public static void setUuid(String uuid) {
		MyMainClient.uuid = uuid;
	}
	
	
}
