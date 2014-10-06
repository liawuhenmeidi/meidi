package inventory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.ProductService;

import user.User;
import database.DB;

public class InventoryMessageManager {
	protected static Log logger = LogFactory.getLog(InventoryMessageManager.class);
	   
	public static List<String> save(int id, Inventory inve) { 
         
		   List<InventoryMessage> orders = inve.getInventory() ;
		   
		   List<String> sqls = new ArrayList<String>();
           
			 for(int i=0;i<orders.size();i++){ 
 
				InventoryMessage order = orders.get(i); 
				String sql = "insert into  inventoryMessage (id, productId ,categoryId,count, inventoryId)" + 
	                         "  values ( null, '"+order.getProductId()+"', '"+order.getCategoryId()+"',"+order.getCount()+", "+id+")";
				logger.info(sql);   
				sqls.add(sql);  
			} 
			 return sqls; 
	   }
	
	
	public static List<InventoryMessage> getInventoryID(User user ,int id){
		List<InventoryMessage> listm = new ArrayList<InventoryMessage>(); 
		       String sql = "";    
			   sql = "select * from  inventoryMessage where inventoryId = "+ id ;
	logger.info(sql);  
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {     
					while (rs.next()) {  
						InventoryMessage orders = getCategoryFromRs(rs); 
						listm.add(orders); 
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally { 
					DB.close(stmt);
					DB.close(rs); 
					DB.close(conn);
				 }
				return listm; 
		 }
	 
	
	public static String delete(int id) {
		String sql = "delete from inventoryMessage where inventoryId = " + id;
        return sql ;
	}
	
	
	
	
	
	private static InventoryMessage getCategoryFromRs(ResultSet rs){
		InventoryMessage c = new InventoryMessage();
		try {    
			c.setId(rs.getInt("id")); 
			c.setCount(rs.getInt("count"));  
			c.setInventoryId(rs.getInt("inventoryId"));
			c.setProductId(rs.getString("productId")); 
			c.setCategoryId(rs.getInt("categoryId"));     
			c.setProductname(ProductService.getIDmap().get(Integer.valueOf(c.getProductId())).getType()); 
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		return c ;
	}
	
	
}
