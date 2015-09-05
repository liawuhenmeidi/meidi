package servlet;

import inventory.InventoryBranch;
import inventory.InventoryBranchManager;
import inventory.InventoryBranchMessage;
import inventory.InventoryBranchMessageManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import makeInventory.MakeInventory;
import makeInventory.MakeInventoryManager;
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
		if ("add".equals(method) || "disableadd".equals(method)) {
			add(request, response);
 
		} else if ("updaterealsendnum".equals(method)) {
			updaterealsendnum(request, response);
		} else if ("updaterealInstoragenum".equals(method)) {
			updaterealInstoragenum(request, response);
		} else if ("updateIOS".equals(method)) {
			// logger.info(1); 
			updateIOS(request, response);
		} else if ("addrealInstoragenum".equals(method)) {
			addrealInstoragenum(request, response);
		}else if("add_send".equals(method)){ 
			add_send(request, response);  
		}else if("add_inventory".equals(method)){
			add_inventory(request, response);
		} 

	}

	/**
	 * 处理微信服务器发来的消息
	 */ 
    public void add_send(HttpServletRequest request, HttpServletResponse response){
    	User user = (User) request.getSession().getAttribute("user");
    	String time = request.getParameter("time");
    	String remark = request.getParameter("remark");
    
		if(StringUtill.isNull(time)){
			time = TimeUtill.getdateString(); 
		}  
		   
		String branchid = request.getParameter("branchid");
		if (StringUtill.isNull(branchid)) {
			branchid = user.getBranchName();
		}  
		 
	
		OrderGoodsAll oa = new OrderGoodsAll();
		OrderMessage om = new OrderMessage();
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		
		String rows = request.getParameter("rows"); 
		String id = OrderMessageManager.getMaxid() + "";
		om.setId(Integer.valueOf(id));
		// om.setOid(oid);
		om.setOpstatues(1);  
		om.setSubmitid(user.getId());
		om.setSubmittime(time);
		if (!StringUtill.isNull(branchid)) {
			om.setBranchid(BranchService.getNameMap().get(branchid).getId());
		} else { 
			om.setBranchid(Integer.valueOf(user.getBranch()));
		} 
		om.setRemark(remark);
		
		 
		String[] rowss = rows.split(",");

		for (int i = 0; i < rowss.length; i++) {
			String row = rowss[i].trim();  
			 //System.out.println("row"+row);
			String type = request.getParameter("product" + row);
            // System.out.println("type"+type);
			Product p = ProductService.gettypemap(user, branchid).get(type);
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
				// logger.info(Integer.valueOf(sta));
				if (Integer.valueOf(sta) == 3) {
					op.setOrdernum(Integer.valueOf(num));
				} else {
					op.setOrdernum(Integer.valueOf(num)
							+ Integer.valueOf(invenNum));
				}
  
				op.setRealnum(Integer.valueOf(num));
				if (Integer.valueOf(sta) == 6 ||Integer.valueOf(sta) == 7
						|| Integer.valueOf(sta) == 8
						|| Integer.valueOf(sta) == 9 
						||Integer.valueOf(sta) == 10) {
					op.setReturnrealsendnum(Integer.valueOf(num));
				} else if ( Integer.valueOf(sta) == 4 ) {
					op.setReturnrealsendnum(Integer.valueOf(num));
					op.setRealsendnum(Integer.valueOf(num));
				} else if(Integer.valueOf(sta) == 5 ){
					 
				}else {
					op.setRealsendnum(Integer.valueOf(num));
				}
			
				op.setStatues(Integer.valueOf(sta));
				op.setSubmitid(user.getId());
				op.setSubmittime(time);
				op.setCid(cid);  
				op.setTid(Integer.valueOf(itype));
				op.setMid(Integer.valueOf(id));
				op.setOpstatues(2);  
				op.setBillingstatues(2);  
				// op.setUuid(uuid); 
				list.add(op);
			} 

		}
		     
		oa.setOm(om);
		oa.setList(list); 
	 	 
		OrderGoodsAllManager.save(user, oa);
		    
		List<String> sql = OrderGoodsAllManager.updateSendcountALl(user, oa);
		 
		DBUtill.sava(sql);  
		logger.info(sql);  
		//System.out.println(time);
		 
		try { 
			response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
    } 
      
    public void add_inventory(HttpServletRequest request, HttpServletResponse response){
    	User user = (User) request.getSession().getAttribute("user");
    	String time = request.getParameter("time"); 
    	String intype = request.getParameter("type");
    	String uuid = request.getParameter("uuid");
    	String remark = request.getParameter("remark"); 
    	logger.info("remark"+remark);  
    	boolean flag = true ;   // 表示是否为修改  
    	if(StringUtill.isNull(uuid)){
    		uuid =  UUID.randomUUID().toString();
    		flag = false ; 
    	}  
    	   
    	int statues = -1;
		if(StringUtill.isNull(time)){  
			time = TimeUtill.getdateString(); 
		}   
		    
		if("model".equals(intype)){
			statues =MakeInventory.model;
		}else if("out".equals(intype)){ 
			statues =MakeInventory.out; 
		}else if("in".equals(intype)){
			statues =MakeInventory.in; 
		} 
		
		
		String branchid = request.getParameter("branchid");
		if (StringUtill.isNull(branchid)) {
			branchid = user.getBranch();
		}else { 
			branchid = BranchService.getNameMap().get(branchid).getId()+"";
		}   
		  
		List<MakeInventory> list = new ArrayList<MakeInventory>();
		    
		String rows = request.getParameter("rows");  
		String[] rowss = rows.split(","); 
 logger.info(StringUtill.GetJson(rowss)); 
		for (int i = 0; i < rowss.length; i++) {
			String row = rowss[i].trim();  
			 //System.out.println("row"+row); 
			String type = request.getParameter("product" + row);
			if(!StringUtill.isNull(type)){
            // System.out.println("type"+type); 
			Product p = ProductService.gettypemapBid(user, branchid).get(type);
			int cid = p.getCategoryID();  
			int itype = p.getId();  
			String num = request.getParameter("num" + row);
			String id = request.getParameter("id"+row);
			// logger.info(invenNum);
			if (StringUtill.isNull(num)) { 
				num = 0 + "";  
			}
			MakeInventory mi = new MakeInventory();
			mi.setCid(cid);
			mi.setTid(itype); 
			mi.setSubmitid(user.getId());
			mi.setSubmittime(TimeUtill.gettime());
			mi.setBid(Integer.valueOf(branchid));
			mi.setStatues(0);  
			mi.setTypestatues(statues); 
			mi.setNum(Integer.valueOf(num)); 
			mi.setUuid(uuid);
			if(!StringUtill.isNull(id)){
				mi.setId(Integer.valueOf(id));
			} 
			mi.setRemark(remark); 
			list.add(mi);
			}
		}    
       logger.info(list);
       MakeInventoryManager.save(user, list,flag,uuid);    
		//System.out.println(time); 
		    
		try {    
			response.sendRedirect("jieguo.jsp?type=updated&mark=" + 1+"&type1="+intype);
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
    }
    
	public void add(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String method = request.getParameter("method");
		String time = request.getParameter("time");
		if(StringUtill.isNull(time)){
			time = TimeUtill.getdateString(); 
		}
		int statues = -1;  
		// String oid = request.getParameter("oid");
		String branchid = request.getParameter("branchid");
		if (StringUtill.isNull(branchid)) {
			branchid = user.getBranchName();
		}
		String id = request.getParameter("id");
		String remark = request.getParameter("remark");
		//logger.info(remark);  
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
			om.setSubmittime(time);
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

		if (StringUtill.isNull(rows)) {

		} else {
			String[] rowss = rows.split(",");

			for (int i = 0; i < rowss.length; i++) {
				String row = rowss[i].trim();  
				 //System.out.println("row"+row);
				String type = request.getParameter("product" + row);
                // System.out.println("type"+type);
				Product p = ProductService.gettypemap(user, branchid).get(type);
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
					// logger.info(Integer.valueOf(sta));
					if (Integer.valueOf(sta) == 3) {
						op.setOrdernum(Integer.valueOf(num));
					} else {
						if(Integer.valueOf(invenNum) <=0){
							op.setOrdernum(Integer.valueOf(num));
						}else { 
							op.setOrdernum(Integer.valueOf(num)
									+ Integer.valueOf(invenNum));
						}
						
					}
 
					op.setRealnum(Integer.valueOf(num));
					op.setStatues(Integer.valueOf(sta));
					op.setSubmitid(user.getId());
					op.setSubmittime(time);
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
		// logger.info("one");
		if (OrderGoodsAllManager.save(user, oa)) {
			try {
				// logger.info(statues);
				if ("disableadd".equals(method)) {
					DBUtill.sava(InventoryBranchMessageManager.update(list));
				}
				if (0 == statues) {
					response.sendRedirect("../jieguo.jsp?type=ordergoodsadd&mark=" + 1);
				} else {
					// logger.info(opstatues);
					if (!StringUtill.isNull(opstatues)
							&& Integer.valueOf(opstatues) == 0) {
						response.sendRedirect("../admin/ordergoods/ordergoodsupdate.jsp?id="
								+ id
								+ "&type="
								+ OrderMessage.unexamine
								+ "&statues=" + OrderMessage.unexamine);
					} else {
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
		Set<String> setup = new HashSet<String>();
		User user = (User) request.getSession().getAttribute("user");
		List<String> listsql = new ArrayList<String>();
		String id = request.getParameter("id");
		String statues = request.getParameter("statues");
		String type = request.getParameter("type");
		String[] ogids = request.getParameterValues("ogid");
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
		if (null != ogids) {
			List<String> listogids = Arrays.asList(ogids);
			// logger.info(listogids);
			
			Map<String, Map<String,List<InventoryBranchMessage>>> mapINM = InventoryBranchMessageManager
					.getmap(oa.getOm().getBranchid(),oa.getOm().getId()); 
			
			 
			// logger.info(mapINM);
			Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryBranchManager
					.getInventoryMap(user);

			Map<String,InventoryBranchMessage> map = new HashMap<String, InventoryBranchMessage>();
			// for (int i = 0; i < ogids.length; i++) {
			if (null != list) {

				for (int i = 0; i < list.size(); i++) {
					OrderGoods og = list.get(i);
					int ogid = og.getId();
					if (listogids.contains("" + ogid)) {
						// int ogid =
						// Integer.valueOf(Integer.valueOf(ogids[i]));
						int operatortype = 18;
						String realsendnum = request.getParameter("realsendnum"
								+ ogid);
						String returnrealsendnum = request
								.getParameter("returnrealsendnum" + ogid);

						int realcont = 0;
						if (StringUtill.isNull(realsendnum)) {
							realsendnum = "0";
						}
						if (StringUtill.isNull(returnrealsendnum)) {
							returnrealsendnum = "0";
						}    
						String sql = OrderGoodsManager.updaterealsendnum(user,
								ogid, realsendnum, returnrealsendnum);

						listsql.add(sql);  

						String sqlup = " update mdinventorybranchmessage set isoverstatues = 0 where inventoryid = "
								+ oa.getOm().getId()
								+ " and  type = "
								+ og.getTid()+" and typestatues = "+og.getrealStatues();
						listsql.add(sqlup);
 
						boolean flag = false;
						boolean upflag = false;
						if (og.getStatues() == 6 || og.getStatues() == 7
								|| og.getStatues() == 8 || og.getStatues() == 9 || og.getStatues() == 10) {
 
							flag = true; 
							operatortype = 16;
							realcont = -Integer.valueOf(returnrealsendnum);
						} else if (og.getStatues() == 4) {
							flag = true;   
							// upflag = true ;
							operatortype = 17;
							/*
							 * realcont = (Integer.valueOf(realsendnum) -
							 * og.getRealnum() - Integer
							 * .valueOf(returnrealsendnum));
							 */
							realcont = (Integer.valueOf(realsendnum) - Integer
									.valueOf(returnrealsendnum));

						} else if ((og.getRealnum() != Integer
								.valueOf(realsendnum)) && og.getStatues() != 5) {
							flag = true;
							upflag = true;
							// realcont = Integer.valueOf(returnrealsendnum) ;
							realcont = (Integer.valueOf(realsendnum) - og
									.getRealnum());
						}   
						logger.info(upflag);  
						if (flag) {
							String sqlIB = "";
							String sqlIBM = "";
							InventoryBranch inb = null;
							try {
								inb = mapin.get(oa.getOm().getBranchid())
										.get(og.getTid() + "")
										.get(og.getrealStatues());
							} catch (Exception e) {
								inb = null;
							} 
							if (null == inb 
									&& !setup.contains(oa.getOm()
											.getBranchid()
											+ "_"
											+ og.getTid()+"_"+og.getrealStatues())) {

								setup.add(oa.getOm().getBranchid() + "_"
										+ og.getTid()+"_"+og.getrealStatues());
								
								sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
										+ "  values ( null,"
										+ og.getCid()
										+ ", '"
										+ og.getTid()
										+ "', '"
										+ 0
										+ "', '"
										+ realcont
										+ "',"
										+ oa.getOm().getBranchid()
										+ ","
										+ og.getrealStatues() + ")";

								sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
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
										+ ",-1,0,0,0,"
										+ og.getrealStatues()
										+ ")";
							} else {

								sqlIB = "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
										+ realcont
										+ ")*1  where  branchid = "
										+ oa.getOm().getBranchid()
										+ " and  type = '"
										+ og.getTid()
										+ "' and typestatues = "
										+ og.getrealStatues();

								if (upflag) {
									List<InventoryBranchMessage> listin = mapINM
											.get(og.getTid() + "").get(og.getrealStatues()+"");
									logger.info(listin);
									int InMid = 0;  
 
									// int oldpapercount = 0;
									int papercount = 0;
									for (int j = 0; j < listin.size(); j++) {
										InventoryBranchMessage in = listin
												.get(j);
										String key = og.getTid()+"_"+in.getTypeStatues();
										InventoryBranchMessage inv = map
												.get(key); 
										// logger.info(in.getInventoryid());
										// logger.info(oa.getOm().getId());
										
											InMid = in.getId(); 
											logger.info(InMid); 
											
											if (null == inv) {
												inv = in;
												// oldpapercount =
												// in.getPapercount()+realcont;
												papercount = in.getPapercount()
														+ realcont;
												in.setAllotPapercount(Integer
														.valueOf(realsendnum));
												in.setPapercount(in
														.getPapercount()
														+ realcont);
												in.setIsOverStatues(0);
												map.put(key,
														in); 

											}
										

										if (InMid != 0 && in.getId() > InMid) {
											 inv =map
													.get(key); 
											if (null == inv) {
												inv = in;
												in.setPapercount(papercount
														+ in.getAllotPapercount());
												in.setOldpapercount(papercount);
 
												map.put(key, 
														in);

											}
										}
									}
								} else {

									sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
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
											+ "' and typestatues = "
											+ og.getrealStatues()
											+ " )*1,(select papercount from mdinventorybranch where branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()
											+ "' and typestatues = "
											+ og.getrealStatues()
											+ " )*1,"
											+ 1
											+ ","
											+ oa.getOm().getBranchid()
											+ ",-1,(select realcount from mdinventorybranch where branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()
											+ "' and typestatues = "
											+ og.getrealStatues()
											+ " )*1"
											// + -Integer.valueOf(realsendnum)
											+ ",(select papercount from mdinventorybranch where branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()
											+ "' and typestatues = "
											+ og.getrealStatues()
											+ " )*1"
											+ realcont
											* 1 
											+ ",0,"
											+ og.getrealStatues() + ")";
								}
							}
							listsql.add(sqlIB);
							listsql.add(sqlIBM);

						}

					}
				}

				logger.info(map);

				List<String> sqlup = InventoryBranchMessageManager.update(map);

				listsql.addAll(sqlup); 

			}

		}

		if (DBUtill.sava(listsql)) {
			try {
				response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
	} 
 
	public void updaterealInstoragenum(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		List<String> listsql = new ArrayList<String>();
		String id = request.getParameter("id");
		String statues = request.getParameter("statues");
		String oid = request.getParameter("oid");
		String[] ogids = request.getParameterValues("ogid");
		OrderGoodsAll oa = null;
		List<OrderGoods> list = null;
		if (!StringUtill.isNull(id)) {
			oa = OrderGoodsAllManager.getOrderGoodsAllBySendid(user, id,
					statues);
  
			list = oa.getList();
		}
		if (null != ogids) {
			List<String> listogids = Arrays.asList(ogids);

			// for (int i = 0; i < ogids.length; i++) {
			if (null != list) {

				for (int i = 0; i < list.size(); i++) {
					OrderGoods og = list.get(i);
					int ogid = og.getId();
					if (listogids.contains("" + ogid)) {
						// int ogid =
						// Integer.valueOf(Integer.valueOf(ogids[i]));
						int operatortype = 19;
						String realsendnum = request.getParameter("realsendnum"
								+ ogid);

						int realcont = 0;
						if (StringUtill.isNull(realsendnum)) {
							realsendnum = "0";
						}

						String sql = OrderGoodsManager.updateInstoragenum(user,
								ogid, realsendnum);

						listsql.add(sql);

						boolean flag = false;

						if (og.getStatues() == 7 || og.getStatues() == 8
								|| og.getStatues() == 9) {
							flag = true;
							operatortype = 19;
							realcont = -Integer.valueOf(realsendnum);
						} else if (og.getStatues() == 1 || og.getStatues() == 2
								|| og.getStatues() == 3) {
							flag = true;
							operatortype = 21;
							// realcont = Integer.valueOf(returnrealsendnum) ;
							realcont = Integer.valueOf(realsendnum);
						}

						if (flag) {
							String sqlIB = "";
							String sqlIBM = "";

							sqlIB = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1 - "
									+ realcont
									+ ")*1  , realcount = ((mdinventorybranch.realcount)*1 + "
									+ realcont
									+ ")*1   where  branchid = "
									+ oa.getOm().getBranchid()
									+ " and  type = '"
									+ og.getTid()
									+ "' and typestatues = " + og.getrealStatues();

							sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
									+ "  values ( null, '"
									+ oa.getOm().getBranchid() 
									+ "', '"+oid+"','"
									+ oid
									+ "','"
									+ TimeUtill.getdateString()
									+ "','"
									+ og.getTid()
									+ "','"
									+ +realcont
									+ "','"
									+ -realcont
									+ "',"
									+ 13
									+ ",(select realcount from mdinventorybranch where branchid = "
									+ oa.getOm().getBranchid()
									+ " and  type = '"
									+ og.getTid()
									+ "' and typestatues = "
									+ og.getrealStatues()
									+ ")*1,(select papercount from mdinventorybranch where branchid = "
									+ oa.getOm().getBranchid()
									+ " and  type = '"
									+ og.getTid()
									+ "'  and typestatues = "
									+ og.getrealStatues()
									+ ")*1,"
									+ 1
									+ ","
									+ oa.getOm().getBranchid()
									+ ",22,(select realcount from mdinventorybranch where branchid = "
									+ og.getTid()
									+ " and  type = '"
									+ og.getTid()
									+ "'  and typestatues = "
									+ og.getrealStatues()
									+ ")*1"
									+ -Integer.valueOf(realcont)
									+ ",(select papercount from mdinventorybranch where branchid = "
									+ oa.getOm().getBranchid()
									+ " and  type = '"
									+ og.getTid()  
									+ "'  and typestatues = "
									+ og.getrealStatues()
									+ ")*1"
									+ +Integer.valueOf(realcont)
									+ ",0,"
									+ realcont
									+ ")";

							
							listsql.add(sqlIB);
							listsql.add(sqlIBM);

						}

					}
				}

			}

		}

		if (DBUtill.sava(listsql)) {
			try {
				response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void addrealInstoragenum(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String rows = request.getParameter("rows");
		String oid = request.getParameter("oid");
		String branchid = request.getParameter("branchid");
		Set<String> setup = new HashSet<String>();
		OrderGoodsAll oa = new OrderGoodsAll();
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		
		OrderMessage om = new OrderMessage();

		int id = OrderMessageManager.getMaxid();
		om.setId(Integer.valueOf(id));
		// om.setOid(oid);
		om.setSubmitid(user.getId());
		om.setSubmittime(TimeUtill.gettime());
		if (!StringUtill.isNull(branchid)) {
			om.setBranchid(BranchService.getNameMap().get(branchid).getId());

		} else {
			om.setBranchid(Integer.valueOf(user.getBranch()));

		}
		om.setOpstatues(2);
		
		
		
		Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryBranchManager
				.getInventoryMap();
		List<String> listsql = new ArrayList<String>();
		String[] rowss = rows.split(",");

		for (int i = 0; i < rowss.length; i++) {
			String row = rowss[i];
			String type = request.getParameter("product" + row);

			Product p = ProductService.gettypemap(user, branchid).get(type);

			if (StringUtill.isNull(branchid)) {
				branchid = user.getBranchName();
			} else {
				branchid = BranchService.getNameMap().get(branchid).getId()
						+ "";
			}

			int cid = p.getCategoryID();
			int itype = p.getId();
			String sta = request.getParameter("statues" + row);

			
			
			

			if (!StringUtill.isNull(type) && !StringUtill.isNull(sta)) {
				OrderGoods op = new OrderGoods();
				
				int operatortype = 19;
				String realsendnum = request.getParameter("realsendnum" + row);
				int realcont = 0;
				if (StringUtill.isNull(realsendnum)) {
					realsendnum = "0";
				}

				boolean flag = false;

				if (Integer.valueOf(sta) == 7 
						|| Integer.valueOf(sta) == 9) {
					flag = true; 
					operatortype = 19; 
					realcont = -Integer.valueOf(realsendnum);
				} else if (Integer.valueOf(sta) == 1
						|| Integer.valueOf(sta) == 3) {
					flag = true;
					operatortype = 21;
					// realcont = Integer.valueOf(returnrealsendnum) ;
					realcont = Integer.valueOf(realsendnum);
				}
                    
				
				op.setRealnum(0);
				op.setStatues(Integer.valueOf(sta));
				op.setSubmitid(user.getId());
				op.setSubmittime(TimeUtill.getdateString());
				op.setCid(cid); 
				op.setTid(Integer.valueOf(itype)); 
				op.setMid(Integer.valueOf(id));
				op.setStatues(op.getRealstatues());  
				op.setBillingstatues(OrderMessage.all);
				op.setInstoragenum(realcont);
				op.setOpstatues(OrderMessage.billing);
				// op.setUuid(uuid);
				list.add(op);

				if (flag) {
					String sqlIB = "";
					String sqlIBM = ""; 
					InventoryBranch in = null;
					try { 
						logger.info(mapin);
						logger.info(mapin.get(Integer.valueOf(branchid)));
						logger.info(mapin.get(Integer.valueOf(branchid))
								.get(itype + ""));
						logger.info(mapin.get(Integer.valueOf(branchid))
								.get(itype + "").get(Integer.valueOf(sta)));
						in = mapin.get(Integer.valueOf(branchid))
								.get(itype + "").get(Integer.valueOf(sta));
					} catch (Exception e) {
						in = null;
					}
 
					if (null == in  
							&& !setup.contains(branchid
									+ "_"
									+ itype)) { 

						setup.add(branchid + "_"
								+ itype);
						
						sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
								+ "  values ( null,"
								+ cid
								+ ", '"
								+ itype
								+ "', '"
								+ +realcont
								+ "', '"
								+ -realcont 
								+ "'," + branchid + "," + sta + ")";

						sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
								+ "  values ( null, '"
								+ branchid
								+ "', '','"
								+ oid 
								+ "','"
								+ TimeUtill.getdateString()
								+ "','"
								+ itype
								+ "',"
								+ +realcont
								+ ",'"
								+ -realcont
								+ "',"
								+ 13
								+ ","
								+ +realcont
								+ ","
								+ -realcont
								+ ","
								+ 1 + "," + branchid + ",22,0,0,0," + sta + ")";
					} else {
						sqlIB = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1 - "
								+ realcont
								+ ")*1  , realcount = ((mdinventorybranch.realcount)*1 + "
								+ realcont
								+ ")*1   where  branchid = "
								+ branchid
								+ " and  type = '"
								+ itype
								+ "' and typestatues = " + sta;

						sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
								+ "  values ( null, '"
								+ branchid
								+ "', '','"
								+ oid
								+ "','"
								+ TimeUtill.getdateString()
								+ "','"
								+ itype
								+ "','"
								+ +realcont
								+ "','"
								+ -realcont
								+ "',"
								+ 13
								+ ",(select realcount from mdinventorybranch where branchid = "
								+ branchid
								+ " and  type = '"
								+ itype
								+ "' and typestatues = "
								+ sta
								+ ")*1,(select papercount from mdinventorybranch where branchid = "
								+ branchid
								+ " and  type = '"
								+ itype
								+ "'  and typestatues = "
								+ sta
								+ ")*1,"
								+ 1
								+ ","
								+ branchid
								+ ",22,(select realcount from mdinventorybranch where branchid = "
								+ itype
								+ " and  type = '"
								+ itype
								+ "'  and typestatues = "
								+ sta
								+ ")*1"
								+ -Integer.valueOf(realcont)
								+ ",(select papercount from mdinventorybranch where branchid = "
								+ branchid
								+ " and  type = '"
								+ itype
								+ "'  and typestatues = "
								+ sta
								+ ")*1"
								+ +Integer.valueOf(realcont)
								+ ",0,"
								+ realcont
								+ ")";
 
					}
					listsql.add(sqlIB);
					listsql.add(sqlIBM);

				}
			} 
		} 
         
		oa.setOm(om);
		oa.setList(list);
		// logger.info("one"); 
		OrderGoodsAllManager.save(user, oa);
		if (DBUtill.sava(listsql) && OrderGoodsAllManager.save(user, oa)) {
			try { 
				response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
			} catch (IOException e) { // TODO Auto-generated catch block
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
		Map<Integer, Set<Integer>> mapb = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Set<Integer>> mapt = new HashMap<Integer, Set<Integer>>();
		// Map<Integer,Map<Set<String>,Set<String>>> mapT = new
		// HashMap<Integer,Map<Set<String>,Set<String>>>();
		Set<Integer> set = new HashSet<Integer>();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				OrderGoodsAll o = list.get(i);
				List<OrderGoods> listog = o.getList();

				for (int j = 0; j < listog.size(); j++) {
					OrderGoods og = listog.get(j);

					Set<Integer> setb = mapb.get(og.getStatues());
					Set<Integer> sett = mapt.get(og.getStatues());
					if (null == setb) {
						setb = new HashSet<Integer>();
						mapb.put(og.getStatues(), setb);
					}
					if (null == sett) {
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
				if (!StringUtill.isNull(oid)) {
					Set<Integer> setb = mapb.get(type);
					Set<Integer> sett = mapt.get(type);

					String sqlinvent = InventoryBranchManager.updateSNMessage(
							setb, sett, oid, time, type);

					String sql = OrderGoodsManager.updateIOS(name, type, oid,
							time);
					listsql.add(sqlinvent);
					listsql.add(sql);
				}

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

	/*
	 * public void updateIOS(HttpServletRequest request, HttpServletResponse
	 * response) { User user = (User) request.getSession().getAttribute("user");
	 * List<String> listsql = new ArrayList<String>(); String name =
	 * request.getParameter("name"); List<OrderGoodsAll> list =
	 * OrderGoodsAllManager.getlist(user, OrderMessage.billing, name); //
	 * logger.info(list.size()); Set<Integer> set = new HashSet<Integer>(); if
	 * (null != list) { for (int i = 0; i < list.size(); i++) { OrderGoodsAll o
	 * = list.get(i); List<OrderGoods> listog = o.getList(); for (int j = 0; j <
	 * listog.size(); j++) { OrderGoods og = listog.get(j);
	 * set.add(og.getStatues()); } } }
	 * 
	 * if (null != set) { Iterator<Integer> it = set.iterator(); String time =
	 * request.getParameter("effectiveendtime"); while (it.hasNext()) { int type
	 * = it.next(); String oid = request.getParameter("oid" + type);
	 * 
	 * String sql = OrderGoodsManager.updateIOS(name, type, oid, time);
	 * listsql.add(sql); } } try { if (listsql.size() == 0) {
	 * response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1); } else {
	 * if (DBUtill.sava(listsql)) {
	 * 
	 * response.sendRedirect("../jieguo.jsp?type=updated&mark=" + 1);
	 * 
	 * } } } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

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
