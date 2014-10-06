package branch;

import java.util.List;
import java.util.Map;

public class BranchService {
   public static Map<Integer,Branch>  map = null;
   public static List<Branch>  list = null; 
   
	public static Map<Integer, Branch> getMap() {
		if(map == null){
			map = BranchManager.getNameMap();
		}
		return map ;
	}
 
	public static List<Branch> getList() {
		if(list == null){ 
			list = BranchManager.getLocate(); 
		}
		return list ;
	}
 
	
	
public static void restartMap() {
	map = BranchManager.getNameMap();
}	

}
