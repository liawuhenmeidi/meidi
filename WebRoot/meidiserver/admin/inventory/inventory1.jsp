<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

String branchid = "";   
if(UserManager.checkPermissions(user, Group.Manger)){
	branchid = request.getParameter("branchid");  
}else if(UserManager.checkPermissions(user, Group.sencondDealsend) || UserManager.checkPermissions(user, Group.sale)){
	branchid = BranchManager.getBranchID(user.getBranch())+""; 
} 
  
String category = request.getParameter("category");

Category c = CategoryManager.getCategory(category);

List<Product> listp = ProductManager.getProduct(category);

 
List<Branch> listbranch = BranchManager.getLocate(); 
List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 
  
List<BranchType> listb = BranchTypeManager.getLocate();
  

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<String,Category> newcategorymap = new HashMap<String,Category>();
if(categorymap != null){
	Set<Integer> key = categorymap.keySet();
    for (Iterator<Integer> it = key.iterator(); it.hasNext();) {
        int s =  it.next();
        newcategorymap.put(s+"", categorymap.get(s));
    }
	
} 

String Categorystr = StringUtill.GetJson(newcategorymap); 


Map<String,List<Branch>> map = BranchManager.getLocateMapBranch();  
    
String mapjosn = StringUtill.GetJson(map);
 
HashMap<String,ArrayList<String>> listt = ProductManager.getProductName();

String plist = StringUtill.GetJson(listt);
 
List<String> listallp = ProductManager.getProductlist();
String listallpp = StringUtill.GetJson(listallp);   
   
Map<Integer,Branch> branchmap = BranchManager.getNameMap();
Map<String,Branch> newbranchmap = new HashMap<String,Branch>();
if(branchmap != null){
	Set<Integer> key = branchmap.keySet();
    for (Iterator<Integer> it = key.iterator(); it.hasNext();) {
        int s =  it.next();
        newbranchmap.put(s+"", branchmap.get(s));
    }
	
}


String branchstr = StringUtill.GetJson(newbranchmap); 
//System.out.println(branchstr);

String inventoryid = request.getParameter("id");
Inventory inventory = null ;
String invent = ""; 
String outbranch = "";
String inbranch = "";
String remark = "";
String inittime = "";
String isdisabel = " "; 

if(!StringUtill.isNull(inventoryid)){
	isdisabel = " disabled=\"disabled\" ";
	inventory = InventoryManager.getInventoryID(user, Integer.valueOf(inventoryid));  
	
	if(branchmap != null){
		   Branch branch = branchmap.get(inventory.getOutbranchid());
	       if(branch != null){
	    	   outbranch = branch.getLocateName(); 
	       } 
	       branch = branchmap.get(inventory.getInbranchid());
	       if(branch != null){ 
	    	   inbranch = branch.getLocateName();  
	       } 
	   }
	
	remark = inventory.getRemark();
	inittime = inventory.getIntime(); 
	List<InventoryMessage> list = inventory.getInventory();
	invent = StringUtill.GetJson(list);  
    if(inventory.getInstatues() == 0 && inventory.getOutstatues() == 0){
    	isdisabel = ""; 
    }
}

  

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




<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
 
<script type="text/javascript">
var disable = '<%=isdisabel %>';

 var jsonmap = '<%=mapjosn%>'; 

 var jsons =  <%=plist%>;

 var jsonallp = <%=listallpp%>; 
 
 var row = 1; 
 var rows = new Array();
 var branchstr = <%=branchstr%>; 
 var Categorystr = <%=Categorystr%>;
 var inventoyr = '<%=invent%>';
 var winPar = null;
 var jsoninvent =  $.parseJSON(inventoyr);
  var typeid = ""; 
 $(function () {  
	 add();
     
 }); 
  
 function startRequest(ctype,branchid){ 
	 var time = $("#time").val();
	 
	
	 if("fresh" == time){
		 var starttime = $("#starttime").val(); 
		 var endtime = $("#endtime").val(); 

		 
		 
		 window.location.href='inventoryDetail.jsp?ctype='+ctype+'&branchid='+branchid+'&starttime='+starttime+'&endtime='+endtime; 
	 }
	
 }
 
 
 function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
 }
  
 function search(ctype,branchid){ 
	 $("#time").val("");
	 $("#starttime").val(""); 
	 $("#endtime").val(""); 
	 winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	 setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
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
 

 
 
 
 function add(){   
	 
	 $("#table tr").remove();    
	 var branch = "<%=branchid%>";
	 var category = "<%=category%>"; 
	 var b = $("#branch").val();  
	 
	 var product = $("#product").val(); 

	 if(branch == null || branch == ""){
		 branch = b ; 
	 }   
	 $("#branch option[value='"+branch+"']").attr("selected","selected"); 
	 //alert(branch);
	 $.ajax({   
	        type: "post", 
	         url: "../server.jsp",    
	         data:"method=inventoryall&branch="+branch+"&category="+category+"&product="+product,
	         dataType: "",   
	         success: function (data) { 
	        	 var addstr = '<tr class="asc"> '+
	        	    
	        	     ' <td>产品类别</td>'+
	        	     ' <td>产品型号</td> '+
	        	     ' <td>账面库存数量</td>'+
	        	     ' <td>实际库存数量</td> ' +
	        	     ' <td>销量</td> ' + 
	        	    ' </tr>';
	        	 var json =  $.parseJSON(data);
	        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i]; 
	        		  
	        		 addstr += '<tr id="record'+row+'" class="asc" ondblclick="search(\''+str.type+'\',\''+branch+'\')"  onclick="serchclick(\''+str.categoryid+'\',\''+str.type+'\',\''+branch+'\',this)">' +  
	        		    
	        		     ' <td>'+str.cateoryName+'</td> ' +   
	        		     ' <td>'+str.type+'</td> ' +   
	        		     ' <td>'+str.papercount+'</td> ' +  
	        		     // inventoryid
	        		     ' <td>'+str.realcount+'</td> ' + 
	        		      
	        		     ' <td></td> ' +  
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
<!--   头部开始   -->
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
<div class="main">  
   
  <input type="hidden" id="time"  value=""/>
  <input type="hidden" id="starttime"  value=""/>
  <input type="hidden" id="endtime"  value=""/>
  
  <div class="weizhi_head">现在位置：<%=c.getName() %>库存
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
	  
	    选择产品类别:     
	  <select id="product">  
	  <option value=""></option>
	   <% if(listp != null){
		   for(int i=0;i<listp.size();i++){
			   Product b = listp.get(i);
			   
	    %>    
	    <option value="<%=b.getType()%>"><%= b.getType()%></option>
	   <% 
		   }    
	   }
	   %>
	  </select>
	  
	   <input type="button" name="" value="查询" onclick="add()"/>   
			   <%
		   }  
   %>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>       
   </div>  
       
 </div>        
     <div style="background-color:;width:100%" >
     <br/> 
        
   <table width="100%" border="1" id="table" cellpadding="0" cellspacing="0" >
     <tr class="asc"> 
      
      <td>产品类别</td>
      <td>产品型号</td> 
      <td>账面库存数量</td>
      <td>实际库存数量</td>  
      <td>销量</td>   
     </tr>
 
   </table>
  

  </div>
       

<br/>

<div id="serach"> 


</div>
</body>
</html>
