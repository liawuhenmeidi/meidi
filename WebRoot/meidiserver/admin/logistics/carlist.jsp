<%@ page language="java"  import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user");
List<Cars> list = CarsManager.getlist();
 


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>职工管理</title>
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
function mysubmit(){ 
 
	var uid = $("#uid").val();
	var num = $("#num").val();
	alert(1); 
	if(null == uid || "" == uid){
		alert("车主不能为空"); 
		return false; 
	}
	
	if(null == num || "" == num){
		alert("车牌号不能为空");
		return false;
	}
	
	
	$("#myform").submit();
}
 


</script>
</head>

<body>
<!--   头部开始   -->

 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
 
     
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">现在位置：车辆登记</div> 

   <div class="table-list">  
<table width="100%" cellspacing="1" id="table"> 
<tr class="dsc">
<td>车主</td> 
<td>
	车牌号
	</td> 
</tr>
	
	
	   
	
	 <%
	 if(null != list){
		 for(int i=0;i<list.size();i++){
			Cars ca = list.get(i);
			 
			 %> 
			 <tr class="asc"> 
			 <td >
			<%=ca.getUser().getUsername() %> 
	 </td>
	<td> 
	 <%=ca.getNum() %> 
	</td>
	  
	</tr> 
			 
			 <% 
		 }
	 }
	 %>
	 
	
	
</table>

     
     </div>


</div>



</body>
</html>
