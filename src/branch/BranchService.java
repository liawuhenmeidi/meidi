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
		init();
		if(map == null){
			map = BranchManager.getIdMap();
		}
		
		return map ;
	}
    
	public static Map<String, Branch> getNameMap() {
		init();
		if(nameMap == null){
			nameMap = BranchManager.getNameMap();
		}
		
		return nameMap ;
	}
	
	public static Branch gerBranchByname(String name ){
		return getNameMap().get(name);
	} 
	
	public static List<Branch> getList() {
		init();
		if(list == null){ 
			list = BranchManager.getLocate(); 
		}
		
		return list ;
	}
  
	public static List<String> getListStr() {
		init();
		if(listStr == null){ 
			listStr = BranchManager.getLocateAllIsable( ); 
		}
		
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
