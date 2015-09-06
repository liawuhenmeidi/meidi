<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8");      
User user = (User)session.getAttribute("user"); 

TokenGen.getInstance().saveToken(request);
 
String token = (String)session.getAttribute("token"); 
String num = request.getParameter("salenum");
if(StringUtill.isNull(num)){
	num= "15";  
}
List<String> listbranchp = BranchService.getListStr();  
String listall = StringUtill.GetJson(listbranchp);  
List<BranchType> listgt = BranchTypeManager.getLocate(); 
 
String endtime = TimeUtill.getdateString();   
endtime = TimeUtill.dataAdd(endtime, -1);
String starttime = TimeUtill.dataAdd(endtime, -Integer.valueOf(num));
   
String branch = user.getBranchName();
String branchtype = BranchService.getMap().get(Integer.valueOf(user.getBranch())).getBranchtype().getSaletype()+"";
 
String json  = "[]";   
if(!StringUtill.isNull(branchtype)){ 
	List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
	json = StringUtill.GetJson(listallp); 
}


// System.out.println("type"+type);  
// 门店  ，型号   
     Map<String,List<SNInventory>> map = new LinkedHashMap<String,List<SNInventory>>(); 
	 
	Collection<SNInventory> listend = InventoryChange.get(TimeUtill.dataAdd(endtime, 1));
//	System.out.println("listend"+listend.size());
	//Map<String, Inventory> mapstart = InventoryChange.changeMap(listend );
	 // 样机   
	 Map<String,SNInventory> mapModel = InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1));
	       
	 // 销量    
	Map<String,SNInventory> mapsale = SaleDownLoad.getMap(starttime,endtime); 
	 
	// 未入库数量    
	Map<Integer,Map<String,InventoryBranch>> mapout = InventoryBranchManager.getmapType(user);
	   
	//System.out.println("mapout"+mapout); 
	//Collection<Inventory> sales = SaleDownLoad.get(starttime, endtime);
 
	if(!listend.isEmpty()){  
		 Iterator<SNInventory> it = listend.iterator();
		 while(it.hasNext()){     
			 SNInventory inve = it.next();
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
	  
	SNInventory sale =  mapsale.get(key);
	SNInventory model = mapModel.get(key);
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
   
		 if(!StringUtill.isNull(branch)){  
	 String branchnum = BranchService.getNameMap().get(branch).getEncoded(); 
	 keyin = branchnum ; 
	 if(branchnum.equals(bnum)){ 
		 List<SNInventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<SNInventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	 }
	 
		 }else {
	 keyin = bnum ;
	 
		 List<SNInventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<SNInventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	
		 } 

		 } 
	}
	 
	 
	 if(!mapsale.isEmpty()){
		 Set<Map.Entry<String,SNInventory>> set= mapsale.entrySet();
		 Iterator<Map.Entry<String,SNInventory>>  it = set.iterator();
		 while(it.hasNext()){
	 Map.Entry<String,SNInventory> mapent =  it.next();
	 SNInventory inve = mapent.getValue();
	 String keyin = "";
	 String bnum = "";
             if("常规机库".equals(inve.getBranchName())){
             	 bnum = "1";
             }else if("特价机库".equals(inve.getBranchName())){
             	 bnum = "2";
            }else { 
         	   bnum = StringUtill.getStringNocn(inve.getBranchName());
            } 	
 
		 if(!StringUtill.isNull(branch)){
	 String branchnum = BranchService.getNameMap().get(branch).getEncoded(); 
	 keyin = branchnum ; 
	 if(branchnum.equals(bnum)){ 
		 List<SNInventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<SNInventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	 }
	  
		 }else {
	 keyin = bnum ;
	 
		 List<SNInventory> li = map.get(keyin);
	if(null == li){ 
		li = new ArrayList<SNInventory>();
		map.put(keyin, li);
	}
	 
	li.add(inve);
	
		 } 

		 }
		 
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
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 



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
		
		$("#salenum").val(<%=num%>);
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
		<div class="weizhi_head">现在位置：<%=user.getBranchName() %>>>智能要货</div>
		<form action="" method="post">
		<select name="salenum" id="salenum">
		 <option value="15" >半月销量</option>
		<option value="30" >一月销量</option>
		</select>
		  
		<input type="submit" value="查看">
		</form> 
		 
		
	 <form action="../OrderGoodsServlet"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="method" value="add"/>  
      <input type="hidden" name="token" value="<%=token%>"/>  
      
      <input type="hidden" name="remark" value="智能要货生成订单"/> 
      
		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">  
				<td align="center" width="5%">序号</td> 
				<td align="center" width="30%">商品名称</td>  
				<td align="center" width="10%">卖场库存数量</td>
				<td align="center" width="10%">样机库存数量</td>
				<td align="center" width="10%">未入库数量</td>
				<td align="center" width="10%">销售数量</td>
				<td align="center" width="25%">要货数量</td>
			</tr>
 
			<%
				int count = 0;
			    List<Integer> list = new ArrayList<Integer>();  
			     
				if (!map.isEmpty()) {
					Set<Map.Entry<String, List<SNInventory>>> set = map.entrySet();
					Iterator<Map.Entry<String, List<SNInventory>>> it = set
							.iterator(); 
					while (it.hasNext()) { 
						Map.Entry<String, List<SNInventory>> mapen = it.next();
						List<SNInventory> li = mapen.getValue();
						Inventoryomparator c = new Inventoryomparator();
						Collections.sort(li, c);
						if (null != li) {
							for (int i = 0; i < li.size(); i++) {
								SNInventory in = li.get(i);
								
										
								
								
								
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
                                int realcont = in.getSaleNum() - (in.getNum()+in.getOutnum()) ;
								if (branch.equals(bname) && realcont > 0 ) {
									String cl = "class=\"asc\"";
									int pid = 0 ;
									count++; 
									
									try{
										pid= ProductService.gettypemap(user, bname).get(in.getGoodpName()).getId();
										
								 		list.add(count);
								 		
									}catch(Exception e){
									  pid = 0 ; 
									  cl="class=\"bsc\"";
									}
									
									
							 		
							 		
			%>  
			<tr <%=cl %>>       
				<td align="center"><%=count%></td> 
				<td align="center" ><input name="product<%=count %>" type="hidden" value="<%=in.getGoodpName()%>"><%=in.getGoodpName()%></td>
				<td align="center" ><%=in.getNum()%></td>
				<td align="center"><%=in.getModelnum()%></td> 
				<td align="center"><%=in.getOutnum()%></td>
				<td align="center"><%=in.getSaleNum()%></td> 
				<td align="center" ><input type="hidden" name="statues<%=count %>" value="1"><input type="hidden" name="orderproductNum<%=count %>" value="<%=realcont%>"><%=realcont%></td>

			</tr>
			<%
				}
							}
						}
					}
				}
				 
				String str = list.toString();
				//System.out.println(str); 
			    str = str.substring(1,str.length()-1);
				//System.out.println(str);  
			%> 
  
           <tr class="asc">
           <td colspan="7" align="center"><input type="submit" value="提交"></td>
           
           </tr>

		</table>
		 
		 
		 <input type="hidden" name="rows" id="rows" value="<%=str%>"/>  
		 
		 
		</form>
	</div>

</body>
</html>