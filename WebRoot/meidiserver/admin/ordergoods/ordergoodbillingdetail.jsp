<%@ page language="java"
	import="java.util.*,ordersgoods.*,product.*,branch.*,org.apache.commons.logging.*,company.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");  
String name = request.getParameter("name");  
String exportName = request.getParameter("exportName");
String orderid = request.getParameter("orderid");     
String exportuuid = "";    
List<OrderGoodsAll> list = OrderGoodsAllManager.getlistName(user,OrderMessage.billing,exportName,name); 
Set<String> set = new HashSet<String>();
String endtime = "";
if (null != list) { 
	for (int i = 0; i < list.size(); i++) {
		OrderGoodsAll o = list.get(i);
		Branch branch = o.getOm().getBranch(); 
		List<OrderGoods> listog = o.getList();
		for (int j = 0; j < listog.size(); j++) {
	        OrderGoods og = listog.get(j);
	       //   System.out.println(og);   
	        if(!StringUtill.isNull(orderid)){ 
	        	if(orderid.equals(og.getOid())){ 
	        		set.add(og.getStatues()+"_"+og.getOid());
	        endtime = StringUtill.getNotNUll(og.getEffectiveendtime());
	        exportuuid = og.getExportuuid();
	        	}
	        } else { 
	        	if(StringUtill.isNull(og.getOid())){
	        		set.add(og.getStatues()+"_"+og.getOid());
	        endtime = StringUtill.getNotNUll(og.getEffectiveendtime());
	        exportuuid = og.getExportuuid();
	        	}
	        }
	         
		} 
	} 
}  
   
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
	$(function() {
		//alert(1);
		//$("#"+type).css("color","red");
	});

	function detail(src) {
		window.location.href = src;
	}

	function search(statues) {
		window.location.href = "maintain.jsp?statues=" + statues;
	}

	function check() {
		var name = $("#name").val();
		if ("" == name || null == name) {
			alert("订单名称不能为空");
			return false;
		}
	}
</script>
</head>

<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：查看订单</div>

		<div class="main_r_tianjia">
			<ul>
				<li><a href="javascript:history.go(-1);">返回</a></li>
			</ul>
		</div>
		<!--  头 单种类  -->

		<table width="100%" border="0" cellspacing="1" id="table">
			<tr class="dsc">
				<td colspan=10>
					<table width="100%">
						<tr>
							<td colspan=2 align="center">订单名称：<%=name%></td>
							<td colspan=2 align="center"></td>
							<td align="center" colspan=4><a
								href="../../../data/exportOrderGM/<%=exportuuid%>.xls"><font
									style="color:blue;font-size:20px;">导出</font> </a>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<tr class="dsc">
				<td colspan=10>
					<form action="../../user/OrderGoodsServlet" method="post">

						<input type="hidden" value="updateIOS" name="method"> <input
							type="hidden" value="<%=exportuuid%>" id="name" name="name">

						<table>

							<tr>
								<td align="center"></td>
								<%
									if (null != set) {
										Iterator<String> it = set.iterator();
										while (it.hasNext()) {
											String str = it.next();
											String[] strs = str.split("_");
											String i = strs[0];
											String oid = strs[1];
											if (StringUtill.isNull(oid)) {
												oid = "";
											}
								%>
								<td align="center"><%=OrderGoods.getStatuesName(Integer.valueOf(i))%>(订单号)</td>
								<td align="center"><input type="text" name="oid<%=i%>"
									value="<%=oid%>" /></td>
								<%
									}
									}
								%>

								<td align="center">订单截止日期</td>
								<td align="center"><input type="text"
									name="effectiveendtime" onclick="new Calendar().show(this);"
									value="<%=endtime%>" /></td>

								<td align="center"><input type="submit" value="保存"
									style="color:red">
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>

			<tr class="dsc">
				<td align="center">序号</td>
				<td align="center">订单号</td>
				<td align="center">商品编码</td>
				<td align="center">商品名称</td>

				<td align="center">订货数量</td>
				<td align="center">订货门店</td>
				<td align="center">订货门店编码</td>
				<td align="center">库位</td>
				<td align="center">日期</td>
				<td align="center">供应商编码</td>
			</tr>
			<%
				if (null != list) {
					int count = 0;
					for (int i = 0; i < list.size(); i++) {
						OrderGoodsAll o = list.get(i);
						Branch branch = o.getOm().getBranch();
						List<OrderGoods> listog = o.getList();
						for (int j = 0; j < listog.size(); j++) {
							OrderGoods og = listog.get(j);
							count++;
							//System.out.println(StringUtill.GetJson(og)); 
							if (!StringUtill.isNull(orderid)
									&& orderid.equals(og.getOid())
									|| StringUtill.isNull(orderid)
									&& StringUtill.isNull(og.getOid())) {
								String serialnumber = og.getSerialnumber();
								if (StringUtill.isNull(serialnumber)) {
									serialnumber = Company.supply;
								}

								String cl = "class=\"asc\"";
								if (StringUtill.isNull(og.getOid())) {
									cl = "class=\"bsc\"";
								}
			%>

			<tr <%=cl%>>
				<td align="center"><%=count%></td>
				<td align="center"><%=og.getOid()%></td>
				<td align="center"><%=og.getProduct().getEncoded()%></td>
				<td align="center"><%=og.getProduct().getType()%></td>

				<td align="center"><%=og.getRealnum()%></td>
				<td align="center"><%=branch.getLocateName()%></td>
				<td align="center"><%=branch.getEncoded()%></td>
				<td align="center"><%=og.getStatuesName()%></td>
				<td align="center"><%=TimeUtill.getdateString()%></td>
				<td align="center"><%=serialnumber%></td>
			</tr>

			<%
				}
						}

					}
				}
			%>

		</table>

	</div>

</body>
</html>