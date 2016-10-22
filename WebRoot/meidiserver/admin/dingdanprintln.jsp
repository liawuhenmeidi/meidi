<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>
  
<%  
long start = System.currentTimeMillis();
 
if(StringUtill.isNull(statues)){
	 statues = Order.serach+"";
}  
  

%> 
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  
<title>查看订单页</title>  
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
    
     width:4000px;
     table-layout:fixed ;
} 
 
#th{
    background-color:white;
    position:absolute;
    width:4000px;
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

var type = "<%=Group.dealSend%>";

$(function () { 
	fixation();
	initOrder(type,statues,num,page,sort,sear); 
});





function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}

function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.name; 
	   				
	   				if(str != null  &&  str != ""){
		   				   attract[i] = str; 
			   	            i++;
		   				}	
	   		}
	   	}); 


		$.ajax({ 
	        type: "post", 
	         url: "server.jsp", 
	         data:"method=deleOrder&id="+attract.toString(),
	         dataType: "",    
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;   
	        	 }else if (data > 0){
	        		  alert("共删除"+data+"条数据");  
	        		  window.location.href="dingdanprintln.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("删除失败"); 
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
function orderPrint(id,statues,type,deliveryStatues){ 
	 if(statues ==0){
		 alert("不能打印未打印订单，请去打印页打印");
	 }else{    
		 window.location.href="print.jsp?id="+id+"&type="+type+"&deliveryStatues="+deliveryStatues; 
	 } 
} 
</script>
 
 <div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>    
      
<jsp:include flush="true" page="page.jsp">
	<jsp:param name="type" value="<%= statues%>"/>  
</jsp:include> 
  
<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div> 

<div class="btn"> 
 <% if(UserManager.checkPermissions(user, Group.Manger)){
	
 %> 
  
 <div class="btn">
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  
</div>
 
<%
}
%>
</div>

</div > 
<div style=" height:130px;">
</div>
<br/>  

 <%@ include file="searchOrderAll.jsp"%>
  
<div id="wrap">
<table  cellspacing="1" id="table" >
		<tr id="th">  
			<!-- <td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>  -->
			<% if(UserManager.checkPermissions(user, Group.Manger)){ %>
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<%} %>
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">所属公司</td>
			<td align="center">销售员</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">OMS订单号</td>
			
			<td align="center">验证码(联保单)</td>
			<td align="center">顾客信息</td>
			<td align="center">票面名称</td>
			<td align="center">票面型号</td>
			<td align="center">票面数量</td>
			<td align="center" >零售价</td> 
			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center" >零售价</td> 
			<td align="center" >体积</td>
			<td align="center">赠品</td>	
			<td align="center">赠品数量</td>
			
			<td align="center">赠品状态</td>
            <td align="center">开票日期</td>
            <td align="center">预约日期</td>
            <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            
            <td align="center">送货地址</td>
             <td align="center">上报状态</td>
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			<td align="center">送货人员</td> 
			
			 <td align="center">送货时间</td>
			 <td align="center">安装人员</td>
			 <td align="center">安装时间</td>
			<td align="center">安装网点</td>
			<td align="center">安装网点结款</td>
			
			<td align="center">厂送票是否已回</td>
			<td align="center">厂送票是否已消</td>
			<td align="center">厂送票是否结款</td>
			<td align="center">备注</td> 
			<td align="center">打印</td>
			  
			<td align="center">安装网点驳回</td> 
			<td align="center">导购退货申请</td>   
		</tr>
	
</table>
     </div>
     
    
</body>
</html>
