package branch;

import java.util.List;
import java.util.Map;
 
public class BranchService {
	public static boolean flag = false ;
    public static Map<Integer,Branch>  map = null;
    public static List<Branch>  list = null; 
    public static Map<String,Branch> nameMap = null ;
    public static List<String> listStr = null;
   
	public static Map<Integer, Branch> getMap() {
		if(map == null){
			map = BranchManager.getIdMap();
		}
		init();
		return map ;
	}
    
	public static Map<String, Branch> getNameMap() {
		if(nameMap == null){
			nameMap = BranchManager.getNameMap();
		}
		init();
		return nameMap ;
	}
	
	public static Branch gerBranchByname(String name ){
		return getNameMap().get(name);
	} 
	
	public static List<Branch> getList() {
		if(list == null){ 
			list = BranchManager.getLocate(); 
		}
		init();
		return list ;
	}
  
	public static List<String> getListStr() {
		if(listStr == null){ 
			listStr = BranchManager.getLocateAllIsable( ); 
		}
		init();
		return listStr ;
	}
	
   
   public static void init(){
	   if(flag){
		   map = BranchManager.getIdMap(); 
		   list = BranchManager.getLocate();
		   nameMap = BranchManager.getNameMap();
	   }
	   flag = false ;
   }
}
