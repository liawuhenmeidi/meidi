<%@ page language="java"   pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>   
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>税务开票基础信息</title>  
        
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
   
$(function () {
	
});



function mysubmit(){ 
	$("#myform").submit();
}



</script> 
</head> 

<body>
<!--   头部开始   -->

 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
  
        
 <!--       -->     
     <div class=""> 
   <div class="weizhi_head"> 
   现在位置：小家电苏宁参数维护
   </div> 
   <div class="table-list">      
               
 <form id="myform" action="SNSmallAppliancesave.do"  method="post">      
   <input  type="hidden" name="token" value="${token}" /> 
   
<table width="100%" cellspacing="1" id="table">      
	 <tr class="asc">  
    
       <td>苏宁账号</td> 
	    <td>
	    <input type="text" name="username" id="username" value=""  value="${TaxBasicMessage.gfmc}"/>
	    </td>
	    
	   
	<td>苏宁密码</td>
	<td> 
	<input type="password" name="password" id="password" value="${TaxBasicMessage.gfsh}"/>
	</td>
	  </tr>
	
	<tr class="asc">
	<td colspan="4"> 
	<input type="button" value="提交" onclick="mysubmit()"/>
	</td> 
	</tr>
  
</table> 
 
   </form>  
     
     
     </div>


</div>



</body>
</html>
