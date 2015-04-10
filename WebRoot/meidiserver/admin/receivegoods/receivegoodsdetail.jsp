<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,product.*,branch.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String id = request.getParameter("id");
GoodsReceipt gr =  GoodsReceitManager.getByid(id);  
 //System.out.println(StringUtill.GetJson(set));
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单审核</title>

<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
	function check() {
		var flag = false;
		
		$("input[type='checkbox'][id='check_box']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.value; 
	   				
	   				if(str != null  &&  str != ""){
		   				  // attract[i] = str; 
			   	          //  i++;
			   	          flag = true;
		   				}	
	   		} 
	   	}); 
		  
		return flag;
	}
 
	
</script>
</head> 
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：查看明细</div>
		
		<!--  头 单种类  -->
		 
		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">
				<td align="center">苏宁收货单号<p>苏宁发货单号</td>
				<td align="center">收货日期<p>苏宁发货日期</td>
				<td align="center">供应商发货单号</td>
				<td align="center">采购订单号<p>退货订单号</td>
				<td align="center">订单类型<P>退货订单类型</td>
				<td align="center">商品代码</td>
				<td align="center">商品名称</td>
				<td align="center">实收数量</td>
				<td align="center">地点</td>
				<td align="center">地点名称</td>
			</tr>
			

			<tr class="asc">
				<td align="center"><%=gr.getReceveid()%></td>
				<td align="center"><%=gr.getReceveTime()%></td>
				<td align="center"><%=gr.getSendid()%></td>
				<td align="center"><%=gr.getBuyid()%></td>
				<td align="center"><%=gr.getOrdertype()%></td>
				<td align="center" ><%=gr.getGoodsnum()%></td>
				<td align="center" ><%=gr.getGoodsName()%></td>
				<td align="center"><%=gr.getRecevenum()%></td>
				<td align="center" ><%=gr.getBranchid()%></td>
				<td align="center" ><%=gr.getBranchName()%></td>
			</tr>
			

		</table>
</form>
	</div>

</body>
</html>