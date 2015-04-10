<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
 
String category = request.getParameter("category");
  
Category c = CategoryManager.getCategory(category); 
 
String type = request.getParameter("type");  
 
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
<link rel="stylesheet" href="../../css/jquery-ui.css"/>
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<script src="../../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
<link href="../../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
 
 var row = 1; 
 var rows = new Array();
 var branchstr = <%=branchstr%>; 

  
 $(function () { 
	 add();
 });
 
 function search(category,branchid,ctype){
	// window.open('inventory1.jsp?category='+category+'&branchid='+branchid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no'); 
	 //window.location.href='inventory1.jsp?category='+category+'&branchid='+branchid;
	 $("#time").val("");
	 $("#starttime").val(""); 
	 $("#endtime").val(""); 
	 winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	
	 setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
	 
 } 
  
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
 
 function add(){    
	 $("#table tr").remove();
	 var category = "<%=category%>";
	 var type = "<%=type%>";

	 var counttype = $("#counttyepe").val();
	 
	 $.ajax({ 
	        type: "post",  
	         url: "../server.jsp",     
	         data:"method=inventorydis&category="+category+"&type="+type,
	         dataType: "",   
	         success: function (data) { 
	        	 var addstr = '<tr class="dsc">'
	        		   + '<td >门店</td>'
						+ '<td >产品类别</td>'
						+ '<td >产品型号</td>'
						+ '<td >'
						+ '<table width="100%"><tr><td align="left">安装网点：账面库存数量</td></tr><tr><td align="left">销售门店：未入库数量</td></tr></table>'
						+ '</td>' + '<td >实际库存数量</td>'
						+ '<td >盘点</td>' + '</tr>';
	        	 var json =  $.parseJSON(data); 
	        	
	        	 for(var i=0;i<json.length;i++){
	        		 var str = json[i]; 
	        		 var pandian ="否";
	        		 if(str.isquery == true){
	        			 pandian = str.time;
	        		 }
	        		 
	        		 if(counttype != 0 || counttype == 0 && str.papercount != 0 ){
		        		 addstr += '<tr id="record'+row+'" class="asc" ondblclick="search(\''+str.categoryid+'\',\''+branchstr[str.branchid].locateName+'\',\''+str.typeid+'\')">' +  
		        		  
		        		     ' <td>'+branchstr[str.branchid].locateName+'</td> ' + 
		        		     ' <td>'+str.cateoryName+'</td> ' +   
		        		     ' <td>'+str.type+'</td> ' +    
		        		     ' <td>'+str.papercount+'</td> ' +  
		        		     // inventoryid
		        		     ' <td>'+str.realcount+'</td> ' + 
		        		     ' <td>'+pandian+'</td> ' +  
		        		     ' </tr>'; 
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
  <div class="weizhi_head">现在位置：<%=c.getName() %>分布
  
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <select id="counttyepe" name = "counttyepe">
         <option value="-1">全部显示</option>
         <option value=0 >只显示库存不为0</option>
     </select> 
     
     &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  
     
    <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>       
 </div>      
 </div>        
          <div class="table-list" >
        <input type="hidden" id="time"  value=""/>
  <input type="hidden" id="starttime"  value=""/>
  <input type="hidden" id="endtime"  value=""/>
  <table width="100%"  cellspacing="1" id="table" >
    
 
   </table>
 
  </div>
       
<br/>

<div id="serach"> 


</div>
</body>
</html>
