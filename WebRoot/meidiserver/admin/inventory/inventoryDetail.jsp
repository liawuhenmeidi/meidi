<%@ page language="java" import="java.util.*,utill.*,product.*,order.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");  
String ctype = request.getParameter("ctype");
String branchid = request.getParameter("branchid");
String endtime = request.getParameter("endtime");
endtime = TimeUtill.dataAdd(endtime,1); 

String starttime = request.getParameter("starttime"); 

Branch b = null;
if(!StringUtill.isNull(branchid)){
	if(NumbleUtill.isNumeric(branchid)){
		b = BranchManager.getLocatebyid(branchid);
	}else {
		b = BranchService.gerBranchByname(branchid);
	}
	branchid = b.getId()+""; 
}

Map<Integer,Branch> branchmap = BranchManager.getIdMap();
List<InventoryBranchMessage > list = InventoryBranchMessageManager.getCategory(ctype,branchid,starttime,endtime);  
Map<Integer,User> usermap = UserService.getMapId(); 
Map<String,String> mapdevity = OrderManager.getDeliveryStatuesMap();

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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
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
    height:500px;
}

</style>
</head>

<body style="scoll:no">
  
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
 
$(function () { 
	$("#wrap").bind("scroll", function(){ 

		if(pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)){
	        //滚动了竖直滚动条
	        pre_scrollTop=($("#wrap").scrollTop() || document.body.scrollTop);
	       
	        if(obj_th){
	            obj_th.style.top=($("#wrap").scrollTop() || document.body.scrollTop)+"px";
	        }
	    }
	    else if(pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)){
	        //滚动了水平滚动条
	        pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollLeft);
	    }
		}); 

}); 

function inventory(inventory,type){
	 if(type == 0 || type == 1){
		 window.location.href='inventorysearch.jsp?id='+inventory;
		 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }else {
		 window.location.href='dingdanDetail.jsp?id='+inventory;
		 //window.open('dingdanDetail.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }
} 

</script>
<div style="position:fixed;width:100%;height:20%;">
<div >
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
</div >
   <div class="weizhi_head">现在位置：库存查询
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   开始时间:<%=starttime%>---结束时间:<%=endtime %>
   <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>
   </div>  

</div>
<div style="height:70px;">
</div>
<br/>  

<div id="wrap">
<table  cellspacing="1" id="table" >
		<tr id="th">  
			    <td align="left">单号</td>
     			<td align="left">日期</td>
     			<td align="left">型号</td>
     			<td align="left">上报状态</td> 
     			<td align="left">操作类型</td>
     			<td align="left">账面调拨数量</td>
     			<td align="left">账面库存数量</td> 
     			<td align="left">实际调拨数量</td>
     			<td align="left">实际库存数量</td> 
		</tr>      
		    
               <% for(int i=0;i<list.size();i++){
	            	   InventoryBranchMessage  in = list.get(i);
	            	   
		        		 String strtype = "";
		        		 int type = in.getOperatortype();
		        		 System.out.println(type);
		        		 String branch = branchmap.get(in.getBranchid()).getLocateName();
		        		 if(branchid != "" && branchid != null){
		        			 papercount =in.getPapercount();
			        		 realcount  = in.getRealcount();
		        		 }else {
	
		        			papercount +=in.getAllotPapercount();
		        			realcount += in.getAllotRealcount();
		        		}
            	   
		        		 if(type == 0 ){ 
		        			 strtype = branch+"出库";
		        		 }else if(type == 1){ 
		        			 strtype = branch+"入库";
		        		 }else if(type == 2){
		        			 strtype = usermap.get(in.getSendUser()).getBranchName()+"派工给"+usermap.get(in.getReceiveuser()).getBranchName();
		        		 }else if(type == 20){
		        			 strtype = usermap.get(in.getReceiveuser()).getBranchName()+"释放"; 
		        		 }else if(type == 11){ 
		        			 strtype =usermap.get(in.getSendUser()).getUsername()+"派工给"+usermap.get(in.getReceiveuser()).getUsername();
		        		 }else if(type == 6){   
		        			 strtype = usermap.get(in.getReceiveuser()).getUsername()+"释放给"+usermap.get(in.getSendUser()).getBranchName();
		        		 }else if(type == 7){    
		        			 strtype = "退货员"+usermap.get(in.getReceiveuser()).getBranchName()+"拉回给"+usermap.get(in.getSendUser()).getBranchName();
		        		 } else if(type == 8){    
		        			 strtype = usermap.get(in.getSendUser()).getBranchName()+"同意"+usermap.get(in.getReceiveuser()).getBranchName()+"退货";
		        		 } else if(type == 9 ) {
		        			 strtype = "退货员"+usermap.get(in.getReceiveuser()).getBranchName()+"释放给"+usermap.get(in.getSendUser()).getBranchName();
		        		 }else if(type == 12){
		        			 strtype = "退货员"+usermap.get(in.getReceiveuser()).getUsername()+"拉回次品给"+usermap.get(in.getSendUser()).getBranchName();
		        		 } 
		        		 
		        		 if(type == 10){
		        			 %>
		        			
		        			 <tr id="<%=in.getInventoryid() %>"  class="asc"  onclick="updateClass(tdis)">   
			        			  <td align="center"><%=usermap.get(in.getSendUser()).getBranchName()%>"已盘点" </td>   
			        			  <td align="center"><%=in.getTime()%></td>  
			        			  <td align="center"><%=in.getType()%></td>    
			        			  <td align="center"> </td>   
			        			  <td align="center"> </td>   
			        			  <td align="center"> </td>   
			        			  <td align="center"><%=papercount%></td>     
			        			  <td align="center"> </td>  
			        			  <td align="center"><%=realcount%></td>  
		        			  </tr>
		        			<%	 
		        		 }else {
		        			 
		        		%>
		        	
		        		   <tr id="<%=in.getInventoryid() %>" class="asc" ondblclick="inventory('<%=in.getInventoryid() %>','<%=type%>')">   
		        		      <td align="center"><%=in.getInventoryString()%></td>   
		        		      <td align="center"><%=in.getTime()%></td>     
		        		      <td align="center"><%=in.getType()%></td>    
		        		      <td align="center"><%=mapdevity.get(in.getDevidety()+"")%></td>   
		        		      <td align="center"><%=strtype%></td>    
		        		      <td align="center"><%=in.getAllotPapercount()%></td>  
		        		      <td align="center"><%=papercount%></td>     
		        		      <td align="center"><%=in.getAllotRealcount()%></td>  
		        		    
		        		      <td align="center"><%=realcount%></td>  
		        		      </tr>
		        		<% 
		        		 }
		        	 }		 
               %>

    
</table> 
     </div>

</body>
</html>
