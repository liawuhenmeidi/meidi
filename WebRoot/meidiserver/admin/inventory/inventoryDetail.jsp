<%@ page language="java" import="java.util.*,utill.*,product.*,order.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");  
String ctype = request.getParameter("ctype");
String branchid = request.getParameter("branchid");
String endtime = request.getParameter("endtime");
   
endtime = TimeUtill.dataAdd(endtime,1); 
String starttime = request.getParameter("starttime"); 

Branch b = new Branch();
if(!StringUtill.isNull(branchid)){
	b = BranchManager.getLocatebyid(branchid);
}

String strbranch = b.getLocateName();
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
   
Map<String,User> usermap = UserService.getuserIdStr();

String usermapstr = StringUtill.GetJson(usermap);

Map<String,String> mapdevity = OrderManager.getDeliveryStatuesMap();
String mapdevitystr = StringUtill.GetJson(mapdevity);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">

</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/map.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<script type="text/javascript">

var ctype = '<%=ctype%>' ; 
var branchid = '<%=branchid%>';
var row = 1;
var branchstr = <%=branchstr%>; 
var starttime = '<%=starttime%>'; 
var endtime = '<%=endtime%>';
var usermapstr = <%=usermapstr%>;
var mapdevity = <%=mapdevitystr%>;
//alert(ctype);
 $(function () {  
	 //initproduct();
	 var flag = false ;
	 var str = "";
	 if(starttime != null && starttime != "" && starttime != "null"){
		 str += " and time  BETWEEN  '" + starttime + "'  and  ";
	     flag = true ;
	 } 
	 
	 if(endtime != null && endtime != "" && endtime != "null"){
		 str += " ' " + endtime + "'";
	 }else if(flag){
		 str += "now()";
	 } 
	 //alert(str);
	  search(ctype,branchid,str);        

 });
 
 function inventory(inventory,type){
	 if(type == 0 || type == 1){
		 window.location.href='inventorysearch.jsp?id='+inventory;
		 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }else {
		 window.location.href='dingdanDetail.jsp?id='+inventory;
		 //window.open('dingdanDetail.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }
 } 
 
 
 function search(ctype,branchid,time){
	 $("#serach table").remove();
	 var map = new Map();
	 var totalpapercount = 0 ;
	 var totalrealcount = 0 ;
	 $.ajax({ 
	        type: "post", 
	         url: "../server.jsp",
	         data:"method=inventorydetail&ctype="+ctype+"&branchid="+branchid+"&time="+time,
	         dataType: "", 
	         success: function (data) { 
	        	//alert(data);
	        	
	        	 var addstr = '<div class="table-list" >'+
	        		  '<table width="100%"  cellspacing="1" id="table" > '+
	        		  '<thead>'+ 
	  	     		  '<tr>'+
	  	        		'<th align="left">单号</th>'+
	  	     			'<th align="left">日期</th>'+
	  	     			'<th align="left">型号</th>'+
	  	     			'<th align="left">上报状态</th>'+ 
	  	     			'<th align="left">操作类型</th>'+
	  	     			'<th align="left">票面调拨数量</th>'+
	  	     			'<th align="left">实际调拨数量</th>'+
	  	     			'<th align="left">账面库存数量</th>'+ 
	  	     			'<th align="left">实际库存数量</th>'+ 
	  	     		  '</tr>'+
	  	     			'</thead> ';
	        		 
	        	 var json =  $.parseJSON(data);
		        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i];
	        		 var strtype = "";
	        		 var type = str.operatortype;
	        		 var branch = branchstr[str.branchid].locateName;
	        		 if(branchid != "" && branchid != null){
	        			 totalpapercount =str.papercount;
		        		 totalrealcount  =str.realcount;
	        		 }else {
	        			 map.put(branchid, str);
	        			 var array = map.keySet();
	        			 for(var j in array) { 
	        				 totalpapercount += map.get(array[j]).papercount;
	        				 totalrealcount += map.get(array[j]).realcount; 
	        				 //document.write("key:(" + array[j] +") <br>value: ("+map.get(array[j])+") <br>");
	        				}
	        		 }
	        		 if(type == 0 ){ 
	        			 strtype = branch+"出库";
	        		 }else if(type == 1){ 
	        			 strtype = branch+"入库";
	        		 }else if(type == 2){
	        			 strtype = usermapstr[str.sendUser].branchName+"派工给"+branch;
	        		 }else if(type == 20){
	        			 strtype = branch+"释放"; 
	        		 }else if(type == 11){
	        			 strtype = branch+"派工给"+usermapstr[str.receiveuser].username;
	        		 }else if(type == 6){   
	        			 strtype = usermapstr[str.receiveuser].username+"释放给"+branch;
	        		 }else if(type == 7){    
	        			 strtype = usermapstr[str.receiveuser].username+"拉回给"+branch;
	        		 } else if(type == 8){    
	        			 strtype = usermapstr[str.sendUser].branchName+"同意"+branch+"退货";
	        		 }   
	        		 addstr += '<tr id="record'+row+'" class="asc" ondblclick="inventory('+str.inventoryid+','+type+')">' +  
	        		     ' <td>'+str.inventoryString+'</td> ' +
	        		     ' <td>'+str.time+'</td> ' +   
	        		     ' <td>'+str.type+'</td> ' +  
	        		     ' <td>'+mapdevity[str.devidety]+'</td> ' + 
	        		     ' <td>'+strtype+'</td> ' +  
	        		     ' <td>'+str.allotPapercount+'</td> ' +
	        		     ' <td>'+str.allotRealcount+'</td> ' +
	        		     ' <td>'+totalpapercount+'</td> ' +   
	        		     ' <td>'+totalrealcount+'</td>' + 
	        		     ' </tr>'; 
	        	 }
	        		     
	        	 addstr += '</table> </div>' ;     
	        		     
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
   <div class="weizhi_head">现在位置：库存查询
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   开始时间:<%=starttime%>---结束时间:<%=endtime %>
   <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>
   </div>       
<br/>

<div id="serach"> 


</div>





</div>


</body>
</html>
