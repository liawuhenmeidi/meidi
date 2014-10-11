<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
 
List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 
  
List<BranchType> listb = BranchTypeManager.getLocate();
  
List<String> listbranch = BranchManager.getLocateAll(); 
String listall = StringUtill.GetJson(listbranch);

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
System.out.println(branchstr);

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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>





<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script type="text/javascript">
var disable = '<%=isdisabel %>';

 var jsonmap = '<%=mapjosn%>'; 

 var jsons =  <%=plist%>;
 
 //var listallp = '<%=listallpp%>';
//alert(listallp);
 var jsonallp = <%=listallpp%>; 
 
 var jsonall = <%=listall%>;
 
 var row = 1; 
 var rows = new Array();
 var branchstr = <%=branchstr%>; 
 var Categorystr = <%=Categorystr%>;
 var inventoyr = '<%=invent%>';
 //alert(inventoyr); 
 var jsoninvent =  $.parseJSON(inventoyr);
  
 $(function () { 
	 //initproduct();
	          
	 initTime();
	 

	 
	 $("#branch").autocomplete({ 
		 source: jsonall
	    });  
	    
	 $("#ordertype0").autocomplete({ 
		 source: jsonallp
	    });  
	 
 });
 
 function initTime(){ 
	   var opt = { };
	    opt.date = {preset : 'date'};
		$('#saledateStart').val('').scroller('destroy').scroller($.extend(opt['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
		var opt2 = { };
	    opt2.date = {preset : 'date'};
		$('#saledateEnd').val('').scroller('destroy').scroller($.extend(opt2['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
}
 
 function inventory(inventory){
	 
	 window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 
 }
 
 
  
 function search(ctype){ 
	 $("#serach table").remove();
	 $.ajax({  
	        type: "post", 
	         url: "../server.jsp",
	         data:"method=inventorydetail&ctype="+ctype,
	         dataType: "", 
	         success: function (data) {
	        	//alert(data);
	        	 var addstr = '<table id ="serach" width="100%" border="1" cellpadding="0" cellspacing="0" > '+

	        		     '<tr> '+
	        		     ' <td>单号</td>'+
	        		     ' <td>日期</td> '+
	        		     ' <td>型号</td>'+
	        		     ' <td>调拨数量</td> ' +
	        		     ' <td>销售数量</td> ' + 
	        		     ' <td>库存</td>' +  
	        		    ' </tr>';
	        		     
	        		 
	        	 var json =  $.parseJSON(data);
		        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i];
	        		 
	        		 addstr += '<tr id="record'+row+'" onclick="inventory('+str.inventoryid+')">' +  
	        		     ' <td>'+str.inventoryid+'</td> ' +
	        		     ' <td>'+str.time+'</td> ' + 
	        		     ' <td>'+str.type+'</td> ' + 
	        		     ' <td>'+str.realcount+'</td>' + 
	        		     ' <td></td> ' +  
	        		     ' <td>'+str.inventcount+'</td> ' +  
	        		     ' </tr>'; 
	        	 }
	        		     
	        	 addstr += '</table>' ;     
	        			 $("#serach").append(addstr);  
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });

 }

 function search(ctype,branchid){
	 window.open('inventoryDetail.jsp?ctype='+ctype+"&branchid="+branchid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
 }
 
 
 function add(){
	 $("#table tr").remove();
	 var ctype = $("#ordertype0").val();
	 var branch = $("#branch").val(); 
	 var saledateStart = $("#saledateStart").val();
	 var saledateEnd = $("#saledateEnd").val();
	 var str = ""; 
	 var flag = false ; 
	 // pos == "" || pos == null || pos == "null"
	 if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
		 str += " and time BETWEEN '" + saledateStart + "'  and  ";
	     flag = true ;
	 } 
	 
	 if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
		 str += " ' " + saledateEnd + "'";
	 }else if(flag){
		 str += "now()";
	 }
	 $.ajax({ 
	        type: "post", 
	         url: "../../admin/server.jsp",
	         data:"method=inventory&branch="+branch+"&ctype="+ctype+"&time="+str,
	         dataType: "", 
	         success: function (data) { 
	        	 var addstr = '<tr class="asc"> '+
	        	     ' <td>序号</td>'+ 
	        	     ' <td>门店</td>'+  
	        	     ' <td>产品类别</td>'+
	        	     ' <td>产品型号</td> '+
	        	     ' <td>账面库存数量</td>'+
	        	     ' <td>实际库存数量</td> ' +
	        	     ' <td>盘点</td> ' + 
	        	    ' </tr>';
	        	 var json =  $.parseJSON(data);
	        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i];
	        		 
	        		 addstr += '<tr id="record'+row+'" class="asc" onclick="search(\''+str.type+'\',\''+str.branchid+'\')">' +  
	        		     ' <td>'+str.id+'</td> ' +
	        		     ' <td>'+branchstr[str.branchid].locateName+'</td> ' + 
	        		     ' <td>'+Categorystr[str.inventoryid].name+'</td> ' + 
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
<!--   头部结束   -->

<div class="main">  
   <div class="weizhi_head">现在位置：库存查询</div> 
   <div class="main_r_tianjia">
   
   </div>      
     <div>     
     <form action="InventoryServlet"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="method" value="add"/>  
                     
  <div style="background-color:;width:80%" > 
                 仓库：  
         <% if(UserManager.checkPermissions(user, Group.dealSend)){
         %> 
         <input type="text" name="branch" id="branch" class="cba" value="<%=outbranch %>"  <%=isdisabel %>/>                    
         <%}else if(UserManager.checkPermissions(user, Group.sencondDealsend)){%> 
         
         <input type="hidden" name="branch" id="branch" class="cba" value="<%=user.getBranch() %>"/>
         <%=user.getBranch() %> 
             
         <% }%>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;           
    类别:
    <select>
    
    <% 
    if(categorylist != null){
    	for(int i=0;i<categorylist.size();i++){
            Category category = categorylist.get(i); 
     %> 
     <option value="<%=category.getId()%>"><%=category.getName() %></option>
    <%}
    } %>
    </select>
     型号:                 
  <input type="text" name="ordertype0"  id="ordertype0"   class="cba" <%=isdisabel %>/>    
  开始时间:<input class="date" type="text" name="saledateStart"  id="saledateStart"  readonly="readonly" ></input>
     
  结束时间:<input class="date2" type="text" name="saledateEnd" id ="saledateEnd"   readonly="readonly"></input> 
       
  <input type="button" name="" value="查询" onclick="add()" <%=isdisabel %>/>        
            
    <br/>    
     <br/>        
   <table width="100%" border="1" id="table" cellpadding="0" cellspacing="0" >
     <tr class="asc"> 
      <td>序号</td>
      <td>门店</td> 
      <td>产品类别</td>
      <td>产品型号</td> 
      <td>账面库存数量</td>
      <td>实际库存数量</td>  
      <td>盘点</td>   
     </tr>
 
   </table>
 
  </div>
       
    
     
   
 </form>
     </div>
<br/>

<div id="serach"> 


</div>





</div>


</body>
</html>
