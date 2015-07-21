<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,java.io.File,httpClient.download.*,httpClient.download.InventoryModelDownLoad,httpClient.download.InventoryBadGoodsDownLoad,httpClient.download.InventoryChange,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%   
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
List<BranchType> listgt = BranchTypeManager.getLocate();
  
String time = request.getParameter("mytime"); 
String type = request.getParameter("branchtype");
   
System.out.println("time"+time+"&&&bid"+type);

%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">

#head td {    
	white-space: nowrap;  
}  
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库存数据</title>
   
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/SimpleCanleder.js"></script>
   
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />  
    
<link rel="stylesheet" href="../../css/SimpleCanleder.css" />
<script type="text/javascript">
 
          
 $(function () {   
	 $(".mytime").simpleCanleder(); 
	 
	 $(".mytime").val("<%=time%>"); 
	 $("#branchtype").val("<%=type%>");
 });         
   
 function wincom(time,type,saletype){ 
	 // alert(1);  
	 winPar = window.open("showinventory.jsp?time="+time+"&type="+type+"&saletype="+saletype,"time","resizable=yes,modal=yes,scroll=no,width="+screen.width*0.8+",top="+(screen.height-300)/2+",left="+(screen.width*0.1)+",height=400px,dialogTop:0px,scroll=no");  	
 } 
</script> 
</head>
    
<body> 
	<!--   头部开始   -->
	<jsp:include flush="true" page="../head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>  
	<!--   头部结束   -->
	<div class="table-list"> 
<form action="dateinventory.jsp" >       
<table>
<tr>  
<td>  
 
  销售系统： <select id="branchtype" name="branchtype">
						<option></option> 
						<% 
							if (null != listgt) {
								for (int i = 0; i < listgt.size(); i++) {
									BranchType bt = listgt.get(i);
									if (bt.getTypestatues() == 1 && !StringUtill.isNull(bt.getSaletypeStr())) {
									 	  
						%>  
						<option value="<%=bt.getSaletypeStr()%>"><%=bt.getName()%></option>
						<%  
							}  
								} 
							}
						%>
				</select>
				<input class="mytime" name="mytime" value="" style="text-align:center;"/>
                <input type="submit" value="查看"/>
    

</td>
 </tr>
 </table>
 <table>
 
 <% if(!StringUtill.isNull(time)) {
	
	    
	 String times[] = time.split("-");
	 String day = times[1];  
	 int days = Integer.valueOf(day);
	 int daycount = 0 ;  
	 if(days == 1 || days == 3 ||days == 5 ||days == 7 ||days == 8 ||days == 10 ||days == 12 ){
		 daycount = 31 ;
		 
	 }else  if(days == 2){ 
		daycount = 28; 
	 }else { 
		 daycount = 30;  
	 } 
	 
	 for(int i = 1 ;i<daycount+1;i++){
		 boolean flagc = false ;
		 boolean flagm = false ;
		 boolean flagb = false ;
		 String fc = "";
		 String fm = "";
		 String fb = "";
		 
		 String strday = "";
	     if(i <10){
	    	 strday = "0"+i;
	     }else { 
	    	 strday = i+""; 
	     } 
	          
	     String realtime = time+"-"+strday ;  
	    // String tempPath = PathUtill.getXMLpath();
		/// tempPath += "data" + File.separator + "DownloadInventory"+File.separator+time+"-"+strday+File.separator+type+File.separator; 
		// System.out.println(tempPath);     
		// System.out.println(time);   
		 Collection<SNInventory> coc = InventoryChange.get(TimeUtill.dataAdd( realtime, 0),type);
	        // 苏宁样机  
	     Collection<SNInventory> com =InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd( realtime, 0),type).values(); 
	        // 苏宁坏机 
	     Collection<SNInventory> cob = InventoryBadGoodsDownLoad.getMap(user, TimeUtill.dataAdd( realtime, 0),type).values();
	     //File filec = new File(tempPath+"common.csv");  
	      
	    // File filem = new File(tempPath+"model.csv");
	    // File fileb = new File(tempPath+"badgoods.csv"); 
	         
	     if(null !=coc  && coc.size() > 0){
	    	 flagc = true ; 
	     }
	     if(null != com && com.size() > 0){
	    	 flagm = true ; 
	     }  
	     if(null != cob && cob.size() > 0){
	    	 flagb = true ;
	     }   
		   if(i == 1){  
			  %>  
			   <tr> 
		     <td><%=i %>   
		     <table> 
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','common','<%=type%>')">实货库存: 
		      <%  
		      if(flagc){
		    	  %>
		    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		        </td></tr>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','model','<%=type%>')">样机库存: 
		       <% 
		      if(flagm){
		    	  %>
		    	    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		       </td></tr>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','bad','<%=type%>')">坏货库存: 
		       <%
		      if(flagb){
		    	  %>
		    	    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		       </td></tr>
		     </table>
		     </td>
			  <%
		   }else { 
			   if(i%5 == 1){
				   %> 
				   </tr>
				   <tr> 
		           <td><%=i %>
		            <table>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','common','<%=type%>')">实货库存: 
		      <%
		      if(flagc){
		    	  %>
		    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		        </td></tr>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','model','<%=type%>')">样机库存: 
		       <% 
		      if(flagm){
		    	  %>
		    	    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		       </td></tr>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','bad','<%=type%>')">坏货库存: 
		       <%
		      if(flagb){
		    	  %>
		    	    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		       </td></tr>
		     </table>
		           
		           </td>
				   
				   <%
			   }else {
				   %>
				     <td><%=i %>
				      <table>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','common','<%=type%>')">实货库存: 
		      <%
		      if(flagc){
		    	  %> 
		    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		        </td></tr>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','model','<%=type%>')"> 样机库存: 
		       <% 
		      if(flagm){ 
		    	  %>
		    	    <input type="checkbox" checked="checked" onclick="return false">	  
		    	  <%
		      }%>
		       
		       </td></tr>
		      <tr><td onclick="wincom('<%=time+"-"+strday %>','bad','<%=type%>')">坏货库存: 
		       <%
		      if(flagb){
		    	  %>
		    	    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		       </td></tr>
		     </table>
				     </td>
				    <%
			   }
		   }
	 }
 }%>




</table>
 
 </form>
	</div>
</body>
</html>
