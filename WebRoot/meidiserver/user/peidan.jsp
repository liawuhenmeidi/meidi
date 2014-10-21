<%@ page language="java" import="java.util.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId();

List<Order> list = null ; 
boolean flag = false ;

String saledateStart = request.getParameter("saledateStart");
String  saledateEnd = request.getParameter("saledateEnd");
String  deliveryStatues  = request.getParameter("deliveryStatues");
// alert(deliveryStatues); 
// 已分配还是未分配
String  sendId = request.getParameter("sendId");
String  str = "";
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

if(deliveryStatues != null && deliveryStatues != "" && deliveryStatues != "null"){
	 if(deliveryStatues.equals(0+"")){ 
		 str += " and sendId = 0";
	 }else {
		 str += " and sendId != 0";   
	 }
}

if(sendId != null && sendId != "" && sendId != "null"){
	 str += " and sendId = '"+sendId+"'";
} 

System.out.println("str"+str);  
list = OrderManager.getOrderlist(user,Group.sencondDealsend,str,"id");   

   
HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员    
List<User> listS = UserManager.getUsers(user,Group.send);
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);   
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

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

</script>
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
 
<!--  头 单种类  --> 
<div class="s_main_tit"><span class="qiangdan"><a href="chaxun_peidan.jsp">我要查询</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
<div class="s_main_tit">订单查询页面</div>

<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
  <tr>
    <td width="15%" class="s_list_m">产品名称</td>
    <td width="15%" class="s_list_m">产品型号</td>
    <td width="25%" class="s_list_m">安装日期</td>
    <td width="25%" class="s_list_m">送货地点</td>
    <td width="15%" class="s_list_m">详情</td>
  </tr>
   <% 
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
 <tr <%=col %>>  
 
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
    <td> <a href="dingdanDetailpeidan.jsp?id=<%=o.getId()%>">[详情]</a></td>
  </tr>
  
     <%} %>
</table>
<br/>



<br/>
</div>

</div>

</body>
</html>