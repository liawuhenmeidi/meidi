<%@ page language="java" import="java.util.*,user.*,java.net.URLEncoder,java.net.URLDecoder,group.*" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>

<%
request.setCharacterEncoding("utf-8");
boolean flag = true ;
User user = (User)session.getAttribute("user");

String username = "";
String password = "";

Cookie Cookies[]=request.getCookies();
if(Cookies==null){
   System.out.println("还没有cookie值");
   }else {
		for(int i=0;i < Cookies.length;i++){
			//System.out.println("cookie值"+Cookies.length);
			if("username".equals(URLDecoder.decode(Cookies[i].getName(),"utf-8"))){
				username = URLDecoder.decode(Cookies[i].getValue(),"utf-8");
			  
			}else if("password".equals(URLDecoder.decode(Cookies[i].getName(),"utf-8"))){
				password = URLDecoder.decode(Cookies[i].getValue(),"utf-8");
				
			}
         }
}

String action = request.getParameter("action");
  
String message = request.getParameter("message");
	if("quit".equals(message)){
		session.invalidate();
		response.sendRedirect("login.jsp"); 
		return ;
	}
    
if(action != null && action.equals("login")) {
	
	username = request.getParameter("username");
    password = request.getParameter("password");
	
	try {
		User u = UserManager.check(username, password);
		if(u != null ){
			String userNamecook = URLEncoder.encode(username, "utf-8");
			String passwordcook = URLEncoder.encode(password, "utf-8");
			
			//URLDecoder.decode(cookies[i].getName(),"utf-8");
			Cookie cookie=new Cookie("username",userNamecook);
			Cookie cookiep=new Cookie("password",passwordcook);
			cookie.setMaxAge(10*60);  //设置过期之前的最长时间
			cookiep.setMaxAge(10*60);
			response.addCookie(cookie);
			response.addCookie(cookiep);
			session.setAttribute("user", u);
		}
	  response.sendRedirect("admin/index.jsp");
	  return ; 
	} catch (UserNotFoundException e) {
		out.println(e.getMessage());
	} catch (PasswordNotCorrectException e) {
		out.println(e.getMessage());
	}  
}

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>微网站管理登陆页面</title>
<style type="text/css">
	div{overflow:hidden; *display:inline-block;}div{*display:block;}
	.login_box{background:url(style/image/login_bg.jpg) no-repeat; width:800px; height:500px; overflow:hidden; position:absolute; left:50%; top:50%; margin-left:-400px; margin-top:-250px;}
	.login_iptbox{bottom:90px;_bottom:72px;color:black;font-size:12px;height:30px;left:50%;
       margin-left:-200px;position:absolute;width:800px; overflow:visible;}
	.login_iptbox .ipt{height:24px; width:110px; margin-right:40px; color:#fff; background:url(http://www.qw365.org/statics/images/admin_img/ipt_bg.jpg) repeat-x; *line-height:24px; border:none; color:#000; overflow:hidden;}
		.login_iptbox label{ *position:relative; *top:-6px;}
	.login_iptbox .ipt_reg{margin-left:12px;width:46px; margin-right:16px; background:url(http://www.qw365.org/statics/images/admin_img/ipt_bg.jpg) repeat-x; *overflow:hidden;text-align:left;padding:2px 0 2px 5px;font-size:16px;font-weight:bold;}
	
	.yzm{position:absolute; background:url(http://www.qw365.org/statics/images/admin_img/login_ts140x89.gif) no-repeat; width:140px; height:89px;right:56px;top:-96px; text-align:center; font-size:12px; display:none;}
	.yzm a:link,.yzm a:visited{color:#036;text-decoration:none;}
	.yzm a:hover{color:black;}
	.yzm img{cursor:pointer; margin:4px auto 7px; width:130px; height:50px; border:1px solid #fff;}
	.cr{font-size:12px;font-style:inherit;text-align:center;color:black;width:100%; position:absolute; bottom:58px;}
	.cr a{color:black;text-decoration:none;}
</style>
<script type = "text/javascript" language = "javascript">

function clear(){
	if (confirm("确定要退出吗？")) {
		location.href = "login.jsp?message=quit";
	}
  }
  </script>
</head>

<body onload="javascript:document.myform.username.focus();">
<div id="login_bg" class="login_box">
	<div class="login_iptbox"> 
   	 <form action="login.jsp" method="post">
   	 <input type="hidden" name="action" value="login"/>
     
   	 <label>用户名：</label><input type="text" size="10" value="<%=username==null?"":username %>"  name="username"/>
   	 <label>密码：</label><input type="password" size="10" value="<%=password==null?"":password %>" name="password"/>
   <!--  	 <label>验证码：</label><input name="code" type="text" class="ipt ipt_reg" onfocus="document.getElementById('yzm').style.display='block'" /> -->  
     <input name="dosubmit" value="登陆" type="submit"  />
    <div id="yzm" class="yzm"> 
    <img id='code_img' onclick='this.src=this.src+"&"+Math.random()' src='http://www.qw365.org/api.php?op=checkcode&code_len=4&font_size=20&width=130&height=50&font_color=&background='/> 
    <br/>  
    <a href="javascript:document.getElementById('code_img').src='http://www.qw365.org/api.php?op=checkcode&m=admin&c=index&a=checkcode&time='+Math.random();void(0);">单击更换验证码</a>
    </div>

     </form>
    </div>
    <div class="cr">CopyRight  <a href="" target="_blank">智历软件科技有限公司</a> </div>
</div>
</body>
</html>