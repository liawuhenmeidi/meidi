<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,product.*,branch.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
Map<String, GoodsReceipt> map = GoodsReceitManager.getMap();
String type= request.getParameter("type");
if("submit".equals(type)){
	List<String> list = new ArrayList<String>();
	String[] ids = request.getParameterValues("id");
	for(int i=0;i<ids.length;i++){
		String id = ids[i];    
		GoodsReceipt gr = map.get(id);
		List<String> sql = GoodsReceitManager.saveDisable(gr);
		if(null != sql || sql.size() != 0 ){
			map.remove(id);  
		}  
		list.addAll(sql);
	}
	
	DBUtill.sava(list); 
	
	
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
		<form action="receivegoods.jsp"  method = "post"  onsubmit="return check()">
		<input type="hidden" name="type" value="submit">
		<table width="100%" border="0" cellspacing="1" id="Ntable">
             <tr class="dsc"> 
              <td class="dsc" colspan=12><input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input> </td>
               
             </tr>
			<tr class="dsc">
			<td width="10%" class="s_list_m"  align="center"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></td>
				<td align="center">苏宁收发货单号</td>
				<td align="center">操作类型</td>
				<td align="center">收发货日期</td> 
				<td align="center">供应商发货单号</td> 
				<td align="center">采购订单号</td>
				<td align="center">订单类型</td>
				<td align="center">商品代码</td>
				<td align="center">商品名称</td>
				<td align="center">实收数量</td>
				<td align="center">地点</td>
				<td align="center">地点名称</td>
			</tr>
			<% 
				if (null != map) {
					Set<Map.Entry<String, GoodsReceipt>> set = map.entrySet();
					Iterator<Map.Entry<String, GoodsReceipt>> it = set.iterator();
					while (it.hasNext()) {
						Map.Entry<String, GoodsReceipt> mapent = it.next();
						GoodsReceipt gr = mapent.getValue();
						String clb = "";
						String clt = "";
						if(0 == gr.getBid()){ 
							clb = "style=color:red";
						}
						if(0 == gr.getTid()){ 
							clt = "style=color:red";
						}	 
			%> 
 
			<tr class="asc">
			    <td align="center"><input type="checkbox"  value="<%= gr.getUuid() %>"  name="id" id="check_box"></input></td>             
				<td align="center"><%=gr.getReceveid()%></td>
				<td align="center"><%=gr.getStatuesName()%></td>
				<td align="center"><%=gr.getReceveTime()%></td>
				<td align="center"><%=gr.getSendid()%></td>
				<td align="center"><%=gr.getBuyid()%></td>
				<td align="center"><%=gr.getOrdertype()%></td>
				<td align="center" <%=clt %>><%=gr.getGoodsnum()%></td>
				<td align="center" <%=clt %>><%=gr.getGoodsName()%></td>
				<td align="center"><%=gr.getRecevenum()%></td>
				<td align="center" <%=clb %>><%=gr.getBranchid()%></td>
				<td align="center" <%=clb %>><%=gr.getBranchName()%></td>
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