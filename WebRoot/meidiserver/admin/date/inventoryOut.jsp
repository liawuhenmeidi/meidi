<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,goodsreceipt.*,salesn.*,java.io.File,httpClient.download.*,httpClient.download.InventoryModelDownLoad,httpClient.download.InventoryBadGoodsDownLoad,httpClient.download.InventoryChange,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%     
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
List<BranchType> listgt = BranchTypeManager.getLocate();   
String method = request.getParameter("method");  
String time = request.getParameter("mytime"); 
String btype = request.getParameter("branchtype");
   

String type= request.getParameter("type");   

//System.out.println("time"+time);

%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">

#head td {
	white-space: nowrap;
}  
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库存数据</title>
   
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/SimpleCanleder.js"></script>
  
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />  
    
<link rel="stylesheet" href="../../css/SimpleCanleder.css" />
<script type="text/javascript">
 
          
 $(function () {   
	 $(".mytime").simpleCanleder(); 
	 
	 $(".mytime").val("<%=time%>"); 
	 
	 $("#branchtype").val("<%=btype%>");
      
 });         
  
 function getInventoryInAndOut(){
	  
		var dates = $("#mytime").val();
		//alert((dates - date)/1000/60); 
		//if((dates - date)/1000/60<5){  
		//	alert("您的操作过于频繁，请稍后重试");
		//	return ;
		//}   
		//alert(1);  
		$("#mybutton").attr("disabled","true");;
		//alert(dates - date);
	 
		     
			 $.ajax({     
			        type: "post",       
			         url: "../server.jsp",  
			         data:"method=inventoryOutOrder&time="+dates, 
			         dataType: "",            
			         success: function (data) {   
			        	// alert(data.src); 
			        	 
			        		alert("更新完毕");
			            
			        	   
			        	 $("#mybutton").attr("disabled",false); 
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			        // alert(errorThrown); 
			            } 
			           });
		 } 
	  
	  
	  
	  
	  </script>

</head>
    
<body> 
	<!--   头部结束   -->
	<div class="table-list"> 
<form action="inventoryOut.jsp" >       
 
 <table width="100%" height="100%"  border="0" cellspacing="0" cellpadding="0"  id="table">
 <input type="hidden" name="method" value="all"/> 
 <tr class="asc" align="left">
 <td >   
 <table  width=100%>
<tr class="asc" >   
<td  >    
   
  销售系统： <select id="branchtype" name="branchtype">
						<option></option> 
						<%
							if (null != listgt) {
								for (int i = 0; i < listgt.size(); i++) {
									BranchType bt = listgt.get(i);
									if (bt.getTypestatues() == 1) {
							 			
						%>
						<option value="<%=bt.getSaletype()%>"><%=bt.getName()%></option>
						<%  
							}   
								} 
							}
						%>
				</select>
				<input class="mytime" name="mytime" id="mytime" value="" style="paddind-top: 100px; padding-left:100px;"/>
 <input type="submit" value="查看"/>
    

</td>
 </tr>  
 
     
 <tr class="asc" align="left">
     
 <td><input type="button" value="与苏宁系统数据同步" id="mybutton" onclick="getInventoryInAndOut();"></input></td>
 
 </tr>
 </table>
 
 </td>
 </tr>
 <tr class="asc"><td align="center" valign="middle"> 
   
    
 <table width="100%"  border="1" cellpadding="0" cellspacing="0">
    <tr class="dsc">
				<td align="center">退货订单编号</td>
				<td align="center">校验码</td>
				<td align="center">退货订单日期</td>
				<td align="center">退货订单有效期</td>
			</tr>
  <%     
  if("all".equals(method)){ 
	  
	  String starttime =  time+"-"+TimeUtill.getStartday(1);
      
	     int max = TimeUtill.getMax(time); 
		 String endtime =time + "-"+max ;    
	   
	  
	  String branch = request.getParameter("branch");    
	  
	  List<OrderReceipt> list = OrderReceitManager.getListTime(starttime,endtime); 
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
	</tr> 
	<% 
		}
		} 
  }
	%>
 
 
 
 
 </table>
 
 
  </td></tr>
 </table>
 </form>
	</div>
</body>
</html>
