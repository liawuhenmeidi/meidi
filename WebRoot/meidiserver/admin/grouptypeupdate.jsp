<%@ page language="java" import="java.util.*,category.*,grouptype.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String id = request.getParameter("id");
Grouptype list =GrouptypeManager.getGrouptype(Integer.valueOf(id));
      
%>  
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<script type="text/javascript">
var bid = "<%=id%>";

function changes(){
	var str1 = $("#locate").val();
	if(str1 == null || str1 == ""){
		alert("不能为空");
	}  
	$.ajax({  
        type: "post",  
         url: "server.jsp",
         data:"method=grouptypeupdate&id="+str1+"&bid="+bid,
         dataType: "",  
         success: function (data) {
           window.location.href="juesetype.jsp";
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

   <div class="weizhi_head">现在位置：职位类别管理</div>     

     
   <div class="table-list">
<table width="100%" cellspacing="0">
	<thead>
		
	</thead>

</table>

<div class="btn">
 
<!--  <input type="button" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  -->

</div>
<div class="btn">   
     
      门店名称： <input type="text"  id="locate" value= "<%=list.getName() %>" name="locate" value= ""/>  
  <input type="button" onclick="changes()"  value="修改"/> </br>   

</div>
</div>
 

 
</body>
</html>
