package branch;

import java.util.List;
import java.util.Map;

public class BranchService {
	public static boolean flag = false ;
    public static Map<Integer,Branch>  map = null;
    public static List<Branch>  list = null; 
   
	public static Map<Integer, Branch> getMap() {
		if(map == null || flag ){
			map = BranchManager.getNameMap();
		}
		return map ;
	}
    
	public static List<Branch> getList() {
		if(list == null || flag ){ 
			list = BranchManager.getLocate(); 
		}
		return list ;
	}
 
   public static void restartMap() {
	  map = BranchManager.getNameMap();
   }	
   
   public static void restartList() {
	   list = BranchManager.getLocate(); 
	}
}
