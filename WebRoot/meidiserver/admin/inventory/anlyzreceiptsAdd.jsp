<%@ page language="java" import="java.util.*,utill.*,product.*,order.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
String inventoryid = request.getParameter("id");
Map<Integer,Branch> branchmap = BranchService.getMap();

HashMap<Integer,Category> mapc = CategoryManager.getCategoryMap();

Inventory inventory = InventoryManager.getInventoryID(user, Integer.valueOf(inventoryid));  
if(branchmap != null){
	Branch inbranch = branchmap.get(inventory.getInbranchid());
}
	
String inittime = inventory.getIntime();  
List<InventoryMessage> list = inventory.getInventory(); 

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/> 
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
    width:800px;
    table-layout:fixed ;
}

#th{  
    background-color:white;
    position:absolute; 
    width:800px; 
    height:30px;
    top:0;
    left:0;
}

td{
 width:200px; 
}

#th td{
width:200px; 
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
<script type="text/javascript" src="../../js/calendar.js"></script> 
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
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

function println(){
	 
	 window.print();

}

</script>
<div style="position:fixed;width:100%;height:70px;">
<div >
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>     
</div > 
   </div>  

<div style="height:70px;"> 
</div>
<div class="weizhi_head">现在位置：调拨单   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="print.jsp?id=<%=inventoryid%>&type=<%=inventory.getIntype() %>"><font style="color:blue;font-size:20px;">打印</font></a> 
<a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>  
</div>  


<div id="wrap"> 
    
<form action="InventoryServlet" method="post" >
<input type="hidden" name="method" value="updatesubscribe"/>
<table  cellspacing="1" id="table" >
		<tr id="th">  
     			<td align="center">名称</td>
     			<td align="center">型号</td> 
     			
     			<td align="center">调拨数量</td>
     			<td align="center">确认数量</td>
     			
		</tr>      
            <% 
             Iterator<InventoryMessage> it = list.iterator();
             while(it.hasNext()){
            	 InventoryMessage in = it.next();
            	// int  Anlycount = (null == in.getAnlycount())?"":in.getAnlycount();
            	 System.out.println(in.getAnlycount());
             %>
            	   <tr id="<%=in.getId() %>" class="asc">   
		 
        			   <td align="center"><%=mapc.get(in.getCategoryId()).getName() %> </td>
        			   <td align="center"><%=in.getProductname() %> </td>
        			   <td align="center"><%=in.getCount() %> </td>
        			   <td align="center"> 
        			  <input type="hidden" name="product" value="<%=in.getId() %>" />
        			  <input type="text" name="<%=in.getId() %>"  id="<%=in.getId() %>"  value="<%=in.getAnlycount()==0?"":in.getAnlycount() %>" /> 
        			
        			  </td>
                       </tr>
            	  
            	  
            <%  }%>
		       
	   <tr class="asc" >
		    <td align="center"  colspan=4> 
		    <input type="submit"  style="background-color:;font-size:20px;" value="调货提交" />
		    </td>
       </tr> 			
</table> 
</form>
  </div>

</body>
</html>
