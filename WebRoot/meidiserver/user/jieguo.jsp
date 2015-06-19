<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
   
 String  resCode = (String)session.getAttribute("message");
 session.removeAttribute("message"); 
   
 String type = request.getParameter("type");
String type1 = request.getParameter("type1");
    
String mark = request.getParameter("mark");
  
 if(!StringUtill.isNull(mark)){  
		resCode = RemarkUtill.getMessage(Integer.valueOf(mark));
 }
 // System.out.println("oid"+oid);
%>
 




<!DOCTYPE html>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta http-equiv="refresh" content="300; url=login.jsp"/>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="../css/songhuo.css">
<title>处理结果</title> 
</head> 

<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var type = '<%=type%>';

$(function () {  
	if("updated" == type){
		//alert(1);
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

	 
<body> 
 <div class="s_main_tit"><span class="qiangdan"><a href="makeInventory/makeinventoryall.jsp?type=<%=type1%>">返回</a></span></div> 
<!--  zhanghao  -->  
  
<div class="shwo_main"> 
   
   <!--   登陆开始   -->
  
   <br>  
   <div class="s_main_tit"><%=resCode %></div>
 
</div>

</body>
</html>