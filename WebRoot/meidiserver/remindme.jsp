<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
   
 String  message = "";
 
 String typeStr = request.getParameter("type");
 int type = Integer.valueOf(typeStr);
 if(type == 10){
	 message = "请先维护产品信息";
 }else if(type ==  Group.send){
	 message = "请先维护安装网点";
 }else if(type == Group.sencondDealsend || type == Group.sale){
	 message = "请先在销售支持中维护文员(派单员)";
 }
 
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

$(function () { 
	
});

</script>

<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" /> 
  </jsp:include>
<body>


<!--  zhanghao  -->

<div class="shwo_main">
   
   <!--   登陆开始   -->

   <br>  
   <div class="s_main_tit"><%=message %></div>
 
</div>

</body>
</html>