<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%@ include file="searchdynamic.jsp"%>       
 <% 
 if(StringUtill.isNull(statues)){
	 statues = Order.orderDispatching +"";
 }

%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style type="text/css">
.fixedHead {  
position:fixed;
}  
 
*{
    margin:0;
    padding:0;
}
#table{  
    width:2700px;
    table-layout:fixed ;
}

#th{  
    background-color:white;
    position:absolute; 
    width:2700px; 
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

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
         
<jsp:include flush="true" page="page.jsp"> 
	<jsp:param name="type" value="<%=statues%>"/> 
</jsp:include> 
 
<div id="headremind"> 
<jsp:include page="headremind.jsp"/>
</div> 

</div > 
<div style="height:120px;">
</div>

<br/>  

<%@ include file="searchOrderAll.jsp"%>  
 

<script type="text/javascript">

sort= "andate asc";
var id = "";
var type = "<%=Group.dealSend%>";

var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>"; 
var usermapstr = <%=usermapstr%> ;
var inventory = "";  
// types   产品型号 
 
 
$(function () { 
	 fixation();
	 initOrder(type,statues,num,page,sort,sear);
});


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
	              
	                question = confirm("您确定要配单并打印吗？\n"+branch+":\n"+data);
	        		if (question != "0"){  
	        			//alert(deliveryStatues);
	        			$.ajax({ 
	        		        type: "post", 
	        		         url: "../LogisticsServlet",
	        		         data:"method=peidan&oid="+oid+"&uid="+uid,
	        		         dataType: "", 
	        		         success: function (data) {
	        		            if(data == 8){
	        		            	alert("导购修改中。稍后重试"); 
	        		            }else if(data == -1){ 
	        		            	alert("请刷新页面");
	        		            }else{ 
	        		            	//window.open('print.jsp?id='+oid+'&deliveryStatues='+deliveryStatues, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
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
	         url: "../LogisticsServlet",
	         data:"method=peidan&oid="+oid+"&uid="+uid,
	         dataType: "",  
	         success: function (data) { 
	            if(data == 8){
	            	alert("导购修改中。稍后重试"); 
	            }else{
	            	if(str1 != 0){ 
	            	   window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues+"&dingma="+str1;  
	            	   //window.open('print.jsp?id='+oid+'&deliveryStatues='+deliveryStatues+'&dingma='+str1, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
	            	}else {
	            		initOrder(type,statues,num,page,sort,sear);	 
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

function changes(opid,oid,conmited,dealsendid,printlnstateus,Returnstatuse,typesearch,object){
	//$(object).css("display","none"); 
	if( 2 == conmited ){         
		if(typesearch == '<%=OrderPrintln.releasemodfy %>' || typesearch == '<%=OrderPrintln.releasedispatch %>'){
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
			         data:"method=shifang&oid="+oid+"&pGroupId="+pgroup+"&opstatues="+typesearch,
			         dataType: "",  
			         success: function (data) {    
			          alert("驳回申请已提交成功"); 
			          initOrder(type,statues,num,page,sort,sear);
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

		if(<%=OrderPrintln.salereleaseanzhuang%> == typesearch || <%=OrderPrintln.salereleasesonghuo%> == typesearch
			|| <%=OrderPrintln.release%> == typesearch || <%=OrderPrintln.releasedispatch%> == typesearch && 2 == Returnstatuse || 0 == typesearch)
		   {
		    question = confirm("请先打印");
			if (question != "0"){
				var typesearch = "<%=Order.deliveryStatuesTuihuo%>";
				$.ajax({  
			        type: "post", 
			         url: "../LogisticsServlet",   
			         data:"opid="+opid+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
			         dataType: "",  
			         success: function (data) {
			        	  
			        	 if(data == true || data == "true"){ 
			        		 window.location.href="print.jsp?id="+oid+"&type="+typesearch+"&uid="+dealsendid ;
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
		         url: "../LogisticsServlet",   
		         data:"opid="+opid+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
		         dataType: "",   
		         success: function (data) {
		        	 initOrder(type,statues,num,page,sort,sear);
		        	
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		            } 
		           });
		}	 
	}else { 
		$.ajax({   
	        type: "post", 
	         url: "../LogisticsServlet",   
	         data:"opid="+opid+"&oid="+oid+"&statues="+conmited+"&uid="+dealsendid,  
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
			<td align="center" >零售价</td> 
			<td align="center" >体积</td>
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
			<td align="center">安装网点驳回</td> 
			<td align="center">导购退货申请</td> 
            <td align="center">导购换货申请</td> 
		</tr> 
	
  
    
    
</table> 
     </div>

</body>
</html>
