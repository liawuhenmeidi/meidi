package product;

import java.util.Map;

public class ProductService {
   public static boolean flag = false ;
   public static Map<String,Product> typemap ;  // 
   public static Map<Integer,Product> idmap ; 
 
public static Map<String, Product> gettypemap() {
	if(typemap == null || flag){
		typemap = ProductManager.getProductType();
	} 
	return typemap;
}
 
  
public static Map<Integer, Product> getIDmap() {
	if(idmap == null || flag ){
		idmap = ProductManager.getProductID();
	}  
	return idmap;
}

}
