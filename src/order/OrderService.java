package order;

import group.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import user.User;
import user.UserManager;
import user.UserService;

import branch.BranchService;

public class OrderService {
	 
	protected static Log logger = LogFactory.getLog(OrderService.class);
	 
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
						html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
						html.append("<td align=\"center\">"+o.getdealsendName()+"</td>");
						if(o.getPhoneRemark()!=1){   
							tdcol = ""; 
						}
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
		//logger.info(html.toString());
		return html.toString();
		 
	}
    
   public static String getHtmlSearch(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
	     
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
			boolean flag = false ; 
			if(UserManager.checkPermissions(user, Group.Manger)){
				flag = true ; 
			}
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
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
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				 
				html.append("<td align=\"center\" >"+ o.getSendprice(1,"</p>")+"</td>");
				 
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
                html.append("<td align=\"center\" >"+ o.getSendprice(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>");
				
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
				
                html.append("<td align=\"center\"> <a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',"+o.getPrintSatues() +",'','"+o.getDeliveryStatues() +"')\">[打印]</a></td>");

        		html.append("<td align=\"center\"> "+release +"</td>");
        		   
        		html.append("<td align=\"center\"> "+returns +"</td>");
        		
		    }
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlpSearch(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>"); 
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				html.append("<td align=\"center\">"+o.getRemark()+"</td>");
				
				html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
				
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\">"+o.getChargeSendtime()); 
				html.append("</td>");
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				html.append("<td align=\"center\">"+(o.getStatuescallback()==0?"否":"是" ));    
				html.append("</td>"); 
				
				html.append("<td align=\"center\">"); 
			    String message = "";
			    if(o.getStatuesinstall()==0){
			    	message = "否";
			    }else if(o.getStatuesinstall()==1){
			    	message = o.getInstalltime()  ;
			    }else if(o.getStatuesinstall()==2){
			    	message = "已忽略";
			    } 
			    html.append(message+"</td>");
		    } 
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlpcharge(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				html.append("<td align=\"center\">"+(o.getStatuescallback()==0?"否":"是" ));    
				html.append("</td>"); 
				
		    }  
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlpchargeall(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");
				 
				/*html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");*/
				 
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				html.append("<td align=\"center\">"+(o.getStatuescallback()==0?"否":"是" ));    
				html.append("</td>"); 
				
		    }  
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlppcharge(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				/*html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");*/
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				
		    }  
		}
		return html.toString();
		 
	} 
  
   public static String getHtmlpcallback(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
							
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				/*html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");*/
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				/*html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");*/
				
		    }  
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlSecondorderDispatching(User user ,List<Order> list){
	   
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    List<User> listSend = UserManager.getUsers(user,Group.send); 
	    
		StringBuffer html = new StringBuffer();
		 
		
		if(null != list){
			
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;

				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				html.append("<td align=\"center\">");
				
				int statuesnew = Order.orderpeisong; 
				int printid = o.getSendId();
			      if(o.getOstatues().salereleasereturno != null){
			    	  o.getOstatues().salereleaseo = o.getOstatues().salereleasereturno ;
			    	  printid = o.getReturnid();
			      } 
			      
			     if(o.getSendId() == 0 && o.getOstatues().release != 0 && o.getOstatues().releasedispatch != 0 ){
						   if(o.getDeliveryStatues() == 9){
							   statuesnew = Order.ordersong;
						   }else if(o.getDeliveryStatues() == 10){
							   statuesnew = Order.orderinsta; 
						   }
						    	  
				
						   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >");
						   html.append("<option value=\"\"></option>");
				   
			               for(int j=0;j< listSend.size();j++){
			            	   User u = listSend.get(j);
			            	   html.append("<option value="+u.getId()+"> "+u.getUsername()+"</option>");
			                    }
				                	
			               html.append("</select> ");  
			            
			               html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','"+statuesnew +"',"+o.getOstatues().release +",'"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"确定\"/>");
					
					} else {
						
					    if(usermap.get(Integer.valueOf(o.getSendId())) != null){
					       
					    	html.append(usermap.get(Integer.valueOf(o.getSendId())).getUsername());
					     
					      }
					} 
				
			     html.append("</td>"); 
			     html.append("<td align=\"center\"> ");
			     html.append("<a href=\"javascript:void(0);\"  onclick=\"searchlocate('"+o.getId() +"')\">[查看位置]</a>");
			     html.append("</td>");
				
			     html.append("<td align=\"center\">"); 
				 
				   if(o.getOstatues().release != 0 && o.getOstatues().releasedispatch != 0 && o.getOstatues().salerelease != 0 && (o.getOstatues().returns == -1 || o.getOstatues().returns == 4 )  ){       
					     
						 html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回订单\" onclick=\"winconfirm('"+o.getId()+"','"+ o.getOstatues().release+"','"+o.getSendId()+"')\"></input>");
					 
					 } 
				   html.append("</td>"); 
				 
				   html.append("<td align=\"center\">");     
				    
				      if(o.getOstatues().salereleaseo != null){
				    	  if(o.getOstatues().salereleaseo .getStatues() == 0 ){
				   
				    		  html.append(o.getOstatues().salereleaseo .getMessage()); 
				    		  html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseo .getId() +"','"+OrderPrintln.comited+"','','','"+OrderPrintln.salerelease+"','"+printid+"')\"  value=\"同意\"/>"); 
				    	      html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseo .getId() +"','"+OrderPrintln.uncomited+"','','','"+OrderPrintln.salerelease+"','"+printid +"')\"  value=\"不同意\"/>");
				   
				   } 
		        }
			
				html.append("</td>");
				html.append("<td align=\"center\">");   
				 
					 if(o.getOstatues().releaseo != null){
					  int sta = o.getOstatues().releaseo.getStatues();
			          String sm = "";
			          if(0 == sta){
			        	  sm = "待确认";
			          }else if(2== sta){
			        	  sm = "申请已同意"; 
			          }else if(4== sta){
			        	  sm = "申请被拒绝";
			          } 
			          html.append(sm );
			
			    	
			       } 

				html.append("</td>");
				html.append("<td align=\"center\">"); 
				   
				if(o.getOstatues().releasedispatch != -1){
					if(o.getOstatues().releasedispatch == 0){
						int statues = -1;    
						  
						if(o.getDeliveryStatues() == 0 || o.getDeliveryStatues() == 9 ){
							if(o.getSendId() == 0){
								statues = 0;  
							}else if(o.getSendId() != 0 ){
								statues = 2;
							}
							 
						}else if (o.getDeliveryStatues() == 1){
							statues = 1 ;  
						}else if(o.getDeliveryStatues() == 2){ 
							statues = 1 ; 
						}else if(o.getDeliveryStatues() == 10){
							if(o.getInstallid() != 0 ){
								statues = 3 ;
							}
						}  
				
					
						html.append(o.getOstatues().releaseo.getMessage());
						html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().releasedispatch+"','"+OrderPrintln.comited+"','"+statues +"','"+o.getReturnstatuse()+"','"+OrderPrintln.releasedispatch +"','"+o.getSendId()+"')\"  value=\"同意\"/>");
				   
				    if(o.getReturnid() == 0){ 
				 
				    html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().releasedispatch+"','"+OrderPrintln.uncomited+"','"+statues+"','"+o.getReturnstatuse()+"','"+OrderPrintln.releasedispatch+"','"+o.getSendId()+"')\"  value=\"不同意\"/>");  
						
				      }
					}
			    }
			
				html.append("</td>");
				  
				html.append("<td align=\"center\">");
				  if( o.getDeliverytype() != 0){
				   if(o.getReturnid() == 0){    
			
				html.append("<select class = \"category\" name=\"category\"  id=\"return"+o.getId() +"\" >");
				html.append(" <option value=\"0\"></option>");
				 
		               for(int j=0;j< listSend.size();j++){
		            	   User u = listSend.get(j);
		            	 
		            	  
		            	
		            	   html.append("<option value="+u.getId()+"> "+u.getUsername()+"</option>");
		            	  
		            	   
		                    }
			                	
		               html.append("</select>");   
		        
		         html.append("<input type=\"button\" onclick=\"change('return"+o.getId()+"','"+o.getId()+"','"+Order.orderreturn+"','-1','"+o.getSendType(0,"</p>")+"')\"  value=\"确定\"/>");
				} else { 
				// 0 表示未送货  1 表示正在送  2 送货成功
				 if(0 == o.getReturnstatuse()){
				
				html.append(" 商品未回");
				
		          }else if(1 == o.getReturnstatuse()){
				
		        	  html.append("商品已回");
				
				
		          }
				if( null != usermap.get(Integer.valueOf(o.getReturnid()))){
				
				 
				html.append(usermap.get(Integer.valueOf(o.getReturnid())).getUsername());
				 
				}
				}
				
				html.append("</td>"); 

				}
		    }
		} 
		return html.toString();
		 
	}
   
   public static String getHtmlporderDispatching(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    List<User> listSend = UserManager.getUsers(user,Group.send); 
	    
		StringBuffer html = new StringBuffer();
		 
		
		if(null != list){
			
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;

				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
			//	html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprintp()+"</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getsendName() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getSendtime() +"</td>");
				
				html.append("<td align=\"center\">"); 
				 
				if(o.getOstatues().release != 0 && o.getOstatues().releasedispatch != 0 && o.getOstatues().salerelease != 0 && (o.getOstatues().returns == -1 || o.getOstatues().returns == 4 ) && o.getOderStatus().equals(10+"") ){       
					     
				  html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回订单\" onclick=\"winconfirm('"+o.getId()+"','"+ o.getOstatues().release+"','"+o.getSendId()+"')\"></input>");
					 
				} 
				
				html.append("</td>"); 
				   
				html.append("<td align=\"center\">"); 
				
				if(o.getOstatues().releasedispatch != 0){
				   if(o.getDeliveryStatues() == 1 || o.getDeliveryStatues() == 9 || o.getDeliveryStatues() == 10){
					   
				   if(o.getInstallid() == 0 && o.getOstatues().release != 0){ 
				
					   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >"); 
					   html.append("<option value=\"\"></option>"); 
				    
		               for(int j=0;j< listSend.size();j++){
		            	   User u = listSend.get(j);
		  
		            	   html.append("<option value=\""+u.getId()+"\"> "+u.getUsername()+" </option>"); 
		            	  
		            	   
		                    }
			                	
		               html.append("</select>");   
		      
		            		   html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','"+Order.orderinstall+"')\"  value=\"确定\"/>"); 
				 }else {
					    
					  
					    	html.append(o.getinstallName()); 
						
						
				}
				
				html.append("</td>"); 
				
				html.append("<td align=\"center\">"); 				   
				     
				if(null != o.getOstatues().salereleaseanzhuango){
		         
				   html.append(o.getOstatues().salereleaseanzhuango.getMessage()); 
				   html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseanzhuango.getId()+"','"+OrderPrintln.comited+"','"+o.getInstallid()+"')\"  value=\"同意\"/>"); 
				   html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseanzhuango.getId()+"','"+OrderPrintln.uncomited+"','"+o.getInstallid()+"')\"  value=\"不同意\"/>"); 
				  
				  }
				  
				   } 
				     html.append("</td>"); 
				    html.append("<td align=\"center\">");       
 
					 if( null != o.getOstatues().releaseo){
					  int sta = o.getOstatues().releaseo.getStatues(); 
			          String sm = "";
			          if(0 == sta){ 
			        	  sm = "待确认";
			          }else if(2== sta){
			        	  sm = "申请已同意"; 
			          }else if(4== sta){
			        	  sm = "申请被拒绝";
			          }  
		        
			          html.append(sm); 
				 
			    	
			       } 
				    html.append("</td>");

				}
		    }
		} 
		return html.toString();
		 
	}
   
   
   public static String getHtmlorderquery(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    
		StringBuffer html = new StringBuffer();
		 
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\" >"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getinstallName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getInstalltime() +" </td>");
				
				 html.append("<td align=\"center\">");
			     boolean query = true;
			     int statues = OrderManager.getShifangStatues(o);	
					if(statues != -1){

						if(o.getOstatues().orp != null){
							
							query = false ;
							if(o.getOstatues().totalshifang == 4 ){
								query = true ;
						
								html.append("您的申请被拒绝");
								html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回\" onclick=\"winconfirm('"+statues+"','"+user.getUsertype() +"','"+o.getId()+"','"+o.getOstatues().totalshifang+"')\"></input>");	
						
							}else if(o.getOstatues().totalshifang == 0){
							
								html.append("您已提交");
						
							}
						}else {
						
							html.append("<input type=\"submit\" class=\button\" name=\"dosubmit\" value=\"驳回\" onclick=\"winconfirm('"+statues+"','"+user.getUsertype()+"','"+o.getId() +"','"+o.getOstatues().totalshifang+"')\"></input>");	
						
						} 
					}else {
					     	query = true ;
					     	html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回\" onclick=\"winconfirm('"+statues+"','"+user.getUsertype()+"','"+o.getId()+"','"+o.getOstatues().totalshifang+"')\"></input>");	
						
					}
			  
				html.append("</td>"); 
					 
			   html.append("<td align=\"center\">");
			 
			    if(o.getReturnid() == 0 && o.getOstatues().releasedispatch != 0  && o.getOstatues().totalshifang != 0){ 
			        if(o.getDeliveryStatues() == 0 || 9 == o.getDeliveryStatues()){
			 
			
			        	html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >");
			     
			       
			 
			        	html.append("<option value=\"\" ></option>"); 
			     
			        	html.append("<option value=\"2\" >送货+安装 </option>");   
			        	html.append("<option value=\"1\" >只送货 </option>");
			     
			        	html.append("</select>");  
			        	html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','songhuo')\"  value=\"确定\"/>");

			   
			
			    }else if(1 == o.getDeliveryStatues()  || 10 == o.getDeliveryStatues()){
			        	  
			    	html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >");
			    	html.append(" <option value=\"4\" >只安装 </option>");  
			                    html.append("</select>  "); 
			                    		html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','songhuo')\"  value=\"确定\"/>");
			    	
			          }
			    }
			    
			     
			    html.append(" </td>");
			    html.append("<td align=\"center\">"); 
					
			   
			    if(o.getReturnstatuse() == 0 && o.getReturnid() != 0 && query){

			   
			    
					 if(o.getReturnid() != 0){
						if(usermap.get(Integer.valueOf(o.getReturnid())) != null){
					 
							html.append(usermap.get(Integer.valueOf(o.getReturnid())).getUsername());
					
					  }
					}  
					
					
					 html.append("</td>");
					html.append("<td class=\"s_list_m\"> ");
					html.append("<select class =\"category\"  name=\"category\"  id=\"return"+o.getId() +"\" >");
 
									 html.append(" <option value=\"1\" >确认 </option>"); 
									 html.append("</select> "); 
									 html.append("<input type=\"button\" onclick=\"change('return"+o.getId()+"','"+o.getId()+"','"+Order.returns +"')\"  value=\"确定\"/>");
									 html.append("</td>");
		
			    }else if(o.getReturnstatuse() == 1){   
		
			    	html.append("<td class=\"s_list_m\">商品已退</td>"); 

			   }else {
				   

				   html.append("<td class=\"s_list_m\"></td>"); 

			   }
 
			    html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
			    
			    html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
			    
                html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
			    
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+o.getprintp()+"</td>");
				
				html.append(" <td align=\"center\">"); 
				
				      if(o.getPrintSatuesP() == 1){
				    	  html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
				  
			      }
				   html.append("</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">"); 
				html.append(o.getStatuesPaigong() == 1 ?"是":"否");
				 
					html.append(" </td>");
				
			
							html.append("<td align=\"center\">"); 
							html.append(o.getDeliverytype() == 2 ?"是":"否");
			
							html.append("</td>");
							html.append("<td align=\"center\">");    
							html.append(o.getStatuescallback()==0?"否":"是" ); 
							html.append("</td>"); 
							html.append("<td align=\"center\">");  
			
				    String message = "";
				    if(o.getStatuesinstall()==0){
				    	message = "否";
				    }else if(o.getStatuesinstall()==1){
				    	message = "是";
				    }else if(o.getStatuesinstall()==2){
				    	message = "已忽略";
				    } 

				    html.append(message);   
				    html.append("</td>");
				
		    }
		} 
		return html.toString();
		 
	}
   
   public static String getHtmldeliveryStatuesTuihuo(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	     
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
			
		
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
       		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
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
		
		
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
      		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
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
    
   public static String getHtmlgo(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	       
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
     		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
                html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");

				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
                html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
                html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
                html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				 
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
								
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				
				
				
				
				
				
				
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");

				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
     		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlcharge(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	      
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
    		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
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
		
		
		if(null != list){
	
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");

				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
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
	     
	    List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend);
	    
		StringBuffer html = new StringBuffer(); 
		

		if(null != list){

		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				  
				 
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				 
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendprice(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">");
				    	   
				   if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) != 8 && o.getPrintSatues() == 0){
					   if( o.getOstatues().returns != 2 && o.getOstatues().returns != 0){ 
					   
						   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+ "\" >");
						   html.append("<option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp </option>");   
						    if(listS != null ){ 
				               for(int j=0;j< listS.size();j++){
				            	   User u = listS.get(j);
				            	   html.append("<option value="+u.getId()+">" +  u.getUsername()+" </option>");
				                    }
						     }
					            
						    html.append("</select> ");
						    html.append("<input type=\"button\" onclick=\"changepeidan('songh"+o.getId()+"','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID() +"')\"  value=\"确定\"/> ");
		
				     }
				   }else if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) == 8 && o.getPrintSatues() == 0){
				
					   if(OrderManager.Check(o.getId())){ 
						     
						   html.append("<input type=\"button\" onclick=\"changepeidan('2','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"打印\"/>"); 
						   html.append("&nbsp;&nbsp;&nbsp");
						  
					   }else {
						   
						   html.append("<input type=\"button\" onclick=\"changepeidan('1','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus()) +"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"打印\"/>");
						   html.append(" &nbsp;&nbsp;&nbsp");
						   html.append("<input type=\"button\" onclick=\"changepeidan('0','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"确定\"/>");  
						   				   
						   
					   }
				   }else if(o.getDealsendId() != 0){
					       
					   html.append(usermap.get(o.getDealsendId()).getUsername() +"</p>"+ usermap.get(o.getDealsendId()).getPhone());
				   }
				 
				 html.append("</td> ");
				 html.append("<td align=\"center\"> "+
				              "<a href=\"javascript:void(0);\"  onclick=\"searchlocate('"+o.getId() +"')\">[查看位置]</a> "+
				               "</td>"); 
				
				
				 html.append("<td align=\"center\">"); 
				   
						 if( o.getOstatues().totalshifang == 2){ 
					    	  
					    	    
					    	html.append(o.getOstatues().orp== null ?"":o.getOstatues().orp.getMessage()+"<p>驳回申请已同意</p>"); 
					    	 
					    	  }else if(o.getOstatues().totalshifang == 4){ 
							  
							    	 
					    		  html.append("<p>驳回申请已拒绝</p> ");
						    
							}else if(o.getOstatues().totalshifang != -1){
								
						  
					  
								html.append(o.getOstatues().orp== null ?"":o.getOstatues().orp.getMessage());  
					    	       
								html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().orp.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','','','"+o.getOstatues().totalshifang+"',this)\"  value=\"同意\"/>") ;
								html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().orp.getId()+"','"+o.getId()+"','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+"','','','"+o.getOstatues().totalshifang+"',this)\"  value=\"不同意\"/>");
					
					   }  
				 
						 html.append("</td>");
				
						 html.append("<td align=\"center\">") ;
				
					 if(o.getOstatues().returns == 2){ 
				    	  
						 html.append("<p>退货申请已同意</p>");
				    	 
				    	  }else if(o.getOstatues().returns == 4){ 
						     	
						    	  
				    		  html.append("<p>退货申请已拒绝</p>");
						    	 
						    	 
						   }else if(o.getOstatues().returns != -1){
									 
							   html.append( o.getOstatues().op1.getMessage());
								  if(o.getOstatues().releasedispatch == 0 ){
								       
									  html.append("安装公司处理中"); 
								  } else if(o.getOstatues().releasedispatch == 2 ){
								  
									  html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch+"','"+o.getReturnstatuse() +"','"+OrderPrintln.releasedispatch+"',this)\"  value=\"同意退货\"/>");
								   
								  }else {
									  if(Integer.valueOf(o.getOderStatus()) == 8){
									
										  html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch +"','"+o.getReturnstatuse()+"','"+OrderPrintln.release +"',this)\"  value=\"打印\"/>");
										  html.append(" <input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch +"','"+o.getReturnstatuse() +"','',this)\"  value=\"确定\"/> "); 
						
									
									  }else {
										  if(o.getOstatues().totalshifang == 0){
											
											  html.append(" 请先处理驳回信息");
											  
											    }else {
									           
											    	html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId() +"','"+o.getOstatues().releasedispatch+"','"+o.getReturnstatuse()+"','"+OrderPrintln.releasedispatch +"',this)\"  value=\"同意\"/>");
											    	html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch +"','"+o.getReturnstatuse() +"','"+OrderPrintln.releasedispatch +"',this)\"  value=\"不同意\"/>");   
								   } 
								 }
						      }
				         }
					 html.append("</td>");
             
					 html.append(" <td>");
              if(o.getOstatues().huanhuo == 2){
              
            	  html.append(" 安装公司处理中");
             
                }else if(o.getOstatues().huanhuo == 0){
            
              html.append(o.getOstatues().huanhuoObject.getMessage() );
              html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().huanhuoObject.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().huanhuo+"','-1','-1',this)\"  value=\"同意\"/>");
              html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().huanhuoObject.getId()+"','"+o.getId()+"','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+"','"+o.getOstatues().huanhuo +"','-1','-1',this)\"  value=\"不同意\"/>");    
               }
             
              html.append("</td>");      		
		    }
		}
		return html.toString();
		 
	}

   
	
}
