<%@ page language="java"  import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user"); 
List<LogisticsMessage>	list = LogisticsMessageManager.getlist();
  
//System.out.println("CarsService.getmap()"+CarsService.getmap());

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
<td>司机</td> 
<td>
	车牌号
</td>
<td>送货地址</td> 
<td>价格</td>
<td>送货时间</td>
<td>提交时间</td>
</tr>
	 
	 <%
	 if(null != list){
		 for(int i=0;i<list.size();i++){
			 LogisticsMessage ca = list.get(i);
			 
			 %>  
			 <tr class="asc"> 
			 <td >  
			<%=ca.getUser().getUsername() %>
	 </td>
	<td> 
<%=ca.getCars().getNum() %>  
	</td>   
	  <td>   
	  <%=ca.getBranch().getLocateName() %>
	  </td>
	  <td> 
	  <%=ca.getPrice() %>
	  </td>
	  <td>
	  <%=ca.getSendtime() %>
	  </td>
	  <td>
	  <%=ca.getSubmittime() %>
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
