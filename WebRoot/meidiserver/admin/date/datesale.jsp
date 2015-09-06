<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,salesn.*,java.io.File,httpClient.download.*,httpClient.download.InventoryModelDownLoad,httpClient.download.InventoryBadGoodsDownLoad,httpClient.download.InventoryChange,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%   
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
List<BranchType> listgt = BranchTypeManager.getLocate();   


String time = request.getParameter("mytime"); 
String btype = request.getParameter("branchtype");
  
//System.out.println("time"+time);

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
	 $("#branchtype").val("<%=btype%>");
 });         
  
</script>
</head>
    
<body> 
	<!--   头部开始   -->
	<jsp:include flush="true" page="../head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>  
	<!--   头部结束   -->
	<div class="table-list"> 
<form action="datesale.jsp" >       

 <table width="100%" height="100%"  border="0" cellspacing="0" cellpadding="0">
 
 <tr><td align="center" valign="middle">
 <table   >
<tr>  
<td align="center">   
   
  销售系统： <select id="branchtype" name="branchtype">
						<option></option> 
						<%
							if (null != listgt) {
								for (int i = 0; i < listgt.size(); i++) {
									BranchType bt = listgt.get(i);
									if (bt.getTypestatues() == 1) {
										
						%>
						<option value="<%=bt.getSaletype()%>"><%=bt.getName()%></option>
						<%  
							}   
								} 
							}
						%>
				</select>
				<input class="mytime" name="mytime" value="" style="paddind-top: 100px; padding-left:100px;"/>
 <input type="submit" value="查看"/>
    

</td>
 </tr>
 </table>
 
 </td>
 </tr>
 <tr><td align="center" valign="middle"> 
  
  
 <table width="50%"  border="1" cellpadding="0" cellspacing="0">
  
 <% if(!StringUtill.isNull(time)) {
	 String type = "";  
	 if("1".equals(btype)){
		 type = "苏宁网上数据";
	 }  
	
	    
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
	  
	 Map<String, Map<String, List<SaleSN>>> mapdb = SaleSNManager.getMapDB(
				time+"-01", time+"-"+daycount,type);    
	System.out.println("mapdb"+mapdb);
	 for(int i = 1 ;i<daycount+1;i++){
		 boolean flag = false ;
		 String strday = "";
	     if(i <10){ 
	    	 strday = "0"+i;
	     }else { 
	    	 strday = i+""; 
	     }  
	     String key = time+"-"+strday;
	     Map<String, List<SaleSN>> mapt = mapdb.get(key);
	     if(null != mapt && mapt.size() != 0 ){
	    	 flag = true ;
	     } 
	    
		   if(i == 1){ 
			  %> 
			   <tr> 
		     <td><%=i %>  
		    
		      <%
		      if(flag){
		    	  %>
		    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		      
		
		 
		 
		     
		     </td>
			  <%
		   }else { 
			   if(i%5 == 1){
				   %> 
				   </tr> 
				   <tr> 
		           <td><%=i %>
		            
		      <%
		      if(flag){ 
		    	  %>
		    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%> 
		           </td>
				   
				   <%
			   }else {
				   %>
				     <td><%=i %>
				     
		      <%
		      if(flag){
		    	  %> 
		    <input type="checkbox" checked="checked" onclick="return false"/>	  
		    	  <%
		      }%>
		      
		       
				     
				     </td>
				    <%
			   }
		   }
	 }
	 
	  
	 
	 
	 
	
 }%> 
 
 
 
 
 </table>
 
 
  </td></tr>
 </table>
 </form>
	</div>
</body>
</html>
