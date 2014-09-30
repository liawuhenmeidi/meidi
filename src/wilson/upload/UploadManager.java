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

import order.Order;
import order.OrderManager;

import database.DB;

public class UploadManager {
	private static XLSReader xlsreader = new XLSReader();
	
	public static boolean saveSalaryFileToDB(String path,String fileName){
		List <UploadSalaryModel> uploadSalaryModelList = new ArrayList<UploadSalaryModel>();
		uploadSalaryModelList = xlsreader.readSalaryModelXLS(path, fileName);
		if(saveSalaryModelList(uploadSalaryModelList)){
			System.out.println("上传提成标准保存成功");
			return true;
		}else{
			System.out.println("上传提成标准保存失败");
			return false;
		}
	}
	
	public static boolean saveSuningFileToDB(String path,String fileName){
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrders = xlsreader.readSuningXLS(path, fileName);
		if(saveOrderList(UploadOrders)){
			System.out.println("上传订单保存成功");
			return true;
		}else{
			System.out.println("上传订单保存失败");
			return false;
		}
	}
	
	public static boolean checkOrder(int uploadOrderId,int dbOrderId){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? ,checkedtime = ? ,checkorderid= ? where id = " + uploadOrderId;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
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
				System.out.println("roll back失败!");
			}
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
		
	}
	
	public static boolean checkOrder(String[] idString){
		int DBOrderID = 0;
		int UploadOrderID = 0;
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? ,checkedtime = ?,checkorderid=? where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		try {
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);  // 事务开始  
			for(int i = 0 ; i < idString.length ; i ++){
				DBOrderID = Integer.parseInt(idString[i].split(",")[0]);
				UploadOrderID = Integer.parseInt(idString[i].split(",")[1]);
				pstmt.setInt(1,0);
				pstmt.setString(2, fmt.format(new Date()));
				pstmt.setInt(3,DBOrderID);
				pstmt.setInt(4, UploadOrderID);
				pstmt.executeUpdate();
				if(OrderManager.updateStatues("orderCharge",Order.query, String.valueOf(DBOrderID)) != 1){
					throw new SQLException();
				}
			}
			conn.commit();
			conn.setAutoCommit(autocommit);
			flag = true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				System.out.println("roll back失败!");
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
		sql = "insert ignore into uploadorder (id, shop,saleno,posno,saletime,dealtime,type,num,saleprice,backpoint,filename,uploadtime,checked,checkedtime,checkorderid) VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,null,null)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		UploadOrder uo = new UploadOrder();
		
		try {
			for(int i = 0 ; i < UploadOrders.size() ; i ++ ){
				uo = UploadOrders.get(i);
				pstmt.setString(1, uo.getShop());
				pstmt.setString(2, uo.getSaleNo());
				pstmt.setString(3, uo.getPosNo());
				pstmt.setString(4, uo.getSaleTime());
				pstmt.setString(5, uo.getDealTime());
				pstmt.setString(6, uo.getType());
				pstmt.setInt(7, uo.getNum());
				pstmt.setDouble(8, uo.getSalePrice());
				pstmt.setDouble(9, uo.getBackPoint());
				pstmt.setString(10, uo.getFileName());
				pstmt.setString(11, fmt.format(new Date()));
				pstmt.setInt(12, uo.getChecked());
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
		String sql = "insert into uploadsalarymodel (id, name,starttime,endtime,catergory,type,content,committime,filename,status) VALUES (null,?,?,?,?,?,?,?,?,0)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		UploadSalaryModel usm = new UploadSalaryModel();
		
		try {
			for(int i = 0 ; i < uploadSalaryModelList.size() ; i ++ ){
				usm = uploadSalaryModelList.get(i);
				pstmt.setString(1, usm.getName());
				pstmt.setString(2, usm.getStartTime());
				pstmt.setString(3, usm.getEndTime());
				pstmt.setString(4, usm.getCatergory());
				pstmt.setString(5, usm.getType());
				pstmt.setString(6, usm.getContent());
				pstmt.setString(7, fmt.format(new Date()));
				pstmt.setString(8, usm.getFileName());
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
				uo.setShop(rs.getString("shop"));
				uo.setSaleNo(rs.getString("saleno"));
				uo.setPosNo(rs.getString("posno"));
				uo.setSaleTime(rs.getString("saletime"));
				uo.setDealTime(rs.getString("dealtime"));
				uo.setType(rs.getString("type"));
				uo.setNum(rs.getInt("num"));
				uo.setSalePrice(rs.getDouble("saleprice"));
				uo.setBackPoint(rs.getDouble("backpoint"));
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
}
