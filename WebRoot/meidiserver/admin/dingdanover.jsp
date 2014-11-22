<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>  
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安装网点结款页</title>

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
    width:1100px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:1100px;
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
  

 
function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox']").each(function(){          
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
	         data:"method=orderover&id="+attract.toString(),
	         dataType: "", 
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("执行失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("执行成功");
	        		  window.location.href="dingdanover.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("执行失败"); 
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
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="page.jsp">
    
	<jsp:param name="type" value="<%=Order.over %>"/>
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div> 

<div class="btn">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input> 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" value="设置标准" onclick="amortization('salesmoney.jsp')" ></input> 
</div>

</div > 
<div style=" height:130px;">
</div>
<br/> 
 <%@ include file="searchOrderAll.jsp"%>
 
  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th" >  
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
			<td align="center">安装网点</td>
			<td align="center">顾客信息</td>

			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
            
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">备注</td>
			<td align="center">结款金额</td>
		</tr>

</table> 

     </div>

</body>
</html>
