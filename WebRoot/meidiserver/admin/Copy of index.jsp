<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");
String search = request.getParameter("search");

if(StringUtill.isNull(pageNum)){
	pageNum = "1"; 
}
if(StringUtill.isNull(numb)){
	numb = "10";
}

//int count = OrderManager.getCount();
int Page = Integer.valueOf(pageNum);
System.out.println("Page"+Page);

int num = Integer.valueOf(numb);
if(Page <=0){
	Page =1 ;
}

List<Order> list = null;
String serchProperty = "";

if(!StringUtill.isNull(search)){ 
	serchProperty = request.getParameter("serchProperty");
	list = OrderManager.getOrderlist(user,Group.Manger,num,Page,serchProperty,search);
}else {
	list = OrderManager.getOrderlist(user,Group.Manger,num,Page);
}

HashMap<Integer,User> usermap = UserManager.getMap();
  
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(Group.sencondDealsend);  

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
  System.out.println("%%%%%"+gMap);
//修改申请
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,0);
// 退货申请
Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,1);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link href="../style/zzsc.css" rel="stylesheet" type="text/css" />
<script src="../js/showlist.js" type="text/javascript"></script>
 
</head>

<body>


<!--   头部开始   -->
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
var pages = "" ;
var num = "";

$(function () {
	 $("#page").blur(function(){
		 pages = $("#page").val();
		 window.location.href="dingdan.jsp?pages="+pages;
	 });
	 
	// $("#search").blur(function(){
	 	
		// var search = $("#search").val();
		// var serchProperty = $("#serchProperty").val();
		 
		// window.location.href="dingdan.jsp?search="+search+"&serchProperty="+serchProperty;
	// });
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdan.jsp?page="+pages+"&numb="+num;
	 });  
});

function serch(){
	 var search = $("#search").val();
	 var serchProperty = $("#serchProperty").val();
	 
	 window.location.href="dingdan.jsp?search="+search+"&serchProperty="+serchProperty;

	
}
function func(str){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
}

function funcc(str,str2){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdan&id="+str2,
         dataType: "", 
         success: function (data) {
           //window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}

function changepeidan(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功"); 
           window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}


function change(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=songhuo&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功"); 
           window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}

function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function orderPrint(id,statues){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=print&id="+id+"&statues="+statues,
         dataType: "",  
         success: function (data) {  
           window.location.href="print.jsp?id="+id;
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}
</script>
<div class="head">
  <div class="head_logo"><img src="../style/image/logo.png" /></div>
</div>
<!--   头部结束   -->
 
<div class="main_a">
   <div class="main content">
    <div class="left-sider">
      <div class="operate">
        <h3> 后台管理 </h3>
        <ul id="J_navlist">
          <li > 
            <h4 >个人中心</h4>
            <div class="list-item none">
              <p ><a href="dingdan.jsp" target="_self">下载游戏</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">运行与更新</a></p>
            </div>
          </li>
          <li>   
            <h4 >订单管理</h4>
            <div class="list-item none">
              <p ><a href="http://sc.chinaz.com/" target="_self">帐号问题</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">密码问题</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">其他登录问题</a></p>
            </div>
          </li>
          <li >
            <h4 >职工管理</h4>
            <div class="list-item none">
              <p ><a href="http://sc.chinaz.com/" target="_self">游戏卡充值</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">第三方充值</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">会员充值</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">声讯充值</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">通宝充值</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">充值兑换</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">充值到账</a></p>
            </div>
          </li>
          <li >
            <h4 >职位管理</h4>
            <div class="list-item none">
              <p ><a href="http://sc.chinaz.com/" target="_self">银子获取</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">银子兑换与转账</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">银子使用</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">积分相关问题</a></p>
            </div>
          </li>
          <li >
            <h4 >产品管理</h4>
            <div class="list-item none">
              <p ><a href="http://sc.chinaz.com/" target="_self">登录密码</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">保护密码</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">支付密码</a></p>
            </div>
          </li>
          <li >
            <h4 >门店管理</h4>
            <div class="list-item none">
              <p ><a href="http://sc.chinaz.com/" target="_self">服装道具</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">杀猪作弊</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">游戏设置</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">游戏规则</a></p>
              <p ><a href="http://sc.chinaz.com/" target="_self">游戏过程</a></p>
            </div>
          </li>
          <li > 
            <h4 >地区管理</h4>
            <div class="list-item none">
              <p ><a href="branch.jsp" target="_self">地区管理</a></p>
            </div>
          </li>
        </ul>
        <script type="text/javascript" language="javascript">
			navList(12);
		</script>
      </div>
    </div>
  </div>
     
 <!--       -->    
     
   <div class="main_r_a">
  <div class="weizhi_head">现在位置：订单管理</div>        
   <div class="main_r_tianjia">
   </div>
   <div class="btn">
    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;每页显示条目数 
     	<select class = "category" name="category"  id="numb" >
     	 <option value="5"> 10</option>
     	 <option value="20"> 20</option>
     	 <option value="40"> 40</option>
     	 <option value="100"> 100</option>
     	</select>
  &nbsp; &nbsp; &nbsp; &nbsp;
     <a href="dingdan.jsp?page=1">首页</a>  
     <a href="dingdan.jsp?page=<%=Page-1%>">上一页</a>
     <a href="dingdan.jsp?page=<%=Page+1%>">下一页</a>
     <a href="dingdan.jsp?page">尾页</a>  
   &nbsp; &nbsp; &nbsp; &nbsp;
   
      第
     <input type="text" size="5" name="username" id ="page"/>    
        页
        
    共
    <%=count %>
    
   条记录  
   &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
   按
       <select class ="" name=""  id="" >
     	 <option value="andate"> 预约安装日期</option>
     	 <option value="saledate">销售日期</option>
     	 <option value="id">订单提交时间</option>
     	</select>
   排序 
   
   &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;
   
     搜索
       <select class ="serchProperty" name="serchProperty"  id="serchProperty" >
     	 <option value="andate"> 预约安装日期</option>
     	 <option value="saledate">销售日期</option>
     	 <option value="pos">pos单号</option>
     	 <option value="sailId"> 销售单号</option>
     	</select>
   <input type="text" size="5" name="search" id ="search"/>
   <input type="button" onclick="serch()"  value="查询"/>
   
   <a href="../Print?pageNum=<%=pageNum%>&numb=<%=numb %>&search=<%=search%>&serchProperty=<%=serchProperty %>" >导出excel文件</a>
      
    
</div>
      
       <br/>
   <div class="table-list">
<table  cellspacing="0" width="100%">
	<thead> 
		<tr> 
			<th align="left" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></th>
			<th align="left"></th>
			<th align="left">门店</th>
			<th align="left">pos单号</th>
			<th align="left">销售单号</th>
			<th align="left">验证码</th>
			
			<th align="left">姓名</th>
			<th align="left">电话</th>
			<th align="left">产品名称</th>
			<th align="left">产品型号</th>
			<th align="left">票面型号</th>
			<th align="left">数量</th>
			<th align="left">赠品</th>
			<th align="left">赠品数量</th>
			<th align="left">赠品状态</th>
            <th align="left">销售日期</th>
            <th align="left">安装日期</th>
            <th align="left">送货地址</th>
            <th align="left">送货状态</th>
			<th align="left">打印状态</th>
			
			<th align="left">提交表单人员</th>
			<th align="left">送货人员</th>
			<th align="left">备注</th>
			<th align="left">打印</th>
			<th align="left">配单</th> 
			<th align="left">修改申请</th>
			<th align="left">退货申请</th>
		</tr>
	</thead>
<tbody> 
  <% 
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
    <tr <%=col %>>
		<td align="left"><input type="checkbox" value="1" name="userid[]"/></td>
		<td align="left"><div id="<%=o.getId()%>"></div></td>
		<td align="left"><%=o.getBranch()%></td>
		<td align="left"><%=o.getPos() %></td>
		<td align="left"><%=o.getSailId() %></td>
		<td align="left"><%=o.getCheck() %></td>
		<td align="left"><%=o.getUsername() %></td>
		<td align="left"><%=o.getPhone1()%></td>  
	    <td align="left"></td>
		     <% 
		     String category = ""; ;
		     String type = "";
		     String saletype = "";
		     String countt = "";
		     List<OrderProduct> lists = OrPMap.get(o.getId());
		     if(lists != null ){
		     for(int g = 0 ;g<lists.size();g++){
		    	 OrderProduct op = lists.get(g);
		         type += op.getSendType()==null ||op.getSendType() == "null" ?"":op.getSendType()  +"</p>";
		         countt += op.getCount() +"</p>";
		         saletype += op.getSaleType()==null ||op.getSaleType() == "null" ? "":op.getSaleType() +"</p>";
		     }
		     }
		     %> 
		 
		 <td align="left"><%=type%></td>
		  <td align="left"><%=saletype%></td>
		 <td align="left"><%=countt%></td> 
		<% 
		     String gstatues = ""; ;
		     String gtype = "";
		     String gcountt = ""; 
		     
		     List<Gift> glists = gMap.get(o.getId());
		     
		     if(null != glists){
		
		     for(int g = 0 ;g<glists.size();g++){
		    	 
		    	 Gift op = glists.get(g);
		    	 if(null !=op ){
		    		 gtype += op.getName()+"</p>";
			         gcountt += op.getCount()+"</p>";
			         String statues = "";
			         if("0".equals(op.getStatues())){
			        	 statues = "需配送";
			         }else {
			        	 statues = "已自提";
			         }
			         gstatues += statues +"</p>";
		    	 }
		     }
		     }
		     %> 
		 <td align="left"><%=gtype%></td>
		 <td align="left"><%=gcountt%></td>
		 <td align="left"><%=gstatues%></td> 
		<td align="left"><%=o.getSaleTime() %></td>
		<td align="left"><%=o.getOdate() %></td>
		<td align="left"><%=o.getLocateDetail() %></td>
		<td align="left">
		<%
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == o.getDeliveryStatues()){
		%>
		 未发货
		<%
          }else if(1 == o.getDeliveryStatues()){

		%>
		正在送货
		<%
          }else if(2 == o.getDeliveryStatues()){
		%>
	    已送货
		<%
          }
		%>
		</td>
		<td align="left">
		
		<%
		//打印状态     0  未打印   1 打印
		if(0 == o.getPrintSatues()){
		%>
		 未打印
		<%
         }else if(1 == o.getPrintSatues()){
		%>
		已打印
		<%
         }
		%>
		
		
		</td>
		
		<td align="left"><%=usermap.get(Integer.valueOf(o.getSaleID())).getUsername() %></td>    
		<td align="left" style="white-space:nowrap;">
		
		<%  if(2 == o.getDeliveryStatues()) {
			 String str = "";
			 if(null != listS){
			 for(int j=0;j< listS.size();j++){
          	   User u = listS.get(j);
          	   if(u.getId() == o.getSendId()){
          		   str = u.getUsername() ;
          	   } 
			 }
			 }
         %>
         <%=str %>
         
		<%
         }else {
        	 String str = "";
        	 for(int j=0;j< listS.size();j++){
          	   User u = listS.get(j);
          	   
          	   if(u.getId() == o.getSendId()){
          		  str = u.getUsername() ;
          	   } 
        	 }
		%>
	<%=str %>
		<%
        
         }
		%>
		 
		 
		</td>
        
        <td align="left"> 
		    <%=o.getRemark() %>
		</td>
		
		<td align="left"> 
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1)">[打印]</a>
		</td>
		<td align="left">
		<%  if(2 == o.getDeliveryStatues()) { 
				for(int j=0;j< listS.size();j++){
	      	     User u = listS.get(j);
	      	     if(u.getId() == o.getSendId()){
	      		   %>
	      		   <%=u.getUsername() %>
	      		   
	      	      <% } 
				  }
         }else {
		%>
		<select class = "category" name="category"  id="songh<%=o.getId() %>" >
		 <option value="0"></option>
		<%    
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str = "";
            	   if(u.getId() == o.getSendId()){
            		   str = "selected=selected" ;
            	   } 
            	   %>
            	    <option value=<%=u.getId() %>  <%= str%>> <%=u.getUsername() %></option>
            	   <% 
            	  
                    }
	                	%>
         </select> 
      
         <input type="button" onclick="changepeidan('songh<%=o.getId()%>','<%=o.getId()%>')"  value="确定"/>
		<%
         }
		%>
		</td>
		<%
		OrderPrintln opp = opMap.get(o.getId());
		if(opp!= null){

		%>
		<td align="left"><span style="cursor:hand" onclick="funcc('ddiv<%=o.getId() %>',<%=o.getId() %>)"></span>
		 <div id="ddiv<%=o.getId()%>"  style= "" >
		  
		    
		    	 <%
		    	  if(opp.getStatues() == 2){
		    	
		    	 %> 
		    	
		    	   <p>修改申请已处理</p>
		    	   
		    	 <%
		    	 
		    	  }else {
		    	      %>
		    	     
		    	     <%=opp.getMessage() %>
		    	    
		    	     <input type="button" onclick="changes('<%=o.getId()%>')"  value="确定"/>
		    	     <%
		    	  }
		    	     %>
		   
		   
		 </div>
		</td>
		
		<%
		}else {
			
			%>	
		
		<td align="left"></td>
		
		<%
		}
		%>
		
		<%
		OrderPrintln oppp = opMap1.get(o.getId());
		if(oppp!= null){
 
		%>
		<td align="left">
		 <div id="ddiv<%=o.getId()%>"  style= "" >
		      <%
		    	  if(oppp.getStatues() == 2){
		    	
		    	 %> 
		    	
		    	   <p>退货申请已处理</p>
		  
		          <%  
		    	  }else {
		    	      %>
		    	     
		    	     <%=oppp.getMessage() %>
		    	    
		    	     <input type="button" onclick="changes('<%=o.getId()%>')"  value="确定"/>
		    	     <%
		    	  }
		    	     %>
		 </div>
		</td>
		 
		<%
		}else {
			%>	
			<td align="left"></td>
	   <%
		  }
		%>
    </tr>
    <%} %>
</tbody>
</table>
 <!--
<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="return confirm('您确定要删除吗？')"></input>

</div>  
 -->

     </div>


</div>
</div>


</body>
</html>
