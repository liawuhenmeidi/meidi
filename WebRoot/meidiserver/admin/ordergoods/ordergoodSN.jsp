<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
<%
	request.setCharacterEncoding("utf-8");
String type = request.getParameter("type"); 
//System.out.println(type);    
String exportmodel = request.getParameter("exportmodel");
String[] ids = request.getParameterValues("omid");         
String[] statues = request.getParameterValues("statues"); 
List<String> listStatues = Arrays.asList(statues); 

 
Map<String,InventoryBranch> map = InventoryBranchManager.getmapTypeBranch(StringUtill.getStr(ids));
List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user,OrderMessage.unexamine,ids,statues,exportmodel);
   
 // System.out.println("map"+StringUtill.GetJson(map));
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
<script type="text/javascript" src="../../js/common.js"></script>
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

	function check(num) {
		// $("#typestatues").val(num);
		if (1 == num) {
			var name = $("#name").val();
			if ("" == name || null == name) {
				alert("订单名称不能为空");
				return false;
			}
		}

		$("#post").submit();

	}
</script>
</head>

<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：订单生成</div>
		<!--  头 单种类  --> 
		<form action="../../Print" id="post" method="post">
			<input type="hidden" value="billing" name="method"> <input
				type="hidden" name="ids" value="<%=StringUtill.getStr(ids)%>" /> <input
				type="hidden" name="statues"
				value="<%=StringUtill.getStr(statues)%>" /> <input type="hidden"
				name="exportmodel" value="<%=exportmodel%>" /> <input type="hidden"
				name="typestatues" id="typestatues" value="<%=type%>" />

			<table width="100%" border="0" cellspacing="1" id="table">
				<tr class="dsc">
					<td colspan=11>
						<table>
							<tr>
								<td align="center">请输入订单名称</td>
								<td align="center"><input type="text" name="name" id="name"
									value="" placeholder="订退单名称不能重复" /></td>
								<td align="center"><input type="button" value="导出"
									onclick="check(1)" style="color:red">
								</td>
								<!-- 
     <td align="center"><input type="button" value="忽略"  onclick="check(0)" style="color:red"></td>
    -->
							</tr>

						</table></td>

				</tr>
				<tr class="dsc">
					<td align="center">序号</td>
					<td align="center">商品编码</td>
					<td align="center">商品名称</td>
					<!-- 
					<td align=center width="10%">未入库数量</td>
					 -->
					<td align=center width="20%">订货数</td>
					<td align=center width="20%">订单数</td>
					<td align="center">订货门店</td>
					<td align="center">订货门店编码</td>
					<td align="center">库位</td>
					<td align="center">日期</td>
					<td align="center">供应商编码</td>
					<td align="center">是否为订单过期产品</td>
				</tr>
				<%
					Set<Integer> pids = new HashSet<Integer>();
					int count = 0;
					if (null != list) {

						for (int i = 0; i < list.size(); i++) {
							OrderGoodsAll o = list.get(i);
							Branch branch = o.getOm().getBranch();
							List<OrderGoods> listog = o.getList();
							for (int j = 0; j < listog.size(); j++) {
								OrderGoods og = listog.get(j);
								pids.add(og.getTid());
								//System.out.println(og.getTid()+"_"+o.getOm().getBranchid());
								InventoryBranch in = map.get(og.getTid() + "_"
										+ o.getOm().getBranchid());
								String serialnumber = og.getSerialnumber();
								count++;

								if (ExportModel.SuNing == Integer.valueOf(exportmodel)) {
									if (StringUtill.isNull(serialnumber)) {
										serialnumber = Company.supply;
									}
								} else if (ExportModel.GuoMei == Integer
										.valueOf(exportmodel)) {
									if (StringUtill.isNull(serialnumber)) {
										serialnumber = Company.supplyGM;
									}
								}

								int InNum = 0;

								if (null != in) {
									InNum = in.getPapercount();
								}
								if (og.getStatues() == 9) {

								}
				%>

				<tr class="asc">
					<td align="center"><%=count%></td>
					<td align="center"><%=og.getProduct().getEncoded()%></td>
					<td align="center"><%=og.getProduct().getType()%></td>
					<!--  
					<td align=center><%=InNum%></td>
					 -->
					<td align=center><%=og.getRealnum()%></td>
					<td align=center><%=og.getOrdernum()%></td>
  
					<td align="center"><%=branch.getNameSN()%></td>
					<td align="center"><%=branch.getEncoded(og.getStatues())%></td>
					<td align="center"><%=og.getStatuesName()%></td>
					<td align="center"><%=TimeUtill.getdateString()%></td>
					<td align="center"><%=serialnumber%></td>
					<td align="center">否</td>
				</tr>

				<%
					}
						}
					}
					
				%>
				<!--  
    <tr class="asc"> 
    <td align="center" colspan=8> 
      <input type="submit" id="submit" value="订单提交" /></td> 
    </tr>
    -->
			</table>
		</form>
	</div>

</body>
</html>