<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../searchdynamic.jsp"%>  
<%  
List<User> listS = UserManager.getUsers(user ,Group.sencondDealsend); 
HashMap<Integer,User> usermap = UserManager.getMap(); 
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.neworder,0,0,"id",""); 
count =  OrderManager.getOrderlistcount(user,Group.dealSend,Order.neworder,0,0,"id",""); 
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  
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

td { 
    width:100px;
    line-height:30px;
}
 
#table{  
    BACKGROUND-IMAGE: url('../image/f.JPG');
    width:2000px;
    table-layout:fixed ;
}

#th{ 
     background-color:white;
    position:absolute;
    width:2000px; 
    height:30px;
    top:0;
    left:0;
}
#wrap{
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:450px;
}

</style>
</head>

<body style="scoll:no">
 
 
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
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
		 window.location.href="neworder.jsp?pages="+pages+"numb="+num;
	 });

	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="neworder.jsp?page="+pages+"&numb="+num;
	 }); 
	  
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="neworder.jsp?page="+pages+"&numb="+num+"&sort="+sort;
	 }); 
}); 
  

function changepeidan(str1,oid){
	question = confirm("您确定要配单并打印吗？");
	if (question != "0"){
		var uid = $("#"+str1).val();
		$.ajax({ 
	        type: "post", 
	         url: "../server.jsp",
	         data:"method=peidan&id="+oid+"&uid="+uid,
	         dataType: "", 
	         success: function (data) { 
	            
	            window.location.href="../print.jsp?id="+oid; 
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	}else {
		return ;
	}
	
	
} 

function changes(str1,str2,str3,str4){

	if( 2 == str3 || "2" == str3){
		if(0 != str4 || "0" != str4){ 
			alert("商品已送货，您不能直接同意，请联系安装公司释放");
			return ;
		}
	} 

	$.ajax({ 
        type: "post", 
         url: "../server.jsp",   
         data:"method=dingdaned&id="+str1+"&oid="+str2+"&statues="+str3,  
         dataType: "",  
         success: function (data) {
           window.location.href="neworder.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
}  

</script>
<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">
  
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
<jsp:include flush="true" page="../page.jsp">
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
	<jsp:param name="type" value="<%=Order.orderDispatching %>"/> 
</jsp:include> 


<jsp:include page="../search.jsp"/>


</div >
 

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
			<td align="center">销售员</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">OMS订单号</td>
			<td align="center">验证码</td>
			
			<td align="center">顾客信息</td>
			
			<td align="center">送货名称</td>
			<td align="center">送货型号</td> 
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
            <td align="center">开票日期</td>
            <td align="center">安装日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
           <td align="center">送货状态</td>
			
			
			
			<td align="center">备注</td>
			<td align="center">配单</td>
  
		</tr>
	
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
		<td align="center"><%=o.getPrintlnid() == null?"":o.getPrintlnid()%></td>  
		<td align="center"><%=o.getBranch()%></td> 
		<td align="center">
		    
                		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		
		</td>
		<% 
		String tdcol = " bgcolor=\"red\"" ;
		  %>     
		 
		<td align="center" <%=o.getPosremark()==1?tdcol:"" %>><%=o.getPos() %></td>
		<td align="center" <%=o.getSailidrecked()==1?tdcol:"" %>><%=o.getSailId() %></td>
		<td align="center" <%=o.getReckedremark()==1?tdcol:"" %>><%=o.getCheck() %></td>
		<%if(o.getPhoneRemark()!=1){ 
			tdcol = ""; 
		} %>
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
		
		<td align="center"><%=o.getSaleTime() %></td>
		<td align="center"><%=o.getOdate() %></td>
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">
				<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>

        <td align="center"> 
		    <%=o.getRemark() %>
		</td>
         <%
         int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
		 int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());

          
         %>
		<td align="center"> 
				
				 <%    	  
				   if(o.getDealsendId() == 0 && o.getDeliveryStatues() != 8 && o.getPrintSatues() == 0){
					   if(modify != 2 && modify != 0 && returns != 2 && returns != 0){
					   %>
						<select class = "category" name="category"  id="songh<%=o.getId() %>" >
						 <option value=""></option>
						<%     if(listS != null ){
				               for(int j=0;j< listS.size();j++){
				            	   User u = listS.get(j);
				            	   String str = "";  
				            	   if(u.getId() == o.getDealsendId()){ 
				            		   str = "selected=selected" ;
				            	   }     
				            	   %> 
				            	    <option value=<%=u.getId() %>  <%= str%>> <%=u.getUsername() %></option>
				            	   <% 
				                    }
						     }
					            %>
				         </select> 
				         <input type="button" onclick="changepeidan('songh<%=o.getId()%>','<%=o.getId()%>','<%=o.getDeliveryStatues() %>','<%=o.getSendType(0,"</p>")%>')"  value="确定"/> 
				         
					<% 	
				     }
				   }else if(o.getDealsendId() == 0 && o.getDeliveryStatues() == 8 && o.getPrintSatues() == 0){
				
					   if(OrderManager.Check(o.getId())){ 
						  %>   
						   <input type="button" onclick="changepeidan('2','<%=o.getId()%>','<%=o.getDeliveryStatues() %>','<%=o.getSendType(0,"</p>")%>')"  value="打印"/>
					         &nbsp;&nbsp;&nbsp;
						  <%
					   }else {
						   %>
						   <input type="button" onclick="changepeidan('1','<%=o.getId()%>','<%=o.getDeliveryStatues() %>','<%=o.getSendType(0,"</p>")%>')"  value="打印"/>
					         &nbsp;&nbsp;&nbsp;
						   <input type="button" onclick="changepeidan('0','<%=o.getId()%>','<%=o.getDeliveryStatues() %>','<%=o.getSendType(0,"</p>")%>')"  value="确定"/>  
						   				   
						   <%
					   }
				   }else if(o.getDealsendId() != 0){
					   %>    
					<%=usermap.get(o.getDealsendId()).getUsername() +"</p>"+ usermap.get(o.getDealsendId()).getPhone()%>
					   <% 
				   }
				 %>
				</td> 
				
				<td align="center"> 
				    <a href="javascript:void(0);"  onclick="searchlocate('<%=o.getId() %>')">[查看位置]</a> 
				</td>

    </tr>

    <% 
    }
    }%>
</table> 
     </div>

</body>
</html>
