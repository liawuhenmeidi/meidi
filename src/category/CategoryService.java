package category;

import java.util.HashMap;
import java.util.List;

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