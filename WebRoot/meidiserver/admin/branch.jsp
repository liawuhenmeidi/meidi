<%@ page language="java" import="java.util.*,category.*,branchtype.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
     
List<BranchType> list =BranchTypeManager.getLocate() ;
boolean flag = UserManager.checkPermissions(user, Group.branch,"w");  
//System.out.println("list.size()"+list.size());
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>门店管理</title> 
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
  
<script type="text/javascript">

function winconfirm(){
	question = confirm("你确认要删除吗？");
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
		$.ajax({  
	        type: "post",  
	         url: "delete.jsp",
	         //data:"method=list_pic&page="+pageCount,
	         data:"method=branchtype&id="+attract.toString(),
	         dataType: "", 
	         success: function (data) {
	          if(data == false || data == "false"){
	        	  alert("删除失败");
	          }else {
	        	  alert("删除成功");
	          } 
	          window.location.href="branch.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("删除失败");
	            } 
	           });
	 }
}


function branch(id){ 
	 window.location.href="branch1.jsp?id="+id;
}


function update(bid,statues){  
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=branchtypeinventory&bid="+bid+"&statues="+statues,
         dataType: "", 
         success: function (data) {
           window.location.href="branch.jsp"; 
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function changes(){
	var str1 = $("#locate").val();
	if(str1 == null || str1 == ""){
		alert("不能为空");
		return ; 
	}  
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=branchtype&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="branch.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
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
   
 <!--       -->    
 
   <div class="weizhi_head">现在位置：门店系统管理</div>     
   <div class="main_r_tianjia">
    <ul>                                                                                                   
     <li><a href="branchupdate.jsp">添加门店系统</a></li>
     </ul>
   
   </div>
     
   <div class="table-list">
<table width="100%" cellspacing="1" id="table">
	<thead>
		<tr >
			<th align="left" width="20">
			 <input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th>
			<th align="left">销售系统编号</th>
			<th align="left">销售系统</th>
			<th align="left">修改</th>  
			<th align="left">系统内门店</th>
			<th align="left">导购是否可见</th>   
		</tr>
	</thead>
<tbody>

<% 
if(list != null){
  for(int i =0 ;i<list.size();i++){
	  BranchType category = list.get(i) ;
%>   
    <tr id="<%=i%>" class="asc"  onclick="updateClass(this)" ondblclick="branch('<%= category.getId()%>')"> 
        
	    <td align="left">
	    	<% if(category.getId() != 1 && category.getId() != 2){%>
	    <input type="checkbox" value="1" name="<%=category.getId() %>"></input>
	    <% }%>
	    </td>  
		<td align="left"><%=category.getId() %></td>
		<td align="left"><%=category.getName() %></td> 
		
		<td align="left"> 
		<% 
		  if(flag){
		  if(category.getIsSystem() == 0 ){%>
		<a href="branchupdate.jsp?id=<%=category.getId() %>">[修改]</a>
		<% }
		}%>
		</td>
		<td align="left">
			<a href="branch1.jsp?id=<%=category.getId() %>">[查看]</a>
		</td>
		<td align="left">
		<% 
		
		String str = category.getStatues() ==0?"否":"是";
		
		if(flag){
			  if(category.getStatues() ==0){
	             %>
	             <%=str %>
	             <input type="button" onclick="update('<%=category.getId()%>','1')"  value="开启"/>                      
	                                            
	            <%
	            }else {   
	            %>
	             <%=str %>
	              <input type="button" onclick="update('<%=category.getId()%>','0')"  value="关闭"/> 
	            <%}
            }else { 
            %>  
             <%=str %>
            <% }%>
            
        </td>
    </tr>   
    <% } 
    }%>
</tbody>
</table>

<div class="btn">

<!--  <input type="button" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  -->

</div>

</div>



</body>
</html>
