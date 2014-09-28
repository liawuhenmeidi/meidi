<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="../searchdynamic.jsp"%>
    
<%  

request.setCharacterEncoding("utf-8");

//list = OrderManager.getOrderlistPrintlnSend(user,Group.sencondDealsend,Integer.valueOf(num),Page,sort);  
SelectOrder sorder = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.orderPrint,num,Page,sort,"");  
List<Order> list = sorder.getList(); 
count = sorder.getCount(); 
session.setAttribute("exportList", list); 

HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员    
List<User> listS = UserManager.getUsers(user,Group.send);
   
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
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
    line-height:15px;
}

#table{  
    width:1300px;
     table-layout:fixed ;
} 
#th{
    
    position:absolute;
    width:1300px;
    height:30px;
    top:0;
    left:0;
}
#wrap{
   background-color:white;
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px;
}

</style>
</head>

<body>

<script type="text/javascript" src="../../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
var pages = "<%=Page%>";   
var num = "<%=num%>";
  
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
		 window.location.href="dingdanprint2.jsp?pages="+pages+"&numb="+num;
	 });
	  
	// $("#search").blur(function(){
	 	
		// var search = $("#search").val();
		// var serchProperty = $("#serchProperty").val();
		 
		// window.location.href="dingdan.jsp?search="+search+"&serchProperty="+serchProperty;
	// });
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdanprint2.jsp?pages="+pages+"&numb="+num;
	 }); 
	 
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdanprint2.jsp?page="+pages+"&numb="+num+"&sort="+sort;
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
         url: "../server.jsp", 
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
         url: "../server.jsp",
         data:"method=dingdaned&oid="+oid+"&id="+id, 
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
         url: "../server.jsp",
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

function orderPrint(id,statues,type){
	var str = "print2";
 if(type == 21){
	 str = "print3" ;
 }
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",   
         data:"method="+str+"&id="+id+"&statues="+statues,
         dataType: "",    
         success: function (data) {   
           window.location.href="../printPaigong.jsp?id="+id+"&type="+type;
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}

</script>

<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">

 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
<jsp:include flush="true" page="../page.jsp">
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="href" value="dingdanprint2.jsp" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/>
    <jsp:param name="type" value="<%=Order.porderPrint%>"/>  
</jsp:include>   
<jsp:include page="../search.jsp"/>
</div>
<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

</div>
<div style=" height:150px;">
</div>

 
<br/>  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">   
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td>
			<td align="center">门店</td> 
			<td align="center">顾客信息</td>
			<td align="center">送货名称</td>

            <td align="center">安装日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			
			<td align="center">送货人员</td>
			<td align="center">备注</td>
		    <td align="center">送货打印</td> 
			<td align="center">退货打印</td> 
			 
			<!--  <td align="center">修改申请</td> -->
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
		<!--  <td align="center"><input type="checkbox" value="1" name="userid[]"/></td> -->
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('../dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
		<td align="center"><%=o.getBranch()%></td>
		<td align="center"> 		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		</td> 
		<% 
		String tdcol = " bgcolor=\"red\"" ;
		if(o.getPhoneRemark()!=1){
			tdcol = "";
		}
		  %>   
		<td align="center"><%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		%>
		
		</td>    
		 
		  <td align="center"><%= o.getCategory(0,"</p>")%></td>  
		    
		 
		   
		
		<td align="center"><%=o.getOdate() %></td>
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">  
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>
		<td align="center">
		
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
		
		<td align="center"> 
		<% if(o.getSendId() != 0){
			if(usermap.get(Integer.valueOf(o.getSendId())) != null){
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }
		}
		 %>
		
		</td>
        <td align="center">   
		    <%=o.getRemark() %>
		</td> 
		
       <td align="center"> 
       <%  if(o.getReturnid() == 0) {%> 
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1,'<%=Order.orderpeisong%>')">[打印]</a>
			<%
		}
		%>
		</td>
	
		<td align="center">  
		 <%  if(o.getReturnid() != 0) {
			 
		 %>
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1,'<%=Order.returns%>')">[打印]</a>
		 <%  }%>
		</td>
		
		
	    
		
		<!--  
		<%
		OrderPrintln opp = opMap.get(o.getId());
		if(opp!= null){

		%>
		<td align="center"><span style="cursor:hand" onclick="funcc('ddiv<%=o.getId() %>',<%=o.getId() %>)"></span>
		 <div id="ddiv<%=o.getId()%>"  style= "" >
		  
		    
		    	 <%
		    	  if(opp.getStatues() == 2){
		    	
		    	 %> 
		    	
		    	   <p>修改申请已处理</p>
		    	   
		    	 <%
		    	 
		    	  }else {
		    	      %>
		    	     
		    	     <%=opp.getMessage() %>
		    	    
		    	     <input type="button" onclick="changes('<%=o.getId()%>','<%=opp.getId() %>')"  value="确定"/>
		    	     <%
		    	  }
		    	     %>
		   
		   
		 </div>
		</td>
		
		<%
		}else {
			
			%>	
		
		<td align="center"></td>
		
		<%
		}
		%>
		-->
		
    </tr> 
    <%}
    
    }%>
</tbody>
</table>


     </div>

</body>
</html>
