<%@ page language="java"  import="java.util.*,category.*,group.*,user.*,branch.*,utill.*,company.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
    Company company = CompanyManager.getLocate(); 
    User user = (User)session.getAttribute("user");
%> 
  
<div class="head" >
   <center><h1><FONT face=华文行楷 color=red size=6>智历</FONT>掌中宝 </h1></center> 

  <center><FONT  size=5>
  <%if(null != user && !user.getBranch().equals(0+"")){%>
  <%=BranchService.getMap().get(Integer.valueOf(user.getBranch())).getLocateName() %>
  <%}%> 
  </FONT></center>  
    
</div> 

