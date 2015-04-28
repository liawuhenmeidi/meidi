package httpClient;
  
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.BasicCookieStore; 
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import utill.TimeUtill;
     
public class MyMainClient{             
	protected static Log logger = LogFactory.getLog( MyMainClient.class);
	public static MyMainClient instance = new MyMainClient();
	private static BasicCookieStore cookieStore = new BasicCookieStore(); 
	private static CloseableHttpClient httpclient = HttpClients.custom() 
    .setDefaultCookieStore(cookieStore)    
    .build();        
	private static String cacheUsername = "haoyuely@163.com";
	private static String cachePassword = "sn26524316";
	private static String cacheLoginURL = "https://passport.suning.com/ids/login";
	     
	public static MyMainClient getinstance(){
		return instance;     
	}    
	public static void main(String[] args) { 
		         
		MyMainClient mc = getinstance();     
		//String branch = StringUtill.toUtf8String("天津宁河店"); 
		// branch = "天津宁河店";   
		//branch = "";     
		mc.run();                     
		// List<String> s = InventorySN.getinventory("122707928","9367");
		//List<String> s = InventorySN.getinventoryModel("105057044",branch,); 
		//List<String> s = InventorySN.getinventoryModel("105057044","");
		//logger.info(s);      
	}	        
	    
	public synchronized void run(){      
		try {   
			startinventoryIN(this); 
			//sdi.selectDeliverInform(new URI("http://scs.suning.com/sps/PurchaseOrderDelivery/orderDelivery.action?menuid=100000111&scspageexptime=1427005676454"),"","",this);
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}     
	}           
                    
	public synchronized void startinventoryIN(MyMainClient mc){ 
		try {             
			String starttime = TimeUtill.getdateString();     
			String endtime  = TimeUtill.getdateString();   
			//starttime = "2015-04-01";            
			//endtime = "2015-04-08";                
			                          
			inventoryOrder.getinventoryOut(starttime,endtime,mc);
			inventoryOrder.getinventoryOutModel(starttime,endtime,mc);
			            
			inventoryIN.getinventoryOut(starttime,endtime,mc); 
			inventoryIN.getinventoryOutModel(starttime,endtime,mc); 
			inventoryIN.getinventoryIN(starttime,endtime,mc);
			                     
			 
		   // 样机退货数据未采集 
		} catch (Exception e) {    
			logger.info(e); 
		}   
	} 
	
	public static CloseableHttpClient getHttpclient() {
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
}
