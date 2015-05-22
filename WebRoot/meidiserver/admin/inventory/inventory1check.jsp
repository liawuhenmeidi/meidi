<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
String userbranch = user.getBranch();   
String category = request.getParameter("category"); 
String branchid = request.getParameter("branchid");
String time = request.getParameter("time");
if(StringUtill.isNull(time)){
	time = TimeUtill.dataAdd(TimeUtill.getdateString(), -1);
} 
if(StringUtill.isNull(branchid)){
	 
}
//boolean flag = UserManager.checkPermissions(user, Group.ordergoods, "f"); 
Category c = CategoryManager.getCategory(category);
 
List<String> listp = ProductService.getlist(Integer.valueOf(category));
 
String allp = StringUtill.GetJson(listp); 
    
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp); 
  
//List<InventoryBranch> list = InventoryAllManager.getlist(user,branchid,
	//	category);    
    
     
Map<String, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryAllManager.getInventoryMap(user,category,branchid,time);  
  
//System.out.println(mapin);  
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
td {
	width: 100px;
	line-height: 30px;
}

#head td {
	white-space: nowrap;
} 
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>



<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" /> 
 
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript">
var jsonall = <%=listall%>;
var allp = <%=allp%>; 

 var row = 1; 
 var rows = new Array(); 
 var winPar = null;
  var typeid = ""; 
  var branch = "<%=branchid%>"; 
  var userbranch = "<%=userbranch%>";
    
 $(function () {  
	 $("#branchid").autocomplete({ 
		 source: jsonall
	    });
	 //allp
	 //add();
     
 });  
  
 function startRequest(ctype,branchid){ 
	 var time = $("#time").val();
	 if("fresh" == time){
		 var starttime = $("#starttime").val(); 
		 var endtime = $("#endtime").val(); 
		 window.location.href='inventoryDetail.jsp?ctype='+ctype+'&branchid='+branchid+'&starttime='+starttime+'&endtime='+endtime; 
		 $("#time").val("");
		 return ;
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
   


function serchclick(category,type,branchid,obj){
	 categoryid = category;
	// alert(type);
	 typeid = type ; 
	 updateClass(obj);  
 } 
 
function pandian(type,branchid){
	$.ajax({   
         type: "post", 
         url: "../server.jsp",    
         data:"method=pandian&branchid="+branchid+"&type="+type,
         dataType: "",   
         success: function (data) { 
        	 add();

            },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
}
 
	function addlInstorage(branchid, type) {
		window.location.href = '../ordergoods/addlInstorage.jsp?branchid='
				+ branchid + '&type=' + type;
	} 
</script>
</head>

<body>
	<!--   头部开始   -->
	<jsp:include flush="true" page="../head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>

	<!--   头部结束   -->
 
	<input type="hidden" id="time" value="" />
	<input type="hidden" id="starttime" value="" />
	<input type="hidden" id="endtime" value="" />
<form action="inventory1check.jsp">
<input type="hidden" name="category" value="<%=category%>"/>
	<table width="100%" id="head">  
		<tr>  
			<td>现在位置：<%=c.getName()%>库存</td>
			<td>仓库:<input type="text" name="branchid" id="branchid" value="<%=branchid %>" />
			</td>  
                 <td>时间:<input name="time" type="text" id="time"
						value="<%=time%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						 /></td> 
			<td><input type="submit" name="" value="查询" /></td>
             
			<td><a href="javascript:history.go(-1);"><font
					style="color:blue;font-size:20px;">返回</font>
			</a></td>
		</tr>





	</table>
</form>
	<div class="table-list">

		<table width="100%" cellspacing="1" id="table">
			<thead>
				<tr> 
					<th align="left">产品类别</th>
					<th align="left">产品型号</th>
					<th align="left">常规特价实货未入库</th>
					<th align="left">样机未入库</th> 
					<th align="left">样机入库</th> 
					<th align="left">苏宁系统库存</th>
					<th align="left">上次盘点日期</th>
					<th align="left">盘点</th>
				</tr>
			</thead>
         <%  if(!mapin.isEmpty()){
        	 Set<Map.Entry<String, Map<String, Map<Integer, InventoryBranch>>>> set = mapin.entrySet();
        	 Iterator<Map.Entry<String, Map<String, Map<Integer, InventoryBranch>>>> it = set.iterator();
             while(it.hasNext()){
            	 Map.Entry<String, Map<String, Map<Integer, InventoryBranch>>> mapent = it.next();
            	 Map<String, Map<Integer, InventoryBranch>> mapb = mapent.getValue();
          //System.out.println(1);
            	 Set<Map.Entry<String, Map<Integer, InventoryBranch>>> setb = mapb.entrySet();
            	 Iterator<Map.Entry<String, Map<Integer, InventoryBranch>>> itb = setb.iterator();
            	 while(itb.hasNext()){
            		 //System.out.println(2);
            		 Map.Entry<String, Map<Integer, InventoryBranch>> mapentb = itb.next();
            		 Map<Integer, InventoryBranch> mapt = mapentb.getValue();
            		 String type = mapentb.getKey();
            		 String cname = ProductService.getIDmap().get(Integer.valueOf(type)).getCname();
            		 String tname = ProductService.getIDmap().get(Integer.valueOf(type)).getType();
            		 int outnum = 0 ;
            		 int inmodelnum = 0 ; 
            		 int outmodelnum = 0 ; 
            		 int snnum = 0 ;
            		 String querytime = "";
            		 Set<Map.Entry<Integer, InventoryBranch>> sett = mapt.entrySet();
            		 Iterator<Map.Entry<Integer, InventoryBranch>> itt =  sett.iterator(); 
            		 while(itt.hasNext()){
            			// System.out.println(3);
            			 Map.Entry<Integer, InventoryBranch> mapentt = itt.next();
            			 InventoryBranch in = mapentt.getValue();
          
            			 if(in.getTypeStatues() == 1){
            				 outnum += in.getPapercount();
            			 }else if(in.getTypeStatues() == 2){
            				 outnum += in.getPapercount();
            			 }else if(in.getTypeStatues() == 3){
            				// System.out.println(in.getTypeStatues()); 
            				 outmodelnum += in.getPapercount(); 
            				 inmodelnum += in.getSnModelnum();  
            			 } 
            			   
            			 snnum = in.getSnNum();
            			
            			 
            		 } 
            		       
            		    
            		  
            		 %> 
            		 <tr class="asc">
            		   
            		 <td><%=cname %></td> 
            		  <td><%=tname %></td> 
            		  <td><%=outnum%></td> 
            		  <td><%=outmodelnum %></td>
            		  <td><%=inmodelnum %></td> 
            		   <td><%=snnum %></td>
            		   <td><%=querytime%></td>
            		   <td ><input type="checkbox" name="inid" ></input></td>
            		 </tr>
  
            		 <%
            		 
            	 }  
            	 
            	 
            	 
             }
         
         
         } %>
		</table>

 
	</div>


	<br />

	<div id="serach"></div>
</body>
</html>
