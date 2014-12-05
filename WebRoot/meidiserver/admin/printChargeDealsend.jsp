<%@ page language="java" import="java.util.*,inventory.*,product.*,branch.*,utill.*,java.text.SimpleDateFormat,category.*,orderPrint.*,gift.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
  
<%   
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");
 
String uid = request.getParameter("dealsendID");
String name = request.getParameter("name");
String price = request.getParameter("price");

%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印页面</title>
<style media=print type="text/css">   
.noprint{visibility:hidden}   
</style>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../css/bass.css" />
<link rel="stylesheet" href="../css/songhuo.css"/>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
 function println(){
	 
	 window.print();

 }
 
 
</script>
</head>

<body>
  
<table width="1010">

  <tr>
    <td colspan="2">&nbsp;</td> 
    <td width="384" rowspan="2" align="center" style="font-size:30px; font-family:"楷体";><strong>安装网点结款</strong></td>
    <td width="300"></td> 
  </tr>
  <tr>  
    <td width="110" style="font-size:25px; font-family:"楷体";>&nbsp;&nbsp;&nbsp;<strong>&nbsp;</strong></td>
    <td></td>
    <td><strong><FONT size=4>日期:<%=TimeUtill.getdateString() %></strong></FONT></td>
  </tr>  
  
</table>   

<table width="1010" border="0" cellpadding="0" cellspacing="0"  id="t"  style="font-size:28px; font-weight:bolder;">
<tr>  
  <td height="30" colspan="5" align="center" valign="middle" bgcolor="#FFFFFF">
  <table width="1010" width="100%" border="0" cellspacing="0" cellpadding="0"> 
 <tr>  

 <td id="d" width="15%" align="center">
网点名称:<%=UserService.getMapId().get(Integer.valueOf(uid)).getUsername() %>
 </td>
 <td id="d" width="15%" align="center"> 
结款明细名:<%=name %>
 </td>
 </tr>  
 <tr>
 <td id="d" width="15%" align="center">
安装费总计:<%=price %>元
 </td>
 <td id="d" width="15%" align="center">
经理签字：
 </td>
 
 </tr>
</table>
  </td>
</tr> 
 
</table>
<center> <input class="noprint" type=button name='button_export' title='打印1' onclick="println()" value="打印"></input></center>
 <!-- <p class="noprint">打印之后请修改打印状态</p>  --> 
</body>
</html>


