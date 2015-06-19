<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>

<%
	List<BranchType> listgt = BranchTypeManager.getLocate(); 
//System.out.println(statues+"&&"+type);
 String time = TimeUtill.getdateString();   
Map<String, List<String>> map = BranchService.getPtypeMap();
String branchtype = StringUtill.GetJson(map);
 
 
ProductSaleModel.Model[] model = ProductSaleModel.Model.values();
 
String sm = StringUtill.GetJson(model); 
// System.out.println(json); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport"
	content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/jquerycommon.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" /> 
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<script type="text/javascript">
  

//alert(listallp);   
  var jsonallp = null;
 //alert(listall); 
   var map = <%=branchtype%>
 var row = 10;    
 var rows = new Array();
 var count = 1 ;    
 var inventory = new Array();  
 var ctypes = new Array();   
 var jsoninventory = null;
 var jsoninventorysn = null;
 var jsoninventorysnmodel = null;
 var jsoninventorysnsale = null;
 
 $(function () {     
	 $("#branchtype").change(function(){
		 //var str = $("#branchid").find("option:selected").text();
		var branchtype = $("#branchtype").val();
		// alert(map[branchtype]);
		 $("#branchid").autocomplete({ 
			 source: map[branchtype]
		    });	  
	 });   
	    
	 $("#branchid").blur(function(){
		  
		 
		 initBranch();  
		  
	 });   
	 
	 addrowinti();

 });    
 
   
 function initBranch(){
	// getproduct(); 
	 getInventory();
 }
  
 function getInventory(){
	 var branchtype = $("#branchtype").val();
	 var branch = $("#branchid").val();
	 if(branch == "" || branch == null){
		 return ;
	 }   
	 $.ajax({   
	        type: "post",   
	         url: "../server.jsp", 
	         data:"method=getInventoryMapByBranch&branch="+branch+"&branchtype="+branchtype,
	         dataType: "",     
	         success: function (data) {  
	        	 var josns = jQuery.parseJSON(data); 
	        	 jsonallp = jQuery.parseJSON(josns["strp"]);
	        	 addrowproductinit();       
	        	 jsoninventory = jQuery.parseJSON(josns["strin"]); 
	        	// alert(jsoninventory);
	        	  
	        	// alert(jsonallp);  
	        	 jsoninventorysn =jQuery.parseJSON(josns["mapsn"]); 
	        	 jsoninventorysnmodel = jQuery.parseJSON(josns["strmodel"]);
	        	 jsoninventorysnsale = jQuery.parseJSON(josns["strsale"]);
	        	
	           },   
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           });
 }
  
 function getproduct(){
	 var branch = $("#branchtype").val();
	 if(branch == "" || branch == null){
		 return ; 
	 }  
	  
	 $.ajax({   
	        type: "post",   
	         url: "../server.jsp",
	         data:"method=getproduct&branchtype="+branch,
	         dataType: "",    
	         success: function (data) { 
	        	 jsonallp = jQuery.parseJSON(data);
	        	 addrowproductinit(); 
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown);  
	            }  
	           });
 }
 
   
 function addrowinti(){ 
	//  alert(rows); 
	 for(var i=0;i<row;i++){  
		 if($.inArray(i,rows) == -1){ 
			 addrow(i); 
		 }  
		    
	}
 }  
       
 function  addrowproductinit(){ 
		 for(var i=0;i<row;i++){  
			 $("#product"+i).autocomplete({  
				 source: jsonallp
			    }); 
			    
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
	 var cl = 'class="asc"';
	  
	  var str = '<tr '+cl+'>' +    
	     ' <td align=center   >'+(row*1+1*1)*1+'</td> '+
	      
	     ' <td  align=center ><input type="text" name="product'+row+'"  id="product'+row+'" placeholder="型号"  style="border-style:none" /></td> ' +    
	     '<td  align=center ><select name="statues'+row+'" id="statues'+row+'">'+
	     '<option value=""></option>'+   
	     '<option value="1">订货</option>'+ 
	      '<option value="3">样机订货</option>'+
	      '<option value="4">换货订货</option>'+
	      '<option value="5">赠品订货</option>'+ 
	      '<option value="6">店外退货 </option>'+ 
	      '<option value="7">已入库退货</option>'+
	      '<option value="9">样机退货</option>'+
	     '<select></td>'+
	      
	     '<td align=center><span style="color:red;font-size:15px;" id="papercount'+row+'" name="papercount'+row+'"></span></td>'+
	     '<td align=center><span style="color:red;font-size:15px;" id="sncount'+row+'"  ></span></td>'+
	     '<td align=center><span style="color:red;font-size:15px;" id="snstatues'+row+'"  ></span></td>'+
	     ' <td align=center ><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'"  placeholder="订单数"  style="border-style:none;width:50px;"   onBlur="addcount()" /></td> ' +
	    
	     ' <td  align=center><input type="button" value="删除" onclick="delet('+row+')"/></td> ' +
	     '</tr>'   
	     
	     ;  
	          
	$("#Wtable").append(str); 
	 
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
	    
	$("#statues"+row).blur(function (){
		initctypes(row); 
		addresults(row); 
	});
	 
	
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
		    row = row +5;    
			addrowinti(); 
		 } 
	   
	   for(var i=0;i<rows.length;i++){
		   var realnum = rows[i]; 
		   if(realnum != num){   
			    
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
  
  
 function getMessage(tname,row,statues){ 
	// alert(tname);
	// alert(jsoninventorysnsale);
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
		        			num = json.ATP+":"+json.goodType;
			        		//st = json.goodType;
		        		}else {  
		        			num += "_"+json.ATP+":"+json.goodType;
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
 
 
 function check(){
	 ctypes = new Array();  
	if(rows.length <1){ 
		alert("没有记录可以提交");  
		return false ;
	}else {
		for(var i=0;i<row;i++){ 
			if($.inArray(i,rows) == -1){
				var type =  $("#product"+i).val();
				//alert(type);
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
		<div class="weizhi_head">现在位置：增加调货单</div>
		<div>
			<form action="../../user/OrderGoodsServlet" method="post"
				onsubmit="return check()">
				<input type="hidden" name="method" value="add" />
				<!--  <input  
					type="hidden" name="token" value="<%=token%>" /> -->
				<input type="hidden" name="rows" id="rows" value="" />
				<table style="width:100% ">
					<tr class="asc">
						<td>销售系统： <select id="branchtype" name="branchtype">
								<option></option>
								<%
									if (null != listgt) {
										for (int i = 0; i < listgt.size(); i++) {
											BranchType bt = listgt.get(i);
											if (bt.getTypestatues() == 1) {
								%>
								<option value="<%=bt.getId()%>"><%=bt.getName()%></option>
								<%
									}
										}
									}
								%>
						</select>
						</td>

						<td align=center>门店</td>
						<td align=center><input type="text" name="branchid"
							id="branchid" placeholder="请先输入门店" value="" class="cba" /></td>
						<td align=center>单号：</td>
						<td align=center>日期：<%=TimeUtill.getdateString()%></td>
					</tr>
					<tr class="asc">
						<td colspan=5 align=center>
							<table style="width:100% " id="Wtable">
								<tr class="dsc">
									<td align=center width="5%">编号</td>
									<td align=center width="20%">产品型号</td>
									<td align=center width="25%">状态</td>
									
									<td align=center width="10%">店外库存数量</td>
									<td align=center width="10%">卖场库存</td>
									<td align=center width="10%">(30天销售数量)</td>
<td align=center width="20%">订货数</td>
									<td align=center width="20%">删除</td>

								</tr>
							</table>
						</td>

					</tr>

					<tr class="asc">
						<td align=center colspan=2><font
							style="color:blue;font-size:20px;">合计: <span
								style="color:red;font-size:20px;" id="addcount"></span> (台) </font>
						</td>
 
						<td align=center colspan=3>备注： <textarea id="remark"
								name="remark"></textarea></td>
					</tr>

					<tr class="asc">
						<td align=center colspan=5><input type="submit" id="submit"
							value="提交" />
						</td>

					</tr>
				</table>




			</form>
		</div>

	</div>

</body>
</html>
