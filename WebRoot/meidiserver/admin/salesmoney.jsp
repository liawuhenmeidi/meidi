<%@ page language="java" import="java.util.*,message.*,locate.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend);
  
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
<title>文员派工页</title>
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
var splits = <%=setStr%>;   
var merges = <%=merge%>;  
var map = <%=mapStr%>; 

$(function () {
	init();
});  

function init(){
	initleft();
	initright();
	inittotal();
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
	for(var i=0;i<splits.length;i++){
		var id = splits[i];
		var c = map[id];             
		str +='<tr >';
		str +='<td>'; 
		str +=c.name+'<input type="hidden" name="salecate" value="'+c.id+'"/>' ;  
		str +='</td>' ;
		str +='<td>';
		str +='<input type="text" id="'+c.id+'" name="'+c.id+'" />';
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
			str +='<input type="text" id="'+ids+'" name="'+ids+'" />';
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
	  splits.splice($.inArray(value,splits),1);
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
}

function split(){
	$('input[name="categorynameRight"]:checked').each(function(){
		  var value = $(this).val();
		  merges.splice($.inArray(value,merges),1);
		  var id = value.split("_"); 
		  for(var j=0;j<id.length;j++){ 
			  splits.push(id[j]); 
		  } 
	  });
	init();
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
		  var value = $("#"+ids).val();
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
		var value = $("#"+ids).val();
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
</script>
<br/>  
 
<div id="wrap" style="text-align:center;" >     
<form  action="server.jsp"  name = "myForm" method ="post"  id="form"   onsubmit="return saverule()">

<input type="hidden" name="method" value="savesalecategory"/> 

<table  cellspacing="1"  id="table"  style="margin:auto;width:90%;"> 
       <tr class="asc">
        <td align="center"  colspan=2>
        <table><tr><td align="left">安装网点</td></tr></table>
        </td> 
        <td align="center"  colspan=2 > 
        <table><tr><td align="left"> 
            <select id="uid" name="uid">
             <option></option>
          <% for(int i=0;i<listS.size();i++) {
        	  User u = listS.get(i);
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

</form>
     </div>

</body>
</html>
