<%@ page language="java"  pageEncoding="UTF-8"   import="jfree.*,utill.*" contentType="text/html;charset=utf-8"%> 
<%   
//访问量统计时间线    
//设置子标题   

String starttime = request.getParameter("starttime");
String endtime = request.getParameter("endtime"); 
String branch = request.getParameter("branch");  
String type = request.getParameter("type");

 
String filename = ""; 
String graphURL = ""; 
boolean flag = false ; 
if(!StringUtill.isNull(starttime) && !StringUtill.isNull(endtime))   {
	//filename = JfreeBar.getDynamicsales(starttime,endtime,branch,type); 
	filename = JfreeBar.getbar(); 
	graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename; 
	flag = true ;
} 
  
    
%>  

<% if(flag){
	%>  
	<img src="<%= graphURL %>"width=500 height=300 border=0 usemap="#<%= filename %>" >
  
	
	<%
} else {
	
	%>
	<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单审核</title>

<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script> 
<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>  
		<div class="weizhi_head">现在位置：查看明细</div>
		<form action="inventorybar.jsp" method="post" onsubmit="return checked()">
			<table cellpadding="1" cellspacing="0">
				<tr>
					<td>开始时间</td>
					<td><input name="starttime" type="text" id="starttime"
						value="<%=starttime%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						readonly="readonly" /></td>

					<td>结束时间</td>
					<td><input name="endtime" type="text" id="endtime" size="10"
						value="<%=endtime%>" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						readonly="readonly" />
					</td>

					<td>门店</td>
					<td><input type="text" name="branch" id="branch"
						value="<%=branch%>" /></td>
					<td>型号</td>

					<td><input type="text" name="product" id="product" value="" />
					</td>


					<td><input type="submit" value="查询" />
					</td>
				</tr>

			</table>
		</form>
		<!--  头 单种类  -->
	</div>
	</head>
	</html>
	
	
	
	
	<%
}%>

