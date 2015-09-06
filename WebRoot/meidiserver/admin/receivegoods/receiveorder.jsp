<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,product.*,branch.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");  

String branch = request.getParameter("branch");    
List<OrderReceipt> list = OrderReceitManager.getList(); 
Set<String> sets = new HashSet<String>();
if (null != list) { 
	for(int i=0;i<list.size();i++){
		OrderReceipt or = list.get(i);
		sets.add(or.getBranchName());
	}
}

Map<String, OrderReceiptAll> map = new LinkedHashMap<String, OrderReceiptAll>();

if (null != list) { 
	for (int i = 0; i < list.size(); i++) {
		OrderReceipt or = list.get(i);
		if(StringUtill.isNull(branch) || !StringUtill.isNull(branch) && branch.equals(or.getBranchName())){
	

		OrderReceiptAll li = map.get(or.getBuyid());
		if (null == li) {  
	//System.out.println(or.getStatuesName());
	if(!"取消".equals(or.getStatuesName())){
	li = new OrderReceiptAll(); 
	Set<Integer> set = new HashSet<Integer>();
	set.add(or.getPrintNum());
	li.setPrintstatues(set);  
	li.setBuyid(or.getBuyid()); 
	li.setCheckNum(or.getCheckNum());
	li.setActiveordertiem(or.getActiveordertiem());
	li.setReceveTime(or.getReceveTime());
	map.put(or.getBuyid(), li); }
		}else {  
	li.getPrintstatues().add(or.getPrintNum());
		}
		}
	}
}
 
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
	function detail(src) {
		window.location.href = src;
	}

	function check() {
		var flag = false;

		$("input[type='checkbox'][id='check_box']").each(function() {
			if ($(this).attr("checked")) {
				var str = this.value;

				if (str != null && str != "") {
					// attract[i] = str; 
					//  i++;
					flag = true;
				}
			}
		});

		return flag;
	}
	 
	
	function getInventory(){
		  
		var mydate = new Date();  
		var dates = mydate.getTime();  
		//alert((dates - date)/1000/60); 
		//if((dates - date)/1000/60<5){  
		//	alert("您的操作过于频繁，请稍后重试");
		//	return ;
		//}   
		//alert(1);  
		$("#initInventory").html("正在刷新");
		//alert(dates - date);
		$("#initInventory").attr("disabled","true");
		     
			 $.ajax({      
			        type: "post",   
			         url: "../server.jsp",
			         data:"method=InitInventoryReceiveorder", 
			         dataType: "",        
			         success: function (data) { 
			        	 date = dates;
			        	 $("#initInventory").html("库存刷新");
			        	 $("#initInventory").removeAttr("disabled"); 
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			        // alert(errorThrown); 
			            } 
			           });
		 } 
	
	
</script>
</head>
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：退货订单</div>
		
		<form action="receiveorder.jsp" method="post" id="post">
			<select name="branch">
				<option value=""></option>
				<%
					if (!sets.isEmpty()) {
						Iterator<String> it = sets.iterator();
						while (it.hasNext()) {
							String br = it.next();
				%>
				<option value="<%=br%>"><%=br%></option>
				<%
					}

					}
				%>
 

			</select> <input type="submit" value="查看" />
			<input type="button" value="刷新" onclick="getInventory();" id="initInventory"/>

		</form>
		
		
		<!--  头 单种类  --> 

		<table width="100%" border="0" cellspacing="1" id="Ntable">

			<tr class="dsc">
				<td align="center">退货订单编号</td>
				<td align="center">校验码</td>
				<td align="center">退货订单日期</td>
				<td align="center">退货订单有效期</td>
				<td align="center">是否打印</td>
			</tr>
			<%
				if (null != map) {
					Set<Map.Entry<String, OrderReceiptAll>> set = map.entrySet();
					Iterator<Map.Entry<String, OrderReceiptAll>> it = set
							.iterator();
					while (it.hasNext()) {
						Map.Entry<String, OrderReceiptAll> mapent = it.next();
						String buyid = mapent.getKey();
						OrderReceiptAll or = mapent.getValue();
			%>

			<tr class="asc"
				ondblclick="detail('receiveorderdetail.jsp?buyid=<%=or.getBuyid()%>&branch=<%=branch%>')">
				<td align="center"><%=or.getBuyid()%></td>
				<td align="center"><%=or.getCheckNum()%></td>
				<td align="center"><%=or.getReceveTime()%></td>
				<td align="center"><%=or.getActiveordertiem()%></td>
				<td align="center"><%=or.getPrintName()%></td>
			</tr>
			<%
				}
				}
			%>

		</table>
	</div>

</body>
</html>