package servlet;

import gift.Gift;
import group.Group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import aftersale.AfterSale;
import aftersale.AfterSaleManager;
import aftersale.AfterSaleProduct;
import aftersale.AfterSaleProductManager;
import aftersale.AftersaleAllManager;

import category.Category;
import category.CategoryManager;
import category.CategoryService;

import order.Order;
import order.OrderManager;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import orderproduct.OrderProductService;
import product.Product;
import product.ProductService;

import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.RemarkUtill;
import utill.StringUtill;
import utill.TimeUtill;

/** 
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	protected static Log logger = LogFactory.getLog(OrderServlet.class);

	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Object object = new Object();

		String method = request.getParameter("method");
		// logger.info(method);
		synchronized (object) {
			if ("println".equals(method)) {
				savePrintln(request, response);
			} else if ("shifang".equals(method)) {
				savashifang(request, response);
			} else if ("huanhuo".equals(method)) {
				HuanHuo(request, response);
			} else if ("savehuanhuo".equals(method)) {
				saveHuanhuo(request, response);
			} else if ("saveTuihuo".equals(method)) {
				saveTuihuo(request, response);
			} else if ("cancelTuihuo".equals(method)) {
				cancelTuihuo(request, response);
			} else if ("aftersale".equals(method)) {
				saveaftersale(request, response);
			} else if ("queryaftersale".equals(method)) {
				queryaftersale(request, response);
			} else if ("updateaftersale".equals(method)) {
				updateaftersale(request, response);
			} else {
				save(request, response);
			}
		}
	}

	private void savashifang(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String id = request.getParameter("oid");
		String opstatues = request.getParameter("opstatues");
		OrderPrintln or = new OrderPrintln();
		or.setOrderid(Integer.valueOf(id));
		if (Integer.valueOf(opstatues) == OrderPrintln.releasedispatch) {
			or.setMessage("文员申请退货");
		} else if (Integer.valueOf(opstatues) == OrderPrintln.releasemodfy) {
			or.setMessage("文员申请释放");
		} else { // OrderPrintln.releasemodfy
			or.setMessage("释放");
		}
		or.setStatues(OrderPrintln.comit);
		or.setType(Integer.valueOf(opstatues));
		or.setUid(user.getId());
		or.setGroupid(user.getUsertype());
		OrderPrintlnManager.save(or);
	}

	private void savePrintln(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String mm = request.getParameter("mm");
		User user = (User) request.getSession().getAttribute("user");

		String message = request.getParameter("message");
		if ("" == id || null == id) {
			id = "0";
		}
		// logger.info(message);
		OrderPrintln or = new OrderPrintln();
		or.setOrderid(Integer.valueOf(id));
		or.setMessage(message);
		or.setStatues(OrderPrintln.comit);
		or.setUid(user.getId());
		or.setGroupid(user.getUsertype());
		// int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
		// or.setpGroupId(pgroup);
		if ("tuihuo".equals(mm)) {
			or.setType(OrderPrintln.returns);
		} else if ("huanhuo".equals(mm)) {
			or.setType(OrderPrintln.huanhuo);
		}
		OrderPrintlnManager.save(or);

		try {
			response.sendRedirect("serch_list.jsp");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 处理请求，并执行resetToken方法，将session中的token去除
			User user = (User) request.getSession().getAttribute("user");
			boolean bflag = true;
			boolean bbflag = true;
			int devedity = -1;

			if (UserManager.checkPermissions(user, Group.sale)) {
				HashMap<Integer, Category> categorymap = CategoryManager
						.getCategoryMap();

				String submitTime = TimeUtill.gettime();

				String pid = TimeUtill.getdatesimple();

				Order order = new Order();

				String id = request.getParameter("orderid");

				String saledate = request.getParameter("saledate");
				String andate = request.getParameter("andate"); // 安装日期

				String POS = request.getParameter("POS");
				if (StringUtill.isNull(POS)) {
					POS = "空";
				}
				String sailId = request.getParameter("sailId");
				if (StringUtill.isNull(sailId)) {
					sailId = "空";
				}
				String check = request.getParameter("check");
				if (StringUtill.isNull(check)) {
					check = "空";
				}
				String phoneRemark = request.getParameter("phoneRemark");
				String posRemark = request.getParameter("posremark");
				String chekedRemark = request.getParameter("chekedremark");
				String sailidRemark = request.getParameter("sailidremark");
				List<OrderProduct> listp = new ArrayList<OrderProduct>();

				List<Gift> listg = new ArrayList<Gift>();

				if (StringUtill.isNull(id)) {
					id = "0";
					String radio = request.getParameter("Statues");

					if ("1".equals(radio)) {

						OrderProduct o = new OrderProduct();

						String categoryId = request
								.getParameter("dingmaordercategory");

						String saleType = request.getParameter("dingmatype");

						String price = request.getParameter("dingmaprize");

						Product p = ProductService.gettypemap(user).get(saleType);
 
						if (p != null) {
							saleType = p.getId() + "";
						} else {
							request.getSession().setAttribute("message",
									"对不起，您提交的型号被修改，请重新提交");
							response.sendRedirect("../jieguo.jsp?type=order");
						}

						String dingmaproductNum = request
								.getParameter("dingmaproductNum");

						String categoryName = categorymap.get(
								Integer.valueOf(categoryId)).getName();
						o.setCategoryId(Integer.valueOf(categoryId));
						o.setSaleType(saleType);
						o.setCount(Integer.valueOf(dingmaproductNum));
						o.setStatues(1);
						if (!StringUtill.isNull(price)) {
							o.setPrice(Double.valueOf(price));
						}
						o.setCategoryName(categoryName);
						listp.add(o);
					}

					String[] producs = request.getParameterValues("product");

					// 送货状态
					for (int i = 0; i < producs.length; i++) {
						OrderProduct o = new OrderProduct();
						String categoryId = request
								.getParameter("ordercategory" + producs[i]);
						String categoryName = categorymap.get(
								Integer.valueOf(categoryId)).getName();
						// String categoryname =
						// CategoryManager.getCategoryMap()
						String sendType = request.getParameter("ordertype"
								+ producs[i]);

						String price = request.getParameter("prize"
								+ producs[i]);

						Product p = ProductService.gettypemap(user).get(sendType);

						if (p != null) {
							sendType = p.getId() + "";
						} else {
							request.getSession().setAttribute("message",
									"对不起，您提交的型号被修改，请重新提交");
							response.sendRedirect("../jieguo.jsp?type=order");
						}

						String productNum = request
								.getParameter("orderproductNum" + producs[i]);
						// productsta
						String salestatues = request.getParameter("productsta"
								+ producs[i]);
						o.setCategoryId(Integer.valueOf(categoryId));
						o.setCount(Integer.valueOf(productNum));
						o.setSendType(sendType);
						o.setStatues(0);
						if (!StringUtill.isNull(price)) {
							o.setPrice(Double.valueOf(price));
						}
						o.setCategoryName(categoryName);
						o.setSalestatues(Integer.valueOf(salestatues));

						// 2 只安装门店提货 3 只安装顾客已提
						if (Integer.valueOf(salestatues) != 0) {
							if (Integer.valueOf(salestatues) == 2) {
								devedity = 9;
							} else if (Integer.valueOf(salestatues) == 3) {
								devedity = 10;

							} else if (Integer.valueOf(salestatues) == 1) {
								devedity = 0;
							}
							bflag = false;
						}
						if (Integer.valueOf(salestatues) == 0) {
							devedity = 8;
							andate = saledate;
							bbflag = false;
						}
						listp.add(o);

					}

					String[] gifts = request.getParameterValues("gift");

					if (gifts != null) {
						logger.info(gifts.length);
						for (int i = 0; i < gifts.length; i++) {
							if ("0".equals(gifts[i])) {
								continue;
							}

							Gift g = new Gift();
							String giftT = request.getParameter("giftT"
									+ gifts[i]);

							String count = request.getParameter("giftCount"
									+ gifts[i]);

							String giftStatues = request.getParameter("giftsta"
									+ gifts[i]);

							g.setCount(Integer.valueOf(count));
							g.setStatues(Integer.valueOf(giftStatues));
							g.setName(giftT);
							listg.add(g);
						}
					}
				}

				String username = request.getParameter("username");

				String diqu = request.getParameter("diqu");

				String phone1 = request.getParameter("phone1");

				String phone2 = request.getParameter("phone2");

				String locations = request.getParameter("locations");

				String remark = request.getParameter("remark");

				order.setId(Integer.valueOf(id));
				order.setSaleTime(saledate);

				order.setOdate(andate);
				order.setPos(POS);
				order.setSaleID(user.getId());
				order.setBranch(Integer.valueOf(user.getBranch()));
				order.setSailId(sailId);
				order.setCheck(check);
				order.setUsername(username);
				order.setPhone1(phone1);
				order.setPhone2(phone2);
				order.setLocate(diqu);
				order.setLocateDetail(locations);
				order.setRemark(remark);
				order.setOrderproduct(listp);
				order.setOrdergift(listg);
				order.setSubmitTime(submitTime);
				order.setPrintlnid(pid);
				order.setPhoneRemark(Integer.valueOf(phoneRemark));
				order.setPosremark(Integer.valueOf(posRemark));
				order.setReckedremark(Integer.valueOf(chekedRemark));
				order.setSailidrecked(Integer.valueOf(sailidRemark));

				Order oldorder = OrderManager.getOrderID(user, order.getId());

				if (oldorder == null) {
					order.setDeliveryStatues(devedity);
					order.setOderStatus(devedity + "");
				}

				if (bflag && bbflag && StringUtill.isNull(id)) {
					request.getSession().setAttribute("message",
							"已自提和需配送不能一起提交");
					response.sendRedirect("../jieguo.jsp?type=order");
					return;
				}

				int flag = OrderManager.save(user, order);

				if (flag != -1) {
					request.getSession().setAttribute("message", "您的订单提交成功");
				} else {
					request.getSession().setAttribute("message",
							"对不起，您的订单提交失败，请您重新提交");
				}
				response.sendRedirect("../jieguo.jsp?type=order&oid=" + flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void saveHuanhuo(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			// 处理请求，并执行resetToken方法，将session中的token去除
			User user = (User) request.getSession().getAttribute("user");

			if (UserManager.checkPermissions(user, Group.sale)) {
				String oid = request.getParameter("orderid");

				Order oldOrder = OrderManager.getOrderID(user,
						Integer.valueOf(oid));
				Map<Integer, List<OrderProduct>> OrPMap = OrderProductService
						.getStaticOrderStatuesM();
				List<OrderProduct> listp = new ArrayList<OrderProduct>();
				List<OrderProduct> list = OrPMap.get(Integer.valueOf(oid));

				String[] producs = request.getParameterValues("product");
				// 送货状态
				for (int i = 0; i < producs.length; i++) {
					int opid = Integer.valueOf(producs[i]);
					for (int j = 0; j < list.size(); j++) {
						OrderProduct or = list.get(j);
						if (or.getStatues() == 0 && opid == or.getId()) {
							int count = Integer.valueOf(request
									.getParameter("orderproductNum" + opid));
							or.setCount(count);
							or.setSalestatues(1);
							listp.add(or);
						}
					}
				}

				Order order = new Order();

				String andate = request.getParameter("andate"); // 安装日期

				String username = request.getParameter("username");

				String diqu = request.getParameter("diqu");

				String phone1 = request.getParameter("phone1");

				String phone2 = request.getParameter("phone2");

				String locations = request.getParameter("locations");

				String remark = request.getParameter("remark");

				order.setId(0);

				order.setSaleTime(oldOrder.getSaleTime());

				order.setOdate(andate);

				order.setPos(oldOrder.getPos());

				order.setSaleID(oldOrder.getSaleID());
				order.setBranch(oldOrder.getBranch());
				order.setSailId(oldOrder.getSailId());
				order.setCheck(oldOrder.getCheck());

				order.setUsername(username);
				order.setPhone1(phone1);
				order.setPhone2(phone2);
				order.setLocate(diqu);
				order.setLocateDetail(locations);

				order.setRemark(remark + ".换货单，务必拉回残机。");
				order.setOderStatus(20 + "");
				order.setOrderproduct(listp);

				order.setSubmitTime(oldOrder.getSubmitTime());
				order.setPrintlnid(oldOrder.getPrintlnid());
				order.setDeliveryStatues(0);

				order.setImagerUrl(oldOrder.getId() + "");

				int flag = OrderManager.save(user, order);

				if (flag != -1) {
					request.getSession().setAttribute("message", "您的订单提交成功");
				} else {
					request.getSession().setAttribute("message",
							"对不起，您的订单提交失败，请您重新提交");
				}
				logger.info(flag);
				response.sendRedirect("../jieguo.jsp?type=order");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void saveTuihuo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 处理请求，并执行resetToken方法，将session中的token去除
			User user = (User) request.getSession().getAttribute("user");
			String oid = request.getParameter("orderid");
			String realpos = request.getParameter("realpos");
			String saleTime = request.getParameter("saledate");
			Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));

			order.setPos(realpos);
			order.setOderStatus(30 + "");
			order.setSaleTime(saleTime);
			order.setImagerUrl(order.getId() + "");

			order.setOrderproduct(order.getOrderproduct());

			order.setOrdergift(order.getOrdergift());

			order.setId(0);

			int flag = OrderManager.save(user, order);

			String ids = "(" + flag + "," + oid + ")";

			OrderManager.updateHPOS(user, ids);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void cancelTuihuo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 处理请求，并执行resetToken方法，将session中的token去除
			User user = (User) request.getSession().getAttribute("user");
			String oid = request.getParameter("orderid");

			String ids = "(" + oid + ")";

			OrderManager.updateHPOS(user, ids);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void saveaftersale(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			boolean flag = true;
			boolean isreturn = true;
			// 处理请求，并执行resetToken方法，将session中的token去除
			User user = (User) request.getSession().getAttribute("user");
			String href = "../admin/afterSale/dingdansubmit.jsp";
			String oid = request.getParameter("orderid");
			List<String> listas = new ArrayList<String>();
			String typemethod = request.getParameter("typemethod");
			String uname = request.getParameter("uname");
			String phone = request.getParameter("phone");
			String cid = request.getParameter("ordercategory");
			String tname = request.getParameter("ordertype");
			String batchNumber = request.getParameter("orderbatchNumber");
			String barcode = request.getParameter("orderbarcode");
			String andate = request.getParameter("andate");

			String saledate = request.getParameter("saledate");

			String location = request.getParameter("locations");
			String remark = request.getParameter("remark");
			String fault = request.getParameter("fault");
			String statues = request.getParameter("statues");
			String pritlnid = request.getParameter("printid");
			String ptype = request.getParameter("ptype");
			String dealsendid = request.getParameter("dealsendid"); 
			//String opid = request.getParameter("opid");

			if (StringUtill.isNull(pritlnid)) {
				pritlnid = "T" + TimeUtill.gettimeString();
			}

			if (StringUtill.isNull(ptype)) {
				ptype = AfterSale.updateOrder + "";
			}
			int submitid = user.getId();
			String branch = user.getBranch();

			int maxid = AfterSaleManager.getMaxid();

			if (!StringUtill.isNull(oid)) {
				maxid = Integer.valueOf(oid);
				flag = AfterSaleManager.checkePermission(user, oid);
				AfterSale af = AfterSaleManager.getAfterSaleID(user, oid);

				submitid = af.getSubmitId();
				branch = af.getBranch() + "";
				isreturn = false;

			}

			if (0 == maxid) {
				maxid = 1;
			}
			logger.info(maxid);
 
			String submit = TimeUtill.getdateString();
			AfterSale af = new AfterSale();
			//if (!StringUtill.isNull(opid)) {
			//	af.setOpid(Integer.valueOf(opid));
			//}

			af.setId(maxid);
			af.setUname(uname);
			af.setPhone(phone);
			af.setCid(Integer.valueOf(cid));
			// logger.info(tname);
			// af.setTid(ProductService.gettypemap().get(tname).getId());
			af.settName(tname);
			af.setBarcode(barcode);
			af.setBatchNumber(batchNumber);
			af.setAndate(andate);
			af.setSaledate(saledate);
			af.setLocation(location);
			af.setDetail(remark);
			af.setType(Integer.valueOf(ptype));
			af.setStatues(AfterSale.typesale);
			af.setBranch(Integer.valueOf(branch));
			af.setPcount(1);
			af.setSubmitTime(submit);
			af.setSubmitId(submitid);
			af.setPrintid(pritlnid);

			if (!StringUtill.isNull(statues)) {
				af.setStatues(Integer.valueOf(statues));
				af.setStatuestime("'" + TimeUtill.getdateString() + "'");
			}

			logger.info(typemethod);

			if ("fault".equals(typemethod)) {
				af.setStatues(AfterSale.typeupdate);
				String uid = request.getParameter("uid");
				String thistime = "'" + request.getParameter("thistime") + "'";
				AfterSaleProduct asp = new AfterSaleProduct();
				asp.setAsid(maxid);

				asp.setCause(fault);
				asp.setType(AfterSaleProduct.fault);
				asp.setDealid(Integer.valueOf(uid));
				asp.setThistime(thistime);
				List<String> listsp = AfterSaleProductManager.getsaveSQL(user,
						asp);
				listas.addAll(listsp);
				href = "../admin/afterSale/dingdansubmitfault.jsp";
			} else if ("maintain".equals(typemethod)) {
				AfterSaleProduct afp = null;
				if (!StringUtill.isNull(oid)) {
					afp = AfterSaleProductManager.getByid(oid);
				}

				String uid = request.getParameter("uid");
				String thistime =  request.getParameter("thistime");
				String nexttime = request.getParameter("nexttime");

				if(!StringUtill.isNull(uid) && !StringUtill.isNull(thistime)){
					if (null == afp) {
						af.setStatues(AfterSale.typeupdate);
 
						//af.setNexttime(nexttime);
						String[] producs = request.getParameterValues("product");

						if (null != producs) {
							// 送货状态
							for (int i = 0; i < producs.length; i++) {
								AfterSaleProduct asp = new AfterSaleProduct();
								String ccid = request.getParameter("ordercategory"
										+ producs[i]);
								String ttname = request.getParameter("ordertype"
										+ producs[i]);
								// logger.info(ttname); 
								int tid = ProductService.gettypemap(user).get(ttname)
										.getId();
								// logger.info(tid);
								asp.setAsid(maxid);
								// logger.info(maxid);
								asp.setCid(Integer.valueOf(ccid));
								asp.setTid(tid);
								// asp.setType(AfterSaleProduct.maintain);
								if (!StringUtill.isNull(uid)) {
									asp.setDealid(Integer.valueOf(uid));
								}

								asp.setNexttime(nexttime);
								asp.setThistime(thistime);
								asp.setCause(fault);
								List<String> listsp = AfterSaleProductManager
										.getsaveSQL(user, asp);
								listas.addAll(listsp);
							}
						} else {
							AfterSaleProduct asp = new AfterSaleProduct();
							asp.setAsid(maxid);
 
							asp.setCause(fault);
							// asp.setType(AfterSaleProduct.maintain);
							asp.setDealid(Integer.valueOf(uid));
							asp.setThistime(thistime);
							List<String> listsp = AfterSaleProductManager
									.getsaveSQL(user, asp);
							listas.addAll(listsp);

						}
					} else {
						afp.setCause(afp.getCause() + "补充问题：" + fault);
						String sqld = AfterSaleProductManager.delete(afp.getId());
						List<String> sali = AfterSaleProductManager.getsaveSQL(
								user, afp); 
						listas.add(sqld); 
						listas.addAll(sali); 
					}
				}else if(!StringUtill.isNull(nexttime)){
					af.setNexttime(nexttime); 
				}
				// logger.info(afp);
				
				href = "../admin/afterSale/dingdansubmitmaintain.jsp";
 
			} else if ("adddetail".equals(typemethod)) {
				String nexttime = request.getParameter("nexttime");
				String opids[] = request.getParameterValues("opid");
				//logger.info(opids.length);  
			    String opidss = StringUtill.getStr(opids);
				//logger.info(opidss);  
				   
 				if ("1".equals(statues)) {  
					if (!StringUtill.isNull(nexttime)) {
						af.setNexttime( nexttime); 
						AftersaleAllManager.updatestatuesmatain(opidss); 
					}
				} else if ("2".equals(statues)) {
					AftersaleAllManager.chargestatuesmatain(opidss);
				}

				// logger.info("nexttime"+nexttime);

			} else {
				AfterSaleProduct asp = new AfterSaleProduct();
				asp.setAsid(maxid);
				asp.setType(AfterSaleProduct.install);
				asp.setResult(AfterSaleProduct.success);
				// asp.setDealid(or.getDealsendId());
				// asp.setDealsendid(!StringUtill.isNull(or.getSendId()+"")?or.getSendId():or.getInstallid());
				asp.setDealid(user.getId()); 
				asp.setDealsendid(Integer.valueOf(dealsendid)); 
				List<String> listsql1 = AfterSaleProductManager.getsaveSQL(
						user, asp);
				listas.addAll(listsql1);
			} 
 
			List<String> listsql = AfterSaleManager.getsaveSQL(user, af);
			listas.addAll(listsql);
			if (flag) { 
				DBUtill.sava(listas);
				isreturn = false;
			}

			try {
				if (isreturn) {
					response.sendRedirect(href);
				} else {
					int mark = -1;
					if (flag) {
						mark = RemarkUtill.success;
					} else {
						mark = RemarkUtill.nopermission;
					}
					response.sendRedirect("../jieguo.jsp?type=updated&mark="
							+ mark);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void queryaftersale(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 处理请求，并执行resetToken方法，将session中的token去除
			User user = (User) request.getSession().getAttribute("user");
			String orderid = request.getParameter("orderid");
			String statues = request.getParameter("statues");

			String[] orderids = orderid.split(",");
			for (int i = 0; i < orderids.length; i++) {
				String ordid = orderids[i];
				String[] oids = ordid.split("_");
				String oid = oids[1];
				// String opid = oids[0];
				Order or = OrderManager.getOrderID(user, Integer.valueOf(oid));
				List<String> listas = new ArrayList<String>();
				AfterSaleManager.saveByOrder(user, or, listas);

				// HashMap<Integer,Category> categorymap =
				// CategoryService.getmap();
				// Map<Integer, Product> pmap = ProductService.getIDmap();

				String sql = OrderProductManager.getupdateIsSubmitsql(oid,
						statues);

				listas.add(sql);

				/*
				 * List<OrderProduct> listop = or.getOrderproduct();
				 * //logger.info(listop); if(null != listop){ for(int
				 * j=0;j<listop.size();j++){ OrderProduct op = listop.get(j);
				 * if(op.getStatues() == 0 && op.getId() ==
				 * Integer.valueOf(opid)){ AfterSale as = new AfterSale();
				 * 
				 * as.setOpid(op.getId()); as.setPrintid(or.getPrintlnid());
				 * as.setAndate
				 * (StringUtill.isNull(or.getInstalltime())==true?or.
				 * getSendtime():or.getInstalltime());
				 * as.setBarcode(op.getBarcode());
				 * as.setBatchNumber(op.getBatchNumber());
				 * as.setBranch(or.getBranch());
				 * as.setBranchName(or.getbranchName(or.getBranch()));
				 * as.setCid(op.getCategoryId()); as.setPcount(op.getCount());
				 * as.setcName(categorymap.get(op.getCategoryId()).getName());
				 * as.setLocation(or.getLocateDetail());
				 * as.setPhone(or.getPhone1());
				 * as.setSaledate(or.getSaleTime());
				 * as.setTid(Integer.valueOf(op.getSendType()));
				 * as.setStatues(Integer.valueOf(statues));
				 * as.settName(pmap.get(
				 * Integer.valueOf(op.getSendType())).getType());
				 * //System.out.println
				 * (pmap.get(Integer.valueOf(op.getSendType())).getName());
				 * as.setUname(or.getUsername());
				 * as.setSubmitTime(TimeUtill.getdateString());
				 * as.setSubmitId(or.getDealsendId());
				 * 
				 * logger.info(as.getOpid());
				 * 
				 * List<String> listsql = AfterSaleManager.getsaveSQL(user, as);
				 * listas.addAll(listsql); } } }
				 */
				DBUtill.sava(listas);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void updateaftersale(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 处理请求，并执行resetToken方法，将session中的token去除
			String orderid = request.getParameter("orderid");
			String statues = request.getParameter("statues");

			List<String> listas = new ArrayList<String>();

			String sql = AfterSaleManager
					.getupdateIsSubmitsql(orderid, statues);
			listas.add(sql);

			DBUtill.sava(listas);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	private void HuanHuo(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		try {
			response.sendRedirect("huanhuo.jsp?id=" + id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 调用核心业务类接收消息、处理消息
		doGet(request, response);

		// 响应消息
	}

}
