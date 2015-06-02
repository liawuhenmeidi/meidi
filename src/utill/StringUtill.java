package utill;

import group.Group;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.CategoryManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StringUtill {
	protected static Log logger = LogFactory.getLog(StringUtill.class);
    public static boolean isNull(String str){
    	boolean flag = false ;
    	if("".equals(str) || "null".equals(str) || null == str || "undefined".equals(str) || "NULL".equals(str)){
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
    	//long start= System.currentTimeMillis(); 
    	if(null != list){
    	 JSONArray jsonArray = JSONArray.fromObject(list);
    	 //logger.info(list.toString());
    	// logger.info("qa"+(System.currentTimeMillis() - start)); 
       	 return jsonArray.toString();
    	}   
    	 return "[]"; 
    }
    
    public static String GetJson(Object object){ 
 	    
    	if(null != object){
         if(object instanceof Collection || object instanceof Object[]){  
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
    // 获取字母不包含汉子 
    public static String getStringNocn(String str){
    	String s = "";
    	if(!StringUtill.isNull(str)){
    		 str = str.replace("(", "").replace(")", "").replace("（","").replace("）", "").replaceAll("\\s*", "");
        	 String regEx = "[\\u4e00-\\u9fa5]"; 

    		 Pattern p = Pattern.compile(regEx);
    		 Matcher m = p.matcher(str);  
             s =m.replaceAll("").trim();   
    	}
    	
		return s; 
		  
    } 
     
    // 获取字母不包含数字 
    public static  String getNumbers(String content) {  
    	String regEx="[^0-9]";   
    	Pattern p = Pattern.compile(regEx);   
    	Matcher m = p.matcher(content);   
        return  m.replaceAll("").trim();   
    }  
    
    
    /*public static String getStringreal(String str){
   	    str = str.replace("(", "").replace(")", "").replace("（","").replace("）", "").replaceAll("\\s*", "");
   	 
		return str;  
		  
   } */
    
    
    public static String UUID(int num){
    	String s = UUID.randomUUID().toString();
    	if(num > 36){
    		num = 36;
    	}
    	if(num <= 0){
    		num = 8;
    	}
    	return s.substring(0,num);
    }
    
    public static String shortUUID(){
    	return UUID(8);
    }
    
    public static String getStr(String[] list){
    	String str = "";
    	if(null != list && list.length != 0){
    		for(int i=0;i<list.length;i++){
    			str += ","+list[i];
    		} 
    		//logger.info(str);
    		str = "("+str.substring(1,str.length())+")";
    	} 
    	return str ;
    }
          
    public static String getStr(List<Integer> list){
    	String str = "";
    	if(null != list && list.size() != 0){
    		for(int i=0;i<list.size();i++){
    			str += ","+list.get(i);
    		}  
    		//logger.info(str);
    		str = "("+str.substring(1,str.length())+")";
    	} 
    	return str ;
    }
    
    public static String getStr(Set<Integer> set){
    	String str = ""; 
    	if(null != set){ 
    		Iterator<Integer> it = set.iterator();
    		while(it.hasNext()){
    			int s = it.next();
    			str += ","+s;
    		} 
    		str = "("+str.substring(1,str.length())+")";
    	}
    	
    	return str ;
    }
      
    
    
    public static String getStr(String[] list,String separator){
    	String str = "";
    	if(null != list && list.length != 0){
    		for(int i=0;i<list.length;i++){
    			str += separator+list[i];
    		} 
    		//logger.info(str);
    		str = str.substring(1,str.length());
    	} 
    	return str ;
    }
    
    public static void main(String args[]){
    	String str1 = "MRO201-4（智能型）净水机";
    	String str2 = "空壳MRO201-4(豪华型）美的净水机 空壳样机";
    	System.out.println(getStringNocn(str1));
    	System.out.println(getStringNocn(str2)); 
    	System.out.print(getStringNocn(str2).equals(getStringNocn(str1)));  
    }
    
    
    public static String toGBKString(String s){
    	StringBuffer sb = new StringBuffer();
    	for (int i=0;i<s.length();i++){ 
    	char c = s.charAt(i); 
    	if (c >= 0 && c <= 255){sb.append(c);}
    	else{ 
    	byte[] b; 
    	try { b = Character.toString(c).getBytes("GBK");}
    	catch (Exception ex) {
    	System.out.println(ex); 
    	b = new byte[0];
    	} 
    	for (int j = 0; j < b.length; j++) {
    	int k = b[j];
    	if (k < 0) k += 256;
    	sb.append("%" + Integer.toHexString(k).toUpperCase());
    	}
    	}
    	}
    	return sb.toString();
    	}
    
    public static String toUtf8String(String s){
    	StringBuffer sb = new StringBuffer();
    	for (int i=0;i<s.length();i++){
    	char c = s.charAt(i);
    	if (c >= 0 && c <= 255){sb.append(c);}
    	else{
    	byte[] b;
    	try { b = Character.toString(c).getBytes("utf-8");}
    	catch (Exception ex) {
    	logger.info(ex);
    	b = new byte[0];
    	}
    	for (int j = 0; j < b.length; j++) {
    	int k = b[j];
    	if (k < 0) k += 256;
    	sb.append("%" + Integer.toHexString(k).toUpperCase());
    	}
    	}
    	}
    	return sb.toString();
    	}
    
    public static String getNotNUll(String str){
    	String strs = "";
    	if(StringUtill.isNull(str)){
    		return strs;
    	} 
    	return str ;
    }
}
