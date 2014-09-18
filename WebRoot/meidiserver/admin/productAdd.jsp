<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*,product.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String categoryID = request.getParameter("categoryID");
System.out.println(categoryID);
String action = request.getParameter("action");
if("add".equals(action)){
	System.out.println(categoryID);
	String productName = request.getParameter("name");
	System.out.println(productName);
	if(!StringUtill.isNull(productName)){
		System.out.println(categoryID);
		ProductManager.save(productName,categoryID);
		response.sendRedirect("product.jsp?categoryID="+categoryID);
	}
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript">

 function check(){
	
	 var name = $("#name").val();
	 //alert(name);
	 if(name == "" || name == null || name == "null"){
		 alert("产品型号不能为空"); 
		 return false;
	 }	 
	 return true ;
 }
 
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main">
   
     
 <!--       -->    
     <div class="">    
   <div class="weizhi_head">现在位置：类别:</div>     
   <div class="main_r_tianjia">
   </div>
     <div >
     <form action="productAdd.jsp"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="action" value="add"/>
      <input type="hidden" name="categoryID" value="<%=categoryID%>"/>
     <div class="juese_head">产品型号：
        <input type="text"  id="name" name="name" />      
       
    <input type="submit" value="提  交" />
     
     </div>
 </form>
     
     
     
     </div>

     
     
     </div>





</body>
</html>
