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

<title>欢迎使用微网站办公系统</title>

<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 

 
<link rel="stylesheet" href="../../css/songhuo.css">
 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
 

$(function () {  
	//$("#"+type).css("color","red");
}); 
  
function detail(src){
	window.location.href=src;
} 
    
function search(statues){
	window.location.href="ordergoodsall.jsp?statues="+statues;
}  
</script>  
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>   
   
<!--  头 单种类  -->  
<div class="s_main_tit"><span class="qiangdan"><a href="../welcom.jsp">返回</a></span></div>
<!-- 
<div class="s_main_tit"><span style="cursor:hand" id="<%=AfterSaleProduct.pending%>" onclick="search('<%=AfterSaleProduct.pending%>')">待审批</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span style="cursor:hand" id="<%=AfterSaleProduct.success%>" onclick="search('<%=AfterSaleProduct.success%>')">已审批</span>&nbsp;&nbsp;</div>
    -->
<!--  订单详情  --> 
<div >
<table width="100%" border="0" cellspacing="1"  > 
  <tr >  
   <td width="30%" class="s_list_m" align="center">订单时间</td>
   <td width="40%" class="s_list_m"  align="center">备注</td>
    
  </tr>  
   <%   
   if(null != map){   
	   Set<Map.Entry<String,OrderGoodsAll>> mapent = map.entrySet();
		Iterator<Map.Entry<String,OrderGoodsAll>> itmap = mapent.iterator();
		int i = 0 ;
		while(itmap.hasNext()){
			Map.Entry<String,OrderGoodsAll> en =  itmap.next();
			OrderGoodsAll o =en.getValue();
			String key = en.getKey();
	    	i++; 
	    	String col = ""; 
	    	if(i%2 == 0){
	    		col = "style='background-color:yellow'";
	    	}  
	    	
  %>       
   
 <tr <%=col %> onclick="detail('ordergoods.jsp?id=<%=key%>')"> 
 <td align="center"><%= o.getOm().getSubmittime()%></td> 
     <td align="center"><%=o.getOm().getRemark()%></td> 
    

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