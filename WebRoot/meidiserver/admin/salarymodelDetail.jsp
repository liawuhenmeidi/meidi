<%@ page language="java" import="java.util.*,message.*,locate.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,wilson.upload.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

UploadSalaryModel salarymodel = new UploadSalaryModel();
String button = request.getParameter("button");
String contentNum = request.getParameter("contentNum");


String idSTR = request.getParameter("id");
int id = Integer.parseInt(idSTR);

boolean close = false;
//接受参数
String catergory = request.getParameter("catergory");
String type = request.getParameter("type");
salarymodel = (UploadSalaryModel)request.getSession().getAttribute("altSalaryModel");


//接受提成标准
String tempStr = "";
String content = "{";

for(int i = 0  ; i < Integer.parseInt(StringUtill.isNull(contentNum)?"0":contentNum) ; i++){
	tempStr = request.getParameter("contentstart" + i);
	if(!StringUtill.isNull(tempStr)){
		content += "\"" + tempStr;
	}
	tempStr = request.getParameter("contentend" + i);
	if(!StringUtill.isNull(tempStr)){
		content += "-" + tempStr + "\"";
	}
	tempStr = request.getParameter("contentvalue" + i);
	if(!StringUtill.isNull(tempStr)){
		content += ":" + "\"" + tempStr + "\"";
	}
	
	content += ",";
}
if(content.endsWith(",")){
	content = content.substring(0,content.length()-1);
}

content += "}";

salarymodel.setCatergory(catergory);
salarymodel.setType(type);
salarymodel.setCommitTime(TimeUtill.gettime());
salarymodel.setContent(content);

//id为-1是新建
if(id ==-1){
	//如果是提交的
	if(button!= null && button.equals("确认修改")){	
		
		//验证是否符合规范
		if(salarymodel.checkContent()){

			if(UploadManager.saveSalaryModel(salarymodel)){
				//保存成功
				close =true;
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
		//直接展示上面的就行
	}
	
//id不为-1，为修改
}else{
	salarymodel = UploadManager.getSalaryModelsById(id);
	
	//如果是提交的
	if(button!= null && button.equals("确认修改")){
		salarymodel.setContent(content);
		salarymodel.setCatergory(catergory);
		salarymodel.setType(type);
		salarymodel.setCommitTime(TimeUtill.gettime());
		
		//验证是否符合规范
		if(salarymodel.checkContent()){

			if(UploadManager.saveSalaryModel(salarymodel)){
				//保存成功
				response.getWriter().write("成功");  
				response.getWriter().flush();   
				response.getWriter().close();    
			}else{
				//保存失败
			}
		}else{
			//不符合 规范的，重新填写!
			salarymodel.setContent("");
		}
	}
}





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
var rows = Number(<%=items.length %>);
var close = <%=close%>;

window.onload = function() { 
	if(rows == 0){
		firstItem();
	}
	if(close){
		window.close();
	}
	}; 

function firstItem(){
	var newCol = "<tr class='asc'><td align='center' colspan='3'><input name='contentstart" + rows + "' id='contentstart" + rows + "' type='text' value='0' readonly='readonly'/> - <input name='contentend" + rows + "'  id='contentend" + rows + "' type='text' value='以上'/></td><td align='center' colspan='2'><input name='contentvalue" + rows + "' id='contentvalue" + rows + "' type='text'/></td></tr>"
	$('#addTarget').before(newCol);
	$('#left').attr("rowspan",Number($('#left').attr("rowspan")) + 1);
	rows = rows + 1 ;
	$('#contentNum').val(Number($('#contentNum').val()) + 1);
}
	
function newItem(){
	var newCol = "<tr class='asc'><td align='center' colspan='3'><input name='contentstart" + rows + "' id='contentstart" + rows + "' type='text'/> - <input name='contentend" + rows + "'  id='contentend" + rows + "' type='text' value='以上'/></td><td align='center' colspan='2'><input name='contentvalue" + rows + "' id='contentvalue" + rows + "' type='text'/></td></tr>"
	$('#addTarget').before(newCol);
	$('#left').attr("rowspan",Number($('#left').attr("rowspan")) + 1);
	rows = rows + 1 ;
	$('#contentNum').val(Number($('#contentNum').val()) + 1);
}

function checkContent(){
	var newCol = Number($('#contentNum').val());
	var tempDouble = 0.0;
	
	white($('#catergory'));
	white($('#type'));
	
	if($('#catergory').val() == ""){
		red($('#catergory'));
	}else if($('#type').val()== ""){
		red($('#type'));
	}
	for(var i = 0 ; i < newCol ; i ++){
		white($('#'+"contentstart" + i));
		white($('#'+"contentend" + i));
		white($('#'+"contentvalue" + i));
	}
	
	for(var i = 0 ; i < newCol ; i ++){
		if(tempDouble == Number($('#'+"contentstart" + i).val())){
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
	
	tempDouble = $('#'+"contentend" + (newCol-1)).val();

	if(tempDouble != '/' && tempDouble != '以上'){
		red($('#'+"contentend" + (newCol-1)));
		return false;
	}
	
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
			<td align="center">名称</td>
			<td align="center">       		  
		     <%=salarymodel.getName() %>
		    </td> 
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
			<td align="center">提交时间</td>
			 <td align="center" ><%=salarymodel.getCommitTime() %></td>
			</tr>

		<tr class="asc" >
		
		<td align="center" rowspan="<%=items.length + 1 %>" id="left">提成标准
		<button type="button" onclick="newItem()">增加一项</button>
		<input name="contentNum" id="contentNum" value="<%=items.length %>" type="hidden"/>
		</td>
		<td align="center" colspan="3" onclick="a()">零售价</td>
		<td align="center" colspan="2">提成</td>
		<%
		for(int i = 0 ; i < items.length ; i ++){
			
		
		%>
		<tr class="asc">
			<td align="center" colspan="3">
			<input type="text" id="contentstart<%=i %>" name="contentstart<%=i %>" value="<%=items[i].split(":")[0].split("-")[0] %>" <%if(items[i].split(":")[0].split("-")[0].equals("0.0")){ %>readonly="readonly" <%} %>/>
			-
			<input type="text" id="contentend<%=i %>" name="contentend<%=i %>" value="<%=items[i].split(":")[0].split("-")[1].equals("/")?"以上":items[i].split(":")[0].split("-")[1] %>"/>
			</td>
		
			<td align="center" colspan="2">
			<input type="text" id="contentvalue<%=i %>" name="contentvalue<%=i %>" value="<%=items[i].split(":")[1] %>" />  
			</td>
		</tr>
		<%
		}
		%>
		</tr>
		
		<tr class="asc" id="addTarget">
			<td width="100%" align="center" colspan="6"><input name="button" type="submit"  style="background-color:red;font-size:25px;"  value="确认修改" /></td>
		</tr>
</table> 
</form>
     </div>

</body>
</html>
