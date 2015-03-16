<%@ page language="java"  pageEncoding="UTF-8" import="aftersale.*" contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>  
<%  
List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend); //UserService.getsencondDealsend(user);
    
List<Category> list = CategoryManager.getCategory(user,Category.sale); 
String clist = StringUtill.GetJson(list);
  
List<Category> listmaintain = CategoryService.getlistmaintain(); 
String clistmaintain = StringUtill.GetJson(listmaintain);

HashMap<String,ArrayList<String>> listt = ProductService.gettypeName();
 
String plist = StringUtill.GetJson(listt);

String id = request.getParameter("id");
//System.out.println(id);
AfterSale af = null ; 
String strorder= null;   
  
if(!StringUtill.isNull(id)){ 
	af = AfterSaleManager.getAfterSaleID(user,id);
	strorder = StringUtill.GetJson(af); 
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
   var jsonmaintain = <%=clistmaintain%>;
   var listop =  null;
   // statues 表示是否是相同顾客报装
   
   var order = <%=strorder%>;

   var row = 1;  
   var rows = new Array();
    
   $(document).ready(function(){
	//initphone();
	init();   
	initproductSerch("#ordercategory","#ordertype");
   });
   
   
   function init(){
		   
	   if(order != null && order != "" && order != "null"){
		   $("#uname").val(order.as.uname);
		   $("#phone").val(order.as.phone); 
		   
		   $("select[id='ordercategory'] option[id='"+order.as.cid+"']").attr("selected","selected");
		   
		   $("#ordercategory").attr("disabled","disabled");
		   $("#hiddenordercategory").val(order.as.cid);  
		   $("#hiddenordercategory").attr("name","ordercategory");
		   
		   $("#ordertype").val(order.as.tName);
		   $("#ordertype").attr("readonly","readonly");
		   
		   $("#orderbatchNumber").val(order.as.batchNumber);
		   $("#orderbarcode").val(order.as.barcode);
		   
		   $("#andate").val(order.as.andate); 
		   $("#saledate").val(order.as.saledate); 
		   $("#locations").val(order.as.location);
		   $("#detail").val(order.as.detail);
		   

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

    
 
   function addrow(listo){
		
	    rows.push(row);
		var yellow = "";
		if(row%2 == 0){
			yellow = "#c4e1e1"; 
		} 
		     
		var str =  '<tr id=produc'+row+ '>' +
	               '<td>维修类别<span style="color:red">*</span></td>'+
	               '<td ><input type="hidden" name="product" value="'+row+'"/>'+
	               '<select class = "category" name="ordercategory'+row+'"  id="ordercategory'+row+'"  style="width:95% ">'; 
	               for(var i=0;i<jsonmaintain.length;i++){
	            	   var ckeck = "";
	            	   var jo = jsonmaintain[i];  
	             	   if(jo.id == listo.categoryId){
	            		   str += '<option value='+jo.id+'  selected="selected" >'+jo.name+'</option>';
	            	   }else { 
	            		   str += '<option value='+jo.id+'>'+jo.name+'</option>'; 
	            	   }

	               }
	               str += '</select> '+
	               ' </td>'+ 
	               ' <td >配件<span style="color:red">*</span></td> '+
	               ' <td  ><input type="text"  id="ordertype'+row+'" name="ordertype'+row+'" value="" style="width:90% " /></td> ' +
	          	   ' <td  ><input type="button"   style="color:white;background-color:#0080FF" name="" value="删除" onclick="deletes(produc'+row+','+row+')"/></td>'+
	          	   '</tr>';
	                 
	        $("#tableproduct").append(str); 
	        initproductSerch("#ordercategory"+row,"#ordertype"+row);
	        row++;  
	        } 
	 
	 function deletes(str,str2){
	 	$(str).empty();
		    rows.splice($.inArray(str2,rows),1);
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
	 var uid = $("#uid").val();
	 var thistime = $("#thistime").val();
	 var nexttime = $("#nexttime").val();
	 
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
	  
	 /*
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
		*/ 
	 if(locations == "" || locations == null || locations == "null"){
		 alert("详细地址不能为空");
		 return false;
	 } 
	 /*  
	 if(rows.length <1){
			alert("请添加保养项目");
			return false ; 
		 }else {
			 for(var i=0;i<rows.length;i++){
				 var str = $("#ordertype"+rows[i]).val();
				 var juese = $("#ordercategory"+rows[i]).val();
				 if(str == "" || str == null || str == "null"){
					 alert("送货型号不能为空");
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
		 }
	}
	  
	 */
	 if(uid == "" || uid  == null || uid  == "null"){
		 alert("维修单位不能为空");
		 return false;
	 }
	 
	 if(thistime == "" || thistime == null || thistime == "null"){
		 alert("保养日期不能为空");
		 return false;
	 }
	/*  
	 if(nexttime == "" || nexttime == null || nexttime == "null"){
		 alert("下次保养日期日期不能为空");
		 return false;
	 }
	 */
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
<input type="hidden" name="typemethod" value="maintain"/> 
<input type="hidden" name="token" value="<%=token%>"/>  
 
<div class="s_main_tit">现在位置:<span class="qian">系统外维修保养配工</span></div>  
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
  <input type="hidden" name="hiddenordercategory" id="hiddenordercategory">
  <select  name="ordercategory" id="ordercategory"  >
  
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
   <td><input type="text" name="orderbatchNumber" id="orderbatchNumber"    /> </td>
   <td>条码</td> 
   <td><input type="text" name="orderbarcode" id="orderbarcode"    /> </td> 

   </tr> 
    <tr class="asc"> 
    
    <td  >安装日期<span style="color:red">*</span></td>
    <td  ><input class="date2" type="text" name="andate" id ="andate" onclick="new Calendar().show(this);"  readonly="readonly" ></input>   </td>
   
    <td  >购买日期<span style="color:red">*</span></td> 
    <td  ><input class="date2" type="text" name="saledate" id ="saledate" onclick="new Calendar().show(this);"   readonly="readonly" ></input>   </td>

 </tr>
   <tr class="asc">  
    <td >详细地址<span style="color:red">*</span></td>
    <td ><textarea  id="locations" name="locations" ></textarea></td>
   <td >故障保养内容<span style="color:red">*</span></td>
    <td ><input type="hidden" value=""><textarea  id="fault" name="fault" ></textarea></td> 
    
   </tr>    
   <tr class="asc">
     <td colspan=4> 
     <table id="tableproduct"  style="width:100%">  
      
      
      
     </table>
     </td>
     
    
   </tr> 
     
     <tr class="dsc" > 
    <td colspan=4>
     <input type="button"  name="" style="color:red" value="增加维修配件" onclick="addrow(0)" width="100%"  />
    </td>
   </tr>  
   
 <tr class="asc"> 
     
    <td  >保养单位<span style="color:red">*</span></td>
    <td  >
    <select  name="uid" id="uid"  >
  
   <option></option>
  <%  
  for(int i=0;i<listS.size();i++){
	  User cate = listS.get(i);
	 
  %>  
    
    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getUsername()%></option>
  <%
  }
  %>
   
  </select> 
    
       </td>
    
    <td  >保养时间<span style="color:red">*</span></td>
    <td  ><input  type="text" name="thistime" id ="thistime" onclick="new Calendar().show(this)"   placeholder="必填"  readonly="readonly" ></input>   </td>
 
 </tr>
 <tr class="asc">
  <td  >下次保养时间<span style="color:red">*</span></td>
    <td  ><input  type="text" name="nexttime" id ="nexttime" onclick="new Calendar().show(this)"   readonly="readonly" ></input>   </td>
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