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
	$("#${orderName}").attr("selected","selected");
});

 

function mysubmit(){ 
	  
	
	
	$("#myform").submit();
}

function taxpost(){
	var name = $("#gfmc").val();
	if(name == '' || name == null){
		alert("请选择购方名称");
		return false ;
	 
	}  
	$("#taxpost").submit();
	
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
   <div class="weizhi_head">现在位置：税务系统单据转化</div> 
   <div class="table-list">        
 
 <form action="../../ExcelUpload" method="post" enctype ="multipart/form-data" >
    <input type="hidden" name="uploadType" value="6"/> 
   <table> 
   <tr>
    <td> <a href="../../../data/model/tax.xls"><font style="color:blue;font-size:20px;" >模板</font> </a></td> 
   <td> 
     文件上传：
   </td>
    <td align="center" ><input id="File1"   name="UpLoadFile" type="file" />

     <input type="submit" name="Button1" value="提交文件" id="Button1" />
    </td>
   </tr>
   
   </table>
 
  
 </form>                     
 <form id="myform" action="taxDetail.do"  method="post">   
   </form>    

	 <table border="1px" align="left" width="100%">
       <tr>
		        <td align="center">序号</td>
				<td align="center">商品编号</td>
				<td align="center">商品名称</td>
				<td align="center">单位</td> 
				<td align="center">单价</td>
				<td align="center">数量</td>
				<td align="center">金额</td> 
				<td align="center">税率</td> 
				<td align="center">税后金额</td> 
				
		</tr> 
		
<c:set value="0" var="count" />   
<c:set value="0" var="moneycount" />  
<c:set value="0" var="bpmoneycount" />  
	 
<c:forEach var="UploadOrder"  items="${list}" varStatus="status"> 
  <c:set value="${count + UploadOrder.num}" var="count" />    
  <c:set value="${moneycount +UploadOrder.totalMoney}" var="moneycount" />  
  <c:set value="${bpmoneycount+UploadOrder.totalMoney*(1-UploadOrder.taxRate/100)}" var="bpmoneycount" />  
     
      
   <tr class="asc"  onclick="updateClass(this)"> 
					<td align="center">${status.count}</td>   
					<td align="center">${UploadOrder.pnum}</td>
					<td align="center">${UploadOrder.pname}</td>
				 
					<td align="center">${UploadOrder.unit}</td>  
					<td align="center">${UploadOrder.prince}</td>  
					<td align="center">${UploadOrder.num}</td>
					<td align="center">${UploadOrder.totalMoney}</td>   
					<td align="center">${UploadOrder.taxRate}</td>
					<td align="center">${UploadOrder.totalMoney*(1-UploadOrder.taxRate/100)}</td>   
					
	</tr>    
     
  </c:forEach>  
  
  
 <tr class="asc"  style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center">合计</td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>  
					<td align="center"></td>  
					<td align="center">${count}</td> 
					<td align="center">${moneycount}</td> 
					<td align="center"></td>
					<td align="center">${bpmoneycount}</td>	
	</tr>
<tr> 
   
  <td colspan="8">
  <form action="../file/taxExchange.do" id="taxpost" method="post">
  <input type="hidden" name="filename" value="${filename}"/>
   购方名称：<select name="gfmc" id="gfmc"> 
   <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
   <c:forEach var="TM"  items="${TaxBasicMessages}" >
     <option value="${TM.gfmc }" >${TM.gfmc }</option>
      </c:forEach> 
   
 </select>  
  <input type="button" value="转化文件" onclick="taxpost()"/>
  </form> 
  
  </td>
</tr> 
	
   
</table> 
  
      
 
     </div>


</div>



</body>
</html>
