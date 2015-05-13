<%@ page language="java"  pageEncoding="UTF-8"   import="jfree.*" contentType="text/html;charset=utf-8"%> 
<%   
//访问量统计时间线  
//设置子标题  
String filename = JfreeLine.getlineName();
String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename; 
  
%> 
<img src="<%= graphURL %>"width=500 height=300 border=0 usemap="#<%= filename %>"> 