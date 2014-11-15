<%@ page language="java" import="java.util.*,category.*,order.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String path = request.getContextPath();
String realPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();    

String action = request.getParameter("action");
String ptype = request.getParameter("ptype");   
String typeStr = request.getParameter("type"); 
int type = Integer.valueOf(typeStr);

GroupService.flag = true ;
Map<String,List<Group>> map = GroupService.getPidMap();
List<Category> list =CategoryManager.getCategory(Category.sale) ;

List<Group>  listg = null ;

if("add".equals(action)){ 
	String name = request.getParameter("name");
	String detail = request.getParameter("detail");
	
	int statues = Integer.valueOf(request.getParameter("Statues"));
	String pid = request.getParameter("pid");
	String[] products = request.getParameterValues("product");
	String[] permission = request.getParameterValues("permission");
	String productes = "";
	String permiss = "";
    for(int i=0;i<products.length;i++){
    	productes += products[i] + "_";
     }
    for(int i=0;i<permission.length;i++){
    	permiss += permission[i] + "_";
     }
   
    productes = productes.substring(0, productes.length()-1);
    
    Group group = new Group();
    group.setDetail(detail);
    group.setName(name);
    group.setStatues(statues);
    group.setProducts(productes); 
    group.setPermissions(permiss);
    group.setPid(Integer.valueOf(pid)); 
    group.setPtype(Integer.valueOf(ptype)); 
    GroupManager.save(user, group);  
    response.sendRedirect("juese.jsp?ptype="+ptype+"&type="+type);
    return ;
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加角色</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">

$(function () {
	
	
	 $("#name").focus(function(){
		    $("#name").css("background-color","#FFFFCC");
		  });
		  $("#name").blur(function(){
		    $("#name").css("background-color","#D6D6FF");
		    var categoryName = $("#name").val();
		    $.ajax({ 
		        type:"post",  
		         url:"server.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=juese_add&jueseName="+categoryName,
		         dataType:"", 
		         success: function (data) {
		        	 if("true" == data){
		        		 alert("已存在相同的角色名称");
		        		 $("#name").focus();
				          return ;
		        	 }
		           }, 
		          error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	  //alert(123);
		            } 
		           });
		  
		  });
}); 


 function check(){
	 var name = $("#name").val();
	 var detail = $("#detail").val();

	 if(name == "" || name == null || name == "null"){
		 alert("角色名称不能为空");
		 return false;
	 }
	 
	 if(detail == "" || detail == null || detail == "null"){
		 alert("详细信息不能为空");
		 return false;
	 }
	 
	 $('input[name="permission"]:checked').each(function(){ 
		    //alert($(this).val()); 
	 });
	 
	 return true ;
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
 <div class="weizhi_head">现在位置：添加/修改角色</div>
 <form action="juese_add.jsp"  method = "post"  onsubmit="return check()">
 <input type="hidden" name="action" value="add"/> 
 <input type="hidden" name="ptype" value="<%=ptype%>"/> 
  <input type="hidden" name="type" value="<%=type%>"/> 
     <div class="juese_head">角色名称：
            <input type="text"  id="name" name="name" />    
        </div> 
       <div class="juese_head">  详细信息:
       <textarea name="detail" id="detail" rows="4" cols="20" > 
       </textarea> 
       </div> 
       <div class="juese_head"></div>
         <div class="juese_head"></div>
   <div class="juese_head">  
		  启用：
		<input type="radio" checked="checked" name="Statues" value="1" />
		<br />
		禁用：
		<input type="radio" name="Statues" value="0" />
</div>
<div class="juese_head"></div>
     <div class="juese_head">产品权限</div>
     
     <ul class="juese_add"> 
       <% if(list != null){
	         for(int i=0;i<list.size();i++){
	        	%>
	        <li><input type="checkbox" name = "product"  value="<%=list.get(i).getId() %>"  id="<%=list.get(i).getId() %>" />&nbsp;<%=list.get(i).getName() %></li>
	      
	       <%  	
	        }
       }
       %>
     </ul>  
     
     <div class="juese_head">功能权限</div>
     <ul class="juese_add">
       <%  
       
       
         if(type == Group.Manger && null != map){
        	 listg = map.get(1+"");
        	%>  
        	 <li><input type="checkbox" value="0" name = "permission" id="p0" onClick="return false" checked="checked" />&nbsp;系统最高权限</li>
        	<%
         }else if(type == Group.dealSend && null != map){
        	 listg = map.get(1+"");
        	 %>
	        <li><input type="checkbox" value="4" name = "permission" id="p4" />&nbsp;管理产品权限</li>
	        <li><input type="checkbox" value="3" name = "permission" id="p3" />&nbsp;管理用户权限</li>
	        <li><input type="checkbox" value="5" name = "permission" id="p5" checked="checked" onClick="return false" />&nbsp;总配单权限</li> 
	        <li><input type="checkbox" value="9" name = "permission" id="p9" />&nbsp;管理门店权限</li> 
	        <li><input type="checkbox" value="10" name = "permission" id="p10" />&nbsp;地区权限</li> 
	       <li><input type="checkbox" value="12" name = "permission" id="p12" />&nbsp;提交调货单权限</li>  
	        <li><input type="checkbox" value="13" name = "permission" id="p13" />&nbsp;确认调货单权限</li> 
        	 <%
         }else if(type == Group.sencondDealsend && null != map){ 
        	listg = map.get(2+"");
       %>
        <li><input type="checkbox" value="3" name = "permission" id="p3" checked="checked" onClick="return false" />&nbsp;管理用户权限</li>
        <li><input type="checkbox" value="8" name = "permission" id="p8"  checked="checked" onClick="return false" />&nbsp;网点派单权限</li>  
        <li><input type="checkbox" value="11" name = "permission" id="p11" checked="checked" onClick="return false"  />&nbsp;打印权限</li> 
        <li><input type="checkbox" value="13" name = "permission" id="p13" checked="checked" onClick="return false" />&nbsp;确认调货单权限</li>
      <%        	  
         }else if(type == Group.sale && null != map){
        	 listg = map.get(2+"");
        	%>
        <li><input type="checkbox" value="1" name = "permission" id="p1" checked="checked" onClick="return false" />&nbsp;提交报装单</li>
        <li><input type="checkbox" value="13" name = "permission" id="p13" checked="checked" onClick="return false" />&nbsp;确认调货单权限</li>
        <li><input type="checkbox" value="7" name = "permission" id="p7"  checked="checked" onClick="return false" />&nbsp;查询门店权限</li>
        <%
         }else if(type == Group.send && null != map) {
        	 listg = map.get(3+"");
        	 %>
       
        <li><input type="checkbox" value="2" name = "permission" id="p2" checked="checked" onClick="return false"  />&nbsp;送货权限</li>
      <%
         }else { 
             listg = GroupService.getList();
         
         %>
       
         <li><input type="checkbox" value="1" name = "permission" id="p1" />&nbsp;提交报装单</li>
        <li><input type="checkbox" value="4" name = "permission" id="p4" />&nbsp;管理产品权限</li>
        <li><input type="checkbox" value="3" name = "permission" id="p3" />&nbsp;管理用户权限</li>
        <li><input type="checkbox" value="2" name = "permission" id="p2" />&nbsp;送货权限</li>
        <li><input type="checkbox" value="5" name = "permission" id="p5" />&nbsp;总配单权限</li> 
        <li><input type="checkbox" value="6" name = "permission" id="p6" />&nbsp;管理职位权限</li>
        <li><input type="checkbox" value="7" name = "permission" id="p7" />&nbsp;查询门店权限</li>
        <li><input type="checkbox" value="8" name = "permission" id="p8" />&nbsp;网点派单权限</li> 
        <li><input type="checkbox" value="9" name = "permission" id="p9" />&nbsp;管理门店权限</li> 
        <li><input type="checkbox" value="10" name = "permission" id="p10" />&nbsp;地区权限</li> 
        <li><input type="checkbox" value="11" name = "permission" id="p11" />&nbsp;打印权限</li> 
       <li><input type="checkbox" value="12" name = "permission" id="p12" />&nbsp;提交调货单权限</li>  
        <li><input type="checkbox" value="13" name = "permission" id="p13" />&nbsp;确认调货单权限</li>	 	 
 
         
     <% }%>
     </ul>  
    
     <div class="juese_head">添加上级管理组
     </div>
     
      <ul class="juese_add">
       <%
        if(listg != null){
	        for(int i=0;i<listg.size();i++){
	        	String str = "";
	        	if(listg.size() == 1){
	        		str = "checked=\"checked\"";
	        	}
	        	%>
	        <li><input type="radio"  name = "pid"  value="<%=listg.get(i).getId() %>"  id="<%=listg.get(i).getId() %>" <%=str %>/>&nbsp;<%=listg.get(i).getName() %></li>  	
	       <%  	
	        }
        }
       %>
     </ul>  
    
    
    
    
     <ul class="juese_add" >
     <li><input type="submit" value="提  交" /></li>
     </ul>
 </form>  
   </div>
     </div>


</body>
</html>
