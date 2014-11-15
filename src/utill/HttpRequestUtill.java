package utill;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class HttpRequestUtill {
	protected static Log logger = LogFactory.getLog(HttpRequestUtill.class); 
	// 获取搜索条件查询
    public static String getSearch(HttpServletRequest request){
    	String sear = ""; 

        Enumeration<String> paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String str = (String) paramNames.nextElement();  
            String[] paramValues = request.getParameterValues(str);
            
            if(!"num".equals(str) && !"page".equals(str) && !"sort".equals(str) && !"statues".equals(str) && !"type".equals(str) && !"method".equals(str) && !"searched".equals(str)){
            	if (paramValues.length == 1) {  
                    String paramValue = paramValues[0];  
                    if (paramValue.length() != 0) {
    	                    if("sendtype".equals(str)){
    	        				String strr = request.getParameter(str);
    	        				if(strr != "" && strr != null){   
    	        					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    	        					sear += " and id in (select orderid  from mdorderproduct where " + str + " in ( select id from mdproduct where ptype like '%" + strr +"%' )  and statues = 0)";
    	        				}  // giftName
    	        			}else if("saletype".equals(str)){
    	        				String strr = request.getParameter(str);  
    	        				if(strr != "" && strr != null){   
    	        					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    	        					sear += " and id in (select orderid  from mdorderproduct where " + str + " in ( select id from mdproduct where ptype like '%" + strr +"%' )  and statues = 1)";
    	        				}  // giftName
    	        			}else if("categoryname".equals(str)){
    	        				String strr = request.getParameter(str); 
    	        				if(strr != "" && strr != null){    
    	        					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    	        					sear += " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where " + str + " like '%" + strr +"%'))";
    	        				}  // giftName
    	        			}else if("giftName".equals(str) || "statues".equals(str)){ 
    	        				String strr = request.getParameter(str);  
    	        				if(strr != "" && strr != null){    
    	        					sear += " and id in (select orderid  from mdordergift where " + str + " like '%" + strr +"%')"; 
    	        				}  // giftName
    	        			}else if("dealSendid".equals(str) || "saleID".equals(str) || "sendId".equals(str) || "installid".equals(str) || "returnid".equals(str) ){
    	        				String strr = request.getParameter(str);
    	        				if(strr != "" && strr != null){  
    	        				  sear += " and " + str + " in (select id from mduser  where username like '%" + strr +"%')"; 
    	        				}
    	        			}else if("orderbranch".equals(str)){
    	        				String strr = request.getParameter(str);
    	        				sear += " and " + str + " in (select id from mdbranch where bname like '%" + strr +"%')";  
    	        			}else if("deliveryStatues".equals(str) || "oderStatus".equals(str)){
    	        				String strr = request.getParameter(str);
    	        				if(strr.equals(-1+"")){
    	        					strr = "3,4,5,11,12,13";
    	        				}
    	        				sear += " and " + str +" in (" + strr +")";
    	        			}else if("saledatestart".equals(str)){
    	        				String start = request.getParameter(str);
    	        				String end = request.getParameter("saledateend");
    	        				String sqlstr = "saledate";
    	        				if(start != null && start != "" && start != "null"){
    	        					sear += " and " + sqlstr + "  BETWEEN '" + start + "'  and  ";
    	        				    
    	        					if(end != null && end != "" && end != "null"){
    		        					sear += " '" + end + "'";
    		        				}else { 
    		        					sear += "now()";
    		        				} 
    	        					
    	        				}else {
    	        					if(end != null && end != "" && end != "null"){
    		        					sear += " and " + sqlstr +  "  < '" + end + "'"; 
    		        				}
    	        				}
    	        			}else if("dealsendTimestart".equals(str)){
    	        				String start = request.getParameter(str);
    	        				String end = request.getParameter("dealsendTimeend");
    	        				String sqlstr = "dealsendTime"; 
    	        				if(start != null && start != "" && start != "null"){
    	        					sear += " and " + sqlstr + "  BETWEEN '" + start + "'  and  ";
    	        				    
    	        					if(end != null && end != "" && end != "null"){
    		        					sear += " '" + end + "'";
    		        				}else { 
    		        					sear += "now()";
    		        				} 
    	        					
    	        				}else {
    	        					if(end != null && end != "" && end != "null"){
    		        					sear += " and " + sqlstr +  "  < '" + end + "'"; 
    		        				}
    	        				}
    	        			}else if(!"dealsendTimeend".equals(str) && !"saledateend".equals(str)) {      
    	        				String strr = request.getParameter(str); 
    	        				if(strr != "" && strr != null){
    	        				  sear += " and " + str + " like '%" + strr +"%'"; 
    	        				}  
    	        			}
                    }  
                }  
            } 
            
        }  
             
        return sear ;
    }
	
    /*public static String getSearch(HttpServletRequest request){
    	String sear = "";     
    	String[] search = request.getParameterValues("search");
    	if(search != null){  
    		for(int i = 0 ;i<search.length;i++){
    			String str = search[i];
    			if("saledate".equals(str) || "andate".equals(str) || "dealsendTime".equals(str)){
    				String start = request.getParameter(str+"start");
    				String end = request.getParameter(str+"end");
    				boolean flag = false ; 
    				if(start != null && start != "" && start != "null"){
    					sear += " and " + str + "  BETWEEN '" + start + "'  and  ";
    				    flag = true ;
    				}   
    				if(end != null && end != "" && end != "null"){
    					sear += " '" + end + "'";
    				}else if(flag){ 
    					sear += "now()";
    				}      
    			}else if("sendtype".equals(str)){
    				String strr = request.getParameter(str);
    				
    				if(strr != "" && strr != null){   
    					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    					sear += " and id in (select orderid  from mdorderproduct where " + str + " in ( select id from mdproduct where ptype like '%" + strr +"%' )  and statues = 0)";
    				}  // giftName
    			}else if("saletype".equals(str)){
    				String strr = request.getParameter(str);  
    				if(strr != "" && strr != null){   
    					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    					sear += " and id in (select orderid  from mdorderproduct where " + str + " in ( select id from mdproduct where ptype like '%" + strr +"%' )  and statues = 1)";
    				}  // giftName
    			}else if("categoryname".equals(str)){
    				String strr = request.getParameter(str); 
    				if(strr != "" && strr != null){    
    					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    					sear += " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where " + str + " like '%" + strr +"%'))";
    				}  // giftName
    			}else if("giftName".equals(str) || "statues".equals(str)){ 
    				String strr = request.getParameter(str);  
    				if(strr != "" && strr != null){    
    					sear += " and id in (select orderid  from mdordergift where " + str + " like '%" + strr +"%')"; 
    				}  // giftName
    			}else if("dealSendid".equals(str) || "saleID".equals(str) || "sendId".equals(str) || "installid".equals(str) || "returnid".equals(str) ){
    				String strr = request.getParameter(str);
    				if(strr != "" && strr != null){  
    				  sear += " and " + str + " in (select id from mduser  where username like '%" + strr +"%')"; 
    				}
    			}else if("orderbranch".equals(str)){
    				String strr = request.getParameter(str);
    				sear += " and " + str + " in (select id from mdbranch where bname like '%" + strr +"%')";  
    			}else if("deliveryStatues".equals(str) || "oderStatus".equals(str)){
    				String strr = request.getParameter(str);
    				if(strr.equals(-1+"")){
    					strr = "3,4,5,11,12,13";
    				}
    				sear += " and " + str +" in (" + strr +")";
    			}else{      
    				String strr = request.getParameter(str);
    				if(strr != "" && strr != null){
    				  sear += " and " + str + " like '%" + strr +"%'"; 
    				}  
    			}
    		} 	
    	}else { 
    		sear = "";
    	} 
    	return sear ;
    }
    */
     
    //  显示cookie
	  
    public static String getSearchCookie(HttpServletRequest request){
    	String sear = "";     
    	String[] search = request.getParameterValues("search");
    	if(search != null){  
    		for(int i = 0 ;i<search.length;i++){
    			String str = search[i];
    			if("saledate".equals(str) || "andate".equals(str) || "dealsendTime".equals(str)){
    				String start = request.getParameter(str+"start");
    				String end = request.getParameter(str+"end");
    				boolean flag = false ; 
    				if(start != null && start != "" && start != "null"){
    					sear += " and " + str + "  BETWEEN '" + start + "'  and  ";
    				    flag = true ;
    				}   
    				if(end != null && end != "" && end != "null"){
    					sear += " '" + end + "'";
    				}else if(flag){ 
    					sear += "now()";
    				}      
    			}else if("sendtype".equals(str) || "saletype".equals(str)){
    				String strr = request.getParameter(str); 
    				if(strr != "" && strr != null){   
    					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    					sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    				}  // giftName
    			}else if("categoryname".equals(str)){
    				String strr = request.getParameter(str); 
    				if(strr != "" && strr != null){    
    					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
    					sear += " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where " + str + " like '%" + strr +"%'))";
    				}  // giftName
    			}else if("giftName".equals(str) || "statues".equals(str)){ 
    				String strr = request.getParameter(str);  
    				if(strr != "" && strr != null){    
    					sear += " and id in (select orderid  from mdordergift where " + str + " like '%" + strr +"%')"; 
    				}  // giftName
    			}else if("dealSendid".equals(str) || "saleID".equals(str) || "sendId".equals(str)){
    				String strr = request.getParameter(str);
    				if(strr != "" && strr != null){ 
    				  sear += " and " + str + " in (select id from mduser  where username like '%" + strr +"%')"; 
    				}
    			}else {     
    				String strr = request.getParameter(str);
    				if(strr != "" && strr != null){
    				  sear += " and " + str + " like '%" + strr +"%'"; 
    				}  
    			}
    		} 	
    	}else { 
    		sear = "";
    	} 
    	return sear ;
    }
	
	
}
