package branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.SaleModel;

import branchtype.BranchTypeService;

public class BranchService {
	public static boolean flag = true;
	// public static Map<Integer,Branch> map = null;
	public static List<Branch> list = null;

	// public static Map<String,Branch> nameMap = null ;
	// public static List<String> listStr = null;

	public static Map<Integer, Branch> getMap() {
		init();
		Map<Integer, Branch> map = new HashMap<Integer, Branch>();

		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);
			map.put(b.getId(), b);
		}
		return map;
	}

	public static Map<String, Branch> getNameMap() {
		init();
		Map<String, Branch> map = new HashMap<String, Branch>();

		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);
			map.put(b.getLocateName(), b);
		}
		return map;
	}
 
	public static Map<String, Branch> getNameMap(int branchtypeid) {
		init();
		Map<String, Branch> map = new HashMap<String, Branch>();

		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);
			if(b.getPid() == branchtypeid){
				map.put(b.getLocateName(), b);
			}
			
		}
		return map;
	}
	
	public static Map<String, Branch> getAnotherNameMap() {
		init();
		Map<String, Branch> map = new HashMap<String, Branch>();
 
		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);
			map.put(b.getNameSN(), b);
		}
		return map; 
	}
	 
	public static Map<String, Branch> getNameSNMap() {
		init(); 
		Map<String, Branch> map = new HashMap<String, Branch>();
		
		List<Integer> li = BranchTypeService.getlistSaleModelID(SaleModel.SuNing); 
		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);  
			if(li.contains(b.getPid())){ 
				map.put(b.getNameSN(), b); 
			}
		}
	
		return map; 
	}
	
	public static Map<String, Branch> getNumMap() {
		init();
		Map<String, Branch> map = new HashMap<String, Branch>();

		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);
			map.put(b.getEncoded(), b);
		}

		return map;
	}   
   
	public static Map<String, Branch> getNumMap(int saletype) {
		init(); 
		Map<String, Branch> map = new HashMap<String, Branch>();
        List<Integer> li = BranchTypeService.getlistSaleModelID(saletype); 
       // System.out.println("branchservice"+li); 
		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i);
			if(li.contains(b.getPid())){
				map.put(b.getEncoded(), b);
			}
		}

		return map;
	}
	
	public static Map<String, List<String>> getPtypeMap() {
		init();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
  
		for (int i = 0; i < list.size(); i++) {
			Branch b = list.get(i); 
			List<String> list = map.get(b.getPid()+"");
			if(null == list){   
				list = new ArrayList<String>();   
				map.put(b.getPid()+"", list); 
			}
			list.add(b.getLocateName());
		}

		return map;
	}
	
	public static Branch gerBranchByname(String name) {
		return getNameMap().get(name);
	}

	public static List<Branch> getList() {
		init();
		if (list == null) {
			list = BranchManager.getLocate();
		}

		return list;
	}

	public static List<Branch> getList(int branchtype) {
		init();
		List<Branch> listb = null;
		if (null != list) {
			listb = new ArrayList<Branch>();
			for (int i = 0; i < list.size(); i++) {
				Branch b = list.get(i);
				if (branchtype == b.getPid()) {
					listb.add(b);
				}
			}
			// list = BranchManager.getLocate();
		}

		return listb;
	}

	public static List<Integer> getListids(int branchtype) {
		init();
		List<Integer> listb = null;
		if (null != list) {
			listb = new ArrayList<Integer>();
			for (int i = 0; i < list.size(); i++) {
				Branch b = list.get(i);
				// System.out.println(b.getPid());
				// System.out.println(branchtype);
				if (branchtype == b.getPid()) {
					// System.out.println(b.getId());
					listb.add(b.getId());
				}
			}
			// list = BranchManager.getLocate();
		}

		return listb;
	}

	public static List<String> getListStr() {
		List<String> listb = null; 
		if (null != list) {
			listb = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				Branch b = list.get(i);
				if ( 0 == b.getDisable() ) {
					listb.add(b.getLocateName());
				} 
			}
			// list = BranchManager.getLocate();
		}  
		return listb ;
	} 
  
	public static void init() {
		if (flag) {
			list = BranchManager.getLocate();
		}
		flag = false;
	}
}
