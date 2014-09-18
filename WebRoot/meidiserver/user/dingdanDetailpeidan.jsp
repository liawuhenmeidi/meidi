<%@ page language="java" import="java.util.*,orderPrint.*,category.*,group.*,user.*,utill.*,product.*,order.*,orderproduct.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int uid = user.getId();
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
HashMap<Integer,User> usermap = UserManager.getMap();
String id = request.getParameter("id");
int opstatues = OrderPrintln.release; 
Order or = OrderManager.getOrderID(user,Integer.valueOf(id));
//获取送货员    
List<User> listS = UserManager.getUsers(user,Group.send);
// 售货员的id号
int saleid = or.getSaleID();
int canupdate = 0;
if(saleid == uid){
	canupdate = 1 ;
}
OrderPrintln oor = OrderPrintlnManager.getOrderStatues(user, or.getId(),0);
int statues = -1 ;

if(oor != null){  
 statues = oor.getStatues();	
}  
 
OrderPrintln oop = OrderPrintlnManager.getOrderStatues(user, or.getId(),1);
int statuess = -1 ;

if(oop != null){
	 statuess = oop.getStatues();	
	}

request.setAttribute("order", or);
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

int printstatues = or.getPrintSatues();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单详细页</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
var id = "";
var oid ="<%=id%>";
var pgroup = "<%=pgroup%>";

var opstatues = "<%=opstatues%>";

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

function changes(oid,id){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdaned&oid="+oid+"&id="+id, 
         dataType: "", 
         success: function (data) {
           window.location.href="dingdanDetailpeidan.jsp";
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

function winconfirm(str,str2){

	if(str2 == "false" ){
		alert("您已经提交释放申请");
		return ;
	}
	 
	question = confirm("你确认要释放吗？");
	if (question != "0"){

		$.ajax({    
	        type:"post", 
	         url:"server.jsp",
	         //data:"method=list_pic&page="+pageCount,      
	         data:"method=shifang&oid="+oid+"&pGroupId="+pgroup+"&opstatues="+opstatues,
	         dataType: "",  
	         success: function (data) {    
	          alert("释放申请已提交成功"); 
	          window.location.href="dingdanDetailpeidan.jsp?id="+oid;
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("释放申请失败");
	            } 
	           });
	 }
}
</script>
</head>
<body>
<div class="s_main">

<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
<span class="qiangdan"><a href="chaxun_peidan.jsp">订单查询</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span>
</div>
  
<!--  头 单种类  -->


<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
  <tr>
    <td width="25%" class="s_list_m">单号</td>
    <td class="s_list_m"><%=or.getPrintlnid() == null?"":or.getPrintlnid()%></td>
     
  </tr>  
  <%
  List<OrderProduct> lists = OrderProductManager.getOrderStatues(user, or.getId());
  for(int g = 0 ;g<lists.size();g++){
 	 OrderProduct op = lists.get(g);
      if(op.getStatues() == 1 ){
      
  
  %>
   <tr>
    <td width="25%" class="s_list_m">票面名称</td>
    <td class="s_list_m">

	<%=categorymap.get(Integer.valueOf(op.getCategoryId())).getName()%>

    </td>
   
  </tr>
  <tr>
    <td width="25%" class="s_list_m">票面型号</td>
    <td class="s_list_m">

		    	 <%=op.getSaleType()%>
 
    </td>
   
  </tr>
  <tr>
    <td width="25%" class="s_list_m">票面数量</td>
    <td class="s_list_m">

		    	 <%=op.getCount()%>

    </td>
   
  </tr>
  <%
     }else {
    	 String col = "";
   	  if(g%2==0){
   		
   		  col = "style='background-color:yellow'";
   	  }
   	 
    	 %> 
    	 
    	 <tr <%=col %>>
         <td width="55%" class="s_list_m">送货名称</td>
     
         <td class="s_list_m">

    		  <%=categorymap.get(Integer.valueOf(op.getCategoryId())).getName()%> 
         </td>
         </tr>
    	 
    	  <tr <%=col %>>
     <td width="55%" class="s_list_m">送货类别</td>
     <td class="s_list_m">
		    
		    	<%=op.getSendType()%>
      </td>
      </tr>
    	 
    	<tr <%=col %>>
      
      <td width="55%" class="s_list_m">送货数量</td>
     
      <td class="s_list_m">

		    	<%=op.getCount()%>  
	
	</td>
  </tr> 
    	 
    	 
    	 
    	 
    <%   
      }
   }
  %>
 
     
    
      
      
      
       
  <tr>
    <td class="s_list_m">顾客姓名</td>
    <td class="s_list_m"><%=or.getUsername() %></td>
  </tr>
   <tr>
 
    
  
  <tr >
    <td class="s_list_m">电话</td> 
    
    <td><a href="tel:<%=or.getPhone1() %>"><%=or.getPhone1() %></a></td>
    <!--  一键拨号  -->
  </tr>
  
    <tr >
    <td class="s_list_m">送货时间</td>
    <td><%=or.getOdate() %></td>
    
  </tr> 
  <tr>
    <td class="s_list_m">送货地区</td>
    <td class="s_list_m"><%=or.getLocate() %></td>
  </tr>
  <tr>
    <td class="s_list_m">送货地点</td>
    <td class="s_list_m"><%=or.getLocateDetail() %></td>
  </tr>
    <%   
		OrderPrintln opp = OrderPrintlnManager.getOrderStatues(user, or.getId(),OrderPrintln.salerelease);
		if(opp!= null){
  
		%> 
		
     <tr>
       <td class="s_list_m">送货员释放请求</td>
       <td class="s_list_m">
     
		<!--  <span style="cursor:hand" onclick="funcc('ddiv<%=or.getId() %>',<%=or.getId() %>)">有申请释放请求</span> -->
		 <div id="ddiv<%=or.getId()%>"  >
		   <table>
		    	 <tr>
		    	     <td>  
		    	     <%=opp.getMessage() %></td>
		    	 </tr> 
		   </table> 
		   <input type="button" onclick="changes('<%=or.getId()%>','<%=opp.getId() %>')"  value="确定"/> 
		 </div>
		</td>
		<%
		}
		%>
  </tr>
      <%    
			OrderPrintln orp = OrderPrintlnManager.getOrderStatues(user, or.getId(),OrderPrintln.release); 
			 if(orp != null){
			  int sta = orp.getStatues();
	          String sm = "";
	          if(0 == sta){
	        	  sm = "待确认";
	          }else if(1== sta){
	        	  sm = "确认中"; 
	          }else if(2== statuess){ 
	        	  sm = "已同意";
	          }else if(4== statuess){
	        	  sm = "已拒绝";  
	          }
      %>  
    <tr>
    <td class="s_list_m">释放状态</td>
     <td>
     <%=sm %>
    </td>
    </tr>
    <% 
    }
    %>
 
   <tr> 
    <td class="s_list_m">订单配工</td>
    <td class="s_list_m"> 
    <%  if(2 == or.getDeliveryStatues()) { 
				for(int j=0;j< listS.size();j++){
	      	     User u = listS.get(j);
	      	     if(u.getId() == or.getSendId()){
	      		   %>
	      		   <%=u.getUsername() %>
	      	      <% } 
				  }
         }else {
		%>
		<select class = "category" name="category"  id="songh<%=or.getId() %>" >
		 <option > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
		<%     
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str1 = "";
            	   if(u.getId() == or.getSendId()){
            		   str1 = "selected=selected" ;
            	   }    
            	   %>  
            	    <option value=<%=u.getId() %>  <%= str1%>> <%=u.getUsername() %></option>
            	   <% 
            	  
                    } 
	               %>
         </select> 
       
         <input type="button" onclick="change('songh<%=or.getId()%>','<%=or.getId()%>')"  value="确定"/>
		<%
         }
		%>
    </td>
  <tr>
  
  
  <tr></tr>
  <tr></tr>
  <tr>

  </tr>
</table>

  <br>
  <br>
    
<span class="qiangdan"><a href="javascript:void(0);" onclick="getmap()">进入地图</a></span>
<span class="qiangdan"> <input type="submit" class="button" name="dosubmit" value="释放订单" onclick="winconfirm('','<%=orp==null%>')"></input></span>
 
<!--  zong end  -->
</div>

</body>
</html>