<%@ page language="java" import="java.util.*,utill.*,product.*,ordersgoods.*,order.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");   
String ctype = request.getParameter("ctype");
//System.out.println("ctype"+ctype);
String endtime = request.getParameter("endtime");
endtime = TimeUtill.dataAdd(endtime,1); 
String starttime = request.getParameter("starttime"); 

String branchid = user.getBranch();  
Branch b = BranchService.getMap().get(Integer.valueOf(branchid));
   
String strbranch = b.getLocateName(); 
    
Map<Integer,Branch> branchmap = BranchManager.getIdMap();
List<InventoryBranchMessage > list = InventoryBranchMessageManager.getCategory(ctype,branchid,starttime,endtime);  
//System.out.println("list"+list); 
Map<Integer,User> usermap = UserService.getMapId();  
Map<String,String> mapdevity = InventoryMessage.getDeliveryStatuesMap(); 
  
int papercount = 0 ; 
int realcount = 0 ;

Set<Integer> bidset = new HashSet<Integer>();
for(int i=0;i<list.size();i++){
	 InventoryBranchMessage in =list.get(i); 
	 int bid = in.getBranchid(); 
	 if( "" == branchid ||  null == branchid){ 
		 if(!bidset.contains(bid)){
			    papercount += in.getOldpapercount();
			    realcount += in.getOldrealcount();
			    bidset.add(bid);
			}
	 } 
}







%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css"> 
.fixedHead { 
position:fixed;
}  
 
*{
    margin:0;
    padding:0;
}
#table{  
    width:900px;
    table-layout:fixed ;
}

#th{  
    background-color:white;
    position:absolute; 
    width:900px; 
    height:30px;
    top:0;
    left:0;
}
#wrap{
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:50px;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/map.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script src="../../js/jquery-ui.js"></script>

<script type="text/javascript">

function inventory(inventory,type,tid){
	 if(type == 0 || type == 1 || type == 3){
		 //window.location.href='inventorysearch.jsp?id='+inventory;
		 window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }else if(type == 13 || type == 15 ){  
		 window.open('../../admin/receivegoods/receivegoodsdetail.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
		     
	 }else if(type == 14 || type == 16 || type == 17 || type == 18 ){ 
		 window.open('../../admin/ordergoods/ordergoodsdetail.jsp?id='+inventory+'&type=<%=OrderMessage.all%>&statues=<%=OrderMessage.billing%>&tid='+tid, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }else{  
		// window.location.href='dingdanDetail.jsp?id='+inventory; 
		 window.open('dingdanDetail.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }
} 
</script>
</head>

<body>

<div class="s_main">
<!--   头部结束   -->
   <div class="weizhi_head">现在位置：库存查询
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   开始时间:<%=starttime%>---结束时间:<%=endtime %>
   <a href="javascript:history.go(-2);"><font style="color:blue;font-size:20px;" >返回</font></a>
   </div>        
    
<div >
<table  cellspacing="1" id="table" > 
		<tr class="dsc">   
			    <td align="center">单号</td>
     			<td align="center">日期</td> 
     			<td align="center">型号</td>
     			<td align="center">上报状态</td>   
     			<td align="center">操作类型</td>   
     			<td align="center">账面调拨数量</td>
     			<td align="center">安装网点：账面库存数量<P>销售门店：未入库数量</td> 
     			<td align="center">实际调拨数量</td>
     			<td align="center">实际库存数量</td>
     			<td align="center">是否确认收发货</td>
		</tr>      
		    
               <% for(int i=0;i<list.size();i++){
	            	   InventoryBranchMessage  in = list.get(i);
	            	    
		        		 String strtype = "";
		        		 int type = in.getOperatortype(); 
		        		 //System.out.println(type);
		        		 String branch = branchmap.get(in.getBranchid()).getLocateName();
		        		 if(branchid != "" && branchid != null){
		        			 papercount =in.getPapercount();
			        		 realcount  = in.getRealcount(); 
			        		 //papercount +=in.getAllotPapercount();
			        		// realcount += in.getAllotRealcount();
		        		 }else {
	
		        			papercount +=in.getAllotPapercount();
		        			realcount += in.getAllotRealcount();
		        		}
            	   
		        		
		        		strtype = in.getMessage(); 
		        		 String cl="class=\"asc\""; 
		        		  if(in.getIsOverStatues() == 1){
		        			  cl="class=\"bsc\""; 
		        		  }
		        		 if(type == 10){
		        			 %>
		        			
		        			 <tr id="<%=in.getInventoryid() %>"   <%=cl %> onclick="updateClass(this)">   
			        			  <td align="center"><%=usermap.get(in.getSendUser()).getBranchName()%>"已盘点" </td>   
			        			  <td align="center"><%=in.getTime()%></td>  
			        			  <td align="center"><%=in.getType()%></td>    
			        			  <td align="center"> </td>   
			        			  <td align="center"> </td>   
			        			  <td align="center"> </td>   
			        			  <td align="center"><%=papercount%></td>     
			        			  <td align="center"> </td>  
			        			  <td align="center"><%=realcount%></td>  
			        			   <td align="center"><%=in.getIsOverStatuesName()%></td>  
		        			  </tr>
		        			<%	 
		        		 }else {
		        			 
		        		%>
		        	 
		        		   <tr id="<%=in.getInventoryid() %>" <%=cl %>  onclick="inventory('<%=in.getInventoryid() %>','<%=type%>','<%=in.getTypeid()%>')">   
		        		      <td align="center"><%=in.getInventoryString()%></td>   
		        		      <td align="center"><%=in.getTime()%></td>     
		        		      <td align="center"><%=in.getType()%></td>    
		        		      <td align="center"><%=mapdevity.get(in.getDevidety()+"")%></td>   
		        		      <td align="center"><%=strtype%></td>    
		        		      <td align="center"><%=in.getAllotPapercount()%></td>  
		        		      <td align="center"><%=papercount%></td>     
		        		      <td align="center"><%=in.getAllotRealcount()%></td>  
		        		    
		        		      <td align="center"><%=realcount%></td>  
		        		         <td align="center"><%=in.getIsOverStatuesName()%></td>   
		        		      </tr>
		        		<% 
		        		 }
		        	 }		 
               %>

    
</table> 
     </div>


</div>
</body>
</html>
