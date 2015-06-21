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
String time = TimeUtill.getdateString();  
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
 Map<String,Map<String,SNInventory>> map = InventoryMerger.get(user,branch,"50",starttime,endtime,null);

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
	  var tids = new Array();
	  var winPar = null; 
	  var branch = "<%=branch%>";
	  var branchtype = "<%=branchtype%>"; 
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
	
	
	function addtid (tid){  
		if(null == tid || "" == tid){
			tid = $("#product").val();
			//alert(tname); 
		} 
		// var data = new  Date(); 
		 //data.setTime(data.getTime()+(60*12 * 60 * 1000));
		// alert(data);  
		// $.cookie("inventorycheck", tid,{ expires: data });   
		 tids.push(tid);      
		 $("#l"+tid).css("display","none");
		  
		 //    
		 if(null == winPar){  
			 winPar = window.open("../ordergoods/ordergoodsupdatecookie.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype+"&time=<%=time%>","time","resizable=yes,modal=yes,scroll=no,width="+screen.width*0.8+",top="+(screen.height-300)/2+",left="+(screen.width*0.1)+",height=400px,dialogTop:0px,scroll=no");  	
		 }else {     
			 if(winPar.closed){    
				 window.location.reload();
				 //tids = new Array();  
				// tids.push(tid);      
				      
				//  alert($("#"+tid));   
				// $("#"+tid).attr("class","bsc");
				// $("#l"+tid).css("display","none");
				   
				// winPar = window.open("../ordergoods/ordergoodsupdate.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype,"time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	 
				 //winPar.location.href="../ordergoods/ordergoodsupdate.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype; 
			 }else {  
				 //winPar.location.reload();
				 winPar.close();  
				 winPar = window.open("../ordergoods/ordergoodsupdatecookie.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype+"&time=<%=time%>","time","resizable=yes,modal=yes,scroll=no,width="+screen.width*0.8+",top="+(screen.height-300)/2+",left="+(screen.width*0.1)+",height=400px,dialogTop:0px,scroll=no");  	 
			 }
			 
		 }
	   
	 }
	
	
</script> 
</head>
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include> 
		<div class="weizhi_head">现在位置：动销率
		&nbsp;&nbsp;&nbsp;
		<a href="javascript:history.go(-1);"><font
					style="color:blue;font-size:20px;">返回</font>
			</a> 
		</div>     
		<form action="inventoryDynamic.jsp" method="post" id="post"
			onsubmit="return checked()"> 
			<table cellpadding="1" width="100%" cellspacing="0">
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

					<td>开始时间:<input name="starttime" type="text" id="starttime"
						value="<%=starttime%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						readonly="readonly" /></td>

					<td>结束时间:<input name="endtime" type="text" id="endtime" size="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						value="<%=TimeUtill.dataAdd(TimeUtill.getdateString(), -1)%>"
						maxlength="10" readonly="readonly" />
					</td>

					<td>门店:<input type="text" name="branch" id="branch"
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
  <div style="width:100%; height:400px; overflow:scroll;" >      
		<table     cellspacing="1" id="Ntable" > 
			<tr style="position:fixed;height:40px;width:97.5%;" class="dsc" >
				<td align="center">序号</td>
				<td align="center">库位名称</td>
 
				<td align="center">商品名称</td>
				<td align="center">商品编码</td>
				<td align="center">库存数量</td>
				<td align="center">样机库存数量</td>
				<td align="center">未入库数量</td>
				<td align="center">销售数量</td> 
				<td align="center">动销率</td> 
				<td align="center">调账调拨</td>
			</tr> 
<tr style="width:100%; height:40px;"></tr>
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
			            	    int tid = 0 ;  
			            	    boolean flag= true ;
			            		 try {   
			            			 if(StringUtill.isNull(tname)){
			            				   
			            				 tname = in.getProduct().getType(); 
			            				 tnum = in.getProduct().getEncoded();
			            				 tid = in.getProduct().getId(); 
			            			 }else { 
			            				 tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
			           							.getId();
			            			 }
			      					 
			      				} catch (Exception e) {
			      				 flag = false ;
			      				
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
				<td align="center">  
            		    <% if(flag){  
            		    	%>
            		    	<label id="l<%=tid %>"  onclick="addtid('<%=tid %>')">[调拨单]</label>
            		    	<%
            		    } %>

            		    </td>  
             
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
				<td align="center"></td> 

			</tr> 


		</table> 
		</div> 
	</div>

</body>
</html>