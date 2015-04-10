package group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class GroupService {
     public static Map<String,List<Group>> map ;
     public static HashMap<Integer,Group> idmap ;
     public static List<Group> list ;
     public static boolean flag = false ; 
     public static Map<Integer,List<Integer>> mappid ; 
    
     public static Map<String,List<Group>> getPidMap(){
    	 init();
    	 if(map == null ){
    		 map = GroupManager.getLocateMap();
    	 }
    	 return map ;
     }
      
     public static Map<Integer,List<Integer>> getmappid(){
    	 init();
    	 if(mappid == null){
    		 mappid = GroupRelateManager.getmap();
    	 }
    	 return mappid ;
     }
      
      
     public static HashMap<Integer,Group> getidMap(){
    	 init();
    	 if(idmap == null ){   
    		 idmap = GroupManager.getGroupMap();
    	 }  
    	 return idmap ; 
     }
     
     public static List<Group> getList(){
    	 init(); 
    	 if(list == null ){
    		 list = GroupManager.getGroup();
    	 } 
    	 return list ;
     }
        
     public static void init(){
    	 if(flag == true ){ 
    		 map = GroupManager.getLocateMap();
    		 mappid = GroupRelateManager.getmap();
    		 idmap = GroupManager.getGroupMap();
    		 list = GroupManager.getGroup();
    	 }
    	 flag = false ;
     }
}
