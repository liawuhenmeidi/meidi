<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
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
String json  = "[]";    
if(!StringUtill.isNull(branchtype)){ 
	List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
	json = StringUtill.GetJson(listallp);  
}  
// System.out.println("type"+type);   
// 门店  ，型号         
 Map<String,Map<String,SNInventory>> map = InventoryMerger.get(user,branch,"50",starttime,endtime);

//System.out.println(map);
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
		var num = $('input[name="totoal"]:checked').val();

		var start = $("#starttime").val();
		var end = $("#endtime").val();
		 
		if("" == start || null == start){
			 alert("开始时间不能为空");
			 return false;
		} 
		 
		if("" ==  end || null ==  end){
			 alert("结束时间不能为空");
			 return false;
		}

		if(1 == num){
			$("#post").attr("action","inventoryDynamictype.jsp");
		}
		  
 
		return true;
	}
</script> 
</head>
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include> 
		<div class="weizhi_head">现在位置：动销率</div>
		<div class="main_r_tianjia">
   <ul>                                                                                                          
     <li><a href="javascript:history.go(-1);">返回</a></li>
     </ul>    
   </div> 
   
		<form action="inventoryDynamic.jsp" method="post" id="post"
			onsubmit="return checked()">
			<table cellpadding="1" cellspacing="0">
				<tr>
					<td>销售系统： <select id="branchtype" name="branchtype">
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

					<td>开始时间</td>
					<td><input name="starttime" type="text" id="starttime"
						value="<%=starttime%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						readonly="readonly" /></td>

					<td>结束时间</td>
					<td><input name="endtime" type="text" id="endtime" size="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						value="<%=TimeUtill.dataAdd(TimeUtill.getdateString(), -1)%>"
						maxlength="10" readonly="readonly" />
					</td>

					<td>门店</td>
					<td><input type="text" name="branch" id="branch"
						value="<%=branch%>" readonly="readonly"/></td>
						<!-- 
					<td>型号</td>

					<td><input type="text" name="product" id="product"
						value="<%=type%>" /></td> 
						 -->
					<td><input type="checkbox" value="1" name="totoal"></input> 型号统计</td>

					<td><input type="submit" value="查询" />
					</td>
				</tr>
 
			</table>
		</form>
		<!--  头 单种类  -->
 
		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">
				<td align="center">序号</td>
				<td align="center">库位名称</td>

				<td align="center">商品名称</td>
				<td align="center">商品编码</td>
				<td align="center">库存数量</td>
				<td align="center">样机库存数量</td>
				<td align="center">未入库数量</td>
				<td align="center">销售数量</td> 
				<td align="center">动销率</td> 
			</tr>

			<%
			 int count = 0 ;  
	          int outnumall = 0 ; 
	  		 int inmodelnumall = 0 ;    
	  		 int outmodelnumall = 0 ;   
	  		 int snnumall = 0 ;
	  		 int snbadall = 0 ;  
	  		 int ccount = 0 ; 
	  	     int allNomention  = 0 ;
	  	     int  salenumall = 0 ; 
	  	     List<SNInventory> li =new ArrayList<SNInventory>(); ;
				if (!map.isEmpty()) {  
					Set<Map.Entry<String,Map<String,SNInventory>>> set = map.entrySet();
					Iterator<Map.Entry<String,Map<String,SNInventory>>> it = set
							.iterator();
					while (it.hasNext()) { 
						Map.Entry<String,Map<String,SNInventory>> mapen = it.next();
						String tnum = mapen.getKey(); 
						Map<String,SNInventory> mapt = mapen.getValue();
						Collection<SNInventory> co  = mapt.values();
						li.addAll(co);
						
						//System.out.println(li.size());
						 
					    
						//Collections.sort(li, c);  
						//System.out.println(li);
						//System.out.println(12); 
					}
				} 
						if (null != li) { 
							Inventoryomparator c = new Inventoryomparator();
							Collections.sort(li, c); 
							
							for (int i = 0; i < li.size(); i++) {
								SNInventory in = li.get(i);
								//bnum = StringUtill.getStringNocn(in.getBranchName()); 
						 
			            		String tname = in.getGoodpName();
			            	    String tnum = in.getGoodNum();
			            		 try {   
			            			 if(StringUtill.isNull(tname)){
			            				   
			            				 tname = in.getProduct().getType(); 
			            				 tnum = in.getProduct().getEncoded();
			            			 }
			      					 
			      				} catch (Exception e) {
			      				 
			      				
			      				} 
			            		 
			            		 
								 int num = in.getNum();
			            		 snnumall+= num ;  
			            		 
			            		 int Nomention = in.getNomention();
			            		 allNomention += Nomention;
			            		 
			            		 
			            		 int inmodelnum = in.getModelnum();
			            		 inmodelnumall += inmodelnum;
			            		 
			            		 int badnum = in.getBadnum();
			            		 snbadall += badnum; 
			            		  
			            		int outnum = in.getIncommonnum();
			            		outnumall += outnum ;
			            		 
			            		int outmodel = in.getInmodelnum();
			            		outmodelnumall += outmodel; 
			            		
			            		 int salenum = in.getSaleNum();
			            		 salenumall += salenum;
									count++;
									 
									
			%>
			<tr class="asc">
				<td align="center"><%=count%></td>
				<td align="center"><%=branch%></td>
  
				<td align="center"><%=tname%></td>
				<td align="center"><%=tnum %></td>  
				<td align="center"><%=num%></td> 
				<td align="center"><%=inmodelnum%></td>
				<td align="center"><%=outnum+outmodel%></td>
				<td align="center"><%=salenum%></td>
				<td align="center"><%=in.getDynamic()%></td>

			</tr>
			<%
							}
						}
				
			%>
<tr class="asc">
				<td align="center"> </td>
				<td align="center"></td>
  
				<td align="center"></td> 
				<td align="center"></td>  
				<td align="center"><%=snnumall%></td>
				<td align="center"><%=inmodelnumall%></td>
				<td align="center"><%=outnumall%></td>
				<td align="center"><%= salenumall%></td>
				<td align="center"></td> 

			</tr>


		</table>
		</form>
	</div>

</body>
</html>