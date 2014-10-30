<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,wilson.salaryCalc.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	String type = request.getParameter("type");
	String name =request.getParameter("name");
	if(type != null && !type.equals("")){
		if(name != null && !name.equals("")){
			if(type.equals("uploadorder")){
				UploadManager.deprecatedUploadOrderByName(name);
			}else if(type.equals("salarymodel")){
				UploadManager.deprecatedSalaryModelByName(name);
			}else if(type.equals("delete")){
				UploadManager.deleteDeprecatedItems();
			}
		}
			
	}
	
	//销售单,苏宁单
	List<UploadOrder> allOrders = UploadManager.getAllUploadOrders();
	List<String> orderNames = UploadManager.getAllUploadOrderNames(allOrders);
	
	//提成标准
	List<UploadSalaryModel> allSalaryModels = UploadManager.getAllSalaryModel();
	List<String> salaryModelNames = UploadManager.getAllSalaryModelNames(allSalaryModels);
	
	//下面用到的临时变量
	String tempName = "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>删除页面</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style type="text/css">
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial",  "Verdana", "sans-serif";
	font-size: 62.5%;
}
</style>

</style>
</head>

<body>
 
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
  <link rel="stylesheet" href="../css/jquery-ui.css">
 
<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include> 
      
<!--  
<p>苏宁/销售单 隐藏</p>
<form action="" method="post">
<input type="hidden" name="type" value="uploadorder"/>
<select name="name">
	<option value="" selected="selected"></option>
	<%
	for(int i = 0 ; i < orderNames.size() ; i ++){
		tempName = orderNames.get(i);
	%>
	<option value="<%=tempName %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input type="submit" value="确认"/>
</form>
-->



<p>提成模板  隐藏</p>
<form action="" method="post">
	<input type="hidden" name="type" value="salarymodel"/>
<select name="name">
	<option value="" selected="selected"></option>
	<%
	for(int i = 0 ; i < salaryModelNames.size() ; i ++){
		tempName = salaryModelNames.get(i);
	%>
	<option value="<%=tempName %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input type="submit" value="确认"/>
</form>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
<hr style="border : 1px dashed blue;" />

<!-- 
<form action="" method="post">
	<input type="hidden" name="type" value="delete"/>
	<input align="bottom"  name="name" type="submit" value="删除所有隐藏数据"/>
</form>
 -->
</body>
</html>
