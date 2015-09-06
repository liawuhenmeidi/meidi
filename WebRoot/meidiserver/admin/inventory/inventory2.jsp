<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

String branchid = "";   
if(UserManager.checkPermissions(user, Group.Manger)){
	branchid = request.getParameter("branchid");  
}else if(UserManager.checkPermissions(user, Group.sencondDealsend)){
	branchid = BranchManager.getBranchID(user.getBranch())+""; 
}
  
String category = request.getParameter("category");

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
   
Map<Integer,Branch> branchmap = BranchManager.getIdMap();
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





<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="../../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
<link href="../../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />

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
  
 var jsoninvent =  $.parseJSON(inventoyr);
  
 $(function () { 
	 add();
 });
 
 function inventory(inventory){
	 window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 
 }
 
 function search(category,branchid){ 
	 window.open('inventory.jsp?category='+category+"&branchid="+branchid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
 }
 
 function add(){  
	 $("#table tr").remove();    
	 var branch = "<%=branchid%>";
	 var category = "<%=category%>"; 
	 var b = $("#branch").val();
	 if(branch == null || branch == ""){
		 branch = b ;
	 } 
	 
	 alert(branch); 
	 alert(category);
	 var str = "";   
	 $.ajax({ 
	        type: "post", 
	         url: "../server.jsp",    
	         data:"method=inventoryall&branch="+branch+"&category="+category+"&time="+str,
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
	        		 
	        		 addstr += '<tr id="record'+row+'" class="asc" onclick="search(\''+str.categoryid+'\',\''+branch+'\')">' +  
	        		    
	        		     ' <td>'+str.cateoryName+'</td> ' +  
	        		     ' <td>'+str.type+'</td> ' +   
	        		     ' <td>'+str.papercount+'</td> ' +  
	        		     // inventoryid
	        		     ' <td>'+str.realcount+'</td> ' + 
	        		      
	        		     ' <td></td> ' +  
	        		     ' </tr>'; 
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
   <%
    if(UserManager.checkPermissions(user, Group.dealSend)){
    	%>
         列表：   
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
          查看分布:
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
		   }  
   %>
                     
 </div>      
 </div>        
     <div style="background-color:;width:80%" >
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
       
     </div>
<br/>

<div id="serach"> 


</div>
</body>
</html>
