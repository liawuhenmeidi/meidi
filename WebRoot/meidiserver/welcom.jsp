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
  
<script type = "text/javascript" language = "javascript">
function clear(){
	if (confirm("确定要退出吗？")) {
		location.href = "server.jsp?method=quit";
	}
  }
  </script>
  </head>
<body>
  <div id="mainDiv">
   <% 
     if(UserManager.checkPermissions(user, Group.sale)){ 	 
   %> 
   <a href="<%=realPath %>order.jsp">顶码销售</a><br>
   <a href="<%=realPath %>serch_list.jsp">查看报装单</a>
   <%
     }  else
   %>  
   <% 
   if(UserManager.checkPermissions(user,Group.send)){ 	 
   %>
   <a href="<%=realPath %>songhuo.jsp">送货单</a>  
   <%
     }
   %> 
   <a href="javascript:clear()">退出登陆</a>
  </div>  

</body>

</html>