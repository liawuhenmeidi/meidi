<%@ page language="java"  import="java.util.*,category.*,group.*,user.*,utill.*,company.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
    Company company = CompanyManager.getLocate(); 
    User user = (User)session.getAttribute("user");
%> 
  
<div class="head" >
  <div class="head_logo" style="color:red;"> 
  <!--  <img src="../style/image/b.jpg"/> 
  <center><h1 style="color:red;"><%=company.getName() %></h1></center>
    --> 
   <center><h1><FONT face=华文行楷 color=red size=6>智历</FONT>掌中宝 </h1></center> 
  </div>
  
  <center><FONT  size=5>
  <%if(user != null){%>
  <%=user.getBranch() %>
  <%}%> 
  </FONT></center>  
    
</div> 

