<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,product.*,branch.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");
  
Map<String,List<SNInventory>> map = InventoryChange.getMap(TimeUtill.getdateString());
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
                 <td align="center">序号</td>
				<td align="center">库位名称</td>
				<td align="center">库位代码</td>
				<td align="center">商品名称</td>
				<td align="center">商品编码</td>
				<td align="center">商品类别</td>
				<td align="center">库存数量</td>

			</tr>
			<% 
				if (!map.isEmpty()) {
					Set<Map.Entry<String, List<SNInventory>>> set = map.entrySet();

					Iterator<Map.Entry<String, List<SNInventory>>> it = set
							.iterator();
					int count = 0 ;
					String cl = ""; 
					while (it.hasNext()) {
						// for(int i=0;i<list.size();i++){
						Map.Entry<String, List<SNInventory>> ent = it.next();
						List<SNInventory> list = ent.getValue(); 
						
						if(count%2 == 0){
							cl = "class=\"asc\"";
						}else {
							cl = "class=\"bsc\"";
						}
						if (null != list && list.size() > 1) {
							count ++;
					
							Iterator<SNInventory> itlist = list.iterator();
							while (itlist.hasNext()) {
								SNInventory in = itlist.next();
			%>
			<tr <%=cl %>>
                <td align="center"><%=count%></td>
				<td align="center"><%=in.getBranchName()%></td>
				<td align="center"><%=in.getBranchNum()%></td>
				<td align="center"><%=in.getGoodpName()%></td>
				<td align="center"><%=in.getGoodNum()%></td>
				<td align="center"><%=in.getGoodType()%></td>
				<td align="center"><%=in.getNum()%></td>



			</tr>


 

			<%
				}
						}
					}
				}
			%>



		</table>
		</form>
	</div>

</body>
</html>