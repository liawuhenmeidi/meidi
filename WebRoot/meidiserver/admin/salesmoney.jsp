<%@ page language="java" import="java.util.*,message.*,locate.*,installsale.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

String chargetype = request.getParameter("chargetype");

HashMap<String,ArrayList<Product>> mapc = ProductManager.getProductstr();  
String mapcstr = StringUtill.GetJson(mapc); 

List<User> listS = null ; 
if(UserManager.checkPermissions(user, Group.dealSend)){
	listS =  UserManager.getUsers(user,Group.sencondDealsend);
}else {
	listS = UserManager.getUsers(user,Group.send); 
}
HashMap<String,User> usermap = UserService.getmapSencd(listS); 
 
Map<String,InstallSale>  mapin = InstallSaleManager.getmap(Integer.valueOf(chargetype)); 
Map<String,List<InstallSaleMessage>> mapinsa = InstallSaleMessageManager.getmap();
   
String str = StringUtill.GetJson(mapin);
String strsa = StringUtill.GetJson(mapinsa);
//System.out.println("______"+strsa);
HashMap<String,Category> map = CategoryService.getmapstr();
String mapStr = StringUtill.GetJson(map);   
Set<String> set = map.keySet();  
String setStr = StringUtill.GetJson(set);  
//System.out.println(setStr); 
//List<Category> listc =  CategoryService.getList();
//String list = StringUtill.GetJson(listc);
String merge= "[]";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安装网点结款标准页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>


<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">
var splitsinit = <%=setStr%>;
var splits = splitsinit;
var mergesint = <%=merge%>;
var merges =  mergesint ;
var map = <%=mapStr%>; 
var mapc = <%=mapcstr%>;
var mapin = <%=str%>;
var mapinsa = <%=strsa%>; 
var products = new Array();

$(function () {
	initbranch(); 
	init();
});  
 
function init(){
	initleft();
	initright(); 
	inittotal();
}

function initdate(){
	 var branchid = $("#uid").val();
	 var InstallSale = mapin[branchid]; 
	 if(null == InstallSale || "" == InstallSale){
		 InstallSale = mapin[-1]; 
	 }else {
		 $("#isuid").val(InstallSale.id);
	 } 
	 if(branchid == null || branchid == "" || InstallSale == "" || InstallSale == null){
		 flag = false ;
		 $("#isuid").val(""); 
		 for(var i=0;i<splits.length;i++){
				var id = splits[i];
				 $("#"+id).val("");  
			} 
		 
	 }else { 
		 flag = true ;
		// message = JSON.parse(InstallSale.message);
		 message = mapinsa[InstallSale.id];
		 //alert( message);		
		 uname = InstallSale.uname;
		 phone = InstallSale.phone;
		 locate = InstallSale.locate;
		 andate =InstallSale.andate;

	 }
	  
	 if(flag){
		 //for(var key in message){
		//	 $("#"+key).val(message[key]);  
		// } 
		 for(var i= 0 ;i<message.length;i++){
			 var categoryID = message[i].categoryID;
			 var dealsend = message[i].dealsend;
			 var product = message[i].productID;
			 if(null != product && ""!= product && 0!= product){
				 $("#p"+product).val(dealsend);
			 }else { 
				 $("#c"+categoryID).val(dealsend);
			 }
		 }
		 
		 $("#"+uname).val(uname); 
		 $("#"+phone).val(phone);  
		 $("#"+locate).val(locate); 
		 $("#"+andate).val(andate); 
		 
	 }
}

function initbranch(){
     var flag = false ;
	 $("#uid").change(function(){ 
		 splits = <%=setStr%>; 
		 merges =  <%=merge%>; 
		 var branchid = $("#uid").val();
		 var InstallSale = mapin[branchid];
		 //alert(InstallSale);
		 if(null == InstallSale || "" == InstallSale){
			 InstallSale = mapin[-1]; 
		 }else {
			 $("#isuid").val(InstallSale.id);
		 }
		 
		 if(branchid == null || branchid == "" || InstallSale == "" || InstallSale == null){
			 flag = false ;
			 $("#isuid").val(""); 
			 for(var i=0;i<splits.length;i++){
					var id = splits[i];
					 $("#"+id).val("");  
					
				} 
			  
		 }else { 
			 flag = true ;
			  
			 //message = JSON.parse(InstallSale.message);
			 message = mapinsa[InstallSale.id];
			 uname = InstallSale.uname;
			
			 phone = InstallSale.phone;
			 locate = InstallSale.locate;
			 andate =InstallSale.andate;
			 
			 //for(var key in message){
				// var id = key.split("_");
				// if(id.length>1){
				//	 merges.push(key); 
					 //for(var j=0;j<id.length;j++){
					//	  splits.splice($.inArray(id[j],splits),1);
					 // }
				// }
		 
			// }
			 
			 for(var i= 0 ;i<message.length;i++){
				 var categoryID = message[i].categoryID+"";
				 var id = categoryID.split("_");
					 if(id.length>1){
						 merges.push(categoryID); 
						 //for(var j=0;j<id.length;j++){
						//	  splits.splice($.inArray(id[j],splits),1);
						//  }
					 }  
			 }
			 
		 } 
		 init();
		  
		 if(flag){
			 //for(var key in message){
				// $("#"+key).val(message[key]);  
			 //}
			 for(var i= 0 ;i<message.length;i++){
				 var categoryID = message[i].categoryID;
				 var dealsend = message[i].dealsend;
				 $("#c"+categoryID).val(dealsend); 
			 }
			  
			 if(1 == uname){ 
				 $("#username1").attr("checked","checked"); 
			 }
             if(1 == phone){
            	 $("#phone1").attr("checked","checked");
             } 
             if(1 == locate){
            	 $("#locate1").attr("checked","checked");
             }
             if(1 == andate ){  
            	 $("#andate1").attr("checked","checked");
             }	 
		 }

	 });
	 
	
}

function initleft(){
	$("#tableC td").remove();
	var str = '<td align="left">';
	for(var i=0;i<splits.length;i++){
		var id = splits[i];
		var c = map[id];
		str += '<input type="checkbox"  value="'+c.id+'" name="categorynameLeft"  id="categoryname'+c.id+'" ></input>'+
               c.name+'<br/>';
	} 
	str += '</td>';
	$("#tableC").append(str);
}

function initright(){
	$("#tableR td").remove(); 
	var str = '<td align="left" >';
	for(var i=0;i<merges.length;i++){
	  var text = "";
	  var ids=merges[i];
	  var id = ids.split("_");
	  for(var j=0;j<id.length;j++){
		  var c = map[id[j]]; 
		  text += c.name+"_";
	  }
      str += '<input type="radio" value="'+ids+'"  name="categorynameRight" id="categoryname'+ids+'" ></input>'+text+'<br/>';
	}
	str += '</td>';
	$("#tableR").append(str);
}

function inittotal(){
	$("#tableT tr").remove(); 
	var str = '';
	for(var i=0;i<splitsinit.length;i++){
		var id = splitsinit[i];
		var c = map[id];             
		str +='<tr >';
		str +='<td>'; 
		str +='<a href="javascript:void(0)" onclick="addProductSale('+c.id+')">'+c.name+'</a><input type="hidden" name="salecate" value="'+c.id+'"/>' ;  
		str +='</td>' ; 
		str +='<td>';
		str +='<input type="text" id="c'+c.id+'" name="c'+c.id+'" />';
		str +='</td>';
		str +='</tr>';       
	}
	
	for(var i=0;i<merges.length;i++){
		  var text = "";
		  var ids=merges[i];
		  var id = ids.split("_");
		  for(var j=0;j<id.length;j++){
			  var c = map[id[j]]; 
			  text += c.name+"_";
		  }
		    str +='<tr >';
			str +='<td>';
			str +=text +'<input type="hidden" name="salecate" value="'+ids+'"/>' ; 
			str +='</td>' ; 
			str +='<td>'; 
			str +='<input type="text" id="c'+ids+'" name="c'+ids+'" />';
			str +='</td>';
			str +='</tr>';
		}
	$("#tableT").append(str);
}

function merge(){
  var s='';
  var i= 0 ;

  $('input[name="categorynameLeft"]:checked').each(function(){
	  var value = $(this).val();
	  //splits.splice($.inArray(value,splits),1);
      s+=value+'_';  
      i++;
  }); 
   
  if(i<2){
	  alert("不符合合并条件");
	  return ;
  }
  s = s.substring(0,s.length-1);
  merges.push(s); 
  init();
  initdate();
}
   
function split(){
	$('input[name="categorynameRight"]:checked').each(function(){
		  var value = $(this).val();
		  //alert(value);
		  merges.splice($.inArray(value,merges),1);
		  //alert(merges);
		  var id = value.split("_"); 
		  for(var j=0;j<id.length;j++){ 
			  splits.push(id[j]); 
		  } 
	  });
	init();
	initdate();
}

function addProductSale(id){
	var flag = $.inArray(id,products);
	if(-1 == flag){ 
		products.push(id);
		var product = mapc[id]; 
		var html = '<div id="productsalediv'+id+'" style="text-align:center;"> ';
		html += '<table id="productsale'+id+'" name="productsale'+id+'" style="margin:auto;width:90%;" >';
	    html += '<tr>'; 
		for(var i=0;i<product.length;i++){
			var pro = product[i];
			if(i%3== 0){ 
				html += '</tr>';
				html += '<tr class="asc">'; 
				html += '<td>'+pro.type+'</td>';
				html += '<td><input type="hidden" name="salepro" value="'+pro.id+'"/><input type="text" id="p'+pro.id+'" name="p'+pro.id+'" /></td>';
			}else{
				html += '<td>'+pro.type+'</td>'; 
				html += '<td><input type="hidden" name="salepro" value="'+pro.id+'"/><input type="text" id="p'+pro.id+'" name="p'+pro.id+'" /></td>';
			}
		}
		html+='<tr>';
		html+='<td colspan=5>';
		html+='</td>';
		html+='<td>'; 
		html+='<input type="button" value="保存"  onclick="saveproduct('+id+')" />';
		html+='</td>';
		html+='</tr>';
		html+='</tr>'; 
		html+='</table>';
		html+='</div>';
		$("#all").append(html);
	}
	initdate();
	$("#productsalediv"+id).css("display","block");
	$("#mainwap").css("display","none");
	//$("#productsalediv").css("display","block");
} 
 
function saverule(){  
	//$("input[name='uid']:checked").val(); 
	var uid = $("#uid").val();
	if(uid == null || uid == ""){
		alert("安装网点不能为空"); 
		return false ;
	}
	 
	for(var i=0;i<merges.length;i++){
		  var ids=merges[i];
		  var value = $("#c"+ids).val();
		  if(null == value || "" == value){
			  alert("提成标准不能为空"); 
			  return false ;
		  }else {
			  if(isNaN(value)){
				  alert("提成标准必须是数字");
				  return false ;
			  }
		  }
	}
	
	for(var i=0;i<splits.length;i++){
		var ids = splits[i];
		var value = $("#c"+ids).val();
		 if(null == value || "" == value){
			  alert("提成标准不能为空");
			  return false ;
		  }else { 
			  if(isNaN(value)){
				  alert("提成标准必须是数字"); 
				  return false ;
			  }
		  }
	} 

	//var username = $("input[name='username']:checked").val();
	//var phone = $("input[name='phone']:checked").val();
	//var locate = $("input[name='locate']:checked").val();
	//var andate = $("input[name='andate']:checked").val();
}

function saveproduct(id){
    var flag = true ;
    var str = ""; 
    var isuid = $("#isuid").val();
	$('input[name="salepro"]').each(function(){
		var key = $(this).val();
		var value = $("#"+key).val();
		if(null != value && ""!= value){
			if(isNaN(value)){ 
				  alert(value);
				  alert("提成标准必须是数字"); 
				  flag = false ;
				  return false ;
			  }else {
				  str+= "&salepro="+key+"&"+key+"="+value;
			  }
		}
	});
	
	//alert("method=savesalepro"+str+"&isuid="+isuid);
	
	$.ajax({ 
        type:"post",  
         url:"server.jsp",  
         //data:"method=list_pic&page="+pageCount,
         data:"method=savesalepro"+str+"&isuid="+isuid,
         dataType: "",   
         success: function (data) {
        	 if(flag){
        			$("#productsalediv"+id).css("display","none");
        			$("#mainwap").css("display","block");
        			//$("#productsalediv").css("display","block");
        		}
           },   
          error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	  return false ;
            } 
           });  
	
	if(flag){
		$("#productsalediv"+id).css("display","none");
		$("#mainwap").css("display","block");
		//$("#productsalediv").css("display","block");
	}
	
	
	
	
}
</script>
<br/>  
 
<div id="wrap" style="text-align:center;" >  

<form  action="server.jsp"  name = "myForm" method ="post"  id="form"   onsubmit="return saverule()">
 
<input type="hidden" name="method" value="savesalecategory"/> 
<input type="hidden" name="isuid" id="isuid" value=""/>   
<input type="hidden" name="chargetype" id="chargetype" value="<%=chargetype%>"/>

<div id="all">
<div id="mainwap">
<table  cellspacing="1"  id="table"  style="margin:auto;width:90%;">
       <tr class="asc">
        <td align="center"  colspan=2>
        <table><tr><td align="left">安装网点</td></tr></table>
        </td> 
        <td align="center"  colspan=2 > 
        <table><tr><td align="left"> 
            <select id="uid" name="uid">
            <option value=""></option>
             <option value="-1">全部</option>
             
          <%    
          Set<Map.Entry<String,User>> s = usermap.entrySet();
          Iterator<Map.Entry<String,User>> its = s.iterator();
          while(its.hasNext()) {
        	  Map.Entry<String,User> ent = its.next();
        	  User u = ent.getValue();  
        	  %>   
        	  <option id="uid" value="<%=u.getId() %>"><%=u.getUsername() %> </option>
        	  <%  
          }%>
        </select></td></tr></table>
        
        </td>
       </tr>
       <tr class="asc"> 
        <td align="center">顾客姓名</td> 
        <td align="center">
        <table><tr><td align="left">
            <input type="radio"  name="username"  value="1"  id="username1" />相同<br/>
	        <input type="radio"  name="username"  value="0"  id="username0"/>不相同
	         </td></tr></table>
            
        </td>
        <td align="center">顾客电话</td>
        <td align="center">
         <table><tr><td align="left">
            <input type="radio"  name="phone"  value="1"  id="phone1" />相同<br/>
	        <input type="radio"  name="phone"  value="0"  id="phone0"/>不相同
	        </td></tr></table>
           
	      </td>
       </tr> 
       <tr class="asc"> 
        <td align="center">送货地址</td> 
        <td align="center">
         <table><tr><td align="left">
            <input type="radio"  name="locate"  value="1"  id="locate1" />相同<br/>
	        <input type="radio"  name="locate"  value="0"  id="locate0"/>不相同
	        </td></tr></table>
            
	     </td>
        <td align="center">预约日期</td>
        <td align="center">
         <table><tr><td align="left"> 
           <input type="radio"  name="andate"  value="1"  id="andate1" />相同<br/>
	       <input type="radio"  name="andate"  value="0"  id="andate0"/>不相同
	      </td></tr></table>
           
         </td>
       </tr>
       <tr class="asc"> 
        <td align="center">送货名称</td> 
        <td align="center" >
           <table>
               <tr id="tableC">
                   
                </tr>
                <tr>
                    <td align="right">
                    <input type="button" value="合并" onclick="merge()"/>
                    </td>
                
                </tr>
              </table>
                            
        </td>
        <td align="center" >
            <table >
               
		               <tr id="tableR">
		                
		               </tr>
 
		              <tr>
                    <td align="right">
                    <input type="button" value="拆分" onclick="split()"/>
                    </td>
                
                </tr>
            </table>
        </td>
         <td align="center" >
             <table id="tableT">
		         

            </table>
         </td>
       </tr>
        <tr class="asc">
          <td colspan=4 align="right">
          <table><tr><td align="left"><input type="submit" value="保存"  /></td></tr></table>
           
          
          </td>
        </tr>
   </table> 
   </div>
  </div>  
</form >
     </div>
    
</body>
</html>
