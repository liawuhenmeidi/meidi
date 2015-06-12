<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,httpClient.download.*,httpClient.*,httpClient.download.InventoryModelDownLoad,httpClient.download.InventoryBadGoodsDownLoad,httpClient.download.InventoryChange,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 

String time = request.getParameter("time");  
String realtime = "";   
if(StringUtill.isNull(time)){
	time = TimeUtill.dataAdd(TimeUtill.getdateString(), -1);
	realtime = time ;
}else {
	realtime = time ;
	if(time.equals(TimeUtill.getdateString())){ 
		time = TimeUtill.dataAdd(TimeUtill.getdateString(), -1);
	} 
}     
 
Collection<SNInventory> coc = InventoryChange.get(TimeUtill.dataAdd(realtime, 1));		 
 
		  
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
td {
	width: 100px;
	line-height: 30px;
}

#head td {
	white-space: nowrap;
} 
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
   
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/cookie/jquery.cookie.js"></script>
 
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" /> 
 
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript">
 var row = 1;   
 var rows = new Array();  
 var winPar = null; 
  var typeid = "";  
  var tids = new Array();
        
 $(function () {   
	 
	 //allp
	 //add();
     
 });         
   
 function startRequest(ctype,branchid){ 
	 var time = $("#time").val();
	 if("fresh" == time){ 
		 var starttime = $("#starttime").val(); 
		 var endtime = $("#endtime").val(); 
		 window.location.href='inventoryDetail.jsp?ctype='+ctype+'&branchid='+branchid+'&starttime='+starttime+'&endtime='+endtime; 
		 $("#time").val("");
		 return ;
	 }
	
 } 
 
   
 function search(ctype,branchid){ 
	 $("#time").val(""); 
	 $("#starttime").val("");  
	 $("#endtime").val(""); 
	 winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	 
	 setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
} 
   
 
 function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
 }
                     
 function addtid (tid){  
	if(null == tid || "" == tid){
		tid = $("#product").val();
		//alert(tname); 
	}
	// var data = new  Date(); 
	 //data.setTime(data.getTime()+(60*12 * 60 * 1000));
	// alert(data);  
	// $.cookie("inventorycheck", tid,{ expires: data });   
	 tids.push(tid);      
	  
	 $("#"+tid).attr("class","bsc");
	 $("#l"+tid).css("display","none");
	 
	 //    
	 if(null == winPar){  
		 winPar = window.open("../ordergoods/ordergoodsupdatecookie.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype,"time","resizable=yes,modal=yes,scroll=no,width="+screen.width*0.8+",top="+(screen.height-300)/2+",left="+(screen.width*0.1)+",height=400px,dialogTop:0px,scroll=no");  	
	 }else {     
		 if(winPar.closed){    
			 window.location.reload();
			 //tids = new Array();  
			// tids.push(tid);      
			      
			//  alert($("#"+tid));   
			// $("#"+tid).attr("class","bsc");
			// $("#l"+tid).css("display","none");
			   
			// winPar = window.open("../ordergoods/ordergoodsupdate.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype,"time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	 
			 //winPar.location.href="../ordergoods/ordergoodsupdate.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype; 
		 }else {  
			 //winPar.location.reload();
			 winPar.close();  
			 winPar = window.open("../ordergoods/ordergoodsupdatecookie.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype+"&time=<%=time%>","time","resizable=yes,modal=yes,scroll=no,width="+screen.width*0.8+",top="+(screen.height-300)/2+",left="+(screen.width*0.1)+",height=400px,dialogTop:0px,scroll=no");  	 
		 }
		 
	 }
   
 }
   
 function search(ctype,branchid){  
	// alert(0);  
	 $("#time").val("");  
	 $("#starttime").val(""); 
	 $("#endtime").val("");  
	// alert(1);
	 winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=700px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	 setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
 }  
     
 function changecolor(obj){
	 if($(obj).attr("checked")){ 
		 $("#"+$(obj).val()).attr("class","dsc");
		 $("#l"+$(obj).val()).css("display","none");
	 }else { 
		 $("#"+$(obj).val()).attr("class","asc");
		 $("#l"+$(obj).val()).css("display","block");
	 }
	 ;
 }

function serchclick(category,type,branchid,obj){
	 categoryid = category;
	// alert(type);
	 typeid = type ; 
	 updateClass(obj);  
 } 
 

 
	function addlInstorage(branchid, type) {
		window.location.href = '../ordergoods/addlInstorage.jsp?branchid='
				+ branchid + '&type=' + type;
	} 
	  
	function winfirm(){ 
		var branch = $("#branch").val();
		//alert(branch); 
		if(null == branch || "" == branch){
			$("#mypost").attr("action","inventory1checkall.jsp");
		}  
		$("#mypost").submit();  
		
	} 
</script>
</head>
 
<body>
	<!--   头部开始   -->
	<jsp:include flush="true" page="../head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>

	<div class="table-list"> 
<form action="../server.jsp" >    
<input type="hidden" name="method" value ="pandians"/> 
  
 <input type="hidden" name="time" value ="<%=time%>"/> 
 <div style="height:400px">   
		<table width="100%"    cellspacing="1" id="table">
			<thead>  
				<tr>  
				<th align="left">编号</th>
					<th align="left">产品类别</th>
					<th align="left">产品型号</th>
					<th align="left">常规特价实货未入库</th>
					<th align="left">样机未入库</th> 
					<th align="left">入库样机</th> 
					
					<th align="left">苏宁系统库存</th>
					<th align="left">已销未提数</th>
					<th align="left">入库坏机</th> 
					<th align="left">上次盘点日期</th>
					<th align="left">调账调拨</th>
					<th align="left">盘点</th>  
				</tr> 
			</thead> 
         <%  
          
         //long start1 = System.currentTimeMillis(); 
          int count = 0 ;  
          int outnumall = 0 ; 
  		 int inmodelnumall = 0 ;    
  		 int outmodelnumall = 0 ;   
  		 int snnumall = 0 ;
  		 int snbadall = 0 ; 
  		 int ccount = 0 ;
  	     int allNomention  = 0 ; 
         Map<Integer, Product>  maps =  ProductService.getIDmap();  
        	  if(null != coc){
                 Iterator<SNInventory> it = coc.iterator();    
            	 while(it.hasNext()){  
            		 SNInventory in = it.next();
            		 //long start111 = System.currentTimeMillis();  
            		 String cname = in.getGoodGroupName();
            		 String tname =in.getGoodpName(); 
            		 try{ 
            			 if(StringUtill.isNull(cname)){
                			 cname = CategoryService.getmap().get(in.getCid()).getName();
                		 }
            			 
            			 if(StringUtill.isNull(tname)){
            				 tname = in.getType(); 
            			 }
            		 }catch(Exception e){
            			 
            		 }
            		 
            		//  System.out.println(StringUtill.GetJson(in)); 
            		 
            		 
            		 int num = in.getNum();
            		 snnumall+= num ; 
            		
            		 int Nomention = in.getNomention();
            		 allNomention += Nomention;
            		 
            		 
            		 int inmodelnum = in.getModelnum();
            		 inmodelnumall += inmodelnum;
            		 
            		 int badnum = in.getBadnum();
            		 snbadall += badnum; 
            		 
            		int outnum = in.getIncommonnum();
            		outnumall += outnum ;
            		 
            		int outmodel = in.getInmodelnum();
            		outmodelnumall += outmodel; 
            		
            		 boolean flag = true ;
            		 int tid = 0 ; 
            		 
            		 try { 
      					tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
      							.getId();
      				} catch (Exception e) {
      					tname = in.getGoodpName();
      					flag = false ;
      				} 
            		 
            		 String querytime = "";  
            		 
            		 String cl = "class=\"asc\""; 
  
            		 count++;          
            		 //snnumall += snnum;  
            		// inmodelnumall += inmodelnum;  
 
            		 %>    
            		 <tr <%=cl %> id="<%=tid %>" >  
            		    <td><%=count %></td>   
            		 <td><%=cname %></td>  
            		  <td><%=tname %></td>    
            		  <td><%=outnum %></td> 
            		  <td><%=outmodel %></td>
            		  <td><%=inmodelnum %></td>   
            		   <td><%=num %></td> 
            		      <td><%=Nomention  %></td>  
            		      <td><%=badnum %></td>
            		   <td><%=querytime%></td> 
            		   
            		    <td> 
            		    <% if(flag){
            		    	%>
            		    	<label id="l<%=tid %>"  onclick="addtid('<%=tid %>')">[调拨单]</label>
            		    	<%
            		    } %>
            		    
            		    </td>  
            		   <td >
            		   <% if(flag){
            			   %>
            			   <input type="checkbox" name="type"  value="<%=tid %>" onclick="changecolor(this)"></input>
            			   <%
            		   } %>
            		   </td>
            		 </tr>  
     
            		 <% 
            	 //long start22 = System.currentTimeMillis();  
            	// System.out.println("***"+(start22 - start11));
            // long start2 = System.currentTimeMillis();  
        	// System.out.println("**"+(start2 - start1)); 
         } }
   
 		//System.out.println("mapc.size()"+mapc.size());
 		//System.out.println("mapm.size()"+mapm.size());   
      
		
        %> <tr class="asc">
		    <td></td> 
		 <td></td> 
		  <td></td> 
		  <td><%=outnumall%></td> 
		  <td><%=outmodelnumall %></td>
		  <td><%=inmodelnumall %></td> 
		   <td><%=snnumall %></td> 
		   <td><%=allNomention %></td>
		   <td><%=snbadall %></td>
		    <td></td>
		   <td ></td>  
		    <td ></td> 
		 </tr>
		       
		 <tr class="asc"> 
		 <td colspan=12 align="center"><input type="submit" value="提交"/> </td>
		    
		  </tr>
		</table>
</div>
 </form>
	</div>


	<br />

	<div id="serach"></div>
</body>
</html>
