<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
    
<%         
   
OrderGoodsAll oa = null;  
List<OrderGoods> list = null; 
String id = request.getParameter("id"); 
String statues = request.getParameter("statues");
String type = request.getParameter("type");
String branchname = ""; 
String remark = ""; 
String message = "导购订货单";  
 
//System.out.println(statues+"&&"+type);
 
if("0".equals(statues)){
	message = "调货单审核";
}
    
if("1".equals(type)){
	message = "订单生成"; 
}else if("2".equals(type)){
	message = "开单发货";
}else if("3".equals(type)){
	message = "历史订单";
}   
   
if(!StringUtill.isNull(id)){ 
	if((OrderMessage.billing+"").equals(type)){    
		oa = OrderGoodsAllManager.getOrderGoodsAllBySendid(user,id,statues); 
	}else if(("3").equals(type)){
		oa = OrderGoodsAllManager.getOrderGoodsAllByid(user, id); 
	}else{ 
		oa = OrderGoodsAllManager.getOrderGoodsAllByid(user,id,Integer.valueOf(statues),type);
	}
	System.out.println(oa); 
	branchname = oa.getOm().getBranchname();
	remark = oa.getOm().getRemark(); 
	list = oa.getList(); 
}  

String branchid = BranchService.getNameMap().get(branchname).getId()+"";
Map<String,InventoryBranch> map = InventoryBranchManager.getmapType(user,branchid);
String jsoninventory = StringUtill.GetJson(map); 
String json = StringUtill.GetJson(list); 
// System.out.println(json); 
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css"/> 
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<script type="text/javascript">
  
 $(function () { 

	
	   
 });  
 
 
</script> 
</head>

<body>
 
<div class="main">  
   <div class="weizhi_head">现在位置：<%=message  %></div>
   <div class="main_r_tianjia">
   <ul>                                                                                                          
     <li><a href="javascript:history.go(-1);">返回</a></li>
     </ul>   
   </div>             
     <div>            
      <table style="width:100% " > 
       <tr class="asc"> 
       <td  align=center>
                          门店 
        </td>   
        <td  align=center> 
        <%=branchname %> 
        </td> 
        <td  align=center> 
         单号：    <%=oa.getOm().getOid() %>
        </td> 
       <td align=center>
       日期：<%= oa.getOm().getSubmittime()%>
       </td>
        </tr>      
       <tr class="asc">   
       <td colspan=4 align=center> 
        <table   style="width:100% "  id="table" >
         <tr class="dsc">     
           <td align=center width="5%"   >编号</td> 
           <td align=center width="20%" > 产品型号</td> 
           <td align=center width="25%" >状态</td>  
            
           <td align=center width="10%">未入库数量</td>  
           <td align=center width="20%"> 订货数</td>  
            <td align=center width="20%">订单数</td>    
           <td align=center width="20%">实际发货数</td> 
           <td align=center width="20%">实际退货数</td>      
          </tr>  
          <%
          
          int realcount = 0 ;
          int ordercount = 0 ;
          int incount = 0 ;
          for(int i=0;i<list.size();i++){
        	  OrderGoods og = list.get(i);
        	  InventoryBranch in = map.get(og.getTname());
        	  int InNum = 0;
        	  if(null != in){
        		  InNum = in.getPapercount();
        	  }
        	  realcount +=og.getRealnum(); 
        	  incount += InNum;
        	  ordercount += InNum+og.getRealnum();
        	  %>
        <tr class="asc"> 
	     <td align=center   ><%=i+1 %></td> 
	     <td  align=center ><%= og.getTname() %></td> 
	     <td  align=center ><%=og.getStatuesName() %></td>   
	     <td align=center><%=  InNum %></td> 
	     <td align=center ><%= og.getRealnum() %></td> 
	     <td align=center ><%=  InNum+og.getRealnum()%></td> 
	      <td align=center ><%=  og.getRealsendnum() %></td> 
	       <td align=center ><%=  og.getReturnrealsendnum() %></td> 
	     </tr> 
        	   
        	  <%
          } %> 
          <tr class="asc"> 
	     <td align=center   >总计</td> 
	     <td  align=center ></td> 
	     <td  align=center ></td>   
	     <td align=center><%= incount %></td>  
	     <td align=center ><%= realcount %></td> 
	     <td align=center ><%= ordercount %></td> 
	     <td align=center ></td> 
	     <td align=center ></td> 
	     </tr>
          
        </table> 
       </td>
        
       </tr>
  
       <tr class="asc" >
       <td align=center colspan=2>
       
       </td>
                             
       <td align=center colspan=2>  备注：<%=remark %></td>
       </tr>
      </table>
     </div>

</div>


</body>
</html>
