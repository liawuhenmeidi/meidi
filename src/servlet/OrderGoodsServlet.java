package servlet;
  
import inventory.InventoryBranchManager;
import inventory.InventoryManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ordersgoods.OrderGoods;
import ordersgoods.OrderGoodsAll;
import ordersgoods.OrderGoodsAllManager;
import ordersgoods.OrderGoodsManager;
import ordersgoods.OrderMessage;
import ordersgoods.OrderMessageManager;

import user.User;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.BranchService;

import product.Product;
import product.ProductService;
  
/**
 * 核心请求处理类
 * 
 * @author
 * @date 2013-05-18
 */

public class OrderGoodsServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;

	protected static Log logger = LogFactory.getLog(OrderGoodsServlet.class);

	/**
	 * 确认请求来自微信服务器
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		String method = request.getParameter("method");

		// String uuid = request.getParameter("uuid");
		logger.info(method);

		if ("add".equals(method)) {
			add(request, response);

		} else if ("updaterealsendnum".equals(method)) {
			updaterealsendnum(request, response);
		} else if ("updateIOS".equals(method)) {
			// logger.info(1);
			updateIOS(request, response);
		}

	}

	/**
	 * 处理微信服务器发来的消息
	 */

	public void add(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		int statues = -1;
		// String oid = request.getParameter("oid");
		String branchid = request.getParameter("branchid");
		String id = request.getParameter("id");
		String remark = request.getParameter("remark");
		String opstatues = request.getParameter("opstatues");
		// logger.info(opstatues);
		String types = request.getParameter("type");

		// logger.info(remark);
		OrderGoodsAll oa = new OrderGoodsAll();
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		String rows = request.getParameter("rows");
		OrderMessage om = new OrderMessage();
		if (StringUtill.isNull(id)) {
			id = OrderMessageManager.getMaxid() + "";
			om.setId(Integer.valueOf(id));
			// om.setOid(oid); 
			om.setSubmitid(user.getId()); 
			om.setSubmittime(TimeUtill.gettime()); 
			if (!StringUtill.isNull(branchid)) {
				om.setBranchid(BranchService.getNameMap().get(branchid).getId());
				statues = 1;
			} else {
				om.setBranchid(Integer.valueOf(user.getBranch()));
				statues = 0;
			}
		} else {
			OrderMessage oms = OrderMessageManager.getbyid(user, id);
			om.setId(Integer.valueOf(id));
			// om.setOid(oid);
			om.setSubmitid(oms.getSubmitid());
			om.setSubmittime(oms.getSubmittime());
			if (!StringUtill.isNull(branchid)) {
				om.setBranchid(BranchService.getNameMap().get(branchid).getId());
				statues = 1;
			} else {
				om.setBranchid(Integer.valueOf(user.getBranch()));
				statues = 0;
			}
		}

		om.setRemark(remark);
		if (!StringUtill.isNull(opstatues)) {
			om.setOpstatues(Integer.valueOf(opstatues)); 
		} else {
			om.setOpstatues(Integer.valueOf(0));
		}
		
		
		
		
		if(StringUtill.isNull(rows)){
			
		}else {
			String[] rowss = rows.split(",");

			for (int i = 0; i < rowss.length; i++) {
				String row = rowss[i];
				String type = request.getParameter("product" + row);
				Product p = ProductService.gettypemap(user).get(type);
				int cid = p.getCategoryID();
				int itype = p.getId();
				String sta = request.getParameter("statues" + row);
				String num = request.getParameter("orderproductNum" + row);
				String invenNum = request.getParameter("papercount" + row);
	           // logger.info(invenNum);  
				if (StringUtill.isNull(num)) {
					num = 0 + "";
				}

				if (StringUtill.isNull(invenNum)) {
					invenNum = "0";
				}
				if (!StringUtill.isNull(type) && !StringUtill.isNull(sta)) {
					OrderGoods op = new OrderGoods();
					// op.setOid(oid);
					// op.setOpstatues(Integer.valueOf(opstatues));
					op.setOrdernum(Integer.valueOf(num) + Integer.valueOf(invenNum));
					op.setRealnum(Integer.valueOf(num));
					op.setStatues(Integer.valueOf(sta));
					op.setSubmitid(user.getId());
					op.setSubmittime(TimeUtill.getdateString());
					op.setCid(cid);
					op.setTid(Integer.valueOf(itype));
					op.setMid(Integer.valueOf(id));
					if ((OrderMessage.billing + "").equals(types)) {
						op.setBillingstatues(OrderMessage.billing);
					}  
					// op.setUuid(uuid);
					list.add(op);
				}

			}
		}
		

		oa.setOm(om);
		oa.setList(list);

		if (OrderGoodsAllManager.save(user, oa)) {
			try {
				//logger.info(statues);
				if (0 == statues) {
					response.sendRedirect("../jieguo.jsp?type=ordergoodsadd&mark=" + 1);
				} else {    
					//logger.info(opstatues); 
					if (!StringUtill.isNull(opstatues) && Integer.valueOf(opstatues) == 0 ) {
						response.sendRedirect("../admin/ordergoods/ordergoodsupdate.jsp?id="+id+"&type="+OrderMessage.unexamine+"&statues="+OrderMessage.unexamine);
					}else{     
						response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1); 
					}
					
				}

				// response.getWriter().write(""+statues);
				// response.getWriter().flush();
				// response.getWriter().close();
			} catch (IOException e) {
				logger.info(e);
			}
		}
		;
	}

	public void updaterealsendnum(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		List<String> listsql = new ArrayList<String>();
		String id = request.getParameter("id");
		String statues = request.getParameter("statues");
		String type = request.getParameter("type");

		OrderGoodsAll oa = null;
		List<OrderGoods> list = null;
		if (!StringUtill.isNull(id)) {
			if ((OrderMessage.billing + "").equals(type)) {
				oa = OrderGoodsAllManager.getOrderGoodsAllBySendid(user, id,
						statues);
			} else {
				oa = OrderGoodsAllManager.getOrderGoodsAllByid(user, id,
						Integer.valueOf(statues), type);
			}

			list = oa.getList();
		}

		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				OrderGoods og = list.get(i);
				int ogid = og.getId();
				int operatortype = 14;
				String realsendnum = request.getParameter("realsendnum" + ogid);
				int realcont = 0;
				if (StringUtill.isNull(realsendnum)) {
					realsendnum = "0";
				} 
				
				String sql = OrderGoodsManager.updaterealsendnum(user,
						ogid, realsendnum);  
				listsql.add(sql);   
				if (og.getRealnum() != Integer.valueOf(realsendnum) && og.getStatues() != 5 ) {
					realcont = (Integer.valueOf(realsendnum) - og.getRealnum());

					if (og.getStatues() == 6 || og.getStatues() == 7
							|| og.getStatues() == 8 || og.getStatues() == 9) {
						realcont = -realcont;
						operatortype = 16;
					}

					String sqlIB = "";
					String sqlIBM = ""; 

					if (null == InventoryBranchManager.getInventoryID(user, oa
							.getOm().getBranchid(), og.getTid() + "")) {
						sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)"
								+ "  values ( null,"
								+ og.getCid()
								+ ", '"
								+ og.getTid()
								+ "', '"
								+ 0 
								+ "', '"
								+ realcont
								+ "'," + oa.getOm().getBranchid() + ")";

						sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
								+ "  values ( null, '"
								+ oa.getOm().getBranchid()
								+ "', '"
								+ oa.getOm().getId()
								+ "','"
								+ oa.getOm().getId()
								+ "','"
								+ TimeUtill.gettime()
								+ "','"
								+ og.getTid()
								+ "',"
								+ 0
								+ ",'"
								+ realcont
								+ "',"
								+ operatortype
								+ "," 
								+ 0 
								+ ","
								+ realcont
								+ ","
								+ 1 
								+ ","
								+ oa.getOm().getBranchid()
								+ ",-1,0,0)";
					} else {

						sqlIB = "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
								+ realcont
								+ ")*1  where  branchid = "
								+ oa.getOm().getBranchid()
								+ " and  type = '" 
								+ og.getTid() + "'";

						sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
								+ "  values ( null, '"
								+ oa.getOm().getBranchid()
								+ "', '"
								+ oa.getOm().getId()
								+ "','"
								+ oa.getOm().getId()
								+ "','"
								+ TimeUtill.gettime()
								+ "','"
								+ og.getTid()
								+ "','"
								+ 0
								+ "','"
								+ realcont
								+ "',"
								+ operatortype
								+ ",(select realcount from mdinventorybranch where branchid = "
								+ oa.getOm().getBranchid()
								+ " and  type = '"
								+ og.getTid()
								+ "')*1,(select papercount from mdinventorybranch where branchid = "
								+ oa.getOm().getBranchid()
								+ " and  type = '"
								+ og.getTid()
								+ "')*1,"
								+ 1
								+ ","
								+ oa.getOm().getBranchid()
								+ ",-1,(select realcount from mdinventorybranch where branchid = "
								+ oa.getOm().getBranchid()
								+ " and  type = '"
								+ og.getTid()
								+ "')*1" 
								// + -Integer.valueOf(realsendnum)
								+ ",(select papercount from mdinventorybranch where branchid = "
								+ oa.getOm().getBranchid()   
								+ " and  type = '"       
								+ og.getTid() + "')*1" + realcont*1+")";
    
					}
					listsql.add(sqlIB);
					listsql.add(sqlIBM);
					
				}

			}
		}

		if (DBUtill.sava(listsql)) {
			try {
				response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void updateIOS(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		List<String> listsql = new ArrayList<String>();
		String name = request.getParameter("name");
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user,
				OrderMessage.billing, name);
		// logger.info(list.size());
		// 状态 ，型号 ，门店 
		Map<Integer,Set<Integer>> mapb = new HashMap<Integer,Set<Integer>>();
		Map<Integer,Set<Integer>> mapt = new HashMap<Integer,Set<Integer>>(); 
		//Map<Integer,Map<Set<String>,Set<String>>> mapT = new HashMap<Integer,Map<Set<String>,Set<String>>>();
		Set<Integer> set = new HashSet<Integer>(); 
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				OrderGoodsAll o = list.get(i);  
				List<OrderGoods> listog = o.getList();
				
				for (int j = 0; j < listog.size(); j++) { 
					OrderGoods og = listog.get(j);
					
					Set<Integer> setb = mapb.get(og.getStatues());
					Set<Integer> sett = mapt.get(og.getStatues());
					if(null == setb){ 
						setb = new HashSet<Integer>();
						mapb.put(og.getStatues(), setb);
					} 
					if(null == sett){ 
						sett = new HashSet<Integer>(); 
						mapt.put(og.getStatues(), sett);
					} 
					
					setb.add(o.getOm().getBranchid());
					sett.add(og.getTid());
					set.add(og.getStatues());
				} 
			}
		}

		 
		if (null != set) {
			Iterator<Integer> it = set.iterator();
			String time = request.getParameter("effectiveendtime");
			while (it.hasNext()) {
				int type = it.next();
				String oid = request.getParameter("oid" + type);
				Set<Integer> setb = mapb.get(type);
				Set<Integer> sett = mapt.get(type);
				     
				String sqlinvent = InventoryBranchManager.updateSNMessage(setb,sett,oid, time); 
				 
				String sql = OrderGoodsManager.updateIOS(name, type, oid, time);
				listsql.add(sqlinvent); 
				listsql.add(sql);
			}
		}
		try {
			if (listsql.size() == 0) {
				response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
			} else {
				if (DBUtill.sava(listsql)) {

					response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public void updateIOS(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		List<String> listsql = new ArrayList<String>();
		String name = request.getParameter("name");
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user,
				OrderMessage.billing, name);
		// logger.info(list.size());
		Set<Integer> set = new HashSet<Integer>();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				OrderGoodsAll o = list.get(i);
				List<OrderGoods> listog = o.getList();
				for (int j = 0; j < listog.size(); j++) {
					OrderGoods og = listog.get(j);
					set.add(og.getStatues());
				}
			}
		}

		if (null != set) {
			Iterator<Integer> it = set.iterator();
			String time = request.getParameter("effectiveendtime");
			while (it.hasNext()) {
				int type = it.next();
				String oid = request.getParameter("oid" + type);

				String sql = OrderGoodsManager.updateIOS(name, type, oid, time);
				listsql.add(sql);
			}
		}
		try {
			if (listsql.size() == 0) {
				response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
			} else {
				if (DBUtill.sava(listsql)) {

					response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 调用核心业务类接收消息、处理消息
		doGet(request, response);

		// 响应消息
	}

}
