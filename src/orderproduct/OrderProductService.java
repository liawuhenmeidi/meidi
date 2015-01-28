package orderproduct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import order.Order;
import order.OrderManager;
import product.Product;
import product.ProductService;
import wilson.upload.UploadOrder;

public class OrderProductService {
	public static Map<Integer,List<OrderProduct>> OrPMap ;
	public static boolean flag = false ;
	 
	public static Map<Integer,List<OrderProduct>> getStaticOrderStatuesM(){
		init(); 
		if(OrPMap == null){
			OrPMap = OrderProductManager.getOrderStatuesM();
		}
		return OrPMap;
	}
	
	public static void init(){
		if(flag){
			OrPMap = OrderProductManager.getOrderStatuesM();
		}
		flag = false ;
	}
	
	//取Order中statues = 1的有多个个
	public static int getTotalNum(Order o){
		int result  = 0;
		OrderProduct op = new OrderProduct();
		List<OrderProduct> opList = o.getOrderproduct();
	
		for(int i = 0 ; i < opList.size() ; i ++){
			op = opList.get(i);
			if(op.getStatues() == 0){
				result ++;
			}
		}
		return result;
	}
	
	//Order o = > 123.2
	public static Double getTotalPrice(Order o){
		Double result  = 0.0;
		OrderProduct op = new OrderProduct();
		List<OrderProduct> opList = o.getOrderproduct();
	
		for(int i = 0 ; i < opList.size() ; i ++){
			op = opList.get(i);
			if(op.getStatues() == 0){
				Product p = ProductService.getIDmap().get(Integer.parseInt(op.getSendType()));
				double t =  p.getStockprice() * op.getCount();
				if(t == 0.0){
					return 0.0;
				}else{
					result += t;
				}
				
			}
		}
		return result;
	}
	
	public static List<OrderProduct> getOrderProductFromList(List<OrderProduct> input,int id){
		List<OrderProduct> result  = new ArrayList<OrderProduct>();
		if(input == null){
			return result;
		}
		for(int i = 0 ; i < input.size() ; i ++ ){
			if(input.get(i).getId() == id){
				result.add(input.get(i));
			}
		}
		return result;
	}
	
	//Order o   =>  67:1:123.2,381:1:155.0
	public static String getSendTypeAndCountAndPrice(Order o,UploadOrder uo) throws Exception{
		return getSendTypeAndCountAndPrice(o,uo,true);
	}
	
	//Order o   =>  MXG15-22:1:123.2,SS15T:2:155.0
	public static String getSendTypeAndCountAndPrice(Order o,UploadOrder uo,boolean id) throws Exception{
		if(!o.isDiangma()){
			return "";
		}
		OrderProduct op = new OrderProduct();
		List<OrderProduct> opList = new ArrayList<OrderProduct>();
		String result = "";
		opList = o.getOrderproduct();
		
		Double totalPrice_db = getTotalPrice(o);
		int num = getTotalNum(o);
		Double tmpPrice = 0.0;
		
		Product p = new Product();
		
		
		
		if(id){
			for(int i = 0 ; i < opList.size() ; i ++){
				op = opList.get(i);
				if(op.getStatues() == 0){
					p = ProductService.getIDmap().get(Integer.parseInt(op.getSendType()));
					if(totalPrice_db == 0){
						tmpPrice = (uo.getSalePrice()/num)/op.getCount();
					}else{
						tmpPrice = p.getStockprice() * uo.getSalePrice() * uo.getNum() / totalPrice_db;
					}
					
					result += op.getSendType() + ":" + op.getCount() + ":" + tmpPrice +  ",";
				}
			}
		}else{
			for(int i = 0 ; i < opList.size() ; i ++){
				op = opList.get(i);
				if(op.getStatues() == 0){
					p = ProductService.getIDmap().get(Integer.parseInt(op.getSendType()));
					if(totalPrice_db == 0){
						tmpPrice = (uo.getSalePrice()/num)/op.getCount();
					}else{
						tmpPrice = p.getStockprice() * uo.getSalePrice() * uo.getNum() / totalPrice_db;
					}
					result += p.getType() + ":" + op.getCount() + ":" + tmpPrice +  ",";
				}
			}
		}

		
		if(result.endsWith(",")){
			result = result.substring(0,result.length()-1);
		}
		return result;
		
	}
	
	//input   =>  1000,MXG15-22:1,SS15T:2
	public static String getPrice(String input){
		String result = "";
		try{
			String priceSTR = input.split(",")[0];
			Product p = new Product();
			
			Double uptotalPrice = Double.parseDouble(priceSTR);
			Double dbtotalPrice = 0.0;
			int dbtotalNum = 0;
			
			boolean isFind = true;
			for(int i = 1 ; i < input.split(",").length ; i ++){
				String type = input.split(",")[i].split(":")[0];
				int num = Integer.parseInt(input.split(",")[i].split(":")[1]);
				
				Double d = ProductService.gettypemap().get(type).getStockprice() * num ;
				if(d == 0.0){
					isFind = false;
				}
				dbtotalPrice += d ;
				dbtotalNum += num;
			}
			
			
			if(isFind){
				//算
				for(int i = 1 ; i < input.split(",").length ; i ++){
					String type = input.split(",")[i].split(":")[0];
					result += ProductService.gettypemap().get(type).getStockprice() / dbtotalPrice  *  uptotalPrice + ",";
				}
				
			}else{
				//均分
				for(int i = 1 ; i < input.split(",").length ; i ++){
					
					result += String.format("%.2f", uptotalPrice / dbtotalNum) + ",";
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
		return result;
	}
}
