<%@ page language="java"    pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
td {
 align:center
}
</style> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />   
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<script type="text/javascript"> 
$(document).ready(function () {  
	  init();
	  //setInterval("startRequest()",5000);
   });  
    
     function init(){ 
    	 startRequest();
     } 
       function startRequest(){	 
    	   $.ajax({ 
    	        type: "post", 
    	         url: "server.jsp",    
    	         data:"method=headremind",  
    	         dataType: "",   
    	         success: function (data) { 
    	           var json =  $.parseJSON(data);   
    	           $("#zhuce").html(json.ucount);
    	           $("#motyfy").html(json.mcount); 
    	           $("#release").html(json.rcount); 
    	           $("#neworder").html(json.ncount);
    	           $("#returns").html(json.recount); 
    	           $("#huanhuo").html(json.hcount); 
    	           $("#inventory").html(json.inventory); 
    	           },  
    	         error: function (XMLHttpRequest, textStatus, errorThrown) {
    	            } 
    	           });

       }

  function serach(str){ 
	  
	  if("zhuce" == str){ 
		  window.open('remind/huiyuan.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("neworder" == str){ 
		  window.open('remind/neworder.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("motyfy" == str){
		  window.open('remind/motify.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("release" == str){
		  window.open('remind/release.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("returns" == str){ 
		  window.open('remind/return.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("inventory" == str){ 
		  window.open('inventory/receipts.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }
	
  }
 
 
</script>
  
 </head>
 <body>
 <div style="text-align:center">
 <table  cellspacing="1" style="width: 95%;margin:auto"> 
  <tr class="asc">  
   <td >
   <a href="javascript:void(0);" onclick="serach('neworder')" style="font-size:15px;"> 收到新单据</a> 
   &nbsp;&nbsp;&nbsp;&nbsp;
   <span style="color:red;font-size:20px;" id="neworder"></span>
   </td>
   <td>
  <a href="javascript:void(0);" onclick="serach('zhuce')" style="font-size:15px;">职工注册信息</a>
   &nbsp;&nbsp;&nbsp;&nbsp; 
  <span style="color:red;font-size:20px;" id="zhuce"></span>
   </td>
   <td>
  <a href="javascript:void(0);" onclick="serach('release')" style="font-size:15px;">安装单位释放</a> 
  &nbsp;&nbsp;&nbsp;&nbsp;   
  <span style="color:red;font-size:20px;" id="release"></span>
   </td>
   <td>
  <a href="javascript:void(0);" onclick="serach('motyfy')" style="font-size:15px;">导购修改申请</a>
  &nbsp;&nbsp;&nbsp;&nbsp;   
 <span style="color:red;font-size:20px;" id="motyfy"></span> 
   </td>
   <td>
  <a href="javascript:void(0);" onclick="serach('returns')" style="font-size:15px;">导购退货申请</a> 
  &nbsp;&nbsp;&nbsp;&nbsp;   
  <span style="color:red;font-size:20px;" id="returns"></span>
   </td>
    <td>
  <a href="javascript:void(0);" onclick="serach('huanhuo')" style="font-size:15px;">导购换货申请</a> 
  &nbsp;&nbsp;&nbsp;&nbsp;   
  <span style="color:red;font-size:20px;" id="huanhuo"></span>
   </td>
    <td>
  <a href="javascript:void(0);" onclick="serach('inventory')" style="font-size:15px;">调拨单待确认</a> 
   &nbsp;&nbsp;&nbsp;&nbsp;   
  <span style="color:red;font-size:20px;" id="inventory"></span>
   </td>
  </tr>
  </table> 
  </div>
</body>

</html>