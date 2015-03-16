<%@ page language="java"  import="java.util.*,utill.*,order.*,category.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>


<%
request.setCharacterEncoding("utf-8");
int type = Integer.valueOf(request.getParameter("type"));
String message = "";  

List<Category> listallp = CategoryService.getList();
String listallpp = StringUtill.GetJson(listallp); 

if(Order.aftersalerepare == type){
	message = "遗留数据处理"; 
}else if(Order.aftersale == type){ 
	message = "未上报工厂单据";  
}else if(Order.aftersaledealcharge == type){
	message = "工厂未结算单据"; 
}else if(Order.aftersalesearch == type){
	message = "售后查询"; 
}else if(Order.aftersalerepare == type){
	message = "系统外维修保养配工";
}else if(Order.aftersalerepare == type){
	message = "网点保养单待配工";
}else if(Order.aftersalephone == type){
	message = "当月需保养用户电话回访"; 
}else if(Order.aftersaledealupload == type){ 
	message = "维修保养未完成单据";
}else if(Order.aftersalesecond == type){
	message = "网点保养单待配工";
}else if(Order.aftersaledeal == type){ 
	message = "网点驳回单据";
}

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/meidiserver/";
 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>   
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/cookie/jquery.cookie.js"></script>
 
<style type="text/css">
td {
 align:center 
} 
</style>  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />   

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript">
var num = 100; 
var page = 1; 
var sort = "submittime desc";
var sear = ""; 
var count = ""; 
var statues = "<%=type%>"; 
var jsonallp = <%=listallpp%>; 

$(function () { 
	 
	$("select[id='numb'] option[value='"+num+"']").attr("selected","selected");
	$("select[id='sort'] option[value='"+sort+"']").attr("selected","selected");
	
	$("#page").blur(function(){
		 page = $("#page").val();
		 $("#page").val(page);
		 initOrder(type,statues,num,page,sort,sear); 
		 //window.location.href=href+"?page="+page+"&numb="+num+"&sort="+sort+"&sear="+sear;
	 });
  
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		  
		 initOrder(type,statues,num,page,sort,sear); 
		// alert(num);
		// window.location.href=href+"?page="+page+"&numb="+num+"&sort="+sort+"&sear="+sear;
	 }); 
	   
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val()); 
		 initOrder(type,statues,num,page,sort,sear);
		 //window.location.href=href+"?page="+page+"&numb="+num+"&sort="+sort+"&sear="+sear;
	 }); 
}); 

function pageadd(){
	page = page*1 +1 ;
	initOrder(type,statues,num,page,sort,sear);
}

function pagesubtraction(){
	page = page*1 -1 ;
	if(page <1){
		page = 1;
	}
	initOrder(type,statues,num,page,sort,sear);
}

function pageinit(){
	page = 1 ;
	initOrder(type,statues,num,page,sort,sear);
} 

function pagelast(){
	page = Math.ceil(count/num) ;
	initOrder(type,statues,num,page,sort,sear);
}

function exportServelet(){
	if(8 == type){
		window.location.href="../../Print?method=exportall&type="+type+"&statues="+statues+"&num="+num+"&page="+page+"&sort="+sort+sear;
	}else {
		window.location.href="../Print?method=exportall&type="+type+"&statues="+statues+"&num="+num+"&page="+page+"&sort="+sort+sear;
	}
	//alert(1);  
	
}
 
function initsearchOrder(){
	var account= $.cookie("searaftersale");
	if(account != "" && account != null){
		account = account.substring(1,account.length);
	}
	if(null != account && ""!= account){
		account = account.split("&");
	}

	if(null != account ){
		for(var i=0;i<account.length;i++){
			var keyvalue = account[i];
			keyvalue = keyvalue.split("=");
			var key = keyvalue[0];
			var value = keyvalue[1];
			if("statues1" == key || "statues2" == key  || "statues3" == key || "statues4" == key || "statues" == key || "deliverytype" == key ){
				if("statues" == key){
					key = "statues0";
				}
				$("#"+key+value).attr("checked","checked");
			}else if("oderStatus" == key || "deliveryStatues" == key){
				value = value.split(",");
				for(var m=0;m<value.length;m++){
					cvalue = value[m];
					$("#"+key+cvalue).attr("checked","checked");
				}
				
			}else{
				$("#"+key).val(value);
			}
		}
	}
	
	
}

function SearchDiv(){
	$("#wrapsearch").css("display","block");
	 intsearch();
	initsearchOrder();
} 
</script>
</head>  
<body>
   <div style="text-align:center">
    <input type="hidden" id="search"  value=""/>
    <input type="hidden" id="sear"  value=""/>
 
 <table  cellspacing="1" style="width: 95%;margin:auto"> 
  <tr >   
   <td >
        现在位置：<%=message %> 
   &nbsp;&nbsp;&nbsp;&nbsp;
   </td>
   <td>
                     行数
     	<select class = "category" name="category"  id="numb">
     	 <option value="100">100</option> 
     	 <option value="200">200</option>
     	 <option value="500">500</option> 
     	 <option value="1000">1000</option> 
     	<!--  <option value="-1">所有</option> --> 
     	</select>     
   </td>
   <td>
     <a href="javascript:void(0)" onclick="pageinit()">首页</a>     
     <a href="javascript:void(0)" onclick="pagesubtraction()">上一页</a>
     <a href="javascript:void(0)" onclick="pageadd()">下一页</a>  
     <a href="javascript:void(0)" onclick="pagelast()">尾页</a>   
   </td>
   <td>
    第    
     <input type="text" size="5" value=""  id="page"/>    
        页
   </td>
   <td>
      共
    <label id="count" style="color:red;"> </label> 
    
   条记录  
   </td>
   <td>
   按 
       <select class ="" name=""  id="sort" >  
       <option value="andate asc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
     	 <option value="andate asc">预约日期</option> 
     	 <option value="saledate asc">开票日期 </option>  
     	 <option value="submittime desc">上报时间 </option>   
     	</select>
        排序 
   
   </td>
   <td>
   <a href="javascript:void(0)" onclick="SearchDiv()"><img src="<%=basePath %>/image/search.png"  style="width:30px;height:30px;" alt="弹出广告图"/></a>
   </td>
   <td>
   <!--<a href="<%=basePath %>Print"><font style="color:red;font-size:20px;" >导出数据</font> </a>  
       <a href="javascript:void(0)" onclick="exportServelet()" ><font style="color:red;font-size:20px;" >导出数据</font></a>   -->  
   </td>
  </tr>
  </table> 
  </div>

</body>
</html>
