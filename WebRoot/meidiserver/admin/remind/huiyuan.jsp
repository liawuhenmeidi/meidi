<%@ page language="java"  import="java.util.*,category.*,group.*,user.*"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");

List<User> ulist = UserManager.getUserszhuce(user);
HashMap<Integer,Group> map = GroupManager.getGroupMap();
boolean flag = UserManager.checkPermissions(user, Group.ManagerUser,"w");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会员管理页面</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function changes(id,name,statues){
	if(statues == 2){
	question = confirm("确定要删除"+name+"吗？");
	if (question == "0"){
       return ;
	}
	}
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=jihuo&id="+id+"&statues="+statues,
         dataType: "", 
         success: function (data) {
           window.location.href="huiyuan.jsp";
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

<div class="main">

<div class="">

<div class="table-list">

<table width="100%" cellspacing="0" id="table"> 
	<thead>
		<tr>

			<th align="left">职工名称</th>
			<th align="left">职工电话</th>
			<th align="left">职工类别</th>
			
			<th align="left">主管</th> 
			<th align="left">所属门店</th>
            <th align="left">是否审核通过</th>
			<!--  <th align="left">修改</th> -->
		</tr>
	</thead>
<% 
  for(int i =0 ;i<ulist.size();i++){
	 User u = ulist.get(i) ;
	
	 
%>
    <tr class="asc">

		<td align="left"><%=u.getUsername() %></td>
		<td align="left"><%= u.getPhone()%></td>
		<td align="left"><%=map.get(u.getUsertype()).getName() %></td>
		<td align="left"><%=u.getChargeName()%></td>
		<td align="left"><%=u.getBranchName() %></td>
		<td align="left">
		
		<% 
		   if(!UserManager.checkPermissions(u, Group.Manger)){
			   String str = 0 == u.getStatues()?"否":"是";
		  
			   if(flag){
					if(0 == u.getStatues()){  
					 %> 
					<%=str %>
					   
					   <input type="button" onclick="changes('<%=u.getId()%>','',1)"  value="激活"/>
					   <input type="button" onclick="changes('<%=u.getId()%>','<%=u.getUsername() %>',2)"  value="删除"/>
					 <%
					   }else { 
					 %> 
					<%=str %>
					   <input type="button" onclick="changes('<%=u.getId()%>','',0)"  value="关闭"/>
					  <input type="button" onclick="changes('<%=u.getId()%>','<%=u.getUsername() %>',2)"  value="删除"/>
					 <%
				   }
			   }else {
				  %> 
				  <%
			   }
		   }
		 %>

		 </td>       

    </tr>
    <% } %>
    </table>
</div>  
     
     
 </div>


</div>



</body>
</html>
