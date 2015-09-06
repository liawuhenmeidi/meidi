<%@ page language="java" import="java.util.*,category.*,locate.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数据备份</title>  

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
$(function () {
});



 
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
   <div class="weizhi_head">现在位置：数据备份</div>     
   <div class="main_r_tianjia"> 
   </div>
     
   <div class="table-list">
  
<div class="btn">
  <form action="DatabaseServlet"  method ="post"  id="form"  onsubmit="return checkedd()" >   
              数据备份目录地址： <input type="text"  id="databasefile" name="databasefile" /> 
 
      <input type="submit"  value="开始备份" />
      </form>
</div>
</div>  
     
     
     </div>


</div>



</body>
</html>
