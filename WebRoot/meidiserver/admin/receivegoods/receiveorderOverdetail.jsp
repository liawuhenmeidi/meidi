<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,inventory.*,product.*,branch.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8");  
User user = (User)session.getAttribute("user");   
String buyid = request.getParameter("buyid");

Map<String,OrderReceipt> maps =  OrderReceitManager.getMapOver(buyid);
//List<OrderReceipt> list = OrderReceitManager.getListOver(buyid);
    
String type= request.getParameter("type");  
if("submit".equals(type)){
	String method = request.getParameter("method");
	// System.out.println("method"+method);  
	List<String> li = new ArrayList<String>();
	String[] ids = request.getParameterValues("id");
	if(method.equals("0")){ 
		for(int i=0;i<ids.length;i++){
			String id = ids[i];   
			OrderReceipt or = maps.get(id);
			String sql = OrderReceitManager.updateDiable(or);
			maps.remove(id);  
			li.add(sql);    
		}    
	}else if(method.equals("1")){ 
		List<String> sql = OrderReceitManager.billing(user,maps,ids,buyid); 
		li.addAll(sql);  
		for(int i=0;i<ids.length;i++){
			String id = ids[i];   
			maps.remove(id);  
		}   
		
		  
	}   
	
	  
	 //System.out.println(li);
	DBUtill.sava(li); 
	
	 
}
Map<Integer,Map<String,InventoryBranch>> map = InventoryBranchManager.getmapType(user); 

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

  function winconfirm(num){
	  $("#method").val(num);
  }
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
		<div class="weizhi_head">现在位置：已退货订单
		 <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>  
		<!--  头 单种类  --> 
		</div>
		<form action="receiveorderOverdetail.jsp" method="post"
			onsubmit="return check()">
			<input type="hidden" name="type" value="submit"> 
			<input type="hidden" name="buyid" value="<%=buyid%>">
			<input 
				type="hidden" name="method" id="method" value="">

			<table width="100%" border="0" cellspacing="1" id="Ntable">
				<tr class="dsc">
					<td align="center" colspan=2><input type="submit"
						class="button" name="dosubmit" value="忽略" onclick=" winconfirm(0)"></input>
					</td>
					<td align="center" colspan=2><input type="submit"
						class="button" name="dosubmit" value="开单退货"
						onclick=" winconfirm(1)"></input>
					</td>
					<td colspan=10></td>
				</tr>
				<tr class="dsc">
					<td width="10%" class="s_list_m" align="center"><input
						type="checkbox" value="" id="allselect"
						onclick="seletall(allselect)"></input></td>

					<td align="center">退货订单编号</td>
					<td align="center">校验码</td>
					<td align="center">商品代码</td>
					<td align="center">商品名称</td>
					<td align="center">地点</td>
					<td align="center">地点名称</td>
					<td align="center">退货订单数量</td>
					<td align="center">实发数量</td>
					<td align="center">仍需拖货数量</td>
					<td align="center">未入库数量</td>
					<td align="center">退货订单日期</td>
					<td align="center">订单类型</td>
					<td align="center">退货订单有效期</td>

				</tr> 
				<%
					if (null != maps) {
						Set<Map.Entry<String, OrderReceipt>> set = maps.entrySet();
						Iterator<Map.Entry<String, OrderReceipt>> it = set.iterator();
						while (it.hasNext()) {
							Map.Entry<String, OrderReceipt> mapent = it.next();
							OrderReceipt gr = mapent.getValue();
							int noIn = 0;
							String clb = "";
							String clt = "";
							if (0 == gr.getBid()) {
								clb = "style=color:red";
							}
							if (0 == gr.getTid()) {
								clt = "style=color:red";
							}
							if (null != map.get(gr.getBid())) {
								if (null != map.get(gr.getBid()).get(gr.getTid())) {
									noIn = map.get(gr.getBid()).get(gr.getTid())
											.getPapercount();
								}
							}
				%>

				<tr class="asc">
					<td align="center"><input type="checkbox"
						value="<%=gr.getId()%>" name="id" id="check_box"></input></td>

					<td align="center"><%=gr.getBuyid()%></td>
					<td align="center"><%=gr.getCheckNum()%></td>
					<td align="center" <%=clt%>><%=gr.getGoodsnum()%></td>
					<td align="center" <%=clt%>><%=gr.getGoodsName()%></td>
					<td align="center" <%=clb%>><%=gr.getBranchid()%></td>
					<td align="center" <%=clb%>><%=gr.getBranchName()%></td>
					<td align="center"><%=gr.getOrderNum()%></td>
					<td align="center"><%=gr.getRecevenum()%></td>
					<td align="center"><%=gr.getRefusenum()%></td>
					<td align="center"><%=noIn%></td>
					<td align="center"><%=gr.getReceveTime()%></td>

					<td align="center"><%=gr.getOrdertype()%></td>
					<td align="center"><%=gr.getActiveordertiem()%></td>







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