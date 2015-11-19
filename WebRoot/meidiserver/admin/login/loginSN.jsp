<%@ page language="java" import="java.util.*,user.*,utill.*,java.net.URLEncoder,java.net.URLDecoder,group.*" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>

<%
request.setCharacterEncoding("utf-8");
boolean flag = true ; 
String data = request.getParameter("data");
    
//System.out.println("src"+src);

System.out.println("src"+StringUtill.GetJson(data)); 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
   
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/SimpleCanleder.js"></script>
   
<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />  
    
<link rel="stylesheet" href="../../css/SimpleCanleder.css" />

<title>苏宁数据同步管理页面</title>

<script type = "text/javascript" language = "javascript">

function clear(){
	
  }
  </script> 
</head>  
 <body> 
    
    <% if(StringUtill.isNull((String)StringUtill.getJSONObject(data).get("src"))){
    	%>   
    	数据同步完毕 
    	 
        <div >  
        <table style="height:400px;width: 50%" cellspacing="1" id="table">
          <tr class="asc">
            
               <td rowspan="3" align="center">
                      <p ><a href="../date/dateinventory.jsp"  >库存数据</a></p>
               </td>
          <%if(((String)StringUtill.getJSONObject(data).get("InventoryDownLoadstatue")) != null){ 
        	  
                 if(((String)StringUtill.getJSONObject(data).get("InventoryDownLoadstatue")).equals(200+"")){
                	%> 
                	   
                	 <td align="center"> 
        	   库存数据更新完毕。
        	  </td> 
        	         <%
                 }else {
                	 %>
                	 <td align="center"> 
        	     库存数据更新失败。 
        	  </td>
                	   
                	 <%
                 }  
          }else {
        	  %>
        	  <td align="center"> 
        	     库存数据更新失败。 
        	  </td>
        	  <%
          }
        %>
         
         </tr>
         
         <tr class="asc">
         
          <%if(((String)StringUtill.getJSONObject(data).get("InventoryModelDownLoadstatue")) != null){ 
        	 
                 if(((String)StringUtill.getJSONObject(data).get("InventoryModelDownLoadstatue")).equals(200+"")){
                	%> 
                	  
                	 <td align="center">  
        	  样机库存数据更新完毕。
        	  </td> 
        	         <%
                 }else {
                	 %> 
                	 <td align="center"> 
        	     样机库存数据更新失败。 
        	  </td> 
                	   
                	 <%
                 }  
          }else {
        	  %> 
        	  <td align="center"> 
        	       样机库存数据更新失败。 
        	  </td>
        	  <%
          }
        %>
        
        
             </tr>
         
          <tr class="asc">
          <%if(((String)StringUtill.getJSONObject(data).get("InventoryModelDownLoadstatue")) != null){ 
        	 
                 if(((String)StringUtill.getJSONObject(data).get("InventoryModelDownLoadstatue")).equals(200+"")){
                	%> 
                	  
                	 <td align="center">  
        	  坏机库存数据更新完毕。
        	  </td> 
        	         <%
                 }else {
                	 %> 
                	 <td align="center"> 
        	    坏机库存数据更新失败。 
        	  </td> 
                	   
                	 <%
                 }   
          }else {
        	  %>  
        	  <td align="center" > 
        	      坏机库存数据更新失败。 
        	  </td>
        	  <%
          }
        %>
         

             </tr>
             
              <tr class="asc">
              <td rowspan="3" align="center">
         <a href="../date/dateinventorychange.jsp" >出入库数据</a>
         </td>
         
         
          <%if(((String)StringUtill.getJSONObject(data).get("Inventoryoutstatue")) != null){ 
        	 
                 if(((String)StringUtill.getJSONObject(data).get("Inventoryoutstatue")).equals(200+"")){
                	%> 
                	  
                	 <td align="center">  
        	  常规特价退货更新完毕。
        	  </td> 
        	         <%
                 }else {
                	 %> 
                	 <td align="center"> 
        	   常规特价退货更新失败。 
        	  </td> 
                	   
                	 <%
                 }   
          }else {
        	  %>  
        	  <td align="center" > 
        	       常规特价退货更新失败。 
        	  </td>
        	  <%
          }
        %> 
         

             </tr>
             
             <tr class="asc">
          <%if(((String)StringUtill.getJSONObject(data).get("InventoryModeloutstatue")) != null){ 
        	 
                 if(((String)StringUtill.getJSONObject(data).get("InventoryModeloutstatue")).equals(200+"")){
                	%> 
                	  
                	 <td align="center">  
        	  样机退货更新完毕。
        	  </td> 
        	         <%
                 }else {
                	 %> 
                	 <td align="center"> 
        	  样机退货更新失败。 
        	  </td>  
                	   
                	 <%
                 }   
          }else {
        	  %>  
        	  <td align="center" > 
        	      样机退货更新失败。 
        	  </td>
        	  <%
          }
        %>
             </tr>
               
             
             <tr class="asc">
          <%if(((String)StringUtill.getJSONObject(data).get("InventoryInstatue")) != null){ 
        	 
                 if(((String)StringUtill.getJSONObject(data).get("InventoryInstatue")).equals(200+"")){
                	%>  
                	       
                	 <td align="center">  
        	  入库信息更新完毕。 
        	  </td>  
        	         <%
                 }else {    
                	 %> 
                	 <td align="center"> 
        	          入库信息更新失败。 
        	  </td>  
                	 <%
                 }    
          }else {
        	  %>  
        	  <td align="center" > 
        	        入库信息更新失败。 
        	  </td>
        	  <%
          }
        %>
         
 
             </tr>         
                    <tr class="asc">  
                    <td align="center"> <a href="../date/inventoryIn.jsp" >订货订单管理</a></td>
          <%if(((String)StringUtill.getJSONObject(data).get("OrderDownLoad")) != null){  
        	 
                 if(((String)StringUtill.getJSONObject(data).get("OrderDownLoad")).equals(200+"")){
                	%>  
                	  
                	 <td align="center">  
        	  订货订单信息更新完毕。 
        	  </td>   
        	         <% 
                 }else {   
                	 %>  
                	 <td align="center"> 
        	 订单信息更新失败。 
        	  </td>  
                	   
                	 <%
                 }    
          }else {
        	  %>  
        	  <td align="center" > 
        	       订单信息更新失败。  
        	  </td>
        	  <%
          }
        %>
         

             </tr>      
                     
            <tr class="asc"> 
                    <td align="center"> <a href="../date/inventoryOut.jsp" >退货订单管理</a></td>
          <%if(((String)StringUtill.getJSONObject(data).get("OrderDownLoad")) != null){ 
        	 
                 if(((String)StringUtill.getJSONObject(data).get("OrderDownLoad")).equals(200+"")){
                	%>  
                	  
                	 <td align="center">  
        	  退货订单更新完毕。 
        	  </td>   
        	         <% 
                 }else {   
                	 %>  
                	 <td align="center"> 
        	退货订单更新失败。 
        	  </td>  
                	   
                	 <%
                 }    
          }else { 
        	  %>  
        	  <td align="center" > 
        	      退货订单更新失败。  
        	  </td>
        	  <%
          }
        %>
          

             </tr>                
                     
                     
                     
                     
        </table>
      
      </div> 
    	
    	
    	
    	<% 
    } else {
    	%>
    	
    	<form action="../server.jsp" method="post">
    <input type="hidden" name="method" value="InitInventorySNverifyCode"/>
      
   	 <label>验证码：</label><input type="text" size="10"  name="verifyCode"/>
	  
	   <img id='code_img' src='<%=(String)StringUtill.getJSONObject(data).get("src")%>'/>  
     <input name="dosubmit" value="提交" type="submit"  />
   
    <br/>   

     </form>
    	
    <%	
    }%>
 
</body>
</html>