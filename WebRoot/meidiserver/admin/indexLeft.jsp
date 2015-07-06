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
 
 var date = 0;
function reloadopned(src){   
	$("#main",window.parent.document).find("#contentpage").attr("src",src);
		//$("#contentpage").attr("src");
  }    
    
function getInventory(){
	 
	var mydate = new Date();  
	var dates = mydate.getTime();  
	//alert((dates - date)/1000/60); 
	//if((dates - date)/1000/60<5){  
	//	alert("您的操作过于频繁，请稍后重试");
	//	return ;
	//}   
	//alert(1);  
	$("#initInventory").html("正在刷新");
	//alert(dates - date);
	$("#initInventory").attr("disabled","true");
	    
		 $.ajax({    
		        type: "post",   
		         url: "server.jsp",
		         data:"method=InitInventorySN", 
		         dataType: "",        
		         success: function (data) { 
		        	 date = dates;
		        	 $("#initInventory").html("库存刷新");
		        	 $("#initInventory").removeAttr("disabled"); 
		           },  
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        // alert(errorThrown); 
		            } 
		           });
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
                        
             <%}
            
           if(UserManager.checkPermissions(user, Group.callback)){
            %> 
                
             <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdancallback.jsp')">客服未回访页</a></p>
               <%} 
           if(UserManager.checkPermissions(user, Group.tuihuo)){
               %> 
                   
                <p ><a href="javascript:void(0);"  onclick="reloadopned('dingdanTuihuo.jsp')">退货订单</a></p>     
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
          <%} %>
        
           <%
           boolean ordergoods_w = UserManager.checkPermissions(user, Group.ordergoods,"w");
           boolean ordergoods_q =UserManager.checkPermissions(user, Group.ordergoods,"q");
           boolean ordergoods_c= UserManager.checkPermissions(user, Group.ordergoods,"c");
           boolean ordergoods_r= UserManager.checkPermissions(user, Group.ordergoods,"r");
           boolean ordergoods_e= UserManager.checkPermissions(user, Group.ordergoods,"e");
             
           if(ordergoods_w || ordergoods_q || ordergoods_c || ordergoods_r){
           %>
             <li >  
            <h4 >卖场开单</h4> 
            <div class="list-item none">   
         
           <%
           if(ordergoods_w){
           %>
            <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoods.jsp')">增加调货单</a></p> 
           <%
           }
           if(ordergoods_q){
               %>
                  <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodsall.jsp')">调货单审核</a></p> 
               <%
               }
           if(ordergoods_c){
               %>
                 <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodexamine.jsp')">调货单生成订单</a></p> 
               <%
               }
           if(ordergoods_r){
               %> 
                  <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodbilling.jsp')">查看订单</a></p> 
               <%
               } 
           %>      
            </div>   
          </li>    
            
          <li>   
            <h4 >数据管理</h4> 
            <div class="list-item none"> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('date/dateinventory.jsp')">库存数据</a></p>
               <p ><a href="javascript:void(0);"  onclick="reloadopned('date/datesale.jsp')">销售数据</a></p>
            <p ><a href="javascript:void(0);"  onclick="reloadopned('date/dateinventorychange.jsp')">出入库数据</a></p>
            </div>
          </li> 
           <%} %>  
           <%  
          
           if(ordergoods_e || ordergoods_c){
           
           %>
           
             <li >
            <h4 >发货管理</h4>
            <div class="list-item none">
                <%  
                if(ordergoods_c){
                    %>
                      <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodssend.jsp')">开单发货</a></p> 
                    <%
                    } 
                if(ordergoods_e){
                    %> 
                     <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodssended.jsp')">修改实收数量</a></p> 
                      <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodsInstorage.jsp')">卖场入库</a></p> 
                 <p ><a href="javascript:void(0);"  onclick="reloadopned('ordergoods/ordergoodssendhistory.jsp')">历史订单</a></p> 
                 <p ><a href="javascript:void(0);"  onclick="reloadopned('receivegoods/receivegoods.jsp')">收货记录</a></p> 
                    
                    <%
                    }  
                %>
            </div>  
          </li>
          <%}
           
           
           
           if(ordergoods_r){
           %>
           <li >
            <h4 >盘点库存管理</h4>
            <div class="list-item none">
              <% 
              if(ordergoods_r){
                  %>   
                    <p ><a href="javascript:void(0);"  onclick="getInventory();" id="initInventory">库存刷新</a></p> 
                     
<!--  
                    <p ><a  href="javascript:void(0);"  onclick="reloadopned('inventory/inventory.jsp')">库存查询</a></p>
                     -->
                    <p ><a  href="javascript:void(0);"  onclick="reloadopned('inventory/inventorycheck.jsp')">库存盘点</a></p>
                  
               
               
                 <!-- 
               <p ><a  href="javascript:void(0);"  onclick="reloadopned('snInventory/inventoryline.jsp')">线性图</a></p>
                
                 <p ><a  href="javascript:void(0);"  onclick="reloadopned('snInventory/inventorybar.jsp')">柱形图</a></p>
                   -->
                 
                  <% 
                  } 
              
              %>
            </div>
          </li> 
        
           <li >
            <h4 >增值服务</h4>
            <div class="list-item none">
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('receivegoods/receiveorder.jsp')">退货订单</a></p>
                               <p ><a  href="javascript:void(0);"  onclick="reloadopned('receivegoods/receiveordertype.jsp')">退货订单统计</a></p>
                 <!--   
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('receivegoods/receiveorderover.jsp')">已退货订单</a></p>
              -->  
              <p ><a  href="javascript:void(0);"  onclick="reloadopned('snInventory/inventory.jsp')">销售查询</a></p>
                         <p ><a  href="javascript:void(0);"  onclick="reloadopned('snInventory/inventoryDynamicBranch.jsp')">动销率</a></p>
               <p ><a  href="javascript:void(0);"  onclick="reloadopned('snInventory/inventorytype.jsp')">库存类别</a></p>
            </div>
          </li>
           
            <%} %>
          <%  
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
          
         %>
          
          
            
          <%
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
              <p ><a href="javascript:void(0);"  onclick="reloadopned('branch.jsp')">门店(销售系统)管理</a></p>
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
        
        <%
        boolean installOrderupload_w = UserManager.checkPermissions(user, Group.installOrderupload,"w");
        boolean installOrderupload_q = UserManager.checkPermissions(user, Group.installOrderupload,"q");
        boolean aftersaleCharge = UserManager.checkPermissions(user, Group.aftersaleCharge);
        boolean maintainOrder_w = UserManager.checkPermissions(user, Group.maintainOrder,"w");
        boolean faultOrder_w = UserManager.checkPermissions(user, Group.faultOrder,"w");
        boolean faultOrder_q = UserManager.checkPermissions(user, Group.faultOrder,"q");
        if(installOrderupload_w || installOrderupload_q ||aftersaleCharge||maintainOrder_w||faultOrder_w||faultOrder_q){
        %>
         
       <li > 
             <h4 >售后服务</h4>     
            <div class="list-item none">
            <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSaleall.jsp')" >售后查询</a></p>
            	 
            <%
              if(installOrderupload_w){ 
            %> 
              <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdansubmit.jsp')" >自营售后报单</a></p>

             <%     
             } 
            if(installOrderupload_q){  
             %>   
            <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalerepare.jsp')" >遗留数据处理</a></p>
                
              <% }
            if(installOrderupload_q || installOrderupload_w){
            	 %>         
            	<p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSale.jsp')" >未上报工厂单据</a></p> 
           
            	 <%  
              }   
            if(aftersaleCharge){
            	%>   
            	    <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintaincharge.jsp')" >工厂未结算单据</a></p>
            	 <% 
              } 
            if(maintainOrder_w){
            	%>   
            	   <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdansubmitmaintain.jsp')" >系统外维修保养配工</a></p>
            	 <% 
              } 
            
            if(faultOrder_w){
            	%>  
            	  
            	 <!--   <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdansubmitfault.jsp')" >维修单配工</a></p> -->
            	   
            	 <%
              }
            if(faultOrder_q && !faultOrder_w ){
            	%>  
            	              <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintaindealsend.jsp')" >网点保养单待配工</a></p>
            	 <% 
              }  
            if(faultOrder_w ){
            	%>               
            	             <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalephone.jsp')" >当月需保养用户电话回访</a></p>
                          <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintain.jsp')" >网点驳回单据</a></p> 
          

            	 <%
              }   
            if(faultOrder_q ){
            	%>               
            	             
                               <p ><a href="javascript:void(0);"  onclick="reloadopned('afterSale/dingdanAfterSalemaintainupload.jsp')" >维修保养未完成单据</a></p> 

            	 <%
              }  
           
            	%>  
            	 

            </div>        
          </li>  
         <%} 
          
         
        boolean logistics_p = UserManager.checkPermissions(user, Group.logistics,"p");
        boolean logistics_c = UserManager.checkPermissions(user, Group.logistics,"c");
         if(logistics_p || logistics_c){
        	%>   
        	 <li > 
        	 <h4 >物流系統</h4>     
            <div class="list-item none"> 
            <% 
            if(logistics_p){
            	%>
            	 <p ><a href="javascript:void(0);"  onclick="reloadopned('logistics/logistics.jsp')" >物流配工</a></p> 
            	 <p ><a href="javascript:void(0);"  onclick="reloadopned('logistics/logisticslist.jsp')" >配工信息</a></p> 
            	  
            	<%
            }  
            if(logistics_c){ 
            	  
            	%>  
            	<p ><a href="javascript:void(0);"  onclick="reloadopned('logistics/caradd.jsp')" >车辆登记</a></p> 
            	<p ><a href="javascript:void(0);"  onclick="reloadopned('logistics/carlist.jsp')" >查看车辆</a></p> 
            	<%
            }
            
            %>

            </div> 
           </li>
        	
        	<%
         }
         
         
         %>
   
        
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
