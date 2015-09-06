<%@ page language="java" import="java.util.*,locate.*,category.*,group.*,user.*,utill.*,product.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%

User user = (User)session.getAttribute("user");
List<Category> list = CategoryManager.getCategory();
String clist = StringUtill.GetJson(list);

HashMap<Integer,ArrayList<Product>> map = ProductManager.getProduct();
List<Locate> listl = LocateManager.getLocate();

HashMap<String,ArrayList<String>> listt = ProductManager.getProductName();
String plist = StringUtill.GetJson(listt);

%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>报装单提交页面</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script> <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="../css/songhuo.css">
<link rel="stylesheet" href="../css/bass.css">

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  
<script type="text/javascript">

  var availableTags = '<%=plist%>';
   var jsons =  $.parseJSON(availableTags);
   var cid = "";
   var ids = "";
   var CList = '<%=clist%>';
   var row = 1;
   var rowz = 1;
   var rows = new Array();
   var rowzs = new Array();

   $(document).ready(function(){
	initTime();
	initpos();
	initPhone();
	initproduct();
   });
   
   function initproductSerch(str,str2){ 
		$(str).change(function(){
			var id = ($(str).children('option:selected').val());
			cid = $(str).val();
			alert(jsons[cid]);
			$(str2).autocomplete({
				 source: jsons[cid]
			    });
			}) ;
       }
   
   function initPhone(){
	   $('#phone1').blur(function (){
			var mes = $('#phone1').val();
			 checkMessage("#phone1",mes);
		});
   }
   
   function initTime(){
	   var opt = { };
	    opt.date = {preset : 'date'};
		$('#serviceDate').val('').scroller('destroy').scroller($.extend(opt['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020',minDate: new Date()}));
		var opt2 = { };
	    opt2.date = {preset : 'date'};
		$('#serviceDate2').val('').scroller('destroy').scroller($.extend(opt2['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'2014',endYear:'2014',minDate: new Date()}));
		var ids = ($(this).children('option:selected').val());
   }
 
 function initpos(){
	   $("#pos").focus(function(){
		    $("#pos").css("background-color","#FFFFCC");
		  });
		  $("#pos").blur(function(){
		    $("#pos").css("background-color","#D6D6FF");
		    var categoryName = $("#pos").val();
		    $.ajax({ 
		        type:"post", 
		         url:"server.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=pos&huiyuanName="+categoryName,
		         dataType: "", 
		         success: function (data) {
		        	 if("true" == data){
		        		 alert("pos已存在，确定提交？");
		        		 $("#name").focus();
				          return ;
		        	 }
		           }, 
		          error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	  //alert(123);
		            } 
		           });
		  });  
    }
   
 function initproduct(){
	 initproductSerch("#dingmacategory","#dingmatype");
	 initproductSerch("#dingma_nocategory","#dingma_notype");
		if ($.browser.msie) { 
			$('input:radio').click(function () { 
				if(this.checked){
			    	 var a = $(this).val();
			    	 if(0==a){
			    		 $("#dingma").css("display","none");
			    		 $("#dingma_no").css("display","block");
			    		 
			    	 }else {
			    		 $("#dingma_no").css("display","none"); 
			    		 $("#dingma").css("display","block");
			    		
			    	 } 	 
			    	 }
			}); 
			}; 
			
		  $('input:radio').change(function() {
			     if(this.checked){
			    	 var a = $(this).val();
			    	 if(0==a){
			    		 $("#dingma").css("display","none");
			    		 $("#dingma_no").css("display","block");
			    	 }else {
			    		 $("#dingma_no").css("display","none"); 
			    		 $("#dingma").css("display","block");
			    	 } 	  
			 }
	      }) ;  
      } 
 
    function deletes(str,str2){
	    $(str).empty();
	    rows.splice($.inArray(str2,rows),1);
     }
 
 
 function addrow(){
    rows.push(row);
	var yellow = ""; 
	if(row%2 == 0){
		yellow = "#fff";
	}
	var json =  $.parseJSON(CList);
	var str = "";
	    str += '<div id="produc'+row+'" name="produc">'+
	           '<input type="hidden" name="product" value="'+row+'"/>'+
	    	   '<table style="width:100%;background-color:'+yellow+'">'+
               ' <tr>' +
               '<td width="25%" class="center">产品类型<span style="color:red">*</span></td>'+
               '<td width="50%" class="center"><select class = "category" name="category'+row+'"  id="category'+row+'"  style="width:95% ">';
               for(var i=0;i<json.length;i++){
            	   var jo = json[i];
            	   str += '<option value='+jo.name+'>'+jo.name+'</option>';
               }
          str += '</select> '+
               ' </td>'+
               '<td width="25%" class="center"><input type="button"   color= "red" name="" value="删除" onclick="deletes(produc'+row+','+row+')"/></td>'+
               ' </tr>' +
               '</table>'+
               '<table style="background-color:'+yellow+'">'+
               ' <tr>'+
               ' <td width="25%" class="center">送货型号<span style="color:red">*</span></td> '+
               ' <td width="35%" class=""><input type="text"  id="type'+row+'" name="type'+row+'" style="width:90% " onclick="serch(type'+row+')"/><div id="aotu'+row+'"></div></td> ' +
               ' <td width="10%" class="center">数量</td> '+
               ' <td width="10%" class=""><input type="text"  id="productNum'+row+'" name="productNum'+row+'" value="1" style="width:50%"/></td> '+
               ' <td width="10%" class="center"><input type="button"   name="" value="+" onclick="add(productNum'+row+')"/></td> '+ 
               ' <td width="10%" class="center"><input type="button"   name="" value="-" onclick="subtraction(productNum'+row+') " /></td> '+ 
               ' </tr>'+
               '<table>'+
                '</div>';
        $("#productDIV").append(str);
        row++;
        initproductSerch("#produc"+row,"#type"+row);
        }	
 
        function addrowZ(){
        	rowzs.push(rowz);
        	var yellow = "";
        	if(rowz%2 == 0){
        		yellow = "#fff";
        	}
        	
        	var str = '<div id=gift'+rowz+'><input type="hidden" name="gift" value="'+rowz+'"/>';
        	str += '<table style="background-color:'+yellow+'">'+
        	  '<tr>'+
        	   ' <td width="25%" class="center">赠品</td>'+
        	    '<td width="35%" class=""> <input type="text"  id="productN'+rowz+'" name="POS" style="width:90% " value=""/> </td>'+
        	   ' <td width="10%" class="center">数量</td>'+
        	   ' <td width="10%" class=""><input type="text"  id="productNn'+rowz+'" name="POS" value="1" style="width:50%"/></td>'+
        	   ' <td width="10%" class="center"><input type="button"   name="" value="+" onclick="add(productNn'+rowz+')" /></td>'+
        	   ' <td width="10%" class="center"><input type="button"   name="" value="-" onclick="subtraction(productNn'+rowz+')" /></td>'+
        	  ' </tr>'+
        	  '  </table>'+
        	 '  <table style="width:100%;background-color:'+yellow+'">'+
        	 ' <tr>'+
        	  '  <td width="25%" class="center">赠品状态</td>'+
        	  '  <td width="50%" class="">'+
        	  '  <select class ="productN'+rowz+'"   style="width:90%; ">'+
        	  '  	<option value="">已自提</option>'+
        	   ' 	<option value="">需配送</option>'+
        	  '  </select>'+

        	   ' </td>'+
        	   ' <td width="25%" class="center"><input type="button"   color= "red" name="" value="删除" onclick="deletes(gift'+rowz+')"/></td>'+
        	   '</tr>'+

        	'</table>';
	        $("#zengpDIV").append(str); 
	        rowz++;
	 }	
  
  function add(str){
	 var id = $(str).val();
     var nid = id*1 + 1;
	 $(str).val(nid);
 }
 
 function subtraction(str){
	 var id = $(str).val();
	 
	 if(id*1<=1){
		 $(str).val(1); 
		 return ;
	 }
     var nid = id*1 - 1;
	 $(str).val(nid);
 }
 
 function checkNull(str){
	 if(str == "" || tr == null || str == "null"){
		 return true;
	 }
 }
 
 
 function checkMessage(str,message){
	 var filter = /^1[3|4|5|8][0-9]\d{4,8}$/;
	 if(filter.test(message)){
		 return true;
	 }else{
		 alert("请填写正确的手机号码");
		 $(str).focus();
		 return false;
	 }
 }
 
 
 function checkedd(){
	// return true ;
	 var saledate = $("#serviceDate").val();
	 var andate = $("#serviceDate2").val();
	 var pos = $("#pos").val();
	 var sailId = $("#sailId").val();
	 var check = $("#check").val();
	 var username = $("#username").val();
	 var phone1 = $("#phone1").val();
	 var phone2 = $("#phone2").val();
	 var locations = $("#locations").val();
	 var remark = $("#remark").val();
	 var radio = $('input:radio[name="Statues"]:checked').val();
	// var phone1 = $("#phone1").val();
	// var name = $("#name").val();
	// var detail = $("#detail").val();	
     if(saledate == "" || saledate == null || saledate == "null"){
		 alert("销售日期不能为空");
		 return false;
	 }
	 
	 if(andate == "" || andate == null || andate == "null"){
		 alert("预约安装时间不能为空");
		 return false;
	 }
     
	 if(pos == "" || pos == null || pos == "null"){
		 alert("pos单号不能为空");
		 return false;
	 }
	 if(sailId == "" || sailId == null || sailId == "null"){
		 alert("销售单号不能为空");
		 return false;
	 }
	 
	 if(check == "" || check == null || check == "null"){
		 alert("校验码不能为空");
		 return false;
	 }
	 if(radio == "" || radio == null || radio == "null"){
		 alert("请选择销售类型");
		 return false;
	 }
	 // 1   是定码
	 if("0"== radio){
		 var product = $("#dingma_notype").val();
		 if(product == "" || product == null || product == "null"){
			 alert("送货型号不能为空");
			 return false;
		 }
	 }else {
		 var product = $("#dingmatype").val();
		 if(product == "" || product == null || product == "null"){
			 alert("票面类别不能为空");
			 return false;
		 }
	 }
	 for(var i=0;i<rows.length;i++){
		 var str = $("#type"+rows[i]).val();
		 if(str == "" || str == null || str == "null"){
			 alert("送货型号不能为空");
			 return false;
		 }
		
	 }
	 if(username == "" || username == null || username == "null"){
		 alert("姓名不能为空");
		 return false;
	 }
	 if(phone1 == "" || phone1 == null || phone1 == "null"){
		 alert("电话不能为空");
		 return false;
	 }
	 
	 if(locations == "" || locations == null || locations == "null"){
		 alert("详细地址不能为空");
		 return false;
	 }
	 
	 $('input[name="permission"]:checked').each(function(){ 
		    alert($(this).val());  
	 });
	 
	 return true ;
 }
 
</script>


</head>


<body>
<div class="s_main">
<div class="s_main_logo"><img src="../image/logo.png"></div>
<form action="OrderServlet"  method ="post"  id="form"  onsubmit="return checkedd()" >
<!--  头 单种类  -->
<div class="s_main_tit">顶码销售<span class="qiangdan"></span></div>
<div class="s_main_tit">门店:<span class="qian"><%=user.getBranch() %></span></div>

<!--  订单详情  -->
<div class="s_dan_box"> </div>

<table style="width:100% ">
  <tr>
    <td width="25%" class="center">销售日期<span style="color:red">*</span></td>
    <td width="50%" class=""><input class="date" type="text" name="saledate" placeholder="选填"  id="serviceDate"  readonly="readonly" style="width:90% "></input>   </td>
    <td width="25%" class="center"></td>
  </tr>
  <tr>
    <td width="25%" class="center">预约安装日期<span style="color:red">*</span></td>
    <td width="50%" class=""><input class="date2" type="text" name="andate" id ="serviceDate2" placeholder="选填"  readonly="readonly" style="width:90% "></input>   </td>

    <td width="25%" class="center"></td>
   </tr>
  <tr>
    <td width="25%" class="center">POS单号 <span style="color:red">*</span></td>
    <td width="50%" class=""> <input type="text"  id="pos" name="POS" style="width:90% "/></td>
    <td width="25%" class="center"> </td>
   </tr>
  <tr>
    <td width="25%" class="center">销售单号 <span style="color:red">*</span></td> 
    <td width="50%" class=""><input type="text"  id="sailId" name="sailId" style="width:90% " /></td>
    <td width="25%" class="center"></td>
  </tr>
  <tr>
    <td width="25%" class="center">效验码<span style="color:red">*</span></td>
    <td width="50%" class=""> <input type="text"  id="check" name="check" style="width:90% " /></td>
    <td width="25%"></td>
  </tr>
  <tr>
    <td width="25%" class="center">顶码销售<span style="color:red">*</span></td>
    <td width="50%" class="">  是
		<input type="radio"  name="Statues" value="1" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		否
		<input type="radio" name="Statues" value="0" /></td>
    <td width="25%"></td>
  </tr>
  </table>
  
  <div id = "productDIV" >
  <div id="dingma"  style= "display:none;">
  <table style="width:100%;background-color:"> 
  <tbody>
  <tr>
  <td width="25%" class="center">产品类型<span style="color:red">*</span></td>
  <td width="50%" class="center">
  <select class="category" name="dingmacategory" id="dingmacategory" style="width:95% ">
  
  <%
  for(int i=0;i<list.size();i++){
	  Category cate = list.get(i);
  %>
    <option value="<%= cate.getId()%>"><%=cate.getName()%></option>
  <%
  }
  %>
  </select>  
  </td>
  <td width="25%" class="center"></td> 
  </tr></tbody></table>
  <table style="background-color:"> 
  <tbody><tr> 
  <td width="25%" class="center">票面型号<span style="color:red">*</span></td> 
   <td width="35%" class="">
   <input type="text" id="dingmatype" name="dingmatype" style="width:90% " onclick="serch(type1)">
   <div id="aotu1"></div>
   </td>  
   </tr>
     <tr>
   <td width="25%" class="center">送货型号<span style="color:red">*</span></td> 
   <td width="35%" class="">
   <input type="text" name="dingma_notype" id="dingma_notype" class="cba"/> 
  </td> 
   <td width="10%" class="center">数量</td>  
   <td width="10%" class=""><input type="text" id="dingmaproductNum" name="dingmaproductNum" value="1" style="width:50%"></td>  
   <td width="10%" class="center"><input type="button" name="" value="+" onclick="add(dingmaproductNum)"></td>  
   <td width="10%" class="center"><input type="button" name="" value="-" onclick="subtraction(dingmaproductNum) "></td>  
   </tr>
   </tbody>
   </table><table>
   </table>
   </div>
   </div>
  <div id = "zengpDIV"  >
   <div id="dingma_no"  style= "display:none;">
  <input type="hidden" name="product" value="0">
  <table style="width:100%;background-color:"> 
  <tbody>
  <tr>
  <td width="25%" class="center">产品类型<span style="color:red">*</span></td>
  <td width="50%" class="center">
  <select class="category" name="dingma_nocategory" id="dingma_nocategory" style="width:95% ">
  <%
  for(int i=0;i<list.size();i++){
	  Category cate = list.get(i); 
  %>
    <option value="<%=cate.getId()%>"><%=cate.getName()%></option>
  <%
  }
  %>
  </select>  
  </td>
  <td width="25%" class="center">
  <input type="button"  name="" value="增加商品" onclick="addrow()" width="100%"/>
  </td> 
  </tr></tbody></table>
  <table style="background-color:"> 
  <tbody><tr> 
  <td width="25%" class="center">送货型号<span style="color:red">*</span></td> 
   <td width="35%" class="">
   <input type="text" name="dingma_notype" id="dingma_notype" class="cba"/> 
  </td>   
   <td width="10%" class="center">数量</td>  
   <td width="10%" class=""><input type="text" id="dingma_noproductNum" name="dingma_noproductNum" value="1" style="width:50%"></td>  
   <td width="10%" class="center"><input type="button" name="" value="+" onclick="add(dingma_noproductNum)"></td>  
   <td width="10%" class="center"><input type="button" name="" value="-" onclick="subtraction(dingma_noproductNum)"></td>  
   </tr></tbody>
   </table><table>
   </table>
   </div>
  </div>
  
   <table style="width:100% ">
  <tr>
    <td width="25%" class="center">姓名<span style="color:red">*</span></td>
    <td width="50%" class=""><input type="text" id="username" name="username" value="" style="width:90%"></input></td>
   <td width="25%" class="center"></td>
   </tr> 
   <tr>
    <td width="25%" class="center">电话<span style="color:red">*</span></td>
    <td width="50%" class=""><input type="text"  id="phone1"  name="phone1" value="" style="width:90%" /></td>
   <td width="25%" class="center"></td>
   </tr>
  <tr>
    <td width="25%" class="center">电话2</td>
    <td width="25%" class=""><input type="text" style="width:90%;" id="phone2" name="phone2" value=""/></td>
   </tr>
    <tr>
    <td width="25%" class="center">所在区域<span style="color:red">*</span></td>

    <td width="25%" class=""><select class = "quyu" name="diqu" >
		<%
		 for(int i=0;i<listl.size();i++){
			 Locate lo = listl.get(i);

		%>	
		 <option value="<%=lo.getLocateName()%>"><%=lo.getLocateName()%></option>
		<%
		 }
		%>
	</select>
	</td>
   </tr>
</table>
   <table style="width:100% ">
  <tr>
    <td width="25%" class="center">详细地址<span style="color:red">*</span></td>
    <td width="75%" class=""><textarea  id="locations" name="locations" ></textarea></td>  
   </tr>
  <tr>
    <td width="25%" class="center">备注</td>
    <td width="75%" class=""><textarea  id="remark" name="remark" ></textarea></td>
   </tr>
</table>
 <table style="width:100% ">
  <tr> 
    <td width="25%" class="center"><input type="submit" value="提  交" /></td>
    <td width="25%"><input type="button"  name="" value="增加赠品" onclick="addrowZ()" width="100%"/></td>
   </tr>
   </table>
 <div class="center"> 
 </div>
</form>
</div>

</body>
</html>