 <%@ page language="java" import="java.util.*,category.*,group.*,user.*,product.*,utill.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String categoryID = request.getParameter("categoryID");
 
boolean flag = UserManager.checkPermissions(user, Group.addprodoct,"w");

List<Product> list = new ArrayList<Product>();
Category category = null; 
if(!StringUtill.isNull(categoryID)){
	HashMap<Integer,Category> mapCat = CategoryManager.getCategoryMap();
	category = mapCat.get(Integer.valueOf(categoryID));  
	list =ProductManager.getProduct(categoryID,Product.sale);
}


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
</head>

<body>
<!--   头部开始   -->
   
<!--   头部结束   -->


<script type="text/javascript">
    var categoryID = "<%=categoryID%>";
	function winconfirm(){
		question = confirm("你确认要删除吗？");
		if (question != "0"){
            var value = "";
			$('input[name="product"]:checked').each(function(){
				 value = $(this).val();
			  });
			//alert(value);
		  //window.open("http://www.codefans.net")
			$.ajax({ 
		        type: "post", 
		         url: "delete.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=product&id="+value,
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
	
	function update(src){
		window.location.href=src;
	}
</script>
<div class="main">
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
 <!--       -->    
   <div class="weizhi_head">现在位置,类别：<%= category.getName()%></div>     
   <div class="main_r_tianjia"> 
     <form action="../ExcelUpload" method="post" enctype ="multipart/form-data" runat="server">
     <input type="hidden" name="uploadType" value="5"/> 
     <input type="hidden" name="categoryID" value="<%=categoryID%>"/> 
     
     <table width="100%">  
     <tr> 
      <td align="center" width="20%">
      <% if(flag){ %>                                                                                                
      <a href="productAdd.jsp?categoryID=<%=categoryID%>&method=add&ptype=<%=category.getPtype()%>"><font style="color:blue;font-size:20px;" >添加产品</font></a>
      <% }%>
      </td>    
      <td> <a href="../DownloadServlet?name=productmuban&type=model"><font style="color:blue;font-size:20px;" >模板</font> </a></td> 
        
      <td align="center" > <font style="color:red;font-size:20px;" >导入数据 : </font></td>
      <td align="center" ><input id="File1"   name="UpLoadFile" type="file" /> </td>
      <td align="center" >
       <input type="submit" name="Button1" value="提交文件" id="Button1" />
      </td> 
      <td align="center" ><a href="category.jsp"><font style="color:blue;font-size:20px;" >返回</font></a>
      </td>
     </tr> 
     
     </table> 
  </form>

   </div> 
   <div class="table-list">
<table width="100%"  cellspacing="1" id="table">
	<thead>
		<tr>
			<th align="left" width="20"></th>
			<!--<input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th>
			 
			<th align="left">产品类别ID</th>  -->
			<th align="left">产品序号</th>
			<th align="left">产品型号</th>
			<th align="left">产品编码</th>
			<th align="left">销售类别</th>
			<th align="left">体积</th> 
			<th align="left">售价(单位元)</th>
			<th align="left">时间(单位天)</th>
			<th align="left">修改</th>
		</tr>
	</thead>
<tbody>
<% 
  for(int i =0 ;i<list.size();i++){
	  Product product = list.get(i) ;
	  
%>
    <tr id="<%=i%>" class="asc"  onclick="updateClass(this)" ondblclick="update('productAdd.jsp?productid=<%=product.getId() %>&method=update&categoryID=<%=categoryID%>&ptype=<%=category.getPtype()%>')">
        
		<td align="left"><input type="radio" name="product" value="<%=product.getId() %>"></input></td>
		
	<!-- 	<td align="left"><%=product.getId() %></td> -->
		<td align="left"><%=i+1 %></td>   
		<td align="left"><%=product.getType() %></td>  
		<td align="left"><%=product.getEncoded() %></td>
		<td align="left"><%=product.getSaletypeName() %></td> 
		<td align="left"><%=product.getSize() %></td>  
		<td align="left"><%=product.getStockprice()%></td> 
		<td align="left"><%=product.getMataintime()%></td> 
		<td align="left">
		<%if(flag){ %>
		<a href="productAdd.jsp?productid=<%=product.getId() %>&method=update&categoryID=<%=categoryID%>&ptype=<%=category.getPtype()%>">[修改]</a>
		<%} %>
		</td> 
    </tr>
    <% } %> 
</tbody>
</table>
<% if(flag){ %>
<div class="btn">
  <input type="submit" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>   
</div>
<% }%>
<div id="pages"></div>
</div>  

</div>



</body>
</html>
