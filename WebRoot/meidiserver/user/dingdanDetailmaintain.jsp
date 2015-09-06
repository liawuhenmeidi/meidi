<%@ page language="java"  import="java.util.*,aftersale.*,category.*,group.*,user.*,utill.*,product.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
String id = request.getParameter("id");
String statues = request.getParameter("statues");
User user = (User)session.getAttribute("user");

List<Category> listmaintain = CategoryService.getlistmaintain(); 
String clistmaintain = StringUtill.GetJson(listmaintain);

HashMap<String,ArrayList<String>> listt = ProductService.gettypeName();

String plist = StringUtill.GetJson(listt);

AfterSale af = null ;  
String strorder= null;    
String listap = null;  
List<AfterSaleProduct> listasp = null ;

if(!StringUtill.isNull(id)){   
	af = AfterSaleManager.getAfterSaleID(user,id);
	strorder = StringUtill.GetJson(af); 
	listasp  = AfterSaleProductManager.getmaintain(af.getId(),statues);
	listap = StringUtill.GetJson(listasp); 
} 
  
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单详细页</title>
 
<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 


<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<link rel="stylesheet" href="../css/jquery-ui.css">
<script type="text/javascript" src="../js/jquery-ui.js"></script>

<script type="text/javascript"> 
var jsons = <%=plist%> ;
var id = "<%=id%>";
var row = 1;  
var rows = new Array();
var jsonmaintain = <%=clistmaintain%>;

function change(statues,oid,type){ 
	
	var statue = $("#"+statues).val(); 
	if(statues == -1){
		return false;
	}  
    var barcode = $("#barcode").val(); 
    var batchNumber = $("#batchNumber").val(); 
    var dealresult = $("#dealresult").val(); 
	var message = "";
	//var x = 0 ;
	//alert(rows);
	if(statue == 1){
		/*
		if(rows.length <1){
			alert("请添加保养项目");
			return false ; 
		 }else {
			 for(var i=0;i<rows.length;i++){
				 var str = $("#ordertype"+rows[i]).val();
				 var juese = $("#ordercategory"+rows[i]).val();
				
				 if(str == "" || str == null || str == "null"){
					 alert("送货型号不能为空");
					 return false; 
				 }else {
					 var flag = true ;
			         	  var array = jsons[juese];
			         	  //alert(array);
			         	  if(array != "" && array != null &&  array != "null" && array != undefined && array != "undefined"){
				         	   for(var k=0;k<array.length;k++){

				         		   if(str == array[k]){
				         			   flag = false  ;
				         		   }
				         	   }
				         	   }
					 if(flag){
						 alert("系统不存在此型号"+str);
						 return false ;
					 }
				 } 

			message +=","+juese+"_"+str; 
		 }
	} 
		*/
	}

	 
	message = message.substring(1, message.length);
	///alert(message); 
	//return ;
	$.ajax({     
        type: "post",       
         url: "../AfterSaleServlet", 
         data:"method="+type+"&afid="+oid+"&statues="+statue+"&message="+message+"&dealresult="+dealresult+"&batchNumber="+batchNumber+"&barcode="+barcode,       
         dataType: "",    
         success: function (date) { 
        	//alert(date); 
        	 if(date == 1){
        		 alert("设置成功"); 
    	         window.location.href="maintain.jsp";
        		 return ; 
        	 }
           
           
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}


function addrow(listo){
	
    rows.push(row); 
	var str =  '<tr id=produc'+row+ '>' +
               '<td>维修类别<span style="color:red">*</span></td>'+
               '<td ><input type="hidden" name="product" value="'+row+'"/>'+
               '<select class = "category" name="ordercategory'+row+'"  id="ordercategory'+row+'"  style="width:95% ">'; 
                
               str += '<option>    </option>';   
               for(var i=0;i<jsonmaintain.length;i++){
            	   var jo = jsonmaintain[i];  
             	   if(jo.id == listo.categoryId){
            		   str += '<option value='+jo.id+'  selected="selected" >'+jo.name+'</option>';
            	   }else { 
            		   str += '<option value='+jo.id+'>'+jo.name+'</option>'; 
            	   }

               }
               str += '</select> '+
               ' </td>'+ 
               ' <td >送货型号<span style="color:red">*</span></td> '+
               ' <td  ><input type="text"  id="ordertype'+row+'" name="ordertype'+row+'" value="" style="width:90% " /></td> ' +
          	   ' <td  ><input type="button"   style="color:white;background-color:#0080FF" name="" value="删除" onclick="deletes(produc'+row+','+row+')"/></td>'+
          	   '</tr>';
                  
        $("#tableproduct").append(str); 
        initproductSerch("#ordercategory"+row,"#ordertype"+row);
        row++;  
        } 
 
 function deletes(str,str2){
 	$(str).empty();
	    rows.splice($.inArray(str2,rows),1);
  } 
 
 function initproductSerch(str,str2){ 
	    cid = $(str).val(); 
		$(str2).autocomplete({ 
			 source: jsons[cid]
		    }); 
		$(str).change(function(){
			$(str2).val("");
			cid = $(str).val();  
			$(str2).autocomplete({
				 source: jsons[cid]
			    });
			}) ;
    } 
</script>
</head>

<body>
<div class="s_main">
   <jsp:include flush="true" page="../head.jsp">
     <jsp:param name="dmsn" value="" />
  </jsp:include> 
  </div>
  <div> 
<span class="qiangdan"><a href="welcom.jsp">返回</a></span>
</div>

<!--  头 单种类  -->

<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">

  <tr>
    <td width="25%" class="s_list_m">单号</td>
    <td class="s_list_m"><%=af.getPrintid() == null?"":af.getPrintid()%></td>
    
  </tr> 
  
  <tr >
    <td width="25%" class="s_list_m">产品型号</td>
    <td class="s_list_m"><%= af.getcName()%></td>
   
  </tr>
 
 <tr  >
    <td width="25%" class="s_list_m">产品类别</td>
    <td class="s_list_m"><%=	af.gettName()%></td>
   
  </tr>
   
   <tr  >
    <td width="25%" class="s_list_m">购买日期</td>
    <td class="s_list_m"><%=af.getSaledate()%></td>
   
  </tr>
  <tr  > 
    <td width="25%" class="s_list_m">安装日期</td>
    <td class="s_list_m"><%=af.getAndate()%></td>
   
  </tr>
  
  <%
  String time = "";   
  int stas = -1 ;
  String cause  = "";
  
  for(int g = 0 ;g<listasp.size();g++){
	  AfterSaleProduct op = listasp.get(g);
	  
	   time = op.getThistime();
	   stas = op.getStatues(); 
	   cause = op.getCause();
	   if(!StringUtill.isNull(op.getCname()) && !StringUtill.isNull(op.getTname())){
    	 %> 
    	  <tr style="background:orange">
     <td width="55%" class="s_list_m">保养型号</td>
     <td class="s_list_m">
		    
		    	<%=  op.getTname()%>
      </td>
      </tr>
    	 
    	
    <%  
	   }
   }
  
  %> 
    
	 
	 <tr> 
    <td width="55%" class="s_list_m">条码</td>
    <td class="s_list_m"> 
    <input type="text" id="barcode" name="barcode" value="<%=af.getBarcode() %>"></input></td>
  </tr> 
  <tr> 
    <td width="55%" class="s_list_m">批号</td>
    <td class="s_list_m"><input type="text" id="batchNumber" name="batchNumber"  value="<%=af.getBatchNumber() %> "></input></td>
  </tr>
 
    
  
  <tr >
    <td class="s_list_m">电话</td> 
     
    <td><a href="tel:<%=af.getPhone() %>"><%=af.getPhone() %></a></td>
    <!--  一键拨号  -->
  </tr>
   
    <tr >
    <td class="s_list_m">预约日期</td>
    <td><%=time %></td>
    
  </tr>
  
  <tr>
    <td class="s_list_m">送货地点</td>
    <td class="s_list_m"><%=af.getLocation() %></td>
  </tr>
  
  <tr>
    <td class="s_list_m">维修保养内容</td>
    <td class="s_list_m">   
       <%=cause%>
  </tr>
  <tr>
    <td class="s_list_m">维修保养结果</td>
    <td class="s_list_m">   
    <textarea id="dealresult" name="dealresult" ></textarea></td> 
  </tr>
  <tr >
     <td class="s_list_m" colspan=2 align="center">  
     <table id="tableproduct"  style="width:100%">  
      
      
      
     </table>
     </td>
     
    
   </tr> 
      
     <tr  > 
      
    <td class="s_list_m">操作</td>
    <td  class="s_list_m">
     <input type="button"  name="" style="color:red" value="增加维修配件" onclick="addrow(0)" width="100%"  />
    </td>
    
   </tr>   
   
   <tr>
    <td class="s_list_m"> 状态</td>
    <td class="s_list_m">
    <%
   
    if(stas == 0 ){
 
    %>
     <select class = "category" name="category"  id="songh<%=af.getId() %>" >
     
      <option value="" >&nbsp;&nbsp;&nbsp;&nbsp;</option> 
      <option value="1" >已处理 </option>  
      <option value="2" >驳回 </option> 
      </select>    
     <input type="button" onclick="change('songh<%=af.getId() %>','<%=af.getId() %>','maintain')"  value="确定"/>

   
   <%
    }
		%>
     
    </td>
 
   <tr >
    <td class="s_list_m">备注</td>
    <td align="left" class="s_list_m"><%=af.getDetail() %></td>
    
  </tr>  
     
</table>

  <br>
  <br> 
 
<!--  zong end  -->
</div>

</body>
</html>