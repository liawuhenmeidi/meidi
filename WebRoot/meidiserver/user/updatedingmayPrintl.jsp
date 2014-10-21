<%@ page language="java" import="java.util.*,category.*,orderproduct.*,group.*,user.*,utill.*,product.*,order.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
User user = (User)session.getAttribute("user");
String id = request.getParameter("id");
String method = request.getParameter("method");
String message = "";
if("tuihuo".equals(method)){ 
	message = "订单已打印，请输入退货理由";
}else if("huanhuo".equals(method)){
	message = "订单已打印，请输入换货理由";
}else {
	message = "订单已打印，请输入修改申请";
}

%>

<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>提交修改请求页</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
  <script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="../css/songhuo.css">
<link rel="stylesheet" href="../css/bass.css">

<script type="text/javascript">


function checkedd(){
	var message = $("#message").val();
	 if(message == "" || message == null || message == "null"){
		 alert("提交内容不能为空");
		 return false;
	 }
	 return true ;
}
</script>

</head>


<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" /> 
  </jsp:include>
<form action="OrderServlet"  method ="post"  id="form"  onsubmit="return checkedd()" >
<!--  头 单种类  --> 
 
<input type="hidden" name="id" value="<%=id%>"/>
<input type="hidden" name="mm" value="<%=method%>"/>
<input type="hidden" name="method" value="println"/>
<div class="s_main_tit">门店:<span class="qian"><%=user.getBranchName() %></span></div>
<div class="s_main_tit"><%=message %><span class="qiangdan"></span></div>

 
       
 <div class="center"  > 
   <textarea  id="message" name="message" >  </textarea>
 </div>
<!--  订单详情  -->
<div class="center"> <input type="submit" value="提  交" /></div>
</form>

</div>

</body>
</html>