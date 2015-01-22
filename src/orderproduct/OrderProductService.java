package orderproduct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import order.Order;
import order.OrderManager;
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
	
	
	//Order o   =>  67:1:123.2,381:1:155.0
	public static String getSendTypeAndCountAndPrice(Order o,UploadOrder uo){
		return getSendTypeAndCountAndPrice(o,uo,true);
	}
	
	//Order o   =>  MXG15-22:1:123.2,SS15T:2:155.0
	public static String getSendTypeAndCountAndPrice(Order o,UploadOrder uo,boolean id){
		OrderProduct op = new OrderProduct();
		List<OrderProduct> opList = new ArrayList<OrderProduct>();
		String result = "";
		opList = o.getOrderproduct();
		
		if(id){
			for(int i = 0 ; i < opList.size() ; i ++){
				op = opList.get(i);
				if(op.getStatues() == 1){
					op.g
					result += op.getSaleType() + ":" + op.getCount() + ",";
				}
			}
		}else{
			for(int i = 0 ; i < opList.size() ; i ++){
				op = opList.get(i);
				if(op.getStatues() == 1){
					result += ProductService.getIDmap().get(Integer.parseInt(op.getSaleType())).getType() + ":" + op.getCount() + ",";
				}
			}
		}

		
		if(result.endsWith(",")){
			result = result.substring(0,result.length()-1);
		}
		return result;
		
	}
}
