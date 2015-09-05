<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
     
<%         
//long start= System.currentTimeMillis();  
String time = TimeUtill.getdateString();
List<String> listallp = ProductService.getlistsale(user); 

String id = request.getParameter("id"); 
String intelligent = request.getParameter("intelligent");
 
//System.out.println("qa"+(System.currentTimeMillis() - start));  
String listallpp = StringUtill.GetJson(listallp);  
OrderGoodsAll oa = null;
List<OrderGoods> list = null; 
String remark = "";  
if(!StringUtill.isNull(id)){
	oa = OrderGoodsAllManager.getOrderGoodsAllByid(user,id);
	list = oa.getList(); 
	remark = oa.getOm().getRemark();
}  
   
if("intelligent".equals(intelligent)){
	remark = "智能要货生成订单"; 
	String num = request.getParameter("salenum");
	if(StringUtill.isNull(num)){
		num= "15";  
	} 
	  
	String endtime = TimeUtill.getdateString();    
	endtime = TimeUtill.dataAdd(endtime, -1); 
	String starttime = TimeUtill.dataAdd(endtime, -Integer.valueOf(num));
	  
	String branch = user.getBranchName(); 
	String branchtype = BranchService.getMap().get(Integer.valueOf(user.getBranch())).getBranchtype().getSaletype()+"";
	 
	list = IntelligentInventory.getmessage(user, starttime, endtime, branch);
	 
} 


String json = StringUtill.GetJson(list);
//if(StringUtill.isNull(json)){
///	json = "[]";
//}
 
Map<String,InventoryBranch> map = InventoryBranchManager.getmapType(user,user.getBranch());
String jsoninventory = StringUtill.GetJson(map); 
    //System.out.println(jsoninventory);  
  
Map<String,List<SNInventory>> mapsn = InventoryChange.getMapBranchType(user,time,Integer.valueOf(user.getBranch()));
 
Map<String,List<SNInventory>> mapsnModel = InventoryModelDownLoad.getMapBranchType(user, time,Integer.valueOf(user.getBranch()));
//   
Map<String,SNInventory> mapsale = SaleDownLoad.getMap(TimeUtill.dataAdd(time, -29),time,Integer.valueOf(user.getBranch())); 
     
//System.out.println(mapsn); 
String jsoninventorysn = StringUtill.GetJson(mapsn);   
String jsoninventorysnmodel = StringUtill.GetJson(mapsnModel); 
String jsoninventorysnsale = StringUtill.GetJson(mapsale); 
 
 //System.out.println(jsoninventorysnsale); 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/jquerycommon.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" type="text/css" href="../../css/songhuo.css"/> 
<link rel="stylesheet" href="../../css/jquery-ui.css"/>  
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
 <style type="text/css"> 
body {font-size: 22px;} 
input{width: 300px; height: 30px; font-size:100%}  
input .cl {width: 100px; height: 30px; font-size:100%} 
select{width: 200px; height: 30px;font-size:100%;}  
</style>

<script type="text/javascript">
  
 
//alert(listallp);  
 var jsonallp = <%=listallpp%>; 
  var jsons = <%=json%>;
 //alert(listall);  
var jsoninventory = <%=jsoninventory%>;
var jsoninventorysn = <%=jsoninventorysn%>;
var jsoninventorysnmodel = <%=jsoninventorysnmodel%>;
var jsoninventorysnsale = <%=jsoninventorysnsale%>;

 var row = 1;   
 var rows = new Array();
 var count = 1 ; 
 var ctypes = new Array();  
  
 $(function () {   
	 var num = jsons.length;
	// row = (Math.floor(num/5)+1)*5;
if(num >row){
	 row  = num ;
}
	
	 addrowinti();
	 
	 if(num > 0){
		 init();
	 }
 }); 
  

 
 
 function init(){
	 
	 for(var i=0;i<jsons.length;i++){
		 var json = jsons[i];  
		 //alert(json.tname);
		$("#product"+i).val(json.tname); 
		$("#statues"+i).val(json.statues); 
		$("#orderproductNum"+i).val(json.realnum);
		
		getMessage(json.tname,i,json.statues); 
		
		if(null != jsoninventory  ){
			// alert(jsoninventory); 
			 var ishava = jsoninventory[json.tname];
			// alert(ishava);    
			 if(null != ishava && ""!= ishava && undefined  != ishava){
				 $("#Ipapercount"+i).val(ishava.papercount);
				 $("#papercount"+i).html(ishava.papercount);
			 }else {   
				 $("#papercount"+i).html(0);
			 }
		}else {
			$("#papercount"+i).html(0); 
		}
		 
		
		
		$("#uuid").val(json.uuid);  
		rows.push(i); 
		var only = json.tname+"_"+json.statues; 
		ctypes.push(only);   
	 }
	  
	 initctypes(jsons.length-1);
 }
 
 function addrowinti(){
	//  alert(rows); 
	 for(var i=0;i<row;i++){  
		 if($.inArray(i,rows) == -1){ 
			 addrow(i); 
		 }  
		    
	}
 } 
 
 function addcount(){ 
	 var totalcount = 0 ;
	 for(var i=0;i<rows.length;i++){
		 var countrow = $("#orderproductNum"+rows[i]).val();
		 if(!isNaN(countrow)){
			 totalcount += countrow*1;
		 }else {  
			 alert("请输入数字");  
			 $("#orderproductNum"+rows[i]).val("");
			 return ; 
		 }
	 } 
	 
	 $("#addcount").html(totalcount);
 }
  
 
 function addrow(row){ 
	 var cl = '';
	 if(row%2 == 0){
		 cl = 'class="asc"';
	 }else {  
		 cl = 'class="bsc"';
	 }   
	  var str = '<tr '+cl+'>' +    
	     ' <td align=center rowspan=6  >'+(row*1+1*1)*1+'</td> '+
	       
	     ' <td  align=center colspan = 2><input type="text" name="product'+row+'"  id="product'+row+'" placeholder="型号"  style="border-style:none" /></td> ' +    
	    // ' <td rowspan=6 align=center><input type="button" value="删除" onclick="delet('+row+')"/></td> ' +   
	     ' </tr>'+ 
	     '<tr '+cl+'>'+   
	      
	     '<td align=center colspan = 2>状态:<select name="statues'+row+'" id="statues'+row+'">'+
	     '<option value=""></option>'+   
	     '<option value="1">订货</option>'+ 
	     '<option value="11">只生成订单不发货</option>'+
	      '<option value="3">样机订货</option>'+
	      '<option value="4">换货订货</option>'+
	      '<option value="5">赠品订货</option>'+
	      '<option value="6">店外退货 </option>'+
	      '<option value="7">已入库退货</option>'+
	      '<option value="9">样机退货</option>'+
	             
	     '<select></td>'+  
	     
	      
	    
	     ' </tr>'+ 
	     '<tr '+cl+'>'+  
	     '<td align=center colspan = 2>店外库存数量:<span style="color:red;font-size:30px;" id="papercount'+row+'" name="papercount'+row+'"></span></td>'+
	     ' </tr>'+ 
	     '<tr '+cl+'>'+  
	     '<td align=center colspan = 2>卖场库存:<span style="color:red;font-size:30px;" id="sncount'+row+'"  ></span></td>'+
	     ' </tr>'+ 
	     '<tr '+cl+'>'+  
	     '<td align=center colspan = 2>(30天销售数量):<span style="color:red;font-size:30px;" id="snstatues'+row+'"  ></span></td>'+
	     ' </tr>'+   
	     '<tr '+cl+'>'+     
	     ' <td align=center><input type="button" value="删除" class="cl" style="width: 100px" onclick="delet('+row+')"/></td> '+
	     ' <td align=center ><input type="text" class="cl"  id="orderproductNum'+row+'" style="width: 100px"  name="orderproductNum'+row+'"  placeholder="订单数"     onBlur="addcount()" /></td> ' +
	      
	     ' </tr>'; 
	     ;  
	          
	$("#tproduct").append(str);
	 
	//alert($("#td"+row).css("width")-10);
	//$("#product"+row).css("width",$("#td"+row).css("width"));
	$("#product"+row).autocomplete({  
		 source: jsonallp
	    });    
	 
	$("#product"+row).blur(function (){
		initctypes(row); 
		addresultp(row);
	});
	  
	$("#product"+row).keydown(function (){
		initctypes(row);
	});
	      
	$("#statues"+row).change(function (){
		initctypes(row); 
		addresults(row); 
	});
	
	//$("#statues"+row).change(function (){
	//	initctypes();
	//}); 
 }  
 
 function getMessage(tname,row,statues){ 
		// alert(tname); 
		 //alert(jsoninventorysnsale);
		 var jsonsale = jsoninventorysnsale[tname];
		 
		        if(9 == statues || 3 == statues){
		       
		        	 var jsons = jsoninventorysnmodel[tname];
		        	 if(undefined == jsons){
		        	
		        		 $("#sncount"+row).html("无");
		        		 if(undefined != jsonsale){
		        		
				        		$("#snstatues"+row).html(jsonsale.saleNum);
				        	}else { 
				        		 
				        		$("#snstatues"+row).html("无");
				        	}
				        }else {   
				        	var num = ""; 
				        	var st = "";
				        	for(var i=0;i<jsons.length;i++){
				        		var json = jsons[i]; 
				        		if(i == 0 ){  
				        			num = json.num+":"+json.serialNumber;
					        		//st = json.serialNumber;
				        		}else { 
				        			num += "_"+json.num+":"+json.serialNumber;;
					        		//st += "_"+json.serialNumber;
				        		}
				        	}   
				        	
				        	if(undefined != jsonsale){
				        		
				        		$("#snstatues"+row).html(jsonsale.saleNum);
				        	}else {  
				        		  
				        		$("#snstatues"+row).html("无");
				        	}
				        	
				        	$("#sncount"+row).html(num);
			        		//$("#snstatues"+row).html(st);
				        }         	
		        	}else {  
		        		 var jsons = jsoninventorysn[tname]; 
		        		 var m = 1+1 ;  
		        		 
		        		 //alert(jsons );
		        		 if(undefined == jsons){  
		        			 $("#sncount"+row).html("无");
		        			 if(undefined != jsonsale){ 
		 		        		$("#snstatues"+row).html(jsonsale.saleNum);
		 		        	}else { 
		 		        		$("#snstatues"+row).html("无");
		 		        	}
		        			 
		        			 
			        }else {   
			        	var num = ""; 
			        	var st = "";  
			        	for(var i=0;i<jsons.length;i++){
			        		var json = jsons[i]; 
			        		if(i == 0 ){
			        			num = json.num+":"+json.goodType;
				        		//st = json.goodType;
			        		}else {  
			        			num += "_"+json.num+":"+json.goodType;
				        		//st += "_"+json.goodType;
			        		}
			        		 
			        		
			        	}     
			        	
			        	//alert(jsonsale );
			        	if(undefined != jsonsale){
			        		$("#snstatues"+row).html(jsonsale.saleNum);
			        	}else {
			        		$("#snstatues"+row).html("无");
			        	}
			        	 
			        	
			        	$("#sncount"+row).html(num);
		        		//$("#snstatues"+row).html(st);
			        }  
		        	 }   
	 } 
 
 
 function addresultp(row){  
	 var ctype = $("#product"+row).val();
	 var statues = $("#statues"+row).val();
	 if(ctype == ""){      
		// alert("型号不能为空");   
		 return false ;    
	 }else {      
		 if($.inArray(ctype,jsonallp) != -1){ 
			 
			 var ishava = jsoninventory[ctype];
			 if(null != ishava && ""!= ishava && undefined  != ishava){
				 $("#papercount"+row).html(jsoninventory[ctype].papercount);
			 }else {
				 $("#papercount"+row).html(0);
			 }
			 
			 if("" != statues && null != statues){
				 var only = ctype+"_"+statues;
				 if($.inArray(only,ctypes) != -1){
					 alert("您已添加此型号");   
					 $("#product"+row).val("");
					 return ;   
				 }else{     
					 rows.push(row);
					
					
					 initctypes();
				 }   
			 } 
			 
		 }else { 
			 alert("您输入的产品不存在，请重新输入");
			 $("#product"+row).val("");
			 return ;
		 }
	 }
 } 
 
 function addresults(row){  
	 var ctype = $("#product"+row).val();
	 var statues = $("#statues"+row).val();
	 //alert(statues); 
	if("" != statues && null != statues){
		
		 //alert(ctype);  
		 if(ctype == ""){   
			 alert("型号不能为空"); 
			 return  ;
		 }else {  
			 if($.inArray(ctype,jsonallp) != -1){
				 var only = ctype+"_"+statues; 
				// alert(only); 
				 //alert(ctypes);  
				 if($.inArray(only,ctypes) != -1){
					 alert("您已添加此型号");    
					// $("#product"+row).val("");
					$("#statues"+row).val(""); 
					 return ;  
				 }else{
					 
					// var ishava = jsoninventory[ctype];
					 //if(null != ishava && ""!= ishava && undefined  != ishava){
					//	 $("#papercount"+row).html(jsoninventory[ctype].papercount);
					// }else {
					//	 $("#papercount"+row).html(0);
					// }
					
					 rows.push(row);
					 initctypes();  
				 }   
			 }else { 
				 alert("您输入的产品不存在，请重新输入");
				 $("#product"+row).val("");
				 return ;
			 }
		 }
	} 
	 
 } 
  
 
  function  initctypes(num){  
	   rows.deleteEle();   
	   ctypes = new Array(); 
	   if(rows.length == row){
		   row = row+1; 
			 addrowinti(); 
		 }   
	    
	   for(var i=0;i<rows.length;i++){ 
		   var realnum = rows[i];  
		   if(num != realnum){    
			   var ctype = $("#product"+realnum).val(); 
			   var statues = $("#statues"+realnum).val(); 
			   if($.inArray(ctype,jsonallp) != -1  && statues != ""){
				   var only = ctype+"_"+statues;
				   getMessage(ctype,realnum,statues); 
				   ctypes.push(only);     
			   }
		   }
	   } 
  } 
    
  
  function delet(row){   
		 $("#product"+row).val("");
		 $("#orderproductNum"+row).val("");
		 $("#statues"+row).val("");
		 $("#snstatues"+row).html("");
		 $("#sncount"+row).html("");
		 $("#papercount"+row).html("");
		 rows.splice($.inArray(row,rows),1); 
		 initctypes();
		 addcount(); 
	 }
	  
    
 function check(){   
	 ctypes = new Array();  
	if(rows.length <1){  
		alert("没有记录可以提交");  
		return false ; 
	}else {  
		for(var i=0;i<row;i++){  
			if($.inArray(i,rows) == -1){
				var type =  $("#product"+i).val(); 
				if("" != type){   
					alert("您有产品未选择状态，请检查");
					return false;
				}
			} 
		}
	}

	for(var i=0;i<rows.length;i++){
		var ctype = $("#product"+rows[i]).val();
		var statues = $("#statues"+rows[i]).val();
		var num = $("#orderproductNum"+rows[i]).val();
		if("" != num){  
			 if(isNaN(num)){   
				 alert("请输入数字");     
		      }   
		}
         
		if("" == statues){
			alert("型号状态不能为空");
		}
		var only = ctype+"_"+statues;  
		 if(ctype == ""){ 
			 alert("型号不能为空");
			 return false ;    
		 }else { 
			 if($.inArray(ctype,jsonallp) != -1){
				 if($.inArray(only,ctypes) != -1){
					 alert("您录入的型号重复，请检查");
					 return false;  
				 } 
			 }else {  
				 alert("您录入的型号不存在，请检查");
				 return false; 
			 } 
		 }
	}     
	  
	 $("#submit").css("display","none");
	 $("#rows").val(rows.toString());
 }
 
</script> 
</head>

<body>

<div class="main">

		     
   <div class="s_main_tit"><span class="qiangdan"></span><span class="qiangdan"><a href="../welcom.jsp">返回</a></span></div>    
     <div>
            
     <form action="../OrderGoodsServlet"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="method" value="add"/> 
      <input type="hidden" name="token" value="<%=token%>"/> 
       <input type="hidden" name="id" id="id" value="<%=id%>"/> 
      <input type="hidden" name="rows" id="rows" value=""/>  
          
      <table style="width:100% "> 
       <tr>  
        <td align=center>
         单号： <%=null == oa?"":oa.getOm().getId() %>
        </td>
       <td align=center> 
       日期：<%=null == oa ?TimeUtill.getdateString():oa.getOm().getSubmittime() %>
       </td>
        </tr>    
       <tr>  
       <td colspan=2  align=center> 
        <table  id="tproduct" style="width:100% " >
        
        </table>
       </td>
        
       </tr>
  
       <tr class="asc">
       <td align=center>
       <font style="color:blue;font-size:20px;" >合计:</font>
       </td>
       <td align=center>
       <font style="color:blue;font-size:20px;" >
             <span style="color:red;font-size:20px;" id="addcount" ></span>
             (单位台)
        </font>
       </td>
       </tr> 
       <tr class="asc" >
       <td align=center> 
                          备注：    
       </td> 
       <td align=center><textarea  id="remark" name="remark"  ><%=remark %> </textarea></td>
       </tr>

       <tr class="asc">
       <td align=center colspan=2>
       <input type="submit" id="submit" value="确认提 交" />
       </td> 
       
       </tr>
      </table>
      
   
   
 
 </form>
     </div>

</div>


</body>
</html>
