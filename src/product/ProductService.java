package product;

import group.GroupService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.Branch;
import branch.BranchService;
import branchtype.BranchType;
import branchtype.BranchTypeManager;

import user.User;
import user.UserService;
import utill.StringUtill;

import category.Category;
import category.CategoryService;

public class ProductService {

	protected static Log logger = LogFactory.getLog(ProductService.class);
	public static boolean flag = true;
	public static Map<Integer, Product> idmap;
	public static List<String> list;
	public static List<Product> listall;
	public static HashMap<String, ArrayList<String>> typeName;
	public static List<String> typeNameList;

	public static List<String> getTypeNameList() {
		init();
		if (typeNameList == null) {
			typeNameList = ProductManager.getAllProductName();
		}
		return typeNameList;
	}

	public static Map<String, Product> gettypemap(User user, String bname) {
		init();
		HashMap<String, Product> map = new HashMap<String, Product>();
		// long start = System.currentTimeMillis();

		List<Integer> li = ProductService.getlistallCid(BranchService
				.getNameMap().get(bname));
		//logger.info(li); 
		if (null != listall) { 
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID())) {
					map.put(op.getType(), op);
				}
			}
		}
		return map;
	}
    
	public static Map<String, Product> gettypemap(User user, Integer branchtype) {
		init();
		HashMap<String, Product> map = new HashMap<String, Product>();
		// long start = System.currentTimeMillis();

		List<Integer> li = ProductService.getlistallCid(branchtype);
		//logger.info(li); 
		if (null != listall) { 
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID())) {
					map.put(op.getType(), op);
				}
			}
		}
		return map;
	}
	
	public static Map<String, Product> gettypemap(User user) {
		init();
		HashMap<String, Product> map = new HashMap<String, Product>();
		// long start = System.currentTimeMillis();
		String[] p = GroupService.getidMap().get(user.getUsertype())
				.getProducts().split("_");

		List<String> li = Arrays.asList(p);
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID() + "")) {
					map.put(op.getType(), op);
				}
			}
		}
		return map;
	}

	public static Map<String, Product> gettypemap(Branch branch) {
		init();
		HashMap<String, Product> map = new HashMap<String, Product>();
		User user = null;
		HashMap<String, List<User>> mapb = UserService.getMapBranchid();
		List<User> listuser = mapb.get(branch.getId());
		if (null != listuser) {
			user = listuser.get(0);
		} else {
			return null;
		}
		// long start = System.currentTimeMillis();
		String[] p = GroupService.getidMap().get(user.getUsertype())
				.getProducts().split("_");

		List<String> li = Arrays.asList(p);
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID() + "")) {
					map.put(op.getType(), op);
				}
			}
		}
		return map;
	}

	public static Map<String, Product> gettypemap() {
		init();
		HashMap<String, Product> map = new HashMap<String, Product>();
		// long start = System.currentTimeMillis();
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				map.put(op.getType(), op);
			}
		}
		return map;
	}

	public static Map<String, Product> gettypeNUmmap() {
		init();
		HashMap<String, Product> map = new HashMap<String, Product>();
		// long start = System.currentTimeMillis();
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				map.put(op.getEncoded(), op);
			}
		}
		return map;
	}

	public static Map<String, Product> gettypeNUmmap(int branchtype) {
		
		init(); 
		List<String> li = new ArrayList<String>();
		HashMap<String, Product> map = new HashMap<String, Product>();
		List<Category> listc = CategoryService.getList();
		// logger.info(listc); 
			for (int i = 0; i < listc.size(); i++) {
				Category c = listc.get(i);
				String sales = c.getSales(); 
				  
				if (!StringUtill.isNull(sales)) {
					// logger.info(sales); 
					String sale[] = sales.split("_");
					for (int j = 0; j < sale.length; j++) {
						if (!StringUtill.isNull(sale[j])) { 
							
							/*logger.info(Integer.valueOf(sale[j])) ;
							logger.info( branchtype);     
							logger.info(Integer.valueOf(6) == 6);
							logger.info(Integer.valueOf(sale[j]) - branchtype);*/
							
							if (Integer.valueOf(sale[j]) == branchtype) {
								li.add(c.getId() + "");
								continue;
							}
						}
					}
				}   
			}     
		 logger.info(li); 
		if (null != getlistall()) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i); 
				if (op.getStatues() == 0 && li.contains(op.getCategoryID() + "")) {
					map.put(op.getEncoded(), op);
				}
			} 
		} 
    
		//logger.info(map);
		
		return map;
	}
	
	public static HashMap<String, ArrayList<String>> gettypeName() {
		init();
		if (typeName == null) {
			typeName = ProductManager.getProductName();
		}
		return typeName;
	}

	public static List<String> getlist(int id) { 
		//long start = System.currentTimeMillis();
		init(); 
		//long start1 = System.currentTimeMillis();
		//logger.info(start1 - start); 
		if (typeName == null) {  
			//logger.info("getlist"); 
			typeName = ProductManager.getProductName();
		}  
		
	//	long start11 = System.currentTimeMillis();
		///logger.info(start11 - start1);  
		List<String> list = typeName.get(id + "");
		//long start2 = System.currentTimeMillis();
		//logger.info(start2 - start1);  
		
		
		return list;
	}

	public static List<Product> getlistall() {
		init();
		if (listall == null) {
			listall = ProductManager.getProductList();
		}
		return listall;
	}

	public static List<String> getlistall(User user) {
		init();
		List<String> list = new ArrayList<String>();
		// long start = System.currentTimeMillis();
		String[] p = GroupService.getidMap().get(user.getUsertype())
				.getProducts().split("_");

		List<String> li = Arrays.asList(p);
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID() + "")) {
					list.add(op.getType());
				}
			}
		}
		// logger.info(System.currentTimeMillis() - start);
		return list;
	}
 
	public static List<String> getlistsale(User user) {
		init();   
		List<String> list = new ArrayList<String>(); 
		// long start = System.currentTimeMillis(); 
		String[] p = GroupService.getidMap().get(user.getUsertype()) 
				.getProducts().split("_"); 
  
		List<String> li = Arrays.asList(p);  
		if (null != listall) {   
			for (int i = 0; i < listall.size(); i++) {  
				Product op = listall.get(i);   
				if (op.getStatues() == 0 && li.contains(op.getCategoryID() + "")) {  
					list.add(op.getType()); 
				}
			}
		}
		// logger.info(System.currentTimeMillis() - start);
		return list;
	}

	
	public static List<Product> getlistallObject(User user) {
		init();
		List<Product> list = new ArrayList<Product>();
		// long start = System.currentTimeMillis();
		String[] p = GroupService.getidMap().get(user.getUsertype())
				.getProducts().split("_");

		List<String> li = Arrays.asList(p);
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID() + "")) {
					list.add(op);
				}
			}
		}
		// logger.info(System.currentTimeMillis() - start);
		return list;
	}

	public static List<String> getlistall(Branch branch) {
		init();
		List<String> list = new ArrayList<String>();
		List<String> li = new ArrayList<String>();

		List<Category> listc = CategoryService.getList();
		// logger.info(listc);
			for (int i = 0; i < listc.size(); i++) {
				Category c = listc.get(i);
				String sales = c.getSales();
				// logger.info(sales);
				if (!StringUtill.isNull(sales)) {
					// logger.info(sales);
					String sale[] = sales.split("_");
					for (int j = 0; j < sale.length; j++) {
						if (!StringUtill.isNull(sale[j])) {
							if (Integer.valueOf(sale[j]) == branch.getPid()) {
								li.add(c.getId() + "");
								continue;
							}
						}

					}
				}  
			}
		// logger.info(li);
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (li.contains(op.getCategoryID() + "")) {
					list.add(op.getType());
				}
			}
		}
		// logger.info(System.currentTimeMillis() - start);
		return list;
	} 
 
	public static List<String> getlistsale(Branch branch) {
		init();
		List<String> list = new ArrayList<String>();
		List<String> li = new ArrayList<String>();

		List<Category> listc = CategoryService.getList();
		// logger.info(listc);
			for (int i = 0; i < listc.size(); i++) {
				Category c = listc.get(i);
				String sales = c.getSales();
				// logger.info(sales);
				if (!StringUtill.isNull(sales)) {
					// logger.info(sales);
					String sale[] = sales.split("_");
					for (int j = 0; j < sale.length; j++) {
						if (!StringUtill.isNull(sale[j])) {
							if (Integer.valueOf(sale[j]) == branch.getPid()) {
								li.add(c.getId() + "");
								continue;
							}
						}

					}
				}  
			}
		// logger.info(li); 
		if (null != listall) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i);
				if (op.getStatues() == 0 && li.contains(op.getCategoryID() + "")) {
					list.add(op.getType());
				}
			} 
		}
		// logger.info(System.currentTimeMillis() - start);
		return list;
	}
	
	public static List<String> getlistsale(int branchtype) {
		init(); 
		List<String> list = new ArrayList<String>();
		List<String> li = new ArrayList<String>();
         
		List<Category> listc = CategoryService.getList();
		// logger.info(listc); 
			for (int i = 0; i < listc.size(); i++) {
				Category c = listc.get(i);
				String sales = c.getSales(); 
				  
				if (!StringUtill.isNull(sales)) {
					// logger.info(sales); 
					String sale[] = sales.split("_");
					for (int j = 0; j < sale.length; j++) {
						if (!StringUtill.isNull(sale[j])) { 
							
							/*logger.info(Integer.valueOf(sale[j])) ;
							logger.info( branchtype);     
							logger.info(Integer.valueOf(6) == 6);
							logger.info(Integer.valueOf(sale[j]) - branchtype);*/
							
							if (Integer.valueOf(sale[j]) == branchtype) {
								li.add(c.getId() + "");
								continue;
							}
						}
					}
				}   
			}     
		 logger.info(li); 
		if (null != getlistall()) {
			for (int i = 0; i < listall.size(); i++) {
				Product op = listall.get(i); 
				if (op.getStatues() == 0 && li.contains(op.getCategoryID() + "")) {
					list.add(op.getType());
				}
			}
		} 
    
		//logger.info(list);
		// logger.info(System.currentTimeMillis() - start);
		return list;
	}
	
	public static List<Integer> getlistallCid(Branch branch) {
		init();
		List<Integer> li = new ArrayList<Integer>();
 
		List<Category> listc = CategoryService.getList();
		//logger.info(listc);
		for (int i = 0; i < listc.size(); i++) {
			Category c = listc.get(i);
			String sales = c.getSales();
		//	logger.info(sales); 
			if (!StringUtill.isNull(sales)) {
			//	logger.info(sales);
			//	logger.info(branch.getPid()); 
				String sale[] = sales.split("_");
				for (int j = 0; j < sale.length; j++) {
					if (!StringUtill.isNull(sale[j])) {
						if (Integer.valueOf(sale[j]) == branch.getPid()) {
							li.add(c.getId());
							continue;
						}
					}

				}
			}
		}

		// logger.info(li);

		// logger.info(System.currentTimeMillis() - start);
		return li;
	}

	public static List<Integer> getlistallCid(Integer branchtype) {
		init();
		List<Integer> li = new ArrayList<Integer>();
 
		List<Category> listc = CategoryService.getList();
		//logger.info(listc);
		for (int i = 0; i < listc.size(); i++) {
			Category c = listc.get(i);
			String sales = c.getSales();
		//	logger.info(sales); 
			if (!StringUtill.isNull(sales)) {
			//	logger.info(sales);
			//	logger.info(branch.getPid()); 
				String sale[] = sales.split("_");
				for (int j = 0; j < sale.length; j++) {
					if (!StringUtill.isNull(sale[j])) { 
						if (Integer.valueOf(sale[j]) == branchtype) {
							li.add(c.getId());
							continue;
						}
					}

				}
			}
		}

		// logger.info(li);

		// logger.info(System.currentTimeMillis() - start);
		return li;
	}
	
	public static List<Product> getlistMatain() {
		List<Category> list = CategoryService.getlistmaintain();
		List<Product> listp = new ArrayList<Product>();
		for (int i = 0; i < listall.size(); i++) {
			if (null != list) {
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getId() == listall.get(i).getCategoryID()) {
						// System.out.println(list.get(j).getId());
						listp.add(listall.get(i));
						// System.out.println(StringUtill.GetJson(listall.get(i)));
					}
				}
			}

		}

		return listp;
	}

	public static Map<Integer, Product> getIDmap() {
		init();
		if (idmap == null) {
			idmap = ProductManager.getProductID();
		}
		return idmap;
	}

	public static void init() {
		if (flag) {
			typeNameList = ProductManager.getAllProductName();
			typeName = ProductManager.getProductName();
			idmap = ProductManager.getProductID();
			listall = ProductManager.getProductList();
		}
		flag = false;
	}
}
