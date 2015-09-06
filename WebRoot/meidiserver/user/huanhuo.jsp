<%@ page language="java" import="java.util.*,gift.*,orderproduct.*,order.*,branch.*,locate.*,category.*,group.*,user.*,utill.*,product.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
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
 

HashMap<String,ArrayList<String>> listt = ProductManager.getProductName();

String plist = StringUtill.GetJson(listt);

Branch branch = BranchService.getMap().get(Integer.valueOf(user.getBranch())); 
  

String id = request.getParameter("id");
Order order = null ; 
String listopp = "";
String strorder= null;
String isdisabel = "";

if(!StringUtill.isNull(id)){
	order = OrderManager.getOrderID(user,Integer.valueOf(id));
	strorder = StringUtill.GetJson(order);
	List<OrderProduct> listop = OrderProductManager.getOrderStatues(user, Integer.valueOf(id));

	listopp = StringUtill.GetJson(listop);

	isdisabel = " readonly";  
}
 
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
   var rows = new Array();
   var andate = new Array();
   var timefirst = true ;
   
   var listop =  null;
   var order = null;
 
   var order = <%=strorder%>;
  //alert(order);
   if(order != null && order != ""){
	   var listopp = '<%=listopp%>' ;
	   listop =  $.parseJSON(listopp);
	 

   }

   var branch = new Array();
  
   
   var disable = '<%=isdisabel %>';

   $(document).ready(function(){
	
	init();    
   });
   
   
   function init(){
		   
	   if(listop != null && listop != "" && listop != "null"){
			  for(var i=0;i<listop.length;i++){
				  var listo = listop[i];
				  if(0 == listo.statues){
						  addrow(listo); 
					}

			   } 
	   }

	   if(order != null && order != "" && order != "null"){
		   
		   $("#serviceDate").val(order.saleTime);
		   $("#serviceDate2").val(order.odate);
		  
		   $("#username").val(order.username);
		   $("#phone1").val(order.phone1);
		   $("#phone2").val(order.phone2);
		   $("#locations").val(order.locateDetail);
		   $("#remark").val(order.remark);
		  
		   $("#quyu").val(order.locate); 
		   
	   }
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

  

    function deletes(str,str2){
    	$(str).empty();
	    rows.splice($.inArray(str2,rows),1);
     } 
 
    
    function addrow(listo){
    	var sele = "";
    	var display = ""; 
    	var yellow = "";
    	if(row%2 == 0){
    		yellow = "#fff"; 
    	}
    	rows.push(row);
    	var type = listo.typeName == null || listo.typeName == undefined ? "":listo.typeName;
    	var count = listo.count == null || listo.count == undefined ? 1:listo.count;
    	var statues = listo.salestatues == null || listo.salestatues == undefined ? -1:listo.salestatues;
    
    	  
    	var str = "";
    	    str += '<div id="produc'+row+'" name="produc">'+
    	           '<input type="hidden" name="product" value="'+listo.id+'"/>'+
    	    	   '<table style="width:100%;background-color:'+yellow+'">'+
                   ' <tr>' +
                   '<td width="25%" class="center">送货类别<span style="color:red">*</span></td>'+
                   '<td width="50%" class="center">'; 
                        
                     for(var i=0;i<json.length;i++){
                	   var ckeck = "";
                	   var jo = json[i];  
                	   if(jo.id == listo.categoryId){
                		   str +=  '<input type="text" class = "category" name="ordercategory'+row+'"  id="ordercategory'+row+'" value="'+jo.name+'" '+disable+'  style="width:95% "/>';
                	   }
  
                   }
                   str += ''+
                   ' </td>'+
                   '<td width="10%" class="center" id="andate'+row+'"></td> '+
                   '<td width="5%" class="center" >天</td>' +
                   '<td width="15%" class="center"></td>'+ 
                    ' </tr>' +
                   '</table>'+
                   '<table style="background-color:'+yellow+'">'+
                   ' <tr>'+
                   ' <td width="25%" class="center">送货型号<span style="color:red">*</span></td> '+
                   ' <td width="35%" class=""><input type="text"  id="ordertype'+row+'" name="ordertype'+row+'" value="'+type+'" style="width:90% " '+disable+'/><div id="aotu'+row+'"></div></td> ' +
                   ' <td width="10%" class="center">送货数量</td> '+ 
                   ' <td width="10%" class=""><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+listo.id+'" value="'+count+'" style="width:50%" '+disable+'/></td> '+
                   ' <td width="20%" class="center"><input type="button"  style="background-color:orange" name="" value="-" onclick="subtraction(orderproductNum'+row+') " /></td> '+ 
                   ' </tr>'+ 
                   '<tr></tr>'+
                   '<table>'+
                   '  </table>'+
              	 '  <table style="width:100%;background-color:'+yellow+'">'+
              	 ' <tr>'+ 
              	  '  <td width="25%" class="center">送货状态</td>'+
              	  '  <td width="50%" class="">'+
              	 '<input type="text" class = "category" name="ordercategory'+row+'"  id="ordercategory'+row+'" '+disable+' value="换货"/>'+
              	   ' </td>'+ 
              	   ' <td width="25%" class="center"><input type="button"   style="color:white;background-color:#0080FF" name="" value="删除" onclick="deletes(produc'+row+','+row+')"/></td>'+
              	   '</tr>'+
              	'</table>'+
                   ' <hr>' + 
                    '</div>'; 
                    
            $("#productDIV").append(str);
          //  $("productsta"+row).attr();
            $("select[id='productsta'"+row+"] option[id='"+statues+"']").attr("selected","selected");
            var cid = $("#ordercategory"+row).val();
            initAndate("#andate"+row,cid);
            row++;
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

  
 function checkedd(){
     
	 var andate = $("#serviceDate2").val();
	 var username = $("#username").val();
	 var phone1 = $("#phone1").val();
	 var phone2 = $("#phone2").val();
	 var locate = $("#quyu").val();
	 var locations = $("#locations").val();
	 var remark = $("#remark").val();
	 var radio = $('input:radio[name="Statues"]:checked').val();
	 
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
	 
	 if(rows.length < 1){
		 alert("没有产品需要换货");
		 return false ;
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



<div class="s_main_tit"><span class="qiangdan"><a href="serch_list.jsp">订单查询</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span><span class="qiangdan"><a href="welcom.jsp">返回</a></span></div>
<form action="OrderServlet"  method ="post"  id="form"   onsubmit="return checkedd()"  > 
<!--  头 单种类  -->  

<input type="hidden" name="method" value="savehuanhuo"/> 
<input type="hidden" name="orderid" value="<%=id %>"/>
<input type="hidden" name="token" value="<%=s%>"/>  
 
<div class="s_main_tit">销售报单<span class="qiangdan"></span></div>
<div class="s_main_tit">门店:<span class="qian"><%=user.getBranchName()%></span></div>  
<!--  订单详情  -->  
<div class="s_dan_box"> </div>

<table style="width:100% "> 

  </table>
  
  <div id = "productDIV" >
  
   </div>
  
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