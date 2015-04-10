<%@ page language="java" 
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
	<%@ include file="../../common.jsp"%>
<%   
	request.setCharacterEncoding("utf-8"); 
String[] ids = request.getParameterValues("omid");   
String statues = request.getParameter("statues"); 
Map<Integer,Map<Integer,OrderGoodsAll>> map = OrderGoodsAllManager.getsendMap(user, Integer.valueOf(statues), ids);
//List<OrderGoodsAll> list = OrderGoodsAllManager.getsendlist(user,OrderMessage.unexamine,ids);  
//System.out.println(list.size());  
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
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript"> 
 

$(function () {  
	//$("#"+type).css("color","red");
}); 
 
function detail(src){
	window.location.href=src;
}
  
function search(statues){
	window.location.href="maintain.jsp?statues="+statues;
}
 
function check(){
	//var name=$("#name").val();
	//if("" == name || null == name){
	//	alert("订单名称不能为空");
	//	return false;
	//} 
}
</script>
</head>

<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：开单发货</div>
		<div class="main_r_tianjia">
			<ul>
				<li><a href="javascript:history.go(-1);">返回</a>
				</li>
			</ul>
		</div> 
		<!--  头 单种类  -->
		<table width="100%" border="0" cellspacing="1" id="table">
			<tr class="dsc">
				<td colspan=10> 
					<table width="100%">   
						<tr> 
							<td colspan=2 align="center"></td>
							<td colspan=2 align="center"></td>
							<td align="center" colspan=4>
								<form action="../../Print" method="post"
									onsubmit="return check()"> 
									<input type="hidden" value="OrderGoodssend" name="method">
									<input type="hidden" name="statues" value="<%=statues%>"> 
									<input type="hidden" name="token" value="<%=token%>" /> 
									<input type="hidden" value="<%=StringUtill.getStr(ids)%>"
										name="ids"> <input type="submit" value="开单导出"
										style="color:red">
								</form></td>

						</tr>

					</table></td>

			</tr>
			<tr class="dsc">
				<td align="center">门店</td>
				<td align="center">商品编码</td>
				<td align="center">商品条码</td>
				<td align="center">商品全名</td>
				<td align="center">商品单位</td>
				<td align="center">数量</td>
				<td align="center">单价</td>
				<td align="center">状态</td>
				<td align="center">订单号</td>
				<td align="center">备注</td>
			</tr>
			<%
				if (null != map) {
					
					Set<Map.Entry<Integer, Map<Integer, OrderGoodsAll>>> set = map
							.entrySet();
					Iterator<Map.Entry<Integer, Map<Integer, OrderGoodsAll>>> it = set
							.iterator();
					
					while (it.hasNext()) {
						Map.Entry<Integer, Map<Integer, OrderGoodsAll>> mapent = it
								.next();
						Map<Integer, OrderGoodsAll> mapb = mapent.getValue();
						Set<Map.Entry<Integer, OrderGoodsAll>> setb = mapb
								.entrySet();
						Iterator<Map.Entry<Integer, OrderGoodsAll>> itb = setb
								.iterator();
						int i = 0 ;
						String cla = "";
						while (itb.hasNext()) {
							i++;
							if(i%2 == 1){
								cla = "asc";
							}else {
								cla = "bsc";
							}
							Map.Entry<Integer, OrderGoodsAll> mapentb = itb.next();
							OrderGoodsAll o = mapentb.getValue();
							Branch branch = o.getOm().getBranch();
							List<OrderGoods> listog = o.getList();
							for (int j = 0; j < listog.size(); j++) {
								OrderGoods og = listog.get(j);
			%> 
 
			<tr class="<%=cla%>">
				<td align="center"><%=branch.getLocateName()%></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"><%=og.getProduct().getType()%></td>
				<td align="center"></td>
				<td align="center"><%=og.getRealnum()%></td>
				<td align="center"></td>
				<td align="center"><%=og.getStatuesName()%></td>
				<td align="center"><%=StringUtill.getNotNUll(og.getOid())%></td>
				<%
					if (j == 0) {
				%>
				<td align="center" rowspan=<%=listog.size()%>><%=o.getOm().getRemark()%></td>
				<%
					}
				%>

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