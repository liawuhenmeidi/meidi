<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="httpClient.*"%>
<%@ include file="../../common.jsp"%>

<%   
     
String id = request.getParameter("id"); 
String inventory  = request.getParameter("inventory"); 
String message = "调货单审核";  
 String time = TimeUtill.getdateString();
//System.out.println(statues+"&&"+type);
OrderGoodsAll oa = null;   
List<OrderGoods> list = null;   
String branchname = ""; 
String branchtype = "";   
String remark = ""; 
String json = "[]"; 
int branchid = -1;
if(!StringUtill.isNull(id)){ 
	oa = OrderGoodsAllManager.getOrderGoodsAllexamByid(user,id); 
	branchname = oa.getOm().getBranchname(); 
	branchtype = BranchService.getMap().get(oa.getOm().getBranchid()).getPid()+"";
	remark = oa.getOm().getRemark();    
	list = oa.getList();     
	 json = StringUtill.GetJson(list); 
	 branchid = oa.getOm().getBranchid(); 
}        
       
if(!StringUtill.isNull(inventory)){  
	branchname = request.getParameter("branch");
	branchtype =request.getParameter("branchtype");  
	String tids = request.getParameter("tids"); 
	System.out.println("branchname"+branchname+"***branchtype"+branchtype+"**tids"+tids);
	String[] tidss = tids.split(","); 
	list = new ArrayList<OrderGoods> ();
	for(int i=0;i<tidss.length;i++){
		String tid = tidss[i];
		OrderGoods og = new OrderGoods();
		og.setTid(Integer.valueOf(tid));
		list.add(og); 
	}  
	 json = StringUtill.GetJson(list); 
}  
 
Map<String,InventoryBranch> map = InventoryBranchManager.getmapType(user,branchid+"");
String jsoninventory = StringUtill.GetJson(map);  
     
Map<String,List<SNInventory>> mapsn = InventoryChange.getMapBranchType(user,time,branchid);
 
Map<String,List<SNInventory>> mapsnModel = InventoryModelDownLoad.getMapBranchType(user, time,branchid);
//   
Map<String,SNInventory> mapsale = SaleDownLoad.getMap(TimeUtill.dataAdd(time, -29),time,branchid); 
      
//System.out.println(mapsn); 
String jsoninventorysn = StringUtill.GetJson(mapsn);   
String jsoninventorysnmodel = StringUtill.GetJson(mapsnModel); 
String jsoninventorysnsale = StringUtill.GetJson(mapsale); 
 
//String SNjson = StringUtill.GetJson(map); 
//System.out.println(json); 


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport"
	content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<style type="text/css">
td {
	align: center
}
</style>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/jquerycommon.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script> 
<script type="text/javascript" src="../../js/cookie/jquery.cookie.js"></script>
<script type="text/javascript">
var branchtype = "<%=branchtype%>";
//alert(listallp);    
  var jsonallp = null;     
  var jsons = <%=json%>; 
 //alert(listall);   
var jsoninventory = <%=jsoninventory%>;
var jsoninventorysn = <%=jsoninventorysn%>;
var jsoninventorysnmodel = <%=jsoninventorysnmodel%>;
var jsoninventorysnsale = <%=jsoninventorysnsale%>;

//alert( $.cookie("branch")); 
//alert( $.cookie("branchtype"));
//  alert(jsoninventorysn);
 var row = 10;   
 var rows = new Array();
 var count = 1 ; 
   
 var ctypes = new Array();  
 
 $(function () {
	 
	 var num = jsons.length;
	  
	 if(num > 10){
		 row = (Math.floor(num/5)+1)*5;
	 }   
	  
	 addrowinti();
	  
	 getproduct(); 
	 
	 if(num > 0){
		 init();
	 } 
 }); 
   
 function getMessage(id,tid,tname,row,statues){   
	 var jsonsale = jsoninventorysnsale[tname];
	        if(9 == statues || 3 == statues){ 
	       
	        	 var jsons = jsoninventorysnmodel[tname];
	        	 if(undefined == jsons){
	        		
	        		 $("#sncount"+row).html("无");
	        		 if(undefined != jsonsale){
	        		
			        		$("#snstatues"+row).html(jsonsale.saleNum);
			        	}else { 
			        		// alert(2);
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
			        		 //alert(2);
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
		        			num = json.num+":"+json.goodType;
			        		//st = json.goodType;
		        		}else {  
		        			num += "_"+json.num+":"+json.goodType;
			        		//st += "_"+json.goodType;
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
	        	 }  
 } 
  
 function getproduct(){
	 $.ajax({   
	        type: "post",  
	         url: "../server.jsp",
	         data:"method=getproduct&branchtype="+branchtype,
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
   
 function init(){
	 for(var i=0;i<jsons.length;i++){
		 var json = jsons[i];   
		 //alert(json.tname); s
		$("#product"+i).val(json.tname);  
		$("#statues"+i).val(json.realstatues); 
		// alert(json.realstatues);
		// $("#table"+i+" td").remove(); 
		//var str = '<td colspan=5 align=center style="color:red" >苏宁库存信息(退货显示库存信息)</td>';
  		// $("#table"+i).append(str);    
  		   // alert(json.statues); 
  		   // alert(1);
		//if(7 == json.statues || 8 == json.statues || 9 == json.statues){
			//$("#table"+i+" td").remove();  
			//var str = '<td colspan=5 align=center style="color:red" >数据更新中</td>';
	  		//$("#table"+i).append(str); 
			getMessage(json.id,json.tid,json.tname,i,json.statues); 
		//}    
		      
		$("#orderproductNum"+i).val(json.realnum);
		//alert(jsoninventory); 
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
		if(json.realstatues != "" && json.realstatues!= null){
			rows.push(i); 
			var only = json.tname+"_"+json.realstatues; 
			ctypes.push(only); 
		}
		  
	 }  
	 addcount();
 }
 
 function addrowinti(){
	//  alert(rows); 
	 for(var i=0;i<row;i++){  
		 if($.inArray(i,rows) == -1){ 
			 addrow(i);
			 $("#product"+i).autocomplete({  
				 source: jsonallp
			    }); 
			  
		 }  
		    
	}
 }  
      
 function  addrowproductinit(){
		//  alert(rows);   
		//alert(jsonallp.length); 
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
	     ' <td align=center  >'+(row*1+1*1)*1+'</td> '+
	     ' <td  align=center ><input type="text" name="product'+row+'"  id="product'+row+'" placeholder="型号"  style="border-style:none" /></td> ' +    
	     ' <td align=center ><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'"  placeholder="订单数"  style="border-style:none;width:50px;"   onBlur="addcount()" /></td> ' +
	     '<td align=center><input type="hidden" name="papercount'+row+'" id="Ipapercount'+row+'"><span style="color:red;font-size:15px;" id="papercount'+row+'"  ></span></td>'+
	     '<td align=center><span style="color:red;font-size:15px;" id="sncount'+row+'"  ></span></td>'+
	     '<td align=center><span style="color:red;font-size:15px;" id="snstatues'+row+'"  ></span></td>'+
	     '<td  align=center ><select name="statues'+row+'" id="statues'+row+'">'+
	         
	     '<option value=""></option>'+    
	     '<option value="1">常规机订货</option>'+
	     '<option value="2">特价机订货</option>'+
	     '<option value="11">常规只生成订单不发货</option>'+
	     '<option value="12">特价只生成订单不发货</option>'+
	      '<option value="3">样机订货</option>'+
	      '<option value="4">换货订货</option>'+
	      '<option value="5">赠品订货</option>'+    
	      '<option value="6">店外退货 </option>'+  
	      '<option value="10">店外样机退货 </option>'+ 
	      '<option value="7">已入库常规退货</option>'+
	      '<option value="8">已入库特价退货</option>'+
	      '<option value="9">已入库样机退货</option>'+
	     '<select></td>'+ 
	     ' <td  align=center><input type="button" value="删除" onclick="delet('+row+')"/></td> ' +
	     '</tr>'
	     ;  
	                 
	$("#Ntable").append(str);
	 
	//alert($("#td"+row).css("width")-10);
	//$("#product"+row).css("width",$("#td"+row).css("width"));
	
	//$("#product"+row).autocomplete({  
		// source: jsonallp
	  //  });     
	   
	$("#product"+row).blur(function (){
		initctypes(row); 
		addresultp(row);
	});
	   
	$("#product"+row).keydown(function (){
		initctypes(row);
		//alert(row); 
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
			 if("" != statues && null != statues){
				 var only = ctype+"_"+statues;
				// alert(only); 
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
					 rows.push(row);
					 initctypes();  
				 }   
			 }else { 


				 ("您输入的产品不存在，请重新输入");
				 $("#product"+row).val("");
				 return ;
			 }
		 }
	} 
	 
 }
     
  function  initctypes(num){  
	   rows.deleteEle(); 
	   ctypes = new Array();
	   //alert(num);
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
				   ctypes.push(only);     
			   }
		   }
	   }  
	     
	  // alert("length"+ctypes);
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
   
 function check(sta){
	 
	ctypes = new Array();  
	if(rows.length <1){ 
		//alert("没有记录可以提交");  
		//return false ;
	}else {
		for(var i=0;i<row;i++){ 
			if($.inArray(i,rows) == -1){
				//alert(i);
				//alert(type);
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
				 return false ;
		      }   
		}
         
		if("" == statues){
			alert("型号状态不能为空");
			 return false ;
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
	   
	 $("#submit1").css("display","none"); 
	 $("#submit2").css("display","none"); 
	 $("#rows").val(rows.toString()); 
	// alert(sta);  
	 if("" != sta && null != sta){
		 $("#opstatues").val(sta); 
	 }
	   // alert( $("#mypost"));
	 $("#mypost").submit();  
 }
 
</script>
</head>

<body>

	<div class="main">
		<div class="weizhi_head">
			现在位置：<%=message%></div>
		<div>
			<form action="../../user/OrderGoodsServlet" method="post" id="mypost">
				<input type="hidden" name="method" value="add" /> <input
					type="hidden" name="token" value="<%=token%>" /> <input
					type="hidden" name="id" id="id" value="<%=id%>" /> <input
					type="hidden" name="rows" id="rows" value="" /> <input
					type="hidden" name="opstatues" id="opstatues" value="" />
				<table style="width:100% ">
					<tr class="asc">
						<td align=center>门店</td>
						<td align=center>
							<%
								if (StringUtill.isNull(branchname)) {
							%> <input type="text" name="branchid" id="branchid"
							placeholder="请先输入门店" value="<%=branchname%>" class="cba" /> <%
 	} else {
 %> <input type="hidden" name="branchid" id="branchid"
							value="<%=branchname%>" /> <%=branchname%> <%
 	}
 %>
						</td>
						 <td align=center>
         单号： <%=null == oa?"":oa.getOm().getId() %>
        </td>  
       <td align=center> 
       日期：<%=null == oa ?TimeUtill.getdateString():oa.getOm().getSubmittime() %>
       </td>
					</tr>
					<tr class="asc">
						<td colspan=4 align=center>
							<table style="width:100% " id="Ntable">
								<tr class="dsc">
									<td align=center width="5%">编号</td>
									<td align=center width="20%">产品型号</td>
									<td align=center width="20%">订货数</td>

									<td align=center width="10%">店外库存数量</td>
<td align=center width="10%">卖场库存</td> 
<td align=center width="10%">(30天销售数量)</td>
									<td align=center width="25%">状态</td>
									<td align=center width="20%">删除</td>
								</tr>
								<!-- 
								
								 -->
							</table></td>
					</tr>

					<tr class="asc">
						<td align=center colspan=2><font
							style="color:blue;font-size:20px;">合计: <span
								style="color:red;font-size:20px;" id="addcount"></span> (台) </font></td>

						<td align=center colspan=2>备注： <textarea id="remark"
								name="remark"><%=remark%></textarea>
						</td>
					</tr>

					<tr class="asc"> 
						<td align=center colspan=2><input type="button" id="submit1"
							value="保存刷新" onclick="check('0')" />
						</td>
						<td align=center colspan=2><input type="button" id="submit2"
							value="审核通过" onclick="check('1')" />
						</td>
					</tr>
				</table>




			</form>
		</div>

	</div>


</body>
</html>
