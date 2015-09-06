<%@ page language="java" import="java.util.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String serch = request.getParameter("serch");
System.out.println(serch);
List<Order> list = null ;
if(StringUtill.isNull(serch)){  
   list = OrderManager.getOrderlist(user,Group.sale);
}else {
   list = OrderManager.getOrderlist(user,Group.sale,serch);
}

HashMap<Integer,User> usermap = UserManager.getMap();   // 获取送货员
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>配单</title>
<meta content="{$head[keywords]}" name="keywords" />
<meta content="{$head[description]}" name="description" />
<base href="{SITE_URL}" />

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="http://voip022.gotoip3.com/meidi/css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
 <script type="text/javascript">
 var id = "";
 function func(str){
     $(id).css("display","none");
 	$("#"+str).css("display","block");
 	id = "#"+str ;
 }
  
 function winconfirm(){
		question = confirm("你确认要释放吗？");
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
			$.ajax({ 
		        type:"post", 
		         url:"server.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=shifang&oid="+attract.toString(),
		         dataType: "", 
		         success: function (data) {
		          alert("释放申请已提交成功");
		          window.location.href="songhuo.jsp";
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		          alert("释放申请失败");
		            } 
		           });
		 }
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
	           window.location.href="songhuo.jsp";
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
<div class="s_main_logo"><img src="../image/logo.png"></div>

<!--  头 单种类  -->
<div class="s_main_tit"><span class="qiangdan"><a href="catchdingdan.jsp">抢单</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span><span class="qiangdan"><a href="chaxun_songhuo.jsp">查询</a></span></div>
<div class="s_main_tit">送货</div>
  

<!--  订单详情  -->
<div class="s_main_box">

<!--  lou start -->


<% 
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
  
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="s_main_table">

  <tr>
    <td width="20%" id="lt">
  产品名称:</td>
    <td width="30%" id="lt" class="red">
    <div id="div<%=o.getId()%>"  >
		   <table>
		     <tr>     
		    </tr>
		     <% 
		     List<OrderProduct> lists = OrderProductManager.getOrderStatues(user, o.getId());
		     for(int g = 0 ;g<lists.size();g++){
		    	 OrderProduct op = lists.get(g);
		    	 %> 
		    	 <tr>  
		    	 <td align="left"><%=categorymap.get(op.getCategoryId()).getName()%></td>
		    	 <td align="left"><%=op.getSendType()%></td>
		    	 </tr>
		    	 <% 
		     } 
		     %> 
		   </table>
		 </div>
    </td>
    <td width="20%" id="lt">安装日期</td>
    <td width="30%" id="ltr" class="red"><%=o.getOdate() %></td>
  </tr>
     <tr>
    <td width="20%" id="lt">地址：</td>
    <td colspan="3" id="ltr" class="red"><%=o.getLocateDetail() %></td>
    </tr>
</table>
<br/>
<!--  lou end  -->



<!--  lou start -->

 <%} %>
<br/>
<!--  lou end  -->

<br/>
</div>

<!--  zong end  -->
</div>

</body>
</html>