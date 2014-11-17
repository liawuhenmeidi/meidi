<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>
  
<%   

//List<Order> list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.orderquery,num,Page,sort,sear);  
//session.setAttribute("exportList", list); 
//count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.orderquery,num,Page,sort,sear);  

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>送货确认页</title>

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
    width:2600px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2600px; 
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

function change(str1,str2,type){
	var statues = $("#"+str1).val(); 
	//alert(uid);
	$.ajax({   
        type: "post",    
         url: "../../LogisticsServlet", 
         data:"method="+type+"&oid="+str2+"&statues="+statues,
         dataType: "",    
         success: function (date) {
        	//alert(date);
        	 if(date == 0){
        		 alert("导购提交修改申请，不能配工");
        		 return ; 
        	 }else if(date == 20){  
        		 alert("导购提交退货申请，不能配工");
        		 return ;
        	 } else{
        		 alert("设置成功"); 
  	        	   window.location.href="dingdanquery.jsp";
        	 }
           
           
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}



//function winconfirm(statues,uid,oid){
	//$.ajax({   
  //      type: "post",   
  //       url: "../server.jsp", 
  //       data:"method=dealshifang&statues="+statues+"&oid="+oid+"&uid="+uid,
  //       dataType: "",   
  //       success: function (date) { 
        	
    //    	 window.location.href="dingdanquery.jsp";
           
    //       },  
    //     error: function (XMLHttpRequest, textStatus, errorThrown) { 
    // 
    //        } 
    //       });
//}
 
function winconfirm(statues,uid,oid,opstatues){
	if(opstatues == 0 ){
		alert("您已经提交释放申请");
		return ;
	}else if(opstatues == 4){
		question = confirm("您的申请被拒绝，是否继续提交？");
	}else { 
		question = confirm("你确认要释放吗？");
	}
		
		if (question != "0"){
			
			//alert(attract.toString());
			$.ajax({    
		        type:"post", 
		         url:"../../user/server.jsp",   
		         //data:"method=list_pic&page="+pageCount,
		        data:"method=shifang&oid="+oid+"&pGroupId="+uid+"&opstatues="+statues,
		         dataType: "",   
		         success: function (data) {  
		          alert("释放申请已提交成功");    
		          window.location.href="dingdanquery.jsp?id="+id;
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		          alert("释放申请失败");
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
           window.location.href="../printPaigong.jsp?id="+id;
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           }); 
}

</script>

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="../page.jsp">
    <jsp:param name="type" value="<%=Order.orderquery%>"/> 
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>
</div > 
<div style=" height:120px;">
</div>
 
<br/> 
<%@ include file="searchOrderAll.jsp"%>  
<div id="wrap"> 
<table  cellspacing="1" id="table">
		<tr id="th">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  
			
			<td align="center"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>-->
			<td align="center">单号</td>
			<td align="center">送货员</td>
			<td align="center">送货时间</td>
			<td align="center">安装员</td>
			<td align="center">安装时间</td> 
			<td align="center">释放</td>
			<td align="center">操作</td>
			<td align="center">退货员</td> 
			<td align="center">退货</td> 
			<td align="center">送货状态</td> 
			
			<td align="center">门店</td>
			<td align="center">验证码(联保单)</td>
			<td align="center">销售员</td>
			<td align="center">顾客信息</td>
			
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
			
            <td align="center">预约日期</td> 
           <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
			<td align="center">打印状态</td>
			
			<td align="center">打印</td> 
			
			<td align="center">备注</td> 
			
			<td align="center">送货是否已结款</td>
			
			<td align="center">先送货后安装</td>
			<td align="center">是否已回访</td>
            <td align="center">安装是否已结款</td>
		</tr>

</table>


     </div>


</body>
</html>
