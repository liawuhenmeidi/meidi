package branchtype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
  
public class BranchTypeService {
	  
	 protected static Log logger = LogFactory.getLog(BranchTypeService.class);
	
	 public static boolean flag = false ;
	 
	 public static List<BranchType> list ;
	  
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
		List<Integer> li = new ArrayList<Integer>();
		if(null != list){
			for(int i=0;i<list.size();i++){
				BranchType b = list.get(i);
				if(b.getSaletype() == type){
					li.add(b.getId());
				}
			}
		}
		return li ;
	}
	
	public static void init(){
			if(flag){ 
				  
			}
			flag = false ;
		}
	 
	 
}
