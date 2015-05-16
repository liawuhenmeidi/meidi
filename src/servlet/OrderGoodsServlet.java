package servlet;

import inventory.InventoryBranch;
import inventory.InventoryBranchManager;
import inventory.InventoryBranchMessage;
import inventory.InventoryBranchMessageManager;
import inventory.InventoryManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Arrays;
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

		if ("add".equals(method) || "disableadd".equals(method)) {
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
		String method = request.getParameter("method");
		int statues = -1;
		// String oid = request.getParameter("oid");
		String branchid = request.getParameter("branchid");
		if (StringUtill.isNull(branchid)) {
			branchid = user.getBranchName();
		}
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

		if (StringUtill.isNull(rows)) {

		} else {
			String[] rowss = rows.split(",");

			for (int i = 0; i < rowss.length; i++) {
				String row = rowss[i];
				String type = request.getParameter("product" + row);

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
			Map<String, List<InventoryBranchMessage>> mapINM = InventoryBranchMessageManager
					.getmap(oa.getOm().getBranchid());
			// logger.info(mapINM);
			Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryBranchManager
					.getInventoryMap(user);

			Map<Integer, Map<Integer, InventoryBranchMessage>> map = new HashMap<Integer, Map<Integer, InventoryBranchMessage>>();
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
								+ og.getTid();
						listsql.add(sqlup);

						boolean flag = false;
						boolean upflag = false;
						if (og.getStatues() == 6 || og.getStatues() == 7
								|| og.getStatues() == 8 || og.getStatues() == 9) {
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
										.get(og.getStatues());
							} catch (Exception e) {
								inb = null;
							}
							if (null == inb) {
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
										+ og.getStatues() + ")";
 
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
										+ oa.getOm().getBranchid() + ",-1,0,0,1,"
											+ og.getStatues()
											+ ")";
							} else {
 
								sqlIB = "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
										+ realcont
										+ ")*1  where  branchid = "
										+ oa.getOm().getBranchid()
										+ " and  type = '" + og.getTid() + "' and typestatues = "
											+ og.getStatues();

								if (upflag) { 
									List<InventoryBranchMessage> listin = mapINM
											.get(og.getTid() + "");
									logger.info(listin);
									int InMid = 0;

									// int oldpapercount = 0;
									int papercount = 0;
									for (int j = 0; j < listin.size(); j++) {
										InventoryBranchMessage in = listin
												.get(j);
										Map<Integer, InventoryBranchMessage> maps = map
												.get(og.getTid());
										// logger.info(in.getInventoryid());
										// logger.info(oa.getOm().getId());
										if (in.getInventoryid() == oa.getOm()
												.getId()) {
											InMid = in.getId();
											logger.info(InMid);
											if (null == maps) {
												maps = new HashMap<Integer, InventoryBranchMessage>();
												map.put(og.getTid(), maps);
											}
											InventoryBranchMessage inv = maps
													.get(in.getInventoryid());
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
												maps.put(in.getInventoryid(),
														in);

											}
										}

										if (InMid != 0 && in.getId() > InMid) {
											InventoryBranchMessage inv = maps
													.get(in.getInventoryid());
											if (null == inv) {
												inv = in;
												in.setPapercount(papercount
														+ in.getAllotPapercount());
												in.setOldpapercount(papercount);

												maps.put(in.getInventoryid(),
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
											+ og.getTid()
											+ "')*1" + realcont * 1 + ",1," + og.getStatues() + ")";
								}  
							}   
							listsql.add(sqlIB);
							listsql.add(sqlIBM);

						}
 
					}
				}
   
				logger.info(map);
 
				List<String> sqlup =
				InventoryBranchMessageManager.update(map);
				  
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
							setb, sett, oid, time,type);  
 
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
