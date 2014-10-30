<%@ page language="java" import="java.util.*,utill.*,product.*,order.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");  
String ctype = request.getParameter("ctype");

String branchName = request.getParameter("branch");
String starttime = request.getParameter("starttime");

String endtimeH = request.getParameter("endtime");
String endtime = TimeUtill.dataAdd(endtimeH,1); 

String counttyepe = request.getParameter("counttyepe");
int countt = -1 ;
if(StringUtill.isNull(counttyepe)){
	countt = -1 ;
}else { 
	countt = Integer.valueOf(counttyepe);
}
 
String submittype = "";
String branchid = "";
Branch b = null;

if(UserManager.checkPermissions(user, Group.dealSend)){
	submittype = "inbranch";
	if(!StringUtill.isNull(branchName)){
		if(NumbleUtill.isNumeric(branchName)){
			b = BranchManager.getLocatebyid(branchName);
		}else {
			b = BranchService.gerBranchByname(branchName);
		}
		branchid = b.getId()+""; 
	}else {
		branchName = "";
	}  
}else if(UserManager.checkPermissions(user, Group.sencondDealsend) || UserManager.checkPermissions(user, Group.sale)){
	branchid = user.getBranch()+"";   
	b = BranchManager.getLocatebyid(branchid); 
	submittype = "outbranch";
} 




List<InventoryBranch>  listInventory = null ;
Map<String,Integer> list = null ; 
Map<String,String> maptype = null ;
if(!StringUtill.isNull(branchid) && !StringUtill.isNull(starttime)  && !StringUtill.isNull(endtimeH)){
	listInventory = InventoryBranchManager.getCategoryid(branchid, "");
	list = InventoryBranchMessageManager.getMapAnalyze(branchid,starttime,endtime); 
	 maptype = InventoryBranchManager.getBranchType(user,branchid); 
}

 
HashMap<Integer,Category>  mapc = CategoryManager.getCategoryMap();
Map<Integer,Branch> branchmap = BranchManager.getIdMap();
Map<Integer,User> usermap = UserService.getMapId(); 
Map<String,String> mapdevity = OrderManager.getDeliveryStatuesMap();

List<String> listbranchp = BranchManager.getLocateAll();  
String listall = StringUtill.GetJson(listbranchp); 


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/> 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<style type="text/css">
.fixedHead { 
position:fixed;
}  
 
*{
    margin:0;
    padding:0;
}
#table{  
    width:900px;
    table-layout:fixed ;
}

#th{  
    background-color:white;
    position:absolute; 
    width:900px; 
    height:30px;
    top:0;
    left:0;
}

td{
 width:150px; 
}

#th td{
width:150px; 
}

#wrap{
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:500px;
}

</style>
</head>

<body style="scoll:no">
  
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script> 
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script type="text/javascript">
var jsonall = <%=listall%>;
var count = "<%=countt%>";
$(function () { 

	$("#wrap").bind("scroll", function(){ 

		if(pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)){
	        //滚动了竖直滚动条
	        pre_scrollTop=($("#wrap").scrollTop() || document.body.scrollTop);
	       
	        if(obj_th){
	            obj_th.style.top=($("#wrap").scrollTop() || document.body.scrollTop)+"px";
	        }
	    }
	    else if(pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)){
	        //滚动了水平滚动条
	        pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollLeft);
	    }
		});
	
	
		 $("#branch").autocomplete({ 
			 source: jsonall
		    });
		 
		 $("select[id='counttyepe'] option[value='"+count+"']").attr("selected","selected");
		 
}); 

function inventory(inventory,type){
	 if(type == 0 || type == 1){
		 window.location.href='inventorysearch.jsp?id='+inventory;
		 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }else {
		 window.location.href='dingdanDetail.jsp?id='+inventory;
		 //window.open('dingdanDetail.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	 }
} 

function checkTime(){
	
	var starttime = $("#starttime").val();

	var endtime = $("#endtime").val();
	if(starttime == null || starttime == ""){
		alert("开始时间不能为空");
		return false;
	}
	if(endtime == null || endtime == ""){
		alert("结束时间不能为空");
		return false;
	}
	if(!isNaN(val)){
		   alert("是数字");
		}else{
		   alert("不是数字");
		}
	
	return false ;
}
</script>
<div style="position:fixed;width:100%;height:20%;">
<div >
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>     
</div > 
<form action="" id="inventory" onsubmit="return checkTime()"> 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<% if(UserManager.checkPermissions(user, Group.dealSend)){ %>
 选择安装网点：<input type="text" name="branch" id="branch" value="<%=branchName %>"   />
 <% }else {
%> 
 网点名称： <%=user.getBranchName() %>
 <%
 }
 %> 
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
选择出库时间:<input name="starttime" type="text" id="starttime" size="10"
                        maxlength="10" value="<%=starttime %>" onclick="new Calendar().show(this);"  readonly="readonly" />
至:<input name="endtime" type="text" id="endtime" size="10" value="<%=endtimeH %>"
                        maxlength="10" onclick="new Calendar().show(this);"  readonly="readonly" />
   
   &nbsp;&nbsp;&nbsp;&nbsp;
   <select id="counttyepe" name = "counttyepe">
     <option value="-1">全部显示</option>
     <option value=0 >只显示库存不为0</option>
  </select> 
    
   <input type="submit"  value="查询" />
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>
    </form> 
   </div>  

<div style="height:70px;">
</div>
<br/>  

<div id="wrap">
<form action="InventoryServlet" method="post" onsubmit="return checkTime()">
<input type="hidden" name="method" value="addsubscribe"/>

<input type="hidden" name="typebranch" value="<%=submittype%>"/>

<input type="hidden" name="inbranch" value="<%=branchid%>"/>
 
<input type="hidden" name="starttime" value="<%=starttime %>"/>
<input type="hidden" name="endtime"  value="<%=endtimeH %>"/>

<table  cellspacing="1" id="table" >
		<tr id="th">  
     			<td align="center">名称</td>
     			<td align="center">型号</td> 
     			<td align="center">账面库存</td>
     			<td align="center">实际库存</td>  
     			<td align="center">出库数量</td> 
     			<td align="center">关联门店出样型号</td> 
     			<td align="center">需调货数量</td>
     			
		</tr>      
		    
              <% 
              if(null !=  listInventory ){
            	  for(int i=0;i<listInventory.size();i++){
	            	  InventoryBranch in = listInventory.get(i);
	            	  
	            	  if(countt == 0 && in.getRealcount() != 0 || countt != 0  ){
	            		 
	            	  
			            	  String branchtype = maptype.get(in.getType());
			            	  if(StringUtill.isNull(branchtype)){
			            		  branchtype = "";
			            	  }else {
			            		  maptype.remove(in.getType());
			            	  }
            	  %>
            	  	 
            	   <tr id=""  class="asc"  onclick="updateClass(this)">   
			 
        			  <td align="center"><%=mapc.get(in.getInventoryid()).getName() %></td>    
        			  <td align="center"><%=in.getType() %> </td>   
        			  <td align="center"><%=in.getPapercount() %> </td>   
        			  <td align="center"><%=in.getRealcount() %> </td>   
        			  <td align="center"><%=list.get(in.getType())*(-1) %> </td> 
        			  <td align="center"><%=branchtype %> </td>  
        			  <td align="center"> 
        			  <input type="hidden" name="product" value="<%=in.getTypeid() %>"/>
        			  <input type="text" name="<%=in.getTypeid() %>" id="<%=in.getTypeid() %>" value=""  /> 
        			
        			  </td>
        			 
                       </tr>
            	  
            	  
            	  <%
	            		 
	            	  }
                }
            	  System.out.println(maptype.size());
               %>
		       
		       
		       <tr class="asc" >
		      
		       <td align="center"  colspan=7> <input type="submit"  style="background-color:;font-size:20px;" value="调货申请" /></td>
		       </tr> 			
		        			 
             <% 
              }
             %>
    
</table> 
</form>
     </div>

</body>
</html>
