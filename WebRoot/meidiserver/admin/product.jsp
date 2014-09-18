<%@ page language="java" import="java.util.*,category.*,group.*,user.*,product.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
</head>

<body>
<!--   头部开始   -->
   
<!--   头部结束   -->
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String categoryID = request.getParameter("categoryID");
String categoryName = request.getParameter("categoryName");

System.out.println(categoryID);
List<Product> list = new ArrayList<Product>();
Category category = null; 
if(!StringUtill.isNull(categoryID)){
	HashMap<Integer,Category> mapCat = CategoryManager.getCategoryMap();
	category = mapCat.get(Integer.valueOf(categoryID));  
	list =ProductManager.getProduct(categoryID,Product.sale);
}


%>

<script type="text/javascript">
    var categoryID = "<%=categoryID%>";
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
		         data:"method=product&id="+attract.toString(),
		         dataType: "", 
		         success: function (data) {
		          alert("删除成功");
		          window.location.href="product.jsp?categoryID="+categoryID;
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
</script>
<div class="main">
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">现在位置,类别：<%= category.getName()%></div>     
   <div class="main_r_tianjia">
   
   <ul>                                                                                                 
     <li><a href="productAdd.jsp?categoryID=<%=categoryID%>">添加产品</a></li>
      <li><a href="category.jsp">返回</a></li>
     </ul>
   </div> 
   <div class="table-list">
<table width="100%" cellspacing="0">
	<thead>
		<tr>
			<th align="left" width="20">
			<input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th>
			<th align="left"></th>
		<!--	<th align="left">产品类别ID</th>  -->
			<th align="left">产品序号</th>
			<th align="left">产品型号</th>
		</tr>
	</thead>
<tbody>
<% 
  for(int i =0 ;i<list.size();i++){
	  Product product = list.get(i) ;
	  
%>
    <tr>
    
		<td align="left"><input type="checkbox" value="1" name="<%=product.getId() %>"></input></td>
		<td align="left"></td>
	<!-- 	<td align="left"><%=product.getId() %></td> -->
		<td align="left"><%=i+1 %></td> 
		<td align="left"><%=product.getType() %></td> 
    </tr>
    <% } %>
</tbody>
</table>
<div class="btn">
  <input type="submit" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>   
</div>
<div id="pages"></div>
</div>  
     
  
     </div>


</div>



</body>
</html>
