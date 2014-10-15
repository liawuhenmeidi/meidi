package utill;

import javax.servlet.http.HttpServletRequest;

import branch.BranchManager;
 
public class HttpRequestUtill {
	// 获取搜索条件查询
    public static String getSearch(HttpServletRequest request){
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
    			}else if("orderbranch".equals(str)){
    				String strr = request.getParameter(str);
    				sear += " and " + str + " in (select id from mdbranch where bname like '%" + strr +"%')";  
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
