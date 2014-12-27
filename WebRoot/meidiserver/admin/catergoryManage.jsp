<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.upload.UploadManager"%>
<%@ page language="java" import="java.util.*,locate.*,utill.*,category.*,product.*,user.*,group.*,branchtype.*,branch.*,wilson.catergory.*,wilson.salaryCalc.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

//接受参数，初始化变量
String name = "\"" + request.getParameter("groupname") + "\"";
String shop = "\"" + request.getParameter("shop") + "\"";


//接受本页提交的参数
String ids = request.getParameter("merges");
if(!"".equals(request.getParameter("submit"))){
	String paraName = request.getParameter("name");
	String paraShopName = request.getParameter("shop");
	if(paraName.equals("all")){
		paraName = "";
	}
	if(paraShopName.equals("all")){
		paraShopName = "";
	}	
	
	
	
	String paraTempName = "";
	String content = "";
	
	CatergoryMaping cm = new CatergoryMaping();
	for(int i = 0 ; i < ids.split(",").length ; i ++){
		if(StringUtill.isNull(ids)||ids.equals("")||ids.equals("Na")){
			break;
		}
		paraTempName = ids.split(",")[i];
		content += paraTempName.replace(",", "").replace(":", "") + ":" + request.getParameter(paraTempName).replace(",", "").replace(":", "") + ",";
	}
	if(content.endsWith(",")){
		content = content.substring(0,content.length()-1);
	}
	
	
	cm.setName(paraName);
	cm.setShop(paraShopName);
	cm.setModifyTime(TimeUtill.gettime());
	cm.setContent(content);
	if(CatergoryManager.updateCatergoryMaping(cm)){
		//保存成功 
		out.print("<script>alert('操作成功!!')</script>");
		name = "\'" + paraName + "\'";
		shop = "\'" + paraShopName + "\'";
		//out.print("<script>alert('操作成功!!');window.close()</script>"); 
		//return;
	}else{
		//out.print("<script>alert('操作失败，请重试!!')</script>");
		out.print("<script>alert('操作失败，请重试!!');window.close()</script>"); 
		//return;
	}
	
}



ArrayList<SalaryResult> salaryResult = (ArrayList<SalaryResult>)request.getSession().getAttribute("calcResult");
List<String> catergoryList = SalaryCalcManager.getCatergoryFromResult(salaryResult);
String setStr = StringUtill.GetJson(catergoryList);

//文超写的，不知道啥意思，估计就是取所有的类别
//List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend);
//HashMap<String,Category> map = CategoryService.getmapstr();
//String mapStr = StringUtill.GetJson(map);   
//Set<String> set = map.keySet();
String merge= "[]";
//所有id
//String setStr = StringUtill.GetJson(set);



//左侧select里面的内容
//List<BranchType> listb = BranchTypeManager.getLocate();
//Map<String,List<Branch>> map2 = BranchManager.getLocateMapBranch(); 
//String mapjosn = StringUtill.GetJson(map2);

Map<String,List<CatergoryMaping>> srcMap = CatergoryManager.getCatergoryMap();
String cmmapSRC = StringUtill.GetJson(srcMap);
String nameList = StringUtill.GetJson(srcMap.keySet());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>品类分组管理页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../css/jquery-impromptu.min.css" />
<script type="text/javascript" src="../js/jquery-impromptu.min.js"></script> 
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>

<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>

 


 
<!--   头部开始   --> 

<script type="text/javascript">
var splits = <%=setStr%>;
var merges = <%=merge%>;


var cmmapSRC = <%=cmmapSRC %>;
var cmNow;

//传入的参量
var name = <%=name%>;
var shop = <%=shop%>;


//添加弹窗相关
var newName = '';
var clickIndex = 0;

$(function () {
	//初始化branch
	initName();
	reloadData();
	initAddButton();
}); 


function initAddButton(){
	 $("#addbtn1").click(function(){
		 clickIndex = 1;
		 $.prompt(statesdemo);
	  });
	 $("#addbtn2").click(function(){
		 clickIndex = 2;
		 $.prompt(statesdemo);
	  });
	$("#delbtn1").click(function(){
		$.prompt("确认删除'" + $('#name').val() + "'吗?(本分组下的所有门店规则也会被删除)", {
			title: "确认删除",
			buttons: { "确认": true, "放弃": false },
			submit: function(e,v,m,f){
				if(v == true){
					delName();
				}
			}
		});
	  });
  	$("#delbtn2").click(function(){
  		if($("#shop").val() != "all"){
  			$.prompt("确认删除'" + $('#shop').val() +"'吗?", {
  				title: "确认删除",
  				buttons: { "确认": true, "放弃": false },
  				submit: function(e,v,m,f){
  					if(v == true){
  						delShop();
  					}
  				}
  			});
  		}else{
  			$.prompt("无法删除'全部'");
  		}
  		
	 });
}

function addName(){
	$.ajax({ 
        type:"post", 
         url:"catergoryMapingDetail.jsp",
         //data:"method=list_pic&page="+pageCount,
         data:"target=name&method=add&content=" + newName,
         dataType: "",  
         success: function (data) {
        	 //refresh
        	 location.href = location.origin + location.pathname + "?groupname=" + newName;
        	 //cmmapSRC[newName]=[];
        	 //$("#name").append("<option value='"+ newName +"'>"+ newName +"</option>");
        	// $("#name").val(newName);
        	 //initShop();
        	 //selectOnChage();
           },  
          error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	  return false ;
            } 
           }); 
}

function addShop(){
	$.ajax({ 
        type:"post", 
         url:"catergoryMapingDetail.jsp",
         //data:"method=list_pic&page="+pageCount,
         data:"target=shop&method=add&content=" + $('#name').val() + "," + newName,
         dataType: "",  
         success: function (data) {
        	 var obj = {};
        	 obj['shop'] = newName;
        	 obj['content'] = '';
        	 obj['name'] = $('#name').val();
        	 cmmapSRC[$('#name').val()].push(obj);
        	 $("#shop").append("<option value='"+ newName +"'>"+ newName +"</option>");
        	 $("#shop").val(newName);
        	 selectOnChage();
           },  
          error: function (XMLHttpRequest, textStatus, errorThrown) {
        	  return false;
            }
           });
}

function delName(){
	$.ajax({ 
        type:"post", 
         url:"catergoryMapingDetail.jsp",
         //data:"method=list_pic&page="+pageCount,
         data:"target=name&method=del&content=" + $('#name').val(),
         dataType: "",  
         success: function (data) {
        	// $("#name").append("<option value='"+ newName +"'>"+ newName +"</option>");
        	delete cmmapSRC[$('#name').val()];
        	$('#name option:selected').remove(); 
        	selectOnChage();
           },
          error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	  return false ;
            } 
           });
}

function delShop(){
	$.ajax({ 
        type:"post", 
         url:"catergoryMapingDetail.jsp",
         //data:"method=list_pic&page="+pageCount,
         data:"target=shop&method=del&content=" + $('#name').val() + "," + $('#shop').val(),
         dataType: "",  
         success: function (data) {
        	 //$("#name").append("<option value='"+ newName +"'>"+ newName +"</option>");
        	 
        	 for(var k = 0 ; k < cmmapSRC[$('#name').val()].legth ; k++){
        		 if(cmmapSRC[$('#name').val()][k].shop == $('#shop option:selected').val()){
        			 cmmapSRC[$('#name').val()].splice(k,1);
        		 }
        	 }
        	 $('#shop option:selected').remove();
        	 selectOnChage();
           },  
          error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	  return false ;
            }
           });
}


function reloadData(){
	//从branch拿到对应的catergoryMaping
	getCatergoryMaping(name,shop);
	//初始化catergoryMaping
	initCatergoryMaping();
	//根据catergoryMaping初始化下面的数据
	init();
	initSaleMan();
}
	
function initSaleMan(){
	if(cmNow != undefined){
		var items = cmNow.content.split(",");
		for(var i = 0 ; i < items.length ; i ++){
			var id = items[i].split(":")[0];
			var val = items[i].split(":")[1];
			$("#" + id).val(val);
		}
	}
}

function selectOnChage(){
	splits = <%=setStr%>;
	merges = [];

	//传入的参量
	name = $("#name").find("option:selected").val(); 
	shop = $("#shop").find("option:selected").val();
	
	if(name == "空"){
		$('#mergebutton').attr('disabled',true);
		$('#splitbutton').attr('disabled',true);
	}else{
		$('#mergebutton').attr('disabled',false);
		$('#splitbutton').attr('disabled',false);
	}
	if(shop == "all"){
		shop = "";
	}
	
	reloadData();
}

function getCatergoryMaping(name,shop){
	var find = false;
	
	//精确匹配
	for(i in cmmapSRC){
		if(i != name){
			continue;
		}
		var json = cmmapSRC[i];
		var j = 0;
	    for(j=0; j<json.length; j++) {
	  	 	if(json[j].shop == shop){
	  	 		cmNow = json[j];
	  	 		var items = cmNow.content.split(",");
	  	 		if(items == ""){
	  	 			break;
	  	 		}
	  	 		find = true;


	  	 		if(items != ""){
	  	 			for(var k = 0 ; k < items.length ; k ++){
	  	 				merges.push(items[k].split(":")[0]);
		  	 		}
	  	 		}
	  	 		break;
	  	 	}
	  	}
	}
	
	//如果没有对应规则，用总的规则
	if(find==false){
		for(i in cmmapSRC){
			if(i != name){
				continue;
			}
			var json = cmmapSRC[i];
			var j = 0;
		    for(j=0; j<json.length; j++) {
		  	 	if(json[j].shop == ''){
		  	 		find = true;
		  	 		cmNow = json[j];
		  	 		
		  	 		var items = cmNow.content.split(",");
		  	 		if(items != ""){
		  	 			for(var k = 0 ; k < items.length ; k ++){
		  	 				merges.push(items[k].split(":")[0]);
			  	 		}
		  	 		}
		  	 		break;
		  	 	}
		  	}
		}
	}
}


function initCatergoryMaping(){
	for(var m = 0 ; m < merges.length ; m ++){
		var cm = merges[m].split("_");
		for(var j=0;j<cm.length;j++){
			var t = cm[j];
			for(var k=0;k<splits.length;k++){
				if(t == splits[k]){
					splits.splice(k,1);
					break;
				}
			}
			
		}
	}
}

function initName(){
    var opt = { }; 
    opt.date = {preset : 'date'};	  
 	$("#name").change(function(){
		$("#shop").html(""); 
		var num = ($("#name").children('option:selected').val());
		
		var options = '<option value="all">全部</option>'; 
		
	 	var json = cmmapSRC[num];
	  
         for(var i=0; i<json.length; i++) {
        	 if(json[i].shop != ""){
        		 options +=  "<option value='"+json[i].shop+"'>"+json[i].shop+"</option>";
        	 }
       	 }
		
		$("#shop").html(options);
		selectOnChage();
	}); 
 	
 	$("#shop").change(function(){
 		selectOnChage();
	}); 
	//设置branch
	$("#name").val(name); 
	if(name == "空"){
		$('#mergebutton').attr('disabled',true);
		$('#splitbutton').attr('disabled',true);
	}else{
		$('#mergebutton').attr('disabled',false);
		$('#splitbutton').attr('disabled',false);
	}
	initShop();
	$("#shop").val(shop); 
}
 
function initShop(){
	$("#shop").html(""); 
	var num = ($("#name").children('option:selected').val());
	
	if(num == "all"){
		var options = '<option value="all">全部</option>';
		$("#shop").html(options);   
	}else{
		var json = cmmapSRC[num];
		var options = '<option value="all">全部</option>';
	    for(var i=0; i<json.length; i++) 
	  	{
	    	if(json[i].shop != ""){
		   		options +=  "<option value='"+json[i].shop+"'>"+json[i].shop+"</option>";
	    	}
	  	}
	  	 $("#shop").html(options);   
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
		var c = splits[i];
		str += '<input type="checkbox"  value="'+c+'" name="categorynameLeft"  id="categoryname'+c+'" ></input>'+
               c+'<br/>';
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
		  var c = id[j]; 
		  text += c+"_";
	  }
      str += '<input type="radio" value="'+ids+'"  name="categorynameRight" id="categoryname'+ids+'" ></input>'+text+'<br/>';
	}
	str += '</td>';
	$("#tableR").append(str);
}

function inittotal(){
	$("#tableT tr").remove(); 
	var str = '';
	for(var i=0;i<merges.length;i++){
		  var text = "";
		  var ids=merges[i];
		  var id = ids.split("_");
		  for(var j=0;j<id.length;j++){
			  var c = id[j]; 
			  text += c+"_";
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
  
  if(i<1){
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

var statesdemo = {
		state0: {
			title: '请输入名称',
			html:'<label>名称 <input type="text" name="fname" value=""></label><br />',
			buttons: { 取消: -1, 确认: 1 },
			focus: 1,
			submit:function(e,v,m,f){ 
				console.log(f);
				e.preventDefault();
				//if(v==-1) 
				if(v==1){
					newName = f.fname;
					if(clickIndex == 1){
						addName();
					}else if(clickIndex == 2){
						addShop();
					}
				}
				$.prompt.close();
			}
		},
};


function saverule(){
	if($('#submitButton').val()=="保存"){
		return false;
	}
	
	var mergesSTR = "";
	for(var i = 0 ; i < merges.length ; i ++){
		mergesSTR += merges[i];
		if(i != merges.length - 1){
			mergesSTR += ",";
		}
	}
	$('#merges').val(mergesSTR);
}

</script>

</head>

<body style="scoll:no">
<br/>  

<div id="wrap" style="text-align:center;" >     
<form  action=""  name = "myForm" method ="post"  id="form"   onsubmit="return saverule()">

<input type="hidden" name="merges" id="merges" value="Na"/>

<table  cellspacing="1"  id="table"  style="margin:auto;width:780px;"> 
       
       <tr class="asc">
        <td align="center">分组名称</td> 
        <td align="center">适用门店</td> 
        <td align="center"></td> 
       </tr>
       <tr class="asc"> 
        <td align="center">
        	<select name="name" id="name">
		         <%
		         Set<String> nameSet = srcMap.keySet();
		         Iterator<String> it = nameSet.iterator();  
		         String tempStr = "";
				 while(it.hasNext()){
					 tempStr = it.next();
				%>	    
				<option value="<%=tempStr%>" ><%=tempStr%></option>
				<%
				 }
				%>
			</select>
			<button id="addbtn1">增加</button>
			<!-- <button id="delbtn1">删除本项</button>  -->
        </td> 
        <td align="center">
        	<select id="shop" name="shop">
        		<option value="all">全部</option> 
       		</select>
       		<!--  
       		<button id="addbtn2">增加</button>
			<button id="delbtn2">删除本项</button>
			-->
        </td>
        <td align="center">导购员</td>
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
          <td colspan=3 align="center"><input id="submitButton" type="submit" name="submit" style="background-color:red;font-size:25px;" value="保存" onclick="$(this).val('保存中')" /></td>
          </td>
        </tr>
   </table> 

</form>
     </div>

</body>
</html>
