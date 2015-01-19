<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%@ include file="../searchdynamic.jsp"%>       
 <%  
 if(StringUtill.isNull(statues)){ 
	 statues = Order.aftersale +"";
 }
    
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>

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
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
         
<jsp:include flush="true" page="page.jsp"> 
	<jsp:param name="type" value="<%=statues%>"/> 
</jsp:include> 

</div > 
<div style="height:100px;">
</div>

<br/>  

<%@ include file="../searchOrderAll.jsp"%>  
 

<script type="text/javascript">

sort= "andate asc";
var id = "";
var type = "<%=Group.aftersalerepare%>";

var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>"; 
var usermapstr = <%=usermapstr%> ;
var inventory = "";  

 
$(function () { 
	 fixation();
	 initOrder(type,statues,num,page,sort,sear);
});

function addImage(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
} 



function searchlocate(id){
	window.open('../adminmap.jsp?id='+id, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

}

 
function adddetail(src){ 
	winPar=window.open(src, 'detail', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');


}

</script>

 
<div id="wrap">
 <%@ include file="../remind.jsp"%> 
<table  cellspacing="1" id="table" >
		<tr id="th">  
		    <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>
			<td align="center">单号</td> 
			<td align="center">顾客信息</td>
			
			<td align="center">设备名称</td>
			<td align="center" >设备型号</td> 
			<td align="center" >设备数量</td>
			
			<td align="center" >批号</td> 
			<td align="center" >条码</td>
			<td align="center">地址</td> 
			<td align="center">单据类型</td>
            <td align="center">安装日期</td>
            <td align="center">预约日期</td>
		</tr>
		 
	      <% 
              String tdcol = "bgcolor=red" ;
	      %>
				
					
				
    
    
</table> 
     </div>

</body>
</html>
