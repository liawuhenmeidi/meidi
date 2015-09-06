<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*,product.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>非顶码销售</title>
<%

User user = (User)session.getAttribute("user");

if("null".equals(user) || null == user){
	response.sendRedirect("dengluN.jsp");
	return ;
}

List<Category> list = CategoryManager.getCategory();
String clist = StringUtill.GetJson(list);
HashMap<Integer,ArrayList<Product>> map = ProductManager.getProduct();

%>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
  <script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="../css/songhuo.css">
<link rel="stylesheet" href="../css/bass.css">

<script type="text/javascript">
   var ids = "";
   var CList = '<%=clist%>';
   var row = 1;
   var rowz = 1;
   var rows = new Array();
   var rowzs = new Array();

   $(document).ready(function(){ 
	addrow();
	addrowZ();
	var opt = { };
    opt.date = {preset : 'date'};
	$('#serviceDate').val('').scroller('destroy').scroller($.extend(opt['date'], 
	{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020',minDate: new Date()}));
	var opt2 = { };
    opt2.date = {preset : 'date'};
	$('#serviceDate2').val('').scroller('destroy').scroller($.extend(opt2['date'], 
	{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020',minDate: new Date()}));
	var ids = ($(this).children('option:selected').val());
	$("#product"+ids).css("display","block");
	$("#category").change(function(){ 
		var id = ($(this).children('option:selected').val());
		$("#product"+ids).css("display","none");
		$("#product"+id).css("display","block");
		ids = id ;
		}) ;
	
	$('#phone1').blur(function (){
		var mes = $('#phone1').val();
		 checkMessage("#phone1",mes);
	});
	
	$('#phone2').blur(function (){
		var mes = $('#phone2').val();
		 checkMessage("#phone2",mes);
	});
   }) ;	
   
 function deletes(str){
	 $(str).empty();
 }
 function addrow(){
    rows.push(row);
   // alert(rows);
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
               '<td width="25%" class="center">产品类型</td>'+
               '<td width="50%" class="center"><select class = "category" name="category'+row+'"  id="category'+row+'"  style="width:95% ">';
               for(var i=0;i<json.length;i++){
            	   var jo = json[i];
            	   str += '<option value='+jo.name+'>'+jo.name+'</option>';
               }
          str += '</select> '+
               ' </td>'+
               '<td width="25%" class="center"><input type="button"   color= "red" name="" value="删除" onclick="deletes(produc'+row+')"/></td>'+
               ' </tr>' +
               '</table>'+
               '<table style="background-color:'+yellow+'">'+
               ' <tr>'+
               ' <td width="25%" class="center">送货型号</td> '+
               ' <td width="35%" class=""><input type="text"  id="type" name="type'+row+'" style="width:90% " onclick="serch(type'+row+')"/><div id="aotu'+row+'"></div></td> ' +
               ' <td width="10%" class="center">数量</td> '+
               ' <td width="10%" class=""><input type="text"  id="productNum'+row+'" name="productNum'+row+'" value="1" style="width:50%"/></td> '+
               ' <td width="10%" class="center"><input type="button"   name="" value="+" onclick="add(productNum'+row+')"/></td> '+ 
               ' <td width="10%" class="center"><input type="button"   name="" value="-" onclick="subtraction(productNum'+row+') " /></td> '+ 
               ' </tr>' +
               '<table>' +
                '</div>';
        $("#productDIV").append(str); 
        row++;
        }	
 
        function serch(str){
        	return ;
        	var autonode=$("#type");
        	$(str).keyup(
        		function(event){
        			var myevent=event||window.event;  
                    var mykeyCode=myevent.keyCode;  
                    //字母，退格，删除，空格  
                    if(mykeyCode>=65&&mykeyCode<=90||mykeyCode==8||mykeyCode==46||mykeyCode==32){  
                    	var word = $(str).val();
                    	autonode.html("");
                    	if(word!=""){  
                                    //当返回的数据长度大于0才显示  
                            if(wordNodes.length>0){  
                                 autonode.show();  
                               }else{  
                                        autonode.hide();  
                               }  
                        }else{  
                            autonode.hide();  
                            highlightindex=-1;  
                        }  
                    }else{  
                        //获得返回框中的值  
                        var rvalue=$("#auto").children("div");  
                        //上下键  
                        if(mykeyCode==38||mykeyCode==40){  
                            //向上  
                            if(mykeyCode==38){  
                                if(highlightindex!=-1){  
                                    setBkColor(rvalue,highlightindex,"white");  
                                    highlightindex--;  
                                }  
                                if(highlightindex==-1){  
                                    setBkColor(rvalue,highlightindex,"white");  
                                    highlightindex=rvalue.length-1;  
                                }  
                                setBkColor(rvalue,highlightindex,"#ADD8E6");  
                                setContent(rvalue,highlightindex);  
                            }  
                            //向下  
                            if(mykeyCode==40){  
                                if(highlightindex!=rvalue.length){  
                                    setBkColor(rvalue,highlightindex,"white");  
                                    highlightindex++;  
                                }  
                                if(highlightindex==rvalue.length){  
                                    setBkColor(rvalue,highlightindex,"white");  
                                    highlightindex=0;  
                                }  
                                setBkColor(rvalue,highlightindex,"#ADD8E6");  
                                setContent(rvalue,highlightindex);  
                            }  
                        }  
                        //回车键  
                        if(mykeyCode==13){  
                            if(highlightindex!=-1){  
                                setContent(rvalue,highlightindex);  
                                highlightindex=-1;  
                                autonode.hide();  
                            }else{  
                                alert($("#content").val());  
                            }  
                        }  
                    }  
                }  
            );//键盘抬起  
      
            //当文本框失去焦点时的做法  
            inputItem.focusout(  
                function(){  
                    //隐藏提示框  
                    autonode.hide();  
                }     
            );  

        	alert($(str).val());
        }
        
        function addrowZ(){
        	rowzs.push(rowz);
        	//alert(rowzs);
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
	 if(username == "" || username == null || username == "null"){
		 alert("姓名不能为空");
		 return false;
	 }
	 if(phone1 == "" || phone1 == null || phone1 == "null"){
		 alert("电话不能为空");
		 return false;
	 }
	 if(phone2 == "" || phone2 == null || phone2 == "null"){
		 alert("备用电话不能为空");
		 return false;
	 }
	 if(locations == "" || locations == null || locations == "null"){
		 alert("详细地址不能为空");
		 return false;
	 }
	 
	 if(remark == "" || remark == null || remark == "null"){
		 alert("备注不能为空");
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
<div class="s_main_tit">非顶码销售<span class="qiangdan"></span></div>
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
  </table>
  
  <div id = "productDIV">
  
  </div>
  <hr>
  <div id = "zengpDIV">
  
  </div>

   <table style="width:100% ">
     <tr> 
    <td width="25%" class="center"></td>
    <td width="25%" class=" center"><input type="button"  name="" value="增加商品" onclick="addrow()" width="100%"/></td>
    <td width="25%" class="center"></td>
    <td width="25%"><input type="button"  name="" value="增加赠品" onclick="addrowZ()" width="100%"/></td>
   </tr>
   </table>
   
   <table style="width:100% ">
  <tr>
    <td width="25%" class="center">姓名</td>
    <td width="50%" class=""><input type="text" id="username" name="username" value="" style="width:90%"></input></td>
   <td width="25%" class="center"></td>
   </tr> 
   <tr>
    <td width="25%" class="center">电话</td>
    <td width="50%" class=""><input type="text"  id="phone1"  name="phone1" value="" style="width:90%" /></td>
   <td width="25%" class="center"></td>
   </tr>
  <tr>
    <td width="25%" class="center">电话2</td>
    <td width="25%" class=""><input type="text" style="width:90%;" id="phone2" name="phone2" value=""/></td>
   </tr>
    <tr>
    <td width="25%" class="center">所在区域</td>

    <td width="25%" class=""><select class = "quyu" name="diqu" >
				<option value="河西区">河西区</option>
				<option value="河东区">河东区</option>
				<option value="南开区">南开区"</option>
				<option value="和平区">和平区</option>
				<option value="东丽区">东丽区</option>
				<option value="红桥区">红桥区</option>
				</select>
	</td>
   </tr>
</table>
   <table style="width:100% ">

  <tr>
    <td width="25%" class="center">详细地址</td>
    <td width="75%" class=""><textarea  id="locations" name="locations" ></textarea></td>  
   </tr>
  <tr>
    <td width="25%" class="center">备注</td>
    <td width="75%" class=""><textarea  id="remark" name="remark" ></textarea></td>
   </tr>
</table>
 <div class="center"> <input type="submit" value="提  交" />
 </div>
</form>
</div>

</body>
</html>