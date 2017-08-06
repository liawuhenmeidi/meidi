<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>






<!DOCTYPE html>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta http-equiv="refresh" content="300; url=login.jsp"/>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="css/songhuo.css">
<title>处理结果</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

</head>

<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">

$(function () { 
	if("updated" == type){
		
		checkedd();
	}
	
});
function checkedd(){
	//parent.location.reload(); 
	//window.returnValue='refresh'; 
	window.close();
	//window.oper.reload();
}

</script>

<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" /> 
  </jsp:include>
 
<div class="s_main_tit">对不起您的操作出现错误，请联系管理员解决</div>


<body>

<!--  zhanghao  -->

<div class="shwo_main">
   
   <!--   登陆开始   -->
   <br>  

</div>

<div style="width:300px;margin:0 auto; padding:20px 0;">
		 		<a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=43052402000113" style="display:inline-block;text-decoration:none;height:20px;line-height:20px;"><img src="ga.png" style="float:left;"/><p style="float:left;height:20px;line-height:20px;margin: 0px 0px 0px 5px; color:#939393;">湘公网安备 43052402000113号</p></a>
		 	</div>


</body>
</html>