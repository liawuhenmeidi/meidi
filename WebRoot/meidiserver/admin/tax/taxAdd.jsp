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
   <form action="add.do" method="post">
  <table>
  <tr>
  <td>    
   现在位置：税务系统基本信息维护</td>
   <td>  
 <select name="gfmc" > 
   <c:forEach var="TM"  items="${TaxBasicMessages}" >
     <option value="${TM.gfmc }" >${TM.gfmc }</option>
      </c:forEach> 
  
 </select>  
   </td> 
   <td>
   <input type="submit" value="查看"/>
   </td>
  </tr>
  </table> 
  </form> 
</div> 
   <div class="table-list">      
               
 <form id="myform" action="save.do"  method="post">      
   <input  type="hidden" name="token" value="${token}" /> 
   
<table width="100%" cellspacing="1" id="table">     
	 <tr class="asc"> 
   
       <td>购方名称</td>
	    <td>
	    <input type="text" name="gfmc" id="gfmc" value="" placeholder="100字节" value="${TaxBasicMessage.gfmc}"/>
	    
	    </td>
	    
	   
	<td>购方税号</td>
	<td>
	<input type="text" name="gfsh" id="gfsh" value="${TaxBasicMessage.gfsh}"/>
	</td>
	  </tr>

	<tr class="asc">
	<td>购方银行账号</td> 
	<td>
	<input type="text" name="gfyhzh" id="gfyhzh" placeholder="100字节" value="${TaxBasicMessage.gfyhzh}"/>
	</td>
	
	<td>购方地址电话</td> 
	<td> 
	<input type="text" name="gfdzdh" id="gfdzdh"  value="${TaxBasicMessage.gfdzdh}"
						 placeholder="100字节" />
	</td>
	</tr>
	



	<tr class="asc"> 
	  
	<td>备注</td>      
	<td>  
	<input type="text" name="bz" id="bz"  value="${TaxBasicMessage.bz}"
						 placeholder="240字节" />
	</td> 
	
	<td>复核人</td>      
	<td>  
	<input type="text" name="fhr" id="fhr" value="${TaxBasicMessage.fhr}"
						 placeholder="8字节" />
	</td>

	</tr>
	<tr class="asc"> 
	 <td>收款人</td> 
	<td> 
	<input type="text" name="skr" id="skr"  value="${TaxBasicMessage.skr}"
		 placeholder="8字节"				  />
	</td>
	<td></td>      
	<td>  
	
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
