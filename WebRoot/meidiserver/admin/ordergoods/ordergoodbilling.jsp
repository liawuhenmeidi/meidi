<%@ page language="java"  import="java.util.*,ordersgoods.*,branchtype.*,product.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%    
request.setCharacterEncoding("utf-8");  
User user = (User)session.getAttribute("user");       
//Map<String,List<OrderGoods>> map  = OrderGoodsAllManager.getbillingmap(user,OrderMessage.billing); 
 // System.out.println(StringUtill.GetJson(map));
  
 String branchtype = request.getParameter("branchtype"); 
List<BranchType> listgt = BranchTypeManager.getLocate();  
 Map<String,Map<String,List<OrderGoods>>> map  = OrderGoodsAllManager.getbillingmap(user,OrderMessage.billing,branchtype); 
 
%>           
<!DOCTYPE html> 
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单审核</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
 
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
	var flag = false;
	var attract = new Array();
	var i = 0;
	
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
  <div class="weizhi_head">现在位置：查看订单</div>
  <form action="ordergoodbilling.jsp" method="post">
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
				<td><input type="submit" id="submit" value="查询" />
				</td>

			</tr>
		</table>
	</form>
<!--  头 单种类  -->     
<form action="ordergoodSN.jsp"  method = "post"  onsubmit="return check()">

<table width="100%" border="0" cellspacing="1"  id="table"> 
  <tr class="dsc"> 
    <td  class="s_list_m"  align="center">订单号</td>
    <td  class="s_list_m"  align="center">订单名称</td>
    <td  class="s_list_m" align="center">订单时间</td>
     <td  class="s_list_m" align="center">有效时间</td>
    <td  class="s_list_m" align="center">订单状态</td>
  </tr>     
   <%    
   if(null != map){   
	   Set<Map.Entry<String,Map<String,List<OrderGoods>>>> mapent = map.entrySet();
	   Iterator<Map.Entry<String,Map<String,List<OrderGoods>>>> itmap = mapent.iterator();
		int i = 0 ;
		while(itmap.hasNext()){ 
			Map.Entry<String,Map<String,List<OrderGoods>>> en =  itmap.next();
			String key = en.getKey(); 
			Map<String,List<OrderGoods>> maps = en.getValue();
			Set<Map.Entry<String,List<OrderGoods>>> mapsent = maps.entrySet();
			Iterator<Map.Entry<String,List<OrderGoods>>> itmaps = mapsent.iterator();
			while(itmaps.hasNext()){ 
				Map.Entry<String,List<OrderGoods>> enmaps =  itmaps.next();
				List<OrderGoods> o =enmaps.getValue();
				String oid = o.get(0).getOid();  
				String time = o.get(0).getUuidtime();
				String statues = o.get(0).getOpstatuesName();
				String endtime = o.get(0).getEffectiveendtime();
			     
  %>                  
 <tr class="asc" ondblclick="detail('ordergoodbillingdetail.jsp?name=<%=key%>&orderid=<%=oid%>')">
     <td align="center"><%= oid%></td>    
     <td align="center"><%=key %></td> 
     <td align="center"><%=time %></td>  
      <td align="center"><%=StringUtill.getNotNUll(endtime)%></td>     
     <td align="center"><%=statues %></td>  
  </tr> 
    				 
			
     <%  }
		}
    }%>
</table>
</form>

</div>

</body>
</html>