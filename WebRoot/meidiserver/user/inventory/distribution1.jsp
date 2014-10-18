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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>





<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script type="text/javascript">
 
 var row = 1; 
 var rows = new Array();
 var branchstr = <%=branchstr%>; 

  
 $(function () { 
	 add();
 });
 
 function search(category,branchid){
	 window.location.href='inventory1.jsp?category='+category+'&branchid='+branchid;
	 //window.open('inventory1.jsp?category='+category+'&branchid='+branchid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no'); 
}
  
 function add(){    
	 $("#table tr").remove();
	 var category = "<%=category%>";
	 var type = "<%=type%>";

	 $.ajax({ 
	        type: "post",  
	         url: "../../admin/server.jsp",     
	         data:"method=inventorydis&category="+category+"&type="+type,
	         dataType: "",   
	         success: function (data) { 
	        	 var addstr = '<tr class="asc"> '+
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
	        		 
	        		 addstr += '<tr id="record'+row+'" class="asc" onclick="search(\''+str.categoryid+'\',\''+str.branchid+'\')">' +  
	        		  
	        		     ' <td>'+branchstr[str.branchid].locateName+'</td> ' + 
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
<!--   头部结束   -->
<div class="main">   
  <div class="weizhi_head">现在位置：<%=c.getName() %>分布
  
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
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
      <td>盘点</td>   
     </tr>
 
   </table>
 
  </div>
       
<br/>

<div id="serach"> 


</div>
</body>
</html>
