<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,makeInventory.*,httpClient.download.*,httpClient.*,httpClient.download.InventoryModelDownLoad,httpClient.download.InventoryBadGoodsDownLoad,httpClient.download.InventoryChange,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%     
request.setCharacterEncoding("utf-8");    
User user = (User)session.getAttribute("user"); 
String category = request.getParameter("category"); 
String branch = request.getParameter("branch"); 
String time = request.getParameter("time");
 
String uuids = "";

String muuid = request.getParameter("smodel");
String inuuid = request.getParameter("sin");
String outuuid = request.getParameter("sout");


if(!StringUtill.isNull(muuid)){
	uuids += ",'"+muuid+"'";
} 
if(!StringUtill.isNull(inuuid)){
	uuids += ",'"+inuuid+"'";
}
if(!StringUtill.isNull(outuuid)){
	uuids += ",'"+outuuid+"'";
}  
//System.out.println(muuid+"***"+inuuid+"****"+outuuid);
String[] typestatues = request.getParameterValues("typestatues"); 
String typestatuess = StringUtill.GetJson(typestatues);
String realtime = "";    
      
int branchid = -1 ; 
int branchtype = -1 ;  
      
if(!StringUtill.isNull(branch)){  
	//System.out.println(branch); 
	branchid = BranchService.getNameMap().get(branch).getId(); 
	branchtype =  BranchService.getNameMap().get(branch).getPid();  
}   
//boolean flag = UserManager.checkPermissions(user, Group.ordergoods, "f"); 
Category c = CategoryManager.getCategory(category);
    
List<String> listp = ProductService.getlist(Integer.valueOf(category));
     
String allp = StringUtill.GetJson(listp);  
         
List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp); 
Map<String,Map<String,SNInventory>> mapin = null; 
if(!StringUtill.isNull(time)){          
	mapin = InventoryMerger.getMap(user,branch,category,TimeUtill.dataAdd(time, -30),time,typestatues,uuids);   
}  
          
Map<Integer,Map<String,MakeInventory>> mapmi = MakeInventoryManager.getMap(user, branchid); 
     
//System.out.println(" mapmi"+ mapmi);  
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
var jsonall = <%=listall%>;
var allp = <%=allp%>; 
 var row = 1;   
 var rows = new Array();  
 var winPar = null; 
  var typeid = "";  
  var branch = "<%=branch%>"; 
  var branchtype = "<%=branchtype%>"; 
  var tids = new Array();
  var typestatues = '<%=typestatuess%>';
  //alert(typestatues ); 
 $(function () {    
	 $("#branch").autocomplete({ 
		 source: jsonall
	    }); 
	  
	$("#smodel").val('<%=muuid%>'); 
	$("#sin").val('<%=inuuid%>');
	$("#sout").val('<%=outuuid%>');
	 
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
		 winPar = window.open("../ordergoods/ordergoodsupdatecookie.jsp?inventory=inventory&branch="+branch+"&tids="+tids.toString()+"&branchtype="+branchtype+"&time=<%=time%>","time","resizable=yes,modal=yes,scroll=no,width="+screen.width*0.8+",top="+(screen.height-300)/2+",left="+(screen.width*0.1)+",height=400px,dialogTop:0px,scroll=no");  	
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
				+ branchid + '&type=' + type+"&time=<%=time%>";
	} 
	     
	function winfirm(num){ 
		if(num == 0){ 
			var branch = $("#branch").val();
			//alert(branch);  
			if(null == branch || "" == branch){
				$("#mypost").attr("action","inventory1checkall.jsp");
			}else { 
				var model = $("#smodel").val(); 
				var ind = $("#sin").val();
				var out = $("#sout").val();
	 
				if(("" == model || null == model) && ("" == ind || null == ind) && ("" == out || null == out)){
					$("#mypost").attr("action","inventory1check.jsp");
				}  
			}   
		}else { 
			$("#mypost").attr("action","../server.jsp"); 
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
 
	<!--   头部结束   --> 
     
<input type="hidden" id="time" value="" />
	<input type="hidden" id="starttime" value="" />
	<input type="hidden" id="endtime" value="" />
   
<form action="inventory2check.jsp" id="mypost" > 
<input type="hidden" name="category" value="<%=category%>"/> 
<input type="hidden" name="method" value ="pandiansall"/>   
<input type="hidden" name="branchid" value ="<%=branchid%>"/>  
 	<div class="weizhi_head">
	<table width="100%" id="head">   
		<tr>     
			<td>现在位置：盘点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;仓库:<input type="text" name="branch" id="branch" value="<%=branch %>" />
			</td>   
			<td>
 
			<%         
			// System.out.print("branchid"+branchid);
			 List<User> list = UserService.getMapBranchid().get(branchid+"") ; 
			 
			      if(null != list){
			    	   
			    	  for(int i=0;i<list.size();i++){
			    		  User u = list.get(i);  
			    		  String str = u.getProductIDS();
			    		 // System.out.println(str);
			    		  str = str.substring(1,str.length()-2);
			    		//  System.out.println(str);
			               boolean flag = false ;  
			    		  String[] p = str.split(",");
			    		  for(int j=0;j<p.length;j++){
			    			  String pid = p[j];  
			    			 // System.out.println(pid+"&&&&"+c.getId());
			    			  if(pid.trim().equals(c.getId()+"")){
			    				  flag = true ;
			    			  }
			    		  }
			    		  
			    		 if(flag){
			    		 
			    		  %> 
			    		 
			    		<%=u.getUsername() %>::

			    		  <%=u.getPhone() %>
			    		  <% 
			    	  }}
			      }
			 %>
			</td>
			<!-- 
			<td>  
		                      盘点类别：   
				常规<input type="checkbox" name="typestatues" id="typestatues1" value="1"/> 
			
				特价<input type="checkbox" name="typestatues" id="typestatues2" value="2" />
				
				 
				样机<input type="checkbox" name="typestatues" id="typestatues3" value="3" />
			
				
			 
			</td>  
			 -->
                 <td >库存时间:<input name="time" type="text" id="time"
						value="<%=time%>" size="10" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填" 
						 /></td>    
			
       
         <td rowspan="2"><input type="button" onclick="winfirm(0)" value="查询" /></td> 
			<td rowspan="2"><label id="l" onclick="addtid('')">[调拨单]</label></td>
			<!--  
             <td> <input type="text" name="product" id="product" placeholder="调拨单增加产品"/><label id="l" onclick="addtid('')">[调拨单]</label></td> --> 
			<td rowspan="2"><a href="javascript:history.go(-1);"><font
					style="color:blue;font-size:20px;">返回</font>
			</a></td>
          
        </tr>
        
        </table>
         </div>
        <table width="60%">
        <tr>
         <td colspan="1" >
			   <div id="dmodel">
			   <font size="3" color="red">  盘点样机：</font>
			
				<select id="smodel" name="smodel">
					<option></option>
				<% 
				
				String mremark = "";
				String inremark = "";
				String outremark = "";
				Map<String,MakeInventory>  mapmodel = mapmi.get(MakeInventory.model);
				   if(null != mapmodel){
				  Set<Map.Entry<String,MakeInventory>> setmodel = mapmodel.entrySet();
				  Iterator<Map.Entry<String,MakeInventory>> itmodel =  setmodel.iterator();
				  while(itmodel.hasNext()){ 
					  Map.Entry<String,MakeInventory> mapent = itmodel.next(); 
					  MakeInventory mi = mapent.getValue();
					  if(null != mi){
					  if(!StringUtill.isNull(mi.getRemark())){ 
						  mremark = "";
					  }
					  %> 
					  <option value="<%= mi.getUuid()%>"><%=mi.getSubmittime()%></option>
					   
					  
					  <%
				  }}
				   }
				
				
				%>
				</select>
				</div>
				
				</td>
				<td align="center">
				备注：<%= mremark%>
				</td>
				</tr>
				<tr>
				<td colspan="1">
				<div id="din">
				 <font size="3" color="red"> 盘点入库库存： </font>
				
				<select id="sin" name = "sin">
				<option></option>
				<%
				Map<String,MakeInventory>  mapind = mapmi.get(MakeInventory.in );
				if(null !=mapind ){
				  Set<Map.Entry<String,MakeInventory>> setin = mapind.entrySet();
				  Iterator<Map.Entry<String,MakeInventory>> itin =  setin.iterator();
				  while(itin.hasNext()){ 
					  Map.Entry<String,MakeInventory> mapent = itin.next(); 
					  MakeInventory mi = mapent.getValue();
					  if(null != mi){
						  if(!StringUtill.isNull(mi.getRemark())){ 
							  inremark = "";
						  }
					  %>
					  
					  <option value="<%= mi.getUuid()%>"><%=mi.getSubmittime()%></option>
					  
					  
					  <%
				  }}
				}
				%>
				
				</select>
				</div>
				
				</td> 
				<td align="center">备注：<%= inremark%></td>
				</tr>
				<tr>
				<td colspan="1" >
				<div id="out">
				   <font size="3" color="red"> 盘点店外库存:  </font>
				
				<select id="sout" name="sout">  
				 	<option></option>
					<% 
					
					
					 String upmremark = "";
			  	     String upinremark = "";
			  	     String upoutremark = "";  
			  	     
					Map<String,MakeInventory>  mapout = mapmi.get(MakeInventory.out);
					if(null != mapout){
				  Set<Map.Entry<String,MakeInventory>> setout = mapout.entrySet();
				  Iterator<Map.Entry<String,MakeInventory>> itout =  setout.iterator();
				  while(itout.hasNext()){ 
					  Map.Entry<String,MakeInventory> mapent = itout.next(); 
					  MakeInventory mi = mapent.getValue();
					  if(null != mi){
						  if(!StringUtill.isNull(mi.getRemark())){ 
							  outremark = "";
						  }
					  %>
					  <option value="<%= mi.getUuid()%>"><%=mi.getSubmittime() %></option>
					    
					   
					  <%
				  }} }
				%>
				
				</select>
				</div>
			
			</td>
              <td align="center">
              备注：	<%= outremark%>
              </td>
        
        </tr>
	</table>   
<div class="table-list">  

       
 <div style="width:100%; height:400px; overflow:scroll;" >      
		<table width="100%"    cellspacing="1" id="table" >
				<tr  style="position:fixed;height:40px;width:97.5%;" class="dsc">   
				<td align="left">编号</td>   
					<td align="left">产品类别</td>
					<td align="left">产品型号</td> 
					<td align="left">常规特价实货未入库</td>
					<td align="left" > <font size="3" color="red"> 盘点店外库存  </font></td> 
					<td align="left">样机未入库</td> 
				 
					<td align="left">入库样机</td> 
					<td align="left" > <font size="3" color="red"> 盘点样机 </font></td>   
					<td align="left">苏宁系统库存</td>
				
					<td align="left">已销未提数</td>  
						<td align="left" ><font size="3" color="red"> 盘点入库库存 </font></td>    
					<td align="left">入库坏机</td> 
					<!-- 
					<td align="left">月销量</td>
					 -->  
					<td align="left">上次盘点日期</td>
					<td align="left">调账调拨</td>
					<td align="left">盘点</td>  
				</tr>  
				<tr style="width:100%; height:40px;"></tr>
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
  	     int saleall = 0 ;
  	     int upmodelall = 0 ;
  	     int upinall = 0 ;
  	     int upoutall = 0 ;
  	     
         Map<Integer, Product>  maps =  ProductService.getIDmap();
         if(null != mapin && !mapin.isEmpty()){  
        	 Set<Map.Entry<String,Map<String,SNInventory>>> set = mapin.entrySet();
        	 Iterator<Map.Entry<String,Map<String,SNInventory>>> it = set.iterator();
             while(it.hasNext()){  
            	 Map.Entry<String,Map<String,SNInventory>> mapent = it.next();
            	  // 型号
            	 String tnum = mapent.getKey();
            	 Map<String,SNInventory> mapb = mapent.getValue();

            	 Set<Map.Entry<String,SNInventory>> setb = mapb.entrySet();
            	 Iterator<Map.Entry<String,SNInventory>> itb = setb.iterator();      
            	 while(itb.hasNext()){  
  
            		 Map.Entry<String,SNInventory> mapentb = itb.next();
            		 String bnum = mapentb.getKey();
            		 SNInventory in = mapentb.getValue();
            		 //long start111 = System.currentTimeMillis();  
            		 String cname = in.getGoodGroupName(); 
            		 
            		 
              	     
            		// System.out.println("cname"+cname);  
            		// System.out.println("in.getCid()"+in.getCid());
            		 String tname =in.getGoodpName();
            		 boolean flag = true ;
            		 int tid = 0 ; 
            		  
            		 try { 
      					tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
      							.getId();
      				} catch (Exception e) {
      					//tname = in.getGoodpName();
      					flag = false ;
      				} 
            		  
            		 try{    
            			 if(StringUtill.isNull(cname)){  
            				// System.out.println("in.getCid()"+StringUtill.GetJson(in.getProduct()));
            				  
                			 cname = in.getProduct().getCname(); 
                			 tid = in.getProduct().getId();
                			 flag = true ; 
                		 } 
            			  
            			 if(StringUtill.isNull(tname)){ 
            				// System.out.println("*******"+tname+"****"+in.getProduct().getType());  
            				 tname = in.getProduct().getType(); 
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
            		
            		int salenum = in.getSaleNum();
            		saleall += salenum; 
            		 
            		int upmodel = in.getUpmodel();
            		upmodelall += upmodel;
            		
            		int upin = in.getUpin();
            		upinall += upin ;
            		
            		int upout = in.getUpout();
            		upoutall += upout ;
            		

             	     
            		
            		String querytime = "";  
            		Map<Integer, String> map = in.getMap(); 
            		if(null != map){
            			Set<Map.Entry<Integer, String>> sets = map.entrySet();
            			Iterator<Map.Entry<Integer, String>> its = sets.iterator();
            			while(its.hasNext()){ 
            				Map.Entry<Integer, String> mapen = its.next();
            				int statues = mapen.getKey();
            				if(null == typestatues){
            					String times = mapen.getValue();
                				String name = in.getTypestatuesName(statues);
                				querytime = name+":"+times; 
            				}else {
            					for(int i=0;i<typestatues.length;i++){
            						String sta = typestatues[i];
            						if(Integer.valueOf(sta) == statues){
            							String times = mapen.getValue();
                        				String name = in.getTypestatuesName(statues);
                        				querytime = name+":"+times; 
            						}
            					} 
            					
            				}
            				
            				
            				
            			}
            		}

            		 String cl = "class=\"asc\""; 
  
            		 count++;           
            		 //snnumall += snnum;  
            		// inmodelnumall += inmodelnum;  
 
            		 %>    
            		 <tr <%=cl %> id="<%=tid %>" ondblclick="search('<%=tid%>','<%=branchid%>')">  
            		    <td><%=count %></td>   
            		 <td><%=cname %></td>  
            		  <td><%=tname %></td>     
            		  <td bgcolor="#B0C4DE"><%=outnum %></td>  
            		  <td bgcolor="#B0C4DE" style="border:2px #000"><%=upout %></td>
            		  <td><%=outmodel %></td>  
            		
            		  <td><%=inmodelnum %></td>   
            		  <td ><%=upmodel %></td>
            		   <td bgcolor="#B0C4DE"><%=num %></td>   
            		   <td bgcolor="#B0C4DE"><%=Nomention  %></td> 
            		       <td bgcolor="#B0C4DE"><%=upin%></td> 
            		      <td><%=badnum %></td> 
            		      <!--  
            		        <td><%=salenum %></td>
            		        -->   
            		   <td><%=querytime%></td> 
            		     
            		    <td> 
            		    <% if(flag){ 
            		    	%>
            		    	<label id="l<%=tid %>"  onclick="addtid('<%=tid %>')">[调拨单]</label>
            		    	<%
            		    } %>

            		    </td>  
            		   <td > 
            		   <% if(flag ){       
            			   if(in.getFlag() == 0){   
            				  if((in.getFlagupin() == 2 || StringUtill.isNull(inuuid)) && in.getFlagupm() ==2 || StringUtill.isNull(muuid) && ( StringUtill.isNull(outuuid) || in.getFlagupout() == 2)){  
            			   %>   
            			   <input type="checkbox" name="type"  value="<%=tid %>" checked="checked" ></input>
            			   
            			    <script type="text/javascript">
            			    $("#<%=tid %>").attr("class","dsc");
            			    </script> 
            			   <%   
            		         }else {
            		        	 %>
            		        	 
            		     <input type="checkbox" name="type"  value="<%=tid %>" ></input>   	 
            		        	 
            		        	 <%
            		         }   
            			   }else {
            				     %>
            				     已匹配提交 
            				      <script type="text/javascript">
            			    $("#<%=tid %>").attr("class","dsc");
            			    </script> 
            				     <%
            			   } 
            		   }
            			   %>
            			   
            			    
            		   </td>
            		 </tr>  
     
            		 <% 
            	 //long start22 = System.currentTimeMillis();  
            	// System.out.println("***"+(start22 - start11));
             } 
             }
            // long start2 = System.currentTimeMillis();  
        	// System.out.println("**"+(start2 - start1)); 
         } 
   
 		//System.out.println("mapc.size()"+mapc.size());
 		//System.out.println("mapm.size()"+mapm.size());   
      
		
        %>
       
		 
        
         <tr class="asc">
		    <td></td>  
		 <td></td> 
		  <td></td> 
		  <td><%=outnumall%></td> 
		   <td><%=upoutall%></td> 
		  <td><%=outmodelnumall %></td>
		  <td><%=inmodelnumall %></td> 
		   <td><%=upmodelall %></td> 
		   <td><%=snnumall %></td> 
		   <td><%=allNomention %></td>
		    <td><%=upinall %></td> 
		   <td><%=snbadall %></td>
		   <!--  
		    <td><%=saleall %></td>
		    --> 
		    <td></td>
		   <td ></td>  
		    <td ></td>  
		 </tr> 
		         
		 <tr class="asc">  
		 <td colspan=15 align="center"><input type="button"  onclick="winfirm(1)" value="提交"/> </td>
		    
		  </tr>
		</table>
</div>
 </form> 
	</div>


	<br />

	<div id="serach"></div>
</body>
</html>
