<%@ page language="java"  import="java.util.*,product.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%   
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String statues = request.getParameter("statues");
if(StringUtill.isNull(statues)){
	statues = AfterSaleProduct.pending+"" ;  
} 
 
List<AftersaleAll> list = AftersaleAllManager.getOrderlistneedmaintain(user,statues);
Map<Integer,AftersaleAll> map = AftersaleAllManager.getAftersaleAllMap(list);  
   

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>欢迎使用微网站办公系统</title>

<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 


<link rel="stylesheet" href="../css/songhuo.css">
 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var type = "<%=statues%>";

$(function () { 
	$("#"+type).css("color","red");
});

function detail(src){
	window.location.href=src;
}
  
function search(type){
	window.location.href="fault.jsp?statues="+type;
}
</script>
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--  头 单种类  --> 
<div class="s_main_tit"><span class="qiangdan"><a href="chaxun_songhuo.jsp">我要查询</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span></div>
<div class="s_main_tit"><span style="cursor:hand" id="<%=AfterSaleProduct.pending%>" onclick="search('<%=AfterSaleProduct.pending%>')">待解决</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span style="cursor:hand" id="<%=AfterSaleProduct.success%>" onclick="search('<%=AfterSaleProduct.success%>')">已解决</span>&nbsp;&nbsp;</div>
   
<!--  订单详情  --> 
<div class="s_main_box">
<table width="100%" class="s_main_table">
  <tr > 
    <td  class="s_list_m">故障原因</td>
    <td  class="s_list_m">维护日期</td>
    <td  class="s_list_m">地点</td>
  </tr>
   <%  
   if(null != map){
		Set<Map.Entry<Integer,AftersaleAll>> mapent = map.entrySet();
		Iterator<Map.Entry<Integer,AftersaleAll>> itmap = mapent.iterator();
		int i = 0 ;
		while(itmap.hasNext()){
	    	AftersaleAll o = itmap.next().getValue();
	    	i++; 
	    	String col = "";
	    	if(i%2 == 0){
	    		col = "style='background-color:yellow'";
	    	}
  %>
 <tr <%=col %> onclick="detail('dingdanDetailfault.jsp?id=<%=o.getAs().getId()%>&statues=<%=statues%>')">  
      <% 
         String fault = ""; 
         String time = "";
         List<AfterSaleProduct> listas = o.getAsplist();
         for(int j=0;j<listas.size();j++){
        	 AfterSaleProduct as = listas.get(j);
        	 fault += as.getCause(); 
             time = as.getThistime();
         } 
         %>
     <td align="left"> 
         
   <%=fault %> 
  
	 </td>    
		     
		     
    <td><%= time %></td>
    <td><%= o.getAs().getLocation()%></td> 
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