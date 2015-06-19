<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
        
<%           
String time = TimeUtill.getdateString();
List<String> listallp = ProductService.getlistsale(user); 
   
String id = request.getParameter("id"); 
String type = request.getParameter("type"); 
String uuid = request.getParameter("uuid");
System.out.println("uuid"+uuid); 
int num = 0 ; 
int typestatues = 0 ;
String typeName = ""; 
if("model".equals(type)){
	num = 1 ; 
	typeName = "样机盘点";
	typestatues = MakeInventory.model;
}else if("out".equals(type)){
	typeName = "未入库盘点"; 
	typestatues = MakeInventory.out;
}else if("in".equals(type)){
	typeName = "入库盘点";
	typestatues = MakeInventory.in;
}   
 
String listallpp = StringUtill.GetJson(listallp);  
List<MakeInventory> list = null;
if(!StringUtill.isNull(uuid)){
	list = MakeInventoryManager.get(user, typestatues,uuid);
}
  



String json = StringUtill.GetJson(list);
System.out.println("list"+json );  
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

<script type="text/javascript">
   

//alert(listallp);  
 var jsonallp = <%=listallpp%>; 
  var jsons = <%=json%>; 

 // alert(jsons); 
 //alert(listall);  

 var row = 2;   
 var rows = new Array();
 var count = 1 ; 
 var ctypes = new Array();  
      
 $(function () {   
	 var num = jsons.length;
	// alert(num);  
	// row = (Math.floor(num/5)+1)*5;
if(num >row){  
	  
	 row  = num+1 ;
} 
	 
addrowNuminti(); 
	  
	 if(num > 0){
		 init();
	 }
 }); 
  

 
 
 function init(){
	 
	 for(var i=0;i<jsons.length;i++){
		 var json = jsons[i]; 
		 //alert(json.tname);
		$("#product"+i).val(json.product.type);  
		$("#num"+i).val(json.num); 
		$("#id"+i).val(json.id);   
		rows.push(i); 
		var only = json.product.type;  
		ctypes.push(only);   
	 }   
	 //initctypes(jsons.length-1);
 }
  
 function addrowNuminti(){  
		//  alert(rows);  
		 for(var i=0;i<row;i++){  
			 if($.inArray(i,rows) == -1){ 
				 addrow(i);   
			 }   
			     
		}
	 } 
 
 
 function addrowNum(){  
	//  alert(rows); 
	 for(var i=0;i<row;i++){  
		 if($.inArray(i,rows) == -1 && i>=row-2){ 
			 addrow(i);   
		 }   
		    
	}
 } 
 
 function addcount(){ 
	 var totalcount = 0 ;
	 for(var i=0;i<rows.length;i++){
		 var countrow = $("#num"+rows[i]).val();
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
	 var num = '';
	 if('<%=num%>'!= 0){
		 num = 'value=<%=num%>';
	 }
	 if(row%2 == 0){
		 cl = 'class="asc"';
	 }else {    
		 cl = 'class="bsc"';
	 }    
	  var str = '<tr '+cl+'>' +       
	     ' <td align=center>'+(row*1+1*1)*1+'<input type="hidden" id="id'+row+'" name="id'+row+'"></td> '+   
	     ' <td  align=center><input type="text" name="product'+row+'"  id="product'+row+'" placeholder="型号"  style="border-style:none" /></td> ' +    
	     ' <td align=center ><input type="text" class="cl"  id="num'+row+'" style="width: 40px" '+num+' name="num'+row+'"  placeholder="数量" onBlur="addcount()" /></td> ' +
	     ' <td align=center><input type="button" value="删除" class="cl" style="width: 50px" onclick="delet('+row+')"/></td> '+
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
	   
	$("#product"+row).focus(function (){
		addrows(); 
	});
	 
	$("#product"+row).keydown(function (){
		initctypes(row);
	}); 
 }  
 
 function addrows(){         
	  if(rows.length == row-1){  
		   row = row+2;      
			 addrowNum();   
		 }   
 }
   
 function addresultp(row){  
	 var ctype = $("#product"+row).val();
	 if(ctype == ""){      
		// alert("型号不能为空");   
		 return false ;    
	 }else {      
		 if($.inArray(ctype,jsonallp) != -1){ 
			 
				 var only = ctype;
				 if($.inArray(only,ctypes) != -1){
					 alert("您已添加此型号");   
					 $("#product"+row).val("");
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
 
  function  initctypes(num){  
	   rows.deleteEle();   
	   ctypes = new Array();  
	     
	  
	       
	   for(var i=0;i<rows.length;i++){ 
		   var realnum = rows[i];  
		   if(num != realnum){    
			   var ctype = $("#product"+realnum).val(); 
			 
			   if($.inArray(ctype,jsonallp) != -1){
				   var only = ctype;
				   ctypes.push(only);     
			   }
		   }
	   } 
  }  
    
  
  function delet(row){   
		 $("#product"+row).val("");
		 $("#num"+row).html(""); 
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
		var num = $("#num"+rows[i]).val();
		if("" != num){  
			 if(isNaN(num)){   
				 alert("请输入数字");     
		      }   
		}
         
		var only = ctype;  
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
  
		     
   <div class="s_main_tit"><span class="qiangdan"><a href="makeinventoryall.jsp?type=<%=type%>">返回</a></span></div>    
     <div>   
               
     <form action="../OrderGoodsServlet"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="method" value="add_inventory"/>        
      <input type="hidden" name="token" value="<%=token%>"/>   
       <input type="hidden" name="id" id="id" value="<%=id%>"/>  
      <input type="hidden" name="rows" id="rows" value=""/>
      <input type="hidden" name="type" value="<%=type%>"/>  
         <input type="hidden" name="uuid" value="<%=uuid%>"/>  
            
      <table style="width:100% "> 
       <tr> 
       <td align=center>
       日期：<%=TimeUtill.getdateString() %>
       </td> 
       <td>盘点类型：<%=typeName %></td>
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
