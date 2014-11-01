package gift;

import java.util.List;
import java.util.Map;

public class GiftService {
   public static Map<Integer,List<Gift>> map ;
   public static boolean flag  = false ;
   
   public static Map<Integer,List<Gift>> getmap(){
	   init();
	   if(map == null){
		   map = GiftManager.getOrderStatues();
	   }
	  
	   return map ;
   }
   
   public static void init(){
	   if(flag){
		   map = GiftManager.getOrderStatues();
	   }
	   flag = false ;
   }
}
