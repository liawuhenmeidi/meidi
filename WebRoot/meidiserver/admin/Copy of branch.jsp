<%@ page language="java" import="java.util.*,category.*,branch.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
List<Branch> list = BranchManager.getLocate(Integer.valueOf(user.getId()));
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>区域管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
$(function () {
}); 

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
		//alert(attract.toString());
	  //window.open("http://www.codefans.net")
		$.ajax({  
	        type: "post",  
	         url: "delete.jsp",
	         //data:"method=list_pic&page="+pageCount,
	         data:"method=branch&id="+attract.toString(),
	         dataType: "", 
	         success: function (data) {
	          alert("删除成功");
	          window.location.href="branch.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("删除失败");
	            } 
	           });
	 }
}

function changes(){
	var str1 = $("#locate").val();
	if(str1 == null || str1 == ""){
		alert("不能为空");
	}
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=branch&id="+str1,
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
<div class="head">
  <div class="head_logo"><img src="../style/image/logo.png" /></div>
</div>

<!--   头部结束   -->


<div class="main">
    
     
 <!--       -->    
     
   <div class="">
   <div class="weizhi_head">现在位置：门店管理</div>     
     
   <div class="table-list">
<table width="100%" cellspacing="0">
	<thead>
		<tr>
			<th align="left"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th>
			<th align="left"></th>
			<th align="left">门店ID</th>
			<th align="left">区域名称</th>
		</tr>
	</thead>
<tbody>
<% 
  for(int i =0 ;i<list.size();i++){
	 Branch u = list.get(i) ;
%>
    <tr>
     
		<th align="left" width="20"><input type="checkbox" value="" id="check_box" name ="<%=u.getId() %>"></input></th>
		<td align="left"></td>
		<td align="left"><%=u.getId() %></td>
		<td align="left"><%=u.getLocateName() %></td>
    </tr>
    <% } %>
</tbody>
</table>
<div class="btn">  
     
      区域名称： <input type="text"  id="locate" name="locate" />  
  <input type="button" onclick="changes()"  value="增加"/> </br>   
  <input type="submit" class="button" name="dosubmit" value="删除"  onclick="winconfirm()"></input>

</div>

<div id="pages"></div>
</div>  
     
     
     </div>


</div>



</body>
</html>
