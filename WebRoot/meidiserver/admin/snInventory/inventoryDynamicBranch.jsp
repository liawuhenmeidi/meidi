<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%   
	request.setCharacterEncoding("utf-8");       
User user = (User)session.getAttribute("user");  
    
String starttime = request.getParameter("starttime");
String endtime = request.getParameter("endtime");   
String branchtype = request.getParameter("branchtype");
String type = request.getParameter("product"); 
String tnum = "";
try{     
//System.out.println(Integer.valueOf(branchtype));
// System.out.println(ProductService.gettypemap(user, Integer.valueOf(branchtype)));
tnum = ProductService.gettypemap(user, Integer.valueOf(branchtype)).get(type).getEncoded(); 

}catch (Exception e){
//e.printStackTrace();
}    
//System.out.println("****tnum"+tnum);
List<BranchType> listgt = BranchTypeManager.getLocate();  

String json  = "[]";    
if(!StringUtill.isNull(branchtype)){   
	List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
	json = StringUtill.GetJson(listallp); 
} 
// System.out.println("type"+type);     
    Map<String,SNInventory> map  = null;
if(!StringUtill.isNull(starttime) && !StringUtill.isNull(endtime)){
	map = InventoryMerger.getBranch(user,"50",branchtype,type,starttime,endtime);
	//System.out.println("mapsale"+mapsale.size());    
}  
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

	var json =<%=json%>; 
	$(function() { 
		  
		$("#branchtype").val(<%=branchtype%>);
		
		 
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
		<div class="weizhi_head">现在位置：动销率
		
       &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;  &nbsp;                                                                                                   
     <a href="javascript:history.go(-1);">返回</a>
   
   </div> 
		<form action="inventoryDynamicBranch.jsp" method="post" id="post"
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

					<td>型号</td>

					<td><input type="text" name="product" id="product"
						value="<%=type%>" /></td>
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
<!--<td align="center">商品名称</td>
				<td align="center">商品编码</td>  -->
				 
				<td align="center">库存数量</td>
				<td align="center">样机库存数量</td>
				<td align="center">未入库数量</td>
				<td align="center">销售数量</td> 
				<td align="center">动销率(%)</td> 
			</tr>
 
			<%
			  // System.out.println(map.size());

				int count = 0;
				int allnum = 0 ;
				int modelnum = 0 ;
				int outnum = 0 ; 
				int salenum = 0 ;  
				if (null != map && !map.isEmpty()) {   
					 Collection<SNInventory> co = map.values();
					// System.out.println(map.keySet()) ; 
					    SNInventory[] array =new SNInventory[co.size()];   
						SNInventory[] lis = co.toArray(array); 
						List<SNInventory> li = Arrays.asList(lis); 
						Inventoryomparator c = new Inventoryomparator();
						Collections.sort(li, c); 
						if (null != li) {     
							for (int i = 0; i < li.size(); i++) {
								SNInventory in = li.get(i);
								//bnum = StringUtill.getStringNocn(in.getBranchName()); 
								String bname = "";
								try{      
									bname = BranchService.getMap().get(in.getBranchid()).getLocateName();
						//System.out.println("*"+bname);			
								}catch(Exception e){ 
					//System.out.println("**"+in.getBranchName());					
									String bnum = StringUtill.getStringNocn(in.getBranchName());
									if(StringUtill.isNull(bnum)){  
										bname = in.getBranchName();      
					//System.out.println(StringUtill.GetJson(in)+"**"+bname+"**"+in.getBranchid());						
									}else {
										try{ 
											bname = BranchService.getNumMap(SaleModel.SuNing).get(bnum).getLocateName();
				//System.out.println("***"+bname);
										}catch(Exception em){
										   bname = in.getBranchName();
				//System.out.println("****"+bname);
										}
									}
								}  
					 			
//System.out.println("bname******"+bname);
									count++;
								allnum += in.getNum() ;
								modelnum += in.getModelnum() ;
								outnum += in.getIncommonnum();
								salenum += in.getSaleNum() ;
									
									
									 
									  
			%>    
			<tr class="asc" ondblclick="window.location.href='inventoryDynamic.jsp?starttime=<%=starttime%>&endtime=<%=endtime%>&branch=<%=bname%>'"> 
				<td align="center"><%=count%></td>
				<td align="center"><%=bname%></td> 
 
				
				<td align="center"><%=in.getNum()%></td>
				<td align="center"><%=in.getModelnum()%></td>
				<td align="center"><%=in.getIncommonnum()%></td>
				<td align="center"><%=in.getSaleNum()%></td>
				<td align="center"><%=in.getDynamic()%></td>

			</tr>
			<%
							} 
						} 
						
						
						%>
						
				<tr class="asc" > 
				<td align="center"><%=count%></td>
				<td align="center">总计</td> 
 
				 
				<td align="center"><%=allnum%></td>
				<td align="center"><%=modelnum%></td> 
				<td align="center"><%=outnum%></td> 
				<td align="center"><%=salenum%></td>  
				<td align="center"><%=salenum*100/(salenum+allnum+modelnum+outnum)%></td>

			</tr>		 
						
						
						<%
				}
			%>



		</table>
		</form>
	</div>

</body>
</html>