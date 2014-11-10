package product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
   public static boolean flag = true ;
   public static Map<String,Product> typemap ;  // 
   public static Map<Integer,Product> idmap ; 
   public static List<String> list ;
   public static HashMap<String,ArrayList<String>> typeName;
   
  public static Map<String, Product> gettypemap() {
	  init();
	 if(typemap == null ){
		typemap = ProductManager.getProductType();
	 } 
	return typemap;
}
 
  public static HashMap<String,ArrayList<String>> gettypeName() {
	  init();
	 if(typeName == null){
		 typeName = ProductManager.getProductName();
	 } 
	return typeName;
}
  
  public static List<String> getlist(int id) {
	  init();
		 if(typeName == null){
			 typeName = ProductManager.getProductName();
		 }  
		return typeName.get(id+"");
	}
	 
  
public static Map<Integer, Product> getIDmap() {
	init();
	if(idmap == null ){
		idmap = ProductManager.getProductID();
	}  
	return idmap;
}

	public static void init(){
		if(flag){
			typemap = ProductManager.getProductType();
			typeName = ProductManager.getProductName();
			idmap = ProductManager.getProductID();
		}
		flag = false ;
	}
}
