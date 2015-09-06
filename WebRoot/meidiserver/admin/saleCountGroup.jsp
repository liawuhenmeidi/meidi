<%@ page language="java" import="java.util.*,message.*,locate.*,installsale.*,uploadtotalgroup.*,utill.*,wilson.upload.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
 
List<String> list = UploadManager.getSalaryModelsCategoryAll(); 
String message = "{}";
UploadTotalGroup up = UploadTotalGroupManager.getUploadTotalGroup();
if(up != null){
   message = up.getCategoryname();
}
 System.out.println(message);
String category = StringUtill.GetJson(list);

String chargetype = request.getParameter("chargetype");
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


<link rel="stylesheet" href="../css/jquery-ui.css"/>
<script src="../js/jquery-ui.js"></script>

<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">
var splits = <%=category%>;
var products = new Array();
var merges = <%=merge%>;
var category =<%=message%>;   
  
$(function () { 
	initmerges();  
	init();
});  
   
  function initmerges(){ 
	 for(var key in category)
	 {   
		 
		  //alert(merges);
		  var id = key.split("_"); 
		  for(var j=0;j<id.length;j++){ 
			  splits.splice($.inArray(id[j],splits),1);
		  }  
		 merges.push(key);
	 }
	  
  }
  
  
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
		str += '<input type="checkbox"  value="'+id+'" name="categorynameLeft"  id="categoryname'+id+'" ></input>'+
		id+'<br/>';
	} 
	str += '</td>';
	$("#tableC").append(str);
}

function initright(){
	$("#tableR td").remove(); 
	var str = '<td align="left" >';
	for(var i=0;i<merges.length;i++){
	  var ids=merges[i];
      str += '<input type="radio" value="'+ids+'"  name="categorynameRight" id="categoryname'+ids+'" ></input>'+ids+'<br/>';
	}
	str += '</td>';
	$("#tableR").append(str);
}

function inittotal(){
	$("#tableT tr").remove(); 
	var str = '';
	//for(var i=0;i<splits.length;i++){
	//	var id = splits[i];        
	//	str +='<tr >';
	//	str +='<td>'; 
	//	str +=id ;  
	//	str +='</td>' ; 
	//	str +='<td>';
	//	str +='</td>';
	//	str +='</tr>';       
//	}
	
	for(var i=0;i<merges.length;i++){
		  var ids=merges[i];
		  
		    str +='<tr >';
			str +='<td>';
			str +=ids +'<input type="hidden" name="salecate" value="'+ids+'"/>' ; 
			str +='</td>' ; 
			str +='<td>'; 
			str +='<input type="text" id="c'+ids+'" name="c'+ids+'" value="'+category[ids]+'" />';
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
 
  s = s.substring(0,s.length-1);
  merges.push(s); 
//  alert(merges);
  init();
 // initdate();
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
	//initdate();
}



</script>
<br/>  
 
<div id="wrap" style="text-align:center;" >  

<form  action="server.jsp"  name = "myForm" method ="post"  id="form"   onsubmit="return saverule()">
 
<input type="hidden" name="method" value="savesalecategorytotal"/>

<div id="all">
<div id="mainwap">
<table  cellspacing="1"  id="table"  style="margin:auto;width:90%;">
       <tr class="asc">
        <td align="center"  colspan=3>
        <table><tr><td align="left">合并规则</td></tr></table>
        </td> 
       </tr>
       <tr class="asc">
       <td align="center" >
           <table>
               <tr id="tableC">
                   
                </tr>
                <tr>
                    <td align="right">
                    <input id="mergebutton" type="button" value="合并" onclick="merge()"/>
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
                    <input id="splitbutton" type="button" value="拆分" onclick="split()"/>
                    </td>
                </tr>
            </table>
        </td>
        
        <td align="center">
        	<table id="tableT"></table>
        </td>
        
        
       </tr>
        <tr class="asc">
          <td colspan=3 align="right">
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
