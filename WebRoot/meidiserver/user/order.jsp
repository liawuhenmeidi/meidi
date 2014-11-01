<%@ page language="java" import="java.util.*,gift.*,orderproduct.*,order.*,branch.*,locate.*,category.*,group.*,user.*,utill.*,product.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
User user = (User)session.getAttribute("user");

TokenGen.getInstance().saveToken(request);
String s = (String)session.getAttribute("token");

if(!UserManager.checkPermissions(user, Group.sale)){ 
	response.sendRedirect("welcom.jsp");
} 
      
List<Category> list = CategoryManager.getCategory(user,Category.sale); 
String clist = StringUtill.GetJson(list);
 
HashMap<Integer,ArrayList<Product>> map = ProductManager.getProduct();
List<Locate> listl = LocateManager.getLocate();
 
HashMap<String,ArrayList<String>> listt = ProductService.gettypeName();

String plist = StringUtill.GetJson(listt);

Branch branch = BranchService.getMap().get(Integer.valueOf(user.getBranch())); 
  

String id = request.getParameter("id");
// 为空表示正常修改     不为空表示导购报送相同顾客单据
String statues = request.getParameter("statues");

Order order = null ; 
String listopp = "";
String listgg = "";
String strorder= null;
String isdisabel = "";

if(!StringUtill.isNull(id)){
	order = OrderManager.getOrderID(user,Integer.valueOf(id));
	strorder = StringUtill.GetJson(order);
	
	if(StringUtill.isNull(statues)){
		List<OrderProduct> listop = OrderProductManager.getOrderStatues(user, Integer.valueOf(id));

		listopp = StringUtill.GetJson(listop);

		List<Gift> listg = GiftManager.getGift(user, Integer.valueOf(id));
		listgg = StringUtill.GetJson(listg);
		isdisabel = " disabled=\"disabled\" "; 
		
	}else {
		id = "";
	}
	 
}
 

 
String  branchmessage = branch.getMessage();
if(StringUtill.isNull( branchmessage)){ 
	 branchmessage = "";
}   
String[] branlist =  branchmessage.split("_");

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
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
 <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
 
<script type="text/javascript" src="../js/calendar.js"></script> 
<link rel="stylesheet" href="../css/songhuo.css">

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  
<script type="text/javascript">

   var messageflag = false;  // 是否需要填写顾客信息
   var flag = false ;
  
  var jsons = <%=plist%> ; 

   var cid = "";  
   var ids = "";  
   var json = <%=clist%>;
   var row = 1;  

   var rowz = 1;
   var rows = new Array();
   var rowzs = new Array();
   var andate = new Array();
   var timefirst = true ;
   
   var listop =  null;
   var listg =  null;
   var order = null;
   // statues 表示是否是相同顾客报装
   var statues = '<%=statues%>' ;
   var order = <%=strorder%>;
  //alert(order);
   if(order != null && order != "" && (statues == null || statues == "")){
	  
	   var listopp = '<%=listopp%>' ;
	   var listgg = '<%=listgg%> ';
	   listop =  $.parseJSON(listopp);
	   listg =  $.parseJSON(listgg);

   }

   var branch = new Array();
   var branchmessage = "<%=branchmessage%>";
   
   var disable = '<%=isdisabel %>';
  
   branch = branchmessage.split("_");
       
   rows.push(0);

   $(document).ready(function(){
	   
	initTime(); 
	if($.inArray("pos", branch) != -1){
		initpos("pos");
	}  
	if($.inArray("sailId", branch) != -1){
		initpos("sailId"); 
	} 
	if($.inArray("checked", branch) != -1){
		initpos("checked");
	} 
	initphone();
	initproduct();
	
	init();   
	initmessage("#productsta0");
   });
   
   
   function initmessage(str){
	    
	   $(str).change(function(){
		   disbale();
	   }); 
   }
   
   function init(){
		   
	   if(listop != null && listop != "" && listop != "null"){
		   var flag = true ;
           var flag2 = false;  //  是否是定吗标记
		   $("#dingma_no").css("display","block");
		   
		   for(var i=0;i<listop.length;i++){
			   var listo = listop[i];
			   if(1 == listo.statues){			   
				   $("#dingma").css("display","block");
				   $("#dingmatype").val(listo.typeName);
				  // alert($("#ordercategory0").get(0));
				 // alert(listo.categoryId);
				 //  alert($("#ordercategory0 #"+listo.categoryId));
				// alert($("#dingmaordercategory #"+listo.categoryId));
				   $("#dingmaordercategory #"+listo.categoryId).attr("selected","selected");
				   $("#dingmaproductNum").val(listo.count);
				  $("#dingmachek").attr("checked","checked");
				 //  types = listo.categoryId; 	
				 flag2 = true ;
			   }
		   }
              if(flag){
            	  $("#dingma_nochek").attr("checked","checked");
              }
		   
		   
			  for(var i=0;i<listop.length;i++){
				  var listo = listop[i];
				  if(0 == listo.statues){
					   if(flag){   
						$("#ordertype0").val(listo.typeName);
						$("#orderproductNum0").val(listo.count);
						//alert(listo.categoryId);
						//alert($("#ordercategory0 #33").attr("selected"));  
						$("select[id='ordercategory0'] option[id='"+listo.categoryId+"']").attr("selected","selected");
						$("select[id='productsta0'] option[id='"+listo.salestatues+"']").attr("selected","selected");
						//$("#ordercategory0 #"+listo.categoryId).attr("selected","selected");  
					    flag = false ;
					   }else {
						   addrow(listo); 
					   }

			   } 
	   }
	   
	   }
	   //listgg
	   if(listgg != null && listgg != "" && listgg != "null"){
		 // alert(listg.length);
		   for(var i=0;i<listg.length;i++){
			   var gift = listg[i];
			    addrowZ(gift);
			   
		   }
	   }   
	   
	   
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
    
   function initproductSerch(str,str2,str3){ 
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

		initAndate(str3,cid);
			}) ;
       } 
   
   // 大 返回true 
   function initAndate(str,cid){
	   
		//alert(cid);
		for(var i=0;i<json.length;i++){
    	   var jo = json[i];
    	   if(jo.id == cid){
    		 var time = jo.time ;
    	//	 alert(time); 
    		 $(str).html("");
  			 $(str).append(time); 
    	   }
		}	
   }
   
   function compare(andate,str2){

	   var Astr1 = new Array();
	  // var Astr2 = new Array();
	   Astr1 = andate.split("-");
	  // Astr2 = str2.split("-");
	   var date = new Date();
	   var year = date.getFullYear();
	   var month = date.getMonth()+1;
	 
	   var day = date.getDate();
	 
	   if((Astr1[0]-year)*365+(Astr1[1]-month)*30+(Astr1[2]-day)*1 > str2){
		   return true ;
	   }
	  return false ;
   }
   
   function compareBeifen(){
	   var Astr1 = new Array();
	   var Astr2 = new Array();
	   Astr1 = str1.split("-");
	   Astr2 = str2.split("-");
	   if(Astr1[0] >Astr2[0]){
		   return true ;
	   }else if(Astr1[0] == Astr2[0] && Astr1[1] >Astr2[1]){
		   return true ;
	   }else if(Astr1[0] == Astr2[0] && Astr1[1] == Astr2[1] && Astr1[2] > Astr2[2]){
		   return true ;
	   }
	  return false ;  
   }
   
   function initPhone(){
	   $('#phone1').blur(function (){
			var mes = $('#phone1').val();
			 checkMessage("#phone1",mes);
		});
   }
    
   function initTime(){
	   
   }
 
 function initpos(str){
	   $("#"+str).focus(function(){
		    $("#"+str).css("background-color","#FFFFCC");
		  });
		  $("#"+str).blur(function(){
		    $("#"+str).css("background-color","#D6D6FF");
		    
		    var pos = $("#"+str).val(); 
		    if(pos != null && pos != ""){

		    $.ajax({ 
		        type:"post", 
		         url:"server.jsp",
		         //data:"method=list_pic&page="+pageCount,
		         data:"method="+str+"&name="+pos+"&branch=<%=user.getBranch()%>",
		         dataType: "",  
		         success: function (data) {
		        	 if("true" == data){
		        		 var message = "";
		        		 if(str == "pos"){
		        			 message = "pos(厂送票)已存在有可能录重,是否继续?";
		        		 }else if(str == "sailId"){
		        			 message = "OMS订单号已存在有可能录重.是否继续?";
		        		 }else if(str == "checked"){
		        			 message = "检验码已存在有可能录重.是否继续?";
		        		 }
		        		 var question = confirm(message);	
		        			if (question == "0"){   
		        				$("#"+str).val("");;
		        				return false ;
		        			}else {  
		        				 $("#"+str+"remark").val("1");
		        			}  
		        	 }else {
		        		 $("#"+str+"remark").val("0"); 
		        	 }  
		           },  
		          error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	  return false ;
		            } 
		           }); 
		    
		    
		    }
		  }); 
		  
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
 
 function initproduct(){
	
	 initproductSerch("#ordercategory0","#ordertype0","#andate0"); 
	 initproductSerch("#dingmaordercategory","#dingmatype","#dingmaandate");
	 
	  var cid = $("#ordercategory0").val();
      initAndate("#andate0",cid);
      
		  $('input:radio').change(function() {
			  $("#dingma_c").css("display","block");
			  $("#dingma_no").css("display","block");
			
			     if(this.checked){
			    	 var a = $(this).val();
			    	 if(1==a){ 
			    		 $("#dingma").css("display","block");	    		   
			    	 }else { 	    		 
			    		 $("#dingma").css("display","none"); 
			    	 } 	  
			 }
	      }) ;  
      } 
 
 
    function deletegifts(str,str2){
    	$(str).empty();
	    rowzs.splice($.inArray(str2,rowzs),1);	   
     }
     
    function deletes(str,str2){
    	$(str).empty();
	    rows.splice($.inArray(str2,rows),1);
     } 
 
    
    function addrow(listo){
    	 var radio = $('input:radio[name="Statues"]:checked').val();
    	 if(radio == "" || radio == null || radio == "null"){
    		 alert("请选择是否顶码销售");  
    		 return false;
    	 } 
    	
    	var sele = "";
    	var display = ""; 
        rows.push(row);
    	var yellow = "";
    	if(row%2 == 0){
    		yellow = "#fff"; 
    	}
    	 
    	var type = listo.typeName == null || listo.typeName == undefined ? "":listo.typeName;
    	var count = listo.count == null || listo.count == undefined ? 1:listo.count;
    	var statues = listo.salestatues == null || listo.salestatues == undefined ? -1:listo.salestatues;
    
    	  
    	var str = "";
    	    str += '<div id="produc'+row+'" name="produc">'+
    	           '<input type="hidden" name="product" value="'+row+'"/>'+
    	    	   '<table style="width:100%;background-color:'+yellow+'">'+
                   ' <tr>' +
                   '<td width="25%" class="center">送货类别<span style="color:red">*</span></td>'+
                   '<td width="50%" class="center"><select class = "category" name="ordercategory'+row+'"  id="ordercategory'+row+'"  style="width:95% " '+disable+'>'; 
                   for(var i=0;i<json.length;i++){
                	   var ckeck = "";
                	   var jo = json[i];  
                	   if(jo.id == listo.categoryId){
                		   str += '<option value='+jo.id+'  selected="selected" >'+jo.name+'</option>';
                	   }else {
                		   str += '<option value='+jo.id+'>'+jo.name+'</option>'; 
                	   }
  
                   }
                   str += '</select> '+
                   ' </td>'+
                   '<td width="10%" class="center" id="andate'+row+'"></td> '+
                   '<td width="5%" class="center" >天</td>' +
                   '<td width="15%" class="center"></td>'+ 
                    ' </tr>' +
                   '</table>'+
                   '<table style="background-color:'+yellow+'">'+
                   ' <tr>'+
                   ' <td width="25%" class="center">送货型号<span style="color:red">*</span></td> '+
                   ' <td width="35%" class=""><input type="text"  id="ordertype'+row+'" name="ordertype'+row+'" value="'+type+'" style="width:90% " '+disable+' onclick="serch(type'+row+')"/><div id="aotu'+row+'"></div></td> ' +
                   ' <td width="10%" class="center">送货数量</td> '+ 
                   ' <td width="10%" class=""><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'" value="'+count+'" style="width:50%" '+disable+'/></td> '+
                   ' <td width="10%" class="center"><input type="button"  style="background-color:orange" name="" value="+" onclick="add(orderproductNum'+row+')" '+disable+'/></td> '+ 
                   ' <td width="10%" class="center"><input type="button"  style="background-color:orange" name="" value="-" onclick="subtraction(orderproductNum'+row+') "  '+disable+'/></td> '+ 
                   ' </tr>'+ 
                   '<tr></tr>'+
                   '<table>'+
                   '  </table>'+
              	 '  <table style="width:100%;background-color:'+yellow+'">'+
              	 ' <tr>'+ 
              	  '  <td width="25%" class="center">送货状态</td>'+
              	  '  <td width="50%" class="">'+
                '  <select  name="productsta'+row+'" id="productsta'+row+'" style="width:90%; " '+disable+'>'+
                   
               '  <option value="" id="-1"></option>    '+ 
                ' <option value="1" id="1">需配送安装</option>'+
                ' <option value="0" id="0">已自提</option>'+
                ' <option value="2" id="2">只安装(师傅门店提货)</option> '+  
             	' <option value="3" id="3">只安装(顾客已提)</option> '+ 
              	  '  </select>'+

              	   ' </td>'+ 
              	   ' <td width="25%" class="center"><input type="button"   style="color:white;background-color:#0080FF" name="" value="删除" onclick="deletes(produc'+row+','+row+')" '+disable+'/></td>'+
              	   '</tr>'+
              	'</table>'+
                   ' <hr>' + 
                    '</div>'; 
                    
            $("#productDIV").append(str);
          //  $("productsta"+row).attr();
           $("select[id='productsta'"+row+"] option[id='"+statues+"']").attr("selected","selected");
            initproductSerch("#ordercategory"+row,"#ordertype"+row);
            var cid = $("#ordercategory"+row).val();
            initAndate("#andate"+row,cid);
            initmessage("#productsta"+row); 
            row++; 
            } 
    
    
    function addrowZ(listo){
    	var sele = "";
    	var name = listo.name == null || listo.name == undefined ? "":listo.name;
    	var count = listo.count == null || listo.count == undefined ? 1:listo.count;
    	var type = listo.statues == null || listo.statues == undefined ? "":listo.statues;
    	if(type == 0){
    		sele = 'selected="selected"'; 
    	}
    	rowzs.push(rowz);
    	var yellow = "";
    	if(rowz%2 == 0){
    		yellow = "#fff";
    	}
    	
    	var str = '<div id=gift'+rowz+'><input type="hidden" name="gift" value="'+rowz+'"/>';

    	    str += '<table style="background-color:'+yellow+'">'+
    	    '<tr>'+
    	   ' <td width="25%" class="center">赠品</td>'+
    	    '<td width="35%" class=""> <input type="text"  id="giftT'+rowz+'" value="'+name+'" name="giftT'+rowz+'" style="width:90% " value="" '+disable+'/> </td>'+
    	   ' <td width="10%" class="center">数量</td>'+
    	   ' <td width="10%" class=""><input type="text"  id="productNn'+rowz+'" value = "'+count+'" name="giftCount'+rowz+'" value="1" style="width:50%" '+disable+'/></td>'+
    	   ' <td width="10%" class="center"><input type="button" style="background-color:orange"  name="" value="+" onclick="add(productNn'+rowz+')"  '+disable+'/></td>'+
    	   ' <td width="10%" class="center"><input type="button" style="background-color:orange"  name="" value="-" onclick="subtraction(productNn'+rowz+')"  '+disable+'/></td>'+
    	  ' </tr>'+
    	  '  </table>'+
    	 '  <table style="width:100%;background-color:'+yellow+'">'+
    	 ' <tr>'+
    	  '  <td width="25%" class="center">赠品状态</td>'+
    	  '  <td width="50%" class="">'+
    	  '  <select  name="giftsta'+rowz+'"   style="width:90%; " '+disable+'>'+
    	  '  	<option value="1" id="1">已自提</option>'+
    	   ' 	<option value="0" id="0" '+sele+'>需配送</option>'+
    	   
    	  '  </select>'+  

    	   ' </td>'+ 
    	   ' <td width="25%" class="center"><input type="button"   style="color:white;background-color:#0080FF"  name="" value="删除" onclick="deletegifts(gift'+rowz+')" '+disable+'/></td>'+
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
	 var filter = /^1[3|4|5|8][0-9]\d{9}$/;
	 //var fiter = /^(((13[0-9]{1})|159|153)+\d{8})$/ ;
	 if(filter.test(message)){
		 return true;
	 }else{
		 alert("请填写正确的手机号码");
		 $(str).focus();
		 return false;
	 }
 }
 
 function disbale(){
	 messageflag = false ;
	 var messflag = false ;  //是否有已自提
	 var statues = -1 ;
	 var product = new Array();
	
	 for(var i=0;i<rows.length;i++){
		 var pro = $("#ordertype"+rows[i]).val();
		  
		 if(i == 0 ){ 
			 product.push(pro);
		 }else {
			
			 if($.inArray(pro, product) == 0 ){
				 alert("不能录入相同的送货型号");
				 return false ;
			 }else { 
				 product.push(pro);
			 }
		 }
		 var str = $("#productsta"+rows[i]).val();
		 //alert(str); 
		 //alert(statues);
		 if(statues == -1){
			 statues = str ;
			 //alert("statues"+statues);
		 }else {
			 //alert(statues);
			//alert(str);
			// alert(statues == str); 
			 if(!(statues == str)){
				 alert("送货状态不一致，不能提交");
				 return false; 
			 }
		 }
		 
		 
		 if(str != 0){
			 messageflag = true ;
		 }
		 if(str == 0){ 
			 messflag = true ;
		 }
	 } 
	    
	 //if(messageflag && messflag){  
	//	 alert("已自提和需派送需安装不能一起提交");
	//	 return false; 
	 //}
	 if(!messageflag){ 
		 //alert(3);
		 $("#disable").css("display","none");
	 }  
	 if(messageflag){
		// alert(4);
		 $("#disable").css("display","block");
	 }
 }
 
  
 function checkedd(){
     
	 
	 var saledate = $("#serviceDate").val();
	 var andate = $("#serviceDate2").val();
	 var pos = $("#pos").val();
	 var sailId = $("#sailId").val();
	 var check = $("#checked").val();
	 var username = $("#username").val();
	 var phone1 = $("#phone1").val();
	 var phone2 = $("#phone2").val();
	 var locate = $("#quyu").val();
	 var locations = $("#locations").val();
	 var remark = $("#remark").val();
	 var radio = $('input:radio[name="Statues"]:checked').val();
	

	 
     if(saledate == "" || saledate == null || saledate == "null"){
		 alert("开票日期不能为空");
		 return false;
	 }
	 
     if($.inArray("pos", branch) != -1){
		 if(pos == "" || pos == null || pos == "null"){
			 alert("pos(厂送)单号不能为空");
			 return false;
		 }
     } 
     
     if($.inArray("sailId", branch) != -1){
		 if(sailId == "" || sailId == null || sailId == "null"){
			 alert("OMS订单号不能为空");
			 return false;
		 }
     }
       
     if($.inArray("checked", branch) != -1){
		 if(check == "" || check == null || check == "null"){
			 alert("校验码不能为空");
			 return false;
		 } 
     }
     
	 if(radio == "" || radio == null || radio == "null"){
		 alert("请选择是否顶码销售");  
		 return false;
	 } 
	 
	 if("1"== radio){
		 var dingmatype = $("#dingmatype").val();
		 if(dingmatype == "" || dingmatype == null || dingmatype == "null"){
			 alert("票面类别不能为空");
			 return false;
		 }else {
			// alert(dingmatype);
			 var str = $("#dingmaordercategory").val();
			 var flag = true ;
        	  var array = jsons[str];
        	  //alert(array);
        	  if(array != "" && array != null &&  array != "null" && array != undefined && array != "undefined"){
	         	   for(var k=0;k<array.length;k++){
	         		   
	         		   if(dingmatype == array[k]){
	         			   flag = false  ;
	         		   }
	         	   }
	         	   }
			 // dingmatype
        	  if(flag){
 				 alert("系统不存在此型号"+dingmatype);
 				 return false ;
 			 }
		 }
	 } 
	    
	 for(var i=0;i<rows.length;i++){
		 var str = $("#ordertype"+rows[i]).val();
		 var juese = $("#ordercategory"+rows[i]).val();
		 var statues = $("#productsta"+rows[i]).val();
		 
		 
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
			 
			 
			 
			// for(var j=0;j<json.length;j++){
	         	//   var jo = json[j];
	         	//   var array = jsons[jo.id];
	         	//  if(array != "" && array != null &&  array != "null" && array != undefined && array != "undefined"){
		         //	   for(var k=0;k<array.length;k++){
		         		   
		         //		   if(str == array[k]){
		         //			   flag = false  ;
		         //		   }
		         //	   }
		         //	   }
				//}
			 if(flag){
				 alert("系统不存在此型号"+str);
				 return false ;
			 }
		 }
		  
		 if(statues == "" || statues == null || statues == "null" ){
			 alert("产品状态不能为空"); 
			 return false ;
		 }
	 }
 
	 for(var i=0;i<rowzs.length;i++){
		
		 var str = $("#giftT"+rowzs[i]).val();
		 if(str == "" || str == null || str == "null"){
			 alert("赠品不能为空");
			 return false;
		 }
	 }
	 
	  
	 var str = disbale(); 
	 
	  
	  if(str == false){
		  return false;
	  }
	 
	 
	 if(messageflag){
		 
		 if(andate == "" || andate == null || andate == "null"){
			 alert("预约安装时间不能为空");
			 return false;
		 }else {
			 var timeold = 0;
			 for(var i=0;i<rows.length;i++){
				
				 if(i == 0){
					timeold = $("#andate"+rows[i])[0].innerText*1;
				 }
				
				 var str = $("#andate"+rows[i])[0].innerText*1;
				// alert(str);
				 if(str < timeold){
					timeold = str ;
				 } 
			 }
			 if(compare(andate,timeold)){
				 alert("预约安装日期超过期限");
				 return false ; 
			 }
		 }
		 
		 
		 if(username == "" || username == null || username == "null"){
			 alert("顾客姓名不能为空");
			 return false;
		 }
		 
		 if(phone1 == "" || phone1 == null || phone1 == "null"){
			 alert("电话不能为空");
			 return false;
		 }
		 else {
		    var filter = /^1[3|4|5|8][0-9]\d{8}$/;  
		    var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
			 if(!filter.test(phone1) && !isPhone.test(phone1)){
				 alert("请填写正确的手机号码或电话");     
				 return false;  
			 }
		 }  
	     
		 if(locate == "" || locate == null || locate == "null"){
			 alert("所属区域不能为空");
			 return false;
		 }
		 
		 if(locations == "" || locations == null || locations == "null"){
			 alert("详细地址不能为空");
			 return false;
		 }
	 }
	 $('input[name="permission"]:checked').each(function(){ 
		    alert($(this).val());  
	 });
	 $("#submit").css("display","none"); 
	 return true ; 
 }
 
</script>


</head>
<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>



<div class="s_main_tit"><span class="qiangdan"><a href="serch_list.jsp">订单查询</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span></div>
<form action="OrderServlet"  method ="post"  id="form"   onsubmit="return checkedd()"  > 
<!--  头 单种类  -->  

<input type="hidden" name="gift" value="0"/>
<input type="hidden" name="product" value="0"/>  
<input type="hidden" id="phoneremark" name="phoneRemark" value="0"/>
<input type="hidden" id="posremark" name="posremark" value="0"/> 
<input type="hidden" id="checkedremark" name="chekedremark" value="0"/>
<input type="hidden" id="sailIdremark" name="sailidremark" value="0"/>
<input type="hidden" name="orderid" value="<%=id %>"/>
<input type="hidden" name="token" value="<%=s%>"/> 
 
<div class="s_main_tit">销售报单<span class="qiangdan"></span></div>
<div class="s_main_tit">门店:<span class="qian"><%=BranchService.getMap().get(Integer.valueOf(user.getBranch())).getLocateName() %></span></div>  
<!--  订单详情  -->  
<div class="s_dan_box"> </div>

<table style="width:100% "> 
 
  <tr>    
    <td width="25%" class="center">开票日期<span style="color:red">*</span></td>
    <td width="50%" class=""><input class="date" type="text" name="saledate" placeholder="必填"  id = "serviceDate" onclick="new Calendar().show(this);" readonly="readonly" style="width:90% "></input>   </td>
    <td width="25%" class="center"></td>
  </tr>
   <% 
    for(int i = 0; i<branlist.length;i++){
    	if("pos".equals(branlist[i])){
    		%>
    <tr> 
    <td width="25%" class="center">pos(厂送)单号 <span style="color:red">*</span></td>
    <td width="50%" class=""> <input type="text"  id="pos" name="POS" style="width:90% "/></td>
    <td width="25%" class="center"> </td>
   </tr> 
    		<%
    	}else if("sailId".equals(branlist[i])){
    		%>
     <tr>
    <td width="25%" class="center">OMS订单号 <span style="color:red">*</span></td> 
    <td width="50%" class=""><input type="text"  id="sailId" name="sailId" style="width:90% " /></td>
    <td width="25%" class="center"></td>
  </tr>		
    		
    		<%
    	}else if("checked".equals(branlist[i])){
    		%>
    <tr>
    <td width="25%" class="center">验证码(联保单)<span style="color:red">*</span></td>
    <td width="50%" class=""> <input type="text"  id="checked" name="check" style="width:90% " /></td>
    <td width="25%"></td>
  </tr> 		
    		
    		<%
    	}
    }
   
   %>
 
 
  <tr >
    <td width="25%" class="center">顶码销售<span style="color:red">*</span></td>
    <td width="50%" class="">  是
		<input type="radio"  name="Statues" value="1"  id="dingmachek" <%=isdisabel %>/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		否
		<input type="radio" name="Statues" value="0"  id="dingma_nochek" <%=isdisabel %>/></td>
    <td width="25%"> </td>
  </tr>
  </table>
  
  <div id = "productDIV" <%=isdisabel %>>
  
   <div id="dingma"  style= "display:none;">
   <hr>
  <table style="width:100%;background-color:" > 
  <tr>
  <td width="25%" class="center">票面类别<span style="color:red">*</span></td>
  <td width="50%" class="center">
  <select class="category" name="dingmaordercategory" id="dingmaordercategory" style="width:95% " <%=isdisabel %>>
  
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
  <td width="10%" class="center" id=""></td>
  <td width="5%" class="center" ></td>  
  <td width="10%" class="center" id="">&nbsp;</td> 
  </tr>

  <tr> 
  <td width="25%" class="center">票面型号<span style="color:red">*</span></td> 
   <td width="75%" class="">
   <input type="text" id="dingmatype" name="dingmatype"  onclick="serch(type1)"  <%=isdisabel %>>
   </td>
   </tr>
    <tr> 
   <td width="25%" class="center">票面数量</td> 
   <td width="25%" class=""><input type="text" id="dingmaproductNum" name="dingmaproductNum" value="1" style="width:50%" <%=isdisabel %>></td>  
   <td width="25%" class="center"><input type="button" name="" style="background-color:orange" value="+" onclick="add(dingmaproductNum)"></td>  
   <td width="25%" class="center"><input type="button" name="" style="background-color:orange" value="-" onclick="subtraction(dingmaproductNum)"></td>  
   </tr>
   </table>
   </div>
   
 <div id="dingma_no"  style= "display:none;">
  <table style="width:100%;background-color:"> 
  <tbody>
  <tr>
  <td width="25%" class="center">送货类别<span style="color:red">*</span></td>
  <td width="50%" class="center">
  <select class="category" name="ordercategory0" id="ordercategory0" style="width:95% " <%=isdisabel %>>
  
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
  <td width="10%" class="center" id="andate0"></td>
  <td width="5%" class="center" >天</td>  
  <td width="10%" class="center" id="">&nbsp;</td> 
  </tr>
    <tr>
   <td width="25%" class="center">送货型号<span style="color:red">*</span></td> 
   <td width="75%" class="">
   <input type="text" name="ordertype0" id="ordertype0" class="cba" <%=isdisabel %>/> 
    </td> 
    </tr>
    <tr>
   <td width="25%" class="center">送货数量</td>  
   <td width="25%" class=""><input type="text" id="orderproductNum0" name="orderproductNum0" value="1" style="width:50%" <%=isdisabel %>></td>  
   <td width="25%" class="center" ><input type="button" style="background-color:orange" name="" value="+" onclick="add(orderproductNum0)" <%=isdisabel %>></td>  
   <td width="25%" class="center" ><input type="button" style="background-color:orange" name="" value="-" onclick="subtraction(orderproductNum0) " <%=isdisabel %>></td>  
   </tr>     
   </table>  
  <table style="width:100%;background-color:"> 
              	 <tr>
               	  <td width="25%" class="center">送货状态</td>
              	  <td width="50%" class=""> 
              	  <select  name="productsta0"   id="productsta0"  style="width:90%; " <%=isdisabel %>>
	              	 <option value="" id=""></option>       
	              	 <option value="1" id="1">需配送安装</option>    
	              	 <option value="0" id="0">已自提</option>     
	              	 <option value="2" id="2">只安装(师傅门店提货)</option>   
	              	 <option value="3" id="3">只安装(顾客已提)</option>  
        
              	 </select>

              	 </td> 
              	 <td width="25%" class="center"></td>
              	   </tr>
              	</table>
    <hr>
   </div>
   </div>
 
  <div id = "zengpDIV" >
    
  </div>
   <table style="width:100%;background-color:orange">
     <tr >   
    <td width="25%" class=" center"><input type="button"  name="" value="增加赠品" onclick="addrowZ(0)" width="100%" <%=isdisabel %>/></td>
    <td width="25%" class="center"></td>
    <td width="25%"><input type="button"  name="" value="再增加商品" onclick="addrow(0)" width="100%" <%=isdisabel %>/></td>
    <td width="25%" class="center"></td> 
   </tr>
   </table> 
   <div id="disable">
   <table style="width:100% ">
   <tr>  
    <td width="25%" class="center">预约日期<span style="color:red">*</span></td>
    <td width="45%" class=""><input class="date2" type="text" name="andate" id ="serviceDate2" onclick="new Calendar().show(this);"  placeholder="必填"  readonly="readonly" style="width:90% "></input>   </td>
    <td width="25%" class="center" id="andates"></td> 
    <td width="5%"></td> 
   </tr> 
  <tr>
    <td width="25%" class="center">顾客姓名<span style="color:red">*</span></td>
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
    <td width="25%" class="center">送货区域<span style="color:red">*</span></td>

    <td width="25%" class="">
    <select class = "quyu" name="diqu" id="quyu">
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
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

  <tr>
    <td width="25%" class="center">详细地址<span style="color:red">*</span></td>
    <td width="75%" class=""><textarea  id="locations" name="locations" ></textarea></td>  
   </tr>
  <tr>
    <td width="25%" class="center">备注</td>
    <td width="75%" class=""><textarea  id="remark" name="remark" ></textarea></td>
   </tr>
</table>
</div>
<div id="submit">
 <table style="width:100%;background-color:orange">
  <tr>  
    <td width="100%" class="center"><input type="submit"  value="提  交" /></td>
   </tr>
   </table>
   </div>
 <div class="center"> 
 </div>
</form>
</div>

</body>
</html>