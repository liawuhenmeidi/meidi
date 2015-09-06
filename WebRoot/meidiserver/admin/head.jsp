<%@ page language="java"  import="java.util.*,company.*"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
    Company company = CompanyManager.getLocate(); 
%>
 
<div  > 
  <div > 
 <!--  <img src="../style/image/logo.png" /> -->
  
 <input type="hidden" id="refresh"  value=""/>
   <center><h1><FONT face=华文行楷 color=red size=6>智历</FONT>掌中宝&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%=company.getName()==null?"请在个人中心,基本信息页维护公司信息":company.getName() %></h1></center> 
  <h1 style="color:red;font-size:20px;"></h1>
   
   
  </div>
  
  <div >
  
  
     
  
  </div> 

</div> 

