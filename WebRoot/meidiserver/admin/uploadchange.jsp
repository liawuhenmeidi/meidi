<%@page import="com.sun.net.httpserver.HttpContext"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java"
	import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,order.*,change.*,orderproduct.*,branchtype.*,branch.*,utill.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	long startTime = System.currentTimeMillis();
	TokenGen.getInstance().saveToken(request);
	String token = (String)session.getAttribute("token"); 
	
	String method = request.getParameter("method");
	boolean showleft = false; 
	boolean showright = false; 
	boolean showContent = false;    
	List<String> ordersalesNames = UploadManager.getUnTotalUploadOrdersNames();
	//显示内容的开关  
	
	UploadChangeAll left = null;
	List<UploadOrder> right = null; 
	 
	List<UploadChange> uploadOrders = UploadChangeManager.getUnCheckedUploadOrders();
	List<String>  orderNames= UploadChangeManager.getAllUploadOrderNames(uploadOrders);
	String selectOrderName1 = ""; 
	String selectOrderName2 = "";  
	String checkBoxStatus = "";    
	Map<String,String> map  = new HashMap<String,String>(); 
    String mapstr = StringUtill.GetJson(map);
    String change = "{}";
    String statues = -1 +"";
    int row =   2 ;  
    Set<String> ri = new HashSet<String>(); 
    String source = "{}";
     
   // System.out.println(method);
	if("commited".equals(method)){   
		//接受两边的id    
		selectOrderName1 = request.getParameter("uploadorder1");
		selectOrderName2 = request.getParameter("uploadorder2");
		showContent = true;  
		if(StringUtill.isNull(selectOrderName2) || StringUtill.isNull(selectOrderName1)){
	         return ; 
		} 
		statues = request.getParameter("statues");
		//接受选定的对比参数    
		left = UploadChangeManager.getUnCheckedUploadOrders(selectOrderName1);
		
		UploadChangeAll leftnew = UploadChangeManager.getUnCheckedString(selectOrderName1);
 
		if(Integer.valueOf(statues) == 0){
	             source = StringUtill.GetJson(leftnew.getBranch());
		}else if(Integer.valueOf(statues) == 1){
	             source = StringUtill.GetJson(leftnew.getTypes());
		} 
		//System.out.println("source"+source); 
	    right = UploadManager.getTotalUploadOrders(selectOrderName2,-1+"",BasicUtill.send); 
		checkBoxStatus = request.getParameter("ridiocheck");
		//初始化要对比的orders      
		ri = UploadChangeManager.getsetbranch(right,Integer.valueOf(statues));
		  // System.out.println(ri.size());
		Map<String,String> mapchange = UploadChangeManager.getmatch(left, ri,checkBoxStatus,statues); 
		//System.out.println(StringUtill.GetJson(mapchange)); 
		change = StringUtill.GetJson(mapchange);  
		map.putAll(mapchange);  
		
		//System.out.println(StringUtill.GetJson(mapchange));
	}else if("RightSerach".equals(method)){
		showright = true ;
		selectOrderName2 = request.getParameter("uploadorder2");
		right = UploadManager.getTotalUploadOrders(selectOrderName2,-1+"",BasicUtill.send);
		map  = BranchTypeChange.getinstance().getMap();
		showContent = true;  
	}else if("searchleft".equals(method)){
		showleft = true ;
		selectOrderName1 = request.getParameter("uploadorder1");
		if(StringUtill.isNull(selectOrderName1)){
	         return ; 
		}  
		left = UploadChangeManager.getUnCheckedUploadOrders(selectOrderName1);
	}else if("confirm".equals(method)) {
		String datechange = request.getParameter("datechange");
		ChangeManager.save(datechange);      
	}else if("delleft".equals(method)){ 
		String[] datechange = request.getParameterValues("leftbranch");
		UploadChangeManager.delete(datechange); 
	}else if("delright".equals(method)){ 
		String[] datechange = request.getParameterValues("lefttype");
		UploadChangeManager.delete(datechange);  
	}else if("addleft".equals(method)){ 
		String branch = request.getParameter("addbranch");
		selectOrderName1 = request.getParameter("uploadorder1");
		UploadChangeManager.save(branch, selectOrderName1, 0);
		 
	}else if("addright".equals(method)){ 
		String branch = request.getParameter("addtype");
		selectOrderName1 = request.getParameter("uploadorder1");
		UploadChangeManager.save(branch, selectOrderName1, 1);
	}  
	
	System.out.println("自动对比页面取数据时间(毫秒) = " + (System.currentTimeMillis() - startTime));
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

<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../style/css/bass.css" />
<style>
redTag {
	color: red
}
</style>
<style type="text/css">
table {
	table-layout: fixed;
}

tr strong,tr td {
	white-space: normal
}
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
var change = <%=change%>; 
var source = <%=source%>;
//alert(source); 
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
  
function commited(statues){
	
	var s1 = $("#uploadorder1").val();
	var s2 = $("#uploadorder2").val();
	if(s1 == "" || s2 == "" || s1 == null || s2 == null){
		return ;
	}
	$(".mybutton").attr("disabled","disabled"); 
	$("#method").val("commited");
	$("#statues").val(statues);  
	$("#baseform").submit();  
	 
} 

function search(num){ 
	
	if(0 == num){
		var s1 = $("#uploadorder1").val();
		if(s1 == ""|| s1 == null){
			return ;
		}
		$("#method").val("searchleft"); 
	}else if( 1== num){
		var s2 = $("#uploadorder2").val();
		if(s2 == "" || s2 == null){
			return ;
		}
		$("#method").val("RightSerach"); 
	}
	$(".mybutton").attr("disabled","disabled");
	$("#baseform").submit();
	
}

function confirmed(){ 
	var date = JSON.stringify(change);
	$("#method").val("confirm");  
	var a = confirm("是否确认?");
		if (a != "0"){   
		$("#typebutton").attr("disable","disable"); 	
		$(".mybutton").attr("disabled","disabled"); 
		$("#datechange").val(date); 	
		$("#baseform").submit(); 
	} 
	
}

function doclick(num){
	var left = $("#left"+num).text();
	var right = $("#right"+num).text();
	var flag = $("#type"+num).attr("checked");

	if(flag == true || flag == "checked"){
		if("" == right || null == right){
			$("#type"+num).removeAttr("checked");
		}else {
			if("" == change[left] || "undefined" == change[left] || undefined == change[left] ){
				change[left] = right;
			}
		} 
	}else { 
		delete change[left];
	} 
}

function adddetail(num,real){ 
	$("#addpos").css("display","block");
	$("#num").val(num);
	$("#pos").val(real); 
 
}

function saveAddPOD(){
	var num = $("#num").val();
	var pos = $("#pos").val();  
    if($.inArray(pos, source) != -1){
    	$("#right"+num).text(pos);  
    	$("#addpos").css("display","none");
	}else { 
		alert("您添加的数据有问题");
	}
}

function deleteleft(num){
	
	var a = confirm("确定要删除吗?");
	if (a != "0"){    
		$(".mybutton").attr("disabled","disabled");
		if( 0 == num){
			$("#method").val("delleft");  
		}else if(1 == num){
			$("#method").val("delright");  
		}
		
		$("#baseform").submit(); 
} 
}


function addleft(num){
	$(".mybutton").attr("disabled","disabled");
	if( 0 == num){
		$("#method").val("addleft");  
	}else if(1 == num){
		$("#method").val("addright");  
	}
	
	$("#baseform").submit(); 
}

</script>


	<jsp:include flush="true" page="head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>



	<form name="baseform" id="baseform" method="post"  action="uploadchange.jsp">
		   <input type="hidden" name="method" id="method" value="" /> <input
			type="hidden" name="statues" id="statues" value="" /> <input
			type="hidden" name="datechange" id="datechange" value="" /> <input
			type="hidden" name="token" value="<%=token%>" />

		<table width="100%" >
			<tr >
				<td  align="center">去除汉字对比 <input type="radio"
					name="ridiocheck" id="cn" value="cn" checked="checked"></input>
					
				 </td>
				<td align="center">
					去除汉字英文字符对比 <input type="radio" name="ridiocheck" id="en" value="en"></input>
				</td>
					<td align="center">
					 模糊对比 <input
					type="radio" name="ridiocheck" id="all" value="all"></input>
				</td>
				<td>
				 
				</td>
				<td>
				</td>
			</tr>
		</table>

       <br/>
       
		<table style="width:100%;height:60%;" cellspacing="1"   id="table" align="center" border=0> 
			<tr class="asc">
				<td align="center" colspan=2>
				<input type="button" id="searchleft" name="searchleft" class="mybutton" value="查看"
					onclick="search(0)" />
					<h3>上传单据</h3>
					 <select name="uploadorder1" id="uploadorder1"  onchange="$('#submitbutton').attr('disabled','disabled');">
						<%
							for (int i = 0; i < orderNames.size(); i++) {
						%>
						  <option value="<%=orderNames.get(i)%>"
						     	<%if (orderNames.get(i).equals(selectOrderName1)) {%>
							     selected="selected" <%}%>><%=orderNames.get(i)%></option>
						<%
							}
						%>
				</select> &nbsp;&nbsp;&nbsp;&nbsp;
			</td>
				<td align="center">
					<table>
						<tr>
							<td align="center"><input type="button" id="branchbutton"
								class="mybutton" name="branchbutton" value="门店对比"
								onclick="commited(0)" />
							</td>
						</tr>
						<tr>
							<td align="center"><input type="button" class="mybutton"
								id="typebutton" name="typebutton" value="型号对比"
								onclick="commited(1)" />
							</td>
						</tr>
						<tr> 
							<td align="center">
								<%
									if (showContent) {
								%> 
			<input type="button" id="submitbutton" class="mybutton"
								value="提交" onclick="confirmed()" /> <%
 	}
 %>
							</td>
						</tr>

					</table></td>
				<td align="center" colspan=2><input type="button"  id="searchright" name="searchright" class="mybutton" value="查看"  onclick="search(1)" /> 
					<h3>销售数据</h3> 
					 <select name="uploadorder2" id="uploadorder2"  onchange="$('#submitbutton').attr('disabled','disabled');">
						<%  
							for (int i = 0; i < ordersalesNames.size(); i++) {
						%> 
						<option value="<%=ordersalesNames.get(i)%>"
							<%if (ordersalesNames.get(i).equals(selectOrderName2)) {%>
							selected="selected" <%}%>><%=ordersalesNames.get(i)%></option>
						<% 
							}
						%>
				  </select>
				</td>
			</tr>
     </table>
      
      
      <% if(showleft) {
    	   %>
    	     <table style="width:100%;height:100%;bgcolor:black" cellspacing="1"    align="center" border=0> 
    	       <tr>
    	          	<td valign="top"> 
					<table style="width:100%;" cellspacing="1" id="Ntable">
					    <tr class="asc">  
					    <td align="center" width="10%"> <input type="button" class="mybutton"  value="删除" onclick="deleteleft('0')"></input></td>
					    <td colspan=2 align="center" ><input type="text" value="" placeholder="输入门店" name="addbranch"></input> 
					    <input type="button" class="mybutton"  value="添加" onclick="addleft('0')"></input></td>
					    </tr>
						<tr class="asc">
						    <td  > </td>
                            <td align="center" width="10%">编号</td>
							<td align="center">门店</td>
						</tr>
						<%  int countb = 0 ;
							Iterator<UploadChange> itb = left.getBranchO().iterator();
							while (itb.hasNext()) {
								countb ++;
								UploadChange up = itb.next();
								String bl = up.getName();
						%> 
						<tr class="asc">
						    <td><input type="checkbox"  name="leftbranch" value="<%=bl %>" /> </td>
						    <td align="center"><%=countb %></td> 
							<td align="center"><%=bl%></td>
						</tr>
 
						<%
							}
						%>

					</table>
			</td>
    	       <td valign="top">
					<table style="width:100%;" cellspacing="1"  id="Ntable">
					    <tr class="asc"> 
					    <td align="center" width="10%"> <input type="button" class="mybutton"  value="删除" onclick="deleteleft('1')"></input></td>
					    <td colspan=2 align="center" ><input type="text" value="" placeholder="输入型号" name="addtype" ></input> 
					    <input type="button" class="mybutton"  value="添加" onclick="addleft('1')"></input></td>
					    </tr>
					
					
						<tr class="asc">
						   <td width="10%"></td>
                            <td align="center" width="10%">编号</td>
							<td align="center">型号</td>
						</tr>
						<%   
						   int countt = 0;
							Iterator<UploadChange> itt = left.getTypesO().iterator();
							while (itt.hasNext()) {
								countt++;
								String bl = itt.next().getName();
								
						%>
						<tr class="asc">
						<td><input type="checkbox"  name="lefttype" value="<%=bl %>" /> </td>
						    <td align="center"><%=countt %></td> 
							<td align="center"><%=bl%></td>
						</tr>

						<%
							}
						%>

					</table>
					</td>
    	       </tr>
    	   
    	   </table>
    	   
    	   <%
      }
      
      
      if(showright){ 
    	    Set<String> rightb = new HashSet<String>();
			Set<String> rightt = new HashSet<String>();
			
			Map<String, String> mapnew = BranchTypeChange.getinstance()
					.getMap();
			Iterator<UploadOrder> it = right.iterator();
			while (it.hasNext()) {
				UploadOrder up = it.next();
				rightb.add(up.getShop());

				String sendtypestr = up.getSendType();
				String[] sendtypestrs = sendtypestr.split(",");
				for (int j = 0; j < sendtypestrs.length; j++) {
					String sendtype = sendtypestrs[j];
					// System.out.println(sendtypestrs[j]);
					String[] sendtypes = sendtype.split(":");
					String realtype = sendtypes[0];
					rightt.add(realtype);
				}

			}
			
			%>
			<table width="100%"> 
    	     <tr class="asc"> 
    	     <td valign="top" align="center">
    	     <table cellspacing="1"  id="Ntable" width="100%">
			<%
			 
			Iterator<String> itbr = rightb.iterator();
			int countbranch = 0;
			while (itbr.hasNext()) {
				String bl = itbr.next();
				countbranch++;
				String ischecked = "";
				String realtype = map.get(bl);
				if (StringUtill.isNull(realtype)) {
					realtype = "";
					ischecked = "bgcolor=\"red\"";
				}
				%>
				<tr class="asc"> 
				 <td align="center" width="10%">
				 <%=countbranch %>
				 </td>
				<td align="center">
				 <%=bl%>
				 </td>
				 <td align="center" <%=ischecked %>>
				 <%=realtype%>
				 </td>
				</tr>
				
				
				<%
			}
    	%>
    	  
    	     </table>
    	     </td>
    	     <td valign="top" align="center">
    	      <table cellspacing="1"  id="Ntable" width="100%">
    	       <%
    	         Iterator<String> ittr = rightt.iterator();
				int counttype = 0;
				while (ittr.hasNext()) {
					counttype++;
					String bl = ittr.next();
					String ischecked = "";
					String realtype = map.get(bl);
					if (StringUtill.isNull(realtype)) {
						realtype = "";
						ischecked = "bgcolor=\"red\"";
					}
					%>
					
				<tr class="asc" > 
				 <td align="center" width="10%">
				 <%=counttype %>
				 </td> 
				<td align="center">
				 <%=bl%>
				 </td>
				  <td align="center" <%=ischecked %>>
				 <%=realtype%>
				 </td>
				</tr>
					<%
					
				}
    	       %>
    	      </table>
    	     
    	     </td>
    	     </tr>
    	  </table>
    	 
    	 
    	
    	  <%
         }
      
       if(showContent){
    	   Set<String> rightb = new HashSet<String>();
			Set<String> rightt = new HashSet<String>();
			Map<String, String> mapnew = BranchTypeChange.getinstance()
					.getMap();
			Set<String> db = mapnew.keySet();
			//System.out.println(db);
			Iterator<UploadOrder> it = right.iterator();
			while (it.hasNext()) {
				UploadOrder up = it.next();
//System.out.println(up.getShop());
				if (!db.contains(up.getShop())) {
					rightb.add(up.getShop());
				}

				String sendtypestr = up.getSendType();
				String[] sendtypestrs = sendtypestr.split(",");
				for (int j = 0; j < sendtypestrs.length; j++) {
					String sendtype = sendtypestrs[j];
					// System.out.println(sendtypestrs[j]);
					String[] sendtypes = sendtype.split(":");
					String realtype = sendtypes[0];
					if (!db.contains(realtype)) {
						rightt.add(realtype);
					}
				}

			}
			
    	   if( 0 == Integer.valueOf(statues)){
    		   %>
    		   <table> 
    		     <tr >
    		       <td valign="top" align="center" width="30%">
    		       <table style="width:100%;" cellspacing="1"  id="Ntable" >
    		       <tr class="bsc">
				           	<td align="center">门店</td>
				        </tr>
    		      <% 
    		      Iterator<UploadChange> itb = left.getBranchO().iterator();
					while (itb.hasNext()) {
						String bl = itb.next().getName();
			            	%>
				        <tr class="asc">
				           	<td align="center"><%=bl%></td>
				        </tr>

				<%
					}
    		      %>
    		       
    		      </table> 
    		      </td>
    		      <td valign="top" align="center" width="70%"> 
                      <table style="width:100%;" cellspacing="1"  id="Ntable" >
						<tr class="asc">
							<td align="center" colspan=3>门店转化</td>
							<td align="center">点击添加</td>
						</tr>
						<% 
							Iterator<String> itbr = rightb.iterator();
								int countbranch = 0;
								String str = "branch";
								while (itbr.hasNext()) {
									String bl = itbr.next();
									countbranch++;
									String ischecked = "";
									String realtype = map.get(bl);
									if (StringUtill.isNull(realtype)) {
										realtype = "";
									} else {
										ischecked = "checked=checked";
									}
						%>
						<tr class="asc"> 
							<td width="10%"><input type="checkbox" id="type<%=str + countbranch%>"
								name="type" <%=ischecked%> 
								onclick="doclick('<%=str + countbranch%>')" /></td>
								<td width="10%"><%=countbranch%></td>
							<td><label id="left<%=str + countbranch%>"><%=bl%></label></td>
							<td onclick="adddetail('<%=str + countbranch%>','<%=realtype%>')">
								<label id="right<%=str + countbranch%>"><%=realtype%></label></td>
						</tr>

						<%
							}
						%>
					</table>
					</td>
    		     </tr>
    		   
    		   </table>
    		   
    		   <%
    		   
    		   
    	   }else if(1 == Integer.valueOf(statues)){
    		   %>
    		   <table>
    		   <tr> 
    		   <td valign="top" width="30%">
					<table style="width:100%;" cellspacing="1"  id="Ntable">
						<tr class="asc">
							<td align="center">型号</td>
						</tr>
						<%
							Iterator<UploadChange> itt = left.getTypesO().iterator();
							while (itt.hasNext()) {
								String bl = itt.next().getName();
						%>
						<tr class="asc" >
							<td align="center"><%=bl%></td>
						</tr>

						<%
							}
						%>

					</table>
					</td>
					<td valign="top" colspan=<%=row%> width="70%">
					<table style="width:100%;" cellspacing="1" id="Ntable">
						<tr class="asc">
							<td align="center" colspan=3>型号转化</td>
							<td align="center">点击添加</td>
						</tr>
						<%
							Iterator<String> ittr = rightt.iterator();
								int counttype = 0;
								String str = "type";
								while (ittr.hasNext()) {
									counttype++;
									String bl = ittr.next();
									String ischecked = "";
									String realtype = map.get(bl);
									if (StringUtill.isNull(realtype)) {
										realtype = "";
									} else {
										ischecked = "checked=checked";
									}
									//System.out.println(bl);
									// System.out.println(map);
						%>
						<tr class="asc">
							<td width="10%"><input type="checkbox" id="type<%=str + counttype%>"
								name="type" <%=ischecked%>
								onclick="doclick('<%=str + counttype%>')" /></td>
							<td width="10%"><%=counttype%></td>
							<td><label id="left<%=str + counttype%>"><%=bl%></label></td>
							<td onclick="adddetail('<%=str + counttype%>','<%=realtype%>')">
								<label id="right<%=str + counttype%>"><%=realtype%></label></td>
						</tr>

						<%
							}
						%>
					</table>
					</td>
    		   </tr>
    		   </table>
    		   <%
    	   }
       }
      
      %>         
	</form> 

	<div id="addpos" style="display:none">
		<div
			style="position:fixed;text-align:center; top:65%;background-color:white; left:30%; margin:-20% 0 0 -20%; height:30%; width:60%; z-index:999;">
			<br /> <input type="hidden" id="num" />
			<table cellspacing="1"
				style="margin:auto;background-color:black; width:80%;height:80%;">
				<tr class="bsc">
					<td align="center" colspan=2><input type="text" name="pos"
						id="pos" /></td>
				</tr>
				<tr class="bsc">

					<td class="center"><input type="button"
						onclick="$('#addpos').css('display','none');"
						style="background-color:#ACD6FF;font-size:25px;width:200px"
						value="取消" />
					</td>
 
					<td class="center"><input type="button"
						onclick="saveAddPOD()"
						style="background-color:#ACD6FF;font-size:25px;width:200px"
						value="确定" />
					</td>
				</tr>

			</table>
		</div>
	</div>
</body>
</html>
