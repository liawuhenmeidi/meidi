package branch;

import java.util.List;
import java.util.Map;

public class BranchService {
	public static boolean flag = false ;
    public static Map<Integer,Branch>  map = null;
    public static List<Branch>  list = null; 
    public static Map<String,Branch> nameMap = null ;
   
	public static Map<Integer, Branch> getMap() {
		if(map == null || flag ){
			map = BranchManager.getIdMap();
		}
		return map ;
	}
    
	public static Map<String, Branch> getNameMap() {
		if(nameMap == null || flag ){
			nameMap = BranchManager.getNameMap();
		}
		return nameMap ;
	}
	
	public static Branch gerBranchByname(String name ){
		return getNameMap().get(name);
	} 
	
	public static List<Branch> getList() {
		if(list == null || flag ){ 
			list = BranchManager.getLocate(); 
		}
		return list ;
	}
 
   public static void restartMap() {
	  map = BranchManager.getIdMap();
   }	
   
   public static void restartList() {
	   list = BranchManager.getLocate(); 
	}
}
