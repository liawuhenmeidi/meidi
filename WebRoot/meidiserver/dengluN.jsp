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
		session.setAttribute("message", e.getMessage());
		response.sendRedirect("jieguo.jsp?type=loginresult");
		//out.println(e.getMessage()); 
		return;   
	} catch (PasswordNotCorrectException e) {
		session.setAttribute("message", e.getMessage());
		response.sendRedirect("jieguo.jsp?type=loginresult");
		//out.println(e.getMessage());
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
 <div style="height:20px;">
   
</div>
<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>


<!--  头 单种类  -->
<div class="s_main_tit"><span class="qiangdan"><a href="javascript:void(0)" onclick="regist()" >注册</a></span></div>
  <div style="height:50px;">
   
  </div>
  
  <form action="dengluN.jsp" method="post">
	<input type="hidden" name="action" value="login"/>
	<table width="100%">

  <tr> 
    <td align="center"><input type="text" placeholder="职工姓名" style="font-size:25px;width:80%"  value="<%=username==null?"":username %>" name="username"/></td>
  </tr> 
  <tr>
  <td></td>
  </tr>
  <tr>  
    <td align="center"><input type="password" placeholder="密码"  style="font-size:25px;width:80%" value="<%=password==null?"":password %>" name="password"/></td>
  </tr>
  <tr>
  <td></td>
  </tr>
 <tr>    
   <td align="center"><input type="submit" style="font-size:20px;background-color:orange;width:90%"   value="登陆"/></td>
  </tr>  
 
</table>
 
    </form>
	 <br/>

<br/>
	 </div>
</body>
</html>
