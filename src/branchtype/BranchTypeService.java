package branchtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
  
public class BranchTypeService {
	  
	 protected static Log logger = LogFactory.getLog(BranchTypeService.class);
	 
	 public static boolean flag = true ;
	  
	 public static List<BranchType> list ;
	  
	 public static Map<Integer,BranchType> map ;
	  
	 
	 public static Map<Integer, BranchType> getMap() {
		 init();   
		 Map<Integer,BranchType> maps = new HashMap<Integer, BranchType>();
		 if(null == map){
			if(null != list){
				  
				for(int i=0;i<list.size();i++){
					BranchType b = list.get(i); 
					maps.put(b.getId(), b); 
				}
			}   
		 }
		 
		 
		return maps;
	}

	public static void setMap(Map<Integer, BranchType> map) {
		BranchTypeService.map = map;
	}

	public static List<BranchType> getList() {
		 init();
		 if(null == list){
			 list = BranchTypeManager.getLocate();
		 } 
		return list;
	}

	public static void setList(List<BranchType> list) {
		BranchTypeService.list = list;
	}
   
	public static List<BranchType> getlistSaleModel(int type){
		init();
		List<BranchType> li = new ArrayList<BranchType>();
		if(null != list){
			for(int i=0;i<list.size();i++){
				BranchType b = list.get(i);
				if(b.getSaletype() == type){
					li.add(b);
				}
			}
		} 
		return li ;
	} 
	  
	public static List<Integer> getlistSaleModelID(int type){
		init();
		List<Integer> li = new ArrayList<Integer>();
		 List<BranchType> list = getList(); 
		if(null != list){ 
			for(int i=0;i<list.size();i++){ 
				BranchType b = list.get(i);
				//System.out.println("branchtypeservice"+type+"&"+b.getSaletype());
				if(b.getSaletype() == type){
					li.add(b.getId());
				}
			}
		}
		return li ;
	} 
	
	public static void init(){
			if(flag){   
				 list = BranchTypeManager.getLocate();
			}
			flag = false ;
		}
	 
	 
}
