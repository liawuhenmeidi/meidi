<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%@ include file="searchdynamic.jsp"%>  
<%   
//List<Order> list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.orderDispatching,num,Page,sort,sear);  
//session.setAttribute("exportList", list); 
//count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.orderDispatching,num,Page,sort,sear);  
  
opstatues = OrderPrintln.release;  

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安装网点派工</title>

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
    width:2200px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2200px;
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


<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="../page.jsp">
     <jsp:param name="type" value="<%=Order.porderDispatching%>"/>   
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div> 
 
</div > 
<div style=" height:120px;">
</div>
 <script type="text/javascript">
sort= "andate asc";
var pages = "" ;
var uuid ="<%=id%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";
var usermapstr = <%=usermapstr%>;
var type = "<%=Group.sencondDealsend%>";

$(function () { 
	 fixation();
	// alert(type+"*"+statues+"*"+num+"*"+page+"*"+sort+"*"+sear);
	 initOrder(type,statues,num,page,sort,sear);
});
 
function changes(oid,id,statues,flag,returnstatues,type,printid){
	if(statues == 2){ 
		if(flag == 2){
			alert("请联系送货员驳回");
			return ;      
		}else if(flag == 3){
			alert("请联系安装员驳回");
			return ;
		}else if(flag == 1){
			if(returnstatues == 0){
				alert("请配置退货员拉回商品再同意");
				return ;
			}
		}

	} 
	//alert(id);
		$.ajax({   
	        type: "post", 
	         url: "../../LogisticsServlet", 
	         data:"oid="+oid+"&opid="+id+"&statues="+statues, 
	         dataType: "", 
	         success: function (data) {
	        	
	        	 if(statues == 2){ 
	        		
	        		 if(<%=OrderPrintln.releasedispatch %> != type){

	        				
	        			window.location.href="../printPaigong.jsp?id="+oid+"&type=<%=Order.deliveryStatuesTuihuo%>&uid="+printid;
	        				
	        		}else {
	        			window.location.href="dingdanpeidan2.jsp";
	        		}
	        	 }else {
	        		
	        		 window.location.href="dingdanpeidan2.jsp";
	        	 } 
	        	 
	     
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           }); 
	
}
 // 派工送货员
function change(str1,oid,type,statues,types,saleId){
	
	var uid = $("#"+str1).val();
	var saleid = uuid;
	   if(type == 4 || type == 5){
		   saleid = saleId;
	   }
	   
	if(uid == null || uid == ""){
		alert("请选择送货员");
		return ;
	}
	 
	var branch = usermapstr[saleid].branchName;
	
	if(0 == statues){   
		alert("您已提交驳回申请，不能派工");
	}else {  
		$.ajax({ 
	        type: "post",   
	         url: "../server.jsp",    
	         data:"method=getinventory&types="+types+"&uid="+saleid,
	         dataType: "",  
	         success: function (data) {  
	        	    inventory = data;
	        	    data = data.replace(/{/g, "");
	        	    data = data.replace(/}/g, "");
	        	    data = data.replace(/,/g, "\n");
	        	    if(type == 3){
	        	    	question = confirm("您确定要配单并打印吗？\n");
	        	    }else {
	        	    	question = confirm("您确定要配单并打印吗？\n"+branch+"\n"+data);
	        	    }
				     
				     if (question != "0"){
								$.ajax({   
							        type: "post",     
							         url: "../../LogisticsServlet", 
							         data:"method="+type+"&oid="+oid+"&uid="+uid,
							         dataType: "",  
							         success: function (data) { 
							        	 if(data == 0){
							        		 alert("导购提交修改申请，不能配工");
							        		 return ; 
							        	 }if(data == -1){
							        		 alert("请刷新页面");
							        		 return ; 
							        	 }else if(data == 20){ 
							        		 alert("导购提交退货申请，不能配工");
							        		 return ; 
							        	 }else {   
							        		 window.location.href="../printPaigong.jsp?id="+oid+"&type="+type;
							        	 }   
							        	 
							           },  
							         error: function (XMLHttpRequest, textStatus, errorThrown) { 
							        // alert(errorThrown); 
							            } 
							           });
				        			
			           } 
	         },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           });
	}
}

function adddetail(src){ 
	//window.location.href=src ;
	window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function winconfirm(str,str2,sendid){
	if(sendid != 0){
		alert("请联系送货员驳回");
		return ;
	}else {
		if(4 == str2){
	    	var question = confirm("您的驳回请求别拒绝，是否继续申请？");
				if(question == 0){
					return ;
				}
	    }else if(0 == str2){
	    	alert("您已提交驳回请求");
	    	return ; 
	    }

	}
  //alert(str2);
    
	
    question = confirm("你确认要驳回吗？");
	
	if (question != "0"){

		$.ajax({    
	        type:"post",  
	         url:"../../user/OrderServlet",
	         //data:"method=list_pic&page="+pageCount,      
	         data:"method=shifang&oid="+str+"&pGroupId="+pgroup+"&opstatues="+opstatues,
	         dataType: "",  
	         success: function (data) {    
	          alert("驳回申请已提交成功");  
	          window.location.href="dingdanpeidan2.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("驳回申请失败");
	            } 
	           });
	 }
}
 
function searchlocate(id){  
	  window.location.href="../../adminmap.jsp?id="+id;
}

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}
</script>
<br/> 
 <%@ include file="searchOrderAll.jsp"%>  
    
<div id="wrap">
   <%@ include file="remind.jsp"%>
    
<table  cellspacing="1" id="table">
		<tr id="th">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">顾客信息</td>
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center" >体积</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
           
            <td align="center">预约日期</td>
            <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
           
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			
			
			<td align="center">备注</td>
			<td align="center">销售员</td>
			<td align="center">送货员</td>
			<td align="center">查看位置</td>
			  <td align="center">驳回文员</td>  
			<td align="center">送货员驳回请求</td> 
			 
		    <td align="center">驳回状态</td>
		    <td align="center">文员退货请求</td> 
		    <td align="center">退货员</td>  
		</tr>

</table>


     </div>


</body>
</html>
