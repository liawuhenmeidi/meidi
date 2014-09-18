<%@ page language="java"  import="java.util.*,category.*,group.*,user.*,utill.*,company.*,order.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
User user = (User)session.getAttribute("user");
%>  
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
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
    	           },  
    	         error: function (XMLHttpRequest, textStatus, errorThrown) {
    	            } 
    	           });

       }

  function serach(str){ 
	  
	  if("zhuce" == str){ 
		  window.showModalDialog('remind/huiyuan.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("neworder" == str){ 
		  window.showModalDialog('remind/neworder.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("motyfy" == str){
		  window.showModalDialog('remind/motify.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("release" == str){
		  window.showModalDialog('remind/release.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }else if("returns" == str){ 
		  window.showModalDialog('remind/return.jsp', 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	  }
	
  }
 
 
</script>
  
 
<div class="head" > 
<a href="javascript:void(0);" onclick="serach('neworder')" style="font-size:20px;"> 收到新单据</a><span style="color:red;font-size:20px;" id="neworder"></span>  <br/> 
  <a href="javascript:void(0);" onclick="serach('zhuce')" style="font-size:20px;">职工注册信息   </a><span style="color:red;font-size:20px;" id="zhuce"></span> <br/>
  <a href="javascript:void(0);" onclick="serach('release')" style="font-size:20px;">安装单位释放信息   </a><span style="color:red;font-size:20px;" id="release"></span> <br/>
  <a href="javascript:void(0);" onclick="serach('motyfy')" style="font-size:20px;">导购修改申请   </a><span style="color:red;font-size:20px;" id="motyfy"></span> <br/> 
  <a href="javascript:void(0);" onclick="serach('returns')" style="font-size:20px;">导购退货申请   </a><span style="color:red;font-size:20px;" id="returns"></span> <br/> 
  
  
</div>    

