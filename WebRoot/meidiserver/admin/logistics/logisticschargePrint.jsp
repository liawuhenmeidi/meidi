<%@ page language="java"  import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user");    
String[] lids = request.getParameterValues("lid");
String statues = request.getParameter("statues");
String method = request.getParameter("method");
String message = "司机";
List<LogisticsMessage>	list =  LogisticsMessageManager.getlistByIds(StringUtill.getStr(lids));
if(null != list && list.size() >0){ 
	if("AdvancePrince".equals(method)){ 
		LogisticsMessageManager.updateAdvancePrince(StringUtill.getStr(lids),statues);
		message = "单位";
	}else {
		if("2".equals(statues)){   
		     LogisticsMessageManager.updatecharge(StringUtill.getStr(lids),statues);
			 response.sendRedirect("logisticslistCharging.jsp?");
		}else if("5".equals(statues)){ 
			 LogisticsMessageManager.updatecharge(StringUtill.getStr(lids),statues);
			 response.sendRedirect("logisticslistchargeconfire.jsp?");
		}else {
			LogisticsMessageManager.updatecharge(StringUtill.getStr(lids),statues);
		}
	}
	
	
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结款</title> 
   
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script type="text/javascript">

function println(){
	 
	 window.print();

}
 


</script>
</head> 
 
<body>
<!--   头部结束   -->
 
<table  cellspacing="1" style="margin:auto;background-color:black; width:400px;height:300px;">  

     <% 
      int total = 0 ;
      String uname = "";
      if(null != list){
    	 for(int i=0;i<list.size();i++){
    		 LogisticsMessage lm = list.get(i);
    		 if("AdvancePrince".equals(method)){
    			 total += lm.getAdvancePrice(); 
    		 }else { 
    			 total += lm.getPrice();
    			 uname = lm.getUser().getUsername();
    		 }
    	 }
     } %>
  
   <tr class="asc">
   <td align="center"><%=message %></td>
   <td align="center"><%=uname  %></td>
   </tr> 
   <tr class="asc">
   <td align="center">
    金额： 
   </td>
   
   <td align="center">
   <%= total%>元 
   </td>
    </tr> <tr class="asc">
   <td align="center">
   日期：
   </td> 
   <td align="center">
   <%=TimeUtill.gettimeString() %>
   </td>
    </tr> <tr class="asc">
   <td align="center">
   经理签字：
   </td>
   <td align="center">
   __________
    
   </td>
   </tr>
   <tr class="asc">
  <td colspan="2" align="center"> 
  <center> <input class="noprint" type=button name='button_export' title='打印1' onclick="println()" value="打印"></input></center>
  </td>
  </tr>
  </table>


</body>
</html>
