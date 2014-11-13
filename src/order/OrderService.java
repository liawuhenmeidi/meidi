package order;

import group.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import user.User;
import user.UserManager;
import user.UserService;

import branch.BranchService;

public class OrderService {
	
	public static String getBranchName(int id){
		if(BranchService.getMap().containsKey(id)){
			return BranchService.getMap().get(id).getLocateName();
		}else{
			return "";
		}
	}
	 
	public static String getHtmlOver(List<Order> list){
		
		StringBuffer html = new StringBuffer();
		/*html.append("<tr id=\"th\"  >");
		html.append("<td align=\"center\" ><input type=\"checkbox\"  id=\"allselect\" onclick=\"seletall(allselect)\"></input> </td>");
		html.append("<td align=\"center\">单号</td>");
		html.append("<td align=\"center\">安装网点</td>");
		html.append("<td align=\"center\">顾客信息</td>");
		html.append("<td align=\"center\">送货名称</td>");
		html.append("<td align=\"center\">送货型号</td>");
		html.append("<td align=\"center\">送货数量</td>");
		html.append("<td align=\"center\">送货地区</td>");
		html.append("<td align=\"center\">送货地址</td>");
		html.append("<td align=\"center\">送货状态</td>");
		html.append("<td align=\"center\">备注</td>");
		html.append("<td align=\"center\">价格</td>");
		html.append("</tr>"); 
		html.append("</tr>"); */
		
		if(null != list){ 
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	List<OrderProduct> listop = o.getOrderproduct();
				 
				for(int m=0;m<listop.size();m++){
					OrderProduct op = listop.get(m);
					
					if(op.getStatues() == 0){
						String tdcol = " bgcolor=\"red\"" ;
						if(o.getPhoneRemark()!=1){
							tdcol = ""; 

						}
						
						html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
						html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\"  id=\"check_box\" name = "+o.getId()+ "></input></td>");
						html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+o.getPrintlnid() == null?"":o.getPrintlnid()+"</a></td>");
						html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getdealsendName()+"</td>");
						html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+"<p><font color=\""+tdcol+"\"> "+o.getPhone1()+"</td>  "); 
						html.append("<td align=\"center\">"+op.getCategoryName()+"</td> ");
						html.append("<td align=\"center\" >"+op.getTypeName()+"</td>  ");
						html.append("<td align=\"center\" >"+ op.getCount()+"</td> ");
						html.append("<td align=\"center\">"+o.getLocate()+"</td>");
						html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
						html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o)+"</td>");
						html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
						html.append("<td align=\"center\" id=\""+o.getId()+op.getId()+"\" style=\"background-color:#E8E8D0\">"+
		                            "<input type=\"text\" id="+op.getCategoryId()+" style=\"border:0; width:80px\"/>"+
		                            "</td>");
		
					}
				}
		    }
		}
		return html.toString();
		 
	}
    
   public static String getHtmlSearch(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
	     
		StringBuffer html = new StringBuffer();
		String tdcol = " bgcolor=\"red\"" ;
		
		if(null != list){
			boolean flag = false ; 
			if(UserManager.checkPermissions(user, Group.Manger)){
				flag = true ; 
			}
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);

				String release = ""; 
				String returns = "";
				
        		if(opmap.get(OrderPrintln.release) != null){ 
        			
        		     OrderPrintln orp = opmap.get(OrderPrintln.release).get(o.getId()); 
        		
	        		 if(orp != null){
	        			 if(orp.getStatues() == 2){ 
	        				 release = "<p>驳回申请已同意</p>";
	        		    	  }else if(orp.getStatues() == 4){ 
	        		    		  release = " <p>驳回申请已拒绝</p>";
	        				   }else {
	        					   release = orp.getMessage();
	        		           }   
	        		    }
        		}
        		
        		if(opmap.get(OrderPrintln.returns) != null){ 
        		OrderPrintln op1 = opmap.get(OrderPrintln.returns).get(o.getId());
        		
        		if(op1 != null){
        			 if(op1.getStatues() == 2){ 
        				      returns = "<p>退货申请已同意</p>"; 
        		    	  }else if(op1.getStatues() == 4){ 
        		    		  returns = "<p>退货申请已拒绝</p>";
        				  }else{
        					  returns = op1.getMessage();	  
        		          }
        		 }
        		}
        		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				if(flag){
					html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
				}  
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getdealsendName()+"<p/>"+o.getdealsendphone()+"</td>");
				
				html.append("<td align=\"center\"> "+ o.getChargeDealsendtime()+" </td>");
				
				html.append("<td align=\"center\">  "+(o.getStatues1()==0?"否":"是") +" </td>");
				
				html.append("<td align=\"center\">  "+(o.getStatues2()==0?"否":"是") +" </td>");
				
				html.append("<td align=\"center\">   "+(o.getStatues3()==0?"否":"是") +" </td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
                html.append("<td align=\"center\"> <a href=\"javascript:void(0);\" onclick=\"orderPrint('\"+o.getId()+\"',\"+o.getPrintSatues() +\",'','\"+o.getDeliveryStatues() +\"')\">[打印]</a></td>");

        		html.append("<td align=\"center\"> "+release +"</td>");
        		
        		html.append("<td align=\"center\"> "+returns +"</td>");
        		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmldeliveryStatuesTuihuo(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	     
		StringBuffer html = new StringBuffer();
		String tdcol = " bgcolor=\"red\"" ;
		
		if(null != list){
			
		
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
				
       		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				
       		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlcome(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	      
		StringBuffer html = new StringBuffer();
		String tdcol = " bgcolor=\"red\"" ;
		
		if(null != list){
			
		
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
				
      		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");

				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
      		
		    }
		}
		return html.toString();
		 
	}
   
   
   public static String getHtmlcallback(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	     
		StringBuffer html = new StringBuffer();
		String tdcol = " bgcolor=\"red\"" ;
		
		if(null != list){
	
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);

				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");

				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");

				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
                html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				
       		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlDispatching(User user ,List<Order> list){ 
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    
	    Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
	     
	    List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend);
	    
		StringBuffer html = new StringBuffer();
		String tdcol = " bgcolor=\"red\"" ;
		
		if(null != list){
			
			
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);

				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				 
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
	            
				 
		        int totalshifang = -1 ; 
		        OrderPrintln orp = null ;  
		        OrderPrintln op1 = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.returns, o.getId()) ;
		        OrderPrintln huanhuoObject = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.huanhuo, o.getId()) ;
		          
		        int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
			    int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());
			    int huanhuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.huanhuo, o.getId());
			    int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId());
			    int salereleasesonghuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleasesonghuo, o.getId());
			    int release	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId());
			    int salereleaseanzhuang	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleaseanzhuang, o.getId());
			    if(release != -1){    
			    	totalshifang = release ;
			    	orp = opmap.get(OrderPrintln.release).get(o.getId()); 
			    	
			    } 
			    if(salereleasesonghuo != -1){
			    	totalshifang = salereleasesonghuo ;
			    	orp = opmap.get(OrderPrintln.salereleasesonghuo).get(o.getId()); 
			    }
			    if(salereleaseanzhuang != -1){
			    	totalshifang = salereleaseanzhuang ;
			    	orp = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()); 
			    }
		       
		 
				
				html.append("<td align=\"center\">");
				    	   
				   if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) != 8 && o.getPrintSatues() == 0){
					   if(modify != 2 && modify != 0 && returns != 2 && returns != 0){
					   
						   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+ "\" >");
						   html.append("<option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp </option>");   
						    if(listS != null ){ 
				               for(int j=0;j< listS.size();j++){
				            	   User u = listS.get(j);
				            	   html.append("<option value="+u.getId()+">" +  u.getUsername()+" </option>");
				                    }
						     }
					            
						    html.append("</select> ");
						    html.append("<input type=\"button\" onclick=\"changepeidan('songh"+o.getId()+"','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+" ','"+o.getSendType(0,"</p>")+"','"+o.getSaleID() +"')\"  value=\"确定\"/> ");
		
				     }
				   }else if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) == 8 && o.getPrintSatues() == 0){
				
					   if(OrderManager.Check(o.getId())){ 
						     
						   html.append("<input type=\"button\" onclick=\"changepeidan('2','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+" ','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+" ')\"  value=\"打印\"/>"); 
						   html.append("&nbsp;&nbsp;&nbsp");
						  
					   }else {
						   
						   html.append("<input type=\"button\" onclick=\"changepeidan('1','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus()) +"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+" ')\"  value=\"打印\"/>");
						   html.append(" &nbsp;&nbsp;&nbsp");
						   html.append("<input type=\"button\" onclick=\"changepeidan('0','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+" ','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+" ')\"  value=\"确定\"/>");  
						   				   
						   
					   }
				   }else if(o.getDealsendId() != 0){
					       
					   html.append(usermap.get(o.getDealsendId()).getUsername() +"</p>"+ usermap.get(o.getDealsendId()).getPhone());
				   }
				 
				 html.append("</td> ");
				 html.append("<td align=\"center\"> "+
				              "<a href=\"javascript:void(0);\"  onclick=\"searchlocate('o.getId() ')\">[查看位置]</a> "+
				               "</td>"); 
				
				
				 html.append("<td align=\"center\">"); 
				   
						 if(totalshifang == 2){ 
					    	  
					    	    
					    	html.append(orp== null ?"":orp.getMessage()+"<p>驳回申请已同意</p>"); 
					    	 
					    	  }else if(totalshifang == 4){ 
							  
							    	 
					    		  html.append("<p>驳回申请已拒绝</p> ");
						    
							}else if(totalshifang != -1){
								
						  
					  
								html.append(orp== null ?"":orp.getMessage());  
					    	       
								html.append("<input type=\"button\" onclick=\"changes('"+orp.getId()+"','"+o.getId()+" ','"+OrderPrintln.comited+"','"+o.getDealsendId()+" ','','','"+totalshifang+" ',this)\"  value=\"同意\"/>") ;
								html.append("<input type=\"button\" onclick=\"changes('"+orp.getId()+"','"+o.getId()+" ','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+" ','','','"+totalshifang+"',this)\"  value=\"不同意\"/>");
					
					   }  
				
				
						 html.append("</td>");
				
						 html.append("<td align=\"center\">") ;
				
					 if(returns == 2){ 
				    	  
						 html.append("<p>退货申请已同意</p>");
				    	 
				    	  }else if(returns == 4){ 
						     	
						    	  
				    		  html.append("<p>退货申请已拒绝</p>");
						    	 
						    	 
						   }else if(returns != -1){
									 
							   html.append( op1.getMessage());
								  if(releasedispatch == 0 ){
								       
									  html.append("安装公司处理中"); 
								  } else if(releasedispatch == 2 ){
								  
									  html.append("<input type=\"button\" onclick=\"changes('"+op1.getId()+"','"+o.getId()+" ','"+OrderPrintln.comited+"','"+o.getDealsendId()+" ','"+releasedispatch+" ','"+o.getReturnstatuse() +"','"+OrderPrintln.releasedispatch+" ',this)\"  value=\"同意退货\"/>");
								   
								  }else {
									  if(Integer.valueOf(o.getOderStatus()) == 8){
									
										  html.append("<input type=\"button\" onclick=\"changes('"+op1.getId()+"','"+o.getId()+" ','"+OrderPrintln.comited+"','"+o.getDealsendId()+" ','"+releasedispatch +"','"+o.getReturnstatuse()+" ','"+OrderPrintln.release +"',this)\"  value=\"打印\"/>");
										  html.append(" <input type=\"button\" onclick=\"changes('"+op1.getId()+"','"+o.getId()+" ','"+OrderPrintln.comited+"','"+o.getDealsendId()+" ','"+releasedispatch +"','"+o.getReturnstatuse() +"','',this)\"  value=\"确定\"/> "); 
						
									
									  }else {
										  if(totalshifang == 0){
											
											  html.append(" 请先处理驳回信息");
											  
											    }else {
									           
											    	html.append("<input type=\"button\" onclick=\"changes('"+op1.getId()+"','"+o.getId()+" ','"+OrderPrintln.comited+"','"+o.getDealsendId() +"','"+releasedispatch+" ','"+o.getReturnstatuse()+" ','"+OrderPrintln.releasedispatch +"',this)\"  value=\"同意\"/>");
											    	html.append("<input type=\"button\" onclick=\"changes('"+op1.getId()+"','"+o.getId()+" ','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+" ','"+releasedispatch +"','"+o.getReturnstatuse() +"','"+OrderPrintln.releasedispatch +"',this)\"  value=\"不同意\"/>");   
								   } 
								 }
						      }
				         }
					 html.append("</td>");
             
					 html.append(" <td>");
              if(huanhuo == 2){
              
            	  html.append(" 安装公司处理中");
             
                }else if(huanhuo == 0){
            
              html.append(huanhuoObject.getMessage() );
              html.append("<input type=\"button\" onclick=\"changes('"+huanhuoObject.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+" ','"+huanhuo+"','-1','-1',this)\"  value=\"同意\"/>");
              html.append("<input type=\"button\" onclick=\"changes('"+huanhuoObject.getId()+"','"+o.getId()+" ','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+" ','"+huanhuo +"','-1','-1',this)\"  value=\"不同意\"/>");    
               }
             
              html.append("</td>");      		
		    }
		}
		return html.toString();
		 
	}

   
	
}
