package product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
   public static boolean flag = false ;
   public static Map<String,Product> typemap ;  // 
   public static Map<Integer,Product> idmap ; 
   public static List<String> list ;
   public static HashMap<String,ArrayList<String>> typeName;
   
  public static Map<String, Product> gettypemap() {
	 if(typemap == null ){
		typemap = ProductManager.getProductType();
	 } 
	return typemap;
}
 
  public static HashMap<String,ArrayList<String>> gettypeName() {
	 if(typeName == null){
		 typeName = ProductManager.getProductName();
	 } 
	return typeName;
}
  
  public static List<String> getlist(int id) {
		 if(list == null){
			 list = ProductManager.getProduct(id);
		 } 
		return list;
	}
	 
  
public static Map<Integer, Product> getIDmap() {
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
