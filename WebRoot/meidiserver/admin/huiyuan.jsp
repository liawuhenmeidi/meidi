<%@ page language="java"  import="java.util.*,category.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");  
User user = (User)session.getAttribute("user");
List<User> list = UserManager.getUsersNodelete(user);  
List<User> listg = new ArrayList<User>(); 
 
listg = UserManager.getUsersNodeleteDown(user, 1); 
 
HashMap<Integer,Group> map = GroupManager.getGroupMap();

boolean flag = UserManager.checkPermissions(user, Group.ManagerUser,"w");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>职工管理</title>
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
var timer1 ;
function changes(id,name,statues){
	if(statues == 2){
	question = confirm("确定要删除"+name+"吗？");
	if (question == "0"){
       return ;
	}
	}
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
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

function update(uid,phone){
	$("#fresh").val("");
	
	$("#phone").val("");
	

	 winPar = window.open("updateUserPhone.jsp?uid="+uid,"phone","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	 timer1 = setInterval("startRequest('"+uid+"')",500);
}

function startRequest(uid){ 
	 var time = $("#fresh").val();
	 if("fresh" == time){
		 $("#fresh").val("");
		 var phone = $("#phone").val();
		 var branchid = $("#branchid").val();
		 clearInterval(timer1);
		 $.ajax({ 
		        type: "post", 
		         url: "server.jsp",
		         data:"method=updatePhone&uid="+uid+"&phone="+phone+"&branchid="+branchid,
		         dataType: "",  
		         success: function (data) {
		           window.location.href="huiyuan.jsp";
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        // alert(errorThrown); 
		            } 
		           });
		
		 //window.location.href='inventoryDetail.jsp?ctype='+ctype+'&branchid='+branchid+'&starttime='+starttime+'&endtime='+endtime; 
	 }
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
   <div class="weizhi_head">现在位置：职工管理  </div> 
   <!--      
   <div class="main_r_tianjia">
    
   <ul>                                                                                                  
     <li><a href="huiyuanAdd.jsp">添加职工</a></li> 
     </ul>
   
   </div> 
     -->
   <div class="table-list">
   
   <input type="hidden" id="fresh"  value=""/>
  <input type="hidden" id="phone"  value=""/>
  <input type="hidden" id="branchid"  value=""/>
   
    
<table width="100%" cellspacing="1" id="table"> 
	<thead>
		<tr >
		    
			<th align="left" width="20px">序号</th>
			<th align="left">职工名称</th>
			<th align="left">职工电话</th>
			<th align="left">职工类别</th>
			<th align="left">主管</th> 
			<th align="left">所属门店</th>
			<th align="left">入职日期</th>
            <th align="left">是否审核通过</th>
			<!--  <th align="left">修改</th> -->
		</tr>
	</thead>
<tbody> 
<% 
  for(int i =0 ;i<list.size();i++){
	 User u = list.get(i) ;
	 
	 
%> 
    <tr  id="<%=u.getId() %>" class="asc"  onclick="updateClass(this)" ondblclick="update('<%=u.getId()%>','<%=u.getPhone() %>')">    

		<!-- <td align="left"><%=u.getId() %></td>   -->
		<td   ><%=i+1 %></td>
		<td align="left"><%=u.getUsername() %></td>
		<td align="left"><%=u.getPhone() %></td> 
		<td align="left"><%=map.get(u.getUsertype()).getName() %></td>
		<td align="left"><%=u.getChargeName()%></td>
		<td align="left"><%= u.getBranchName() %></td>
		<td align="left"><%=u.getEntryTime() %></td> 
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
		<!--
		 <td align="left">
			<a href="huiyuanUpdate.jsp?id=<%=u.getId() %>">[修改]</a>
		</td>
		 -->
    </tr>
    <% } %>
    
    <% 
     if(listg != null ){
    	 
 
  for(int i =0 ;i<listg.size();i++){
	 User u = listg.get(i) ;
	 
%> 
    <tr class="asc"  id="<%=u.getId() %>"  onclick="updateClass(this)">
    
		
		
		<!--  <td align="left"><%=u.getId() %></td>  -->
		<td align="left"><%=list.size()+1+i %></td>
		<td align="left"><%=u.getUsername() %></td>
		<td align="left"><%=u.getPhone() %> </td> 
		<td align="left"><%=map.get(u.getUsertype()).getName() %></td>
		<td align="left"><%=u.getChargeName()%></td>
		<td align="left"><%=BranchService.getMap().get(Integer.valueOf(u.getBranch())) == null ? "":BranchService.getMap().get(Integer.valueOf(u.getBranch())).getLocateName() %></td>
		<td align="left"><%=u.getEntryTime() %></td> 
		<td align="left"> 
		 <%
		 String str = 0 == u.getStatues()?"否":"是";
		  
		   if(flag){
				   if(0 == u.getStatues()){
					   
				 %>
				 <%=str %>
				 
				 <input type="button" onclick="changes('<%=u.getId()%>',1)"  value="激活"/>
				   <input type="button" onclick="changes('<%=u.getId()%>',2)"  value="删除"/>
				 <%
			
				   }else {
					   
				 %> 
				 <%=str %>
				 <input type="button" onclick="changes('<%=u.getId()%>',0)"  value="关闭"/>
				  <input type="button" onclick="changes('<%=u.getId()%>',2)"  value="删除"/>
				 <%
			   }
		   }else {
			  %> 
			   <%=str %>
			  <%
		   }
		 %>
		 
		 </td>       
		<!--  
		 <td align="left">
			<a href="huiyuanUpdate.jsp?id=<%=u.getId() %>" >[修改]</a>
		</td>
		-->  
    </tr>
    <% }
   
      }%>
</tbody>
</table>
 
<div class="btn">
 
</div>
<div id="pages"></div>
</div>  
     
     
     </div>


</div>



</body>
</html>
