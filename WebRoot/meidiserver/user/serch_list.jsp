<%@ page language="java" import="java.util.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%  
 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
  
boolean flag = false ;
String saledateStart = request.getParameter("saledateStart");
String saledateEnd = request.getParameter("saledateEnd");
String sailId = request.getParameter("sailId");
String pos = request.getParameter("pos");
String username = request.getParameter("username");
String phone1 = request.getParameter("phone1");
String printlnid = request.getParameter("printlnid"); 
String str = "";   
// pos == "" || pos == null || pos == "null"
if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
	 str += " and saledate BETWEEN '" + saledateStart + "'  and  ";
    flag = true ;
}   
  
if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
	 str += " ' " + saledateEnd + "'";
}else if(flag){
	 str += "now()";
}
  
if(sailId != null && sailId  != "" && sailId  != "null"){
	 str += " and sailId like '%"+sailId+"%'";
}
if(pos != null && pos != "" && pos != "null"){
	 str += " and pos like '%"+pos+"%'";
}  
if(username != null && username != "" && username != "null"){
	 str += " and username like '%"+username+"%'";
}     
if(phone1 != null && phone1 != "" && phone1 != "null"){
	 str += " and phone1 like '%"+phone1+"%'";
};
if(!StringUtill.isNull(printlnid)){
	str += "and printlnid like '%" + printlnid +"%'";
}
List<Order> list = OrderManager.getOrderlist(user,Group.sale,str,"id"); 

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
		    	 <td ><%=op.getSendType()%></td>
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