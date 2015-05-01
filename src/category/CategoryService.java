package category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import branch.Branch;

public class CategoryService{
	public static HashMap<Integer,Category> map;
	public static HashMap<String,Category> mapstr;
	public static List<Category> list;
	public static List<Category> listmaintain;
	
	public static boolean flag = false ;
	
	public static HashMap<Integer,Category> getmap(){
		if(null == map){
			map = CategoryManager.getCategoryMap();
		}
		return map ;
	} 
	 
	public static  List<Integer> getByExportModel(int expor){
		init();    
		List<Category> list = getList();    
		List<Integer> listb = null;  
		if (null != list) {  
			listb = new ArrayList<Integer>();
			for (int i = 0; i < list.size(); i++) {
				Category c = list.get(i);  
				// System.out.println(c.getExportmodel());
				 //System.out.println(expor);  
				if (expor == c.getExportmodel()) {
					// System.out.println(b.getId());
					listb.add(c.getId());
				}
			}
			// list = BranchManager.getLocate();
		}

		return listb;
	}
	
	public static HashMap<String,Category> getmapstr(){
		if(null == mapstr){ 
			mapstr = CategoryManager.getCategoryMapStr();
		}  
		return mapstr ;
	}
	
	public static List<Category> getlistmaintain(){
		if(null == listmaintain){
			listmaintain = CategoryManager.getCategorymaintain();
		}
		return listmaintain ;
	}
	
	public static List<Category> getList(){
		if(null == list){
			list = CategoryManager.getCategory();
		}
		return list ;
	}
	
	public static void init(){
		if(flag){ 
			list = CategoryManager.getCategory();
			listmaintain = CategoryManager.getCategorymaintain();
			map = CategoryManager.getCategoryMap();
		}
		flag = false ;
	}
}