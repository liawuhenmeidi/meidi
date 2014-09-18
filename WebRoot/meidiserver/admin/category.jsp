<%@ page language="java" import="java.util.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

List<Category> list =CategoryManager.getCategory(user,Category.sale) ;
    
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
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
			alert(attract.toString());
		  //window.open("http://www.codefans.net")
			$.ajax({ 
		        type: "post", 
		         url: "delete.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=category&id="+attract.toString(),
		         dataType: "", 
		         success: function (data) {
		          alert("删除成功");
		          window.location.href="category.jsp";
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
		}

	}
	 
	function changes(id,statues){
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=duanhuo&id="+id+"&statues="+statues,
	         dataType: "", 
	         success: function (data) {
	           window.location.href="category.jsp";
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

<div class="main">
 
     
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">现在位置：产品类别管理</div>     
   <div class="main_r_tianjia">
   
   <ul>                                                                                                 
     <li><a href="categoryAdd.jsp">添加产品类别</a></li>
     </ul>
   
   </div>
     
   <div class="table-list">
<table width="100%" cellspacing="0">
	<thead>
		<tr>
			<!--<th align="left" width="20">
			 <input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th> -->
			<th align="left"></th>
			<th align="left">产品列别描述</th>
			<th align="left">预约安装截止日期</th>
			<th align="left">是否已断货</th> 
			<th align="left">修改</th> 
			<th align="left">查看产品型号</th>
		</tr>
	</thead>
<tbody>
<% 
  for(int i =0 ;i<list.size();i++){
	  Category category = list.get(i) ;
%>
    <tr>
		<!--  <td align="left"><input type="checkbox" value="1" name="<%=category.getId() %>"></input></td> -->
		<td align="left"></td>
		<td align="left"><%=category.getName() %></td> 
		<td align="left"><%=category.getTime() %></td>
		<td align="left">
		<%
		   if(0 == category.getStatues()){
		 %>
		 否
		 <input type="button" onclick="changes('<%=category.getId()%>',1)"  value="删除"/>
		 <%
		   }else {
		 %>
		 是
		 <input type="button" onclick="changes('<%=category.getId()%>',0)"  value="激活"/>
		 <%
	   }
		 %>
		 </td>
		<td align="left"><a href="categoryUpdate.jsp?id=<%=category.getId() %>">[修改]</a></td>
		<td align="left">
			<a href="product.jsp?categoryID=<%=category.getId() %>">[查看]</a>
		</td>
    </tr>
    <% } %>
</tbody>
</table>

<div class="btn">

<!--  <input type="button" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  -->

</div>
<div id="pages"></div>
</div>  

     </div>

</div>



</body>
</html>
