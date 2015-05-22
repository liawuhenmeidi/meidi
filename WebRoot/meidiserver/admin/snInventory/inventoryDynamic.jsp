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
Map<String,List<Inventory>> map = new LinkedHashMap<String,List<Inventory>>();
 
if(!StringUtill.isNull(starttime) && !StringUtill.isNull(endtime)){
	 
	Collection<Inventory> listend = InventoryChange.get(TimeUtill.dataAdd(endtime, 1));
//	System.out.println("listend"+listend.size());
	//Map<String, Inventory> mapstart = InventoryChange.changeMap(listend );
	 // 样机   
	 Map<String,Inventory> mapModel = InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1));
	      
	 // 销量    
	Map<String,Inventory> mapsale = SaleDownLoad.getMap(starttime,endtime);  
	// 未入库数量    
	 Map<Integer,Map<String,InventoryBranch>> mapout = InventoryBranchManager.getmapType(user);
	   
	//System.out.println("mapout"+mapout); 
	//Collection<Inventory> sales = SaleDownLoad.get(starttime, endtime);
 
	if(!listend.isEmpty()){  
		 Iterator<Inventory> it = listend.iterator();
		 while(it.hasNext()){     
	 Inventory inve = it.next();
	 String bnum = "";
             if("常规机库".equals(inve.getBranchName())){
             	 bnum = "1";
             }else if("特价机库".equals(inve.getBranchName())){
             	 bnum = "2";
            }else { 
         	   bnum = StringUtill.getStringNocn(inve.getBranchName());
            } 	
             
	String key = bnum+"_"+inve.getGoodNum();  
	 
	int bid = 0 ; 
	String tname = "" ;
	
	try{ 
		bid = BranchService.getNumMap(SaleModel.SuNing).get(bnum).getId();
	}catch (Exception e){ 
		//e.printStackTrace();
		bid = 0 ;
	}  
	try{ 
		tname  = ProductService.gettypeNUmmap().get(inve.getGoodNum()).getType();
	}catch (Exception e){ 
		tname  = "" ;
		//e.printStackTrace();
	} 
		// logger.info(key);    
	  
	Inventory sale =  mapsale.get(key);
	Inventory model = mapModel.get(key);
	InventoryBranch inout = null ; 
	try{  
		inout = mapout.get(bid).get(tname);  
	}catch (Exception e){
		//e.printStackTrace();
		inout = null;
	} 
	
	// System.out.println("sale"+sale);  
	 if(null !=sale){   
		//System.out.println("sale"+sale);
		inve.setSaleNum(sale.getSaleNum());
		mapsale.remove(key);
	 }
	 
	 if(null != model){
		 inve.setModelnum(model.getNum());
		 mapModel.remove(key);
	 } 
	    
	 if(null != inout){ 
		inve.setOutnum(inout.getPapercount());
	 }  
	  
	// System.out.println("key"+key);
	 
	 String keyin = "";
	    
	 if(StringUtill.isNull(type)){ 
   
		 if(!StringUtill.isNull(branch)){  
	 String branchnum = BranchService.getNameMap().get(branch).getEncoded(); 
	 keyin = branchnum ; 
	 if(branchnum.equals(bnum)){ 
		 List<Inventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<Inventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	 }
	 
		 }else {
	 keyin = bnum ;
	 
		 List<Inventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<Inventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	
		 } 
		 
	 }else {
		   
		 String tnum = "";
		    
		 try{   
	 //System.out.println(Integer.valueOf(branchtype));
	// System.out.println(ProductService.gettypemap(user, Integer.valueOf(branchtype)));
	 tnum = ProductService.gettypemap(user, Integer.valueOf(branchtype)).get(type).getEncoded(); 
		 }catch (Exception e){
	//e.printStackTrace();
		 }  
		// System.out.println(" tnum "+ tnum );
		 if(tnum.equals(inve.getGoodNum())){
	 if(!StringUtill.isNull(branch) ){
		 String branchnum = BranchService.getNameSNMap().get(branch).getEncoded(); 
		 keyin = tnum ;
		 if(branchnum.equals(bnum)){ 
	 List<Inventory> li = map.get(keyin);
		if(null == li){ 
			li = new ArrayList<Inventory>();
			map.put(keyin, li);
		}
		 
		li.add(inve);
		 }
		 
	 }else {
		     keyin = tnum ;
		   
	 List<Inventory> li = map.get(keyin);
		if(null == li){ 
			li = new ArrayList<Inventory>();
			map.put(keyin, li);
		}
		 
		li.add(inve);
		
	 } 
		 } 
	 }	
		 } 
	}
	 
	
	 if(!mapsale.isEmpty()){
		 Set<Map.Entry<String,Inventory>> set= mapsale.entrySet();
		 Iterator<Map.Entry<String,Inventory>>  it = set.iterator();
		 while(it.hasNext()){
	 Map.Entry<String,Inventory> mapent =  it.next();
	 Inventory inve = mapent.getValue();
	 String keyin = "";
	 String bnum = "";
             if("常规机库".equals(inve.getBranchName())){
             	 bnum = "1";
             }else if("特价机库".equals(inve.getBranchName())){
             	 bnum = "2";
            }else { 
         	   bnum = StringUtill.getStringNocn(inve.getBranchName());
            } 	
               
	 if(StringUtill.isNull(type)){
 
		 if(!StringUtill.isNull(branch)){
			 String branchnum = BranchService.getNameMap().get(branch).getEncoded(); 
	 keyin = branchnum ; 
	 if(branchnum.equals(bnum)){ 
		 List<Inventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<Inventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	 }
	  
		 }else {
	 keyin = bnum ;
	 
		 List<Inventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<Inventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	
		 } 
		 
	 }else {
		 
		 String tnum = "";
		 
		 try{ 
	 tnum = ProductService.gettypemap(user, Integer.valueOf(branchtype)).get(type).getEncoded(); 
		 }catch (Exception e){
	 tnum = "";
		 } 
		 
		 if(tnum.equals(inve.getGoodNum())){
	 if(!StringUtill.isNull(branch) ){
		 String branchnum = "";
		 try{ 
	 branchnum = BranchService.getNameSNMap().get(branch).getEncoded(); 
		 }catch (Exception e){
	 branchnum = "";
		 }
		
		 keyin = tnum ;
		 if(branchnum.equals(bnum)){ 
	 List<Inventory> li = map.get(keyin);
		if(null == li){ 
			li = new ArrayList<Inventory>();
			map.put(keyin, li);
		}
		 
		li.add(inve);
		 }
		 
	 }else {
		     keyin = tnum ;
		   
	 List<Inventory> li = map.get(keyin);
		if(null == li){ 
			li = new ArrayList<Inventory>();
			map.put(keyin, li);
		}
		 
		li.add(inve);
		
	 } 
		 } 
	 }	
		 }
		 
	 } 
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
		<div class="weizhi_head">现在位置：查看明细</div>
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
						value="<%=branch%>" /></td>
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

				<td align="center">商品名称</td>
				<td align="center">商品编码</td>
				<td align="center">库存数量</td>
				<td align="center">样机库存数量</td>
				<td align="center">未入库数量</td>
				<td align="center">销售数量</td> 
				<td align="center">动销率</td> 
			</tr>

			<%
				int count = 0;
				if (!map.isEmpty()) {
					Set<Map.Entry<String, List<Inventory>>> set = map.entrySet();
					Iterator<Map.Entry<String, List<Inventory>>> it = set
							.iterator();
					while (it.hasNext()) {
						Map.Entry<String, List<Inventory>> mapen = it.next();
						List<Inventory> li = mapen.getValue();
						Inventoryomparator c = new Inventoryomparator();
						Collections.sort(li, c);
						if (null != li) {
							for (int i = 0; i < li.size(); i++) {
								Inventory in = li.get(i);
								String bnum = StringUtill.getStringNocn(in
										.getBranchName());
								String bname = "";
								if (StringUtill.isNull(bnum)) {
									bnum = in.getBranchNum();
								}

								if (null != BranchService
										.getNumMap(SaleModel.SuNing)) {
									if (null != BranchService.getNumMap(
											SaleModel.SuNing).get(bnum)) {
										bname = BranchService
												.getNumMap(SaleModel.SuNing)
												.get(bnum).getLocateName();
									}
								}

								if (!StringUtill.isNull(branch)
										&& branch.equals(bname)
										|| StringUtill.isNull(branch)) {
									count++;
									
									
			%>
			<tr class="asc">
				<td align="center"><%=count%></td>
				<td align="center"><%=bname%></td>
  
				<td align="center"><%=in.getGoodpName()%></td>
				<td align="center"><%=in.getGoodNum() %></td> 
				<td align="center"><%=in.getNum()%></td>
				<td align="center"><%=in.getModelnum()%></td>
				<td align="center"><%=in.getOutnum()%></td>
				<td align="center"><%=in.getSaleNum()%></td>
				<td align="center"><%=in.getDynamic()%></td>

			</tr>
			<%
				}
							}
						}
					}
				}
			%>



		</table>
		</form>
	</div>

</body>
</html>