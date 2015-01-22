<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
 
String category = request.getParameter("category"); 
String branchid = request.getParameter("branchid");
String userbranch = user.getBranch();
Branch branch = null;   
if(UserManager.checkPermissions(user, Group.Manger)){
	branchid = request.getParameter("branchid");
}else if(UserManager.checkPermissions(user, Group.sale) ){
	branch = BranchManager.getLocatebyid(user.getBranch());
	if(StringUtill.isNull(branchid)){
		branchid = user.getBranch()+"";   
	}
} 
  
Category c = CategoryManager.getCategory(category);

List<Product> listp = ProductManager.getProduct(category);

   
List<Branch> listbranch = BranchManager.getLocate(); 
List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 
  
 
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();


%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript">

var row = 1; 
var rows = new Array();
var winPar = null;
var typeid = ""; 
var branch = "<%=branchid%>";

var category = "<%=category%>"; 
var userbranch = "<%=userbranch%>";

$(function () {  
	 add();
    
}); 
 
//function startRequest(ctype,branchid){ 
//	 var time = $("#time").val();
	// if("fresh" == time){
	//	 var starttime = $("#starttime").val(); 
	//	 var endtime = $("#endtime").val(); 
	//	 window.location.href='inventoryDetail.jsp?ctype='+ctype+'&branchid='+branchid+'&starttime='+starttime+'&endtime='+endtime; 
	//	 $("#time").val("");
	//	 return ;
	// }
	
//}


function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
}
 
function search(ctype,branchid){ 
		 if('<%=user.getBranch()%>' != branchid ){ 
			 alert("对不起，您不能查看");
		 }else {
			 window.location.href="time.jsp?ctype="+ctype+"&branchid="+branchid;
			// $("#time").val("");
			// $("#starttime").val(""); 
			// $("#endtime").val(""); 
			// winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
			// setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
	 
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
	 var canpandian = false ;
	 
	 $("#table tr").remove();    

	 var b = $("#branch").val(); 
	 
	 var product = $("#product").val(); 
	  
	 if((branch == null || branch == "") && b!= null&&b!=""){
		 branch = b ; 
	 }
     
	 if(b!= null&&b!=""){
		 branch = b ;
	 } 
	 
	 if(userbranch == branch){
		 canpandian = true ;
	 }
	 
	 //alert(branch);
	 $("#branch option[value='"+branch+"']").attr("selected","selected"); 
	 //alert(branch);
	 $.ajax({   
	        type: "post", 
	         url: "../../admin/server.jsp",    
	         data:"method=inventoryall&branch="+branch+"&category="+category+"&product="+product,
	         dataType: "",   
	         success: function (data) { 
	        	 var addstr =  '<thead>'+ 
	     		  '<tr>'+
		        		'<th align="left">产品类别</th>'+
		     			'<th align="left">产品型号</th>'+
		     			'<th align="left">账面库存数量</th>'+
		     			'<th align="left">实际库存数量</th>'+
		     			'<th align="left">盘点</th>'+ 
	     		  '</tr>'+
	     			'</thead> ';
	        	
	        	 var json =  $.parseJSON(data);
	        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i]; 
	                 var pandian = "是";
	                 
	                 if(str.isquery == false|| str.isquery == "false"){
	                	 if(canpandian){
	                		 pandian = '<span style="cursor:hand,color:red" onclick="pandian(\''+str.typeid+'\',\''+userbranch+'\')">盘点确认</span>'; 
	                	 }else {
	                		 pandian = "否";
	                	 }
	                 }else{
	                	 if(canpandian){
	                		 pandian = str.time; 
	                	 }
	                 }
	        		 addstr += '<tr id="record'+row+'" class="asc" onclick="search(\''+str.typeid+'\',\''+branch+'\')">' +  
	        		    
	        		     ' <td>'+str.cateoryName+'</td> ' +   
	        		     ' <td>'+str.type+'</td> ' +   
	        		     ' <td>'+str.papercount+'</td> ' +  
	        		     // inventoryid
	        		     ' <td>'+str.realcount+'</td> ' + 
	        		      
	        		     ' <td>'+pandian+'</td> ' +  
	        		     ' </tr>'; 
	        		     row++;
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
   <div class="weizhi_head">现在位置：库存查询
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
   <a href="inventory.jsp?branchid=<%=branchid%>"><font style="color:blue;font-size:20px;" >返回</font></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   <input type="button" name="" value="查询" onclick="add()"/>        
   </div>    
   
  <div class="main_r_tianjia">
   <%
    if(UserManager.checkPermissions(user, Group.sale)){ 
		  String branchids = branch.getBranchids();
		   if(branchids != null){
%>

  选择仓库:     
  <select id="branch">  
  <option value=""></option> 
   <%  
       String branchs[] = branchids.split("_"); 
	   for(int i=0;i<listbranch.size();i++){
		   Branch b = listbranch.get(i); 
		   for(int j=0;j<branchs.length;j++){ 
			   if(branchs[j].equals(b.getId()+"")){
				  
				   %>    
				    <option value="<%= b.getId()%>"><%=b.getLocateName()%></option>
				   <%    
			   } 
		   }
	   }  
  
   %>
  </select> 
  <br/> 
	    选择产品型号:     
	  <select id="product">  
	  <option value=""></option>
	   <% if(listp != null){
		   for(int i=0;i<listp.size();i++){
			   Product b = listp.get(i);
			   
	    %>    
	    <option value="<%=b.getId()%>"><%= b.getType()%></option>
	   <% 
		   }    
	   }
	   %> 
	  </select>
	  <br/>
	  
	   <br/>
<%
	   }  
       }
    %>
     
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
