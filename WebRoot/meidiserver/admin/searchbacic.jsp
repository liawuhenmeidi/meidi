<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%    
request.setCharacterEncoding("utf-8");
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>

</head>

<body style="scoll:no">

<script type="text/javascript"> 
var win ;
function startRequest(){ 
	 var search = $("#search").val();
	 if("fresh" == search){
		 sear = $("#sear").val();
		 sear += "&searched=searched";
		 $("#search").val("");
		 alert(sear);
		 initOrder(type,statues,num,page,sort,sear);
	 }
}
  
function clickSearch(){ 
	 $("#search").val("");
	 $("#sear").val(""); 
	 
	 if(win == null || win == ""){
		 win = window.open("searchOrderAll.jsp","time","resizable=yes,modal=yes,,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no,width=1000px,top="+(screen.height-500)/2+",left="+(screen.width-1000)/2+",height=500px,dialogTop:0px");  	
	 }else {
		 
	 }
	 
	
	 setInterval("startRequest()",500);   
} 



function SonMaximize() 
{ 
       if(win&&win.open&&!win.closed) 
       {       alert(1);
              //win.moveTo(-4,-4); 
              win.resizeTo(screen.availWidth+8,screen.availHeight+8); 
       }else{ 
              alert('还没有打开窗口或已经关闭'); 
       } 
} 


 
	
</script>


<div > 
  
   <input type="hidden" id="search"  value=""/>
   <input type="hidden" id="sear"  value=""/>
  
 
 <input type="button"  value="搜索" style="width:80px;height:20px" onclick="clickSearch()" />
 
</div>



</body>
</html>
