<%@ page language="java" import="java.util.*,utill.*,wilson.upload.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");

UploadSalaryModel salarymodel = new UploadSalaryModel();


String idSTR = request.getParameter("id");
String type = request.getParameter("type");


if(!StringUtill.isNull(type) && !StringUtill.isNull(idSTR)){
	int id = Integer.parseInt(idSTR);
	UploadManager.deleteSalaryModelById(id);
	out.print("success");
}else{
	out.print("fail");
}

%>
