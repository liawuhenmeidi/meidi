package wilson.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import order.Order;
import order.OrderManager;
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
	
	public static boolean checkDBOrder(int dbOrderId){
		
		if(OrderManager.updateStatues("orderCharge",Order.query, String.valueOf(dbOrderId)) != 1){
				return true;
		} else{
			return false;
		}
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
	
	public static boolean checkOrder(int uploadOrderId,int dbOrderId){
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
			if(OrderManager.updateStatues("orderCharge",Order.query, String.valueOf(dbOrderId)) != 1){
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
	
	public static boolean checkOrder(String[] idString){
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
			}
			if(OrderManager.updateStatues("orderCharge",Order.query, dbIdString) != 1){
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
		sql = "insert into uploadorder (id,name,salesman,shop,posno,saletime,type,num,saleprice,filename,uploadtime,checked,checkedtime,checkorderid) VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?)";	
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
				pstmt.setString(9, uo.getFileName());
				pstmt.setString(10, fmt.format(new Date()));
				pstmt.setInt(11, uo.getChecked());
				pstmt.setString(12, uo.getCheckedTime());
				pstmt.setInt(13, uo.getCheckOrderId());
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
	
	private static boolean saveSalaryModelList(List<UploadSalaryModel> uploadSalaryModelList) {
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

	
	public static List<UploadOrder> getUnCheckedUploadOrders(){
		List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = 1";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo.setId(rs.getInt("id"));
				uo.setName(rs.getString("name"));
				uo.setSaleManName(rs.getString("salesman"));
				uo.setShop(rs.getString("shop"));
				uo.setPosNo(rs.getString("posno"));
				uo.setSaleTime(rs.getString("saletime"));
				uo.setType(rs.getString("type"));
				uo.setNum(rs.getInt("num"));
				uo.setSalePrice(rs.getDouble("saleprice"));
				uo.setFileName(rs.getString("filename"));
				uo.setChecked(rs.getInt("checked"));
				uo.setCheckedTime(rs.getString("checkedtime"));
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
		String sql = "select * from uploadorder where checked = 0";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
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
				uo.setId(rs.getInt("id"));
				uo.setName(rs.getString("name"));
				uo.setSaleManName(rs.getString("salesman"));
				uo.setShop(rs.getString("shop"));
				uo.setPosNo(rs.getString("posno"));
				uo.setSaleTime(rs.getString("saletime"));
				uo.setType(rs.getString("type"));
				uo.setNum(rs.getInt("num"));
				uo.setSalePrice(rs.getDouble("saleprice"));
				uo.setFileName(rs.getString("filename"));
				uo.setChecked(rs.getInt("checked"));
				uo.setCheckedTime(rs.getString("checkedtime"));
				uo.setCheckOrderId(rs.getInt("checkorderid"));
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
	
	public static List<UploadSalaryModel> getAllSalaryModel(){
		List<UploadSalaryModel> result = new ArrayList<UploadSalaryModel>();
		String sql = "select * from uploadsalarymodel where status != -1";
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
		String sql = "select * from uploadorder where name = '" + name + "'";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
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
		String sql = "select * from uploadorder where name = '" + name + "' and checked = 0";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
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
	
	public static List<UploadSalaryModel> getSalaryModelsByName(String name){
		List <UploadSalaryModel> result = new ArrayList<UploadSalaryModel>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadsalarymodel where name = '" + name + "' and status != -1";

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
	
	public static List<UploadOrder> getAllUploadOrders(){
		List<UploadOrder> result = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked != -1";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
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
	
}
