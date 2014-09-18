<%@ page language="java" import="java.util.*,category.*,user.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");
boolean flag = true ;
User user = (User)session.getAttribute("user");
String path = request.getContextPath();
String realPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);    
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="viewport" content="initial-scale=1, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0"/>
  <title>欢迎使用微网站办公系统</title>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
 
<link rel="stylesheet" href="../css/songhuo.css">

<script type = "text/javascript" language = "javascript">
function clear(){
	if (confirm("确定要退出吗？")) {
		location.href = "server.jsp?method=quit";
	}
  }
  </script>
  </head>
<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--  头 单种类  -->
<div class="s_main_tit">登陆选项<span class="qiangdan"></span></div>
 
  <div class="s_main_box">
<ul class="door">



   <%  
     if(UserManager.checkPermissions(user, Group.sale)){ 	 
   %>  
   <li><a href="<%=realPath %>order.jsp">报装 </a></li>
   <li><a href="<%=realPath %>serch_list.jsp">查看报装单</a></li>
   <%  
     }  else
   %>   
   <%   
   if(UserManager.checkPermissions(user,Group.send)){ 	 
   %>
   <li><a href="<%=realPath %>songhuo.jsp">送货单</a></li>
   <li><a href="<%=realPath %>tuihuo.jsp">退货单</a></li> 
   <%
     }else 
   %> 
   <%  
   if(UserManager.checkPermissions(user,Group.sencondDealsend)){ 	 
   %>
   <li><a href="<%=realPath %>peidan.jsp">派工</a></li>
   <%
     }
   %> 
  <li> <a href="javascript:clear()">退出登陆</a></li>
   </ul>
  </div>   
</div>
</body>

</html>