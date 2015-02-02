package wilson.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import change.BranchTypeChange;
import change.ChangeManager;

import order.Order;
import order.OrderManager;
import orderPrint.OrderPrintlnService;
import orderproduct.OrderProduct;
import orderproduct.OrderProductService;
import product.ProductService;
import uploadtotal.UploadTotal;
import uploadtotalgroup.UploadTotalGroup;
import uploadtotalgroup.UploadTotalGroupManager;
import user.User;
import utill.BasicUtill;
import utill.StringUtill;
import wilson.matchOrder.AfterMatchOrder;

import database.DB;

public class UploadManager {
	private static XLSReader xlsreader = new XLSReader();
	protected static Log logger = LogFactory.getLog(UploadManager.class);
	
	public static boolean saveSalaryFileToDB(String path,String fileName){
		List <UploadSalaryModel> uploadSalaryModelList = new ArrayList<UploadSalaryModel>();
		uploadSalaryModelList = xlsreader.readSalaryModelXLS(path, fileName);
		if(saveSalaryModelList(uploadSalaryModelList)){
			logger.info("上传提成标准保存成功");
			return true;
		}else{
			logger.info("上传提成标准保存失败");
			return false;
		}
	}
	
	public static boolean saveSalesFileToDB(String path,String fileName){
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrders = xlsreader.readSalesXLS(path, fileName);
		if(saveOrderList(UploadOrders)){
			logger.info("上传销售单保存成功");
			return true;
		}else{
			logger.info("上传销售单保存失败");
			return false;
		}
	}
	
	public static boolean savechangeFileToDB(String path,String fileName){
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		BranchTypeChange bt = xlsreader.readchangeXML(path,fileName);
		ChangeManager.save(bt);  
		if(saveOrderList(UploadOrders)){
			logger.info("上传销售单保存成功");
			return true;
		}else{
			logger.info("上传销售单保存失败");
			return false;
		}
	}
	
	public static boolean saveSuningFileToDB(String path,String fileName){
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrders = xlsreader.readSuningXLS(path, fileName);
		if(saveOrderList(UploadOrders)){
			logger.info("上传订单保存成功");
			return true;
		}else{
			logger.info("上传订单保存失败");
			return false;
		}
	}
	
	public static boolean checkDBOrder(User user ,int dbOrderId,String checkName){
		
		if(OrderManager.updateStatues(user,"orderCharge",checkName, String.valueOf(dbOrderId)) != 1){
				return true;
		} else{
			return false;
		}
	}
	
	//更改uploadorder店名(仅提供给manualCheckout.jsp使用)
	//input :     id,name;id,name
	public static boolean transferShopName(String input){
		boolean result = false;
		
		if(StringUtill.isNull(input)){
			return true;
		}
		
		if(input.endsWith("_")){
			input = input.substring(0,input.length()-1);
		}
		
		Connection conn = DB.getConn();
		String sql = "update uploadorder set shop = ? where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for(int i = 0 ; i < input.split("_").length; i ++){
				pstmt.setString(1, input.split("_")[i].split(",")[1]);
				pstmt.setInt(2, Integer.parseInt(input.split("_")[i].split(",")[0]));
				pstmt.executeUpdate();
			}	
			
			
			conn.commit();
			conn.setAutoCommit(autoCommit);
			result = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("修改店名异常，回滚！");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("回滚失败！！！请注意");
				e1.printStackTrace();
			}
			result =false;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return result;
	}
	
	//更改uploadorder送货型号(仅提供给manualCheckout.jsp使用)
		//input :     id,orderId_id,orderId
		public static boolean transferType(User user,String input){
			boolean result = false;
			
			if(StringUtill.isNull(input)){
				return true;
			}
			
			if(input.endsWith("_")){
				input = input.substring(0,input.length()-1);
			}
			
			
			//取id_orderId对应的order (开始)
			String db_ids = "";
			String up_ids = "";
			Map<String,Order> orderMap = new HashMap<String, Order>();
			
			Map<String,UploadOrder> uploadOrderMap = new HashMap<String, UploadOrder>();
			Map<String,String> output = new HashMap<String, String>();
			
			for(int i = 0 ; i < input.split("_").length ; i ++){
				db_ids += input.split("_")[i].split(",")[1] + ",";
				up_ids += input.split("_")[i].split(",")[0] + ",";
			}
			if(db_ids.endsWith(",")){
				db_ids = db_ids.substring(0,db_ids.length() - 1);
			}
			if(up_ids.endsWith(",")){
				up_ids = up_ids.substring(0,up_ids.length() - 1);
			}
			
			uploadOrderMap = UploadManager.getUploadOrderMapByIds(up_ids);
			orderMap = OrderManager.getOrdermapByIds(user, db_ids);
			//(结束)
			
			
			
			Connection conn = DB.getConn();
			String sql = "update uploadorder set salesman = ? where id = ?";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				boolean autoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);
				for(int i = 0 ; i < input.split("_").length; i ++){
					String orderid = input.split("_")[i].split(",")[1];
					String uploadorderid = input.split("_")[i].split(",")[0];
					
					
					
					Order o = new Order();
					UploadOrder uo = new UploadOrder();
					o = orderMap.get(orderid);
					uo = uploadOrderMap.get(uploadorderid);
					if(null == o || null == uo){
						throw new SQLException();
					}
					
					pstmt.setString(1, OrderProductService.getSendTypeAndCountAndPrice(o,uo));
					pstmt.setInt(2, Integer.parseInt(uploadorderid));
					pstmt.executeUpdate();
					
					
				}	
				
				
				conn.commit();
				conn.setAutoCommit(autoCommit);
				result = true ;
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.info("修改送货型号异常，回滚！");
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.info("回滚失败！！！请注意");
					e1.printStackTrace();
				}
				result =false;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("修改送货型号异常，回滚！");
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.info("回滚失败！！！请注意");
					e1.printStackTrace();
				}
				result =false;
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
			return result;
		}
		
	//更改uploadorder送货型号(仅提供给manualCheckout.jsp使用)
	//input :     id,orderId_id,orderId
	public static boolean transferType(User user,String input,HttpServletRequest request){
		boolean result = false;
		
		//先处理session中的
		List<OrderProduct> type_transList = (List<OrderProduct>)request.getSession().getAttribute("type_transList");
		if(type_transList == null){
			type_transList = new ArrayList<OrderProduct>();
		}
		String idsInSession = "";
		Map<String,List<OrderProduct>> idMapInSession = new HashMap<String, List<OrderProduct>>();
		for(int i = 0 ; i < type_transList.size() ; i ++){
			List<OrderProduct> tmp = idMapInSession.get(String.valueOf(type_transList.get(i).getId()));
			if(tmp == null){
				tmp = new ArrayList<OrderProduct>();
				idMapInSession.put(String.valueOf(type_transList.get(i).getId()),tmp);
			}
			tmp.add(type_transList.get(i));
		}
		request.getSession().setAttribute("type_transList", null);
				
		
		
		Connection conn = DB.getConn();
		String sql = "update uploadorder set salesman = ? where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);

		try {
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			Iterator<String> it = idMapInSession.keySet().iterator();
			
			while(it.hasNext()){
				String key = it.next();
				
				List<OrderProduct> tmp = new ArrayList<OrderProduct>();
				tmp = idMapInSession.get(key);
				
				String out = "";
				for(int j = 0 ; j < tmp.size() ; j ++){
					//MXG15-22:1:123.2,SS15T:2:155.0
					
					out += ProductService.gettypemap().get(tmp.get(j).getTypeName()).getId() + ":";
					out += tmp.get(j).getCount() + ":";
					out += tmp.get(j).getPrice() + ",";
				}
				if(out.endsWith(",")){
					out = out.substring(0,out.length() -1);
				}
				
				pstmt.setString(1, out);
				pstmt.setInt(2, Integer.parseInt(key));
				pstmt.executeUpdate();
			}
				
			
			
			conn.commit();
			conn.setAutoCommit(autoCommit);

			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改送货型号异常，回滚！");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("回滚失败！！！请注意");
				e1.printStackTrace();
			}
			result =false;
		} 
		
		
		
		if(StringUtill.isNull(input)){
			return true;
		}
		
		if(input.endsWith("_")){
			input = input.substring(0,input.length()-1);
		}
		
		
		

		//取id_orderId对应的order (开始)
		String db_ids = "";
		String up_ids = "";
		Map<String,Order> orderMap = new HashMap<String, Order>();
		
		Map<String,UploadOrder> uploadOrderMap = new HashMap<String, UploadOrder>();
		Map<String,String> output = new HashMap<String, String>();
		
		for(int i = 0 ; i < input.split("_").length ; i ++){
			db_ids += input.split("_")[i].split(",")[1] + ",";
			up_ids += input.split("_")[i].split(",")[0] + ",";
		}
		if(db_ids.endsWith(",")){
			db_ids = db_ids.substring(0,db_ids.length() - 1);
		}
		if(up_ids.endsWith(",")){
			up_ids = up_ids.substring(0,up_ids.length() - 1);
		}
		
		uploadOrderMap = UploadManager.getUploadOrderMapByIds(up_ids);
		orderMap = OrderManager.getOrdermapByIds(user, db_ids);
		//(结束)
		
		

		try {
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for(int i = 0 ; i < input.split("_").length; i ++){
				String orderid = input.split("_")[i].split(",")[1];
				String uploadorderid = input.split("_")[i].split(",")[0];
				
				if(idMapInSession.containsKey(uploadorderid)){
					continue;
				}
				
				Order o = new Order();
				UploadOrder uo = new UploadOrder();
				o = orderMap.get(orderid);
				uo = uploadOrderMap.get(uploadorderid);
				if(null == o || null == uo){
					throw new SQLException();
				}
				
				if(uploadorderid.equals("61511")){
					System.out.println(1);
				}
				pstmt.setString(1, OrderProductService.getSendTypeAndCountAndPrice(o,uo,true,true));
				pstmt.setInt(2, Integer.parseInt(uploadorderid));
				pstmt.executeUpdate();
				
				
			}	
			
			
			conn.commit();
			conn.setAutoCommit(autoCommit);
			result = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			logger.info("修改送货型号异常，回滚！");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("回滚失败！！！请注意");
				e1.printStackTrace();
			}
			result =false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改送货型号异常，回滚！");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("回滚失败！！！请注意");
				e1.printStackTrace();
			}
			result =false;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return result;
	}
	
	public static boolean checkUploadOrder(int uploadOrderId){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? ,checkedtime = ? ,checkorderid= ? where id = " + uploadOrderId;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			pstmt.setInt(1,0);
			pstmt.setString(2, fmt.format(new Date()));
			pstmt.setInt(3, -1);
			pstmt.executeUpdate();
			
			flag = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
		
	}
	
	public static boolean checkDBOrderStrList(User user,String dbOrderIdStrList,String checkName){
		
		if(OrderManager.updateStatues(user ,"orderCharge",checkName, dbOrderIdStrList) == 1){
				return true;
		} else{
			return false;
		}
	}
	
	public static boolean confirmDBOrderStrList(User user,String dbOrderIdStrList,String statues){

		if(OrderManager.updateStatues(user ,"orderGo",statues, dbOrderIdStrList) == 1){
				return true;
		} else{
			return false;
		}
	}
	
	public static boolean confirmUploadOrderStrList(String uploadOrderIdStrList){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? where id in (" + uploadOrderIdStrList + ")" ;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {

			pstmt.setInt(1,UploadOrder.COMFIRMED);
			pstmt.executeUpdate();
			logger.info(sql);
			flag = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
		
	}
	
	public static boolean checkUploadOrderStrList(String uploadOrderIdStrList){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? ,checkedtime = ? ,checkorderid= ? where id in (" + uploadOrderIdStrList + ")" ;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			pstmt.setInt(1,0);
			pstmt.setString(2, fmt.format(new Date()));
			pstmt.setInt(3, -1);
			pstmt.executeUpdate();
			logger.info(sql);
			flag = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
		
	}
	
	public static boolean checkOrder(User user ,int uploadOrderId,int dbOrderId,String checkName){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? ,checkedtime = ? ,checkorderid= ? where id = " + uploadOrderId;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);  // 事务开始  
			pstmt.setInt(1,0);
			pstmt.setString(2, fmt.format(new Date()));
			pstmt.setInt(3, dbOrderId);
			pstmt.executeUpdate();
			if(OrderManager.updateStatues(user,"orderCharge",checkName, String.valueOf(dbOrderId)) != 1){
				throw new SQLException();
			}
			conn.commit();   // 提交给数据库处理   
			conn.setAutoCommit(autocommit);
			flag = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.info("roll back失败!");
			}
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
		
	} 
	
	public static boolean checkOrder(User user,String[] idString,String checkName){
		//接受{"1,2","2,3"}类型的ID输入,前一个是DB的Order，后一个是upload的Order哦~
		if(idString == null || idString.length <1){
			return false;
		}
		
		int DBOrderID = 0;
		int UploadOrderID = 0;
		boolean flag = false;
		Connection conn = DB.getConn();
		
		List<String> uploadIds = new ArrayList<String>();
		List<String> dbIds = new ArrayList<String>();
		
		String dbIdString = "";
		
		for(int i = 0 ; i < idString.length ; i++){
			dbIds.add(idString[i].split(",")[0]);
			uploadIds.add(idString[i].split(",")[1]);
			dbIdString+=idString[i].split(",")[0] + ",";
		}
		dbIdString = dbIdString.substring(0,dbIdString.length()-1);
		
		
		String sql = "update uploadorder set checked = ? ,checkedtime = ?,checkorderid=? where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			
			logger.info("自动匹配开始");
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);  // 事务开始  
			for(int i = 0 ; i < idString.length ; i ++){
				DBOrderID = Integer.parseInt(dbIds.get(i));
				UploadOrderID = Integer.parseInt(uploadIds.get(i));
				pstmt.setInt(1,0);
				pstmt.setString(2, fmt.format(new Date()));
				pstmt.setInt(3,DBOrderID);
				pstmt.setInt(4, UploadOrderID);
				pstmt.executeUpdate();
				logger.info(sql);
			}
			if(OrderManager.updateStatues(user,"orderCharge",checkName, dbIdString) != 1){
				throw new SQLException();
			}
			conn.commit();
			conn.setAutoCommit(autocommit);
			flag = true ;
			logger.info("自动匹配结束");
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("roll back失败!");
				e1.printStackTrace();
			}
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	
	public static boolean saveOrderList(List <UploadOrder> UploadOrders){
		String sql = ""; 
		sql = "insert into uploadorder (id,name,salesman,shop,posno,saletime,type,num,saleprice,backpoint,filename,uploadtime,checked,checkedtime,checkorderid) VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		UploadOrder uo = new UploadOrder();
		
		try {
			for(int i = 0 ; i < UploadOrders.size() ; i ++ ){
				uo = UploadOrders.get(i);
				pstmt.setString(1, uo.getName());
				pstmt.setString(2, uo.getSaleManName());
				pstmt.setString(3, uo.getShop());
				pstmt.setString(4, uo.getPosNo());
				pstmt.setString(5, uo.getSaleTime());
				pstmt.setString(6, uo.getType());
				pstmt.setInt(7, uo.getNum());
				pstmt.setDouble(8, uo.getSalePrice());
				pstmt.setDouble(9, uo.getBackPoint());
				pstmt.setString(10, uo.getFileName());
				pstmt.setString(11, fmt.format(new Date()));
				pstmt.setInt(12, uo.getChecked());
				pstmt.setString(13, uo.getCheckedTime());
				pstmt.setInt(14, uo.getCheckOrderId());
				pstmt.executeUpdate();
				uo = new UploadOrder();
			}		
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return true;
	}
	
	public static boolean saveUploadOrder(UploadOrder uploadOrder){
		boolean result = false;
		String sql = ""; 
		sql = "update uploadorder set shop=?,posno=?,saletime=?,type=?,num=?,saleprice=? where id = " + uploadOrder.getId();	
		Connection conn = DB.getConn();
		
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
				pstmt.setString(1, uploadOrder.getShop());
				pstmt.setString(2, uploadOrder.getPosNo());
				pstmt.setString(3, uploadOrder.getSaleTime());
				pstmt.setString(4, uploadOrder.getType());
				pstmt.setInt(5, uploadOrder.getNum());
				pstmt.setDouble(6, uploadOrder.getSalePrice());
				pstmt.executeUpdate();
				result = true;
				logger.info(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return result;
	}
	
	public static boolean saveSalaryModelList(List<UploadSalaryModel> uploadSalaryModelList) {
		boolean flag = false;
		String sql = "insert into uploadsalarymodel (id,shop,name,starttime,endtime,catergory,type,content,committime,filename,status) VALUES (null,?,?,?,?,?,?,?,?,?,0)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		UploadSalaryModel usm = new UploadSalaryModel();
		
		try {
			for(int i = 0 ; i < uploadSalaryModelList.size() ; i ++ ){
				usm = uploadSalaryModelList.get(i);
				pstmt.setString(1, usm.getShop());
				pstmt.setString(2, usm.getName());
				pstmt.setString(3, usm.getStartTime());
				pstmt.setString(4, usm.getEndTime());
				pstmt.setString(5, usm.getCatergory());
				pstmt.setString(6, usm.getType());
				pstmt.setString(7, usm.getContent());
				pstmt.setString(8, fmt.format(new Date()));
				pstmt.setString(9, usm.getFileName());
				pstmt.executeUpdate();
				usm = new UploadSalaryModel();
			}		
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag;
	}
	
	public static boolean saveSalaryModel(UploadSalaryModel uploadSalary) {
		boolean flag = false;
		
		//id为-1时，新增
		if(uploadSalary.getId() == -1){
			String sql = "insert into uploadsalarymodel VALUES (null,?,?,?,?,?,?,?,?,?,0)";	
			Connection conn = DB.getConn();
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1, uploadSalary.getShop());
				pstmt.setString(2, uploadSalary.getName());
				pstmt.setString(3, uploadSalary.getStartTime());
				pstmt.setString(4, uploadSalary.getEndTime());
				pstmt.setString(5, uploadSalary.getCatergory());
				pstmt.setString(6, uploadSalary.getType());
				pstmt.setString(7, uploadSalary.getContent());
				pstmt.setString(8, fmt.format(new Date()));
				pstmt.setString(9, uploadSalary.getFileName());
				pstmt.executeUpdate();
				uploadSalary = new UploadSalaryModel();
						
				flag = true;
			} catch (SQLException e) {
				e.printStackTrace();

			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		}else{
		//id不为-1的时候，为修改
			String sql = "update uploadsalarymodel set shop=?,name=?,starttime=?,endtime=?,catergory=?,type=?,content=?,committime=?,filename=?,status=? where id = " + uploadSalary.getId();	
			Connection conn = DB.getConn();

			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1, uploadSalary.getShop());
				pstmt.setString(2, uploadSalary.getName());
				pstmt.setString(3, uploadSalary.getStartTime());
				pstmt.setString(4, uploadSalary.getEndTime());
				pstmt.setString(5, uploadSalary.getCatergory());
				pstmt.setString(6, uploadSalary.getType());
				pstmt.setString(7, uploadSalary.getContent());
				pstmt.setString(8, uploadSalary.getCommitTime());
				pstmt.setString(9, uploadSalary.getFileName());
				pstmt.setInt(10, uploadSalary.getStatus());
				pstmt.executeUpdate();		
				flag = true;
			} catch (SQLException e) {
				e.printStackTrace();

			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
			
		}
		
		return flag;
	}

	public static UploadOrder getUploadOrderFromRS(ResultSet rs) throws SQLException{
		UploadOrder uo = new UploadOrder(); 
		
		uo.setId(rs.getInt("id"));
		uo.setName(rs.getString("name"));
		uo.setSaleManName(rs.getString("salesman"));
		uo.setShop(rs.getString("shop"));
		uo.setPosNo(rs.getString("posno"));
		if(uo.getPosNo()==null||uo.getPosNo().equals("")){
			uo.setPosNo("");
		}
		uo.setSaleTime(rs.getString("saletime"));
		uo.setType(rs.getString("type"));
		uo.setNum(rs.getInt("num"));
		uo.setSalePrice(rs.getDouble("saleprice"));
		uo.setFileName(rs.getString("filename"));
		uo.setChecked(rs.getInt("checked"));
		uo.setCheckedTime(rs.getString("checkedtime"));
		uo.setCheckOrderId(rs.getInt("checkorderid"));
		uo.setBackPoint(rs.getDouble("backpoint"));
		return uo;
		
	}
	public static List<String> getUnconfirmedUploadOrderNames(){
		List <String> unConfirmedUploadOrderNames = new ArrayList<String>();

		Connection conn = DB.getConn(); 
		String sql = "select DISTINCT name from uploadorder where checked = " + UploadOrder.COMPARE_DEFAULT;

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		String tmp = "";
		try {     
			while (rs.next()) {
				tmp = "";
				tmp = rs.getString("name");
				unConfirmedUploadOrderNames.add(tmp);
			}
			/**
			 * 
			//
			sql = "select DISTINCT name from uploadorder where checked = " + UploadOrder.CALCED + " or checked = " + UploadOrder.CHECKED;
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				tmp = "";
				tmp = rs.getString("name");
				unConfirmedUploadOrderNames.remove(tmp);
			}
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return unConfirmedUploadOrderNames;
	}
	
	public static List<UploadOrder> getUnCheckedUploadOrders(){
		List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = " + UploadOrder.UNCHECK +  " or checked = " + UploadOrder.COMFIRMED + " order by shop";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				unCheckedUploadOrders.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return unCheckedUploadOrders;
	}
	
	public static List<UploadOrder> getCheckedUploadOrders(){
		List <UploadOrder> checkedUploadOrders = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = 0 order by shop";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				checkedUploadOrders.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return checkedUploadOrders;
	}
	
	public static List<String> getCalcedUploadOrdersNames(){
		List <String> result = new ArrayList<String>();

		Connection conn = DB.getConn(); 
		String sql = "select distinct name from uploadorder where checked = 2 order by shop";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		String tmp = "";
		try {     
			while (rs.next()) {
				tmp = rs.getString("name");
				result.add(tmp);
				tmp = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<String> getCheckedUploadOrdersNames(){
		List <String> result = new ArrayList<String>();

		Connection conn = DB.getConn(); 
		String sql = "select distinct name from uploadorder where checked = 0 order by shop";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		String tmp = "";
		try {     
			while (rs.next()) {
				tmp = rs.getString("name");
				result.add(tmp);
				tmp = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	 
	public static List<String> getUnTotalUploadOrdersNames(){
		List <String> result = new ArrayList<String>();

		Connection conn = DB.getConn(); 
		String sql = "select distinct name from uploadorder where dealtime is null order by shop";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		String tmp = "";
		try {     
			while (rs.next()) {
				tmp = rs.getString("name");
				result.add(tmp);
				tmp = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<UploadOrder> getTotalUploadOrders(String id){
		List <UploadOrder> checkedUploadOrders = new ArrayList<UploadOrder>();
		Connection conn = DB.getConn();  
		String str = "";
		if(isBackUped(id)){
			str = "  and  checked =  5";
		}else { 
			str = "  and  checked !=  5";
		}    
		 
		String sql = "select * from uploadorder where dealtime is null and name = '"+id+"' "+str+" order by shop";
logger.info(sql); 
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				checkedUploadOrders.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return checkedUploadOrders;
	}
	
	public static List<UploadOrder> getTotalUploadOrders(String id,String statues){
		List <UploadOrder> checkedUploadOrders = new ArrayList<UploadOrder>();
 
		Connection conn = DB.getConn();
		String search = "";
		if(!StringUtill.isNull(statues)){
			if(Integer.valueOf(statues) == 1){
				search = " and checked in (1,3) ";
			}else if(Integer.valueOf(statues) == 0){ 
				search = " and checked in (0,2) "; 
			}
		}  
		String str = "";
		if(isBackUped(id)){
			str = "  and  checked =  5";
		}else {
			str = "  and  checked !=  5"; 
		}     
		String sql = "select * from uploadorder where dealtime is null and name = '"+id+"' "+str + search+" order by shop";
   logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				checkedUploadOrders.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return checkedUploadOrders;
	}
	
	public static Map<String,Map<String,List<UploadTotal>>> getTotalOrdersGroup(String id,int totaltype,String checked){
		Map<String,Map<String,List<UploadTotal>>> map = new HashMap<String,Map<String,List<UploadTotal>>>();
		Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
		
		String message = "";
		UploadTotalGroup upt = UploadTotalGroupManager.getUploadTotalGroup();
		if(upt != null){
		   message = upt.getCategoryname();
		}
//int count = 0 ;		
		
		Map<String, HashMap<String, UploadTotal>> maps = UploadManager.getTotalOrdersShop(id,totaltype,checked);
		
		Set<Map.Entry<String, HashMap<String, UploadTotal>>> setmap = maps.entrySet();
		Iterator<Map.Entry<String, HashMap<String, UploadTotal>>> itmap = setmap.iterator();
		while(itmap.hasNext()){
			Map.Entry<String, HashMap<String, UploadTotal>> enmap = itmap.next();
			HashMap<String, UploadTotal> maptype = enmap.getValue();
			Set<Map.Entry<String, UploadTotal>> setmaptype =  maptype.entrySet();
			Iterator<Map.Entry<String, UploadTotal>> itmaptype = setmaptype.iterator();
			while(itmaptype.hasNext()){
				Map.Entry<String, UploadTotal> enmaptype = itmaptype.next();
				UploadTotal up = enmaptype.getValue();
//count += up.getCount();		
				String tpe = ""; 
				if(null != mapus){
					UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
					if(null != ups){
						tpe = ups.getCatergory(); 
					}
				}
				
				if(!StringUtill.isNull(message)){
					JSONObject jsObj = JSONObject.fromObject(message);
					Iterator<String> it = jsObj.keys();
					 
					while(it.hasNext()){ 
						String type = it.next();
						if(type.contains(tpe)){
							tpe = type ;
						}
					}
				}

				Map<String,List<UploadTotal>> mapll = map.get(up.getBranchname());
				
				if(null == mapll){
					mapll = new HashMap<String,List<UploadTotal>>();
					map.put(up.getBranchname(), mapll);
				}
				
				List<UploadTotal> list = mapll.get(tpe);
				if(null == list){
					list = new ArrayList<UploadTotal>();
					mapll.put(tpe, list);
				}			 
				list.add(up);			
			}
		}
//logger.info(count); 		
		return map;
		
		
	}
	
	public static Map<String,Map<String,List<UploadTotal>>> getTotalOrdersCategoryGroup(String id,int totaltype,String checked){
		Map<String,Map<String,List<UploadTotal>>> map = new HashMap<String,Map<String,List<UploadTotal>>>();
		Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
		
		String message = "";
		UploadTotalGroup upt = UploadTotalGroupManager.getUploadTotalGroup();
		if(upt != null){
		   message = upt.getCategoryname();
		}
		
		
		Map<String, HashMap<String, UploadTotal>> maps = UploadManager.getTotalOrdersCategory(id,totaltype,checked );
		Set<Map.Entry<String, HashMap<String, UploadTotal>>> setmap = maps.entrySet();
		Iterator<Map.Entry<String, HashMap<String, UploadTotal>>> itmap = setmap.iterator();
		while(itmap.hasNext()){
			Map.Entry<String, HashMap<String, UploadTotal>> enmap = itmap.next();
			String tp = enmap.getKey();
			HashMap<String, UploadTotal> maptype = enmap.getValue();
			Set<Map.Entry<String, UploadTotal>> setmaptype =  maptype.entrySet();
			Iterator<Map.Entry<String, UploadTotal>> itmaptype = setmaptype.iterator();
			while(itmaptype.hasNext()){
				Map.Entry<String, UploadTotal> enmaptype = itmaptype.next();
				UploadTotal up = enmaptype.getValue();
int count = 0 ;
				String tpe = ""; 
				if(null != mapus){
					UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(tp));
					if(null != ups){
						tpe = ups.getCatergory(); 
					}
				}
				
				if(!StringUtill.isNull(message)){
					JSONObject jsObj = JSONObject.fromObject(message);
					Iterator<String> it = jsObj.keys();
					 
					while(it.hasNext()){ 
						String type = it.next();
						if(type.contains(tpe)){
							tpe = type ;
						}
					}
				}
				
				Map<String,List<UploadTotal>> mapll = map.get(tpe);
				if(null == mapll){
					mapll = new HashMap<String,List<UploadTotal>>();
					map.put(tpe, mapll);
				}
				
				List<UploadTotal> list = mapll.get(up.getBranchname());
				if(null == list){
					list = new ArrayList<UploadTotal>();
					mapll.put(up.getBranchname(), list);
				}			 
				list.add(up);
				
			}	
		}
		
		return map;
		
		
	} 
	 
	public static Map<String, HashMap<String, UploadTotal>> getTotalOrdersShop(String id,int totaltype,String checked){
		List<UploadOrder> list = UploadManager.getTotalUploadOrders(id,checked);
		//Map<String,UploadSalaryModel> listSM = getSalaryModelsAll();  
		Map<String, HashMap<String, UploadTotal>> map = new HashMap<String,HashMap<String,UploadTotal>>();
		if(null != list && list.size() >0){
			for(int i=0;i<list.size();i++){
				UploadOrder up = list.get(i);
				HashMap<String, UploadTotal> branchname= map.get(up.getShop());
				if(null == branchname){
					branchname = new HashMap<String, UploadTotal>();
					map.put(up.getShop(), branchname);
				} 
			    String relatype = "";
			    if(BasicUtill.sale == totaltype){
			    	relatype = up.getType();
			    	UploadTotal upt = branchname.get(relatype);
					if(null == upt){
						upt = new UploadTotal();
						upt.setBranchname(up.getShop());
						upt.setCount(up.getNum());
						upt.setName(up.getName());
						upt.setType(relatype);
						upt.setTotalcount(Math.abs(up.getSalePrice())*up.getNum());
						upt.setTatalbreakcount(Math.abs(up.getSalePrice())*up.getNum()*(1-up.getBackPoint()/100));
						branchname.put(relatype,upt); 
					}else {
						upt.setCount(upt.getCount()+up.getNum());
						upt.setTotalcount(upt.getTotalcount()+Math.abs(up.getSalePrice())*up.getNum());  
						upt.setTatalbreakcount(upt.getTatalbreakcount()+Math.abs(up.getSalePrice())*up.getNum()*(1-up.getBackPoint()/100));
					} 
			    }else if(BasicUtill.send == totaltype){
			    	String sendtypestr = up.getSendType(); 
					 String[] sendtypestrs = sendtypestr.split(",");
					 for(int j=0;j<sendtypestrs.length;j++){
						 String sendtype = sendtypestrs[j];
						// System.out.println(sendtypestrs[j]);
						 String[] sendtypes = sendtype.split(":");
						 relatype = sendtypes[0];  
						 int realcount = Integer.valueOf(sendtypes[1]);
						 Double prince = Math.abs(Double.valueOf(sendtypes[2]));
					     UploadTotal upt = branchname.get(relatype);
							if(null == upt){
								upt = new UploadTotal();
								upt.setBranchname(up.getShop());
								upt.setCount(up.getNum());
								upt.setName(up.getName());
								upt.setType(relatype);
								upt.setTotalcount(prince*realcount);
								upt.setTatalbreakcount(prince*realcount*(1-up.getBackPoint()/100));
								branchname.put(relatype,upt); 
							}else {
								upt.setCount(upt.getCount()+realcount);
								upt.setTotalcount(upt.getTotalcount()+prince*realcount);  
								upt.setTatalbreakcount(upt.getTatalbreakcount()+prince*realcount*(1-up.getBackPoint()/100));
							}
							
							
					 }
			    }
				
				  
			}
		}
		return map;
	}
	 
	public static Map<String, HashMap<String, UploadTotal>> getTotalOrdersCategory(String id,int totaltype){
		List<UploadOrder> list = UploadManager.getTotalUploadOrders(id);
		//logger.info(list.size());
		//Map<String,UploadSalaryModel> listSM = getSalaryModelsAll();  
		Map<String, HashMap<String, UploadTotal>> map = new HashMap<String,HashMap<String,UploadTotal>>();
		if(null != list && list.size() >0){
			for(int i=0;i<list.size();i++){
				UploadOrder up = list.get(i);
				String relatype = "";
			    if(BasicUtill.sale == totaltype){
			    	relatype = up.getType();
			    }else if(BasicUtill.send == totaltype){
			    	relatype = up.getTypeForCalc();
			    }
				HashMap<String, UploadTotal> branchname= map.get(relatype);
				if(null == branchname){
					branchname = new HashMap<String, UploadTotal>();
					map.put(up.getType(), branchname);
				}
				
				UploadTotal upt = branchname.get(up.getShop());
				if(null == upt){
					upt = new UploadTotal();
					upt.setBranchname(up.getShop());
					upt.setCount(up.getNum());
					upt.setName(up.getName());
					upt.setType(relatype);
					upt.setTotalcount(up.getSalePrice());
					upt.setTatalbreakcount(up.getSalePrice()*(1-up.getBackPoint()/100));
					branchname.put(up.getShop(),upt);  
				}else {
					upt.setCount(upt.getCount()+up.getNum());
					upt.setTotalcount(upt.getTotalcount()+up.getSalePrice());  
					upt.setTatalbreakcount(upt.getTatalbreakcount()+up.getSalePrice()*(1-up.getBackPoint()/100));
				}
				  
			}
		}
		return map;
	}
	
	public static Map<String, HashMap<String, UploadTotal>> getTotalOrdersCategory(String id,int totaltype,String checked){
		List<UploadOrder> list = UploadManager.getTotalUploadOrders(id,checked);
		//logger.info(list.size());
		//Map<String,UploadSalaryModel> listSM = getSalaryModelsAll();  
		Map<String, HashMap<String, UploadTotal>> map = new HashMap<String,HashMap<String,UploadTotal>>();
		if(null != list && list.size() >0){
			for(int i=0;i<list.size();i++){
				UploadOrder up = list.get(i);
				String relatype = "";
			    if(BasicUtill.sale == totaltype){
			    	relatype = up.getType();
			    	HashMap<String, UploadTotal> branchname= map.get(relatype);
					if(null == branchname){
						branchname = new HashMap<String, UploadTotal>();
						map.put(relatype, branchname);
					}
					 
					UploadTotal upt = branchname.get(up.getShop());
					if(null == upt){
						upt = new UploadTotal();
						upt.setBranchname(up.getShop());
						upt.setCount(up.getNum());
						upt.setName(up.getName());
						upt.setType(relatype);
						upt.setTotalcount(Math.abs(up.getSalePrice())*up.getNum());
						upt.setTatalbreakcount(Math.abs(up.getSalePrice())*up.getNum()*(1-up.getBackPoint()/100));
						branchname.put(up.getShop(),upt);
					}else {
						upt.setCount(upt.getCount()+up.getNum());
						upt.setTotalcount(upt.getTotalcount()+Math.abs(up.getSalePrice())*up.getNum());  
						upt.setTatalbreakcount(upt.getTatalbreakcount()+Math.abs(up.getSalePrice())*up.getNum()*(1-up.getBackPoint()/100));
					}
					
			    }else if(BasicUtill.send == totaltype){
			    	 String sendtypestr = up.getSendType(); 
					 String[] sendtypestrs = sendtypestr.split(",");
					 for(int j=0;j<sendtypestrs.length;j++){
						 String sendtype = sendtypestrs[j]; 
						// System.out.println(sendtypestrs[j]);
						 String[] sendtypes = sendtype.split(":");
						 relatype = sendtypes[0];  
						 int realcount = Integer.valueOf(sendtypes[1]);
						 Double prince = Math.abs(Double.valueOf(sendtypes[2]));
						 
						 HashMap<String, UploadTotal> branchname= map.get(relatype);
							if(null == branchname){
								branchname = new HashMap<String, UploadTotal>();
								map.put(relatype, branchname);
							}
							
							UploadTotal upt = branchname.get(up.getShop());
							if(null == upt){
								upt = new UploadTotal();
								upt.setBranchname(up.getShop());
								upt.setCount(realcount);
								upt.setName(up.getName()); 
								upt.setType(relatype);
								upt.setTotalcount(prince*realcount);
								upt.setTatalbreakcount(prince*realcount*(1-up.getBackPoint()/100));
								branchname.put(up.getShop(),upt);  
							}else { 
								upt.setCount(upt.getCount()+realcount);
								upt.setTotalcount(upt.getTotalcount()+prince*realcount);  
								upt.setTatalbreakcount(upt.getTatalbreakcount()+prince*realcount*(1-up.getBackPoint()/100));
							}
							
							
					 }
					 
			    	
					 
					
			    }
  
			}
		}
		return map;
	}
	public static HashMap<String, List<UploadTotal>> getTotalOrdersGroup(String id,String type,int totaltype,String checked){
		HashMap<String, List<UploadTotal>> map = new HashMap<String, List<UploadTotal>>();
        Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
		
		String message = "";
		UploadTotalGroup upt = UploadTotalGroupManager.getUploadTotalGroup();
		if(upt != null){
		   message = upt.getCategoryname();
		}
		
		HashMap<String, UploadTotal> maptypeinit = UploadManager.getTotalOrders(id,"type",totaltype,checked);

		Set<Map.Entry<String, UploadTotal>> setmaiinit = maptypeinit.entrySet();
		Iterator<Map.Entry<String, UploadTotal>> itmapinit = setmaiinit.iterator();
		while(itmapinit.hasNext()){
			Map.Entry<String, UploadTotal> en = itmapinit.next();
			UploadTotal up = en.getValue();

			String tpe = ""; 
			if(null != mapus){
				UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
				if(null != ups){
					tpe = ups.getCatergory(); 
				}
			}
			
			if(!StringUtill.isNull(message)){
				JSONObject jsObj = JSONObject.fromObject(message);
				Iterator<String> it = jsObj.keys();
				  
				while(it.hasNext()){  
					String types = it.next();
					if(types.contains(tpe)){
						tpe = types ;
					}
				}
			}
			
			List<UploadTotal> list = map.get(tpe);
			if(null == list){
				list = new ArrayList<UploadTotal>();
				map.put(tpe, list);
			}		 	 
			list.add(up);		
			
		}
			
		
		return map;
	}
	
	public static HashMap<String, UploadTotal> getTotalOrders(String id,String type,int totaltype,String checked){
		List<UploadOrder> list = UploadManager.getTotalUploadOrders(id,checked);
		HashMap<String, UploadTotal> map = new HashMap<String,UploadTotal>(); 
		if(null != list && list.size() >0){
			for(int i=0;i<list.size();i++){
				UploadOrder up = list.get(i);
				String relatype = "";
			    if(BasicUtill.sale == totaltype){
			    	relatype = up.getType();
			    	UploadTotal upt = map.get(relatype);
					if(null == upt){
						upt = new UploadTotal(); 
						upt.setBranchname(up.getShop());
						upt.setCount(up.getNum());
						upt.setName(up.getName());
						upt.setType(relatype); 
						upt.setTotalcount(Math.abs(up.getSalePrice())*up.getNum());
						upt.setTatalbreakcount(Math.abs(up.getSalePrice())*up.getNum()*(1-up.getBackPoint()/100));
						map.put(relatype,upt); 
					}else {
						upt.setCount(upt.getCount()+up.getNum());
						upt.setTotalcount(upt.getTotalcount()+Math.abs(up.getSalePrice())*up.getNum());
						upt.setTatalbreakcount(upt.getTatalbreakcount()+Math.abs(up.getSalePrice())*up.getNum()*(1-up.getBackPoint()/100));
					}
					
			    }else if(BasicUtill.send == totaltype){
			    	 String sendtypestr = up.getSendType(); 
					 String[] sendtypestrs = sendtypestr.split(",");
					 for(int j=0;j<sendtypestrs.length;j++){
						 String sendtype = sendtypestrs[j]; 
						// System.out.println(sendtypestrs[j]);
						 String[] sendtypes = sendtype.split(":");
						 relatype = sendtypes[0];  
						 int realcount = Integer.valueOf(sendtypes[1]); 
						 Double prince = Math.abs(Double.valueOf(sendtypes[2]));
			             
						 UploadTotal upt = map.get(relatype);
							if(null == upt){
								upt = new UploadTotal(); 
								upt.setBranchname(up.getShop());
								upt.setCount(realcount);
								upt.setName(up.getName());
								upt.setType(relatype); 
								upt.setTotalcount(prince*realcount);
								upt.setTatalbreakcount(prince*realcount*(1-up.getBackPoint()/100));
								map.put(relatype,upt); 
							}else { 
								upt.setCount(upt.getCount()+realcount);
								upt.setTotalcount(upt.getTotalcount()+prince*realcount);
								upt.setTatalbreakcount(upt.getTatalbreakcount()+prince*realcount*(1-up.getBackPoint()/100));
							}
							
					 }
			    	
					
			    }
		
			}
		}
		
		return map;
	}
	
	public static UploadOrder getUploadOrderById(int id){
		UploadOrder result = new UploadOrder();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where id = " + id;

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result = uo	;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static Map<String,UploadOrder> getUploadOrderMapByIds(String ids){
		Map<String,UploadOrder> result = new HashMap<String,UploadOrder>();
		
		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where id in ( " + ids + " )";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.put(String.valueOf(uo.getId()), uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<AfterMatchOrder> getCheckedAfterMatchOrder(Date startDate,Date endDate){
		List <AfterMatchOrder> checkedAfterMatchOrder = new ArrayList<AfterMatchOrder>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = 0 and saletime > " + sdf.format(startDate)  + " and saletime < " + sdf.format(endDate);

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		AfterMatchOrder amo = new AfterMatchOrder();
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				amo.initUploadSideOrder(uo);
				checkedAfterMatchOrder.add(amo);
				uo = new UploadOrder();
				amo = new AfterMatchOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		checkedAfterMatchOrder = initOrder(checkedAfterMatchOrder);
		return checkedAfterMatchOrder;
	}
	
	public static List<AfterMatchOrder> initOrder(List<AfterMatchOrder> lists){

		List<Order> ordersFromDB = OrderManager.getCheckedDBOrders();
		for(int i = 0 ; i < lists.size() ; i ++){
			for(int j = 0 ; j < ordersFromDB.size() ; j ++){
				if(lists.get(i).getUploadSideOrderId() == ordersFromDB.get(j).getId()){
					lists.get(i).initDBSideOrder(ordersFromDB.get(j));
					break;
				}
			}
		}
		return lists;
	}
	
	public static List<UploadSalaryModel> getAllSalaryModel(){
		List<UploadSalaryModel> result = new ArrayList<UploadSalaryModel>();
		String sql = "select * from uploadsalarymodel where status != " + UploadSalaryModel.DELETE;
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadSalaryModel usm = new UploadSalaryModel();
		try {
			while(rs.next()){
				usm.setId(rs.getInt("id"));
				usm.setShop(rs.getString("shop"));
				usm.setName(rs.getString("name"));
				usm.setStartTime(rs.getString("starttime"));
				usm.setEndTime(rs.getString("endtime"));
				usm.setCatergory(rs.getString("catergory"));
				usm.setType(rs.getString("type"));
				usm.setContent(rs.getString("content"));
				usm.setCommitTime(rs.getString("committime"));
				usm.setFileName(rs.getString("filename"));
				usm.setStatus(rs.getInt("status"));
				result.add(usm);
				usm = new UploadSalaryModel();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result.clear();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
	
	//传入salaryModels的list对象，取出名称返回
	public static List<String> getAllSalaryModelNames(List<UploadSalaryModel> salaryModels){
		List<String> result = new ArrayList<String>();
		if(salaryModels == null || salaryModels.size() == 0){
			return result;
		}
		for(int i = 0 ; i < salaryModels.size() ; i ++){
			if(salaryModels.get(i) == null){
				continue;
			}
			if(!result.contains(salaryModels.get(i).getName())){
				result.add(salaryModels.get(i).getName());
			}
		}
		return result;
	}
	
	public static List<String> getAllSalaryModelNames(){
		List<String> result = new ArrayList<String>();
		String sql = "select distinct name from uploadsalarymodel where status != " + UploadSalaryModel.DELETE;
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		String tmp = "";
		try {
			while(rs.next()){
				tmp = rs.getString("name");
				result.add(tmp);
				tmp = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result.clear();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
	
	//传入uploadorders的list对象，取出名称返回
	public static List<String> getAllUploadOrderNames(List<UploadOrder> uploadorders){
		List<String> result = new ArrayList<String>();
		if(uploadorders == null || uploadorders.size() == 0){
			return result;
		}
		for(int i = 0 ; i < uploadorders.size() ; i ++){
			if(uploadorders.get(i) == null){
				continue;
			}
			if(!result.contains(uploadorders.get(i).getName())){
				result.add(uploadorders.get(i).getName());
			}
		}
		return result;
	}
	
	public static List<UploadOrder> getOrdersByName(String name){
		List <UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where name = '" + name + "' and checked != " + UploadOrder.ORIGIN  +" order by id";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	public static List<UploadOrder> getUnComfirmedUploadOrdersByName(String name){
		List <UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = " + UploadOrder.COMPARE_DEFAULT + " and name = '" + name + "' order by shop" ;
		logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<UploadOrder> getUnCheckedUploadOrdersByName(String name){
		List <UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where (checked = " + UploadOrder.UNCHECK + " or checked = " + UploadOrder.COMFIRMED + " )and name = '" + name + "' order by shop" ;
		logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<UploadOrder> getCheckedUploadOrdersByName(String name){
		List <UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = 0 and name = '" + name + "' order by shop" ;
		logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	
	public static List<UploadOrder> getCheckedOrdersByName(String name){
		List <UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where name = '" + name + "' and checked = 0 order by name,shop";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static Map<String,UploadSalaryModel> getSalaryModelsAll(){
		Map<String,UploadSalaryModel> result = new HashMap<String,UploadSalaryModel>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadsalarymodel where  status != " + UploadSalaryModel.DELETE + " order by name";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {     
			while (rs.next()) {
				UploadSalaryModel usm = new UploadSalaryModel();
				usm.setId(rs.getInt("id"));
				usm.setShop(rs.getString("shop"));
				usm.setName(rs.getString("name"));
				usm.setStartTime(rs.getString("starttime"));
				usm.setEndTime(rs.getString("endtime"));
				usm.setCatergory(rs.getString("catergory"));
				usm.setType(rs.getString("type"));
				usm.setContent(rs.getString("content"));
				usm.setCommitTime(rs.getString("committime"));
				usm.setFileName(rs.getString("filename"));
				usm.setStatus(rs.getInt("status"));
				result.put(StringUtill.getStringNocn(usm.getType()), usm); 
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<String> getSalaryModelsCategoryAll(){
		List<String> list = new ArrayList<String>();

		Connection conn = DB.getConn(); 
		String sql = "select catergory from uploadsalarymodel where  status != " + UploadSalaryModel.DELETE + " group by catergory";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {     
			while (rs.next()) {
				list.add(rs.getString("catergory"));	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return list;
	}
	
	public static List<UploadSalaryModel> getSalaryModelsByName(String name){
		List <UploadSalaryModel> result = new ArrayList<UploadSalaryModel>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadsalarymodel where name = '" + name + "' and status != " + UploadSalaryModel.DELETE + " order by name";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadSalaryModel usm = new UploadSalaryModel();
		try {     
			while (rs.next()) {
				usm.setId(rs.getInt("id"));
				usm.setShop(rs.getString("shop"));
				usm.setName(rs.getString("name"));
				usm.setStartTime(rs.getString("starttime"));
				usm.setEndTime(rs.getString("endtime"));
				usm.setCatergory(rs.getString("catergory"));
				usm.setType(rs.getString("type"));
				usm.setContent(rs.getString("content"));
				usm.setCommitTime(rs.getString("committime"));
				usm.setFileName(rs.getString("filename"));
				usm.setStatus(rs.getInt("status"));
				result.add(usm);
				usm = new UploadSalaryModel();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<UploadSalaryModel> getSalaryModelsByName(List<String> names){
		List <UploadSalaryModel> result = new ArrayList<UploadSalaryModel>();
		if(names.size()<= 0){
			return result;
		}
		
		String nameSTR = "";
		for(int i = 0 ; i < names.size() ; i ++){
			nameSTR += "\""+ names.get(i) + "\",";
		}
		if(nameSTR.endsWith(",")){
			nameSTR = nameSTR.substring(0,nameSTR.length() -1);
		}
		

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadsalarymodel where name in (" + nameSTR + ") and status != " +  UploadSalaryModel.DELETE +  "  order by name";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadSalaryModel usm = new UploadSalaryModel();
		try {     
			while (rs.next()) {
				usm.setId(rs.getInt("id"));
				usm.setShop(rs.getString("shop"));
				usm.setName(rs.getString("name"));
				usm.setStartTime(rs.getString("starttime"));
				usm.setEndTime(rs.getString("endtime"));
				usm.setCatergory(rs.getString("catergory"));
				usm.setType(rs.getString("type"));
				usm.setContent(rs.getString("content"));
				usm.setCommitTime(rs.getString("committime"));
				usm.setFileName(rs.getString("filename"));
				usm.setStatus(rs.getInt("status"));
				result.add(usm);
				usm = new UploadSalaryModel();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static UploadSalaryModel getSalaryModelsById(int id){
		UploadSalaryModel result = new UploadSalaryModel();
		
		Connection conn = DB.getConn(); 
		String sql = "select * from uploadsalarymodel where id = " + id  +" and status != -1";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {     
			while (rs.next()) {
				result.setId(rs.getInt("id"));
				result.setShop(rs.getString("shop"));
				result.setName(rs.getString("name"));
				result.setStartTime(rs.getString("starttime"));
				result.setEndTime(rs.getString("endtime"));
				result.setCatergory(rs.getString("catergory"));
				result.setType(rs.getString("type"));
				result.setContent(rs.getString("content"));
				result.setCommitTime(rs.getString("committime"));
				result.setFileName(rs.getString("filename"));
				result.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<String> getOrderNamesByStatus(int under_status){
		List<String> result = new ArrayList<String>();
		
		List<String> temp = new ArrayList<String>();
		Connection conn = DB.getConn(); 
		String sql = "select DISTINCT name from uploadorder where checked = " + under_status;
		
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {     
			while (rs.next()) {
				temp.add(rs.getString("name"));
			}
			
			String names = "";
			for(int i = 0 ; i < temp.size() ; i ++){
				names += "\"" + temp.get(i) + "\"" + ",";
			}
			if(names.endsWith(",")){
				names = names.substring(0,names.length() -1);
			}
			
			
			sql = "select DISTINCT name from uploadorder where name not in (" + names + ")";
			if(StringUtill.isNull(names)){
				sql = "select DISTINCT name from uploadorder";
			}
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				result.add(rs.getString("name"));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	
	
	public static List<UploadOrder> getAllUploadOrders(){
		List<UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked != -1";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo = getUploadOrderFromRS(rs);
				result.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	public static boolean deleteDeprecateSalaryModels(){
		boolean result = false;
		Connection conn = DB.getConn();
		ResultSet rs = null;
		Statement stmt = null;
		String ids = "";
		try {
			logger.info("清理过期uploadsalarymodel开始");

			String sql = "select uploadsalarymodelid from salaryresult ";
			stmt = DB.getStatement(conn);
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				ids += rs.getInt("uploadsalarymodelid") + ",";
			}
			if(ids.length() <= 0){
				return true;
			}
			
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			
			
			sql = "delete from uploadsalarymodel where status = " + UploadSalaryModel.DELETE  + "  and id not in (" + ids + ")";
			stmt.execute(sql);
			
			result = true ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result ;  
	}
	public static boolean deleteUploadOrderByName(String name){
		
		//加trasaction 删除salaryResult里面记录
		boolean flag = false;
		Connection conn = DB.getConn();
		ResultSet rs = null;
		Statement stmt = null;
		String ids = "";
		
		try {
			logger.info("删除uploadOrder事务开始");
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			String sql = "select id from uploadorder where name = '" + name + "'";
			stmt = DB.getStatement(conn);
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				ids += rs.getInt("id") + ",";
			}
			if(ids.length() <= 0){
				return flag;
			}
			
			ids = ids.substring(0,ids.length()-1);
			
			sql = "delete from uploadorder where id in (" + ids + ")";
			stmt.execute(sql);
			sql = "delete from salaryresult where uploadorderid in (" + ids + ")";
			stmt.execute(sql);
			
			conn.commit();
			conn.setAutoCommit(autoCommit);
			
			flag = true ;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("删除uploadOrder事务失败");
			try {
				logger.info("删除uploadOrder事务回滚");
				conn.rollback();
				logger.info("删除uploadOrder事务回滚成功");
			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.info("删除uploadOrder事务回滚失败!!!");
			}
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		logger.info("删除uploadOrder事务成功");
		return flag ;  
	}
	public static boolean deleteSalaryModelByName(String name){
		boolean flag = false;
		Connection conn = DB.getConn();
		
		String sql = "update uploadsalarymodel set name = '',status =  " + UploadSalaryModel.DELETE  + " where name = '" + name + "'";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		Statement stmt = DB.getStatement(conn);
		try {
			stmt.execute(sql);
			flag = true ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	public static boolean deleteSalaryModelById(int id){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "delete from uploadsalarymodel  where id = " + id;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		Statement stmt = DB.getStatement(conn);
		try {
			stmt.execute(sql);
			flag = true ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	public static boolean deprecatedUploadOrderByName(String name){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = -1 where name = '" + name + "'";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		Statement stmt = DB.getStatement(conn);
		try {
			stmt.execute(sql);
			flag = true ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	public static boolean deprecatedSalaryModelByName(String name){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadsalarymodel set status = -1 where name = '" + name + "'";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		Statement stmt = DB.getStatement(conn);
		try {
			stmt.execute(sql);
			flag = true ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	public static boolean deleteDeprecatedItems(){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "delete from uploadsalarymodel where status = -1 ";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		Statement stmt = DB.getStatement(conn);
		try {
			stmt.execute(sql);
			sql = "delete from uploadorder where checked = -1 ";
			stmt.execute(sql);
			flag = true ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ; 
	}
	
	public static boolean isUploaderFileNameExist(String fileName){
		boolean result = true;
		Connection conn = DB.getConn();
		String sql = "select count(*) from uploadorder where name = '" + fileName + "'";
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
			int temp = 0 ;
			while(rs.next()){
				temp = Integer.parseInt(rs.getString("count(*)"));
			}
			if(temp == 0 ){
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result ; 
	}
	
	public static boolean isSalaryModelFileNameExist(String fileName){
		boolean result = true;
		Connection conn = DB.getConn();
		String sql = "select count(*) from uploadsalarymodel where name = '" + fileName + "'";
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
			int temp = 0 ;
			while(rs.next()){
				temp = Integer.parseInt(rs.getString("count(*)"));
			}
			if(temp == 0 ){
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result ; 
	}
	
	public static boolean isSalesOrder(List<UploadOrder> list){
		boolean result = true;
		for(int i = 0 ; i < list.size() ; i ++){
			if(!StringUtill.isNull(list.get(i).getPosNo())){
				if(list.get(i).getPosNo().length() > 0){
					result = false;
					break;
				}
			}
		}
		return result;
	}

	public static List<String> getShopNameListFromFileName(String filename) {
		List<String> result = new ArrayList<String>();
		Connection conn = DB.getConn();
		String sql = "select distinct shop from uploadorder where name = '" + filename + "'";
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
			String tmp = "";
			while(rs.next()){
				tmp = rs.getString("shop");
				if(!StringUtill.isNull(tmp) && !result.contains(tmp)){
					result.add(tmp);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result; 
	}
	
	public static List<UploadOrder> initMultiUploadOrder(List<UploadOrder> input){
		List<UploadOrder> result = new ArrayList<UploadOrder>();
		for(int i = 0 ;  i< input.size() ; i ++){
			String content = input.get(i).getSaleManName();
			if(!content.equals("")){	
				for(int j = 0 ; j<content.split(",").length ; j++){
					UploadOrder uo = new UploadOrder(input.get(i));
					String type = content.split(",")[j].split(":")[0];
					type = ProductService.getIDmap().get(Integer.parseInt(type)).getType();
					String num = content.split(",")[j].split(":")[1];
					String salePrice = content.split(",")[j].split(":")[2];
					
					uo.setSaleManName("");
					uo.setType(type);
					uo.setNum(Integer.parseInt(num));
					uo.setSalePrice(Double.parseDouble(salePrice));
					uo.setCheckOrderId(UploadOrder.CHECK_ORDER_ID_SEPARATE);
					
					result.add(uo);
				}
			}else{
				result.add(input.get(i));
			}
		}
		return result;
	}
	
	public static boolean isBackUped(String name){
		boolean result = false;
		Connection conn = DB.getConn();
		//暂时先这样。。。囧
		String sql = "";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try{
			sql = "select count(*) from uploadorder where name = '" + name + "' and checked = '" + UploadOrder.ORIGIN + "'";
			pstmt = DB.prepare(conn, sql);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			int num = rs.getInt(1);
			if(num > 0){
				result = true;
			}else{
				result = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return result;
		}
		return result;
		
	}

	public static List<UploadSalaryModel> getSalaryModelList(Date startDate ,Date endDate) {
		List<UploadSalaryModel> result = new ArrayList<UploadSalaryModel>();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select * from uploadsalarymodel where ( status = 0 and starttime >= " + fmt.format(startDate)  + " and endtime <= " + fmt.format(endDate) + " ) " +
				"or ( status = 0 and starttime < " + fmt.format(endDate)  + " and endtime > " + fmt.format(endDate) + " ) " +
						"or ( status = 0 and starttime < " + fmt.format(startDate)  + " and endtime > " + fmt.format(startDate) + " ) ";
		System.out.print("sql");
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadSalaryModel usm = new UploadSalaryModel();
		try {
			while(rs.next()){
				usm.setName(rs.getString("name"));
				usm.setShop(rs.getString("shop"));
				usm.setId(rs.getInt("id"));
				usm.setStartTime(rs.getString("starttime"));
				usm.setEndTime(rs.getString("endtime"));
				usm.setCatergory(rs.getString("catergory"));
				usm.setType(rs.getString("type"));
				usm.setContent(rs.getString("content"));
				usm.setCommitTime(rs.getString("committime"));
				usm.setFileName(rs.getString("filename"));
				usm.setStatus(rs.getInt("status"));
				result.add(usm);
				usm = new UploadSalaryModel();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
}
