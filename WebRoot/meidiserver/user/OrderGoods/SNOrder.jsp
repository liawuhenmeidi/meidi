<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");       
User user = (User)session.getAttribute("user");
String bid = user.getBranch();
Branch branch = BranchService.getMap().get(Integer.valueOf(bid)); 
String starttime = TimeUtill.getdateString();  
List<OrderSN > list = OrderDownLoad.geteffective(starttime,TimeUtill.dataAdd(starttime, 29));  
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
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 

<script type="text/javascript"> 

	$(function() { 
		  
		
	});
</script>
</head>
<body> 
	<div class="s_main">
		<div class="weizhi_head">现在位置：<%=user.getBranchName() %>>>查看苏宁订单号</div> 
		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">   
				<td align="center" >订单编号</td> 
				
				<td align="center" >商品名称</td>  
				<td align="center" >商品代码</td> 
				<td align="center" >订单类型</td> 
				<td align="center" >可入库数量</td>
			</tr>
 
			<%    
			//System.out.println(branch.getNameSN());
						if (null != list) {
							for (int i = 0; i < list.size(); i++) {
								OrderSN in = list.get(i); 
								//System.out.println(in.getBranchName()); 
								if(in.getNum() != in.getInNum() && (branch.getNameSN().equals(in.getBranchName()))){
									%>
									<tr class="asc">    
				
				<td align="center" ><%=in.getBranchName() %></td> 
				<td align="center" ><%=in.getGoodpName()%></td>  
				<td align="center"><%=in.getGoodNum()%></td> 
				<td align="center" ><%=in.getGoodType() %></td>
				<td align="center" ><%=in.getNum()-in.getInNum() %></td>
			</tr>
									
									
									<%
								}
								
								
								
								
							}
							
						}
										
								
								
								
								
			%> 
  
         

		</table>
		 
		 
		
	</div>

</body>
</html>