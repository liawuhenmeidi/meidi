<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,product.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp);  
 
String starttime = request.getParameter("starttime");
String endtime = request.getParameter("endtime"); 
String branch = request.getParameter("branch");  
  
List<Inventory> list = InventorySale.compare(starttime,endtime);
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
<script type="text/javascript">
	var jsonall =<%=listall%>;
	$(function() { 
		$("#branch").autocomplete({
			source : jsonall
		});
	});
	 
	
	function checked() {
		var start = $("#starttime").val();
		var end = $("#endtime").val();
		
		if("" == start || null == start){
			 alert("开始时间不能为空");
			 return false;
		} 
		
		if("" ==  end || null ==  end){
			 alert("结束时间不能为空");
			 return false;
		}
		
		

		return true;
	}
</script>
</head>
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：查看明细</div>
		<form action="inventory.jsp" method="post" onsubmit="return checked()">
			<table cellpadding="1" cellspacing="0">
				<tr>
					<td>开始时间</td>
					<td><input name="starttime" type="text" id="starttime"
						value="<%=starttime%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						readonly="readonly" /></td>
 
					<td>结束时间</td> 
					<td><input name="endtime" type="text" id="endtime" size="10"
						value="<%=TimeUtill.dataAdd(TimeUtill.getdateString(), -1)%>" maxlength="10"
						readonly="readonly" />
					</td>

					<td>门店</td>
					<td><input type="text" name="branch" id="branch"
						value="<%=branch%>" /></td>
					<td>型号</td>

					<td><input type="text" name="product" id="product" value="" />
					</td>


					<td><input type="submit" value="查询" />
					</td>
				</tr>

			</table>
		</form>
		<!--  头 单种类  -->

		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">
				<td align="center">序号</td>
				<td align="center">库位名称</td>

				<td align="center">商品名称</td>
				<td align="center">商品编码</td>

				<td align="center">库存较少数量</td>
				<td align="center">入库数量</td>
				<td align="center">销售数量</td>

			</tr>

			<%
				if (null != list) {
					for (int i = 0; i < list.size(); i++) {
						Inventory in = list.get(i);
						String bnum = StringUtill.getStringNocn(in.getBranchName());
						String bname = "";
						if (StringUtill.isNull(bnum)) {
							bnum = in.getBranchNum();
						}

						if (null != BranchService.getNumMap(SaleModel.SuNing)) {
							if (null != BranchService.getNumMap(SaleModel.SuNing)
									.get(bnum)) {
								bname = BranchService.getNumMap(SaleModel.SuNing)
										.get(bnum).getLocateName();
							}
						}

						if (!StringUtill.isNull(branch) && branch.equals(bname)
								|| StringUtill.isNull(branch)) {
							
			%>
			<tr class="asc">
				<td align="center"><%=i + 1%></td>
				<td align="center"><%=bname%></td>

				<td align="center"><%=in.getGoodpName()%></td>
				<td align="center"><%=in.getGoodNum()%></td>
 
				<td align="center"><%=in.getInreduce()-in.getInInbranch()%></td>
				<td align="center"><%=in.getInInbranch()%></td>
				<td align="center"><%=in.getSaleNum()%></td>


			</tr>
			<%
				}
					}
				}
			%>



		</table>
		</form>
	</div>

</body>
</html>