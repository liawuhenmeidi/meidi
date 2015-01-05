<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>


<html>
 <head>
 <body>
<div id="addbook"  style="position:absolute;display:none">
 <table style="border-collapse: collapse;">
 <tr>
 <td>
 <a href="javascript:void(0);"  onclick="closedbook()" target="_blank">
     <img src="../../../image/bookShutDown.jpg" border="0"> 
     </a>
 </td>
 </tr>
  <tr>
   <td>
    <a id="daiban" href="javascript:void(0);"  onclick="openbook()" target="_blank">
     <img src="../../../image/book.jpg" border="0"> 
     </a>
   </td>
   <td>
   </td>
  </tr>
 
 </table>
 
 </div> 
<script> 
var x = 50,y = 60;
var xin = true, yin = true; 
var step = 2; 
var delay = 10; 
var obj=document.getElementById("addbook") ;
var url = "";


 
function floatAD() {
	var L=T=0 ;
	var R= document.body.clientWidth-obj.offsetWidth ;
	var B = document.body.clientHeight-obj.offsetHeight ;
	 B= 200;
	
	obj.style.left = (x)+'px' ;
	obj.style.top = (y)+'px' ;
	
	x = x + step*(xin?1:-1) ;
	if (x < L) {
		xin = true; x = L;
	} 
	if (x > R){ xin = false; x = R;} 
	y = y + step*(yin?1:-1) ;
	if (y < T) { yin = true; y = T ;} 
	if (y > B) { yin = false; y = B; } 
}  

function initbook(type){
	$("#addbook").css("display","block");
	var itl= setInterval("floatAD()", delay) ;
	obj.onmouseover=function(){clearInterval(itl);} ;
	obj.onmouseout=function(){itl=setInterval("floatAD()", delay);} ;
	if("inventory" == type){
		url = "../inventory/receipts.jsp";
	}
}


function closedbook(){
	$("#addbook").css("display","none");
}

function openbook(){
	window.open(url, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	$("#addbook").css("display","none");
}
</script> 
 
<!--  
代码分析： <div id="ad" style="position:absolute"><a href=http://www.sunsprite.net target="_blank"><img src="img01.gif" border="0"></a></div> 
<script> 
var x = 50,y = 60 //浮动层的初始位置，分别对应层的初始X坐标和Y坐标 
var xin = true, yin = true //判断层的X坐标和Y坐标是否在在控制范围之内，xin为真是层向右移动，否则向左；yin为真是层向下移动，否则向上 
var step = 1 //层移动的步长，值越大移动速度越快 
var delay = 10 //层移动的时间间隔,单位为毫秒，值越小移动速度越快 
var obj=document.getElementById("ad") //捕获id为ad的层作为漂浮目标 
function floatAD() { 
var L=T=0 //层移动范围的左边界(L)和上边界(T)坐标 

-->
 </body>
 
 </head>
</html>