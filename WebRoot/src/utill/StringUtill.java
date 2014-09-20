package utill;

import group.Group;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.CategoryManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StringUtill {
	protected static Log logger = LogFactory.getLog(StringUtill.class);
    public static boolean isNull(String str){
    	boolean flag = false ;
    	if("".equals(str) || "null".equals(str) || null == str){
    		flag = true ;
    	}
    	return flag ;
    }
    
    public static String GetJson(String[] list) {  
        // java 转数组  
    	 
        if(list != null){
        	JSONArray jsonArray = JSONArray.fromObject(list);  
            String s = jsonArray.toString(); 
            return s ; 
        }
        
       return "" ;  
    } 
    
    public static String GetJson(List list){ 
    	   
    	if(null != list){
    	 JSONArray jsonArray = JSONArray.fromObject(list);
    	 //logger.info(list.toString());
       	 return jsonArray.toString();
    	} 
    	 return "";
    }
    
    public static String GetJson(Object object){ 
 	   
    	if(null != object){
         if(object instanceof Collection){  
        	 JSONArray jsObj = JSONArray.fromObject(object); 
        	 return jsObj.toString(); 
         }else {
        	 JSONObject jsObj = JSONObject.fromObject(object);
        	 //logger.info(list.toString());
           	 return jsObj.toString(); 
         }
    	 
    	} 
    	 return "";
    }
    
    public static String GetSqlInGroup(List<Group> listg){
    	String str = "(";
    	
    	if(listg == null || listg.size() == 0){
    		return " ( -1 ) ";
    	}else {
    	
    	
		for(int i=0;i<listg.size();i++){
			Group g = listg.get(i);
			str += g.getId()+",";
		} 
		str = str.substring(0, str.length()-1);
		str += ")";
    	}
    	return str ;
    	
    }
    public static String GetJson(Map map){
   // logger.info(map.toString()); 
    	JSONObject jsObj = JSONObject.fromObject(map);  
   	 return jsObj.toString();
   }
    
}
