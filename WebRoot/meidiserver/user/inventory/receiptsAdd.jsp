<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
       
List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 

List<Branch> listbranch = BranchManager.getLocate(); 

List<String> listbranchp = BranchManager.getLocateAll();  
String listall = StringUtill.GetJson(listbranchp); 

Map<String,List<Branch>> map = BranchManager.getLocateMapBranch();  
    
String mapjosn = StringUtill.GetJson(map);
 
HashMap<String,ArrayList<String>> listt = ProductManager.getProductName();

String plist = StringUtill.GetJson(listt);
  
List<String> listallp = ProductManager.getProductlist();
String listallpp = StringUtill.GetJson(listallp);   
   
Map<Integer,Branch> branchmap = BranchManager.getNameMap();
  
String inventoryid = request.getParameter("id");
Inventory inventory = new Inventory() ;
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
    if(inventory.getInstatues() == 0 && inventory.getOutstatues() == 0 && user.getId() == inventory.getUid()){
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

<script type="text/javascript">
var disable = '<%=isdisabel %>';

 var jsonmap = '<%=mapjosn%>'; 
 var availableTags = '<%=plist%>';
 var jsons =  $.parseJSON(availableTags);
 
 var listallp = '<%=listallpp%>';
//alert(listallp);
 var jsonallp =  $.parseJSON(listallp); 
 
 var listall = '<%=listall%>';
  
 //alert(listall);
 var jsonall =  $.parseJSON(listall);
 
 var row = 1; 
 var rows = new Array();
  
 var inventoyr = '<%=invent%>';
 //alert(inventoyr); 
 var jsoninvent =  $.parseJSON(inventoyr);
  
 var ctypes = new Array();  
 
 
 $(function () { 
	 initproduct();
	 
	 $("#outbranchs").change(function(){
		 var str = $("#outbranchs").find("option:selected").text();
		 $("#outbranch").val(str);
	 });
	         
	 $("#inbranchs").change(function(){
		 var str = $("#inbranchs").find("option:selected").text();
		 $("#inbranch").val(str);
	 });
	 
	 
	 $("#outbranch").autocomplete({ 
		 source: jsonall
	    });  
	 $("#inbranch").autocomplete({ 
		 source: jsonall
	    });  
	    
	 $("#ordertype0").autocomplete({ 
		 source: jsonallp
	    });  
	 
 });
 
 function initproduct(){ 
	// alert(jsoninvent);
	 if(jsoninvent != null && jsoninvent != "" ){
		 for(var i=0;i<jsoninvent.length;i++){
			 
			 var json = jsoninvent[i];
			 //alert(json);
			 rows.push(row);
			 var str = '';   
			 str += '<tr id="record'+row+'" class="asc">' +  
			     ' <td>'+json.productId+'<input type="hidden" name="orderproductType'+row+'" value="'+json.productId+'"/><input type="hidden" name="product" value="'+row+'"/></td> ' +
			     ' <td><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'" value="'+json.count+'" style="width:50%" '+disable+'/></td> ' +
			     ' <td><input type="button" value="删除" onclick="delet(record'+row+','+row+')" '+disable+'/></td> ' +  
			     ' </tr>';  
			$("#table").append(str);      
			row++;    
			  
		 }
	 }
 }
 //function initproductSerch(str,str2){ 
	 //   cid = $(str).val();
	   
		//$(str2).autocomplete({ 
		//	 source: jsons[cid]
		//    }); 
		//$(str).change(function(){
		//	$(str2).val("");
		//	cid = $(str).val(); 
		//	$(str2).autocomplete({
		//		 source: jsons[cid]
		//	    }); 
		//	}) ; 
   // } 
 
 function add(){
	 var ctype = $("#ordertype0").val();
	 if(ctype == ""){
		 alert("型号不能为空");
		 return false ;
	 }else {
		 if($.inArray(ctype,ctypes) != -1){
			 alert("您已添加此型号"); 
			 return ;
		 }else{
			 ctypes.push(ctype); 
		 }
	 }
	 
	 rows.push(row);
	 var count = 1;  
	
	 var str = '';  
	 str += '<tr id="record'+row+'" class="asc">' +  
	     ' <td>'+ctype+'<input type="hidden" name="orderproductType'+row+'" value="'+ctype+'"/><input type="hidden" name="product" value="'+row+'"/></td> ' +
	     ' <td><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'" value="'+count+'" style="width:50%" /></td> ' +
	     ' <td><input type="button" value="删除" onclick="delet(record'+row+','+row+')"/></td> ' +  
	     ' </tr>'; 
	$("#table").append(str);      
	row++;   
	$("#ordertype0").val(""); 
 }
  
 function delet(str,str2){ 
	rows.splice($.inArray(str2,rows),1);
	$(str).remove();
 }
 
 function check(){
	var branch = $("#outbranch").val();
	
	if(branch == ""){ 
		alert("请选择仓库"); 
		return false ;
	}
	 
	 
	if(rows.length <1){
		alert("没有记录可以提交"); 
		return false ;
	}
	 
	for(var i=0;i<rows.length;i++){
		var count = $("#orderproductNum"+rows[i]).val();
		if(count <=0){
			alert("产品数量不能小于1");
			return false ;
		}
	} 
	
	 $("#submit").css("display","none");
	 
	 return true ;
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
   <div class="weizhi_head">现在位置：库存单据管理页</div> 
   <div class="main_r_tianjia">
   <ul>                                                                                                          
     <li><a href="receipts.jsp">返回</a></li>
      <% 
      //System.out.println("aa"+user.getBranch()+inventory.getOutstatues()+UserManager.checkPermissions(user, Group.inventoryquery));
      if(outbranch.equals(user.getBranch()) && inventory.getOutstatues() == 0 && UserManager.checkPermissions(user, Group.inventoryquery)){ 
      %>  
      <li><a href="../admin/InventoryServlet?method=outbranch&id=<%=inventory.getId() %>">出库方确认</a></li>
      <%
      }  
      %>
     <% if(inbranch.equals(user.getBranch()) && inventory.getInstatues() == 0 && UserManager.checkPermissions(user, Group.inventoryquery)){ 
      %>  
      <li><a href="../admin/InventoryServlet?method=inbranch&id=<%=inventory.getId() %>">入库方确认</a></li>
      <% 
      } 
     
      if(inventory.getInstatues() == 1 && inventory.getOutstatues() == 1){
      %> 
      <li><a href="print.jsp?id=<%=inventoryid%>">打印</a></li> 
      <% 
      }
      %>
      
     </ul>   
   </div>      
     <div>     
     <form action="InventoryServlet"  method = "post"  onsubmit="return check()">
      <input type="hidden" name="method" value="add"/>  
      <input type="hidden" name="id" value="<%=inventoryid %>"/>
                     
  <div  > 
   <center><div id="branchmessage"><font style="color:red;font-size:20px;" >调拨单</font></div></center>
   <br/>
                 出库单位：  
         输入<input type="text" name="outbranch" id="outbranch" class="cba" value="<%=outbranch %>"  <%=isdisabel %>/>                    
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    选择 <select id="outbranchs" <%=isdisabel %>>  
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
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;          
                 入库单位： 
             输入<input type="text" name="inbranch" id="inbranch" value="<%=inbranch %>" class="cba"  <%=isdisabel %>/>
    选择 <select id="inbranchs" <%=isdisabel %>>  
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
  <br/>
     备&nbsp;&nbsp;&nbsp;&nbsp;注：    
       <textarea  id="remark" name="remark"  <%=isdisabel %> ><%=remark %></textarea>
  <br/>
  <br/>
     增加型号:           
           
  <input type="text" name="ordertype0"  id="ordertype0"   class="cba" <%=isdisabel %>/>          
  <input type="button" name="" value="+" onclick="add()" <%=isdisabel %>/>        
            
    <br/>    
     <br/>        
   <table width="100%" border="1" cellpadding="0" cellspacing="0" id="table">
     <tr class="asc"> 
    
      <td>产品型号</td> 
      <td>数量</td> 
      <td>删除</td> 
     
     </tr>
 
   </table>
   
  <% 
	  if(inventory.getInstatues() == 0 && inventory.getOutstatues() == 0 ){
   %>
   <input type="submit" id="submit" value="确认提 交" <%=isdisabel %>/>
  <%  
	  } 
  %> 

  &nbsp;&nbsp;&nbsp;&nbsp;合计
  
  </div>
       
    
     
   
 </form>
     </div>

</div>


</body>
</html>
