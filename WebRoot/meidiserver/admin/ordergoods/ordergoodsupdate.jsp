<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="httpClient.*"%>
<%@ include file="../../common.jsp"%>
 
<%  
	//Map<String,List<String>> map = new HashMap<String,List<String>>(); 
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp);  
String id = request.getParameter("id");   
String statues = request.getParameter("statues");
String type = request.getParameter("type");
String message = "调货单审核"; 

//System.out.println(statues+"&&"+type);
OrderGoodsAll oa = null;  
List<OrderGoods> list = null; 
String branchname = "";
String remark = "";       
if(!StringUtill.isNull(id)){ 
	if((OrderMessage.billing+"").equals(type)){   
		oa = OrderGoodsAllManager.getOrderGoodsAllBySendid(user,id,statues); 
	}else {  
		oa = OrderGoodsAllManager.getOrderGoodsAllByid(user,id,Integer.valueOf(statues),type);
	}
	
	branchname = oa.getOm().getBranchname();
	remark = oa.getOm().getRemark(); 
	list = oa.getList(); 
} 
//if(null != list){
//	for(int i=0;i<list.size();i++){
//		OrderGoods og = list.get(i); 
//		if(og.getStatues() == 6 || og.getStatues() == 7){
//			List<String> s = InventorySN.getinventoryByName(og.getTid()+"",branchname);
	//		map.put(og.getId()+"", s);  
//		}
//	}
//}  
String branchid = BranchService.getNameMap().get(branchname).getId()+"";
Map<String,InventoryBranch> map = InventoryBranchManager.getmapType(user,branchid);
String jsoninventory = StringUtill.GetJson(map); 
String json = StringUtill.GetJson(list); 
//String SNjson = StringUtill.GetJson(map); 
// System.out.println(json);
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



<script type="text/javascript">
 
  
//alert(listallp);  
  var jsonallp = null; 
  var jsons = <%=json%>;
 //alert(listall);
var jsoninventory = <%=jsoninventory%>;
 var row = 10;   
 var rows = new Array();
 var count = 1 ; 
   
 var ctypes = new Array();  
 var jsonall = <%=listall%>;
 
 $(function () {   
	 
	 //$("#branchid").change(function(){
	//	 var str = $("#branchid").find("option:selected").text();
		// $("#branchid").val(str);
	// });
	 
	 $("#branchid").autocomplete({ 
		 source: jsonall
	    }); 
	 
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
    
 function initBranch(){
	 getproduct(); 
	 getInventory();
 } 
 

 function getMessage(id,tid,row,statues){
	 var branch = $("#branchid").val();
	 $.ajax({    
	        type: "post",      
	         url: "../server.jsp", 
	         data:"method=InventorySN&branch="+branch+"&tname="+tid+"&statues="+statues+"&id="+id,
	         dataType: "",   
	         success: function (data) { 
	        	 data = jQuery.parseJSON(data);
	        	//  alert(data.length);    
	        	 $("#table"+row+" td").remove();
	        	 var str = '';
	        	 if(9 == statues){
	        		 str = '<tr class="asc">'+
						'<td >商品名称</td>'+
						'<td >库位名称</td>'+
						'<td >数量</td>'+
						'<td >序列号</td>'+
	              '</tr>'; 
	        	 }else {
	        		 str = '<tr class="asc">'+
						'<td >商品名称</td>'+
						'<td >库位名称</td>'+

						'<td >ATP可卖数</td>'+
						'<td >实货可用数</td>'+
						'<td >销售已销未提</td>'+
	              '</tr>'; 
	        	 } 
	        	 
	        	 if(data.length >0 ){  
	        		 for(var i=0;i<data.length;i++){
	        			  
	        			 str += '<tr class="asc" id="td'+i+'">'+data[i]+'</tr>';
	        			 //alert(str); 
	        			
	        		 }   
	        		 $("#table"+row).append(str);
	        		 $("td"+i+" td").css("align","center");
	        	 } else { 
	        		 var str = '<td colspan=5 align=center style="color:red" >无库存</td>';
	        		 $("#table"+row).append(str);
	        	 }
	        	  
	        	 
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 var str = '<td colspan=5 align=center style="color:red">获取数据错误</td>';
        		 $("#table"+row).append(str); 
	            } 
	           });
 }
  
 function getproduct(){
	 var branch = $("#branchid").val();
	 if(branch == "" || branch == null){
		 return ;
	 } 
	 $.ajax({   
	        type: "post",  
	         url: "../server.jsp",
	         data:"method=getproduct&branch="+branch,
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
		 //alert(json.tname);
		$("#product"+i).val(json.tname); 
		$("#statues"+i).val(json.statues); 
		 
		// $("#table"+i+" td").remove(); 
		//var str = '<td colspan=5 align=center style="color:red" >苏宁库存信息(退货显示库存信息)</td>';
  		// $("#table"+i).append(str);    
  		   // alert(json.statues); 
  		   // alert(1);
		if(7 == json.statues || 8 == json.statues || 9 == json.statues){
			$("#table"+i+" td").remove();  
			var str = '<td colspan=5 align=center style="color:red" >数据更新中</td>';
	  		$("#table"+i).append(str); 
			getMessage(json.id,json.tid,i,json.statues); 
		} 
		
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
		rows.push(i); 
		var only = json.tname+"_"+json.statues; 
		ctypes.push(only);   
	 } 
	 addcount();
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
	 if(rows.length == 10){
		 row = 15;
		 addrowinti();
	 }
	 $("#addcount").html(totalcount);
 }
  
 
 function addrow(row){ 
	 var cl = 'class="asc"';
	 
	  var str = '<tr '+cl+'>' +    
	     ' <td align=center  rowspan=2 >'+(row*1+1*1)*1+'</td> '+
	      
	     ' <td  align=center ><input type="text" name="product'+row+'"  id="product'+row+'" placeholder="型号"  style="border-style:none" /></td> ' +    
	     '<td  align=center ><select name="statues'+row+'" id="statues'+row+'">'+
	     '<option value=""></option>'+   
	     '<option value="1">常规机订货</option>'+
	      '<option value="2">特价机订货</option>'+ 
	      '<option value="3">样机订货</option>'+
	      '<option value="4">换货订货</option>'+
	      '<option value="5">赠品订货</option>'+
	      '<option value="6">店外退货 </option>'+
	      '<option value="7">已入库常规退货</option>'+
	      '<option value="8">已入库特价退货</option>'+
	      '<option value="9">已入库样机退货</option>'+
	     '<select></td>'+      
	     '<td align=center><input type="hidden" name="papercount'+row+'" id="Ipapercount'+row+'"><span style="color:red;font-size:15px;" id="papercount'+row+'"  ></span></td>'+
	     ' <td align=center ><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'"  placeholder="订单数"  style="border-style:none;width:50px;"   onBlur="addcount()" /></td> ' +
	     ' <td  align=center><input type="button" value="删除" onclick="delet('+row+')"/></td> ' +
	     '</tr>'+     
	     '<tr '+cl+' ><td colspan=5 align=center ><table id="table'+row+'" width="100%"></table></td></tr>'
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
		initctypes();
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
				 alert("您输入的产品不存在，请重新输入");
				 $("#product"+row).val("");
				 return ;
			 }
		 }
	} 
	 
 }
  
  function  initctypes(row){  
	   rows.deleteEle(); 
	   ctypes = new Array();   
	   for(var i=0;i<rows.length;i++){
		   if(i != row){  
			   var ctype = $("#product"+rows[i]).val();
			   var statues = $("#statues"+rows[i]).val();
			   if($.inArray(ctype,jsonallp) != -1  && statues != ""){
				   var only = ctype+"_"+statues;
				   ctypes.push(only);     
			   }
		   }
	   } 
  } 
    
 function delet(row){   
	 $("#product"+row).val("");
	 $("#orderproductNum"+row).val("");
	 $("#statues"+row).val(""); 
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
			<form action="../../user/OrderGoodsServlet" method="post"
				id="mypost" >
				<input type="hidden" name="method" value="add" /> <input 
					type="hidden" name="token" value="<%=token%>" /> <input
					type="hidden" name="id" id="id" value="<%=id%>" /> <input
					type="hidden" name="rows" id="rows" value="" /> <input
					type="hidden" name="opstatues" id="opstatues" value="" /> 
				<input type="hidden" name="type" id="type" value="<%=type%>" />
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
						<td align=center>单号：</td>
						<td align=center>日期：<%=TimeUtill.getdateString()%></td>
					</tr>
					<tr class="asc">
						<td colspan=4 align=center>
							<table style="width:100% " id="Ntable">
								<tr class="dsc">
									<td align=center width="5%">编号</td>
									<td align=center width="20%">产品型号</td>
									<td align=center width="25%">状态</td>

									<td align=center width="10%">未入库数量</td>
									<td align=center width="20%">订货数</td>
									<td align=center width="20%">删除</td>
								</tr>
								<!-- 
								
								 -->
							</table>
						</td>
					</tr>

					<tr class="asc">
						<td align=center colspan=2><font
							style="color:blue;font-size:20px;">合计: <span
								style="color:red;font-size:20px;" id="addcount"></span> (台) </font>
						</td>

						<td align=center colspan=2>备注： <textarea id="remark"
								name="remark"><%=remark%></textarea></td>
					</tr>
   
					<tr class="asc"> 
						<td align=center colspan=2><input type="button" id="submit1"
							value="保存刷新" onclick="check('0')"/></td>
						<td align=center colspan=2><input type="button" id="submit2"
							value="审核通过" onclick="check('1')"/></td>
					</tr>
				</table>




			</form>
		</div>

	</div>


</body>
</html>
