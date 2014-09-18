<%@ page language="java" import="java.util.*,category.*,order.*,user.*,orderproduct.*,group.*,orderPrint.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
System.out.println(user);
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
List<Order> list = OrderManager.getOrderlist(user,Group.send);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>送货</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../css/songhuo.css">
</head>
<body>
<div class="s_main">
<div class="s_main_logo"><img src="../image/logo.png"></div>
<!--  头 单种类  -->

<div class="s_main_tit">要配送的单<span class="qiangdan"><a href="catchdingdan.jsp">抢单</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span></div>
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
<div class="s_list">
<table width="100%">
  <tr>
    <td width="5%" class="s_list_m"></td>
    <td width="30%" class="s_list_m">产品名称</td>
    <td width="30%" class="s_list_m">送货地址</td>
    <td width="10%" class="s_list_m">产品状态</td>
    <td width="10%" class="s_list_m">释放状态</td>
  </tr>
  <% 
    for(int i=0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2==0){
    		col = "style=background-color:yellow";
    	}
  %>
  <tr>
    <th align="left"><input type="checkbox" value="" id="check_box"  name="<%=o.getId() %>"></input></th>
    <td align="left">
		 <div id="div<%=o.getId()%>"  col >
		   <table>
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
    
    <td><a href=""><%=o.getLocateDetail() %></a></td><!--  点击进入 单详情界面  -->
    <td>
    <%
      if(2!=o.getDeliveryStatues()){
    %>
     <select class = "category" name="category"  id="songh<%=o.getId() %>" >
     <option value="0" >未完成 </option>
     <option value="2" >已完成 </option>
      </select>
     <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>')"  value="确定"/>
    </td>
     <td>
     <%    
			OrderPrintln or = OrderPrintlnManager.getOrderStatues(user, o.getId(),3);
			 if(or != null){
			  int statues = or.getStatues();
	          String sm = "";
	          if(0 == statues){
	        	  sm = "待确认";
	          }else if(1== statues){
	        	  sm = "确认中";
	          }else if(2== statues){
	        	  sm = "已确认";
	          } 
      %>
     <%=sm %>
     <%
          }else {

     %>
              无
     <%
	   }
     %>
     
    <%
	
		}else{

    %>
                      已完成
    <%
      }
    %>
</td>
  </tr>
  
 <%
      }
  %>
</table>
 <input type="submit" class="button" name="dosubmit" value="释放" onclick="winconfirm()"></input>
</div>
<!--  zong end  -->
</div>
</body>
</html>