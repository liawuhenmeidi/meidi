<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
  
<%  

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId();  
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
int opstatues = OrderPrintln.release; 
 
String serchProperty = ""; 
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");
String search = request.getParameter("search");
String sort = request.getParameter("sort");
if(StringUtill.isNull(pageNum)){
	pageNum = "1"; 
}
if(StringUtill.isNull(numb)){
	numb = "10";
}
if(StringUtill.isNull(sort)){ 
	sort = "id"; 
}

int count = 0;
int Page = Integer.valueOf(pageNum);

System.out.println("Page"+Page);

int num = Integer.valueOf(numb);
if(Page <=0){ 
	Page =1 ;
} 
List<Order> list = null ;
boolean flag = false ;

 
//list = OrderManager.getOrderlistl(user,Group.sencondDealsend,"",sort); 
     
list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.callback,num,Page,sort,"");  
count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.callback,num,Page,sort,"");  
     
HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员    
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
List<User> listS = UserManager.getUsers(user,Group.send);
    
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style type="text/css">
.fixedHead { 
position:fixed;
}  
.tabled tr td{ 
width:50px
}  
*{
    margin:0;
    padding:0;
}

td{ 
    width:100px;
    line-height:30px;
}

table{  
    width:2700px;
}
#th{
    background-color:#888888;
    position:absolute;
    width:2700px;
    height:30px;
    top:0;
    left:0; 
}
#wrap{
 
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:450px;
}

</style>
</head>

<body>

<script type="text/javascript" src="../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
var pages = "" ;
var num = "";
var oid ="<%=id%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";

$(function () {
	$("#wrap").bind("scroll", function(){ 

		if(pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)){
	        //滚动了竖直滚动条
	        pre_scrollTop=($("#wrap").scrollTop() || document.body.scrollTop);
	       
	        if(obj_th){
	            obj_th.style.top=($("#wrap").scrollTop() || document.body.scrollTop)+"px";
	        }
	    }
	    else if(pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)){
	        //滚动了水平滚动条
	        pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollLeft);
	    }
		}); 
	$("select[id='numb'] option[value='"+num+"']").attr("selected","selected");
	
	 $("#page").blur(function(){
		 pages = $("#page").val();
		 window.location.href="dingdanpeidan2.jsp?pages="+pages+"&numb="+num;
	 });
	 
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdanpeidan2.jsp?pages="+pages+"&numb="+num;
	 });  
	 
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdanpeidan2.jsp?page="+pages+"&numb="+num+"&sort="+sort;
	 }); 
});

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
 
function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="dingdanpeidan2.jsp";
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
         url: "../user/server.jsp",
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功");  
           window.location.href="dingdanpeidan2.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}
 
function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
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
		
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp", 
	         data:"method=statuescallback&id="+attract.toString(),
	         dataType: "",   
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("操作成功"); 
	        		  window.location.href="dingdancallback.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("服务器忙，请稍后重试"); 
	            }  
	           });
	}
}

function orderPrint(id,statues){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=print2&id="+id+"&statues="+statues,
         dataType: "",  
         success: function (data) {  
           window.location.href="printPaigong.jsp?id="+id;
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           }); 
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
	};
}  
</script>
<div style="position:fixed;width:100%;height:200px;background:#CCCCCC;">

 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
<jsp:include flush="true" page="page.jsp">
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
	<jsp:param name="type" value="<%=Order.orderPrint%>"/> 
</jsp:include> 
<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input>  
</div>

</div>

<div style=" height:150px;">
</div>

 
<br/>  
<div id="wrap">
<table  cellspacing="1">
		<tr id="th">  
			
			<td align="left" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="left">单号</td>
			<td align="left">门店</td>
			<td align="left">pos单号</td>
			<td align="left">销售单号</td>
			<td align="left">验证码</td>
			
			<td align="left">顾客姓名</td>
			<td align="left">电话</td>
			<td align="left">票面名称</td>
			<td align="left">票面型号</td>
			<td align="left">票面数量</td>
			
			<td align="left">送货名称</td>
			<td align="left">送货型号</td>
			<td align="left">送货数量</td>
			<td align="left">赠品</td>
			<td align="left">赠品数量</td>
			
			<td align="left">赠品状态</td>
            <td align="left">开票日期</td>
            <td align="left">安装日期</td>
            <td align="left">送货地区</td>
            <td align="left">送货地址</td>
            
            <td align="left">送货状态</td>
			<td align="left">打印状态</td>
			<td align="left">打印</td>
			<td align="left">报单人员</td>
			<td align="left">备注</td>
			
			<td align="left">送货人员</td>
			<td align="left">送货员释放请求</td>  
		  
		     
		</tr>
	
<tbody> 
  <% 
   if(null != list){
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
   <tr id="<%=o.getId()+"ss" %>"  class="asc"  onclick="updateClass(this)"> 
		<td align="left" width="20"><input type="checkbox" value="" id="check_box" name = "<%=o.getId() %>"></input></td>
		<td align="left"><%=o.getPrintlnid() == null?"":o.getPrintlnid()%></td>
		<td align="left"><%=o.getBranch()%></td>
		<td align="left"><%=o.getPos() %></td>
		<td align="left"><%=o.getSailId() %></td>
		<td align="left"><%=o.getCheck() %></td>
		<td align="left"><%=o.getUsername() %></td>
		<td align="left"><%=o.getPhone1()%></td>  
		  <td align="center"><%= o.getCategory(1,"</p>")%></td>    
		  <td align="center" ><%=o.getSendType(1,"</p>")%></td>    
		  <td align="center" ><%= o.getSendCount(1,"</p>")%></td>    
		  <td align="center"><%= o.getCategory(0,"</p>")%></td>  
		  <td align="center" ><%=o.getSendType(0,"</p>")%></td>  
		  <td align="center" ><%= o.getSendCount(0,"</p>")%></td>   
		<td align="center" ><%= o.getGifttype("</p>")%></td>  
		<td align="center" ><%= o.getGifcount("</p>")%></td>  
		<td align="center" ><%= o.getGifStatues("</p>")%></td>
		
		<td align="left"><%=o.getSaleTime() %></td>
		<td align="left"><%=o.getOdate() %></td>
		<td align="left"><%=o.getLocate()%></td>
		<td align="left"><%=o.getLocateDetail() %></td>
		<td align="left">
		<%
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == o.getDeliveryStatues()){
		%>
		 未发货
		<%
          }else if(1 == o.getDeliveryStatues()){

		%>
		正在送货
		<%
          }else if(2 == o.getDeliveryStatues()){
		%>
	    已送货
		<%
          }else if(3 == o.getDeliveryStatues()){
		%>
		
		 已退货
		<%
          }
		%>
		</td>
		<td align="left">
		 
		<%
		//打印状态     0  未打印   1 打印
		if(0 == o.getPrintSatuesP()){
		%>
		 未打印  
		<%
         }else if(1 == o.getPrintSatuesP()){
		%>
		已打印
		<%
         }
		%>
		
		
		</td>
		  <td align="left"> 
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1)">[打印]</a>
		</td>
		<td align="left"><%=usermap.get(Integer.valueOf(o.getSaleID())).getUsername() %></td>    
		
        <td align="left">   
		    <%=o.getRemark() %>
		</td>
 
		<td align="left"> 
		<% if(o.getSendId() != 0){  
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }
		 %>
		
		</td>
	
		<td align="left">    
		    <%
		      OrderPrintln opp =   opMap.get(o.getId()); 
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
		<%
		}
		%>
		</td>
		
    </tr>
    <%}
    
    }%>
</tbody>
</table>


     </div>


</div>
</div>


</body>
</html>
