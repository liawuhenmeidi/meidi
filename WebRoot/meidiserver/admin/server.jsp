<%@ page language="java"
	import="java.util.*,orderproduct.*,httpClient.*,makeInventory.*,ordersgoods.*,httpClient.*,httpClient.download.*,saledealsend.*,uploadtotalgroup.*,installsale.*,product.*,message.*,inventory.*,branchtype.*,user.*,utill.*,locate.*,branch.*,order.*,orderPrint.*,category.*,group.*,grouptype.*"
	pageEncoding="utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 

	User user = (User) session.getAttribute("user");
 
	String method = request.getParameter("method");
 
	if ("deleOrder".equals(method)) {
		String id = request.getParameter("id");
		String[] list = id.split(",");
		int count = 0;
		for (int i = 0; i < list.length; i++) {
			boolean flag = OrderManager.delete(user,
					Integer.valueOf(list[i]));
			if (flag) {
				count++;
			}
		}
		response.getWriter().write("" + count);
		response.getWriter().flush();
		response.getWriter().close();
	} else if ("category_add".equals(method)) {
		String categoryName = request.getParameter("categoryName");
		boolean b = CategoryManager.getName(categoryName);
		response.getWriter().write("" + b);
		response.getWriter().flush();
		response.getWriter().close();
	} else if ("updatePhone".equals(method)) {
		String uid = request.getParameter("uid");
		String phone = request.getParameter("phone");
		String branchid = request.getParameter("branchid");
		boolean b = UserManager.updatePhone(uid, phone, branchid);
		response.getWriter().write("" + b);
		response.getWriter().flush();
		response.getWriter().close();
		//updatePhone
	} else if ("locate".equals(method)) {
		String locateName = request.getParameter("id");
		LocateManager.save(locateName);
	} else if ("jusetype".equals(method)) {
		String jueseName = request.getParameter("id");
		GrouptypeManager.save(jueseName);
	} else if ("branch".equals(method)) { // branchis 
		String locateName = request.getParameter("id");
		String pid = request.getParameter("pid");
		//BranchManager.save(locateName,pid);        
	} else if ("branchis".equals(method)) { // branchis 
		String locateName = request.getParameter("id");
		String pid = request.getParameter("pid");
		boolean flag = BranchManager.isname(locateName);
		response.getWriter().write("" + flag);
		response.getWriter().flush();
		response.getWriter().close();
	} else if ("branchtypeupdate".equals(method)) {
		String bid = request.getParameter("bid");
		String c = request.getParameter("id"); 
		String typestatues = request.getParameter("typestatues");
		String exportmodel = request.getParameter("modelstatues");
		BranchTypeManager.update(c, bid, typestatues,exportmodel);
		//branchinventory
	} else if ("branchinventory".equals(method)) {
		String bid = request.getParameter("bid");
		String statues = request.getParameter("statues");
		BranchManager.update(bid, statues);
		//branchtypeinventory
	} else if ("branchtypeinventory".equals(method)) {
		String bid = request.getParameter("bid");
		String statues = request.getParameter("statues");
		BranchTypeManager.update(Integer.valueOf(statues), bid);
		//branchtypeinventory
	} else if ("grouptypeupdate".equals(method)) {
		String bid = request.getParameter("bid");
		String c = request.getParameter("id");
		GrouptypeManager.update(c, bid);
		//branchtypeupdate
	} else if ("jihuo".equals(method)) {
		String id = request.getParameter("id");
		String statues = request.getParameter("statues");
		//System.out.println("id"+id+"&&&statues"+statues); 
		UserManager.setStatues(Integer.valueOf(id),
				Integer.valueOf(statues));
	} else if ("duanhuo".equals(method)) {
		String id = request.getParameter("id");
		String statues = request.getParameter("statues");
		CategoryManager.setStatues(Integer.valueOf(id),
				Integer.valueOf(statues));
	} else if ("huiyuan_add".equals(method)) {
		//System.out.println("&&&&&"); 
		String categoryName = request.getParameter("huiyuanName");
		boolean b = UserManager.getName(categoryName);
		response.getWriter().write("" + b);
		response.getWriter().flush();
		response.getWriter().close();
	} else if ("orderGo".equals(method)) {
		String id = request.getParameter("id");
		String statue = request.getParameter("statues");
		if (Integer.valueOf(statue) == 0) {
			method = "orderCome";
		}
		int statues = OrderManager.updateStatues(user, method, statue,
				id);
		response.getWriter().write("" + statues);
		response.getWriter().flush();
		response.getWriter().close(); //orderDingma
	} else if ("orderCharge".equals(method)) {
		String id = request.getParameter("id");
		String statue = request.getParameter("statues");
		if ("0".equals(statue)) {
			method = "orderGo";
		}
		int statues = OrderManager.updateStatues(user, method, statue,
				id);
		response.getWriter().write("" + statues);
		response.getWriter().flush();
		response.getWriter().close(); //orderDingma
	} else if ("orderDingma".equals(method)
			|| "orderCome".equals(method) || "print2".equals(method)
			|| "print3".equals(method) || "print4".equals(method)
			|| "print".equals(method) || "orderover".equals(method)
			|| "statuescallback".equals(method)
			|| "statuespaigong".equals(method)
			|| "statuesinstall".equals(method)
			|| "statuesinstalled".equals(method)
			|| "printdingma".equals(method)
			|| "statuescallback".equals(method)
			|| "wenyuancallback".equals(method)) {
		String id = request.getParameter("id");
		int statues = OrderManager.updateStatues(user, method,
				Order.query + "", id);
		response.getWriter().write("" + statues);
		response.getWriter().flush();
		response.getWriter().close(); //orderDingma
	} else if ("huiyuan_zhuguan".equals(method)) {
		System.out.println(12);
		String type = request.getParameter("type");
		List<String> list = UserManager.getUsersregist(Integer
				.valueOf(type));
		String str = StringUtill.GetJson(list);
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close();
	} else if ("juese_add".equals(method)) {
		String categoryName = request.getParameter("jueseName");
		boolean b = GroupManager.getName(categoryName);
		response.getWriter().write("" + b);
		response.getWriter().flush();
		response.getWriter().close();
	} else if ("headremind".equals(method)) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<User> ulist = UserManager.getUserszhuce(user);
		int ucount = 0;
		if (ulist != null) {
			ucount = UserManager.getUserszhuce(user).size();
		}
		int mcount = OrderManager.getOrderlistcount(user,
				Group.dealSend, Order.motify, 0, 0, "id", "");
		int rcount = OrderManager.getOrderlistcount(user,
				Group.dealSend, Order.release, 0, 0, "id", "");
		int ncount = OrderManager.getOrderlistcount(user,
				Group.dealSend, Order.neworder, 0, 0, "id", "");
		int recount = OrderManager.getOrderlistcount(user,
				Group.dealSend, Order.returns, 0, 0, "id", "");
		int hcount = OrderManager.getOrderlistcount(user,
				Group.dealSend, Order.huanhuo, 0, 0, "id", "");
		int inventory = InventoryManager.getCategory(user,
				"unconfirmed").size();
		int analyInventory = InventoryManager.getCategoryAnalyze(user,
				"confirmed").size();

		map.put("ucount", ucount);
		map.put("mcount", mcount);
		map.put("rcount", rcount);
		map.put("ncount", ncount);
		map.put("recount", recount);
		map.put("hcount", hcount);
		map.put("inventory", inventory);
		map.put("analyInventory", analyInventory);

		String strmap = StringUtill.GetJson(map);
		response.getWriter().write(strmap);
		response.getWriter().flush();
		response.getWriter().close();

	} else if ("disatchpHeadremind".equals(method)) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int dcount = OrderManager.getOrderlistcount(user,
				Group.sencondDealsend, Order.dispatch, 0, 0, "id", "");
		int icount = OrderManager.getOrderlistcount(user,
				Group.sencondDealsend, Order.installonly, 0, 0, "id",
				"");
		//int ncount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.neworder,0,0,"id",""); 
		List<User> ulist = UserManager.getUserszhuce(user);
		int hcount = 0;
		if (ulist != null) {
			hcount = UserManager.getUserszhuce(user).size();
		}
		int inventory = InventoryManager.getCategory(user,
				"unconfirmed").size();
		int analyInventory = InventoryManager.getCategoryAnalyze(user,
				"confirmed").size();
		int rcount = OrderManager.getOrderlistcount(user,
				Group.sencondDealsend, Order.release, 0, 0, "id", "");
		map.put("dcount", dcount);
		map.put("icount", icount);
		map.put("hcount", hcount);
		map.put("rcount", rcount);
		map.put("inventory", inventory);
		map.put("analyInventory", analyInventory);

		String strmap = StringUtill.GetJson(map);
		response.getWriter().write(strmap);
		response.getWriter().flush();
		response.getWriter().close(); //inventorydetail
	} else if ("inventory".equals(method)) {

		String branch = request.getParameter("branch");
		String ctype = request.getParameter("ctype");
		String time = request.getParameter("time");

		List<InventoryBranch> list = InventoryBranchManager
				.getCategory(user,branch, ctype);
		System.out.println(time + "time");
		if (!StringUtill.isNull(time)) {
			session.setAttribute("inventorytime", time);
		} else {
			session.removeAttribute("inventorytime");
		}
		String str = StringUtill.GetJson(list);
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close(); //inventory
	} else if ("dealsendcharge".equals(method)
			|| "sendcharge".equals(method)
			|| "installcharge".equals(method)
			|| "sendinstallcharge".equals(method)) {
		boolean flag = false;
		String name = request.getParameter("name");
		String dealsendid = request.getParameter("branchid");
		String id = request.getParameter("id");
		String said = request.getParameter("said");
		//System.out.println(said ); 
		String savetype = request.getParameter("savetype");
		//System.out.println(savetype );
		String message = request.getParameter("message");
		List<SaledealsendMessage> list = new ArrayList<SaledealsendMessage>();

		Saledealsend sa = new Saledealsend();

		if (StringUtill.isNull(said)) {
			said = (SaledealsendManager.getmaxid() + 1) + "";
			flag = true;
		} else {
			Saledealsend sade = SaledealsendManager
					.getSaledealsend(said);
			sa.setGivestatues(sade.getGivestatues());
			sa.setReceivestatues(sade.getReceivestatues());
		}

		sa.setId(Integer.valueOf(said));

		if ("left".equals(savetype)) {
			sa.setGivestatues(1);
		} else if ("right".equals(savetype)) {
			sa.setReceivestatues(1);
		}
		if (!StringUtill.isNull(dealsendid + "")) {
			sa.setDealsendid(Integer.valueOf(dealsendid));
		}

		if (!StringUtill.isNull(message)) {
			String[] messages = message.split(",");
			for (int i = 0; i < messages.length; i++) {
				String messa = messages[i];
				String[] messas = messa.split("-");
				SaledealsendMessage sas = new SaledealsendMessage();
				sas.setDealsendMessage(messas[2]);
				sas.setDealsendprice(Integer.valueOf(messas[1]));
				sas.setSaledealsendID(Integer.valueOf(said));
				sas.setOrderids(messas[0]);
				list.add(sas);
			}
		} 
		sa.setList(list);
		sa.setMessage(message);
		sa.setName(name);
		sa.setOrderids(id);
		SaledealsendManager.save(user, sa, flag, method);
		//System.out.print(name+"**"+branchid+"*"+id+"*"+message);
	} else if ("inventoryall".equals(method)) {
		String str = "";  
		String branch = request.getParameter("branch");
		String category = request.getParameter("category");
		String product = request.getParameter("product");
		String isSN = request.getParameter("isSN"); 
		String typestatues = request.getParameter("typestatues"); 
		//System.out.println(product);        
		Collection<InventoryAll> c = InventoryAllManager.getMap(user,branch,
				category, product,isSN,typestatues);     
		str = StringUtill.GetJson(c);  
		//System.out.println(str+""); 
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close(); //inventory 
	} else if ("inventoryallSN".equals(method)) {
		String str = "";   
		String branch = request.getParameter("branch");
		String category = request.getParameter("category");
		String product = request.getParameter("product");
		String isSN = request.getParameter("isSN");   
		//System.out.println(product);         
		Map<String,List<InventoryAll>> c = InventoryAllManager.getMapSN(user,branch,
				category, product,isSN); 
		// System.out.println(c);  
		str = StringUtill.GetJson(c);   
		//System.out.println(str+""); 
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close(); //inventory
	}else if ("inventorydis".equals(method)) {
		String str = "";
		String branch = request.getParameter("branch");
		String category = request.getParameter("category");
		String type = request.getParameter("type");
		if (StringUtill.isNull(type)) { 
			List<InventoryBranch> list = InventoryBranchManager
					.getCategoryid(user,branch, category);
			Map<String, InventoryAll> map = new HashMap<String, InventoryAll>();

			for (int i = 0; i < list.size(); i++) {
				InventoryBranch inb = list.get(i);
				int categoryid = inb.getInventoryid();
				int branchid = inb.getBranchid();
				InventoryAll listp = map.get(branchid + "");
				if (listp == null) {
					listp = new InventoryAll();
					Category c = CategoryManager.getCategory(categoryid
							+ "");
					listp.setCategoryid(c.getId());
					listp.setCateoryName(c.getName());
					listp.setBranchid(branchid);
					listp.setTypeid(inb.getTypeid());
					listp.setPapercount(inb.getPapercount());
					listp.setRealcount(inb.getRealcount());
					listp.setIsquery(inb.isquery());
					map.put(branchid + "", listp);
				} else {
					listp.setPapercount(listp.getPapercount()
							+ inb.getPapercount());
					listp.setRealcount(listp.getRealcount()
							+ inb.getRealcount());
					listp.setIsquery(listp.getIsquery()
							&& inb.isquery());
				}

				Collection<InventoryAll> c = map.values();
				str = StringUtill.GetJson(c);
			}
		} else {
			List<InventoryBranch> list = InventoryBranchManager
					.getCategory(user,branch, type);
			Map<String, InventoryAll> map = new HashMap<String, InventoryAll>();

			for (int i = 0; i < list.size(); i++) {
				InventoryBranch inb = list.get(i);
				int categoryid = inb.getInventoryid();
				int branchid = inb.getBranchid();
				InventoryAll listp = map.get(branchid + "");
				if (listp == null) {
					listp = new InventoryAll();
					Category c = CategoryManager.getCategory(categoryid
							+ "");
					listp.setCategoryid(c.getId());
					listp.setCateoryName(c.getName());
					listp.setType(inb.getType());
					listp.setTypeid(inb.getTypeid());
					listp.setBranchid(branchid);
					listp.setPapercount(inb.getPapercount());
					listp.setRealcount(inb.getRealcount());
					listp.setIsquery(inb.isquery());
					listp.setTime(inb.getQuerymonth());
					map.put(branchid + "", listp);
				} else {
					listp.setPapercount(listp.getPapercount()
							+ inb.getPapercount());
					listp.setRealcount(listp.getRealcount()
							+ inb.getRealcount());
					listp.setIsquery(listp.getIsquery()
							&& inb.isquery());
				}

				Collection<InventoryAll> c = map.values();
				str = StringUtill.GetJson(c);
			}
		}
		System.out.println(str + "");
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close(); //inventory
	} else if ("inventorydetail".equals(method)) {

		String ctype = request.getParameter("ctype");
		String branchid = request.getParameter("branchid");
		String time = request.getParameter("time");
		if (StringUtill.isNull(time)) {
			time = "";
		}
		List<InventoryBranchMessage> list = InventoryBranchMessageManager
				.getCategory(ctype, branchid, time);

		String str = StringUtill.GetJson(list);
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close(); //inventory
	} else if ("getinventory".equals(method)) { 
		String types = request.getParameter("types");
		String uid = request.getParameter("uid");
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtill.isNull(types) && !StringUtill.isNull(uid)) {
			User u = UserManager.getUesrByID(uid);
			List<InventoryBranch> list = InventoryBranchManager
					.getCategory(user,u.getBranch(), ""); 
			String[] type = types.split("</p>");
			for (int i = 0; i < type.length; i++) {
				if (!StringUtill.isNull(type[i])) {
					map.put(type[i], "账面库存：" + 0 + "实际库存:" + 0);
					for (int j = 0; j < list.size(); j++) {
						InventoryBranch inb = list.get(j);

						//System.out.println(type[i]+"***"+inb.getType());
						if (inb.getType().equals(type[i])) {
							map.put(type[i],
									"账面库存：" + inb.getPapercount()
											+ "   实际库存:"
											+ inb.getRealcount());
						}
					}
				}
			}
		}
		String str = StringUtill.GetJson(map);
		response.getWriter().write(str);
		response.getWriter().flush();
		response.getWriter().close(); //inventory

	} else if ("quit".equals(method)) {
		session.invalidate();
		response.sendRedirect("login.jsp");
		return;
	} else if ("updateorder".equals(method)) {
		int statues = -1;
		int pstatues = 0;
		String typeMethod = request.getParameter("typeMethod");
		String oid = request.getParameter("oid");
		String phone1 = request.getParameter("phone1");
		String andate = request.getParameter("andate");
		String diqu = request.getParameter("diqu");
		String locations = request.getParameter("locations");
		String message = request.getParameter("message");
		String remark = request.getParameter("remark") == null ? ""
				: request.getParameter("remark");
		if (UserManager.checkPermissions(user, Group.dealSend)) {
			String POS = request.getParameter("pos");
			String sailId = request.getParameter("sailid");
			String check = request.getParameter("check");
			String saledate = request.getParameter("saledate");
			String saleType = request.getParameter("dingmatype");
			String categoryId = request
					.getParameter("dingmaordercategory");
			String count = request.getParameter("dingmaproductNum");
			statues = OrderManager.updateMessage(phone1, andate,
					locations, POS, sailId, check, oid, remark,
					saledate, diqu);
			if (!StringUtill.isNull(saleType)
					&& !StringUtill.isNull(categoryId)) {

				Order order = new Order();
				order.setId(Integer.valueOf(oid));
				String type = order.getSendType(1, "");

				if (StringUtill.isNull(type)) {

					OrderProduct o = new OrderProduct();
					if (!NumbleUtill.isNumeric(saleType)) {
						type = ProductService.gettypemap()
								.get(saleType).getId()
								+ "";
					}

					o.setCategoryId(Integer.valueOf(categoryId));
					o.setCount(Integer.valueOf(count));
					o.setSaleType(type);
					o.setStatues(1);

					List<OrderProduct> listop = new ArrayList<OrderProduct>();
					listop.add(o);
					order.setOrderproduct(listop);

					List<String> sqls = OrderProductManager.save(
							Integer.valueOf(oid), order);
					DBUtill.sava(sqls);
				} else {

					pstatues = OrderProductManager.updateOrderStatues(
							user, categoryId, saleType, count, oid);
				}
			} else {
				String sql = OrderProductManager.delete(
						Integer.valueOf(oid), 1);
				DBUtill.sava(sql);
			}
			OrderProductService.flag = true;
		} else {
			statues = OrderManager.updateMessage(phone1, andate,
					locations, oid);
		}
		if (!StringUtill.isNull(message)) {
			Message messa = new Message();
			messa.setOid(Integer.valueOf(oid));
			messa.setMessage(user.getUsername() + ":" + message + "\n");
			MessageManager.save(user, messa);
		}
		//response.sendRedirect("dingdanDetail.jsp?id="+oid);
		//response.sendRedirect(""); 

		if (statues == -1 || pstatues == -1) {
			response.sendRedirect("../jieguo.jsp?type=update");
			//System.out.println(123);  
			session.setAttribute("message", "修改失败");

		} else {
			if ("print".equals(typeMethod)) {
				response.sendRedirect("print.jsp?id=" + oid + "&type="
						+ Order.dingma);
			} else {
				response.sendRedirect("../jieguo.jsp?type=updated");
				//System.out.println(123);   
				session.setAttribute("message", "修改成功");
			}

		} 
		return;  
	} else if ("pandian".equals(method)) {
		//data:"method=pandian&branchid="+branchid+"&type="+type,
		String branchid = request.getParameter("branchid");
		String type = request.getParameter("type");   
		InventoryBranchManager.update(user, branchid, type);
	}  else if ("pandianSN".equals(method)) {  
		//data:"method=pandian&branchid="+branchid+"&type="+type, 
		String branchid = request.getParameter("branchid");
		String type = request.getParameter("type"); 
		String typestatues = request.getParameter("typestatues"); 
		InventoryBranchManager.updateSN(user, branchid, type,typestatues);
	}else if ("pandians".equals(method)) {    
		//data:"method=pandian&branchid="+branchid+"&type="+type,
		String branch = request.getParameter("branch");
	   String time = request.getParameter("time");   
	   String[] typestatues = request.getParameterValues("typestatues"); 
	  
	    branch = new String(branch.getBytes("UTF-8"), "ISO8859-1");
		String branchid = request.getParameter("branchid");  
		String[] types = request.getParameterValues("type"); 
		String category = request.getParameter("category");   
		InventoryBranchManager.update(user, branchid, types,time,typestatues);    
		                 
		response.sendRedirect("inventory/inventory1check.jsp?branch="+branch+"&category="+category+"&time="+time);
		
	}else if ("pandiansall".equals(method)) {      
		//data:"method=pandian&branchid="+branchid+"&type="+type,
	   String branch = request.getParameter("branch");
	   branch = new String(branch.getBytes("UTF-8"), "ISO8859-1");
	   String time = request.getParameter("time");   
	    
		String branchid = request.getParameter("branchid");  
		String[] types = request.getParameterValues("type");  
		String uuids = "";
		
		String[] typestatues = new String[5];    
		String muuid = request.getParameter("smodel");
		String inuuid = request.getParameter("sin");
		String outuuid = request.getParameter("sout");

		if(!StringUtill.isNull(muuid)){
			uuids += ",'"+muuid+"'";
			typestatues[0] ="3";
		} 
		if(!StringUtill.isNull(inuuid)){
			uuids += ",'"+inuuid+"'";
			typestatues[1] ="0";
		} 
		if(!StringUtill.isNull(outuuid)){
			uuids += ",'"+outuuid+"'";
			typestatues[2] ="1";
			typestatues[3] ="2";
		}  
		
		String category = request.getParameter("category");  
		  
		if(null != types && types.length >0){
			InventoryBranchManager.update(user, branchid, types,time,typestatues);    
			List<String> listsql = MakeInventoryManager.update(uuids, types);
			DBUtill.sava(listsql); 
		}
		//System.out.println(StringUtill.GetJson(types));  
		
		response.sendRedirect("inventory/inventory2check.jsp?branch="+branch+"&category="+category+"&time="+time+"&smodel="+muuid+"&sin="+inuuid+"&sout="+outuuid);
		
	}else if ("savesalecategory".equals(method)) { 
		String isuid = request.getParameter("isuid");
		String chargetype = request.getParameter("chargetype");
		String uid = request.getParameter("uid");
		String username = request.getParameter("username");
		String phone = request.getParameter("phone");
		String locate = request.getParameter("locate");
		String andate = request.getParameter("andate");
		String[] category = request.getParameterValues("salecate");
		String[] product = null;

		try {
			product = request.getParameterValues("salepro");
		} catch (Exception e) {

		}

		boolean flag = true;

		if (null == isuid || "".equals(isuid)) {
			isuid = (InstallSaleManager.getmaxid() + 1) + "";
			if (StringUtill.isNull(isuid)) {
				isuid = 1 + "";
			}
			flag = false;
		}

		List<InstallSaleMessage> list = new ArrayList<InstallSaleMessage>();
		//String message = "{";
		for (int i = 0; i < category.length; i++) {
			String id = category[i];
			String price = request.getParameter("c" + id);
			InstallSaleMessage ins = new InstallSaleMessage();

			ins.setCategoryID(id);
			ins.setDealsend(Integer.valueOf(price));
			ins.setInstallsaleID(Integer.valueOf(isuid));
			list.add(ins);
			// message += "\""+id+"\":\""+price+"\",";
		}

		if (null != product) {
			for (int i = 0; i < product.length; i++) {
				String id = product[i];
				if (!StringUtill.isNull(id)) {
					String price = request.getParameter("p" + id);
					if (!StringUtill.isNull(price)) {
						InstallSaleMessage ins = new InstallSaleMessage();
						ins.setProductID(Integer.valueOf(id));
						ins.setDealsend(Integer.valueOf(price));
						ins.setInstallsaleID(Integer.valueOf(isuid));
						list.add(ins);
					}
				}

				// message += "\""+id+"\":\""+price+"\",";
			}
		}

		//message = message.substring(0,message.length()-1)+"}";

		InstallSale in = new InstallSale();

		in.setId(Integer.valueOf(isuid));

		if (!StringUtill.isNull(uid)) {
			in.setUid(Integer.valueOf(uid));
		}
		if (!StringUtill.isNull(username)) {
			in.setUname(Integer.valueOf(username));
		}
		if (!StringUtill.isNull(phone)) {
			in.setPhone(Integer.valueOf(phone));
		}
		if (!StringUtill.isNull(locate)) {
			in.setLocate(Integer.valueOf(locate));
		}
		if (!StringUtill.isNull(andate)) {
			in.setAndate(Integer.valueOf(andate));
		}

		in.setType(Integer.valueOf(chargetype));

		//in.setMessage(message); 
		in.setList(list);

		if (flag) {
			InstallSaleManager.update(in);
		} else {
			InstallSaleManager.save(in);
		}

		response.sendRedirect("../jieguo.jsp?type=updated");
		//

	} else if ("savesalecategorytotal".equals(method)) {
		try {
			String isuid = request.getParameter("isuid");
			String[] category = request.getParameterValues("salecate");
			String message = "{";
			for (int i = 0; i < category.length; i++) {
				String id = category[i];
				String price = request.getParameter("c" + id);
				message += "\"" + id + "\":\"" + price + "\",";
			}

			message = message.substring(0, message.length() - 1) + "}";

			UploadTotalGroup up = new UploadTotalGroup();

			up.setCategoryname(message);
			UploadTotalGroupManager.save(up);
		} catch (Exception e) {
			UploadTotalGroupManager.delete();
		}
		response.sendRedirect("../jieguo.jsp?type=updated");
		//  
	} else if ("getopjson".equals(method)) {
		String id = request.getParameter("oid");
		Order or = OrderManager.getOrderID(user, Integer.valueOf(id));
		String str = or.getSendTypejson(0);
		response.getWriter().write(str); 
		response.getWriter().flush();    
		response.getWriter().close(); //inventory
	} else if ("getproduct".equals(method)) { 
		String branchtype = request.getParameter("branchtype");
		//System.out.println(branchtype);     
		//List<String> listallp = ProductService.getlistall(BranchService.getNameMap().get(branch));
		List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
		//System.out.println(StringUtill.GetJson(listallp));   
		response.getWriter().write(StringUtill.GetJson(listallp));
		response.getWriter().flush();
		response.getWriter().close(); //inventory 
	} else if ("InventorySN".equals(method)) {
		String branch = request.getParameter("branch");
		String tname = request.getParameter("tname");
		String statues = request.getParameter("statues");
		String id = request.getParameter("id");  
		Map<String, String> map = new HashMap<String, String>();
		//System.out.println("branch"+branch+"tname"+tname+"statues"+statues);       
		List<String> s = InventorySN.getinventoryByName(tname, branch,
				statues, map, id);
		//List<String> list = OrderGoodsManager.Updateserialnumber(map);
		//DBUtill.sava(list);      
		// System.out.println(StringUtill.GetJson(map)); 
		response.getWriter().write(StringUtill.GetJson(s));
		response.getWriter().flush(); 
		response.getWriter().close(); //inventory  
	} else if ("getInventoryMapByBranch".equals(method)) {
  
		String branchtype = request.getParameter("branchtype");
		String branch = request.getParameter("branch");
		Map<String,String> map = new HashMap<String,String>();
		
		List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
		String strp = StringUtill.GetJson(listallp);
		
		
		Branch b = BranchService.getNameMap(Integer.valueOf(branchtype)).get(branch);
		//System.out.println("branch"+b.getId());  
		String time = TimeUtill.getdateString();  
		Map<String,InventoryBranch> mapin = InventoryBranchManager.getmapType(user,b.getId()+"");
		String strin = StringUtill.GetJson(mapin);
		
	    Map<String,List<SNInventory>> mapsn = InventoryChange.getMapBranchType(user,time,b.getId());
		  
	    String strsn = StringUtill.GetJson(mapsn);
	   // System.out.println("mapsn"+mapsn);
		Map<String,List<SNInventory>> mapsnModel = InventoryModelDownLoad.getMapBranchType(user, time,b.getId());
		String strmodel = StringUtill.GetJson(mapsnModel);
		// System.out.println("mapsnModel"+strmodel);
		Map<String,SNInventory> mapsale = SaleDownLoad.getMap(TimeUtill.dataAdd(time, -29),time,b.getId()); 
		     
		 String strsale = StringUtill.GetJson(mapsale);
		// System.out.println("strsale"+strsale);
		 
		map.put("strp", strp);
		map.put("strin", strin);
		map.put("mapsn", strsn);
		map.put("strmodel", strmodel);
		map.put("strsale", strsale);
		//System.out.println(map); 
		response.getWriter().write(StringUtill.GetJson(map));
		response.getWriter().flush();
		response.getWriter().close(); //
	} else if ("getInventoryOut".equals(method)) {
		   
				String branchtype = request.getParameter("branchtype");
				String branch = request.getParameter("branch");
				Map<String,String> map = new HashMap<String,String>();
				
				List<String> listallp = ProductService.getlistsale(Integer.valueOf(branchtype));
				String strp = StringUtill.GetJson(listallp);
				
				
				Branch b = BranchService.getNameMap(Integer.valueOf(branchtype)).get(branch);
				//System.out.println("branch"+b.getId());  
				String time = TimeUtill.getdateString();  
				Map<String,InventoryBranch> mapin = InventoryBranchManager.getmapType(user,b.getId()+"");
				String strin = StringUtill.GetJson(mapin);
				
			   
				// System.out.println("strsale"+strsale);
				 
				map.put("strp", strp);
				map.put("strin", strin);
				  
				//System.out.println(map); 
				response.getWriter().write(StringUtill.GetJson(map));
				response.getWriter().flush();
				response.getWriter().close(); //
		} else if ("InitInventorySN".equals(method)) { 
			
			
	   Map<String,String> map = MyMainClient.getinstance().run(); 
	  // String src = map.get("src");    
      // System.out.println("login/loginSN.jsp?src="+src); 
	  // response.sendRedirect("login/loginSN.jsp?src="+src);
	    
		response.getWriter().write(StringUtill.GetJson(map)); 
		response.getWriter().flush();
		response.getWriter().close(); // 
	} else if ("InitInventoryInAndOut".equals(method)) { 
		
		   String time = request.getParameter("time"); 
		   //Map<String,String> map =  
				     
		MyMainClient.getinstance().initInventoryInAndOut(time);
		  // String src = map.get("src");   
	      // System.out.println("login/loginSN.jsp?src="+src); 
		  // response.sendRedirect("login/loginSN.jsp?src="+src);
		  //  StringUtill.GetJson(map) 
			response.getWriter().write(1); 
			response.getWriter().flush();
			response.getWriter().close(); // 
		}  
	 
	else if ("InitInventorySNverifyCode".equals(method)) { 
		   Map<String,String> maps = new HashMap<String,String>();
		   
		   String verifyCode = request.getParameter("verifyCode");
		    
		   maps.put("verifyCode", verifyCode);
		   
		  //  System.out.println(verifyCode); 
		   Map<String,String> map = MyMainClient.getinstance().run(maps);
		  // String src = map.get("src");   
	      // System.out.println("login/loginSN.jsp?src="+src); 
		  // response.sendRedirect("login/loginSN.jsp?src="+src);
		     
			response.getWriter().write(StringUtill.GetJson(map)); 
			response.getWriter().flush();
			response.getWriter().close(); //   
			//   
		}else if ("InitInventoryInOrder".equals(method)) { 
			     
			   String time = request.getParameter("time"); 
			   //Map<String,String> map =   
					       
			MyMainClient.getinstance().initInventoryInOrder(time);
			  // String src = map.get("src");     
		      // System.out.println("login/loginSN.jsp?src="+src); 
			  // response.sendRedirect("login/loginSN.jsp?src="+src);
			  //  StringUtill.GetJson(map) 
				response.getWriter().write(1); 
				response.getWriter().flush();
				response.getWriter().close(); //  
			}else if ("inventoryOutOrder".equals(method)) { 
			     
			   String time = request.getParameter("time"); 
			   //Map<String,String> map =    
					       
			MyMainClient.getinstance().InitInventoryOutOrder(time);
			  // String src = map.get("src");     
		      // System.out.println("login/loginSN.jsp?src="+src); 
			  // response.sendRedirect("login/loginSN.jsp?src="+src);
			  //  StringUtill.GetJson(map) 
				response.getWriter().write(1); 
				response.getWriter().flush();
				response.getWriter().close(); //  
			}  else if ("InitInventoryReceiveorder".equals(method)) {
		//MyMainClient.getinstance().refresh(); 
		response.getWriter().write(1);
		response.getWriter().flush();
		response.getWriter().close();       //
	}
%>
