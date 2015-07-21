<%@ page language="java" import="java.util.*,utill.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
TokenGen.getInstance().saveToken(request); 
String s = (String)session.getAttribute("token");
  
String action = request.getParameter("action"); 
 
List<Grouptype> list = GrouptypeManager.getGrouptype(); 
   
Map<String,List<Group>> mapgroup = GroupManager.getLocateMap();
String mapgroupg = StringUtill.GetJson(mapgroup);
 
//System.out.println(mapgroupg);
   
HashMap<String,List<User>> mapg = GroupManager.getGroupPidMapUser();
String mapgg = StringUtill.GetJson(mapg);
//System.out.println(mapgg);  
List<BranchType> listb = BranchTypeManager.getLocate();

//Map<String,List<String>> map = BranchManager.getLocateMap();  
    
Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
 
String mapjosn = StringUtill.GetJson(map);
//List<User> listu = UserManager.getUsersregist(Group.Charge); 


%> 

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />
 
<title>员工注册</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="css/songhuo.css">
<link rel="stylesheet" type="text/css" rev="stylesheet" href="css/bass.css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script src="js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>

<link href="css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
 
<script src="js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
<link href="css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

var jsonmap = '<%=mapjosn%>';   
var jsong = '<%=mapgg%>';
//alert(jsong); 
var jsons = <%=mapgroupg%>;  // 二次组
 
$(function () {
    var opt = { }; 
    opt.date = {preset : 'date'};
	$('#serviceDate').val('').scroller('destroy').scroller($.extend(opt['date'], 
	{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020',maxDate: new Date()}));	
  
		$("#username").focus(function(){
		    $("#username").css("background-color","#FFFFCC");
		  });
		
		  $("#username").blur(function(){
		    $("#username").css("background-color","#D6D6FF");
		    var categoryName = $("#username").val();
		     
		    $.ajax({  
		        type:"post",   
		         url:"server.jsp",  
		         //data:"method=list_pic&page="+pageCount,
		         data:"method=huiyuan_add&huiyuanName="+categoryName,
		         dataType: "",  
		         success: function (data) {
		        	 if("true" == data){ 
		        		 alert("已存在相同的职员名称");
		        		 $("#username").focus();
				          return ;
		        	 }  
		           },   
		          error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	  return false ;
		            } 
		           }); 
		  }); 
		 
		  $("#juesereal").change(function(){
			  $("#juese").html(""); 
			  var num = ($("#juesereal").children('option:selected').val());

			  var json = jsons[num];
			  
	          var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
	          if(json != null ){
	        	  for(var i=0; i<json.length; i++) 
		        	 { 
		        	 options +=  "<option value='"+json[i].id+"'>"+json[i].name+"</option>";
		        	 } 
		        	 $("#juese").html(options);  
	          }
	             	  
		  }); 
		  
 
		  $("#juese").change(function(){
			  $("#zhuguan").html("");
				 var num = ($("#juese").children('option:selected').attr("value"));
		      //alert(1);
				// alert(num);
		     var jsons =  $.parseJSON(jsong);
				
			  var json = jsons[num];
			 //alert(json); 
	          var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
	          if(json != null){
	        	  for(var i=0; i<json.length; i++) 
		        	 { 
		        	 options +=  "<option value='"+json[i].id+"'>"+json[i].username+"</option>";
		        	 } 
	          }
	          
	        	 $("#zhuguan").html(options);   
			 }); 
    
		  
		  $("#branchtype").change(function(){
			  $("#branch").html(""); 
			  var num = ($("#branchtype").children('option:selected').val());
			  var jsons =  $.parseJSON(jsonmap);
		
			  var json = jsons[num];
			  //alert(json);
	          var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
	          for(var i=0; i<json.length; i++) 
	        	 {
	        	 options +=  "<option value='"+json[i].id+"'>"+json[i].locateName+"</option>";
	        	 }
	        	 $("#branch").html(options);    	  
		  }); 
		  
		  
		  
 });
 

 function check(){
	 
	 var username = $("#username").val();
	 var juese = $("#juese").val();
	 var position = $("#position").val();
	 var branch = $("#branch").val(); 
	 var phone = $("#phone").val(); 
	 var password = $("#password").val();
	 var password2 = $("#password2").val(); 
	 var zhuguan = $("#zhuguan").val();
	 var serviceDate = $("#serviceDate").val(); 
	 if(username == "" || username == null || username == "null"){
		 alert("职工姓名不能为空");
		 return false;
	 } 
	 
	 if(juese == "" || juese == null || juese == "null"){
		 alert("所属职位不能为空");
		 return false;
	 }
	 if(zhuguan == "" || zhuguan == null || zhuguan == "null"){
		 alert("主管不能为空");
		 return false;
	 }
	
	 if(branch == "" || branch == null || branch == "null"){
		 alert("所属门店不能为空");
		 return false;
	 }
	
	 if(phone == "" || phone == null || phone == "null"){
		 alert("电话不能为空");
		 return false;
	 }else { 
		 var filter = /^1[3|4|5|8][0-9]\d{8}$/;  
		    var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		   
		     if(phone.length != 8 && phone.length != 11){
		    	 alert("请填写正确的手机号码或电话"); 
		    	 return false; 
		     }
			 if(!filter.test(phone) && !isPhone.test(phone)){
				 alert("请填写正确的手机号码或电话");     
				 return false;  
			 }
		 }
	 
	 if(serviceDate == "" || serviceDate == null || serviceDate == "null"){
		 alert("入职时间不能为空");
		 return false;
	 }
	 
	 if(password == "" || password == null || password == "null"){
		 alert("密码不能为空");
		 return false;
	 }
	 if(password != password2){
		 alert("两次输入密码不一致");
		 return false;
	 }
	
	 $("#submit").css("display","none"); 
	 return true ;
 } 
 
 function regist(){
		location.href = "dengluN.jsp";
		
	} 
 
</script>
</head> 
<body>
<div class="s_main">
 
<jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>  
  
<!--  头 单种类  -->
<div class="s_main_tit">员工注册<span class="qiangdan"><a href="javascript:void(0)" onclick="regist()" >返回</a></span> </div>
   
<!--  订单详情  -->  
<div class="s_main_box">      
<form action="RegistServlet"   method = "post"  onsubmit="return check()"> 
 <input type="hidden" name="method" value="zhuce"/>     
 <input type="hidden" name="token" value="<%=s%>"/> 
 <table width="100%" class="s_main_table">
    
  <tr>
    <td width="5%"></td>
    <td width="25%">职工姓名<span style=" color:#F00;">*</span></td>
    <td width="65%"><input type="text"  id="username" name="username" /></td>
    <td width="5%"></td>
  </tr>
      
     <tr>
    <td width="5%"></td>
    <td width="25%">所属职位<span style=" color:#F00;">*</span></td>
    <td width="65%"><select id="juesereal" name="juesereal">
           <option >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        	 <%
        for(int i=0;i<list.size();i++){
        	Grouptype g = list.get(i);
        	 if(g.getId() != 1){ 
        	%>   
        	 <option value="<%=g.getId()%>"> <%=g.getName()%></option>
        	 <%  
        	 }
        }
       %> 
       </select >
       <br>
        <select id="juese" name="juese">
          <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
       </select>
       
       
       </td>
    <td width="5%">
     
    
    
    </td>
  </tr>
    <tr>
    <td width="5%"></td>
    <td width="25%">主&nbsp;&nbsp;&nbsp;&nbsp;管<span style=" color:#F00;">*</span></td>
    <td width="65%"> 
    <select class = "quyu" id="zhuguan" name="zhuguan"  width="200px">
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
 
   </select>
   
  </td>
    <td width="5%"></td>
  </tr>
  
     <tr>
    <td width="5%"></td>
    <td width="25%">所属门店<span style=" color:#F00;">*</span></td>
    <td width="65%">
     <select class = "quyu" name="branchtype" id="branchtype" >
          <option >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
		<%
		 for(int i=0;i<listb.size();i++){
			 BranchType lo = listb.get(i); 
			 if(lo.getId() != 2){ 
		%>	    
		 <option value="<%=lo.getId()%>"><%=lo.getName()%></option>
		<%
			 }
		 }
		%>
	</select>
	 <br>
    <select id="branch" name="branch">
          <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option> 
       </select>
    <td width="5%">
    
    
    </td>
  </tr>
  
  
  
     <tr>
    <td width="5%"></td>
    <td width="25%">电&nbsp;&nbsp;&nbsp;&nbsp;话<span style=" color:#F00;">*</span></td>
    <td width="65%"> <input type="text"  id="phone" name="phone" /> </td>
    <td width="5%"></td>
  </tr>
  
     
  
  
     <tr>
    <td width="5%"></td>
    <td width="25%">入职时间<span style=" color:#F00;">*</span></td>
    <td width="65%"><input class="date" type="text" name="date" placeholder="选填"  id="serviceDate"  readonly="readonly" ></input>   </td>
    <td width="5%"></td>
  </tr>
  
  <tr> 
    <td width="5%"></td>
    <td width="25%">所在地址</td>
    <td width="65%"> <input type="text"  id="location" name="location" placeholder="安装单位必填" /> </td>
    <td width="5%"></td>
  </tr>
  
       <tr>
    <td width="5%"></td>
    <td width="25%">设置密码<span style=" color:#F00;">*</span></td>
    <td width="65%"><input class="password" type="password" name="password"   id="password"   ></input>   </td>
    <td width="5%"></td>
  </tr>

       <tr>
    <td width="5%"></td>
    <td width="25%">密码确认<span style=" color:#F00;">*</span></td>
    <td width="65%"><input class="password" type="password" name="password2"   id="password2"   ></input>   </td>
    <td width="5%"></td>
  </tr>

</table>
 <div id="submit">
<table> 
 <tr>  
   <td width="20%"></td>
   <td width="60%"><input type="submit" style="font-size:20px;width:200px"   value="注册"/></td>
   <td width="20%"></td>
  </tr> 
 
</table>
</div>
   </form>  
<br/>

<br/>
</div>

<!--  zong end  -->
</div>

</body>
</html>