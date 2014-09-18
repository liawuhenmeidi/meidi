<%@ page language="java" import="java.util.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
System.out.println(user);
// usermap.get(o.getSaleID()).getUsername()
List<Order> list = OrderManager.getOrderlist(user,Group.sale);
HashMap<Integer,User> usermap = UserManager.getMap();
List<User> listS = UserManager.getUsers(Group.sale);      // 获取送货员
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>查询订单</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
function func(str){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
}

function change(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=songhuo&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功"); 
           window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}
</script>
</head>


<body>
<div class="s_main">
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
<!--  头 单种类  -->
<div class="s_main_tit">订单查询页面<span class="qiangdan"><a href="">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span><span class="qiangdan"><a href="">查询</a></span></div>
 

<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
  <tr>
    <td width="25%" class="s_list_m">产品名称</td>
    <td width="25%" class="s_list_m">产品型号</td>
    <td width="25%" class="s_list_m">安装日期</td>
    <td width="25%" class="s_list_m">送货地点</td>
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
		     <% 
		     List<OrderProduct> lists = OrderProductManager.getOrderStatues(user, o.getId());
		     for(int g = 0 ;g<lists.size();g++){
		    	 OrderProduct op = lists.get(g);
		    	 %> 
		    	 <td align="left"><%= op.getCategoryId()%></td>
		    	 <td align="left"><%=op.getSendType()%></td>
		    	 <% 
		     }
		     
		     %> 
		<td align="left"><%=o.getSaleTime() %></td>
		<td align="left">
		<%=o.getUsername() %>
        </td>
		<td align="left">
		<%=o.getPhone1() %> 
        </td>
		<td align="left">
			<a href="dingdanDetail.jsp?id=<%=o.getId()%>">[查看]</a>
		</td>
		
    </tr>
    <%} %>
</table>
<br/>

<br/>
</div>
<!--  zong end  -->
</div>
</body>
</html>