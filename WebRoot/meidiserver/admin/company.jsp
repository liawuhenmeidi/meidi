<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*,company.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
Company company = CompanyManager.getLocate();
  
String action = request.getParameter("action");

if("add".equals(action)){
  String uname = request.getParameter("uname");
  String cname = request.getParameter("cname");
  String locate = request.getParameter("locate");
  String phone = request.getParameter("phone"); 
  String location = request.getParameter("location");
  company.setLocate(locate);
  company.setName(cname);
  company.setUsername(uname);
  company.setPhone(phone); 
  company.setLocation(location);
  CompanyManager.save(company);

}

%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>公司信息</title>
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
</head>
<body>



<script type="text/javascript">

 $(function(){

 });
 
 function check(){
	 var cname = $("#cname").val();
	 var uname = $("#uname").val();
	 var phone = $("#phone").val();
	 var locate = $("#locate").val();
	 var location = $("#location").val();
	  
	 if(cname == "" || cname == null || cname == "null"){
		 alert("公司名称不能为空");
		 return false;
	 }
	 if(locate == "" || locate == null || locate == "null"){
		 alert("公司所在地区不能为空");
		 return false;
	 }
	 if(location == "" || location == null || location == "null"){
		 alert("公司详细地址不能为空");
		 return false;
	 }
	 
	 if(phone == "" || phone == null || phone == "null"){
		 alert("联系电话不能为空");
		 return false;
	 }else {    
		 var filter = /^1[3|4|5|8][0-9]\d{8}$/;  
		    var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
			 if(!filter.test(phone1) && !isPhone.test(phone1)){
				 alert("请填写正确的手机号码或电话");     
				 return false;  
			 } 
	 }	
	
	 if(uname == "" || uname == null || uname == "null"){
		 alert("联系人不能为空");
		 return false; 
	 }
	  
	 return true ;
 }
 
</script>
<!--   头部开始   -->
<div class="head">
  
</div>

<form action="company.jsp"   method = "post"  onsubmit="return check()">
 <input type="hidden" name="action" value="add"/>
 <table width="100%" >
   
  <tr> 
    <td width="5%"></td>
    <td width="25%">公司名称<span style=" color:#F00;">*</span></td>
    <td width="65%"><input type="text"  value="<%=company.getName()==null?"":company.getName() %>" id="cname" name="cname" /></td>
    <td width="5%"></td>
  </tr>
     
     <tr>
    <td width="5%"></td>
    <td width="25%">公司所在地区<span style=" color:#F00;">*</span></td>

    <td width="65%"><input type="text"  value="<%=company.getLocate()==null?"":company.getLocate() %>" id="locate" name="locate" /></td>
   
  
    <td width="5%"></td>
  </tr>
  
    <tr>
    <td width="5%"></td>
    <td width="25%">公司详细地址<span style=" color:#F00;">*</span></td>

    <td width="65%"><input type="text"  value="<%=company.getLocation()==null?"":company.getLocation() %>" id="location" name="location" /></td>
   
  
    <td width="5%"></td>
  </tr>
  
     <tr>
    <td width="5%"></td>
    <td width="25%">联系电话<span style=" color:#F00;">*</span></td>
    <td width="65%"> <input type="text"  value="<%=company.getPhone()==null?"":company.getPhone() %>" id="phone" name="phone" /> </td>
    <td width="5%"></td>
  </tr>
   
     <tr>
    <td width="5%"></td>
    <td width="25%">联系人<span style=" color:#F00;">*</span></td> 
     <td width="65%"><input type="text"  value="<%=company.getUsername()==null?"":company.getUsername() %>" id="uname" name="uname" /></td>
    <td width="5%"></td>
  </tr>
  
    
      
  
          

         <tr>
    <td width="5%"></td>
    <td width="25%"></td>
    <td width="65%"></td>
    <td width="5%"></td>
  </tr>
  
  
  
         <tr>
    <td width="5%"></td>
    <td width="25%"></td>
    <td width="65%"><input type="submit" value="提交" /></td>
    <td width="5%"></td>
  </tr>
  
  
  
</table>
        </form>  
<br/>

<br/>








</body>
</html>
