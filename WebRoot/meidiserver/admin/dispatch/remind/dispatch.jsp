<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="../../searchdynamic.jsp"%> 
<%   
if(searchflag){ 
	sort= "andate asc";
}  
List<Order> list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.orderDispatching,num,Page,sort,sear);  
session.setAttribute("exportList", list); 
count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.orderDispatching,num,Page,sort,sear);  

opstatues = OrderPrintln.release;  

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

#table{  
    width:2100px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2100px;
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


<!--   头部开始   -->
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
var pages = "" ;
var num = "";
var uuid ="<%=id%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";
var usermapstr = <%=usermapstr%>;

$(function () {

});
 
function changes(oid,id,statues,flag,returnstatues,type,printid){
	if(statues == 2){ 
		if(flag == 2){
			alert("请联系送货员驳回");
			return ;      
		}else if(flag == 3){
			alert("请联系安装呀驳回");
			return ;
		}else if(flag == 1){
			if(returnstatues == 0){
				alert("请配置退货员拉回商品再同意");
				return ;
			}
		}

	} 
		$.ajax({   
	        type: "post", 
	         url: "../server.jsp", 
	         data:"method=dingdaned&oid="+oid+"&id="+id+"&statues="+statues, 
	         dataType: "", 
	         success: function (data) {
	        	
	        	 if(statues == 2){ 
	        		
	        		 if(<%=OrderPrintln.releasedispatch %> != type){

	        				
	        			window.location.href="../printPaigong.jsp?id="+oid+"&type=<%=Order.deliveryStatuesTuihuo%>&uid="+printid;
	        				
	        		}else {
	        			window.location.href="dingdanpeidan2.jsp";
	        		}
	        	 }else {
	        		
	        		 window.location.href="dingdanpeidan2.jsp";
	        	 } 
	        	 
	     
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           }); 
	
}
 // 派工送货员
function change(str1,oid,type,statues,types,saleId){
	
	var uid = $("#"+str1).val();
	var saleid = uuid;
	   if(type == 4 || type == 5){
		   saleid = saleId;
	   }
	   
	if(uid == null || uid == ""){
		alert("请选择送货员");
		return ;
	}
	 
	var branch = usermapstr[saleid].branchName;
	
	if(0 == statues){   
		alert("您已提交驳回申请，不能派工");
	}else {  
		$.ajax({ 
	        type: "post",   
	         url: "../server.jsp",    
	         data:"method=getinventory&types="+types+"&uid="+saleid,
	         dataType: "",  
	         success: function (data) {  
	        	    inventory = data;
	        	    data = data.replace(/{/g, "");
	        	    data = data.replace(/}/g, "");
	        	    data = data.replace(/,/g, "\n");
	        	    if(type == 3){
	        	    	question = confirm("您确定要配单并打印吗？\n");
	        	    }else {
	        	    	question = confirm("您确定要配单并打印吗？\n"+branch+"\n"+data);
	        	    }
				     
				     if (question != "0"){
								$.ajax({   
							        type: "post",     
							         url: "../../user/server.jsp", 
							         data:"method=peidan&id="+oid+"&uid="+uid+"&type="+type,
							         dataType: "",  
							         success: function (data) { 
							        	 if(data == 0){
							        		 alert("导购提交修改申请，不能配工");
							        		 return ; 
							        	 }else if(data == 20){ 
							        		 alert("导购提交退货申请，不能配工");
							        		 return ; 
							        	 }else {   
							        		 window.location.href="../printPaigong.jsp?id="+oid+"&type="+type;
							        	 }   
							        	 
							           },  
							         error: function (XMLHttpRequest, textStatus, errorThrown) { 
							        // alert(errorThrown); 
							            } 
							           });
				        			
			           } 
	         },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           });
	}
}

function adddetail(src){ 
	//window.location.href=src ;
	window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function winconfirm(str,str2,sendid){
	if(sendid != 0){
		alert("请联系送货员驳回");
		return ;
	}else {
		if(4 == str2){
	    	var question = confirm("您的驳回请求别拒绝，是否继续申请？");
				if(question == 0){
					return ;
				}
	    }else if(0 == str2){
	    	alert("您已提交驳回请求");
	    	return ; 
	    }

	}
  //alert(str2);
    
	
    question = confirm("你确认要驳回吗？");
	
	if (question != "0"){

		$.ajax({    
	        type:"post",  
	         url:"../../user/server.jsp",
	         //data:"method=list_pic&page="+pageCount,      
	         data:"method=shifang&oid="+str+"&pGroupId="+pgroup+"&opstatues="+opstatues,
	         dataType: "",  
	         success: function (data) {    
	          alert("驳回申请已提交成功");  
	          window.location.href="dingdanpeidan2.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("驳回申请失败");
	            } 
	           });
	 }
}

function searchlocate(id){  
	  window.location.href="../../adminmap.jsp?id="+id;
}

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}
</script>

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="../../page.jsp">
    <jsp:param name="sear" value="<%=sear %>" /> 
	<jsp:param name="page" value="<%=Page %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
     <jsp:param name="type" value="<%=Order.porderDispatching%>"/>  
</jsp:include> 

<jsp:include page="../../search.jsp">
 <jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
</jsp:include> 


</div > 
<div style=" height:170px;">
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
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
           
            <td align="center">预约日期</td>
            <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
           
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			
			
			<td align="center">备注</td>
			<td align="center">销售员</td>
			<td align="center">送货员</td>
			<td align="center">查看位置</td>
			 
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
		<td align="center"><%=o.getbranchName(o.getBranch())%></td>  
		
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
		 
		
		<td align="center"><%=o.getOdate() %></td>
		<td align="center"><%=o.getDealSendTime() %></td>
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">
		<%=OrderManager.getDeliveryStatues(o) %>
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
		    <%=o.getRemark() %>
		</td>
        <td align="center"> 		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		</td> 
		<td align="center">
		<%    

		  OrderPrintln salereleaseo = opmap.get(OrderPrintln.salerelease) == null?null:opmap.get(OrderPrintln.salerelease).get(o.getId()); 
		  int printid = o.getSendId();
	      OrderPrintln salereleasereturno = opmap.get(OrderPrintln.salereleasereturn) == null?null:opmap.get(OrderPrintln.salereleasereturn).get(o.getId()); 

	      if(salereleasereturno != null){
	    	  salereleaseo = salereleasereturno ;
	    	  printid = o.getReturnid();
	      }
	      
		int releasemodfy = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasemodfy, o.getId()) ;	
		int release = OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId()) ;
	    int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId()) ;
	    int salerelease = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salerelease, o.getId()) ;
	    int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
	    int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());
	    
	    int statuesnew = Order.orderpeisong; 
	    
	    
	    
	     if(o.getSendId() == 0 && release != 0 && releasedispatch != 0 && releasemodfy != 0){
				   if(o.getDeliveryStatues() == 9){
					   statuesnew = Order.ordersong;
				   }else if(o.getDeliveryStatues() == 10){
					   statuesnew = Order.orderinsta; 
				   }
				    	  
			%> 
			<select class = "category" name="category"  id="songh<%=o.getId() %>" >
			 <option value=""></option>
			<%     
	               for(int j=0;j< listSend.size();j++){
	            	   User u = listSend.get(j);
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
	           
	         <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>','<%=statuesnew %>',<%=release %>,'<%=o.getSendType(0,"</p>")%>','<%=o.getSaleID() %>')"  value="确定"/>
			<%
			} else {
				
			    if(usermap.get(Integer.valueOf(o.getSendId())) != null){
			       %>
			       <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
			     <%
			      }
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
</tbody>
</table>


     </div>


</body>
</html>
