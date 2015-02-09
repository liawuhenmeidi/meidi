<%@page import="java.net.URLEncoder"%>

<%@ page language="java" import="java.util.*,wilson.upload.*,change.*,net.sf.json.JSONObject,uploadtotalgroup.*,utill.*,wilson.matchOrder.*,uploadtotal.*,user.*,wilson.salaryCalc.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%  
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	    
	List<String> orderNames = UploadManager.getUnTotalUploadOrdersNames();
	Map<String,String> map = BranchTypeChange.getinstance().getMap();
	//System.out.println(StringUtill.GetJson(map)); 
	Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
	String message = ""; 
	UploadTotalGroup upt = UploadTotalGroupManager.getUploadTotalGroup();
	if(upt != null){
	   message = upt.getCategoryname(); 
	}
	
	
	String type = request.getParameter("type");
	String id = request.getParameter("said");
	String checkedStatus = "";
	boolean check = false ;
	boolean total = false ;
	Map<String,Map<String,List<UploadTotal>>> mapt = null ;
	Map<String,Map<String,List<UploadTotal>>> mapc = null ;
	//Map<String, HashMap<String, UploadTotal>> map = null; 
	//HashMap<String, UploadTotal> maptypeinit = null;
	HashMap<String, List<UploadTotal>> maptypeinit = null;
	List<UploadOrder> list = null ;   
	if("check".equals(type)){   
		 checkedStatus = request.getParameter("checkedStatus");
		 list = UploadManager.getTotalUploadOrders(id,checkedStatus,BasicUtill.send);  
		 check = true ;  
	}else if("total".equals(type)){  
		total = true ;    
		checkedStatus = request.getParameter("realcheckedStatus");
		mapt = UploadManager.getTotalOrdersGroup(id,BasicUtill.send,checkedStatus);  
	}else if("typetotal".equals(type)){  
		total = true ;    
		checkedStatus = request.getParameter("realcheckedStatus");
		maptypeinit = UploadManager.getTotalOrdersGroup(id,"type",BasicUtill.send,checkedStatus);
	}else if("totalcategory".equals(type)){
		total = true ;  
		checkedStatus = request.getParameter("realcheckedStatus");
		mapc = UploadManager.getTotalOrdersCategoryGroup(id,BasicUtill.send,checkedStatus);
	} 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传管理页面</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style media=print type="text/css">   
.noprint{visibility:hidden}   
</style>
<script type="text/javascript">

function amortization(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
} 

function detail(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function println(){
	$("#wrapsearch").css("display","block");
} 

function checkedorder(){
	 
	$("#wrapsearch").css("display","block");
	$("#tablechecked").css("display","block");
}   
 
function changeprintln(){
	 
    $('input[name="oderStatus"]').not("input:checked").each(function(){
    	//alert($(".noprinln"+$(this).val()));
    	// $(".noprinln"+$(this).val()).attr("class","noprint");
    	$(".noprinln"+$(this).val()).css("display","none");
        
    }); 
	$("#wrapsearch").css("display","none");
	 window.print();
}

function changechecked(){
	$('#post').attr('action',''); 
	$("#wrapchecked").css("display","none");
	
	$("#post").submit();
} 

function checkedd(){
	
}
 
function adddanwei(){ 
	$("#wrapsearch").css("display","block");
	$("#wanglaidanwei").css("display","block");
	
}
function exports(){
	var branch = $("#danwei").val();
	var uname = $("#danweiuname").val();
	if(branch == ""){ 
		alert("往来单位不能为空");
		return ;
	}
	 
	if(uname == ""){
		alert("经手人不能为空");
		return ;
	}
	$('#method').val('<%=type %>');
	$('#post').attr('action','../GuanJiaPoPrint?type=<%=BasicUtill.send%>'); 
	$("#post").submit();
	$("#wrapsearch").css("display","none");
	$("#wanglaidanwei").css("display","none");
	
}

</script>
</head> 

<body>
   <form action="" method="post" id="post" onsubmit="return checkedd()">
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型号转化: 
   <input type="hidden" name="type" id="type" value="check"/>
   <select name="said" id="said">
	<option ></option> 
	<%
	for(int i = 0 ; i < orderNames.size() ; i ++){ 
		String sain  = orderNames.get(i);
		if(sain.equals(id)){
			%>
			<option value="<%=sain %>" selected="selected"><%=sain %></option>
			<%
		}else { 
			%>
			<option value="<%=sain %>" ><%=sain %></option>
			<%
		}
	} 
	%>
</select>
<input type="hidden" name="realcheckedStatus" id="realcheckedStatus" value=""/>
<input type="button" value="查看" class="noprint" onclick="checkedorder()"/>
 
<% if(check){ %>
 
<input type="button" class="button" value="设置标准" onclick="amortization('saleCountGroup.jsp')" ></input>
<input type="submit" value="品类门店统计" onclick="$('#type').val('totalcategory');$('#realcheckedStatus').val('<%=checkedStatus%>')"/>
<input type="submit" value="门店品类统计" onclick="$('#type').val('total');$('#realcheckedStatus').val('<%=checkedStatus%>')"/>
<input type="submit" value="型号统计" onclick="$('#type').val('typetotal');$('#realcheckedStatus').val('<%=checkedStatus%>')"/>
<%} 
if(total){ %>  
<input type="hidden" name="method" id="method" value=""/> 
<input type="button" class="noprint"  value="导出" onclick="adddanwei()"/>
<input type="button" class="noprint"  value="打印" onclick="println()" ></input>
<%}  
%> 

 <div id="wrapsearch" style="position:fixed;text-align:center; top:50%;background-color:white; left:30%; margin:-20% 0 0 -20%; height:50%; width:50%; z-index:999;display:none"> 
 <div id="tablesearch" style="display:none">
 <table  cellspacing="1" style="margin:auto;background-color:black; width:80%;height:80%;">  
		   
		<tr class="bsc">
			<td align="center">
			请选择需要打印的信息
			</td>
		</tr>  
 
		<tr class="bsc">
		<td align="center" id="salecategorynamecd">
		    <input type="checkbox"  name="oderStatus"  value="1"  />序号&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="2"  />门店&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="11"  />转化后门店&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="3" />品类&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="4" />型号&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="10" />转化后型号&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="5"  />单价&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="6"  />数量&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="7"  />销售金额&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="8"  />扣点后单价&nbsp;&nbsp;<br/>
		    <input type="checkbox"  name="oderStatus"  value="9"  />扣点后金额&nbsp;&nbsp;<br/>
		</td>	
		
		</tr> 
		<tr class="bsc">
			<td class="center" ><input type="button" onclick="changeprintln()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="确定" /></td>
		</tr>
	
</table> 
 </div>
<div id="tablechecked" style="display:none">

<table   cellspacing="1" style="margin:auto;background-color:black; width:80%;height:80%;">  
		    
		<tr class="bsc">
			<td align="center">
			请选择显示上传销售单与上报销售单的对比结果
			</td>
		</tr> 
 
		<tr class="bsc">
		<td align="center" id="salecategorynamecd">
		    <input type="radio"  name="checkedStatus"  value="0"  />已对比&nbsp;&nbsp;<br/>
		    <input type="radio"  name="checkedStatus"  value="1"  />未对比&nbsp;&nbsp;<br/>
		    <input type="radio"  name="checkedStatus"  value="-1" />全部类&nbsp;&nbsp;<br/>
		</td>	
		
		</tr> 
		<tr class="bsc">
			<td class="center" ><input type="button" onclick="changechecked()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="确定" /></td>
		</tr>
	
</table> 

</div>

 <div id="wanglaidanwei" style="display:none">
 <table  cellspacing="1" style="margin:auto;background-color:black; width:80%;height:80%;">
   <tr class="bsc"> 
       <td>往来单位： </td> 
       <td> <input type="text" name="branch" id="danwei" placeholder="必填"/> </td>
   </tr> 
   <tr class="bsc"> 
       <td>经手人： </td>
       <td> <input type="text" name="name" id="danweiuname" placeholder="必填"/> </td>
   </tr> 
   
   
   <tr class="bsc" > 
			<td class="center" colspan=2><input type="button" onclick="exports()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="确定" /></td>
		</tr>
 </table>
      
 
 </div>

</div>
 </form>
<% if(check){ %>
<table border="1px" align="left" width="100%">
       <tr>
		        <td align="center">序号</td>
				<td align="center">销售门店</td>
				<td align="center">转化后门店</td>	
				<td align="center">销售日期</td>
				<td align="center">品类</td> 
				<td align="center">送货型号</td> 
				<td align="center">转化后型号</td>		   
				<td align="center">单价</td>
				<td align="center">送货数量</td> 
				<td align="center">供价</td>
				<td align="center">扣点</td>
				<td align="center">扣点后单价</td>
				<td align="center">扣点后价格</td>
		</tr>
 
 <%
    int count = 0;
	double moneycount = 0 ;
	double bpmoneycount = 0 ;
	
   if(null != list){  
	for(int i = 0 ; i < list.size() ; i ++){ 
		UploadOrder sain  = list.get(i);
		String tpe = "";
		if(null != mapus){
			UploadSalaryModel up = mapus.get(StringUtill.getStringNocn(sain.getType()));
			if(null != up){
				tpe = up.getCatergory(); 
			}
		}   
		   
		 //System.out.println(sain.getId());
		 String sendtypestr = sain.getSendType();
		 String[] sendtypestrs = sendtypestr.split(",");
		 for(int j=0;j<sendtypestrs.length;j++){
			 String sendtype = sendtypestrs[j];
			// System.out.println(sendtypestrs[j]);
			 String[] sendtypes = sendtype.split(":");
			 String realtype = sendtypes[0]; 
			 Double realcount = Double.valueOf(sendtypes[1]);
			 Double prince = Math.abs(Double.valueOf(sendtypes[2]));
			 count += realcount;  
			 moneycount += prince*realcount;
			 bpmoneycount += prince*realcount*(1-sain.getBackPoint()/100);
			 
	%>
	<tr class="asc"  onclick="updateClass(this)"> 
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=sain.getShop() %></td>
					<% if(null==map.get(sain.getShop())){
						%>
						<td align="center"  bgcolor="red"><%=sain.getShop()%></td>
					  <%  
					}else {
						%>  
						<td align="center"><%=map.get(sain.getShop()) %></td>
					<%
					}%>
					
					<td align="center"><%=sain.getSaleTime() %></td>
					<td align="center"><%=tpe %></td> 
					<td align="center"><%=realtype %></td> 
					
					<% if(null==map.get(realtype)){
						%>
						<td align="center" bgcolor="red"><%=realtype%></td>
					  <% 
					}else { 
						%> 
						<td align="center" ><%=map.get(realtype) %></td>
					<%
					}%>    
					<td align="center"><%=DoubleUtill.getdoubleTwo(prince)  %></td>
					<td align="center"><%=realcount %></td>  
					<td align="center"><%=DoubleUtill.getdoubleTwo(prince*realcount) %></td>  
					<td align="center"><%=sain.getBackPoint() %></td>  
					<td align="center"><%=DoubleUtill.getdoubleTwo(prince*(1-sain.getBackPoint()/100)) %></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(prince*realcount*(1-sain.getBackPoint()/100)) %></td>
	</tr>
	<%
		 }
	} 
   }
	%>
 
 <tr class="asc"  style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"><%=count %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(moneycount)%></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(bpmoneycount)%></td>
	</tr>
 

</table>
<%} %>


<% if(total){ %>
<table border="1px" align="left" width="100%">
       <tr>
		        <td align="center" class="noprinln1">序号</td>
				<td align="center" class="noprinln2">门店</td>
				<td align="center" class="noprinln11">转化后门店</td>	
				<td align="center" class="noprinln3">品类</td> 
				<td align="center" class="noprinln4">型号</td>
				<td align="center" class="noprinln10">转化后型号</td>		 
				<td align="center" class="noprinln5">单价</td> 
				<td align="center" class="noprinln6">数量</td> 
				<td align="center" class="noprinln7">销售金额</td>
				<td align="center" class="noprinln8">扣点后单价</td>
				<td align="center" class="noprinln9">扣点后金额</td>
		</tr>

 <% 
   int idcount = 0 ;
   double AllTotalcount = 0 ;
   int AllCount = 0 ;
   double AllTatalbreakcount = 0 ;
   
   
   if(null != mapt){ 
	   Set<Map.Entry<String,Map<String,List<UploadTotal>>>> setmap = mapt.entrySet();
	   Iterator<Map.Entry<String,Map<String,List<UploadTotal>>>> itmap = setmap.iterator();
	   while(itmap.hasNext()){
		   Map.Entry<String,Map<String,List<UploadTotal>>> enmap = itmap.next();
		   Map<String,List<UploadTotal>> maptype = enmap.getValue();
		   Set<Map.Entry<String,List<UploadTotal>>> setmaptype =  maptype.entrySet();
		   Iterator<Map.Entry<String,List<UploadTotal>>> itmaptype = setmaptype.iterator();
		   double Totalcount = 0 ;
		   int Count = 0 ;
		   double Tatalbreakcount = 0 ;
		   String branchname = "";
		   while(itmaptype.hasNext()){
			   Map.Entry<String,List<UploadTotal>> enmaptype = itmaptype.next();
			   String key = enmaptype.getKey();
			   if(!StringUtill.isNull(message)){
					JSONObject jsObj = JSONObject.fromObject(message);
					Iterator<String> it = jsObj.keys();
					while(it.hasNext()){ 
						String t = it.next();
						if(key.equals(t)){ 
							key = jsObj.getString(key);
						}
					}
				}
			   
			   List<UploadTotal> listup = enmaptype.getValue();
			   double initTotalcount = 0 ;
			   int initCount = 0 ;
			   double initTatalbreakcount = 0 ;
			   if(null != listup){
				   for(int i=0;i<listup.size();i++){
					   UploadTotal up = listup.get(i);
					   branchname = up.getBranchname();
					   Totalcount += up.getTotalcount();
					   Count += up.getCount();
					   Tatalbreakcount += up.getTatalbreakcount();
					   AllTotalcount += up.getTotalcount();
					   AllCount += up.getCount();
					   AllTatalbreakcount += up.getTatalbreakcount();
					   initTotalcount += up.getTotalcount();
					   initCount += up.getCount();
					   initTatalbreakcount += up.getTatalbreakcount();
					   idcount ++;
					   
					   String tpe = "";
						if(null != mapus){
							UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
							if(null != ups){
								tpe = ups.getCatergory(); 
							}
						}
		  %>  
		  
		   <tr class="asc"  ondblclick="detail('uploadSendCountDetail.jsp?branch=<%=up.getBranchname() %>&type=<%=up.getType() %>&said=<%=id %>&totaltype=<%=BasicUtill.send %>&checkedStatus=<%=checkedStatus %>')" onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"><%=idcount %></td>
					<td align="center" class="noprinln2"><%=up.getBranchname() %></td>
					<% if(null==map.get(up.getBranchname())){
						%>
						<td align="center" bgcolor="red" class="noprinln11"><%=up.getBranchname()%></td>
					  <% 
					}else {
						%> 
						<td align="center" class="noprinln11"  ><%=map.get(up.getBranchname()) %></td>
					<%
					}%>
					
					<td align="center" class="noprinln3"><%=tpe%></td>
					<td align="center" class="noprinln4"><%=up.getType()%></td>
					<% if(null==map.get(up.getType())){
						%>
						<td align="center" bgcolor="red" class="noprinln10"><%=up.getType()%></td>
					  <% 
					}else {
						%> 
						<td align="center"  class="noprinln10"><%=map.get(up.getType()) %></td>
					<%
					}%>    
					<td align="center" class="noprinln5"><%=0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()) %></td>
					<td align="center" class="noprinln6"><%=up.getCount() %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(up.getTotalcount()) %></td>  
					<td align="center" class="noprinln8"><%=0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTatalbreakcount()/up.getCount()) %></td> 
					<td align="center" class="noprinln9" ><%=DoubleUtill.getdoubleTwo(up.getTatalbreakcount()) %></td> 
	       </tr>
		   
		 <%  
			   } 
			   
		   }
      %>
      
        <tr class="asc"  style="background:orange"  onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"></td>
					<td align="center" class="noprinln2"><%=branchname %></td>
					<td align="center" class="noprinln11"></td> 
					<td align="center" class="noprinln3"><%=key%></td>
					<td align="center" class="noprinln4"></td>
					
					<td align="center" class="noprinln5"></td>
					<td align="center" class="noprinln10"></td>
					<td align="center" class="noprinln6"><%=initCount %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(initTotalcount) %></td> 
					<td align="center" class="noprinln8"></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(initTatalbreakcount) %></td> 
	     </tr>
      
      <%
	   }
	   
	   %>
	   <tr class="asc" style="background:#ff7575" ondblclick="unconfire('<%=branchname%>')" onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"></td>
					<td align="center" class="noprinln2"><%=branchname %></td>
					<td align="center" class="noprinln11"></td>
					<td align="center" class="noprinln3">总计</td>
					<td align="center" class="noprinln4"></td>
					<td align="center" class="noprinln5"></td>
					<td align="center" class="noprinln10"></td>
					<td align="center" class="noprinln6"><%=Count %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(Totalcount) %></td> 
					<td align="center" class="noprinln8"></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(Tatalbreakcount) %></td> 
	    </tr>
	   
	   
	   <%
      } 
  } 
     
   if(null != mapc){ 
	   Set<Map.Entry<String,Map<String,List<UploadTotal>>>> setmap = mapc.entrySet();
	   Iterator<Map.Entry<String,Map<String,List<UploadTotal>>>> itmap = setmap.iterator();
	   while(itmap.hasNext()){
		   Map.Entry<String,Map<String,List<UploadTotal>>> enmap = itmap.next();
		   String key = enmap.getKey();
		   
		   if(!StringUtill.isNull(message)){
				JSONObject jsObj = JSONObject.fromObject(message);
				Iterator<String> it = jsObj.keys();
				while(it.hasNext()){ 
					String t = it.next();
					if(key.equals(t)){ 
						key = jsObj.getString(key);
					}
				}
			}
		   
		   Map<String,List<UploadTotal>> maptype = enmap.getValue();
		   Set<Map.Entry<String,List<UploadTotal>>> setmaptype =  maptype.entrySet();
		   Iterator<Map.Entry<String,List<UploadTotal>>> itmaptype = setmaptype.iterator();
		   double Totalcount = 0 ;
		   int Count = 0 ;
		   double Tatalbreakcount = 0 ;
		   while(itmaptype.hasNext()){
			   Map.Entry<String,List<UploadTotal>> enmaptype = itmaptype.next();
			   List<UploadTotal> listup = enmaptype.getValue();
			   String branchname = enmaptype.getKey();
			   double initTotalcount = 0 ;
			   int initCount = 0 ;
			   double initTatalbreakcount = 0 ;
			   if(null != listup){
				   for(int i=0;i<listup.size();i++){
					   UploadTotal up = listup.get(i);
					   Totalcount += up.getTotalcount();
					   Count += up.getCount();
					   Tatalbreakcount += up.getTatalbreakcount();
					   AllTotalcount += up.getTotalcount();
					   AllCount += up.getCount();
					   AllTatalbreakcount += up.getTatalbreakcount();
					   initTotalcount += up.getTotalcount();
					   initCount += up.getCount();
					   initTatalbreakcount += up.getTatalbreakcount();
					   idcount ++;
					   
					   String tpe = "";
						if(null != mapus){
							UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
							if(null != ups){
								tpe = ups.getCatergory(); 
							}
						}
		  %>  
		  
		   <tr class="asc"  ondblclick="detail('uploadSendCountDetail.jsp?branch=<%=up.getBranchname() %>&type=<%=up.getType() %>&said=<%=id %>&totaltype=<%=BasicUtill.send %>&checkedStatus=<%=checkedStatus %>')" onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"><%=idcount %></td>
					<td align="center" class="noprinln2"><%=up.getBranchname() %></td>
					<td align="center" class="noprinln11"><%=up.getRealbranchname() %></td>
					<td align="center" class="noprinln3"><%=tpe%></td>
					<td align="center" class="noprinln4"><%=up.getType()%></td>
					<td align="center" class="noprinln10"><%=up.getRealtype() %></td>  
					<td align="center" class="noprinln5"><%=0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()) %></td>
					<td align="center" class="noprinln6"><%=up.getCount() %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(up.getTotalcount()) %></td>  
					<td align="center" class="noprinln8"><%=0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTatalbreakcount()/up.getCount()) %></td> 
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(up.getTatalbreakcount()) %></td> 
	       </tr>
		   
		 <%  
			   } 
			   
		   }
      %>
      
        <tr class="asc"  style="background:orange"  onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"></td>
					<td align="center" class="noprinln2"><%=branchname %></td>
						<td align="center" class="noprinln11"></td>
					<td align="center" class="noprinln3"><%=key%></td>
					<td align="center" class="noprinln4"></td>
					<td align="center" class="noprinln5"></td>
					<td align="center" class="noprinln10"></td>
					<td align="center" class="noprinln6"><%=initCount %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(initTotalcount) %></td> 
					<td align="center" class="noprinln8"></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(initTatalbreakcount) %></td> 
	     </tr>
      
      <%
	   }
	   
	   %>
	   <tr class="asc" style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"></td>
					<td align="center" class="noprinln2"></td>
						<td align="center" class="noprinln11"></td>
					<td align="center" class="noprinln3">总计</td>
					<td align="center" class="noprinln4"></td>
					<td align="center" class="noprinln10"></td>
					<td align="center" class="noprinln5"></td>
					<td align="center" class="noprinln6"><%=Count %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(Totalcount) %></td> 
					<td align="center" class="noprinln8"></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(Tatalbreakcount) %></td> 
	    </tr>
	   <%
      } 
  } 

   if(null != maptypeinit){
	   Set<Map.Entry<String, List<UploadTotal>>> setmaptype =  maptypeinit.entrySet();
	   Iterator<Map.Entry<String, List<UploadTotal>>> itmaptype = setmaptype.iterator();
	   double Totalcount = 0 ;
	   int Count = 0 ;
	   double Tatalbreakcount = 0 ;
	   String branchname = "";
	   while(itmaptype.hasNext()){
		   Map.Entry<String, List<UploadTotal>> enmaptype = itmaptype.next();
		   String key = enmaptype.getKey();
		   if(!StringUtill.isNull(message)){
				JSONObject jsObj = JSONObject.fromObject(message);
				Iterator<String> it = jsObj.keys();
				while(it.hasNext()){ 
					String t = it.next();
					if(key.equals(t)){ 
						key = jsObj.getString(key);
					}
				}
			}
		   
		   List<UploadTotal> uplist = enmaptype.getValue();
		   double initTotalcount = 0 ;
		   int initCount = 0 ;
		   double initTatalbreakcount = 0 ;
		   
		   if(null != uplist){
			   Iterator<UploadTotal> it = uplist.iterator();
			   while(it.hasNext()){
				   UploadTotal up = it.next();
				   Totalcount += up.getTotalcount();
				   Count += up.getCount();
				   Tatalbreakcount += up.getTatalbreakcount();
				   AllTotalcount += up.getTotalcount();
				   AllCount += up.getCount();
				   AllTatalbreakcount += up.getTatalbreakcount();
				   initTotalcount += up.getTotalcount();
				   initCount += up.getCount();
				   initTatalbreakcount += up.getTatalbreakcount();
				   idcount ++; 
				   
				   String tpe = "";
					if(null != mapus){
						UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
						if(null != ups){
							tpe = ups.getCatergory(); 
						}
					}
				    
				  %> 
				  <tr class="asc"  ondblclick="detail('uploadSendCountDetail.jsp?type=<%=up.getType() %>&said=<%=id %>&totaltype=<%=BasicUtill.send %>&checkedStatus=<%=checkedStatus %>')" onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"><%=idcount %></td>
					<td align="center" class="noprinln2"></td>
						<td align="center" class="noprinln11"></td>
					<td align="center" class="noprinln3"><%=tpe%></td>
					<td align="center" class="noprinln4"><%=up.getType()%></td>
					<td align="center" class="noprinln10"><%=map.get(up.getType()) %></td>  
					<td align="center" class="noprinln5"><%=0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()) %></td> 
					<td align="center" class="noprinln6"><%=up.getCount() %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(up.getTotalcount()) %></td> 
					<td align="center" class="noprinln8"><%=0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTatalbreakcount()/up.getCount()) %></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(up.getTatalbreakcount()) %></td> 
	           </tr>   
				  
				  <%
				   } 
			   } 
		  // branchname = up.getBranchname();
		  %>  	  
		  <tr class="asc" style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"></td>
					<td align="center" class="noprinln2"></td>
						<td align="center" class="noprinln11"></td>
					<td align="center" class="noprinln3"><%=key%></td>
					<td align="center" class="noprinln4"></td>
					<td align="center" class="noprinln10"></td>
					<td align="center" class="noprinln5"></td>
					<td align="center" class="noprinln6"><%=initCount %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(initTotalcount) %></td> 
					<td align="center" class="noprinln8"></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(initTatalbreakcount) %></td> 
	</tr>   
		 <%  
		   
	   } 
   }
	%>
	
	
	 <tr class="asc" style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center" class="noprinln1"></td>
					<td align="center" class="noprinln2">总计</td>
						<td align="center" class="noprinln11"></td>
					<td align="center" class="noprinln3">总计</td>
					<td align="center" class="noprinln4"></td>
					<td align="center" class="noprinln10"></td>
					<td align="center" class="noprinln5"></td>
					<td align="center" class="noprinln6"><%=AllCount %></td>
					<td align="center" class="noprinln7"><%=DoubleUtill.getdoubleTwo(AllTotalcount) %></td> 
					<td align="center" class="noprinln8"></td>
					<td align="center" class="noprinln9"><%=DoubleUtill.getdoubleTwo(AllTatalbreakcount) %></td> 
	    </tr>
</table> 
<%} %>


</body>
</html>
