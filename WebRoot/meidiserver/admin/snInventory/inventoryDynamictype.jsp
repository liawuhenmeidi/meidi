<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8");      
User user = (User)session.getAttribute("user"); 
List<String> listbranchp = BranchService.getListStr();  
String listall = StringUtill.GetJson(listbranchp);  
List<BranchType> listgt = BranchTypeManager.getLocate(); 
String starttime = request.getParameter("starttime");
String endtime = request.getParameter("endtime"); 
String branch = request.getParameter("branch");  
String type = request.getParameter("product"); 
String branchtype = request.getParameter("branchtype");
         
String json  = "";  
if(!StringUtill.isNull(branchtype)){
	List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
	json = StringUtill.GetJson(listallp); 
}   
// System.out.println("type"+type);  
// 门店  ，型号     

  
	 // 库存    
	 Map<String,SNInventory> map  = null;
	 if(!StringUtill.isNull(starttime) && !StringUtill.isNull(endtime)){ 
	 	map = InventoryMerger.getType(user,"50",branchtype,type,starttime,endtime);
	 	//System.out.println("mapsale"+mapsale.size());    
	 }  
	 
	 
	//System.out.println("mapsale"+mapsale.size()); 





// 库存
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

<style type="text/css"> 
td { 
	width: 100px;
	line-height: 30px;
}   

#head td {
	white-space: nowrap;
} 
</style>


<script type="text/javascript">
	var jsonall =<%=listall%>;
	var json =<%=json%>;
	$(function() {  
		 
		$("#branchtype").val(<%=branchtype%>);
		$("#branch").autocomplete({
			source : jsonall
		});
		$("#product").autocomplete({
			source : json
		});
	});
	
	function checked() {
		var start = $("#starttime").val(); 
		var end = $("#endtime").val(); 
		var type = $("#product").val(); 
		if("" == start || null == start){ 
			 alert("开始时间不能为空"); 
			 return false; 
		}   
		 
		if("" ==  end || null ==  end){
			 alert("结束时间不能为空");
			 return false;
		} 
		//  alert(type); 
		if("" != type && null != type){
			$("#post").attr("action","inventoryDynamic.jsp");
		}
		 

		return true;
	}
	
	function windorm(starttime,endtime,product){
		var branchtype = $("#branchtype").val(); 
		window.location.href='inventoryDynamicBranch.jsp?starttime='+starttime+'&endtime='+endtime+'&product='+product+'&branchtype='+branchtype;
		
		
	}
</script> 
</head> 
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include> 
		<div class="weizhi_head">现在位置:动销率
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:history.go(-1);"><font
					style="color:blue;font-size:20px;">返回</font>
			</a> 
		</div>

		<form action="inventoryDynamictype.jsp" method="post" id="post"
			onsubmit="return checked()">  
			<table cellpadding="1" cellspacing="0" width="100%">
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
					</select></td> 

					<td>开始时间:<input name="starttime" type="text" id="starttime"
						value="<%=starttime%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						readonly="readonly" /></td>

					<td>结束时间:<input name="endtime" type="text" id="endtime" size="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						value="<%=TimeUtill.dataAdd(TimeUtill.getdateString(), -1)%>"
						maxlength="10" readonly="readonly" />
					</td> 
					<td>型号:<input type="text" name="product" id="product"
						value="<%=type%>" /></td>


					<td><input type="submit" value="查询" />
					</td>
				</tr>

			</table>
		</form>
		<!--  头 单种类  -->

	<div style="width:100%; height:400px; overflow:scroll;" >      
		<table     cellspacing="1" id="Ntable" > 
			<tr style="position:fixed;height:40px;width:97.5%;" class="dsc" >
				<td align="center">序号</td>
				<td align="center">商品名称</td>
				<td align="center">商品编码</td>
				<td align="center">库存数量</td>
				<td align="center">样机库存数量</td> 
				<td align="center">未入库数量</td>
				<td align="center">销售数量</td>
				<td align="center">动销率</td>
			</tr>  
   <tr style="width:100%; height:40px;"></tr>
			<%
			int count = 0;
			int allnum = 0 ;
			int modelnum = 0 ;
			int outnum = 0 ; 
			int salenum = 0 ; 
				if (null != map && !map.isEmpty()) {
					    Collection<SNInventory> li = map.values();
						Inventoryomparator c = new Inventoryomparator();
						List list = Arrays.asList(li.toArray());
						Collections.sort(list, c);    
						if (!list.isEmpty()) {
							Iterator<SNInventory> it = list.iterator();
							while(it.hasNext()){ 
							  String tname = "";
								SNInventory in = it.next();
								try{
									tname = ProductService.gettypeNUmmap().get(in.getGoodNum()).getType();
								}catch(Exception e){
									tname = in.getGoodpName();
								}
								 
								allnum += in.getNum() ;
								modelnum += in.getModelnum() ;
								outnum += in.getIncommonnum();
								salenum += in.getSaleNum() ;
								
								count ++; 
								//String tname  = ProductService.gettypeNUmmap().get(in.getGoodNum()).getType();
							    
			%>  
			<tr class="asc"  ondblclick="windorm('<%=starttime%>','<%=endtime%>','<%=tname%>')">
				<td align="center"><%=count%></td>
				   
				<td align="center"><%=tname%></td>
				<td align="center"><%=in.getGoodNum() %></td>
				<td align="center"><%=in.getNum()%></td>
					<td align="center"><%=in.getModelnum()%></td>
				<td align="center"><%=in.getIncommonnum() %></td>
				<td align="center"><%=in.getSaleNum()%></td>
				<td align="center"><%=in.getDynamic()%></td>

			</tr>  
			<%
				}  
							}
						}
			%>
		 
				<tr class="asc" > 
				<td align="center"><%=count%></td>
				<td align="center">总计</td> 
 <td align="center"></td>
				 
				<td align="center"><%=allnum%></td>
				<td align="center"><%=modelnum%></td>  
				<td align="center"><%=outnum%></td> 
				<td align="center"><%=salenum%></td>  
				<td align="center"><%=salenum*100/(salenum+allnum+modelnum+outnum)%></td>

			</tr>		 
						 

 
		</table>
		</div>
	</div>

</body>
</html>