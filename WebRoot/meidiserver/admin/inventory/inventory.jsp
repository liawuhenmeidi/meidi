<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

List<String> listbranchp = BranchManager.getLocateAll();  
String listall = StringUtill.GetJson(listbranchp); 


String branchid = "";
Branch branch = null;   
if(UserManager.checkPermissions(user, Group.Manger)){
	branchid = request.getParameter("branchid"); 
}else if(UserManager.checkPermissions(user, Group.sencondDealsend) || UserManager.checkPermissions(user, Group.sale) && !UserManager.checkPermissions(user, Group.dealSend)){
	branchid = user.getBranch()+"";   
	branch = BranchManager.getLocatebyid(branchid);
} 
  


  
List<Branch> listbranch = BranchService.getList(); 

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
 
td {  
    width:100px;
    line-height:30px;
}




</style>  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库存查询</title>

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css"/>
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<script type="text/javascript">
 var jsonall = <%=listall%>;
 var row = 1;  
 var rows = new Array();
 var categoryid = "";
 $(function () { 
	 $("#branch").autocomplete({ 
		 source: jsonall
	    }); 
	 add();
 }); 
   
 function search(category,branchid){ 
	  window.location.href='inventory1.jsp?category='+category+'&branchid='+branchid;
		// window.open('inventory1.jsp?category='+category+'&branchid='+branchid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no'); 
 }   
 
 function distri(){
	 if(categoryid == null || categoryid == ""){
		 alert("请选择商品");
	 }else { 
	     //window.open('distribution.jsp?category='+categoryid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
		 window.location.href='distribution.jsp?category='+categoryid; 
	   }
	 }
 
 function serchclick(category,branchid,obj){
	 categoryid = category;
	 updateClass(obj);  
  } 
 
 function add(){  
	 $("#table tr").remove();
	 var b = $("#branch").val();
	 var branch = "<%=branchid%>";
	// if(branch == null || branch == ""){
	//	 branch = b ;
	// }  
	var counttype = $("#counttyepe").val();
	
	if(b != null && b != ""){
		branch = b ;
	} 
	 //alert(branch);
	 $.ajax({ 
	        type: "post", 
	         url: "../server.jsp",    
	         data:"method=inventoryall&branch="+branch,
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
	        		 var pandian ="否";
	        		 if(str.isquery == true){
	        			 pandian = "是";
	        		 }
	        		 if(counttype != 0 || counttype == 0 && str.papercount != 0 ){
		        		 addstr += '<tr id="record'+row+'" class="asc" ondblclick="search(\''+str.categoryid+'\',\''+branch+'\')"  onclick="serchclick(\''+str.categoryid+'\',\''+branch+'\',this)" >' +  
		        		      
		        		     ' <td>'+str.cateoryName+'</td> ' +  
		        		     ' <td>'+str.type+'</td> ' +    
		        		     ' <td>'+str.papercount+'</td> ' +  
		        		     // inventoryid
		        		     ' <td>'+str.realcount+'</td> ' + 
		        		      
		        		     ' <td>'+pandian+'</td> ' +  
		        		     ' </tr>'; 
		        		     row ++;
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
<!--   头部开始   -->
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
<div class="main">   
   <div class="weizhi_head">现在位置：库存查询
     
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   <% 
    if(UserManager.checkPermissions(user, Group.dealSend)){
    	%>
   
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
    <a href="javascript:distri();"> 查看分布</a>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
       仓库:   
       <input type="text" name="branch" id="branch" class="cba"  />  
	 
	   <select id="counttyepe" name = "counttyepe">
         <option value="-1">全部显示</option>
         <option value=0 >只显示库存不为0</option>
     </select> 
  
	   <input type="button" name="" value="查询" onclick="add()"/>   
			   <%
	} 
   %>
                    
 </div>        
     <div class="table-list" >
     <br/>        
   <table width="100%"  cellspacing="1" id="table" >
   <thead>
		<tr>
			<th align="left">产品类别</th>
			<th align="left">产品型号</th>
			<th align="left">账面库存数量</th>
			<th align="left">实际库存数量</th>
			<th align="left">盘点</th> 
		</tr>
	</thead>
   </table>
 
  </div>
       
     </div>
<br/>

<div id="serach"> 


</div>
</body>
</html>
