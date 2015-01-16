<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>  
<%  
     
List<Category> list = CategoryManager.getCategory(user,Category.sale); 
String clist = StringUtill.GetJson(list);
 
HashMap<String,ArrayList<String>> listt = ProductService.gettypeName();
 
String plist = StringUtill.GetJson(listt);

String id = request.getParameter("id");


Order order = null ; 
String strorder= null;

if(!StringUtill.isNull(id)){
	order = OrderManager.getOrderID(user,Integer.valueOf(id));
	strorder = StringUtill.GetJson(order);

}
 
%> 
   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>报装单提交页面</title>
 <link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script> 
 
<link rel="stylesheet" href="../../css/jquery-ui.css">
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<style type="text/css"> 

#wrap{
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
}

#table td{
   text-align: center;
}

</style>  
<script type="text/javascript">
  
   var jsons = <%=plist%> ;
   var cid = "";  
   var ids = "";  
   var json = <%=clist%>;
   var listop =  null;
   // statues 表示是否是相同顾客报装
   
   var order = <%=strorder%>;

   
   
   $(document).ready(function(){
	//initphone();
	//init();   
	initproductSerch("#ordercategory","#ordertype");
   });
   
   
   function init(){
		   
	   if(listop != null && listop != "" && listop != "null"){       
			  for(var i=0;i<listop.length;i++){
				  var listo = listop[i];  
						$("#ordertype0").val(listo.typeName);
						$("#orderproductNum0").val(listo.count);
						//alert(listo.categoryId);
						//alert($("#ordercategory0 #33").attr("selected"));  
						$("select[id='ordercategory0'] option[id='"+listo.categoryId+"']").attr("selected","selected");
						$("select[id='productsta0'] option[id='"+listo.salestatues+"']").attr("selected","selected");
						//$("#ordercategory0 #"+listo.categoryId).attr("selected","selected");  
	   }
	   
	   }
	   //listgg
	  
	   
	   
	   if(order != null && order != "" && order != "null"){
		   if(statues == null || statues == ""){
			   $("#serviceDate").val(order.saleTime);
			  
			   $("#pos").val(order.pos);
			   $("#sailId").val(order.sailId);
			   $("#checked").val(order.check);
			   $("#phoneremark").val(order.phoneRemark);
			   $("#posremarkremark").val(order.phoneRemark);
			   $("#checkedremark").val(order.phoneRemark);
			   $("#sailidremark").val(order.phoneRemark);
		   }
		   
		   $("#serviceDate2").val(order.odate);
		   $("#username").val(order.username);
		   $("#phone1").val(order.phone1);
		   $("#phone2").val(order.phone2);
		   $("#locations").val(order.locateDetail);
		   $("#remark").val(order.remark);
		   $("#quyu").val(order.locate); 
		   
		   
	   }
   }
    
   function initproductSerch(str,str2){ 
	    cid = $(str).val(); 
		$(str2).autocomplete({ 
			 source: jsons[cid]
		    }); 
		$(str).change(function(){
			$(str2).val("");
			cid = $(str).val();  
			$(str2).autocomplete({
				 source: jsons[cid]
			    });
			}) ;
       } 

    
 function initphone(){ 
	   $("#phone1").focus(function(){
		    $("#phone1").css("background-color","#FFFFCC");
		  });
		  $("#phone1").blur(function(){
		    $("#phone1").css("background-color","#D6D6FF");
		    var phone1 = $("#phone1").val(); 
		    //alert(phone1);
		    $.ajax({ 
		        type:"post",  
		         url:"server.jsp",  
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=phone1&name="+phone1+"&branch=<%=user.getBranch()%>",
		         dataType: "",  
		         success: function (data) {
		        	 if("true" == data){ 
		        		 var question = confirm("顾客电话相同单据有可能录重，是否继续?");	
		        			if (question == "0"){
		        				//$("#phone1").val("");
		        				//$("#phone1").focus(); 
		        				window.location.href="order.jsp";
		        				return false ;
		        			}else {   
		        				$("#phoneremark").val("1"); 
		        			}  
		        	 }else {
		        		 $("#phoneremark").val("0"); 
		        	 }  
		           },   
		          error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	  return false ;
		            } 
		           });  
		  });  
  }
 
 function checkedd(){
     
	 var uname = $("#uname").val();
	 var phone = $("#phone").val();
	 var juese = $("#ordercategory").val();
	 var str = $("#ordertype").val();
	 
	 
	 var orderbatchNumber = $("#orderbatchNumber").val();
	 var orderbarcode = $("#orderbarcode").val();
	 var andate = $("#andate").val();
	 var saledate = $("#saledate").val();
	 
	 var locations = $("#locations").val();
	 
	 
	 
	 if(uname == "" || uname == null || uname == "null"){
		 alert("顾客姓名不能为空");
		 return false;
	 }
	 
	 if(phone == "" || phone == null || phone == "null"){
		 alert("电话不能为空");
		 return false;
	 }
	 else {
	    var filter = /^1[3|4|5|7|8][0-9]\d{8}$/;  
	    var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		 if(!filter.test(phone) && !isPhone.test(phone)){
			 alert("请填写正确的手机号码或电话");     
			 return false;  
		 }
	 }  
	 
	 
	 if(str == "" || str == null || str == "null"){
		 alert("产品型号不能为空");
		 return false; 
	 }else {
		 var flag = true ;
         	  var array = jsons[juese];
         	  //alert(array); 
         	  if(array != "" && array != null &&  array != "null" && array != undefined && array != "undefined"){
	         	   for(var k=0;k<array.length;k++){
	         		   if(str == array[k]){
	         			   flag = false  ;
	         		   }
	         	   }
	         	   }
		
		 if(flag){
			 alert("系统不存在此型号"+str);
			 return false ;
		 }
	 }
	  
	 
	 if(orderbatchNumber == "" || orderbatchNumber == null || orderbatchNumber == "null"){
		 alert("批号不能为空");
		 return false;
	 }
     
    
	 if(orderbarcode == "" || orderbarcode == null || orderbarcode == "null"){
		 alert("条码不能为空");
		 return false;
	 }
	 
     if(andate == "" || andate == null || andate == "null"){
		 alert("安装日期不能为空");
		 return false;
	 }
	 
     if(saledate == "" || saledate == null || saledate == "null"){
		 alert("购买日期不能为空");
		 return false;
	 }
		 
	 if(locations == "" || locations == null || locations == "null"){
		 alert("详细地址不能为空");
		 return false;
	 }
	 
	 
	 $("#submit").css("display","none"); 
	 return true ; 
 }
 
</script>


</head>
<body>

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
  </div>

<div style="height:70px;">
</div>
  
<form action="../../user/OrderServlet"  method ="post"  id="form"   onsubmit="return checkedd()"  > 
<!--  头 单种类  -->  

<input type="hidden" name="orderid" value="<%=id %>"/>
<input type="hidden" name="method" value="aftersale"/>
<input type="hidden" name="token" value="<%=token%>"/>  

<div class="s_main_tit">上报单位:<span class="qian"><%=BranchService.getMap().get(Integer.valueOf(user.getBranch())).getLocateName() %></span></div>  
<!--  订单详情  -->   
  
 <div id="wrap"> 
  <table  cellspacing="1" id="table" style="width:90%">
  <tr class="asc">
    <td  >顾客姓名<span style="color:red">*</span></td>
    <td ><input type="text" id="uname" name="uname" value=""  placeholder="必填" ></input></td>
    
    <td >电话<span style="color:red">*</span></td>
    <td ><input type="text"  id="phone"  name="phone" value=""   placeholder="必填" /></td>

   </tr> 		
  <tr class="asc">
  <td  >产品类别<span style="color:red">*</span></td>
  <td  >
  <select  name="ordercategory" id="ordercategory" >
   <option></option>
  <% 
  for(int i=0;i<list.size();i++){
	  Category cate = list.get(i);
	 
  %>  
    
    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getName()%></option>
  <%
  }
  %>
  
  </select>   
  </td>
   <td  >产品型号<span style="color:red">*</span></td> 
   <td  > 
   <input type="text" name="ordertype" id="ordertype"   placeholder="必填" /> 
    </td> 
    
   </tr>    

   <tr class="asc">
   <td>批号</td>  
   <td><input type="text" name="orderbatchNumber" id="orderbatchNumber"   placeholder="必填" /> </td>
   <td>条码</td> 
   <td><input type="text" name="orderbarcode" id="orderbarcode"   placeholder="必填" /> </td> 

   </tr> 
  
   
    <tr class="asc"> 
    
    <td  >安装日期<span style="color:red">*</span></td>
    <td  ><input class="date2" type="text" name="andate" id ="andate" onclick="new Calendar().show(this);"  placeholder="必填"  readonly="readonly" ></input>   </td>
   
    <td  >购买日期<span style="color:red">*</span></td>
    <td  ><input class="date2" type="text" name="saledate" id ="saledate" onclick="new Calendar().show(this);"  placeholder="必填"  readonly="readonly" ></input>   </td>

 </tr>
   <tr class="asc"> 
    <td >详细地址<span style="color:red">*</span></td>
    <td ><textarea  id="locations" name="locations" ></textarea></td>  
    <td >备注</td>
    <td ><textarea  id="remark" name="remark" ></textarea></td>
   
   </tr>
   
   
   <tr class="asc"> 
    <td colspan="4" style="background-color:orange" class="center"><input type="submit"  value="提  交" /></td>
   </tr>
   </table>
   </div>
 <div class="center"> 
 </div>
</form>


</body>
</html>