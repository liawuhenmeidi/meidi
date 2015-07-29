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
   <div class="weizhi_head">现在位置：税务系统基本信息维护</div> 
   <div class="table-list">      
               
 <form id="myform" action="save.do"  method="post">      
   <input  type="hidden" name="token" value="${token}" /> 
  
<table width="100%" cellspacing="1" id="table">    
	<tr class="asc">   
	<td>单据号<span style=" color:#F00;"></span></td>
	 <td >    
	   <input type="text" name="djh" value="${TaxBasicMessage.djh}" placeholder="专用发票或普通发票"/>
	 </td>
    <td>发票种类<span style=" color:#F00;">*</span></td>  
    <td> 
	 <input type="text" name="fpzl" value="${TaxBasicMessage.fpzl}" placeholder="专用发票或普通发票"/>
    </td>   
	  
	</tr> 
	<tr class="asc">
	<td>类别代码</td>
	<td>   
     <input type="text" name="lbdm" id="lbdm" placeholder="10字节" value="${TaxBasicMessage.lbdm}"/>
	</td> 
	<td>发票号码</td>
	<td> 
      <input type="text" name="fphm" id="fphm" placeholder="8字节" value="${TaxBasicMessage.fphm}"/>
	</td> 
	</tr>
	
	 <tr class="asc"> 
    <td width="25%">开票日期<span style=" color:#F00;">*</span></td>
    <td width="65%">
     <input type="text" name="kprq" id="kprq" placeholder="YYYYMMDD" value="${TaxBasicMessage.kprq}"/>
    
    </td> 
       <td>购方名称</td>
	    <td>
	    <input type="text" name="gfmc" id="gfmc" value="" placeholder="100字节" value="${TaxBasicMessage.gfmc}"/>
	    
	    </td>
	    
	    </tr>

	<tr class="asc">
	<td>购方税号</td>
	<td>
	<input type="text" name="gfsh" id="gfsh" value="${TaxBasicMessage.gfsh}"/>
	</td>
	 
	<td>购方银行账号</td> 
	<td>
	<input type="text" name="gfyhzh" id="gfyhzh" placeholder="100字节" value="${TaxBasicMessage.gfyhzh}"/>
	</td>
	</tr>
	
	<tr class="asc">  
	<td>购方地址电话</td> 
	<td> 
	<input type="text" name="gfdzdh" id="gfdzdh"  value="${TaxBasicMessage.gfdzdh}"
						 placeholder="100字节" />
	</td>
	<td>销方名称</td>      
	<td>  
	<input type="text" name="xfmc" id="xfmc"  value="${TaxBasicMessage.xfmc}"
						 placeholder="100字节" />
	</td>
	</tr> 

	<tr class="asc"> 
	 <td>销方税号</td> 
	<td> 
	<input type="text" name="xfsh" id="xfsh"  value="${TaxBasicMessage.xfsh}"
						  />
	</td>
	<td>销方银行账号</td>      
	<td>  
	<input type="text" name="xfyhzh" id="xfyhzh"  value="${TaxBasicMessage.xfyhzh}"
						 placeholder="100字节" />
	</td>
	 
	 
	 
	 
	</tr>
	

	<tr class="asc"> 
	 <td>销方地址电话</td> 
	<td> 
	<input type="text" name="xfdzdh" id="xfdzdh"  value="${TaxBasicMessage.xfdzdh}"
				placeholder="100字节"		  />
	</td>
	<td>合计金额</td>      
	<td>  
	<input type="text" name="hjje" id="hjje"  value="${TaxBasicMessage.hjje}"
						 />  
	</td>
    
	</tr>

	<tr class="asc"> 
	 <td>合计税额</td> 
	<td> 
	<input type="text" name="hjse" id="hjse"  value="${TaxBasicMessage.hjse}"
						  />
	</td>
	<td>备注</td>      
	<td>  
	<input type="text" name="bz" id="bz"  value="${TaxBasicMessage.bz}"
						 placeholder="240字节" />
	</td>

	</tr>

	<tr class="asc"> 
	 <td>开票人</td> 
	<td> 
	<input type="text" name="kpr" id="kpr" value="${TaxBasicMessage.kpr}"
		 placeholder="8字节"				  />
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
