<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title> 
<base target="_parent"/>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link href="../style/zzsc.css" rel="stylesheet" type="text/css" />  
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/showlist.js" ></script>

 <script type = "text/javascript" language = "javascript"> 
 
function reloadopned(src){   
	$("#main",window.parent.document).find("#contentpage").attr("src",src);
		//$("#contentpage").attr("src");
  }  
  </script>
</head>

<body> 

<!--   头部开始   -->

  
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
              <p ><a href="javascript:void(0);"  onclick="reloadopned('loginN.jsp')">个人中心</a></p>
              <%
              if(UserManager.checkPermissions(user,Group.Manger)){
			     %> 
			    <p ><a href="javascript:void(0);"  onclick="reloadopned('company.jsp')">公司基本信息</a></p>
			    <%
			     }
			    %>
               </div>
          </li> 
          <%if(UserManager.checkPermissions(user, Group.dealSend) || UserManager.checkPermissions(user, Group.sale,"r") || UserManager.checkPermissions(user, Group.callback)){ %>
          <li>       
            <h4 >订单管理</h4>
            <div class="list-item none"> 
            <%
           if(UserManager.checkPermissions(user, Group.dealSend)){
            %> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdan.jsp')">文员派工页</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanrepare.jsp')">预约派工页</a></p> 
           <%}
            if(UserManager.checkPermissions(user, Group.sale,"r")){
                %> 
            <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanprintln.jsp')">查看订单页</a></p>
            <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanTuihuo.jsp')">退货订单</a></p>                  
             <%}
            
           if(UserManager.checkPermissions(user, Group.callback)){
            %> 
                
             <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdancallback.jsp')">客服未回访页</a></p>
               <%} 
           
           %>
           
            </div>  
          </li> 
          <% }
          if(UserManager.checkPermissions(user, Group.come) || UserManager.checkPermissions(user, Group.go) || UserManager.checkPermissions(user, Group.over) || UserManager.checkPermissions(user, Group.salecharge) ){%>
          <li >
            <h4 >厂送单管理</h4>
            <div class="list-item none">
            <% 
           if(UserManager.checkPermissions(user, Group.come)){
            %> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanCome.jsp')">厂送票未回</a></p>
           <%}
            if(UserManager.checkPermissions(user, Group.go)){
                %> 
             <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdango.jsp')">厂送票未消</a></p>
               <%}
            if(UserManager.checkPermissions(user, Group.over)){
                %> 
             <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanCharge.jsp')">厂送票未结款</a></p>
               <%}
            
            if(UserManager.checkPermissions(user, Group.sallOrder) || UserManager.checkPermissions(user, Group.Commission) || UserManager.checkPermissions(user, Group.sall)){
                %> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('uploadManage.jsp')">上传管理</a></p>
               <%}
            if(UserManager.checkPermissions(user, Group.salecharge) ){ 
                %> 
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('manualCheckout.jsp')">结款页</a></p>
               <%}
            %>
            </div>
          </li> 
          <% }
          if(UserManager.checkPermissions(user, Group.saledcharge) || UserManager.checkPermissions(user, Group.dealsendcharge)){%>
           <li >  
            <h4 >工资管理</h4> 
            <div class="list-item none">
            <% 
            if(UserManager.checkPermissions(user, Group.saledcharge)){
                %> 
                  <p ><a href="javascript:void(0);"  onclick="reloadopned('salaryCalc.jsp')">提成计算页</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('salaryExport.jsp')">提成导出页</a></p>
              
               <%}
            
            if(UserManager.checkPermissions(user, Group.dealsendcharge)){
            %> 
             <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanover.jsp')">安装网点结款页</a></p>  
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dealsendExport.jsp')">安装网点结款管理</a></p>
           <%}
            %>
            
             
            </div>
          </li>
           <li >
            <h4 >销售统计</h4>  
            <div class="list-item none">
              
              <p ><a href="javascript:void(0);"  onclick="reloadopned('uploadSaleCount.jsp')">票面销售统计</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('uploadSendCount.jsp')">送货销售统计</a></p>
              <%  if(UserManager.checkPermissions(user, Group.change)){%>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('changedetail.jsp')">转化规则</a></p>
               
             <%} 
              if(UserManager.checkPermissions(user, Group.change,"w")){
            	  %>
            	  <p ><a href="javascript:void(0);"  onclick="reloadopned('uploadchange.jsp')">生成转化规则</a></p>
             
              <% }%> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('uploadSendCountchange.jsp')">送货转换销售统计</a></p>
            </div>
          </li>  
           
          <%
          }
         if(UserManager.checkPermissions(user, Group.ManagerUser)){

          %> 
          <li >
            <h4 >职工管理</h4>
            <div class="list-item none">
              <p ><a href="javascript:void(0);"  onclick="reloadopned('huiyuan.jsp')">职工管理</a></p>
            </div>
          </li> 
          <% } 
         if(UserManager.checkPermissions(user,Group.inventory) || UserManager.checkPermissions(user, Group.inventoryquery) || UserManager.checkPermissions(user, Group.inventoryreserve)){
         %>
       <li > 
            <h4 >库存管理</h4>  
            <div class="list-item none">  
            <% 
              if(UserManager.checkPermissions(user, Group.inventory)){
            %> 
             <p ><a  href="javascript:void(0);"  onclick="reloadopned('inventory/receipts.jsp')">单据管理</a></p>
           <%}
            if(UserManager.checkPermissions(user, Group.inventoryquery)){
               %> 
               <p ><a  href="javascript:void(0);"  onclick="reloadopned('inventory/inventory.jsp')">库存查询</a></p>
               <%}
            if(UserManager.checkPermissions(user, Group.inventoryreserve)){
                %> 
                  <p ><a  href="javascript:void(0);"  onclick="reloadopned('inventory/inventoryAnalyze.jsp')">预约调货</a></p>
               <%}
            if(UserManager.checkPermissions(user, Group.inventoryreserve)){
                %> 
               <p ><a  href="javascript:void(0);"  onclick="reloadopned('inventory/analyzrecepts.jsp')">调货处理</a></p>   
               <%}
            
            %>

            </div>
          </li>  
         <% 
          }
         if(UserManager.checkPermissions(user, Group.juese)){
         %>
            <li >
            <h4 >岗位管理</h4>
            <div class="list-item none">
              <p ><a href="javascript:void(0);"  onclick="reloadopned('juesetype.jsp')">岗位管理</a></p>
            </div>
          </li>
            <%
         } 
         
         if(UserManager.checkPermissions(user, Group.addprodoct)){
    
          %>
          <li >
            <h4 >产品管理</h4>
            <div class="list-item none">
              <p ><a href="javascript:void(0);"  onclick="reloadopned('category.jsp')">产品管理</a></p>

            </div>
          </li>
          
          <%
         }
         if(UserManager.checkPermissions(user, Group.branch)){
        	 
        
           %>
          <li >
            <h4 >门店管理</h4>
            <div class="list-item none">
              <p ><a href="javascript:void(0);"  onclick="reloadopned('branch.jsp')">门店管理</a></p>
            </div>
          </li> 
          
          
          <%
         }
          if(UserManager.checkPermissions(user, Group.locate)){
          
          
          %>
          <li > 
            <h4 >地区管理</h4>
            <div class="list-item none">
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('locate.jsp')">地区管理</a></p>
            </div>
          </li>
          <%
          }if(UserManager.checkPermissions(user, Group.Manger)){
          %>
          
          <li >  
            <h4 >数据备份</h4>   
            <div class="list-item none">
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('database.jsp')">数据备份</a></p>
            </div>      
          </li> 
        <% 
        }if(UserManager.checkPermissions(user, Group.sencondDealsend)){
        	%>
        	<li > 
            <h4 >安装网点派工</h4>     
            <div class="list-item none">
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdanpeidan2.jsp')">安装网点派工</a></p>
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdanquery.jsp')">送货确认页</a></p> 
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdanpeidan2anzhuang.jsp')">安装派工</a></p> 
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdanpeidan2s.jsp')">安装网点查询</a></p>
            </div>  
            </li>  
            <li > 
             <h4 >费用结算</h4>     
            <div class="list-item none">
            
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dealsendExport.jsp')" >安装网点结款管理</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('sendExport.jsp')" >送货结款管理</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdan_chargep.jsp')" >送货结款</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdancallback.jsp')" >客服未回访页</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdan_charge.jsp')" >安装结款</a></p>
              <p ><a href="javascript:void(0);"  onclick="reloadopned('dispatch/dingdan_chargeall.jsp')" >送货安装结款</a></p> 
            </div>        
          </li> 
 	 
        <%	  
        }
        %>  
       <li > 
             <h4 >售后服务</h4>     
            <div class="list-item none">
            <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSaleall.jsp')" >售后查询</a></p>
            	 
            <%
              if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){ 
            %> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdansubmit.jsp')" >售后报单</a></p>

             <%   
             } 
            if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){  
             %>   
            <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalerepare.jsp')" >遗留数据处理</a></p>
                
              <% }
            if(UserManager.checkPermissions(user, Group.installOrderupload,"q") || UserManager.checkPermissions(user, Group.installOrderupload,"w") ){
            	 %>         
            	<p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSale.jsp')" >未上报工厂单据</a></p> 
           
            	 <%  
              }   
            if(UserManager.checkPermissions(user, Group.aftersaleCharge)){
            	%>   
            	    <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintaincharge.jsp')" >工厂未结算单据</a></p>
            	 <% 
              } 
            if(UserManager.checkPermissions(user, Group.maintainOrder,"w")){
            	%>   
            	   <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdansubmitmaintain.jsp')" >系统外维修保养配工</a></p>
            	 <% 
              } 
            
            if(UserManager.checkPermissions(user, Group.faultOrder,"w")){
            	%>  
            	  
            	 <!--   <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdansubmitfault.jsp')" >维修单配工</a></p> -->
            	   
            	 <%
              }
            if(UserManager.checkPermissions(user, Group.faultOrder,"q") && !UserManager.checkPermissions(user, Group.faultOrder,"w") || UserManager.checkPermissions(user, Group.maintainOrder,"q") && !UserManager.checkPermissions(user, Group.faultOrder,"w")){
            	%>  
            	              <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintaindealsend.jsp')" >网点保养单待配工</a></p>
            	 <% 
              }  
            if(UserManager.checkPermissions(user, Group.faultOrder,"w") || UserManager.checkPermissions(user, Group.faultOrder,"w")){
            	%>               
            	             <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalephone.jsp')" >当月需保养用户电话回访</a></p>
                          <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintain.jsp')" >网点驳回单据</a></p> 
          

            	 <%
              }   
            if(UserManager.checkPermissions(user, Group.faultOrder,"q") || UserManager.checkPermissions(user, Group.faultOrder,"q")){
            	%>               
            	             
                               <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintainupload.jsp')" >维修保养未完成单据</a></p> 

            	 <%
              }  
           
            	%>  
            	 

            </div>        
          </li>  
         
   
        
         </ul> 
        <script type="text/javascript" language="javascript">
			navList(12);
		</script>
      </div>
    </div>
  </div>

</div>


</body>
</html>
