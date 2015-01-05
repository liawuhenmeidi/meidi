<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>
  
<%  

//List<Order> list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.pcharge,num,Page,sort,sear);  
//session.setAttribute("exportList", list); 
//count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.pcharge,num,Page,sort,sear);  

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>送货结款</title>

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
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
var id = "";
var pages = "" ;
var num = "";
var oid ="<%=id%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";
var type = "<%=Group.sencondDealsend%>";

$(function () { 
	 fixation();
	// alert(type+"*"+statues+"*"+num+"*"+page+"*"+sort+"*"+sear);
	 initOrder(type,statues,num,page,sort,sear);
});

function func(str){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
}
function funcc(str,str2){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
	$.ajax({  
        type: "post", 
         url: "../server.jsp", 
         data:"method=dingdan&id="+str2,
         dataType: "", 
         success: function (data) {
            // window.location.href="senddingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}
 
function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="dingdanpeidan2.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function change(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post",    
         url: "../../user/server.jsp",
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功");  
           window.location.href="dingdanpeidan2.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}
 
function winconfirm(){
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
	         url: "../server.jsp", 
	         data:"method=statuespaigong&id="+attract.toString(),
	         dataType: "",   
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("操作成功"); 
	        		  window.location.href="dingdan_chargep.jsp";
	        	 };	  
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("服务器忙，请稍后重试"); 
	            }  
	           });
	}
}

function orderPrint(id,statues){
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=print2&id="+id+"&statues="+statues,
         dataType: "",  
         success: function (data) {  
           window.location.href="printPaigong.jsp?id="+id;
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

function amortization(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:1000px;dialogTop:0px;dialogLeft:center;scroll:no');
} 
</script>

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>    
      
<jsp:include flush="true" page="../page.jsp">
   
    <jsp:param name="type" value="<%=Order.pchargepaisong%>"/>  
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

<div class="btn">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input> 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" value="设置标准" onclick="amortization('../salesmoney.jsp?chargetype=<%=BasicUtill.send %>')" ></input> 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
 <input type="submit" class="button" value="保存" onclick="save('sendcharge')" ></input>

</div>

</div >  
<div style=" height:130px;">
</div> 
  <%@ include file="searchOrderAll.jsp"%>  
 
<br/>  
<div id="wrap">
 <%@ include file="remind.jsp"%>
<table  cellspacing="1" id="table">
		<tr id="th">  
			
			<td align="center"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">销售员</td>
			<td align="center">顾客信息</td>
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
          
            <td align="center">预约日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			
			<td align="center">备注</td>
			<td align="center">送货人员</td>
			<td align="center">送货时间</td>
			<td align="center">安装人员</td>
			 <td align="center">安装时间</td>
			 <td align="center">先送货后安装</td>
		   <td align="center">结款金额</td>
		</tr>
	

</table>


     </div>


</body>
</html>
