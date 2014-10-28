package category;

import java.util.HashMap;

public class CategoryService{
	public static HashMap<Integer,Category> map;
	public static HashMap<Integer,Category> getmap(){
		if(null == map){
			map = CategoryManager.getCategoryMap();
		}
		return map ;
	}
}