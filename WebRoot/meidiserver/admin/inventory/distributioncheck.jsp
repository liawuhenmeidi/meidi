<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,branchtype.*,inventory.InventoryBranch,inventory.InventoryBranchManager,product.*,comparator.*,branch.*,enums.*,httpClient.download.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<% 
	request.setCharacterEncoding("utf-8");       
User user = (User)session.getAttribute("user");   
String category = request.getParameter("category");
String endtime = TimeUtill.getdateString(); 
   
// System.out.println("type"+type);   
// 门店  ，型号      
Map<String,SNInventory> map = new HashMap<String,SNInventory>(); 

	 // 库存 
	Collection<SNInventory> listend = InventoryChange.get(endtime);
//	System.out.println("listend"+listend.size());
	//Map<String, SNInventory> mapstart = SNInventoryChange.changeMap(listend );
	 // 样机    
	 Map<String,SNInventory> mapModel = InventoryModelDownLoad.getMap(user, endtime);
	          
	 // 销量     
	// 未入库数量        
	  
	Map<Integer,Map<String,InventoryBranch>> mapout = InventoryBranchManager.getmapType(user,CategoryService.getmap().get(Integer.valueOf(category)));
	    
	//System.out.println("mapout"+mapout); 
	//Collection<SNInventory> sales = SaleDownLoad.get(starttime, endtime);
   
	if(null != listend && !listend.isEmpty()){  
		 Iterator<SNInventory> it = listend.iterator();
		 while(it.hasNext()){     
	 SNInventory inve = it.next();
	 String bnum = "";
             if("常规机库".equals(inve.getBranchName())){
             	 bnum = "1";
             }else if("特价机库".equals(inve.getBranchName())){
             	 bnum = "2";
            }else { 
         	   bnum = StringUtill.getStringNocn(inve.getBranchName()).trim();
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
	 
	SNInventory model = mapModel.get(key);
	InventoryBranch inout = null ; 
	
	try{  
		inout = mapout.get(bid).get(tname); 
		mapout.get(bid).remove(tname);
	}catch (Exception e){
		//e.printStackTrace();
		inout = null;
	}  
	// System.out.println("sale"+sale);  
	
	 
	 if(null != model){
		 inve.setModelnum(model.getModelnum());
		 mapModel.remove(key);
	 } 
	    
	 if(null != inout){ 
		inve.setOutnum(inout.getPapercount());
	 }  
	  
	// System.out.println("key"+key);
	 
	 String keyin = "";
	 keyin = bnum ;
	
	 
	SNInventory li = map.get(keyin);
	if(null == li){  
		map.put(keyin, inve);
	}else {
		li.setNum(li.getNum()+inve.getNum());
		li.setModelnum(li.getModelnum()+inve.getModelnum());
		li.setSaleNum(li.getSaleNum()+inve.getSaleNum());
		li.setOutnum(li.getOutnum()+inve.getOutnum());
	}
	
		 } 
	}
	 
	
	 if(!mapModel.isEmpty()){
		 Set<Map.Entry<String,SNInventory>> set= mapModel.entrySet();
		 Iterator<Map.Entry<String,SNInventory>>  it = set.iterator();
		 while(it.hasNext()){
	 Map.Entry<String,SNInventory> mapent =  it.next();
	 SNInventory inve = mapent.getValue();
	 InventoryBranch inout = null ; 
	 String keyin = ""; 
	 String bnum = ""; 
             if("常规机库".equals(inve.getBranchName())){
             	 bnum = "1";
             }else if("特价机库".equals(inve.getBranchName())){
             	 bnum = "2";
            }else {   
         	   bnum = inve.getBranchNum().trim(); 
            } 	  
             
             
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
		try{  
			inout = mapout.get(bid).get(tname); 
			mapout.get(bid).remove(tname); 
		}catch (Exception e){
			//e.printStackTrace();
			inout = null;
		}  
		
		 if(null != inout){ 
				inve.setOutnum(inout.getPapercount());
			 }  
	 
      // System.out.println(bnum);        
	 keyin = bnum ;
	  
	SNInventory li = map.get(keyin);
	if(null == li){ 
		map.put(keyin, inve);
	}else {
		li.setNum(li.getNum()+inve.getNum());
		li.setModelnum(li.getModelnum()+inve.getModelnum());
		li.setSaleNum(li.getSaleNum()+inve.getSaleNum());
		li.setOutnum(li.getOutnum()+inve.getOutnum());
	}
		 } 
		 
	 } 
	 
	 
	 if(!mapout.isEmpty()){
		 System.out.println(mapout.size());
		 Set<Map.Entry<Integer,Map<String,InventoryBranch>>> set = mapout.entrySet();
		 Iterator<Map.Entry<Integer,Map<String,InventoryBranch>>> it = set.iterator();
		 while(it.hasNext()){
			 Map.Entry<Integer,Map<String,InventoryBranch>> mapent =  it.next();
			 int bid = mapent.getKey();
			 String keyin = BranchService.getMap().get(bid).getEncoded();
			 Map<String,InventoryBranch> mapinb = mapent.getValue();
			 Collection<InventoryBranch> co = mapinb.values();
			 Iterator<InventoryBranch> itco = co.iterator();
			 
			// System.out.println(keyin) ;
			 while(itco.hasNext()){  
				 InventoryBranch inco = itco.next();
				 SNInventory li = map.get(keyin);
					if(null == li){ 
						SNInventory inve = new SNInventory();
						inve.setOutnum(inco.getPapercount());
						inve.setGoodNum(inco.getProduct().getEncoded());
						inve.setBranchNum(keyin); 
						map.put(keyin, inve); 
					}else { 
						 
						li.setOutnum(li.getOutnum()+inco.getPapercount());
					}
				 
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



<script type="text/javascript"> 

	
	$(function() { 

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
			$("#post").attr("action","SNInventoryDynamictype.jsp");
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
		<div class="weizhi_head">现在位置：盘点
		
       &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;  &nbsp;                                                                                                   
     <a href="javascript:history.go(-1);">返回</a>
   
   </div>
	
		<!--  头 单种类  -->
 
		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">
				<td align="center">序号</td>
				<td align="center">库位名称</td>
<!--<td align="center">商品名称</td>
				<td align="center">商品编码</td>  -->
				 
				<td align="center">卖场库存数量</td>
				<td align="center">样机库存数量</td>
				<td align="center">未入库数量</td>
				<td align="center">销售数量</td> 
			</tr>

			<%
			  // System.out.println(map.size());

				int count = 0;
				int allnum = 0 ;
				int modelnum = 0 ;
				int outnum = 0 ;
				int salenum = 0 ;
				if (!map.isEmpty()) {   
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
									 
									bname = BranchService 
											.getNumMap(SaleModel.SuNing)
											.get(in.getBranchNum()).getLocateName();
			//System.out.println("*"+bname);  
								}catch(Exception e){   
									try{
										if(!StringUtill.isNull(StringUtill.getStringNocn(in.getBranchName()))){
											bname = BranchService 
													.getNumMap(SaleModel.SuNing)
										 			.get(StringUtill.getStringNocn(in.getBranchName())).getLocateName();
										}else {
											bname = in.getBranchName(); 
										}
										 
										//System.out.println("**"+in.getBranchName()); 
										//System.out.println("**"+bname);						
									}catch(Exception e1){  
										//System.out.println(in.getBranchNum());
										//System.out.println("error:"+bnum+"&&&&&right:"+in.getBranchName());
										bname = in.getBranchName(); 
									}
									
									
									 
								} 
 // System.out.println("***"+bname);
									count++;
								allnum += in.getNum() ;
								modelnum += in.getModelnum() ;
								outnum += in.getOutnum() ;
								salenum += in.getSaleNum() ;
									
									
									
									   
			%>     
			<tr class="asc" ondblclick="window.location.href='inventory2check.jsp?&branch=<%=bname%>&category=<%=category%>'"> 
				<td align="center"><%=count%></td> 
				<td align="center"><%=bname%></td>   
 
				
				<td align="center"><%=in.getNum()%></td>
				<td align="center"><%=in.getModelnum()%></td>
				<td align="center"><%=in.getOutnum()%></td>
				<td align="center"><%=in.getSaleNum()%></td>
			

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
			

			</tr>		 
						
						
						<%
				}
			%>



		</table>
		</form>
	</div>

</body>
</html>