<%@ page language="java" import="java.util.*,utill.*,company.*,httpClient.download.InventoryChange,httpClient.download.InventoryModelDownLoad,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
String product = request.getParameter("product");  
String[] typestatue = request.getParameterValues("typestatues");
String typestatuesSN = request.getParameter("typestatuesSN");
   
String typestatues = "";
if(null == typestatue){
	if( StringUtill.isNull(typestatuesSN)){
		typestatues = "1,2,3"; 
	}else {
		typestatues = "-2"; 
	}
	
}else {  
	typestatues = StringUtill.getStr(typestatue, ",");
}  

List<String> listallp = ProductService.getlistall(user);
//System.out.println("qa"+(System.currentTimeMillis() - start));  
String listallpp = StringUtill.GetJson(listallp);
              
String branchid = user.getBranch();         
Map<String,List<InventoryBranch>> map= InventoryAllManager.getMapTypePandian(user,branchid,
			"", product,"isSN",typestatues,typestatuesSN); 
          
System.out.println("c.size()"+map.size()) ;  
%>   
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css"/>  
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript">
 
var row = 1; 
var rows = new Array();
var winPar = null;
var typeid = "";  
var jsonallp = <%=listallpp%>;  
var branchid = "<%=branchid%>";
var typestatues = "<%=typestatues%>";
var typestatuesSn = "<%=typestatuesSN%>";     
$(function () {  
	// alert(jsonallp);
	 $("#product").autocomplete({  
		 source: jsonallp
	    }); 
	 
	 var ss =typestatues.split(",");
	 for(var i=0;i<ss.length;i++){
		 var num = ss[i];
		 $("#typestatues"+num).attr("checked","checked");
	 }  
	 $("#typestatues"+typestatuesSn).attr("checked","checked");
});  
 
function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
}
 
function search(ctype,branchid){ 
		 if('<%=user.getBranch()%>' != branchid ){ 
			 alert("对不起，您不能查看");
		 }else {
			 window.location.href="time.jsp?ctype="+ctype+"&branchid="+branchid;
		 }
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
 
 
function pandian(type,branchid,typestatues){
	$.ajax({   
         type: "post",   
         url: "../../admin/server.jsp",    
         data:"method=pandianSN&branchid="+branchid+"&type="+type+"&typestatues="+typestatues,
         dataType: "",     
         success: function (data) {   
        	 window.location.reload();
            },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
	   
	
}


</script>
</head>

<body>
 <!--       -->    
   <div class="weizhi_head">
 <table>
 <tr>
 <td>
  现在位置：入库单查询
 
 </td>
 <td></td>
 <td>
  供应商编码:<%=Company.supply%>
 </td>
 </tr>
   
 </table>  
   </div>      
    <form action="inventorycheck.jsp">
  <table>
  <tr>
      
  <td>
    <input type="text" name="" id="product" placeholder="产品型号"/> 
  </td> 
     <td rowspan="2">
    <input type="submit" name="" value="查询" /> 
  </td> 
  </tr>
  <tr>    
  <td>产品类别：  
				常规<input type="checkbox" name="typestatues" value="1" id="typestatues1"/> 
				特价<input 
				type="checkbox" name="typestatues" value="2" id="typestatues2"/> 
				样机<input
				type="checkbox" name="typestatues" value="3" id="typestatues3"/>
				入库样机<input 
				type="checkbox" name="typestatuesSN" value="4" id="typestatues4" />
				</td> 
  
  </tr> 
  </table>
    </form> 
<table width="100%"  cellspacing="1" id="Ntable">
	<tr class="asc">    
	     		    
		     			<th align="center">产品型号</th>
		     			<th align="center">产品条码</th>
		     			<th align="center">产品类别</th>
		     			<th align="center">未入库数量</th>
		     			<th align="center">盘点</th>
	     		  </tr>    
  <%   
  if(null != map && !map.isEmpty()){ 
	  Set<Map.Entry<String,List<InventoryBranch>>> set = map.entrySet();
	  Iterator<Map.Entry<String,List<InventoryBranch>>> it = set.iterator();
	  while(it.hasNext()){  
		  Map.Entry<String,List<InventoryBranch>> inmap =  it.next();
		  String type = inmap.getKey();  
		
		  List<InventoryBranch> list = inmap.getValue();
		 
		  int size = list.size();
		  
		  for(int i=0;i<list.size();i++){
			  InventoryBranch in = list.get(i);
			  
			  if(i == 0 ){  
				 %>  
				 <tr class="asc"> 
		  <td align="center" rowspan="<%=size%>"><%=in.getType() %> </td>
		   <td align="center" rowspan="<%=size%>"><%=in.getProduct().getEncoded()%> </td>
		  <td align="center"><%=in.getTypestatuesName() %> </td>
		    <td align="center"><%=in.getPapercount() %> </td>
		 <td align="center">
		 <%
		   if(in.getIsquery() == 0 && !in.getTypeid().equals("-1")){
			  %>
			  <span style="color:red" onclick="pandian('<%=in.getTypeid() %>','<%=user.getBranch()%>','<%=in.getTypeStatues()%>')">盘点确认</span> <%
		   }else {
			   %> 
			   <%=in.getQuerymonth() %>
			   <%
		   }
		 %>
		 
		 </td>
		  </tr> 
				  <% 
			  }else {  
				  %> 
				  <tr class="asc">   
		   
		  <td align="center"><%=in.getTypestatuesName() %> </td> 
		    <td align="center"><%=in.getPapercount() %> </td> 
		    <td align="center">
		    
		    <% 
		   if(in.getIsquery() == 0 && !in.getTypeid().equals("-1")){
			  %>  
			  
			  <span style="color:red" onclick="pandian('<%=in.getTypeid() %>','<%=user.getBranch()%>','<%=in.getTypeStatues()%>')">盘点确认</span>
			  <%
		   }else {
			   %> 
			    <%=in.getQuerymonth() %>
			   <%
		   }
		 %>
		    </td>
		    
		  </tr>
				   
				  <%
				  
			  }
		  %>  
		  <%
		  
		  }
		  
		  
		 
	  }
  }
  
  
  
  
  
  %>
</table>



</body>
</html>
