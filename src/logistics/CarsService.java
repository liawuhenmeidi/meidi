package logistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarsService {
    public static List<Cars> list = null ;
    public static boolean flag = false ;
     
    public static Map<Integer,Cars> getmap(){
    	init();
    	Map<Integer,Cars> map = new HashMap<Integer,Cars>();
    	if(null != list){
    		for(int i=0;i<list.size();i++){
    			Cars ca  = list.get(i);
    			map.put(ca.getId(), ca);
    		}
    	}
    	return map;
    }  
    public static void getList(){
    	init();    
    	list = CarsManager.getlist();
    }   
      
    public static void init(){  
    	if(flag || null == list){ 
    		list = CarsManager.getlist();
    	}
    }
}
