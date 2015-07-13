<%@ page language="java" import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
        
<%            
User user = (User)session.getAttribute("user"); 
String id = request.getParameter("id"); 
boolean flag = false ; 


LogisticsMessage lm  = LogisticsMessageManager.getByid(Integer.valueOf(id));
         
%>  
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />   
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" type="text/css" href="../../css/songhuo.css"/>  

</head> 
<body>  
 
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
 
   <div class="">
   <div class="weizhi_head">现在位置：查看明细
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>
    
   </div>      
     
      <table style="width:80% "> 
      <tr class="asc">
<td align="center" >   
	车牌号  
</td>   
<td align="center"> 
<%=lm.getCars().getNum() %>  
	</td>   
</tr><tr class="asc"> 
<td align="center"> 送货地址</td> 
<td align="center">    
	  <%=lm.getLocates()%>
	  </td> 
</tr><tr class="asc">
<td align="center"> 价格</td>
<td align="center">  
	  <%=lm.getPrice() %>
	  </td>  
</tr><tr class="asc">
<td align="center"> 送货时间</td>
 <td align="center"> 
	  <%=lm.getSendtime() %>
	  </td> 
 </tr>    
 <tr class="asc">
 <td align="center">行车记录</td>
 <td align="center"> 
 <table>
	 
 <%  
 String locateM = lm.getLocateMessage();
 if(!StringUtill.isNull(locateM)){
	 String[] locateMs = locateM.split(",");
	 for(int i=1;i<locateMs.length;i++){
		 String locate = locateMs[i];  
		 String[] locates = locate.split("::");
		 String time = locates[0]; 
		 String l = locates[1];
		 
		 %>
		 <tr><td><%= time%></td><td><%=l %></td></tr>
		 
		 <%
	 }
	 
	 
 }
 
 
 %>
 </table>
 </td>  
 </tr>
	 
   
     
      </table>
      
     </div>


</body>
</html>
