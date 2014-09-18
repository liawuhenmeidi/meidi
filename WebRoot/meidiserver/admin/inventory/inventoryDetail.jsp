<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
 
String ctype = request.getParameter("ctype");
String branchid = request.getParameter("branchid");



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">

</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<script type="text/javascript">

var ctype = '<%=ctype%>' ; 
var branchid = '<%=branchid%>';
var row = 1; 
//alert(ctype);
 $(function () {  
	 //initproduct();
	  search(ctype,branchid);        
	 
	 
 });
 
 function inventory(inventory,type){
	 if(type == 0 || type == 1){
		 window.showModalDialog('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }else {
		 window.showModalDialog('../dingdanDetail.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }
 }
 
 function search(ctype,branchid){
	 $("#serach table").remove();
	 $.ajax({ 
	        type: "post", 
	         url: "../server.jsp",
	         data:"method=inventorydetail&ctype="+ctype+"&branchid="+branchid,
	         dataType: "", 
	         success: function (data) {
	        	//alert(data);
	        	
	        	 var addstr = '<table id ="serach" width="100%" border="1" cellpadding="0" cellspacing="0" > '+
	        		     '<tr class="asc"> '+
	        		     ' <td>单号</td>'+ 
	        		     ' <td>日期</td> '+
	        		     ' <td>型号</td>'+
	        		     ' <td>操作类型</td> ' +
	        		     ' <td>调拨数量</td> ' +
	        		     ' <td>实际库存数量</td> ' + 
	        		     ' <td>账面库存数量</td>' +  
	        		    ' </tr>';
	        		     
	        		 
	        	 var json =  $.parseJSON(data);
		        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i];
	        		 var strtype = "";
	        		 var type = str.operatortype;
	        		 //alert(type); 
	        		 if(type == 0 ){ 
	        			 strtype = "仓库出货";
	        		 }else if(type == 1){
	        			 strtype = "仓库入货";
	        		 }else if(type == 2){
	        			 strtype = "文员派工";
	        		 }else if(type == 20){
	        			 strtype = "安装公司释放";
	        		 }else if(type == 11){
	        			 strtype = "安装公司派工";
	        		 }else if(type == 6){ 
	        			 strtype = "送货员释放";
	        		 } 
	        		 addstr += '<tr id="record'+row+'" class="asc" ondblclick="inventory('+str.inventoryid+','+type+')">' +  
	        		     ' <td>'+str.inventoryid+'</td> ' +
	        		     ' <td>'+str.time+'</td> ' +  
	        		     ' <td>'+str.type+'</td> ' + 
	        		     ' <td>'+strtype+'</td> ' + 
	        		     ' <td>'+str.count+'</td> ' +
	        		     ' <td>'+str.realcount+'</td>' + 
	        		     ' <td>'+str.papercount+'</td> ' +  
	        		      
	        		     ' </tr>'; 
	        	 }
	        		     
	        	 addstr += '</table>' ;     
	        		     
	        			 $("#serach").append(addstr);  
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });

 }

 
  

</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main">  
   <div class="weizhi_head">现在位置：库存查询</div>       
<br/>

<div id="serach"> 


</div>





</div>


</body>
</html>
