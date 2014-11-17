package orderPrint;

import java.util.Map;

import user.User;

public class OrderPrintlnService {
	public static boolean flag = false ;
	
	public static Map<Integer,Map<Integer,OrderPrintln>> opmap = null ;
	
    public static Map<Integer,Map<Integer,OrderPrintln>> getmap(User user){
    	init(user); 
    	if(null == opmap){
    		 opmap = OrderPrintlnManager.getOrderStatuesMap(user);
    	}
    	return opmap;
    }  
    
    public static void init(User user){
		if(flag){
			opmap = OrderPrintlnManager.getOrderStatuesMap(user);
		}
		flag = false ;
	}
}
