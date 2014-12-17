<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>  
 <%
 if(StringUtill.isNull(statues)){
	 statues = Order.callback+"";
 }

%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客服未回访页</title> 

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
 <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>

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
     width:1800px; 
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:1800px;
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



<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="page.jsp">
  
	<jsp:param name="type" value="<%=Order.callback%>"/> 
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>
<% if(UserManager.checkPermissions(user, Group.callback,"w")){
 %>
 <div class="btn">
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input>  
</div>
<% 	
}%>

</div > 
<div style=" height:130px;">
</div>
<br/>  
 
 <%@ include file="searchOrderAll.jsp"%>
 


<script type="text/javascript">
var type = "<%=Group.dealSend%>";
var id = "";
var oid ="<%=id%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";

$(function () { 
	fixation();
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
	         url: "server.jsp", 
	         data:"method=wenyuancallback&id="+attract.toString(),
	         dataType: "",   
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("操作成功"); 
	        		  window.location.href="dingdancallback.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("服务器忙，请稍后重试"); 
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

  
</script>


<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
			 
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
			<td align="center">门店</td>
			
			<td align="center">顾客信息</td>
			<td align="center">送货状态</td>
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
            
            <td align="center">预约日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">销售员</td>
            
			
			<td align="center">备注</td>
			<td align="center">送货人员</td>
			<td align="center">送货时间</td>
			<td align="center">安装人员</td> 
			<td align="center">安装时间</td> 
		  
		     
		</tr>

  
</table>


     </div>



</body>
</html>
