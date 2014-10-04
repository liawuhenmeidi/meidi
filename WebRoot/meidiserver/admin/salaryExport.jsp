<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	System.out.println(startDate + "|" + endDate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成导出页面</title>
  
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

<form action="/meidi/meidiserver/SalaryExport" method="post">
<p>开始: <input name="startDate" type="text" id="datepicker1"></p>
----------
<p>结束: <input name="endDate" type="text" id="datepicker2"></p>
<input type="submit" value="确认"/>
</form>
</body>
</html>
