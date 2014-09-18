<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>权限管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
</head>

<body>

<% 

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String action = request.getParameter("action");
String ptype = request.getParameter("ptype"); 
List<Category> list =CategoryManager.getCategory(Category.sale) ;
HashMap<Integer,Group> map =GroupManager.getGroupMap() ;
Group groups = null;
String id = request.getParameter("id");
System.out.println(id);
String statuesInit =  "";
String productsInit = "";
String permissions = "";

if(!StringUtill.isNull(id)){
	groups = map.get(Integer.valueOf(id));
	statuesInit = groups.getStatues()+"";
	productsInit = groups.getProducts();
	permissions = groups.getPermissions();
}

System.out.println(action);

if("permission".equals(action)){

	int statues = Integer.valueOf(request.getParameter("Statues"));
	String[] products = request.getParameterValues("product");
	String[] permission = request.getParameterValues("permission");
	String productes = "";
	String permiss = "";
	if(products != null){
		for(int i=0;i<products.length;i++){
	    	productes += products[i] + "_";
	     }
	}
    if(permission != null ){
    	for(int i=0;i<permission.length;i++){
        	permiss += permission[i] + "_";
         }
    }
    if(productes != "" && productes != null){
    	productes = productes.substring(0, productes.length()-1);
    }

    String ids = request.getParameter("id");
    Group group = new Group();
    group.setId(Integer.valueOf(ids));
    System.out.println(id);
    group.setStatues(statues);
    group.setProducts(productes);
    group.setPermissions(permiss);
  
    GroupManager.updatePermisson(user, group);
    response.sendRedirect("juese.jsp?ptype="+ptype);
}

%>

<script type="text/javascript">
 var statuesInit = "<%=statuesInit%> ";
 var productsInit = "<%=productsInit%>";
 var permissions = "<%=permissions%>";
 var products = new Array();
 var permission = new Array();
 $(function(){ 
	 if(statuesInit == 0){ 
		 $("#statues").attr("checked","checked");
	 }
	 products = productsInit.split("_");
	 permission =  permissions.split("_");
	 for(var i=0;i<products.length;i++){
		 $("#"+products[i]).attr("checked","checked");
	 }
	 for(var i=0;i<permission.length;i++){
		 $("#p"+permission[i]).attr("checked","checked");
	 }
	 
 });
 
 function check(){
	 var name = $("#name").val();
	 var detail = $("#detail").val();
	 $('input[name="permission"]:checked').each(function(){ 
		  //  alert($(this).val()); 	    
	 });
	 
	 return true ;
 }
 
</script>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main">
 
     
 <!--       -->    
     
<div class="">
 <div class="weizhi_head">现在位置：<%=groups.getName() %></div>
 <form action="authority.jsp"  method = "post"  onsubmit="return check()">
  
   <input type="hidden" name="action" value="permission"/>
   <input type="hidden" name="id" value="<%=id%>"/> 
   <input type="hidden" name="ptype" value="<%=ptype%>"/>  
   <div class="juese_head">   
		   启用：
		<input type="radio" checked="checked"  name="Statues" value="1" />
		<br />
		   禁用：
		<input type="radio" name="Statues" id = "statues" value="0" />
		<br />
    </div>
    <div class="juese_head">
    </div>
     <div class="juese_head">产品权限</div>
     
     <ul class="juese_add">
       <%
        for(int i=0;i<list.size();i++){
        	%>
        
        <li><input type="checkbox" name = "product"  value="<%=list.get(i).getId() %>"  id="<%=list.get(i).getId() %>" />&nbsp;<%=list.get(i).getName() %></li>
        	
       <%  	
        }
       %>
     </ul>  
     
     <div class="juese_head">功能权限</div>
     <ul class="juese_add">
     <li><input type="checkbox" value="0" name = "permission" id="p0" />&nbsp;系统最高权限</li>
        <li><input type="checkbox" value="1" name = "permission" id="p1" />&nbsp;提交报装单</li>
        <li><input type="checkbox" value="4" name = "permission" id="p4" />&nbsp;管理产品权限</li>
        <li><input type="checkbox" value="3" name = "permission" id="p3" />&nbsp;管理用户权限</li>
        <li><input type="checkbox" value="2" name = "permission" id="p2" />&nbsp;送货权限</li>
        <li><input type="checkbox" value="5" name = "permission" id="p5" />&nbsp;总配单权限</li> 
        <li><input type="checkbox" value="6" name = "permission" id="p6" />&nbsp;管理职位权限</li>
        <li><input type="checkbox" value="7" name = "permission" id="p7" />&nbsp;查询门店权限</li>
        <li><input type="checkbox" value="8" name = "permission" id="p8" />&nbsp;二次配单权限</li> 
        <li><input type="checkbox" value="9" name = "permission" id="p9" />&nbsp;管理门店权限</li> 
        <li><input type="checkbox" value="10" name = "permission" id="p10" />&nbsp;地区权限</li> 
        <li><input type="checkbox" value="11" name = "permission" id="p11" />&nbsp;打印权限</li> 
        <li><input type="checkbox" value="12" name = "permission" id="p12" />&nbsp;提交调货单权限</li> 
        <li><input type="checkbox" value="13" name = "permission" id="p13" />&nbsp;确认调货单权限</li> 
   </ul> 
     <div class="main_r_tianjia" /> 
 
   <ul>
     <li><input type="submit" value="提  交" /></li>
     </ul>
 </form>  
   </div>
     </div>

</div>

</body>
</html>
