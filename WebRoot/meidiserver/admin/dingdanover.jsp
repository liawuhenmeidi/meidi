<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>
 
<%   
request.setCharacterEncoding("utf-8");

List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.over,num,Page,sort,sear);
session.setAttribute("exportList", list); 
count = OrderManager.getOrderlistcount(user,Group.dealSend,Order.over,num,Page,sort,sear);
  
HashMap<Integer,User> usermap = UserManager.getMap();
//     System.out.println(count+"**"+"num"+"");
//获取二次配单元（工队）

List<User> listS = UserManager.getUsers(user,Group.sencondDealsend);   

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
    line-height:15px;
}

#table{  
    width:1500px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:1500px;
    height:30px;
    top:0;
    left:0;
}
#wrap{ 
   
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px;
}

</style>
</head>

<body>
<script type="text/javascript" src="../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
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
	
	
});

function serch(){
	 var str = "";
	 var search = $("#search").val();
	 var serchProperty = $("#serchProperty").val();
	 var dealSendid = $("#dealSendid").val();
	  
	 if(dealSendid != null && dealSendid !=""){
		 str += "and dealSendid = " + dealSendid ;
	 }
	 if(serch != null && serchProperty != null && serchProperty != "" && serch != ""){
		 str += "  and " + serchProperty + "  like '%25"+search+"%25'	";
	 } 
	 window.location.href="dingdanover.jsp?search="+str;  
	 // sencondsend

}

function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.name;
	   				if(str != null && str != ""){
		   				   attract[i] = str;
			   	            i++;
		   				}
	   		}
	   	});
		
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",   
	         data:"method=orderover&id="+attract.toString(),
	         dataType: "", 
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("执行失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("执行成功");
	        		  window.location.href="dingdanover.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("执行失败"); 
	            } 
	           });
		
		
	}
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

 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
<jsp:include flush="true" page="page.jsp"> 
    <jsp:param name="searched" value="<%=searched %>" /> 
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/>   
	<jsp:param name="type" value="<%=Order.over %>"/> 
</jsp:include>  

<jsp:include page="search.jsp"/>

<div class="btn"> 
 <!--
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="return confirm('您确定要删除吗？')"></input>
  -->
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 &nbsp;<input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input>  
</div>  
</div >
 
<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

</div>

<div style=" height:170px;">
</div>

 
<br/>  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
			<td align="center">门店</td>
			
			
			<td align="center">顾客信息</td>
		
			
			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
            
           
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
            
			
			
			<td align="center">安装单位</td>
			<td align="center">送货人员</td>
			<td align="center">送货时间</td>
			<td align="center">备注</td>
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
		<td align="center" width="20"><input type="checkbox" value="" id="check_box" name = "<%=o.getId() %>"></input></td>
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
		<td align="center"><%=o.getBranch()%></td> 
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
		  <td align="center" ><%=o.getSendType(0,"</p>")%></td>  
		  <td align="center" ><%= o.getSendCount(0,"</p>")%></td>   
		<td align="center" ><%= o.getGifttype("</p>")%></td>  
		<td align="center" ><%= o.getGifcount("</p>")%></td>  
		<td align="center" ><%= o.getGifStatues("</p>")%></td>
		
		
		
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>
		
		<td align="center" style="white-space:nowrap;">
		 
		 <% if(o.getDealsendId() != 0){  
		 %> 
		 <%=usermap.get(Integer.valueOf(o.getDealsendId())).getUsername() %>
		 <%
		  }
		 %>
          
		</td>
        
        <td align="center" style="white-space:nowrap;"> 
		  <% if(o.getSendId() != 0){  
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }
		 %>
		 
		</td>
         
        <td align="center"> 
		    <%=o.getSendtime() %>
		</td>
        
        <td align="center"> 
		    <%=o.getRemark() %>
		</td>
		
		
    </tr>
    <%}
    
    }%>
</tbody>
</table> 

     </div>

</body>
</html>
