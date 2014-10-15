package group;

import java.util.List;
import java.util.Map;
 
public class GroupService {
     public static Map<String,List<Group>> map ;
     public static boolean flag = false ; 
      
     public static Map<String,List<Group>> getPidMap(){
    	 if(map == null || flag == true ){
    		 map = GroupManager.getLocateMap();
    	 }
    	 return map ;
     }
}
