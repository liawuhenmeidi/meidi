<%@page import="com.sun.net.httpserver.HttpContext"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,order.*,change.*,orderproduct.*,branchtype.*,branch.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<% 
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	//显示内容的开关
	boolean showContent = false;
	
	long startTime = System.currentTimeMillis();
	String method = request.getParameter("method");
	List <UploadChange> left = new ArrayList<UploadChange>();
	List <UploadChange> right = new ArrayList<UploadChange>();
	 List<MathChange> list  = null ;
	 List<UploadChange> uploadOrders = UploadChangeManager.getUnCheckedUploadOrders();
	List<String> orderNames = UploadChangeManager.getAllUploadOrderNames(uploadOrders);
	String selectOrderName1 = "";
	String selectOrderName2 = "";
	String checkBoxStatus = ""; 
	Map<String,String> map  = BranchTypeChange.getinstance().getMap();
    String mapstr = StringUtill.GetJson(map);
	if("commited".equals(method)){  
		//接受两边的id 
		selectOrderName1 = request.getParameter("uploadorder1");
		selectOrderName2 = request.getParameter("uploadorder2"); 
		//接受选定的对比参数
		checkBoxStatus = request.getParameter("ridiocheck");
		//初始化要对比的orders
		MatchOrder mo = new MatchOrder();
	    if(null != uploadOrders){    
	    	for(int i=0;i<uploadOrders.size();i++){
	    		UploadChange up = uploadOrders.get(i);
	    		if(up.getFilename().equals(selectOrderName1) && up.getStatues() == 1){
	    			left.add(up); 
	    		} 
	    		if(up.getFilename().equals(selectOrderName2) && up.getStatues()  == 0 ){
	    			right.add(up);
	    		}
	    		
	    	} 
	    }   
	     
	    list = UploadChangeManager.getmatch(left, right,checkBoxStatus);
		System.out.println(left.size());
		System.out.println(right.size());
		
	}else if("confirm".equals(method)) {
		String[] dbSide = request.getParameterValues("dbside");
		String[] radioleft = request.getParameterValues("radioleft");
		String[] radioright= request.getParameterValues("radioright");
		if(null != dbSide && dbSide.length > 0){
			ChangeManager.save(dbSide);
		}
	    
		if(null != radioleft && radioleft.length == 1 && null != radioright && radioright.length == 1){
			ChangeManager.save(radioleft,radioright); 
		}
		
	}
	
	System.out.println("自动对比页面取数据时间(毫秒) = " + (System.currentTimeMillis() - startTime));
	showContent = true; 
    
	 
		//计算本轮的计算的个数
 
	int inter = 1;
	//System.out.println("自动对比页面总执行时间(毫秒) = " + (System.currentTimeMillis() - startTime));

	//下面用到的背景色
	String backgroundColor ="";
	boolean showColor = false;
	
	//下面用到的是否check
	boolean isChecked = false;
	
	//是否禁用
	boolean dbsideDisabled =false;
	boolean uploadsideDisabled=false;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结款页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
	<style>
     redTag{color:red}
	</style>
	<style type="text/css"> 
table {table-layout:fixed;} 
tr strong,tr td {white-space:normal} 
</style>
</head>

<body>
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript">


var checkBoxStatus = '<%=checkBoxStatus%>';
var scrollFrom;
var fix;
var map = <%=mapstr%>; 
$(function () { 
	initCheckBox();
	initcheck();
});

function trIsChecked(tr){

	var checkbox1 = $(tr).find('input')[0];
	var checkbox2 = $(tr).find('input')[1];
	if(checkbox1.checked && checkbox2.checked){
		return true;
	}else{
		return false;
	}
}

function scrollToPrev(){
	
	if($(scrollFrom).index() == 0){
		//到最后一行了
		
		if(trIsChecked(scrollFrom)){
			//滚到这个位置	
			$('#maindiv').animate({scrollTop:$(scrollFrom).offset().top - fix + $('#maindiv').scrollTop()}, 800);
		}
	}else{
		
		if(trIsChecked(scrollFrom)){
			//滚到这个位置	
			$('#maindiv').animate({scrollTop:$(scrollFrom).offset().top - fix + $('#maindiv').scrollTop()}, 800);
			scrollFrom = $(scrollFrom).prev();
		}else{
			scrollFrom = $(scrollFrom).prev();
			scrollToPrev();
		}
		
	}
}

function scrollToNext(){
	
	if($(scrollFrom).index() == $('#baseTable tr').length - 1){
		//到最后一行了
		
		if(trIsChecked(scrollFrom)){
			//滚到这个位置	
			$('#maindiv').animate({scrollTop:$(scrollFrom).offset().top - fix + $('#maindiv').scrollTop()}, 800);
		}
	}else{
		
		if(trIsChecked(scrollFrom)){
			//滚到这个位置	
			$('#maindiv').animate({scrollTop:$(scrollFrom).offset().top - fix + $('#maindiv').scrollTop()}, 800);
			scrollFrom = $(scrollFrom).next();
		}else{
			scrollFrom = $(scrollFrom).next();
			scrollToNext();
		}
		
	}
}


function transferShopName(){
	var output = '';
	var id1col = 15;
	var id2col = 7;
	var shop1col = 14;
	var shop2col = 6;
	if($('#baseTable tr').length > 0){
		var cols = $('#baseTable tr')[0].cells.length;
		 $('#baseTable tr').each(function () { 
			 var checkbox1 = $('#'+ this.cells[cols-id1col].id);
			 var checkbox2 = $('#'+ this.cells[cols-id2col].id);
			 var shop1 = $('#'+  this.cells[cols-shop1col].id);
			 var shop2 = $('#'+  this.cells[cols-shop2col].id);
			 if(checkbox1.find("input").attr('checked') == 'checked' && checkbox2.find("input").attr('checked') == 'checked'){
				output += checkbox2.find("input").val() + "," + shop1.text() + "_";
				
				shop2.html("<redtag class='style'>" + shop1.text() + "</redtag>");
			 }
			 
		 });
	}
	$('#transferShop').val(output);
}

function initCheckBox(){
	$("#"+checkBoxStatus).attr("checked","checked");
 
}



$(function () {
    var opt = { }; 
    opt.date = {preset : 'date'};	    
 });
 

var leftcount = 0  ;
var rightcount = 0 ;
function initcheck(){
	
	$("[name='dbside'][checked]").each(function(){
		 leftcount++;
	    });

	 $("[name='uploadside'][checked]").each(function(){
		 rightcount++;
	    });
	 
	 $("#leftcount").text("匹配数量"+leftcount);
	$("#rightcount").text("匹配数量"+rightcount);
	
}

function initleftcount(obj){
	if($(obj).is(':checked')){
		leftcount++;
	}else {
		leftcount--;
	}
	$("#leftcount").text("匹配数量"+leftcount);

}

function initrightcount(obj){
	if($(obj).is(':checked')){
		rightcount++;
	}else {
		rightcount--;
	}
	$("#rightcount").text("匹配数量"+rightcount);
}

var startClick = 0 ;
function baseFormSubmit(){
	if(getCheckBox() == '00000'){
		alert('请至少选择一个对比项');
		$('#startbutton').val('对比');
		return false;
	} 
	if(startClick == 0){
		startClick = 1;
		return true;
	}else{ 
		$('#startbutton').attr('disabled','disabled');
		return false;
	}
}

function commited(){
	$("#startbutton").val("正在对比"); 
	$("#method").val("commited"); 
	$("#baseform").submit();  
	$("#startbutton").attr("disable","disable"); 
	$("#submitbutton").attr("disable","disable"); 
}

function confirmed(){ 
	$("#method").val("confirm");  
	var a = confirm("是否确认?");
		if (a != "0"){  
		$("#baseform").submit();
		$("#startbutton").attr("disable","disable"); 
		$("#submitbutton").attr("disable","disable"); 	
	}
	
}
</script>
 
  
<jsp:include flush="true" page="head.jsp">
<jsp:param name="dmsn" value="" />
</jsp:include>
    


 <form name="baseform" id="baseform" method="post" action ="uploadchange.jsp">
 <input type="hidden" name="method" id="method" value=""/>
 
 <table width="100%" >
	<tr>
		<td colspan="2" align="left">
		去除汉字对比 
		<input type="radio" name="ridiocheck" id="cn" value="cn"  checked="checked"></input>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		去除汉字英文字符对比
		<input type="radio" name="ridiocheck" id="en" value="en" ></input>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		模糊对比 
		<input type="radio" name="ridiocheck" id="all" value="all"  ></input>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		</td>
	</tr> 
</table>
 
    
<table style="width:100%;height:100%" align="center" border=0 id="table">
   <tr class="asc"> 
    <td align="center">
     &nbsp;&nbsp;&nbsp;&nbsp;
			<h3>上传单据</h3>
			<select name="uploadorder1" onchange="$('#submitbutton').attr('disabled','disabled');" >
				<%
				for(int i = 0 ; i < orderNames.size() ; i ++){
				%>
				<option value="<%=orderNames.get(i) %>" <%if(orderNames.get(i).equals(selectOrderName1)){ %>selected="selected" <%} %>><%=orderNames.get(i) %></option>
				<%
				}
				%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;
    
    </td>
   <td align="center">
   <label id="leftcount"></label>
   </td>
   <td align="center">
   <input type="button" id="startbutton" name="startbutton" value="对比" onclick="commited()"/>
			
			<br/>
			 
			
			<%
			if(showContent){
			%>
			<input type="button" value="导出" onclick="$('#baseform').attr('action','../MatchOrderExport');$('#baseform').submit()"/>
			<br/> 
			<input type="button" id="submitbutton" value="提交" onclick="confirmed()"/>
			<%
			}   
			%> 
   
   </td>
   <td align="center">

			 <button type="button" id="scrollNext">下一个</button><br/>
			 <button type="button" id="scrollPrev">上一个</button>
   
   
   </td>
   <td align="center">
   <button id="transferbutton" type="button">店名转换</button>
			
			 
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h3>上传单据</h3>
			<select name="uploadorder2" onchange="$('#submitbutton').attr('disabled','disabled');" >
				<%
				for(int i = 0 ; i < orderNames.size() ; i ++){
				%>
				<option value="<%=orderNames.get(i) %>" <%if(orderNames.get(i).equals(selectOrderName2)){ %>selected="selected" <%} %>><%=orderNames.get(i) %></option>
				<%
				}
				%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;
   
   </td>
   </tr>
   
   <tr class="asc">   

			<td align="center">选中</td>
			
			<td align="center">票面型号</td> 
			
			<td align="center">序号</td> 
			<td align="center">选中</td>
			
			<td align="center">票面型号</td> 
			
	 
		</tr> 
		
		 
		<%
		if(null != list && list.size() > 0 )
		for(int i = 0 ; i < list.size();i++	){
			MathChange m = list.get(i);
			//System.out.println(StringUtill.GetJson(m)); 
		%>  
		<tr class="asc">
			<td align="center" ><input checked="checked" <%if(dbsideDisabled) {%>disabled="disabled"<% }%> name="dbside"  type="checkbox" value="<%=m.getChange().getId()+"_"+m.getBechange().getId() %>"  onclick="initleftcount(this)" /></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="dbshop"><%= list.get(i).getChange().getName() %></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="dbshop"><%= i+1 %></td>
            <td align="center" ><input checked="checked" disabled="disabled" type="checkbox"   /></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=m.getBechange().getId() %>dbtype"><%= m.getBechange().getName() %></td> 
		</tr>
		 
		<%	
			
		}
		
		if(null != left){
			for(int i=0;i<left.size();i++){
				UploadChange up = left.get(i);
				%>
		<tr class="asc">
			<td align="center" ><input <%if(dbsideDisabled) {%>disabled="disabled"<% }%>   type="radio" value="<%=up.getId() %>"  name="radioleft" onclick="initleftcount(this)" /></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="dbshop"><%= up.getName() %></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="dbshop"><%= i+1 %></td>
            <td align="center" id="db"></td>
			<td align="center" ></td> 
		</tr>
				<%
			}
		}
		
		
		if(null != right){
			for(int i =0 ;i<right.size();i++){
				UploadChange up = right.get(i);
				%>
			<tr class="asc"> 
			<td align="center" ></td>
			<td align="center" ></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="dbshop"><%= i+1 %></td>
            <td align="center" ><input <%if(dbsideDisabled) {%>disabled="disabled"<% }%>  type="radio" value="<%=up.getId() %>"  name="radioright" onclick="initleftcount(this)" /></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=up.getId() %>dbtype"><%= up.getName() %></td> 
		</tr>
				
				
				<%
			}
		}
		%>
		
 </table>
    
 </form>


</body>
</html>
