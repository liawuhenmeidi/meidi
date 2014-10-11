<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
String inventoryid = request.getParameter("id");

List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 
List<Branch> listbranch = BranchService.getList();
 
List<String> listbranchp = BranchManager.getLocateAll();  
String listall = StringUtill.GetJson(listbranchp); 
  
List<String> listallp = ProductManager.getProductlist();
String listallpp = StringUtill.GetJson(listallp);   
   
//Map<Integer,Branch> branchmap = BranchManager.getNameMap();
Map<Integer,Branch> branchmap = BranchService.getMap();

Inventory inventory = new Inventory() ;
String invent = ""; 
Branch outbranch = new Branch();
Branch inbranch = new Branch(); 
String remark = "";  
String inittime = ""; 
String isdisabel = " ";  

if(!StringUtill.isNull(inventoryid)){
	isdisabel = " disabled=\"disabled\" ";
	inventory = InventoryManager.getInventoryID(user, Integer.valueOf(inventoryid));  
	 
	if(branchmap != null){
		   outbranch = branchmap.get(inventory.getOutbranchid());
		   inbranch = branchmap.get(inventory.getInbranchid());
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
  
 var listallp = '<%=listallpp%>';
//alert(listallp);
 var jsonallp =  $.parseJSON(listallp); 
 
 var jsonall = <%=listall%>;

 var row = 1; 
 var rows = new Array();
   
 var jsoninventstr =  '<%=invent%>';
 
 var jsoninvent =  $.parseJSON(jsoninventstr); 
 
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
			     ' <td>'+json.productname+'<input type="hidden" name="orderproductType'+row+'" value="'+json.productname+'"/><input type="hidden" name="product" value="'+row+'"/></td> ' +
			     ' <td><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'" value="'+json.count+'" style="width:50%" '+disable+'/></td> ' +
			     ' <td><input type="button" value="删除" onclick="delet(record'+row+','+row+')" '+disable+'/></td> ' +  
			     ' </tr>';  
			$("#table").append(str);      
			row++;    
			  
		 }
	 }
	  
	 addcount();
 }
 
 function addcount(){
	 var totalcount = 0 ;
	 for(var i=0;i<rows.length;i++){
		 var countrow = $("#orderproductNum"+rows[i]).val();
		 totalcount += countrow*1;
	 }
	 $("#addcount").html(totalcount);
 }
 
 
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
	     ' <td><input type="text"  id="orderproductNum'+row+'" name="orderproductNum'+row+'" value="'+count+'" onBlur="addcount()" style="width:50%" /></td> ' +
	     ' <td><input type="button" value="删除" onclick="delet(record'+row+','+row+')"/></td> ' +  
	     ' </tr>'; 
	$("#table").append(str);      
	row++;   
	$("#ordertype0").val(""); 
	
	 addcount();
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
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main">  
   <div class="weizhi_head">现在位置：库存单据管理页</div> 
   <div class="main_r_tianjia">
   <ul>                                                                                                          
     <li><a href="javascript:history.go(-1);">返回</a></li>
      <%  
      //System.out.println("aa"+user.getBranch()+inventory.getOutstatues()+UserManager.checkPermissions(user, Group.inventoryquery));
      if(user.getBranch().equals(outbranch.getId()+"") && inventory.getOutstatues() == 0 && UserManager.checkPermissions(user, Group.inventoryquery) || outbranch.getStatues() == 1 && UserManager.checkPermissions(user, Group.dealSend) && inventory.getOutstatues() == 0 ){ 
      %>    
      <li><a href="InventoryServlet?method=outbranch&id=<%=inventory.getId() %>">出库方确认</a></li>
      <%
      }  
      %>
     <% if(user.getBranch().equals(inbranch.getId()+"") && inventory.getInstatues() == 0 && UserManager.checkPermissions(user, Group.inventoryquery) || inbranch.getStatues() == 1 && UserManager.checkPermissions(user, Group.dealSend) && inventory.getOutstatues() == 0){ 
      %>   
      <li><a href="InventoryServlet?method=inbranch&id=<%=inventory.getId() %>">入库方确认</a></li>
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
                     
  <div > 
   <center><div id="branchmessage"><font style="color:red;font-size:20px;" >调拨单</font></div></center>
   <br/>
      单号：<%=inventoryid %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期：<%=inventory.getIntime()==null?"":inventory.getIntime() %> <br/>
                 出库单位：  
         输入<input type="text" name="outbranch" id="outbranch" class="cba" value="<%=outbranch.getLocateName()==null?"":outbranch.getLocateName() %>"  <%=isdisabel %>/>                    
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;          
                 入库单位： 
             输入<input type="text" name="inbranch" id="inbranch" value="<%=inbranch.getLocateName()==null?"":inbranch.getLocateName() %>" class="cba"  <%=isdisabel %>/>
 
  <br/>
   
     增加型号:           
           
  <input type="text" name="ordertype0"  id="ordertype0"   class="cba" <%=isdisabel %>/>          
  <input type="button" name="" value="+" onclick="add()" <%=isdisabel %>/>        
            
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      备&nbsp;&nbsp;&nbsp;&nbsp;注：    
       <textarea  id="remark" name="remark"  style="height:18px;width:200px"  <%=isdisabel %> ><%=remark %></textarea>
  
     <br/>        
   <table width="100%" border="1" cellpadding="0" cellspacing="0" id="table">
     <tr class="asc"> 
    
      <td>产品型号</td> 
      <td>数量</td> 
      <td>删除</td> 
     
     </tr>
 
   </table>  
     &nbsp;&nbsp;&nbsp;&nbsp;
    <font style="color:blue;font-size:20px;" >合计:<span style="color:red;font-size:20px;" id="addcount"></span></font>
    <br/>
    
  <% 
	  if(inventory.getInstatues() == 0 && inventory.getOutstatues() == 0 ){
   %>
   <input type="submit" id="submit" value="确认提 交" <%=isdisabel %>/>
  <%  
	  } 
  %> 

  </div>
   
 </form>
     </div>
</div>


</body>
</html>
