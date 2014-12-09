package servlet;


import inventory.InventoryBranchManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import order.Order;
import order.OrderManager;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderPrint.OrderPrintlnService;

import user.User;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 核心请求处理类
 * 
 * @author 
 * @date 2013-05-18
 */ 

public class LogisticsServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 
	protected static Log logger = LogFactory.getLog(LogisticsServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */
	 
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		   
		 
		String method = request.getParameter("method");
		
		String uid = request.getParameter("uid"); 
		String oid = request.getParameter("oid");
		String opid = request.getParameter("opid");
		String devstatues = request.getParameter("statues");
		
		
		User user = (User)request.getSession().getAttribute("user");
		 
		Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
		
		int statues = getstatues(order);
		
		logger.info("method"+method);
		logger.info("opid"+opid);
		logger.info("devstatues"+devstatues);
		logger.info(statues);
		if(-1 == statues){
			if("peidan".equals(method)){
				statues = dealsend(user,order,uid,method);
			}else if((Order.orderpeisong+"").equals(method) || (Order.ordersong+"").equals(method)){
				statues = send(user,order,uid,method);
			}else if((Order.orderinstall+"").equals(method)){
				statues = install(user,order,uid,method);
			}else  if("songhuo".equals(method)){
				statues = songhuo(user,order,devstatues,method); 
			}    
		}else if(20 == statues){
			  
			 if((Order.orderreturn+"").equals(method)){
					statues = orderreturn(user,order,uid,method); 
		     }else if((""+Order.returns).equals(method)){
					statues = Orderreturns(user,order,devstatues,method); 
		      }   
		}  

		
		if((2+"").equals(devstatues) && !StringUtill.isNull(opid)){
			 
			OrderPrintln op = OrderPrintlnManager.getOrderStatues(Integer.valueOf(opid));
			  
			logger.info(StringUtill.GetJson(op));
			 
			if(null != op && op.getStatues() == 0){
				method = op.getType()+"";
				logger.info(method);
				if((OrderPrintln.release+"").equals(method)){
					statues = release(user,order,opid,method); 
				}else if((OrderPrintln.salerelease+"").equals(method)){
					statues = salerelease(user,order,opid,method); 
				}else if((OrderPrintln.salereleasereturn+"").equals(method)){
					statues = salereleasereturn(user,order,opid,method); 
				}else if((OrderPrintln.returns+"").equals(method)){ 
					statues = returns(user,order,opid,method); 
				}else if((OrderPrintln.salereleaseanzhuang+"").equals(method)){
					statues = salereleaseanzhuang(user,order,opid,method); 
				}else if((OrderPrintln.releasedispatch+"").equals(method)){
					statues = releasedispatch(user,order,opid,method); 
				} 
			} 
		}else if(!(2+"").equals(devstatues) && !StringUtill.isNull(opid)){
			String sql = "update  mdorderupdateprint set statues = 4 where id = " + opid+";";
			OrderPrintlnService.flag = true ;
			DBUtill.sava(sql);
		}
		
		try {
			response.getWriter().write(""+statues);
			response.getWriter().flush(); 
			response.getWriter().close(); 
		} catch (IOException e) {
			logger.info(e);
		}
    }
	/**
	 * 处理微信服务器发来的消息
	 */
	 
	public synchronized int dealsend(User user , Order order , String uid,String method){
		int count = -1 ;
		
		if(order.getDealsendId() == 0 ){
			List<String> listsql = new ArrayList<String>();

			String sql = "update mdorder set dealSendid = "+uid+" , printSatues = 1 , dealsendTime = '"+TimeUtill.gettime()+"'  where id = " + order.getId();		
			
			List<String> lists = InventoryBranchManager.chage(user, method, Integer.valueOf(uid), order);
			listsql.add(sql);
		    listsql.addAll(lists);
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		}
		
		return count ;
		 
	} 
	
	public synchronized int send(User user , Order order , String uid,String method){
		int count = -1 ;
				
		if(order.getSendId() == 0 ){
			List<String> listsql = new ArrayList<String>();
			String sql = "update mdorder set sendId = "+uid+"  , printSatuesp= 1  where id = " + order.getId() ;
			List<String> lists = InventoryBranchManager.chage(user, method, Integer.valueOf(uid), order); 
			listsql.add(sql);
		    listsql.addAll(lists);
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		}
		
		return count ;
		 
	} 
	
	public synchronized int Orderreturns(User user , Order order , String uid,String method){
		    int count = -1 ;
				
			List<String> listsql = new ArrayList<String>();
		     
			String sql =  "update mdorder set returnstatues = 1 , returntime = '"+TimeUtill.gettime()+"'  where id = " + order.getId() ;
			
			listsql.add(sql);  
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		
		
		return count ;
		 
	} 
	
	public synchronized int orderreturn(User user , Order order , String uid,String method){
		int count = -1 ;
				
		if(order.getReturnid() == 0 ){
			List<String> listsql = new ArrayList<String>();
		    
			String sql = "update mdorder set returnid = "+uid+" , returnprintstatues = 1  where id = " + order.getId() ;
			List<String> lists = InventoryBranchManager.chage(user, method, order.getDealsendId(), order);
			listsql.add(sql);
		    listsql.addAll(lists);
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		}
		
		return count ;
		 
	} 
	
	
	// 安装人员释放
	public synchronized int salereleaseanzhuang(User user , Order order , String uid,String method){
		int count = -1 ;
				
		if(order.getInstallid() != 0 ){
			List<String> listsql = new ArrayList<String>();
			
			String sql1 = "delete from mdorderupdateprint where id = " + uid;
			OrderPrintlnService.flag = true ;
			String sql = "update mdorder set installid = 0,printSatuesp = 0  where id = " + order.getId() ;

			listsql.add(sql);
			listsql.add(sql1);
			
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		}
		return count ;
		 
	} 
	
	// 文员向安装公司提交退货申请     
		public synchronized int releasedispatch(User user , Order order , String uid,String method){
			int count = -1 ;

				List<String> listsql = new ArrayList<String>();
				 
				String sql1 = "update  mdorderupdateprint set statues = 2 where id = " + uid+";";
				OrderPrintlnService.flag = true ;
				String sql2 =  "update mdorder set returnstatues = 2 , returntime = '"+TimeUtill.gettime()+"'  where id = " + order.getId() ;
				  
				listsql.add(sql1);  
				listsql.add(sql2); 
			    if( DBUtill.sava(listsql)){
			    	count = 1 ;
			    } 
			return count ; 
			 
		} 
	// 送货员释放
		public synchronized int salerelease(User user , Order order , String uid,String method){
			int count = -1 ;
					
			if(order.getSendId() != 0 ){
				List<String> listsql = new ArrayList<String>();
				
				String sql1 = "delete from mdorderupdateprint where id = " + uid;
				OrderPrintlnService.flag = true ;
				String sql = "update mdorder set sendId = 0, printSatuesp = 0  where id = " + order.getId();
				
				List<String> lists = InventoryBranchManager.chage(user, "salereleasesonghuo", order.getDealsendId(), order);
				 
				listsql.add(sql);
				listsql.add(sql1);
			    listsql.addAll(lists);
			    if( DBUtill.sava(listsql)){
			    	count = 1 ;
			    }
			}
			return count ;
			 
		} 
		
	// 安装网点释放
	public synchronized int release(User user , Order order , String opid,String method){
		int count = -1 ;
				
		if(order.getDealsendId() != 0 ){
			List<String> listsql = new ArrayList<String>();
			
			String sql1 = "delete from mdorderupdateprint where id = " + opid;
			OrderPrintlnService.flag = true ;
			String sql = "update mdorder set dealSendid = 0  , printSatues = 0  where id = " + order.getId();
			
			List<String> lists = InventoryBranchManager.chage(user, "shifang", order.getDealsendId(), order);
			
			listsql.add(sql);
			listsql.add(sql1);
		    listsql.addAll(lists);
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		}
		return count ;
		 
	} 
	
	// 退货员释放
		public synchronized int salereleasereturn(User user , Order order , String uid,String method){
			int count = -1 ;
					
			if(order.getReturnid() != 0 ){
				List<String> listsql = new ArrayList<String>();
				
				String sql1 = "delete from mdorderupdateprint where id = " + uid;
				OrderPrintlnService.flag = true ;
				String sql = "update mdorder set returnid = 0, printSatuesp = 0  where id = " +order.getId();
				
				List<String> lists = InventoryBranchManager.chage(user, "salereleasereturn", order.getDealsendId(), order);
				
				listsql.add(sql);
				listsql.add(sql1);
				
			    listsql.addAll(lists);
			    if( DBUtill.sava(listsql)){
			    	count = 1 ;
			    }
			}
			return count ;
			 
		} 
		
		// 导购同意安装网点释放 
				public synchronized int returns(User user , Order order , String uid,String method){
					int count = -1 ;
							
					if(!order.isreturn()){ 
						List<String> listsql = new ArrayList<String>();
						
						String sql2 = "delete from mdorderupdateprint where id = " + uid;
						OrderPrintlnService.flag = true ;
						String sql = "update mdorder set deliveryStatues  = (mdorder.deliveryStatues + 3)  where id = " + order.getId();
						
						if(order.getOderStatus().equals(20+"")){ 
							String sql1 = " delete from mdorderupdateprint where orderid = "+ order.getImagerUrl()+ " and mdtype = 10 ";
				            listsql.add(sql1);
						}
			            
						List<String> lists = InventoryBranchManager.chage(user,"returns", order.getDealsendId(), order);     
								
						listsql.add(sql);
						listsql.add(sql2);
					    listsql.addAll(lists);
					    if( DBUtill.sava(listsql)){
					    	count = 1 ;
					    }
					}
					return count ;
					 
				} 
				
	public synchronized int install(User user , Order order , String uid,String method){
		int count = -1 ;
		
		if(order.getInstallid() == 0 ){
			List<String> listsql = new ArrayList<String>();
			String sql = "update mdorder set installid = "+uid+" , printSatuesp= 1  where id = " + order.getId() ;
			listsql.add(sql);
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		}
		
		return count ;
		 
	} 
	
	public synchronized int songhuo(User user , Order order , String devstatues,String method){
		int count = -1 ;
		String sql = "";
		int statues = Integer.valueOf(devstatues);
		List<String> listsql = new ArrayList<String>();
		
		if(order.getOderStatus().equals(20+"")){
			if(order.getDeliveryStatues() != 1 && order.getDeliveryStatues() != 2){
				if((2 == statues || 1 == statues )){
					if(2 == statues){
						sql = "update mdorder set deliveryStatues = "+statues+" , deliverytype = 1 , sendTime = '"+TimeUtill.gettime()+"' , installTime = '"+TimeUtill.gettime()+"' , installid = mdorder.sendId   where id = " + order.getId();
					}else if(1 == statues){
						sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 ,  printSatuesp = 0 , sendTime = '"+TimeUtill.gettime()+"'  where id = " +order.getId();
					}
			    	String sql1 = " delete from mdorderupdateprint where orderid = "+ order.getImagerUrl();
			    	OrderPrintlnService.flag = true ;
			    	List<String> lists = InventoryBranchManager.chage(user, method, order.getDealsendId(), order);
				    listsql.addAll(lists); 
			    	listsql.add(sql1);
			    }
			}else if(order.getDeliveryStatues() == 1 && statues != 1){
				statues = 2 ;    
				sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 , installTime = '"+TimeUtill.gettime()+"'  where id = " + order.getId();
			}
			
	    }else {
	    	if(2 == statues){
				sql = "update mdorder set deliveryStatues = "+statues+" , deliverytype = 1 , sendTime = '"+TimeUtill.gettime()+"' , installTime = '"+TimeUtill.gettime()+"' , installid = mdorder.sendId   where id = " + order.getId();
			}else if(1 == statues) {  
				sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 ,  printSatuesp = 0 , sendTime = '"+TimeUtill.gettime()+"'  where id = " + order.getId();
			}else if( 4 == statues ||  9 == statues || 10 == statues){
				statues = 2 ;    
				sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 , installTime = '"+TimeUtill.gettime()+"'  where id = " + order.getId();
			} 
	    }  
		
		if(!StringUtill.isNull(sql)){
			listsql.add(sql); 
		}
		    if( DBUtill.sava(listsql)){
		    	count = 1 ;
		    }
		
		return count ;
		 
	} 
	
	public int getstatues(Order order){
		int count = -1 ; 
		List<OrderPrintln> list = OrderPrintlnManager.getOrderPrintlnbyOrderid(order.getId());
	     
		for(int i=0;i<list.size();i++){  
			OrderPrintln o = list.get(i);  
			if(o.getType() == OrderPrintln.modify && o.getStatues() != 4){
				count =  OrderPrintln.modify; 
			}else if(o.getType() == OrderPrintln.returns && o.getStatues() != 4){  
				count =  20 ;     
			} 
		}
		return count ;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			 {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 调用核心业务类接收消息、处理消息
		doGet(request,response);

		// 响应消息
	}
    
}
