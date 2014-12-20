<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>权限管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
</head>

<body>  

<% 

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String action = request.getParameter("action");
String ptype = request.getParameter("ptype"); 
String typeStr = request.getParameter("type"); 




int type = Integer.valueOf(typeStr);

List<Category> list =CategoryManager.getCategory(Category.sale) ;
HashMap<Integer,Group> map =GroupManager.getGroupMap() ;
Map<String,List<Group>> maplist = GroupService.getPidMap();
Group groups = null;
String id = request.getParameter("id");
//System.out.println(id);
String statuesInit =  "";
String productsInit = ""; 
String permissions = "";
List<Group>  listg = GroupManager.getGroup() ;
List<Integer> pid = null ;

if(!StringUtill.isNull(id)){
	groups = map.get(Integer.valueOf(id));
	statuesInit = groups.getStatues()+"";
	productsInit = groups.getProducts();
	permissions = groups.getPermissions();
	pid = groups.getPid(); 
	//System.out.println(permissions);
}

if("permission".equals(action)){

	int statues = Integer.valueOf(request.getParameter("Statues"));
	String[] products = request.getParameterValues("product");
	String[] permission = request.getParameterValues("permission");
	String[] ppid = request.getParameterValues("pid");
    List<Integer> l = new ArrayList<Integer>(); 
	String productes = "";
	String permiss = "";
	if(products != null){
		for(int i=0;i<products.length;i++){
	    	productes += products[i] + "_";
	     }
	}
    if(permission != null ){
    	for(int i=0;i<permission.length;i++){
        	permiss += permission[i] + "_";
         }
    }
    if(productes != "" && productes != null){
    	productes = productes.substring(0, productes.length()-1);
    } 
    
    if(null != ppid){
    	for(int i=0;i<ppid.length;i++){ 
        	l.add(Integer.valueOf(ppid[i])); 
        }
    }   
    
    String ids = request.getParameter("id");
    Group group = new Group();
    group.setId(Integer.valueOf(ids));
    group.setPid(l); 
    //System.out.println(id);
    group.setStatues(statues);
    group.setProducts(productes);
    group.setPermissions(permiss); 
    group.setPtype(groups.getPtype()); 
    GroupManager.updatePermisson(user, group);
    response.sendRedirect("juese.jsp?ptype="+ptype+"&type="+typeStr);
    return ;
}

%>

<script type="text/javascript">
 var statuesInit = "<%=statuesInit%> ";
 var productsInit = "<%=productsInit%>";
 var permissions = "<%=permissions%>";
 var products = new Array();
 var permission = new Array();
 $(function(){ 
	 if(statuesInit == 0){ 
		 $("#statues").attr("checked","checked");
	 }
	 products = productsInit.split("_");
	 permission =  permissions.split("_");
	 
	 for(var i=0;i<products.length;i++){
		 $("#"+products[i]).attr("checked","checked");
	 }
	 for(var i=0;i<permission.length;i++){
		 $("#p"+permission[i]).attr("checked","checked");
	 }
	 
 });
 
 function check(){
	 var m = "";
	 var p = "";
	 var pe ="";
	 
	 $("input[name='pid']:checked").each(function(){ 
		    m += this.val();
	 });
	 
	 $("input[name='product']:checked").each(function(){ 
		    p += this.val();
	 });
	 
	 $("input[name='permission']:checked").each(function(){ 
		    pe += this.val();
	 });
	 
	 if(m == ""){
		 alert("上级管理组不能为空");
		 return false ;
	 }
	  
	 if(p == ""){
		 alert("产品不能为空");
		 return false ;
	 }
	 
	 if(pe == ""){
		 alert("权限不能为空");
		 return false ;
	 }
	 return true ;
 }
 
</script>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main">
 
     
 <!--       -->    
     
<div class="">
 <div class="weizhi_head">现在位置：<%=groups.getName() %></div>
 <form action="authority.jsp"  method = "post"  onsubmit="return check()">
  
   <input type="hidden" name="action" value="permission"/>
   <input type="hidden" name="id" value="<%=id%>"/> 
   <input type="hidden" name="ptype" value="<%=ptype%>"/>
   <input type="hidden" name="type" value="<%=typeStr%>"/>   
   <div class="juese_head">   
		   启用：
		<input type="radio" checked="checked"  name="Statues" value="1" />
		<br />
		   禁用：
		<input type="radio" name="Statues" id = "statues" value="0" />
		<br />
    </div>
    <div class="juese_head">
    </div>
     <div class="juese_head">产品权限</div>
     
    <div style="text-align:center">
      
     <table cellspacing="1" id="table" style="width: 80%;margin:auto"> 
       <%
        for(int i=0;i<list.size();i++){
        	if(i%4==0){
        		if(i == 0 ){
        			%>
        			<tr class="asc"  onclick="updateClass(this)">
        			<%
        		}else {
        			%>
        			</tr>
        			<tr class="asc"  onclick="updateClass(this)">
        			<%
        		}
        	}
        	 
        	%> 
        <td width=25% ><input type="checkbox" name = "product"  value="<%=list.get(i).getId() %>"  id="<%=list.get(i).getId() %>" />&nbsp;<%=list.get(i).getName() %></td>
        	
       <%  	
        }
       int cout = list.size()%4 ;
       for(int i=0;i<4-cout;i++){
    	   %>
    	   
    	    <td width=25% ></td>
    	   <%
       }
       %>
       </tr>
     </table>
     </div>
     
     <div class="juese_head">功能权限</div>
     <div style="text-align:center">
      
     <table cellspacing="1" id="table" style="width: 80%;margin:auto"> 
       
       
       <%  
         String dealsend = "";
         String sencondDealsend = "";
         String sale = "";
         String send = "";
         listg = GroupService.getList();
         if(type == Group.Manger && null != map){
        	// listg = maplist.get(1+"");
        	%>  
        	 <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>管理员权限</td><td></td></tr></table></td></tr> 
        	 <tr class="asc"  onclick="updateClass(this)" class="asc"  onclick="updateClass(this)"><td width=25% >系统最高权限:</td><td width=25% >写(操作)<input type="checkbox" value="0_w" name = "permission" id="p0_w" onClick="return false" checked="checked" /></td><td width=25% >读(查看)<input type="checkbox" value="0-r" name = "permission" id="p0-r" />&nbsp;</td></tr>
        	<%
         }else if(type == Group.dealSend && null != map){ 
        	 listg = maplist.get(1+"");
        	 //dealsend = "checked=\"checked\" onClick=\"return false\"";
        	%>
        	 <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>基础信息类权限</td><td></td></tr></table></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >用户:</td><td width=25% >管理<input type="checkbox" value="3-w" name = "permission" id="p3-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="3-r" name = "permission" id="p3-r" /></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >门店:</td><td width=25% >管理<input type="checkbox" value="9-w" name = "permission" id="p9-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="9-r" name = "permission" id="p9-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >地区:</td><td width=25% >管理<input type="checkbox" value="10-w" name = "permission" id="p10-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="10-r" name = "permission" id="p10-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >产品:</td><td width=25% >可管理<input type="checkbox" value="4-w" name = "permission" id="p4-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="4-r" name = "permission" id="p4-r" /></td></tr> 
        	 <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>报装单类权限</td><td></td></tr></table></td></tr> 
         <tr class="asc"  onclick="updateClass(this)"><td width=25% >报装单:</td><td width=25% ></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="1-r" name = "permission" id="p1-r" /></td></tr>	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >修改单据:</td><td width=25% >可修改<input type="checkbox" value="17-w" name = "permission" id="p17-w" /></td><td width=25% ></td><td width=25% ></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >公司文员配工:</td><td width=25% >可配工<input type="checkbox" value="5-w" name = "permission" id="p5-w" <%=dealsend %>/></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="5-r" name = "permission" id="p5-r" /></td></tr> 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点释放申请:</td><td width=25% ></td><td width=25% >可确认<input type="checkbox" value="19-q" name = "permission" id="p19-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="19-r" name = "permission" id="p19-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >报装单回访:</td><td width=25% >可确认<input type="checkbox" value="20-w" name = "permission" id="p20-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="20-r" name = "permission" id="p20-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >厂送票已回:</td><td width=25% >可确认<input type="checkbox" value="21-w" name = "permission" id="p21-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="21-r" name = "permission" id="p21-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >厂送票已销:</td><td width=25% >可确认<input type="checkbox" value="22-w" name = "permission" id="p22-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="22-r" name = "permission" id="p22-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >厂送票已结款:</td><td width=25% >可确认<input type="checkbox" value="23-w" name = "permission" id="p23-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="23-r" name = "permission" id="p23-r" /></td></tr> 
        <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>库存类权限</td><td></td></tr></table></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >调货单权限:</td><td width=25% >可提交<input type="checkbox" value="13-w" name = "permission" id="p13-w" /></td><td width=25% >可确认<input type="checkbox" value="13-q" name = "permission" id="p13-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="13-r" name = "permission" id="p13-r" /></td></tr>	 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >库存权限:</td><td width=25% ></td><td width=25% ></td><td width=25% >可查询<input type="checkbox" value="12-r" name = "permission" id="p12-r" /></td></tr>	 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >预约调货权限:</td><td width=25% >可提交<input type="checkbox" value="28-w" name = "permission" id="p28-w" /></td><td width=25% >可确认<input type="checkbox" value="28-q" name = "permission" id="p28-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="28-r" name = "permission" id="p28-r" /></td></tr>	 	 
       <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>公司文员类权限</td><td></td></tr></table></td></tr> 
        
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >系统对比单:</td><td width=25% >可上传<input type="checkbox" value="24-w" name = "permission" id="p24-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="24-r" name = "permission" id="p24-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >提成标准:</td><td width=25% >可上传<input type="checkbox" value="25-w" name = "permission" id="p25-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="25-r" name = "permission" id="p25-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >销售单:</td><td width=25% >可上传<input type="checkbox" value="26-w" name = "permission" id="p26-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="26-r" name = "permission" id="p26-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >结款页:</td><td width=25% >可对比提交<input type="checkbox" value="29-w" name = "permission" id="p29-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="29-r" name = "permission" id="p29-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >导购提成</td><td width=25% >可对比提交<input type="checkbox" value="31-w" name = "permission" id="p31-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="31-r" name = "permission" id="p31-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点结款标准:</td><td width=25% >可操作<input type="checkbox" value="32-w" name = "permission" id="p32-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="32-r" name = "permission" id="p32-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点结款:</td><td width=25% >计算和确认<input type="checkbox" value="30-w" name = "permission" id="p30-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="30-r" name = "permission" id="p30-r" /></td></tr> 
        	 
        	 <% 
         }else if(type == Group.sencondDealsend && null != map){ 
        	listg = maplist.get(2+"");
        	//sencondDealsend = "checked=\"checked\" onClick=\"return false\""; 
        	%>
        	 <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>报装单类权限</td><td></td></tr></table></td></tr> 	
        	 <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点配工:</td><td width=25% >可配工<input type="checkbox" value="8-w" name = "permission" id="p8-w" <%= sencondDealsend%>/></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="8-r" name = "permission" id="p8-r" /></td></tr> 
             <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点释放申请:</td><td width=25% >可提交<input type="checkbox" value="19-w" name = "permission" id="p19-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="19-r" name = "permission" id="p19-r" /></td></tr> 
             <tr class="asc"  onclick="updateClass(this)"><td width=25% >待送货单据:</td><td width=25% >可送货<input type="checkbox" value="2-w" name = "permission" id="p2-w" <%= send%>/></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="2-r" name = "permission" id="p2-r" /></td></tr>
        	<tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>库存类权限</td><td></td></tr></table></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >调货单权限:</td><td width=25% ></td><td width=25% >可确认<input type="checkbox" value="13-q" name = "permission" id="p13-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="13-r" name = "permission" id="p13-r" /></td></tr>	 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >库存权限:</td><td width=25% ></td><td width=25% ></td><td width=25% >可查询<input type="checkbox" value="12-r" name = "permission" id="p12-r" /></td></tr>	 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >预约调货权限:</td><td width=25% >可提交<input type="checkbox" value="28-w" name = "permission" id="p28-w" /></td><td width=25% >可确认<input type="checkbox" value="28-q" name = "permission" id="p28-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="28-r" name = "permission" id="p28-r" /></td></tr>	 	 
        	<%
        	   
         }else if(type == Group.sale && null != map){
        	 listg = maplist.get(2+"");
        	// sale = "checked=\"checked\" onClick=\"return false\""; 
            %>
            <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>报装单类权限</td><td></td></tr></table></td></tr> 
            <tr class="asc"  onclick="updateClass(this)"><td width=25% >报装单:</td><td width=25% >可提交(导购上报)<input type="checkbox" value="1-w" name = "permission" id="p1-w" <%=sale %> /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="1-r" name = "permission" id="p1-r" /></td></tr>
            <tr class="asc"  onclick="updateClass(this)"><td width=25% >退货申请:</td><td width=25% >可提交<input type="checkbox" value="14-w" name = "permission" id="p14-w"  /></td><td width=25% ></td><td width=25% ></td></tr>
            <tr class="asc"  onclick="updateClass(this)"><td width=25% >换货申请:</td><td width=25% >可提交<input type="checkbox" value="16-w" name = "permission" id="p16-w"  /></td><td width=25% ></td><td width=25% ></td></tr>
              
            <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>库存类权限</td><td></td></tr></table></td></tr> 
            <tr class="asc"  onclick="updateClass(this)"><td width=25% >调货单权限:</td><td width=25% ></td><td width=25% >可确认<input type="checkbox" value="13-q" name = "permission" id="p13-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="13-r" name = "permission" id="p13-r" /></td></tr>	 	 
            <tr class="asc"  onclick="updateClass(this)"><td width=25% >库存权限:</td><td width=25% ></td><td width=25% ></td><td width=25% >可查询<input type="checkbox" value="12-r" name = "permission" id="p12-r" /></td></tr>	 	 
            <% 
         }else if(type == Group.send && null != map) {
        	// listg = map.get(3+"");
        	 send = "checked=\"checked\" onClick=\"return false\"";   
         }else { 
         %>     
               
        <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>基础信息类权限</td><td></td></tr></table></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >用户权限:</td><td width=25% >管理<input type="checkbox" value="3-w" name = "permission" id="p3-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="3-r" name = "permission" id="p3-r" /></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >门店权限:</td><td width=25% >管理<input type="checkbox" value="9-w" name = "permission" id="p9-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="9-r" name = "permission" id="p9-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >地区权限:</td><td width=25% >管理<input type="checkbox" value="10-w" name = "permission" id="p10-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="10-r" name = "permission" id="p10-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >产品权限:</td><td width=25% >可管理<input type="checkbox" value="4-w" name = "permission" id="p4-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="4-r" name = "permission" id="p4-r" /></td></tr> 
                  
        <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>报装单类权限</td><td></td></tr></table></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >报装单:</td><td width=25% >可提交(导购上报)<input type="checkbox" value="1-w" name = "permission" id="p1-w" <%=sale %> /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="1-r" name = "permission" id="p1-r" /></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >退货申请:</td><td width=25% >可提交<input type="checkbox" value="14-w" name = "permission" id="p14-w"  /></td><td width=25% >可确认<input type="checkbox" value="14-q" name = "permission" id="p14-q" /></td><td width=25% ></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >换货申请:</td><td width=25% >可提交<input type="checkbox" value="16-w" name = "permission" id="p16-w"  /></td><td width=25% ></td><td width=25% ></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >修改单据:</td><td width=25% >可修改<input type="checkbox" value="17-w" name = "permission" id="p17-w" /></td><td width=25% ></td><td width=25% ></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >公司文员配工:</td><td width=25% >可配工<input type="checkbox" value="5-w" name = "permission" id="p5-w" <%=dealsend %>/></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="5-r" name = "permission" id="p5-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点配工:</td><td width=25% >可配工<input type="checkbox" value="8-w" name = "permission" id="p8-w" <%= sencondDealsend%>/></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="8-r" name = "permission" id="p8-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点释放申请:</td><td width=25% >可提交<input type="checkbox" value="19-w" name = "permission" id="p19-w" /></td><td width=25% >可确认<input type="checkbox" value="19-q" name = "permission" id="p19-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="19-r" name = "permission" id="p19-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >待送货单据:</td><td width=25% >可送货<input type="checkbox" value="2-w" name = "permission" id="p2-w" <%= send%>/></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="2-r" name = "permission" id="p2-r" /></td></tr>
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >报装单回访:</td><td width=25% >可确认<input type="checkbox" value="20-w" name = "permission" id="p20-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="20-r" name = "permission" id="p20-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >厂送票已回:</td><td width=25% >可确认<input type="checkbox" value="21-w" name = "permission" id="p21-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="21-r" name = "permission" id="p21-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >厂送票已销:</td><td width=25% >可确认<input type="checkbox" value="22-w" name = "permission" id="p22-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="22-r" name = "permission" id="p22-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >厂送票已结款:</td><td width=25% >可确认<input type="checkbox" value="23-w" name = "permission" id="p23-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="23-r" name = "permission" id="p23-r" /></td></tr> 
        
        
        <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>库存类权限</td><td></td></tr></table></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >调货单权限:</td><td width=25% >可提交<input type="checkbox" value="13-w" name = "permission" id="p13-w" /></td><td width=25% >可确认<input type="checkbox" value="13-q" name = "permission" id="p13-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="13-r" name = "permission" id="p13-r" /></td></tr>	 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >库存权限:</td><td width=25% ></td><td width=25% ></td><td width=25% >可查询<input type="checkbox" value="12-r" name = "permission" id="p12-r" /></td></tr>	 	 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >预约调货权限:</td><td width=25% >可提交<input type="checkbox" value="28-w" name = "permission" id="p28-w" /></td><td width=25% >可确认<input type="checkbox" value="28-q" name = "permission" id="p28-q" /></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="28-r" name = "permission" id="p28-r" /></td></tr>	 	 
       
        
        <tr class="dsc"  onclick="updateClass(this)"><td colspan=4 align="left" ><table><tr><td></td><td>公司文员类权限</td><td></td></tr></table></td></tr> 
        
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >系统对比单:</td><td width=25% >可上传<input type="checkbox" value="24-w" name = "permission" id="p24-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="24-r" name = "permission" id="p24-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >提成标准:</td><td width=25% >可上传<input type="checkbox" value="25-w" name = "permission" id="p25-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="25-r" name = "permission" id="p25-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >销售单:</td><td width=25% >可上传<input type="checkbox" value="26-w" name = "permission" id="p26-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="26-r" name = "permission" id="p26-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >结款页:</td><td width=25% >可对比提交<input type="checkbox" value="29-w" name = "permission" id="p29-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="29-r" name = "permission" id="p29-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >导购提成</td><td width=25% >可对比提交<input type="checkbox" value="31-w" name = "permission" id="p31-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="31-r" name = "permission" id="p31-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点结款标准:</td><td width=25% >可操作<input type="checkbox" value="32-w" name = "permission" id="p32-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="32-r" name = "permission" id="p32-r" /></td></tr> 
        <tr class="asc"  onclick="updateClass(this)"><td width=25% >安装网点结款:</td><td width=25% >计算和确认<input type="checkbox" value="30-w" name = "permission" id="p30-w" /></td><td width=25% ></td><td width=25% >读(查看)&nbsp;<input type="checkbox" value="30-r" name = "permission" id="p30-r" /></td></tr> 
      <%} %>
     </table>
     </div>
     <div class="juese_head">添加上级管理组:
       </div>
     
      <div style="text-align:center">
      
     <table cellspacing="1" id="table" style="width: 80%;margin:auto"> 
       <%
       if(null != listg){ 
	        for(int i=0;i<listg.size();i++){
	        	String str = "";
	        	if(null != pid){ 
	        		if(pid.contains(listg.get(i).getId())){
		        		str = "checked=\"checked\"";
		        	} 
	        	}        	
	        	if(i%4==0){
	        		if(i == 0 ){
	        			%>
	        			<tr class="asc"  onclick="updateClass(this)">
	        			<%
	        		}else {
	        			%>
	        			</tr>
	        			<tr class="asc"  onclick="updateClass(this)">
	        			<%
	        		}
	        	}
	        	 
	        	%> 
	        <td width=25% ><input type="checkbox"  name = "pid"  value="<%=listg.get(i).getId() %>"  id="<%=listg.get(i).getId() %>" <%=str %>/>&nbsp;<%=listg.get(i).getName() %></td>
	       <%  	
	        } 
	        int coutpid = listg.size()%4 ;
	        for(int i=0;i<4-coutpid;i++){ 
	     	   %>
	     	    <td width=25% ></td>
	     	   <%
	        }
       }
       %>
       </tr>
     </table>
     </div> 
 
      
   <ul>
     <li><input type="submit" value="提  交" /></li>
     </ul>
 </form>  
   </div>
     </div>


</body>
</html>
