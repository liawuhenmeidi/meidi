<%@ page language="java" import="java.util.*,product.*,inventory.*,branch.*,utill.*,java.text.SimpleDateFormat,category.*,orderPrint.*,gift.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%   
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");
 
String inventoryid = request.getParameter("id");
Inventory inventory = InventoryManager.getInventoryID(user, Integer.valueOf(inventoryid));
Map<Integer,Branch> branchmap = BranchManager.getIdMap();  

List<InventoryMessage> list = inventory.getInventory(); 

%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印页面</title>
<style media=print type="text/css">   
.noprint{visibility:hidden}   
</style>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../css/bass.css" />
<link rel="stylesheet" href="../../css/songhuo.css"/>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
 function println(){
	 
	 window.print();

 }
 
 
</script>
</head>

<body>
  
<table width="1010">

  <tr>
    <td colspan="2">&nbsp;</td> 
    <td width="384" rowspan="2" align="center" style="font-size:30px; font-family:"楷体";><strong><%=user.getBranch() %></strong></td>
    <td width="300"><strong><FONT size=5>单&nbsp;&nbsp;号：<%=inventory.getId() %></strong></FONT></td> 
  </tr>
  <tr>  
    <td width="110" style="font-size:25px; font-family:"楷体";>&nbsp;&nbsp;&nbsp;<strong>&nbsp;</strong></td>
    <td><strong><FONT size=4>开票日期:<%=inventory.getIntime() %></strong></FONT></td>
  </tr>  
  
</table>   

<table width="1010" border="0" cellpadding="0" cellspacing="0"  id="t"  style="font-size:28px; font-weight:bolder;">
<tr>  
  <td height="30" colspan="5" align="center" valign="middle" bgcolor="#FFFFFF">
  <table width="1010" width="100%" border="0" cellspacing="0" cellpadding="0"> 
 <tr> 
 <td id="d" width="15%" align="center">
 出库方
 </td > 
 <td id="d" width="35%" align="center">
 <%=branchmap.get(inventory.getOutbranchid()).getLocateName() %>
 </td>  
 <td id="d" width="15%" align="center">
入库方
 </td>
 <td id="d" width="35%" align="center">
<%=branchmap.get(inventory.getInbranchid()).getLocateName() %>
 </td> 
 </tr> 
</table>
  <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <% for(int i=0;i<list.size();i++){ 
	  InventoryMessage in = list.get(i); 
  %> 			
    <tr>
      <td width="4%" height="30" align="center" valign="middle" id="d"></td>
      <td width="9%" height="30" align="center" valign="middle" id="d">品类</td> 
      <td width="17%" height="30" align="center"  id="d">&nbsp;<%=in.getCategoryId() %></td>
      <td width="11%" height="30" align="center" valign="middle" id="d">型号</td> 
      <td width="38%" height="30" align="center" valign="middle" id="d"><%=ProductService.getIDmap().get(Integer.valueOf(in.getProductId())).getType() %></td>
      <td width="12%" height="30" align="center" valign="middle" id="d">数量</td>
      <td width="9%" height="30" align="center"valign="middle" id="e"><%=in.getCount() %></td>
    </tr> 
   <% }%>
 
  </table></td>
</tr> 
 
</table>
<center> <input class="noprint" type=button name='button_export' title='打印1' onclick="println()" value="打印"></input></center>
 <!-- <p class="noprint">打印之后请修改打印状态</p>  --> 
</body>
</html>


