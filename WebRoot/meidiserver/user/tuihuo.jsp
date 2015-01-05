<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="searchdynamic.jsp"%> 
<%   
if(type.equals(Order.serach+"")){
	type = Order.unquery+"";
}

List<Order> list = OrderManager.getOrderlist(user,Group.tuihuo,Integer.valueOf(type),-1,0,sort,sear);
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM();
//System.out.println(user);
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>欢迎使用微网站办公系统</title>

<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 


<link rel="stylesheet" href="../css/songhuo.css">
 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var type = "<%=type%>";

$(function () { 
	$("#"+type).css("color","red");
});

function detail(src){
	window.location.href=src;
}
 
function search(type){
	window.location.href="tuihuo.jsp?type="+type;
}
</script>
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--  头 单种类  -->
<div class="s_main_tit"><span class="qiangdan"><a href="chaxun_songhuo.jsp">我要查询</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
 <div class="s_main_tit"><span style="cursor:hand" id="<%=Order.unquery%>" onclick="search('<%=Order.unquery%>')">待处理</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span style="cursor:hand" id="<%=Order.query%>" onclick="search('<%=Order.query%>')">已处理</span>&nbsp;&nbsp;</div> 
<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
  <tr > 
    <td width="20%" class="s_list_m">产品名称</td>
    <td width="30%" class="s_list_m">产品型号</td>
    <td width="20%" class="s_list_m">安装日期</td>
    <td width="30%" class="s_list_m">送货地点</td>
  </tr>
   <% 
   if(list != null){
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
 <tr <%=col %> onclick="detail('dingdanDetailsonghuo.jsp?id=<%=o.getId()%>')">  
 
     <td align="left"> 
  <table> 
  
  
    <%    
    
    List<OrderProduct> lists = OrPMap.get(o.getId());
   
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
   
     <%}
    }%>
</table>
<br/>



<br/>
</div>

</div>

</body>
</html>