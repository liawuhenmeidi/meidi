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
    	         url: "../server.jsp",    
    	         data:"method=disatchpHeadremind",  
    	         dataType: "",    
    	         success: function (data) { 
    	           var json =  $.parseJSON(data);   
    	           $("#disptach").html(json.dcount); 
    	           $("#installonly").html(json.icount); 
    	           $("#release").html(json.rcount);
    	           $("#huiyuan").html(json.hcount);
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
	  }
  }
 
 
</script>
 
<div class="head" >  
  <a href="javascript:void(0);" onclick="serach('Dispatching')" style="font-size:20px;">送货派工  </a><span style="color:red;font-size:20px;" id="disptach"></span> <br/>
  <a href="javascript:void(0);" onclick="serach('installonly')" style="font-size:20px;">安装派工  </a><span style="color:red;font-size:20px;" id="installonly"></span> <br/>
  <a href="javascript:void(0);" onclick="serach('release')" style="font-size:20px;">释放信息  </a><span style="color:red;font-size:20px;" id="release"></span> <br/>
  <a href="javascript:void(0);" onclick="serach('huiyuan')" style="font-size:20px;">员工注册  </a><span style="color:red;font-size:20px;" id="huiyuan"></span> <br/>

</div>    

