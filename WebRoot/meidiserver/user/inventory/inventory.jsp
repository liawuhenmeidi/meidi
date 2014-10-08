<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

String branchid = "";
Branch branch = null;   
if(UserManager.checkPermissions(user, Group.sencondDealsend) || UserManager.checkPermissions(user, Group.sale)){
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
<title>产品管理</title>


<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>

<script type="text/javascript">
  
 var row = 1; 
 var rows = new Array();
 var categoryid = "";
 $(function () {  
	 add();
 }); 
    
 function search(category,branchid){ 
		// window.open('inventory1.jsp?category='+category+'&branchid='+branchid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no'); 
		 window.location.href="inventory1.jsp?category="+category+"&branchid="+branchid;
 }   
   
 function distri(){
	 if(categoryid == null || categoryid == ""){
		 alert("请选择商品");
	 }else { 
		 window.location.href='distribution.jsp?category='+categoryid;
		 //window.open('distribution.jsp?category='+categoryid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
       }
	 }
 
 function serchclick(category,branchid,obj){
	 search(category,branchid);
	 //categoryid = category;
	 //updateClass(obj);  
  } 
 
 function add(){  
	 $("#table tr").remove();
	 var b = $("#branch").val();
	 var branch = "<%=branchid%>";
	// if(branch == null || branch == ""){
	//	 branch = b ;
	// }  
	if(b != null && b != ""){
		branch = b ;
	} 
	// alert(branch);
	 $.ajax({ 
	        type: "post", 
	         url: "../../admin/server.jsp",    
	         data:"method=inventoryall&branch="+branch,
	         dataType: "",   
	         success: function (data) { 
	        	 var addstr = '<tr class="asc" > '+
	        	     
	        	     ' <td>产品类别</td>'+
	        	     ' <td>产品型号</td> '+
	        	     ' <td>账面库存数量</td>'+
	        	     ' <td>实际库存数量</td> ' +
	        	     ' <td>销量</td> ' + 
	        	    ' </tr>';
	        	 var json =  $.parseJSON(data);
	        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i]; 
	        		 
	        		 addstr += '<tr id="record'+row+'" class="asc" ondblclick="search(\''+str.categoryid+'\',\''+branch+'\')"  onclick="serchclick(\''+str.categoryid+'\',\''+branch+'\',this)" >' +  
	        		     
	        		     ' <td>'+str.cateoryName+'</td> ' +  
	        		     ' <td>'+str.type+'</td> ' +    
	        		     ' <td>'+str.papercount+'</td> ' +  
	        		     // inventoryid
	        		     ' <td>'+str.realcount+'</td> ' + 
	        		      
	        		     ' <td></td> ' +  
	        		     ' </tr>'; 
	        		     row ++;
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
 <jsp:include flush="true" page="../../head.jsp">
  <jsp:param name="dmsn" value="" /> 
  </jsp:include>

<!--   头部结束   -->
<div class="main">   
   <div class="weizhi_head">现在位置：库存查询
     
   
   <% 
    if(UserManager.checkPermissions(user, Group.dealSend)){
    	%>
   
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
    <a href="javascript:distri();"> 查看分布</a>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
         选择仓库:     
	  <select id="branch">  
	  <option value=""></option>
	   <% if(listbranch != null){
		   for(int i=0;i<listbranch.size();i++){
			   Branch b = listbranch.get(i);
	    %>   
	    <option value="<%=b.getId()%>"><%=b.getLocateName()%></option>
	   <% 
		   }   
	   }
	   %>
	  </select>
	   <input type="button" name="" value="查询" onclick="add()"/>   
			   <%
		   }else if(UserManager.checkPermissions(user, Group.sale)){ 
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
					    <option value="<%=b.getId()%>"><%=b.getLocateName()%></option>
					   <%    
				   } 
			   }
		   }  
	  
	   %>
	  </select>
	   <input type="button" name="" value="查询" onclick="add()"/>  
   <%
		   }  
	}
   %>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
   <a href="../welcom.jsp"><font style="color:blue;font-size:20px;" >返回</font></a>                     
 </div>        
     <div class="table-list" >
     <br/>        
   <table width="100%"  cellspacing="1" id="table" >
    <thead>
     <tr >  
      <td>产品类别</td>
      <td>产品型号</td> 
      <td>账面库存数量</td>
      <td>实际库存数量</td>  
      <td>销量</td>   
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
