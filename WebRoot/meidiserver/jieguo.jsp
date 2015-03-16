<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
   
 String  resCode = (String)session.getAttribute("message");
 session.removeAttribute("message");
 
 String type = request.getParameter("type");
 String oid = request.getParameter("oid");
   
String mark = request.getParameter("mark");

 if(!StringUtill.isNull(mark)){ 
		resCode = RemarkUtill.getMessage(Integer.valueOf(mark));
 }

 
 // System.out.println("oid"+oid);
%>





<!DOCTYPE html>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta http-equiv="refresh" content="300; url=login.jsp"/>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="css/songhuo.css">
<title>处理结果</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

</head> 

<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var type = '<%=type%>';

$(function () {  
	if("updated" == type){
		//alert(1);
		checkedd(); 
	}
	
});
function checkedd(){
	//parent.location.reload(); 
	//window.returnValue='refresh'; 
	window.close();
	//window.oper.reload();
}

</script>

<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" /> 
  </jsp:include>

<% if("order".equals(type)){  
	
%> 
<div class="s_main_tit">报单提交
	<span class="qiangdan"><a href="user/order.jsp">继续报装</a></span>
	<% if(!StringUtill.isNull(oid)){
		if(Integer.valueOf(oid) != -1){
			%>
	 <span class="qiangdan"><a href="user/order.jsp?id=<%=oid%>&statues=statues">相同顾客报装</a></span>	
			<%
		}
	} %>
	
	<span class="qiangdan"><a href="user/welcom.jsp">返回</a></span>
</div>

<% 
} else if("zhuce".equals(type)){
%>
  
<div class="s_main_tit">员工注册<span class="qiangdan"><a href="dengluN.jsp">登陆</a></span><span class="qiangdan"></span></div>

<%
}else if("loginresult".equals(type)){
	%>
	<div class="s_main_tit">登陆结果<span class="qiangdan"><a href="dengluN.jsp">登陆</a></span></div>
	
	<%
}

%>
<body>


<!--  zhanghao  -->

<div class="shwo_main">
   
   <!--   登陆开始   -->

   <br>  
   <div class="s_main_tit"><%=resCode %></div>
 
</div>

</body>
</html>