<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,wilson.salaryCalc.*,java.text.SimpleDateFormat;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	String startDateSTR = request.getParameter("startDate");
	String endDateSTR = request.getParameter("endDate");
	
	 
	//取出所有salaryResult
	List<SalaryResult> salaryResult = SalaryCalcManager.getSalaryResult();
	//取出对应的uploadOrder
	List<UploadOrder> showOrders = SalaryCalcManager.getUploadOrderFromSalaryResult(salaryResult);
	//取出对应的名字
	List<String> orderNames = UploadManager.getAllUploadOrderNames(showOrders);
	
	
	
	List<SalaryResult> salaryResultBytime = new ArrayList<SalaryResult>(); 
	List<UploadOrder> showOrdersBytime = new ArrayList<UploadOrder>();
	List<String> orderNamesBytime = new ArrayList<String>();
	
	if(startDateSTR != null && !startDateSTR.equals("") && endDateSTR!= null && !endDateSTR.equals("")){
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();
		startDate = df1.parse(startDateSTR);
	    endDate = df1.parse(endDateSTR);
	    salaryResultBytime = SalaryCalcManager.getSalaryResultByDate(startDate, endDate);
	    showOrdersBytime = SalaryCalcManager.getUploadOrderFromSalaryResult(salaryResultBytime);
	    orderNamesBytime = UploadManager.getAllUploadOrderNames(showOrdersBytime);
	}
	
	//下面用到的temp变量
	String tempName = "";
	
	boolean byTime = false;
	if(orderNamesBytime.size() > 0 ){
		byTime = true;
	}
	
	session.setAttribute("allOrders", salaryResultBytime);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成导出页</title>
  
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
 

<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>     
<p>按时间导出</p>
<form action="" method="post">
<input type="hidden" name="type" value="bydate"/>
开始: <input name="startDate" type="text" id="datepicker1"/>----------结束: <input name="endDate" type="text" id="datepicker2"/>
<input type="submit" value="搜索"/>
</form>

<%
if(byTime){
%>

<form action="../SalaryExportServlet" method="post">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名称:
<input type="hidden" name="type" value="byname"/>
<select name="name">
	<option value="all" selected="selected">全部</option>
	<%
	for(int i = 0 ; i < orderNamesBytime.size() ; i ++){
		tempName = orderNamesBytime.get(i);
	%>
	<option value="<%=tempName %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input type="submit" value="导出"/>
</form>
<%
}
%>

<hr style="border : 1px dashed blue;" />

<p>按文件名导出</p>

<form action="../SalaryExportServlet" method="post">
<input type="hidden" name="type" value="byname"/>
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
<input type="submit" value="导出"/>
</form>
</body>
</html>
