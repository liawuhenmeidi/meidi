<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">

</style> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />   
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

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
    	         url: "../server.jsp",    
    	         data:"method=disatchpHeadremind",  
    	         dataType: "",    
    	         success: function (data) { 
    	           var json =  $.parseJSON(data);   
    	           $("#disptach").html(json.dcount); 
    	           $("#installonly").html(json.icount); 
    	           $("#release").html(json.rcount);
    	           $("#huiyuan").html(json.hcount);
    	           $("#inventory").html(json.inventory);
    	           // huiyuan
    	         //  $("#neworder").html(json.ncount);
    	         //  $("#returns").html(json.recount);  
    	           },  
    	         error: function (XMLHttpRequest, textStatus, errorThrown) {
    	            } 
    	           });

       }

  function serach(str){ 
	  if("Dispatching" == str){ 
		  window.open('remind/dispatch.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("installonly" == str){  
		  window.open('remind/install.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("release" == str){
		  window.open('remind/release.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("returns" == str){ 
		  window.open('remind/return.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("huiyuan" == str){ 
		  window.open('remind/huiyuan.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("inventory" == str){ 
		  window.open('../inventory/receipts.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }
  }
 
 
</script>
  </head>
 <body>
 <div style="text-align:center">
 <table  cellspacing="1" style="width: 95%;margin:auto"> 
  <tr class="asc" id="1">  
   <td>
    <a href="javascript:void(0);" onclick="serach('Dispatching')" style="font-size:20px;">送货派工  </a>
   </td>
   <td>
    <span style="color:red;font-size:20px;" id="disptach"></span> <br/>
   </td>
   <td>
    <a href="javascript:void(0);" onclick="serach('installonly')" style="font-size:20px;">安装派工  </a>
  </td>
  <td>
 <span style="color:red;font-size:20px;" id="installonly"></span> <br/>
   </td>
   <td>
  <a href="javascript:void(0);" onclick="serach('release')" style="font-size:20px;">释放信息  </a>
  </td>
  <td>
   <span style="color:red;font-size:20px;" id="release"></span> <br/>
   </td>
   <td>
  <a href="javascript:void(0);" onclick="serach('huiyuan')" style="font-size:20px;">员工注册  </a>
 </td>
 <td>
 <span style="color:red;font-size:20px;" id="huiyuan"></span> <br/>
   </td>
    <td>
  <a href="javascript:void(0);" onclick="serach('inventory')" style="font-size:15px;">调拨单待确认</a> 
   &nbsp;&nbsp;&nbsp;&nbsp;   
  <span style="color:red;font-size:20px;" id="inventory"></span>
   </td>
  </tr >
  
  </table> 
  </div>
</body>

</html>
 


 
  


