<%@ page language="java"  import="java.util.*,gift.*,orderPrint.*,category.*,group.*,user.*,utill.*,product.*,order.*,orderproduct.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
String id = request.getParameter("id");
String type = request.getParameter("type");
String statues = request.getParameter("statues"); 
Order or = OrderManager.getOrderID(user, Integer.valueOf(id));
String str = or.getSendTypejson(0);

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单详细页</title>
 
<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 


<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
 
<script type="text/javascript">

var oid = "<%=id%>";
var realop = <%=str%>;  
function quxiao(){
	window.location.href="dingdanDetailsonghuo.jsp?id="+oid;
}

function dochange(statues,oid,type,json){
	$.ajax({   
        type: "post",     
         url: "../LogisticsServlet", 
         data:"method="+type+"&oid="+oid+"&statues="+statues+"&json="+json,
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
  	           if(type == 21){ 
  	        	   window.location.href="tuihuo.jsp";
  	           }else {
  	        	   window.location.href="songhuo.jsp";
  	           } 
        	 }
           
           
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function saveAddPOD(){
	var json = '[';
	 var type = $("#type").val();
	 var statues = $("#statues").val();
	 for(var i=0;i<realop.length;i++){
			var opp = realop[i]; 
			if(opp != ""){
				var id = opp.id;
				var barcode = $("#barcode"+id).val();
				var batchnumber = $("#batchnumber"+id).val();
				   if( null != barcode && barcode != "" && batchnumber != "" && null != batchnumber){
					   json += '{"id":"'+id+'","barcode":"'+barcode+'","batchnumber":"'+batchnumber+'"},';
                       
					 }
				   
			} 
	 }
   json = json.substring(0, json.length-1)+"]";
   dochange(statues,oid,type,json);
   
}

</script>
</head>

<body>
 
<!--  头 单种类  -->

<!--  订单详情  -->

<div class="s_main_box">
<input type="hidden" id="type" value="<%=type%>"></input> 
<input type="hidden" id="statues" value="<%=statues%>"></input>
<table width="100%" class="s_main_table">

		<tr class="bsc" > 
		   <td align="center" colspan=2>
		     <table >
		     <tr>
		     <td>
		     单号：<%=or.getPrintlnid() %>
		     </td>
		     </tr>
		     <% 
		     List<OrderProduct> list = or.getOrderproduct();
		     if(null != list && list.size() >0){
		    	 for(int i=0 ;i<list.size();i++){
		    		 OrderProduct op = list.get(i);
		    		 if(op.getStatues() == 0 ){
		    			 %>
		    	<tr>
			    <td align="center" >
			   型号：<%=op.getTypeName() %>
			    </td>
			    </tr>
			    <tr>
			    <td align="center" >
			    条码:
			    </td>
			    <td align="center" >
			    <input type="text" id="barcode<%=op.getId() %>"  placeholder="安装产品必填"/>
			    </td>
			     </tr>
			    <tr>
			    <td align="center" >
			    批号:
			    </td>
			    <td align="center" >
			    <input type="text" id="batchnumber<%=op.getId() %>"  placeholder="安装产品必填"/>
			    </td>
			    </tr>

		    			 <%
		    		 }
		    	 }
		     }
		     
		     
		     
		     %>
		     
		     
		     
		     
		      </table>
		   </td>	
		</tr> 
		<tr class="bsc">
		
		    <td class="center" ><input type="button" onclick="quxiao()"  style="background-color:#ACD6FF;font-size:25px;"  value="取消" /></td>
		
			<td class="center" ><input type="button" onclick="saveAddPOD()"  style="background-color:#ACD6FF;font-size:25px;"  value="确定" /></td>
		</tr>
  
  
</table>

 
</div>

</body>
</html>