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
			         data:"method=InitInventoryInOrder&time="+dates, 
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
 
 
 
</script>
</head>
    
<body>  
	<!--   头部结束   -->  
	<div class="table-list"> 
<form action="inventoryIn.jsp" >       
<input type="hidden" name="method" value="all"/> 
 <table width="100%" height="100%"  border="0" cellspacing="0" cellpadding="0"  id="table">
  
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
  
   
  <%    
  if("all".equals(method)){
	  %><tr> 
    <td>订单号</td>
      <td>开始日期</td>
      <td>过期日期</td>
    </tr>
	  <% 
	  String starttime =  time+"-"+TimeUtill.getStartday(1);
	         
	     int max = TimeUtill.getMax(time); 
		 String endtime =time + "-"+max ;       
		 LinkedHashMap<String,List<OrderSN>>  map = OrderSNManager.getMapAllOrderSn(starttime,endtime);
		      
		 if(null != map){       
			 Iterator<Map.Entry<String,List<OrderSN>>> iter = map.entrySet().iterator(); 
			   
			while(iter.hasNext()){    
				 Map.Entry<String,List<OrderSN>> entry =iter.next(); 
				 Object key = entry.getKey();  
				 List<OrderSN> val = entry.getValue(); 
  
				OrderSN init = val.get(0);
				%>  
				<tr>    
				  <td><%=key %></td>
				  <td><%=init.getStarttime() %></td>
				  <td><%=init.getEndtime() %></td>
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
