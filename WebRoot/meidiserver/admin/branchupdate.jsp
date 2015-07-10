<%@ page language="java" import="java.util.*,category.*,enums.*,exportModel.*,branchtype.*,utill.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
BranchType list = new BranchType(); 
   
String id = request.getParameter("id");
int typestatues = 0; 
int modelstatues = 0 ; 
if(!StringUtill.isNull(id)){ 
	list =BranchTypeManager.getLocate(Integer.valueOf(id)) ;
	typestatues = list.getTypestatues(); 
	modelstatues = list.getSaletype();  
}  

      
%> 
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<script type="text/javascript">
var bid = "<%=id%>";
var statues = "<%=typestatues%>";
var modelstatues = "<%=modelstatues%>";
$(function (){ 
	init(); 
}); 
  
function init(){    
	$("input[value="+statues+"][name=typestatues]").attr("checked",'checked');
	$("input[value="+modelstatues+"][name=modelstatues]").attr("checked",'checked');
}   
   
function changes(){    
	var str1 = $("#locate").val(); 
	var modelstatues = $('input[name="modelstatues"]:checked').val();
	var typestatues = $('input[name="typestatues"]:checked').val();
	//alert(typestatues); 
	if(str1 == null || str1 == ""){  
		 
		alert("不能为空");
		return ;
	}  
	  
	if( "" == typestatues || null == typestatues){
		alert("门店属性不能为空");
		return ; 
	} 
	 
	if( "" == modelstatues || null == modelstatues){
		alert("所属卖场不能为空");
		return ; 
	}  
	
	$.ajax({  
        type: "post",  
         url: "server.jsp",
         data:"method=branchtypeupdate&id="+str1+"&bid="+bid+"&typestatues="+typestatues+"&modelstatues="+modelstatues,
         dataType: "",
         success: function (data) { 
           window.location.href="branch.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

</script>
</head>
 
<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include> 


   <div class="weizhi_head">现在位置：门店类别管理</div>     
 <br/>
<table width="80%" cellspacing="1" id="table" >
<tr class="asc"> 
<td align=center>门店名称：</td>
<td align=center> <input type="text"  id="locate" value= "<%=list.getName() %>" name="locate" value= ""/>  </td>
</tr>  
<tr class="asc">    
<td align=center>门店属性</td> 
<td align=center>  
 <input   type="radio"  name="typestatues" value="<%=BranchType.sale%>"/>销售卖场
 <input   type="radio" name="typestatues" value="<%=BranchType.install%>"/>售后网点
</td>  
</tr> 

<tr class="asc">    
<td align=center>所属卖场</td>  
<td align=center>  
<%      
     
SaleModel.Model[] models = SaleModel.Model.values();
   int num = models.length;     
   for(int i=0;i<num;i++){      
	   SaleModel.Model model = models[i];              
	   %> 
	   <input   type="radio"  name="modelstatues"  value="<%=model.getValue()%>"/><%=model.name() %>
	    
	   <%
	   
   }
%> 
</td>   
</tr> 
<tr class="asc"  >
<td align=center colspan=2><input type="button" onclick="changes()"  value="提交"/>  </td>
</tr>
</table>
 


</body>
</html>
