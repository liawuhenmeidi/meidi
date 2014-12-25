	<%@ page language="java" import="java.util.*,message.*,locate.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,wilson.upload.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

UploadSalaryModel salarymodel = new UploadSalaryModel();
String button = request.getParameter("button");
String contentNum = request.getParameter("contentNum");


String idSTR = request.getParameter("id");
int id = Integer.parseInt(idSTR);

//接受参数
String catergory = request.getParameter("catergory");
String type = request.getParameter("type");
String filename = request.getParameter("filename");
salarymodel = (UploadSalaryModel)request.getSession().getAttribute("altSalaryModel");
List<String> selectModelsNameList = (List<String>)request.getSession().getAttribute("selectModelsNameList");

//接受提成标准
String tempStr = "0";
String content = "{";

for(int i = 0  ; i < Integer.parseInt(StringUtill.isNull(contentNum)?"0":contentNum) ; i++){
	tempStr = request.getParameter("contentstart" + i);
	if(!StringUtill.isNull(tempStr)){
		content += "\"" + Double.parseDouble(tempStr);
	}
	
	tempStr = request.getParameter("contentend" + i);
	if(!StringUtill.isNull(tempStr)){
		tempStr = (Double.parseDouble(tempStr) + 1) + "";
		content += "-" + tempStr + "\"";
	}
	tempStr = request.getParameter("contentvalue" + i);
	if(!StringUtill.isNull(tempStr)){
		content += ":" + "\"" + tempStr + "\"";
	}
	
	content += ",";
}

content += "\"" + request.getParameter("contentstartlast") + "-" + request.getParameter("contentendlast") + "\":\"" + request.getParameter("contentvaluelast") + "\""; 


if(content.endsWith(",")){
	content = content.substring(0,content.length()-1);
}

content += "}";



//id为-1是新建
if(id ==-1){
	//如果是提交的
	if(button!= null && button.equals("正在修改")){	
		salarymodel.setCatergory(catergory);
		salarymodel.setType(type);
		salarymodel.setCommitTime(TimeUtill.gettime());
		salarymodel.setContent(content);
		salarymodel.setName(filename);
		//验证是否符合规范
		if(salarymodel.checkContent()){

			if(UploadManager.saveSalaryModel(salarymodel)){
				//保存成功 
				out.print("<script>alert('操作成功!!');window.close()</script>"); 
				return;
			}else{
				//保存失败
			}
		}else{
			//不符合 规范的，重新填写!
			salarymodel.setContent("");
			
			salarymodel.setCatergory("无");
		}
	
	//如果不是提交的	
	}else{
		salarymodel.setId(-1);
		salarymodel.setType("");
		salarymodel.setCommitTime(TimeUtill.gettime());
		salarymodel.setContent("");
		//直接展示上面的就行
	}
	
//id不为-1，为修改
}else{
	salarymodel = UploadManager.getSalaryModelsById(id);
	
	//如果是提交的
	if(button!= null && button.equals("正在修改")){
		salarymodel.setContent(content);
		salarymodel.setCatergory(catergory);
		salarymodel.setType(type);
		salarymodel.setCommitTime(TimeUtill.gettime());
		
		//验证是否符合规范
		if(salarymodel.checkContent()){

			if(UploadManager.saveSalaryModel(salarymodel)){
				//保存成功  
				out.print("<script>alert('操作成功!!');window.close()</script>"); 
				return;
			}else{
				//保存失败
			}
		}else{
			//不符合 规范的，重新填写!
			salarymodel.setContent("");
			salarymodel.setCatergory("无");
		}
	}
}



String tempString = "";

%>

<%
String[] items =  salarymodel.getContent().replace("{", "").replace("}", "").replace("\"", "").split(",");
	//没东西的话，清空
	if(items[0].equals("")){
		items = new String[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成标准增加/修改 页面</title>
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
var rows = Number(<%=items.length %>) - 1;

window.onload = function() { 
	if(rows == 0){
		$('#contentvaluelast').removeAttr("readonly");
	}
	if(rows == -1){
		$('#contentvaluelast').removeAttr("readonly");
		rows = 0;
	}
	}; 

function firstItem(){
	var newCol = "<tr class='asc'><td align='center' colspan='3'><input name='contentstart" + rows + "' id='contentstart" + rows + "' type='text' value='0' readonly='readonly'/> - <input name='contentend" + rows + "'  id='contentend" + rows + "'  onchange='itemOnchange($(this),"+ rows + ")' type='text' value='以上'/></td><td align='center' colspan='2'><input name='contentvalue" + rows + "' id='contentvalue" + rows + "' type='text'/></td></tr>"
	$('#addTarget').before(newCol);
	$('#left').attr("rowspan",Number($('#left').attr("rowspan")) + 1);
	rows = rows + 1 ;
	$('#contentNum').val(Number($('#contentNum').val()) + 1);
}
	
function newItem(){
	var newCol = "<tr class='asc'><td align='center' colspan='3'><input name='contentstart" + rows + "' id='contentstart" + rows + "' type='text' readonly='readonly'  /> - <input name='contentend" + rows + "'  id='contentend" + rows + "' onchange='itemOnchange($(this),"+ rows + ")' type='text' value=''/></td><td align='center' colspan='2'><input name='contentvalue" + rows + "' id='contentvalue" + rows + "' type='text' value='' onchange='changelast($(this)," + rows + ")'/></td></tr>"
	$('#addTarget').before(newCol);
	$('#left').attr("rowspan",Number($('#left').attr("rowspan")) + 1);
	var target1 = "contentend" + (Number(rows)-1);
	var target2 = "contentstart" + rows;
	
	if(rows == 0){
		target2 = "contentstartlast";
		
		$('#contentstart0').val('0');
	}
	$('#' + target2).val(Number($('#' + target1).val())+1);
	
	rows = rows + 1 ;
	$('#contentNum').val(Number($('#contentNum').val()) + 1);
	$('#contentvaluelast').attr("readonly","readonly");
	
	var target = "contentvalue" + (rows -1);
	changelast($('#' + target),(rows -1));
}

function delItem(){
	if(rows > 0){
		$('#addTarget').prev().remove();
		$('#left').attr("rowspan",Number($('#left').attr("rowspan")) - 1);
		rows = rows - 1 ;
		$('#contentNum').val(Number($('#contentNum').val()) - 1);
	}
	if(rows == 0){
		$('#contentvaluelast').removeAttr("readonly");
		$('#contentstartlast').val('0');
	}
	
	var target = "contentvalue" + (rows -1);
	changelast($('#' + target),(rows -1));
}

function itemOnchange(obj,id){
	var target = "contentstart" + (Number(id)+1);
	if((Number(id)+1) == rows){
		target = "contentstartlast";
	}
	$('#' + target).val(Number(obj.val()) + 1);
}

function changelast(obj,id){
	if(Number(id) + 1 == rows){
		$('#contentvaluelast').val(obj.val());
	}
}

function checkContent(){
	var newCol = Number($('#contentNum').val()) - 1;
	if(newCol < 0){
		newCol = 0 ;
	}
	var tempDouble = -1.0;
	
	white($('#catergory'));
	white($('#type'));
	white($('#contentstartlast'));
	white($('#contentendlast'));
	white($('#contentvaluelast'));
	
	if($('#catergory').val() == ""){
		red($('#catergory'));
		return false;
	}else if($('#type').val()== ""){
		red($('#type'));
		return false;
	}
	for(var i = 0 ; i < newCol ; i ++){
		white($('#'+"contentstart" + i));
		white($('#'+"contentend" + i));
		white($('#'+"contentvalue" + i));
	}
	
	for(var i = 0 ; i < newCol ; i ++){
		if(tempDouble == (Number($('#'+"contentstart" + i).val())) - 1){
			if(Number($('#'+"contentend" + i).val()) <= tempDouble){
				red($('#'+"contentend" + i));
				return false;
			}
			tempDouble = Number($('#'+"contentend" + i).val());
			
			
			//判断提成数是否为空
			if($('#'+"contentvalue" + i).val() == "" || isNaN($('#'+"contentvalue" + i).val().replace("%",""))){
				red($('#'+"contentvalue" + i));
				return false;
			}
		}else{
			red($('#'+"contentstart" + i));
			return false;
		}
	} 
	
	var target = "contentvalue" + newCol;
	if($('#' + target).val() != $('#contentvaluelast').val() && newCol != 0){
		red($('#contentvaluelast'));
		return false;
	}
	//tempDouble = $('#'+"contentend" + (newCol-1)).val();

	//if(tempDouble != '/' && tempDouble != '以上'){
	//	red($('#'+"contentend" + (newCol-1)));
	//	return false;
	//}
	
	return true;
	
}



function a(){
	alert(checkContent());
}

function red(object){
	object.css('background-color','#FF0000');
}

function white(object){
	object.css('background-color','#FFFFFF');
}

function checkedd(){
	if(!checkContent()){
		return false;
	}
	if($('#submitbutton').val() == '正在修改'){
		return false;
	}
	$('#submitbutton').val('正在修改');
}
</script>
<div style="position:fixed;width:100%;height:100px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

</div >



<div style=" height:50px;">
</div>


 
<br/>  
 
<div id="wrap">  
<form  action=""  method ="post"  id="form"   onsubmit="return checkedd()"  >
<table  cellspacing="1"  id="table" > 
		<tr  class="asc">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td> 
			<td align="center"><input type="text" value="<%=salarymodel.getId() %>" disabled="disabled"/></td>  
			<td align="center">门店</td>
			<td align="center" id="shop"><%=salarymodel.getShop() %></td> 
			<td align="center">提交时间</td>
			<td align="center"><%=salarymodel.getCommitTime() %></td> 
		</tr> 
		<tr class="asc">	 
			<td align="center" >类别</td>
			<td align="center" >
			    <input type="text"  id="catergory" name="catergory" value="<%=salarymodel.getCatergory() %>"  />
			</td>
				<td align="center">型号</td>
			<td align="center">			
			    <input type="text" name="type" id="type" value="<%=salarymodel.getType() %>"  /> 
			</td>
			<td align="center">名称</td>
			 <td align="center" >
			 <select name="filename">
				<%for(int i = 0 ; i < selectModelsNameList.size() ; i ++){ %>
				<option value="<%=selectModelsNameList.get(i)  %>"><%=selectModelsNameList.get(i) %></option>
				<%} %>
			</select>  
			</td>
			</tr>

		<tr class="asc" >
		
		<td align="center" rowspan="<%=items.length + 1 + 1 %>" id="left">提成标准
		<button type="button" onclick="newItem()">增加一项</button>
		<button type="button" onclick="delItem()">删除一项</button>
		<input name="contentNum" id="contentNum" value="<%=items.length - 1 < 0 ? "0" : String.valueOf(items.length - 1)%>" type="hidden"/>
		</td>
		<td align="center" colspan="3" >零售价</td>
		<td align="center" colspan="2">提成</td>
		<%
		for(int i = 0 ; i < items.length -1 ; i ++){
			
		
		%>
		<tr class="asc">
			<td align="center" colspan="3">
			<input type="text" id="contentstart<%=i %>" name="contentstart<%=i %>" value="<%=items[i].split(":")[0].split("-")[0]%>" readonly="readonly"/>
			-
			<input type="text" id="contentend<%=i %>" name="contentend<%=i %>" onchange="itemOnchange($(this),<%=i %>)" value="<%=String.valueOf(Double.parseDouble(items[i].split(":")[0].split("-")[1])-1) %>"/>
			</td>
		
			<td align="center" colspan="2">
			<input type="text" id="contentvalue<%=i %>" name="contentvalue<%=i %>" value="<%=items[i].split(":")[1] %>" onchange="changelast($(this),<%=i %>)" />  
			</td>
		</tr>
		<%
		}
		%>
		</tr>
		
		<%
		int i = items.length - 1;
		boolean itemsIsNull = false;
		
		if(items.length == 0){	
			itemsIsNull = true;
		}
		%>
		<tr class="asc" id="addTarget">
			<td align="center" colspan="3">
			<input type="text" id="contentstartlast" name="contentstartlast" value="<%=itemsIsNull ? "0" : items[i].split(":")[0].split("-")[0]%>" readonly="readonly"/>
			-
			<input type="text" id="contentendlast" name="contentendlast" readonly="readonly" value="以上"/>
			</td>
		
			<td align="center" colspan="2">
			<input type="text" id="contentvaluelast" name="contentvaluelast" value="<%=itemsIsNull?"0":items[i].split(":")[1] %>" readonly="readonly" />  
		</tr>
		
		
		<tr class="asc">
			<td width="100%" align="center" colspan="6">
			<input id="submitbutton" name="button" type="submit"  style="background-color:red;font-size:25px;"  value="确认修改" />
			</td>
		</tr>
</table> 
</form>
     </div>

</body>
</html>
