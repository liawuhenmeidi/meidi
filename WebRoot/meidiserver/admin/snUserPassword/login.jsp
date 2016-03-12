<%@ page language="java"   pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%> 
 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>苏宁用户名密码操作页面</title>

<script type = "text/javascript" language = "javascript">
 
  </script>
</head>
   
<body onload="javascript:document.myform.username.focus();">
<div id="login_bg" class="login_box"> 
	<div class="login_iptbox">          
   	 <form action="<%=basePath%>meidiserver/admin/SNUserPasswordSave.do" method="post">
   	 <input type="hidden" name="action" value="login"/> 
           
   	 <label>用户名:</label><input type="text" size="10" value="${user.username}"  name="username"/>
   	 <label>密码：</label><input type="password" size="10" value="${user.password}" name="password"/>
   	 
     <input name="dosubmit" value="保存" type="submit"  /> 
    <br/>    
    </div>

     </form>
    </div>
</div>
</body>
</html>