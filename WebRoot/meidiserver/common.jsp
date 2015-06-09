<%@ page language="java" import="java.util.*,utill.*,enums.*,httpClient.download.*,goodsreceipt.*,product.*,ordersgoods.*,exportModel.*,company.*,ordersgoods.*,config.*,gift.*,locate.*,order.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%         
User user = (User)session.getAttribute("user"); 
request.setCharacterEncoding("utf-8");  

TokenGen.getInstance().saveToken(request);
 
String token = (String)session.getAttribute("token"); 

 
%>