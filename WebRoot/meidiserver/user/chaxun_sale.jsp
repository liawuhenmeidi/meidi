<%@ page language="java" import="java.util.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>导购查询报装单</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
 <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="../css/songhuo.css">


<script type="text/javascript">
$(document).ready(function(){
	initTime();
   });

 function change(){
	 var flag = false ;
	 var saledateStart = $("#saledateStart").val();
	 var saledateEnd = $("#saledateEnd").val();
	 var sailId = $("#sailId").val();
	 var pos = $("#pos").val();
	 var username = $("#username").val();
	 var phone1 = $("#phone1").val();
	 var str = "";
	 // pos == "" || pos == null || pos == "null"
	 if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
		 str += " and saledate BETWEEN '" + saledateStart + "'  and  ";
	     flag = true ;
	 } 
	 
	 if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
		 str += " ' " + saledateEnd + "'";
	 }else if(flag){
		 str += "now()";
	 }
	   
	 if(sailId != null && sailId  != "" && sailId  != "null"){
		 str += " and sailId like '%25"+sailId+"%25'";
	 }
	 if(pos != null && pos != "" && pos != "null"){
		 str += " and pos like '%25"+pos+"%25'";
	 }
	 if(username != null && username != "" && username != "null"){
		 str += " and username like '%25"+username+"%25'";
	 } 
	 if(phone1 != null && phone1 != "" && phone1 != "null"){
		 str += " and phone1 like '%25"+phone1+"%25'";
	 };
  
	 window.location.href="serch_list.jsp?serch="+str;
  }
 
function initTime(){ 
	   var opt = { };
	    opt.date = {preset : 'date'};
		$('#saledateStart').val('').scroller('destroy').scroller($.extend(opt['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
		var opt2 = { };
	    opt2.date = {preset : 'date'};
		$('#saledateEnd').val('').scroller('destroy').scroller($.extend(opt2['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
}
</script>
</head>

<body>
<div class="s_main">
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
<!--  头 单种类  -->
<div class="s_main_tit"><span class="qiangdan"><a href="order.jsp">我要报装</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
<div class="s_main_tit">订单查询页面</div>
 

<!--  订单详情  -->
<div class="s_main_box">
<form action="serch_list.jsp"  method ="post"  id="form"  onsubmit="return checkedd()" >

<table width="100%" class="s_main_table">
<tr>
    <td width="25%">单号:</td>
    <td width="25%"><input type="text"  name="printlnid" id ="printlnid"/></td>
  </tr>
  <tr>
    <td width="40%">销售开始时间:</td>
    <td>  <input class="date" type="text" name="saledateStart" placeholder=""  id="saledateStart"  readonly="readonly" style="width:90% "></input>
     </td>
      </tr> 
    <tr>
     <td width="40%">销售结束时间:</td>
     <td ><input class="date2" type="text" name="saledateEnd" id ="saledateEnd" placeholder=""  readonly="readonly" style="width:90% "></input> 
     </td>
  </tr> 
 <tr>
    <td>销售单号:</td>
    <td width="25%"><input type="text"  name="sailId" id ="sailId"/></td>
 </tr>
 <tr>
    <td width="25%">POS单号:</td>
    <td width="25%"><input type="text"  name="pos" id ="pos"/></td>
  </tr>
 <tr>
    <td>客户姓名:</td>
    <td><input type="text"  name="username" id ="username"/></td>
    
      </tr>
 <tr>
    
    <td width="25%">电话:</td>
    <td width="25%"><input type="text"  name="phone1" id ="phone1"/></td>
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
   <!--   <input type="button" onclick="change()"  value="查询"/> </td>-->  
     <td width="100%" class="center"><input type="submit"  value="查询" /></td>
     
    <td width="25%"></td>
  </tr>
</table>
</form>
<br/>

<br/>
</div>


</div>

</body>
</html>