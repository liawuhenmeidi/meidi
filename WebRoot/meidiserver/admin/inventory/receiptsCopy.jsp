<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>  

<% 
String message = "调拨单";
String type = request.getParameter("type");

if(!StringUtill.isNull(type)){
	if(Integer.valueOf(type) == 3){  
		message = "调账面库存单据";
	} 
} 


String inventoryid = request.getParameter("id");
 
List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 
List<Branch> listbranch = BranchService.getList();
   
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp); 
//System.out.println(listall);    
List<String> listallp = ProductService.getTypeNameList();
String listallpp = StringUtill.GetJson(listallp);   
//System.out.println(listallpp);
Map<Integer,Branch> branchmap = BranchService.getMap();

Inventory inventory = new Inventory(); 
String invent = "[]"; 
Branch outbranch = new Branch();
Branch inbranch = new Branch(); 
String remark = "";  
String inittime = ""; 
String isdisabel = " ";  
 
//System.out.println(inventoryid);

if(!StringUtill.isNull(inventoryid)){
	inventory = InventoryManager.getInventoryID(user, Integer.valueOf(inventoryid));  
	 
	if(branchmap != null){
		   outbranch = branchmap.get(inventory.getOutbranchid());
		   inbranch = branchmap.get(inventory.getInbranchid());
	   }
	if(inventory.getIntype() == 3){
		message = "调账面库存单据";
	}
	
	type = inventory.getIntype()+"";  
	List<InventoryMessage> list = inventory.getInventory();
	invent = StringUtill.GetJson(list);
  
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
<title>单据管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css"/>
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

 
<script type="text/javascript">

var disable = '<%=isdisabel %>';
    
 var jsonallp =  <%=listallpp%>; 
 
 var jsonall = <%=listall%>;
 
 var row = 1;  
 var rows = new Array();
 
 var jsoninvent =  <%=invent%>; 
  
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
		 if($.inArray(ctype,jsonallp) != -1){
			 if($.inArray(ctype,ctypes) != -1){
				 alert("您已添加此型号"); 
				 return ;
			 }else{
				 ctypes.push(ctype); 
			 } 
		 }else {
			 alert("您输入的产品不存在，请重新输入");
			 return ;
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
 
 function checkedd(buttontype){
	var flag = true ;
	var outbranch = $("#outbranch").val();
	var inbranch = $("#inbranch").val();
	
	 $("#method").val(buttontype);
	 
	if(outbranch == "" || outbranch == null){ 
		alert("请选择出库单位"); 
		flag =  false ;
	}else {
		if($.inArray(outbranch, jsonall) == -1){
			alert("出库单位不存在"); 
			flag =  false ;
		}
	}
	
	if(inbranch == "" || inbranch == null){ 
		alert("请选择入库单位"); 
		flag =  false ;
	}else{
		if($.inArray(inbranch, jsonall) == -1){
			alert("入库单位不存在"); 
			flag =  false ;
		}
	}

	if(rows.length <1){
		alert("没有记录可以提交"); 
		flag =  false ;
	}
	 
	for(var i=0;i<rows.length;i++){
		var count = $("#orderproductNum"+rows[i]).val();
		
		if(isNaN(count)){
			alert("产品数量必须是数字");
			flag =  false ;
	      } 
	}
	
	if(flag){
		$("#form").submit();
		
		 $("#submit").css("display","none");
	}

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
      
      <li><a href="print.jsp?id=<%=inventoryid%>">打印</a></li> 
       
      
     </ul>   
   </div>      
     <div>       
     <form action="InventoryServlet"  method = "post"  id="form"> 
      <input type="hidden" name="method" id="method" value=""/> 
      <input type="hidden" name="type" value="<%=type%>"/>  
      <input type="hidden" name="token" value="<%=token%>"/>  
                        
  <div >   
   <center><div id="branchmessage"><font style="color:red;font-size:20px;" ><%=message %></font></div></center>
   <br/>
     <font style="color:red;font-size:20px;" > 单号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><br/>
     <font style="color:red;font-size:20px;" > 日期：</font> <br/>
                 出库单位：  
    <input type="text" name="outbranch" id="outbranch" class="cba" value="<%=inbranch.getLocateName()==null?"":inbranch.getLocateName() %>"  <%=isdisabel %>/>                    
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;          
                 入库单位： 
     <input type="text" name="inbranch" id="inbranch"  class="cba"  value="<%=outbranch.getLocateName()==null?"":outbranch.getLocateName() %>" <%=isdisabel %>/>
 
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
  if(UserManager.checkPermissions(user, Group.inventory,"w")){
	 
   %> 
   <input type="button" id="button" value="确认提 交"  onclick="checkedd('add')" <%=isdisabel %>/>
   
 
  
  <input type="button" id="button" value="删除"  onclick="checkedd('del')"/> 
  
  <%
   
  }
      
  %>
  </div>
   
 </form>
     </div>
</div>


</body>
</html>
