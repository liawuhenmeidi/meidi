<%@ page language="java" import="java.util.*,utill.*,makeInventory.*,category.*,inventory.*,user.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");
boolean flag = true ;   
User user = (User)session.getAttribute("user");
String type = request.getParameter("type");
  
if(StringUtill.isNull(type)){ 
	type = "model";  
} 
int typestatues = 0 ;
String typeName = "";  
 
if("model".equals(type)){
	typeName = "样机盘点"; 
	typestatues = MakeInventory.model;
}else if("out".equals(type)){
	typeName = "未入库盘点";
	typestatues = MakeInventory.out;
}else if("in".equals(type)){
	typeName = "入库盘点";
	typestatues = MakeInventory.in;
}   
  
List<MakeInventory> list =MakeInventoryManager.get(user, typestatues);




%>   
<!DOCTYPE html>
<html> 
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="viewport" content="initial-scale=1, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/>
  <title>欢迎使用微网站办公系统</title>

 <script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" href="../../css/songhuo.css">
 
<script type = "text/javascript" language = "javascript">

var type = "<%=type%>"; 
$(function () {     
	 $("#"+type).css("color","red");
}); 
  
function clear(ty){
	//alert(ty);
	//alert(type);  
	if(type == ty){ 
		location.href = "makeinventory.jsp?type="+ty;
		 
	}else {
		location.href = "makeinventoryall.jsp?type="+ty;
	}
	
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
<div class="s_main_box">
  <table width="100%">    
  <tr>       
  <td align="center" ><a id="model" href="javascript:clear('model')"><font size="3" >样机盘点</font></a></td>  
  <td align="center" ><a id="out" href="javascript:clear('out')"><font size="3" >未入库盘点</font></a></td> 
  <td align="center"><a  id="in"  href="javascript:clear('in')"><font size="3" >已入库盘点</font></a></td>
  </tr>   
     
     <tr></tr>
  <%if(null != list){ 
	  for(int i=0;i<list.size();i++){
		  MakeInventory mi = list.get(i); 
		  
		  %>   
	<tr><td  align="center"><%=typeName %></td><td colspan="2" align="center"><a href="makeinventory.jsp?type=<%=type%>&uuid=<%= mi.getUuid()%>"><%=mi.getSubmittime() %></a></td></tr>	  
		  
		  
		  <%
	  }
	  
	  
  } %>   
  </table>
  
  </div>
</div> 
</body>

</html>