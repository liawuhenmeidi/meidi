<%@ page language="java" import="java.util.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId();

List<Order> list = null ;
boolean flag = false ; 
String saledateStart = request.getParameter("#saledateStart");
String  saledateEnd = request.getParameter("#saledateEnd");
String  deliveryStatues  = request.getParameter("#deliveryStatues");
// alert(deliveryStatues);
// 已分配还是未分配
String  sendId = request.getParameter("#sendId");
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
		 str += " and sendId = '"+id+"'";
	 }else {
		 str += " and sendId != '"+id+"'";  
	 }
}

if(sendId != null && sendId != "" && sendId != "null"){
	 str += " and sendId = '"+sendId+"'";
}

 
   list = OrderManager.getOrderlist(user,Group.sencondDealsend);

HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员    
List<User> listS = UserManager.getUsers(user,Group.send);
   
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

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
            // window.location.href="senddingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}

function changes(str1,str2){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="peidan.jsp";
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
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功"); 
           window.location.href="peidan.jsp";
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
<div class="s_main_tit">配单<span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span><span class="qiangdan"><a href="chaxun_peidan.jsp">查询</a></span></div>
  

<!--  订单详情  -->
<div class="s_main_box">

<!--  lou start -->


<% 
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	String col = "";
    	if(i%2==0){
    		col = "style=background-color:yellow";
    	}
  %>
  
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="s_main_table">

  <tr>
    <td width="20%" id="lt">产品名称:</td>
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
  
<tr>
    <td width="20%" id="blt">配单人员</td>
    <td width="30%" id="blt">
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
		 <option > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
		<%     
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str1 = "";
            	   if(u.getId() == o.getSendId()){
            		   str1 = "selected=selected" ;
            	   }    
            	   %>  
            	    <option value=<%=u.getId() %>  <%= str1%>> <%=u.getUsername() %></option>
            	   <% 
            	  
                    } 
	               %>
         </select> 
      
         <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>')"  value="确定"/>
		<%
         }
		%>
    </td>
    <td width="20%" id="blt">释放请求</td>
    <td width="30%" id="brlt">
    		<%
		OrderPrintln opp = opMap.get(o.getId());
		if(opp!= null){

		%>
		<span style="cursor:hand" onclick="funcc('ddiv<%=o.getId() %>',<%=o.getId() %>)">有申请释放请求</span>
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