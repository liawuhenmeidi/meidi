<%@ page language="java" import="java.util.*,utill.*,branchtype.*,branch.*,category.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String action = request.getParameter("action");

String id = request.getParameter("id");
 
User users = new User();
if(!StringUtill.isNull(id)){
	users = UserManager.getUesrByID(id);
	System.out.println("id"+id);
}

List<Group> list = GroupManager.getGroup();  

HashMap<String,List<String>> mapg = GroupManager.getGroupPidMap();
String mapgg = StringUtill.GetJson(mapg);

List<BranchType> listb = BranchTypeManager.getLocate();

Map<String,List<String>> map = BranchManager.getLocateMap();  
   
String mapjosn = StringUtill.GetJson(map);

//List<User> listu = UserManager.getUsersregist(Group.Charge); 

if("add".equals(action)){ 
	
	String name = request.getParameter("username");
	System.out.println(name);
	String positions = request.getParameter("position");
	String branch = request.getParameter("branch");
    String juese = request.getParameter("juese");
    String date = request.getParameter("date");  
    String phone = request.getParameter("phone");
    String password = request.getParameter("password");
    String charge = request.getParameter("zhuguan");
    User u = new User(); 
    u.setId(Integer.valueOf(id));  
    u.setBranch(branch);
    u.setEntryTime(date);  
    u.setPositions(positions);
    u.setPhone(phone); 
    u.setUsertype(Integer.valueOf(juese));
    u.setUsername(name);  
    u.setUserpassword(password);
    u.setStatues(users.getStatues());
    u.setCharge(charge);
    boolean flag = UserManager.update(u);;
   	// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
      // System.out.println("RegistServlet"+str);
   	if(flag){
   		response.sendRedirect("huiyuan.jsp");
   	}else {   
   		response.sendRedirect("huiyuanAdd.jsp");  
   	}  
}

%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>员工注册</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="../css/songhuo.css">
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>

  <link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
  <script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>

  <script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">



var jsonmap = '<%=mapjosn%>';   
var jsong = '<%=mapgg%>';

$(function () {
    var opt = { };
    opt.date = {preset : 'date'};
		$('#serviceDate').scroller('destroy').scroller($.extend(opt['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020',maxDate: new Date()}));	
 
		$("#username").focus(function(){
		    $("#username").css("background-color","#FFFFCC");
		  });
		  $("#username").blur(function(){
		    $("#username").css("background-color","#D6D6FF");
		    var categoryName = $("#username").val();
		    $.ajax({ 
		        type:"post", 
		         url:"admin/server.jsp",
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
		        	  //alert(123);
		            } 
		           });
		  });
		  
		    
		  $("#juese").change(function(){
				 var num = ($("#juese").children('option:selected').val());
		   //  alert(num);
		     
		     var jsons =  $.parseJSON(jsong);
				
			  var json = jsons[num];
			  //alert(json);
	          var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
	          for(var i=0; i<json.length; i++) 
	        	 {
	        	 options +=  "<option values='"+json[i]+"'>"+json[i]+"</option>";
	        	 }
	        	 $("#zhuguan").html(options);   
		     
		     
				// $.ajax({ 
				  //      type:"post", 
				   //      url:"server.jsp",
				         //data:"method=list_pic&page="+pageCount,
				   //      data:"method=huiyuan_zhuguan&type="+num,
				    //     dataType: "", 
				    //     success: function (data) {
				        	// alert(data);
				        	
				     //    var json =  $.parseJSON(data);
				      //    var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
				      //    for(var i=0; i<json.length; i++) 
				       // 	 {
				      //  	 options +=  "<option values='"+json[i].id+"'>"+json[i].username+"</option>";
				       // 	 }
				      //  	 $("#zhuguan").html(options);    	 
				       //    }, 
				       //   error: function (XMLHttpRequest, textStatus, errorThrown) { 
				        	  //alert(123);
				       //     } 
				   //   }); 
				

			 }); 
 
		  $("#branchtype").change(function(){
			  var num = ($("#branchtype").children('option:selected').val());
			  var jsons =  $.parseJSON(jsonmap);
		
			  var json = jsons[num];
			  //alert(json);
	          var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
	          for(var i=0; i<json.length; i++) 
	        	 {
	        	 options +=  "<option values='"+json[i]+"'>"+json[i]+"</option>";
	        	 }
	          
	        	 $("#branch").html(options);    	  
			  
			  
			  
			  
		  }); 	  
		  
		  
		  
		  
		  
 });
 
 
 
 
 
 
 
 
 
 
 
 
 function check(){
	 
	 var username = $("#username").val();
	 var position = $("#position").val();
	 var branch = $("#branch").val();
	 var phone = $("#phone").val();
	 var password = $("#password").val();
	 var password2 = $("#password2").val();
	 var zhuguan = $("#zhuguan").val();
	 
	 if(username == "" || username == null || username == "null"){
		 alert("职工名称不能为空");
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
			 if(!filter.test(phone)){
				 alert("请填写正确的手机号码");
				 return false;
			 }
		 }
	 
	 if(zhuguan == "" || zhuguan == null || zhuguan == "null"){
		 alert("主管不能为空");
		 return false;
	 }
	 

	 if(detail == "" || detail == null || detail == "null"){
		 alert("所属门店不能为空");
		 return false;
	 }
	   
	 $('input[name="permission"]:checked').each(function(){ 
		    alert($(this).val()); 
		    
	 });
	 return true ;
 }
 
</script>
</head>


<body>
<div class="s_main">
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
<!--  头 单种类  -->
  
<!--  订单详情  -->
<div class="s_main_box">
<form action="huiyuanUpdate.jsp"   method = "post"  onsubmit="return check()">
 <input type="hidden" name="action" value="add"/> 
 <input type="hidden" name="password" value="<%=users.getUserpassword()  %>" id=""/>
 <input type="hidden" name="id" value="<%=users.getId()  %>" id=""/> 
 <table width="100%" class="s_main_table">

  <tr> 
    <td width="5%"></td>
    <td width="25%">职工姓名<span style=" color:#F00;">*</span></td>
    <td width="65%"><input type="text"  id="username" value="<%=users.getUsername() %>" name="username" /></td>
    <td width="5%"></td>
  </tr>
  
     <tr>
    <td width="5%"></td>
    <td width="25%">所属职位<span style=" color:#F00;">*</span></td>
    <td width="65%"><select id="juese" name="juese">
       <option ></option>
        	 <%
        for(int i=0;i<list.size();i++){
        	Group g = list.get(i);
        	String str = "";
        	if(users.getUsertype() == g.getId()){
        		str = "selected = \"selected\"";
        		
        	}
        	%>
        	 <option value="<%= g.getId() %>"  <%=str %>> <%=g.getName() %></option>
        	 <%  
        }
       %> 
        	    
       </select></td>
    <td width="5%"></td>
  </tr>
    <tr>
    <td width="5%"></td>
    <td width="25%">主&nbsp;&nbsp;&nbsp;&nbsp;管<span style=" color:#F00;">*</span></td>
    <td width="65%"> 
    <select class = "quyu" id="zhuguan" name="zhuguan"  width="200px">
    <option value="<%=users.getCharge()==null?"暂无主管":users.getCharge()%> "  id = ""><%=users.getCharge()==null?"暂无主管":users.getCharge()%></option>
      
   </select>   
 
  </td>
    <td width="5%"></td>
  </tr>
  
     <tr>
    <td width="5%"></td>
    <td width="25%">所属门店<span style=" color:#F00;">*</span></td>
    <td width="65%">
     <select class = "quyu" name="branchtype" id="branchtype" >
      <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
		<%
		 for(int i=0;i<listb.size();i++){
			 BranchType lo = listb.get(i); 
		%>	    
		 <option value="<%=lo.getId()%>"><%=lo.getName()%></option>
		<%
		 }
		%> 
	</select>
    <select id="branch" name="branch">
       <option ><%=users.getBranch() %></option>  
       </select>
    <td width="5%">
    </td>
  </tr>
  
  
     <tr>
    <td width="5%"></td>
    <td width="25%">电&nbsp;&nbsp;&nbsp;&nbsp;话<span style=" color:#F00;">*</span></td>
    <td width="65%"> <input type="text"  id="phone" value="<%=users.getPhone() %>" name="phone" /> </td>
    <td width="5%"></td>
  </tr>

     <tr>
    <td width="5%"></td>
    <td width="25%">入职时间<span style=" color:#F00;">*</span></td>
    <td width="65%"><input class="date" type="text" value="<%=users.getEntryTime() %>" name="date" placeholder="选填"  id="serviceDate"  readonly="readonly" ></input>   </td>
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
    <td width="65%"><input type="submit" value="提  交" /></td>
    <td width="5%"></td>
  </tr>
  
  
  
</table>
        </form>  
<br/>

<br/>
</div>

<!--  zong end  -->
</div>

</body>
</html>