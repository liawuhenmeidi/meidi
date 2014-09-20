<%@ page language="java" import="java.util.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String ptype = request.getParameter("ptype");
String id = request.getParameter("id");
List<User> list = UserManager.getUsersL(id); 
HashMap<Integer,Group> map = GroupManager.getGroupMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function changes(id,statues){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=jihuo&id="+id+"&statues="+statues,
         dataType: "", 
         success: function (data) { 
           window.location.href="huiyuanJ.jsp?id=<%=id%>&ptype=<%=ptype%>";
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
 <div class="main_r_tianjia">  
   <ul>   
     <li><a href="juese.jsp?ptype=<%=ptype%>">返回上级</a></li>
     </ul>
   </div> 
<!--   头部结束   -->

<div class="main">
  
      
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">现在位置：职工管理</div>     

      
   <div class="table-list">
<table width="100%" cellspacing="1" id="table">
	<thead>
		<tr>
			<th align="left" width="20"><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"></th>
			<th align="left"></th>
			<th align="left">职工ID</th>
			<th align="left">职工名称</th>
			<th align="left">职工类别</th>
			<!-- <th align="left">职位</th>  -->
			<th align="left">所属门店</th>
			<th align="left">入职日期</th>
            <th align="left">操作</th>
			<!--  <th align="left">修改</th> -->
		</tr>
	</thead>
<tbody>
<% 
  for(int i =0 ;i<list.size();i++){
	 User u = list.get(i) ;
%>
    <tr id="<%=i%>" class="asc"  onclick="updateClass(this)">
    
		<td align="left"><input type="checkbox" value="1" name="userid[]"></td>
		<td align="left"></td>
		<td align="left"><%=i+1 %></td>
		<td align="left"><%=u.getUsername() %></td>
		<td align="left"><%=map.get(u.getUsertype()).getName() %></td>
	<!--	<td align="left"><%=u.getPositions() %></td>   -->
		<td align="left"><%=u.getBranch() %></td>
		<td align="left"><%=u.getEntryTime() %></td>       
		<!--  <td align="left">
			<a href="huiyuanUpdate.jsp?id=<%=u.getId() %>">[修改]</a>
		</td>
		--> 
		<td align="left">
		 <%
		   if(0 == u.getStatues()){
		 %>
		 否
		 <input type="button" onclick="changes('<%=u.getId()%>',1)"  value="激活"/>
		   <input type="button" onclick="changes('<%=u.getId()%>',2)"  value="删除"/>
		 <%
		   }else {
		 %> 
		 是
		 <input type="button" onclick="changes('<%=u.getId()%>',0)"  value="关闭"/>
		  <input type="button" onclick="changes('<%=u.getId()%>',2)"  value="删除"/>
		 <%
	   }
		 %>
		 
		 </td> 
    </tr>
    <% } %>
</tbody>
</table>
  

<div id="pages"></div>
</div>  
     
     
     </div>


</div>



</body>
</html>
