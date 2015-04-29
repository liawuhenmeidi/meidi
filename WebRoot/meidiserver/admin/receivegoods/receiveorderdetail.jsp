<%@ page language="java"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
	<%@ include file="../../common.jsp"%>
<%
	request.setCharacterEncoding("utf-8");    
String buyid = request.getParameter("buyid");    
Map<String,OrderReceipt> maps =  OrderReceitManager.getMap(buyid);  
String type= request.getParameter("type");   
   
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
		<div class="weizhi_head">现在位置：查看收货记录</div>

		<!--  头 单种类  -->
		<form action="../../Print" method="post"
			onsubmit="return check()">
			
		
			<table width="100%" border="0" cellspacing="1" id="Ntable">
				 
				<tr class="dsc">         
					<td class="dsc" colspan=16>
					<input type="submit" class="button"  
						name="dosubmit" value="退厂拖机单" ></input>
						<input type="hidden" value="OrderGoodsSN" name="method">
						<input type="hidden" name="token" value="<%=token%>" /> 
						<input type="hidden" name="buyid" value="<%=buyid%>" />  
						</td>
 
				</tr> 
				
				<tr class="dsc">
					<td width="10%" class="s_list_m" align="center"><input
						type="checkbox" value="" id="allselect"
						onclick="seletall(allselect)"></input></td>


					<td align="center">送货确认书编号</td>
					<td align="center">退货订单编号</td>
					<td align="center">校验码</td>
					<td align="center">商品代码</td>
					<td align="center">商品名称</td>
					<td align="center">地点</td>
					<td align="center">地点名称</td>
					<td align="center">退货订单数量</td>
					<td align="center">实发数量</td>
					<td align="center">仍需拖货数量</td>
					<td align="center">退货订单日期</td>
					<td align="center">订单类型</td>
					<td align="center">退货订单有效期</td>
					<td align="center">批次</td>
					<td align="center">打印次数</td>
				</tr>
				<% 
				if (null != maps) { 
					Set<Map.Entry<String, OrderReceipt>> set = maps.entrySet();
					Iterator<Map.Entry<String, OrderReceipt>> it = set.iterator();
					while (it.hasNext()) {
						Map.Entry<String, OrderReceipt> mapent = it.next();
						OrderReceipt gr = mapent.getValue();
							String clb = "";
							String clt = "";
							if (0 == gr.getBid()) {
								clb = "style=color:red";
							}
							if (0 == gr.getTid()) {
								clt = "style=color:red";
							}
							 
				%> 
				<tr class="asc">

					<td align="center"><input type="checkbox"
						value="<%=gr.getId()%>" name="id" id="check_box"></input></td>

 
					<td align="center"><%=gr.getQueryNum()%></td>
					<td align="center"><%=gr.getBuyid()%></td>
					<td align="center"><%=gr.getCheckNum()%></td>
					<td align="center" <%=clt%>><%=gr.getGoodsnum()%></td>
					<td align="center" <%=clt%>><%=gr.getGoodsName()%></td>
					<td align="center" <%=clb%>><%=gr.getBranchid()%></td>
					<td align="center" <%=clb%>><%=gr.getBranchName()%></td>
					<td align="center"><%=gr.getOrderNum()%></td>
					<td align="center"><%=gr.getRecevenum()%></td>
					<td align="center"><%=gr.getRefusenum()%></td>
					<td align="center"><%=gr.getReceveTime()%></td>
					<td align="center"><%=gr.getOrdertype()%></td>
					<td align="center"><%=gr.getActiveordertiem()%></td>
 

					<td align="center"><%=gr.getPici()%></td>
					
					<td align="center"><%=gr.getPrintNum()%></td>
				</tr>
				<%
					}
					}
				%>

			</table>
		</form>
	</div>

</body>
</html>