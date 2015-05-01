<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
     
<%         
//long start= System.currentTimeMillis();     
List<String> listallp = ProductService.getlistsale(user); 
//System.out.println("qa"+(System.currentTimeMillis() - start));  
String listallpp = StringUtill.GetJson(listallp);  
OrderGoodsAll oa = null;
List<OrderGoods> list = null;
String id = request.getParameter("id"); 
String remark = "";
if(!StringUtill.isNull(id)){
	oa = OrderGoodsAllManager.getOrderGoodsAllByid(user,id);
	list = oa.getList(); 
	remark = oa.getOm().getRemark();
}  
 
Map<String,InventoryBranch> map = InventoryBranchManager.getmapType(user,user.getBranch());
String jsoninventory = StringUtill.GetJson(map);
String json = StringUtill.GetJson(list); 
// System.out.println(json); 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0,user-scalable=yes"/> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/jquerycommon.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" type="text/css" href="../../css/songhuo.css"/> 
<link rel="stylesheet" href="../../css/jquery-ui.css"/>  
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
 
<script type="text/javascript">
  
 
//alert(listallp);  
 var jsonallp = <%=listallpp%>; 
  var jsons = <%=json%>;
 //alert(listall); 
   var jsoninventory = <%=jsoninventory%>;
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
		$("#uuid").val(json.uuid);  
		rows.push(i); 
		var only = json.tname+"_"+json.statues; 
		ctypes.push(only);   
	 }
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
	     ' <td align=center rowspan=2  >'+(row*1+1*1)*1+'</td> '+
	      
	     ' <td colspan=2 align=center ><input type="text" name="product'+row+'"  id="product'+row+'" placeholder="型号"  style="border-style:none" /></td> ' +    
	     '<td rowspan=2 align=center ><select name="statues'+row+'" id="statues'+row+'">'+
	     '<option value=""></option>'+  
	     '<option value="1">常规特价订货</option>'+
	      '<option value="3">样机订货</option>'+
	      '<option value="4">换货订货</option>'+
	      '<option value="5">赠品订货</option>'+
	      '<option value="6">店外退货 </option>'+
	      '<option value="7">已入库常规特价退货</option>'+
	      '<option value="9">已入库样机退货</option>'+
	     
	     '<select></td>'+   
	     ' <td rowspan=2 align=center><input type="button" value="删除" onclick="delet('+row+')"/></td> ' +   
	     ' </tr>'+ 
	     '<tr '+cl+'>'+  
	     '<td align=center><span style="color:red;font-size:15px;" id="papercount'+row+'" name="papercount'+row+'"></span></td>'+
	     ' <td align=center ><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'"  placeholder="订单数"  style="border-style:none;width:50px;"   onBlur="addcount()" /></td> ' +
	     
	     '</tr>'
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
		initctypes();
	});
	    
	$("#statues"+row).blur(function (){
		initctypes(row); 
		addresults(row); 
	});
	
	//$("#statues"+row).change(function (){
	//	initctypes();
	//}); 
	 
	
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
		   row = row+5;
			 addrowinti();
		 } 
	   
	   for(var i=0;i<rows.length;i++){
		   if(i != num){  
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
   <div class="s_main_tit">现在位置：导购订货单<span class="qiangdan"><a href="ordergoodsBig.jsp?id=<%=id%>">放大</a></span><span class="qiangdan"><a href="../welcom.jsp">返回</a></span></div>    
     <div>           
     <form action="../OrderGoodsServlet"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="method" value="add"/> 
      <input type="hidden" name="token" value="<%=token%>"/> 
       <input type="hidden" name="id" id="id" value="<%=id%>"/> 
      <input type="hidden" name="rows" id="rows" value=""/>  
          
      <table style="width:100% "> 
       <tr> 
        <td align=center>
         单号： 
        </td>
       <td align=center>
       日期：<%=TimeUtill.getdateString() %>
       </td>
        </tr>    
       <tr>  
       <td colspan=2  align=center> 
        <table  id="tproduct" style="width:100% " >
         <tr class="asc">    
           <td align=center width="5%" rowspan=2  >编号</td> 
           <td align=center width="45%" colspan=2> 产品型号</td> 
           <td align=center width="30%" rowspan=2  >状态</td>  
           <td align=center width="20%" rowspan=2> 删除</td> 
         </tr>  
          <tr class="asc">   
         <td align=center width="20%">未入库数量</td>  
         <td align=center width="30%"> 订单数</td> 
        
              
          </tr>
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
