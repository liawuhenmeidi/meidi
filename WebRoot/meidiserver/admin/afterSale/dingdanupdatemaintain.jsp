<%@ page language="java"  pageEncoding="UTF-8" import="aftersale.*" contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>  
<%  
List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend); //UserService.getsencondDealsend(user);
     
List<Category> listall = CategoryService.getList();
String clistall = StringUtill.GetJson(listall);
 
List<Category> list = CategoryManager.getCategory(user,Category.sale); 
String clist = StringUtill.GetJson(list);
 
List<Category> listmaintain = CategoryService.getlistmaintain(); 
String clistmaintain = StringUtill.GetJson(listmaintain);
 
HashMap<String,ArrayList<String>> listt = ProductService.gettypeName();
 
String plist = StringUtill.GetJson(listt);

String id = request.getParameter("id");
String statues = request.getParameter("statues"); 
//System.out.println(id);
AfterSale af = null ; 
String strorder= null;    
String listap = null;   
AftersaleAll asf = null;
String matainids = "";
if(!StringUtill.isNull(id)){ 
	asf =  AftersaleAllManager.getAfterSaleID(user, id);
	af = asf.getAs(); 
	Product p = ProductService.gettypemap().get(af.gettName());
	if(null != p){
		matainids = p.getMatainids();
	}
	//af = AfterSaleManager.getAfterSaleID(user,id);
	strorder = StringUtill.GetJson(af);  
	List<AfterSaleProduct> listasp  = asf.getAsplist();
	//List<AfterSaleProduct> listasp  = AfterSaleProductManager.getmaintain(af.getId(),AfterSaleProduct.pending+"");
	listap = StringUtill.GetJson(listasp);  
}    

%> 
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>报装单提交页面</title>
 <link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script> 
 
<link rel="stylesheet" href="../../css/jquery-ui.css">
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<style type="text/css"> 

#wrap{
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
}

#table td{
   text-align: center;
}

</style>  
<script type="text/javascript">
  
   var jsons = <%=plist%> ;
   var cid = "";  
   var ids = "";  
   var json = <%=clist%>;
   var jsonmaintain = <%=clistmaintain%>;
   var listop =  null;  
   // statues 表示是否是相同顾客报装
   
   var order = <%=strorder%>;

   var row = 1;  
   var rows = new Array();
   var listap = <%=listap%>; 
   
   $(document).ready(function(){
	//initphone();
	init();   
	initproductSerch("#ordercategory","#ordertype");
   });
   
   
   function init(){
		   
	   if(order != null && order != "" && order != "null"){
		   $("#uname").val(order.uname);
		   $("#phone").val(order.phone); 

		   $("select[id='ordercategory'] option[id='"+order.cid+"']").attr("selected","selected");
		   
		   $("#ordercategory").attr("disabled","disabled");
		   $("#hiddenordercategory").val(order.cid);  
		   $("#hiddenordercategory").attr("name","ordercategory");
		   
		   $("#ordertype").val(order.tName);
		   $("#ordertype").attr("readonly","readonly");
		    
		   $("#orderbatchNumber").val(order.batchNumber);
		   $("#orderbarcode").val(order.barcode);
		
		  
		   $("#andate").val(order.andate); 
		  // $("#nexttime").val(order.nexttime);   
		   $("#nexttime").val(order.nexttime); 
		     
		   $("#yuyueandate").val(order.nexttime); 
		   $("#saledate").val(order.saledate); 
		   $("#locations").val(order.location);
		   $("#remark").text(order.detail);

	   } 
	   if(null != listap){ 
		  // var time = "";
		   var thistime = "";
		   for(var i=0;i<listap.length;i++){
			   time = listap[i].nexttime;
			   thistime = listap[i].thistime;
			   
               addrowinit(listap[i]);   
		   }   
		  // $("#nexttime").val(time);
		   if(thistime != "" && thistime != null){
			  // $("#andate").val(thistime);  
		   }
		   $("#thistime").val(thistime); 
		   
	   }
	  
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

   function addrowinit(listo){
		//alert(JSON.parse(listo));
	    rows.push(row);
		//var yellow = "";
		//if(row%2 == 0){
		//	yellow = "#c4e1e1"; 
		//} 
		  
	    var str =  '<tr class="dsc">' +
	           
	           '<td>'+listo.cause+'</td>'+
	           ' <td>'+listo.tname+'</td><td>';     
		if(listo.type == 2){
			
            for(var i=0;i<jsonmaintain.length;i++){
         	   //var ckeck = ""; 
         	   var jo = jsonmaintain[i];  
         	   if(jo.id == listo.categoryId){
         		   str += jo.name;
         	   }
            } 
		 }else if(listo.type == 1 ||listo.type == 0 || listo.type == 3){
			 str += listo.dealresult;  
		
		 }
		 
		str += ' </td>'+
		         '<td  >'+listo.typeStr+'</td> ' +
                ' <td  >'+listo.dealName+'</td> ' +
                ' <td  >'+listo.dealsendName+'</td> ' +
                ' <td  >'+listo.dealtime+'</td> ' +
                ' <td  >'+listo.resultStr+'</td> ' +
       	   '</tr>';  
                 
     $("#tableproductinit").append(str); 
 
	 } 
  
   function addrow(listo){
		//alert(JSON.parse(listo));
	    rows.push(row);
		//var yellow = "";
		//if(row%2 == 0){
		//	yellow = "#c4e1e1"; 
		//} 
		     
		var str =  '<tr id=produc'+row+ '>' +
	               '<td>维修类别<span style="color:red">*</span></td>'+
	               '<td ><input type="hidden" name="product" value="'+row+'"/>'+
	               '<select class = "category" name="ordercategory'+row+'"  id="ordercategory'+row+'"  style="width:95% ">'; 
	               for(var i=0;i<jsonmaintain.length;i++){
	            	 //  var ckeck = "";
	            	   var jo = jsonmaintain[i];  
	            	   if(jo.id == listo.categoryId){
	            		   str += '<option value='+jo.id+'  selected="selected" >'+jo.name+'</option>';
	            	   }else {
	            		   str += '<option value='+jo.id+'>'+jo.name+'</option>'; 
	            	   }
 
	               }  
	               
	               var type = listo.tname;
	               if(listo.tname == null || listo.tname =="" || listo.tname==undefined ||listo.tname=="undefined" ){
	            	  type="";  
	               } 
	               
	               str += '</select> '+
	               ' </td>'+ 
	               ' <td >维修配件<span style="color:red">*</span></td> '+
	               ' <td  ><input type="text"  id="ordertype'+row+'" name="ordertype'+row+'" value="'+type+'" style="width:90% " /></td> ' +
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
	 
   
 function checkedd(type){
      
	 var uname = $("#uname").val();
	 var phone = $("#phone").val();
	 //var juese = $("#ordercategory").val();
	 //var str = $("#ordertype").val();
	 
	 
	 //var orderbatchNumber = $("#orderbatchNumber").val();
	// var orderbarcode = $("#orderbarcode").val();
	// var andate = $("#andate").val();
	// var saledate = $("#saledate").val();
	  
	 var locations = $("#locations").val();
	 var thistime = $("#thistime").val();  
	 var nexttime = $("#nexttime").val(); 
	 var uid = $("#uid").val(); 
	 
	 if(uname == "" || uname == null || uname == "null"){
		 alert("顾客姓名不能为空");
		 return false;
	 }
	 
	 if(phone == "" || phone == null || phone == "null"){
		 alert("电话不能为空");
		 return false;
	 }
	 else {
	    var filter = /^1[3|4|5|7|8][0-9]\d{8}$/;  
	    var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		 if(!filter.test(phone) && !isPhone.test(phone)){
			 alert("请填写正确的手机号码或电话");     
			 return false;  
		 }
	 }  
	 
	 /*
	 if(str == "" || str == null || str == "null"){
		 alert("产品型号不能为空");
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
	  
	
	 if(orderbatchNumber == "" || orderbatchNumber == null || orderbatchNumber == "null"){
		 alert("批号不能为空");
		 return false;
	 }
     
    
	 if(orderbarcode == "" || orderbarcode == null || orderbarcode == "null"){
		 alert("条码不能为空");
		 return false;
	 }
	 
     if(andate == "" || andate == null || andate == "null"){
		 alert("安装日期不能为空");
		 return false;
	 }
	 
     if(saledate == "" || saledate == null || saledate == "null"){
		 alert("购买日期不能为空");
		 return false;
	 }
     */ 
	 if(locations == "" || locations == null || locations == "null"){
		 alert("详细地址不能为空");
		 return false;
	 } 
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
		 }
	}
	  
	*/
	if( 1 == type){
		 if(thistime == "" || thistime == null || thistime == "null"){
			 alert("保养时间不能为空");
			 return false;
		 } 
		 
		 if(uid == "" || uid == null || uid == "null"){
			 alert("维修单位不能为空");
			 return false;
		 }  
		 if(nexttime == "" || nexttime == null || nexttime == "null"){
			 alert("下次保养时间不能为空");
			 return false;
		 }  
		 
	}else  if( 0 == type){
		 $("#thistime").val("");  
		 $("#uid").val("");  
		 if(nexttime == "" || nexttime == null || nexttime == "null"){
			 alert("下次保养时间不能为空");
			 return false;
		 }  
	 } 
     
	 $("#submit").css("display","none"); 
	 $("form").submit();
	 window.opener.location.reload();
	  
 }
 
</script>

 
</head>
<body>

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
  </div>

<div style="height:70px;">
</div> 
  
<form action="../../user/OrderServlet"  method ="post"  id="form"    > 
<!--  头 单种类  -->   
  
<input type="hidden" name="orderid" value="<%=id %>"/> 
<input type="hidden" name="method" value="aftersale"/> 
<input type="hidden" name="typemethod" value="maintain"/> 
<input type="hidden" name="token" value="<%=token%>"/>  
<input type="hidden" name="statues" value="<%=statues%>"/>  
<input type="hidden" name="printid" value="<%=af.getPrintid()%>"/> 
  
<div class="s_main_tit">上报单位:<span class="qian"><%=BranchService.getMap().get(Integer.valueOf(user.getBranch())).getLocateName() %></span></div>  
<!--  订单详情  -->   
  
 <div id="wrap">  
  <table  cellspacing="1" id="table" style="width:90%">
  <tr class="asc">
    <td  >顾客姓名<span style="color:red">*</span></td>
    <td ><input type="text" id="uname" name="uname" value=""  placeholder="必填" ></input></td>
    
    <td >电话<span style="color:red">*</span></td>
    <td ><input type="text"  id="phone"  name="phone" value=""   placeholder="必填" /></td>

   </tr> 		
  <tr class="asc">
  <td  >产品类别<span style="color:red">*</span></td>
  <td  >
  <input type="hidden" name="hiddenordercategory" id="hiddenordercategory">
  <select  name="ordercategory" id="ordercategory"  >
  
   <option></option>
  <% 
  for(int i=0;i< listall.size();i++){ 
	  Category cate =  listall.get(i);
	 
  %>   
    
    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getName()%></option>
  <%
  }
  %>
  
  </select>   
  </td>
   <td  >产品型号<span style="color:red">*</span></td> 
   <td  > 
   <input type="text" name="ordertype" id="ordertype"   placeholder="必填" /> 
    </td> 
    
   </tr>    

   <tr class="asc">
   <td>批号</td>  
   <td><input type="text" name="orderbatchNumber" id="orderbatchNumber"    /> </td>
   <td>条码</td> 
   <td><input type="text" name="orderbarcode" id="orderbarcode"    /> </td> 
 
   </tr> 
    <tr class="asc"> 
      
    <td  >购买日期</td>
    <td  ><input class="date2" type="text" name="saledate" id ="saledate" onclick="new Calendar().show(this);"   readonly="readonly" ></input>   </td>
     
    <td  >安装日期</td> 
    <td  ><input class="date2" type="text" name="andate" id ="andate" onclick="new Calendar().show(this);"  readonly="readonly" ></input>   </td>
   
 </tr>
   <tr class="asc"> 
    <td >详细地址<span style="color:red">*</span></td>
    <td ><textarea  id="locations" name="locations" ></textarea></td>  
    <td >故障保养内容<span style="color:red">*</span></td>
    <td ><input type="hidden" value=""><textarea  id="fault" name="fault" ></textarea></td> 
    
   </tr> 
   <tr class="asc">
     <td colspan=4> 
     <table id="tableproductinit"  style="width:100%">  
      <tr class="dsc">
		         <td>维修保养内容</td>
		          <td>维修配件</td>
		         <td>维修保养结果</td>
			    <td >单据类型</td>
                <td >网点</td> 
                 <td >人员</td> 
                <td >维修保养完成时间</td> 
                <td >状态</td> 
	            </tr>  
      
      
     </table>
     </td>
     
    
   </tr> 
    <tr class="asc">
     <td colspan=4> 
     <table id="tableproduct"  style="width:100%">  
      
      
      
     </table>
     </td>
     
    
   </tr>  
       
   <tr class="asc">
     <td colspan=4> 
     <table   style="width:100%">
        <tr class="asc_enable">
          <td>更换配件名称</td>
          <td>价格</td>
          <td>更换周期</td>
          <td>最近更换时间</td>
          <td>下次更换时间</td>
        </tr>  
         
        <% if(!StringUtill.isNull(matainids)){
        	List<String> l = new ArrayList<String>();
              // System.out.println(l);
        	String matainidss[] = matainids.split(",");
        	String html = "";
        	for(int i=0;i<matainidss.length;i++){
        		String opid = matainidss[i]; 
        		String name = ProductService.getIDmap().get(Integer.valueOf(opid)).getType();
        		int time = ProductService.getIDmap().get(Integer.valueOf(opid)).getMataintime();
        		double price = ProductService.getIDmap().get(Integer.valueOf(opid)).getStockprice();
        		String lasttime = AftersaleAllManager.getlasttime(asf, Integer.valueOf(opid));
        		String nexttime = TimeUtill.dataAdd(lasttime, time);
        		
        		if(l.size()>0){
        			boolean flag = true ;
        			for(int j=0;j<l.size();j++){
        				String str = l.get(j);
        				String[] strs = str.split("_");
        				if(TimeUtill.compare(strs[5], nexttime)){
        					l.add(j,opid+"_"+name+"_"+time+"_"+price+"_"+lasttime+"_"+nexttime);
        					flag = false ;
        					break ;
        				}else {
        					continue; 
        				}
        			}
        			if(flag){
        				l.add(opid+"_"+name+"_"+time+"_"+price+"_"+lasttime+"_"+nexttime);
        			}
        		}else {
        			l.add(opid+"_"+name+"_"+time+"_"+price+"_"+lasttime+"_"+nexttime);
        		}
        	}
        	//String nexttime = "";
        	for(int i=0;i<l.size();i++){
        		String str = l.get(i);
        		String[] strs = str.split("_");
        		String opid = strs[0];
        		String name = strs[1];
        		String time = strs[2]; 
        		String price = strs[3]; 
        		String lasttime =strs[4];
        		String nexttime = strs[5];
        		%>
        		<tr class="asc_enable">  
        		<td><%=name %></td>
        		<td><%=price  %></td>
        		<td><%=time %></td>
        		<td><%=lasttime %></td>
        		<td><%=nexttime %></td>
        		</tr>
        		
        		
        		
        		<%
        	}
        	
        }
      %>
      
     </table>
     </td>
     
    
   </tr>  
     
     <tr class="dsc" > 
    <td colspan=4>
     <input type="button"  name="" style="color:red" value="增加保养项目" onclick="addrow(0)" width="100%"  />
    </td> 
   </tr>  
   
 <tr class="asc"> 

    <td  >保养单位<span style="color:red">*</span></td>
    <td  >
    <select  name="uid" id="uid"  >
  
   <option></option>
  <%  
  for(int i=0;i<listS.size();i++){
	  User cate = listS.get(i);
	 
  %>  
    
    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getUsername()%></option>
  <%
  }
  %>
   
  </select> 
    
       </td>
   
     <td  >保养时间<span style="color:red">*</span></td>
    <td  ><input  type="text" name="thistime" id ="thistime" onclick="new Calendar().show(this)"   placeholder="必填"  readonly="readonly" ></input>   </td>
  </tr> 
  <tr class="asc">
    <td  >下次保养时间<span style="color:red">*</span></td>
  
    <td  ><input  type="text" name="nexttime" id ="nexttime" onclick="new Calendar().show(this)"    readonly="readonly" ></input>   </td>
      <!--  
    <td  ><label id="nexttime" ></label>  </td>-->
  <td >备注</td>
    <td ><textarea  id="remark" name="remark" ></textarea></td>
 </tr>
 <% if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){ %>
   <tr class="asc"> 
    <td colspan="2" style="background-color:orange" class="center"><input type="button" onclick="checkedd(0)"  value="修改" /></td>
   <td colspan="2" style="background-color:orange" class="center"><input type="button"  onclick="checkedd(1)"  value="配工" /></td>
   </tr> 
   <%} %>
   </table>
   </div>
 <div class="center"> 
 </div>
</form>


</body>
</html>