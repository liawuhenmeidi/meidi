<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");       
User user = (User)session.getAttribute("user");
String tname = request.getParameter("typename"); 
//System.out.println(tname);  
String bid = user.getBranch();  
Branch branch = BranchService.getMap().get(Integer.valueOf(bid)); 
String starttime = TimeUtill.getdateString();  
List<OrderSN > list = OrderDownLoad.geteffective(starttime,TimeUtill.dataAdd(starttime, 29));
Map<String,List<OrderSN>>   map =new HashMap<String,List<OrderSN>>();
if(null != list){    
	   for(int i=0;i<list.size();i++){
		   OrderSN in = list.get(i);           
		  
		   if(in.getNum() != in.getInNum() && (branch.getNameSN().equals(in.getBranchName()))){
			  // if(in.getGoodpName().equals(tname)){
				//   onum = in.getOrderNum(); 
			  // }            
			   // System.out.println("in.getEndtime()"+in.getEndtime());
			   List<OrderSN> li = map.get(in.getOrderNum());
			   if(null == li){
				   li = new ArrayList<OrderSN>();
				   map.put(in.getOrderNum(), li);
			   }
			   //System.out.println("in.getEndtime()"+in.getEndtime());
			   li.add(in); 
	       } 
	   }	   
}



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
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 

<script type="text/javascript"> 
    var jsonall = new Array();
    //jsonall.push("我们是好人"); 
	$(function() {   
		$("#typename").autocomplete({ 
			 source: jsonall  
		    });
		
	});


</script> 
</head>
<body> 
	<div class="s_main">
		<div class="weizhi_head">现在位置：<%=user.getBranchName() %>>>查看苏宁订单号
		
		
		</div> 
		<form action="" id="myform"> 
		<table><tr><td>商品名称：<input type="text" name="typename" id="typename" class="cba"><input type="submit" value="查询"></td></tr></table>
		</form>
		 
		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">   
				<td align="center" >订单编号</td> 
				
				<td align="center" >商品名称</td>  
				<td align="center" >商品代码</td> 
				<td align="center" >订单类型</td> 
				<td align="center" >可入库数量</td>
				<td align="center" >有效日期</td>
			</tr>
 
			<%    
			//System.out.println(branch.getNameSN());
						if (null != map) {
							 Set<Map.Entry<String,List<OrderSN>>> set = map.entrySet();
							 Iterator<Map.Entry<String,List<OrderSN>>> it = set.iterator();
							 while(it.hasNext()){
								 Map.Entry<String,List<OrderSN>> mapent = it.next();
								 String om = mapent.getKey();
								 List<OrderSN> li = mapent.getValue();
							      
							for (int i = 0; i < li.size(); i++) {
								  OrderSN in = li.get(i);    
									 String cl = "";
									if(tname.equals(in.getGoodpName())){
										 cl = "bgcolor=\"red\""; 
									 }

									 
									%>
	<script type="text/javascript">   
if($.inArray("<%=in.getGoodpName() %>", jsonall) == -1){
	jsonall.push("<%=in.getGoodpName() %>");   
}
</script> 								
									
									 
									
									<tr class="asc">    
				<% if(i == 0){  
					%>
					<td align="center" rowspan="<%=li.size()%>"><%=in.getOrderNum() %></td> 
					<%
				}%>  
				
				<td align="center" <%=cl %>><%=in.getGoodpName()%></td>  
				<td align="center"><%=in.getGoodNum()%></td> 
				<td align="center" ><%=in.getGoodType() %></td>
				<td align="center" ><%=in.getNum()-in.getInNum() %></td>
					<td align="center" ><%=in.getEndtime()%></td>
			</tr>
									 
									
									<%
							}
										
							 }
					}		
								
								
			%> 
  
         

		</table>
		 
		 
		
	</div>

</body>
</html>