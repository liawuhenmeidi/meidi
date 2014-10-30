<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../searchdynamic.jsp"%>      
<%         
if(searchflag){
	sort= "andate asc";
}
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.neworder,0,0,sort,sear); 
count =  OrderManager.getOrderlistcount(user,Group.dealSend,Order.neworder,0,0,sort,sear); 
session.setAttribute("exportList", list);

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
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px;
}

</style>
</head>

<body style="scoll:no">
  
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
var id = "";
var pgroup = "<%=pgroup%>";
var usermapstr = <%=usermapstr%>;
var opstatues = "<%=opstatues%>"; 
var inventory = "";  
// types   产品型号 
function changepeidan(str1,oid,deliveryStatues,types,saleId){
	var uid = $("#"+str1).val();
	var saleid = $("#"+str1).val();
   if(deliveryStatues == 9 || deliveryStatues == 10 || deliveryStatues == 8){
	   saleid = saleId;
   }

   var branch = usermapstr[saleid].branchName;
   
	if(deliveryStatues != 8 ){ 
		if(uid == null || uid == ""){
			alert("请选择安装公司");
			return ;
		}
		
		$.ajax({ 
	        type: "post",  
	         url: "server.jsp",   
	         data:"method=getinventory&types="+types+"&uid="+saleid,
	         dataType: "",  
	         success: function (data) {  
	        	    inventory = data;
	        	    data = data.replace(/{/g, "");
	        	    data = data.replace(/}/g, "");
	        	    data = data.replace(/,/g, "\n"); 
	               // alert(str);  
	                question = confirm("您确定要配单并打印吗？\n"+branch+":\n"+data);
	        		if (question != "0"){  
	        			//alert(deliveryStatues);
	        			$.ajax({ 
	        		        type: "post", 
	        		         url: "server.jsp",
	        		         data:"method=peidan&id="+oid+"&uid="+uid,
	        		         dataType: "", 
	        		         success: function (data) { 
	        		            if(data == 8){
	        		            	alert("导购修改中。稍后重试"); 
	        		            }else{ 
	        		            	 window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues;  
	        		            }
	        		           },  
	        		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        		            } 
	        		           });
	        		}else {
	        			return ;
	        		}
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });   
		
		
	}else { 
		uid = 0; 
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=peidan&id="+oid+"&uid="+uid,
	         dataType: "", 
	         success: function (data) { 
	            if(data == 8){
	            	alert("导购修改中。稍后重试"); 
	            }else{
	            	if(str1 != 0){ 
	            	   window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues+"&dingma="+str1;  
	            	}else {
	            		window.location.href="dingdan.jsp";	 
	            	}
	            }
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	}
	
}

function addImage(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
} 

function changes(opid,oid,conmited,dealsendid,printlnstateus,Returnstatuse,type,object){
	//$(object).css("display","none"); 
	if( 2 == conmited ){         
		if(type == '<%=OrderPrintln.releasemodfy %>' || type == '<%=OrderPrintln.releasedispatch %>'){
			if(Returnstatuse != 2 ){         
			question = confirm("商品已送货，您不能直接同意，是否联系安装公司驳回");
			if (question != "0"){
				
				if(printlnstateus == 0){   
					alert("您已经提交"); 
				}else {
				$.ajax({     
			        type:"post",  
			         url:"../user/server.jsp",  
			         //data:"method=list_pic&page="+pageCount,       
			         data:"method=shifang&oid="+oid+"&pGroupId="+pgroup+"&opstatues="+type,
			         dataType: "",  
			         success: function (data) {    
			          alert("驳回申请已提交成功"); 
			          window.location.href="dingdan.jsp";
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			          alert("驳回申请失败");
			            } 
			           });
			}
			}
			return ;
			} 
		}

		if(<%=OrderPrintln.salereleaseanzhuang%> == type || <%=OrderPrintln.salereleasesonghuo%> == type
			|| <%=OrderPrintln.release%> == type || <%=OrderPrintln.releasedispatch%> == type && 2 == Returnstatuse || 0 == type)
		   {
		    question = confirm("请先打印");
		
			if (question != "0"){
				var type = "<%=Order.deliveryStatuesTuihuo%>";
				$.ajax({  
			        type: "post", 
			         url: "server.jsp",   
			         data:"method=dingdaned&id="+opid+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
			         dataType: "",  
			         success: function (data) {
			        	 
			        	 if(data == true || data == "true"){ 
			        		 window.location.href="print.jsp?id="+oid+"&type="+type+"&uid="+dealsendid ;
			        	 }
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			            } 
			           });
				
			}else {
				return ;
			} 
			
			
		}else {
			$.ajax({    
		        type: "post", 
		         url: "server.jsp",   
		         data:"method=dingdaned&id="+opid+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
		         dataType: "",   
		         success: function (data) {
		             window.location.href="dingdan.jsp";
		        	
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		            } 
		           });
		}	 
	}else { 
		$.ajax({   
	        type: "post", 
	         url: "server.jsp",   
	         data:"method=dingdaned&id="+opid+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
	         dataType: "",   
	         success: function (data) {
	             window.location.href="dingdan.jsp";
	        	
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	} 
 
		
	
}  

function searchlocate(id){
	window.open('../adminmap.jsp?id='+id, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

}

 
function adddetail(src){ 
	winPar=window.open(src, 'detail', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');


}

</script>
<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="../page.jsp">
    <jsp:param name="sear" value="<%=sear %>" /> 
	<jsp:param name="page" value="<%=Page %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
	<jsp:param name="type" value="<%=Order.orderDispatching %>"/> 
</jsp:include> 

<jsp:include page="../search.jsp">
    <jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
</jsp:include> 
</div > 
<div style="height:140px;">
</div>
<br/>  

<div id="wrap">
<table  cellspacing="1" id="table" >
		<tr id="th">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td> 
			<td align="center">门店</td>
			<td align="center">销售员</td>
			<td align="center">pos(提货)单号</td>
			<td align="center">OMS订单号</td>
			
			<td align="center">验证码(联保单)</td>
			<td align="center">顾客信息</td>
			<td align="center">送货名称</td>
			<td align="center" >送货型号</td> 
			   
			<td align="center" >送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
            <td align="center">开票日期</td>
            
            <td align="center">预约日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
           <td align="center">上报状态</td>
           <td align="center">送货状态</td>
			<td align="center">备注</td>
			 
			<td align="center">配单</td>
			<td align="center">查看位置</td> 
			
		</tr> 
	
  <%  
   if(null != list){
		for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		  %>  
		     
		    <tr id="<%=o.getId()+"ss" %>"  class="asc"  onclick="updateClass(this)">   

				<td align="center"><a href="javascript:void(0)" onclick="adddetail('dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
				<td align="center"><%=o.getbranchName(o.getBranch())%></td>  
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
				<%=OrderManager.getOrderStatues(o) %> 
				</td>
				<td align="center">
				<%=OrderManager.getDeliveryStatues(o) %> 
				</td>
		        <td align="center"> 
				    <%=o.getRemark() %>
				</td>
		 
		       <%  
		        int totalshifang = -1 ; 
		        OrderPrintln orp = null ; 
		        OrderPrintln op = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.modify, o.getId()) ;
		        OrderPrintln op1 = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.returns, o.getId()) ;
		        OrderPrintln huanhuoo = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.huanhuo, o.getId()) ;
		        OrderPrintln huanhuoObject = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.huanhuo, o.getId()) ;
		        
		        int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
			    int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());
			    int huanhuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.huanhuo, o.getId());
			    int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId());
			    int salereleasesonghuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleasesonghuo, o.getId());
			    int release	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId());
			    int salereleaseanzhuang	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleaseanzhuang, o.getId());
			    int releasemodfy	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasemodfy, o.getId());
			    int type = -1 ;
			    if(release != -1){   
			    	totalshifang = release ;
			    	orp = opmap.get(OrderPrintln.release).get(o.getId()); 
			    	
			    } 
			    if(salereleasesonghuo != -1){
			    	totalshifang = salereleasesonghuo ;
			    	orp = opmap.get(OrderPrintln.salereleasesonghuo).get(o.getId()); 
			    }
			    if(salereleaseanzhuang != -1){
			    	totalshifang = salereleaseanzhuang ;
			    	orp = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()); 
			    }
		       %>
		 
				<td align="center"> 
				
				 <%    	  
				   if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) != 8 && o.getPrintSatues() == 0){
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
				         <input type="button" onclick="changepeidan('songh<%=o.getId()%>','<%=o.getId()%>','<%=Integer.valueOf(o.getOderStatus()) %>','<%=o.getSendType(0,"</p>")%>','<%=o.getSaleID() %>')"  value="确定"/> 
				         
					<% 	
				     }
				   }else if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) == 8 && o.getPrintSatues() == 0){
				
					   if(OrderManager.Check(o.getId())){ 
						  %>   
						   <input type="button" onclick="changepeidan('2','<%=o.getId()%>','<%=Integer.valueOf(o.getOderStatus()) %>','<%=o.getSendType(0,"</p>")%>','<%=o.getSaleID() %>')"  value="打印"/>
					         &nbsp;&nbsp;&nbsp;
						  <%
					   }else {
						   %>
						   <input type="button" onclick="changepeidan('1','<%=o.getId()%>','<%=Integer.valueOf(o.getOderStatus()) %>','<%=o.getSendType(0,"</p>")%>','<%=o.getSaleID() %>')"  value="打印"/>
					         &nbsp;&nbsp;&nbsp;
						   <input type="button" onclick="changepeidan('0','<%=o.getId()%>','<%=Integer.valueOf(o.getOderStatus()) %>','<%=o.getSendType(0,"</p>")%>','<%=o.getSaleID() %>')"  value="确定"/>  
						   				   
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
      }
    %>
    
    
    
</table> 
     </div>

</body>
</html>
