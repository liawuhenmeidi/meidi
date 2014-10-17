<%@ page language="java" import="java.util.*,user.*,group.*" pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
 
User user = (User)session.getAttribute("user");

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


String message = request.getParameter("message");
if("0".equals(message)){
	message = "修改成功";
}else if("1".equals(message)){
	message = "修改失败"; 
}

String resetpassword = request.getParameter("resetpassword");

if("resetpassword".equals(resetpassword)){
	
	String password = request.getParameter("password");
	String password1 = request.getParameter("password1");
	User u = UserManager.check(user.getUsername(), password);
	if(u != null && UserManager.checkPermissions(u, Group.Manger)){
		u.setUserpassword(password1);
		if(UserManager.update(u)){ 
			message = "0";  
		}else {  
			message = "1"; 
		}
		response.sendRedirect("loginN.jsp?message="+message);
	}

}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>微网站管理账号页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/> 
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style>
.door{ width:98%; height:auto; overflow:hidden; margin:10px auto; padding:0px;}

 .door li{ display:block; float:left; margin-left:10%; width:35%; height:60px; margin-top:20px; font-size:20px; line-height:60px; text-align:center; font-family:"微软雅黑";
      -moz-border-radius: 15px;      /* Gecko browsers */
    -webkit-border-radius: 15px;   /* Webkit browsers */
    border-radius:15px;            /* W3C syntax */
  }


</style>
</head>

<body>
  
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>  
<script type = "text/javascript" language = "javascript">
function clear(){
	if (confirm("确定要退出吗？")) {
		location.href = "../login.jsp?message=quit";
	}
  }
  
function resetPassword(){
	//alert(1);
	$("#repassword").css("display","block");
	$("#readme").css("display","none");
	// $("#dingma_c").css("display","block");
  }
  
function checkedd(){
	var password = $("#password").val();
	var password1 = $("#password1").val();
	var password2 = $("#password2").val(); 
	if(password == null || password == ""){
		alert("请输入原始密码");
		return false ;
	}
	if(password1 == null || password1 == ""){
		alert("请输入新密码");
		return false ;
	}
	if(password2 == null || password2 == ""){
		alert("请输入确认密码");
		return false ;
	}
	if(!(password1 == password2)){
		alert("两次输入的密码不一致");
		return false ;
	}
	return true ;
}  
  
  </script>
<!--   头部开始   -->
<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

     
 <!--       -->    
      
<div class="weizhi_head">现在位置：个人中心页面</div>
    <% 
      if(null != user ){
	  %>
            
    <div class="s_main_box">
    
  <ul class="door">
   <li><%=user.getUsername()%>您已经登陆 </li>
    <li> </li>
   <%  
     } 
     if(UserManager.checkPermissions(user,Group.Manger)){
    	 
     %> 
    <li><a href="javascript:resetPassword()">修改密码</a></li>
    <%
     }
    %>
    <li><a href="javascript:clear()">退出</a></li>
    </ul> 
    </div>
    <div class="weizhi_head"></div>

    <%
    if(message != null && message != "" ){
     %>
   <div class="weizhi_head"></div>
    
     <ul class="door"> 
      
      <li><%=message %></li>
     
    </ul> 
     <%
     }
     %>
      
     
 <div id="readme">
 

   <table width="100%" class="s_main_table">
  
  <tr>
   <td width="10%"></td>
    <td width="10%">使用说明</td>
    <td width="70%"></td>
  </tr>
  <tr>
    <td></td> 
    <td>1</td>
    <td>产品管理注册产品</td>
  </tr>
  <tr>
    <td></td>
    <td>2</td> 
    <td>职位管理注册职位</td>
  </tr>
  <tr>
   <td></td>
     <td>3</td>
    <td>门店管理增加门店</td>
    
    <td> </td>
  </tr>
  <tr>
   <td></td>
     <td>4</td>
    <td>手机端员工注册</td>
    
    <td> </td>
  </tr>
  </table>
	 <br/>

    </div>
     
      
       
    <div id="repassword" style= "display:none;">

   <form action="loginN.jsp" method="post" onsubmit="return checkedd()">
    <input type="hidden" name="resetpassword" value="resetpassword"/> 
   <table width="100%" class="s_main_table">
  
  <tr>
   <td width="10%"></td>
    <td width="30%">请输入原始密码</td>
    <td width="50%"><input type="password" size="30" value="" name="password"  id="password"/></td>
    <td width="10%"></td>
  </tr>
  <tr>
    <td></td> 
    <td>新密码</td>
    <td><input type="password" size="30" value="" name="password1"  id="password1"/></td>
  </tr>
  <tr>
    <td></td>
    <td>新密码确认</td> 
    <td><input type="password" size="30" value="" id="password2" name="password2"/></td>
  </tr>
  <tr>
     <td></td>
    <td></td>
    
    <td> 
    <input type="submit"  name="" value="确认" /></td>
  </tr>
  </table>
    </form>

	 <br/>

    </div>
    
    
    
    
    
    
</body>
</html>
