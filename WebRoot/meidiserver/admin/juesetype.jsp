<%@ page language="java" import="java.util.*,user.*,grouptype.*,category.*,group.*;" pageEncoding="utf-8"%>

<% 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
List<Grouptype> list = GrouptypeManager.getGroup(user);

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

	function winconfirm(id){
		//alert(id);
		var question = confirm("你确认要删除吗？");	
		if (question != "0"){
			// return false ;
			$.ajax({ 
		        type: "post", 
		         url: "delete.jsp",
		         data:"method=juesetype&id="+id,
		         dataType: "",  
		         success: function (data) {
		        	 if(data == -1){
		        		 alert("角色列别里有角色名称。请先删除角色名称") ;
		        		 return ;
		        	 }else if (data > 0){
		        		  alert("删除成功");
		        		  window.location.href="juesetype.jsp";
		        	 };	  
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	 alert("删除失败"); 
		            } 
		           });
			
			
		}
	}
	
	
	
	function add(){
		var str1 = $("#locate").val();
		if(str1 == null || str1 == ""){
			alert("不能为空");
		}
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=jusetype&id="+str1,
	         dataType: "", 
	         success: function (data) {
	           window.location.href="juesetype.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           }); 
	   }   
	
</script> 
<!--   头部开始   -->

 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
 
    <div class="weizhi_head">现在位置:职位类别管理</div>     
   <div class="main_r_tianjia">  
   

     
   <div class="table-list">
<table width="100%" cellspacing="1" id="table">
	<thead>
		<tr>  
			
		<!-- <input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> -->	
			<th align="left">编号</th>
			<th align="left">角色类别</th>
			<th align="left">成员管理</th>
			<th align="left">修改</th>
			<th align="left">删除</th>
		</tr>
	</thead>
<tbody>

   <% if(list != null){
      for(int i=0;i<list.size();i++){
	   Grouptype g = list.get(i); 

    %>  
    <tr id="<%=i%>" class="asc"  onclick="updateClass(this)"> 
	      <td align="left"><%=i+1 %></td>
		  <td align="left"><%=g.getName() %></td> 
		   <td align="left"><a href="juese.jsp?ptype=<%=g.getId()%>&type=<%=g.getType()%>&statues=<%=g.getStatues()%>">成员管理</a></td>
		<% if(g.getStatues() == 1) {%>
		   <td align="left"></td>
		  	<td align="left"></td>
        <% }else {
         %>
       
          
		   <td align="left"><a href="grouptypeupdate.jsp?id=<%=g.getId()%>">[修改]</a></td>
		   <td align="left">  
           <a href="javascript:void(0);" onclick="winconfirm('<%=g.getId()%>')">[删除]</a>   
           </td> 
        <% }%>	
        
    </tr>  
    <% } 
   }
    %>
    
</tbody>
</table>
 
<div class="btn"> 
职位类别名称： <input type="text"  id="locate" name="locate" /> 
      <input type="button" onclick="add()"  value="增加"/> 
</div> 
     </div>
</div> 
</body>
</html>
