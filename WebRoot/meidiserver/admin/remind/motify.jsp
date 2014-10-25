<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../searchdynamic.jsp"%> 
<%    
 
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.motify ,0,0,"id",sear); 
count =   OrderManager.getOrderlistcount(user,Group.dealSend,Order.motify ,0,0,"id",sear);  

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

//Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.modify);
// 退货申请
//Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.returns); 
  
//Map<Integer,OrderPrintln> opMap2 = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.release);
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
var pgroup = "<%=pgroup%>";

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
		 window.location.href="motify.jsp?pages="+pages+"numb="+num;
	 });

	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="motify.jsp?page="+pages+"&numb="+num;
	 }); 
	  
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="motify.jsp?page="+pages+"&numb="+num+"&sort="+sort;
	 }); 
}); 
  
function changepeidan(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
          if(data = 0) {
        	 alert("订单已打印，不能配单");  
          }	 
           alert("设置成功"); 
           window.location.href="motify.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
}

function changes(str1,str2,str3,str4,str5,str6,type){
	  
	if( 2 == str3 ){        
		if(0 != str4){
			if(str6 != 2 ){    
			question = confirm("商品已送货，您不能直接同意，是否联系安装公司释放");
			if (question != "0"){
				
				if(str5 == 0){   
					alert("您已经提交"); 
				}else {
				$.ajax({     
			        type:"post",  
			         url:"../../user/server.jsp",  
			         //data:"method=list_pic&page="+pageCount,       
			         data:"method=shifang&oid="+str2+"&pGroupId="+pgroup+"&opstatues="+type,
			         dataType: "",  
			         success: function (data) {    
			          alert("释放申请已提交成功"); 
			          //window.location.href="dingdan.jsp";
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			          alert("释放申请失败");
			            } 
			           });
			} 
			}
			return ;
			} 
		}
	
	if(<%=OrderPrintln.salereleaseanzhuang%> == type || <%=OrderPrintln.salereleasesonghuo%> == type || <%=OrderPrintln.release%> == type || <%=OrderPrintln.releasedispatch%> == type && 2 == str6){
	question = confirm("请先打印");
	
	if (question != "0"){
		var type = "<%=Order.deliveryStatuesTuihuo%>"; 
		window.location.href="../print.jsp?id="+str2+"&type="+type ;
	}else {
		return ; 
	} 
	}	 
	} 
		$.ajax({ 
	        type: "post", 
	         url: "../server.jsp",   
	         data:"method=dingdaned&id="+str1+"&oid="+str2+"&statues="+str3,  
	         dataType: "",  
	         success: function (data) {
	           window.location.href="motify.jsp";
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
			<td align="center">导购修改申请</td> 
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
				<%=OrderManager.getDeliveryStatues(o) %>
		</td> 

        <td align="center"> 
		    <%=o.getRemark() %>
		</td>
         
         <%
         OrderPrintln op = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.modify, o.getId()) ;
         int totalshifang = -1 ; 
	     OrderPrintln orp = null ; 
         int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
         int releasemodfy	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasemodfy, o.getId());
         int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId());
		    int salereleasesonghuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleasesonghuo, o.getId());
		    int release	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId());
		    int salereleaseanzhuang	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleaseanzhuang, o.getId());
         
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
    </tr>

    <% 
    }
    }%>
</table> 
     </div>

</body>
</html>
