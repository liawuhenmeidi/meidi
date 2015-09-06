<%@ page language="java" import="java.util.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>送货查询页面</title>  

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script> <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript">
$(document).ready(function(){
	//initTime();
   });

 function change(){
	 var saledateStart = $("#saledateStart").val();
	 var saledateEnd = $("#saledateEnd").val();
	 var deliveryStatues  = $("#deliveryStatues").val();
	 var username  = $("#username").val();
	 var phone1  = $("#phone1").val();
	 
	 var str = "";
	 // pos == "" || pos == null || pos == "null"
	 if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
		 str += " and saledate like '%25"+saledateStart+"%25'";
	 } 
	 if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
		 str += " and saledate like '%25"+saledateEnd+"%25'";
	 }
	 if(deliveryStatues != null && deliveryStatues != "" && deliveryStatues != "null"){
		 str += " and deliveryStatues like '%25"+deliveryStatues+"%25'";
	 }
	 if(username != null && username != ""){
		 str += " and username like '%25"+username+"%25'";
	 } 
	 if(phone1 != null && phone1 != ""){
		 str += " and phone1 like '%25"+phone1+"%25'";
	 }
	 alert(str);
	 
	 window.location.href="songhuo.jsp?serch="+str;
  }
 

</script>
</head>


<body>
<div class="s_main">
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--  头 单种类  --> 
<div class="s_main_tit"><span class="qiangdan"><a href="songhuo.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
<div class="s_main_tit">订单查询页面</div>
 
 
<!--  订单详情  -->
<div class="s_main_box">
<form action="songhuo.jsp"  method ="post"  id="form"  onsubmit="return checkedd()" >

<table width="100%" class="s_main_table">
   <tr>
    <td width="25%">单号:</td>
    <td width="25%"><input type="text"  name="printlnid" id ="printlnid"/></td>
  </tr>
 <tr>
    <td>客户姓名:</td>
    <td><input type="text"  name="username" id ="username"/></td>
    
      </tr>
 <tr>
    
    <td width="25%">客户电话:</td>
    <td width="25%"><input type="text"  name="phone1" id ="phone1"/></td>
  </tr>
  <tr>
    
    <td width="25%">门店:</td>
    <td width="25%"><input type="text"  name="orderbranch" id ="orderbranch"/></td>
  </tr>

 <tr> 
    <td>送货状态</td>
    <td colspan="3">
     <select class = "category" name="deliveryStatues"  id="deliveryStatues" >
     <option value="0" >未完成 </option> 
     <option value="1" >已送货 </option>
     <option value="2" >已送货安装</option>
     <option value="-1">已退货</option>
      </select>

    </td>
    </tr>
  <tr> 
    <td></td>
    <td></td>
    <td width="25%"></td>
    <td width="25%"></td>
  </tr>
     
  
       <tr>
    <td></td>
    <td></td>
     <td width="25%">
     <td width="100%" class="center"><input type="submit"  value="查询" /></td>
    <td width="25%"></td>
  </tr>
</table>
</form> 
<br/>
<br/>
</div>














<!--  zong end  -->
</div>






</body>
</html>