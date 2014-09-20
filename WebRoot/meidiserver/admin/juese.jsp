<%@ page language="java" import="java.util.*,user.*,group.*,category.*;" pageEncoding="utf-8"%>

<%

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String ptype = request.getParameter("ptype"); 
List<Group> list = GroupManager.getGroup(user,Integer.valueOf(ptype));

List<Group> listg = GroupManager.getGroupdown(user, Integer.valueOf(ptype),1);
%>   

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色管理页面</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
</head>
<body>



<script type="text/javascript">

	function winconfirm(){
		var question = confirm("你确认要删除吗？");	
		if (question != "0"){
			var attract = new Array();
			var i = 0;
			 
			$("input[type='checkbox']").each(function(){          
		   		if($(this).attr("checked")){
		   				var str = this.name;
		   	            attract[i] = str;
		   	            i++;	
		   		}
		   	}); 
		//alert(attract.toString())	;
			$.ajax({ 
		        type: "post", 
		         url: "delete.jsp",
		         data:"method=juese&id="+attract.toString(),
		         dataType: "", 
		         success: function (data) {
		        	 if(data == -1){
		        		 alert("职位内有职员，请先删除职员") ;
		        		 return ;
		        	 }else if (data > 0){
		        		  alert("删除成功"); 
		        		  window.location.href="juese.jsp?ptype=<%=ptype%>";
		        	 };	  
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	 alert("删除失败"); 
		            } 
		           });

		}
	}
	
	function seletall(all){
		if($(all).attr("checked")){
			$("input[type='checkbox']").each(function(){
				$(this).attr("checked",true);
	
		     });
		}else if(!$(all).attr("checked")){
			$("input[type='checkbox']").each(function(){
				$(this).attr("checked",false);
		     });
		};
	}  
	
</script>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main">

    <div class="weizhi_head">现在位置:职位管理</div>     
   <div class="main_r_tianjia">  
   <ul>  
     <li><a href="juese_add.jsp?ptype=<%=ptype%>">添加职位</a></li>
     </ul>
       
   </div>
      
   <div class="table-list">
<table width="100%" cellspacing="1" id="table">
	<thead>
		<tr>  
			<th align="left" width="20"></th>
		<!-- <input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> -->	
			<th align="left"></th>
			<th align="left">角色名称</th>
			<th align="left">角色描述</th>
			<th align="left">状态</th>
			<th align="left">管理操作</th>  
			
		</tr>
	</thead>
<tbody>
   <% for(int i=0;i<list.size();i++){
	   Group g = list.get(i);
    %> 
    <tr id="<%=i%>" class="asc"  onclick="updateClass(this)">
		<th align="left" width="20"><input type="checkbox" value="" id="check_box" name = "<%=g.getId() %>"></input></th>
	   <th align="left"></th>
		<td align="left"><%=g.getName() %></td>
		<td align="left"><%=g.getDetail() %></td>
		<%
		if(g.getStatues() == 1){
	
		%>
		<td align="left" class="red">√</td>
		<%
		}else {
			%>
		<td align="left" class="red">X</td>
			<% 	
		}
		%>
		<td align="left">
        <a href="authority.jsp?id=<%=g.getId()%>&ptype=<%=ptype%>">权限设置</a> | <a href="huiyuanJ.jsp?id=<%=g.getId()%>&ptype=<%=ptype%>">成员管理</a> | <a href="jueseUpdate.jsp?id=<%=g.getId()%>&ptype=<%=ptype%>">修改</a> 
        
        </td>	
        
    </tr>
    <% }
    %>
    
       <% if(listg != null ){
     for(int i=0;i<listg.size();i++){
	   Group g = listg.get(i);
    %>
    <tr>
		<th align="left" width="20"><input type="checkbox" value="" id="check_box" name = "<%=g.getId() %>"></input></th>
			<th align="left"></th>
		<td align="left"><%=g.getName() %></td>
		<td align="left"><%=g.getDetail() %></td>
		<%
		if(g.getStatues() == 1){
	
		%>
		<td align="left" class="red">√</td>
		<%
		
		}else {
			%>
			
		<td align="left" class="red">X</td>
			<% 	
		}
		%>
		<td align="left">
        <a href="authority.jsp?id=<%=g.getId()%>" target="contentpage">权限设置</a> | <a href="huiyuanJ.jsp?id=<%=g.getId()%>&ptype=<%=g.getId()%>" target="contentpage">成员管理</a> | <a href="jueseUpdate.jsp?id=<%=g.getId()%>" target="contentpage">修改</a> 
        
        </td>	
        <td>
        </td>
    </tr>
    <% } 
       } 
    %>
</tbody>
</table>

<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  
 
</div>
     </div>
</div>
</body>
</html>
