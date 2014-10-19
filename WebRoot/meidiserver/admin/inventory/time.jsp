<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>时间</title>
 
<script type="text/javascript" src="../../js/calendar.js"></script> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
 function gettime(){
	 
	 
	 var starttime = $("#starttime").val(); 
	 var endtime = $("#endtime").val(); 
  
	 window.opener.document.getElementById("starttime").value =starttime;
	 window.opener.document.getElementById("endtime").value =endtime;
	 window.opener.document.getElementById("time").value ="fresh";
	 //window.returnValue='refresh'; 
	// timer=window.setInterval("IfWindowClosed()",500);
      window.close(); 
 }

</script>
</head>
<body>
<!--   头部开始   -->
<!--   头部结束   -->  
  <table cellpadding="1" cellspacing="0" style="margin:auto">
   <tr> 
      <td>开始时间</td>
      <td><input name="control_date" type="text" id="starttime" size="10"
                        maxlength="10" onclick="new Calendar().show(this);"  readonly="readonly" /> </td>
   </tr> 
  <tr> 
      <td>结束时间</td> 
      <td> <input name="control_date2" type="text" id="endtime" size="10"
                        maxlength="10" onclick="new Calendar().show(this);"  readonly="readonly" /></td>
  </tr>
  <tr> 
     <td></td>
     <td><input type="button"  onclick="gettime()"  style="background-color:red;font-size:20px;"  value="确认" /></td>
  </tr>
 
 </table>
</body>
</html>
