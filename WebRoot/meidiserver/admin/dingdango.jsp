<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%> 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>厂送票未消</title>

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
#table{  
    width:2300px;
    table-layout:fixed ;
}
#th{
    
    position:absolute;
    width:2300px;
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
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
var id = "";
var type = "<%=Group.dealSend%>";
 
$(function () { 
	fixation();
	 initOrder(type,statues,num,page,sort,sear); 
});

function serch(){
	 var search = $("#search").val();
	 var serchProperty = $("#serchProperty").val();
	 
	 window.location.href="dingdango.jsp?pages="+pages+"&munb="+num+"search="+search+"&serchProperty="+serchProperty;

	
}


function winconfirm(statues){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox'][id='check_box']").each(function(){          
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
	         data:"method=orderGo&id="+attract.toString()+"&statues="+statues,
	         dataType: "", 
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("执行失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("执行成功");
	        		  window.location.href="dingdango.jsp";
	        	 };	  
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("执行失败"); 
	            } 
	           });
		
		
	}
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
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="page.jsp">
	<jsp:param name="type" value="<%=Order.go%>"/> 
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

<%
if(UserManager.checkPermissions(user, Group.go)){
	%>
	<div class="btn">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm('1')"></input> 
  &nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="返回上一级状态" onclick="winconfirm('0')"></input> 
</div>
	<%} %>


</div > 
<div style=" height:130px;">
</div>
<br/>  
<%@ include file="searchOrderAll.jsp"%>
  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
			
			<td align="center">pos(厂送)单号</td>
			<td align="center">OMS订单号</td>
			<td align="center">验证码(联保单)</td>
			
			 <td align="center">开票日期</td>
			<td align="center">票面名称</td>
			<td align="center">票面型号</td>
			<td align="center">票面数量</td>
			
			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			
			<td align="center">赠品状态</td>
			<td align="center">门店</td> 
			<td align="center">销售员</td>
			<td align="center">顾客信息</td>
           
            <td align="center">预约日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            
            <td align="center">送货状态</td>

			
			<td align="center">备注</td>
		</tr>
</table>
</div>

</body>
</html>
