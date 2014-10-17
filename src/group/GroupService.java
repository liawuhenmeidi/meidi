package group;

import java.util.List;
import java.util.Map;
 
public class GroupService {
     public static Map<String,List<Group>> map ;
     public static List<Group> list ;
     public static boolean flag = false ; 
      
     public static Map<String,List<Group>> getPidMap(){
    	 if(map == null || flag == true ){
    		 map = GroupManager.getLocateMap();
    	 }
    	 return map ;
     }
     
     public static List<Group> getList(){
    	 if(list == null || flag == true ){
    		 list = GroupManager.getGroup();
    	 }
    	 return list ;
     }
}
