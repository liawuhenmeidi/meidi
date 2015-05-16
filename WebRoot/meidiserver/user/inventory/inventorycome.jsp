<%@ page language="java" import="java.util.*,utill.*,company.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
 
List<String> listallp = ProductService.getlistall(user);
//System.out.println("qa"+(System.currentTimeMillis() - start));  
String listallpp = StringUtill.GetJson(listallp);

String branchid = user.getBranch(); 

%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css"/>  
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript">
 
var row = 1; 
var rows = new Array();
var winPar = null;
var typeid = ""; 
var jsonallp = <%=listallpp%>; 
var branchid = "<%=branchid%>";

$(function () {  
	 add(); 
	// alert(jsonallp);
	 $("#product").autocomplete({  
		 source: jsonallp
	    });   
}); 
 
function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
}
 
function search(ctype,branchid){ 
		 if('<%=user.getBranch()%>' != branchid ){ 
			 alert("对不起，您不能查看");
		 }else {
			 window.location.href="time.jsp?ctype="+ctype+"&branchid="+branchid;
		 }
   } 
 

function distri(){
if(typeid == null || typeid == ""){
	 alert("请选择产品型号"); 
}else { 
	 window.location.href='distribution1.jsp?category='+categoryid+'&type='+typeid;
    //window.open('distribution1.jsp?category='+categoryid+'&type='+typeid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
  }
}
 
function serchclick(category,type,branchid,obj){
	 categoryid = category;
	 typeid = type ; 
	 updateClass(obj);  
} 


function pandian(type,branchid){
	
	$.ajax({   
         type: "post", 
         url: "../../admin/server.jsp",    
         data:"method=pandian&branchid="+branchid+"&type="+type,
         dataType: "",   
         success: function (data) { 
        	 add();

            },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
	
	event.cancelBubble =true;
}
 
 
function add(){   
	 var canpandian = true ;
	 $("#table tr").remove();    
 
	 var product = $("#product").val(); 
	 //alert(branchid);
	 $.ajax({   
	        type: "post",   
	         url: "../../admin/server.jsp",    
	         data:"method=inventoryallSN&branch="+branchid+"&product="+product+"&isSN=isSN",
	         dataType: "",        
	         success: function (data) { 
	        	 //alert(data);    
	        	 var addstr =  '<thead>'+ 
	     		  '<tr>'+   
	     		        '<th align="left">订单号</th>'+
		        		 
		     			'<th align="left">产品型号</th>'+ 
		     			'<th align="left">产品条码</th>'+
		     			'<th align="left">产品类别</th>'+ 
		     			'<th align="left">未入库数量</th>'+
		     			'<th align="left">过期时间</th>'+
		     			'<th align="left">盘点</th>'+ 
	     		  '</tr>'+   
	     			'</thead> ';
	        	 
	        	 var jsons =  $.parseJSON(data);
	        	  
	        	 
	        	 for(var p in jsons){
	        		 var key = p ;
	        		 var json = jsons[p];
	        		 var num = json.length; 
	        		 for(var i=0;i<json.length;i++){
		        		 var str = json[i]; 
		                 var pandian = "是";
		                  
		                 if(str.isquery == false|| str.isquery == "false"){
		                	 if(canpandian){
		                		 pandian = '<span style="cursor:hand,color:red" onclick="pandian(\''+str.typeid+'\',\''+branchid+'\')">盘点确认</span>'; 
		                	 }else {  
		                		 pandian = "否"; 
		                	 }
		                 }else{     
		                	 if(canpandian){  
		                		 pandian = str.time;  
		                	 }    
		                 } 
		                  
		                 if(str.papercount != 0){
		                	 addstr += '<tr id="record'+row+'" class="asc" onclick="search(\''+str.typeid+'\',\''+branchid+'\')">'; 
		        		      
		                	 if(i == 0 ){ 
		                		 addstr +=  ' <td rowspan='+num+'>'+key+'</td> ' ;
		        		     }  
		                	     
		                	 addstr += ' <td>'+str.type+'</td> ' +      
		        		     ' <td>'+str.product.encoded+'</td> ' +  
		        		     ' <td>'+str.typestatuesName+'</td> ' + 
		        		     ' <td>'+str.papercount+'</td> ' +   
		        		     // inventoryid 
		        		    ' <td>'+str.activetime+'</td> ' + 
		        		      
		        		     ' <td>'+pandian+'</td> ' +  
		        		     ' </tr>'; 
		        		     row++;
		                 }
		        		
		        	 }
	        		
	        		    
	        		} 
	        	
	        	 
	        	
	        	
	        	 $("#table").append(addstr);      
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });

}


</script>
</head>

<body>

<div class="s_main">
  <input type="hidden" id="time"  value=""/>
  <input type="hidden" id="starttime"  value=""/>
  <input type="hidden" id="endtime"  value=""/>
    
 <!--       -->    
   <div class="weizhi_head">
 <table>
 <tr>
 <td>
  现在位置：入库单查询
 
 </td>
 <td></td>
 <td>
  供应商编码:<%=Company.supply%>
 </td>
 </tr>
 
 </table>  
   </div>      
    
  <div class="main_r_tianjia">
  <table>
  <tr>
   
  
  <td>
    选择产品型号:    
    <input type="text" name="" id="product"/> 
    
    
   
  </td>
 
	
     <td>
    <input type="button" name="" value="查询" onclick="add()"/> 
  </td>
  </tr>
  </table>
          
   </div>  
   <div class="table-list">

<div class="btn">
<table width="100%"  cellspacing="1" id="table">
	

</table>

</div>
<div id="pages"></div>
</div>  

</div>



</body>
</html>
