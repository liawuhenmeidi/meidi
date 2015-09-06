<%@ page language="java" import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
        
<%           
User user = (User)session.getAttribute("user"); 
String statues = request.getParameter("statues");
String method = request.getParameter("method");
if(StringUtill.isNull(statues)){
	statues = "0" ;
} 
 
if("agree".equals(method)){  
	LogisticsMessageManager.updateAgree(user); 
}

List<LogisticsMessage>	list = LogisticsMessageManager.getlist(user,Integer.valueOf(statues));
  
//System.out.println("list"+json );     
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 
 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/jquerycommon.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" type="text/css" href="../../css/songhuo.css"/> 
<link rel="stylesheet" href="../../css/jquery-ui.css"/>  
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript">
        
 var type = "<%=statues%>";
    
 $(function () {    
 	$("#"+type).css("color","red");
 }); 
    
 function search(statues){
		window.location.href="logistic.jsp?statues="+statues;
	}   
 function detail(id){
		window.location.href="logisticDetail.jsp?id="+id+"&statues="+type;
	}
 
 
 function Agree(){  
	 window.location.href="logistic.jsp?method=agree&statues="+type;
 }
</script> 
</head> 
<body>  
<div class="main">  
<div class="s_main_tit"><span class="qiangdan"><a href="../welcom.jsp">返回</a></span></div>
<div class="s_main_tit"><span style="cursor:hand" id="0" onclick="search('0')">待送货</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span style="cursor:hand" id="1" onclick="search('1')">已送货</span>&nbsp;&nbsp;<span style="cursor:hand" id="2" onclick="search('2')">待同意</span>&nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:hand" id="3" onclick="search('3')">待结算</span></div>
 
     <div>    
                 
     <form method = "post"  >
     
            
      <table style="width:100% "> 
      <tr class="dsc">
<td align="center"> 
	车牌号
</td>
<td align="center"> 送货地址</td> 
<td align="center"> 价格</td>
<td align="center"> 送货时间</td>
  
</tr>
      
       <% 
       int total = 0 ;
       if(null != list){
    	   for(int i=0;i<list.size();i++){
    		   LogisticsMessage lm = list.get(i);
    		   total += lm.getPrice();
    		   String cl = "class=\"asc\"";
  			 if(lm.getPid() != 0){
  				 cl = "class=\"bsc\"";
  			 } 
    		 
  			 if(lm.getOperation() != 0){
  				 cl = "class=\"rsc\"";
  			 } 
    		   
    		   %> 
    		  <tr <%=cl %> onclick="detail('<%=lm.getId()%>')"> 
			  
	<td align="center"> 
<%=lm.getCars().getNum() %>  
	</td>   
	  <td align="center">   
	  <%=lm.getLocates() %>
	  </td> 
	  <td align="center">   
	  <%=lm.getPrice() %>
	  </td>  
	  <td align="center"> 
	  <%=lm.getSendtime() %>
	  </td>
	</tr> 
			  
    		   
    		   
    		   
    		   <%
    	   }
    	   %>
    	   
    	   <tr class="asc" > 
			  
	<td align="center"> 
	合计： 
	</td>   
	  <td align="center">   
	 
	  </td> 
	  <td align="center">   
	  <%=total %>
	  </td>  
	  <td align="center"> 
	 
	  </td>
	</tr> 
	<%
       } 
       
       if("2".equals(statues)){ 
    	    %>
    	    
    	  <tr class="asc" > 
			  
	<td align="center"> 
	</td>   
	  <td align="center">   
	 
	  </td>   
	  <td align="center" colspan="2">   
       <input type="button" value="同意结款" onclick="Agree()"/>
	  </td>  
	 
	</tr>   
    	    
    	    
    	    <%
       }
       
       %>
  
      
      
      </table>
 </form>
     </div>

</div>


</body>
</html>
