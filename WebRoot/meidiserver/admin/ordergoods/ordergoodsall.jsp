<%@ page language="java"  import="java.util.*,ordersgoods.*,product.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%    
request.setCharacterEncoding("utf-8");  
User user = (User)session.getAttribute("user"); 
Map<String,OrderGoodsAll> map  = OrderGoodsAllManager.getmap(user,OrderMessage.unexamine); 
 // System.out.println(StringUtill.GetJson(map));  
%>             
<!DOCTYPE html>  
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单审核</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
 

$(function () {  
	//$("#"+type).css("color","red");
}); 
 
function detail(src){
	window.location.href=src;
}
  
function search(statues){
	window.location.href="maintain.jsp?statues="+statues;
}
</script> 
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>  
   
   <div class="weizhi_head">现在位置：订货单审核</div>       
<!--  头 单种类  --> 
<table width="100%" border="0" cellspacing="1"  id="table"> 
  <tr class="dsc">  
  
    
    <td width="25%" class="s_list_m" align="center">门店</td>
    <td width="25%" class="s_list_m" align="center">订单提交人</td>
    <td width="25%" class="s_list_m" align="center">订单时间</td> 
    <td width="25%" class="s_list_m"  align="center">备注</td>
  </tr>  
   <%   
   if(null != map){   
	   Set<Map.Entry<String,OrderGoodsAll>> mapent = map.entrySet();
		Iterator<Map.Entry<String,OrderGoodsAll>> itmap = mapent.iterator();
		int i = 0 ;
		//System.out.println(12); 
		while(itmap.hasNext()){
			Map.Entry<String,OrderGoodsAll> en =  itmap.next();
			OrderGoodsAll o =en.getValue(); 
			String key = en.getKey();
			 
  %>         
 <tr class="asc" ondblclick="detail('ordergoodsupdate.jsp?id=<%=key%>&type=<%=OrderMessage.unexamine%>&statues=<%=OrderMessage.unexamine%>')">  
   
     <td align="center"><%=o.getOm().getBranchname()%></td> 
    <td align="center"><%=o.getOm().getUser().getUsername()%></td> 
      <td align="center"><%= o.getOm().getSubmittime()%></td>
     <td align="center"><%= o.getOm().getRemark()%></td>
    
  </tr> 
   
     <%}
    }%>
</table>
<br/>



<br/>
</div>

</div>

</body>
</html>