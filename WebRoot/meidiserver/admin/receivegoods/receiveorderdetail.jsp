<%@ page language="java"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
	<%@ include file="../../common.jsp"%>
<%
	request.setCharacterEncoding("utf-8");    
String buyid = request.getParameter("buyid");  
String branch = request.getParameter("branch"); 

String method = request.getParameter("method");
if(!StringUtill.isNull(method)){  
	String ids[] = request.getParameterValues("id");
	OrderReceitManager.delete(ids); 

	
} 


Map<String,OrderReceipt> maps =  OrderReceitManager.getMap(buyid,branch);  
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

  
	function check(num) {
		  
		if(num == 1){
			$("#post").attr("action","receiveorderdetail.jsp");
		}
		
		
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
		
		if(flag){
			$("#post").submit();
		}
	
	}
 
	
</script>
</head>
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：未退货订单
 <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>  
		<!--  头 单种类  --> </div>  
		<form action="../../Print" id="post" method="post"
			>
			
		
			<table width="100%" border="0" cellspacing="1" id="Ntable">
				  
				<tr class="dsc">         
					<td class="dsc" colspan=17>
					<input type="button" class="button"  
						name="dosubmit" value="退厂拖机单" onclick="check(0)"></input>
						<input type="button" class="button"  
						name="dosubmit" value="删除" onclick="check(1)"></input>
						<input type="hidden" value="OrderGoodsSN" name="method">
						<input type="hidden" name="token" value="<%=token%>" /> 
						<input type="hidden" name="buyid" value="<%=buyid%>" />  
						<input type="hidden" name="branch" value="<%=branch%>" />  
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
					<td align="center">状态</td>
					<td align="center">打印次数</td>
				</tr>
				<% 
				if (null != maps) { 
					Set<Map.Entry<String, OrderReceipt>> set = maps.entrySet();
					Iterator<Map.Entry<String, OrderReceipt>> it = set.iterator();
					while (it.hasNext()) {
						Map.Entry<String, OrderReceipt> mapent = it.next();
						OrderReceipt gr = mapent.getValue();
						if(!"取消".equals(gr.getStatuesName())){
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
					<td align="center"><%=gr.getStatuesName()%></td>
					<td align="center"><%=gr.getPrintNum()%></td>
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