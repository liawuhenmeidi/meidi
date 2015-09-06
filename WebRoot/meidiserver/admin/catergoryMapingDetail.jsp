<%@page import="wilson.catergory.CatergoryMaping"%>
<%@page import="wilson.catergory.CatergoryManager"%>
<%@ page language="java" import="java.util.*,utill.*,wilson.upload.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");

UploadSalaryModel salarymodel = new UploadSalaryModel();

//filename
String filename = (String)request.getSession().getAttribute("addName_filename");

//name or shop
String target = request.getParameter("target");

//add or del
String method = request.getParameter("method");

//content
String content = request.getParameter("content");

if(!StringUtill.isNull(target)&&!StringUtill.isNull(method) && !StringUtill.isNull(content)) {
	if(target.equals("name")){
		
		
		if(method.equals("add")){
			
			String catergoryName = content;
			if(CatergoryManager.addCatergoryMaping(catergoryName,filename)){
				out.print("success");
			}else{
				out.print("fail");
			}
			
		}else if(method.equals("del")){
			if(CatergoryManager.delCatergoryMaping(content)){
				out.print("success");
			}else{
				out.print("fail");
			}
		}
		
		
		
	}else if(target.equals("shop")){
		
		
		if(method.equals("add")){
			
			CatergoryMaping cm = new CatergoryMaping();
			cm.setModifyTime(TimeUtill.gettime());
			cm.setContent("");
			cm.setName(content.split(",")[0]);
			cm.setShop(content.split(",")[1]);
			if(CatergoryManager.addCatergoryMaping(cm)){
				out.print("success");
			}else{
				out.print("fail");
			}
			
		}else if(method.equals("del")){
			
			if(CatergoryManager.delCatergoryMaping(content.split(",")[0],content.split(",")[1])){
				out.print("success");
			}else{
				out.print("fail");
			}
			
		}

	}
	
	
}else{
	out.print("fail");
}
%>
