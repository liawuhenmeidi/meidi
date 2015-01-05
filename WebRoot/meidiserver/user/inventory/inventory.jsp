<%@ page language="java" import="java.util.*,utill.*,inventory.*,branch.*,category.*,branchtype.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
String branchid = request.getParameter("branchid");

Branch branch = null;   
if(UserManager.checkPermissions(user, Group.sale) || UserManager.checkPermissions(user, Group.sencondDealsend)){
	if(StringUtill.isNull(branchid)){
		branchid = user.getBranch()+"";    
	}
}
branch = BranchManager.getLocatebyid(user.getBranch()+"");
List<Branch> listbranch = BranchService.getList(); 

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
	 
	
	 $("#branch option[value='"+branch+"']").attr("selected","selected");
	 
	 $.ajax({ 
	        type: "post", 
	         url: "../../admin/server.jsp",    
	         data:"method=inventoryall&branch="+branch,
	         dataType: "",   
	         success: function (data) { 
	        	 var addstr = "" ; 
	     addstr +=   "<thead>"+
	     		"<tr>"+
		     		"<th align=\"left\">产品类别</th>"+
		     		"<th align=\"left\">产品型号</th>"+
		     		"<th align=\"left\">账面库存数量</th>"+
		     		"<th align=\"left\">实际库存数量</th>"+
		     		"<th align=\"left\">盘点</th>"+ 
	     		"</tr>"+
	     "</thead> "; 

	        	 var json =  $.parseJSON(data);
	        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i]; 
	        		 var pandian ="否";
	        		 if(str.isquery == true){
	        			 pandian = "是";
	        		 }
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

     
 <!--       -->   
   <div class="weizhi_head">现在位置：库存查询
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   <a href="../welcom.jsp"><font style="color:blue;font-size:20px;" >返回</font></a>  &nbsp;&nbsp;&nbsp;&nbsp; <input type="button" name="" value="查询" onclick="add()"/>       
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
				    <option value="<%=b.getId()%>"><%=b.getLocateName()%></option>
				   <%    
			   } 
		   }
	   }  
  
   %>
  </select>
  
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

</div>
</div>



</body>
</html>
