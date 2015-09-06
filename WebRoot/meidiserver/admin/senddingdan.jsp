<%@ page language="java" import="java.util.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

List<Order> list = OrderManager.getOrderlist(user,Group.dealSend);
HashMap<Integer,User> usermap = UserManager.getMap(); 
List<User> listS = UserManager.getUsers(Group.send);      // 获取送货员
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配单页面</title>

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
function funcc(str,str2){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
	$.ajax({  
        type: "post", 
         url: "server.jsp", 
         data:"method=dingdan&id="+str2,
         dataType: "", 
         success: function (data) {
           window.location.href="senddingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}

function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           //window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
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
           window.location.href="senddingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}
</script>
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
<div class="weizhi_head">现在位置：订单管理</div>        
   <div class="main_r_tianjia">
   </div>
     
   <div class="table-list">
<table width="100%" cellspacing="0">
	<thead> 
		<tr>
			<th align="left">产品名称/产品类别/产品数量</th>
            <th align="left">地址</th>
            <th align="left">电话</th>
			<th align="left">安装日期</th>
			<th align="left">配单人员</th>
			<th align="left">是否有释放请求</th>
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
		<td align="left"><%=o.getLocateDetail() %></td>

		<td align="left">
		  <%=o.getPhone1() %>
        </td>
		<td align="left"><%=o.getOdate() %></td>  
		<td align="left" style="white-space:nowrap;">
		
		<%  if(2 == o.getDeliveryStatues()) { 
				for(int j=0;j< listS.size();j++){
	      	     User u = listS.get(j);
	      	     if(u.getId() == o.getSendId()){
	      		   %>
	      		   <%=u.getUsername() %>
	      		   
	      	      <% } 
				  }
         }else {
		%>
		<select class = "category" name="category"  id="songh<%=o.getId() %>" >
		<% 
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str = "";
            	   if(u.getId() == o.getSendId()){
            		   str = "selected=selected" ;
            	   } 
            	   %>
            	    <option value=<%=u.getId() %>  <%= str%>> <%=u.getUsername() %></option>
            	   <% 
            	  
                    }
	                	%>
         </select> 
      
         <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>')"  value="确定"/>
		<%
         }
		%>
		</td>
		<%
		OrderPrintln opp = opMap.get(o.getId());
		if(opp!= null){

		%>
		<td align="left"><span style="cursor:hand" onclick="funcc('ddiv<%=o.getId() %>',<%=o.getId() %>)">*</span>
		 <div id="ddiv<%=o.getId()%>"  style= "display:none;background-color:yellow" >
		   <table>
		    	 <tr>
		    	     <td>
		    	     <%=opp.getMessage() %></td>
		    	 </tr> 
		   </table>
		   <input type="button" onclick="changes('<%=o.getId()%>')"  value="确定"/>
		 </div>
		</td>
		<%
		}
		%>
		
    </tr>
    <%} %>
</tbody>
</table>

</body>
</html>
