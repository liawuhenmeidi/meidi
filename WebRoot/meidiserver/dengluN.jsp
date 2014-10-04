<%@ page language="java" import="java.util.*,java.net.*,user.*,group.*" pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
boolean flag = false ;

User user = (User)session.getAttribute("user");
if(null != user){
	flag = true ;
}


String action = request.getParameter("action");

String username = "";
String password = "";

Cookie Cookies[]=request.getCookies();

if(Cookies==null){
   System.out.println("还没有cookie值");
   }else {
		for(int i=0;i < Cookies.length;i++){
			if("username".equals(URLDecoder.decode(Cookies[i].getName(),"utf-8"))){
				username = URLDecoder.decode(Cookies[i].getValue(),"utf-8"); 
			}else if("password".equals(URLDecoder.decode(Cookies[i].getName(),"utf-8"))){
				password = URLDecoder.decode(Cookies[i].getValue(),"utf-8");
			}
         }
}  


if(action != null && action.equals("login")) {
	 username = request.getParameter("username");
	 password = request.getParameter("password");
	try {
		User u = UserManager.check(username, password);
		user = u ;
		if(u != null ){
			String userNamecook = URLEncoder.encode(username, "utf-8");
			String passwordcook = URLEncoder.encode(password, "utf-8");
 
			//URLDecoder.decode(cookies[i].getName(),"utf-8");
			Cookie cookie=new Cookie("username",userNamecook);
			Cookie cookiep=new Cookie("password",passwordcook);
			cookie.setMaxAge(10*60);  //设置过期之前的最长时间
			cookiep.setMaxAge(10*60);
			response.addCookie(cookie);
			response.addCookie(cookiep);
 
			session.setAttribute("user", u);
			
			response.sendRedirect("user/welcom.jsp");

			return ;
		}
		return ;
	} catch (UserNotFoundException e) {
		out.println(e.getMessage());
		return;
	} catch (PasswordNotCorrectException e) {
		out.println(e.getMessage());
		return;
	}
}
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="baidu-tc-verification" content="7dc8d94b8710852faced5a7e5289a7af" />
<title>欢迎使用微网站办公系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>

<link rel="stylesheet" href="css/songhuo.css"/>


</head>

<body>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type = "text/javascript" language = "javascript">
var flag = "<%=flag%>"; 

$(document).ready(function(){
	if(flag == "true"){
		location.href = "user/welcom.jsp";
	}

});
function clear(){
	if (confirm("确定要退出吗？")) {
		location.href = "user/server.jsp?method=quit";
	} 
  }
   
function regist(){
	location.href = "zhuce.jsp";
	
}
  </script>
  
<!--   头部开始   -->
<div class="s_main">

<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>


<!--  头 单种类  -->
<div class="s_main_tit">用户登陆</div>
 
 
<!--  订单详情  -->
<div class="s_main_box">
  
  <form action="dengluN.jsp" method="post">
	<input type="hidden" name="action" value="login"/>
	<table width="100%" class="s_main_table">
  
  <tr>
   <td width="10%"></td>
    <td width="30%">职工姓名</td>
    <td width="50%"><input type="text" size="10" value="<%=username==null?"":username %>" name="username"/></td>
    <td width="10%"></td>
  </tr>
  <tr>
    <td></td>
    <td>密码</td>
    <td><input type="password" size="10" value="<%=password==null?"":password %>" name="password"/></td>
  </tr>
  <tr>
     <td></td>
     <td> 
    <input type="button"  name="" value="注册" onclick="regist()" /></td>
    <td><input type="submit" value="登陆"/></td>
    
    
  </tr>
  </table>
    </form>
	 <br/>

<br/>
	 </div>
	 </div>
</body>
</html>
