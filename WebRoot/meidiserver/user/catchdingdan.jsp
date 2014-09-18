<%@ page language="java" import="java.util.*,category.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
System.out.println(user);

// usermap.get(o.getSaleID()).getUsername()
List<Order> list = OrderManager.getOrderlist(user,Group.catchsend);

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>抢单</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../css/songhuo.css">
</head>
<body>
<div class="s_main">
<div class="s_main_logo"><img src="../image/logo.png"></div>
<!--  头 单种类  -->

<div class="s_main_tit">要配送的单<span class="qiangdan"><a href="songhuo.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
 <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
 <script type="text/javascript">
 var id = "";
 
 function winconfirm(){
		question = confirm("你确认要抢单吗？");
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
		        type: "post", 
		         url: "server.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=qiangdan&oid="+attract.toString(),
		         dataType: "", 
		         success: function (data) {
		          alert("抢单成功");
		          window.location.href="songhuo.jsp";
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		          alert("抢单失败");
		            } 
		           });
		 }
	}
 
 function func(str){
     $(id).css("display","none");
 	$("#"+str).css("display","block");
 	id = "#"+str ;
 }
 function seletall(all){
		if($(all).attr("checked")){
			$("input[type='checkbox']").each(function(){
				$(this).attr("checked",true);
	
		     });
		}else if(!$(all).attr("checked")){
			$("input[type='checkbox']").each(function(){
				$(this).attr("checked",false);
	
		     });
		}

	}
 
 function change(str1,str2){
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=songhuo&id="+str2,
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
<div class="s_list">
<table width="100%">
  <tr>
  <td align="left" width="10%">
    <td width="10%" class="s_list_m">订单号</td>
    <td width="20%" class="s_list_m">产品</td>
    <td width="10%" class="s_list_m">安装日期</td>
    <td width="10%" class="s_list_m">姓名</td>
    <td width="10%" class="s_list_m">电话</td>
    <td width="10%" class="s_list_m">送货地址</td>
  </tr>
  <% 
    for(int i=0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2==0){
    		col = "style=background-color:yellow";
    	}
  %>
  <tr <%= col%> >
    <th align="left"><input type="checkbox" value="" id="check_box"  name="<%=o.getId() %>"></input></th>
    <td><%=o.getId() %></td>
    <td align="left">
		 <div id="div<%=o.getId()%>">
		   <table>
		     <% 
		     List<OrderProduct> lists = OrderProductManager.getOrderStatues(user, o.getId());
		     for(int g = 0 ;g<lists.size();g++){
		    	 OrderProduct op = lists.get(g);
		    	 %> 
		    	 <tr  >
		    	 <td align="left"><%= op.getCategoryId()%></td>
		    	 <td align="left"><%=op.getSendType()%></td>
		    	 <td align="left"><%=op.getCount()%></td>
		    	 </tr>
		    	 <% 
		     }
		     
		     %> 
		   </table>
		 </div>
		
		</td>
    <td><%=o.getOdate() %></td>
    <td><%=o.getUsername() %></td>
    <td><%=o.getPhone1() %></td>
    
    <td><a href=""><%=o.getLocateDetail() %></a></td><!--  点击进入 单详情界面  -->
  </tr>
  
 <%
    }
  %>
  
</table>
 <input type="submit" class="button" name="dosubmit" value="抢单" onclick="winconfirm()"></input>
</div>
<!--  zong end  -->
</div>
</body>
</html>