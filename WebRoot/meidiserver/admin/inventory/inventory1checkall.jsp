<%@ page language="java" 
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,httpClient.download.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
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
	branchid = BranchService.getNameMap().get(branch).getId(); 
}   
//boolean flag = UserManager.checkPermissions(user, Group.ordergoods, "f"); 
Category c = CategoryManager.getCategory(category);
  
List<String> listp = ProductService.getlist(Integer.valueOf(category));
 
String allp = StringUtill.GetJson(listp); 
     
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp); 

Collection<SNInventory> listend = InventoryChange
				.get(TimeUtill.dataAdd(time, 1));
		// System.out.println("listend"+listend.size());  
Map<String,Map<String, SNInventory>>  mapc = InventoryChange
				.changeMapTypeBranch(listend);   
       
 
Map<String,Map<String, SNInventory>> mapm = InventoryChange
.changeMapTypeBranchNum(InventoryModelDownLoad
		.getMap(user, TimeUtill.dataAdd(time, 1)).values());   
 
  
Map<String,Map<String, SNInventory>> mapbad = InventoryChange
.changeMapTypeBranch(InventoryBadGoodsDownLoad
		.getMap(user, TimeUtill.dataAdd(time, 1)).values()); 
  
//System.out.println("mapbad.size()"+mapbad.size());
Map<String, Map<Integer, Map<Integer, InventoryBranch>>> mapin = InventoryAllManager.getInventoryMap(user,category,branch,time,mapc,mapm,mapbad);  		 
//System.out.println("mapbad.size()"+mapbad.size()); 
		  
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
	
	
	function winfirm(){ 
		var branch = $("#branch").val();
		
		if(null == branch || "" == branch){
			//$("#mypost").attr("action","inventory1checkall.jsp");
		}else {   
			$("#mypost").attr("action","inventory1check.jsp");
			$("#mypost").submit();  
		}  
		
		 
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
<form action="inventory1check.jsp" id="mypost"> 
<input type="hidden" name="category" value="<%=category%>"/>
	<table width="100%" id="head">  
		<tr>  
			<td>现在位置：盘点</td>
			<td>仓库:<input type="text" name="branch" id="branch" value="<%=branch %>" />
			</td>  
                 <td>时间:<input name="time" type="text" id="time"
						value="<%=time%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填"
						 /></td> 
			<td><input type="button" onclick="winfirm()" value="查询" /></td>
             
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
					<th align="left">入库样机</th> 
					
					<th align="left">苏宁系统库存</th>
					<th align="left">入库坏机</th> 
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
  		  
         Map<Integer, Product>  maps =  ProductService.getIDmap();
         if(null != mapin && !mapin.isEmpty()){  
        	 Set<Map.Entry<String, Map<Integer, Map<Integer, InventoryBranch>>>> set = mapin.entrySet();
        	 Iterator<Map.Entry<String, Map<Integer, Map<Integer, InventoryBranch>>>> it = set.iterator();
             while(it.hasNext()){ 
            	 Map.Entry<String, Map<Integer, Map<Integer, InventoryBranch>>> mapent = it.next();
            	 String type = mapent.getKey();
            	 Map<Integer, Map<Integer, InventoryBranch>> mapb = mapent.getValue();

          
            	 Set<Map.Entry<Integer, Map<Integer, InventoryBranch>>> setb = mapb.entrySet();
            	 Iterator<Map.Entry<Integer, Map<Integer, InventoryBranch>>> itb = setb.iterator();
                 String encode = "";
            	 String cname = "";
        		 String tname = "";
        		 int countin = 0 ;
        		 //long start222 = System.currentTimeMillis();  
            	// System.out.println("****"+(start222 - start111));  
        		 int outnum = 0 ;
        		 int inmodelnum = 0 ;  
        		 int outmodelnum = 0 ; 
        		 int snnum = 0 ; 
        		 int snbad = 0 ;
        		 
        		 int cnum = 0;  
 				int mnum = 0; 
 				int badnum = 0 ;
 				
 				
 			
 				
        		 
        		 
            	 while(itb.hasNext()){  
            		  
            		 //System.out.println(2); 
            		 Map.Entry<Integer, Map<Integer, InventoryBranch>> mapentb = itb.next();
            		 Map<Integer, InventoryBranch> mapt = mapentb.getValue();
            		 int bid = mapentb.getKey();   
            		 //long start111 = System.currentTimeMillis();  
            		 
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
                     		 encode =maps.get(Integer.valueOf(type)).getEncoded();
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
            				 snbad += in.getSnBad();
            			 }
            			 
            			 if(!StringUtill.isNull(in.getQuerymonth())){
            				 querytime = in.getQuerymonth();
            			 }
            			  
 
            		 }  
            		 
            		  
            		 Map<String, SNInventory> map = mapc.get(encode);
      				if(null != map){ 
      					Collection<SNInventory> co = map.values();
      					Iterator<SNInventory> itco = co.iterator();
      					while(itco.hasNext()){
      						SNInventory in = itco.next();
      						snnum +=in.getNum();
      						
      					}
      					
      					mapc.remove(encode);
      				}
      				
      				Map<String, SNInventory> mapmm = mapm.get(encode);
      				if(null != mapmm){
      					Collection<SNInventory> co = mapmm.values();
      					Iterator<SNInventory> itco = co.iterator();
      					while(itco.hasNext()){
      						SNInventory in = itco.next();
      						inmodelnum  +=in.getNum();
      						
      					}
      					
      					mapm.remove(encode);
      				} 
      				 
      				 
      				Map<String, SNInventory> mapbadd = mapbad.get(encode);
      				if(null !=mapbadd){
      					Collection<SNInventory> co = mapbadd.values();
      					Iterator<SNInventory> itco = co.iterator();
      					while(itco.hasNext()){
      						SNInventory in = itco.next();
      						snbad +=in.getNum();
      						 
      					}
      					
      					mapbad.remove(encode);
      				} 
            		  
            	      
            		 
            	 }   
            	 count++;    
            	 snnumall += snnum;  
        		 inmodelnumall += inmodelnum;  
            	 //long start22 = System.currentTimeMillis();  
            	// System.out.println("***"+(start22 - start11));
            	 
            	 %>  
        		 <tr class="asc">
        		    <td><%=count %></td> 
        		 <td><%=cname %></td> 
        		  <td><%=tname %></td>  
        		  <td><%=outnum%></td> 
        		  <td><%=outmodelnum %></td>
        		  <td><%=inmodelnum %></td> 
        		   <td><%=snnum %></td> 
        		      <td><%=snbad %></td> 
        		 
        		 </tr> 

        		 <%  
       
       
             }
          
          
            // long start2 = System.currentTimeMillis();  
        	// System.out.println("**"+(start2 - start1)); 
          
         } 
   
 		//System.out.println("mapc.size()"+mapc.size());
 		//System.out.println("mapm.size()"+mapm.size());   
         
          
          
       int countb = 0 ;
       Set<Map.Entry<String,Map<String, SNInventory>>> setm = mapm
 				.entrySet();
 		Iterator<Map.Entry<String,Map<String, SNInventory>>> itm = setm
 				.iterator();
 		while (itm.hasNext()) { 
 			Map.Entry<String,Map<String, SNInventory>> mapentc = itm
 					.next();
 			
 			
 			//countb++;
 			Map<String, SNInventory> inmap = mapentc.getValue();
 			
 			 Collection<SNInventory> co = inmap.values();
 			 
 			Iterator<SNInventory> itc = co.iterator();
 			int tid = -1;
 			int bid = -1; 
 			String tname = "";
 			String bname = "";
 			String type = mapentc.getKey(); 
 			boolean flag = true ;
 			
 			int cnum = 0;
 			int badnum = 0 ;
 			int num = 0 ;
 			String groupname = "";
 			while(itc.hasNext()){
 				SNInventory in = itc.next();
 				groupname = in.getGoodGroupName();
 				tname = in.getGoodpName();
 				num += in.getModelnum();
 			
			 
 			}
 			
 			
 			Map<String, SNInventory> map = mapc.get(type);
				if(null != map){ 
					Collection<SNInventory> coo = map.values();
					Iterator<SNInventory> itco = coo.iterator();
					while(itco.hasNext()){
						SNInventory in = itco.next();
						cnum+=in.getNum();
						
					}
					
					mapc.remove(type);
				}
				
				Map<String, SNInventory> mapbadd = mapbad.get(type);
  				if(null !=mapbadd){
  					Collection<SNInventory> cooo = mapbadd.values();
  					Iterator<SNInventory> itco = cooo.iterator();
  					while(itco.hasNext()){
  						SNInventory in = itco.next();
  						badnum +=in.getNum();
  						 
  					}
  					
  					mapbad.remove(type);
  				} 
  			    count++;  
  			 snnumall += cnum;  
       		 inmodelnumall += num;  
  				  
 				 
 				 %>   
        		 <tr class="asc">
        		 <td><%=count %></td>  
        		 <td><%=groupname %></td> 
        		  <td><%=tname %></td>  
        		  <td><%=0%></td>   
        		  <td><%=0 %></td>   
        		  <td><%=num%></td> 
        		   <td><%=cnum %></td> 
        		   <td><%=badnum %></td> 
        		   
        		 </tr>

        		 <% 
 		
 		}   
          
 		System.out.println("mapc.size()"+mapc.size());
 		System.out.println("mapm.size()"+mapm.size());   
 		System.out.println("mapbad.size()"+mapbad.size()); 
         
        Set<Map.Entry<String,Map<String, SNInventory>>> setc = mapc
				.entrySet();
		Iterator<Map.Entry<String,Map<String, SNInventory>>> itc = setc
				.iterator();
		while (itc.hasNext()) {
			Map.Entry<String,Map<String, SNInventory>> mapentc = itc
					.next();
			String type = mapentc.getKey();
			Map<String, SNInventory> inmap = mapentc.getValue();
			 Collection<SNInventory> co = inmap.values();
	 			
	 		Iterator<SNInventory> itcl = co.iterator();
	 		String groupname = "";
	 		String tname = "";
	 		int num = 0 ;
			while(itcl.hasNext()){
				SNInventory in = itcl.next();
				groupname = in.getGoodGroupName();
				tname = in.getGoodpName();
				
				num +=in.getNum() ;  
			} 
			snnumall += num;   
			 count++;    
				 %>      
    		 <tr class="asc">
    		 <td><%=count %></td>  
    		 <td><%=groupname %></td> 
    		  <td><%=tname %></td>   
    		  <td><%=0%></td>    
    		  <td><%=0 %></td>  
    		  <td><%=0%></td>   
    		   <td><%=num %></td> 
    		    <td>0</td>
    		  
    		  
    		 </tr>

    		 <% 
		}   
        
		Set<Map.Entry<String,Map<String, SNInventory>>> setbad = mapbad
				.entrySet();
		Iterator<Map.Entry<String,Map<String, SNInventory>>> itbad = setbad
				.iterator();
		while (itbad.hasNext()) {
			Map.Entry<String,Map<String, SNInventory>> mapentc = itbad
					.next();
			String type= mapentc.getKey();
			Map<String, SNInventory> inmap = mapentc.getValue();
			Collection<SNInventory> co = inmap.values(); 
			
			 Iterator<SNInventory> it = co.iterator();
			 String groupname = "";
			 String tname = "";
			 int num = 0 ;
			 while(it.hasNext()){ 
				 SNInventory in =  it.next();
				 groupname = in.getGoodGroupName();
				 tname = in.getGoodpName(); 
				 snbadall += in.getNum();   
				 
			 } 
			
			 
			
			 count++;     
			 
			 
				//	logger.info(in.getGoodNum());
				
				 %>      
    		 <tr class="asc">
    		 <td><%=count %></td>  
    		 <td><%=groupname %></td> 
    		  <td><%=tname %></td>  
    		  <td><%=0%></td>    
    		  <td><%=0 %></td>   
    		  <td><%=0%></td>   
    		   <td><%=0 %></td> 
    		    <td><%=num %></td>
    		   
    		 </tr>

    		 <% 

		} 
		
        %> <tr class="asc">
		    <td></td> 
		 <td></td> 
		  <td></td> 
		  <td><%=outnumall%></td> 
		  <td><%=outmodelnumall %></td>
		  <td><%=inmodelnumall %></td> 
		   <td><%=snnumall %></td>  
		   <td><%=snbadall %></td>
		 </tr>
		        
		 <tr class="asc"> 
		 <td colspan=8 align="center"><input type="submit" value="提交"/> </td>
		  
		  </tr>
		</table>

 </form>
	</div>


	<br />

	<div id="serach"></div>
</body>
</html>
