<%@ page language="java" import="java.util.*,category.*,inventory.*,user.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");
boolean flag = true ;   
User user = (User)session.getAttribute("user");
List<Inventory> invetorylist = InventoryManager.getCategory(user,"unconfirmed");  
String path = request.getContextPath();
String realPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);    
%>   
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="viewport" content="initial-scale=1, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/>
  <title>欢迎使用微网站办公系统</title>

 
<link rel="stylesheet" href="../css/songhuo.css">
  
<script type = "text/javascript" language = "javascript">
function clear(){
	if (confirm("确定要退出吗？")) { 
		location.href = "server.jsp?method=quit";
	}
  } 
  </script>
  </head>
<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
 
<!--  头 单种类  -->
<div class="s_main_tit">登陆选项<span class="qiangdan"></span></div>
 
  <div class="s_main_box">
<ul class="door"> 
   <%  
     if(UserManager.checkPermissions(user, Group.sale,"w")){ 	 
   %>          
   <li><a href="order.jsp">报装 </a></li>  
    <%          
     }  
   if(UserManager.checkPermissions(user, Group.ordergoods,"w")){ 	 
	   %>          
	   <li><a href="OrderGoods/ordergoods.jsp">订货 </a></li> 
	   <li><a href="OrderGoods/ordergoodsall.jsp">未审核订单 </a></li> 
	   <!--   
	    <li><a href="OrderGoods/inventoryDynamic.jsp">智能要货 </a></li> 
	    -->
	    
	    <%            
	     }  
   if(UserManager.checkPermissions(user, Group.ordergoods,"r")){ 	 
	   %>             
	   <!--       
	   <li><a href="inventory/inventorycome.jsp">未入库 </a></li> 
	    -->   
	   <li><a href="OrderGoods/SNOrder.jsp">全部订单号 </a></li> 
	   <li><a href="makeInventory/makeinventoryall.jsp">盘点</a></li>
	  <!--   
	    <li><a href="inventory/inventorycheck.jsp">盘点</a></li>
	    --> 
	    <%           
	     }  
   if(UserManager.checkPermissions(user, Group.sale,"r")){
      %>    
    	     <li><a href="serch_list.jsp">查看报装单</a></li>
    <%	  
    } 
   if(UserManager.checkPermissions(user,Group.inventory)){
	    %>      
 <li><a href="inventory/receipts.jsp">调拨单</a><FONT color=#000000 >&nbsp;<%=invetorylist.size() %></FONT></li>
	<%	  
	}
   if(UserManager.checkPermissions(user,Group.inventoryquery)){
	%>     
   <li><a href="inventory/inventory.jsp">库存查询</a></li>
	    <%	   
	} 
   if(UserManager.checkPermissions(user,Group.send)){ 	 
   %>
   <li><a href="../admin/sendExport.jsp">送货结款</a></li>
   <li><a href="<%=realPath %>songhuo.jsp">送货单</a></li>
    <li><a href="<%=realPath %>maintain.jsp">维修保养单</a> </li> 
   <li><a href="<%=realPath %>tuihuo.jsp">退货单</a></li> 
   <%
     }
   if(UserManager.checkPermissions(user,Group.sencondDealsend)){ 	 
   %>
   <li><a href="inventory/inventory.jsp">库存查询</a></li>
   <% 
     }   
   
    
   boolean logistics_d = UserManager.checkPermissions(user, Group.logistics,"d");
   if(logistics_d){ 
	   %> 
	 <li><a href="logistics/logistic.jsp">待处理物流信息</a> </li> 
	  <li><a href="logistics/maptest.jsp">地图测试</a> </li>  
	   <% 
   }  
   %> 
    
   
 
    <li> <a href="javascript:clear()">退出登陆</a></li>
   </ul>
  </div>   
</div> 
</body>

</html>