<%@page import="utill.StringUtill"%>
<%@page import="org.apache.poi.util.StringUtil"%>
<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,wilson.salaryCalc.*,order.*,orderproduct.*,user.*,wilson.catergory.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	//js参数接受
	String selectModelsName = request.getParameter("selectModelsName");
	List<String> selectModelsNameList = new ArrayList<String>();
	if(!StringUtill.isNull(selectModelsName)){
		for(int i = 0 ; i < selectModelsName.split(",").length ; i ++){
			selectModelsNameList.add(selectModelsName.split(",")[i]);
		}
	}

	//selectModelsNameList中是否有参数?
	boolean hasSelectModelName = false;
	if(selectModelsNameList.size() > 0){
		hasSelectModelName = true;
	}
	
	//左侧参数接受
	String paraOrderName = request.getParameter("orders");

	String paraSave = request.getParameter("save");
	
	boolean showLeft = false;
	boolean showRight = false;
	boolean showResult = false;
	boolean isSave = false;
	
	showLeft = "true".equals(request.getParameter("showleft"))?true:false;
	showRight = "true".equals(request.getParameter("showright"))?true:false;
	showResult = "true".equals(request.getParameter("calcbutton"))?true:false;
	
	if("true".equals(request.getParameter("recalc")) || "true".equals(paraSave)){
		showResult = true;
	}
	
	//if("true".equals(paraSave)){
	//	isSave = false;
	//}
	
	List<UploadOrder> showOrders = new ArrayList<UploadOrder>();
	List<UploadSalaryModel> showSalaryModels = new ArrayList<UploadSalaryModel>();
	List<SalaryResult> salaryResult = new ArrayList<SalaryResult>();
	List<UploadOrder> unCalcUploadOrders = new ArrayList<UploadOrder>();
	
	if(paraOrderName != null && !paraOrderName.equals("")){
		showOrders = UploadManager.getCheckedOrdersByName(paraOrderName);
		showOrders = UploadManager.initMultiUploadOrder(showOrders);
	}
	
	
	showSalaryModels = UploadManager.getSalaryModelsByName(selectModelsNameList);
	

	//取要显示的order和salarymodel实例的集合
	//List<UploadOrder> uploadOrders = UploadManager.getCheckedUploadOrders();
	//List<UploadSalaryModel> salaryModels = UploadManager.getAllSalaryModel();
	
	//取出其中的名称
	List<String> orderNames = UploadManager.getCheckedUploadOrdersNames();
	orderNames.removeAll(UploadManager.getCalcedUploadOrdersNames());

	
	List<String> salaryModelsNames = UploadManager.getAllSalaryModelNames();
	String groupname = request.getParameter("selectGroup");
	
	//取出分组列表
	Map<String,List<CatergoryMaping>> srcCatergoryMap = CatergoryManager.getCatergoryMap();
	Set CatergoryMapingSet = srcCatergoryMap.keySet();
	
	//if(isSave){
	//		salaryResult = (ArrayList<SalaryResult>)request.getSession().getAttribute("calcResult");
	//		unCalcUploadOrders = (ArrayList<UploadOrder>)request.getSession().getAttribute("unCalcResult");
	//		//SalaryCalcManager.saveSalaryResult(salaryResult,groupname);
	//		response.sendRedirect("salaryCalc.jsp");
	//		return;
	//}
	
	//是否锁住groupname
	boolean lockGroupName = false;
	
	if(showResult){
		SalaryCalcManager.calcSalary(showOrders, showSalaryModels,request);
		ArrayList<SalaryResult> resultInSession = (ArrayList<SalaryResult>)request.getSession().getAttribute("calcResult");
		salaryResult = new ArrayList(resultInSession);
		unCalcUploadOrders = (ArrayList<UploadOrder>)request.getSession().getAttribute("unCalcResult");

		if("false".equals(groupname)){
			String tempGroupname = CatergoryManager.getCatergoryMapingByUploadOrderName(paraOrderName);
			if(!"".equals(tempGroupname)){
				lockGroupName = true;
				groupname = tempGroupname;
			}else{
				Iterator<String> tempIt = CatergoryMapingSet.iterator();  
				while (tempIt.hasNext()) {  
					String it = tempIt.next();
					if("空".equals(it)){
						groupname = it;
						break;
					}
					if(!tempIt.hasNext()){
						if(!groupname.equals("空")){
							groupname = it;
						}
					}
				}
			}
			
		}
		salaryResult = SalaryCalcManager.sortSalaryResult(salaryResult, groupname);
		session.setAttribute("addName_filename", paraOrderName);
		if(paraSave != null && !paraSave.equals("")){
			if(showOrders!=null &&showOrders.size() > 0 && showSalaryModels!=null&&showSalaryModels.size()>0){
				SalaryCalcManager.saveSalaryResult(salaryResult,groupname,unCalcUploadOrders);
				response.sendRedirect("salaryCalc.jsp");
				return;
			}
		}
	}
	
	
	
	
	
	session.setAttribute("selectModelsNameList", selectModelsNameList);
	if(showSalaryModels!= null && showSalaryModels.size() > 0 ){	
		session.setAttribute("altSalaryModel", showSalaryModels.get(0));
	}else{
		UploadSalaryModel tmp = new UploadSalaryModel();
		tmp.setShop("无");
		session.setAttribute("altSalaryModel", tmp);
	}
	
	//下面用到
	String tempString = "";
	//下面用到的背景色
    String backgroundColor ="#B9D3EE";
	
	//修改页面里面用到
	ArrayList<SalaryResult> modifiedSalaryResult = new ArrayList<SalaryResult>();
	session.setAttribute("modifiedSalaryResult", modifiedSalaryResult);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成计算页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
</head>

<body>
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
var selectModelsName = 	<%=StringUtill.GetJson(selectModelsNameList) %>;
	
$(function () {
	//初始化
	reloadSelectModels();
	initothers();
}); 

function initothers(){
	$('#groupname').val('<%=groupname%>');
	$('#save_selectModelsName').val(selectModelsName);
	$('#save_orders').val('<%=paraOrderName%>');
	$('#save_groupname').val('<%=groupname%>');
	$('#models').val(selectModelsName[selectModelsName.length - 1]);
}

function del(id,obj){
	obj.text('删除中');
	
	$.ajax({ 
        type:"post", 
         url:"salarymodelDelete.jsp",
         //data:"method=list_pic&page="+pageCount,
         data:"type=del&id="+id,
         dataType: "",  
         success: function (data) {
        	obj.parent().parent().remove();
           },  
          error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	  return false ;
            } 
           }); 
}

function reloadSelectModels(){
	var base = "已选的提成标准: ";
	for(var i = 0 ; i < selectModelsName.length ; i ++){
		base += "<br/><button onclick='delModel($(this))' style='background:url(../../image/closebutton.png) no-repeat; border:1;width:250px' value='" + selectModelsName[i] + "'>" + selectModelsName[i] + "</button>";
	}
	$('#selectModels').html(base);

	 $('#models option').each(function(){
	   if(selectModelsName.indexOf($(this).val()) >= 0 ){
	    $(this).remove();
	   }
	  });
}

function delModel(obj){
	var delItem = obj.val();
	for(var i = 0 ; i < selectModelsName.length ; i ++){
		if(delItem == selectModelsName[i]){
			selectModelsName.splice(i, 1);
			$('#models').append("<option value='" + delItem  + "'>"+ delItem + "</option>");
		}
	}
	reloadSelectModels();
	return true;
}

function addModel(){
	var addedItem = $('#models').val();
	if(addedItem != ""){
		for(var i = 0 ; i < selectModelsName.length ; i ++){
			if(addedItem == selectModelsName[i]){
				return false;
			}
		}
		selectModelsName.push(addedItem);
		reloadSelectModels();
		return true;
	}else{
		return false;
	}
	
}

function beforSubmit(){
	$('#selectModelsName').val(selectModelsName);
}
</script>

  
<jsp:include flush="true" page="head.jsp">
<jsp:param name="dmsn" value="" />
</jsp:include>

<table>
<tr>
<td align="left" valign="top">
	<table  cellspacing="1" border="2px" >
			<tr>
				<form method="post" action="" id="baseForm" onsubmit="beforSubmit()">
				<input id="selectModelsName" name="selectModelsName" value="" type="hidden"/>
				<td colspan="8" align="center">
				<h3>需要提成的订单</h3>
				&nbsp;&nbsp;
				<select name="orders"/>
				<%
				for(int i = 0 ; i < orderNames.size() ; i ++){
					tempString = orderNames.get(i);
					
				%>
				<option <%	if(tempString.equals(paraOrderName)){%>selected="selected"<% }%>  value="<%=tempString %>"><%=tempString %></option>
				}
				<%
				}
				%>
				&nbsp;&nbsp;
				<input type="hidden" name="showleft" value="<%=showLeft %>" id="showleft"/>
				<input type="submit" value="<%=showLeft?"隐藏":"显示" %>" id="showleftbutton" onclick="$('#selectGroup').val($('#groupname').val());$('#recalc').val('<%=showResult %>');if($('#showleft').val()=='false'){$('#showleft').val('true');$('showleftbutton').val('隐藏');}else{$('#showleft').val('false');$('showleftbutton').val('显示');}"/> 
				<!-- <input type="submit" value="显示" onclick="$('#showleft').val('true')"/>-->
				</td>
				
			</tr>
			<tr>  
				<td align="center">序号</td>
				<td align="center">销售门店</td>
				<td align="center">pos(厂送)单号</td>
				<td align="center">销售日期</td>
				<td align="center">型号</td> 
				<td align="center">票面数量</td> 
				<td align="center">供价</td>
				
			</tr>
			<%
			if(showLeft){
				for(int i = 0 ; i < showOrders.size() ; i ++){
			%>
				<tr>  
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=showOrders.get(i).getShop() %></td>
					<td align="center"><%=showOrders.get(i).getPosNo()%></td>
					<td align="center"><%=showOrders.get(i).getSaleTime() %></td>
					<td align="center"><%=showOrders.get(i).getType() %></td> 
					<td align="center"><%=showOrders.get(i).getNum() %></td> 
					<td align="center"><%=showOrders.get(i).getSalePrice() %></td>
					
				</tr>
			<%
				}
			}
			%>
	</table>
</td>
<td width="100px" align="center" valign="top"><button style="background-color:red;font-size:35px;" name="calcbutton" id="calcbutton" value="false" onclick="$('#calcbutton').val('true')">计算</button>
<input type="hidden" value="false" id="recalc" name = "recalc"/>
<input type="hidden" value="false" id="selectGroup" name="selectGroup"/>
</td>
<td valign="top">
	<table cellspacing="1" border="2px" id="salarymodels">
			<tr>
				
				<td colspan="9" align="center">
				<h3>提成标准</h3>
				&nbsp;&nbsp;
				<label id="selectModels" for="noid">已选的提成标准:</label>
				<br/>
				<select id="models" name="models">
				<option value="" selected="selected"></option>
				<%
				for(int i = 0 ; i < salaryModelsNames.size() ; i ++){
					tempString = salaryModelsNames.get(i);
				%>
				<option value="<%=tempString %>"><%=tempString %></option>
				<%
				}
				%>
				
				</select>
				&nbsp;&nbsp;
				<input type="hidden" name="showright" value="<%=showRight %>" id="showright"/>
				<button type="button" onclick="addModel()">添加</button>
				<input type="submit" value="<%=showRight?"隐藏":"显示" %>" id="showrightbutton" onclick="$('#selectGroup').val($('#groupname').val());$('#recalc').val('<%=showResult %>');if($('#showright').val()=='false'){$('#showright').val('true');$('showrightbutton').val('隐藏');}else{$('#showright').val('false');$('showrightbutton').val('显示');}"/> 
				<!-- <input type="submit" value="显示" onclick="$('#showright').val('true')"/>-->
				<%if(hasSelectModelName){ %>  
				<a href="#" onClick="javascript:window.open('salarymodelDetail.jsp?id=-1', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')"><input type="button" value="新增一行"/></a>
				<%} %>
				</td>
				</form>
			</tr>
			<tr>
				<td align="center">序号</td>
				<td align="center">文件名</td>
				<td align="center">门店</td>
				<td align="center">类别</td>
				<td align="center">型号</td>
				<td align="center">提成标准</td>
				<td align="center">生效日期</td>
				<td align="center">截至日期</td>
				<td align="center">删除</td>
		
			</tr> 
			<%
			if(showRight){
				for(int i = 0 ; i < showSalaryModels.size() ; i ++){
			%>
				<tr>
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=showSalaryModels.get(i).getName() %></td>
					<td align="center"><%=showSalaryModels.get(i).getShop() %></td>
					<td align="center"><%=showSalaryModels.get(i).getCatergory() %></td>
					<td align="center"><a href="#" onClick="javascript:window.open('salarymodelDetail.jsp?id=<%=showSalaryModels.get(i).getId() %>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')"><%=showSalaryModels.get(i).getType() %></a></td>
					<td align="center"><%=showSalaryModels.get(i).getPrintContent() %></td>
					<td align="center"><%=showSalaryModels.get(i).getStartTime() %></td>
					<td align="center"><%=showSalaryModels.get(i).getEndTime() %></td> 
					<td align="center"><button type="button" onclick="del(<%=showSalaryModels.get(i).getId() %>,$(this))">删除此项</button></td>
	
				</tr> 
			<%
				}
				if(showSalaryModels.size() > 0 ){		
			%>
				<tr>
					<td align="center" colspan="9"><a href="#" onClick="javascript:window.open('salarymodelDetail.jsp?id=-1', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no,height=320,width=850')"><input type="button" value="新增一行"/></a></td>
					
				</tr> 
			<%
				}
			}
			%>
	</table>
</td> 
</tr>
<%
if(showResult){
%>



<!-- control panel -->
<tr>
<td colspan="3" align="center">
<button name="catergoryMapingButton" style="background-color:red;font-size:35px;" onClick="javascript:window.open('./catergoryManage.jsp?groupname=' + $('#groupname').val(), 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')">分组管理</button>
当前分组:
<%if(!lockGroupName){ %>
<select id="groupname" name="groupname">
<%Iterator<String> it = CatergoryMapingSet.iterator(); 
  String itString = "";
	while(it.hasNext()){
		itString = it.next();
	
%>
	<option value="<%=itString %>"><%=itString %></option>
<%
	}
}else{
%>
<select id="groupname" name="groupname">
<option value="<%=groupname %>"><%=groupname %></option>
<%} %>
</select>
<button name="groupButton" style="background-color:red;font-size:35px;" onclick="$('#selectGroup').val($('#groupname').val());$('#recalc').val('true');$('#baseForm').submit()" >重新计算</button>

<!-- control panel ends -->




</td>
</tr>


<tr>
<td colspan="3" align="center">
	<table border="2px" align="center">
		<tr>
			<td align="center" colspan="13"><h3>提成结果</h3></td>
		</tr>
		<tr>
			<td align="center">序号</td>
			<td align="center">文件名</td>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">类别</td> 
			<td align="center">导购员</td> 
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
			<td align="center">提成标准</td>
			<td align="center">单价</td>	
			<td align="center">合计提成</td>
			<td align="center">修改</td>
		</tr>
		<%
		boolean total = false;
		for(int i = 0 ; i < salaryResult.size() ; i ++){
			if(salaryResult.get(i).getStatus() == SalaryResult.STATUS_TOTAL){
				total = true;
			}else{
				total = false;
			}
		%>
		<tr bgcolor='<%=total?backgroundColor:"" %>'>
			<td align="center"><%=i+1 %></td>
			<!-- 比较麻烦，但是不这样取不出来 -->
			<%
			if(i != salaryResult.size()-1){
			%>
				<td align="center"><%=total?"总计":salaryResult.get(i).getUploadOrder().getName() %></td>
			<%
			}else{
			%>
				<td align="center">文件总计</td>
			<%
			}
			%>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getShop() %></td>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getPosNo() %></td>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getSaleTime() %></td>
			<td align="center"><%=salaryResult.get(i).getSalaryModel().getCatergory() %></td> 
			<td align="center"><%=salaryResult.get(i).getSaleManName() %></td> 
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getType() %></td> 
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getNum() %></td> 
			<td align="center"><%=salaryResult.get(i).getSalaryModel().getContent() %></td>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getSalePrice() %></td>
			<td align="center"><%=salaryResult.get(i).getSalary() %></td>
			<!--  
			<%if(salaryResult.get(i).getStatus() >= 0){ %>
			<td align="center"><a href="#" onClick="javascript:window.open('./salaryResultDetailInSession.jsp?id=<%=salaryResult.get(i).getUploadOrder().getId() %>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')" ><button>修改</button></a></td>
			<%} %>
			-->
		</tr>	
		<%
		}
		%>
		
		<tr>
			<td align="center" colspan="13"><h3>无对应提成标准的订单</h3></td>
		</tr>
		
		<tr>
			<td align="center">序号</td>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">型号</td> 
			<td align="center">票面数量</td> 
			<td align="center">供价</td>
		</tr>
		<%
		for(int i = 0 ; i < unCalcUploadOrders.size() ; i ++){
			
		%>
		<tr>
			<td align="center"><%=i+1 %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getShop() %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getPosNo() %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getSaleTime() %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getTypeForCalc() %></td> 
			<td align="center"><%=unCalcUploadOrders.get(i).getNum() %></td> 
			<td align="center"><%=unCalcUploadOrders.get(i).getSalePrice() %></td>
		</tr>
		<%
		}
		%>
		
		<%if(salaryResult.size() > 0 )
		{ 
		%>
		<tr>
			<td align="center" colspan="12">
			<form method="post" action="" onsubmit="$('#catergorymaping').val($('#groupname').val());$('#submitButton').attr('disabled','true');">
			<input type="hidden"  name="save" value="true" />
			<input type="hidden"  id="save_groupname" name="selectGroup" value="" />
			<input type="hidden" id="save_orders" name="orders" value=""/>
			<input type="hidden" id="save_selectModelsName" name="selectModelsName" value="" />
			<input type="submit" value="提交保存" id="submitButton" onclick="return confirm('是否确认?')"/>
			</form>
			</td>	
		</tr>
		<%
		}
		%>
	</table>
</td>
</tr>
<%
}
%>

</table>

</body>
</html>
