<%@ page language="java" import="java.util.*,message.*,locate.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>


<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">




</script>

<br/>  
 
<div id="wrap" style="text-align:center;">  
<form  action="server.jsp"  name = "myForm" method ="post"  id="form"   >

<input type="hidden" name="method" value="updateorder"/>
<input type="hidden" name="typeMethod" id="print" value=""/>  

<table  cellspacing="1"  id="table"  style="margin:auto;"> 
       <tr class="asc">
        <td align="center"  colspan=2>安装网点</td> 
        <td align="center"  colspan=2 ></td>
       </tr>
       <tr class="asc"> 
        <td align="center">顾客姓名</td> 
        <td align="center">a</td>
        <td align="center">顾客电话</td>
        <td align="center">a</td>
       </tr>
       <tr class="asc"> 
        <td align="center">送货名称</td> 
        <td align="center">a</td>
        <td align="center">顾客电话</td>
        <td align="center">a</td>
       </tr>
        <tr class="asc"> 
        <td align="center">送货地址</td> 
        <td align="center">a</td>
        <td align="center">预约日期</td>
        <td align="center">a</td>
       </tr>
   </table> 

</form>
     </div>

</body>
</html>
