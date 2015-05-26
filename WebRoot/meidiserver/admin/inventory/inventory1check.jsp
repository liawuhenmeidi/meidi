<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,httpClient.download.InventoryModelDownLoad,httpClient.download.InventoryChange,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
String userbranch = user.getBranch();   
String category = request.getParameter("category"); 
String branch = request.getParameter("branch");
String time = request.getParameter("time");
int branchid = -1 ;
if(StringUtill.isNull(time)){
	time = TimeUtill.dataAdd(TimeUtill.getdateString(), -1);
}    
if(!StringUtill.isNull(branch)){  
	//branch = new String(branch.getBytes("ISO8859-1"), "UTF-8");
	  
	//System.out.println(branch);
	branchid = BranchService.getNameMap().get(branch).getId(); 
}  
//boolean flag = UserManager.checkPermissions(user, Group.ordergoods, "f"); 
Category c = CategoryManager.getCategory(category);
  
List<String> listp = ProductService.getlist(Integer.valueOf(category));
 
String allp = StringUtill.GetJson(listp); 
     
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp); 
    
//List<InventoryBranch> list = InventoryAllManager.getlist(user,branchid,
	//	category);     
           
//long start = System.currentTimeMillis();
// Map<String, Map<String, Map<Integer, InventoryBranch>>> mapin = null;

 // System.out.println(System.currentTimeMillis() - start);
//System.out.println(mapin);      
 // System.out.println(mapin ); 
    
Collection<httpClient.download.Inventory> listend = InventoryChange
				.get(TimeUtill.dataAdd(time, 1));
		// System.out.println("listend"+listend.size());
Map<String, httpClient.download.Inventory> mapc = InventoryChange
				.changeMap(listend);  
 
Map<String, httpClient.download.Inventory> mapm = InventoryModelDownLoad
				.getMap(user, TimeUtill.dataAdd(time, 1));
		  
		 
Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryAllManager.getInventoryMap(user,category,branch,time,mapc,mapm);  		 
		
		 
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
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" /> 
 
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript">
var jsonall = <%=listall%>;
var allp = <%=allp%>; 

 var row = 1; 
 var rows = new Array(); 
 var winPar = null;
  var typeid = ""; 
  var branch = "<%=branch%>"; 
  var userbranch = "<%=userbranch%>";
    
 $(function () {  
	 $("#branch").autocomplete({ 
		 source: jsonall
	    });
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
 
 
 function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
 }
  
 function search(ctype,branchid){ 
	 $("#time").val("");
	 $("#starttime").val(""); 
	 $("#endtime").val(""); 
	 winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	
	 setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
 } 
   


function serchclick(category,type,branchid,obj){
	 categoryid = category;
	// alert(type);
	 typeid = type ; 
	 updateClass(obj);  
 } 
 
function pandian(type,branchid){
	$.ajax({   
         type: "post", 
         url: "../server.jsp",    
         data:"method=pandian&branchid="+branchid+"&type="+type,
         dataType: "",   
         success: function (data) { 
        	 add();

            },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
}
 
	function addlInstorage(branchid, type) {
		window.location.href = '../ordergoods/addlInstorage.jsp?branchid='
				+ branchid + '&type=' + type;
	} 
</script>
</head>

<body>
	<!--   头部开始   -->
	<jsp:include flush="true" page="../head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>

	<!--   头部结束   --> 
 
	<input type="hidden" id="time" value="" />
	<input type="hidden" id="starttime" value="" />
	<input type="hidden" id="endtime" value="" />
<form action="inventory1check.jsp"> 
<input type="hidden" name="category" value="<%=category%>"/>
	<table width="100%" id="head">  
		<tr>  
			<td>现在位置：<%=c.getName()%>库存</td>
			<td>仓库:<input type="text" name="branch" id="branch" value="<%=branch %>" />
			</td>  
                 <td>时间:<input name="time" type="text" id="time"
						value="<%=time%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						 /></td> 
			<td><input type="submit" name="" value="查询" /></td>
             
			<td><a href="javascript:history.go(-1);"><font
					style="color:blue;font-size:20px;">返回</font>
			</a></td>
		</tr>





	</table>
</form> 
	<div class="table-list"> 
<form action="../server.jsp" >    
<input type="hidden" name="method" value ="pandians"/> 
 
<input type="hidden" name="branchid" value ="<%=branchid%>"/> 
<input type="hidden" name="branch" value ="<%=branch%>"/>
<input type="hidden" name="category" value ="<%=category%>"/>  
 <input type="hidden" name="time" value ="<%=time%>"/>   
		<table width="100%" cellspacing="1" id="table">
			<thead> 
				<tr> 
				<th align="left">编号</th>
					<th align="left">产品类别</th>
					<th align="left">产品型号</th>
					<th align="left">常规特价实货未入库</th>
					<th align="left">样机未入库</th> 
					<th align="left">样机入库</th> 
					<th align="left">苏宁系统库存</th>
					<th align="left">上次盘点日期</th>
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
  		 int ccount = 0 ;
  		  
         Map<Integer, Product>  maps =  ProductService.getIDmap();
         if(null != mapin && !mapin.isEmpty()){  
        	 Set<Map.Entry<Integer, Map<String, Map<Integer, InventoryBranch>>>> set = mapin.entrySet();
        	 Iterator<Map.Entry<Integer, Map<String, Map<Integer, InventoryBranch>>>> it = set.iterator();
             while(it.hasNext()){ 
            	 Map.Entry<Integer, Map<String, Map<Integer, InventoryBranch>>> mapent = it.next();
            	 Map<String, Map<Integer, InventoryBranch>> mapb = mapent.getValue();
          //System.out.println(1);  
       //  System.out.println( mapb ); 
       //  System.out.println( mapb.size() ); 
          
            	 Set<Map.Entry<String, Map<Integer, InventoryBranch>>> setb = mapb.entrySet();
            	 Iterator<Map.Entry<String, Map<Integer, InventoryBranch>>> itb = setb.iterator();
            	 
            	
            	   
            	 //long start11 = System.currentTimeMillis();      
            	 while(itb.hasNext()){  
            		 
            		 //System.out.println(2); 
            		 Map.Entry<String, Map<Integer, InventoryBranch>> mapentb = itb.next();
            		 Map<Integer, InventoryBranch> mapt = mapentb.getValue();
            		 String type = mapentb.getKey(); 
            		 //long start111 = System.currentTimeMillis();  
            		 String cname = "";
            		 String tname = "";
            		 int countin = 0 ;
            		 //long start222 = System.currentTimeMillis();  
                	// System.out.println("****"+(start222 - start111));  
            		 int outnum = 0 ;
            		 int inmodelnum = 0 ;  
            		 int outmodelnum = 0 ; 
            		 int snnum = 0 ;  
            		 String querytime = "";  
            		 Set<Integer> sb = new HashSet<Integer>();  
            		 Set<Map.Entry<Integer, InventoryBranch>> sett = mapt.entrySet();
            		 Iterator<Map.Entry<Integer, InventoryBranch>> itt =  sett.iterator();
            		 while(itt.hasNext()){   
            			// System.out.println(3); 
            			 Map.Entry<Integer, InventoryBranch> mapentt = itt.next();
            			 InventoryBranch in = mapentt.getValue();
            			 if(in.getTypeid().equals("-1")){
            				   
                 			 cname = in.getGoodname(); 
                 			  
                 		 }else { 
                 			 cname = maps.get(Integer.valueOf(type)).getCname();
                     		 tname =maps.get(Integer.valueOf(type)).getType();
                 		 }
            			 
            			 
            			 
            			 //System.out.println(in.getTypeStatues()); 
            			 if(in.getTypeStatues() == 1){ 
            				 outnum += in.getPapercount(); 
            				 outnumall += in.getPapercount(); 
            			 }else if(in.getTypeStatues() == 2){ 
            				 outnum += in.getPapercount(); 
            				 outnumall += in.getPapercount(); 
            			 }else if(in.getTypeStatues() ==0){   
            				 outnum += in.getPapercount(); 
            				 outnumall += in.getPapercount(); 
            			 }else if(in.getTypeStatues() ==3 ){
            				 outmodelnum += in.getPapercount(); 
            				 outmodelnumall += in.getPapercount(); 
            			 }   
            			 
            			 if(!sb.contains(in.getBranchid())){
            				 sb.add(in.getBranchid()); 
            				 snnum += in.getSnNum();
                 			   
            				 inmodelnum += in.getSnModelnum(); 
            			 }
            			 
            			 if(!StringUtill.isNull(in.getQuerymonth())){
            				 querytime = in.getQuerymonth();
            			 }
            			  

            		 }     
            		 count++;          
            		 snnumall += snnum;  
            		 inmodelnumall += inmodelnum;  
            		   
            		
            		 
            		  
            		 %>  
            		 <tr class="asc">
            		    <td><%=count %></td> 
            		 <td><%=cname %></td> 
            		  <td><%=tname %></td> 
            		  <td><%=outnum%></td> 
            		  <td><%=outmodelnum %></td>
            		  <td><%=inmodelnum %></td> 
            		   <td><%=snnum %></td> 
            		   <td><%=querytime%></td>
            		   <td ><input type="checkbox" name="type" value="<%=type %>"></input></td>
            		 </tr> 
   
            		 <% 
            		  
            	 }  
            	 
            	 //long start22 = System.currentTimeMillis();  
            	// System.out.println("***"+(start22 - start11));
            	 
            	 
       
       
             }
          
          
            // long start2 = System.currentTimeMillis();  
        	// System.out.println("**"+(start2 - start1)); 
          
         } 
  
 		//System.out.println("mapc.size()"+mapc.size());
 		//System.out.println("mapm.size()"+mapm.size());   
         
          
         
       int countb = 0 ;
       Set<Map.Entry<String, httpClient.download.Inventory>> setm = mapm
 				.entrySet();
 		Iterator<Map.Entry<String, httpClient.download.Inventory>> itm = setm
 				.iterator();
 		while (itm.hasNext()) { 
 			Map.Entry<String, httpClient.download.Inventory> mapentc = itm
 					.next();
 			
 			String key = mapentc.getKey(); 
 			//countb++;
 			httpClient.download.Inventory in = mapentc.getValue();
 			int tid = -1;
 			int bid = -1;
 			String tname = "";
 			String bname = "";
 			boolean flag = true ;
 			 
 			// logger.info(in.getBranchName());
 			 
 			try {
 				
 				
 				bname = BranchService.getNumMap().get(in.getBranchNum())
 						.getLocateName();
 				bid = BranchService.getNumMap().get(in.getBranchNum()).getId();
 			} catch (Exception e) {
 				//logger.info(in.getBranchNum());
 				bname = "";
 				flag = false ;
 			}
            
 			try { 
 				tname = in.getGoodpName();
				tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
						.getId();
			} catch (Exception e) {
				tname = in.getGoodpName();
				flag = false ;
			}
 			
 			int cnum = 0;
 			
 			
 			
 			if (StringUtill.isNull(branch) || branch.equals(bname)) {
 				
 	 				
 				try { 
 	 				cnum = mapc.get(key).getNum();
 	 				//ccount++;
 	 				mapc.remove(key); 
 	 			} catch (Exception e) {
 	 				cnum = 0;
 	 			}
 				
 				// logger.info(bname);
 				// logger.info(in.getBranchNum());
 				//countb++;
 				 count++; 
 				 snnumall += cnum;   
            		 inmodelnumall += in.getNum(); 
 				 %>   
        		 <tr class="asc">
        		 <td><%=count %></td>  
        		 <td><%=in.getGoodGroupName() %></td> 
        		  <td><%=tname %></td>  
        		  <td><%=0%></td>   
        		  <td><%=0 %></td> 
        		  <td><%=in.getNum()%></td> 
        		   <td><%=cnum %></td> 
        		   <td></td> 
        		   
        		   <td >
        		   <% 
        		   if(flag){
        			     
        			   %> 
        			   <input type="checkbox" name="type" value="<%=tid %>"></input>
        			   
        			   <%
        		   }
        		   
        		   
        		   %>
        		   </td>
        		 </tr>

        		 <%
 				

 			

 			
 			}
 		}   
        
 		System.out.println("mapc.size()"+mapc.size());
 		System.out.println("mapm.size()"+mapm.size());  
         
         
        Set<Map.Entry<String, httpClient.download.Inventory>> setc = mapc
				.entrySet();
		Iterator<Map.Entry<String, httpClient.download.Inventory>> itc = setc
				.iterator();
		while (itc.hasNext()) {
			Map.Entry<String, httpClient.download.Inventory> mapentc = itc
					.next();
			String key = mapentc.getKey();
			httpClient.download.Inventory in = mapentc.getValue();
			//itc.remove();
			int bid = -1;
			int tid = -1;
			boolean flag = true ; 
			String tname = "";
			

			String bname = "";
			try { 
				//System.out.println(in.getBranchName()); 
				bname = BranchService.getNumMap()
						.get(StringUtill.getStringNocn(in.getBranchName()))
						.getLocateName();   
				//bid = BranchService.getNumMap().get(in.getBranchNum()).getId();
			} catch (Exception e) {
				bname = "";   
			} 
			
			try { 
 				tname = in.getGoodpName();
				tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
						.getId();
			} catch (Exception e) {
				tname = in.getGoodpName();
				flag = false ;
			}
			 
			if (StringUtill.isNull(branch) || branch.equals(bname)) {
				 count++; 
				//	logger.info(in.getGoodNum());
				 ccount++; 
				 snnumall += in.getNum();   
				 %>      
    		 <tr class="asc">
    		 <td><%=count %></td>  
    		 <td><%=in.getGoodGroupName() %></td> 
    		  <td><%=tname %></td>  
    		  <td><%=0%></td>    
    		  <td><%=0 %></td>  
    		  <td><%=0%></td>  
    		   <td><%=in.getNum() %></td> 
    		   <td></td>  
    		   <td >
    		    
    		    <% 
        		   if(flag){
        			      
        			   %> 
        			   <input type="checkbox" name="type" value="<%=tid %>"></input>
        			   
        			   <%
        		   }
        		   
        		   
        		   %>
    		   </td>
    		 </tr>

    		 <% 

				
			}
		}   
        
        %> <tr class="asc">
		    <td></td> 
		 <td></td> 
		  <td></td> 
		  <td><%=outnumall%></td> 
		  <td><%=outmodelnumall %></td>
		  <td><%=inmodelnumall %></td> 
		   <td><%=snnumall %></td> 
		   <td></td>
		   <td ></td> 
		 </tr>
		     
		 <tr class="asc"> 
		 <td colspan=9 align="center"><input type="submit" value="提交"/> </td>
		 
		  </tr>
		</table>

 </form>
	</div>


	<br />

	<div id="serach"></div>
</body>
</html>
