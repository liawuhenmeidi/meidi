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
                     
 <form id="myform" action="taxDetail.do"  method="post">  
 <table>
    <tr> 
    
      <td>
      文件选择:<select name="orderName" id="orderName"> 
	<option ></option> 
    <c:forEach var="orderName"  items="${orderNames}" >
     <option value="${orderName}" id="${orderName}"  >${orderName}</option>
      </c:forEach> 
  
</select> 
       
      </td>  
      <td>
      <input type="submit" value="查看" class="noprint" onclick="$('#myform').attr('action','taxDetail.do')"/>
      </td>  
      <td>   
      <input type="submit" value="转化文件" class="noprint" onclick="$('#myform').attr('action','taxExchange.do')"/>
      </td>
    </tr>
  
 </table>    
    
   </form>   

	 <table border="1px" align="left" width="100%">
       <tr>
		        <td align="center">序号</td>
				<td align="center">销售门店</td>
				<td align="center">销售日期</td>
				<td align="center">票面型号</td> 
				<td align="center">单价</td>
				<td align="center">票面数量</td> 
				<td align="center">供价</td> 
				<td align="center">扣点</td>
				<td align="center">扣点后单价</td>
				<td align="center">扣点后价格</td>
		</tr> 
		
<c:set value="0" var="count" />   
<c:set value="0" var="moneycount" />  
<c:set value="0" var="bpmoneycount" />  
	 
<c:forEach var="UploadOrder"  items="${UploadOrders}" > 
  <c:set value="${count + UploadOrder.num}" var="count" />    
  <c:set value="${moneycount +UploadOrder.salePrice*UploadOrder.num}" var="moneycount" />  
  <c:set value="${bpmoneycount+UploadOrder.salePrice*UploadOrder.num*(1-UploadOrder.backPoint/100)}" var="bpmoneycount" />  
     
      
   <tr class="asc"  onclick="updateClass(this)" id="${UploadOrder.id}"> 
					<td align="center">${status.count}</td>   
					<td align="center">${UploadOrder.shop}</td>
					<td align="center">${UploadOrder.saleTime}</td>
				 
					<td align="center">${UploadOrder.type}</td>  
					<td align="center">${UploadOrder.salePrice}</td>
					<td align="center">${UploadOrder.num}</td>   
					<td align="center">${UploadOrder.salePrice*UploadOrder.num}</td>
					<td align="center">${UploadOrder.backPoint}</td>   
					<td align="center">${UploadOrder.salePrice*(1-UploadOrder.backPoint/100)}</td>
					<td align="center">${DoubleUtill.getdoubleTwo(Math.abs(UploadOrder.salePrice())*UploadOrder.num()*(1-UploadOrder.backPoint/100))}</td>
	</tr>    
     
  </c:forEach>  
 
 
 <tr class="asc"  style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center">${count}</td> 
					<td align="center">${DoubleUtill.getdoubleTwo(moneycount)}</td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center">${DoubleUtill.getdoubleTwo(bpmoneycount)}</td>
	</tr>
  
	 
	
   
</table> 
  
      
 
     </div>


</div>



</body>
</html>
