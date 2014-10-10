<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="searchdynamic.jsp"%>    
<%       

List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.orderDispatching,num,Page,sort,sear);

session.setAttribute("exportList", list);

count = OrderManager.getOrderlistcount(user,Group.dealSend,Order.orderDispatching,num,Page,sort,sear);    
   
HashMap<Integer,User> usermap = UserManager.getMap();
        
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(user ,Group.sencondDealsend);   
 
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  
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

td { 
    width:100px;
    line-height:15px;
}
 
#table{  
    width:2400px;
    table-layout:fixed ;
}

#th{  
    background-color:white;
    position:absolute; 
    width:2400px; 
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
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
var id = "";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>"; 
var inventory = "";
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
     
     
function getinventory(uid,types){
	$.ajax({ 
        type: "post",  
         url: "server.jsp",   
         data:"method=getinventory&types="+types+"&uid="+uid,
         dataType: "",  
         success: function (data) { 
        	 inventory = data; 
           alert(data);
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });  
}     
      
function changepeidan(str1,oid,deliveryStatues,types){
	var uid = $("#"+str1).val();
	
	if(deliveryStatues != 8 ){ 
		if(uid == null || uid == ""){
			alert("请选择安装公司");
			return ;
		}
		
		$.ajax({ 
	        type: "post",  
	         url: "server.jsp",   
	         data:"method=getinventory&types="+types+"&uid="+uid,
	         dataType: "",  
	         success: function (data) {  
	        	    inventory = data;
	        	    data = data.replace(/{/g, "");
	        	    data = data.replace(/}/g, "");
	        	    data = data.replace(/,/g, "\n"); 
	               // alert(str);  
	                question = confirm("您确定要配单并打印吗？\n"+data);
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

function changes(str1,oid,conmited,dealsendid,printlnstateus,Returnstatuse,type){
	//alert(dealsendid); 
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
			|| <%=OrderPrintln.release%> == type || <%=OrderPrintln.releasedispatch%> == type && 2 == Returnstatuse)
		   {
		    question = confirm("请先打印");
		
			if (question != "0"){
				var type = "<%=Order.deliveryStatuesTuihuo%>";
				$.ajax({  
			        type: "post", 
			         url: "server.jsp",   
			         data:"method=dingdaned&id="+str1+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
			         dataType: "",  
			         success: function (data) {
			        	 
			        	 if(data == true || data == "true"){ 
			        		 window.location.href="print.jsp?id="+oid+"&type="+type ;
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
		         data:"method=dingdaned&id="+str1+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
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
	         data:"method=dingdaned&id="+str1+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
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
	//window.location.href=src ;
	winPar=window.open(src, 'detail', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}

</script>
<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:70%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include> 
      
<jsp:include flush="true" page="page.jsp">
    <jsp:param name="sear" value="<%=sear %>" /> 
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
	<jsp:param name="type" value="<%=Order.orderDispatching %>"/> 
</jsp:include> 


<jsp:include page="search.jsp">
 <jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
</jsp:include> 
</div >
 
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
           <td align="center">送货状态</td>
			<td align="center">备注</td>
			 
			<td align="center">配单</td>
			<td align="center">查看位置</td> 
			<td align="center">安装单位驳回</td> 
			<td align="center">导购修改申请</td> 
			<td align="center">导购退货申请</td> 
           
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
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>
        <td align="center"> 
		    <%=o.getRemark() %>
		</td>
 
       <%  
        int totalshifang = -1 ; 
        OrderPrintln orp = null ; 
        OrderPrintln op = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.modify, o.getId()) ;
        OrderPrintln op1 = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.returns, o.getId()) ;
         
        int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
	    int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());
	    int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId());
	    int salereleasesonghuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleasesonghuo, o.getId());
	    int release	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId());
	    int salereleaseanzhuang	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleaseanzhuang, o.getId());
	    int releasemodfy	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasemodfy, o.getId());
	    
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
		
		<td align="center"> 
		<%    
		    if(modify == -1 && returns == -1 )
			 if(totalshifang == 2){ 
		    	 %> 
		    	  <%=orp== null ?"":orp.getMessage()  %>
		    	   <p>驳回申请已同意</p>
		    	 <%
		    	  }else if(totalshifang == 4){ 
				 %> 
				    	 
				 <p>驳回申请已拒绝</p> 
			    <%
				}else if(totalshifang != -1){
			 %> 
		  
		 <%=orp== null ?"":orp.getMessage() %> 
		    	       
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','','','<%=totalshifang %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','','','<%=totalshifang%>')"  value="不同意"/>
		<%
		   }   
		%>
		</td> 
		<td align="center"> 
		<%
			 if(modify == 2){ 
		    	 %> 
		    	   <p>导购修改中</p>  
		    	   
		    	 <%
		    	  }else if(modify == 4){ 
				    	 %> 
				    	   <p>修改申请已拒绝</p>
				    	 <% 	 
				  }else if(modify != -1){	  
			 %>
		   
		 <%=op == null ? "":op.getMessage() %>  
		 <% if(releasemodfy == 0 || totalshifang== 0 || totalshifang == 4){
			 
		  %> 
		   安装公司处理中 
		 <%}else if(releasemodfy == 2 || totalshifang == 2){  
		 %>
		 <input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=releasemodfy %>','','<%=OrderPrintln.modify %>')"  value="同意"/>
	      
		<%}else if(releasemodfy == -1){ %> 
		<input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=releasemodfy %>','','<%=OrderPrintln.releasemodfy %>')"  value="同意"/>
		<input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','<%=releasemodfy %>','','<%=OrderPrintln.releasemodfy %>')"  value="不同意"/>
		<% }%>
		 
		</td>
		<%
		 } 
		%>
		<td align="center"> 
		<% 
			 if(returns == 2){ 
		    	 %> 
		    	   <p>退货申请已同意</p>
		    	 <% 
		    	  }else if(returns == 4){ 
				     	
				    	 %> 
				    	   <p>退货申请已拒绝</p>
				    	 <%
				    	 
				   }else if(returns != -1){
			 %>
		 <%=op1.getMessage() %>
		  <%if(releasedispatch == 0 ){
		   %>    
		   安装公司处理中 
		  <% } else if(releasedispatch == 2 ){%>
		  
		  <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=releasedispatch %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意退货"/>
		   
		  <%}else {
			  if(o.getDeliveryStatues() == 8){
				  %>
		     <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=releasedispatch %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.release %>')"  value="打印"/>
		     <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=releasedispatch %>','<%=o.getReturnstatuse() %>','')"  value="确定"/>  

				  <%
			  }else {
			  %>	         
		 <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=releasedispatch %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','<%=releasedispatch %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="不同意"/> 
		
		<% }
		}%>
		</td>
		<%
		 }
		}
		%>
     
     
     
    </tr>
  
    <% 
    }%>
    
    
    
</table> 
     </div>

</body>
</html>
