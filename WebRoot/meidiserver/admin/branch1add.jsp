<%@ page language="java" import="java.util.*,category.*,utill.*,branch.*,branchtype.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String pid = request.getParameter("pid");
String id = request.getParameter("id"); 
System.out.println(pid+")))"+id);
Branch branchold = null;
String branchoidname = "";
String message = null;
String[] permission = null ;
if(!StringUtill.isNull(id)){
	branchold= BranchManager.getLocatebyid(id);
	branchoidname = branchold.getLocateName();
	message = branchold.getMessage();
	//if(!StringUtill.isNull(message)){
	//	permission = message.split("_");
	//}
} 

//String json = StringUtill.GetJson(permission);

String action = request.getParameter("action");
if("add".equals(action)){
	String branchname = request.getParameter("locate"); 
	System.out.println(branchname);
	permission = request.getParameterValues("permission");
    String messagenew = "";
    if(permission != null ){  
		for(int i = 0;i<permission.length;i++){
			if(!StringUtill.isNull(permission[i])){
				messagenew += permission[i]+"_";
			}
		} 
    } 
	Branch branch = new Branch(); 
	if(!StringUtill.isNull(id)){
		branch.setId(Integer.valueOf(id)); 
	}
	
	branch.setLocateName(branchname);
	branch.setPid(Integer.valueOf(pid));
	branch.setMessage(messagenew);   
	BranchManager.save(branch); 
	response.sendRedirect("branch1.jsp?id="+pid); 
}

BranchType branch = BranchTypeManager.getLocate(Integer.valueOf(pid)); 

%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
  
<script type="text/javascript">
var pid = "<%=pid%>";
var json = "<%=message%>";
var products = new Array();
$(document).ready(function(){
	products = json.split("_"); 
	 for(var i=0;i<products.length;i++){
		 $("#"+products[i]).attr("checked","checked");
	 }
	
	
	
	
  $("#locate").focus(function(){
      $("#locate").css("background-color","#FFFFCC");
  }); 
  
  $("#locate").blur(function(){
	   changes();
   }); 

   });

function changes(){
	var str1 = $("#locate").val();
	if(str1 == null || str1 == ""){
		alert("不能为空");
	}  
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=branchis&id="+str1+"&pid="+pid,
         dataType: "", 
         success: function (data) {
        	
        	 if(data == "true" || data == true ){
        		var question = confirm("门店已存在？是否继续?");	
     			if (question == "0"){   
     				$("#locate").focus();
     				return  ;
     			}  

        	 }
        	 
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
   
 <!--       -->     
 
   <div class="weizhi_head">现在位置：<%=branch.getName()%></div>     
     <div class="main_r_tianjia">
   <ul>                                                                                                     
     <li><a href="branch1.jsp?id=<%=pid %>">返回</a></li>
     </ul>
     
   </div>
        
     <div> 
     
     <form action="branch1add.jsp"  method = "post"  onsubmit="return checkedd()">
      <input type="hidden" name="action" value="add"/>
      <input type="hidden" name="pid" value="<%=pid%>"/> 
       <input type="hidden" name="id" value="<%=id%>"/> 
          门店名称<span style="color:red">*</span>&nbsp;&nbsp;&nbsp;&nbsp;：
      <input type="text"  id="locate" value="<%=branchoidname %>" name="locate" /> <br />     
       门店报装单需要信息<span style="color:red">*</span>:  
     <ul class="juese_add">  
        <li><input type="checkbox" value="pos" name = "permission" id="pos" />&nbsp;pos(厂送)单号</li>
        <li><input type="checkbox" value="sailId" name = "permission" id="sailId" />&nbsp;OMS订单号</li>
        <li><input type="checkbox" value="checked" name = "permission" id="checked" />&nbsp;验证码(联保单)</li>
     </ul>  
      <input type="submit" value="提  交" />

 </form>

     </div>
     
     
     
   






</body>
</html>
