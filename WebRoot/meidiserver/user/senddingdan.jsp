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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看报装单</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

</head>
<body>


<!--   头部开始   -->
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
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
   <div class="main_r">      
   <div class="main_r_tianjia">
   </div>
     
   <div class="table-list">
<table width="100%" cellspacing="0">
	<thead> 
		<tr>
			<th align="left" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></th>
			<th align="left" class="s_list_m">产品名称/产品类别/产品数量</th>
            <th align="left" class="s_list_m">销售日期</th>
            <th align="left" class="s_list_m">顾客姓名</th>
            <th align="left" class="s_list_m">电话</th>
			<th align="left" class="s_list_m">查看详细信息</th>
		</tr>
	</thead>
<tbody> 
  <% 
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2==0){
    		col = "style=background-color:yellow";
    	}
  %>
    <tr  <%= col%>  >
		<td align="left">
	
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
</tbody>
</table>

<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="return confirm('您确定要删除吗？')"></input>

</div>

<div id="pages"></div>
</div>  
     
     
     </div>


</div>



</body>
</html>
