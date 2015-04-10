<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货订单</title>

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
var realoid;   
function serch(){
	 var search = $("#search").val();
	 var serchProperty = $("#serchProperty").val();
	 
	 window.location.href="dingdanTuihuo.jsp?pages="+pages+"&numb="+num+"search="+search+"&serchProperty="+serchProperty;

	
}

$(function () { 
	fixation();
	initOrder(type,statues,num,page,sort,sear);	 
});


function saveAddPOD(){
	 $("#addpos").css("display","none");
	 
    var realpos = $("#realpos").val();
    var saledate = $("#saledate").val();
    if( null == realpos || realpos == "" || saledate == "" || null == saledate){
		 return ; 
	 }
	$.ajax({  
        type: "post", 
         url: "../user/OrderServlet",
         data:"method=saveTuihuo&orderid="+realoid+"&realpos="+realpos+"&saledate="+saledate,
         dataType: "", 
         success: function (data) {
        	 
        	 initOrder(type,statues,num,page,sort,sear);	 
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

function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox'][id='check_box']").each(function(){          
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
	         url: "../user/OrderServlet", 
	         data:"method=cancelTuihuo&orderid="+attract.toString(),
	         dataType: "",   
	         success: function (data) {
	        	 initOrder(type,statues,num,page,sort,sear);	 
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("操作失败"); 
	            } 
	           });
	}
}

function AddPOS(printid,oid){
	realoid = oid ;
	$("#addprintid").text("单号:"+printid);
	$("#addpos").css("display","block");
}   
</script>
 
 
 <div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="page.jsp">
    
	 <jsp:param name="type" value="<%=Order.deliveryStatuesTuihuo %>"/> 
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>

</div>
<%  
//System.out.println(UserManager.checkPermissions(user, Group.tuihuo,"q")+user.getPositions());
if(UserManager.checkPermissions(user, Group.tuihuo,"q")){
	%>   
	<div class="btn"> 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="忽略确认" onclick="winconfirm()"></input>  
</div>
	<%
} %>


</div > 
<div style=" height:120px;">
</div>
<br/>  
<%@ include file="searchOrderAll.jsp"%>




<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">
		    <td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>  
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">销售员</td>   
			<td align="center">pos(厂送)单号</td>
			<td align="center">OMS订单号</td>
			<td align="center">验证码(联保单)</td>
			
			<td align="center">顾客信息</td>
			<td align="center">票面名称</td>
			<td align="center">票面型号</td>
			<td align="center">票面数量</td>
			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td> 
            <td align="center">开票日期</td>  
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
			<td align="center">备注</td>
		</tr>
</table>
</div> 

<div id="addpos" style="display:none"> 
<div style="position:fixed;text-align:center; top:65%;background-color:white; left:30%; margin:-20% 0 0 -20%; height:30%; width:50%; z-index:999;">
<br/>
<table   cellspacing="1" style="margin:auto;background-color:black; width:80%;height:80%;">  
        <tr class="bsc">
		<td align="center" colspan=2>
		  <label id="addprintid"></label>
		</td> 	
		</tr> 
		<tr class="bsc">
		<td align="center" >
		   pos单号:<input type="text" id="realpos"  placeholder="必填"/>
		</td>
		<td align="center" >
		   退货日期:<input type="text" id="saledate" onclick="new Calendar().show(this);" placeholder="必填"/>
		</td>	 
		</tr> 
		<% if(UserManager.checkPermissions(user, Group.tuihuo,"q")){%>
		<tr class="bsc">
		   
		    <td class="center" ><input type="button" onclick="$('#addpos').css('display','none');"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="取消" /></td>
		
			<td class="center" ><input type="button" onclick="saveAddPOD()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="确定" /></td>
		</tr>
	<%} %>
</table> 
</div>
</div>
 
</body>
</html>
