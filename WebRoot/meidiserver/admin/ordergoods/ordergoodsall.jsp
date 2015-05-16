<%@ page language="java"
	import="java.util.*,ordersgoods.*,product.*,branchtype.*,branch.*,inventory.*,httpClient.download.*,httpClient.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8");  
User user = (User)session.getAttribute("user");
String branchtype = request.getParameter("branchtype");  
String isDisable = request.getParameter("isDisable");  
  
 
List<BranchType> listgt = BranchTypeManager.getLocate(); 
Map<String,OrderGoodsAll> map  = OrderGoodsAllManager.getmap(user,OrderMessage.unexamine);
      
Map<Integer,InventoryBranch>  mapin = InventoryBranchManager.getmapKeyBranchType(user,branchtype);
  
 // System.out.println(StringUtill.GetJson(map));
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

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	$(function() {
		//$("#"+type).css("color","red");
	});

	function detail(src) {
		window.location.href = src;
	}

	function search(statues) {
		window.location.href = "maintain.jsp?statues=" + statues;
	}
</script>
</head>

<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>

		<div class="weizhi_head">现在位置：订货单审核</div>
		<!--  头 单种类  -->
		<form action="ordergoodsall.jsp" method="post">
			<table>
				<tr>
					<td>销售系统： <select id="branchtype" name="branchtype">
							<option></option>
							<%
								if (null != listgt) {
									for (int i = 0; i < listgt.size(); i++) {
										BranchType bt = listgt.get(i);
										if (bt.getTypestatues() == 1) {
							%>
							<option value="<%=bt.getId()%>"><%=bt.getName()%></option>
							<%
								}
									}
								}
							%>
					</select>
					</td>


					<td id="isDisable"><input type="checkbox" name="isDisable"
						value="1">订单已过期产品</td>

					<td><input type="submit" id="submit" value="查询" />
					</td>
				</tr>
			</table>
		</form>

		<table width="100%" border="0" cellspacing="1" id="table">
			<tr class="dsc">


				<td width="25%" class="s_list_m" align="center">门店</td>
				<td width="25%" class="s_list_m" align="center">订单提交人</td>
				<td width="25%" class="s_list_m" align="center">订单时间</td>
				<td width="25%" class="s_list_m" align="center">备注</td>
			</tr>
			<%
				if (null != map) {
					Set<Map.Entry<String, OrderGoodsAll>> mapent = map.entrySet();
					Iterator<Map.Entry<String, OrderGoodsAll>> itmap = mapent
							.iterator();
					int i = 0; 
					//System.out.println(12);  
					while (itmap.hasNext()) {
						Map.Entry<String, OrderGoodsAll> en = itmap.next();
						OrderGoodsAll o = en.getValue();
						String key = en.getKey();  
						if (StringUtill.isNull(branchtype) 
								|| !StringUtill.isNull(branchtype)
								&& BranchService.getMap()
										.get(o.getOm().getBranchid()).getPid() == Integer
										.valueOf(branchtype)) {
			%>
			<tr class="asc"
				ondblclick="detail('ordergoodsupdate.jsp?id=<%=key%>')">

				<td align="center"><%=o.getOm().getBranchname()%></td>
				<td align="center"><%=o.getOm().getUser().getUsername()%></td>
				<td align="center"><%=o.getOm().getSubmittime()%></td>
				<td align="center"><%=o.getOm().getRemark()%></td>

			</tr>

			<%
				}
					}
				}

				if (!StringUtill.isNull(isDisable)) {
					if (!mapin.isEmpty()) {
						Set<Map.Entry<Integer, InventoryBranch>> set = mapin
								.entrySet();
						Iterator<Map.Entry<Integer, InventoryBranch>> it = set
								.iterator();
						while(it.hasNext()){
						Map.Entry<Integer, InventoryBranch> mapen = it.next();
						int branchid = mapen.getKey(); 
						InventoryBranch ib = mapen.getValue();
						if (StringUtill.isNull(ib.getActivetime())) {
							Branch branch = BranchService.getMap().get(
									ib.getBranchid());

							String cl = "class=\"bsc\"";
			%>
  
			<tr <%=cl%> ondblclick="detail('ordergoodsdisable.jsp?branch=<%=branch.getId()%>')">
 
				<td align="center"><%=branch.getLocateName()%></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>

			</tr>




			<%
				}
						}
					}

				}
			%>
		</table>
		<br /> <br />
	</div>
</body>
</html>