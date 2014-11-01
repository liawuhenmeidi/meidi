package orderproduct;

import java.util.List;
import java.util.Map;

public class OrderProductService {
	public static Map<Integer,List<OrderProduct>> OrPMap ;
	public static boolean flag = false ;
	 
	public static Map<Integer,List<OrderProduct>> getStaticOrderStatuesM(){
		init(); 
		if(OrPMap == null){
			OrPMap = OrderProductManager.getOrderStatuesM();
		}
		return OrPMap;
	}
	
	public static void init(){
		if(flag){
			OrPMap = OrderProductManager.getOrderStatuesM();
		}
		flag = false ;
	}
}
