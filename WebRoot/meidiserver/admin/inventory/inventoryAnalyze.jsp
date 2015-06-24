<%@ page language="java" import="java.util.*,utill.*,product.*,order.*,comparator.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
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
}else if(UserManager.checkPermissions(user, Group.sencondDealsend)){
	branchid = user.getBranch()+"";   
	b = BranchManager.getLocatebyid(branchid); 
	submittype = "outbranch";
} 




List<InventoryBranch>  listInventory = null ;
Map<String,Integer> list = null ;  
Map<String,InventoryBranch> maptype = null ;
if(!StringUtill.isNull(branchid) && !StringUtill.isNull(starttime)  && !StringUtill.isNull(endtimeH)){
	listInventory = InventoryBranchManager.getCategoryid(user,branchid, "");

InventoryBranchComparator c = new InventoryBranchComparator();
	Collections.sort(listInventory,c);
	
	list = InventoryBranchMessageManager.getMapAnalyze(branchid,starttime,endtime);
	
	
	 maptype = InventoryBranchManager.getBranchTypeObject(user,branchid); 
}

//System.out.println(list);
HashMap<Integer,Category>  mapc = CategoryManager.getCategoryMap();
Map<Integer,Branch> branchmap = BranchManager.getIdMap();
Map<Integer,User> usermap = UserService.getMapId();  
 
List<String> listbranchp = BranchManager.getLocateAll();  
String listall = StringUtill.GetJson(listbranchp); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约调货</title>
<link rel="stylesheet" href="../../css/jquery-ui.css"/>
 
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

<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<script type="text/javascript">
var jsonall = <%=listall%>; 
var count = "<%=countt%>";
$(function () { 
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
 
function detail(type){
	window.open('inventoryDetail.jsp?starttime=<%=starttime%>&endtime=<%=endtime%>&ctype='+type+'&branchid=<%=branchName%>', 'abc', 'resizable:yes;dialogWidth:600px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
	//window.location.href='';
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
	
	return true ;
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
              boolean flag = false ;
              Map<String,List<String>> map = new HashMap<String,List<String>>() ;
              if(maptype != null){  
            	  Set<String> setold =  maptype.keySet();
            	 Iterator<String> it = setold.iterator();
            	 while(it.hasNext()){
            		 String type= it.next();
            		 String typenew = StringUtill.getStringNocn(type);
            		 List<String> l = map.get(typenew);
            		 if(null == l){
            			 l = new ArrayList<String>();
            			 map.put(typenew, l); 
            		 }
            		 l.add(type);
            	 }
              } 
              
              if(null !=  listInventory ){
            	  for(int i=0;i<listInventory.size();i++){
            		  flag = true ;
	            	  InventoryBranch in = listInventory.get(i);
	            	  
	            	  if(countt == 0 && in.getRealcount() != 0 || countt != 0  ){
	            		  List<String> l = map.get(StringUtill.getStringNocn(in.getType()));
	            		  String branchtypeStr = "";
	            		  if(null != l){
	            			  for(int j=0;j<l.size();j++){
	            				  String s = l.get(j);
	            				  InventoryBranch branchtype = maptype.get(s);
	    	            		  
	    			            	  if(branchtype == null){ 
	    			            		//  System.out.println(1+in.getType());
	    			            		  branchtypeStr += ""; 
	    			            	  }else {  
	    			            		 // System.out.println(2+in.getType());
	    			            		  branchtypeStr += branchtype.getType()+"\n";
	    			            		  maptype.remove(branchtype.getType());  
	    			            	  }
	            			  }
	            		  }
	            		  
            	  %>  
            	  	 
            	   <tr id=""  class="asc"  onclick="updateClass(this)" ondblclick="detail('<%=in.getTypeid()%>')">    
			 
        			  <td align="center"><%=mapc.get(in.getInventoryid()).getName() %></td>    
        			  <td align="center"><%=in.getType() %> </td>   
        			  <td align="center"><%=in.getPapercount() %> </td>   
        			  <td align="center"><%=in.getRealcount() %> </td> 
        			  <td align="center"><%=null == list.get(in.getType()) ? "0":list.get(in.getType())*(-1) %> </td> 
        			  <td align="center"><%=branchtypeStr %> </td>   
        			  <td align="center"> 
        			  <input type="hidden" name="product" value="<%=in.getTypeid() %>"/>
        			  <input type="text" name="<%=in.getTypeid() %>" id="<%=in.getTypeid() %>" value=""  /> 
        			
        			  </td>
        			 
                       </tr>
            	  
            	  
            	  <%
	            		 
	            	  }
                }
              } 
              
             
              if(null != maptype && maptype.size() > 0){
            	  Set<Map.Entry<String, InventoryBranch>> set = maptype.entrySet();
            	  Iterator<Map.Entry<String, InventoryBranch>> it = set.iterator();
            	  
            	  while(it.hasNext()){
            		  flag = true ;
            		  Map.Entry<String, InventoryBranch> maps = it.next();
            		   
            		  InventoryBranch in = maps.getValue();
            		  %>
            	<tr id=""  class="asc"  onclick="updateClass(this)">   
			 
        			  <td align="center"></td>    
        			  <td align="center"></td>   
        			  <td align="center"> </td>   
        			  <td align="center"> </td>   
        			  <td align="center"> </td> 
        			  <td align="center"><%=in.getType() %> </td>  
        			  <td align="center"> 
        			  <input type="hidden" name="product" value="<%=in.getTypeid() %>"/>
        			  <input type="text" name="<%=in.getTypeid() %>" id="<%=in.getTypeid() %>" value=""  /> 
        			
        			  </td>
        			 
                       </tr>	  
            		  
            		  
            		  <%
            	  }
              }
              
              if(flag){
               %>
		       
		       
		       <tr class="asc" >
		      
		       <td align="center"  colspan=7> 
		       <%  if(UserManager.checkPermissions(user, Group.inventoryreserve,"w")){ %>
		       <input type="submit"  style="background-color:;font-size:20px;" value="调货申请" />
		       <%} %>
		       </td>
		       </tr> 			
		        			 
             <% 
              }
             %>
    
</table> 
</form>
     </div>

</body>
</html>
