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
 
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
  <link rel="stylesheet" href="../css/jquery-ui.css">
  <script>
  $(function() {
    $( "#datepicker1" ).datepicker();
  });
  $(function() {
	    $( "#datepicker2" ).datepicker();
	  });
  
  $.datepicker.regional['zh-CN'] = {
		    clearText: '清除',
		    clearStatus: '清除已选日期',
		    closeText: '关闭',
		    closeStatus: '不改变当前选择',
		    prevText: '<上月',
		    prevStatus: '显示上月',
		    prevBigText: '<<',
		    prevBigStatus: '显示上一年',
		    nextText: '下月>',
		    nextStatus: '显示下月',
		    nextBigText: '>>',
		    nextBigStatus: '显示下一年',
		    currentText: '今天',
		    currentStatus: '显示本月',
		    monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		    monthNamesShort: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二'],
		    monthStatus: '选择月份',
		    yearStatus: '选择年份',
		    weekHeader: '周',
		    weekStatus: '年内周次',
		    dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
		    dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
		    dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
		    dayStatus: '设置 DD 为一周起始',
		    dateStatus: '选择 m月 d日, DD',
		    dateFormat: 'yy-mm-dd',
		    firstDay: 1,
		    initStatus: '请选择日期',
		    isRTL: false
		   };
  $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
  $.datepicker1.setDefaults($.datepicker.regional['zh-CN']);
  $.datepicker2.setDefaults($.datepicker.regional['zh-CN']);
  </script>
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
