<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="searchdynamic.jsp"%> 

<%     
     
List<Order> list = OrderManager.getOrderlist(user,Group.sale,Order.serach,-1,0,sort,sear); 
Map<Integer,List<OrderProduct>> mapOP = OrderProductManager.getOrderStatuesM(user);
HashMap<Integer,User> usermap = UserManager.getMap();   // 获取送货员
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>欢迎使用微网站办公系统</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../css/songhuo.css">

<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function detail(src){
	window.location.href=src;
}
</script>
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--  头 单种类  -->
<div class="s_main_tit"><span class="qiangdan"><a href="chaxun_sale.jsp">我要查询</a></span><span class="qiangdan"><a href="order.jsp">我要报装</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
<div class="s_main_tit">订单查询页面</div>
 
<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
  <tr >
    <td width="20%" class="s_list_m">产品类别</td>
    <td width="20%" class="s_list_m">产品型号</td>
    <td width="20%" class="s_list_m">预约日期</td>
    <td width="30%" class="s_list_m">送货地点</td>
  </tr>  
   <% 
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>  
 <tr <%=col %> onclick="detail('dingdanDetail.jsp?id=<%=o.getId()%>')">  
 
     <td align="left"> 
  <table>
    <%    List<OrderProduct> lists = mapOP.get((o.getId()));
		     if(null != lists){
			     for(int g = 0 ;g<lists.size();g++){
			    	 OrderProduct op = lists.get(g);
	                 if(op.getStatues() != 1 ){
	                    
			    	 %> 
			    	 <tr>
			    	 <td ><%=categorymap.get(Integer.valueOf(op.getCategoryId())).getName()%> </td>
			    	 </tr>
			    	 <% 
			        }
			     }
		     }
		     %> 
		     </table>

  
		     </td>  
     <td align="left"> 
     <table>
    <%    
       if(null != lists){
		     for(int g = 0 ;g<lists.size();g++){
		    	 OrderProduct op = lists.get(g);
                 if(op.getStatues() != 1 ){

		    	 %> 
		    	 <tr>
		    	 <td ><%=ProductService.getIDmap().get(Integer.valueOf(op.getSendType())).getType()%></td>
		    	 </tr>
		    	 <% 
		        }
		     }
       }
		     %> 
		     </table>
		 </td>    
		     
		      
    <td><%=o.getOdate() %></td>
    
    <td><%=o.getLocateDetail()%></td>
    
  </tr>
  
     <%} %>
</table>
<br/>



<br/>
</div>

</div>

</body>
</html>