package main;

import java.util.List;

import category.Category;
import category.CategoryManager;

import order.Order;
import order.OrderManager;
import user.User;
import user.UserManager;

public class MDmain {
	
   public static void main(String args[]){
	   List<Category> list = CategoryManager.getCategory();
	   System.out.println(list.size());
   }
   
   
   public void start(){
	   User user = new User();
	   //user.setName("lihen");
	   //user.setPassword("luhen");
	   //user.setType(2);
	   user.setProducts("1_2_3");
	   //UserManager.check(user.getName(), user.getPassword());
	   UserManager.save(user);
	    Order order = new Order();
	   // order.setProductCategory("彩电");
		order.setProductType("00xds");
		order.setDate("20140102");
		order.setTime("10:08-11:08");
		order.setUserMessage("liaowuhen");
		order.setMail("297655@qq.com");
		order.setLocate("tianjin");
		order.setLocateDetail("南开区");
		//order.setPhone("15620524734");
		//order.setLocalPhone("03586067455");
		OrderManager.save(user, order);
   }
}
